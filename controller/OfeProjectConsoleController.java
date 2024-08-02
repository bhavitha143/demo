package com.bh.realtrack.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.service.IOFEProjectConsoleService;

/**
 * @author Subash Darsi
 */

@CrossOrigin
@RestController
@RequestMapping(value= "/api/v1/ofeConsole")
public class OfeProjectConsoleController {
private static final Logger log = LoggerFactory.getLogger(OfeProjectConsoleController.class);
	

	public OfeProjectConsoleController(IOFEProjectConsoleService projectConsoleService) {
	super();
	this.projectConsoleService = projectConsoleService;
		}
	
	IOFEProjectConsoleService projectConsoleService;
	@GetMapping(value="/getProjectDetails")
	public Map<String,Object> getProjectDetails(@RequestParam String projectId){
		return projectConsoleService.getProjectDetails(projectId);
	}

	@GetMapping(value="/getProjectBillingWidgetChartDetails")
	public Map<String,Object> getProjectBillingDetails(@RequestParam String projectId){
		return projectConsoleService.getProjectBillingDetails(projectId);
	}

	@GetMapping(value="/getProjectRiskDetails")
	public Map<String,Object> getProjectRiskDetails(@RequestParam String projectId){
		return projectConsoleService.getProjectRiskDetails(projectId);
	}

	@GetMapping(value="/getProjectDocumentCount")
	public Map<String,Object> getProjectDocumentCount(@RequestParam String projectId){
		return projectConsoleService.getProjectDocumentCount(projectId);
	}
	
	@GetMapping(value="/getProjectDocumentList")
	public Map<String,Object> getProjectDocumentList(@RequestParam String projectId,@RequestParam String status){
		return projectConsoleService.getProjectDocumentList(projectId,status);
	}

	@GetMapping(value="/getPrjConsoleSCurveDetails")
	public Map<String,Object> getPrjConsoleSCurveDetails(@RequestParam String projectId){
		return projectConsoleService.getPrjConsoleSCurveDetails(projectId);
	}

	@GetMapping(value="/getPrjConsoleFinCashDetails")
	public Map<String,Object> getPrjConsoleFinCashDetails(@RequestParam String projectId){
		return projectConsoleService.getPrjConsoleFinCashDetails(projectId);
	}
	
	@GetMapping(value="/getPrjConsoleMrpDetails")
	public Map<String,Object> getPrjConsoleMrpDetails(@RequestParam String projectId){
		return projectConsoleService.getPrjConsoleMrpDetails(projectId);
	}
	
	@GetMapping(value="/getPrjConsoleQualityDetails")
	public Map<String,Object> getPrjConsoleQualityDetails(@RequestParam String projectId){
		return projectConsoleService.getPrjConsoleQualityDetails(projectId);
	}
	
	@GetMapping(value="/getPrjConsoleVor")
	public Map<String,Object> getPrjConsoleVorDetails(@RequestParam String projectId){
		return projectConsoleService.getPrjConsoleVorDetails(projectId);
	}
}
