package com.bh.realtrack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.FormParam;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bh.realtrack.dto.CashCollectionDashboardDropDownDTO;
import com.bh.realtrack.dto.CashCollectionDashboardFilterDTO;
import com.bh.realtrack.dto.CashCollectionDashboardOverallSummaryDetailDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OverallSummaryDetailDTO;
import com.bh.realtrack.dto.UpdateDetailsDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.service.CashCollectionDashboardService;
import com.bh.realtrack.util.AssertUtils;

/**
 * @author Thakur Aarthi
 */

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/cashCollectionDashboard")
public class CashCollectionDashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CashCollectionDashboardController.class);

	@Autowired
	CashCollectionDashboardService cashCollectionDashboardService;

	@RequestMapping(value = "/getConfiguratorDetails", method = RequestMethod.GET)
	public Map<String, Object> getConfiguratorDetails(@RequestParam String companyId) {
		return cashCollectionDashboardService.getConfiguratorDetails(companyId);
	}

	@RequestMapping(value = "/getOpenInvoiceDetails", method = RequestMethod.GET)
	public Map<String, Object> getOpenInvoiceDetails(@RequestParam String projectId) {
		return cashCollectionDashboardService.getOpenInvoiceDetails(projectId);
	}

	@RequestMapping(value = "/getCashCollectionDashboardDropDownDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public CashCollectionDashboardDropDownDTO getCashCollectionDashboardDropDownDetail(
			@RequestBody HeaderDashboardDetailsDTO headerDetails) {
		return cashCollectionDashboardService.getCashCollectionDashboardDropDownDetail(headerDetails);
	}

	@RequestMapping(value = "/getCashCollectionDashboardManageProjectDetails", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardManageProjectDetails(
			@RequestBody HeaderDashboardDetailsDTO headerDetails) {
		return cashCollectionDashboardService.getCashCollectionDashboardManageProjectDetails(headerDetails);
	}

	@RequestMapping(value = "/getCashCollectionDashboardOverallSummaryDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardOverallSummaryDetail(
			@RequestBody CashCollectionDashboardFilterDTO filterValues) throws Exception {
		return cashCollectionDashboardService.getCashCollectionDashboardOverallSummaryDetail(filterValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardSegmentSummaryDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardSegmentSummaryDetail(
			@RequestBody CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		return cashCollectionDashboardService.getCashCollectionDashboardSegmentSummaryDetail(kpiValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardRegionSummaryDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardRegionSummaryDetail(
			@RequestBody CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		return cashCollectionDashboardService.getCashCollectionDashboardRegionSummaryDetail(kpiValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardProjectDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardProjectDetail(
			@RequestBody CashCollectionDashboardFilterDTO filterValues) throws Exception {
		return cashCollectionDashboardService.getCashCollectionDashboardProjectDetail(filterValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardCashDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardCashDetail(
			@RequestBody CashCollectionDashboardFilterDTO filterValues) throws Exception {
		return cashCollectionDashboardService.getCashCollectionDashboardCashDetail(filterValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardBusinessUnitSummary", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getBusinessUnitSummary(@RequestBody CashCollectionDashboardFilterDTO filterValues)
			throws Exception {
		return cashCollectionDashboardService.getBusinessUnitSummary(filterValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardTPSProjectDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardTPSProjectDetail(
			@RequestBody CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		return cashCollectionDashboardService.getCashCollectionDashboardTPSProjectDetail(kpiValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardTPSConsolidatedProjectDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardTPSConsolidatedProjectDetail(
			@RequestBody CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		return cashCollectionDashboardService.getCashCollectionDashboardTPSConsolidatedProjectDetail(kpiValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardTPSOverallSummaryDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public OverallSummaryDetailDTO getCashCollectionDashboardTPSOverallSummaryDetail(
			@RequestBody CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		return cashCollectionDashboardService.getCashCollectionDashboardTPSOverallSummaryDetail(kpiValues);
	}

	@RequestMapping(value = "/getCashCollectionDashboardTPSCashDetail", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getCashCollectionDashboardTPSCashDetail(
			@RequestBody CashCollectionDashboardOverallSummaryDetailDTO kpiValues) {
		return cashCollectionDashboardService.getCashCollectionDashboardTPSCashDetail(kpiValues);
	}

	@RequestMapping(value = "/getProjectOutOfRTUploadStatus", method = RequestMethod.GET)
	public UpdateDetailsDTO updateDetials(@RequestParam final String companyId, @RequestParam String tier3,
			@RequestHeader final HttpHeaders headers) {
		return cashCollectionDashboardService.getUpdateDetails(companyId, tier3);
	}

	@RequestMapping(value = "/getNotProcessedProjectDetails", method = RequestMethod.GET)
	public Map<String, List<ErrorDetailsDTO>> getNotProcessedProjectDetails(@RequestParam final String companyId,
			@RequestParam String tier3, @RequestHeader final HttpHeaders headers) {
		Map<String, List<ErrorDetailsDTO>> responseMap = new HashMap<String, List<ErrorDetailsDTO>>();
		List<ErrorDetailsDTO> errorDTOList = new ArrayList<ErrorDetailsDTO>();
		if (null != tier3 && !tier3.isEmpty() && tier3.equalsIgnoreCase("Installation")) {
			errorDTOList = cashCollectionDashboardService.getInstallNotProcessedProjectDetails(companyId);
		} else {
			errorDTOList = cashCollectionDashboardService.getNotProcessedProjectDetails(companyId);
		}
		responseMap.put("errorDetails", errorDTOList);
		return responseMap;
	}

	@RequestMapping(value = "/getTargetUploadStatus", method = RequestMethod.GET)
	public UpdateDetailsDTO updateDetails(@RequestParam final String companyId,
			@RequestHeader final HttpHeaders headers) {
		return cashCollectionDashboardService.getTargetUploadDetails(companyId);
	}

	@RequestMapping(value = "/getUpdatedTargetDetails", method = RequestMethod.GET)
	public Map<String, Object> getUpdatedTargetDetails(@RequestParam String companyId, @RequestParam String year,
			@RequestParam String category) {
		return cashCollectionDashboardService.getUpdatedTargetDetails(companyId, year, category);
	}

	@RequestMapping(value = "/uploadProjectOutOfRT", method = RequestMethod.POST)
	public Map<String, String> uploadProjectOutOfRT(@RequestParam final String companyId, @RequestParam String tier3,
			@FormParam("excelFile") final MultipartFile excelFile, @RequestHeader final HttpHeaders headers) {
		try {
			if (null != tier3 && !tier3.isEmpty() && tier3.equalsIgnoreCase("Installation")) {
				return cashCollectionDashboardService.importInstallProjectExcelData(companyId, excelFile);
			} else {
				return cashCollectionDashboardService.importProjectExcelData(companyId, excelFile);
			}
		} catch (Exception e) {
			LOGGER.error("Error while uploading Projects Excel file :: {}" , e.getMessage());
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
	}

	@RequestMapping(value = "/uploadTargetData", method = RequestMethod.POST)
	public Map<String, String> uploadTargetData(@RequestParam final String companyId,
			@FormParam("excelFile") final MultipartFile excelFile, @RequestHeader final HttpHeaders headers) {
		try {
			return cashCollectionDashboardService.importTargetExcelData(companyId, excelFile);
		} catch (Exception e) {
			LOGGER.error("Error while uploading Target Excel file :: {}" , e.getMessage());
			throw new ClientErrorException(ErrorCode.BAD_REQUEST.getResponseStatus());
		}
	}

	@GetMapping("/downloadOutOfProjectTemplate")
	public void downloadOutOfProjectTemplate(@RequestParam final String companyId, @RequestParam String tier3,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "Project_Details_" + AssertUtils.sanitizeString(companyId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = null;
			if (null != tier3 && !tier3.isEmpty() && tier3.equalsIgnoreCase("Installation")) {
				plData = cashCollectionDashboardService.getInstallProjectTemplate(companyId);
			} else {
				plData = cashCollectionDashboardService.getProjectTemplate(companyId);
			}
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Project details{}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Project details excel file {}" , e.getMessage());
			}
		}
	}

	@GetMapping("/downloadTargetTemplate")
	public void downloadTargetTemplate(@RequestParam final String companyId, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "Target_Details_" + AssertUtils.sanitizeString(companyId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = cashCollectionDashboardService.getTargetTemplate(companyId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Target details{}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Target details excel file{}" , e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/downloadCashCollectionDetails", method = RequestMethod.GET)
	public void downloadBillingDetails(@RequestParam int companyId, @RequestParam String business,
			@RequestParam String segment, @RequestParam String pmLeader, @RequestParam String spm,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String currentYearFlag,
			@RequestParam int customerId, @RequestParam String region, @RequestParam String selectedQuarter,
			@RequestParam String financialSegment, @RequestParam String imLeader, @RequestParam String tier3,@RequestParam String projectList,
			HttpServletResponse response) throws Exception {
		String fileName = "Cash_Collection_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			CashCollectionDashboardOverallSummaryDetailDTO filterDTO = new CashCollectionDashboardOverallSummaryDetailDTO();
			filterDTO.setCompanyId(companyId);
			filterDTO.setBusiness(business);
			filterDTO.setSegment(segment);
			filterDTO.setPmLeader(pmLeader);
			filterDTO.setSpm(spm);
			filterDTO.setStartDate(startDate);
			filterDTO.setEndDate(endDate);
			filterDTO.setCurrentYearFlag(currentYearFlag);
			filterDTO.setCustomerId(customerId);
			filterDTO.setRegion(region);
			filterDTO.setSelectedQuater(selectedQuarter);
			filterDTO.setFinancialSegment(financialSegment);
			filterDTO.setTier3(tier3);
			filterDTO.setImLeader(imLeader);
			filterDTO.setProjectList(projectList);
			byte[] plData = null;
			if (null != filterDTO.getTier3() && !filterDTO.getTier3().isEmpty()
					&& filterDTO.getTier3().equalsIgnoreCase("Installation")) {
				plData = cashCollectionDashboardService.downloadInstallCashDetails(filterDTO);
			} else {
				if(filterDTO.getCompanyId()==4) {
					plData = cashCollectionDashboardService.downloadCashDetailsForOFE(filterDTO);
				}else {
				  plData = cashCollectionDashboardService.downloadCashDetails(filterDTO);
				}
			}
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading cash collection details{}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading cash collection details{}" , e.getStackTrace());
			}
		}
	}

}
