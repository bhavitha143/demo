package com.bh.realtrack.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bh.realtrack.dao.IBillingPortfolioDAO;
import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.ActualCurveDTO;
import com.bh.realtrack.dto.BaselineCurveDTO;
import com.bh.realtrack.dto.BillingCycleDTO;
import com.bh.realtrack.dto.BillingCycleRemarksDTO;
import com.bh.realtrack.dto.BillingKpiDTO;
import com.bh.realtrack.dto.BillingLinearityChartDTO;
import com.bh.realtrack.dto.BillingLinearityChartPopupDTO;
import com.bh.realtrack.dto.BillingMilestoneListDTO;
import com.bh.realtrack.dto.BillingProjectListDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.ExceptionCategoryDTO;
import com.bh.realtrack.dto.ExceptionDTO;
import com.bh.realtrack.dto.ExceptionRemarksDTO;
import com.bh.realtrack.dto.FileUploadStatusDTO;
import com.bh.realtrack.dto.ForecastCurveDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.PercentageDTO;
import com.bh.realtrack.dto.SegmentSummaryDTO;
import com.bh.realtrack.dto.TPSBillingOutOfRTProjectDetailsDTO;
import com.bh.realtrack.dto.ViewInvoiceDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.BillingPortfolioConstants;

@Repository
public class BillingPortfolioDAOImpl implements IBillingPortfolioDAO {

