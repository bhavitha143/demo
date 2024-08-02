package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.AtCapacityFiltersDTO;
import com.bh.realtrack.dto.AtLocationDTO;
import com.bh.realtrack.dto.DemandVsAvailablilityDTO;
import com.bh.realtrack.dto.EditLocationTableDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OFETeTDashboardDTO;
import com.bh.realtrack.dto.ResourcePnsTable;

public interface OFETeTDashboardService {

	Map<String, Object> getTeTDashboardFilters(HeaderDashboardDetailsDTO filterValues);

	Map<String, Object> getTeTManageProject(HeaderDashboardDetailsDTO filterValues);

	Map<String, Object> getTeTProjectTable(OFETeTDashboardDTO filterValues,boolean flag);

	Map<String, Object> getTeTDashboardKpis(OFETeTDashboardDTO filterValues);

	Map<String, Object> getLocationTable();

	Map<String, Object> editLocationTable(List<EditLocationTableDTO> editLocationDto);

	String getEditAccessTetDashboard(String sso);

	byte[] exportTeTDashboardExcelDownloadData(String projectId, String roleId, String resourceId, String region,
			String location, String activityStatus, String startDate, String endDate, String period, String demandType,
			String projectName, boolean flag);

	Map<String, Object> readAssetTrackerData();

	Map<String, Object> getTeTDashboardCapacityFilters(HeaderDashboardDetailsDTO filterValues);

	Map<String, Object> getTeTDashboardResourcePnsTable(HeaderDashboardDetailsDTO filterValues);

	Map<String, Object> getTeTDashboardResourcePnsFilter();

	Map<String, Object> updateResourcePns(List<ResourcePnsTable> resourcePnsTable);

	Map<String, Object> deleteResourcePns(ResourcePnsTable resourcePnsTable);

	Map<String, Object> getTeTDashboardATLocationTable();

	Map<String, Object> updateAtLocationTable(List<AtLocationDTO> filterValues);

	Map<String, Object> getTeTDashboardAtCapacityTable(AtCapacityFiltersDTO filterValues);

	Map<String, Object> getTeTDashboardAtCapacityKpis(AtCapacityFiltersDTO filterValues);

	Map<String, Object> saveResourcePns(List<ResourcePnsTable> resourcePnsTable);

	Map<String, Object> getTeTDashboardAtCapacityFilters(HeaderDashboardDetailsDTO filterValues);

	Map<String, Object> getDemandAndCpacityFilters(HeaderDashboardDetailsDTO filterValues);

	byte[] downloadDemandAndCpacityExcel(String projectId, String roleId,String p6Region,String p6Location, String activityStatus, String resourceId,
			String region, String location, String startDate, String endDate, int minExcess, String gapToggle,boolean flag);

	Map<String, Object> getDemandVsCapacityTable(DemandVsAvailablilityDTO dto, boolean flag);

	Map<String, Object> getDemandVsCapacityKpis(DemandVsAvailablilityDTO filterValues);

	byte[] getAtCapacityExcelDownload(String resourceId, String status, String region, String currentFacility,
			String p6Location, String partNumbers, String trackId);

	Map<String, Object> getDemandVsCapacityTablePopup(String resourceId, String resourceName, String projectId, String status, String date,
			boolean flag, String p6Location, String p6Region, String atRegion, String atLocation, String role);

	Map<String,Object> getLastRefreshDate();

}
