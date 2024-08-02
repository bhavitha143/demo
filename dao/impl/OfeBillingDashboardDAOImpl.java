package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.OfeBillingDashboardDAO;
import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.BusinessUnitSummaryDTO;
import com.bh.realtrack.dto.DashboardCountDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ForecastChartDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceDropdownListDTO;
import com.bh.realtrack.dto.InvoiceListDTO;
import com.bh.realtrack.dto.ManageProjectResponseDTO;
import com.bh.realtrack.dto.MilestoneListDTO;
import com.bh.realtrack.dto.OFECashCollectionCurveTableDTO;
import com.bh.realtrack.dto.OFECollectedCashDTO;
import com.bh.realtrack.dto.OFEForecastCashDTO;
import com.bh.realtrack.dto.OFEOpenInvoiceDataTableDTO;
import com.bh.realtrack.dto.OFEOtrCashBaselineDTO;
import com.bh.realtrack.dto.OFEPastDueCommitmentDTO;
import com.bh.realtrack.dto.OFESaveOpenInvoiceDTO;
import com.bh.realtrack.dto.OfeBillingInvoiceListDTO;
import com.bh.realtrack.dto.OfeBillingMilestoneListDTO;
import com.bh.realtrack.dto.OfeBillingProjectListDTO;
import com.bh.realtrack.dto.OfeCurveResponseDTO;
import com.bh.realtrack.dto.OfeOpenInvoiceChartPopupDetails;
import com.bh.realtrack.dto.OpenInvoiceChartDTO;
import com.bh.realtrack.dto.OpenInvoiceDTO;
import com.bh.realtrack.dto.ProjectTargetDTO;
import com.bh.realtrack.dto.TrendChartRequestDTO;
import com.bh.realtrack.dto.TrendChartResponseDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.AssertUtils;
import com.bh.realtrack.util.ExecutionConstants;
import com.bh.realtrack.util.OfeBillingDashboardConstants;

/**
 * @author Radhika Tadas
 */