	private static final Logger log = LoggerFactory.getLogger(BillingPortfolioDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	BillingDAOImpl billingDAOImpl;

	@SuppressWarnings("deprecation")
	@Override
	public List<String> getPmLeaderDropDown(String projectId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_PM_LEADER_LIST, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						String name = "";
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							name = rs.getString(1);
							if (null != name) {
								list.add(name);
							}
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> getSpmDropDown(String projectId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_SPM_LIST, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						String name = "";
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							name = rs.getString(1);
							if (null != name) {
								list.add(name);
							}
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> getFinancialSegmentDropDown(String projectId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_FINANCIAL_SEGMENT_LIST,
				new Object[] { projectId, projectId }, new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						String name = "";
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							name = rs.getString(1);
							if (null != name) {
								list.add(name);
							}
						}
						return list;
					}
				});
	}

	@Override
	public Map<String, Object> getBillingDashOverAllSummary(String projectId, String startDate, String endDate,
			String business) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("financialBlcount", "No data found");
		responseMap.put("forecastCount", "No data found");
		responseMap.put("actualCount", "No data found");
		responseMap.put("variationCount", "No data found");
		responseMap.put("financialITOBlcount", "No data found");
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_OVERALL_SUMMARY_LIST);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				responseMap.put("financialBlcount",
						null != rs.getString("O_FINANCIAL_COUNT") ? rs.getString("O_FINANCIAL_COUNT") : "0");
				responseMap.put("forecastCount",
						null != rs.getString("O_FORECAST_COUNT") ? rs.getString("O_FORECAST_COUNT") : "0");
				responseMap.put("actualCount",
						null != rs.getString("O_ACTUAL_COUNT") ? rs.getString("O_ACTUAL_COUNT") : "0");
				responseMap.put("variationCount",
						null != rs.getString("O_VARIATION_COUNT") ? rs.getString("O_VARIATION_COUNT") : "0");
				responseMap.put("financialITOBlcount",
						null != rs.getString("O_ITO_BILLING_COUNT") ? rs.getString("O_ITO_BILLING_COUNT") : "0");
			}
		} catch (SQLException e) {
			log.error("Exception while getting billing count details :: {}" , e.getMessage());
		}
		return responseMap;
	}

	@Override
	public List<SegmentSummaryDTO> getSegmentSummary(String projectId, String startDate, String endDate,
			String business) {
		List<SegmentSummaryDTO> list = new ArrayList<SegmentSummaryDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_SEGMENT_SUMMARY_LIST);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SegmentSummaryDTO summaryDTO = new SegmentSummaryDTO();
				summaryDTO.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				summaryDTO.setFinancialITOBl(
						null != rs.getString("o_financial_ito_billing") ? rs.getDouble("o_financial_ito_billing") : 0);
				summaryDTO.setLastEstimateVsFinancialBL(
						null != rs.getString("o_le_vs_f_bl") ? rs.getString("o_le_vs_f_bl") : "");
				summaryDTO.setFinancialBl(null != rs.getString("o_financial_bl") ? rs.getDouble("o_financial_bl") : 0);
				summaryDTO.setActual(null != rs.getString("o_actual") ? rs.getDouble("o_actual") : 0);
				summaryDTO.setForecast(null != rs.getString("o_forecast") ? rs.getDouble("o_forecast") : 0);
				summaryDTO.setLastEstimatePeriodToCurrentWeek(
						null != rs.getString("o_last_estimate_period_to_current_week")
								? rs.getDouble("o_last_estimate_period_to_current_week")
								: 0);
				summaryDTO.setDiffActualVsFinancialBL(
						null != rs.getString("o_actual_in_v_fin_bl_variation_vs_financial_bl")
								? rs.getDouble("o_actual_in_v_fin_bl_variation_vs_financial_bl")
								: 0);
				summaryDTO.setStatus(null != rs.getString("o_status") ? rs.getString("o_status") : "");
				summaryDTO.setBillingTgt(null != rs.getString("o_billing_tgt") ? rs.getString("o_billing_tgt") : "");
				summaryDTO.setOpp(null != rs.getString("o_opp") ? rs.getString("o_opp") : "");
				summaryDTO.setMediumHighRisk(
						null != rs.getString("o_medium_high_risk") ? rs.getString("o_medium_high_risk") : "");
				list.add(summaryDTO);
			}
		} catch (SQLException e) {
			log.error("Exception while getting segment summary details :: {}" , e.getMessage());
		}
		return list;
	}

	public List<BillingProjectListDTO> getBillingProjectList(String projectId, String startDate, String endDate,
			String business) {
		List<BillingProjectListDTO> projectList = new ArrayList<BillingProjectListDTO>();
		BillingProjectListDTO billingProjectListDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_PROJECT_TAB_DATA);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				billingProjectListDTO = new BillingProjectListDTO();
				billingProjectListDTO
						.setProjectId(null != rs.getString("o_project_id") ? rs.getString("o_project_id") : "");
				billingProjectListDTO.setMasterProjectname(
						null != rs.getString("o_master_project_name") ? rs.getString("o_master_project_name") : "");
				billingProjectListDTO
						.setCustomeName(null != rs.getString("o_customer") ? rs.getString("o_customer") : "");
				billingProjectListDTO.setPmName(null != rs.getString("o_pm") ? rs.getString("o_pm") : "");
				billingProjectListDTO.setSpm(null != rs.getString("o_spm") ? rs.getString("o_spm") : "");
				billingProjectListDTO.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				billingProjectListDTO.setFinBlAmountUsd(
						null != rs.getString("o_fin_bl_amount") ? rs.getString("o_fin_bl_amount") : "");
				billingProjectListDTO.setForecast(null != rs.getString("o_forecast") ? rs.getString("o_forecast") : "");
				billingProjectListDTO.setVarianceToBL(
						null != rs.getString("o_variance_to_bl") ? rs.getString("o_variance_to_bl") : "");
				billingProjectListDTO.setActual(null != rs.getString("o_actual") ? rs.getString("o_actual") : "");
				billingProjectListDTO.setFctsAmtAsToday(
						null != rs.getString("o_fct_amt_as_of_today") ? rs.getString("o_fct_amt_as_of_today") : "");
				billingProjectListDTO.setStatus(null != rs.getString("o_status") ? rs.getString("o_status") : "");
				billingProjectListDTO.setMasterProjectId(
						null != rs.getString("o_master_project_id") ? rs.getString("o_master_project_id") : "");
				billingProjectListDTO.setCompanyId(rs.getInt("o_company_id"));
				billingProjectListDTO
						.setCompanyCode(null != rs.getString("o_company_name") ? rs.getString("o_company_name") : "");
				billingProjectListDTO.setCompanyName(null != rs.getString(16) ? rs.getString(16) : "");
				billingProjectListDTO.setCountry(null != rs.getString("o_country") ? rs.getString("o_country") : "");
				billingProjectListDTO
						.setPrcoRegion(null != rs.getString("o_prco_region") ? rs.getString("o_prco_region") : "");
				billingProjectListDTO.setOpp(null != rs.getString("o_opp_amt") ? rs.getString("o_opp_amt") : "");
				billingProjectListDTO.setMediumHighRisk(
						null != rs.getString("o_mediumhighrisk_amt") ? rs.getString("o_mediumhighrisk_amt") : "");
				billingProjectListDTO
						.setBillingLE(null != rs.getString("o_billing_le") ? rs.getString("o_billing_le") : "");
				billingProjectListDTO.setBillingToGo(
						null != rs.getString("o_billing_le_vs_actual") ? rs.getString("o_billing_le_vs_actual") : "");
				billingProjectListDTO.setFinancialSegment(
						null != rs.getString("o_financial_segment") ? rs.getString("o_financial_segment") : "");
				projectList.add(billingProjectListDTO);
			}
		} catch (SQLException e) {
			log.error("Exception while getting project list details :: {}" , e.getMessage());
		}
		return projectList;
	}

	@Override
	public List<BillingMilestoneListDTO> getBillingMilestoneList(String projectId, String startDate, String endDate,
			String business) {
		List<BillingMilestoneListDTO> list = new ArrayList<BillingMilestoneListDTO>();
		BillingMilestoneListDTO summaryDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_MILESTONE_TAB_DATA);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO = new BillingMilestoneListDTO();
				summaryDTO.setProjectId(null != rs.getString("o_project_id") ? rs.getString("o_project_id") : "");
				summaryDTO.setPmName(null != rs.getString("o_pm") ? rs.getString("o_pm") : "");
				summaryDTO.setPmLeader(null != rs.getString("o_pm_leader") ? rs.getString("o_pm_leader") : "");
				summaryDTO.setProjectName(
						null != rs.getString("o_master_project_name") ? rs.getString("o_master_project_name") : "");
				summaryDTO.setMilestone(null != rs.getString("o_milestone") ? rs.getString("o_milestone") : "");
				summaryDTO.setDescription(
						null != rs.getString("o_milestone_description") ? rs.getString("o_milestone_description") : "");
				summaryDTO.setAmountUsd(null != rs.getString("o_amount_usd") ? rs.getString("o_amount_usd") : "");
				summaryDTO.setInvAmountUsd(
						null != rs.getString("o_inv_amount_usd") ? rs.getString("o_inv_amount_usd") : "");
				summaryDTO.setFinBlDt(null != rs.getString("o_fin_bl_dt") ? rs.getString("o_fin_bl_dt") : "");
				summaryDTO.setForecastDt(null != rs.getString("o_forecast_dt") ? rs.getString("o_forecast_dt") : "");
				summaryDTO.setActualFinishDt(
						null != rs.getString("o_actual_finish_dt") ? rs.getString("o_actual_finish_dt") : "");
				summaryDTO.setComments(null != rs.getString("o_comments") ? rs.getString("o_comments") : "");
				summaryDTO.setForecastCheck(
						null != rs.getString("o_forecast_check") ? rs.getString("o_forecast_check") : "");
				summaryDTO.setInvoiceDate(null != rs.getString("o_invoice_date") ? rs.getString("o_invoice_date") : "");
				summaryDTO.setMilStatus(null != rs.getString("o_mil_status") ? rs.getString("o_mil_status") : "");
				summaryDTO.setRiskOppty(null != rs.getString("o_risk_oppty") ? rs.getString("o_risk_oppty") : "");
				summaryDTO.setFinancialSegment(
						null != rs.getString("financial_segment_out") ? rs.getString("financial_segment_out") : "");
				list.add(summaryDTO);
			}
		} catch (Exception e) {
			log.error("Exception while getting milestone list details :: {}" , e.getMessage());
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<PercentageDTO> getPercentage(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(
				BillingPortfolioConstants.PERCENTAGE_DISPLAY, new Object[] { projectId, projectId, startDate, endDate,
						startDate, endDate, projectId, projectId, startDate, endDate, startDate, endDate },
				new ResultSetExtractor<List<PercentageDTO>>() {
					public List<PercentageDTO> extractData(ResultSet rs) throws SQLException {
						List<PercentageDTO> list = new ArrayList<PercentageDTO>();
						while (rs.next()) {
							PercentageDTO percentageDTO = new PercentageDTO();
							percentageDTO.setWeekly(null != rs.getString(1) ? rs.getString(1) : "");
							percentageDTO.setPercentOfFbl(rs.getDouble(2));
							list.add(percentageDTO);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ForecastCurveDTO> getForecastCurve(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.FORECAST_CURVE_DATA,
				new Object[] { projectId, projectId, startDate, endDate, startDate, endDate },
				new ResultSetExtractor<List<ForecastCurveDTO>>() {
					public List<ForecastCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ForecastCurveDTO> list = new ArrayList<ForecastCurveDTO>();
						while (rs.next()) {
							ForecastCurveDTO forecastCurveDTO = new ForecastCurveDTO();
							String weekly = (null != rs.getString(1) ? rs.getString(1) : "");
							forecastCurveDTO.setWeekly(weekly);
							String wk = weekly.substring(3, weekly.length());
							forecastCurveDTO.setEpochDate1(rs.getDouble(3));
							forecastCurveDTO.setDisplaydate(null != rs.getString(2) ? rs.getString(2) : "");
							forecastCurveDTO.setEpochDate(Double.parseDouble(wk.replace("-", "")));
							forecastCurveDTO.setCumDispAmount(rs.getDouble(4));
							list.add(forecastCurveDTO);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ActualCurveDTO> getActualCurve(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.ACTUAL_CURVE_DATA,
				new Object[] { projectId, projectId, startDate, endDate },
				new ResultSetExtractor<List<ActualCurveDTO>>() {
					public List<ActualCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ActualCurveDTO> list = new ArrayList<ActualCurveDTO>();
						while (rs.next()) {
							ActualCurveDTO actualCurveDTO = new ActualCurveDTO();
							String weekly = (null != rs.getString(1) ? rs.getString(1) : "");
							actualCurveDTO.setWeekly(weekly);
							String wk = weekly.substring(3, weekly.length());
							actualCurveDTO.setInvoicedate(null != rs.getString(2) ? rs.getString(2) : "");
							actualCurveDTO.setEpochDate1(rs.getDouble(3));
							actualCurveDTO.setEpochDate(Double.parseDouble(wk.replace("-", "")));
							actualCurveDTO.setCumDispAmount(rs.getDouble(4));
							list.add(actualCurveDTO);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BaselineCurveDTO> getBaselineCurve(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.BASELINE_CURVE_DATA,
				new Object[] { projectId, projectId, startDate, endDate, startDate, endDate },
				new ResultSetExtractor<List<BaselineCurveDTO>>() {
					public List<BaselineCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<BaselineCurveDTO> list = new ArrayList<BaselineCurveDTO>();
						while (rs.next()) {
							BaselineCurveDTO baselineCurveDTO = new BaselineCurveDTO();
							String weekly = (null != rs.getString(1) ? rs.getString(1) : "");
							String wk = weekly.substring(3, weekly.length());
							baselineCurveDTO.setWeekly(weekly);
							baselineCurveDTO.setDisplaydate(null != rs.getString(2) ? rs.getString(2) : "");
							baselineCurveDTO.setEpochDate1(rs.getDouble(3));
							baselineCurveDTO.setEpochDate(Double.parseDouble(wk.replace("-", "")));
							baselineCurveDTO.setCumDispAmount(rs.getDouble(4));
							list.add(baselineCurveDTO);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public Double getCurrentGap(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.CURRENT_GAP_COUNT,
				new Object[] { projectId, projectId, startDate, startDate, endDate, projectId, projectId, startDate },
				new ResultSetExtractor<Double>() {
					public Double extractData(ResultSet rs) throws SQLException {
						double count = 0;
						while (rs.next()) {
							count = rs.getDouble(1);
						}
						return count;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getLastUpdatedDate() {
		return jdbcTemplate.query(BillingPortfolioConstants.LAST_UPDATED_DATE, new Object[] {},
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";

						while (rs.next()) {
							date = rs.getString(1);

						}
						return date;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getGapRecovery(String projectId, String startDate, String endDate) {
		return jdbcTemplate
				.query(BillingPortfolioConstants.GAP_RECOVERY,
						new Object[] { projectId, projectId, startDate, startDate, endDate, startDate, projectId,
								projectId, startDate, endDate, startDate, endDate, endDate, endDate },
						new ResultSetExtractor<Integer>() {
							public Integer extractData(ResultSet rs) throws SQLException {
								int count = 0;
								while (rs.next()) {
									count = rs.getInt(1);
								}
								return count;
							}
						});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<String> getWeeks(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.ALL_WEEKS,
				new Object[] { projectId, projectId, startDate, endDate, startDate, endDate, projectId, projectId,
						startDate, endDate, projectId, projectId, startDate, endDate, startDate, endDate },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							String weeks = "";
							weeks = rs.getString(1);
							list.add(weeks);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BillingKpiDTO> getBillingKpi(String projectId, String startDate, String endDate, String business) {
		List<BillingKpiDTO> list = new ArrayList<BillingKpiDTO>();
		BillingKpiDTO kpiDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_KPI_LIST);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			BigDecimal invoiceCT = null;
			BigDecimal pmRequestCT = null;
			BigDecimal billingCT = null;
			String invoiceCTStr = null;
			String pmRequestCTStr = null;
			String billingCTStr = null;
			while (rs.next()) {
				kpiDTO = new BillingKpiDTO();
				invoiceCT = null;
				pmRequestCT = null;
				billingCT = new BigDecimal(0);
				invoiceCTStr = rs.getString("o_p75_inv_issue_ct");
				pmRequestCTStr = rs.getString("o_p75_pm_request_ct");
				billingCTStr = rs.getString("o_p75_billing_ct");
				if (null != billingCTStr && !"NA".equalsIgnoreCase(billingCTStr)) {
					if (null != invoiceCTStr && !"NA".equalsIgnoreCase(invoiceCTStr)) {
						invoiceCT = new BigDecimal(invoiceCTStr);
						invoiceCT = invoiceCT.setScale(1, BigDecimal.ROUND_HALF_UP);
						billingCT = billingCT.add(invoiceCT);
						invoiceCTStr = invoiceCT.toString();
					}
					if (null != pmRequestCTStr && !"NA".equalsIgnoreCase(pmRequestCTStr)) {
						pmRequestCT = new BigDecimal(pmRequestCTStr);
						pmRequestCT = pmRequestCT.setScale(1, BigDecimal.ROUND_HALF_UP);
						billingCT = billingCT.add(pmRequestCT);
						pmRequestCTStr = pmRequestCT.toString();
					}
					billingCT = billingCT.setScale(1, BigDecimal.ROUND_HALF_UP);
					billingCTStr = billingCT.toString();
				}
				kpiDTO.setFiscalWeek(null != rs.getString("o_weekly") ? rs.getString("o_weekly") : "");
				kpiDTO.setInvoiceCount(null != rs.getString("o_inv") ? rs.getString("o_inv") : "");
				kpiDTO.setP75InvIssueCt(null != invoiceCTStr ? invoiceCTStr : "");
				kpiDTO.setP75PmRequestCt(null != pmRequestCTStr ? pmRequestCTStr : "");
				kpiDTO.setP75BillingCt(null != billingCTStr ? billingCTStr : "");
				kpiDTO.setTotalInvoiceCount(null != rs.getString("o_total_inv") ? rs.getString("o_total_inv") : "");
				boolean checkFlag = checkFiscalWeek(rs.getString("o_weekly"));
				if (checkFlag) {
					list.add(kpiDTO);
				} else {
					kpiDTO.setFiscalWeek(null != rs.getString("o_weekly") ? rs.getString("o_weekly") : "");
					kpiDTO.setInvoiceCount(null);
					kpiDTO.setP75InvIssueCt(null);
					kpiDTO.setP75PmRequestCt(null);
					kpiDTO.setP75BillingCt(null);
					kpiDTO.setTotalInvoiceCount(null);
					list.add(kpiDTO);
				}
			}

		} catch (Exception e) {
			log.error("Exception while getting billing kpi details :: {}" , e.getMessage());
		}
		return list;
	}

	public boolean checkFiscalWeek(String input) {
		boolean checkFlag = false;
		int currYear = Calendar.getInstance().get(Calendar.YEAR);
		int currFiscalWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
		if (!input.isEmpty() && input != null && !input.equalsIgnoreCase("")) {
			String[] weekParts = input.split("-");
			if (Integer.parseInt(weekParts[2]) < currYear) {
				checkFlag = true;
			} else if (Integer.parseInt(weekParts[2]) == currYear && Integer.parseInt(weekParts[1]) <= currFiscalWeek) {
				checkFlag = true;
			}
		}
		return checkFlag;
	}

	@Override
	public List<ExceptionDTO> getBillingException(String projectId, String startDate, String endDate, String business) {
		List<ExceptionDTO> list = new ArrayList<ExceptionDTO>();
		ExceptionDTO exceptionDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_EXCEPTION_CATEGORY);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				exceptionDTO = new ExceptionDTO();
				exceptionDTO.setProjectId(null != rs.getString("o_project_id") ? rs.getString("o_project_id") : "");
				exceptionDTO.setMasterProjectName(
						null != rs.getString("o_master_project_name") ? rs.getString("o_master_project_name") : "");
				exceptionDTO.setPmName(null != rs.getString("o_pm") ? rs.getString("o_pm") : "");
				exceptionDTO.setPmLeaderName(null != rs.getString("o_pm_leader") ? rs.getString("o_pm_leader") : "");
				exceptionDTO.setBusinessUnit(
						null != rs.getString("o_business_unit") ? rs.getString("o_business_unit") : "");
				exceptionDTO.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				exceptionDTO.setReasonForException(
						null != rs.getString("o_reason_for_exp") ? rs.getString("o_reason_for_exp") : "");
				exceptionDTO.setCashMilestoneActivityId(
						null != rs.getString("o_cash_milestome_act_id") ? rs.getString("o_cash_milestome_act_id") : "");
				exceptionDTO.setMilestoneDesc(
						null != rs.getString("o_milestone_des") ? rs.getString("o_milestone_des") : "");
				exceptionDTO.setMaxActualFinishDt(
						null != rs.getString("o_max_actual_finish_date") ? rs.getString("o_max_actual_finish_date")
								: "");
				exceptionDTO.setPmRequestDt(
						null != rs.getString("o_pm_request_date") ? rs.getString("o_pm_request_date") : "");
				exceptionDTO.setActualInvoiceDt(
						null != rs.getString("o_actual_invoice_date") ? rs.getString("o_actual_invoice_date") : "");
				exceptionDTO.setInvoiceNumber(null != rs.getString("o_invoice_no") ? rs.getString("o_invoice_no") : "");
				exceptionDTO.setActualInvoiceYFw(
						null != rs.getString("o_actual_invoice_y_fw") ? rs.getString("o_actual_invoice_y_fw") : "");
				exceptionDTO.setRemarks(null != rs.getString("o_remarks") ? rs.getString("o_remarks") : "");
				exceptionDTO.setRemarksComments(
						null != rs.getString("o_remarks_comments") ? rs.getString("o_remarks_comments") : "");
				if (!exceptionDTO.getReasonForException().equalsIgnoreCase("")) {
					list.add(exceptionDTO);
				}
			}
		} catch (Exception ex) {
			log.error("Exception in BillingPortfolioDAOImpl :: getBillingException() :: {}" , ex.getMessage());
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	public BillingCycleDTO getOverallBillingCycleDetail(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_OVERALL_BILLING_P50_P75_P90_DETAILS,
				new Object[] { projectId, projectId, startDate, endDate }, new ResultSetExtractor<BillingCycleDTO>() {
					public BillingCycleDTO extractData(ResultSet rs) throws SQLException {
						BillingCycleDTO billingCycleDetail = new BillingCycleDTO();
						while (rs.next()) {
							billingCycleDetail.setP50(
									null != rs.getString("P50_billing_CT") ? rs.getString("P50_billing_CT") : "");
							billingCycleDetail.setP75(
									null != rs.getString("P75_billing_CT") ? rs.getString("P75_billing_CT") : "");
							billingCycleDetail.setP90(
									null != rs.getString("P90_billing_CT") ? rs.getString("P90_billing_CT") : "");
							return billingCycleDetail;
						}
						return billingCycleDetail;
					}
				});
	}

	@SuppressWarnings("deprecation")
	public BillingCycleDTO getTargetBillingCycleDetail(String business, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_TARGET_BILLING_P50_P75_P90_DETAILS,
				new Object[] { business, startDate, endDate }, new ResultSetExtractor<BillingCycleDTO>() {
					public BillingCycleDTO extractData(ResultSet rs) throws SQLException {
						BillingCycleDTO billingCycleDetail = new BillingCycleDTO();
						while (rs.next()) {
							String remarks = "", value = "";
							remarks = rs.getString("remarks");
							value = rs.getString("value");
							if (null != remarks && remarks.equalsIgnoreCase("BILL_CYCL_TGT_P50")) {
								billingCycleDetail.setP50(value);
							} else if (null != remarks && remarks.equalsIgnoreCase("BILL_CYCL_TGT_P75")) {
								billingCycleDetail.setP75(value);
							} else if (null != remarks && remarks.equalsIgnoreCase("BILL_CYCL_TGT_P90")) {
								billingCycleDetail.setP90(value);
							}
						}
						return billingCycleDetail;
					}
				});
	}

	@Override
	public List<ViewInvoiceDTO> getBillingInvoiceDetails(String projectId, String startDate, String endDate,
			String business) {
		List<ViewInvoiceDTO> list = new ArrayList<ViewInvoiceDTO>();
		ViewInvoiceDTO viewInvoiceDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_INVOICE_DETAILS);) {
			System.out.println(projectId);
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				viewInvoiceDTO = new ViewInvoiceDTO();
				viewInvoiceDTO.setProjectId(null != rs.getString("o_project_id") ? rs.getString("o_project_id") : "");
				viewInvoiceDTO.setMasterProjectName(
						null != rs.getString("o_master_project_name") ? rs.getString("o_master_project_name") : "");
				viewInvoiceDTO.setPmName(null != rs.getString("o_pm") ? rs.getString("o_pm") : "");
				viewInvoiceDTO.setPmLeaderName(null != rs.getString("o_pm_leader") ? rs.getString("o_pm_leader") : "");
				viewInvoiceDTO.setBusinessUnit(
						null != rs.getString("o_business_unit") ? rs.getString("o_business_unit") : "");
				viewInvoiceDTO.setSegment(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				viewInvoiceDTO.setCashMilestoneActivityId(
						null != rs.getString("o_cash_milestome_act_id") ? rs.getString("o_cash_milestome_act_id") : "");
				viewInvoiceDTO.setMilestoneDesc(
						null != rs.getString("o_milestone_des") ? rs.getString("o_milestone_des") : "");
				viewInvoiceDTO.setActualInvoiceYFw(
						null != rs.getString("o_actual_invoice_y_fw") ? rs.getString("o_actual_invoice_y_fw") : "");
				viewInvoiceDTO.setInvoiceNo(null != rs.getString("o_invoice_no") ? rs.getString("o_invoice_no") : "");
				viewInvoiceDTO.setMaxActualFinishDt(
						null != rs.getString("o_max_actual_finish_date") ? rs.getString("o_max_actual_finish_date")
								: "");
				viewInvoiceDTO.setPmRequestDt(
						null != rs.getString("o_pm_request_date") ? rs.getString("o_pm_request_date") : "");
				viewInvoiceDTO.setActualInvoiceDt(
						null != rs.getString("o_actual_invoice_date") ? rs.getString("o_actual_invoice_date") : "");
				viewInvoiceDTO.setBillingCT(null != rs.getString("o_billing_ct") ? rs.getString("o_billing_ct") : "");
				viewInvoiceDTO
						.setPmRequestCT(null != rs.getString("o_pm_request_ct") ? rs.getString("o_pm_request_ct") : "");
				viewInvoiceDTO.setInvoiceIssueCT(
						null != rs.getString("o_inv_issue_ct") ? rs.getString("o_inv_issue_ct") : "");
				viewInvoiceDTO.setRemarks(null != rs.getString("o_remarks") ? rs.getString("o_remarks") : "");
				viewInvoiceDTO.setRemarksComments(
						null != rs.getString("o_remarks_comments") ? rs.getString("o_remarks_comments") : "");
				list.add(viewInvoiceDTO);
			}
		} catch (Exception ex) {
			log.error("Exception in BillingPortfolioDAOImpl :: getBillingInvoiceDetails() :: {}" , ex.getMessage());
		}
		return list;
	}

	@Override
	public List<ExceptionCategoryDTO> getBillingCategoryDetails(String projectId, String startDate, String endDate,
			String business) {
		List<ExceptionCategoryDTO> list = new ArrayList<ExceptionCategoryDTO>();
		ExceptionCategoryDTO exceptionCategoryDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingPortfolioConstants.GET_EXCEPTION_CATEGORY_DETAILS);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			String noOfInv = "", noOfInvPer = "", invAmt = "", invAmtPer = "";
			while (rs.next()) {
				exceptionCategoryDTO = new ExceptionCategoryDTO();
				exceptionCategoryDTO
						.setExceptionCategory(null != rs.getString("o_category") ? rs.getString("o_category") : "");
				noOfInv = rs.getString("o_no_of_invoice");
				if (null != noOfInv && "99999".equalsIgnoreCase(noOfInv)) {
					exceptionCategoryDTO.setNoOfInvoice("NA");
				} else {
					exceptionCategoryDTO.setNoOfInvoice(null != noOfInv ? noOfInv : "");
				}

				invAmt = rs.getString("o_invoice_amt");
				if (null != invAmt && "99999".equalsIgnoreCase(invAmt)) {
					exceptionCategoryDTO.setAmount("NA");
				} else {
					exceptionCategoryDTO.setAmount(null != invAmt ? invAmt : "");
				}

				invAmtPer = rs.getString("o_invoice_amt_per");
				if (null != invAmtPer && "99999".equalsIgnoreCase(invAmtPer)) {
					exceptionCategoryDTO.setAmountPercent("NA");
				} else {
					exceptionCategoryDTO.setAmountPercent(null != invAmtPer ? invAmtPer : "");
				}

				noOfInvPer = rs.getString("o_no_of_invoice_per");
				if (null != noOfInvPer && "99999".equalsIgnoreCase(noOfInvPer)) {
					exceptionCategoryDTO.setNoOfInvoicePercent("NA");
				} else {
					exceptionCategoryDTO.setNoOfInvoicePercent(null != noOfInvPer ? noOfInvPer : "");
				}

				if (!exceptionCategoryDTO.getExceptionCategory().equalsIgnoreCase("")) {
					list.add(exceptionCategoryDTO);
				}
			}
		}

		catch (Exception exp) {

		}
		return list;
	}

	@Override
	public List<BillingKpiDTO> getMonthlyBillingKpi(String projectId, String startDate, String endDate,
			String business) {
		List<BillingKpiDTO> list = new ArrayList<BillingKpiDTO>();
		BillingKpiDTO kpiDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_MONTHLY_BILLING_KPI);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				kpiDTO = new BillingKpiDTO();
				kpiDTO.setFiscalWeek(null != rs.getString("o_month") ? rs.getString("o_month") : "");
				kpiDTO.setInvoiceCount(null != rs.getString("o_inv") ? rs.getString("o_inv") : "");
				kpiDTO.setP75InvIssueCt(
						null != rs.getString("o_p75_inv_issue_ct") ? rs.getString("o_p75_inv_issue_ct") : "");
				kpiDTO.setP75PmRequestCt(
						null != rs.getString("o_p75_pm_request_ct") ? rs.getString("o_p75_pm_request_ct") : "");
				kpiDTO.setP75BillingCt(
						null != rs.getString("o_p75_billing_ct") ? rs.getString("o_p75_billing_ct") : "");
				kpiDTO.setTotalInvoiceCount(null != rs.getString("o_total_inv") ? rs.getString("o_total_inv") : "");
				boolean checkFlag = checkFiscalMonth(rs.getString("o_month"));
				if (checkFlag) {
					list.add(kpiDTO);
				} else {
					kpiDTO.setFiscalWeek(null != rs.getString("o_month") ? rs.getString("o_month") : "");
					kpiDTO.setInvoiceCount(null);
					kpiDTO.setP75InvIssueCt(null);
					kpiDTO.setP75PmRequestCt(null);
					kpiDTO.setP75BillingCt(null);
					kpiDTO.setTotalInvoiceCount(null);
					list.add(kpiDTO);
				}
			}
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.ENGLISH);
			list.sort((BillingKpiDTO s1, BillingKpiDTO s2) -> YearMonth
					.parse(StringUtils.capitalize(s1.getFiscalWeek().replace("-", " ").toLowerCase()), formatter)
					.compareTo(YearMonth.parse(
							StringUtils.capitalize(s2.getFiscalWeek().replace("-", " ").toLowerCase()), formatter)));
		} catch (Exception e) {
			log.error("getMonthlyBillingKpi(): Exception occurred : {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<BillingKpiDTO> getQuarterlyBillingKpi(String projectId, String startDate, String endDate,
			String business) {
		List<BillingKpiDTO> list = new ArrayList<BillingKpiDTO>();
		BillingKpiDTO kpiDTO = null;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_QUARTERLY_BILLING_KPI);) {
			pstm.setString(1, projectId);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, business);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				kpiDTO = new BillingKpiDTO();
				kpiDTO.setFiscalWeek(null != rs.getString("o_quarter") ? rs.getString("o_quarter") : "");
				kpiDTO.setInvoiceCount(null != rs.getString("o_inv") ? rs.getString("o_inv") : "");
				kpiDTO.setP75InvIssueCt(
						null != rs.getString("o_p75_inv_issue_ct") ? rs.getString("o_p75_inv_issue_ct") : "");
				kpiDTO.setP75PmRequestCt(
						null != rs.getString("o_p75_pm_request_ct") ? rs.getString("o_p75_pm_request_ct") : "");
				kpiDTO.setP75BillingCt(
						null != rs.getString("o_p75_billing_ct") ? rs.getString("o_p75_billing_ct") : "");
				kpiDTO.setTotalInvoiceCount(null != rs.getString("o_total_inv") ? rs.getString("o_total_inv") : "");
				boolean checkFlag = checkFiscalQuarter(rs.getString("o_quarter"));
				if (checkFlag) {
					list.add(kpiDTO);
				} else {
					kpiDTO.setFiscalWeek(null != rs.getString("o_quarter") ? rs.getString("o_quarter") : "");
					kpiDTO.setInvoiceCount(null);
					kpiDTO.setP75InvIssueCt(null);
					kpiDTO.setP75PmRequestCt(null);
					kpiDTO.setP75BillingCt(null);
					kpiDTO.setTotalInvoiceCount(null);
					list.add(kpiDTO);
				}
			}
		} catch (Exception e) {
			log.error("getQuarterlyBillingKpi(): Exception occurred : {}" , e.getMessage());
		}
		return list;
	}

	public boolean checkFiscalMonth(String input) {
		boolean checkFlag = false;
		try {
			int currYear = Calendar.getInstance().get(Calendar.YEAR);
			int currMonth = Calendar.getInstance().get(Calendar.MONTH);
			if (!input.isEmpty() && input != null && !input.equalsIgnoreCase("")) {
				String[] monthParts = input.split("-");
				Date date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(monthParts[0]);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				int inputMonth = calendar.get(Calendar.MONTH);
				if (Integer.parseInt(monthParts[1]) < currYear) {
					checkFlag = true;
				} else if (Integer.parseInt(monthParts[1]) == currYear && inputMonth <= currMonth) {
					checkFlag = true;
				}
			}
		} catch (Exception e) {
			log.error("checkFiscalMonth(): Exception occurred : {}" , e.getMessage());
		}
		return checkFlag;
	}

	public boolean checkFiscalQuarter(String input) {
		boolean checkFlag = false;
		String inputQuarterStr = "";
		try {
			Calendar calender = new GregorianCalendar();
			int currQuarter = (calender.get(Calendar.MONTH) / 3) + 1;
			int currYear = calender.get(Calendar.YEAR);
			if (!input.isEmpty() && input != null && !input.equalsIgnoreCase("")) {
				String[] selectedQuarterArr = input.split("-");
				inputQuarterStr = selectedQuarterArr[1].substring(1);
				if (Integer.parseInt(selectedQuarterArr[0]) < currYear) {
					checkFlag = true;
				} else if (Integer.parseInt(selectedQuarterArr[0]) == currYear
						&& Integer.parseInt(inputQuarterStr) <= currQuarter) {
					checkFlag = true;
				}
			}
		} catch (Exception e) {
			log.error("checkFiscalQuarter(): Exception occurred : {}" , e.getMessage());
		}
		return checkFlag;
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BaselineCurveDTO> getITOBaselineCurve(String projectId, String startDate, String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.ITO_BASELINE_CURVE_DATA,
				new Object[] { projectId, projectId, startDate, endDate },
				new ResultSetExtractor<List<BaselineCurveDTO>>() {
					public List<BaselineCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<BaselineCurveDTO> list = new ArrayList<BaselineCurveDTO>();
						while (rs.next()) {
							BaselineCurveDTO baselineCurveDTO = new BaselineCurveDTO();
							String weekly = (null != rs.getString(1) ? rs.getString(1) : "");
							String wk = weekly.substring(3, weekly.length());
							baselineCurveDTO.setWeekly(weekly);
							baselineCurveDTO.setDisplaydate(null != rs.getString(2) ? rs.getString(2) : "");
							baselineCurveDTO.setEpochDate1(rs.getDouble(3));
							baselineCurveDTO.setEpochDate(Double.parseDouble(wk.replace("-", "")));
							baselineCurveDTO.setCumDispAmount(rs.getDouble(4));
							list.add(baselineCurveDTO);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getTPSBillingReportURI() {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_TPS_BILLING_URI, new Object[] {},
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String uri = "";
						while (rs.next()) {
							uri = rs.getString("uri");
						}
						return uri;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<DropDownDTO> getBillingInvoiceRemarksList() {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_BILLING_CYCLE_REMARKS_LIST, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						String remark = "";
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dropDownDTO = new DropDownDTO();
							remark = rs.getString("remark");
							if (remark != null && !remark.equalsIgnoreCase("")) {
								dropDownDTO.setKey(remark);
								dropDownDTO.setVal(remark);
								list.add(dropDownDTO);
							}
						}
						return list;
					}
				});
	}

	@Override
	public int saveBillingMilestoneRemarkDetails(List<BillingCycleRemarksDTO> invoiceList, String sso) {
		int result = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			for (BillingCycleRemarksDTO dto : invoiceList) {
				if ((null != dto.getRemarks() && !dto.getRemarks().equalsIgnoreCase(""))
						|| (null != dto.getRemarksComments() && !dto.getRemarksComments().equalsIgnoreCase(""))) {
					deleteBillingInvoicesRemarksDetails(dto.getProjectId(), dto.getCashMilestoneId());
					PreparedStatement pstm = con
							.prepareStatement(BillingPortfolioConstants.INSERT_INVOICE_REMARKS_DETAILS);
					pstm.setString(1, dto.getProjectId());
					pstm.setString(2, dto.getCashMilestoneId());
					pstm.setString(3, dto.getRemarks());
					pstm.setString(4, dto.getRemarksComments());
					pstm.setString(5, sso);
					if (pstm.executeUpdate() > 0) {
						result = 1;
					}
				}
			}
		} catch (Exception e) {
			log.error("something went wrong while  while saving remark details:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving remark details:{}" , e.getMessage());
				}
			}
		}
		return result;
	}

	public void deleteBillingInvoicesRemarksDetails(String projectId, String cashMilestoneId) {
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.DELETE_INVOICE_REMARKS_DETAILS);
			pstm.setString(1, projectId);
			pstm.setString(2, cashMilestoneId);
			pstm.executeUpdate();
		} catch (Exception e) {
			log.error("something went wrong while deleting remark details:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while deleting remark details:{}" , e.getMessage());
				}
			}
		}
	}

	@Override
	public List<DropDownDTO> getExceptionRemarksList() {
		List<DropDownDTO> list = new ArrayList<DropDownDTO>();
		String remark = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.GET_EXCEPTION_REMARKS_LIST);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DropDownDTO dropDownDTO = new DropDownDTO();
				remark = rs.getString("remark");
				if (remark != null && !remark.equalsIgnoreCase("")) {
					dropDownDTO.setKey(remark);
					dropDownDTO.setVal(remark);
					list.add(dropDownDTO);
				}
			}
			return list;
		} catch (SQLException e) {
			log.error("getExceptionRemarksList(): Exception occurred : {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public int saveBillingExceptionRemarkDetails(List<ExceptionRemarksDTO> exceptionList, String sso) {
		int result = 0;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingPortfolioConstants.INSERT_EXCEPTION_REMARKS_DETAILS);) {
			for (ExceptionRemarksDTO dto : exceptionList) {
				if ((null != dto.getRemarks() && !dto.getRemarks().equalsIgnoreCase(""))
						|| (null != dto.getRemarksComments() && !dto.getRemarksComments().equalsIgnoreCase(""))) {
					deleteExceptionRemarksDetails(dto.getProjectId(), dto.getCashMilestoneActivityId());
					pstm.setString(1, dto.getProjectId());
					pstm.setString(2, dto.getCashMilestoneActivityId());
					pstm.setString(3, dto.getRemarks());
					pstm.setString(4, dto.getRemarksComments());
					pstm.setString(5, sso);
					pstm.addBatch();
					if (pstm.executeUpdate() > 0) {
						result = 1;
					}
				}
			}
		} catch (Exception e) {
			log.error("saveBillingExceptionRemarkDetails(): Exception occurred : {}" , e.getMessage());
		}
		return result;
	}

	public void deleteExceptionRemarksDetails(String projectId, String cashMilestoneActivityId) {
		String query = "";
		query = BillingPortfolioConstants.DELETE_EXCEPTION_REMARKS_DETAILS;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			pstm.setString(2, cashMilestoneActivityId);
			pstm.executeUpdate();
		} catch (Exception e) {
			log.error("deleteExceptionRemarksDetails(): Exception occurred : {}" , e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ActivitiesDTO> getBillingActivitiesDetails(String projectId, String cashMilestoneId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_BILLING_ACTIVITY_DETAILS,
				new Object[] { projectId, cashMilestoneId }, new ResultSetExtractor<List<ActivitiesDTO>>() {
					public List<ActivitiesDTO> extractData(ResultSet rs) throws SQLException {
						List<ActivitiesDTO> list = new ArrayList<ActivitiesDTO>();
						while (rs.next()) {
							ActivitiesDTO activitiesDTO = new ActivitiesDTO();
							activitiesDTO
									.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							activitiesDTO.setProjectName(
									null != rs.getString("project_name") ? rs.getString("project_name") : "");
							activitiesDTO.setCostingProject(
									null != rs.getString("costing_project") ? rs.getString("costing_project") : "");
							activitiesDTO.setCashMilestoneActivityId(null != rs.getString("cash_milestone_activity_id")
									? rs.getString("cash_milestone_activity_id")
									: "");
							activitiesDTO.setActivityId(
									null != rs.getString("activity_id") ? rs.getString("activity_id") : "");
							activitiesDTO.setActivityDesc(
									null != rs.getString("activity_desc") ? rs.getString("activity_desc") : "");
							activitiesDTO.setActivityType(
									null != rs.getString("activity_type") ? rs.getString("activity_type") : "");
							activitiesDTO.setDummyCodeRef1(
									null != rs.getString("dummy_code_ref1") ? rs.getString("dummy_code_ref1") : "");
							activitiesDTO
									.setItemCode(null != rs.getString("item_code") ? rs.getString("item_code") : "");
							activitiesDTO.setBlFinishDt(
									null != rs.getString("bl_finish_dt") ? rs.getString("bl_finish_dt") : "");
							activitiesDTO.setActualFinishDt(
									null != rs.getString("actual_finish_dt") ? rs.getString("actual_finish_dt") : "");
							activitiesDTO.setForecastFinishDt(
									null != rs.getString("forecast_finish_dt") ? rs.getString("forecast_finish_dt")
											: "");
							activitiesDTO.setWip(null != rs.getString("wip") ? rs.getString("wip") : "");
							activitiesDTO.setPor(null != rs.getString("por") ? rs.getString("por") : "");
							activitiesDTO.setPoNumberLine(
									null != rs.getString("po_number_line") ? rs.getString("po_number_line") : "");
							activitiesDTO.setResourceName(
									null != rs.getString("resource_name") ? rs.getString("resource_name") : "");
							activitiesDTO.setIssuePlannedDt(
									null != rs.getString("issue_planned_dt") ? rs.getString("issue_planned_dt") : "");
							activitiesDTO.setFinishPlannedDt(
									null != rs.getString("finish_planned_dt") ? rs.getString("finish_planned_dt") : "");
							activitiesDTO.setDocSupplier(
									null != rs.getString("doc_supplier") ? rs.getString("doc_supplier") : "");
							activitiesDTO.setProrataDesc(
									null != rs.getString("prorata_desc") ? rs.getString("prorata_desc") : "");
							activitiesDTO.setLagN(null != rs.getString("lag_n") ? rs.getString("lag_n") : "");
							list.add(activitiesDTO);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	public String getProjectList(int companyId, String business, String segment, int customerId, String region,
			String projectId, String pmLeader, String spm, String financialSegment) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_PROJECT_LIST,
				new Object[] { companyId, business, business, segment, segment, customerId, customerId, region, region,
						projectId, projectId, pmLeader, pmLeader, spm, spm, financialSegment, financialSegment,
						business, business, segment, segment, region, region, pmLeader, pmLeader, spm, spm, projectId,
						projectId, financialSegment, financialSegment, business },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String projectIds = "", project;
						while (rs.next()) {
							project = rs.getString(1);
							if (project != null && !project.equalsIgnoreCase("") && !projectIds.equalsIgnoreCase("")) {
								projectIds = projectIds + ";" + project;
							} else {
								projectIds = project;
							}
						}
						return projectIds;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BillingLinearityChartDTO> getBillingLinearityChart(String projectId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_BILLING_LINEARITY_CHART, new Object[] { projectId },
				new ResultSetExtractor<List<BillingLinearityChartDTO>>() {
					public List<BillingLinearityChartDTO> extractData(ResultSet rs) throws SQLException {
						List<BillingLinearityChartDTO> list = new ArrayList<BillingLinearityChartDTO>();
						while (rs.next()) {
							BillingLinearityChartDTO dto = new BillingLinearityChartDTO();
							dto.setQuarterYear(
									null != rs.getString("o_year_quarter") ? rs.getString("o_year_quarter") : "");
							dto.setMonth(
									null != rs.getString("o_year_month_name") ? rs.getString("o_year_month_name") : "");
							dto.setVal(null != rs.getString("o_total_invoice_amount")
									? rs.getString("o_total_invoice_amount")
									: "");
							dto.setValPercent(null != rs.getString("o_mile_year_month_amt_per")
									? rs.getString("o_mile_year_month_amt_per")
									: "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BillingLinearityChartPopupDTO> getBillingLinearityChartPopup(String projectId, String startDate,
			String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_BILLING_LINEARITY_CHART_POPUP,
				new Object[] { startDate, endDate, projectId, projectId },
				new ResultSetExtractor<List<BillingLinearityChartPopupDTO>>() {
					public List<BillingLinearityChartPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<BillingLinearityChartPopupDTO> list = new ArrayList<BillingLinearityChartPopupDTO>();
						while (rs.next()) {
							BillingLinearityChartPopupDTO dto = new BillingLinearityChartPopupDTO();
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceDate(
									null != rs.getString("actual_invoice_dt") ? rs.getString("actual_invoice_dt") : "");
							dto.setInvoiceDueDate(null != rs.getString("due_dt") ? rs.getString("due_dt") : "");
							dto.setInvoiceAmount(
									null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							dto.setCurr(
									null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setBillingMilestoneId(null != rs.getString("cash_milestone_activity_id")
									? rs.getString("cash_milestone_activity_id")
									: "");
							dto.setBillingMilestoneDesc(
									null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
							dto.setPmRequestDate(
									null != rs.getString("pm_request_dt") ? rs.getString("pm_request_dt") : "");
							dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							dto.setProjectName(
									null != rs.getString("master_project_name") ? rs.getString("master_project_name")
											: "");
							dto.setPm(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BillingLinearityChartPopupDTO> getBillingLinearityChartPopupForLE(String projectId, String startDate,
			String endDate) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_BILLING_LINEARITY_CHART_POPUP_LE,
				new Object[] { startDate, endDate, projectId, projectId, startDate, endDate, projectId, projectId },
				new ResultSetExtractor<List<BillingLinearityChartPopupDTO>>() {
					public List<BillingLinearityChartPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<BillingLinearityChartPopupDTO> list = new ArrayList<BillingLinearityChartPopupDTO>();
						while (rs.next()) {
							BillingLinearityChartPopupDTO dto = new BillingLinearityChartPopupDTO();
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceDate(
									null != rs.getString("actual_invoice_dt") ? rs.getString("actual_invoice_dt") : "");
							dto.setInvoiceDueDate(null != rs.getString("due_dt") ? rs.getString("due_dt") : "");
							dto.setInvoiceAmount(
									null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							dto.setCurr(
									null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setBillingMilestoneId(null != rs.getString("cash_milestone_activity_id")
									? rs.getString("cash_milestone_activity_id")
									: "");
							dto.setBillingMilestoneDesc(
									null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
							dto.setPmRequestDate(
									null != rs.getString("pm_request_dt") ? rs.getString("pm_request_dt") : "");
							dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							dto.setProjectName(
									null != rs.getString("master_project_name") ? rs.getString("master_project_name")
											: "");
							dto.setPm(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
							dto.setForcastDate(null != rs.getString("forecast_dt") ? rs.getString("forecast_dt") : "");
							dto.setBillingAmountUSD(null != rs.getString("combined_forecast_amount")
									? rs.getString("combined_forecast_amount")
									: "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<BillingLinearityChartPopupDTO> downloadBillingLinearityExcel(String projectId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_BILLING_LINEARITY_DETAILS,
				new Object[] { projectId, projectId, projectId, projectId },
				new ResultSetExtractor<List<BillingLinearityChartPopupDTO>>() {
					public List<BillingLinearityChartPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<BillingLinearityChartPopupDTO> list = new ArrayList<BillingLinearityChartPopupDTO>();
						while (rs.next()) {
							BillingLinearityChartPopupDTO dto = new BillingLinearityChartPopupDTO();
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceDate(
									null != rs.getString("actual_invoice_dt") ? rs.getString("actual_invoice_dt") : "");
							dto.setInvoiceDueDate(null != rs.getString("due_dt") ? rs.getString("due_dt") : "");
							dto.setInvoiceAmount(
									null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							dto.setCurr(
									null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setBillingMilestoneId(null != rs.getString("cash_milestone_activity_id")
									? rs.getString("cash_milestone_activity_id")
									: "");
							dto.setBillingMilestoneDesc(
									null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
							dto.setPmRequestDate(
									null != rs.getString("pm_request_dt") ? rs.getString("pm_request_dt") : "");
							dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							dto.setProjectName(
									null != rs.getString("master_project_name") ? rs.getString("master_project_name")
											: "");
							dto.setPm(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String companyId) {
		return jdbcTemplate.query(BillingPortfolioConstants.LAST_SUCCESSFULL_UPDATE_DATA,
				new Object[] { Integer.parseInt(companyId) },
				new ResultSetExtractor<List<LastSuccessfulUpdateDetailsDTO>>() {
					public List<LastSuccessfulUpdateDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<LastSuccessfulUpdateDetailsDTO> detailsList = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
						while (rs.next()) {
							LastSuccessfulUpdateDetailsDTO detail = new LastSuccessfulUpdateDetailsDTO(rs.getString(1),
									rs.getString(2), rs.getString(3), rs.getString(4));
							detailsList.add(detail);
						}
						return detailsList;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<LastUpdateDetailsDTO> getBillingLastUpdatedData(String companyId) {
		return jdbcTemplate.query(BillingPortfolioConstants.LAST_UPDATE_DATA,
				new Object[] { Integer.parseInt(companyId) }, new ResultSetExtractor<List<LastUpdateDetailsDTO>>() {
					public List<LastUpdateDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<LastUpdateDetailsDTO> detailsList = new ArrayList<LastUpdateDetailsDTO>();
						while (rs.next()) {
							LastUpdateDetailsDTO detail = new LastUpdateDetailsDTO(rs.getString("insert_by"),
									rs.getString("status"), rs.getString("update_dt"), rs.getString("upload_file_name"),
									rs.getString("total_records"), rs.getString("not_processed_record"));
							detailsList.add(detail);
						}
						return detailsList;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ErrorDetailsDTO> getErrorDetailsData(String companyId) {
		return jdbcTemplate.query(BillingPortfolioConstants.ERROR_DETAILS_DATA,
				new Object[] { Integer.parseInt(companyId) }, new ResultSetExtractor<List<ErrorDetailsDTO>>() {
					public List<ErrorDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
						while (rs.next()) {
							String errmessage = rs.getString(2);
							if (null != errmessage && !errmessage.equalsIgnoreCase("")) {
								ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS", errmessage);
								detailsList.add(detail);
							}
						}
						return detailsList;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_NOT_PROCESED_PROJECT_DETAILS, new Object[] {},
				new ResultSetExtractor<List<ErrorDetailsDTO>>() {
					public List<ErrorDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
						while (rs.next()) {
							String errmessage = rs.getString("projectid");
							String errorLevel = rs.getString("error_level");
							if (null != errorLevel && !errorLevel.equalsIgnoreCase("")
									&& errorLevel.equalsIgnoreCase("project") && null != errmessage
									&& !errmessage.equalsIgnoreCase("")) {
								ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS",
										"Projects " + errmessage + " already present");
								detailsList.add(detail);
							} else if (null != errorLevel && !errorLevel.equalsIgnoreCase("")
									&& errorLevel.equalsIgnoreCase("milestone") && null != errmessage
									&& !errmessage.equalsIgnoreCase("")) {
								ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS",
										"Milestones " + errmessage + " are duplicated");
								detailsList.add(detail);
							}
						}
						return detailsList;
					}
				});
	}

	@Override
	public List<TPSBillingOutOfRTProjectDetailsDTO> getTPSBillingOutOfProjectTemplateDetails(String companyId) {
		List<TPSBillingOutOfRTProjectDetailsDTO> list = new ArrayList<TPSBillingOutOfRTProjectDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingPortfolioConstants.GET_PROJECT_EXCEL_TEMPLATE_DETAILS);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				TPSBillingOutOfRTProjectDetailsDTO dto = new TPSBillingOutOfRTProjectDetailsDTO();
				dto.setBusiness(null != rs.getString("business") ? rs.getString("business") : "");
				dto.setRegion(null != rs.getString("region") ? rs.getString("region") : "");
				dto.setCustomer(null != rs.getString("customer") ? rs.getString("customer") : "");
				dto.setInstallationCountry(
						null != rs.getString("installation_country") ? rs.getString("installation_country") : "");
				dto.setSegment(null != rs.getString("segment") ? rs.getString("segment") : "");
				dto.setProject(null != rs.getString("project") ? rs.getString("project") : "");
				dto.setProjectName(null != rs.getString("project_name") ? rs.getString("project_name") : "");
				dto.setMilestone(null != rs.getString("milestone") ? rs.getString("milestone") : "");
				dto.setPm(null != rs.getString("pm") ? rs.getString("pm") : "");
				dto.setSpm(null != rs.getString("spm") ? rs.getString("spm") : "");
				dto.setPmLeader(null != rs.getString("pm_leader") ? rs.getString("pm_leader") : "");
				dto.setDescription(null != rs.getString("description") ? rs.getString("description") : "");
				dto.setUsdMilestoneAmount(
						null != rs.getString("usd_milestone_amount") ? rs.getString("usd_milestone_amount") : "");
				dto.setLastEstimate(null != rs.getString("last_estimate_forecast_date")
						? rs.getString("last_estimate_forecast_date")
						: "");
				dto.setRiskOppty(null != rs.getString("risk_oppty") ? rs.getString("risk_oppty") : "");
				dto.setComment(null != rs.getString("comment1") ? rs.getString("comment1") : "");
				dto.setInvoiceNumber(null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
				dto.setInvoiceDate(null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
				dto.setInvoicedAmountUSD(
						null != rs.getString("invoiced_amount_usd") ? rs.getString("invoiced_amount_usd") : "");
				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			log.error("Exception while getting out of rt project template details :: {}" , e.getMessage());
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean getFileUploadStatus(String companyId, String moduleName) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_FILE_UPLOAD_STATUS,
				new Object[] { Integer.parseInt(companyId), moduleName }, new ResultSetExtractor<Boolean>() {
					@Override
					public Boolean extractData(ResultSet rs) throws SQLException {
						boolean inProgressFileUpload = false;
						while (rs.next()) {
							String status = "";
							status = null != rs.getString("status") ? rs.getString("status") : "";
							if (!status.equalsIgnoreCase("") && status.equalsIgnoreCase("In-Progress")) {
								inProgressFileUpload = true;
							}
						}
						return inProgressFileUpload;
					}
				});
	}

	@Override
	public Map<Integer, String> fetchProjectOutOfRTExcelHeaderColumnMap() {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap.put(0, "BUSINESS");
		headerIndexMap.put(1, "REGION");
		headerIndexMap.put(2, "CUSTOMER");
		headerIndexMap.put(3, "INSTALLATION COUNTRY");
		headerIndexMap.put(4, "SEGMENT");
		headerIndexMap.put(5, "PROJECT");
		headerIndexMap.put(6, "PROJECT NAME");
		headerIndexMap.put(7, "MILESTONE");
		headerIndexMap.put(8, "PM");
		headerIndexMap.put(9, "SPM");
		headerIndexMap.put(10, "PM LEADER");
		headerIndexMap.put(11, "DESCRIPTION");
		headerIndexMap.put(12, "USD MILESTONE AMOUNT");
		headerIndexMap.put(13, "LAST ESTIMATE (FORECAST DATE)");
		headerIndexMap.put(14, "RISK/OPPTY");
		headerIndexMap.put(15, "COMMENT");
		headerIndexMap.put(16, "INVOICE NUMBER");
		headerIndexMap.put(17, "INVOICE DATE");
		headerIndexMap.put(18, "INVOICED AMOUNT(USD)");
		return headerIndexMap;
	}

	@Override
	public void updateFileTrackingDetails(FileUploadStatusDTO statusDTO) {
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.UPDATE_FILE_UPLOAD_STATUS);
			pstm.setString(1, statusDTO.getStatus());
			pstm.setString(2, statusDTO.getErrorMsg());
			pstm.setString(3, statusDTO.getSso());
			pstm.setInt(4, Integer.parseInt(statusDTO.getTrackingId()));
			if (pstm.executeUpdate() > 0) {

			}
		} catch (Exception e) {
			log.error("something went wrong while updating file upload status:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while updating file upload status:{}" , e.getMessage());
				}
			}
		}
	}

	@Override
	public void insertFileTrackingDetails(FileUploadStatusDTO statusDTO) {
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(BillingPortfolioConstants.INSERT_FILE_UPLOAD_STATUS);
			pstm.setInt(1, Integer.parseInt(statusDTO.getCompanyId()));
			pstm.setString(2, statusDTO.getModuleName());
			pstm.setString(3, statusDTO.getStatus());
			pstm.setString(4, statusDTO.getFileName());
			pstm.setString(5, statusDTO.getSso());
			pstm.setString(6, statusDTO.getSso());
			if (pstm.executeUpdate() > 0) {
			}
		} catch (Exception e) {
			log.error("something went wrong while inserting file upload status:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while inserting file upload status:{}" , e.getMessage());
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public Integer getFileTrackingId(FileUploadStatusDTO statusDTO) {
		return jdbcTemplate.query(BillingPortfolioConstants.GET_TRACKING_ID, new Object[] {
				Integer.parseInt(statusDTO.getCompanyId()), statusDTO.getModuleName(), statusDTO.getFileName() },
				new ResultSetExtractor<Integer>() {
					@Override
					public Integer extractData(ResultSet rs) throws SQLException {
						int trackId = 0;
						while (rs.next()) {
							trackId = null != rs.getString("trackId") ? rs.getInt("trackId") : 0;
						}
						return trackId;
					}
				});
	}

	@Override
	public void insertProjectExcelStageData(List<TPSBillingOutOfRTProjectDetailsDTO> list,
			FileUploadStatusDTO statusDTO) {
		int batchIndex = 0;
		deleteFileUploadStageData(statusDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingPortfolioConstants.INSERT_PROJECT_EXCEL_STAGE_DATA);) {
			for (TPSBillingOutOfRTProjectDetailsDTO dto : list) {
				pstm.setString(1, dto.getBusiness());
				pstm.setString(2, dto.getRegion());
				pstm.setString(3, dto.getCustomer());
				pstm.setString(4, dto.getInstallationCountry());
				pstm.setString(5, dto.getSegment());
				pstm.setString(6, dto.getProject());
				pstm.setString(7, dto.getProjectName());
				pstm.setString(8, dto.getMilestone());
				pstm.setString(9, dto.getPm());
				pstm.setString(10, dto.getSpm());
				pstm.setString(11, dto.getPmLeader());
				pstm.setString(12, dto.getDescription());
				pstm.setString(13, dto.getUsdMilestoneAmount());
				pstm.setString(14, dto.getLastEstimate());
				pstm.setString(15, dto.getRiskOppty());
				pstm.setString(16, dto.getComment());
				pstm.setString(17, dto.getInvoiceNumber());
				pstm.setString(18, dto.getInvoiceDate());
				pstm.setString(19, dto.getInvoicedAmountUSD());
				pstm.setString(20, statusDTO.getSso());
				pstm.setString(21, statusDTO.getSso());
				pstm.addBatch();
				batchIndex++;
				if (batchIndex % 500 == 0) {
					log.info("Inserting " + batchIndex + " rows into Stage table.");
					pstm.executeBatch();
				}
			}
			log.info("Inserting " + batchIndex + " rows into Stage table.");
			pstm.executeBatch();
		} catch (Exception e) {
			log.error("Exception while inserting data in out of rt project excel stage table :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void deleteFileUploadStageData(FileUploadStatusDTO statusDTO) {
		String query = "";
		query = BillingPortfolioConstants.DELETE_PROJECT_EXCEL_STAGE_DATA;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.executeUpdate();
		} catch (Exception e) {
			log.error("Exception while deleting cash collection file upload stage table data :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	@Override
	public void callFileUploadStageToTarget(FileUploadStatusDTO statusDTO) {
		String query = "";
		query = BillingPortfolioConstants.CALL_PROJECT_EXCEL_STAGE_TO_TARGET;
		log.info("Stage To Target - Track ID :: " + statusDTO.getTrackingId());
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, Integer.parseInt(statusDTO.getTrackingId()));
			pstm.setInt(2, Integer.parseInt(statusDTO.getCompanyId()));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				log.info("Stage To Target Response :: " + rs.getString(1));
			}
		} catch (Exception e) {
			log.error("Exception while executing billing file upload stage to target procedure :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

}
