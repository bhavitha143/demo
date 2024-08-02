package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.ServerErrorException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IBillingDAO;
import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.ActivitiesPopupDTO;
import com.bh.realtrack.dto.ActualCurveDTO;
import com.bh.realtrack.dto.AllMilestonesDTO;
import com.bh.realtrack.dto.BaselineCurveDTO;
import com.bh.realtrack.dto.BillingSummaryDTO;
import com.bh.realtrack.dto.BlankBaselineDateDTO;
import com.bh.realtrack.dto.BlankBaselineDatePopupDTO;
import com.bh.realtrack.dto.CashCollectionCurveTableDTO;
import com.bh.realtrack.dto.CashCollectionInvoicesDetails;
import com.bh.realtrack.dto.CollectedCashDTO;
import com.bh.realtrack.dto.CurveTable;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ExchangeRateDTO;
import com.bh.realtrack.dto.FinancialBLCurveDTO;
import com.bh.realtrack.dto.ForecastCashDTO;
import com.bh.realtrack.dto.ForecastCurveDTO;
import com.bh.realtrack.dto.InvoiceDetailDTO;
import com.bh.realtrack.dto.ItoBaselineCurveDTO;
import com.bh.realtrack.dto.ItoCashBaselineDTO;
import com.bh.realtrack.dto.MilestonesDTO;
import com.bh.realtrack.dto.MilestonesToBillDTO;
import com.bh.realtrack.dto.NextToBillDTO;
import com.bh.realtrack.dto.OpenInvoiceChartDTO;
import com.bh.realtrack.dto.OpenInvoiceChartPopupDetails;
import com.bh.realtrack.dto.OpenInvoiceDTO;
import com.bh.realtrack.dto.OpenInvoiceDataTable;
import com.bh.realtrack.dto.OtrCashBaselineDTO;
import com.bh.realtrack.dto.PODetailsDTO;
import com.bh.realtrack.dto.PastDueCommitmentDTO;
import com.bh.realtrack.dto.SaveOpenInvoiceDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.AssertUtils;
import com.bh.realtrack.util.BillingConstants;
import com.bh.realtrack.util.BillingPubConstants;

