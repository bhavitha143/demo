package com.bh.realtrack.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bh.realtrack.dto.CMAnalysisSaveCommentsDTO;
import com.bh.realtrack.service.ICMTrackerService;
import com.bh.realtrack.util.AssertUtils;

/**
 * @author shwsawan
 *
 */

@RestController
@CrossOrigin
public class CMTrackerController {

	private static final Logger log = LoggerFactory.getLogger(CMTrackerController.class);

	@Autowired
	private ICMTrackerService iCMTrackerService;

	@RequestMapping(value = "/getCMAnalysisSummary", method = RequestMethod.GET)
	public Map<String, Object> getCMAnalysisSummary(@RequestParam String projectId,
			@RequestParam String viewContingencyFlag, @RequestParam String viewUnAssignedBudgetFlag,
			@RequestParam String fxImpactOnCostFlag) {
		return iCMTrackerService.getCMAnalysisSummary(projectId, viewContingencyFlag, viewUnAssignedBudgetFlag,
				fxImpactOnCostFlag);
	}

	@RequestMapping(value = "/getCMAnalysisComments", method = RequestMethod.GET)
	public Map<String, Object> getCMAnalysisComments(@RequestParam String projectId, @RequestParam String category) {
		return iCMTrackerService.getCMAnalysisComments(projectId, category);
	}

	@RequestMapping(value = "/saveCMAnalysisComments", method = RequestMethod.POST, produces = "application/json", consumes = "application/json", headers = "Accept=application/json")
	public Map<String, Object> saveCMAnalysisComments(@RequestHeader HttpHeaders headers,
			@RequestBody CMAnalysisSaveCommentsDTO commentsDTO) {
		return iCMTrackerService.saveCMAnalysisComments(commentsDTO);
	}

	@RequestMapping(value = "/getCMAnalysisDropdowns", method = RequestMethod.GET)
	public Map<String, Object> getCMAnalysisDropdowns(@RequestParam String projectId) {
		return iCMTrackerService.getCMAnalysisDropdowns(projectId);
	}

	// Modified by Tushar Chavda
	// Dt : 2022-04-28
	@RequestMapping(value = "/getCmAnalysisExpCatChart", method = RequestMethod.GET)
	public Map<String, Object> getCmAnalysisExpCatChart(@RequestParam String projectId, @RequestParam String subProject,
			@RequestParam String categoryList, @RequestParam String expenditureCategoryFlag,
			@RequestParam String fxImpactOnCostFlag) {
		return iCMTrackerService.getCmAnalysisExpCatChart(projectId, subProject, categoryList, expenditureCategoryFlag,
				fxImpactOnCostFlag);
	}

	@RequestMapping(value = "/getCmAnalysisExpCatChartPopup", method = RequestMethod.GET)
	public Map<String, Object> getCmAnalysisExpCatChartPopup(@RequestParam String projectId,
			@RequestParam String subProject, @RequestParam String expenditureCategoryFlag,
			@RequestParam String category, @RequestParam(required = false) String level,
			@RequestParam(required = false) String expenditureCategory, @RequestParam(required = false) String job,
			@RequestParam(required = false) String task) {
		return iCMTrackerService.getCmAnalysisExpCatChartPopup(projectId, subProject, expenditureCategoryFlag, category,
				level, expenditureCategory, job, task);
	}

	@RequestMapping(value = "/getCMAnalysisTrendChart", method = RequestMethod.GET)
	public Map<String, Object> getCMAnalysisTrendChart(@RequestParam String projectId, @RequestParam String cmFlag) {
		return iCMTrackerService.getCMAnalysisTrendChart(projectId, cmFlag);
	}

	@RequestMapping(value = "/downloadCMAnalysisTrendDetails", method = RequestMethod.GET)
	public void downloadCMAnalysisTrendDetails(@RequestParam final String projectId,
			@RequestHeader final HttpHeaders headers, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String fileName = "CM_Trend_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iCMTrackerService.downloadCMAnalysisTrendDetails(projectId);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading cm analysis trend excel file :: {}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading cm analysis trend excel file :: {}" , e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/downloadCMAnalysisExpCatDetails", method = RequestMethod.GET)
	public void downloadCMAnalysisExpCatDetails(@RequestParam final String projectId, @RequestParam String subProject,
			@RequestParam String expenditureCategoryFlag, @RequestHeader final HttpHeaders headers,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String fileName = "CM_Analysis_ExpCat_Details_" + AssertUtils.sanitizeString(projectId) + ".xlsx";
		response.setContentType("text/xlsx");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
		try {
			byte[] plData = iCMTrackerService.downloadCMAnalysisExpCatDetails(projectId, subProject,
					expenditureCategoryFlag);
			IOUtils.write(plData, response.getOutputStream());
		} catch (Exception e) {
			log.error("Error occured when downloading cm analysis exp cat details excel file :: {}" , e.getMessage());
		} finally {
			try {
			} catch (Exception e) {
				log.error("Error occured when downloading cm analysis exp cat details excel file :: {}" , e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/getCMAnalysisSourceUpdatedDates", method = RequestMethod.GET)
	public Map<String, Object> getCMAnalysisSourceUpdatedDates() {
		return iCMTrackerService.getCMAnalysisSourceUpdatedDates();
	}
}
