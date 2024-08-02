package com.bh.realtrack.dao;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.BusinessUnitSummaryDTO;
import com.bh.realtrack.dto.DashboardCountDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.ForecastChartDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceDropdownListDTO;
import com.bh.realtrack.dto.InvoiceListDTO;
import com.bh.realtrack.dto.ManageProjectResponseDTO;
import com.bh.realtrack.dto.MilestoneListDTO;
import com.bh.realtrack.dto.OFEOpenInvoiceDataTableDTO;
import com.bh.realtrack.dto.OFESaveOpenInvoiceDTO;
import com.bh.realtrack.dto.OfeBillingInvoiceListDTO;
import com.bh.realtrack.dto.OfeBillingMilestoneListDTO;
import com.bh.realtrack.dto.OfeBillingProjectListDTO;
import com.bh.realtrack.dto.OfeCurveResponseDTO;
import com.bh.realtrack.dto.OfeOpenInvoiceChartPopupDetails;
import com.bh.realtrack.dto.ProjectTargetDTO;
import com.bh.realtrack.dto.TrendChartRequestDTO;
import com.bh.realtrack.dto.TrendChartResponseDTO;


public interface OfeBillingDashboardDAO {

	List<ManageProjectResponseDTO> getmanageProjectList(
			HeaderDashboardDetailsDTO headerDetails, String projectId);

	String getRole(String sso);

	List<BusinessUnitSummaryDTO> getBusinessUnitSummary(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	String getFinancialBlKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	String getLastEstimateKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	String getActualKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	String getTogoVsFBLKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	String getBmLinkageKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate);
	
	String getUnmatchedBLKpi(BillingDashboardFilterDTO filterValues);

	List<OfeBillingProjectListDTO> getBillingProjectList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate);

	List<OfeBillingMilestoneListDTO> getBillingMilestoneList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate);

	List<OfeCurveResponseDTO> getActualCurveList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate);

	List<OfeCurveResponseDTO> getForecastCurveList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate);

	List<OfeCurveResponseDTO> getFinancialBlCurveList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate);

	List<String> getWeeks(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	List<OfeBillingInvoiceListDTO> getBillingInvoiceList(BillingDashboardFilterDTO filterValues, String startDate,
			String endDate);

	String getEditAccess(String sso);

	List<ProjectTargetDTO> getProjectTargetList(HeaderDashboardDetailsDTO header, String projectId);

	int editProjectTargetList(ProjectTargetDTO targetDto, String sso);

	List<TrendChartResponseDTO> getTrendChart(TrendChartRequestDTO header, String startDate, String endDate);

	String getOverdueKpi(BillingDashboardFilterDTO filterValues, String startDate, String endDate);

	Map<String, Object> getOpenInvoiceChart(String projectId);

	String getLastUpdatedDate(String projectId);

	List<OfeOpenInvoiceChartPopupDetails> getOpenInvoiceChartPopupDetails(String projectId, String chartType,
			String statusCode);

	List<OFEOpenInvoiceDataTableDTO> getOpenInvoiceDatatable(String projectId);

	boolean saveOpenInvoiceDetails(List<OFESaveOpenInvoiceDTO> invoicesList, String sso);

	Map<String, Object> getCollectedCashCurve(String projectId, Map<String, Object> cashCollectionCurveMap);

	Map<String, Object> getPastDueCurve(String projectId, Map<String, Object> cashCollectionCurveMap);

	String getOpenInvoiceLastSavedDate(String projectId);

	Map<String, Object> getForecastCashCurve(String projectId, Map<String, Object> cashCollectionCurveMap);

	Map<String, Object> getOtrCashBaselineCurve(String projectId, Map<String, Object> cashCollectionCurveMap);

	List<ForecastChartDTO> getForecastChart(String projectId);

	String getEditAccessBillingWidget(String sso);

	int editInvoiceList(List<InvoiceListDTO> invoiceDto, String sso);

	int editMilestoneList(List<MilestoneListDTO> milestoneDto, String sso);

	String getBMVLastRefreshDate();

	List<String> getScurveFunctionCode(String projectId, String published);

	List<DropDownDTO> getScurveProductLineCode(String projectId, String published);

	List<String> getScurveProjectCode(String projectId, String published);

	DashboardCountDTO getDashboardColorCount(String sso, int customerId, int companyId, String warrantyFlag);

	DashboardCountDTO getDashboardWarrantyFlagCount(String sso, int customerId, int companyId, String warrantyFlag);

	List<InvoiceDropdownListDTO> getInvoiceDropDownList(String projectId);


}