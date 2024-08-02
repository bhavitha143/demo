package com.bh.realtrack.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bh.realtrack.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.OFEDocumentationDAO;
import com.bh.realtrack.util.DocumentationConstants;
import com.bh.realtrack.util.OFETeTDashboardConstants;

@Repository
@SuppressWarnings("deprecation")
public class OFEDocumentationDAOImpl implements OFEDocumentationDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(CommonDAO.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<DropDownDTO> getProjectIds(HeaderDashboardDetailsDTO filterValues) {
		return jdbcTemplate.query(DocumentationConstants.GET_DEMAND_CAPACITY_PROJECT_IDS,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getRoles(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_DEMAND_CAPACITY_ROLE_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getResourseName(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_DEMAND_CAPACITY_RESOURCE_NAME,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getStatus() {
		return jdbcTemplate.query(DocumentationConstants.GET_DEMAND_CAPACITY_STATUS_FILTER,
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getRegion() {
		return jdbcTemplate.query(DocumentationConstants.GET_DEMAND_CAPACITY_REGION,
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getlocations() {
		return jdbcTemplate.query(DocumentationConstants.GET_DEMAND_CAPACITY_LOCATION,
				new ResultSetExtractor<List<DropDownDTO>>() {
					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						List<DropDownDTO> list = new ArrayList<DropDownDTO>();
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<String> getMinExcess() {
		return jdbcTemplate.query(DocumentationConstants.GET_MIN_EXCESS_FILTER, new ResultSetExtractor<List<String>>() {
			public List<String> extractData(ResultSet rs) throws SQLException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {

					list.add(rs.getString(1));
				}
				return list;
			}
		});
	}

	@Override
	public String getOtdPerc(String projectId) {

		return jdbcTemplate.query(DocumentationConstants.GET_OTD_PERCENT, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getReWorkPerc(String projectId) {

		return jdbcTemplate.query(DocumentationConstants.GET_REWORK_PERCENT, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getFirstPassYeildPer(String projectId) {

		return jdbcTemplate.query(DocumentationConstants.GET_FIRST_PASS_YIELD, new Object[] { projectId, projectId },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getCoustmerReviewPerc(String projectId) {

		return jdbcTemplate.query(DocumentationConstants.GET_MEDIAN_CUSTOMER_REVIEW, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getTotalDocDurationPerc(String projectId) {

		return jdbcTemplate.query(DocumentationConstants.GET_MEDIAN_TOTAL_DOCUMENT_DURATION, new Object[] { projectId },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getOtdCount(DocumentationFiltersDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_OTD_COUNT, new Object[] { filterValues.getProjectId(),
				filterValues.getSubProjectId(), filterValues.getSubProjectId(), filterValues.getFunctionalCode(),
				filterValues.getFunctionalCode(), filterValues.getProductLine(), filterValues.getProductLine(),
				filterValues.getOwner(), filterValues.getOwner(), filterValues.getDocType(), filterValues.getDocType(),
				filterValues.getPurposeOfIssue(), filterValues.getPurposeOfIssue(), filterValues.getSeaCritical(),
				filterValues.getSeaCritical(), filterValues.getSupSuppliername(), filterValues.getSupSuppliername()
				,filterValues.getDocGroup(),filterValues.getDocGroup()},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getReWorkCount(DocumentationFiltersDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_REWORK_COUNT, new Object[] { filterValues.getProjectId(),
				filterValues.getSubProjectId(), filterValues.getSubProjectId(), filterValues.getFunctionalCode(),
				filterValues.getFunctionalCode(), filterValues.getProductLine(), filterValues.getProductLine(),
				filterValues.getOwner(), filterValues.getOwner(), filterValues.getDocType(), filterValues.getDocType(),
				filterValues.getPurposeOfIssue(), filterValues.getPurposeOfIssue(), filterValues.getSeaCritical(),
				filterValues.getSeaCritical(), filterValues.getSupSuppliername(), filterValues.getSupSuppliername()
						,filterValues.getDocGroup(),filterValues.getDocGroup()},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getFirstPassYeildCount(DocumentationFiltersDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_FIRST_PASS_COUNT, new Object[] {
				filterValues.getProjectId(),filterValues.getSubProjectId(), filterValues.getSubProjectId(), 
				filterValues.getFunctionalCode(), filterValues.getFunctionalCode(), filterValues.getProductLine(),
				filterValues.getProductLine(), filterValues.getOwner(), filterValues.getOwner(),
				filterValues.getDocType(), filterValues.getDocType(), filterValues.getPurposeOfIssue(),
				filterValues.getPurposeOfIssue(), filterValues.getSeaCritical(), filterValues.getSeaCritical(),
				filterValues.getSupSuppliername(), filterValues.getSupSuppliername(),filterValues.getDocGroup(),filterValues.getDocGroup(),filterValues.getProjectId(),
				filterValues.getSubProjectId(), filterValues.getSubProjectId(), filterValues.getFunctionalCode(),
				filterValues.getFunctionalCode(), filterValues.getProductLine(), filterValues.getProductLine(),
				filterValues.getOwner(), filterValues.getOwner(), filterValues.getDocType(), filterValues.getDocType(),
				filterValues.getPurposeOfIssue(), filterValues.getPurposeOfIssue(), filterValues.getSeaCritical(),
				filterValues.getSeaCritical(), filterValues.getSupSuppliername(), filterValues.getSupSuppliername()
						,filterValues.getDocGroup(),filterValues.getDocGroup()},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getCoustmerReviewCount(DocumentationFiltersDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_MEDIAN_CUSTOMER_COUNT,
				new Object[] { filterValues.getProjectId(), filterValues.getFunctionalCode(),
						filterValues.getFunctionalCode(), filterValues.getProductLine(), filterValues.getProductLine(),
						filterValues.getOwner(), filterValues.getOwner(), filterValues.getDocType(),
						filterValues.getDocType(), filterValues.getPurposeOfIssue(), filterValues.getPurposeOfIssue(),
						filterValues.getSeaCritical(), filterValues.getSeaCritical(), filterValues.getSupSuppliername(),
						filterValues.getSupSuppliername() ,filterValues.getDocGroup(),filterValues.getDocGroup()},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getTotalDocDurationCount(DocumentationFiltersDTO filterValues) {

		return jdbcTemplate.query(DocumentationConstants.GET_MEDIAN_TOTAL_DOCUMENT_DURATION_COUNT, new Object[] {
				 filterValues.getProjectId(),
				filterValues.getFunctionalCode(), filterValues.getFunctionalCode(), filterValues.getProductLine(),
				filterValues.getProductLine(), filterValues.getOwner(), filterValues.getOwner(),
				filterValues.getDocType(), filterValues.getDocType(), filterValues.getPurposeOfIssue(),
				filterValues.getPurposeOfIssue(), filterValues.getSeaCritical(), filterValues.getSeaCritical(),
				filterValues.getSupSuppliername(), filterValues.getSupSuppliername(),filterValues.getDocGroup(),filterValues.getDocGroup() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}
	
	@Override
	public String getroleId(int role) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ROLEID,
				new Object[] {role },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getTotalCount(String projectId,String roleId) {
		String query = "";
		if (roleId == "Internal"){
			query = OFETeTDashboardConstants.GET_TOTAL_COUNT;
		} else {
			query = OFETeTDashboardConstants.GET_TOTAL_COUNT1;
		}
		return jdbcTemplate.query(query,
				new Object[] {projectId },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}
	
	@Override
	public String getFinalizedCount(String projectId,String roleId) {
		String query = "";
		if (roleId == "Internal"){
			query = OFETeTDashboardConstants.GET_FINALIZED_COUNT;
		} else {
			query = OFETeTDashboardConstants.GET_FINALIZED_COUNT1;
		}
		return jdbcTemplate.query(query,
				new Object[] {projectId},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}
	
	@Override
	public List<DocumentationPopupChartDTO> getProjectDeckDocChartpopup(String projectId,String roleId) {
		String query = "";

		if (roleId == "Internal"){
			query = OFETeTDashboardConstants.GET_DOC_CHART_POPUP_LIST;
		} else {
			query = OFETeTDashboardConstants.GET_DOC_CHART_POPUP_LIST1;
		}
		return jdbcTemplate.query(query, new Object[] { projectId },
				new ResultSetExtractor<List<DocumentationPopupChartDTO>>() {
					public List<DocumentationPopupChartDTO> extractData(ResultSet rs) throws SQLException {
						List<DocumentationPopupChartDTO> list = new ArrayList<DocumentationPopupChartDTO>();
						while (rs.next()) {
							DocumentationPopupChartDTO dto = new DocumentationPopupChartDTO();
							dto.setpId(rs.getInt(1));
							dto.setProjectName(rs.getString(2) != null ? rs.getString(2) : "");
							dto.setDocId(rs.getInt(3));
							dto.setDocNumber(rs.getString(4) != null ? rs.getString(4) : "");
							dto.setDocumentId(rs.getString(5) != null ? rs.getString(5) : "");
							dto.setClientDocId(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setSupplierDocId(rs.getString(7) != null ? rs.getString(7) : "");
							dto.setDocTitle(rs.getString(8) != null ? rs.getString(8) : "");	
							dto.setDocStatus(rs.getString(9) != null ? rs.getString(9) : "");
							dto.setDocGroup(rs.getString(10) != null ? rs.getString(10) : "");
							dto.setPurposeOfIssue(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setFuctionalCode(rs.getString(12) != null ? rs.getString(12) : "");
							dto.setProductLine(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setOwner(null != rs.getString(14) ? rs.getString(14) : "");
							dto.setDocType(null != rs.getString(15) ? rs.getString(15) : "");
							dto.setDocProgressCapped(rs.getInt(16));
							dto.setLifeCycle(rs.getString(17) != null ? rs.getString(17) : "");
							dto.setDocNumber(null != rs.getString(18) ? rs.getString(18) : "");
							dto.setNumberOfMilestone(rs.getInt(19));
							dto.setHasAndMilestone(null != rs.getString(20) ? rs.getString(20) : "");
							dto.setFirstMilestoneId(rs.getInt(21));
							dto.setFirstMilestoneOrder(rs.getFloat(22));
							dto.setFirstMilestoneCode(rs.getString(23) != null ? rs.getString(23) : "");
							dto.setFirstMilestoneBlDate(rs.getString(24) != null ? rs.getString(24) : "");
							dto.setFirstMilestoneFcDate(null != rs.getString(25) ? rs.getString(25) : "");
							dto.setFirstMilestoneActualDate(rs.getString(26) != null ? rs.getString(26) : "");
							dto.setFirstMilestoneLastRevId( rs.getInt(27));
							dto.setFirstMilestoneLastOrder(rs.getFloat(28));
							dto.setFirstMilestoneLastRev(null != rs.getString(29) ? rs.getString(29) : "");							
							dto.setLastMilestoneId(rs.getInt(30));
							dto.setLastMilestoneCode(rs.getString(31) != null ? rs.getString(31) : "");
							dto.setLastMilestoneOrder(rs.getFloat(32));
							dto.setLastRevisionId(rs.getInt(33));
							dto.setLastRevision(rs.getString(34) != null ? rs.getString(34) : "");
							dto.setLastRevisionOrder(rs.getFloat(35));
							dto.setLastCustRevisionOrder(rs.getFloat(36));
							dto.setLastRevisionStatus(null != rs.getString(37) ? rs.getString(37) : "");
							dto.setLastRevisionStatusBundle(rs.getString(38) != null ? rs.getString(38) : "");
							dto.setLastCustRevisionissueDate(rs.getString(39) != null ? rs.getString(39) : "");
							dto.setDocFinalitionStatus(null != rs.getString(40) ? rs.getString(40) : "");
							dto.setDocLastupdated(rs.getString(41) != null ? rs.getString(41) : "");
							dto.setLastMileFirstIssueDate(null != rs.getString(42) ? rs.getString(42) : "");
							dto.setSeaCritical(null != rs.getString(43) ? rs.getString(43) : "");
							dto.setSupSupplierName(null != rs.getString(44) ? rs.getString(44) : "");
							
							
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<MyProjectsExcelDTO> downloadMyProjectExcel(String sso, int companyId, int customerId, String warrantyFlag) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_MY_PROJECTS_LIST,
				new Object[]{sso,companyId,customerId,warrantyFlag},
				new ResultSetExtractor<List<MyProjectsExcelDTO>>() {
					@Override
					public List<MyProjectsExcelDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<MyProjectsExcelDTO> list = new ArrayList<MyProjectsExcelDTO>();
						while(rs.next()){
							MyProjectsExcelDTO dto = new MyProjectsExcelDTO();
							dto.setFavorite(rs.getString("favourite") != null ? rs.getString("favourite") : "");
							dto.setProjectId(rs.getString("project_id") != null ? rs.getString("project_id") : "");
							dto.setProjectName(rs.getString("project_name") != null ? rs.getString("project_name") : "");
							dto.setBusiness(rs.getString("business_unit") != null ? rs.getString("business_unit") : "");
							dto.setSegment(rs.getString("segment") != null ? rs.getString("segment") : "");
							dto.setEndUser(rs.getString("customer_name") != null ? rs.getString("customer_name") : "");
							dto.setRegion(rs.getString("region") != null ? rs.getString("region") : "");
							dto.setProjectManager(rs.getString("project_manager") != null ? rs.getString("project_manager") : "");
							dto.setProjectValue(rs.getString("po") != null ? rs.getString("po") : "");
							dto.setHse(rs.getString("hse_color") != null ? rs.getString("hse_color") : "");
							dto.setCustomerHealth(rs.getString("customer_health_color") != null ? rs.getString("customer_health_color") : "");
							dto.setActionsAndEscalations(rs.getString("actions_color") != null ? rs.getString("actions_color") : "");
							dto.setQuality(rs.getString("quality_color") != null ? rs.getString("quality_color") : "");
							dto.setSchedule(rs.getString("schedule") != null ? rs.getString("schedule") : "");
							dto.setContract(rs.getString("contract_color") != null ? rs.getString("contract_color") : "");
							dto.setRisk(rs.getString("risk_color") != null ? rs.getString("risk_color") : "");
							dto.setFinancials(rs.getString("financial_color") != null ? rs.getString("financial_color") : "");
							dto.setDocManagement(rs.getString("document_color") != null ? rs.getString("document_color") : "");
							dto.setCustomerOTDTrend(rs.getString("customer_otd_trend_out") != null ? rs.getString("customer_otd_trend_out") : "");
							dto.setCustomerOTDPercent(rs.getString("customer_otd_out") != null ? rs.getString("customer_otd_out") : "");
							dto.setInternalOTDTrend(rs.getString("internal_otd_trend_out") != null ? rs.getString("internal_otd_trend_out") : "");
							dto.setInternalOTDPercent(rs.getString("internal_otd_out") != null ? rs.getString("internal_otd_out") : "");
							dto.setCmAsPercent(rs.getString("cm_as_sold") != null ? rs.getString("cm_as_sold") : "");
							dto.setCmADPercent(rs.getString("cm_as_actual") != null ? rs.getString("cm_as_actual") : "");
							dto.setDeltaCMTrend(rs.getString("delta_cm_trend_out") != null ? rs.getString("delta_cm_trend_out") : "");
							dto.setDeltaCMPercent(rs.getString("delta_cm_out") != null ? rs.getString("delta_cm_out") : "");
							dto.setBilledPercent(rs.getString("billed_perc_out") != null ? rs.getString("billed_perc_out") : "");
							dto.setPocPercent(rs.getString("overall_actual_progress_out") != null ? rs.getString("overall_actual_progress_out") : "");
							dto.setHighlights(rs.getString("highlights") != null ? rs.getString("highlights") : "");
							dto.setPublishDate(rs.getString("publish_date") != null ? rs.getString("publish_date") : "");
							dto.setPpPercent(rs.getString("overall_planned_progress_out") != null ? rs.getString("overall_planned_progress_out") : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

}
