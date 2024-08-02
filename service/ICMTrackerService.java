package com.bh.realtrack.service;

import java.util.Map;

import com.bh.realtrack.dto.CMAnalysisSaveCommentsDTO;

/**
 * @author shwsawan
 *
 */

public interface ICMTrackerService {

	Map<String, Object> getCMAnalysisSummary(String projectId, String viewContingencyFlag,
			String viewUnAssignedBudgetFlag, String fxImpactOnCostFlag);

	Map<String, Object> getCMAnalysisComments(String projectId, String category);

	Map<String, Object> saveCMAnalysisComments(CMAnalysisSaveCommentsDTO commentsDTO);

	Map<String, Object> getCMAnalysisDropdowns(String projectId);

	// Modified by Tushar Chavda
	// Dt : 2022-04-28
	Map<String, Object> getCmAnalysisExpCatChart(String projectId, String subProject, String categoryList,
			String expenditureCategoryFlag, String fxImpactOnCostFlag);

	Map<String, Object> getCmAnalysisExpCatChartPopup(String projectId, String subProject,
			String expenditureCategoryFlag, String category, String level, String expenditureCategory, String job,
			String task);

	Map<String, Object> getCMAnalysisTrendChart(String projectId, String cmFlag);

	byte[] downloadCMAnalysisTrendDetails(String projectId);

	byte[] downloadCMAnalysisExpCatDetails(String projectId, String subProject, String expenditureCategoryFlag);

	Map<String, Object> getCMAnalysisSourceUpdatedDates();

}
