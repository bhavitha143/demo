package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.CashCollectionDashboardDAO;
import com.bh.realtrack.dto.CashCollectionDashboardDropDownDTO;
import com.bh.realtrack.dto.CashCollectionDashboardOverallSummaryDetailDTO;
import com.bh.realtrack.dto.CashDashboardManageProjectResponseDTO;
import com.bh.realtrack.dto.CategoryDTO;
import com.bh.realtrack.dto.DownloadOFETargetTemplateDTO;
import com.bh.realtrack.dto.DownloadProjectTemplateDTO;
import com.bh.realtrack.dto.DownloadTPSTargetTemplateDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.FileUploadStatusDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceDetailsDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.OverallSummaryDetailDTO;
import com.bh.realtrack.dto.RegionSummaryDetailDTO;
import com.bh.realtrack.dto.SegmentSummaryDetailDTO;
import com.bh.realtrack.dto.TPSCashDetailDTO;
import com.bh.realtrack.dto.TPSProjectDetailDTO;
import com.bh.realtrack.dto.UpdateRegionTargetDetailsDTO;
import com.bh.realtrack.dto.UpdateTargetDetailsDTO;
import com.bh.realtrack.dto.YearDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.CashCollectionDashboardConstants;

/**
 * @author Thakur Aarthi
 */
