package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.CashCollectionDashboardDAO;
import com.bh.realtrack.dto.CashCollectionDashboardDropDownDTO;
import com.bh.realtrack.dto.CashCollectionDashboardFilterDTO;
import com.bh.realtrack.dto.CashCollectionDashboardOverallSummaryDetailDTO;
import com.bh.realtrack.dto.CashDashboardBusinessUnitSummaryDTO;
import com.bh.realtrack.dto.CashDashboardManageProjectResponseDTO;
import com.bh.realtrack.dto.CashDetailDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.OFEBusinessSummaryDetailsDTO;
import com.bh.realtrack.dto.OFEProjectDetailsDTO;
import com.bh.realtrack.dto.ProjectDetailDTO;
import com.bh.realtrack.dto.UpdateTargetDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.CashCollectionDashboardConstants;

/**
 * @author Thakur Aarthi
 */
@Repository(value = "ofeCashCollectionDashboardDAOImpl")
public class OfeCashCollectionDashboardDAOImpl implements CashCollectionDashboardDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(OfeCashCollectionDashboardDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	
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

	@Override
	public CashCollectionDashboardDropDownDTO getCashCollectionDashboardDropDown(
			HeaderDashboardDetailsDTO headerDetails) {

		List<String> quater = getQuarterList();

		CashCollectionDashboardDropDownDTO dto = new CashCollectionDashboardDropDownDTO();
		dto.setQuater(quater);
		
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

	public List<CashDashboardBusinessUnitSummaryDTO> getBusinessUnitSummary(
			CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
				List<CashDashboardBusinessUnitSummaryDTO> list = new ArrayList<CashDashboardBusinessUnitSummaryDTO>();
				try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(CashCollectionDashboardConstants.GET_CASH_COLLECTION_BUSINESS_UNIT_SUMMARY)) {
			ps.setString(1, filterValues.getProjectList());
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CashDashboardBusinessUnitSummaryDTO dto = new CashDashboardBusinessUnitSummaryDTO();

							dto.setBusinessUnit(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setPqCollection(rs.getFloat(2));
							dto.setBillAndCash(rs.getFloat(3));
							dto.setCashLE(rs.getFloat(4));
							dto.setTotalCollectionTarget(rs.getFloat(5));
							dto.setCollectedCashVal(rs.getFloat(6));
							dto.setCollectedToGoLE(rs.getFloat(7));
							dto.setCollectedVpw(rs.getFloat(8));
							dto.setPastDueLE(rs.getFloat(9));
							dto.setPdTarget(rs.getFloat(10));
							dto.setPastDue(rs.getFloat(11));
							dto.setOpp(rs.getFloat(12));
							dto.setMediumHighRisk(rs.getFloat(13));
							dto.setDisputed(rs.getFloat(14));
							dto.setEscalated(rs.getFloat(15));
							dto.setLastEstiPerToctw(rs.getFloat(16));
							dto.setCollectedVsPtcw(null != rs.getString(17) ? rs.getString(17) : "");
							dto.setCollectionVTGT("123");
							dto.setPdVTGT("456");
							dto.setCollectionTarget(rs.getFloat(18));
							dto.setLeVsTgt(rs.getFloat(19));
							list.add(dto);
	}

		} catch (Exception exception) {
			LOGGER.error("exception occurred======================={}" , exception.getMessage());
		}

		return list;
	}


	public List<ProjectDetailDTO> getCashCollectionDashboardProjectDetail(CashCollectionDashboardFilterDTO filterValues,
			String startDate, String endDate) {
		List<ProjectDetailDTO> list = new ArrayList<ProjectDetailDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(CashCollectionDashboardConstants.GET_CASH_COLLECTION_PROJECT_TAB)) {
			ps.setString(1, filterValues.getProjectList());
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				ProjectDetailDTO dto = new ProjectDetailDTO();

				dto.setPrjId(null != rs.getString(1) ? rs.getString(1) : "");
				dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
				dto.setCustomerName(null != rs.getString(3) ? rs.getString(3) : "");
				dto.setPmName(null != rs.getString(4) ? rs.getString(4) : "");
				dto.setBusinessUnit(null != rs.getString(5) ? rs.getString(5) : "");
				dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
				dto.setPqCollectionVal(String.valueOf(rs.getDouble(7)) != null ? rs.getDouble(7) : 0.0);
				dto.setBillAndCash(String.valueOf(rs.getDouble(8)) != null ? rs.getDouble(8) : 0.0);
				dto.setCashLE(String.valueOf(rs.getDouble(9)) != null ? rs.getDouble(9) : 0.0);
				dto.setTotalCollectionTarget(String.valueOf(rs.getDouble(10)) != null ? rs.getDouble(10) : 0.0);
				dto.setCollectedCashVal(String.valueOf(rs.getDouble(11)) != null ? rs.getDouble(11) : 0.0);
				dto.setCollectedToGoLE(String.valueOf(rs.getDouble(12)) != null ? rs.getDouble(12) : 0.0);
				dto.setCollectedVPW(String.valueOf(rs.getDouble(13)) != null ? rs.getDouble(13) : 0.0);
				dto.setPastDueLE(String.valueOf(rs.getDouble(14)) != null ? rs.getDouble(14) : 0.0);
				dto.setPastDue(String.valueOf(rs.getDouble(15)) != null ? rs.getDouble(15) : 0.0);
				dto.setPdTarget(String.valueOf(rs.getDouble(16)) != null ? rs.getDouble(16) : 0.0);
				dto.setOpp(String.valueOf(rs.getDouble(17)) != null ? rs.getDouble(17) : 0.0);
				dto.setMediumHighRisk(String.valueOf(rs.getDouble(18)) != null ? rs.getDouble(18) : 0.0);
				dto.setDisputed(String.valueOf(rs.getDouble(19)) != null ? rs.getDouble(19) : 0.0);
				dto.setEscalated(String.valueOf(rs.getDouble(20)) != null ? rs.getDouble(20) : 0.0);
				dto.setLastEstiPerToctw(String.valueOf(rs.getDouble(21)) != null ? rs.getDouble(21) : 0.0);
				dto.setCollectedVsPTCW(null != rs.getString(22) ? rs.getString(22) : "");
				dto.setCollectionTarget(String.valueOf(rs.getDouble(23)) != null ? rs.getDouble(23) : 0.0);
				dto.setLeVsTgt(String.valueOf(rs.getDouble(24)) != null ? rs.getDouble(24) : 0.0);
				list.add(dto);
			}

		} catch (Exception exception) {
			LOGGER.error("exception occurred======================={}" , exception.getMessage());
		}

		return list;
	}
  
	public String getPastDueKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_PAST_DUE_KPI,
				new Object[] {startDate, startDate, startDate, startDate, startDate, endDate, startDate, endDate, 
						startDate, filterValues.getProjectList(), filterValues.getProjectList() },
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

	public String getPastDueLEKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(
				CashCollectionDashboardConstants.GET_PAST_DUE_LE_KPI, new Object[] { startDate, startDate, 
						startDate, startDate,startDate, startDate,
						startDate, startDate, startDate, startDate,
						startDate, startDate, startDate, startDate, startDate,
						  startDate, startDate, startDate, startDate, startDate,
						  filterValues.getProjectList(), filterValues.getProjectList() },
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

	public String getCashLEKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_CASH_LE_KPI,
				new Object[] { startDate, endDate, startDate, endDate, startDate, endDate,
						filterValues.getProjectList(), filterValues.getProjectList() },
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

	public String getCollectedCashKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(
				CashCollectionDashboardConstants.GET_COLLECTED_CASH_KPI, new Object[] { startDate, startDate, startDate,
						startDate, filterValues.getProjectList(), filterValues.getProjectList(), startDate, endDate },
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

	public String getBmLinkageKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_BM_LINKAGE_KPI,
				new Object[] { filterValues.getProjectList(), filterValues.getProjectList(),
						filterValues.getBillingDateFlag(), filterValues.getBillingDateFlag(), startDate,
						filterValues.getBillingDateFlag(), endDate, filterValues.getProjectList(),
						filterValues.getProjectList(), filterValues.getBillingDateFlag(), startDate,
						filterValues.getBillingDateFlag(), endDate },
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

	public String getUnmatchedBLKpi(CashCollectionDashboardFilterDTO filterValues) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_UNMATCHED_BILLING_KPI,
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

	public String getCollectedToGoKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_COLLECTED_TO_GO_KPI,
				new Object[] { startDate, endDate, startDate, endDate, startDate, endDate,
						filterValues.getProjectList(), filterValues.getProjectList(), startDate, startDate, startDate,
						startDate, filterValues.getProjectList(), filterValues.getProjectList(), startDate,
						endDate },
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

	public List<CashDetailDTO> getCashCollectionDashboardCashList(CashCollectionDashboardFilterDTO filterValues,
			String startDate, String endDate) {
		List<CashDetailDTO> list = new ArrayList<CashDetailDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(CashCollectionDashboardConstants.GET_CASH_COLLECTION_CASH_TAB)) {
			ps.setString(1, filterValues.getProjectList());
			ps.setString(2, startDate);
			ps.setString(3, endDate);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CashDetailDTO dto = new CashDetailDTO();

				dto.setPrjId(null != rs.getString(1) ? rs.getString(1) : "");
				dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
				dto.setCustomerName(null != rs.getString(3) ? rs.getString(3) : "");
				dto.setPmName(null != rs.getString(4) ? rs.getString(4) : "");
				dto.setBu(null != rs.getString(5) ? rs.getString(5) : "");
				dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
				dto.setCashInvoiceNumber(null != rs.getString(7) ? rs.getString(7) : "");
				dto.setCashRisk(null != rs.getString(8) ? rs.getString(8) : "");
				dto.setComments(null != rs.getString(9) ? rs.getString(9) : "");
				dto.setCashInvoiceDt(null != rs.getString(10) ? rs.getString(10) : "");
				dto.setCashInvoiceDueDt(null != rs.getString(11) ? rs.getString(11) : "");
				dto.setCashCollectionDate(null != rs.getString(12) ? rs.getString(12) : "");
				dto.setCashCurrency(null != rs.getString(13) ? rs.getString(13) : "");
				dto.setCashInvCurrencyCode(null != rs.getString(14) ? rs.getString(14) : "");
				dto.setCashInvAmountUsd(null != rs.getString(15) ? rs.getString(15) : "");
				dto.setCashFromCurrency(null != rs.getString(16) ? rs.getString(16) : "");
				dto.setCashOpenInvoiceAmount(null != rs.getString(17) ? rs.getString(17) : "");
				dto.setCashOpenAmountDocCurr(null != rs.getString(18) ? rs.getString(18) : "");
				dto.setCashCashCollected(null != rs.getString(19) ? rs.getString(19) : "");
				dto.setCashStatus(null != rs.getString(20) ? rs.getString(20) : "");
				dto.setCashCashLeAmt(null != rs.getString(21) ? rs.getString(21) : "");
				dto.setCashProrataLine(null != rs.getString(22) ? rs.getString(22) : "");
				dto.setCashMilestoneDescription(null != rs.getString(23) ? rs.getString(23) : "");
				dto.setCashP6Milestoneforecast(null != rs.getString(24) ? rs.getString(24) : "");
				dto.setCashEstimCashDate(null != rs.getString(25) ? rs.getString(25) : "");
				dto.setCashMileCurrency(null != rs.getString(26) ? rs.getString(26) : "");
				dto.setCashMilestoneAmountUsd(null != rs.getString(27) ? rs.getString(27) : "");
				dto.setCashInvSource(null != rs.getString(28) ? rs.getString(28) : "");
				list.add(dto);
			}

		} catch (Exception exception) {
			LOGGER.error("exception occurred======================={}",  exception.getMessage());
		}

		return list;
	}

	@Override
	public List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String companyId) {
		List<LastSuccessfulUpdateDetailsDTO> list = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
		LastSuccessfulUpdateDetailsDTO successfulDTO = new LastSuccessfulUpdateDetailsDTO();
		successfulDTO.setSsoId("1234");
		successfulDTO.setStatus("Success");
		successfulDTO.setInsertDate("07-07-2021");
		successfulDTO.setFileName("cash collection");
		list.add(successfulDTO);

		return list;
	}

	@Override
	public List<LastUpdateDetailsDTO> getLastUpdatedDate(String companyId) {
		List<LastUpdateDetailsDTO> list = new ArrayList<LastUpdateDetailsDTO>();

		LastUpdateDetailsDTO successfulDTO = new LastUpdateDetailsDTO();
		successfulDTO.setSsoId("1234");
		successfulDTO.setStatus("In Progress");
		successfulDTO.setInsertDate("07-07-2021");
		successfulDTO.setFileName("cash collection");
		list.add(successfulDTO);

		return list;
	}

	@Override
	public List<ErrorDetailsDTO> getErrorDetailsData(String companyId) {
		List<ErrorDetailsDTO> list = new ArrayList<ErrorDetailsDTO>();

		ErrorDetailsDTO successfulDTO = new ErrorDetailsDTO();
		successfulDTO.setCompanyId("2");
		successfulDTO.setErrorDetails("Error");
		list.add(successfulDTO);

		return list;
	}

	public List<UpdateTargetDetailsDTO> getUpdatedTargetDetails(String companyId, String year) {
		List<UpdateTargetDetailsDTO> detailsList = new ArrayList<UpdateTargetDetailsDTO>();
		UpdateTargetDetailsDTO updateTargetDetailsDTO = new UpdateTargetDetailsDTO();
		updateTargetDetailsDTO.setSegment("123");
		updateTargetDetailsDTO.setQ1CollectionTgt("cash");
		updateTargetDetailsDTO.setQ1PDTgt("collection");
		updateTargetDetailsDTO.setQ2CollectionTgt("project");
		updateTargetDetailsDTO.setQ2PDTgt("123");
		updateTargetDetailsDTO.setQ3CollectionTgt("456");
		updateTargetDetailsDTO.setQ3PDTgt("abc");
		updateTargetDetailsDTO.setQ4CollectionTgt("abcccc");
		updateTargetDetailsDTO.setQ4PDTgt("xyzz");
		detailsList.add(updateTargetDetailsDTO);
		return detailsList;
	}

	@Override
	public List<LastSuccessfulUpdateDetailsDTO> getTargetLastSuccessfulUpdateData(String companyId) {
		List<LastSuccessfulUpdateDetailsDTO> list = new ArrayList<LastSuccessfulUpdateDetailsDTO>();
		LastSuccessfulUpdateDetailsDTO successfulDTO = new LastSuccessfulUpdateDetailsDTO();
		successfulDTO.setSsoId("1234");
		successfulDTO.setStatus("Success");
		successfulDTO.setInsertDate("07-07-2021");
		successfulDTO.setFileName("cash collection");
		list.add(successfulDTO);

		return list;
	}

	@Override
	public List<LastUpdateDetailsDTO> getTargetLastUpdatedDate(String companyId) {
		List<LastUpdateDetailsDTO> list = new ArrayList<LastUpdateDetailsDTO>();

		LastUpdateDetailsDTO successfulDTO = new LastUpdateDetailsDTO();
		successfulDTO.setSsoId("1234");
		successfulDTO.setStatus("In Progress");
		successfulDTO.setInsertDate("07-07-2021");
		successfulDTO.setFileName("cash collection");
		list.add(successfulDTO);

		return list;
	}

	@Override
	public List<ErrorDetailsDTO> getTargetErrorDetailsData(String companyId) {
		List<ErrorDetailsDTO> list = new ArrayList<ErrorDetailsDTO>();

		ErrorDetailsDTO successfulDTO = new ErrorDetailsDTO();
		successfulDTO.setCompanyId("2");
		successfulDTO.setErrorDetails("Error");
		list.add(successfulDTO);

		return list;
	}

	@Override
	public List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<OFEBusinessSummaryDetailsDTO> getBusinessUnitSummaryForOFE
	(CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
				List<OFEBusinessSummaryDetailsDTO> list = new ArrayList<OFEBusinessSummaryDetailsDTO>();
				try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(CashCollectionDashboardConstants.GET_CASH_COLLECTION_BUSINESS_UNIT_SUMMARY)) {
			ps.setString(1, kpiValues.getProjectList());
			ps.setString(2, kpiValues.getStartDate());
			ps.setString(3, kpiValues.getEndDate());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OFEBusinessSummaryDetailsDTO dto = new OFEBusinessSummaryDetailsDTO();

							dto.setBusiness(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setPqCollection(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setBillAndCash(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCashLE(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setTotalCollectionTarget(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setCollectedCashVal(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setCollecttionToGoLE(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setCollectedVPW(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setPastDueLE(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setPdTarget(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setPastDue(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setOpp(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setMediumHighRisk(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setDisputed(null != rs.getString(14) ? rs.getString(14) : "");
							dto.setEscalated(null != rs.getString(15) ? rs.getString(15) : "");
							dto.setLastEstimatePTCW(null != rs.getString(16) ? rs.getString(16) : "");
							dto.setCollectedVsPTCW(null != rs.getString(17) ? rs.getString(17) : "");
							dto.setCollectionTarget(rs.getDouble(18));
							dto.setLeVsTgt(rs.getDouble(19));
							list.add(dto);
	}

		} catch (Exception exception) {
			LOGGER.error("exception occurred======================={}" , exception.getMessage());
		}

		return list;
	}
	
	public List<OFEProjectDetailsDTO> getCashCollectionDashboardProjectDetailForOFE(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<OFEProjectDetailsDTO> list = new ArrayList<OFEProjectDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(CashCollectionDashboardConstants.GET_CASH_COLLECTION_PROJECT_TAB)) {
			ps.setString(1, kpiValues.getProjectList());
			ps.setString(2, kpiValues.getStartDate());
			ps.setString(3, kpiValues.getEndDate());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				OFEProjectDetailsDTO dto = new OFEProjectDetailsDTO();

				dto.setPrjId(null != rs.getString(1) ? rs.getString(1) : "");
				dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
				dto.setCustomerName(null != rs.getString(3) ? rs.getString(3) : "");
				dto.setPmName(null != rs.getString(4) ? rs.getString(4) : "");
				dto.setBusinessUnit(null != rs.getString(5) ? rs.getString(5) : "");
				dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
				dto.setPqCollectionVal(null != rs.getString(7) ? rs.getString(7) : "");
				dto.setBillAndCash(null != rs.getString(8) ? rs.getString(8) : "");
				dto.setCashLE(null != rs.getString(9) ? rs.getString(9) : "");
				dto.setTotalCollectionTarget(null != rs.getString(10) ? rs.getString(10) : "");
				dto.setCollectedCashVal(null != rs.getString(11) ? rs.getString(11) : "");
				dto.setCollectedToGoLE(null != rs.getString(12) ? rs.getString(12) : "");
				dto.setCollectedVPW(null != rs.getString(13) ? rs.getString(13) : "");
				dto.setPastDueLE(null != rs.getString(14) ? rs.getString(14) : "");
				dto.setPastDue(null != rs.getString(15) ? rs.getString(15) : "");
				dto.setPdTarget(null != rs.getString(16) ? rs.getString(16) : "");
				dto.setOpp(null != rs.getString(17) ? rs.getString(17) : "");
				dto.setMediumHighRisk(null != rs.getString(18) ? rs.getString(18) : "");
				dto.setDisputed(null != rs.getString(19) ? rs.getString(19) : "");
				dto.setEscalated(null != rs.getString(20) ? rs.getString(20) : "");
				dto.setLastEstiPerToctw(null != rs.getString(21) ? rs.getString(21) : "");
				dto.setCollectedVsPTCW(null != rs.getString(22) ? rs.getString(22) : "");

				list.add(dto);
			}

		} catch (Exception exception) {
			LOGGER.error("exception occurred======================={}" , exception.getMessage());
		}

		return list;
	}
	
	
	public List<CashDetailDTO> getCashCollectionDashboardCashListForOFE(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		List<CashDetailDTO> list = new ArrayList<CashDetailDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement ps = con
						.prepareStatement(CashCollectionDashboardConstants.GET_CASH_COLLECTION_CASH_TAB)) {
			ps.setString(1, kpiValues.getProjectList());
			ps.setString(2, kpiValues.getStartDate());
			ps.setString(3, kpiValues.getEndDate());
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				CashDetailDTO dto = new CashDetailDTO();

				dto.setPrjId(null != rs.getString(1) ? rs.getString(1) : "");
				dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
				dto.setCustomerName(null != rs.getString(3) ? rs.getString(3) : "");
				dto.setPmName(null != rs.getString(4) ? rs.getString(4) : "");
				dto.setBu(null != rs.getString(5) ? rs.getString(5) : "");
				dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
				dto.setCashInvoiceNumber(null != rs.getString(7) ? rs.getString(7) : "");
				dto.setCashRisk(null != rs.getString(8) ? rs.getString(8) : "");
				dto.setCashInvoiceDt(null != rs.getString(9) ? rs.getString(9) : "");
				dto.setCashInvoiceDueDt(null != rs.getString(10) ? rs.getString(10) : "");
				dto.setCashCollectionDate(null != rs.getString(11) ? rs.getString(11) : "");
				dto.setCashCurrency(null != rs.getString(12) ? rs.getString(12) : "");
				dto.setCashInvCurrencyCode(null != rs.getString(13) ? rs.getString(13) : "");
				dto.setCashInvAmountUsd(null != rs.getString(14) ? rs.getString(14) : "");
				dto.setCashFromCurrency(null != rs.getString(15) ? rs.getString(15) : "");
				dto.setCashOpenInvoiceAmount(null != rs.getString(16) ? rs.getString(16) : "");
				dto.setCashOpenAmountDocCurr(null != rs.getString(17) ? rs.getString(17) : "");
				dto.setCashCashCollected(null != rs.getString(18) ? rs.getString(18) : "");
				dto.setCashStatus(null != rs.getString(19) ? rs.getString(19) : "");
				dto.setCashCashLeAmt(null != rs.getString(20) ? rs.getString(20) : "");
				dto.setCashProrataLine(null != rs.getString(21) ? rs.getString(21) : "");
				dto.setCashMilestoneDescription(null != rs.getString(22) ? rs.getString(22) : "");
				dto.setCashP6Milestoneforecast(null != rs.getString(23) ? rs.getString(23) : "");
				dto.setCashEstimCashDate(null != rs.getString(24) ? rs.getString(24) : "");
				dto.setCashMileCurrency(null != rs.getString(25) ? rs.getString(25) : "");
				dto.setCashMilestoneAmountUsd(null != rs.getString(26) ? rs.getString(26) : "");
				dto.setCashInvSource(null != rs.getString(27) ? rs.getString(27) : "");
				
				
				list.add(dto);
			}

		} catch (Exception exception) {
			LOGGER.error("exception occurred======================={}" , exception.getMessage());
		}

		return list;
	}

	public String getPastDueDateKpi(CashCollectionDashboardFilterDTO filterValues, String startDate, String endDate) {
		return jdbcTemplate.query(CashCollectionDashboardConstants.GET_PAST_DUE_KPI,
				new Object[] {startDate,startDate, startDate, startDate, startDate, endDate, startDate, endDate, 
						startDate, filterValues.getProjectList(), filterValues.getProjectList() },
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(final ResultSet rs) {
						String kpi = new String();
						try {
							while (rs.next()) {
								kpi = rs.getString(2);
							}
						} catch (SQLException e) {

							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return kpi;
					}
				});
	}



}