@Repository
public class BillingDAOImpl implements IBillingDAO {
	private static Logger log = LoggerFactory.getLogger(BillingDAOImpl.class.getName());
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<NextToBillDTO> getNextToBillPopup(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_NEXT_TO_BILL, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<NextToBillDTO>>() {
					public List<NextToBillDTO> extractData(ResultSet rs) throws SQLException {
						List<NextToBillDTO> list = new ArrayList<NextToBillDTO>();
						while (rs.next()) {
							NextToBillDTO milestoneDTO = new NextToBillDTO();
							milestoneDTO.setMilestoneId(null != rs.getString(1) ? rs.getString(1) : "");
							milestoneDTO.setMilestoneDesc(null != rs.getString(2) ? rs.getString(2) : "");
							milestoneDTO.setOrigAmount(null != rs.getString(3) ? rs.getString(3) : "");
							milestoneDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							milestoneDTO.setConvertedAmount(null != rs.getString(5) ? rs.getString(5) : "");
							milestoneDTO.setStatus(null != rs.getString(6) ? rs.getString(6) : "");
							milestoneDTO.setForecastDate(null != rs.getString(7) ? rs.getString(7) : "");
							list.add(milestoneDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<BillingSummaryDTO> getBillingSummary(String projectId) {
		List<BillingSummaryDTO> list = new ArrayList<BillingSummaryDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingConstants.GET_BILLING_SUMMARY)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				BillingSummaryDTO summaryDTO = new BillingSummaryDTO();
				summaryDTO.setBilled(rs.getDouble("o_billed"));
				summaryDTO.setContractValuePerct(rs.getDouble("o_contract_value_perct"));
				summaryDTO.setInDays(rs.getInt("o_in_days"));
				summaryDTO.setNextToBill(rs.getDouble("o_next_to_bill"));
				summaryDTO.setForecastDt(null != rs.getString("o_forecast_dt") ? rs.getString("o_forecast_dt") : "");
				summaryDTO.setCycleTimeP75(
						null != rs.getString("o_cycle_time_p75") ? rs.getString("o_cycle_time_p75") : "");
				summaryDTO.setBgRequired(null != rs.getString("o_required") ? rs.getString("o_required") : "");
				summaryDTO.setBgIssued(null != rs.getString("o_issued") ? rs.getString("o_issued") : "");
				summaryDTO.setNotBilled(null != rs.getString("o_not_billed") ? rs.getString("o_not_billed") : "");
				list.add(summaryDTO);
			}
		} catch (SQLException e) {
			log.error("Exception while getting billing summary details :: {}", e.getMessage());
		}
		return list;
	}

	@Override
	public List<MilestonesToBillDTO> getMilestonesToBill(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_MILESTONES_TO_BILL, new Object[] { projectId },
				new ResultSetExtractor<List<MilestonesToBillDTO>>() {
					public List<MilestonesToBillDTO> extractData(ResultSet rs) throws SQLException {
						List<MilestonesToBillDTO> list = new ArrayList<MilestonesToBillDTO>();
						while (rs.next()) {
							MilestonesToBillDTO milestoneDTO = new MilestonesToBillDTO();
							milestoneDTO.setMilestoneId(
									null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							milestoneDTO.setProrataDesc(
									null != rs.getString("prorata_desc") ? rs.getString("prorata_desc") : "");
							milestoneDTO.setAmount(null != rs.getString("amount") ? rs.getString("amount") : "");
							milestoneDTO.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							milestoneDTO
									.setAmountUsd(null != rs.getString("amount_usd") ? rs.getString("amount_usd") : "");
							milestoneDTO.setCalcStatus(
									null != rs.getString("calc_status") ? rs.getString("calc_status") : "");
							milestoneDTO.setBaselineDt(
									null != rs.getString("baseline_dt") ? rs.getString("baseline_dt") : "");
							milestoneDTO.setForecastDt(
									null != rs.getString("forecast_date") ? rs.getString("forecast_date") : "");
							milestoneDTO.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
							milestoneDTO.setPublishedForecastDt(null != rs.getString("published_forecast_dt")
									? rs.getString("published_forecast_dt")
									: "");
							milestoneDTO.setEdit("");
							milestoneDTO.setDetails("");
							milestoneDTO.setBankGuaranteeFlag(
									null != rs.getString("bank_guarantee_flag") ? rs.getString("bank_guarantee_flag")
											: "");
							milestoneDTO
									.setRiskOppty(null != rs.getString("risk_oppty") ? rs.getString("risk_oppty") : "");
							list.add(milestoneDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<AllMilestonesDTO> getAllMilestones(String projectId) {
		List<AllMilestonesDTO> list = new ArrayList<AllMilestonesDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingConstants.GET_ALL_MILESTONES)) {
			pstm.setString(1, projectId);

			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				AllMilestonesDTO milestoneDTO = new AllMilestonesDTO();
				milestoneDTO.setMilestoneId(
						null != rs.getString("milestone_id_out") ? rs.getString("milestone_id_out") : "");
				milestoneDTO.setProrataDesc(
						null != rs.getString("prorata_desc_out") ? rs.getString("prorata_desc_out") : "");
				milestoneDTO.setAmount(null != rs.getString("amount_out") ? rs.getString("amount_out") : "");
				milestoneDTO.setCurrency(null != rs.getString("currency_out") ? rs.getString("currency_out") : "");
				milestoneDTO.setAmountUsd(null != rs.getString("amount_usd_out") ? rs.getString("amount_usd_out") : "");
				milestoneDTO
						.setCalcStatus(null != rs.getString("calc_status_out") ? rs.getString("calc_status_out") : "");
				milestoneDTO
						.setBaselineDt(null != rs.getString("baseline_dt_out") ? rs.getString("baseline_dt_out") : "");
				milestoneDTO.setForecastDt(
						null != rs.getString("forecast_date_out") ? rs.getString("forecast_date_out") : "");
				milestoneDTO.setComments(null != rs.getString("comments_out") ? rs.getString("comments_out") : "");
				milestoneDTO.setDetails(null != rs.getString("details_out") ? rs.getString("details_out") : "");
				milestoneDTO.setInvoiceNumber(
						null != rs.getString("invoice_number_out") ? rs.getString("invoice_number_out") : "");
				milestoneDTO.setActualInvoiceDt(
						null != rs.getString("actual_invoice_dt_out") ? rs.getString("actual_invoice_dt_out") : "");
				milestoneDTO.setPmRequestDt(
						null != rs.getString("pm_request_dt_out") ? rs.getString("pm_request_dt_out") : "");
				milestoneDTO.setPublishedForecastDt(
						null != rs.getString("published_forecast_dt_out") ? rs.getString("published_forecast_dt_out")
								: "");
				milestoneDTO.setInvoiceAmount(
						null != rs.getString("invoice_amount_out") ? rs.getString("invoice_amount_out") : "");
				milestoneDTO.setBankGuaranteeFlag(
						null != rs.getString("bank_guarantee_flag_out") ? rs.getString("bank_guarantee_flag_out") : "");
				milestoneDTO.setBillingCT(null != rs.getString("billing_ct_out") ? rs.getString("billing_ct_out") : "");
				milestoneDTO.setMaxPredessorDt(
						null != rs.getString("max_actual_finish_dt_out") ? rs.getString("max_actual_finish_dt_out")
								: "");
				milestoneDTO.setMilestoneCausal(
						null != rs.getString("milestone_causal_out") ? rs.getString("milestone_causal_out") : "");
				milestoneDTO.setCollectionStatus(null != rs.getString("status_out") ? rs.getString("status_out") : "");
				milestoneDTO.setInvoiceDueDate(
						null != rs.getString("invoice_due_date_out") ? rs.getString("invoice_due_date_out") : "");

				milestoneDTO.setCollectionDate(
						null != rs.getString("collection_date_out") ? rs.getString("collection_date_out") : "");

				milestoneDTO.setCashCollected(
						null != rs.getString("cash_collected$_out") ? rs.getString("cash_collected$_out") : "");

				milestoneDTO.setOutstandingAmount(
						null != rs.getString("outstanding_amount_out") ? rs.getString("outstanding_amount_out") : "");

				list.add(milestoneDTO);

			}
		} catch (SQLException e) {
			log.error("Exception in BillingDAOImpl::getAllMilestones() : " + e.getMessage());
		}

		return list;
	}

	@Override
	public List<ForecastCurveDTO> getForecastCurve(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_FORECAST_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<ForecastCurveDTO>>() {
					public List<ForecastCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ForecastCurveDTO> list = new ArrayList<ForecastCurveDTO>();
						while (rs.next()) {
							ForecastCurveDTO curveDTO = new ForecastCurveDTO();
							curveDTO.setCashMilestoneActivityId(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setProrataDesc(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setConvertedAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setDisplayCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							curveDTO.setOracleForecastDt(null != rs.getString(8) ? rs.getString(8) : "");
							curveDTO.setInvoiceNumber(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setDisplaydate(null != rs.getString(10) ? rs.getString(10) : "");
							curveDTO.setEpochDate(rs.getDouble(11));
							curveDTO.setCumDispAmount(rs.getDouble(12));
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ActualCurveDTO> getActualCurve(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_ACTUAL_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<ActualCurveDTO>>() {
					public List<ActualCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ActualCurveDTO> list = new ArrayList<ActualCurveDTO>();
						while (rs.next()) {
							ActualCurveDTO curveDTO = new ActualCurveDTO();
							curveDTO.setProrataDesc(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setCashMilestoneActivityId(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setAggOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setAggConvertedAmount(
									null != rs.getString(4) ? formatString(rs.getString(4)) : "");
							curveDTO.setInvoiceNumber(null != rs.getString(5) ? rs.getString(5) : "");
							curveDTO.setCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setInvoicedate(null != rs.getString(7) ? rs.getString(7) : "");
							curveDTO.setConvertedAmount(null != rs.getString(8) ? formatString(rs.getString(8)) : "");
							curveDTO.setMilestoneStatus(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setEpochDate(rs.getDouble(10));
							curveDTO.setCumDispAmount(rs.getDouble(11));
							curveDTO.setOrigAmount(null != rs.getString(12) ? formatString(rs.getString(12)) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	public Date getDateInSqlFormat(String sDate) {
		Date sqlDate = null;

		if (!sDate.equals("")) {
			SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
			Date date = null;
			try {
				date = sdf1.parse(sDate);
				sqlDate = new Date(date.getTime());
			} catch (ParseException e) {
			}
		}
		return sqlDate;
	}

	@Override
	public int updateMilestoneDetails(List<MilestonesDTO> milestonesList, String sso) {
		int result = 0;
		if (AssertUtils.isListNotEmpty(milestonesList)) {
			Connection con = null;
			for (MilestonesDTO dto : milestonesList) {
				log.info("Activity from Post call  {} ", dto.getCashMilestoneActivityId());
				log.info("Comments from Post call   {}", dto.getComments());
				log.info("Risk/Oppty from Post call  {} ", dto.getRiskOppty());
				log.info("Forecast Dt from Post call  {} ", dto.getForecastDt());
			}
			try {
				con = jdbcTemplate.getDataSource().getConnection();
				for (MilestonesDTO dto : milestonesList) {
					PreparedStatement pstm = con.prepareStatement(BillingConstants.UPDATE_MILESTONE_DETAILS);
					pstm.setDate(1, AssertUtils.validateDate(dto.getForecastDt()));
					pstm.setString(2, dto.getComments());
					pstm.setString(3, dto.getRiskOppty());
					pstm.setDate(4, AssertUtils.validateDate(dto.getForecastDt()));
					pstm.setDate(5, AssertUtils.validateDate(dto.getForecastDt()));
					pstm.setString(6, dto.getCashMilestoneActivityId());
					pstm.setString(7, dto.getProjectId());
					pstm.setDate(8, AssertUtils.validateDate(dto.getForecastDt()));
					pstm.setDate(9, AssertUtils.validateDate(dto.getForecastDt()));
					pstm.setString(10, sso);
					pstm.setString(11, dto.getProjectId());
					pstm.setString(12, dto.getCashMilestoneActivityId());
					if (pstm.executeUpdate() > 0) {
						result = 1;
					}
				}
			} catch (Exception e) {
				log.error("something went wrong while SAVING MILESTONE DETAILS:{}", e.getMessage());
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
		} else {
			result = 1;
		}
		return result;

	}

	@Override
	public String publishMilestoneDetails(String projectId, String sso, List<String> changedPubMilestoneIds) {
		Array changedPubMilestoneIdsArr = null;
		String returnValue = "";
		ResultSet rs = null;

		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con.prepareStatement(BillingConstants.PUBLISH_MILESTONE_DETAILS)) {
			if (changedPubMilestoneIds.size() != 0) {
				changedPubMilestoneIdsArr = con.createArrayOf("text", changedPubMilestoneIds.toArray());
			}
			log.info("Publish Milestone Ids :: {}", changedPubMilestoneIdsArr);

			ps.setString(1, projectId);
			ps.setString(2, sso);
			ps.setArray(3, changedPubMilestoneIdsArr);

			rs = ps.executeQuery();

			while (rs.next()) {
				returnValue = rs.getString(1);
			}
		} catch (Exception e) {
			log.error("something went wrong while PUBLISHING MILESTONE DETAILS: {}", e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		}
		return returnValue;
	}

	@Override
	public List<CurveTable> getBillingTable(String projectId) {
		List<String> listOfGraphDates = new ArrayList<String>();
		return jdbcTemplate.query(BillingConstants.GET_REPORT_TABLE,
				new Object[] { projectId, projectId, projectId, projectId, projectId, projectId, projectId, projectId,
						projectId, projectId, projectId, projectId, projectId },
				new ResultSetExtractor<List<CurveTable>>() {
					public List<CurveTable> extractData(ResultSet rs) throws SQLException {
						List<CurveTable> list = new ArrayList<CurveTable>();
						while (rs.next()) {
							if (null != rs.getString(1)) {
								String graphDate = rs.getString(1);
								if (!listOfGraphDates.contains(graphDate)) {
									listOfGraphDates.add(graphDate);
								}
							}
							CurveTable curveDTO = new CurveTable();
							curveDTO.setGraphDt(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setForecastAmount(null != rs.getString(2) ? rs.getString(2) : "0.00");
							curveDTO.setActualAmount(null != rs.getString(3) ? rs.getString(3) : "0.00");
							curveDTO.setBaselineAmount(null != rs.getString(4) ? rs.getString(4) : "0.00");
							curveDTO.setPubForecastAmount(null != rs.getString(5) ? rs.getString(5) : "0.00");
							curveDTO.setFinBlAmount(null != rs.getString(6) ? rs.getString(6) : "0.00");
							curveDTO.setBlankBlForecastAmount(null != rs.getString(7) ? rs.getString(7) : "0.00");
							curveDTO.setItoBillingAmount(null != rs.getString(8) ? rs.getString(8) : "0.00");
							list.add(curveDTO);

						}

						List<CurveTable> listOfFilteredTable = filterTableList(listOfGraphDates, list);
						return listOfFilteredTable;
					}
				});
	}

	public List<CurveTable> filterTableList(List<String> listOfGraphDates, List<CurveTable> tableList) {
		List<CurveTable> filteredTableList = new ArrayList<CurveTable>();
		Map<String, CurveTable> tableMap = new LinkedHashMap<String, CurveTable>();

		listOfGraphDates.forEach(graphDate -> {

			List<CurveTable> listOfSameGraphDates = tableList.stream()
					.filter(tableObj -> tableObj.getGraphDt().equalsIgnoreCase(graphDate)).collect(Collectors.toList());

			listOfSameGraphDates.forEach(curveObj -> {
				if (!tableMap.containsKey(graphDate)) {
					tableMap.put(graphDate, curveObj);
				} else {
					CurveTable table = tableMap.get(graphDate);
					if (Double.valueOf(curveObj.getActualAmount()) != 0.00) {
						table.setActualAmount(curveObj.getActualAmount());
					}
					if (Double.valueOf(curveObj.getForecastAmount()) != 0.00) {
						table.setForecastAmount(curveObj.getForecastAmount());
					}
					if (Double.valueOf(curveObj.getBaselineAmount()) != 0.00) {
						table.setBaselineAmount(curveObj.getBaselineAmount());
					}
					if (Double.valueOf(curveObj.getPubForecastAmount()) != 0.00) {
						table.setPubForecastAmount(curveObj.getPubForecastAmount());
					}
					if (Double.valueOf(curveObj.getFinBlAmount()) != 0.00) {
						table.setFinBlAmount(curveObj.getFinBlAmount());
					}
					if (Double.valueOf(curveObj.getBlankBlForecastAmount()) != 0.00) {
						table.setBlankBlForecastAmount(curveObj.getBlankBlForecastAmount());
					}
					if (Double.valueOf(curveObj.getItoBillingAmount()) != 0.00) {
						table.setItoBillingAmount(curveObj.getItoBillingAmount());
					}

					tableMap.put(graphDate, table);
				}
			});
		});
		filteredTableList.addAll(tableMap.values());
		return filteredTableList;
	}

	@Override
	public List<BaselineCurveDTO> getBaselineCurve(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_BASELINE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<BaselineCurveDTO>>() {
					public List<BaselineCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<BaselineCurveDTO> list = new ArrayList<BaselineCurveDTO>();
						while (rs.next()) {
							BaselineCurveDTO curveDTO = new BaselineCurveDTO();
							curveDTO.setCashMilestoneActivityId(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setProrataDesc(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setConvertedAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setDisplayCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							curveDTO.setDisplaydate(null != rs.getString(8) ? rs.getString(8) : "");
							curveDTO.setEpochDate(rs.getDouble(9));
							curveDTO.setCumDispAmount(rs.getDouble(10));
							curveDTO.setAggInvoiceNm(null != rs.getString(11) ? rs.getString(11) : "");
							curveDTO.setAggInvoiceDt(null != rs.getString(12) ? rs.getString(12) : "");
							curveDTO.setAggInvoiceAmount(
									null != rs.getString(13) ? formatString(rs.getString(13)) : "");
							curveDTO.setAggInvoiceAmountUsd(
									null != rs.getString(14) ? formatString(rs.getString(14)) : "");
							curveDTO.setForecastDt(null != rs.getString(15) ? rs.getString(15) : "");
							curveDTO.setBaselineDt(null != rs.getString(16) ? rs.getString(16) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ItoBaselineCurveDTO> getItoBaselineCurve(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_ITO_BASELINE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<ItoBaselineCurveDTO>>() {
					public List<ItoBaselineCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ItoBaselineCurveDTO> itoBaselineCurveList = new ArrayList<ItoBaselineCurveDTO>();

						while (rs.next()) {
							ItoBaselineCurveDTO itoBaselineCurveObj = new ItoBaselineCurveDTO();
							itoBaselineCurveObj.setDisplaydate(
									null != rs.getString(1) ? rs.getString(1) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveObj.setDisplayCurrency(
									null != rs.getString(2) ? rs.getString(2) : BillingConstants.EMPTY_STRING);
							if (null != rs.getString(3) && BillingConstants.EMPTY_STRING != rs.getString(3)) {
								itoBaselineCurveObj.setCumDispAmount(Double.valueOf(rs.getString(3)));
							}
							itoBaselineCurveObj.setBaselineDt(
									null != rs.getString(4) ? rs.getString(4) : BillingConstants.EMPTY_STRING);
							if (null != rs.getString(5) && BillingConstants.EMPTY_STRING != rs.getString(5)) {
								itoBaselineCurveObj.setEpochDate(Double.valueOf(rs.getString(5)));
							}
							itoBaselineCurveObj.setConvertedAmount(String.valueOf(rs.getInt(6)));
							itoBaselineCurveObj.setOrigAmount(
									null != rs.getString(7) ? rs.getString(7) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveObj.setOrigCurrency(
									null != rs.getString(8) ? rs.getString(8) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveObj.setUsdPortion(
									null != rs.getString(9) ? rs.getString(9) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveObj.setEurPortion(
									null != rs.getString(10) ? rs.getString(10) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveObj.setMilestoneAmount(
									null != rs.getString(10) ? rs.getString(11) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveObj.setCurrency(
									null != rs.getString(12) ? rs.getString(12) : BillingConstants.EMPTY_STRING);
							itoBaselineCurveList.add(itoBaselineCurveObj);
						}
						return itoBaselineCurveList;
					}
				});
	}

	@Override
	public List<NextToBillDTO> getPublishNextToBillPopup(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_NEXT_TO_BILL, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<NextToBillDTO>>() {
					public List<NextToBillDTO> extractData(ResultSet rs) throws SQLException {
						List<NextToBillDTO> list = new ArrayList<NextToBillDTO>();
						while (rs.next()) {
							NextToBillDTO milestoneDTO = new NextToBillDTO();
							milestoneDTO.setMilestoneId(null != rs.getString(1) ? rs.getString(1) : "");
							milestoneDTO.setMilestoneDesc(null != rs.getString(2) ? rs.getString(2) : "");
							milestoneDTO.setOrigAmount(null != rs.getString(3) ? rs.getString(3) : "");
							milestoneDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							milestoneDTO.setConvertedAmount(null != rs.getString(5) ? rs.getString(5) : "");
							milestoneDTO.setStatus(null != rs.getString(6) ? rs.getString(6) : "");
							milestoneDTO.setForecastDate(null != rs.getString(7) ? rs.getString(7) : "");
							list.add(milestoneDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<BillingSummaryDTO> getPublishBillingSummary(String projectId) {
		List<BillingSummaryDTO> list = new ArrayList<BillingSummaryDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPubConstants.GET_PUB_BILLING_SUMMARY)) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				BillingSummaryDTO summaryDTO = new BillingSummaryDTO();
				summaryDTO.setBilled(rs.getDouble("o_billed"));
				summaryDTO.setContractValuePerct(rs.getDouble("o_contract_value_perct"));
				summaryDTO.setInDays(rs.getInt("o_in_days"));
				summaryDTO.setNextToBill(rs.getDouble("o_next_to_bill"));
				summaryDTO.setForecastDt(null != rs.getString("o_forecast_dt") ? rs.getString("o_forecast_dt") : "");
				summaryDTO.setCycleTimeP75(
						null != rs.getString("o_cycle_time_p75") ? rs.getString("o_cycle_time_p75") : "");
				summaryDTO.setBgRequired(null != rs.getString("o_required") ? rs.getString("o_required") : "");
				summaryDTO.setBgIssued(null != rs.getString("o_issued") ? rs.getString("o_issued") : "");
				summaryDTO.setNotBilled(null != rs.getString("o_not_billed") ? rs.getString("o_not_billed") : "");
				list.add(summaryDTO);
			}
		} catch (SQLException e) {
			log.error("Exception while getting publish billing summary details :: {}", e.getMessage());
		}
		return list;
	}

	@Override
	public List<MilestonesToBillDTO> getPublishMilestonesToBill(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_MILESTONES_TO_BILL, new Object[] { projectId },
				new ResultSetExtractor<List<MilestonesToBillDTO>>() {
					public List<MilestonesToBillDTO> extractData(ResultSet rs) throws SQLException {
						List<MilestonesToBillDTO> list = new ArrayList<MilestonesToBillDTO>();
						while (rs.next()) {
							MilestonesToBillDTO milestoneDTO = new MilestonesToBillDTO();
							milestoneDTO.setMilestoneId(
									null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							milestoneDTO.setProrataDesc(
									null != rs.getString("prorata_desc") ? rs.getString("prorata_desc") : "");
							milestoneDTO.setAmount(null != rs.getString("amount") ? rs.getString("amount") : "");
							milestoneDTO.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							milestoneDTO
									.setAmountUsd(null != rs.getString("amount_usd") ? rs.getString("amount_usd") : "");
							milestoneDTO.setCalcStatus(
									null != rs.getString("calc_status") ? rs.getString("calc_status") : "");
							milestoneDTO.setBaselineDt(
									null != rs.getString("baseline_dt") ? rs.getString("baseline_dt") : "");
							milestoneDTO.setForecastDt(
									null != rs.getString("forecast_date") ? rs.getString("forecast_date") : "");
							milestoneDTO.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
							milestoneDTO.setPublishedForecastDt(null != rs.getString("published_forecast_dt")
									? rs.getString("published_forecast_dt")
									: "");
							milestoneDTO.setEdit("");
							milestoneDTO.setDetails("");
							milestoneDTO.setBankGuaranteeFlag(
									null != rs.getString("bank_guarantee_flag") ? rs.getString("bank_guarantee_flag")
											: "");
							milestoneDTO
									.setRiskOppty(null != rs.getString("risk_oppty") ? rs.getString("risk_oppty") : "");
							list.add(milestoneDTO);
						}
						return list;
					}
				});
	}

	
	@Override
	public List<AllMilestonesDTO> getPublishAllMilestones(String projectId) {
		List<AllMilestonesDTO> list = new ArrayList<AllMilestonesDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingPubConstants.GET_PUB_ALL_MILESTONES)) {
			pstm.setString(1, projectId);

			ResultSet rs = pstm.executeQuery();
						while (rs.next()) {
							AllMilestonesDTO milestoneDTO = new AllMilestonesDTO();
							milestoneDTO.setMilestoneId(
									null != rs.getString("milestone_id_out") ? rs.getString("milestone_id_out") : "");
							milestoneDTO.setProrataDesc(
									null != rs.getString("prorata_desc_out") ? rs.getString("prorata_desc_out") : "");
							milestoneDTO.setAmount(null != rs.getString("amount_out") ? rs.getString("amount_out") : "");
							milestoneDTO.setCurrency(null != rs.getString("currency_out") ? rs.getString("currency_out") : "");
							milestoneDTO
									.setAmountUsd(null != rs.getString("amount_usd_out") ? rs.getString("amount_usd_out") : "");
							milestoneDTO.setCalcStatus(
									null != rs.getString("calc_status_out") ? rs.getString("calc_status_out") : "");
							milestoneDTO.setBaselineDt(
									null != rs.getString("baseline_dt_out") ? rs.getString("baseline_dt_out") : "");
							milestoneDTO.setForecastDt(
									null != rs.getString("forecast_date_out") ? rs.getString("forecast_date_out") : "");
							milestoneDTO.setComments(null != rs.getString("comments_out") ? rs.getString("comments_out") : "");
							milestoneDTO.setDetails(null != rs.getString("details_out") ? rs.getString("details_out") : "");
							milestoneDTO.setInvoiceNumber(
									null != rs.getString("invoice_number_out") ? rs.getString("invoice_number_out") : "");
							milestoneDTO.setActualInvoiceDt(
									null != rs.getString("actual_invoice_dt_out") ? rs.getString("actual_invoice_dt_out") : "");
							milestoneDTO.setPmRequestDt(
									null != rs.getString("pm_request_dt_out") ? rs.getString("pm_request_dt_out") : "");
							milestoneDTO.setPublishedForecastDt(null != rs.getString("published_forecast_dt_out")
									? rs.getString("published_forecast_dt_out")
									: "");
							milestoneDTO.setInvoiceAmount(
									null != rs.getString("invoice_amount_out") ? rs.getString("invoice_amount_out") : "");
							milestoneDTO.setBankGuaranteeFlag(
									null != rs.getString("bank_guarantee_flag_out") ? rs.getString("bank_guarantee_flag_out")
											: "");
							milestoneDTO
									.setBillingCT(null != rs.getString("billing_ct_out") ? rs.getString("billing_ct_out") : "");
							milestoneDTO.setMaxPredessorDt(
									null != rs.getString("max_actual_finish_dt_out") ? rs.getString("max_actual_finish_dt_out")
											: "");
							milestoneDTO.setMilestoneCausal(
									null != rs.getString("milestone_causal_out") ? rs.getString("milestone_causal_out") : "");
							milestoneDTO.setCollectionDate(									
									null != rs.getString("collection_date_out") ? rs.getString("collection_date_out") : "");

							milestoneDTO.setCollectionStatus(									
									null != rs.getString("status_out") ? rs.getString("status_out") : "");

							milestoneDTO.setInvoiceDueDate(
									null != rs.getString("invoice_due_date_out") ? rs.getString("invoice_due_date_out") : "");
							milestoneDTO.setCashCollected(
									null != rs.getString("cash_collected$_out") ? rs.getString("cash_collected$_out") : "");
							milestoneDTO.setOutstandingAmount(
									null != rs.getString("outstanding_amount_out") ? rs.getString("outstanding_amount_out") : "");
							list.add(milestoneDTO);

						}
					} catch (SQLException e) {
						log.error("Exception in BillingDAOImpl::getPublishAllMilestones() : " + e.getMessage());
					}

					return list;
				}
				



	@Override
	public List<ForecastCurveDTO> getPublishForecastCurve(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_FORECAST_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<ForecastCurveDTO>>() {
					public List<ForecastCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ForecastCurveDTO> list = new ArrayList<ForecastCurveDTO>();
						while (rs.next()) {
							ForecastCurveDTO curveDTO = new ForecastCurveDTO();
							curveDTO.setCashMilestoneActivityId(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setProrataDesc(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setConvertedAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setDisplayCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							curveDTO.setOracleForecastDt(null != rs.getString(8) ? rs.getString(8) : "");
							curveDTO.setInvoiceNumber(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setDisplaydate(null != rs.getString(10) ? rs.getString(10) : "");
							curveDTO.setEpochDate(rs.getDouble(11));
							curveDTO.setCumDispAmount(rs.getDouble(12));
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ActualCurveDTO> getPublishActualCurve(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_ACTUAL_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<ActualCurveDTO>>() {
					public List<ActualCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ActualCurveDTO> list = new ArrayList<ActualCurveDTO>();
						while (rs.next()) {
							ActualCurveDTO curveDTO = new ActualCurveDTO();
							curveDTO.setProrataDesc(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setCashMilestoneActivityId(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setAggOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setAggConvertedAmount(
									null != rs.getString(4) ? formatString(rs.getString(4)) : "");
							curveDTO.setInvoiceNumber(null != rs.getString(5) ? rs.getString(5) : "");
							curveDTO.setCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setInvoicedate(null != rs.getString(7) ? rs.getString(7) : "");
							curveDTO.setConvertedAmount(null != rs.getString(8) ? formatString(rs.getString(8)) : "");
							curveDTO.setMilestoneStatus(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setEpochDate(rs.getDouble(10));
							curveDTO.setCumDispAmount(rs.getDouble(11));
							curveDTO.setOrigAmount(null != rs.getString(12) ? formatString(rs.getString(12)) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<CurveTable> getPublishBillingTable(String projectId) {
		return jdbcTemplate.query(
				BillingPubConstants.GET_PUB_REPORT_TABLE, new Object[] { projectId, projectId, projectId, projectId,
						projectId, projectId, projectId, projectId, projectId, projectId, projectId },
				new ResultSetExtractor<List<CurveTable>>() {
					public List<CurveTable> extractData(ResultSet rs) throws SQLException {
						List<CurveTable> list = new ArrayList<CurveTable>();
						while (rs.next()) {
							CurveTable curveDTO = new CurveTable();
							curveDTO.setGraphDt(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setForecastAmount(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setActualAmount(null != rs.getString(3) ? rs.getString(3) : "");
							curveDTO.setBaselineAmount(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setBlankBlForecastAmount(null != rs.getString(5) ? rs.getString(5) : "");
							curveDTO.setFinBlAmount(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setItoBillingAmount(null != rs.getString(7) ? rs.getString(7) : "0.00");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<BaselineCurveDTO> getPublishBaselineCurve(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_BASELINE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<BaselineCurveDTO>>() {
					public List<BaselineCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<BaselineCurveDTO> list = new ArrayList<BaselineCurveDTO>();
						while (rs.next()) {
							BaselineCurveDTO curveDTO = new BaselineCurveDTO();
							curveDTO.setCashMilestoneActivityId(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setProrataDesc(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setConvertedAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setDisplayCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							curveDTO.setDisplaydate(null != rs.getString(8) ? rs.getString(8) : "");
							curveDTO.setEpochDate(rs.getDouble(9));
							curveDTO.setCumDispAmount(rs.getDouble(10));
							curveDTO.setAggInvoiceNm(null != rs.getString(11) ? rs.getString(11) : "");
							curveDTO.setAggInvoiceDt(null != rs.getString(12) ? rs.getString(12) : "");
							curveDTO.setAggInvoiceAmount(
									null != rs.getString(13) ? formatString(rs.getString(13)) : "");
							curveDTO.setAggInvoiceAmountUsd(
									null != rs.getString(14) ? formatString(rs.getString(14)) : "");
							curveDTO.setForecastDt(null != rs.getString(15) ? rs.getString(15) : "");
							curveDTO.setBaselineDt(null != rs.getString(16) ? rs.getString(16) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<BlankBaselineDateDTO> getBlankBaselineDate(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_BLANK_BASELINE_DATE, new Object[] { projectId },
				new ResultSetExtractor<List<BlankBaselineDateDTO>>() {
					public List<BlankBaselineDateDTO> extractData(ResultSet rs) throws SQLException {
						List<BlankBaselineDateDTO> list = new ArrayList<BlankBaselineDateDTO>();
						while (rs.next()) {
							BlankBaselineDateDTO curveDTO = new BlankBaselineDateDTO();
							curveDTO.setxAxisDt(rs.getDouble(1));
							curveDTO.setConvertedAmount(rs.getDouble(2));
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<BlankBaselineDatePopupDTO> getBlankBaseLineDatePopup(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_BLANK_BASELINE_DATE_POPUP, new Object[] { projectId },
				new ResultSetExtractor<List<BlankBaselineDatePopupDTO>>() {
					public List<BlankBaselineDatePopupDTO> extractData(ResultSet rs) throws SQLException {
						List<BlankBaselineDatePopupDTO> list = new ArrayList<BlankBaselineDatePopupDTO>();
						while (rs.next()) {
							BlankBaselineDatePopupDTO popupDTO = new BlankBaselineDatePopupDTO();
							popupDTO.setCashMilestoneActivityId(null != rs.getString(1) ? rs.getString(1) : "");
							popupDTO.setProrataDesc(null != rs.getString(2) ? rs.getString(2) : "");
							popupDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							popupDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							popupDTO.setConvertedAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							popupDTO.setDisplayCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							popupDTO.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							popupDTO.setFilterFlag(null != rs.getString(8) ? rs.getString(8) : "");
							popupDTO.setAggActualInvoiceDt(null != rs.getString(9) ? rs.getString(9) : "");
							popupDTO.setAggInvoiceNumber(null != rs.getString(10) ? rs.getString(10) : "");
							popupDTO.setAggInvoiceAmount(
									null != rs.getString(11) ? formatString(rs.getString(11)) : "");
							popupDTO.setAggInvoiceAmountUsd(
									null != rs.getString(12) ? formatString(rs.getString(12)) : "");
							list.add(popupDTO);
						}
						return list;
					}
				});
	}

	@Override
	public String getLastSavedDate(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_LAST_SAVED_DATE, new Object[] { projectId },
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
	public List<BlankBaselineDateDTO> getPublishBlankBaselineDate(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_BLANK_BASELINE_DATE, new Object[] { projectId },
				new ResultSetExtractor<List<BlankBaselineDateDTO>>() {
					public List<BlankBaselineDateDTO> extractData(ResultSet rs) throws SQLException {
						List<BlankBaselineDateDTO> list = new ArrayList<BlankBaselineDateDTO>();
						while (rs.next()) {
							BlankBaselineDateDTO curveDTO = new BlankBaselineDateDTO();
							curveDTO.setxAxisDt(rs.getDouble(1));
							curveDTO.setConvertedAmount(rs.getDouble(2));
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public String getLastPublishDate(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_LAST_PUBLISHED_DATE, new Object[] { projectId },
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
	public List<BlankBaselineDatePopupDTO> getPublishBlankBaseLineDatePopup(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_BLANK_BASELINE_DATE_POPUP, new Object[] { projectId },
				new ResultSetExtractor<List<BlankBaselineDatePopupDTO>>() {
					public List<BlankBaselineDatePopupDTO> extractData(ResultSet rs) throws SQLException {
						List<BlankBaselineDatePopupDTO> list = new ArrayList<BlankBaselineDatePopupDTO>();
						while (rs.next()) {
							BlankBaselineDatePopupDTO popupDTO = new BlankBaselineDatePopupDTO();
							popupDTO.setCashMilestoneActivityId(null != rs.getString(1) ? rs.getString(1) : "");
							popupDTO.setProrataDesc(null != rs.getString(2) ? rs.getString(2) : "");
							popupDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							popupDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							popupDTO.setConvertedAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							popupDTO.setDisplayCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							popupDTO.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							popupDTO.setFilterFlag(null != rs.getString(8) ? rs.getString(8) : "");
							popupDTO.setAggActualInvoiceDt(null != rs.getString(9) ? rs.getString(9) : "");
							popupDTO.setAggInvoiceNumber(null != rs.getString(10) ? rs.getString(10) : "");
							popupDTO.setAggInvoiceAmount(
									null != rs.getString(11) ? formatString(rs.getString(11)) : "");
							popupDTO.setAggInvoiceAmountUsd(
									null != rs.getString(12) ? formatString(rs.getString(12)) : "");
							list.add(popupDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ForecastCurveDTO> getPubForecastCurve(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_PUB_FORECAST_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<ForecastCurveDTO>>() {
					public List<ForecastCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<ForecastCurveDTO> list = new ArrayList<ForecastCurveDTO>();
						while (rs.next()) {
							ForecastCurveDTO curveDTO = new ForecastCurveDTO();
							curveDTO.setInvoiceNumber(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setCashMilestoneActivityId(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setProrataDesc(null != rs.getString(3) ? rs.getString(3) : "");
							curveDTO.setCumDispAmount(rs.getDouble(4));
							curveDTO.setOrigAmount(null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setConvertedAmount(null != rs.getString(7) ? formatString(rs.getString(7)) : "");
							curveDTO.setDisplayCurrency(null != rs.getString(8) ? rs.getString(8) : "");
							curveDTO.setStatus(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setCombinedForecastDt(null != rs.getString(10) ? rs.getString(10) : "");
							curveDTO.setOracleForecastDt(null != rs.getString(11) ? rs.getString(11) : "");
							curveDTO.setDisplaydate(null != rs.getString(12) ? rs.getString(12) : "");
							curveDTO.setEpochDate(rs.getDouble(13));
							curveDTO.setP6ForecastDt(null != rs.getString(12) ? rs.getString(12) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ActivitiesPopupDTO> getActivitiesPopup(String projectId, String cashMilestoneActivityId) {
		return jdbcTemplate.query(BillingConstants.GET_ACTIVITY_POPUP,
				new Object[] { projectId, cashMilestoneActivityId },
				new ResultSetExtractor<List<ActivitiesPopupDTO>>() {
					public List<ActivitiesPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<ActivitiesPopupDTO> list = new ArrayList<ActivitiesPopupDTO>();
						while (rs.next()) {
							ActivitiesPopupDTO activitiesDTO = new ActivitiesPopupDTO();
							activitiesDTO.setCostingProject(null != rs.getString(1) ? rs.getString(1) : "");
							activitiesDTO.setCashMilestoneActivityId(null != rs.getString(2) ? rs.getString(2) : "");
							activitiesDTO.setActivityId(null != rs.getString(3) ? rs.getString(3) : "");
							activitiesDTO.setActivityDesc(null != rs.getString(4) ? rs.getString(4) : "");
							activitiesDTO.setActivityType(null != rs.getString(5) ? rs.getString(5) : "");
							activitiesDTO.setDummyCodeRef1(null != rs.getString(6) ? rs.getString(6) : "");
							activitiesDTO.setItemCode(null != rs.getString(7) ? rs.getString(7) : "");
							activitiesDTO.setBlFinishDt(null != rs.getString(8) ? rs.getString(8) : "");
							activitiesDTO.setActualFinishDt(null != rs.getString(9) ? rs.getString(9) : "");
							activitiesDTO.setForecastFinishDt(null != rs.getString(10) ? rs.getString(10) : "");
							activitiesDTO.setWip(null != rs.getString(11) ? rs.getString(11) : "");
							activitiesDTO.setPor(null != rs.getString(12) ? rs.getString(12) : "");
							activitiesDTO.setPoNumberLine(null != rs.getString(13) ? rs.getString(13) : "");
							activitiesDTO.setResourceName(null != rs.getString(14) ? rs.getString(14) : "");
							activitiesDTO.setIssuePlannedDt(null != rs.getString(15) ? rs.getString(15) : "");
							activitiesDTO.setFinishPlannedDt(null != rs.getString(16) ? rs.getString(16) : "");
							activitiesDTO.setDocSupplier(null != rs.getString(17) ? rs.getString(17) : "");
							activitiesDTO.setLagN(null != rs.getString(18) ? rs.getString(18) : "");
							list.add(activitiesDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ActivitiesPopupDTO> getPublishActivitiesPopup(String projectId, String cashMilestoneActivityId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_ACTIVITY_POPUP,
				new Object[] { projectId, cashMilestoneActivityId },
				new ResultSetExtractor<List<ActivitiesPopupDTO>>() {
					public List<ActivitiesPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<ActivitiesPopupDTO> list = new ArrayList<ActivitiesPopupDTO>();
						while (rs.next()) {
							ActivitiesPopupDTO activitiesDTO = new ActivitiesPopupDTO();
							activitiesDTO.setCostingProject(null != rs.getString(1) ? rs.getString(1) : "");
							activitiesDTO.setCashMilestoneActivityId(null != rs.getString(2) ? rs.getString(2) : "");
							activitiesDTO.setActivityId(null != rs.getString(3) ? rs.getString(3) : "");
							activitiesDTO.setActivityDesc(null != rs.getString(4) ? rs.getString(4) : "");
							activitiesDTO.setActivityType(null != rs.getString(5) ? rs.getString(5) : "");
							activitiesDTO.setDummyCodeRef1(null != rs.getString(6) ? rs.getString(6) : "");
							activitiesDTO.setItemCode(null != rs.getString(7) ? rs.getString(7) : "");
							activitiesDTO.setBlFinishDt(null != rs.getString(8) ? rs.getString(8) : "");
							activitiesDTO.setActualFinishDt(null != rs.getString(9) ? rs.getString(9) : "");
							activitiesDTO.setForecastFinishDt(null != rs.getString(10) ? rs.getString(10) : "");
							activitiesDTO.setWip(null != rs.getString(11) ? rs.getString(11) : "");
							activitiesDTO.setPor(null != rs.getString(12) ? rs.getString(12) : "");
							activitiesDTO.setPoNumberLine(null != rs.getString(13) ? rs.getString(13) : "");
							activitiesDTO.setResourceName(null != rs.getString(14) ? rs.getString(14) : "");
							activitiesDTO.setIssuePlannedDt(null != rs.getString(15) ? rs.getString(15) : "");
							activitiesDTO.setFinishPlannedDt(null != rs.getString(16) ? rs.getString(16) : "");
							activitiesDTO.setDocSupplier(null != rs.getString(17) ? rs.getString(17) : "");
							activitiesDTO.setLagN(null != rs.getString(18) ? rs.getString(18) : "");
							list.add(activitiesDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<ActivitiesDTO> getAllActivities(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_ALL_ACTIVITY, new Object[] { projectId },
				new ResultSetExtractor<List<ActivitiesDTO>>() {
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
							activitiesDTO.setPoAcceptanceFlag(
									null != rs.getString("po_acceptance") ? rs.getString("po_acceptance") : "");
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

	@Override
	public List<ActivitiesDTO> getPublishAllActivities(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_ALL_ACTIVITY, new Object[] { projectId },
				new ResultSetExtractor<List<ActivitiesDTO>>() {
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
							activitiesDTO.setPoAcceptanceFlag(
									null != rs.getString("po_acceptance") ? rs.getString("po_acceptance") : "");
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

	@Override
	public String getMilestoneDescription(String projectId, String cashMilestoneActivityId) {
		return jdbcTemplate.query(BillingConstants.GET_MILESTONE_DESC,
				new Object[] { cashMilestoneActivityId, projectId }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String desc = "";
						while (rs.next()) {
							desc = null != rs.getString(1) ? rs.getString(1) : "";
						}
						return desc;
					}
				});
	}

	@Override
	public String getPubMilestoneDescription(String projectId, String cashMilestoneActivityId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_MILESTONE_DESC,
				new Object[] { cashMilestoneActivityId, projectId }, new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String desc = "";
						while (rs.next()) {
							desc = null != rs.getString(1) ? rs.getString(1) : "";
						}
						return desc;
					}
				});
	}

	public String formatString(String amount) {
		String result = "";
		int i = 0;
		ArrayList<String> outList = new ArrayList<String>();
		try {
			if (AssertUtils.isNotEmpty(amount)) {
				List<String> list = Arrays.asList(amount.split("\\s*,\\s*"));
				for (String obj : list) {
					String amt = "";
					amt = NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(obj));
					outList.add(amt);
					i++;
				}
				if (i > 1) {
					result = StringUtils.join(outList, "; ");
				} else {
					result = outList.get(0);
				}
			}
		} catch (Exception e) {
			log.error("formatString(): Exception occurred : {}", e.getMessage());
		}
		return result;
	}

	@Override
	public List<ExchangeRateDTO> getExchangeRate(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_EXCHANGE_RATE, new Object[] { projectId },
				new ResultSetExtractor<List<ExchangeRateDTO>>() {
					public List<ExchangeRateDTO> extractData(ResultSet rs) throws SQLException {
						List<ExchangeRateDTO> exchangeRateDTO = new ArrayList<ExchangeRateDTO>();
						while (rs.next()) {
							ExchangeRateDTO exchangeRate = new ExchangeRateDTO();
							exchangeRate.setOrig_currency_code(null != rs.getString(1) ? rs.getString(1) : "");
							exchangeRate.setYr(null != rs.getString(2) ? rs.getString(2) : "");
							exchangeRate.setConversion_rate(null != rs.getString(3) ? rs.getString(3) : "");
							exchangeRateDTO.add(exchangeRate);
						}
						return exchangeRateDTO;
					}
				});
	}

	@Override
	public List<FinancialBLCurveDTO> getFinancialBLCurve(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_FINANCIAL_BL_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<FinancialBLCurveDTO>>() {
					public List<FinancialBLCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<FinancialBLCurveDTO> list = new ArrayList<FinancialBLCurveDTO>();
						while (rs.next()) {
							FinancialBLCurveDTO curveDTO = new FinancialBLCurveDTO();
							curveDTO.setMilestoneId(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setMilestoneDesc(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setConvertedAmountUsd(
									null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setForecastDt(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setEpochDate(rs.getDouble(7));
							curveDTO.setCumDispAmount(rs.getDouble(8));
							curveDTO.setStatus(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setInvoiceNumber(null != rs.getString(10) ? rs.getString(10) : "");
							curveDTO.setInvoiceDt(null != rs.getString(11) ? rs.getString(11) : "");
							curveDTO.setInvoiceAmount(null != rs.getString(12) ? formatString(rs.getString(12)) : "");
							curveDTO.setInvoiceAmountUsd(
									null != rs.getString(13) ? formatString(rs.getString(13)) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public List<FinancialBLCurveDTO> getPubFinancialBLCurve(String projectId) {
		return jdbcTemplate.query(BillingPubConstants.GET_PUB_FINANCIAL_BL_CURVE, new Object[] { projectId },
				new ResultSetExtractor<List<FinancialBLCurveDTO>>() {
					public List<FinancialBLCurveDTO> extractData(ResultSet rs) throws SQLException {
						List<FinancialBLCurveDTO> list = new ArrayList<FinancialBLCurveDTO>();
						while (rs.next()) {
							FinancialBLCurveDTO curveDTO = new FinancialBLCurveDTO();
							curveDTO.setMilestoneId(null != rs.getString(1) ? rs.getString(1) : "");
							curveDTO.setMilestoneDesc(null != rs.getString(2) ? rs.getString(2) : "");
							curveDTO.setOrigAmount(null != rs.getString(3) ? formatString(rs.getString(3)) : "");
							curveDTO.setOrigCurrency(null != rs.getString(4) ? rs.getString(4) : "");
							curveDTO.setConvertedAmountUsd(
									null != rs.getString(5) ? formatString(rs.getString(5)) : "");
							curveDTO.setForecastDt(null != rs.getString(6) ? rs.getString(6) : "");
							curveDTO.setEpochDate(rs.getDouble(7));
							curveDTO.setCumDispAmount(rs.getDouble(8));
							curveDTO.setStatus(null != rs.getString(9) ? rs.getString(9) : "");
							curveDTO.setInvoiceNumber(null != rs.getString(10) ? rs.getString(10) : "");
							curveDTO.setInvoiceDt(null != rs.getString(11) ? rs.getString(11) : "");
							curveDTO.setInvoiceAmount(null != rs.getString(12) ? formatString(rs.getString(12)) : "");
							curveDTO.setInvoiceAmountUsd(
									null != rs.getString(13) ? formatString(rs.getString(13)) : "");
							list.add(curveDTO);
						}
						return list;
					}
				});
	}

	@Override
	public boolean getShowPublishButtonFlag(String projectId) {
		return jdbcTemplate.query(BillingConstants.SHOW_PUBLISH_BUTTON, new Object[] { projectId },
				new ResultSetExtractor<Boolean>() {
					public Boolean extractData(ResultSet rs) throws SQLException {
						boolean showPublishButtonFlag = false;
						while (rs.next()) {
							String flag = null != rs.getString("flag") ? rs.getString("flag") : "";
							if (flag.equalsIgnoreCase("Y")) {
								showPublishButtonFlag = true;
							}
						}
						return showPublishButtonFlag;
					}
				});
	}

	@Override
	public Map<String, Object> getOpenInvoiceChart(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_OPEN_INVOICE_CHART, new Object[] { projectId },
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
							if (dto.getTypes().equalsIgnoreCase(BillingConstants.PAST_DUE_CHART_TYPE)) {
								pastDueList.add(dto);
							} else if (dto.getTypes().equalsIgnoreCase(BillingConstants.CURRENT_DUE_CHART_TYPE)) {
								currentQuarterList.add(dto);
							} else if (dto.getTypes().equalsIgnoreCase(BillingConstants.FUTURE_DUE_CHART_TYPE)) {
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
		return jdbcTemplate.query(BillingConstants.GET_INVOICE_CHART_DATE, new Object[] { projectId },
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
	public List<OpenInvoiceChartPopupDetails> getOpenInvoiceChartPopupDetails(String projectId, String chartType,
			String statusCode) {
		String type = "", code = "";
		if (chartType.equalsIgnoreCase("PAST_DUE")) {
			type = BillingConstants.PAST_DUE_CHART_TYPE;
		} else if (chartType.equalsIgnoreCase("CURRENT_DUE")) {
			type = BillingConstants.CURRENT_DUE_CHART_TYPE;
		} else if (chartType.equalsIgnoreCase("FUTURE_DUE")) {
			type = BillingConstants.FUTURE_DUE_CHART_TYPE;
		}

		if (statusCode.equalsIgnoreCase("DISPUTED")) {
			code = BillingConstants.DISPUTED_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("COLLECTION_ESCALATED")) {
			code = BillingConstants.COLLECTION_ESCALATED_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("NOT_DISPUTED")) {
			code = BillingConstants.NOT_DISPUTED_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("CREDIT_NOTE")) {
			code = BillingConstants.CREDIT_NOTE_STATUS_CODE;
		} else if (statusCode.equalsIgnoreCase("COMMIT_TO_PAY")) {
			code = BillingConstants.COMMIT_TO_PAY_STATUS_CODE;
		}

		return jdbcTemplate.query(BillingConstants.GET_OPEN_INVOICE_CHART_POPUP,
				new Object[] { projectId, projectId, type, code },
				new ResultSetExtractor<List<OpenInvoiceChartPopupDetails>>() {
					public List<OpenInvoiceChartPopupDetails> extractData(ResultSet rs) throws SQLException {
						List<OpenInvoiceChartPopupDetails> list = new ArrayList<OpenInvoiceChartPopupDetails>();
						while (rs.next()) {
							OpenInvoiceChartPopupDetails dto = new OpenInvoiceChartPopupDetails();
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
	public List<OpenInvoiceDataTable> getOpenInvoiceDatatable(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_OPEN_INVOICE_DATA_TABLE, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<OpenInvoiceDataTable>>() {
					public List<OpenInvoiceDataTable> extractData(ResultSet rs) throws SQLException {
						List<OpenInvoiceDataTable> list = new ArrayList<OpenInvoiceDataTable>();
						while (rs.next()) {
							OpenInvoiceDataTable dto = new OpenInvoiceDataTable();
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
							dto.setClientUniqueDocumentNumber(null != rs.getString("client_unique_document_number")
									? rs.getString("client_unique_document_number")
									: "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public boolean saveOpenInvoiceDetails(List<SaveOpenInvoiceDTO> invoicesList, String sso) {
		boolean resultFlag = false, deleteFlag = false;
		int count = 0;
		Connection con = null;
		try {
			if (AssertUtils.isListNotEmpty(invoicesList)) {
				con = jdbcTemplate.getDataSource().getConnection();
				deleteFlag = deleteOpenInvoiceDetails(invoicesList.get(0).getProjectId());
				for (SaveOpenInvoiceDTO dto : invoicesList) {
					PreparedStatement pstm = con.prepareStatement(BillingConstants.INSERT_OPEN_INVOICE_DETAILS);
					pstm.setString(1, dto.getProjectId());
					pstm.setString(2, dto.getInvoiceNumber());
					pstm.setString(3, dto.getRisk());
					pstm.setString(4, dto.getComments());
					pstm.setString(5, dto.getClientUniqueDocumentNumber());
					pstm.setString(6, sso);
					if (pstm.executeUpdate() > 0) {
						count++;
						resultFlag = true;
					}
				}
				log.info("Inserted " + count + " rows for project id :: " + invoicesList.get(0).getProjectId());
			}
		} catch (Exception e) {
			log.error("something went wrong while saving cash invoices details:{}", e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while saving cash invoices details:{}", e.getMessage());
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

				pstm = con.prepareStatement(BillingConstants.SELECT_OPEN_INVOICE_DETAILS);
				pstm.setString(1, projectId);
				rs = pstm.executeQuery();
				while (rs.next()) {
					count = rs.getString("count");
					log.info("Selected " + count + " rows for project id :: " + projectId);
				}
				if (projectId != null && !projectId.isEmpty() && count.equalsIgnoreCase("0")) {
					resultFlag = true;
				}
				pstm = con.prepareStatement(BillingConstants.DELETE_OPEN_INVOICE_DETAILS);
				pstm.setString(1, projectId);
				int result = pstm.executeUpdate();
				if (result > 0) {
					log.info("Deleted " + count + " rows for project id :: " + projectId);
					resultFlag = true;
				}
			}
		} catch (Exception e) {
			log.error("something went wrong while deleting cash invoices details:{}", e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					log.error("something went wrong while deleting cash invoices details:{}", e.getMessage());
				}
			}
		}
		return resultFlag;
	}

	@Override
	public void callCashCollectionProcedure() {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingConstants.CALL_CASH_COLLECTION_PROCEDURE)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				log.info("Response :: {}", rs.getString(1));
			}
		} catch (Exception e) {
			log.error("Exception while executing cash collection procedure :: {}", e.getMessage());
		}
	}

	@Override
	public String getOpenInvoiceLastSavedDate(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_OPEN_INVOICE_SAVE_DATE, new Object[] { projectId },
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
		return jdbcTemplate.query(BillingConstants.GET_FORECAST_CASH_CURVE,
				new Object[] { projectId, projectId, projectId, projectId, projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, CashCollectionCurveTableDTO> compareMap = null;
						List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
						List<ForecastCashDTO> list = new ArrayList<ForecastCashDTO>();
						CashCollectionCurveTableDTO compareDTO = new CashCollectionCurveTableDTO();
						if ((Map<String, CashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, CashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, CashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							ForecastCashDTO dto = new ForecastCashDTO();
							dto.setCumDispAmount(
									null != rs.getString("display_amount") ? rs.getString("display_amount") : "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setBaselineDt(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setMilestoneId(
									null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							dto.setMilestoneDesc(
									null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
							dto.setMilestoneAmountUSD(
									null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd")
											: "");
							dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
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
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							if (!displayMonth.equalsIgnoreCase("")) {
								compareDTO = (CashCollectionCurveTableDTO) compareMap.get(displayMonth);
								if (null == compareDTO) {
									compareDTO = new CashCollectionCurveTableDTO();
									compareDTO.setDisplayDate(displayMonth);
									compareDTO.setForecastCash(cumDispAmt);
									compareMap.put(displayMonth, compareDTO);
								} else {
									compareDTO.setForecastCash(cumDispAmt);
								}
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((CashCollectionCurveTableDTO s1, CashCollectionCurveTableDTO s2) -> YearMonth
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
	public Map<String, Object> getForecastCashCurveForPascalProject(String projectId, Map<String, Object> map) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
		return jdbcTemplate.query(BillingConstants.GET_PASCAL_FORECAST_CASH_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, CashCollectionCurveTableDTO> compareMap = null;
						List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
						List<ForecastCashDTO> list = new ArrayList<ForecastCashDTO>();
						CashCollectionCurveTableDTO compareDTO = new CashCollectionCurveTableDTO();
						if ((Map<String, CashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, CashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, CashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							ForecastCashDTO dto = new ForecastCashDTO();
							dto.setCumDispAmount(
									null != rs.getString("display_amount") ? rs.getString("display_amount") : "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setBaselineDt(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setMilestoneId(
									null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							dto.setMilestoneDesc(
									null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
							dto.setMilestoneAmountUSD(
									null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd")
											: "");
							dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
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
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							if (!displayMonth.equalsIgnoreCase("")) {
								compareDTO = (CashCollectionCurveTableDTO) compareMap.get(displayMonth);
								if (null == compareDTO) {
									compareDTO = new CashCollectionCurveTableDTO();
									compareDTO.setDisplayDate(displayMonth);
									compareDTO.setForecastCash(cumDispAmt);
									compareMap.put(displayMonth, compareDTO);
								} else {
									compareDTO.setForecastCash(cumDispAmt);
								}
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((CashCollectionCurveTableDTO s1, CashCollectionCurveTableDTO s2) -> YearMonth
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
		return jdbcTemplate.query(BillingConstants.GET_COLLECTED_CASH_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, CashCollectionCurveTableDTO> compareMap = null;
						List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
						List<CollectedCashDTO> list = new ArrayList<CollectedCashDTO>();
						CashCollectionCurveTableDTO compareDTO = new CashCollectionCurveTableDTO();
						if ((Map<String, CashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, CashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, CashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							CollectedCashDTO dto = new CollectedCashDTO();
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
							compareDTO = (CashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new CashCollectionCurveTableDTO();
								compareDTO.setDisplayDate(displayMonth);
								compareDTO.setCollectedCash(cumDispAmt);
								compareMap.put(displayMonth, compareDTO);
							} else {
								compareDTO.setCollectedCash(cumDispAmt);
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((CashCollectionCurveTableDTO s1, CashCollectionCurveTableDTO s2) -> YearMonth
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
		return jdbcTemplate.query(BillingConstants.GET_OTR_CASH_BASELINE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, CashCollectionCurveTableDTO> compareMap = null;
						List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
						List<OtrCashBaselineDTO> list = new ArrayList<OtrCashBaselineDTO>();
						CashCollectionCurveTableDTO compareDTO = new CashCollectionCurveTableDTO();
						if ((Map<String, CashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, CashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, CashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							OtrCashBaselineDTO dto = new OtrCashBaselineDTO();
							dto.setCumDispAmount(
									null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd")
											: "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setBaselineDt(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
							dto.setMilestoneId(
									null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
							dto.setMilestoneDesc(
									null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
							dto.setMilestoneAmount(
									null != rs.getString("milestone_amount") ? rs.getString("milestone_amount") : "");
							dto.setMilestoneAmountUSD(
									null != rs.getString("milestone_amount_usd") ? rs.getString("milestone_amount_usd")
											: "");
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
							dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							dto.setConvertedAmountUSD(
									null != rs.getString("converted_amount_usd") ? rs.getString("converted_amount_usd")
											: "");
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							compareDTO = (CashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new CashCollectionCurveTableDTO();
								compareDTO.setDisplayDate(displayMonth);
								compareDTO.setOtrCashBaseline(cumDispAmt);
								compareMap.put(displayMonth, compareDTO);
							} else {
								compareDTO.setOtrCashBaseline(cumDispAmt);
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((CashCollectionCurveTableDTO s1, CashCollectionCurveTableDTO s2) -> YearMonth
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
	public Map<String, Object> getItoCashBaselineCurve(String projectId, Map<String, Object> map) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yy", Locale.ENGLISH);
		return jdbcTemplate.query(BillingConstants.GET_ITO_CASH_BASELINE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, CashCollectionCurveTableDTO> compareMap = null;
						List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
						List<ItoCashBaselineDTO> list = new ArrayList<ItoCashBaselineDTO>();
						CashCollectionCurveTableDTO compareDTO = new CashCollectionCurveTableDTO();
						if ((Map<String, CashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, CashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, CashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							ItoCashBaselineDTO dto = new ItoCashBaselineDTO();
							dto.setCumDispAmount(null != rs.getString("ito_cash") ? rs.getString("ito_cash") : "");
							dto.setDisplayDate(
									null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayMonth(
									null != rs.getString("display_month") ? rs.getString("display_month") : "");
							dto.setBaselineDt(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setDisplayCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setItoDate(null != rs.getString("display_date") ? rs.getString("display_date") : "");
							dto.setItoCash(null != rs.getString("ito_cash") ? rs.getString("ito_cash") : "");
							dto.setOrigAmount(null != rs.getString("billing_usd") ? rs.getString("billing_usd") : "");
							dto.setOrigCurrency(
									null != rs.getString("display_currency") ? rs.getString("display_currency") : "");
							dto.setEurPortion(null != rs.getString("eur_portion") ? rs.getString("eur_portion") : "");
							dto.setUsdPortion(null != rs.getString("usd_portion") ? rs.getString("usd_portion") : "");
							list.add(dto);
							cumDispAmt = dto.getCumDispAmount();
							displayMonth = dto.getDisplayMonth();
							compareDTO = (CashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new CashCollectionCurveTableDTO();
								compareDTO.setDisplayDate(displayMonth);
								compareDTO.setItoCashBaseline(cumDispAmt);
								compareMap.put(displayMonth, compareDTO);
							} else {
								compareDTO.setItoCashBaseline(cumDispAmt);
							}
						}
						tableList.addAll(compareMap.values());
						tableList.sort((CashCollectionCurveTableDTO s1, CashCollectionCurveTableDTO s2) -> YearMonth
								.parse(StringUtils.capitalize(s1.getDisplayDate().toLowerCase()), formatter)
								.compareTo(YearMonth.parse(StringUtils.capitalize(s2.getDisplayDate().toLowerCase()),
										formatter)));
						map.put("itoCashBaseline", list);
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
		return jdbcTemplate.query(BillingConstants.GET_PAST_DUE_CURVE, new Object[] { projectId },
				new ResultSetExtractor<Map<String, Object>>() {
					public Map<String, Object> extractData(ResultSet rs) throws SQLException {
						Map<String, CashCollectionCurveTableDTO> compareMap = null;
						List<CashCollectionCurveTableDTO> tableList = new ArrayList<CashCollectionCurveTableDTO>();
						List<PastDueCommitmentDTO> list = new ArrayList<PastDueCommitmentDTO>();
						CashCollectionCurveTableDTO compareDTO = new CashCollectionCurveTableDTO();
						if ((Map<String, CashCollectionCurveTableDTO>) map.get("compareMap") != null) {
							compareMap = (Map<String, CashCollectionCurveTableDTO>) map.get("compareMap");
						} else {
							compareMap = new LinkedHashMap<String, CashCollectionCurveTableDTO>();
						}
						while (rs.next()) {
							String displayMonth, cumDispAmt;
							PastDueCommitmentDTO dto = new PastDueCommitmentDTO();
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
							compareDTO = (CashCollectionCurveTableDTO) compareMap.get(displayMonth);
							if (null == compareDTO) {
								compareDTO = new CashCollectionCurveTableDTO();
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
						tableList.sort((CashCollectionCurveTableDTO s1, CashCollectionCurveTableDTO s2) -> YearMonth
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
	public List<CashCollectionInvoicesDetails> getAllInvoicesDetails(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_ALL_INVOICE_CHART_DETAILS,
				new Object[] { projectId, projectId, projectId },
				new ResultSetExtractor<List<CashCollectionInvoicesDetails>>() {
					public List<CashCollectionInvoicesDetails> extractData(ResultSet rs) throws SQLException {
						List<CashCollectionInvoicesDetails> list = new ArrayList<CashCollectionInvoicesDetails>();
						while (rs.next()) {
							CashCollectionInvoicesDetails dto = new CashCollectionInvoicesDetails();
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setCustomer(null != rs.getString("customer") ? rs.getString("customer") : "");
							dto.setInvoiceDate(
									null != rs.getString("actual_invoice_dt") ? rs.getString("actual_invoice_dt") : "");
							dto.setInvoiceDueDate(null != rs.getString("due_dt") ? rs.getString("due_dt") : "");
							dto.setCollectionDate(
									null != rs.getString("collection_date") ? rs.getString("collection_date") : "");
							dto.setCollectionCT(
									null != rs.getString("collection_ct") ? rs.getString("collection_ct") : "");
							dto.setLagVsDueDate(
									null != rs.getString("lag_vs_due_dt") ? rs.getString("lag_vs_due_dt") : "");
							dto.setCurrency(
									null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
							dto.setInvoiceAmountCurr(
									null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount$") ? rs.getString("invoice_amount$") : "");
							dto.setOutstandingAmountCurr(
									null != rs.getString("outstanding_amount") ? rs.getString("outstanding_amount")
											: "");
							dto.setOutstandingAmountUSD(
									null != rs.getString("outstanding_amount$") ? rs.getString("outstanding_amount$")
											: "");
							dto.setCashCollected(
									null != rs.getString("cash_collected") ? rs.getString("cash_collected") : "");
							dto.setCashCollectedUSD(
									null != rs.getString("cash_collected$") ? rs.getString("cash_collected$") : "");
							dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							dto.setPaymentTerms(
									null != rs.getString("payment_term_desc") ? rs.getString("payment_term_desc") : "");
							dto.setDhl(null != rs.getString("dhl_number") ? rs.getString("dhl_number") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<CashCollectionInvoicesDetails> getAllInvoicesDetailsForPascalProject(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_PASCAL_ALL_INVOICE_CHART_DETAILS,
				new Object[] { projectId, projectId, projectId, projectId },
				new ResultSetExtractor<List<CashCollectionInvoicesDetails>>() {
					public List<CashCollectionInvoicesDetails> extractData(ResultSet rs) throws SQLException {
						List<CashCollectionInvoicesDetails> list = new ArrayList<CashCollectionInvoicesDetails>();
						while (rs.next()) {
							CashCollectionInvoicesDetails dto = new CashCollectionInvoicesDetails();
							dto.setInvoiceNumber(
									null != rs.getString("invoice_number") ? rs.getString("invoice_number") : "");
							dto.setCustomer(null != rs.getString("customer") ? rs.getString("customer") : "");
							dto.setInvoiceDate(
									null != rs.getString("actual_invoice_dt") ? rs.getString("actual_invoice_dt") : "");
							dto.setInvoiceDueDate(null != rs.getString("due_dt") ? rs.getString("due_dt") : "");
							dto.setCollectionDate(
									null != rs.getString("collection_date") ? rs.getString("collection_date") : "");
							dto.setCollectionCT(
									null != rs.getString("collection_ct") ? rs.getString("collection_ct") : "");
							dto.setLagVsDueDate(
									null != rs.getString("lag_vs_due_dt") ? rs.getString("lag_vs_due_dt") : "");
							dto.setCurrency(
									null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
							dto.setInvoiceAmountCurr(
									null != rs.getString("invoice_amount") ? rs.getString("invoice_amount") : "");
							dto.setInvoiceAmountUSD(
									null != rs.getString("invoice_amount$") ? rs.getString("invoice_amount$") : "");
							dto.setOutstandingAmountCurr(
									null != rs.getString("outstanding_amount") ? rs.getString("outstanding_amount")
											: "");
							dto.setOutstandingAmountUSD(
									null != rs.getString("outstanding_amount$") ? rs.getString("outstanding_amount$")
											: "");
							dto.setCashCollected(
									null != rs.getString("cash_collected") ? rs.getString("cash_collected") : "");
							dto.setCashCollectedUSD(
									null != rs.getString("cash_collected$") ? rs.getString("cash_collected$") : "");
							dto.setStatus(null != rs.getString("status") ? rs.getString("status") : "");
							dto.setPaymentTerms(
									null != rs.getString("payment_term_desc") ? rs.getString("payment_term_desc") : "");
							dto.setDhl(null != rs.getString("dhl_number") ? rs.getString("dhl_number") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public Boolean checkProjectIsPascalProject(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_PASCAL_PROJECT_LIST, new Object[] {},
				new ResultSetExtractor<Boolean>() {
					public Boolean extractData(ResultSet rs) throws SQLException {
						String pascalProjects = "";
						boolean isPascalProject = false;
						List<String> pascalProjectList = new ArrayList<String>();
						while (rs.next()) {
							pascalProjects = rs.getString("attribute_value");
							if (pascalProjects != null) {
								String[] pascalProjectStr = pascalProjects.split(";");
								pascalProjectList = Arrays.asList(pascalProjectStr);
							}
						}
						if (pascalProjectList.contains(projectId)) {
							isPascalProject = true;
						}
						return isPascalProject;
					}
				});
	}

	@Override
	public List<DropDownDTO> getVorFilter(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_VOR_FILTER_LIST, new Object[] { projectId, projectId },
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> filterList = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(2));
							filterList.add(dto);
						}
						return filterList;
					}
				});
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getPOLastUpdatedDate(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_PO_LAST_UPDATED_DATE, new Object[] {},
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

	@SuppressWarnings("deprecation")
	@Override
	public PODetailsDTO getPODetails(String projectId) {
		return jdbcTemplate.query(BillingConstants.GET_PO_DETAILS,
				new Object[] { projectId, projectId, projectId, projectId }, new ResultSetExtractor<PODetailsDTO>() {
					public PODetailsDTO extractData(ResultSet rs) throws SQLException {
						PODetailsDTO dto = new PODetailsDTO();
						while (rs.next()) {
							dto.setPoValue(null != rs.getString("po_value") ? rs.getString("po_value") : "");
							dto.setDisplayCurrencyUnits(null != rs.getString("display_currency_units")
									? rs.getString("display_currency_units")
									: "");
						}
						return dto;
					}
				});
	}

	@Override
	public List<InvoiceDetailDTO> getPOInvoiceDetails(String projectId) {
		List<InvoiceDetailDTO> list = new ArrayList<InvoiceDetailDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(BillingConstants.GET_PO_INVOICE_CHART_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				InvoiceDetailDTO dto = new InvoiceDetailDTO();
				dto.setProjectId(null != rs.getString("projectid_out") ? rs.getString("projectid_out") : "");
				dto.setInvoiceAmount(
						null != rs.getString("invoice_amount_out") ? rs.getString("invoice_amount_out") : "");
				dto.setOrigCurrencyCode(
						null != rs.getString("orig_currency_code_out") ? rs.getString("orig_currency_code_out") : "");
				dto.setPastDue(null != rs.getString("pastdue_out") ? rs.getString("pastdue_out") : "");
				dto.setPoValue(null != rs.getString("po_value_out") ? rs.getString("po_value_out") : "");
				dto.setSalesToGo("5");
				dto.setCollectedAmount(
						null != rs.getString("collected_amount_out") ? rs.getString("collected_amount_out") : "");
				dto.setCollectedAmountOfe(
						null != rs.getString("collected_amount_ofe_out") ? rs.getString("collected_amount_ofe_out")
								: "");
				list.add(dto);
			}

		} catch (SQLException e) {
			log.error("Exception in  BillingDAOImpl class ::getPOInvoiceDetails() {}", e.getMessage());
		}

		return list;

	}
}