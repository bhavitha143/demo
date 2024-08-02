package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dto.CashCollectionDashboardDropDownDTO;
import com.bh.realtrack.dto.CashCollectionDashboardFilterDTO;
import com.bh.realtrack.dto.CashCollectionDashboardOverallSummaryDetailDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OverallSummaryDetailDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.dto.UpdateTargetDetailsDTO;

/**
 * @author Thakur Aarthi
 */
public interface CashCollectionDashboardService {

	CashCollectionDashboardDropDownDTO getCashCollectionDashboardDropDownDetail(
			HeaderDashboardDetailsDTO headerDetails);

	Map<String, Object> getCashCollectionDashboardManageProjectDetails(HeaderDashboardDetailsDTO headerDetails);

	Map<String, Object> getCashCollectionDashboardOverallSummaryDetail(CashCollectionDashboardFilterDTO filterValues)
			throws Exception;

	Map<String, Object> getCashCollectionDashboardSegmentSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	Map<String, Object> getCashCollectionDashboardProjectDetail(CashCollectionDashboardFilterDTO filterValues)
			throws Exception;

	Map<String, Object> getBusinessUnitSummary(CashCollectionDashboardFilterDTO filterValues) throws Exception;

	Map<String, Object> getCashCollectionDashboardTPSProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	OverallSummaryDetailDTO getCashCollectionDashboardTPSOverallSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	Map<String, Object> getCashCollectionDashboardCashDetail(CashCollectionDashboardFilterDTO filterValues)
			throws Exception;

	Map<String, Object> getOpenInvoiceDetails(String projectId);

	Map<String, Object> getConfiguratorDetails(String companyId);

	Map<String, Object> getCashCollectionDashboardTPSCashDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	UpdateDetailsDTO getUpdateDetails(String companyId, String tier3);

	UpdateDetailsDTO getTargetUploadDetails(String companyId);

	byte[] getProjectTemplate(String companyId);

	byte[] getInstallProjectTemplate(String companyId);

	byte[] getTargetTemplate(String companyId);

	byte[] downloadCashDetails(CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	Map<String, String> importTargetExcelData(String companyId, MultipartFile excelFile);

	Map<String, String> importProjectExcelData(String companyId, MultipartFile excelFile);

	Map<String, String> importInstallProjectExcelData(String companyId, MultipartFile excelFile);

	List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId);
	
	List<ErrorDetailsDTO> getInstallNotProcessedProjectDetails(String companyId);

	Map<String, Object> getCashCollectionDashboardRegionSummaryDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	Map<String, Object> getUpdatedTargetDetails(String companyId, String year, String category);

	Map<String, Object> getCashCollectionDashboardTPSConsolidatedProjectDetail(
			CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

	byte[] downloadInstallCashDetails(CashCollectionDashboardOverallSummaryDetailDTO kpiValues);
	
	byte[] downloadCashDetailsForOFE(CashCollectionDashboardOverallSummaryDetailDTO kpiValues);

}