@Repository(value = "tpsCashCollectionDashboardDAOImpl")
public class TpsCashCollectionDashboardDAOImpl implements CashCollectionDashboardDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(TpsCashCollectionDashboardDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<YearDTO> getConfiguratorDetails(String companyId) {
		List<YearDTO> yearsDetails = new ArrayList<YearDTO>();

		yearsDetails = jdbcTemplate.query(CashCollectionDashboardConstants.CONFIGURATOR_DETAILS,
				new ResultSetExtractor<List<YearDTO>>() {

					public List<YearDTO> extractData(ResultSet rs) throws SQLException {
						List<YearDTO> yearList = new ArrayList<YearDTO>();
						while (rs.next()) {
							YearDTO eachInvoice = new YearDTO(rs.getString(1), rs.getString(1));
							yearList.add(eachInvoice);
						}
						return yearList;
					}

				});

		return yearsDetails;

	}

	public List<InvoiceDetailsDTO> getOpenInvoiceDetails(String projectId) {
		List<InvoiceDetailsDTO> list = new ArrayList<InvoiceDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_OPEN_INVOICE_DETAILS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				InvoiceDetailsDTO dto = new InvoiceDetailsDTO();

				dto.setInvoiceNo(null != rs.getString("client_billing_document_number_out")
						? rs.getString("client_billing_document_number_out")
						: "");
				dto.setOutstandingAmount(null != rs.getString("ar_out") ? rs.getString("ar_out") : "");
				// dto.setArStatus(null != rs.getString("types") ? rs.getString("types") : "");
				dto.setRisk(null != rs.getString("risk_o") ? rs.getString("risk_o") : "");
				dto.setComments(null != rs.getString("comments_o") ? rs.getString("comments_o") : "");
				dto.setOutstandingAmtInvoice(null != rs.getString("outstanding_amount_invoice_currency_o")
						? rs.getString("outstanding_amount_invoice_currency_o")
						: "");
				dto.setCurrency(null != rs.getString("from_currency_o") ? rs.getString("from_currency_o") : "");
				dto.setInvoiceDate(null != rs.getString("invoice_date_o") ? rs.getString("invoice_date_o") : "");
				dto.setPaymentTerm(
						null != rs.getString("payment_term_description_o") ? rs.getString("payment_term_description_o")
								: "");
				dto.setDueDate(null != rs.getString("invoice_due_date_o") ? rs.getString("invoice_due_date_o") : "");
				dto.setCommittedOn(null != rs.getString("commited_on_o") ? rs.getString("commited_on_o") : "");
				dto.setDispute(null != rs.getString("dispute_status_o") ? rs.getString("dispute_status_o") : "");
				dto.setCollectorComments(
						null != rs.getString("collection_comments_o") ? rs.getString("collection_comments_o") : "");

				list.add(dto);

			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection open invoice details popup :: {}" , e.getMessage());
		}
		return list;
	}

	public List<String> getPmLeaderDropDown(HeaderDashboardDetailsDTO headerDetails) {
		List<String> list = new ArrayList<String>();
		String name = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.GET_PM_LEADER_LIST);) {
			pstm.setInt(1, headerDetails.getCompanyId());
			pstm.setString(2, headerDetails.getBusiness());
			pstm.setString(3, headerDetails.getBusiness());
			pstm.setString(4, headerDetails.getSegment());
			pstm.setString(5, headerDetails.getSegment());
			pstm.setInt(6, headerDetails.getCustomerId());
			pstm.setInt(7, headerDetails.getCustomerId());
			pstm.setString(8, headerDetails.getRegion());
			pstm.setString(9, headerDetails.getRegion());
			pstm.setString(10, headerDetails.getProjectId());
			pstm.setString(11, headerDetails.getProjectId());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
				if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
					list.add(name);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection - pm leader list :: {}" , e.getMessage());
		}
		return list;
	}

	public List<String> getSpmDropDown(HeaderDashboardDetailsDTO headerDetails) {
		List<String> list = new ArrayList<String>();
		String name = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.GET_SPM_LIST);) {
			pstm.setInt(1, headerDetails.getCompanyId());
			pstm.setString(2, headerDetails.getBusiness());
			pstm.setString(3, headerDetails.getBusiness());
			pstm.setString(4, headerDetails.getSegment());
			pstm.setString(5, headerDetails.getSegment());
			pstm.setInt(6, headerDetails.getCustomerId());
			pstm.setInt(7, headerDetails.getCustomerId());
			pstm.setString(8, headerDetails.getRegion());
			pstm.setString(9, headerDetails.getRegion());
			pstm.setString(10, headerDetails.getProjectId());
			pstm.setString(11, headerDetails.getProjectId());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
				if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
					list.add(name);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection - spm list :: {}" , e.getMessage());
		}
		return list;
	}

	public List<String> getImLeaderDropDown(HeaderDashboardDetailsDTO headerDetails) {
		List<String> list = new ArrayList<String>();
		String name = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.GET_IM_LEADER_LIST);) {
			pstm.setInt(1, headerDetails.getCompanyId());
			pstm.setString(2, headerDetails.getBusiness());
			pstm.setString(3, headerDetails.getBusiness());
			pstm.setString(4, headerDetails.getSegment());
			pstm.setString(5, headerDetails.getSegment());
			pstm.setInt(6, headerDetails.getCustomerId());
			pstm.setInt(7, headerDetails.getCustomerId());
			pstm.setString(8, headerDetails.getRegion());
			pstm.setString(9, headerDetails.getRegion());
			pstm.setString(10, headerDetails.getProjectId());
			pstm.setString(11, headerDetails.getProjectId());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
				if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
					list.add(name);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection - im leader list :: {}" , e.getMessage());
		}
		return list;
	}

	public List<String> getFinancialSegmentDropDown(HeaderDashboardDetailsDTO headerDetails) {
		List<String> list = new ArrayList<String>();
		String projectIds = "", name = "";
		projectIds = getProjectListUsingTopFilter(headerDetails);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_FINANCIAL_SEGMENT_LIST);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, projectIds);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
				if (name != null && !name.equalsIgnoreCase("") && !name.equalsIgnoreCase("")) {
					list.add(name);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection - financial segment list :: {}" , e.getMessage());
		}
		return list;
	}

	@SuppressWarnings("deprecation")
	private String getProjectListUsingTopFilter(HeaderDashboardDetailsDTO headerDetails) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_PROJECT_LIST_USING_TOP_FILTER,
				new Object[] { headerDetails.getCompanyId(), headerDetails.getBusiness(), headerDetails.getBusiness(),
						headerDetails.getSegment(), headerDetails.getSegment(), headerDetails.getCustomerId(),
						headerDetails.getCustomerId(), headerDetails.getRegion(), headerDetails.getRegion(),
						headerDetails.getProjectId(), headerDetails.getProjectId(), headerDetails.getBusiness(),
						headerDetails.getBusiness(), headerDetails.getSegment(), headerDetails.getSegment(),
						headerDetails.getRegion(), headerDetails.getRegion() },
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

	public List<String> getQuarterList() {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_QUARTER_LIST, new Object[] {},
				new ResultSetExtractor<List<String>>() {
					public List<String> extractData(ResultSet rs) throws SQLException {
						String quarters = "";
						List<String> list = new ArrayList<String>();
						while (rs.next()) {
							quarters = rs.getString("attribute_value");
							if (quarters != null) {
								String[] quarterStr = quarters.split(";");
								list = Arrays.asList(quarterStr);
							}
						}
						return list;
					}
				});
	}

	public String getProjectList(CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		String projectIds = "", project = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.GET_PROJECT_LIST);) {
			pstm.setInt(1, kpiValues.getCompanyId());
			pstm.setString(2, kpiValues.getBusiness());
			pstm.setString(3, kpiValues.getBusiness());
			pstm.setString(4, kpiValues.getSegment());
			pstm.setString(5, kpiValues.getSegment());
			pstm.setInt(6, kpiValues.getCustomerId());
			pstm.setInt(7, kpiValues.getCustomerId());
			pstm.setString(8, kpiValues.getRegion());
			pstm.setString(9, kpiValues.getRegion());
			pstm.setString(10, kpiValues.getProjectId());
			pstm.setString(11, kpiValues.getProjectId());
			pstm.setString(12, kpiValues.getPmLeader());
			pstm.setString(13, kpiValues.getPmLeader());
			pstm.setString(14, kpiValues.getSpm());
			pstm.setString(15, kpiValues.getSpm());
			pstm.setString(16, kpiValues.getFinancialSegment());
			pstm.setString(17, kpiValues.getFinancialSegment());
			pstm.setString(18, kpiValues.getBusiness());
			pstm.setString(19, kpiValues.getBusiness());
			pstm.setString(20, kpiValues.getSegment());
			pstm.setString(21, kpiValues.getSegment());
			pstm.setString(22, kpiValues.getRegion());
			pstm.setString(23, kpiValues.getRegion());
			pstm.setString(24, kpiValues.getPmLeader());
			pstm.setString(25, kpiValues.getPmLeader());
			pstm.setString(26, kpiValues.getSpm());
			pstm.setString(27, kpiValues.getSpm());
			pstm.setString(28, kpiValues.getFinancialSegment());
			pstm.setString(29, kpiValues.getFinancialSegment());
			pstm.setString(30, kpiValues.getProjectId());
			pstm.setString(31, kpiValues.getProjectId());
			pstm.setString(32, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !projectIds.equalsIgnoreCase("")) {
					projectIds = projectIds + ";" + project;
				} else {
					projectIds = project;
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection project list :: {}" , e.getMessage());
		}
		return projectIds;
	}

	@Override
	public CashCollectionDashboardDropDownDTO getCashCollectionDashboardDropDown(
			HeaderDashboardDetailsDTO headerDetails) {
		List<String> spm = getSpmDropDown(headerDetails);
		List<String> pmLeader = getPmLeaderDropDown(headerDetails);
		List<String> imLeader = getImLeaderDropDown(headerDetails);
		List<String> quarter = getQuarterList();
		List<String> financialSegment = getFinancialSegmentDropDown(headerDetails);
		List<String> tier3 = new ArrayList<String>();
		tier3.add("Equipment");
		tier3.add("Installation");
		CashCollectionDashboardDropDownDTO dto = new CashCollectionDashboardDropDownDTO();
		dto.setSpm(spm);
		dto.setPmLeader(pmLeader);
		dto.setQuater(quarter);
		dto.setFinancialSegment(financialSegment);
		dto.setTier3(tier3);
		dto.setImLeader(imLeader);
		return dto;
	}

	@Override
	public List<CashDashboardManageProjectResponseDTO> getmanageProjectList(HeaderDashboardDetailsDTO headerDetails,
			String projectId) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_MANAGE_PROJECTS,
				new Object[] { headerDetails.getCompanyId(), headerDetails.getCompanyId(), headerDetails.getBusiness(),
						headerDetails.getBusiness(), headerDetails.getSegment(), headerDetails.getSegment(),
						headerDetails.getRegion(), headerDetails.getRegion(), headerDetails.getCustomerId(),
						headerDetails.getCustomerId() },
				new ResultSetExtractor<List<CashDashboardManageProjectResponseDTO>>() {
					public List<CashDashboardManageProjectResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<CashDashboardManageProjectResponseDTO> list = new ArrayList<CashDashboardManageProjectResponseDTO>();
						while (rs.next()) {
							CashDashboardManageProjectResponseDTO dto = new CashDashboardManageProjectResponseDTO();

							dto.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setBusinessUnit(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setSegment(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCustomerName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setCompanyName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");

							list.add(dto);
						}

						return list;
					}
				});
	}

	public OverallSummaryDetailDTO getCashCollectionDashboardTPSOverallSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		OverallSummaryDetailDTO dto = new OverallSummaryDetailDTO();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_OVERALL_SUMMARY_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NumberFormat df = new DecimalFormat("#.##");
				Double cashToGo = 0.0, cashLe = 0.0, collectedFWValue = 0.0;
				dto.setCurrentFW(null != rs.getString("opastdue_fw") ? rs.getString("opastdue_fw") : "");
				dto.setCollectedFW(null != rs.getString("oCollectedCash_FW") ? rs.getString("oCollectedCash_FW") : "");
				dto.setPastDueFWValue(null != rs.getString("opastdue") ? rs.getString("opastdue") : "");
				dto.setPastDueLE(null != rs.getString("opastdue_le") ? rs.getString("opastdue_le") : "");
				dto.setCashLE(null != rs.getString("ocashle") ? rs.getString("ocashle") : "");
				dto.setCollectedFWValue(null != rs.getString("ocollectedcash") ? rs.getString("ocollectedcash") : "");

				// Modified by Tushar Chavda
				// Dt: 2022-06-30
				dto.setDisputedAmt(null != rs.getString("disputed_amt") ? rs.getString("disputed_amt") : "");
				dto.setDisputedAmtAged(
						null != rs.getString("disputed_amt_aged") ? rs.getString("disputed_amt_aged") : "");

				if (null != rs.getString("ocashle")) {
					cashLe = rs.getDouble("ocashle");
				}
				if (null != rs.getString("ocollectedcash")) {
					collectedFWValue = rs.getDouble("ocollectedcash");
				}
				if (null != cashLe && null != collectedFWValue) {
					cashToGo = cashLe - collectedFWValue;
				}
				dto.setCashToGo(null != cashToGo && (cashToGo > 0) ? String.valueOf(df.format(cashToGo)) : "");
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection overall summary details :: {}" , e.getMessage());
		}
		return dto;
	}

	public List<SegmentSummaryDetailDTO> getCashCollectionDashboardSegmentSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<SegmentSummaryDetailDTO> list = new ArrayList<SegmentSummaryDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_SEGMENT_SUMMARY_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SegmentSummaryDetailDTO dto = new SegmentSummaryDetailDTO();

				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setPqCollection(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setTotalCollectionTarget(null != rs.getString("total_collection_target")
						&& !(rs.getDouble("total_collection_target") == 0) ? rs.getString("total_collection_target")
								: "");
				dto.setCollectedMMsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollecttionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPdTarget(null != rs.getString("pd_target") && !(rs.getDouble("pd_target") == 0)
						? rs.getString("pd_target")
						: "");
				dto.setPastDueMMUsd(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setCollectionVTGT(null != rs.getString("collection_vtgt") ? rs.getString("collection_vtgt") : "");
				dto.setPdVTGT(null != rs.getString("pd_vtgt") ? rs.getString("pd_vtgt") : "");

				// Modified by Tushar Chavda
				// Dt: 2022-06-30
				dto.setDisputedAmt(null != rs.getString("disp_amt") ? rs.getString("disp_amt") : "");
				dto.setDisputedAmtAged(null != rs.getString("disp_amt_aged") ? rs.getString("disp_amt_aged") : "");

				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection segment summary details :: {}" , e.getMessage());
		}
		return list;
	}

	public List<TPSCashDetailDTO> getCashCollectionDashboardTPSCashDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<TPSCashDetailDTO> list = new ArrayList<TPSCashDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_CASH_TAB_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				TPSCashDetailDTO dto = new TPSCashDetailDTO();
				dto.setProjectId(null != rs.getString("o_project_id") ? rs.getString("o_project_id") : "");
				dto.setProjectName(
						null != rs.getString("o_master_project_name") ? rs.getString("o_master_project_name") : "");
				dto.setCustomer(null != rs.getString("o_contract_customer") ? rs.getString("o_contract_customer") : "");
				dto.setSegmentBusinessOFE(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setRegion(null != rs.getString("o_region") ? rs.getString("o_region") : "");
				dto.setInvoiceNumber(null != rs.getString("o_invoice_number") ? rs.getString("o_invoice_number") : "");
				dto.setRiskOppty(null != rs.getString("o_risk") ? rs.getString("o_risk") : "");
				dto.setInvoiceDate(
						null != rs.getString("o_actual_invoice_dt") ? rs.getString("o_actual_invoice_dt") : "");
				dto.setInvoiceDueDate(
						null != rs.getString("o_invoice_due_date") ? rs.getString("o_invoice_due_date") : "");
				dto.setCollectionDate(null != rs.getString("o_payment_date") ? rs.getString("o_payment_date") : "");
				dto.setCurrencyBillingData(
						null != rs.getString("o_orig_currency") ? rs.getString("o_orig_currency") : "");
				dto.setInvoiceAmountCurr(
						null != rs.getString("o_invoice_currency") ? rs.getString("o_invoice_currency") : "");
				dto.setInvoiceAmountDollar(
						null != rs.getString("o_invoice_amount_usd") && !(rs.getDouble("o_invoice_amount_usd") == 0)
								? rs.getString("o_invoice_amount_usd")
								: "");
				dto.setOutstandingAmountCurr(
						null != rs.getString("o_from_currency") ? rs.getString("o_from_currency") : "");
				dto.setOutstandingAmountDollar(
						null != rs.getString("o_open_invoice_amount") && !(rs.getDouble("o_open_invoice_amount") == 0)
								? rs.getString("o_open_invoice_amount")
								: "");
				dto.setCashCollectedCurr(
						null != rs.getString("o_local_currency_code") ? rs.getString("o_local_currency_code") : "");
				dto.setCashCollectedDollar(
						null != rs.getString("o_cash_collected_local") && !(rs.getDouble("o_cash_collected_local") == 0)
								? rs.getString("o_cash_collected_local")
								: "");
				dto.setCashLEDollar(
						null != rs.getString("o_cashle") && !(rs.getDouble("o_cashle") == 0) ? rs.getString("o_cashle")
								: "");
				dto.setStatus(null != rs.getString("o_status") ? rs.getString("o_status") : "");
				dto.setMilestoneId(null != rs.getString("o_milestone_id") ? rs.getString("o_milestone_id") : "");
				dto.setMilestoneDesc(null != rs.getString("o_milestone_desc") ? rs.getString("o_milestone_desc") : "");
				dto.setForecastDate(null != rs.getString("o_forecast_date") ? rs.getString("o_forecast_date") : "");
				dto.setForecastCollectionDate(
						null != rs.getString("o_forecast_coll_date") ? rs.getString("o_forecast_coll_date") : "");
				dto.setCurrency(null != rs.getString("o_bill_curr") ? rs.getString("o_bill_curr") : "");
				dto.setMilestoneAmountCurr(
						null != rs.getString("o_mil_amount_curr") ? rs.getString("o_mil_amount_curr") : "");
				dto.setMilestoneAmountDollar(
						null != rs.getString("o_milestone_amount") && !(rs.getDouble("o_milestone_amount") == 0)
								? rs.getString("o_milestone_amount")
								: "");
				dto.setRiskComments(null != rs.getString("o_comment") ? rs.getString("o_comment") : "");
				dto.setBillingRiskOppty(
						null != rs.getString("o_billi_risk_oppty") ? rs.getString("o_billi_risk_oppty") : "");
				dto.setDaysToCollect(
						null != rs.getString("o_days_to_collect") && !(rs.getDouble("o_days_to_collect") == 0)
								? rs.getString("o_days_to_collect")
								: "");
				dto.setFinancialSegment(
						null != rs.getString("o_financial_segment") ? rs.getString("o_financial_segment") : "");

				// Modified by Tushar Chavda
				// Dt: 2022-07-01
				dto.setColCommitDtOut(
						null != rs.getString("col_commit_dt_out") ? rs.getString("col_commit_dt_out") : "");
				dto.setDispStatusOut(null != rs.getString("disp_status_out") ? rs.getString("disp_status_out") : "");
				dto.setDispNumberOut(null != rs.getString("disp_number_out") ? rs.getString("disp_number_out") : "");
				dto.setDispCategoryOut(
						null != rs.getString("disp_category_out") ? rs.getString("disp_category_out") : "");
				dto.setDispCodeDescOut(
						null != rs.getString("disp_code_desc_out") ? rs.getString("disp_code_desc_out") : "");
				dto.setDispDtFirstCommentOut(
						null != rs.getString("disp_dt_first_comment_out") ? rs.getString("disp_dt_first_comment_out")
								: "");
				dto.setLatestDispCmtDtDDispOut(null != rs.getString("latest_disp_cmt_dt_disp_out")
						? rs.getString("latest_disp_cmt_dt_disp_out")
						: "");
				dto.setDispOwnerOut(null != rs.getString("disp_Owner_out") ? rs.getString("disp_Owner_out") : "");
				dto.setDispMgrLvl1Out(
						null != rs.getString("disp_mgr_lvl1_out") ? rs.getString("disp_mgr_lvl1_out") : "");
				dto.setDispAgeingBuckOut(
						null != rs.getString("disp_ageing_buck_out") ? rs.getString("disp_ageing_buck_out") : "");
				dto.setCustEscalatedOut(
						null != rs.getString("cust_escalated_out") ? rs.getString("cust_escalated_out") : "");
				dto.setCustEscalatedToOut(
						null != rs.getString("cust_escalated_to_out") ? rs.getString("cust_escalated_to_out") : "");

				dto.setBcBilled(null != rs.getString("b_c_billed_o") ? rs.getString("b_c_billed_o") : "");
				dto.setBcToGo(null != rs.getString("b_c_to_go_o") ? rs.getString("b_c_to_go_o") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection cash details :: {}" , e.getMessage());
		}
		return list;
	}

	public List<TPSProjectDetailDTO> getCashCollectionDashboardTPSProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<TPSProjectDetailDTO> list = new ArrayList<TPSProjectDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_PROJECT_TAB_DETAILS)) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				TPSProjectDetailDTO dto = new TPSProjectDetailDTO();
				dto.setPrjId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setProjectName(
						null != rs.getString("master_project_name") ? rs.getString("master_project_name") : "");
				dto.setContractCustomer(
						null != rs.getString("contract_customer") ? rs.getString("contract_customer") : "");
				dto.setPm(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
				dto.setSpm(null != rs.getString("spm_out") ? rs.getString("spm_out") : "");
				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setPqCollectionMMUsd(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setCollectedMMUsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollectionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPastDueMMUSD(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setAr(null != rs.getString("o_ar") ? rs.getString("o_ar") : "");
				dto.setInstallCountry(
						null != rs.getString("o_install_country") ? rs.getString("o_install_country") : "");
				dto.setFinancialSegment(
						null != rs.getString("financial_segment_out") ? rs.getString("financial_segment_out") : "");
				dto.setBcBilled(null != rs.getString("b_c_billed_o") ? rs.getString("b_c_billed_o") : "");
				dto.setBcToGo(null != rs.getString("b_c_to_go_o") ? rs.getString("b_c_to_go_o") : "");
				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection project details :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String companyId) {
		List<LastSuccessfulUpdateDetailsDTO> detailsList = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.LAST_SUCCESSFULL_UPDATE_DATA);) {
			pstm.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LastSuccessfulUpdateDetailsDTO detail = new LastSuccessfulUpdateDetailsDTO(rs.getString(1),
						rs.getString(2), rs.getString(3), rs.getString(4));
				detailsList.add(detail);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Last Successful Update details ::{} " , e.getMessage());
		}
		return detailsList;
	}

	public List<LastSuccessfulUpdateDetailsDTO> getInstallLastSuccessfulUpdateData(String companyId) {
		List<LastSuccessfulUpdateDetailsDTO> detailsList = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_INSTALL_LAST_SUCCESSFULL_UPDATE_DATA);) {
			pstm.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LastSuccessfulUpdateDetailsDTO detail = new LastSuccessfulUpdateDetailsDTO(rs.getString(1),
						rs.getString(2), rs.getString(3), rs.getString(4));
				detailsList.add(detail);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Install Last Successful Update details :: {}"
					, e.getMessage());
		}
		return detailsList;
	}

	@Override
	public List<LastUpdateDetailsDTO> getLastUpdatedDate(String companyId) {
		List<LastUpdateDetailsDTO> detailsList = new ArrayList<LastUpdateDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.LAST_UPDATE_DATA);) {
			pstm.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LastUpdateDetailsDTO detail = new LastUpdateDetailsDTO(rs.getString("insert_by"),
						rs.getString("status"), rs.getString("update_dt"), rs.getString("upload_file_name"),
						rs.getString("total_records"), rs.getString("not_processed_record"));
				detailsList.add(detail);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Last Update date :: {}" , e.getMessage());
		}
		return detailsList;
	}

	public List<LastUpdateDetailsDTO> getInstallLastUpdatedDate(String companyId) {
		List<LastUpdateDetailsDTO> detailsList = new ArrayList<LastUpdateDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_INSTALL_LAST_UPDATE_DATA);) {
			pstm.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LastUpdateDetailsDTO detail = new LastUpdateDetailsDTO(rs.getString("insert_by"),
						rs.getString("status"), rs.getString("update_dt"), rs.getString("upload_file_name"),
						rs.getString("total_records"), rs.getString("not_processed_record"));
				detailsList.add(detail);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Install Last Update date :: {}" , e.getMessage());
		}
		return detailsList;
	}

	@Override
	public List<ErrorDetailsDTO> getErrorDetailsData(String companyId) {
		List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.ERROR_DETAILS_DATA);) {
			pstm.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String errmessage = rs.getString(2);
				if (null != errmessage && !errmessage.equalsIgnoreCase("")) {
					ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS", errmessage);
					detailsList.add(detail);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Error Details :: {}" , e.getMessage());
		}
		return detailsList;
	}

	public List<ErrorDetailsDTO> getInstallErrorDetailsData(String companyId) {
		List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_INSTALL_ERROR_DETAILS_DATA);) {
			pstm.setInt(1, Integer.parseInt(companyId));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String errmessage = rs.getString(2);
				if (null != errmessage && !errmessage.equalsIgnoreCase("")) {
					ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS", errmessage);
					detailsList.add(detail);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Install Error Details :: {}" , e.getMessage());
		}
		return detailsList;
	}

	@Override
	public List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId) {
		List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_NOT_PROCESSED_PROJECT_DETAILS);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String errmessage = rs.getString("projectid");
				if (null != errmessage && !errmessage.equalsIgnoreCase("")) {
					ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS", "Projects " + errmessage + " already present");
					detailsList.add(detail);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Not Processed Project Details :: {}" , e.getMessage());
		}
		return detailsList;
	}

	public List<ErrorDetailsDTO> getInstallNotProcessedProjectDetails(String companyId) {
		List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						CashCollectionDashboardConstants.GET_INSTALL_NOT_PROCESSED_PROJECT_DETAILS);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String errmessage = rs.getString("projectid");
				if (null != errmessage && !errmessage.equalsIgnoreCase("")) {
					ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS", "Projects " + errmessage + " already present");
					detailsList.add(detail);
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection Install Not Processed Project Details :: {}"
					, e.getMessage());
		}
		return detailsList;
	}

	public List<UpdateTargetDetailsDTO> getUpdatedTargetDetails(String companyId, String year, String category) {
		List<UpdateTargetDetailsDTO> detailsList = new ArrayList<UpdateTargetDetailsDTO>();
		Map<String, UpdateTargetDetailsDTO> segmentMap = new HashMap<String, UpdateTargetDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_UPDATED_TARGET_DETAILS);) {
			pstm.setString(1, year);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				UpdateTargetDetailsDTO detail = new UpdateTargetDetailsDTO();
				String remark = "", segment = "", yearQuarter = "", value = "";
				segment = rs.getString("segment");
				remark = rs.getString("remarks");
				yearQuarter = rs.getString("year_quarter");
				value = rs.getString("value");
				detail = segmentMap.get(segment);
				if (null == detail) {
					detail = new UpdateTargetDetailsDTO();
					detail.setSegment(segment);
					segmentMap.put(segment, detail);
					detailsList.add(detail);
				}
				if (category.equalsIgnoreCase("Equipment Segment")) {
					if (remark.equalsIgnoreCase("Cash")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3CollectionTgt(value);
						} else {
							detail.setQ4CollectionTgt(value);
						}
					} else if (remark.equalsIgnoreCase("PD")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3PDTgt(value);
						} else {
							detail.setQ4PDTgt(value);
						}
					} else if (remark.equalsIgnoreCase("BILL_TGT")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1BillingTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2BillingTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3BillingTgt(value);
						} else {
							detail.setQ4BillingTgt(value);
						}
					}
				} else if (category.equalsIgnoreCase("Installation Segment")) {
					if (remark.equalsIgnoreCase("INSTALL_CASH")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3CollectionTgt(value);
						} else {
							detail.setQ4CollectionTgt(value);
						}
					} else if (remark.equalsIgnoreCase("INSTALL_PD")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3PDTgt(value);
						} else {
							detail.setQ4PDTgt(value);
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting target segment details :: {}" , e.getMessage());
		}
		return detailsList;
	}

	public List<LastSuccessfulUpdateDetailsDTO> getTargetLastSuccessfulUpdateData(String companyId) {

		return jdbcTemplate.query(CashCollectionDashboardConstants.TARGET_LAST_SUCCESSFUL_UPDATE_DATA,
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

	public List<LastUpdateDetailsDTO> getTargetLastUpdatedDate(String companyId) {

		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_TARGET_LAST_UPDATED_DATE,
				new Object[] { Integer.parseInt(companyId) }, new ResultSetExtractor<List<LastUpdateDetailsDTO>>() {

					public List<LastUpdateDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<LastUpdateDetailsDTO> detailsList = new ArrayList<LastUpdateDetailsDTO>();

						while (rs.next()) {
							LastUpdateDetailsDTO detail = new LastUpdateDetailsDTO(rs.getString(1), rs.getString(2),
									rs.getString(3), rs.getString(4));
							detailsList.add(detail);
						}
						return detailsList;
					}

				});

	}

	public List<ErrorDetailsDTO> getTargetErrorDetailsData(String companyId) {

		return jdbcTemplate.query(CashCollectionDashboardConstants.TARGET_ERROR_DETAILS_DATA,
				new Object[] { Integer.parseInt(companyId) }, new ResultSetExtractor<List<ErrorDetailsDTO>>() {

					public List<ErrorDetailsDTO> extractData(ResultSet rs) throws SQLException {
						List<ErrorDetailsDTO> detailsList = new ArrayList<ErrorDetailsDTO>();
						while (rs.next()) {
							String errMsg = rs.getString(2);
							if (null != errMsg && !errMsg.equalsIgnoreCase("")) {
								ErrorDetailsDTO detail = new ErrorDetailsDTO("TPS", errMsg);
								detailsList.add(detail);
							}
						}
						return detailsList;
					}
				});
	}

	public boolean getFileUploadStatus(String companyId, String moduleName) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_FILE_UPLOAD_STATUS,
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

	public Map<Integer, String> fetchTargetExcelHeaderColumnMap(String companyId) {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		if (companyId.equalsIgnoreCase("2")) {
			headerIndexMap.put(0, "Business");
			headerIndexMap.put(1, "Segment / Region");
			headerIndexMap.put(2, "Quarter");
			headerIndexMap.put(3, "Remark");
			headerIndexMap.put(4, "Value");
			headerIndexMap.put(5, "Category");

		} else if (companyId.equalsIgnoreCase("4")) {
			headerIndexMap.put(0, "Project ID");
			headerIndexMap.put(1, "Segment");
			headerIndexMap.put(2, "Quarter");
			headerIndexMap.put(3, "Remark");
			headerIndexMap.put(4, "Value");
		}
		return headerIndexMap;
	}

	public Map<Integer, String> getProjectExcelHeaderColumnMap(String companyId) {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap.put(0, "Project Id");
		headerIndexMap.put(1, "PM");
		headerIndexMap.put(2, "SPM");
		headerIndexMap.put(3, "PM Leader");
		headerIndexMap.put(4, "Project Name");
		headerIndexMap.put(5, "Contract Customer");
		headerIndexMap.put(6, "Business");
		headerIndexMap.put(7, "Segment");
		headerIndexMap.put(8, "Region");
		headerIndexMap.put(9, "Invoice Number");
		headerIndexMap.put(10, "Risk/Oppty");
		headerIndexMap.put(11, "Risk Comments");
		headerIndexMap.put(12, "Invoice Date");
		headerIndexMap.put(13, "Invoice Due Date");
		headerIndexMap.put(14, "Collection Date");
		headerIndexMap.put(15, "Currency");
		headerIndexMap.put(16, "Invoice Amount Curr");
		headerIndexMap.put(17, "Outstanding Amount Curr");
		headerIndexMap.put(18, "Cash Collected Curr");
		headerIndexMap.put(19, "Milestone ID");
		headerIndexMap.put(20, "Milestone Desc");
		headerIndexMap.put(21, "Forecast Collection Date");
		headerIndexMap.put(22, "Billing Currency");
		headerIndexMap.put(23, "Milestone Amount Curr");
		return headerIndexMap;
	}

	public Map<Integer, String> getInstallProjectExcelHeaderColumnMap(String companyId) {
		Map<Integer, String> headerIndexMap = new HashMap<Integer, String>();
		headerIndexMap.put(0, "Installation Id");
		headerIndexMap.put(1, "IM");
		headerIndexMap.put(2, "IM Leader");
		headerIndexMap.put(3, "Installation Project Name");
		headerIndexMap.put(4, "Contract Customer");
		headerIndexMap.put(5, "Business");
		headerIndexMap.put(6, "Segment");
		headerIndexMap.put(7, "Region");
		headerIndexMap.put(8, "Invoice Number");
		headerIndexMap.put(9, "Risk/Oppty");
		headerIndexMap.put(10, "Risk Comments");
		headerIndexMap.put(11, "Invoice Date");
		headerIndexMap.put(12, "Invoice Due Date");
		headerIndexMap.put(13, "Collection Date");
		headerIndexMap.put(14, "Currency");
		headerIndexMap.put(15, "Invoice Amount Curr");
		headerIndexMap.put(16, "Outstanding Amount Curr");
		headerIndexMap.put(17, "Cash Collected Curr");
		return headerIndexMap;
	}

	public void insertFileTrackingDetails(FileUploadStatusDTO statusDTO) {
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.INSERT_FILE_UPLOAD_STATUS);
			pstm.setInt(1, Integer.parseInt(statusDTO.getCompanyId()));
			pstm.setString(2, statusDTO.getModuleName());
			pstm.setString(3, statusDTO.getStatus());
			pstm.setString(4, statusDTO.getFileName());
			pstm.setString(5, statusDTO.getSso());
			pstm.setString(6, statusDTO.getSso());
			if (pstm.executeUpdate() > 0) {
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while inserting file upload status:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("something went wrong while inserting file upload status:{}" , e.getMessage());
				}
			}
		}
	}

	public int getFileTrackingId(FileUploadStatusDTO statusDTO) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_TRACKING_ID, new Object[] {
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

	public void updateFileTrackingDetails(FileUploadStatusDTO statusDTO) {
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(CashCollectionDashboardConstants.UPDATE_FILE_UPLOAD_STATUS);
			pstm.setString(1, statusDTO.getStatus());
			pstm.setString(2, statusDTO.getErrorMsg());
			pstm.setString(3, statusDTO.getSso());
			pstm.setInt(4, Integer.parseInt(statusDTO.getTrackingId()));
			if (pstm.executeUpdate() > 0) {

			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while updating file upload status:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					LOGGER.error("something went wrong while updating file upload status:{}" , e.getMessage());
				}
			}
		}
	}

	public void deleteFileUploadStageData(FileUploadStatusDTO statusDTO) {
		String query = "";
		if (statusDTO.getModuleName().equalsIgnoreCase(CashCollectionDashboardConstants.GET_TARGET_EXCEL_MODULE_NAME)) {
			query = CashCollectionDashboardConstants.DELETE_TARGET_EXCEL_STAGE_DATA;
		} else if (statusDTO.getModuleName()
				.equalsIgnoreCase(CashCollectionDashboardConstants.GET_PROJECT_EXCEL_MODULE_NAME)) {
			query = CashCollectionDashboardConstants.DELETE_PROJECT_EXCEL_STAGE_DATA;
		} else if (statusDTO.getModuleName()
				.equalsIgnoreCase(CashCollectionDashboardConstants.GET_INSTALL_PROJECT_EXCEL_MODULE_NAME)) {
			query = CashCollectionDashboardConstants.DELETE_INSTALL_PROJECT_EXCEL_STAGE_DATA;
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setInt(1, Integer.parseInt(statusDTO.getCompanyId()));
			pstm.executeUpdate();
		} catch (Exception e) {
			LOGGER.error("Exception while deleting cash collection file upload stage table data :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void insertTPSTargetExcelStageData(List<DownloadTPSTargetTemplateDTO> list, FileUploadStatusDTO statusDTO) {
		int batchIndex = 0;
		deleteFileUploadStageData(statusDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.INSERT_TARGET_EXCEL_STAGE_DATA);) {
			for (DownloadTPSTargetTemplateDTO dto : list) {
				pstm.setString(1, dto.getBusiness());
				pstm.setString(2, dto.getSegment());
				pstm.setDate(3, getFileUploadDate(dto.getStartDt()));
				pstm.setDate(4, getFileUploadDate(dto.getEndDt()));
				pstm.setString(5, dto.getQuarter());
				pstm.setString(6, dto.getRemarks());
				pstm.setDouble(7, Double.parseDouble(dto.getValue()));
				pstm.setDouble(8, Integer.parseInt(statusDTO.getCompanyId()));
				pstm.setString(9, dto.getCategory());
				pstm.addBatch();
				batchIndex++;
				if (batchIndex % 500 == 0) {
					LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
					pstm.executeBatch();
				}
			}
			LOGGER.info("Inserting {}" , batchIndex + "{} rows into Stage table.");
			pstm.executeBatch();
		} catch (Exception e) {
			LOGGER.error("Exception while inserting data in TPS target excel stage table :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void insertOFETargetExcelStageData(List<DownloadOFETargetTemplateDTO> list, FileUploadStatusDTO statusDTO) {
		int batchIndex = 0;
		deleteFileUploadStageData(statusDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.INSERT_TARGET_EXCEL_STAGE_DATA);) {
			for (DownloadOFETargetTemplateDTO dto : list) {
				pstm.setString(1, dto.getProjectId());
				pstm.setString(2, dto.getSegment());
				if (dto.getStartDt() != null && !dto.getStartDt().equalsIgnoreCase("")) {
					pstm.setDate(3, getFileUploadDate(dto.getStartDt()));
				} else {
					pstm.setNull(3, java.sql.Types.DATE);
				}
				if (dto.getEndDt() != null && !dto.getEndDt().equalsIgnoreCase("")) {
					pstm.setDate(4, getFileUploadDate(dto.getEndDt()));
				} else {
					pstm.setNull(4, java.sql.Types.DATE);
				}
				pstm.setString(5, dto.getQuarter());
				pstm.setString(6, dto.getRemarks());
				if (dto.getValue() != null && !dto.getValue().equalsIgnoreCase("")) {
					pstm.setDouble(7, Double.parseDouble(dto.getValue()));
				} else {
					pstm.setNull(7, java.sql.Types.DOUBLE);
				}
				pstm.setDouble(8, Integer.parseInt(statusDTO.getCompanyId()));
				pstm.addBatch();
				batchIndex++;
				if (batchIndex % 500 == 0) {
					LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
					pstm.executeBatch();
				}
			}
			LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
			pstm.executeBatch();
		} catch (Exception e) {
			LOGGER.error("Exception while inserting data in OFE target excel stage table :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void insertProjectExcelStageData(List<DownloadProjectTemplateDTO> list, FileUploadStatusDTO statusDTO) {
		int batchIndex = 0;
		deleteFileUploadStageData(statusDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.INSERT_PROJECT_EXCEL_STAGE_DATA);) {
			for (DownloadProjectTemplateDTO dto : list) {
				pstm.setString(1, dto.getProjectId());
				pstm.setString(2, dto.getPm());
				pstm.setString(3, dto.getSpm());
				pstm.setString(4, dto.getPmLeader());
				pstm.setString(5, dto.getProjectName());
				pstm.setString(6, dto.getContractCustomer());
				pstm.setString(7, dto.getBusiness());
				pstm.setString(8, dto.getSegment());
				pstm.setString(9, dto.getRegion());
				pstm.setString(10, dto.getInvoiceNumber());
				pstm.setString(11, dto.getRiskyOppty());
				pstm.setString(12, dto.getRiskComments());
				if (dto.getInvoiceDate() != null && !dto.getInvoiceDate().equalsIgnoreCase("")) {
					pstm.setDate(13, getFileUploadDate(dto.getInvoiceDate()));
				} else {
					pstm.setNull(13, java.sql.Types.DATE);
				}
				if (dto.getInvoiceDueDate() != null && !dto.getInvoiceDueDate().equalsIgnoreCase("")) {
					pstm.setDate(14, getFileUploadDate(dto.getInvoiceDueDate()));
				} else {
					pstm.setNull(14, java.sql.Types.DATE);
				}
				if (dto.getCollectionDate() != null && !dto.getCollectionDate().equalsIgnoreCase("")) {
					pstm.setDate(15, getFileUploadDate(dto.getCollectionDate()));
				} else {
					pstm.setNull(15, java.sql.Types.DATE);
				}
				pstm.setString(16, dto.getCurrency());// invoice_currency
				if (dto.getInvoiceAmountCurr() != null && !dto.getInvoiceAmountCurr().equalsIgnoreCase("")) {
					pstm.setDouble(17, Double.parseDouble(dto.getInvoiceAmountCurr()));
				} else {
					pstm.setNull(17, java.sql.Types.DOUBLE);
				}
				if (dto.getOutstandingAmountCurr() != null && !dto.getOutstandingAmountCurr().equalsIgnoreCase("")) {
					pstm.setDouble(18, Double.parseDouble(dto.getOutstandingAmountCurr()));
				} else {
					pstm.setNull(18, java.sql.Types.DOUBLE);
				}
				if (dto.getCashCollectedCurr() != null && !dto.getCashCollectedCurr().equalsIgnoreCase("")) {
					pstm.setDouble(19, Double.parseDouble(dto.getCashCollectedCurr()));
				} else {
					pstm.setNull(19, java.sql.Types.DOUBLE);
				}
				pstm.setString(20, dto.getMilestoneID());
				pstm.setString(21, dto.getMilestoneDesc());
				if (dto.getForecastCollectionDate() != null && !dto.getForecastCollectionDate().equalsIgnoreCase("")) {
					pstm.setDate(22, getFileUploadDate(dto.getForecastCollectionDate()));
				} else {
					pstm.setNull(22, java.sql.Types.DATE);
				}
				pstm.setString(23, dto.getBillingCurrency());
				if (dto.getMilestoneAmountCurr() != null && !dto.getMilestoneAmountCurr().equalsIgnoreCase("")) {
					pstm.setDouble(24, Double.parseDouble(dto.getMilestoneAmountCurr()));
				} else {
					pstm.setNull(24, java.sql.Types.DOUBLE);
				}
				pstm.setInt(25, Integer.parseInt(statusDTO.getCompanyId()));
				pstm.addBatch();
				batchIndex++;
				if (batchIndex % 500 == 0) {
					LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
					pstm.executeBatch();
				}
			}
			LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
			pstm.executeBatch();
		} catch (Exception e) {
			LOGGER.error("Exception while inserting data in project excel stage table :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void insertInstallProjectExcelStageData(List<DownloadProjectTemplateDTO> list,
			FileUploadStatusDTO statusDTO) {
		int batchIndex = 0;
		deleteFileUploadStageData(statusDTO);
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.INSERT_INSTALL_PROJECT_EXCEL_STAGE_DATA);) {
			for (DownloadProjectTemplateDTO dto : list) {
				pstm.setString(1, dto.getInstallationJobNumber());
				pstm.setString(2, dto.getPm());
				pstm.setString(3, dto.getPmLeader());
				pstm.setString(4, dto.getProjectName());
				pstm.setString(5, dto.getContractCustomer());
				pstm.setString(6, dto.getBusiness());
				pstm.setString(7, dto.getSegment());
				pstm.setString(8, dto.getRegion());
				pstm.setString(9, dto.getInvoiceNumber());
				pstm.setString(10, dto.getRiskyOppty());
				pstm.setString(11, dto.getRiskComments());
				if (dto.getInvoiceDate() != null && !dto.getInvoiceDate().equalsIgnoreCase("")) {
					pstm.setDate(12, getFileUploadDate(dto.getInvoiceDate()));
				} else {
					pstm.setNull(12, java.sql.Types.DATE);
				}
				if (dto.getInvoiceDueDate() != null && !dto.getInvoiceDueDate().equalsIgnoreCase("")) {
					pstm.setDate(13, getFileUploadDate(dto.getInvoiceDueDate()));
				} else {
					pstm.setNull(13, java.sql.Types.DATE);
				}
				if (dto.getCollectionDate() != null && !dto.getCollectionDate().equalsIgnoreCase("")) {
					pstm.setDate(14, getFileUploadDate(dto.getCollectionDate()));
				} else {
					pstm.setNull(14, java.sql.Types.DATE);
				}
				pstm.setString(15, dto.getCurrency());
				if (dto.getInvoiceAmountCurr() != null && !dto.getInvoiceAmountCurr().equalsIgnoreCase("")) {
					pstm.setDouble(16, Double.parseDouble(dto.getInvoiceAmountCurr()));
				} else {
					pstm.setNull(16, java.sql.Types.DOUBLE);
				}
				if (dto.getOutstandingAmountCurr() != null && !dto.getOutstandingAmountCurr().equalsIgnoreCase("")) {
					pstm.setDouble(17, Double.parseDouble(dto.getOutstandingAmountCurr()));
				} else {
					pstm.setNull(17, java.sql.Types.DOUBLE);
				}
				if (dto.getCashCollectedCurr() != null && !dto.getCashCollectedCurr().equalsIgnoreCase("")) {
					pstm.setDouble(18, Double.parseDouble(dto.getCashCollectedCurr()));
				} else {
					pstm.setNull(18, java.sql.Types.DOUBLE);
				}
				pstm.setInt(19, Integer.parseInt(statusDTO.getCompanyId()));
				pstm.addBatch();
				batchIndex++;
				if (batchIndex % 500 == 0) {
					LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
					pstm.executeBatch();
				}
			}
			LOGGER.info("Inserting " + batchIndex + " rows into Stage table.");
			pstm.executeBatch();
		} catch (Exception e) {
			LOGGER.error("Exception while inserting data in install project excel stage table :: {}" , e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void callFileUploadStageToTarget(FileUploadStatusDTO statusDTO) {
		String query = "";
		if (statusDTO.getModuleName().equalsIgnoreCase(CashCollectionDashboardConstants.GET_TARGET_EXCEL_MODULE_NAME)) {
			query = CashCollectionDashboardConstants.CALL_TARGET_EXCEL_STAGE_TO_TARGET;
		} else if (statusDTO.getModuleName()
				.equalsIgnoreCase(CashCollectionDashboardConstants.GET_PROJECT_EXCEL_MODULE_NAME)) {
			query = CashCollectionDashboardConstants.CALL_PROJECT_EXCEL_STAGE_TO_TARGET;
		} else if (statusDTO.getModuleName()
				.equalsIgnoreCase(CashCollectionDashboardConstants.GET_INSTALL_PROJECT_EXCEL_MODULE_NAME)) {
			query = CashCollectionDashboardConstants.CALL_INSTALL_PROJECT_EXCEL_STAGE_TO_TARGET;
		}
		LOGGER.info("Stage To Target - Track ID :: {}" , statusDTO.getTrackingId());
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query)) {
			pstm.setInt(1, Integer.parseInt(statusDTO.getTrackingId()));
			pstm.setInt(2, Integer.parseInt(statusDTO.getCompanyId()));
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LOGGER.info("Stage To Target Response :: {}" , rs.getString(1));
			}
		} catch (Exception e) {
			LOGGER.error("Exception while executing cash collection file upload stage to target procedure :: {}"
					, e.getMessage());
			statusDTO.setErrorMsg("Error");
			statusDTO.setStatus("Error");
			updateFileTrackingDetails(statusDTO);
		}
	}

	public void callCashCollectionProcedure() {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.CALL_CASH_COLLECTION_PROCEDURE)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LOGGER.info("Cash Collection Proc Response :: {}" , rs.getString(1));
			}
		} catch (Exception e) {
			LOGGER.error("Exception while executing cash collection procedure :: {}" , e.getMessage());
		}
	}

	public void callInstallCashCollectionProcedure() {
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.CALL_INSTALL_CASH_COLLECTION_PROCEDURE)) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				LOGGER.info("Install Cash Collection Proc Response :: {}" , rs.getString(1));
			}
		} catch (Exception e) {
			LOGGER.error("Exception while executing install cash collection procedure :: {}" , e.getMessage());
		}
	}

	public Date getFileUploadDate(String inputDateStr) throws ParseException {
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date outputDate = null;
		String formattedInputDateStr = "";
		if (inputDateStr != null && !inputDateStr.equalsIgnoreCase("")) {
			java.util.Date date = (java.util.Date) inputDateFormat.parse(inputDateStr);
			formattedInputDateStr = inputDateFormat.format(date);
			outputDate = Date.valueOf(formattedInputDateStr);
		}
		return outputDate;
	}

	public List<DownloadProjectTemplateDTO> getTPSProjectTemplateDetails(String companyId) {
		List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_PROJECT_EXCEL_TEMPLATE_DETAILS);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DownloadProjectTemplateDTO dto = new DownloadProjectTemplateDTO();
				dto.setProjectId(null != rs.getString("projectid") ? rs.getString("projectid") : "");
				dto.setPm(null != rs.getString("pm") ? rs.getString("pm") : "");
				dto.setSpm(null != rs.getString("spm") ? rs.getString("spm") : "");
				dto.setPmLeader(null != rs.getString("pmleader") ? rs.getString("pmleader") : "");
				dto.setProjectName(null != rs.getString("projectname") ? rs.getString("projectname") : "");
				dto.setContractCustomer(
						null != rs.getString("contractcustomer") ? rs.getString("contractcustomer") : "");
				dto.setBusiness(null != rs.getString("business_unit") ? rs.getString("business_unit") : "");
				dto.setSegment(null != rs.getString("segment") ? rs.getString("segment") : "");
				dto.setRegion(null != rs.getString("region") ? rs.getString("region") : "");
				dto.setInvoiceNumber(null != rs.getString("invoiceno") ? rs.getString("invoiceno") : "");
				dto.setRiskyOppty(null != rs.getString("risk") ? rs.getString("risk") : "");
				dto.setRiskComments(null != rs.getString("riskcomments") ? rs.getString("riskcomments") : "");
				dto.setInvoiceDate(null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
				dto.setInvoiceDueDate(null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
				dto.setCollectionDate(null != rs.getString("collection_date") ? rs.getString("collection_date") : "");
				dto.setCurrency(null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
				dto.setInvoiceAmountCurr(
						null != rs.getString("invoice_amt_curr") ? rs.getString("invoice_amt_curr") : "");
				dto.setOutstandingAmountCurr(
						null != rs.getString("outstanding_amt_cur") ? rs.getString("outstanding_amt_cur") : "");
				dto.setCashCollectedCurr(null != rs.getString("cash_coll_curr") ? rs.getString("cash_coll_curr") : "");
				dto.setMilestoneID(null != rs.getString("milestone_id") ? rs.getString("milestone_id") : "");
				dto.setMilestoneDesc(null != rs.getString("milestone_desc") ? rs.getString("milestone_desc") : "");
				dto.setForecastCollectionDate(
						null != rs.getString("forecast_coll_date") ? rs.getString("forecast_coll_date") : "");
				dto.setBillingCurrency(null != rs.getString("millestone_curr") ? rs.getString("millestone_curr") : "");
				dto.setMilestoneAmountCurr(null != rs.getString("mile_amt_curr") ? rs.getString("mile_amt_curr") : "");
				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error("Exception while getting project template details :: {}" , e.getMessage());
		}
		return list;
	}

	public List<DownloadProjectTemplateDTO> getTPSInstallProjectTemplateDetails(String companyId) {
		List<DownloadProjectTemplateDTO> list = new ArrayList<DownloadProjectTemplateDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(
						CashCollectionDashboardConstants.GET_INSTALL_PROJECT_EXCEL_TEMPLATE_DETAILS);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DownloadProjectTemplateDTO dto = new DownloadProjectTemplateDTO();
				dto.setProjectId(null != rs.getString("projectid") ? rs.getString("projectid") : "");
				dto.setInstallationJobNumber(
						null != rs.getString("installation_job_number") ? rs.getString("installation_job_number") : "");
				dto.setPm(null != rs.getString("im") ? rs.getString("im") : "");
				dto.setPmLeader(null != rs.getString("im_leader") ? rs.getString("im_leader") : "");
				dto.setProjectName(
						null != rs.getString("installation_project_name") ? rs.getString("installation_project_name")
								: "");
				dto.setContractCustomer(
						null != rs.getString("contractcustomer") ? rs.getString("contractcustomer") : "");
				dto.setBusiness(null != rs.getString("business_unit") ? rs.getString("business_unit") : "");
				dto.setSegment(null != rs.getString("segment") ? rs.getString("segment") : "");
				dto.setRegion(null != rs.getString("region") ? rs.getString("region") : "");
				dto.setInvoiceNumber(null != rs.getString("invoiceno") ? rs.getString("invoiceno") : "");
				dto.setRiskyOppty(null != rs.getString("risk") ? rs.getString("risk") : "");
				dto.setRiskComments(null != rs.getString("riskcomments") ? rs.getString("riskcomments") : "");
				dto.setInvoiceDate(null != rs.getString("invoice_date") ? rs.getString("invoice_date") : "");
				dto.setInvoiceDueDate(null != rs.getString("invoice_due_date") ? rs.getString("invoice_due_date") : "");
				dto.setCollectionDate(null != rs.getString("collection_date") ? rs.getString("collection_date") : "");
				dto.setCurrency(null != rs.getString("invoice_currency") ? rs.getString("invoice_currency") : "");
				dto.setInvoiceAmountCurr(
						null != rs.getString("invoice_amt_curr") ? rs.getString("invoice_amt_curr") : "");
				dto.setOutstandingAmountCurr(
						null != rs.getString("outstanding_amt_cur") ? rs.getString("outstanding_amt_cur") : "");
				dto.setCashCollectedCurr(null != rs.getString("cash_coll_curr") ? rs.getString("cash_coll_curr") : "");
				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error("Exception while getting project template details :: {}" , e.getMessage());
		}
		return list;
	}

	public List<DownloadTPSTargetTemplateDTO> getTPSTargetTemplateDetails(String companyId) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_TARGET_EXCEL_TEMPLATE_DETAILS, new Object[] {},
				new ResultSetExtractor<List<DownloadTPSTargetTemplateDTO>>() {
					public List<DownloadTPSTargetTemplateDTO> extractData(ResultSet rs) throws SQLException {
						List<DownloadTPSTargetTemplateDTO> list = new ArrayList<DownloadTPSTargetTemplateDTO>();
						while (rs.next()) {
							DownloadTPSTargetTemplateDTO dto = new DownloadTPSTargetTemplateDTO();
							dto.setBusiness(null != rs.getString("business_unit") ? rs.getString("business_unit") : "");
							dto.setQuarter(null != rs.getString("year_quarter") ? rs.getString("year_quarter") : "");
							dto.setSegment(null != rs.getString("segment") ? rs.getString("segment") : "");
							dto.setRemarks(null != rs.getString("remarks") ? rs.getString("remarks") : "");
							dto.setValue(null != rs.getString("value") ? rs.getString("value") : "");
							dto.setCategory(null != rs.getString("category") ? rs.getString("category") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	public List<RegionSummaryDetailDTO> getCashCollectionDashboardRegionSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<RegionSummaryDetailDTO> list = new ArrayList<RegionSummaryDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_REGION_SUMMARY_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				RegionSummaryDetailDTO dto = new RegionSummaryDetailDTO();
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setPqCollection(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setTotalCollectionTarget(null != rs.getString("total_collection_target")
						&& !(rs.getDouble("total_collection_target") == 0) ? rs.getString("total_collection_target")
								: "");
				dto.setCollectedMMsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollecttionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPdTarget(null != rs.getString("pd_target") && !(rs.getDouble("pd_target") == 0)
						? rs.getString("pd_target")
						: "");
				dto.setPastDueMMUsd(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setCollectionVTGT(null != rs.getString("collection_vtgt") ? rs.getString("collection_vtgt") : "");
				dto.setPdVTGT(null != rs.getString("pd_vtgt") ? rs.getString("pd_vtgt") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection region summary details :: {}" , e.getMessage());
		}
		return list;
	}

	public List<CategoryDTO> getConfiguratorCategoryDetails(String companyId) {
		List<CategoryDTO> list = new ArrayList<CategoryDTO>();
		CategoryDTO segmentDTO = new CategoryDTO("", "");
		segmentDTO.setKey("Equipment Segment");
		segmentDTO.setValue("Equipment Segment");
		list.add(segmentDTO);

		CategoryDTO installSegmentDTO = new CategoryDTO("", "");
		installSegmentDTO.setKey("Installation Segment");
		installSegmentDTO.setValue("Installation Segment");
		list.add(installSegmentDTO);

		CategoryDTO region = new CategoryDTO("", "");
		region.setKey("Equipment Region");
		region.setValue("Equipment Region");
		list.add(region);

		CategoryDTO installRegion = new CategoryDTO("", "");
		installRegion.setKey("Installation Region");
		installRegion.setValue("Installation Region");
		list.add(installRegion);

		return list;
	}

	public List<TPSProjectDetailDTO> getCashCollectionDashboardTPSConsolidatedProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<TPSProjectDetailDTO> list = new ArrayList<TPSProjectDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_CONSOLIDATED_PROJECT_TAB_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			pstm.setString(4, kpiValues.getBusiness());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				TPSProjectDetailDTO dto = new TPSProjectDetailDTO();
				dto.setPrjId(null != rs.getString("project_id_out") ? rs.getString("project_id_out") : "");
				dto.setProjectName(
						null != rs.getString("master_project_name") ? rs.getString("master_project_name") : "");
				dto.setContractCustomer(
						null != rs.getString("contract_customer") ? rs.getString("contract_customer") : "");
				dto.setPm(null != rs.getString("pm_name") ? rs.getString("pm_name") : "");
				dto.setSpm(null != rs.getString("spm_out") ? rs.getString("spm_out") : "");
				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setPqCollectionMMUsd(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setCollectedMMUsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollectionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPastDueMMUSD(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setFinancialSegment(
						null != rs.getString("financial_segment_out") ? rs.getString("financial_segment_out") : "");
				dto.setBcBilled(null != rs.getString("b_c_billed_o") ? rs.getString("b_c_billed_o") : "");
				dto.setBcToGo(null != rs.getString("b_c_to_go_o") ? rs.getString("b_c_to_go_o") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection consolidated project details :: {}" , e.getMessage());
		}
		return list;
	}

	public Object getUpdatedRegionTargetDetails(String companyId, String year, String category) {
		List<UpdateRegionTargetDetailsDTO> detailsList = new ArrayList<UpdateRegionTargetDetailsDTO>();
		Map<String, UpdateRegionTargetDetailsDTO> regionMap = new HashMap<String, UpdateRegionTargetDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_UPDATED_REGION_TARGET_DETAILS);) {
			pstm.setString(1, year);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				UpdateRegionTargetDetailsDTO detail = new UpdateRegionTargetDetailsDTO();
				String remark = "", region = "", yearQuarter = "", value = "";
				region = rs.getString("region");
				remark = rs.getString("remarks");
				yearQuarter = rs.getString("year_quarter");
				value = rs.getString("value");
				detail = regionMap.get(region);
				if (null == detail) {
					detail = new UpdateRegionTargetDetailsDTO();
					detail = new UpdateRegionTargetDetailsDTO();
					detail.setRegion(region);
					regionMap.put(region, detail);
					detailsList.add(detail);
				}
				if (category.equalsIgnoreCase("Equipment Region")) {
					if (remark.equalsIgnoreCase("Cash")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3CollectionTgt(value);
						} else {
							detail.setQ4CollectionTgt(value);
						}
					} else if (remark.equalsIgnoreCase("PD")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3PDTgt(value);
						} else {
							detail.setQ4PDTgt(value);
						}
					}
				} else if (category.equalsIgnoreCase("Installation Region")) {
					if (remark.equalsIgnoreCase("INSTALL_CASH")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2CollectionTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3CollectionTgt(value);
						} else {
							detail.setQ4CollectionTgt(value);
						}
					} else if (remark.equalsIgnoreCase("INSTALL_PD")) {
						if (yearQuarter.equalsIgnoreCase("1")) {
							detail.setQ1PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("2")) {
							detail.setQ2PDTgt(value);
						} else if (yearQuarter.equalsIgnoreCase("3")) {
							detail.setQ3PDTgt(value);
						} else {
							detail.setQ4PDTgt(value);
						}
					}
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting target region details :: " + e.getMessage());
		}
		return detailsList;
	}

	public OverallSummaryDetailDTO getCashCollectionDashboardTPSInstallOverallSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		OverallSummaryDetailDTO dto = new OverallSummaryDetailDTO();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getInstallProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_INSTALL_OVERALL_SUMMARY_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				NumberFormat df = new DecimalFormat("#.##");
				Double cashToGo = 0.0, cashLe = 0.0, collectedFWValue = 0.0;
				dto.setCurrentFW(null != rs.getString("opastdue_fw") ? rs.getString("opastdue_fw") : "");
				dto.setCollectedFW(null != rs.getString("oCollectedCash_FW") ? rs.getString("oCollectedCash_FW") : "");
				dto.setPastDueFWValue(null != rs.getString("opastdue") ? rs.getString("opastdue") : "");
				dto.setPastDueLE(null != rs.getString("opastdue_le") ? rs.getString("opastdue_le") : "");
				dto.setCashLE(null != rs.getString("ocashle") ? rs.getString("ocashle") : "");
				dto.setCollectedFWValue(null != rs.getString("ocollectedcash") ? rs.getString("ocollectedcash") : "");
				if (null != rs.getString("ocashle")) {
					cashLe = rs.getDouble("ocashle");
				}
				if (null != rs.getString("ocollectedcash")) {
					collectedFWValue = rs.getDouble("ocollectedcash");
				}
				if (null != cashLe && null != collectedFWValue) {
					cashToGo = cashLe - collectedFWValue;
				}
				dto.setCashToGo(null != cashToGo && (cashToGo > 0) ? String.valueOf(df.format(cashToGo)) : "");
			}
		} catch (SQLException e) {
			LOGGER.error(
					"Exception while getting cash collection install overall summary details :: " + e.getMessage());
		}
		return dto;
	}

	public List<SegmentSummaryDetailDTO> getCashCollectionDashboardInstallSegmentSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<SegmentSummaryDetailDTO> list = new ArrayList<SegmentSummaryDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getInstallProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_INSTALL_SEGMENT_SUMMARY_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SegmentSummaryDetailDTO dto = new SegmentSummaryDetailDTO();

				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setPqCollection(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setTotalCollectionTarget(null != rs.getString("total_collection_target")
						&& !(rs.getDouble("total_collection_target") == 0) ? rs.getString("total_collection_target")
								: "");
				dto.setCollectedMMsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollecttionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPdTarget(null != rs.getString("pd_target") && !(rs.getDouble("pd_target") == 0)
						? rs.getString("pd_target")
						: "");
				dto.setPastDueMMUsd(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setCollectionVTGT(null != rs.getString("collection_vtgt") ? rs.getString("collection_vtgt") : "");
				dto.setPdVTGT(null != rs.getString("pd_vtgt") ? rs.getString("pd_vtgt") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error(
					"Exception while getting cash collection install segment summary details :: " + e.getMessage());
		}
		return list;
	}

	public List<RegionSummaryDetailDTO> getCashCollectionDashboardInstallRegionSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<RegionSummaryDetailDTO> list = new ArrayList<RegionSummaryDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getInstallProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_INSTALL_REGION_SUMMARY_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				RegionSummaryDetailDTO dto = new RegionSummaryDetailDTO();
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setPqCollection(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setTotalCollectionTarget(null != rs.getString("total_collection_target")
						&& !(rs.getDouble("total_collection_target") == 0) ? rs.getString("total_collection_target")
								: "");
				dto.setCollectedMMsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollecttionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPdTarget(null != rs.getString("pd_target") && !(rs.getDouble("pd_target") == 0)
						? rs.getString("pd_target")
						: "");
				dto.setPastDueMMUsd(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setCollectionVTGT(null != rs.getString("collection_vtgt") ? rs.getString("collection_vtgt") : "");
				dto.setPdVTGT(null != rs.getString("pd_vtgt") ? rs.getString("pd_vtgt") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection install region summary details :: " + e.getMessage());
		}
		return list;
	}

	public List<TPSProjectDetailDTO> getCashCollectionDashboardTPSInstallProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<TPSProjectDetailDTO> list = new ArrayList<TPSProjectDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getInstallProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_INSTALL_PROJECT_TAB_DETAILS)) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				TPSProjectDetailDTO dto = new TPSProjectDetailDTO();
				dto.setPrjId(null != rs.getString("installation_job_number_out")
						? rs.getString("installation_job_number_out")
						: "");
				dto.setProjectName(null != rs.getString("installation_project_name_out")
						? rs.getString("installation_project_name_out")
						: "");
				dto.setContractCustomer(null != rs.getString("customer_out") ? rs.getString("customer_out") : "");
				dto.setIm(null != rs.getString("im_out") ? rs.getString("im_out") : "");
				dto.setImLeader(null != rs.getString("im_leader_out") ? rs.getString("im_leader_out") : "");
				dto.setSegment(null != rs.getString("segment_out") ? rs.getString("segment_out") : "");
				dto.setRegion(null != rs.getString("region_out") ? rs.getString("region_out") : "");
				dto.setPqCollectionMMUsd(
						null != rs.getString("pq_collection_out") && !(rs.getDouble("pq_collection_out") == 0)
								? rs.getString("pq_collection_out")
								: "");
				dto.setBillAndCashMMUsd(null != rs.getString("bill_n_cash") && !(rs.getDouble("bill_n_cash") == 0)
						? rs.getString("bill_n_cash")
						: "");
				dto.setCashLEMMUsd(
						null != rs.getString("cash_le") && !(rs.getDouble("cash_le") == 0) ? rs.getString("cash_le")
								: "");
				dto.setCollectedMMUsd(
						null != rs.getString("collected_cash_out") && !(rs.getDouble("collected_cash_out") == 0)
								? rs.getString("collected_cash_out")
								: "");
				dto.setCollectionToGoLE(
						null != rs.getString("collected_to_go_le") && !(rs.getDouble("collected_to_go_le") == 0)
								? rs.getString("collected_to_go_le")
								: "");
				dto.setCollectedVPW(null != rs.getString("collected_vpw") && !(rs.getDouble("collected_vpw") == 0)
						? rs.getString("collected_vpw")
						: "");
				dto.setPdLE(null != rs.getString("pastdue_le") && !(rs.getDouble("pastdue_le") == 0)
						? rs.getString("pastdue_le")
						: "");
				dto.setPastDueMMUSD(
						null != rs.getString("pastdue") && !(rs.getDouble("pastdue") == 0) ? rs.getString("pastdue")
								: "");
				dto.setOpp(null != rs.getString("opp") && !(rs.getDouble("opp") == 0) ? rs.getString("opp") : "");
				dto.setMediumHighRisk(
						null != rs.getString("medium_high_risk") && !(rs.getDouble("medium_high_risk") == 0)
								? rs.getString("medium_high_risk")
								: "");
				dto.setDisputedMMUsd(
						null != rs.getString("disputed") && !(rs.getDouble("disputed") == 0) ? rs.getString("disputed")
								: "");
				dto.setEscalatedMMUsd(null != rs.getString("escalated") && !(rs.getDouble("escalated") == 0)
						? rs.getString("escalated")
						: "");
				dto.setLastEstimatePTCW(
						null != rs.getString("last_esti_per_toctw") && !(rs.getDouble("last_esti_per_toctw") == 0)
								? rs.getString("last_esti_per_toctw")
								: "");
				dto.setCollectedVsPTCW(
						null != rs.getString("collected_vs_ptcw") ? rs.getString("collected_vs_ptcw") : "");
				dto.setAr(null != rs.getString("o_ar") ? rs.getString("o_ar") : "");
				dto.setInstallCountry(
						null != rs.getString("o_install_country") ? rs.getString("o_install_country") : "");
				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection install project details :: " + e.getMessage());
		}
		return list;

	}

	public List<TPSCashDetailDTO> getCashCollectionDashboardTPSInstallCashDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<TPSCashDetailDTO> list = new ArrayList<TPSCashDetailDTO>();
		String projectIds = "", startDate = "", endDate = "";
		projectIds = getInstallProjectList(kpiValues);
		startDate = kpiValues.getStartDate();
		endDate = kpiValues.getEndDate();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_TPS_INSTALL_CASH_TAB_DETAILS);) {
			pstm.setString(1, projectIds);
			pstm.setString(2, startDate);
			pstm.setString(3, endDate);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				TPSCashDetailDTO dto = new TPSCashDetailDTO();
				dto.setProjectId(
						null != rs.getString("o_installation_job_number") ? rs.getString("o_installation_job_number")
								: "");
				dto.setProjectName(null != rs.getString("o_installation_project_name")
						? rs.getString("o_installation_project_name")
						: "");
				dto.setCustomer(null != rs.getString("o_customer") ? rs.getString("o_customer") : "");
				dto.setSegmentBusinessOFE(null != rs.getString("o_segment") ? rs.getString("o_segment") : "");
				dto.setRegion(null != rs.getString("o_region") ? rs.getString("o_region") : "");
				dto.setInvoiceNumber(null != rs.getString("o_invoice_number") ? rs.getString("o_invoice_number") : "");
				dto.setRiskOppty(null != rs.getString("o_risk") ? rs.getString("o_risk") : "");
				dto.setInvoiceDate(
						null != rs.getString("o_actual_invoice_dt") ? rs.getString("o_actual_invoice_dt") : "");
				dto.setInvoiceDueDate(
						null != rs.getString("o_invoice_due_date") ? rs.getString("o_invoice_due_date") : "");
				dto.setCollectionDate(null != rs.getString("o_payment_date") ? rs.getString("o_payment_date") : "");
				dto.setCurrencyBillingData(
						null != rs.getString("o_orig_currency") ? rs.getString("o_orig_currency") : "");
				dto.setInvoiceAmountCurr(
						null != rs.getString("o_invoice_currency") ? rs.getString("o_invoice_currency") : "");
				dto.setInvoiceAmountDollar(
						null != rs.getString("o_invoice_amount_usd") && !(rs.getDouble("o_invoice_amount_usd") == 0)
								? rs.getString("o_invoice_amount_usd")
								: "");
				dto.setOutstandingAmountCurr(
						null != rs.getString("o_from_currency") ? rs.getString("o_from_currency") : "");
				dto.setOutstandingAmountDollar(
						null != rs.getString("o_open_invoice_amount") && !(rs.getDouble("o_open_invoice_amount") == 0)
								? rs.getString("o_open_invoice_amount")
								: "");
				dto.setCashCollectedCurr(
						null != rs.getString("o_local_currency_code") ? rs.getString("o_local_currency_code") : "");
				dto.setCashCollectedDollar(
						null != rs.getString("o_cash_collected_local") && !(rs.getDouble("o_cash_collected_local") == 0)
								? rs.getString("o_cash_collected_local")
								: "");
				dto.setCashLEDollar(
						null != rs.getString("o_cashle") && !(rs.getDouble("o_cashle") == 0) ? rs.getString("o_cashle")
								: "");
				dto.setStatus(null != rs.getString("o_status") ? rs.getString("o_status") : "");
				dto.setMilestoneId(null != rs.getString("o_milestone_id") ? rs.getString("o_milestone_id") : "");
				dto.setMilestoneDesc(null != rs.getString("o_milestone_desc") ? rs.getString("o_milestone_desc") : "");
				dto.setForecastDate(null != rs.getString("o_forecast_date") ? rs.getString("o_forecast_date") : "");
				dto.setForecastCollectionDate(
						null != rs.getString("o_forecast_coll_date") ? rs.getString("o_forecast_coll_date") : "");
				dto.setCurrency(null != rs.getString("o_bill_curr") ? rs.getString("o_bill_curr") : "");
				dto.setMilestoneAmountCurr(
						null != rs.getString("o_mil_amount_curr") ? rs.getString("o_mil_amount_curr") : "");
				dto.setMilestoneAmountDollar(
						null != rs.getString("o_milestone_amount") && !(rs.getDouble("o_milestone_amount") == 0)
								? rs.getString("o_milestone_amount")
								: "");
				dto.setRiskComments(null != rs.getString("o_comment") ? rs.getString("o_comment") : "");
				dto.setBillingRiskOppty(
						null != rs.getString("o_billi_risk_oppty") ? rs.getString("o_billi_risk_oppty") : "");
				dto.setDaysToCollect(
						null != rs.getString("o_days_to_collect") && !(rs.getDouble("o_days_to_collect") == 0)
								? rs.getString("o_days_to_collect")
								: "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection install cash details :: " + e.getMessage());
		}
		return list;
	}

	public String getInstallProjectList(CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		String projectIds = "", project = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_INSTALL_PROJECT_LIST);) {
			pstm.setInt(1, kpiValues.getCompanyId());
			pstm.setString(2, kpiValues.getBusiness());
			pstm.setString(3, kpiValues.getBusiness());
			pstm.setString(4, kpiValues.getSegment());
			pstm.setString(5, kpiValues.getSegment());
			pstm.setInt(6, kpiValues.getCustomerId());
			pstm.setInt(7, kpiValues.getCustomerId());
			pstm.setString(8, kpiValues.getRegion());
			pstm.setString(9, kpiValues.getRegion());
			pstm.setString(10, kpiValues.getProjectId());
			pstm.setString(11, kpiValues.getProjectId());
			pstm.setString(12, kpiValues.getPmLeader());
			pstm.setString(13, kpiValues.getPmLeader());
			pstm.setString(14, kpiValues.getSpm());
			pstm.setString(15, kpiValues.getSpm());
			pstm.setString(16, kpiValues.getFinancialSegment());
			pstm.setString(17, kpiValues.getFinancialSegment());
			pstm.setString(18, kpiValues.getImLeader());
			pstm.setString(19, kpiValues.getImLeader());
			pstm.setString(20, kpiValues.getBusiness());
			pstm.setString(21, kpiValues.getBusiness());
			pstm.setString(22, kpiValues.getSegment());
			pstm.setString(23, kpiValues.getSegment());
			pstm.setString(24, kpiValues.getRegion());
			pstm.setString(25, kpiValues.getRegion());
			pstm.setString(26, kpiValues.getImLeader());
			pstm.setString(27, kpiValues.getImLeader());
			pstm.setString(28, kpiValues.getProjectId());
			pstm.setString(29, kpiValues.getProjectId());
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				project = rs.getString(1);
				if (project != null && !project.equalsIgnoreCase("") && !projectIds.equalsIgnoreCase("")) {
					projectIds = projectIds + ";" + project;
				} else {
					projectIds = project;
				}
			}
		} catch (SQLException e) {
			LOGGER.error("Exception while getting cash collection install project list :: " + e.getMessage());
		}
		return projectIds;
	}

	public List<InvoiceDetailsDTO> getInstallOpenInvoiceDetails(String projectId) {
		List<InvoiceDetailsDTO> list = new ArrayList<InvoiceDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CashCollectionDashboardConstants.GET_INSTALL_OPEN_INVOICE_DETAILS);) {
			pstm.setString(1, projectId);
			pstm.setString(2, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				InvoiceDetailsDTO dto = new InvoiceDetailsDTO();
				dto.setInvoiceNo(null != rs.getString("client_billing_document_number")
						? rs.getString("client_billing_document_number")
						: "");
				dto.setOutstandingAmount(null != rs.getString("ar") ? rs.getString("ar") : "");
				dto.setArStatus(null != rs.getString("types") ? rs.getString("types") : "");
				dto.setRisk(null != rs.getString("risk") ? rs.getString("risk") : "");
				dto.setComments(null != rs.getString("comments") ? rs.getString("comments") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			LOGGER.error(
					"Exception while getting cash collection install open invoice details popup :: " + e.getMessage());
		}
		return list;
	}
}