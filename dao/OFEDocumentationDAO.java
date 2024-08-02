package com.bh.realtrack.dao;

import java.util.List;

import com.bh.realtrack.dto.*;

public interface OFEDocumentationDAO {
	public List<DropDownDTO> getProjectIds(HeaderDashboardDetailsDTO filterValues);
	
	public List<DropDownDTO> getRoles(HeaderDashboardDetailsDTO filterValues);
	
	public List<DropDownDTO> getResourseName(HeaderDashboardDetailsDTO filterValues);
	
	public List<DropDownDTO> getStatus();
	
	public List<DropDownDTO> getRegion();
	
	public List<DropDownDTO> getlocations();
	
	public List<String> getMinExcess();
	
	public String getOtdPerc(String projectId);
	
	public String getReWorkPerc(String projectId);
	
	public String getFirstPassYeildPer(String projectId);
	
	public String getCoustmerReviewPerc(String projectId);
	
	public String getTotalDocDurationPerc(String projectId);
	
	String getOtdCount(DocumentationFiltersDTO filterValues);
	
	String getReWorkCount(DocumentationFiltersDTO filterValues);
	
	String getFirstPassYeildCount(DocumentationFiltersDTO filterValues);
	
	String getCoustmerReviewCount(DocumentationFiltersDTO filterValues);
	
	String getTotalDocDurationCount(DocumentationFiltersDTO filterValues);
	
	List<DocumentationPopupChartDTO> getProjectDeckDocChartpopup(String projectId, String roleId);
	
	String getFinalizedCount(String projectId, String roleId);
	
	String getTotalCount(String projectId, String roleId);

	String getroleId(int role);

	List<MyProjectsExcelDTO> downloadMyProjectExcel(String sso,int companyId, int customerId, String warrantyFlag);
	
}
