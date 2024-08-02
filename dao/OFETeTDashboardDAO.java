package com.bh.realtrack.dao;

import java.util.List;

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

public interface OFETeTDashboardDAO {

	List<DropDownDTO> getIprProjectNameFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getRoleNameFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getResourceNameFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getLocationFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getRegionFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getActivityStatusFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getDemandTypeFilter(HeaderDashboardDetailsDTO filterValues);

	List<OFETeTDashboardManageProjectDTO> getManageProjectList(HeaderDashboardDetailsDTO filterValues,
			String projectId);

	List<OFETeTProjectTableDTO> getTeTProjectTable(OFETeTDashboardDTO filterValues);

	String getDemandLines(OFETeTDashboardDTO filterValues);

	String getAssetsDemanded(OFETeTDashboardDTO filterValues);

	String getAssetDays(OFETeTDashboardDTO filterValues);

	String getMaxDailyAssets(OFETeTDashboardDTO filterValues);

	List<EditLocationTableDTO> getTeTLocationTable();

	boolean editLocationTable(List<EditLocationTableDTO> editLocationDto, String sso);

	String getEditAccessTetDashboard(String sso);

	List<OFETeTProjectTableDTO> exportTeTDashboardExcelDownloadData(OFETeTDashboardDTO dto);

	public List<AssetTrackerDataDTO> readAssetTrackerData();

	public int insertDataIntoStageTable(List<AssetTrackerStageDTO> stageDtoList, String sso);

	List<DropDownDTO> getResourceFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getStatusFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getCapacityAtLocationFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getCapacityP6LocationFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getPartNumberFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getCapacityRegionFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getPartNumberFilter();

	List<ResourcePnsTable> getTeTDashboardResourcePnsTable();

	List<PnsDropDownDTO> getResourceIDFilter();

	List<DropDownDTO> getResourceNameFilter();

	int deleteResourcePns(ResourcePnsTable resourcePnsTable);

	List<AtLocationDTO> getTeTDashboardATLocationTable();

	List<AtCapacityDTO> getTeTDashboardAtCapacityTable(AtCapacityFiltersDTO filterValues);

	int updateAtLocationTable(List<AtLocationDTO> atLocationDTO, String sso);

	String getAssetCount(AtCapacityFiltersDTO filterValues);

	String getActiveAssetCount(AtCapacityFiltersDTO filterValues);

	int updateResourcePns(List<ResourcePnsTable> resourcePnsTable, String sso);

	int saveResourcePns(List<ResourcePnsTable> resourcePnsTable, String sso);

	void deleteDataFromAssetTrackerStage();

	void deleteDataFromAssetTrackerTarget();

	int insertDataIntoTargetTable(List<AssetTrackerStageDTO> stageDtoList, String sso);

	List<DropDownDTO> getTeTDashboardCapacityFilters();

	List<DropDownDTO> getDCProjectFilters(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getDCRoleFilter(HeaderDashboardDetailsDTO filterValues);
	
	List<DropDownDTO> getP6DemandRegionFilter(HeaderDashboardDetailsDTO filterValues);
	
	List<DropDownDTO> getP6DemandLocationFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getDCResourceFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getDCLocationFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getDCRegionFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getDCActivityStatusFilter(HeaderDashboardDetailsDTO filterValues);

	List<DropDownDTO> getMinExcessFilter(HeaderDashboardDetailsDTO filterValues);

	List<DemandVsCapacityTableDTO> downloadDemandAndCpacityExcel(DemandVsAvailablilityDTO dto);

	String getToCoverGaps(DemandVsAvailablilityDTO filterValues);

	String getWorstDayGapCount(DemandVsAvailablilityDTO filterValues);

	String getAssetsCount(DemandVsAvailablilityDTO filterValues);

	String getMinExcessCount(DemandVsAvailablilityDTO filterValues);

	String getAssetsWoMinExcessCount(DemandVsAvailablilityDTO filterValues);

	List<DropDownDTO> getTrackIdFilter(HeaderDashboardDetailsDTO filterValues);

	List<DemandDailyAndMonthPopupDTO> getDemandDailyAndMonthPopupList(String resourceId, String resourceName,
		String projectId, String date, boolean flag, String p6Region, String p6Location);

	List<AssetDailyAndMonthlyPopupDTO> getAssetDailyAndMonthlyPopupList(String resourceId, String resourceName, String projectId, String status,
			String date, boolean flag, String atRegion, String atLocation, String p6Region, String p6Location, String role);
	
	List<OFETeTProjectTableMonthDTO> getTeTProjectTableForMonth(OFETeTDashboardDTO filterValues);

	List<AssetDailyAndMonthlyPopupDTO> getAssetDailyPopupList(String resourceId, String resourceName, String projectId, String status,  String date,
			boolean flag, String atRegion, String atLocation, String p6Region, String p6Location, String role);

	List<DemandVsCapacityTableMonthDTO> getDemandVsCapacityMonthTableList(DemandVsAvailablilityDTO dto);

	String initialFunctionCallForView(DemandVsAvailablilityDTO dto);

	String getP6LastUpdatedDate();

	String getAssetTrackerLastUpdatedDate();


}