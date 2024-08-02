package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;


import com.bh.realtrack.dto.AtCapacityFiltersDTO;
import com.bh.realtrack.dto.AtLocationDTO;
import com.bh.realtrack.dto.DemandVsAvailablilityDTO;
import com.bh.realtrack.dto.EditLocationTableDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OFETeTDashboardDTO;
import com.bh.realtrack.dto.ResourcePnsTable;
import com.bh.realtrack.service.OFETeTDashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @author Thakur Aarthi
 */
@RestController
@CrossOrigin
@RequestMapping(value = "api/v1")
public class OFETeTDashboardController {

	private static final Logger LOGGER = LoggerFactory.getLogger(BillingPortfolioController.class);

	@Autowired
	OFETeTDashboardService iOFETeTDashboardService;

	@RequestMapping(value = "/getTeTDashboardFilters", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardFilters(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardFilters(filterValues);
	}

	@RequestMapping(value = "/getTeTManageProject", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTManageProject(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getTeTManageProject(filterValues);
	}

	@RequestMapping(value = "/getTeTProjectTable", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTProjectTable(@RequestBody OFETeTDashboardDTO filterValues,@RequestParam("isMonthly") boolean flag) {
		return iOFETeTDashboardService.getTeTProjectTable(filterValues,flag);
	}

	@RequestMapping(value = "/getTeTDashboardKpis", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardKpis(@RequestBody OFETeTDashboardDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardKpis(filterValues);
	}

	@RequestMapping(value = "/getLocationTable", method = RequestMethod.GET)
	public Map<String, Object> getLocationTable() {
		return iOFETeTDashboardService.getLocationTable();
	}

	@RequestMapping(value = "/editLocationTable", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> editLocationTable(@RequestBody List<EditLocationTableDTO> editLocationDto) {
		return iOFETeTDashboardService.editLocationTable(editLocationDto);
	}

	@RequestMapping(value = "/getEditAccessTetDashboard", method = RequestMethod.GET)
	public String getEditAccessTetDashboard(@RequestParam String sso) {
		return iOFETeTDashboardService.getEditAccessTetDashboard(sso);
	}

	@RequestMapping(value = "/getTeTDashboardExcelDownload", method = RequestMethod.GET)
	public void getTeTDashboardExcelDownload(@RequestParam final String projectId, final String roleId,
			final String resourceId, final String region, final String location, final String activityStatus,
			final String startDate, final String endDate, final String period, final String demandType,
			final String projectName,final boolean flag,@RequestHeader final HttpHeaders headers, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String fileName = "TeTDashboard.xlsx";

		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iOFETeTDashboardService.exportTeTDashboardExcelDownloadData(projectId, roleId, resourceId,
					region, location, activityStatus, startDate, endDate, period, demandType, projectName,flag);
			IOUtils.write(plData, response.getOutputStream());

		} catch (Exception e) {
			LOGGER.error("Error occured when downloading excel details{}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading excel details{}" , e.getStackTrace());
			}
		}
	}

	@Scheduled(cron = "0 30 9 ? * MON-FRI")
	@RequestMapping(value = "/readAssetTrackerData", method = RequestMethod.GET)
	public Map<String, Object> readAssetTrackerData() {
		LOGGER.info("Scheduler started for readAssetTrackerData");
		return iOFETeTDashboardService.readAssetTrackerData();
	}

	// Capacity Tab
	@RequestMapping(value = "/getTeTDashboardCapacityFilters", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardCapacityFilters(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardCapacityFilters(filterValues);
	}

	@RequestMapping(value = "/getTeTDashboardAtCapacityTable", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardAtCapacityTable(@RequestBody AtCapacityFiltersDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardAtCapacityTable(filterValues);
	}

	@RequestMapping(value = "/getTeTDashboardAtCapacityKpis", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardAtCapacityKpis(@RequestBody AtCapacityFiltersDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardAtCapacityKpis(filterValues);
	}

	@RequestMapping(value = "/getAtCapacityExcelDownload", method = RequestMethod.GET)
	public void getAtCapacityExcelDownload(@RequestParam final String resourceId, final String status,
			final String region, final String currentFacility, final String p6Location, final String partNumbers,final String trackId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileName = "TeTDashboardAtCapacity.xlsx";

		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iOFETeTDashboardService.getAtCapacityExcelDownload(resourceId, status, region,
					currentFacility, p6Location, partNumbers,trackId);
			IOUtils.write(plData, response.getOutputStream());

		} catch (Exception e) {
			LOGGER.error("Error occured when downloading excel details {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading excel details{}" , e.getStackTrace());
			}
		}
	}

	// resource_id_pns Tab
	@RequestMapping(value = "/getTeTDashboardResourcePnsTable", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardResourcePnsTable(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardResourcePnsTable(filterValues);
	}

	@RequestMapping(value = "/getTeTDashboardResourcePnsFilters", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardResourcePnsFilters(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardResourcePnsFilter();
	}

	@RequestMapping(value = "/saveResourcePns", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> saveResourcePns(@RequestHeader HttpHeaders headers,
			@RequestBody List<ResourcePnsTable> resourcePnsTable) {
		return new ResponseEntity<Map<String, Object>>(iOFETeTDashboardService.saveResourcePns(resourcePnsTable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/updateResourcePns", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public ResponseEntity<?> updateResourcePns(@RequestHeader HttpHeaders headers,
			@RequestBody List<ResourcePnsTable> resourcePnsTable) {
		return new ResponseEntity<Map<String, Object>>(iOFETeTDashboardService.updateResourcePns(resourcePnsTable),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteResourcePns", method = RequestMethod.POST)
	public Map<String, Object> deleteResourcePns(@RequestBody ResourcePnsTable resourcePnsTable) {
		return iOFETeTDashboardService.deleteResourcePns(resourcePnsTable);
	}

	// At Location
	@RequestMapping(value = "/getTeTDashboardATLocationTable", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardATLocationTable() {
		return iOFETeTDashboardService.getTeTDashboardATLocationTable();
	}

	@RequestMapping(value = "/updateAtLocationTable", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> updateAtLocationTable(@RequestBody List<AtLocationDTO> filterValues) {
		return iOFETeTDashboardService.updateAtLocationTable(filterValues);
	}

	@RequestMapping(value = "/getTeTDashboardAtLocationFilters", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardAtLocationFilters(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getTeTDashboardAtCapacityFilters(filterValues);
	}

	// Demand Vs Capacity

	@RequestMapping(value = "/getDemandAndCpacityFilters", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getDemandAndCpacityFilters(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return iOFETeTDashboardService.getDemandAndCpacityFilters(filterValues);
	}
	
	@RequestMapping(value = "/downloadDemandAndCpacityExcel", method = RequestMethod.GET)
	public void downloadDemandAndCpacityExcel(@RequestParam final String projectId, final String roleId,
			final String p6Region, final String p6Location,
			final String activityStatus, final String resourceId, final String region, final String location,
			final String startDate, final String endDate, final int minExcess, final String gapToggle,final boolean flag,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String fileName = "Demand_Capacity.xlsx";

		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iOFETeTDashboardService.downloadDemandAndCpacityExcel(projectId, roleId, p6Region, 
					p6Location ,activityStatus,resourceId, region, location, startDate, endDate, minExcess, gapToggle,flag);
			IOUtils.write(plData, response.getOutputStream());

		} catch (Exception e) {
			LOGGER.error("Error occured when downloading excel details{}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading excel details{}" , e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/getDemandVsCapacityTable", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getDemandVsCapacityTable(@RequestBody DemandVsAvailablilityDTO dto,@RequestParam("isMonthly") boolean flag) {
		return iOFETeTDashboardService.getDemandVsCapacityTable(dto,flag);
	}

	@RequestMapping(value = "/getDemandVsCapacityKpis", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTeTDashboardAtCapacityKpis(@RequestBody DemandVsAvailablilityDTO filterValues) {
		return iOFETeTDashboardService.getDemandVsCapacityKpis(filterValues);
	}
	
	@RequestMapping(value = "/getDemandVsCapacityTablePopup", method = RequestMethod.GET)
	public Map<String, Object> getDemandVsCapacityTablePopup( @RequestParam("resourceId") String resourceId,
			@RequestParam("resourceName") String resourceName, @RequestParam("projectId") String projectId, @RequestParam("status") String status,@RequestParam String date,@RequestParam("isMonthly") boolean flag,
			@RequestParam("p6Location") String p6Location,@RequestParam("p6Region") String p6Region,@RequestParam("atRegion") String atRegion,@RequestParam("atLocation") String atLocation, @RequestParam("role") String role) {
		return iOFETeTDashboardService.getDemandVsCapacityTablePopup(resourceId, resourceName, projectId, status, date,flag,p6Location,p6Region,atRegion,atLocation,role);
	}

	@GetMapping(value = "/getLastRefreshDate")
	public Map<String, Object> getLastRefreshDate(){
		return iOFETeTDashboardService.getLastRefreshDate();
	}

}
