package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.CashCollectionDashboardDropDownDTO;
import com.bh.realtrack.dto.CashDashboardManageProjectResponseDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceDetailsDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.UpdateTargetDetailsDTO;

public interface CashCollectionDashboardDAO {

	CashCollectionDashboardDropDownDTO getCashCollectionDashboardDropDown(HeaderDashboardDetailsDTO headerDetails);

	List<CashDashboardManageProjectResponseDTO> getmanageProjectList(HeaderDashboardDetailsDTO headerDetails, String projectId);

	List<LastSuccessfulUpdateDetailsDTO> getTargetLastSuccessfulUpdateData(String companyId);

	List<LastUpdateDetailsDTO> getTargetLastUpdatedDate(String companyId);

	List<ErrorDetailsDTO> getTargetErrorDetailsData(String companyId);
	
	List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String companyId);

	List<LastUpdateDetailsDTO> getLastUpdatedDate(String companyId);

	List<ErrorDetailsDTO> getErrorDetailsData(String companyId);

	List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId);

	
}