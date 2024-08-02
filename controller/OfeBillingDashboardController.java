package com.bh.realtrack.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

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

import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.DashboardCountDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.InvoiceListDTO;
import com.bh.realtrack.dto.MilestoneListDTO;
import com.bh.realtrack.dto.OFESaveOpenInvoiceDTO;
import com.bh.realtrack.dto.ProjectTargetDTO;
import com.bh.realtrack.dto.TrendChartRequestDTO;
import com.bh.realtrack.service.OfeBillingDashboardService;
import com.bh.realtrack.util.AssertUtils;

/**
 * @author Radhika Tadas
 */

@CrossOrigin
@RestController
@RequestMapping(value = "api/v1/billing")
public class OfeBillingDashboardController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BillingPortfolioController.class);
	
	@Autowired
	OfeBillingDashboardService billingDashboardService;

	@RequestMapping(value = "/getBillingManageProjects", method = RequestMethod.POST,consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getBillingManageProjects(@RequestBody HeaderDashboardDetailsDTO headerDetails) {
		return billingDashboardService.getBillingManageProjects(headerDetails);
	}
	
	@RequestMapping(value = "/getBusinessUnitSummary", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers ="Accept=application/json") 
	public Map<String, Object> getBusinessUnitSummary(@RequestBody BillingDashboardFilterDTO filterValues) throws Exception {
			 return billingDashboardService.getBusinessUnitSummary(filterValues); 
	}
	
	@RequestMapping(value = "/getBillingKpis", method = RequestMethod.POST, consumes = "application/json",  produces = "application/json",headers = "Accept=application/json")
	public Map<String, Object> getBillingKpis(@RequestBody BillingDashboardFilterDTO filterValues) throws Exception {
		return billingDashboardService.getBillingKpis(filterValues);
	}
	
	@RequestMapping(value = "/getBillingProjectList", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers ="Accept=application/json") 
	public Map<String, Object> getBillingProjectList(@RequestBody BillingDashboardFilterDTO filterValues) throws Exception {
			 return billingDashboardService.getBillingProjectList(filterValues); 
	}

	@RequestMapping(value = "/getBillingCurveList", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers ="Accept=application/json") 
	public Map<String, Object> getBillingCurveList(@RequestBody BillingDashboardFilterDTO filterValues) throws Exception {
			 return billingDashboardService.getBillingCurveList(filterValues); 
	}
	
	@RequestMapping(value = "/getBillingInvoiceList", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers ="Accept=application/json") 
	public Map<String, Object> getBillingInvoiceList(@RequestBody BillingDashboardFilterDTO filterValues) throws Exception {
			 return billingDashboardService.getBillingInvoiceList(filterValues); 
	}
	
	@RequestMapping(value = "/getEditAccess", method = RequestMethod.GET)
	public String getEditAccess(@RequestParam String sso) {
		return billingDashboardService.getEditAccess(sso);
	}
	
	@RequestMapping(value = "/getProjectTargetList", method = RequestMethod.POST,consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getProjectTargetList(@RequestBody HeaderDashboardDetailsDTO headerDetails) {
		return billingDashboardService.getProjectTargetList(headerDetails);
	}
	
	@RequestMapping(value = "/editProjectTargetList", method = RequestMethod.POST,consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> editProjectTargetList(@RequestBody ProjectTargetDTO targetDto) {
		return billingDashboardService.editProjectTargetList(targetDto);
	}
	
	@RequestMapping(value = "/getTrendChart", method = RequestMethod.POST,consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getTrendChart(@RequestBody TrendChartRequestDTO headerDetails) {
		return billingDashboardService.getTrendChart(headerDetails);
	}
	
	@RequestMapping(value = "/getProjectTargetExcel", method = RequestMethod.GET)
	public void getProjectTargetExcel(@RequestParam("company-id") int companyId,
			@RequestParam("business-unit") String business, @RequestParam String segment,@RequestParam int customerId,@RequestParam String region, HttpServletResponse response) throws Exception{
		String fileName = "Project_Target_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] data =  billingDashboardService.getProjectTargetExcel(companyId, business, segment,customerId,region);
			IOUtils.write(data, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing details{}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing details{}" , e.getStackTrace());
			}
		}
	}
	
	@RequestMapping(value = "/getOpenInvoiceChart", method = RequestMethod.GET)
	public Map<String, Object> getOpenInvoiceChart(@RequestParam String projectId) {
		return billingDashboardService.getOpenInvoiceChart(projectId);
	}

	@GetMapping("/getOpenInvoiceChartPopupDetails")
	public Map<String, Object> getOpenInvoiceChartPopupDetails(@RequestParam String projectId,
			@RequestParam String chartType, @RequestParam String statusCode) {
		return billingDashboardService.getOpenInvoiceChartPopupDetails(projectId, chartType, statusCode);
	}

	@GetMapping("/getOpenInvoiceDatatable")
	public Map<String, Object> getOpenInvoiceDatatable(@RequestParam String projectId) {
		return billingDashboardService.getOpenInvoiceDatatable(projectId);
	}

	@RequestMapping(value = "/saveOpenInvoiceDetails", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveOpenInvoiceDetails(@RequestHeader HttpHeaders headers,
			@RequestBody List<OFESaveOpenInvoiceDTO> invoicesList) {
		return billingDashboardService.saveOpenInvoiceDetails(invoicesList);
	}

	@GetMapping("/getCashCollectionReportCurve")
	public Map<String, Object> getCashCollectionReportCurve(@RequestParam String projectId) {
		return billingDashboardService.getCashCollectionReportCurve(projectId);
	}
	
	@RequestMapping(value = "/getOpenInvoiceDatatableExcel", method = RequestMethod.GET)
	public void getOpenInvoiceDatatableExcel(@RequestParam String projectId, HttpServletResponse response) throws Exception{
		String fileName = "Open_Invoice_Datatable.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] data =  billingDashboardService.getOpenInvoiceDatatableExcel(projectId);
			IOUtils.write(data, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing details {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing details{}" , e.getStackTrace());
			}
		}
	}
	
	@RequestMapping(value = "/getForecastChart", method = RequestMethod.GET)
	public Map<String, Object> getForecastChart(@RequestParam String projectId) {
		return billingDashboardService.getForecastChart(projectId);
	}
	
	@RequestMapping(value = "/getForecastExcel", method = RequestMethod.GET)
	public void getForecastExcel(@RequestParam String projectId, HttpServletResponse response) throws Exception{
		String fileName = "forecast_tab.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] data =  billingDashboardService.getForecastExcel(projectId);
			IOUtils.write(data, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading Billing details{}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading Billing details{}" , e.getStackTrace());
			}
		}
	}

	@RequestMapping(value = "/getEditAccessBillingWidget", method = RequestMethod.GET)
	public String getEditAccessBillingWidget(@RequestParam String sso) {
		return billingDashboardService.getEditAccessBillingWidget(sso);
	}
	
	@RequestMapping(value = "/editInvoiceList", method = RequestMethod.POST,consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> editInvoiceList(@RequestBody List<InvoiceListDTO> invoiceDto ) {
		return billingDashboardService.editInvoiceList(invoiceDto);
	}

	@RequestMapping(value = "/editMilestoneList", method = RequestMethod.POST,consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> editMilestoneList(@RequestBody List<MilestoneListDTO> milestoneDto) {
		return billingDashboardService.editMilestoneList(milestoneDto);
	}
	
	@RequestMapping(value = "/getBMVLastRefreshDate", method = RequestMethod.GET)
	public Map<String,Object> getBMVLastRefreshDate() {
		return billingDashboardService.getBMVLastRefreshDate();
	}
	
	@RequestMapping(value = "/scurveFilters", method = RequestMethod.GET)
	public Map<String, Object> getScurvefilters(@RequestParam String projectId,@RequestParam String published) {
		return billingDashboardService.getScurvefilters(projectId,published);
	}
	
	@RequestMapping(value="/getDashboardColorCount", method= RequestMethod.GET)
	public DashboardCountDTO getDashboardColorCount(@RequestParam int customerId,@RequestParam int companyId,@RequestParam String warrantyFlag){
		return billingDashboardService.getDashboardColorCount(customerId,companyId,warrantyFlag);
	}

	@RequestMapping(value = "/downloadOpenInvoiceChartPopupDetailsExcel", method = RequestMethod.GET)
	public void downloadOpenInvoiceChartPopupDetails(@RequestParam String projectId, @RequestParam String chartType,@RequestParam String statusCode,HttpServletResponse response) throws Exception {
		String fileName = "Open_Invoices_Details.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = billingDashboardService.downloadOpenInvoiceChartPopupDetails(projectId,chartType,statusCode);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			LOGGER.error("Error occured when downloading open invoices Chart popup details :: {}" , e.getStackTrace());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading open invoices Chart popup details :: {}" , e.getStackTrace());
			}
		}
	}
	
	@RequestMapping(value = "/getInvoiceDropDownList", method = RequestMethod.GET)
	public Map<String, Object> getInvoiceDropDownList(@RequestParam String projectId) {
		return billingDashboardService.getInvoiceDropDownList(projectId);
	}
}
