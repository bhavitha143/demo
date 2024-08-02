package com.bh.realtrack.dao.impl;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.ICMTrackerDAO;
import com.bh.realtrack.dto.CMAnalysisCommentsDTO;
import com.bh.realtrack.dto.CMAnalysisContingencyDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisCostDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisEngDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisExpCategoryChartDTO;
import com.bh.realtrack.dto.CMAnalysisLogisticsDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisOtherDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSaveCommentsDTO;
import com.bh.realtrack.dto.CMAnalysisSummaryDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSupplyChainDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisTrendChartDTO;
import com.bh.realtrack.dto.CMTrendDetailsDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.SourceUpdatesDTO;
import com.bh.realtrack.dto.YAxisCMDTO;
import com.bh.realtrack.util.CMTrackerConstants;

/**
 * @author shwsawan
 *
 */

@Repository
public class CMTrackerDAOImpl implements ICMTrackerDAO {

	private static Logger log = LoggerFactory.getLogger(CMTrackerDAOImpl.class.getName());

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public CMAnalysisSummaryDetailsDTO getCMAnalysisSummary(String projectId, String viewContingencyFlag,
			String viewUnAssignedBudgetFlag, String fxImpactOnCostFlag) {
		CMAnalysisSummaryDetailsDTO summaryDTO = new CMAnalysisSummaryDetailsDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUMMARY);) {
			String fxImpactOnCostVal = null != fxImpactOnCostFlag && !fxImpactOnCostFlag.isEmpty()
					&& fxImpactOnCostFlag.equalsIgnoreCase("Y") ? "FX" : "N";
			pstm.setString(1, projectId);
			pstm.setString(2, "9_1_" + viewContingencyFlag);
			pstm.setString(3, "9_2_" + viewUnAssignedBudgetFlag);
			pstm.setString(4, fxImpactOnCostVal);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				summaryDTO.setSalesAsSold(null != rs.getString("sales_as_out") ? rs.getString("sales_as_out") : "");
				summaryDTO.setBudgetCostAsSold(
						null != rs.getString("bdj_cost_as_out") ? rs.getString("bdj_cost_as_out") : "");
				summaryDTO.setCmAsSold(null != rs.getString("cm_as_out") ? rs.getString("cm_as_out") : "");
				summaryDTO.setCmPerAsSold(null != rs.getString("cm_per_as_out") ? rs.getString("cm_per_as_out") : "");
				summaryDTO.setFxCost(
						null != rs.getString("fx_impact_on_cost_out") ? rs.getString("fx_impact_on_cost_out") : "");
				summaryDTO.setSalesBCE(null != rs.getString("sales_bce_out") ? rs.getString("sales_bce_out") : "");
				summaryDTO.setBudgetCostBCE(
						null != rs.getString("bdj_cost_bce_cal_out") ? rs.getString("bdj_cost_bce_cal_out") : "");
				summaryDTO.setCmBCE(null != rs.getString("cm_bce_out") ? rs.getString("cm_bce_out") : "");
				summaryDTO.setCmPerBCE(null != rs.getString("cm_per_bce_out") ? rs.getString("cm_per_bce_out") : "");
				summaryDTO.setLastBCEDate(null != rs.getString("bce_date_out") ? rs.getString("bce_date_out") : "");
				summaryDTO.setSalesEAC(null != rs.getString("sales_eac_out") ? rs.getString("sales_eac_out") : "");
				summaryDTO.setEac(null != rs.getString("eac_out") ? rs.getString("eac_out") : "");
				summaryDTO.setCmEAC(null != rs.getString("cm_eac_out") ? rs.getString("cm_eac_out") : "");
				summaryDTO.setCmPerEAC(null != rs.getString("cm_per_eac_out") ? rs.getString("cm_per_eac_out") : "");
				summaryDTO.setContingency91(
						null != rs.getString("contingency_9_1_out") ? rs.getString("contingency_9_1_out") : "");
				summaryDTO.setContingency92(
						null != rs.getString("contingency_9_2_out") ? rs.getString("contingency_9_2_out") : "");

				// Modified by Tushar Chavda
				// Dt : 2022-05-02
				summaryDTO.setContingencyValueBCE_9_1(null != rs.getString("contingency_value_bce_9_1_out")
						? rs.getString("contingency_value_bce_9_1_out")
						: "");
				summaryDTO.setContingencyValueBCE_9_2(null != rs.getString("contingency_value_bce_9_2_out")
						? rs.getString("contingency_value_bce_9_2_out")
						: "");

				// Modified by Tushar Chavda
				// Dt : 2022-06-01
				summaryDTO.setUpdatedDate(
						null != rs.getString("as_sold_date_out") ? rs.getString("as_sold_date_out") : "");
				summaryDTO.setFxCostBce(
						null != rs.getString("fx_impact_on_cost_bce_out") ? rs.getString("fx_impact_on_cost_bce_out")
								: "");

				// Modified by Tushar Chavda
				// Dt : 2022-07-01
				summaryDTO.setContingency95(
						null != rs.getString("contingency_9_5_out") ? rs.getString("contingency_9_5_out") : "");
				summaryDTO.setWarningFlag(
						null != rs.getString("warning_flag_out") ? rs.getString("warning_flag_out") : "");
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis summary details :: {}" , e.getMessage());
		}
		return summaryDTO;
	}

	@Override
	public String getCMAnalysisUpdatedOn(String projectId) {
		String updatedOn = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUMMARY_DATE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				updatedOn = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis summary updated on date :: {}" , e.getMessage());
		}
		return updatedOn;
	}

	@Override
	public CMAnalysisSaveCommentsDTO getCMAnalysisLatestComments(CMAnalysisSaveCommentsDTO commentsDTO,
			String projectId, String category) {
		String query = "";
		if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")) {
			if (category.equalsIgnoreCase("BCE")) {
				query = CMTrackerConstants.GET_CM_TRACKER_SUMMARY_BCE_COMMENTS;
			} else if (category.equalsIgnoreCase("EAC")) {
				query = CMTrackerConstants.GET_CM_TRACKER_SUMMARY_EAC_COMMENTS;
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")) {
					if (category.equalsIgnoreCase("BCE")) {
						commentsDTO.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
						commentsDTO.setUpdatedOn(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
						commentsDTO.setBceComments(
								null != rs.getString("bce_comments") ? rs.getString("bce_comments") : "");
					} else if (category.equalsIgnoreCase("EAC")) {
						commentsDTO.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
						commentsDTO.setUpdatedOn(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
						commentsDTO.setEacComments(
								null != rs.getString("eac_comments") ? rs.getString("eac_comments") : "");
					}
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis summary comments :: {}" , e.getMessage());
		}
		return commentsDTO;
	}

	@Override
	public List<CMAnalysisCommentsDTO> getCMAnalysisComments(String projectId, String category) {
		List<CMAnalysisCommentsDTO> list = new ArrayList<CMAnalysisCommentsDTO>();
		String query = "";
		if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")) {
			if (category.equalsIgnoreCase("BCE")) {
				query = CMTrackerConstants.GET_CM_TRACKER_BCE_COMMENTS;
			} else if (category.equalsIgnoreCase("EAC")) {
				query = CMTrackerConstants.GET_CM_TRACKER_EAC_COMMENTS;
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisCommentsDTO commentsDTO = new CMAnalysisCommentsDTO();
				if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")
						&& category.equalsIgnoreCase("BCE")) {
					commentsDTO.setUpdatedBy(null != rs.getString("update_by") ? rs.getString("update_by") : "");
					commentsDTO.setUpdatedOn(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
					commentsDTO.setComments(null != rs.getString("bce_comments") ? rs.getString("bce_comments") : "");
				} else if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")
						&& category.equalsIgnoreCase("EAC")) {
					commentsDTO.setUpdatedBy(null != rs.getString("update_by") ? rs.getString("update_by") : "");
					commentsDTO.setUpdatedOn(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
					commentsDTO.setComments(null != rs.getString("eac_comments") ? rs.getString("eac_comments") : "");
				}
				list.add(commentsDTO);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis comments :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public boolean saveCMAnalysisEACComments(CMAnalysisSaveCommentsDTO commentsDTO, String sso) {
		boolean resultFlag = false;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.SAVE_CM_TRACKER_EAC_COMMENTS);) {
			pstm.setString(1, commentsDTO.getProjectId());
			pstm.setString(2, commentsDTO.getEacComments());
			pstm.setString(3, sso);
			if (pstm.executeUpdate() > 0) {
				resultFlag = true;
			}
		} catch (Exception e) {
			log.error("something went wrong while saving cm analysis eac comments :: {}" , e.getMessage());
		}
		return resultFlag;
	}

	@Override
	public boolean saveCMAnalysisBCEComments(CMAnalysisSaveCommentsDTO commentsDTO, String sso) {
		boolean resultFlag = false;
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.SAVE_CM_TRACKER_BCE_COMMENTS);) {
			pstm.setString(1, commentsDTO.getProjectId());
			pstm.setString(2, commentsDTO.getBceComments());
			pstm.setString(3, sso);
			if (pstm.executeUpdate() > 0) {
				resultFlag = true;
			}
		} catch (Exception e) {
			log.error("something went wrong while saving cm analysis bce comments :: {}" , e.getMessage());
		}
		return resultFlag;
	}

	@Override
	public List<DropDownDTO> getCMAnalysisSubProjectFilter(String projectId) {
		List<DropDownDTO> list = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUB_PROJECT_FILTER);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DropDownDTO dto = new DropDownDTO();
				String subProject = rs.getString(1);
				if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
					dto.setKey(subProject);
					dto.setVal(subProject);
					list.add(dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis subProject filter :: {}" , e.getMessage());
		}
		return list;
	}

	// Modified by Tushar Chavda
	// Dt : 2022-04-28
	@Override
	public Map<String, Object> getCmAnalysisExpCatChart(String projectId, String subProject, String categoryList,
			String expenditureCategoryFlag, String fxImpactOnCostFlag) {
		Map<String, Object> chartData = new LinkedHashMap<String, Object>();
		CMAnalysisExpCategoryChartDTO dto = new CMAnalysisExpCategoryChartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_EXP_CATEGORY_CHART);) {
			List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
			List<DropDownDTO> expenditureCategoryList = new ArrayList<DropDownDTO>();

			String selectedMode = "";
			subProjectFilterList = getCMAnalysisSubProjectFilter(projectId);
			expenditureCategoryList = getExpenditureCategory(projectId);
			String[] subProjectStr = subProject.split(";");
			String[] categoryStr = categoryList.split(";");

			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL") || subProjectFilterList.size() == subProjectStr.length) {
					selectedMode = "expcat_overall";
					subProjectStr = "OVERALL".split(";");
				} else {
					selectedMode = "expcat_job";
				}
			}

			if (null != categoryList && !categoryList.isEmpty() && !categoryList.equalsIgnoreCase("")) {
				if (categoryList.equalsIgnoreCase("OVERALL") || expenditureCategoryList.size() == categoryStr.length) {
					// selectedMode = "expcat_overall";
					categoryStr = "OVERALL".split(";");
				} else {
					// selectedMode = "expcat_job";
				}
			}

			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array categoryStrArr = con.createArrayOf("varchar", categoryStr);

			log.info(selectedMode + " :::: " + subProjectStrArr);
			log.info("Categories selected  :::: {}" , categoryStrArr);

			pstm.setString(1, fxImpactOnCostFlag);
			pstm.setString(2, fxImpactOnCostFlag);
			pstm.setString(3, fxImpactOnCostFlag);
			pstm.setString(4, fxImpactOnCostFlag);
			pstm.setString(5, fxImpactOnCostFlag);
			pstm.setString(6, fxImpactOnCostFlag);
			pstm.setString(7, projectId);
			pstm.setString(8, selectedMode);
			pstm.setArray(9, subProjectStrArr);
			pstm.setArray(10, subProjectStrArr);
			pstm.setArray(11, categoryStrArr);
			pstm.setArray(12, categoryStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String expCat = "";
				dto = new CMAnalysisExpCategoryChartDTO();
				expCat = rs.getString("expenditure_category");
				dto.setCategory(null != rs.getString("area1") ? rs.getString("area1") : "");
				dto.setAsSold(null != rs.getString("bdj_cost_as") ? rs.getString("bdj_cost_as") : "");
				dto.setLastBCE(null != rs.getString("bdj_cost_bce") ? rs.getString("bdj_cost_bce") : "");
				dto.setToGO(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setTotalEac(null != rs.getString("total_eac") ? rs.getString("total_eac") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setEacVsAsSold(null != rs.getString("eac_vs_as_sold") ? rs.getString("eac_vs_as_sold") : "");

				// Modified by Tushar Chavda
				// Dt : 2022-06-01
				// dto.setActualCost(null != rs.getString("total_cost") ?
				// rs.getString("total_cost") : "");
				dto.setTotalCost(null != rs.getString("total_cost") ? rs.getString("total_cost") : "");
				dto.setActualCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
				dto.setUpdatedDate(null != rs.getString("updated_date") ? rs.getString("updated_date") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");

				if (null != expCat && !expCat.isEmpty() && !expCat.equalsIgnoreCase("")) {
					chartData.put(expCat, dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis exp cat chart :: {}" , e.getMessage());
		}
		return chartData;
	}

	// Added by Tushar Chavda
	// Dt : 2022-04-28
	public List<DropDownDTO> getAreaCategory(String projectId) {

		List<DropDownDTO> areaCategoryList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_AREA_CATEGORY_LIST);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DropDownDTO dropDownDTO = new DropDownDTO();
				String areaCategory = rs.getString(1);
				if (null != areaCategory && !areaCategory.isEmpty() && !areaCategory.equalsIgnoreCase("")) {
					dropDownDTO.setKey(areaCategory);
					dropDownDTO.setVal(areaCategory);
					areaCategoryList.add(dropDownDTO);
				}
			}
		} catch (SQLException e) {
			log.error("Exception occured while trying to get Area Category list {}" , e.getMessage());
		}
		return areaCategoryList;
	}

	// Added by Tushar Chavda
	// Dt : 2022-04-28
	public List<DropDownDTO> getExpenditureCategory(String projectId) {

		List<DropDownDTO> areaCategoryList = new ArrayList<DropDownDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_EXPENDITURE_CATEGORY_LIST);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				DropDownDTO dropDownDTO = new DropDownDTO();
				String expenditureCategory = rs.getString(1);
				if (null != expenditureCategory && !expenditureCategory.isEmpty()
						&& !expenditureCategory.equalsIgnoreCase("")) {
					dropDownDTO.setKey(expenditureCategory);
					dropDownDTO.setVal(expenditureCategory);
					areaCategoryList.add(dropDownDTO);
				}
			}
		} catch (SQLException e) {
			log.error("Exception occured while trying to get Expenditure Category list {}" , e.getMessage());
		}
		return areaCategoryList;
	}

	// Added by Tushar Chavda
	// Dt : 2022-04-28
	@Override
	public Map<String, Object> getCmAnalysisAreaChart(String projectId, String subProject, String categoryList,
			String expenditureCategoryFlag, String fxImpactOnCostFlag) {
		Map<String, Object> chartData = new LinkedHashMap<String, Object>();
		CMAnalysisExpCategoryChartDTO dto = new CMAnalysisExpCategoryChartDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_AREA_CHART);) {
			List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
			List<DropDownDTO> areaCategoryList = new ArrayList<DropDownDTO>();

			String selectedMode = "";

			subProjectFilterList = getCMAnalysisSubProjectFilter(projectId);
			areaCategoryList = getAreaCategory(projectId);

			String[] subProjectStr = subProject.split(";");
			String[] categoryStr = categoryList.split(";");

			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL") || subProjectFilterList.size() == subProjectStr.length) {
					selectedMode = "area_overall";
					subProjectStr = "OVERALL".split(";");
				} else {
					selectedMode = "area_job";
				}
			}

			if (null != categoryList && !categoryList.isEmpty() && !categoryList.equalsIgnoreCase("")) {
				if (categoryList.equalsIgnoreCase("OVERALL") || areaCategoryList.size() == categoryStr.length) {
					// selectedMode = "area_overall";
					categoryStr = "OVERALL".split(";");
				} else {
					// selectedMode = "area_job";
				}
			}

			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			Array categoryStrArr = con.createArrayOf("varchar", categoryStr);

			log.info(selectedMode + " :::: " + subProjectStrArr);
			log.info("Categories selected " + " :::: " + categoryStrArr);

			pstm.setString(1, fxImpactOnCostFlag);
			pstm.setString(2, fxImpactOnCostFlag);
			pstm.setString(3, fxImpactOnCostFlag);
			pstm.setString(4, fxImpactOnCostFlag);
			pstm.setString(5, fxImpactOnCostFlag);
			pstm.setString(6, fxImpactOnCostFlag);
			pstm.setString(7, projectId);
			pstm.setString(8, selectedMode);
			pstm.setArray(9, subProjectStrArr);
			pstm.setArray(10, subProjectStrArr);
			pstm.setArray(11, categoryStrArr);
			pstm.setArray(12, categoryStrArr);

			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				String area = "";
				dto = new CMAnalysisExpCategoryChartDTO();
				area = rs.getString("area1");
				dto.setCategory(null != rs.getString("area1") ? rs.getString("area1") : "");
				dto.setAsSold(null != rs.getString("bdj_cost_as") ? rs.getString("bdj_cost_as") : "");
				dto.setLastBCE(null != rs.getString("bdj_cost_bce") ? rs.getString("bdj_cost_bce") : "");
				dto.setToGO(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setTotalEac(null != rs.getString("total_eac") ? rs.getString("total_eac") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setEacVsAsSold(null != rs.getString("eac_vs_as_sold") ? rs.getString("eac_vs_as_sold") : "");

				// Modified by Tushar Chavda
				// Dt : 2022-06-03
				// dto.setActualCost(null != rs.getString("total_cost") ?
				// rs.getString("total_cost") : "");
				dto.setTotalCost(null != rs.getString("total_cost") ? rs.getString("total_cost") : "");
				dto.setActualCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
				dto.setUpdatedDate(null != rs.getString("updated_date") ? rs.getString("updated_date") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				if (null != area && !area.isEmpty() && !area.equalsIgnoreCase("")) {
					chartData.put(area, dto);
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis area chart :: {}" , e.getMessage());
		}
		return chartData;
	}

	@Override
	public String getCmAnalysisExpCatUpdatedOn(String projectId) {
		String updatedOn = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_EXP_CATEGORY_CHART_DATE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				updatedOn = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis trend chart updated on date :: {}" , e.getMessage());
		}
		return updatedOn;
	}

	@Override
	public Map<String, Object> getCMAnalysisTrendChart(String projectId, String cmFlag) {
		Map<String, Object> chartData = new LinkedHashMap<String, Object>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_TREND_CHART);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisTrendChartDTO dto = new CMAnalysisTrendChartDTO();
				String yearQuarter = "";
				yearQuarter = rs.getString("year_quarter");
				if (null != cmFlag && !cmFlag.isEmpty() && !cmFlag.equalsIgnoreCase("")
						&& cmFlag.equalsIgnoreCase("Y")) {
					dto.setCmAsSold(null != rs.getString("cm_per_as") ? rs.getString("cm_per_as") : "");
					dto.setCmBCE(null != rs.getString("cm_per_bce") ? rs.getString("cm_per_bce") : "");
					dto.setEac(null != rs.getString("cm_per_eac") ? rs.getString("cm_per_eac") : "");
				} else if (null != cmFlag && !cmFlag.isEmpty() && !cmFlag.equalsIgnoreCase("")
						&& cmFlag.equalsIgnoreCase("N")) {
					dto.setCmAsSold(null != rs.getString("cm_as") ? rs.getString("cm_as") : "");
					dto.setCmBCE(null != rs.getString("cm_bce") ? rs.getString("cm_bce") : "");
					dto.setEac(null != rs.getString("cm_eac") ? rs.getString("cm_eac") : "");
				}
				chartData.put(yearQuarter, dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis trend chart :: {}" , e.getMessage());
		}
		return chartData;
	}

	@Override
	public List<CMTrendDetailsDTO> getCMAnalysisTrendExcelDetails(String projectId) {
		List<CMTrendDetailsDTO> list = new ArrayList<CMTrendDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_TREND_CHART_EXCEL_DOWNLOAD);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMTrendDetailsDTO dto = new CMTrendDetailsDTO();
				dto.setYearQuarter(null != rs.getString("year_quarter") ? rs.getString("year_quarter") : "");
				dto.setJob(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setBdjCostAs(null != rs.getString("bdj_cost_as") ? rs.getString("bdj_cost_as") : "");
				dto.setBdjCostBce(null != rs.getString("bdj_cost_bce") ? rs.getString("bdj_cost_bce") : "");
				dto.setTotalCost(null != rs.getString("total_cost") ? rs.getString("total_cost") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis trend excel details :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public String getCMAnalysisTrendChartUpdatedOn(String projectId) {
		String updatedOn = "";
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_TREND_CHART_DATE);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				updatedOn = null != rs.getString("last_updated_dt") ? rs.getString("last_updated_dt") : "";
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis trend chart updated on date :: {}" , e.getMessage());
		}
		return updatedOn;
	}

	@Override
	public List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainProjectDetails(String projectId,
			String subProject, String expenditureCategoryFlag, String category) {
		List<CMAnalysisSupplyChainDetailsDTO> list = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUPPLY_CHAIN_PROJECT_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisSupplyChainDetailsDTO dto = new CMAnalysisSupplyChainDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setCttBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBceWOFx(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				dto.setAccumulatedCost(
						null != rs.getString("accumulated_cost") ? rs.getString("accumulated_cost") : "");
				dto.setCybTotal(null != rs.getString("cyb_total") ? rs.getString("cyb_total") : "");
				dto.setLmCredit(null != rs.getString("lm_credit") ? rs.getString("lm_credit") : "");
				dto.setFxImpactOnCost(
						null != rs.getString("fx_impact_on_cost") ? rs.getString("fx_impact_on_cost") : "");
				dto.setEacCost(null != rs.getString("eac_cost") ? rs.getString("eac_cost") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setCttLastBce(null != rs.getString("cttLastBce") ? rs.getString("cttLastBce") : "");
				dto.setFxCostBCE(
						null != rs.getString("fx_impact_on_cost_bce") ? rs.getString("fx_impact_on_cost_bce") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis supply chain project details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainJobDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category, String expenditureCategory) {
		List<CMAnalysisSupplyChainDetailsDTO> list = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUPPLY_CHAIN_EXPCAT_JOB_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, expenditureCategory);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisSupplyChainDetailsDTO dto = new CMAnalysisSupplyChainDetailsDTO();
				dto.setJobNumber(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setCttBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBceWOFx(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setAccumulatedCost(
						null != rs.getString("accumulated_cost") ? rs.getString("accumulated_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				dto.setCybTotal(null != rs.getString("cyb_total") ? rs.getString("cyb_total") : "");
				dto.setLmCredit(null != rs.getString("lm_credit") ? rs.getString("lm_credit") : "");
				dto.setFxImpactOnCost(
						null != rs.getString("fx_impact_on_cost") ? rs.getString("fx_impact_on_cost") : "");
				dto.setEacCost(null != rs.getString("eac_cost") ? rs.getString("eac_cost") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setCttLastBce(null != rs.getString("cttLastBce") ? rs.getString("cttLastBce") : "");
				dto.setFxCostBCE(
						null != rs.getString("fx_impact_on_cost_bce") ? rs.getString("fx_impact_on_cost_bce") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis supply chain job details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainTaskDetails(String projectId,
			String subProject, String expenditureCategoryFlag, String category, String expenditureCategory,
			String job) {
		List<CMAnalysisSupplyChainDetailsDTO> list = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUPPLY_CHAIN_EXPCAT_TASK_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, job);
			pstm.setString(5, expenditureCategory);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisSupplyChainDetailsDTO dto = new CMAnalysisSupplyChainDetailsDTO();
				dto.setTask(null != rs.getString("task") ? rs.getString("task") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setCttBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBceWOFx(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setAccumulatedCost(
						null != rs.getString("accumulated_cost") ? rs.getString("accumulated_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				dto.setCybTotal(null != rs.getString("cyb_total") ? rs.getString("cyb_total") : "");
				dto.setLmCredit(null != rs.getString("lm_credit") ? rs.getString("lm_credit") : "");
				dto.setFxImpactOnCost(
						null != rs.getString("fx_impact_on_cost") ? rs.getString("fx_impact_on_cost") : "");
				dto.setEacCost(null != rs.getString("eac_cost") ? rs.getString("eac_cost") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setCttLastBce(null != rs.getString("cttLastBce") ? rs.getString("cttLastBce") : "");
				dto.setFxCostBCE(
						null != rs.getString("fx_impact_on_cost_bce") ? rs.getString("fx_impact_on_cost_bce") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis supply chain task details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainItemDetails(String projectId,
			String subProject, String expenditureCategoryFlag, String category, String expenditureCategory, String job,
			String task) {
		List<CMAnalysisSupplyChainDetailsDTO> list = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUPPLY_CHAIN_EXPCAT_ITEM_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, job);
			pstm.setString(5, task);
			pstm.setString(6, expenditureCategory);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisSupplyChainDetailsDTO dto = new CMAnalysisSupplyChainDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setJobNumber(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				dto.setTask(null != rs.getString("task") ? rs.getString("task") : "");
				dto.setItemCode(null != rs.getString("item_code") ? rs.getString("item_code") : "");
				dto.setItemDescription(
						null != rs.getString("item_description") ? rs.getString("item_description") : "");
				dto.setInvOrgCode(null != rs.getString("inv_org_code") ? rs.getString("inv_org_code") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setCttBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBceWOFx(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setAccumulatedCost(
						null != rs.getString("accumulated_cost") ? rs.getString("accumulated_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				dto.setCybTotal(null != rs.getString("cyb_total") ? rs.getString("cyb_total") : "");
				dto.setLmCredit(null != rs.getString("lm_credit") ? rs.getString("lm_credit") : "");
				dto.setFxImpactOnCost(
						null != rs.getString("fx_impact_on_cost") ? rs.getString("fx_impact_on_cost") : "");
				dto.setEacCost(null != rs.getString("eac_cost") ? rs.getString("eac_cost") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setDummyItemCode(null != rs.getString("dummy_item_code") ? rs.getString("dummy_item_code") : "");
				dto.setItemType(null != rs.getString("item_type") ? rs.getString("item_type") : "");
				dto.setMakeBuy(null != rs.getString("make_buy") ? rs.getString("make_buy") : "");
				dto.setPeggedItem(null != rs.getString("pegged_item") ? rs.getString("pegged_item") : "");
				dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
				dto.setSpotExchangeRate(
						null != rs.getString("spot_exchange_rate") ? rs.getString("spot_exchange_rate") : "");
				dto.setSwapExchangeRate(
						null != rs.getString("swap_exchange_rate") ? rs.getString("swap_exchange_rate") : "");
				dto.setCybQuantity(null != rs.getString("cyb_quantity") ? rs.getString("cyb_quantity") : "");
				dto.setCybSource(null != rs.getString("cyb_source") ? rs.getString("cyb_source") : "");
				dto.setCybSupplyType(null != rs.getString("cyb_supply_type") ? rs.getString("cyb_supply_type") : "");
				dto.setCybOrderNumber(null != rs.getString("cyb_order_number") ? rs.getString("cyb_order_number") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setCttLastBce(null != rs.getString("cttLastBce") ? rs.getString("cttLastBce") : "");
				dto.setFxCostBCE(
						null != rs.getString("fx_impact_on_cost_bce") ? rs.getString("fx_impact_on_cost_bce") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis supply chain item details  popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisEngDetailsDTO> getCmAnalysisEngDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisEngDetailsDTO> list = new ArrayList<CMAnalysisEngDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_ENG_DETAILS_LIST);) {
			List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
			String selectedMode = "";
			subProjectFilterList = getCMAnalysisSubProjectFilter(projectId);
			String[] subProjectStr = subProject.split(";");
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (null != expenditureCategoryFlag && !expenditureCategoryFlag.isEmpty()
						&& !expenditureCategoryFlag.equalsIgnoreCase("")) {
					if (expenditureCategoryFlag.equalsIgnoreCase("Y")) {
						if (subProject.equalsIgnoreCase("OVERALL")
								|| subProjectFilterList.size() == subProjectStr.length) {
							selectedMode = "expcat_overall";
							subProjectStr = "OVERALL".split(";");
						} else {
							selectedMode = "expcat_job";
						}
					} else if (expenditureCategoryFlag.equalsIgnoreCase("N")) {
						if (subProject.equalsIgnoreCase("OVERALL")
								|| subProjectFilterList.size() == subProjectStr.length) {
							// selectedMode = "area_overall";
							selectedMode = "expcat_overall";
							subProjectStr = "OVERALL".split(";");
						} else {
							// selectedMode = "area_job";
							selectedMode = "expcat_job";
						}
					}
				}
			}
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			log.info(selectedMode + " :::: " + subProjectStrArr);
			pstm.setString(1, projectId);
			pstm.setString(2, selectedMode);
			pstm.setArray(3, subProjectStrArr);
			pstm.setArray(4, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisEngDetailsDTO dto = new CMAnalysisEngDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");

				dto.setBdjCostAs(null != rs.getString("bdj_cost_as") ? rs.getString("bdj_cost_as") : "");
				dto.setBdjCostBce(null != rs.getString("bdj_cost_bce") ? rs.getString("bdj_cost_bce") : "");
				dto.setTotalCost(null != rs.getString("total_cost") ? rs.getString("total_cost") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setEac(null != rs.getString("eac") ? rs.getString("eac") : "");
				dto.setSelectedmode(null != rs.getString("selected_mode") ? rs.getString("selected_mode") : "");

				// Modified by Tushar Chavda
				// Dt : 2022-06-03
				dto.setActualCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis eng details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisCostDetailsDTO> getCmAnalysisCostDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisCostDetailsDTO> list = new ArrayList<CMAnalysisCostDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_COST_DETAILS_LIST);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisCostDetailsDTO dto = new CMAnalysisCostDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setRole1(null != rs.getString("role1") ? rs.getString("role1") : "");
				dto.setLatestEstimateInt(
						null != rs.getString("latest_estimate_int") ? rs.getString("latest_estimate_int") : "");
				dto.setLatestEstimateExt(
						null != rs.getString("latest_estimate_ext") ? rs.getString("latest_estimate_ext") : "");
				dto.setActualSpent(null != rs.getString("actual_spent") ? rs.getString("actual_spent") : "");
				dto.setJotTot(null != rs.getString("jot_tot") ? rs.getString("jot_tot") : "");
				dto.setToGoInt(null != rs.getString("to_go_int") ? rs.getString("to_go_int") : "");
				dto.setToGoExt(null != rs.getString("to_go_ext") ? rs.getString("to_go_ext") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis cost details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisLogisticsDetailsDTO> getCmAnalysisLogisticsDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisLogisticsDetailsDTO> list = new ArrayList<CMAnalysisLogisticsDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_LOGISTICS_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisLogisticsDetailsDTO dto = new CMAnalysisLogisticsDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setJobNumber(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setTotalCost(null != rs.getString("total_cost") ? rs.getString("total_cost") : "");
				dto.setReferenceVolume(
						null != rs.getString("reference_volume") ? rs.getString("reference_volume") : "");
				dto.setActualVolume(null != rs.getString("actual_volume") ? rs.getString("actual_volume") : "");
				dto.setProgress(null != rs.getString("progress") ? rs.getString("progress") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setEac(null != rs.getString("eac") ? rs.getString("eac") : "");
				dto.setDeltaEacVsAs(null != rs.getString("delta_eac_vs_as") ? rs.getString("delta_eac_vs_as") : "");
				dto.setDeltaEacVsBce(null != rs.getString("delta_eac_vs_bce") ? rs.getString("delta_eac_vs_bce") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis logistics details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisSupplyChainDetailsDTO> exportCmAnalysisSupplyChainDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisSupplyChainDetailsDTO> list = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUPPLY_CHAIN_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisSupplyChainDetailsDTO dto = new CMAnalysisSupplyChainDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setJobNumber(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				dto.setTask(null != rs.getString("task") ? rs.getString("task") : "");
				dto.setItemCode(null != rs.getString("item_code") ? rs.getString("item_code") : "");
				dto.setItemDescription(
						null != rs.getString("item_description") ? rs.getString("item_description") : "");
				dto.setInvOrgCode(null != rs.getString("inv_org_code") ? rs.getString("inv_org_code") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setCttBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBceWOFx(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setAccumulatedCost(
						null != rs.getString("accumulated_cost") ? rs.getString("accumulated_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				dto.setCybTotal(null != rs.getString("cyb_total") ? rs.getString("cyb_total") : "");
				dto.setLmCredit(null != rs.getString("lm_credit") ? rs.getString("lm_credit") : "");
				dto.setFxImpactOnCost(
						null != rs.getString("fx_impact_on_cost") ? rs.getString("fx_impact_on_cost") : "");
				dto.setEacCost(null != rs.getString("eac_cost") ? rs.getString("eac_cost") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setDummyItemCode(null != rs.getString("dummy_item_code") ? rs.getString("dummy_item_code") : "");
				dto.setItemType(null != rs.getString("item_type") ? rs.getString("item_type") : "");
				dto.setMakeBuy(null != rs.getString("make_buy") ? rs.getString("make_buy") : "");
				dto.setPeggedItem(null != rs.getString("pegged_item") ? rs.getString("pegged_item") : "");
				dto.setCurrency(null != rs.getString("currency") ? rs.getString("currency") : "");
				dto.setSpotExchangeRate(
						null != rs.getString("spot_exchange_rate") ? rs.getString("spot_exchange_rate") : "");
				dto.setSwapExchangeRate(
						null != rs.getString("swap_exchange_rate") ? rs.getString("swap_exchange_rate") : "");
				dto.setCybQuantity(null != rs.getString("cyb_quantity") ? rs.getString("cyb_quantity") : "");
				dto.setCybSource(null != rs.getString("cyb_source") ? rs.getString("cyb_source") : "");
				dto.setCybSupplyType(null != rs.getString("cyb_supply_type") ? rs.getString("cyb_supply_type") : "");
				dto.setCybOrderNumber(null != rs.getString("cyb_order_number") ? rs.getString("cyb_order_number") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setCttLastBce(null != rs.getString("cttLastBce") ? rs.getString("cttLastBce") : "");
				dto.setFxCostBCE(
						null != rs.getString("fx_impact_on_cost_bce") ? rs.getString("fx_impact_on_cost_bce") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis supply chain details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisSupplyChainDetailsDTO> exportCmAnalysisSupplyChainTaskDetails(String projectId,
			String subProject, String expenditureCategoryFlag, String category) {
		List<CMAnalysisSupplyChainDetailsDTO> list = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_SUPPLY_CHAIN_TASK_DETAILS_LIST);) {
			String[] subProjectStr = subProject.split(";");
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisSupplyChainDetailsDTO dto = new CMAnalysisSupplyChainDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setJobNumber(null != rs.getString("job_number") ? rs.getString("job_number") : "");
				dto.setTask(null != rs.getString("task") ? rs.getString("task") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category") ? rs.getString("expenditure_category") : "");
				dto.setCttBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBceWOFx(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setBudgetAs(null != rs.getString("budget_as") ? rs.getString("budget_as") : "");
				dto.setLastBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setAccumulatedCost(
						null != rs.getString("accumulated_cost") ? rs.getString("accumulated_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				dto.setCybTotal(null != rs.getString("cyb_total") ? rs.getString("cyb_total") : "");
				dto.setLmCredit(null != rs.getString("lm_credit") ? rs.getString("lm_credit") : "");
				dto.setFxImpactOnCost(
						null != rs.getString("fx_impact_on_cost") ? rs.getString("fx_impact_on_cost") : "");
				dto.setEacCost(null != rs.getString("eac_cost") ? rs.getString("eac_cost") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setCttLastBce(null != rs.getString("cttLastBce") ? rs.getString("cttLastBce") : "");
				dto.setFxCostBCE(
						null != rs.getString("fx_impact_on_cost_bce") ? rs.getString("fx_impact_on_cost_bce") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis supply chain details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisOtherDetailsDTO> getCmAnalysisOtherDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisOtherDetailsDTO> list = new ArrayList<CMAnalysisOtherDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_OTHER_DETAILS_LIST);) {
			List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
			String selectedMode = "";
			subProjectFilterList = getCMAnalysisSubProjectFilter(projectId);
			String[] subProjectStr = subProject.split(";");
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL") || subProjectFilterList.size() == subProjectStr.length) {
					selectedMode = "expcat_overall";
					subProjectStr = "OVERALL".split(";");
				} else {
					selectedMode = "expcat_job";
				}
			}
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			log.info(selectedMode + " :::: " + subProjectStrArr);
			pstm.setString(1, projectId);
			pstm.setArray(2, subProjectStrArr);
			pstm.setArray(3, subProjectStrArr);
			pstm.setString(4, category);
			pstm.setString(5, selectedMode);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisOtherDetailsDTO dto = new CMAnalysisOtherDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				dto.setJobNumber(null != rs.getString("job") ? rs.getString("job") : "");
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category_org") ? rs.getString("expenditure_category_org")
								: "");
				dto.setBdjCostAs(null != rs.getString("bdj_cost_as") ? rs.getString("bdj_cost_as") : "");
				dto.setBdjCostBce(null != rs.getString("bdj_cost_bce") ? rs.getString("bdj_cost_bce") : "");
				dto.setTotalCost(null != rs.getString("total_cost") ? rs.getString("total_cost") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setEac(null != rs.getString("eac1") ? rs.getString("eac1") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_budget") ? rs.getString("eac_vs_as_budget") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setActualCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis others details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<CMAnalysisOtherDetailsDTO> getCmAnalysisServicesDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisOtherDetailsDTO> list = new ArrayList<CMAnalysisOtherDetailsDTO>();
		List<DropDownDTO> subProjectFilterList = new ArrayList<DropDownDTO>();
		String selectedMode = "", query = "";
		subProjectFilterList = getCMAnalysisSubProjectFilter(projectId);
		String[] subProjectStr = subProject.split(";");
		if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
			if (subProject.equalsIgnoreCase("OVERALL") || subProjectFilterList.size() == subProjectStr.length) {
				query = CMTrackerConstants.GET_CM_TRACKER_SERVICES_OVERALL_DETAILS_LIST;
			} else {
				query = CMTrackerConstants.GET_CM_TRACKER_SERVICES_JOB_LEVEL_DETAILS_LIST;
			}
		}
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(query);) {
			if (null != subProject && !subProject.isEmpty() && !subProject.equalsIgnoreCase("")) {
				if (subProject.equalsIgnoreCase("OVERALL") || subProjectFilterList.size() == subProjectStr.length) {
					selectedMode = "expcat_overall";
					subProjectStr = "OVERALL".split(";");
				} else {
					selectedMode = "expcat_job";
				}
			}
			Array subProjectStrArr = con.createArrayOf("varchar", subProjectStr);
			log.info(selectedMode + " :::: " + subProjectStrArr);
			if (subProject.equalsIgnoreCase("OVERALL") || selectedMode.equalsIgnoreCase("expcat_overall")) {
				pstm.setString(1, projectId);
				pstm.setString(2, category);
			} else {
				pstm.setString(1, projectId);
				pstm.setArray(2, subProjectStrArr);
				pstm.setArray(3, subProjectStrArr);
			}
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisOtherDetailsDTO dto = new CMAnalysisOtherDetailsDTO();
				dto.setProjectId(null != rs.getString("project_id") ? rs.getString("project_id") : "");
				if (subProject.equalsIgnoreCase("OVERALL") || selectedMode.equalsIgnoreCase("expcat_overall")) {
					dto.setTask(null != rs.getString("task") ? rs.getString("task") : "");
				} else {
					dto.setTask(null != rs.getString("task") ? rs.getString("task") : "");
					dto.setJobNumber(null != rs.getString("job") ? rs.getString("job") : "");
				}
				dto.setExpenditureCategory(
						null != rs.getString("expenditure_category_org") ? rs.getString("expenditure_category_org")
								: "");
				dto.setBdjCostAs(null != rs.getString("budget_as_sold") ? rs.getString("budget_as_sold") : "");
				dto.setBdjCostBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setTotalCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
				dto.setToGo(null != rs.getString("to_go") ? rs.getString("to_go") : "");
				dto.setEac(null != rs.getString("eac") ? rs.getString("eac") : "");
				dto.setEacVsAsBudget(null != rs.getString("eac_vs_as_sold") ? rs.getString("eac_vs_as_sold") : "");
				dto.setEacVsLastBce(null != rs.getString("eac_vs_last_bce") ? rs.getString("eac_vs_last_bce") : "");
				dto.setActualCost(null != rs.getString("actual_cost") ? rs.getString("actual_cost") : "");
				dto.setCommitments(null != rs.getString("commitments") ? rs.getString("commitments") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis services details popup :: {}" , e.getMessage());
		}
		return list;
	}

	@Override
	public List<SourceUpdatesDTO> getCMAnalysisSourceUpdatedDates() {
		List<SourceUpdatesDTO> list = new ArrayList<SourceUpdatesDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_ANALYSIS_SOURCE_UPDATED_DATES);) {
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				SourceUpdatesDTO dto = new SourceUpdatesDTO();
				dto.setSource(null != rs.getString("process_description") ? rs.getString("process_description") : "");
				dto.setSourceUpdatedDate(null != rs.getString("updated_on") ? rs.getString("updated_on") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis cost details popup :: " + e.getMessage());
		}
		return list;
	}

	@Override
	public YAxisCMDTO getCMAnalysisTrendChartYaxis(String projectId, String cmFlag) {
		YAxisCMDTO dto = new YAxisCMDTO();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con.prepareStatement(CMTrackerConstants.GET_CM_ANALYSIS_TREND_CHART_YAXIS);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				if (null != cmFlag && !cmFlag.isEmpty() && cmFlag.equalsIgnoreCase("Y")) {
					dto.setyAxisMinCM(null != rs.getString("min_per_cm") ? rs.getString("min_per_cm") : "");
					dto.setyAxisMaxCM(null != rs.getString("max_per_cm") ? rs.getString("max_per_cm") : "");
				} else {
					dto.setyAxisMinCM(null != rs.getString("min_cm") ? rs.getString("min_cm") : "");
					dto.setyAxisMaxCM(null != rs.getString("max_cm") ? rs.getString("max_cm") : "");
				}
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis trend chart Y-axis details :: {}" , e.getMessage());
		}
		return dto;
	}

	@Override
	public List<CMAnalysisContingencyDetailsDTO> getCmAnalysisContingencyDetails(String projectId,
			String expenditureCategoryFlag, String category) {
		List<CMAnalysisContingencyDetailsDTO> list = new ArrayList<CMAnalysisContingencyDetailsDTO>();
		try (Connection con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstm = con
						.prepareStatement(CMTrackerConstants.GET_CM_TRACKER_CONTINGENCY_DETAILS_LIST);) {
			pstm.setString(1, projectId);
			ResultSet rs = pstm.executeQuery();
			while (rs.next()) {
				CMAnalysisContingencyDetailsDTO dto = new CMAnalysisContingencyDetailsDTO();
				dto.setContingencyTask(
						null != rs.getString("contingency_task") ? rs.getString("contingency_task") : "");
				dto.setAsSold(null != rs.getString("as_sold") ? rs.getString("as_sold") : "");
				dto.setLastBce(null != rs.getString("last_bce") ? rs.getString("last_bce") : "");
				dto.setTotalReleased(
						null != rs.getString("total_rel_aft_last_bce") ? rs.getString("total_rel_aft_last_bce") : "");
				dto.setEac(null != rs.getString("eac") ? rs.getString("eac") : "");
				list.add(dto);
			}
		} catch (SQLException e) {
			log.error("Exception while getting cm analysis contingency details popup :: {}" , e.getMessage());
		}
		return list;
	}

}