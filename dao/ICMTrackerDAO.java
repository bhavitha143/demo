package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.CMAnalysisCommentsDTO;
import com.bh.realtrack.dto.CMAnalysisContingencyDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisCostDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisEngDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisLogisticsDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisOtherDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSaveCommentsDTO;
import com.bh.realtrack.dto.CMAnalysisSummaryDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSupplyChainDetailsDTO;
import com.bh.realtrack.dto.CMTrendDetailsDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.SourceUpdatesDTO;
import com.bh.realtrack.dto.YAxisCMDTO;

/**
 * @author shwsawan
 *
 */

public interface ICMTrackerDAO {

	CMAnalysisSummaryDetailsDTO getCMAnalysisSummary(String projectId, String viewContingencyFlag,
			String viewUnAssignedBudgetFlag, String fxImpactOnCostFlag);

	String getCMAnalysisUpdatedOn(String projectId);

	CMAnalysisSaveCommentsDTO getCMAnalysisLatestComments(CMAnalysisSaveCommentsDTO commentsDTO, String projectId,
			String category);

	List<CMAnalysisCommentsDTO> getCMAnalysisComments(String projectId, String category);

	boolean saveCMAnalysisEACComments(CMAnalysisSaveCommentsDTO commentsDTO, String sso);

	boolean saveCMAnalysisBCEComments(CMAnalysisSaveCommentsDTO commentsDTO, String sso);

	List<DropDownDTO> getCMAnalysisSubProjectFilter(String projectId);

	// Modified by Tushar Chavda
	// Dt : 2022-04-28
	Map<String, Object> getCmAnalysisExpCatChart(String projectId, String subProject, String categoryList,
			String expenditureCategoryFlag, String fxImpactOnCostFlag);

	Map<String, Object> getCmAnalysisAreaChart(String projectId, String subProject, String categoryList,
			String expenditureCategoryFlag, String fxImpactOnCostFlag);

	String getCmAnalysisExpCatUpdatedOn(String projectId);

	Map<String, Object> getCMAnalysisTrendChart(String projectId, String cmFlag);

	List<CMTrendDetailsDTO> getCMAnalysisTrendExcelDetails(String projectId);

	String getCMAnalysisTrendChartUpdatedOn(String projectId);

	List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainProjectDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainJobDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category, String expenditureCategory);

	List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainTaskDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category, String expenditureCategory, String job);

	List<CMAnalysisSupplyChainDetailsDTO> getCmAnalysisSupplyChainItemDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category, String expenditureCategory, String job, String task);

	List<CMAnalysisEngDetailsDTO> getCmAnalysisEngDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	List<CMAnalysisCostDetailsDTO> getCmAnalysisCostDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	List<CMAnalysisLogisticsDetailsDTO> getCmAnalysisLogisticsDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	List<CMAnalysisSupplyChainDetailsDTO> exportCmAnalysisSupplyChainDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	List<CMAnalysisOtherDetailsDTO> getCmAnalysisOtherDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	List<SourceUpdatesDTO> getCMAnalysisSourceUpdatedDates();

	YAxisCMDTO getCMAnalysisTrendChartYaxis(String projectId, String cmFlag);

	List<CMAnalysisSupplyChainDetailsDTO> exportCmAnalysisSupplyChainTaskDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

	// Added by Tushar Chavda
	// Dt : 2022-04-28
	List<DropDownDTO> getAreaCategory(String projectId);

	List<DropDownDTO> getExpenditureCategory(String projectId);

	List<CMAnalysisContingencyDetailsDTO> getCmAnalysisContingencyDetails(String projectId,
			String expenditureCategoryFlag, String category);

	List<CMAnalysisOtherDetailsDTO> getCmAnalysisServicesDetails(String projectId, String subProject,
			String expenditureCategoryFlag, String category);

}
