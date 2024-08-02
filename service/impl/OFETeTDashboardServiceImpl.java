package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.OFETeTDashboardDAO;
import com.bh.realtrack.dto.AssetDailyAndMonthlyPopupDTO;
import com.bh.realtrack.dto.AssetTrackerDataDTO;
import com.bh.realtrack.dto.AssetTrackerStageDTO;
import com.bh.realtrack.dto.AtCapacityDTO;
import com.bh.realtrack.dto.AtCapacityFiltersDTO;
import com.bh.realtrack.dto.AtLocationDTO;
import com.bh.realtrack.dto.DemandDailyAndMonthPopupDTO;
import com.bh.realtrack.dto.DemandVsAvailablilityDTO;
import com.bh.realtrack.dto.DemandVsCapacityTableDTO;
import com.bh.realtrack.dto.DemandVsCapacityTableMonthDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.EditLocationTableDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OFETeTDashboardDTO;
import com.bh.realtrack.dto.OFETeTDashboardManageProjectDTO;
import com.bh.realtrack.dto.OFETeTProjectTableDTO;
import com.bh.realtrack.dto.OFETeTProjectTableMonthDTO;
import com.bh.realtrack.dto.PnsDropDownDTO;
import com.bh.realtrack.dto.ResourcePnsTable;
import com.bh.realtrack.excel.ExportBillingDetailsExcel;
import com.bh.realtrack.excel.ExportDemandVsCapacityExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICommonService;
import com.bh.realtrack.service.OFETeTDashboardService;

@Service
public class OFETeTDashboardServiceImpl implements OFETeTDashboardService {

	@Autowired
	private OFETeTDashboardDAO iOFETeTDashboardDAO;

	@Autowired
	private CallerContext callerContext;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ICommonService commonService;

	private static final Logger log = LoggerFactory.getLogger(OfeBillingDashboardServiceImpl.class);

	@Override
	public Map<String, Object> getTeTDashboardFilters(HeaderDashboardDetailsDTO filterValues) {

		LocalDate date = LocalDate.now();
		String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MMM-dd"));

		LocalDate startDate = date.minusYears(1);
		LocalDate endDate = date.plusYears(1);
		int startYear = startDate.getYear();
		int endYear = endDate.getYear();

		String metric1StartDate = startYear + "-Jan-01";
		String metric1EndDate = endYear + "-Dec-31";

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<DropDownDTO> iprProjectName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> roleName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> resourceName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> location = new ArrayList<DropDownDTO>();
		List<DropDownDTO> region = new ArrayList<DropDownDTO>();
		List<DropDownDTO> activityStatus = new ArrayList<DropDownDTO>();
		List<DropDownDTO> demandType = new ArrayList<DropDownDTO>();
		iprProjectName = iOFETeTDashboardDAO.getIprProjectNameFilter(filterValues);
		roleName = iOFETeTDashboardDAO.getRoleNameFilter(filterValues);
		resourceName = iOFETeTDashboardDAO.getResourceNameFilter(filterValues);
		location = iOFETeTDashboardDAO.getLocationFilter(filterValues);
		region = iOFETeTDashboardDAO.getRegionFilter(filterValues);
		activityStatus = iOFETeTDashboardDAO.getActivityStatusFilter(filterValues);
		demandType = iOFETeTDashboardDAO.getDemandTypeFilter(filterValues);

		data.put("IprProjectName", iprProjectName);
		data.put("RoleName", roleName);
		data.put("ResourceName", resourceName);
		data.put("Location", location);
		data.put("Region", region);
		data.put("ActivityStatus", activityStatus);
		data.put("DemandType", demandType);
		data.put("startDate", metric1StartDate);
		data.put("endDate", metric1EndDate);
		response.put("data", data);
		return response;

	}

	@Override
	public Map<String, Object> getTeTManageProject(HeaderDashboardDetailsDTO filterValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		String projectId = commonService.fetchFavProjects();

		if (null == projectId) {
			projectId = "0";
		}
		List<OFETeTDashboardManageProjectDTO> manageProjectList = new ArrayList<OFETeTDashboardManageProjectDTO>();
		manageProjectList = iOFETeTDashboardDAO.getManageProjectList(filterValues, projectId);

		response.put("ManageProjectList", manageProjectList);
		return response;

	}

