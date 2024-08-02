package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bh.realtrack.dto.*;
import com.bh.realtrack.excel.ExportLandingProjectDetailsExcel;
import com.bh.realtrack.model.CallerContext;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.OFEDocumentationDAO;
import com.bh.realtrack.service.OFEDocumentationService;

@Service
public class OFEDocumentationServiceImpl implements OFEDocumentationService {

	private static final Logger log = LoggerFactory.getLogger(OFEDocumentationServiceImpl.class);

	@Autowired
	private OFEDocumentationDAO documentationDAO;

	@Autowired
	private CallerContext callerContext;

	@Override
	public Map<String, Object> getDemandVsCapacityFilters(HeaderDashboardDetailsDTO filterValues) {

		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<DropDownDTO> projectIds = new ArrayList<DropDownDTO>();
		List<DropDownDTO> roles = new ArrayList<DropDownDTO>();
		List<DropDownDTO> status = new ArrayList<DropDownDTO>();
		List<DropDownDTO> regions = new ArrayList<DropDownDTO>();
		List<DropDownDTO> locations = new ArrayList<DropDownDTO>();
		List<DropDownDTO> resourseName = new ArrayList<DropDownDTO>();
		List<String> minExcess = new ArrayList<String>();

		projectIds = documentationDAO.getProjectIds(filterValues);
		roles = documentationDAO.getRoles(filterValues);
		resourseName = documentationDAO.getResourseName(filterValues);
		status = documentationDAO.getStatus();
		regions = documentationDAO.getRegion();
		locations = documentationDAO.getlocations();
		minExcess = documentationDAO.getMinExcess();
		data.put("minExcess", minExcess);
		data.put("projectIds", projectIds);
		data.put("roles", roles);
		data.put("resourseName", resourseName);
		data.put("status", status);
		data.put("regions", regions);
		data.put("locations", locations);

		response.put("data", data);
		return response;

	}

	@Override
	public Map<String, Object> getProjectDeckDocumentationKpis(String projectId) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		String otdPerc = null;
		String reworkPerc = null;
		String firstPassYeildPer = null;
		String coustmerReviewPerc = null;
		String totalDocDurationPerc = null;

		otdPerc = documentationDAO.getOtdPerc(projectId);
		reworkPerc = documentationDAO.getReWorkPerc(projectId);
		firstPassYeildPer = documentationDAO.getFirstPassYeildPer(projectId);
		coustmerReviewPerc = documentationDAO.getCoustmerReviewPerc(projectId);
		totalDocDurationPerc = documentationDAO.getTotalDocDurationPerc(projectId);

		data.put("otdPerc", otdPerc);
		data.put("reworkPerc", reworkPerc);
		data.put("firstPassYeildPerc", firstPassYeildPer);
		data.put("coustmerReviewPerc", coustmerReviewPerc);
		data.put("totalDocDurationPerc", totalDocDurationPerc);
		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getOpDocumentationKpis(DocumentationFiltersDTO filterValues) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();

		String otdPerc = null;
		String reworkPerc = null;
		String firstPassYeildPer = null;
		String coustmerReviewPerc = null;
		String totalDocDurationPerc = null;

		otdPerc = documentationDAO.getOtdCount(filterValues);
		reworkPerc = documentationDAO.getReWorkCount(filterValues);
		firstPassYeildPer = documentationDAO.getFirstPassYeildCount(filterValues);
		coustmerReviewPerc = documentationDAO.getCoustmerReviewCount(filterValues);
		totalDocDurationPerc = documentationDAO.getTotalDocDurationCount(filterValues);

		data.put("otdPerc", otdPerc);
		data.put("reworkPerc", reworkPerc);
		data.put("firstPassYeildPerc", firstPassYeildPer);
		data.put("coustmerReviewPerc", coustmerReviewPerc);
		data.put("totalDocDurationPerc", totalDocDurationPerc);
		response.put("data", data);
		return response;
	}

	@Override
	public Map<String, Object> getProjectDeckDocumentationStatus(String projectId, int role) {
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> response = new HashMap<String, Object>();
		List<DocumentationPopupChartDTO> chartPopupList = new ArrayList<DocumentationPopupChartDTO>();
		String totalCount = null;
		String finalizedCount = null;
		String roleId = null;
		
		roleId = documentationDAO.getroleId(role);
		totalCount = documentationDAO.getTotalCount(projectId, roleId);
		finalizedCount = documentationDAO.getFinalizedCount(projectId, roleId);
		chartPopupList = documentationDAO.getProjectDeckDocChartpopup(projectId, roleId);

		data.put("totalCount", totalCount);
		data.put("finalizedCount", finalizedCount);
		data.put("chartPopupList", chartPopupList);
		response.put("data", data);
		return response;
	}

	@Override
	public byte[] downloadOfeMyProjectsExcel(OFEProjectDTO projectList) {
		Map<String, Object> response = new HashMap<String,Object>();
		String sso = callerContext.getName();
		log.info("sso "+sso);
		ExportLandingProjectDetailsExcel excelDto = new ExportLandingProjectDetailsExcel();
		List<MyProjectsExcelDTO> list = new ArrayList<MyProjectsExcelDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			list = documentationDAO.downloadMyProjectExcel(sso,projectList.getCompanyId(),projectList.getCustomerId(),projectList.getWarrantyFlag());
			log.info("list :"+list.size());
			excelDto.exportOfeMyProjectsExcel(workbook, list, projectList.getPreferenceValue());
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading project details excel file :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading project details excel file :: " + e.getMessage());
			}
		}
		return excelData;
	}

}
