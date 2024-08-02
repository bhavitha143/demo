package com.bh.realtrack.service;

import java.util.Map;

public interface IOFEProjectConsoleService {

	Map<String, Object> getProjectDetails(String projectId);

	Map<String, Object> getProjectBillingDetails(String projectId);
	
	Map<String, Object> getProjectRiskDetails(String projectId);
	
	Map<String, Object> getProjectDocumentCount(String projectId);
	
	Map<String, Object> getProjectDocumentList(String projectId,String status);
	
	Map<String, Object> getPrjConsoleSCurveDetails(String projectId);

	Map<String, Object> getPrjConsoleFinCashDetails(String projectId);

	Map<String, Object> getPrjConsoleMrpDetails(String projectId);

	Map<String, Object> getPrjConsoleQualityDetails(String projectId);

	Map<String, Object> getPrjConsoleVorDetails(String projectId);
}