	@Override
	public Map<String, Object> getTeTProjectTable(OFETeTDashboardDTO filterValues,boolean flag) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<OFETeTProjectTableDTO> projectTable = new ArrayList<OFETeTProjectTableDTO>();
		List<OFETeTProjectTableMonthDTO> projectTableMon = new ArrayList<OFETeTProjectTableMonthDTO>();
		
		if(flag==true) {
			projectTableMon = iOFETeTDashboardDAO.getTeTProjectTableForMonth(filterValues);
			response.put("ProjectTable", projectTableMon);	
			
		}else {
			projectTable = iOFETeTDashboardDAO.getTeTProjectTable(filterValues);
			response.put("ProjectTable", projectTable);	
		}
		return response;
	}

	@Override
	public Map<String, Object> getTeTDashboardKpis(OFETeTDashboardDTO filterValues) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		String demandLines = null;
		String assetsDemanded = null;
		String assetDays = null;
		String maxDailyAssets = null;

		demandLines = iOFETeTDashboardDAO.getDemandLines(filterValues);
		assetsDemanded = iOFETeTDashboardDAO.getAssetsDemanded(filterValues);
		assetDays = iOFETeTDashboardDAO.getAssetDays(filterValues);
		maxDailyAssets = iOFETeTDashboardDAO.getMaxDailyAssets(filterValues);

		data.put("DemandLines", demandLines);
		data.put("AssetsDemanded", assetsDemanded);
		data.put("AssetDays", assetDays);
		data.put("MaxDailyAssets", maxDailyAssets);
		response.put("data", data);
		return response;

	}

	@Override
	public Map<String, Object> getLocationTable() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<EditLocationTableDTO> locationTable = new ArrayList<EditLocationTableDTO>();
		locationTable = iOFETeTDashboardDAO.getTeTLocationTable();

		response.put("LocationTable", locationTable);
		return response;
	}

	@Override
	public Map<String, Object> editLocationTable(List<EditLocationTableDTO> editLocationDto) {
		boolean result = false;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		String sso = callerContext.getName();

		try {
			result = iOFETeTDashboardDAO.editLocationTable(editLocationDto, sso);
			if (result = true) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}

		return responseMap;
	}

	@Override
	public String getEditAccessTetDashboard(String sso) {

		String editAccessFlag = "false";
		try {
			if (sso != null) {
				editAccessFlag = iOFETeTDashboardDAO.getEditAccessTetDashboard(sso);

			} else {
				throw new Exception("Error getting edit access for : " + sso);
			}
		} catch (Exception e) {
			log.error("getEditAccess(): Exception occurred : " + e.getMessage());
		}
		return editAccessFlag;
	}

	@Override
	public byte[] exportTeTDashboardExcelDownloadData(String projectId, String roleId, String resourceId, String region,
			String location, String activityStatus, String startDate, String endDate, String period, String demandType,
			String projectName,boolean flag) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
		List<OFETeTProjectTableMonthDTO> projectExcelMon = new ArrayList<OFETeTProjectTableMonthDTO>();
		List<OFETeTProjectTableDTO> projectExcelDay = new ArrayList<OFETeTProjectTableDTO>();

		try {
			OFETeTDashboardDTO filterValues = new OFETeTDashboardDTO();
			filterValues.setProjectId(projectId);
			filterValues.setRoleId(roleId);
			filterValues.setResourceId(resourceId);
			filterValues.setRegion(region);
			filterValues.setLocation(location);
			filterValues.setActivityStatus(activityStatus);
			filterValues.setStartDate(startDate);
			filterValues.setEndDate(endDate);
			filterValues.setPeriod(period);
			filterValues.setDemandType(demandType);
			filterValues.setProjectName(projectName);
			
			if(flag==true) {
				projectExcelMon = iOFETeTDashboardDAO.getTeTProjectTableForMonth(filterValues);
				exportBillingDetailsExcel.exportOfeTeTDashboardMonExcel(workbook, projectExcelMon);	
				
			}else {
				projectExcelDay = iOFETeTDashboardDAO.getTeTProjectTable(filterValues);
				exportBillingDetailsExcel.exportOfeTeTDashboardDayExcel(workbook, projectExcelDay);	
			}
			
			workbook.write(bos);
			excelData = bos.toByteArray();

		} catch (Exception e) {
			log.error("Error occured when downlaoding Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downlaoding Excel file" + e.getMessage());
			}
		}
		return excelData;
	}
	
	public Map<String, Object> readAssetTrackerData() {

		String sso = callerContext.getName();
		Map<String, Object> response = new HashMap<String, Object>();
		List<AssetTrackerDataDTO> assetTrakerData = new ArrayList<AssetTrackerDataDTO>();
		assetTrakerData = iOFETeTDashboardDAO.readAssetTrackerData();

		List<AssetTrackerStageDTO> stageDtoList = readJsonData(assetTrakerData);

		// delete stage data
		iOFETeTDashboardDAO.deleteDataFromAssetTrackerStage();

		int insertedListCount = iOFETeTDashboardDAO.insertDataIntoStageTable(stageDtoList, sso);
		log.info("insertedListCount for stageTable = " + insertedListCount);
		if (insertedListCount != 0) {
			// delete target data
			iOFETeTDashboardDAO.deleteDataFromAssetTrackerTarget();
			// call into target
			insertedListCount = iOFETeTDashboardDAO.insertDataIntoTargetTable(stageDtoList, sso);
			log.info("insertedListCount for TargetTable = "+ insertedListCount);
			if (insertedListCount != 0) {
				response.put("Success", "Success");
			} else {
				response.put("Error", "Error");
			}
		} else {
			response.put("Error", "Error");
		}

		return response;
	}

	private List<AssetTrackerStageDTO> readJsonData(List<AssetTrackerDataDTO> assetTrakerData) {
		List<AssetTrackerStageDTO> assetTrackerList = new ArrayList<AssetTrackerStageDTO>();
		log.info("Reading json Data started ");
		for (AssetTrackerDataDTO assetTrackerDTO : assetTrakerData) {
			JSONObject featuresJsonObject;
			try {
				AssetTrackerStageDTO dto = new AssetTrackerStageDTO();
				featuresJsonObject = new JSONObject(assetTrackerDTO.getJson());

				if (featuresJsonObject.toString().contains("id") && !featuresJsonObject.get("id").equals(null)) {
					dto.setId(featuresJsonObject.getString("id"));
				}
				if (featuresJsonObject.toString().contains("siteId")
						&& !featuresJsonObject.get("siteId").equals(null)) {
					dto.setCurrent_facility(featuresJsonObject.getString("siteId"));
				}
				JSONObject customDataArray = featuresJsonObject.getJSONObject("customData");
				if (customDataArray.toString().contains("partNo") && !customDataArray.get("partNo").equals(null)) {
					dto.setPart_number(customDataArray.get("partNo"));
				}
				if (customDataArray.toString().contains("status") && !customDataArray.get("status").equals(null)) {
					dto.setStatus(customDataArray.getString("status"));
				}
				if (customDataArray.toString().contains("costNew") && !customDataArray.get("costNew").equals(null)) {
					dto.setCostnew(customDataArray.get("costNew"));
				}
				if (customDataArray.toString().contains("serialNo") && !customDataArray.get("serialNo").equals(null)) {
					dto.setSerial_number(customDataArray.get("serialNo"));
				}
				if (customDataArray.toString().contains("repairCost")
						&& !customDataArray.get("repairCost").equals(null)) {
					dto.setRepair_cost(customDataArray.get("repairCost"));
				}
				if (customDataArray.toString().contains("calibrationFrequency")
						&& !customDataArray.get("calibrationFrequency").equals(null)) {
					dto.setCalibration_freq(customDataArray.get("calibrationFrequency"));
				}
				if (customDataArray.toString().contains("maintenanceFrequency")
						&& !customDataArray.get("maintenanceFrequency").equals(null)) {
					dto.setMaintainance_freq(customDataArray.get("maintenanceFrequency"));
				}
				if (customDataArray.toString().contains("calibrationFrequencyUOM")
						&& !customDataArray.get("calibrationFrequencyUOM").equals(null)) {
					dto.setCalibration_freq_uom(customDataArray.get("calibrationFrequencyUOM"));
				}
				if (customDataArray.toString().contains("maintenanceFrequencyUOM")
						&& !customDataArray.get("maintenanceFrequencyUOM").equals(null)) {
					dto.setMaintainance_freq_uom(customDataArray.get("maintenanceFrequencyUOM"));
				}
				if (customDataArray.toString().contains("lastCalibrationDate")
						&& !customDataArray.get("lastCalibrationDate").equals(null)) {
					Timestamp stamp = new Timestamp(customDataArray.getLong("lastCalibrationDate"));
					Date date = new Date(stamp.getTime());
					dto.setCalib_date(date);
				}
				if (customDataArray.toString().contains("lastMaintenanceDate")
						&& !customDataArray.get("lastMaintenanceDate").equals(null)) {
					Timestamp stamp = new Timestamp(customDataArray.getLong("lastMaintenanceDate"));
					Date date = new Date(stamp.getTime());
					dto.setMaintainance_date(date);
				}
				if (customDataArray.toString().contains("description") && !customDataArray.get("description").equals(null)) {
					dto.setDescription(customDataArray.getString("description"));
				}

				if (null != assetTrackerDTO.getRealtrackStatus() ) {
					dto.setRealtrackStatus(assetTrackerDTO.getRealtrackStatus());
				}
				
				assetTrackerList.add(dto);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				log.error("Exception occured while readingJsonData" +e.getMessage());
			}
		}

		return assetTrackerList;
	}

	@Override
	public Map<String, Object> getTeTDashboardCapacityFilters(HeaderDashboardDetailsDTO filterValues) {

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<DropDownDTO> partNumberStatus = new ArrayList<DropDownDTO>();
		List<DropDownDTO> resourceName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> statusName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> capacityRegionName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> capacityAtLocation = new ArrayList<DropDownDTO>();
		List<DropDownDTO> capacityP6Location = new ArrayList<DropDownDTO>();
		List<DropDownDTO> trackId = new ArrayList<DropDownDTO>();
		resourceName = iOFETeTDashboardDAO.getResourceFilter(filterValues);
		statusName = iOFETeTDashboardDAO.getStatusFilter(filterValues);
		capacityRegionName = iOFETeTDashboardDAO.getCapacityRegionFilter(filterValues);
		capacityAtLocation = iOFETeTDashboardDAO.getCapacityAtLocationFilter(filterValues);
		capacityP6Location = iOFETeTDashboardDAO.getCapacityP6LocationFilter(filterValues);
		partNumberStatus = iOFETeTDashboardDAO.getPartNumberFilter(filterValues);
		trackId = iOFETeTDashboardDAO.getTrackIdFilter(filterValues);

		data.put("resourceName", resourceName);
		data.put("statusName", statusName);
		data.put("capacityRegionName", capacityRegionName);
		data.put("capacityAtLocation", capacityAtLocation);
		data.put("capacityAtLocation", capacityAtLocation);
		data.put("capacityP6Location", capacityP6Location);
		data.put("partNumberStatus", partNumberStatus);
		data.put("trackId", trackId);
		response.put("data", data);
		return response;

	}

	@Override
	public Map<String, Object> getTeTDashboardResourcePnsTable(HeaderDashboardDetailsDTO filterValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<ResourcePnsTable> resourcePnsTable = new ArrayList<ResourcePnsTable>();
		resourcePnsTable = iOFETeTDashboardDAO.getTeTDashboardResourcePnsTable();

		response.put("ResourcePnsTable", resourcePnsTable);
		return response;
	}

	@Override
	public Map<String, Object> getTeTDashboardResourcePnsFilter() {

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<PnsDropDownDTO> resourcePnsId = new ArrayList<PnsDropDownDTO>();
		// List<DropDownDTO> resourcePnsName = new ArrayList<DropDownDTO>();
		List<DropDownDTO> partNumber = new ArrayList<DropDownDTO>();
		resourcePnsId = iOFETeTDashboardDAO.getResourceIDFilter();
		// resourcePnsName = iOFETeTDashboardDAO.getResourceNameFilter();
		partNumber = iOFETeTDashboardDAO.getPartNumberFilter();

		data.put("resourcePnsId", resourcePnsId);
		// data.put("resourcePnsName", resourcePnsName);
		data.put("partNumber", partNumber);
		response.put("data", data);
		return response;

	}

	@Override
	public Map<String, Object> updateResourcePns(List<ResourcePnsTable> resourcePnsTable) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String sso = callerContext.getName();

			result = iOFETeTDashboardDAO.updateResourcePns(resourcePnsTable, sso);
			if (result == 1) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveResourcePns(List<ResourcePnsTable> resourcePnsTable) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String sso = callerContext.getName();

			result = iOFETeTDashboardDAO.saveResourcePns(resourcePnsTable, sso);
			if (result == 1) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> deleteResourcePns(ResourcePnsTable resourcePnsTable) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		int result = 0;
		try {
			result = iOFETeTDashboardDAO.deleteResourcePns(resourcePnsTable);
			if (result == 1) {
				responseMap.put("status", "Success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}

		return responseMap;
	}

	@Override
	public Map<String, Object> getTeTDashboardATLocationTable() {
		Map<String, Object> response = new HashMap<String, Object>();
		List<AtLocationDTO> resourcePnsTable = new ArrayList<AtLocationDTO>();
		resourcePnsTable = iOFETeTDashboardDAO.getTeTDashboardATLocationTable();

		response.put("ResourcePnsTable", resourcePnsTable);
		return response;
	}

	@Override
	public Map<String, Object> updateAtLocationTable(List<AtLocationDTO> atLocationDTO) {
		int result = 0;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		try {
			String sso = callerContext.getName();
			result = iOFETeTDashboardDAO.updateAtLocationTable(atLocationDTO, sso);
			if (result == 1) {
				responseMap.put("status", "success");
			} else {
				responseMap.put("status", "Error");
			}

		} catch (Exception e) {
			responseMap.put("status", "Error");
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getTeTDashboardAtCapacityTable(AtCapacityFiltersDTO filterValues) {
		Map<String, Object> response = new HashMap<String, Object>();
		List<AtCapacityDTO> atCapacityTable = new ArrayList<AtCapacityDTO>();
		atCapacityTable = iOFETeTDashboardDAO.getTeTDashboardAtCapacityTable(filterValues);
		response.put("atCapacityTable", atCapacityTable);
		return response;
	}

	@Override
	public Map<String, Object> getTeTDashboardAtCapacityKpis(AtCapacityFiltersDTO filterValues) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		String assetCount = null;
		String activeAssetCount = null;

		assetCount = iOFETeTDashboardDAO.getAssetCount(filterValues);
		activeAssetCount = iOFETeTDashboardDAO.getActiveAssetCount(filterValues);

		data.put("assetCount", assetCount);
		data.put("activeAssetCount", activeAssetCount);

		response.put("data", data);
		return response;
	}

	@Override
	public byte[] getAtCapacityExcelDownload(String resourceId, String status, String region, String currentFacility,
			String p6Location, String partNumbers, String trackId) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		byte[] excelData = null;
		ExportBillingDetailsExcel exportBillingDetailsExcel = new ExportBillingDetailsExcel();
		List<AtCapacityDTO> atCapacityTable = new ArrayList<AtCapacityDTO>();

		try {
			AtCapacityFiltersDTO dto = new AtCapacityFiltersDTO();
			dto.setResource_id(resourceId);
			dto.setStatus(status);
			dto.setRegion(region);
			dto.setRegion(currentFacility);
			dto.setP6_location(p6Location);
			dto.setPart_numbers(partNumbers);
			dto.setTrackId(trackId);
			atCapacityTable = iOFETeTDashboardDAO.getTeTDashboardAtCapacityTable(dto);
			exportBillingDetailsExcel.getAtCapacityExcelDownload(workbook, atCapacityTable);
			workbook.write(bos);
			excelData = bos.toByteArray();

		} catch (Exception e) {
			log.error("Error occured when downlaoding Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downlaoding Excel file" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getTeTDashboardAtCapacityFilters(HeaderDashboardDetailsDTO filterValues) {

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<DropDownDTO> capacityP6Location = new ArrayList<DropDownDTO>();

		capacityP6Location = iOFETeTDashboardDAO.getTeTDashboardCapacityFilters();

		data.put("capacityP6Location", capacityP6Location);
		response.put("data", data);
		return response;

	}

	@Override
	public Map<String, Object> getDemandAndCpacityFilters(HeaderDashboardDetailsDTO filterValues) {

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<DropDownDTO> projectFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> roleFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> p6DemandRegionFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> p6DemandLocationFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> resourceFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> locationFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> regionFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> activityStatusFilterDropdown = new ArrayList<DropDownDTO>();
		List<DropDownDTO> minExcessFilterDropdown = new ArrayList<DropDownDTO>();
		projectFilterDropdown = iOFETeTDashboardDAO.getDCProjectFilters(filterValues);
		roleFilterDropdown = iOFETeTDashboardDAO.getDCRoleFilter(filterValues);
		p6DemandRegionFilterDropdown = iOFETeTDashboardDAO.getP6DemandRegionFilter(filterValues);
		p6DemandLocationFilterDropdown = iOFETeTDashboardDAO.getP6DemandLocationFilter(filterValues);
		resourceFilterDropdown = iOFETeTDashboardDAO.getDCResourceFilter(filterValues);
		locationFilterDropdown = iOFETeTDashboardDAO.getDCLocationFilter(filterValues);
		regionFilterDropdown = iOFETeTDashboardDAO.getDCRegionFilter(filterValues);
		activityStatusFilterDropdown = iOFETeTDashboardDAO.getDCActivityStatusFilter(filterValues);
		minExcessFilterDropdown = iOFETeTDashboardDAO.getMinExcessFilter(filterValues);

		data.put("projectFilterDropdown", projectFilterDropdown);
		data.put("roleFilterDropdown", roleFilterDropdown);
		data.put("p6DemandRegionFilterDropdown", p6DemandRegionFilterDropdown);
		data.put("p6DemandLocationFilterDropdown", p6DemandLocationFilterDropdown);
		data.put("resourceFilterDropdown", resourceFilterDropdown);
		data.put("locationFilterDropdown", locationFilterDropdown);
		data.put("regionFilterDropdown", regionFilterDropdown);
		data.put("activityStatusFilterDropdown", activityStatusFilterDropdown);
		data.put("minExcessFilterDropdown", minExcessFilterDropdown);
		response.put("data", data);
		return response;

	}

	@Override
	public byte[] downloadDemandAndCpacityExcel(String projectId, String roleId,String p6Region,
			String p6Location, String activityStatus,
			String resourceId, String region, String location, String startDate, String endDate, int minExcess,
			String gapToggle,boolean flag) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		//XSSFWorkbook workbook = new XSSFWorkbook();
		SXSSFWorkbook workbook = null;
		byte[] excelData = null;
		ExportDemandVsCapacityExcel demandVsCapacityDetailsExcel = new ExportDemandVsCapacityExcel();

		List<DemandVsCapacityTableDTO> exportExcelDownloadDailyData = new ArrayList<DemandVsCapacityTableDTO>();
		List<DemandVsCapacityTableMonthDTO> exportExcelDownloadMonthData = new ArrayList<DemandVsCapacityTableMonthDTO>();

		try {
			DemandVsAvailablilityDTO dto = new DemandVsAvailablilityDTO();
			dto.setProjectId(projectId);
			dto.setRoleId(roleId);
			dto.setResourceId(resourceId);
			dto.setRegion(region);
			dto.setLocation(location);
			dto.setActivityStatus(activityStatus);
			dto.setStartDate(startDate);
			dto.setEndDate(endDate);
			dto.setMinExcess(minExcess);
			dto.setGapToggle(gapToggle);
			dto.setP6Region(p6Region);
			dto.setP6Location(p6Location);
			workbook = new SXSSFWorkbook();
			if (flag == true ) {
				exportExcelDownloadMonthData = iOFETeTDashboardDAO.getDemandVsCapacityMonthTableList(dto);
				log.info("Excel size :"+exportExcelDownloadMonthData.size());
				demandVsCapacityDetailsExcel.exportDemandVsCapacityMonthExcel(workbook, exportExcelDownloadMonthData);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}else {
				exportExcelDownloadDailyData = iOFETeTDashboardDAO.downloadDemandAndCpacityExcel(dto);
				log.info("Excel size :"+exportExcelDownloadDailyData.size());
				demandVsCapacityDetailsExcel.exportDemandVsCapacityExcel(workbook, exportExcelDownloadDailyData);
				workbook.write(bos);
				excelData = bos.toByteArray();
			}
			
		} catch (Exception e) {
			log.error("Error occured when downlaoding Excel file" + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downlaoding Excel file" + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getDemandVsCapacityTable(DemandVsAvailablilityDTO dto,boolean flag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DemandVsCapacityTableDTO> dailyTableList = new ArrayList<DemandVsCapacityTableDTO>();
		List<DemandVsCapacityTableMonthDTO> monthTableList = new ArrayList<DemandVsCapacityTableMonthDTO>();
		String viewCall = iOFETeTDashboardDAO.initialFunctionCallForView(dto);
		try {
			if(flag==true) {				
				monthTableList = iOFETeTDashboardDAO.getDemandVsCapacityMonthTableList(dto);
				responseMap.put("DemandVsCapacityTable", monthTableList);				
			}else
			{
				dailyTableList = iOFETeTDashboardDAO.downloadDemandAndCpacityExcel(dto);
				responseMap.put("DemandVsCapacityTable", dailyTableList);
			}

		} catch (Exception e) {
			log.error("getDemandVsCapacityTable(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getDemandVsCapacityKpis(DemandVsAvailablilityDTO filterValues) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		String gapCount = null;
		String worstDayGapCount = null;
		String assetCount = null;
		String minExcessCount = null;
		String assetsWoMinExcessCount = null;
		//String viewCall = iOFETeTDashboardDAO.initialFunctionCallForView(filterValues);
		gapCount = iOFETeTDashboardDAO.getToCoverGaps(filterValues);
		worstDayGapCount = iOFETeTDashboardDAO.getWorstDayGapCount(filterValues);
		assetCount = iOFETeTDashboardDAO.getAssetsCount(filterValues);
		minExcessCount = iOFETeTDashboardDAO.getMinExcessCount(filterValues);
		assetsWoMinExcessCount = iOFETeTDashboardDAO.getAssetsWoMinExcessCount(filterValues);

		data.put("gapCount", gapCount);
		data.put("worstDayGapCount", worstDayGapCount);
		data.put("assetCount", assetCount);
		data.put("minExcessCount", minExcessCount);
		data.put("assetsWoMinExcessCount", assetsWoMinExcessCount);
		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getDemandVsCapacityTablePopup(String resourceId, String resourceName, String projectId, String status, String date,
			boolean flag,String p6Location, String p6Region, String atRegion, String atLocation, String role ) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DemandDailyAndMonthPopupDTO> DemandDailyPopupList = new ArrayList<DemandDailyAndMonthPopupDTO>();
		List<AssetDailyAndMonthlyPopupDTO> AssetsDailyPopupList = new ArrayList<AssetDailyAndMonthlyPopupDTO>();
		List<DemandDailyAndMonthPopupDTO> DemandMonthlyPopupList = new ArrayList<DemandDailyAndMonthPopupDTO>();
		List<AssetDailyAndMonthlyPopupDTO> AssetsMonthlyPopupList = new ArrayList<AssetDailyAndMonthlyPopupDTO>();
		
		if (flag == true) {
			DemandMonthlyPopupList = iOFETeTDashboardDAO.getDemandDailyAndMonthPopupList(resourceId, resourceName, projectId, date,
					flag,p6Region,p6Location);
			responseMap.put("DemandPopupList", DemandMonthlyPopupList);
			AssetsMonthlyPopupList = iOFETeTDashboardDAO.getAssetDailyAndMonthlyPopupList(resourceId, resourceName, projectId, status, date,
					flag,atRegion,atLocation, p6Region, p6Location, role);
			responseMap.put("AssetsPopupList", AssetsMonthlyPopupList);
			
		} else {
			DemandDailyPopupList = iOFETeTDashboardDAO.getDemandDailyAndMonthPopupList(resourceId, resourceName, projectId, date,
					flag,p6Region,p6Location);
			responseMap.put("DemandPopupList", DemandDailyPopupList);
			AssetsDailyPopupList = iOFETeTDashboardDAO.getAssetDailyPopupList(resourceId, resourceName, projectId, status,
					date, flag,atRegion,atLocation, p6Region, p6Location, role);
			responseMap.put("AssetsPopupList", AssetsDailyPopupList);

		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getLastRefreshDate() {
		Map<String, Object> responseMap = new HashMap<>();
		String p6LastUpdatedDate=iOFETeTDashboardDAO.getP6LastUpdatedDate();
		String assetTrackerLastUpdatedDate=iOFETeTDashboardDAO.getAssetTrackerLastUpdatedDate();

		responseMap.put("p6LastUpdatedDate",p6LastUpdatedDate);
		responseMap.put("assetTrackerLastUpdatedDate",assetTrackerLastUpdatedDate);

		return responseMap;
	}
}