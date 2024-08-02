package com.bh.realtrack.controller;


import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.OFEProjectDTO;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import com.bh.realtrack.dto.DemandVsAvailablilityDTO;
import com.bh.realtrack.dto.DocumentationFiltersDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.service.OFEDocumentationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller class for developing API for Demand vs Capacity dashboard.
 * 
 * @author katteja
 *
 */
@RestController
@CrossOrigin
public class OFEDocumentationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(OFEDocumentationController.class);

	@Autowired
	private OFEDocumentationService documentationService;

	@RequestMapping(value = "/getDemandVsCapacityFilters", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getDemandVsCapacityFilters(@RequestBody HeaderDashboardDetailsDTO filterValues) {
		return documentationService.getDemandVsCapacityFilters(filterValues);
	}

	@RequestMapping(value = "/getProjectDeckDocumentationKpis", method = RequestMethod.GET)
	public Map<String, Object> getProjectDeckDocumentationKpis(@RequestParam("projectId") String projectId) {
		return documentationService.getProjectDeckDocumentationKpis(projectId);
	}
	
	@RequestMapping(value = "/getOpDocumentationKpis", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public Map<String, Object> getOpDocumentationKpis(@RequestBody DocumentationFiltersDTO filterValues) {
		return documentationService.getOpDocumentationKpis(filterValues);
	}
	
	@RequestMapping(value = "/getProjectDeckDocumentationStatus", method = RequestMethod.GET)
	public Map<String, Object> getProjectDeckDocumentationStatus(@RequestParam("projectId") String projectId,@RequestParam("role") int role) {
		return documentationService.getProjectDeckDocumentationStatus(projectId,role);
	}

	@RequestMapping(value = "/downloadOfeMyProjectsExcel", method = RequestMethod.POST, consumes = "application/json", produces = "application/json", headers = "Accept=application/json")
	public void downloadOfeMyProjectsExcel(@RequestBody OFEProjectDTO projectList,
										   @RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response ){
		String fileName = "Projects.xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = documentationService.downloadOfeMyProjectsExcel(projectList);
			IOUtils.write(plData,response.getOutputStream());
		}catch (Exception e) {
			LOGGER.error("Error occured when downloading project details excel file :: {}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				LOGGER.error("Error occured when downloading project details excel file :: {}" , e.getMessage());
			}
		}
	}


}
