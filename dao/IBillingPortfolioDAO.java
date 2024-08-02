package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.ActualCurveDTO;
import com.bh.realtrack.dto.BaselineCurveDTO;
import com.bh.realtrack.dto.BillingCycleDTO;
import com.bh.realtrack.dto.BillingCycleRemarksDTO;
import com.bh.realtrack.dto.BillingKpiDTO;
import com.bh.realtrack.dto.BillingLinearityChartDTO;
import com.bh.realtrack.dto.BillingLinearityChartPopupDTO;
import com.bh.realtrack.dto.BillingMilestoneListDTO;
import com.bh.realtrack.dto.BillingProjectListDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ErrorDetailsDTO;
import com.bh.realtrack.dto.ExceptionCategoryDTO;
import com.bh.realtrack.dto.ExceptionDTO;
import com.bh.realtrack.dto.ExceptionRemarksDTO;
import com.bh.realtrack.dto.FileUploadStatusDTO;
import com.bh.realtrack.dto.ForecastCurveDTO;
import com.bh.realtrack.dto.LastSuccessfulUpdateDetailsDTO;
import com.bh.realtrack.dto.LastUpdateDetailsDTO;
import com.bh.realtrack.dto.PercentageDTO;
import com.bh.realtrack.dto.SegmentSummaryDTO;
import com.bh.realtrack.dto.TPSBillingOutOfRTProjectDetailsDTO;
import com.bh.realtrack.dto.ViewInvoiceDTO;

public interface IBillingPortfolioDAO {

	List<String> getPmLeaderDropDown(String projectId);

	List<String> getSpmDropDown(String projectId);

	List<String> getFinancialSegmentDropDown(String projectId);

	Map<String, Object> getBillingDashOverAllSummary(String projectId, String startDate, String endDate,
			String business);

	List<SegmentSummaryDTO> getSegmentSummary(String projectId, String startDate, String endDate, String business);

	List<BillingProjectListDTO> getBillingProjectList(String projectId, String startDate, String endDate,
			String business);

	List<BillingMilestoneListDTO> getBillingMilestoneList(String projectId, String startDate, String endDate,
			String business);

	List<ForecastCurveDTO> getForecastCurve(String projectId, String startDate, String endDate);

	List<ActualCurveDTO> getActualCurve(String projectId, String startDate, String endDate);

	List<BaselineCurveDTO> getBaselineCurve(String projectId, String startDate, String endDate);

	List<String> getWeeks(String projectId, String startDate, String endDate);

	List<BaselineCurveDTO> getITOBaselineCurve(String projectId, String startDate, String endDate);

	Double getCurrentGap(String projectId, String startDate, String endDate);

	String getLastUpdatedDate();

	List<PercentageDTO> getPercentage(String projectId, String startDate, String endDate);

	int getGapRecovery(String projectId, String startDate, String endDate);

	List<BillingKpiDTO> getBillingKpi(String projectId, String startDate, String endDate, String business);

	List<ExceptionDTO> getBillingException(String projectId, String startDate, String endDate, String business);

	BillingCycleDTO getOverallBillingCycleDetail(String projectId, String startDate, String endDate);

	BillingCycleDTO getTargetBillingCycleDetail(String projectId, String startDate, String endDate);

	List<ViewInvoiceDTO> getBillingInvoiceDetails(String projectId, String startDate, String endDate, String business);

	List<ExceptionCategoryDTO> getBillingCategoryDetails(String projectId, String startDate, String endDate,
			String business);

	List<BillingKpiDTO> getMonthlyBillingKpi(String projectId, String startDate, String endDate, String business);

	String getTPSBillingReportURI();

	List<DropDownDTO> getBillingInvoiceRemarksList();

	int saveBillingMilestoneRemarkDetails(List<BillingCycleRemarksDTO> invoiceList, String sso);

	List<DropDownDTO> getExceptionRemarksList();

	int saveBillingExceptionRemarkDetails(List<ExceptionRemarksDTO> exceptionList, String sso);

	List<ActivitiesDTO> getBillingActivitiesDetails(String projectId, String cashMilestoneId);

	List<BillingLinearityChartDTO> getBillingLinearityChart(String projectId);

	List<BillingLinearityChartPopupDTO> getBillingLinearityChartPopup(String projectId, String startDate,
			String endDate);

	List<BillingLinearityChartPopupDTO> getBillingLinearityChartPopupForLE(String projectId, String startDate,
			String endDate);

	String getProjectList(int companyId, String business, String segment, int customerId, String region,
			String projectId, String pmLeader, String spm, String financialSegment);

	List<LastSuccessfulUpdateDetailsDTO> getLastSuccessfulUpdateData(String companyId);

	List<LastUpdateDetailsDTO> getBillingLastUpdatedData(String companyId);

	List<ErrorDetailsDTO> getErrorDetailsData(String companyId);

	List<ErrorDetailsDTO> getNotProcessedProjectDetails(String companyId);

	List<TPSBillingOutOfRTProjectDetailsDTO> getTPSBillingOutOfProjectTemplateDetails(String companyId);

	boolean getFileUploadStatus(String companyId, String moduleName);

	Map<Integer, String> fetchProjectOutOfRTExcelHeaderColumnMap();

	void updateFileTrackingDetails(FileUploadStatusDTO statusDTO);

	void insertFileTrackingDetails(FileUploadStatusDTO statusDTO);

	Integer getFileTrackingId(FileUploadStatusDTO statusDTO);

	void insertProjectExcelStageData(List<TPSBillingOutOfRTProjectDetailsDTO> list, FileUploadStatusDTO statusDTO);

	void callFileUploadStageToTarget(FileUploadStatusDTO statusDTO);

	List<BillingKpiDTO> getQuarterlyBillingKpi(String projectId, String startDate, String endDate, String business);

	List<BillingLinearityChartPopupDTO> downloadBillingLinearityExcel(String projectId);

}