@Repository
public class OfeBillingDashboardDAOImpl implements OfeBillingDashboardDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfeBillingDashboardDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<ManageProjectResponseDTO> getmanageProjectList(HeaderDashboardDetailsDTO headerDetails,
			String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_MANAGE_PROJECTS,
				new Object[] { projectId, projectId, headerDetails.getCompanyId(), headerDetails.getCompanyId(),
						headerDetails.getBusiness(), headerDetails.getBusiness(), headerDetails.getSegment(),
						headerDetails.getSegment(), headerDetails.getRegion(), headerDetails.getRegion(),
						headerDetails.getCustomerId(), headerDetails.getCustomerId() },
				new ResultSetExtractor<List<ManageProjectResponseDTO>>() {
					public List<ManageProjectResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<ManageProjectResponseDTO> list = new ArrayList<ManageProjectResponseDTO>();
						while (rs.next()) {
							ManageProjectResponseDTO dto = new ManageProjectResponseDTO();

							dto.setMasterProjectName(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setBusinessUnit(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setSegment(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCustomerName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setCompanyName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setCustomerId(rs.getInt(7));
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<BusinessUnitSummaryDTO> getBusinessUnitSummary(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_BUSINESS_UNIT_SUMMARY,
				new Object[] {filterValues.getProjectList(),filterValues.getProjectList(),startDate,endDate,filterValues.getProjectList(),filterValues.getProjectList(),
						filterValues.getBillingDateFlag(),startDate,endDate,startDate,endDate,filterValues.getProjectList(),filterValues.getProjectList(),
						startDate,endDate,filterValues.getBillingDateFlag(),startDate,filterValues.getBillingDateFlag(),endDate,filterValues.getBillingDateFlag(),startDate,
						filterValues.getProjectList() ,filterValues.getProjectList(),startDate,filterValues.getProjectList(),
						filterValues.getProjectList(),startDate,filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),filterValues.getBillingDateFlag(),
						startDate,filterValues.getBillingDateFlag(),endDate,filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),
						filterValues.getBillingDateFlag(),startDate,filterValues.getBillingDateFlag(),endDate,filterValues.getProjectList(),filterValues.getProjectList(),
						filterValues.getBillingDateFlag(),startDate,filterValues.getBillingDateFlag(),endDate,filterValues.getProjectList(),filterValues.getProjectList(),
						endDate,filterValues.getProjectList() ,filterValues.getProjectList(),startDate,endDate,filterValues.getProjectList(),filterValues.getProjectList()},
				new ResultSetExtractor<List<BusinessUnitSummaryDTO>>() {
					public List<BusinessUnitSummaryDTO> extractData(ResultSet rs) throws SQLException {
						List<BusinessUnitSummaryDTO> list = new ArrayList<BusinessUnitSummaryDTO>();
						while (rs.next()) {
							BusinessUnitSummaryDTO dto = new BusinessUnitSummaryDTO();
												
							dto.setBusinessUnit(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setLe_vs_f_bl(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setFinancial_bl_usd_mm(null != rs.getString(3) ? rs.getString(3) : "");
     						dto.setLast_estimate_usd_mm(null != rs.getString(4) ? rs.getString(4) : "");	
							dto.setActual_usd_mm(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setTogo_actual_fin_var_vs_financial_bl_usd_mm(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setLastEstimatePeriodToCurrentWeek(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setStatus(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setBmLinkage(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setOverdue(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setBillingTarget(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setLeVsTgt(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setOppty(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setRiskLevel(null != rs.getString(14) ? rs.getString(14) : "");
							
							list.add(dto);
						}
						LOGGER.info("businessUnitSummaryList======================={}",list.size());
						return list;
					}
				});
	}
	
	@Override
	public String getRole(String sso) {
		return jdbcTemplate.query(ExecutionConstants.GET_ROLE, new Object[] { sso }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String role = new String();
				try {
					while (rs.next()) {
						role = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return role;
			}
		});
	}

	@Override
	public String getFinancialBlKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_FINANCIAL_BL_KPI, new Object[] { filterValues.getProjectList(),filterValues.getProjectList(),
				startDate, filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),startDate,endDate,startDate,endDate}, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}

	@Override
	public String getLastEstimateKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_LAST_ESTIMATE_KPI, new Object[] { filterValues.getProjectList(),filterValues.getProjectList(),startDate,
				filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),startDate,endDate,startDate,endDate}, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}

	@Override
	public String getActualKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_ACTUAL_KPI, new Object[] { filterValues.getProjectList(),filterValues.getProjectList(),
				startDate,endDate}, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}

	@Override
	public String getTogoVsFBLKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_TO_GO_FINANCIAL_BL_KPI, new Object[] {filterValues.getProjectList(),filterValues.getProjectList(),startDate,endDate,
				filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),filterValues.getBillingDateFlag(),
				startDate,endDate,startDate,endDate,filterValues.getProjectList(),filterValues.getProjectList(),startDate	}, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}

	@Override
	public String getBmLinkageKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_BM_LINKAGE, new Object[] { filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),filterValues.getBillingDateFlag(),startDate,filterValues.getBillingDateFlag(),
				endDate,filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getBillingDateFlag(),startDate,filterValues.getBillingDateFlag(),endDate
				}, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}
	
	@Override
	public String getOverdueKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_OVERDUE, new Object[] { filterValues.getProjectList(),filterValues.getProjectList(),
				startDate }, new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}

	@Override
	public String getUnmatchedBLKpi(BillingDashboardFilterDTO filterValues) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_UNMATCHED_BILLING, 
				new Object[] { filterValues.getProjectList(), filterValues.getProjectList() },
				new ResultSetExtractor<String>() {
			@Override
			public String extractData(final ResultSet rs) {
				String kpi = new String();
				try {
					while (rs.next()) {
						kpi = rs.getString(1);
					}
				} catch (SQLException e) {

					throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
				}
				return kpi;
			}
		});
	}

	@Override
	public List<OfeBillingProjectListDTO> getBillingProjectList(BillingDashboardFilterDTO filterValues,
			String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_BILLING_PROJECT_LIST,
				new Object[] {filterValues.getProjectList(),startDate,endDate,filterValues.getBillingDateFlag()
						},
				new ResultSetExtractor<List<OfeBillingProjectListDTO>>() {
					public List<OfeBillingProjectListDTO> extractData(ResultSet rs) throws SQLException {
						List<OfeBillingProjectListDTO> list = new ArrayList<OfeBillingProjectListDTO>();
						while (rs.next()) {
							OfeBillingProjectListDTO dto = new OfeBillingProjectListDTO();
							dto.setBusinessUnit(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setProjectId(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setMasterProjectName(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCustomerName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setPmName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setProjectTargetDay(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setP50_of_bm(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setBaselineUsd(rs.getFloat(8));
							dto.setLeUsd(rs.getFloat(9));
							dto.setBillingTarget(rs.getFloat(10));
							dto.setLeVsTgt(rs.getFloat(11));
							dto.setLeVsBl(rs.getFloat(12));
							dto.setInvoiceUsd(rs.getFloat(13));
							dto.setOppty(rs.getFloat(14));
							dto.setRiskLevel(rs.getFloat(15));
							dto.setToGoVsLeOut(rs.getFloat(16));
							dto.setLeUptoCw(rs.getFloat(17));
							dto.setStatus(null != rs.getString(18) ? rs.getString(18) : "");
							dto.setMasterProjectId(null != rs.getString(19) ? rs.getString(19) : "");
							dto.setCompanyId(rs.getInt(20));
							dto.setCompanyCode(null != rs.getString(21) ? rs.getString(21) : "");
							dto.setCompanyName(null != rs.getString(22) ? rs.getString(22) : "");
							dto.setCountry(null != rs.getString(23) ? rs.getString(23) : "");
							dto.setPrcoRegion(null != rs.getString(24) ? rs.getString(24) : "");
							dto.setOverdueAmount(rs.getFloat(25));
							dto.setUnmatchedBilling(rs.getFloat(26));
							dto.setBmLinkage(null != rs.getString(27) ? rs.getString(27) : "");
							
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<OfeBillingMilestoneListDTO> getBillingMilestoneList(BillingDashboardFilterDTO filterValues,
			String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_BILLING_MILESTONE,
				new Object[] {filterValues.getProjectList(),startDate,endDate,filterValues.getBillingDateFlag()
						},
				new ResultSetExtractor<List<OfeBillingMilestoneListDTO>>() {
					public List<OfeBillingMilestoneListDTO> extractData(ResultSet rs) throws SQLException {
						List<OfeBillingMilestoneListDTO> list = new ArrayList<OfeBillingMilestoneListDTO>();
						while (rs.next()) {
							OfeBillingMilestoneListDTO dto = new OfeBillingMilestoneListDTO();
							dto.setBusinessUnit(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setMilestone(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setPmName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setDescription(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setAmountUsd(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setProjectTargetDay(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setP50_of_bm(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setEstimBillingDateDriver(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setEstimBillingCycleTime(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setBlP6ForecastDate(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setLeP6ForecastDate(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setBaselineDt(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setLastEstimate(null != rs.getString(14) ? rs.getString(14) : "");
							dto.setLeVsBl(null != rs.getString(15) ? rs.getString(15) : "");
							dto.setInvoiceNumber(null != rs.getString(16) ? rs.getString(16) : "");
							dto.setInvoiceDate(null != rs.getString(17) ? rs.getString(17) : "");
							dto.setInvAmountUsd(null != rs.getString(18) ? rs.getString(18) : "");
							dto.setMilStatus(null != rs.getString(19) ? rs.getString(19) : "");
							dto.setLinkStatus(null != rs.getString(20) ? rs.getString(20) : "");
							dto.setLeOrBl(null != rs.getString(21) ? rs.getString(21) : "");
							dto.setRollOffMatch(null != rs.getString(22) ? rs.getString(22) : "");
							dto.setRisk(null != rs.getString(23) ? rs.getString(23) : "");
							dto.setComment(null != rs.getString(24) ? rs.getString(24) : "");
							list.add(dto);
						}
						return list;
					}
				});
	}
	
	@Override
	public List<OfeBillingInvoiceListDTO> getBillingInvoiceList(BillingDashboardFilterDTO filterValues,
			String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_BILLING_INVOICE_LIST,
				new Object[] { filterValues.getProjectList(),filterValues.getProjectList(), startDate,endDate },
				new ResultSetExtractor<List<OfeBillingInvoiceListDTO>>() {
					public List<OfeBillingInvoiceListDTO> extractData(ResultSet rs) throws SQLException {
						List<OfeBillingInvoiceListDTO> list = new ArrayList<OfeBillingInvoiceListDTO>();
						while (rs.next()) {
							OfeBillingInvoiceListDTO dto = new OfeBillingInvoiceListDTO();
												
							dto.setBusinessUnit(null != rs.getString(1) ? rs.getString(1) : "");	
							dto.setProjectId(null != rs.getString(2) ? rs.getString(2) : "");	
							dto.setInvoiceNumber(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setLineNum(null != rs.getString(4) ? rs.getString(4) : "");	
							dto.setInvoiceDescription(null != rs.getString(5) ? rs.getString(5) : "");	
							dto.setInvoiceDate(null != rs.getString(6) ? rs.getString(6) : "");	
							dto.setInvAmount(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setCurrencyCode(null != rs.getString(8) ? rs.getString(8) : "");	
							dto.setInvAmountUsd(null != rs.getString(9) ? rs.getString(9) : "");
														
							list.add(dto);
						}

						return list;
					}
				});
	}
	
	@Override
	public List<OfeCurveResponseDTO> getActualCurveList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_ACTUAL_CURVE,
				new Object[] { filterValues.getProjectList(),filterValues.getProjectList(),startDate,endDate},
				new ResultSetExtractor<List<OfeCurveResponseDTO>>() {
					public List<OfeCurveResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<OfeCurveResponseDTO> list = new ArrayList<OfeCurveResponseDTO>();
						while (rs.next()) {
							OfeCurveResponseDTO dto = new OfeCurveResponseDTO();
							 dto.setWeekly(null != rs.getString(1) ? rs.getString(1) : "");	
							 dto.setInvoiceDate(null != rs.getString(2) ? rs.getString(2) : "");	
							 dto.setEpochDate(null != rs.getString(3) ? rs.getString(3) : "");	
							 dto.setCumDispAmount(rs.getDouble(4));	
							 														
							list.add(dto);
						}

						return list;
					}
				});
	}
	
	@Override
	public List<OfeCurveResponseDTO> getForecastCurveList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_FORECAST_CURVE,
				new Object[] { filterValues.getBillingDateFlag(), filterValues.getProjectList(),
						filterValues.getProjectList(), filterValues.getBillingDateFlag(),
						filterValues.getBillingDateFlag(), startDate, filterValues.getBillingDateFlag(), endDate,startDate,
						filterValues.getProjectList(),filterValues.getProjectList(),startDate },
				new ResultSetExtractor<List<OfeCurveResponseDTO>>() {
					public List<OfeCurveResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<OfeCurveResponseDTO> list = new ArrayList<OfeCurveResponseDTO>();
						while (rs.next()) {
							OfeCurveResponseDTO dto = new OfeCurveResponseDTO();
							dto.setWeekly(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setInvoiceDate(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setEpochDate(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCumDispAmount(rs.getDouble(4));

							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<OfeCurveResponseDTO> getFinancialBlCurveList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_FINANCIAL_CURVE,
				new Object[] { filterValues.getBillingDateFlag(), filterValues.getProjectList(),
						filterValues.getProjectList(), filterValues.getBillingDateFlag(), startDate, endDate, startDate,
						endDate, filterValues.getBillingDateFlag(), filterValues.getBillingDateFlag(), startDate,
						filterValues.getBillingDateFlag(), endDate,startDate, filterValues.getProjectList(),
						filterValues.getProjectList(), startDate },
				new ResultSetExtractor<List<OfeCurveResponseDTO>>() {
					public List<OfeCurveResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<OfeCurveResponseDTO> list = new ArrayList<OfeCurveResponseDTO>();
						while (rs.next()) {
							OfeCurveResponseDTO dto = new OfeCurveResponseDTO();
							dto.setWeekly(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setInvoiceDate(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setEpochDate(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCumDispAmount(rs.getDouble(4));

							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<String> getWeeks(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_WEEKS,
				new Object[] { startDate,endDate,startDate,filterValues.getProjectList(),filterValues.getProjectList(),
						startDate},
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							
							list.add(null != rs.getString(1) ? rs.getString(1) : "");	
							
						}

						return list;
					}
				});
	}
	
	@Override
	public String getEditAccess(String sso) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_EDIT_ACCESS,
				new Object[] {sso},
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String flag = "false";
						while (rs.next()) {
							if(rs.getString(1).equalsIgnoreCase("Y")) {
								flag = "true";	
							}
									
						}
						return flag;
					}
				});
	}

	@Override
	public List<ProjectTargetDTO> getProjectTargetList(HeaderDashboardDetailsDTO headerDetails,String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_TARGET_LIST,
				new Object[] { projectId, projectId, headerDetails.getCompanyId(), headerDetails.getCompanyId(),
						headerDetails.getBusiness(), headerDetails.getBusiness(), headerDetails.getSegment(),
						headerDetails.getSegment(), headerDetails.getRegion(), headerDetails.getRegion(),
						headerDetails.getRegion(), headerDetails.getCustomerId(),
						headerDetails.getCustomerId() },
				new ResultSetExtractor<List<ProjectTargetDTO>>() {
					public List<ProjectTargetDTO> extractData(ResultSet rs) throws SQLException {
						List<ProjectTargetDTO> list = new ArrayList<ProjectTargetDTO>();
						while (rs.next()) {
							ProjectTargetDTO dto = new ProjectTargetDTO();
							dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
							dto.setWeightedAvgCt(null != rs.getString("weighted_avg_ct") ? rs.getString("weighted_avg_ct") : "");
							dto.setP90OfBm(null != rs.getString("p90_of_bm") ? rs.getString("p90_of_bm") : "");
							dto.setP50OfBm(null != rs.getString("p50_of_bm") ? rs.getString("p50_of_bm") : "");
							dto.setProjectTargetDay(null != rs.getString("project_target_day") ? rs.getString("project_target_day") : "");
							dto.setProjectPaymentTerm(null != rs.getString("project_payment_term") ? rs.getString("project_payment_term") : "");
							dto.setUpdatedOn(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
							dto.setUpdateyBy(null != rs.getString("updatey_by") ? rs.getString("updatey_by") : "");
							dto.setMonthlyBillingSlot(null != rs.getString("monthly_bill_slot_days") ? rs.getString("monthly_bill_slot_days") : "");
							dto.setCurQtrBillingTarget(rs.getString("current_qtr_billing_target"));
							dto.setCurQtrCollectionTarget(rs.getString("current_qtr_collection_target"));
							dto.setTaxWithholdingPercentage(null != rs.getString("tax_withholding_perc") ? rs.getString("tax_withholding_perc") : "");
							list.add(dto);
						}

						return list;
					}
				});
	}
	
	@Override
	public int editProjectTargetList(ProjectTargetDTO targetDto,
			String sso) {
		int result = 0;
		Connection con = null;
			try {
				con = jdbcTemplate.getDataSource().getConnection();
				
					PreparedStatement pstm = con.prepareStatement(OfeBillingDashboardConstants.UPDATE_TARGET_TABLE);
										
					pstm.setString(1,targetDto.getProjectId());
					pstm.setString(2,targetDto.getProjectTargetDay());
					pstm.setString(3,targetDto.getProjectPaymentTerm());
					pstm.setString(4,sso);
					pstm.setString(5, targetDto.getMonthlyBillingSlot());
					pstm.setString(6, targetDto.getCurQtrBillingTarget());
					pstm.setString(7, targetDto.getCurQtrCollectionTarget());
				    pstm.setString(8, targetDto.getTaxWithholdingPercentage());
					
					ResultSet rs = pstm.executeQuery();
					
					while (rs.next()) {
						result = rs.getInt(1);
						LOGGER.info("editProjectTarget {}: ",rs.getInt(1));
					}
				    LOGGER.info("result :  {}", result);

			} catch (Exception e) {

				LOGGER.error("something went wrong while SAVING MILESTONE DETAILS:{}"
						, e.getMessage());
				throw new ServerErrorException(
						ErrorCode.INTERNAL.getResponseStatus(), e);
			} finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		
		return result;

	}
	
	@Override
	public List<TrendChartResponseDTO> getTrendChart(TrendChartRequestDTO headerDetails,String startDate, String endDate) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_TREND_CHART,
				new Object[] { headerDetails.getProjectId(), headerDetails.getProjectId(),headerDetails.getProjectId(), headerDetails.getProjectId(),
						endDate,endDate,endDate,endDate},
				new ResultSetExtractor<List<TrendChartResponseDTO>>() {
					public List<TrendChartResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<TrendChartResponseDTO> list = new ArrayList<TrendChartResponseDTO>();
						while (rs.next()) {
							TrendChartResponseDTO dto = new TrendChartResponseDTO();
							dto.setFirstDate(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setWeightedAvgCycleTime(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setP90OfBm(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setP50OfBm(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setMilestoneLinkage	(null != rs.getString(5) ? rs.getString(5) : "");	
							list.add(dto);
						}

						return list;
					}
				});
	}
	
	@Override
	public Map<String, Object> getOpenInvoiceChart(String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_OPEN_INVOICE_CHART, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					Map<String, Object> responseMap = new HashMap<String, Object>();
					List<OpenInvoiceDTO> pastDueList = new ArrayList<OpenInvoiceDTO>();
					List<OpenInvoiceDTO> currentQuarterList = new ArrayList<OpenInvoiceDTO>();
					List<OpenInvoiceDTO> futureDueList = new ArrayList<OpenInvoiceDTO>();
					OpenInvoiceChartDTO pastDueDTO = new OpenInvoiceChartDTO();
					OpenInvoiceChartDTO currentQuarterDTO = new OpenInvoiceChartDTO();
					OpenInvoiceChartDTO futureDueDTO = new OpenInvoiceChartDTO();

					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							OpenInvoiceDTO dto = new OpenInvoiceDTO();
							dto.setTypes(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setDisputeColor(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setCount(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setSum(null != rs.getString(4) ? rs.getString(4) : "");
							if (dto.getTypes().equalsIgnoreCase(OfeBillingDashboardConstants.PAST_DUE_CHART_TYPE)) {
								pastDueList.add(dto);
							} else if (dto.getTypes().equalsIgnoreCase(OfeBillingDashboardConstants.CURRENT_DUE_CHART_TYPE)) {
								currentQuarterList.add(dto);
							} else if (dto.getTypes().equalsIgnoreCase(OfeBillingDashboardConstants.FUTURE_DUE_CHART_TYPE)) {
								futureDueList.add(dto);
							}
						}
						pastDueDTO = getOpenInvoiceChartResponse(pastDueList);
						currentQuarterDTO = getOpenInvoiceChartResponse(currentQuarterList);
						futureDueDTO = getOpenInvoiceChartResponse(futureDueList);
						responseMap.put("pastDue", pastDueDTO);
						responseMap.put("currentQuarter", currentQuarterDTO);
						responseMap.put("futureDue", futureDueDTO);
						return responseMap;
					}
				});
	}

	public OpenInvoiceChartDTO getOpenInvoiceChartResponse(List<OpenInvoiceDTO> list) {
		OpenInvoiceChartDTO dto = new OpenInvoiceChartDTO();
		int totalCount = 0;
		Double totalAmount = 0.0;
		for (OpenInvoiceDTO obj : list) {
			if (obj.getDisputeColor().equalsIgnoreCase("Black")) {
				dto.setDisputedAmount(obj.getSum());
				dto.setDisputedCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Red")) {
				dto.setCollectionEscalatedAmount(obj.getSum());
				dto.setCollectionEscalatedCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Orange")) {
				dto.setNotDisputedAmount(obj.getSum());
				dto.setNotDisputedCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Green")) {
				dto.setCommitToPayAmount(obj.getSum());
				dto.setCommitToPayCount(obj.getCount());
			} else if (obj.getDisputeColor().equalsIgnoreCase("Blue")) {
				dto.setCreditNoteAmount(obj.getSum());
				dto.setCreditNoteCount(obj.getCount());
			}
			totalCount = totalCount + Integer.parseInt(obj.getCount());
			totalAmount = totalAmount + Double.parseDouble(obj.getSum());
			dto.setTotalCount(String.valueOf(totalCount));
			dto.setTotalAmount(Double.toString(Math.round(totalAmount * 100.0) / 100.0));
		}
		return dto;
	}

	@Override
	public String getLastUpdatedDate(String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_INVOICE_CHART_DATE, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = null != rs.getString(1) ? rs.getString(1) : "";
						}
						return date;
					}
				});
	}

	@Override
	public List<OfeOpenInvoiceChartPopupDetails> getOpenInvoiceChartPopupDetails(String projectId, String chartType,
			String statusCode) {
		String type = "", code = "";
		if (chartType.equalsIgnoreCase("PAST_DUE")) {
			type = OfeBillingDashboardConstants.PAST_DUE_CHART_TYPE;
		} else if (chartType.equalsIgnoreCase("CURRENT_DUE")) {
			type = OfeBillingDashboardConstants.CURRENT_DUE_CHART_TYPE;
		} else if (chartType.equalsIgnoreCase("FUTURE_DUE")) {
			type = OfeBillingDashboardConstants.FUTURE_DUE_CHART_TYPE;
		}

		if (statusCode.equalsIgnoreCase("DISPUTED")) {
			code = OfeBillingDashboardConstants.DISPUTED_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("COLLECTION_ESCALATED")) {
			code = OfeBillingDashboardConstants.COLLECTION_ESCALATED_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("NOT_DISPUTED")) {
			code = OfeBillingDashboardConstants.NOT_DISPUTED_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("CREDIT_NOTE")) {
			code = OfeBillingDashboardConstants.CREDIT_NOTE_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("COMMIT_TO_PAY")) {
			code = OfeBillingDashboardConstants.COMMIT_TO_PAY_STATUS_CODE;
		}

		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_OPEN_INVOICE_CHART_POPUP,
				new Object[] { projectId, projectId, type, code },
				new ResultSetExtractor<List<OfeOpenInvoiceChartPopupDetails>>() {
					public List<OfeOpenInvoiceChartPopupDetails> extractData(ResultSet rs) throws SQLException {
						List<OfeOpenInvoiceChartPopupDetails> list = new ArrayList<OfeOpenInvoiceChartPopupDetails>();
						while (rs.next()) {
							OfeOpenInvoiceChartPopupDetails dto = new OfeOpenInvoiceChartPopupDetails();
							dto.setType(null != rs.getString("types") ? rs.getString("types") : "");
							dto.setCategory(null != rs.getString("category") ? rs.getString("category") : "");
							dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceAmountCurr(
									null != rs.getString("invoice_amount_curr") ? rs.getString("invoice_amount_curr")
											: "");
							dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setOpenInvoiceAmountCurr(null != rs.getString("open_invoice_amount_curr")
									? rs.getString("open_invoice_amount_curr")
									: "");
							dto.setArUSD(null != rs.getString("ar_usd") ? rs.getString("ar_usd") : "");
							dto.setInvoiceDate(
									null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							dto.setInvoiceDueDate(
									null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
							dto.setBillingMilestone(
									null != rs.getString("billing_milestone") ? rs.getString("billing_milestone") : "");
							dto.setCollectionCode(
									null != rs.getString("collection_code") ? rs.getString("collection_code") : "");
							dto.setCollectionComments(
									null != rs.getString("collection_comments") ? rs.getString("collection_comments")
											: "");
							dto.setCollectionName(
									null != rs.getString("collector_name") ? rs.getString("collector_name") : "");
							dto.setDisputeCodeDesc(null != rs.getString("dispute_code_description")
									? rs.getString("dispute_code_description")
									: "");
							dto.setDisputeComments(
									null != rs.getString("dispute_comments") ? rs.getString("dispute_comments") : "");
							dto.setDisputeDate(
									null != rs.getString("dispute_date") ? rs.getString("dispute_date") : "");
							dto.setDisputeNumber(
									null != rs.getString("dispute_number") ? rs.getString("dispute_number") : "");
							dto.setDisputeOwner(
									null != rs.getString("dispute_owner") ? rs.getString("dispute_owner") : "");
							dto.setDisputeResolutionDate(null != rs.getString("dispute_resolution_date")
									? rs.getString("dispute_resolution_date")
									: "");
							dto.setCommitedOn(null != rs.getString("commited_on") ? rs.getString("commited_on") : "");
							dto.setDisputeStatus(
									null != rs.getString("dispute_status") ? rs.getString("dispute_status") : "");
							dto.setLatestAction(
									null != rs.getString("latest_action") ? rs.getString("latest_action") : "");
							dto.setCustomerSegment(
									null != rs.getString("customer_segment") ? rs.getString("customer_segment") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setPaymentTerm(
									null != rs.getString("payment_term") ? rs.getString("payment_term") : "");
							dto.setPo(null != rs.getString("po") ? rs.getString("po") : "");
							dto.setLeName(null != rs.getString("le_name") ? rs.getString("le_name") : "");
							dto.setCreditLimit(
									null != rs.getString("credit_limit") ? rs.getString("credit_limit") : "");
							dto.setDhl("");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<OFEOpenInvoiceDataTableDTO> getOpenInvoiceDatatable(String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_OPEN_INVOICE_DATA_TABLE, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<OFEOpenInvoiceDataTableDTO>>() {
					public List<OFEOpenInvoiceDataTableDTO> extractData(ResultSet rs) throws SQLException {
						List<OFEOpenInvoiceDataTableDTO> list = new ArrayList<OFEOpenInvoiceDataTableDTO>();
						while (rs.next()) {
							OFEOpenInvoiceDataTableDTO dto = new OFEOpenInvoiceDataTableDTO();
							dto.setType(null != rs.getString("types") ? rs.getString("types") : "");
							dto.setCategory(null != rs.getString("category") ? rs.getString("category") : "");
							dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceAmountCurr(
									null != rs.getString("invoice_amount_curr") ? rs.getString("invoice_amount_curr")
											: "");
							dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setOpenInvoiceAmountCurr(null != rs.getString("open_invoice_amount_curr")
									? rs.getString("open_invoice_amount_curr")
									: "");
							dto.setArUSD(null != rs.getString("ar_usd") ? rs.getString("ar_usd") : "");
							dto.setInvoiceDate(
									null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							dto.setInvoiceDueDate(
									null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
							dto.setBillingMilestone(
									null != rs.getString("billing_milestone") ? rs.getString("billing_milestone") : "");
							dto.setRiskOpty(null != rs.getString("risk") ? rs.getString("risk") : "");
							dto.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
							dto.setCollectionCode(
									null != rs.getString("collection_code") ? rs.getString("collection_code") : "");
							dto.setCollectionComments(
									null != rs.getString("collection_comments") ? rs.getString("collection_comments")
											: "");
							dto.setCollectionName(
									null != rs.getString("collector_name") ? rs.getString("collector_name") : "");
							dto.setDisputeCodeDesc(null != rs.getString("dispute_code_description")
									? rs.getString("dispute_code_description")
									: "");
							dto.setDisputeComments(
									null != rs.getString("dispute_comments") ? rs.getString("dispute_comments") : "");
							dto.setDisputeDate(
									null != rs.getString("dispute_date") ? rs.getString("dispute_date") : "");
							dto.setDisputeNumber(
									null != rs.getString("dispute_number") ? rs.getString("dispute_number") : "");
							dto.setDisputeOwner(
									null != rs.getString("dispute_owner") ? rs.getString("dispute_owner") : "");
							dto.setDisputeResolutionDate(null != rs.getString("dispute_resolution_date")
									? rs.getString("dispute_resolution_date")
									: "");
							dto.setCommitedOn(null != rs.getString("commited_on") ? rs.getString("commited_on") : "");
							dto.setDisputeStatus(
									null != rs.getString("dispute_status") ? rs.getString("dispute_status") : "");
							dto.setLatestAction(
									null != rs.getString("latest_action") ? rs.getString("latest_action") : "");
							dto.setCustomerSegment(
									null != rs.getString("customer_segment") ? rs.getString("customer_segment") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setPaymentTerm(
									null != rs.getString("payment_term") ? rs.getString("payment_term") : "");
							dto.setPo(null != rs.getString("po") ? rs.getString("po") : "");
							dto.setLeName(null != rs.getString("le_name") ? rs.getString("le_name") : "");
							dto.setCreditLimit(
									null != rs.getString("credit_limit") ? rs.getString("credit_limit") : "");
							dto.setDhl("");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public boolean saveOpenInvoiceDetails(List<OFESaveOpenInvoiceDTO> invoicesList, String sso) {
		boolean resultFlag = false, deleteFlag = false,deleteHstFlag = false;
		int count = 0;
		Connection con = null;
		try {
			if (AssertUtils.isListNotEmpty(invoicesList)) {
				con = jdbcTemplate.getDataSource().getConnection();
				deleteFlag = deleteOpenInvoiceDetails(invoicesList.get(0).getProjectId());
				deleteHstFlag = deleteHstInvoiceDetails(invoicesList.get(0).getProjectId());
				for (OFESaveOpenInvoiceDTO dto : invoicesList) {
					PreparedStatement pstm = con.prepareStatement(OfeBillingDashboardConstants.INSERT_OPEN_INVOICE_DETAILS);
					pstm.setString(1, dto.getProjectId());
					pstm.setString(2, dto.getInvoiceNumber());
					pstm.setString(3, dto.getRisk());
					pstm.setString(4, dto.getComments());
					pstm.setString(5, sso);
					pstm.setString(6, dto.getCollectorName());
					if (pstm.executeUpdate() > 0) {
						count++;
						resultFlag = true;
					}
				}
				
				for (OFESaveOpenInvoiceDTO dto : invoicesList) {
					PreparedStatement pstm1 = con.prepareStatement(OfeBillingDashboardConstants.INSERT_HST_INVOICE_DETAILS);
					pstm1.setString(1, dto.getProjectId());
					pstm1.setString(2, dto.getInvoiceNumber());
					pstm1.setString(3, dto.getRisk());
					pstm1.setString(4, dto.getComments());
					pstm1.setString(5, sso);
					pstm1.setString(6, dto.getCollectorName());
					if (pstm1.executeUpdate() > 0) {
						resultFlag = true;
					}
				}
				LOGGER.info("Inserted " + count + " rows for project id :: " + invoicesList.get(0).getProjectId());
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while saving cash invoices details:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("something went wrong while saving cash invoices details:{}" , e.getMessage());
				}
			}
		}
		return resultFlag;
	}

	private boolean deleteHstInvoiceDetails(String projectId) {
		boolean resultFlag = false;
		String count = "";
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			pstm = con.prepareStatement(OfeBillingDashboardConstants.DELETE_HST_INVOICE_DETAILS);
			pstm.setString(1, projectId);
			int result = pstm.executeUpdate();
			if (result > 0) {
				LOGGER.info("Deleted {}" , count ," rows for project id :: {} " , projectId);
				resultFlag = true;
			}

		} catch (SQLException e) {
			LOGGER.error("something went wrong while deleting History invoices details:{}" , e.getMessage());
		} finally {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LOGGER.error("something went wrong while deleting History invoices details:{}" , e.getMessage());
			}
		}
	}
		return resultFlag;
	}
	
	private boolean deleteOpenInvoiceDetails(String projectId) {
		boolean resultFlag = false;
		String count = "";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if (projectId != null && !projectId.isEmpty() && !projectId.equalsIgnoreCase("")) {
				con = jdbcTemplate.getDataSource().getConnection();

				pstm = con.prepareStatement(OfeBillingDashboardConstants.SELECT_OPEN_INVOICE_DETAILS);
				pstm.setString(1, projectId);
				rs = pstm.executeQuery();
				while (rs.next()) {
					count = rs.getString("count");
					LOGGER.info("Selected " + count + " rows for project id :: " + projectId);
				}
				if (projectId != null && !projectId.isEmpty() && count.equalsIgnoreCase("0")) {
					resultFlag = true;
				}
				pstm = con.prepareStatement(OfeBillingDashboardConstants.DELETE_OPEN_INVOICE_DETAILS);
				pstm.setString(1, projectId);
				int result = pstm.executeUpdate();
				if (result > 0) {
					LOGGER.info("Deleted {}" + count + " rows for project id :: {}" + projectId);
					resultFlag = true;
				}
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while deleting cash invoices details:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("something went wrong while deleting cash invoices details:{}" , e.getMessage());
				}
			}
		}
		return resultFlag;
	}

	@Override
	public String getOpenInvoiceLastSavedDate(String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_OPEN_INVOICE_SAVE_DATE, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = null != rs.getString(1) ? rs.getString(1) : "";
						}
						return date;
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getForecastCashCurve(String projectId, Map<String, Object> map) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_FORECAST_CASH_CURVE,
				new Object[] { projectId, projectId, projectId,projectId }, new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, OFECashCollectionCurveTableDTO> compareMap = null;
						List<OFECashCollectionCurveTableDTO> tableList = new ArrayList<OFECashCollectionCurveTableDTO>();
						List<OFEForecastCashDTO> list = new ArrayList<OFEForecastCashDTO>();
						OFECashCollectionCurveTableDTO compareDTO = new OFECashCollectionCurveTableDTO();
						if ((Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, OFECashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							OFEForecastCashDTO dto = new OFEForecastCashDTO();
							dto.setMilestoneId(null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							dto.setMilestoneDesc(null != rs.getString("milestone_description") ? rs.getString("milestone_description") : "");
							dto.setMilestoneAmount(null != rs.getString("milestone_amount") ? rs.getString("milestone_amount") : "");
							dto.setInvoiceAmount(null != rs.getString("inv_amount") ? rs.getString("inv_amount") : "");
							dto.setDisplayDate(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setCurrency(null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setMilestoneAmountUSD(null != rs.getString("amount_val") ? rs.getString("amount_val"): "");
							dto.setInvoiceAmountUSD(null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd"): "");
							dto.setInvoiceDate(null != rs.getString("invoice_dt") ? rs.getString("invoice_dt") : "");
							dto.setCurrencyCode(null != rs.getString("currency_code") ? rs.getString("currency_code") : "");
							dto.setStatus(null != rs.getString("milestone_status") ? rs.getString("milestone_status"):"") ;
							dto.setLinkStatus(null != rs.getString("link_status") ? rs.getString("link_status") : "");
							dto.setForecastSource(null != rs.getString("forecast_source") ? rs.getString("forecast_source"):""); 
							dto.setCumDispAmount(null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd") :""); 
							dto.setInvoiceNumber(null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setBaselineDt(null != rs.getString("estim_date") ? rs.getString("estim_date") : "");
							dto.setInvoiceDueDate(null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
							dto.setTaxWithholdingPercentage(null != rs.getString("tax_withholding_perc") ? rs.getString("tax_withholding_perc") : "");
							dto.setNetCashFcUSD(null != rs.getString("net_cash_fc_usd") ? rs.getString("net_cash_fc_usd") : "");

							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							if (!displayMonth.equalsIgnoreCase("")) {
								compareDTO = (OFECashCollectionCurveTableDTO) compareMap.get(displayMonth);
								if (null == compareDTO) {
									compareDTO = new OFECashCollectionCurveTableDTO();
									compareDTO.setDisplayDate(displayMonth);
									compareDTO.setForecastCash(cumDispAmt);
									compareMap.put(displayMonth, compareDTO);
								} else {
									compareDTO.setForecastCash(cumDispAmt);
								}
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((OFECashCollectionCurveTableDTO s1, OFECashCollectionCurveTableDTO s2) -> YearMonth
								.parse(StringUtils.capitalize(s1.getDisplayDate().toLowerCase()), formatter)
								.compareTo(YearMonth.parse(StringUtils.capitalize(s2.getDisplayDate().toLowerCase()),
										formatter)));
						map.put("forecastCash", list);
						map.put("compareMap", compareMap);
						map.put("tableList", tableList);
						return map;
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCollectedCashCurve(String projectId, Map<String, Object> map) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_COLLECTED_CASH_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, OFECashCollectionCurveTableDTO> compareMap = null;
						List<OFECashCollectionCurveTableDTO> tableList = new ArrayList<OFECashCollectionCurveTableDTO>();
						List<OFECollectedCashDTO> list = new ArrayList<OFECollectedCashDTO>();
						OFECashCollectionCurveTableDTO compareDTO = new OFECashCollectionCurveTableDTO();
						if ((Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, OFECashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							OFECollectedCashDTO dto = new OFECollectedCashDTO();
							dto.setCumDispAmount(
									null != rs.getString("cash_collected") ? rs.getString("cash_collected") : "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setBaselineDt(null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceDate(
									null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							dto.setInvoiceAmount(
									null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setInvoiceDueDate(
									null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
							dto.setCashCollected(
									null != rs.getString("cash_collected") ? rs.getString("cash_collected") : "");
							dto.setCashCollectionDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							dto.setConvertedAmountUSD(
									null != rs.getString("converted_amount_usd") ? rs.getString("converted_amount_usd")
											: "");
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							compareDTO = (OFECashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new OFECashCollectionCurveTableDTO();
								compareDTO.setDisplayDate(displayMonth);
								compareDTO.setCollectedCash(cumDispAmt);
								compareMap.put(displayMonth, compareDTO);
							} else {
								compareDTO.setCollectedCash(cumDispAmt);
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((OFECashCollectionCurveTableDTO s1, OFECashCollectionCurveTableDTO s2) -> YearMonth
								.parse(StringUtils.capitalize(s1.getDisplayDate().toLowerCase()), formatter)
								.compareTo(YearMonth.parse(StringUtils.capitalize(s2.getDisplayDate().toLowerCase()),
										formatter)));
						map.put("collectedCash", list);
						map.put("compareMap", compareMap);
						map.put("tableList", tableList);
						return map;
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOtrCashBaselineCurve(String projectId, Map<String, Object> map) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_OTR_CASH_BASELINE_CURVE, new Object[] {  projectId, projectId, projectId,
				projectId, projectId, projectId,projectId, projectId, projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, OFECashCollectionCurveTableDTO> compareMap = null;
						List<OFECashCollectionCurveTableDTO> tableList = new ArrayList<OFECashCollectionCurveTableDTO>();
						List<OFEOtrCashBaselineDTO> list = new ArrayList<OFEOtrCashBaselineDTO>();
						OFECashCollectionCurveTableDTO compareDTO = new OFECashCollectionCurveTableDTO();
						if ((Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, OFECashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							OFEOtrCashBaselineDTO dto = new OFEOtrCashBaselineDTO();
							dto.setCumDispAmount(
									null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd")
											: "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							//dto.setBaselineDt(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							//dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setMilestoneId(
									null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							dto.setMilestoneDesc(
									null != rs.getString("milestone_description") ? rs.getString("milestone_description") : "");
							dto.setMilestoneAmount(
									null != rs.getString("milestone_amount") ? rs.getString("milestone_amount") : "");
							dto.setMilestoneAmountUSD(
									null != rs.getString("amount_val") ? rs.getString("amount_val")
											: "");
							//dto.setInvoiceNumber(
									//null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							//dto.setInvoiceDate(
									//null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							//dto.setInvoiceAmount(
									//null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							//dto.setInvoiceAmountUSD(
							//		null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")	: "");
						//	dto.setInvoiceDueDate(
									//null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
							//dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							//dto.setConvertedAmountUSD(
								//	null != rs.getString("converted_amount_usd") ? rs.getString("converted_amount_usd")	: "");
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							compareDTO = (OFECashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new OFECashCollectionCurveTableDTO();
								compareDTO.setDisplayDate(displayMonth);
								compareDTO.setOtrCashBaseline(cumDispAmt);
								compareMap.put(displayMonth, compareDTO);
							} else {
								compareDTO.setOtrCashBaseline(cumDispAmt);
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((OFECashCollectionCurveTableDTO s1, OFECashCollectionCurveTableDTO s2) -> YearMonth
								.parse(StringUtils.capitalize(s1.getDisplayDate().toLowerCase()), formatter)
								.compareTo(YearMonth.parse(StringUtils.capitalize(s2.getDisplayDate().toLowerCase()),
										formatter)));
						map.put("otrCashBaseline", list);
						map.put("compareMap", compareMap);
						map.put("tableList", tableList);
						return map;
					}
				});
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getPastDueCurve(String projectId, Map<String, Object> map) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_PAST_DUE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, OFECashCollectionCurveTableDTO> compareMap = null;
						List<OFECashCollectionCurveTableDTO> tableList = new ArrayList<OFECashCollectionCurveTableDTO>();
						List<OFEPastDueCommitmentDTO> list = new ArrayList<OFEPastDueCommitmentDTO>();
						OFECashCollectionCurveTableDTO compareDTO = new OFECashCollectionCurveTableDTO();
						if ((Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, OFECashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, OFECashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							OFEPastDueCommitmentDTO dto = new OFEPastDueCommitmentDTO();
							dto.setCumDispAmount(null != rs.getString("display_invoice_amount_usd")
									? rs.getString("display_invoice_amount_usd")
									: "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setBaselineDt(null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setInvoiceDate(
									null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount_usd") ? rs.getString("invoice_amount_usd")
											: "");
							dto.setInvoiceDueDate(
									null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
							dto.setAging(null != rs.getString("aging") ? rs.getString("aging") : "");
							dto.setDisputeType(
									null != rs.getString("dispute_type") ? rs.getString("dispute_type") : "");
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							compareDTO = (OFECashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new OFECashCollectionCurveTableDTO();
								compareDTO.setDisplayDate(displayMonth);
								compareDTO.setPastDue(cumDispAmt);
								compareMap.put(displayMonth, compareDTO);
							} else {
								float cumDispAmount = 0.0f;
								if (compareDTO.getPastDue() != null) {
									cumDispAmount = Float.parseFloat(cumDispAmt)
											+ Float.parseFloat(compareDTO.getPastDue());
								} else {
									cumDispAmount = Float.parseFloat(cumDispAmt);
								}
								compareDTO.setPastDue(String.valueOf((Math.round(cumDispAmount * 100.0) / 100.0)));
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((OFECashCollectionCurveTableDTO s1, OFECashCollectionCurveTableDTO s2) -> YearMonth
								.parse(StringUtils.capitalize(s1.getDisplayDate().toLowerCase()), formatter)
								.compareTo(YearMonth.parse(StringUtils.capitalize(s2.getDisplayDate().toLowerCase()),
										formatter)));
						map.put("pastDue", list);
						map.put("compareMap", compareMap);
						map.put("tableList", tableList);
						return map;
					}
				});
	}

	@Override
	public List<ForecastChartDTO> getForecastChart(String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_FORECAST_CHART, new Object[] { projectId,projectId,projectId,projectId,projectId },
				new ResultSetExtractor<List<ForecastChartDTO>>() {
					public List<ForecastChartDTO> extractData(ResultSet rs) throws SQLException {
						List<ForecastChartDTO> list = new ArrayList<ForecastChartDTO>();
						while (rs.next()) {
							ForecastChartDTO dto = new ForecastChartDTO();
							dto.setMilestoneId(null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							dto.setInvoiceNumber(null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setEstimCashDate(null != rs.getString("estim_cash_date") ? rs.getString("estim_cash_date") : "");
							dto.setOrigAmount(null != rs.getString("orig_amount") ? rs.getString("orig_amount") : "");
							dto.setAmountInUsd(null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd") : "");
							dto.setDescription(null != rs.getString("milestone_description") ? rs.getString("milestone_description") : "");
							dto.setInvoiceDt(null != rs.getString("invoice_dt") ? rs.getString("invoice_dt") : "");
							dto.setCurrencyCode(null != rs.getString("inv_currency_code") ? rs.getString("inv_currency_code") : "");
							dto.setMilestoneStatus(null != rs.getString("milestone_status") ? rs.getString("milestone_status") : "");
							dto.setLinkStatus(null != rs.getString("link_status") ? rs.getString("link_status") : "");
							dto.setForecastSource(null != rs.getString("forecast_source") ? rs.getString("forecast_source") : "");
							dto.setTaxWithholdingPercentage(null != rs.getString("tax_withholding_perc") ? rs.getString("tax_withholding_perc") : "");
							dto.setNetCashFcUSD(null != rs.getString("net_cash_fc_usd") ? rs.getDouble("net_cash_fc_usd") : 0);
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public String getEditAccessBillingWidget(String sso) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_EDIT_ACCESS_BILLING_WIDGET,
				new Object[] {sso},
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String flag = "false";
						while (rs.next()) {
							if(rs.getString(1).equalsIgnoreCase("Y")) {
								flag = "true";	
							}
									
						}
						return flag;
					}
				});
	}

	@Override
	public int editInvoiceList(List<InvoiceListDTO> invoiceDto, String sso) {
		int result = 0;
		ResultSet rs = null;
		Connection con = null;
		try {
			if (AssertUtils.isListNotEmpty(invoiceDto)) {
				con = jdbcTemplate.getDataSource().getConnection();
				for (InvoiceListDTO dto : invoiceDto) {
					PreparedStatement pstm = con.prepareStatement(OfeBillingDashboardConstants.INVOICE_TABLE);
					pstm.setString(1, dto.getProjectId());
					pstm.setString(2, sso);
					pstm.setString(3, dto.getEstimCashDate());
					pstm.setString(4, dto.getActivityId());
					pstm.setString(5, dto.getInvoiceNumber());
					pstm.setString(6, dto.getInvoiceLine());
					rs = pstm.executeQuery();
					while (rs.next()) {
						result = rs.getInt(1);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while SAVING INVOICE DETAILS:{}" ,e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public int editMilestoneList(List<MilestoneListDTO> milestoneDto, String sso) {
		int result = 0;
		ResultSet rs = null;
		Connection con = null;
		try {
			if (AssertUtils.isListNotEmpty(milestoneDto)) {
				con = jdbcTemplate.getDataSource().getConnection();
				for (MilestoneListDTO dto : milestoneDto) {
					PreparedStatement pstm = con.prepareStatement(OfeBillingDashboardConstants.MILESTONE_TABLE);
					pstm.setString(1, dto.getProjectId());
					pstm.setString(2, sso);
					pstm.setString(3, dto.getEstimBillDate());
					pstm.setString(4, dto.getActivityId());
					pstm.setString(5, dto.getSubProjectId());
					pstm.setString(6, dto.getRisk());
					pstm.setString(7, dto.getComment());
					pstm.setString(8, dto.getLinkInvoiceNumber());
					rs = pstm.executeQuery();
					while (rs.next()) {
						result = rs.getInt(1);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while SAVING MILESTONE DETAILS:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public String getBMVLastRefreshDate() {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_BMV_LAST_REFRESH_DATE, new Object[] { },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String date = "";
						while (rs.next()) {
							date = null != rs.getString(1) ? rs.getString(1) : "";
						}
						return date;
					}
				});
	}

	@Override
	public List<String> getScurveFunctionCode(String projectId, String published) {
		
		if(published.equalsIgnoreCase("true")) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_SCURVE_FUNC_CODE,
				new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							list.add(null != rs.getString(1) ? rs.getString(1) : "");	
						}
						return list;
					}
				});
		}else {
			return jdbcTemplate.query(OfeBillingDashboardConstants.GET_SCURVE_FUNC_FALSE_CODE,
					new Object[] { projectId },
					new ResultSetExtractor<List<String>>() {
						public List<String> extractData(ResultSet rs) throws SQLException {
							List<String> list = new ArrayList<String>();
							while (rs.next()) {
								list.add(null != rs.getString(1) ? rs.getString(1) : "");	
							}
							return list;
						}
					});
		}
	}

	@Override
	public List<DropDownDTO> getScurveProductLineCode(String projectId, String published) {
		
		if(published.equalsIgnoreCase("true")) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_SCURVE_PRODUCT_LINES, new Object[] { projectId },
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setVal(null != rs.getString(2) ? rs.getString(2) : "");
							list.add(dto);
						}
						return list;
					}
				});
		}else {
			return jdbcTemplate.query(OfeBillingDashboardConstants.GET_SCURVE_FALSE_PRODUCT_LINES, new Object[] { projectId },
					new ResultSetExtractor<List<DropDownDTO>>() {
						public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
							List<DropDownDTO> list = new ArrayList<DropDownDTO>();
							while (rs.next()) {
								DropDownDTO dto = new DropDownDTO();
								dto.setKey(null != rs.getString(1) ? rs.getString(1) : "");
								dto.setVal(null != rs.getString(2) ? rs.getString(2) : "");
								list.add(dto);
							}
							return list;
						}
					});
		}
	}

	@Override
	public List<String> getScurveProjectCode(String projectId, String published) {
		
		if(published.equalsIgnoreCase("true")) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_SCURVE_PROJECT_CODE,
				new Object[] { projectId },
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							list.add(null != rs.getString(1) ? rs.getString(1) : "");	
						}
						return list;
					}
				});
		}else {

			return jdbcTemplate.query(OfeBillingDashboardConstants.GET_SCURVE_PROJECT_FALSE_CODE,
					new Object[] { projectId },
					new ResultSetExtractor<List<String>>() {
						public List<String> extractData(ResultSet rs) throws SQLException {
							List<String> list = new ArrayList<String>();
							while (rs.next()) {
								list.add(null != rs.getString(1) ? rs.getString(1) : "");	
							}
							return list;
						}
					});
			
			
		}
	}

	@Override
	public DashboardCountDTO getDashboardColorCount(String sso,int customerId,int companyId, String warrantyFlag) {
		
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_DASHBOARD_RED_FLAG_COUNT,
				new Object[] {sso,companyId, customerId,warrantyFlag},
				new ResultSetExtractor<DashboardCountDTO>() {
					public DashboardCountDTO extractData(ResultSet rs) throws SQLException {
						DashboardCountDTO dto = new DashboardCountDTO();
						while (rs.next()) {
							dto.setRedFlagCount(null != rs.getString(1) ? rs.getString(1) : "0");
							dto.setProjectsCount(null != rs.getString(2) ? rs.getString(2) : "0");
						}
						return dto;
					}
				});
	}

	@Override
	public DashboardCountDTO getDashboardWarrantyFlagCount(String sso, int customerId, int companyId,
			String warrantyFlag) {
		
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_DASHBOARD_WARRANTY_FLAG_COUNT,
				new Object[] {sso,companyId, customerId,warrantyFlag},
				new ResultSetExtractor<DashboardCountDTO>() {
					public DashboardCountDTO extractData(ResultSet rs) throws SQLException {
						DashboardCountDTO dto = new DashboardCountDTO();
						while (rs.next()) {
							dto.setRedFlagCount(null != rs.getString(1) ? rs.getString(1) : "0");
							dto.setProjectsCount(null != rs.getString(2) ? rs.getString(2) : "0");
						}
						return dto;
					}
				});
	}

	@Override
	public List<InvoiceDropdownListDTO> getInvoiceDropDownList(String projectId) {
		return jdbcTemplate.query(OfeBillingDashboardConstants.GET_INVOICE_DROPDOWN_LIST, new Object[] { projectId,projectId,projectId },
				new ResultSetExtractor<List<InvoiceDropdownListDTO>>() {
					public List<InvoiceDropdownListDTO> extractData(ResultSet rs) throws SQLException {
						List<InvoiceDropdownListDTO> list = new ArrayList<InvoiceDropdownListDTO>();
						while (rs.next()) {
							InvoiceDropdownListDTO dto = new InvoiceDropdownListDTO();
							dto.setInvNumber(rs.getString("invoice_number"));
							dto.setInvDesc(rs.getString("invoice_description"));
							dto.setInvDate(rs.getString("invoice_date"));
							dto.setInvAmount(rs.getString("inv_amount"));
							dto.setInvCurrency(rs.getString("inv_currency_code"));
							
							list.add(dto);
						}
						return list;
					}
				});
	}
}