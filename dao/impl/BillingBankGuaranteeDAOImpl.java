package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.IBillingBankGuaranteeDAO;
import com.bh.realtrack.dao.helper.BankGuaranteeDAOHelper;
import com.bh.realtrack.dto.BankGuaranateePieChartDTO;
import com.bh.realtrack.dto.BankGuaranateeSummaryDetailsDTO;
import com.bh.realtrack.dto.BankGuaranteePopupDetailsDTO;
import com.bh.realtrack.util.BillingBankGuaranteeConstants;
import com.bh.realtrack.util.BillingCustEntitlementConstants;

@Repository
public class BillingBankGuaranteeDAOImpl implements IBillingBankGuaranteeDAO {
	private static Logger log = LoggerFactory.getLogger(BillingBankGuaranteeDAOImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private BankGuaranteeDAOHelper bankGuaranteeDAOHelper;

	@Override
	public String getBankGuaranteeUpdatedOn(String projectId) {
		String date = BillingCustEntitlementConstants.EMPTY_STRING;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingBankGuaranteeConstants.GET_BANK_GUARANTEE_CHART_DATE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				date = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt")
						: BillingCustEntitlementConstants.EMPTY_STRING;
			}
		} catch (SQLException e) {
			log.error("Exception while getting bank guarantee updated on date :: {}" , e.getMessage());
		}
		return date;
	}

	@Override
	public BankGuaranateeSummaryDetailsDTO getBankGuaranteeSummary(String projectId) {
		BankGuaranateeSummaryDetailsDTO summaryDTO = new BankGuaranateeSummaryDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingBankGuaranteeConstants.GET_BANK_GUARANTEE_SUMMARY_CNT);) {
			pstm.setString(1, projectId);
			pstm.setString(2, projectId);
			pstm.setString(3, projectId);
			pstm.setString(4, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setTotal(null != rs.getString("total_count") ? rs.getString("total_count") : "");
				summaryDTO.setTotalAmt(null != rs.getString("total_amount") ? rs.getString("total_amount") : "");
				summaryDTO.setUnderRelease(
						null != rs.getString("under_release_count") ? rs.getString("under_release_count") : "");
				summaryDTO.setUnderReleaseAmt(
						null != rs.getString("under_release_amount") ? rs.getString("under_release_amount") : "");
				summaryDTO.setIssued(null != rs.getString("issued_count") ? rs.getString("issued_count") : "");
				summaryDTO.setIssuedAmt(null != rs.getString("issued_amount") ? rs.getString("issued_amount") : "");
				summaryDTO.setExpired(
						null != rs.getString("expired_closed_count") ? rs.getString("expired_closed_count") : "");
				summaryDTO.setExpiredAmt(
						null != rs.getString("expired_closed_amount") ? rs.getString("expired_closed_amount") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting bank guarantee summary details :: {}" , e.getMessage());
		}
		return summaryDTO;
	}

	@Override
	public Map<String, Object> getBankGuaranteeBgAmtConnectedPieChart(String projectId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		BankGuaranateePieChartDTO defaultDTO = new BankGuaranateePieChartDTO();
		defaultDTO.setCount("0");
		defaultDTO.setAmount("0");
		resultMap.put("applied", defaultDTO);
		resultMap.put("inProcess", defaultDTO);
		resultMap.put("outOfBid", defaultDTO);
		resultMap.put("readyForReview", defaultDTO);
		resultMap.put("readyToApply", defaultDTO);
		resultMap.put("issued", defaultDTO);
		resultMap.put("expired", defaultDTO);
		resultMap.put("total", defaultDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingBankGuaranteeConstants.GET_BANK_GUARANTEE_BG_AMT_CONNECTED_CHART);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			Double totalAmt = 0.0;
			while (rs.next()) {
				BankGuaranateePieChartDTO dto = new BankGuaranateePieChartDTO();
				String status = "", count = "", amount = "";
				status = rs.getString("guarantee_status");
				count = rs.getString("count");
				amount = rs.getString("gti_nominal_amount_in_dollar");
				dto.setCount(count);
				dto.setAmount(amount);
				if (null != status && !"".equalsIgnoreCase(status)) {
					totalAmt = totalAmt + Double.parseDouble(amount);
					if (status.equalsIgnoreCase("Applied")) {
						resultMap.put("applied", dto);
					} else if (status.equalsIgnoreCase("In Process")) {
						resultMap.put("inProcess", dto);
					} else if (status.equalsIgnoreCase("Out for Bid")) {
						resultMap.put("outOfBid", dto);
					} else if (status.equalsIgnoreCase("Ready for Review")) {
						resultMap.put("readyForReview", dto);
					} else if (status.equalsIgnoreCase("Ready to Apply")) {
						resultMap.put("readyToApply", dto);
					} else if (status.equalsIgnoreCase("Issued")) {
						resultMap.put("issued", dto);
					} else if (status.equalsIgnoreCase("Expired/Closed")) {
						resultMap.put("expired", dto);
					}
					if (totalAmt != 0) {
						resultMap.put("total", String.valueOf(String.format("%.2f", totalAmt)));
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting bank guarantee BG Amt Connected Pie Chart :: {}" , e.getMessage());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getBankGuaranteeBgAmtIssuedByTypePieChart(String projectId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		BankGuaranateePieChartDTO defaultDTO = new BankGuaranateePieChartDTO();
		defaultDTO.setCount("0");
		defaultDTO.setAmount("0");
		resultMap.put("advPayGuarantee", defaultDTO);
		resultMap.put("custGuarantee", defaultDTO);
		resultMap.put("paymentGuarantee", defaultDTO);
		resultMap.put("performGuarantee", defaultDTO);
		resultMap.put("tenderGuarantee", defaultDTO);
		resultMap.put("otherType", defaultDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(BillingBankGuaranteeConstants.GET_BANK_GUARANTEE_BG_AMT_ISSUED_PIE_CHART);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				BankGuaranateePieChartDTO dto = new BankGuaranateePieChartDTO();
				String type = "", count = "", amount = "";
				type = rs.getString("guarantee_type");
				count = rs.getString("count");
				amount = rs.getString("gti_nominal_amount_in_dollar");
				dto.setCount(count);
				dto.setAmount(amount);
				if (type.equalsIgnoreCase("Advance Payment Guarantee")) {
					resultMap.put("advPayGuarantee", dto);
				} else if (type.equalsIgnoreCase("Customs Guarantee")) {
					resultMap.put("custGuarantee", dto);
				} else if (type.equalsIgnoreCase("Payment Guarantee")) {
					resultMap.put("paymentGuarantee", dto);
				} else if (type.equalsIgnoreCase("Performance Guarantee")) {
					resultMap.put("performGuarantee", dto);
				} else if (type.equalsIgnoreCase("Tender Guarantee")) {
					resultMap.put("tenderGuarantee", dto);
				} else if (type.equalsIgnoreCase("Other Type")) {
					resultMap.put("otherType", dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting bank guarantee BG Amt Issued Pie Chart :: {}" , e.getMessage());
		}
		return resultMap;
	}

	@Override
	public Map<String, Object> getBankGuaranteeBgAmtToRecoverPieChart(String projectId) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		BankGuaranateePieChartDTO defaultDTO = new BankGuaranateePieChartDTO();
		defaultDTO.setCount("0");
		defaultDTO.setAmount("0");
		resultMap.put("openEndedExpired", defaultDTO);
		resultMap.put("agedBG", defaultDTO);
		resultMap.put("others", defaultDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						BillingBankGuaranteeConstants.GET_BANK_GUARANTEE_BG_AMT_TO_RECOVER_PIE_CHART);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				BankGuaranateePieChartDTO dto = new BankGuaranateePieChartDTO();
				String status = "", count = "", amount = "";
				status = rs.getString("bg_amount_to_recover");
				count = rs.getString("count");
				amount = rs.getString("gti_nominal_amount_in_dollar");
				dto.setCount(count);
				dto.setAmount(amount);
				if (null != status && !"".equalsIgnoreCase(status)) {
					if (status.equalsIgnoreCase("Open Ended Expired")) {
						resultMap.put("openEndedExpired", dto);
					} else if (status.equalsIgnoreCase("Aged BG")) {
						resultMap.put("agedBG", dto);
					} else if (status.equalsIgnoreCase("Others")) {
						resultMap.put("others", dto);
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting bank guarantee BG Amt To Recover Pie Chart :: {}" , e.getMessage());
		}
		return resultMap;
	}

	@Override
	public List<BankGuaranteePopupDetailsDTO> getBankGuaranteePopupDetails(String projectId, String chartType,
			String status) {
		List<BankGuaranteePopupDetailsDTO> list = new ArrayList<BankGuaranteePopupDetailsDTO>();
		String query = bankGuaranteeDAOHelper.getBankGuaranteePopupQuery(chartType, status);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				BankGuaranteePopupDetailsDTO dto = new BankGuaranteePopupDetailsDTO();
				dto.setGuaranteeNo(null != rs.getString("gti_systemid") ? rs.getString("gti_systemid")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setGuaranteeOrigNo(null != rs.getString("gti_corp_refno") ? rs.getString("gti_corp_refno")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setGuarantor(null != rs.getString("gti_corp_refno1") ? rs.getString("gti_corp_refno1")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setBeneficiaryName(null != rs.getString("gti_beneficiary_name_address")
						? rs.getString("gti_beneficiary_name_address")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setIssuanceCountry(null != rs.getString("specific_country_of_issuance")
						? rs.getString("specific_country_of_issuance")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setApplicantName(
						null != rs.getString("gti_applicant_name_address") ? rs.getString("gti_applicant_name_address")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setCurr(null != rs.getString("gti_nominal_currency") ? rs.getString("gti_nominal_currency")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setAmountInCurr(null != rs.getString("gti_nominal_amount") ? rs.getString("gti_nominal_amount")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setCreationDate(null != rs.getString("creation_date") ? rs.getString("creation_date")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setIssueDate(null != rs.getString("gti_issue_date") ? rs.getString("gti_issue_date")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setExpiryDate(null != rs.getString("gti_expiry_date") ? rs.getString("gti_expiry_date")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setIssuanceCyt(null != rs.getString("bg_issuance_cyt") ? rs.getString("bg_issuance_cyt")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setInstrumentType(null != rs.getString("gti_instrument_type") ? rs.getString("gti_instrument_type")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setGuaranteeType(null != rs.getString("gti_guarantee_type") ? rs.getString("gti_guarantee_type")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setGuaranteeStatus(
						null != rs.getString("gti_guarantee_status") ? rs.getString("gti_guarantee_status")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setFormOfGuarantee(
						null != rs.getString("gti_form_of_guarantee") ? rs.getString("gti_form_of_guarantee")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setInstrumentPurpose(
						null != rs.getString("gti_instrument_purpose") ? rs.getString("gti_instrument_purpose")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setValidityType(null != rs.getString("gti_validity_type") ? rs.getString("gti_validity_type")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setJob(
						null != rs.getString("job") ? rs.getString("job") : BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setDaysCoolOffPeriod(
						null != rs.getString("days_cool_off_period") ? rs.getString("days_cool_off_period")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setDaysCurePeriod(null != rs.getString("days_cure_period") ? rs.getString("days_cure_period")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setProjectInConsortium(
						null != rs.getString("is_project_in_consortium") ? rs.getString("is_project_in_consortium")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setCashMilestoneLinked(null != rs.getString("is_cash_milestone_linked_to_bond")
						? rs.getString("is_cash_milestone_linked_to_bond")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setMilestoneId(null != rs.getString("milestoneid") ? rs.getString("milestoneid")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setOpenTransferability(
						null != rs.getString("is_open_transferability") ? rs.getString("is_open_transferability")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setNotificationClauseFlag(
						null != rs.getString("notification_clause_flag") ? rs.getString("notification_clause_flag")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setInternalOrderNo(null != rs.getString("internal_order_no") ? rs.getString("internal_order_no")
						: BillingBankGuaranteeConstants.EMPTY_STRING);
				dto.setMacroGuaranteeType(
						null != rs.getString("macro_guarantee_type") ? rs.getString("macro_guarantee_type")
								: BillingBankGuaranteeConstants.EMPTY_STRING);
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting bank guarantee popup details :: {}" , e.getMessage());
		}
		return list;
	}

}
