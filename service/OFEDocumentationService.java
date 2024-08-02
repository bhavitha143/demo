package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.DocumentationFiltersDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OFEProjectDTO;

public interface OFEDocumentationService {
	
	public Map<String, Object> getDemandVsCapacityFilters(HeaderDashboardDetailsDTO filterValues);

	Map<String, Object> getOpDocumentationKpis(DocumentationFiltersDTO filterValues);

	Map<String, Object> getProjectDeckDocumentationKpis(String projectId);

	Map<String, Object> getProjectDeckDocumentationStatus(String projectId, int role);

	byte[] downloadOfeMyProjectsExcel(OFEProjectDTO projectList);

}
