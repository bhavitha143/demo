package com.bh.realtrack.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.LandingBubbleChartDTO;
import com.bh.realtrack.dto.LandingManageKPIDisplayData;
import com.bh.realtrack.service.OfeLandingDashboardService;

/**
 * @author Tripti Chaurasia
 */

@CrossOrigin
@RestController
@RequestMapping(value= "/api/v1/landing")

public class OfeLandingDashboardController {
	
	private static final Logger log = LoggerFactory.getLogger(OfeLandingDashboardController.class);

	
	@Autowired
	OfeLandingDashboardService landingDashboardService;
	
	@RequestMapping(value="/getLandingManageProjects", method= RequestMethod.POST, consumes= "application/json" , produces= "application/json" , headers = "Accept=application/json")
	public Map<String,Object> getLandingManageProjects(@RequestBody HeaderDashboardDetailsDTO headerDetails){
		return landingDashboardService.getLandingManageProjects(headerDetails);
	}
	
	@RequestMapping(value="/getLandingBillingCount", method=RequestMethod.POST, consumes= "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String,Object> getLandingBillingCount(@RequestBody BillingDashboardFilterDTO filterValues) throws Exception{
		return landingDashboardService.getLandingBillingCount(filterValues);
	}
	
	@RequestMapping(value="/getLandingBubbleChart", method= RequestMethod.POST, consumes= "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String,Object> getLandingBubbleChart(@RequestBody LandingBubbleChartDTO chartValues){
		return landingDashboardService.getLandingBubbleChart(chartValues);
	}
	
	@RequestMapping(value="/getLandingBubbleTrendChart", method=RequestMethod.GET)
	public Map<String,Object> getTrendChartData(@RequestParam String projectId){
		return landingDashboardService.getLandingBubbleTrendChart(projectId);
	}
	
	@RequestMapping(value="/getLandingManageKPIDropdowns", method = RequestMethod.GET)
	public Map<String,Object> getLandingManageKPIDropdowns(@RequestParam("companyId") int companyId){
		return landingDashboardService.getLandingManageKPIDropdowns(companyId);
	}
	
	
	@RequestMapping(value="/saveLandingManageKPI", method = RequestMethod.POST,consumes= "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String,Object> saveManageKPI(@RequestBody LandingManageKPIDisplayData manageKPI){
		return landingDashboardService.saveLandingManageKPI(manageKPI);
	}
	
	@RequestMapping(value="/getLandingManageKPI", method=RequestMethod.GET)
	public Map<String,Object> getLandingManageKPI(@RequestParam("companyId") int companyId ){
		return landingDashboardService.getLandingManageKPI(companyId);
	}
	
	@RequestMapping(value = "/getLandingManageKPIDisplayData", method = RequestMethod.GET)
	public Map<String,Object> getLandingManageKPIDisplayData(@RequestParam("companyId") int companyId,
			@RequestParam("kpiCode") String kpiCode,@RequestParam("businessUnit") String businessUnit){
		return landingDashboardService.getLandingManageKPIDisplayData(companyId, kpiCode, businessUnit);
	}
	
	@RequestMapping(value="/getKPITrendChart", method = RequestMethod.GET)
	public Map<String,Object> getKPITrendChart(@RequestParam("businessUnit") String businessUnit, @RequestParam("kpiCategory") String kpiCategory){
		return landingDashboardService.getKPITrendChart(businessUnit,kpiCategory);
	}
	
	@RequestMapping(value="/getLandingProjectList", method = RequestMethod.POST)
	public Map<String,Object> getLandingProjectList(@RequestBody HeaderDashboardDetailsDTO projectDetails){
		return landingDashboardService.getLandingProjectList(projectDetails);
	}
	
	@RequestMapping(value="/downloadProjectDetails", method = RequestMethod.GET)
	public void downloadProjectDetails(@RequestParam final String projectId,@RequestParam final int companyId,@RequestParam final String businessUnit, 
			@RequestParam final String segment, @RequestParam final String region, @RequestParam final int customerId, 
			@RequestHeader final HttpHeaders headers, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String fileName = "Projects.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = landingDashboardService.downloadProjectDetails(projectId,companyId,businessUnit,
					segment,region,customerId);
			IOUtils.write(plData,response.getOutputStream());
		}catch (Exception e) {
			log.error("Error occured when downloading project details excel file :: {}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading project details excel file :: {}" , e.getMessage());
			}
		}
		
	}

	@PostMapping(value="/getLandingQualityKPI")
	public Map<String,Object> getLandingQualityKPI(@RequestBody BillingDashboardFilterDTO filterValues){
		return landingDashboardService.getLandingQualityKPI(filterValues);
	}
	
}

