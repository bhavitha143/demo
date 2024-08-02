package com.bh.realtrack.service;

import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.DashboardCountDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceListDTO;
import com.bh.realtrack.dto.MilestoneListDTO;
import com.bh.realtrack.dto.OFESaveOpenInvoiceDTO;
import com.bh.realtrack.dto.ProjectTargetDTO;
import com.bh.realtrack.dto.TrendChartRequestDTO;


/**
 * @author Radhika Tadas
 */
public interface OfeBillingDashboardService {

	Map<String, Object> getBillingManageProjects(HeaderDashboardDetailsDTO headerDetails);

	Map<String, Object> getBusinessUnitSummary(BillingDashboardFilterDTO filterValues) throws Exception;

	Map<String, Object> getBillingKpis(BillingDashboardFilterDTO filterValues)throws Exception;

	Map<String, Object> getBillingProjectList(BillingDashboardFilterDTO filterValues)throws Exception;

	Map<String, Object> getBillingCurveList(BillingDashboardFilterDTO filterValues) throws Exception;

	Map<String, Object> getBillingInvoiceList(BillingDashboardFilterDTO filterValues) throws Exception;

	String getEditAccess(String sso);

	Map<String, Object> getProjectTargetList(HeaderDashboardDetailsDTO headerDetails);

	Map<String, Object> editProjectTargetList(ProjectTargetDTO targetDto);

	Map<String, Object> getTrendChart(TrendChartRequestDTO headerDetails);

	byte[] getProjectTargetExcel(int companyId, String business, String segment, int customerId, String region);

	Map<String, Object> getOpenInvoiceChart(String projectId);

	Map<String, Object> getOpenInvoiceChartPopupDetails(String projectId, String chartType, String statusCode);

	Map<String, Object> getOpenInvoiceDatatable(String projectId);;

	Map<String, Object> saveOpenInvoiceDetails(List<OFESaveOpenInvoiceDTO> invoicesList);

	Map<String, Object> getCashCollectionReportCurve(String projectId);

	byte[] getOpenInvoiceDatatableExcel(String business);

	Map<String, Object> getForecastChart(String projectId);

	byte[] getForecastExcel(String projectId);

	String getEditAccessBillingWidget(String sso);

	Map<String, Object> editInvoiceList(List<InvoiceListDTO> invoiceDto);

	Map<String, Object> editMilestoneList(List<MilestoneListDTO> milestoneDto);

	Map<String, Object> getBMVLastRefreshDate();

	Map<String, Object> getScurvefilters(String projectId, String published);

	DashboardCountDTO getDashboardColorCount(int customerId,int companyId, String warrantyFlag);

	//Map<String, Object> getAllInvoicesDetails(String projectId);

	byte[] downloadOpenInvoiceChartPopupDetails(String projectId, String chartType, String statusCode);

	Map<String, Object> getInvoiceDropDownList(String projectId);

	

}