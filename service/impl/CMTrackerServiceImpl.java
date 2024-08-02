package com.bh.realtrack.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.realtrack.dao.ICMTrackerDAO;
import com.bh.realtrack.dto.CMAnalysisCommentsDTO;
import com.bh.realtrack.dto.CMAnalysisContingencyDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisCostDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisEngDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisLogisticsDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisOtherDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSaveCommentsDTO;
import com.bh.realtrack.dto.CMAnalysisSummaryDTO;
import com.bh.realtrack.dto.CMAnalysisSummaryDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSupplyChainDetailsDTO;
import com.bh.realtrack.dto.CMTrendDetailsDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.SourceUpdatesDTO;
import com.bh.realtrack.dto.YAxisCMDTO;
import com.bh.realtrack.excel.ExportCMTrackerExcel;
import com.bh.realtrack.model.CallerContext;
import com.bh.realtrack.service.ICMTrackerService;

/**
 * @author shwsawan
 *
 */

@Service
public class CMTrackerServiceImpl implements ICMTrackerService {

	private static final Logger log = LoggerFactory.getLogger(CMTrackerServiceImpl.class);

	private ICMTrackerDAO iCMTrackerDAO;
	private CallerContext callerContext;

	@Autowired
	public CMTrackerServiceImpl(ICMTrackerDAO iCMTrackerDAO, CallerContext callerContext) {
		this.iCMTrackerDAO = iCMTrackerDAO;
		this.callerContext = callerContext;
	}

	@Override
	public Map<String, Object> getCMAnalysisSummary(String projectId, String viewContingencyFlag,
			String viewUnAssignedBudgetFlag, String fxImpactOnCostFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		CMAnalysisSaveCommentsDTO commentsDTO = new CMAnalysisSaveCommentsDTO();
		CMAnalysisSummaryDetailsDTO summaryDTO = new CMAnalysisSummaryDetailsDTO();
		CMAnalysisSummaryDTO asSoldDTO = new CMAnalysisSummaryDTO();
		CMAnalysisSummaryDTO lastBceDTO = new CMAnalysisSummaryDTO();
		CMAnalysisSummaryDTO eacDTO = new CMAnalysisSummaryDTO();
		String fxImpact = "", lastBCEComment = "", lastEACComment = "", updatedOn = "", contingency91 = "",
				contingency92 = "", availableContingency91 = "", availableContingency92 = "",
				toggleMessageContingeny91 = "", toggleMessageContingeny92 = "";
		boolean toggleFlagContingeny91 = true, toggleFlagContingeny92 = true;
		try {
			if (projectId != null) {

				summaryDTO = iCMTrackerDAO.getCMAnalysisSummary(projectId, viewContingencyFlag,
						viewUnAssignedBudgetFlag, fxImpactOnCostFlag);
				updatedOn = iCMTrackerDAO.getCMAnalysisUpdatedOn(projectId);

				commentsDTO = iCMTrackerDAO.getCMAnalysisLatestComments(commentsDTO, projectId, "EAC");
				commentsDTO = iCMTrackerDAO.getCMAnalysisLatestComments(commentsDTO, projectId, "BCE");

				asSoldDTO.setSales(summaryDTO.getSalesAsSold());
				asSoldDTO.setBudget(summaryDTO.getBudgetCostAsSold());
				asSoldDTO.setCm(summaryDTO.getCmAsSold());
				asSoldDTO.setCmPer(summaryDTO.getCmPerAsSold());

				lastBceDTO.setSales(summaryDTO.getSalesBCE());
				lastBceDTO.setBudget(summaryDTO.getBudgetCostBCE());
				lastBceDTO.setCm(summaryDTO.getCmBCE());
				lastBceDTO.setCmPer(summaryDTO.getCmPerBCE());
				lastBceDTO.setLastBCEDate(summaryDTO.getLastBCEDate());

				eacDTO.setSales(summaryDTO.getSalesEAC());
				eacDTO.setEac(summaryDTO.getEac());
				eacDTO.setCm(summaryDTO.getCmEAC());
				eacDTO.setCmPer(summaryDTO.getCmPerEAC());

				lastBCEComment = commentsDTO.getBceComments();
				lastEACComment = commentsDTO.getEacComments();

				fxImpact = summaryDTO.getFxCost();
				contingency91 = summaryDTO.getContingency91();
				contingency92 = summaryDTO.getContingency92();
				availableContingency91 = summaryDTO.getContingencyValueBCE_9_1();
				availableContingency92 = summaryDTO.getContingencyValueBCE_9_2();

				if (null != contingency91 && !contingency91.isEmpty() && contingency91.equalsIgnoreCase("0")) {
					toggleFlagContingeny91 = false;
					if (null != availableContingency91 && !availableContingency91.isEmpty()
							&& availableContingency91.equalsIgnoreCase("0")) {
						toggleMessageContingeny91 = "Contingency not available.";
					} else {
						toggleMessageContingeny91 = "Estimated At Completion CM% greater or equal to Last BCE CM%";
					}
				}

				if (null != contingency92 && !contingency92.isEmpty() && contingency92.equalsIgnoreCase("0")) {
					toggleFlagContingeny92 = false;
					if (null != availableContingency92 && !availableContingency92.isEmpty()
							&& availableContingency92.equalsIgnoreCase("0")) {
						toggleMessageContingeny92 = "Contingency not available.";
					} else {
						toggleMessageContingeny92 = "Estimated At Completion CM% greater or equal to Last BCE CM%";
					}
				}

				if (null != viewContingencyFlag && !viewContingencyFlag.isEmpty()
						&& viewContingencyFlag.equalsIgnoreCase("Y")) {
					if (null != contingency91 && !contingency91.isEmpty() && null != availableContingency91
							&& !availableContingency91.isEmpty()) {
						availableContingency91 = String.valueOf(Integer.parseInt(summaryDTO.getContingency91())
								+ Integer.parseInt(summaryDTO.getContingencyValueBCE_9_1()));
					}

				}

				if (null != viewUnAssignedBudgetFlag && !viewUnAssignedBudgetFlag.isEmpty()
						&& viewUnAssignedBudgetFlag.equalsIgnoreCase("Y")) {
					if (null != contingency92 && !contingency92.isEmpty() && null != availableContingency92
							&& !availableContingency92.isEmpty()) {
						availableContingency92 = String.valueOf(Integer.parseInt(summaryDTO.getContingency92())
								+ Integer.parseInt(summaryDTO.getContingencyValueBCE_9_2()));
					}
				}

				responseMap.put("asSold", asSoldDTO);
				responseMap.put("lastBCE", lastBceDTO);
				responseMap.put("estimateAtCompletion", eacDTO);
				responseMap.put("contigency91", contingency91);
				responseMap.put("contigency92", contingency92);
				responseMap.put("fxImpact", fxImpact);
				responseMap.put("lastBCEComment", lastBCEComment);
				responseMap.put("lastEACComment", lastEACComment);
				responseMap.put("updatedOn", updatedOn);
				responseMap.put("contingencyValueBCE_9_1", summaryDTO.getContingencyValueBCE_9_1());
				responseMap.put("contingencyValueBCE_9_2", summaryDTO.getContingencyValueBCE_9_2());
				responseMap.put("updatedDate", summaryDTO.getUpdatedDate());
				responseMap.put("fxCostBce", summaryDTO.getFxCostBce());
				responseMap.put("toggleFlagContingeny91", toggleFlagContingeny91);
				responseMap.put("toggleFlagContingeny92", toggleFlagContingeny92);
				responseMap.put("toggleMessageContingeny91", toggleMessageContingeny91);
				responseMap.put("toggleMessageContingeny92", toggleMessageContingeny92);

				// Modified by Tushar Chavda
				// Dt : 2022-07-01
				responseMap.put("contingency95", summaryDTO.getContingency95());
				responseMap.put("warningFlag", summaryDTO.getWarningFlag());
				if (summaryDTO.getWarningFlag().equalsIgnoreCase("Y")) {
					responseMap.put("warningMessage", "Existing Non-NP scope not accounted in CM Analysis");
				} else {
					responseMap.put("warningMessage", "");
				}

			} else {
				throw new Exception("Error getting cm analysis summary for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCMAnalysisSummary(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCMAnalysisComments(String projectId, String category) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<CMAnalysisCommentsDTO> list = new ArrayList<CMAnalysisCommentsDTO>();
		try {
			if (projectId != null) {
				list = iCMTrackerDAO.getCMAnalysisComments(projectId, category);
				responseMap.put("data", list);
			} else {
				throw new Exception("Error getting cm analysis comments for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCMAnalysisComments(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> saveCMAnalysisComments(CMAnalysisSaveCommentsDTO commentsDTO) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		boolean resultFlag = false;
		String sso = callerContext.getName();
		try {
			if (null != commentsDTO.getProjectId()) {
				if ((null != commentsDTO.getBceComments() && !commentsDTO.getBceComments().isEmpty())
						|| (null != commentsDTO.getEacComments() && !commentsDTO.getEacComments().isEmpty())) {
					if (null != commentsDTO.getBceComments() && !commentsDTO.getBceComments().isEmpty()) {
						resultFlag = iCMTrackerDAO.saveCMAnalysisBCEComments(commentsDTO, sso);
					}
					if (null != commentsDTO.getEacComments() && !commentsDTO.getEacComments().isEmpty()) {
						resultFlag = iCMTrackerDAO.saveCMAnalysisEACComments(commentsDTO, sso);
					}
					if (resultFlag) {
						responseMap.put("status", "success");
						responseMap.put("message", "Data saved successfully.");
					} else {
						responseMap.put("status", "Error");
						responseMap.put("message", "Error in saving data.");
					}
				} else {
					responseMap.put("status", "Error");
					responseMap.put("message", "Please enter atleast one comment.");
				}
			}
		} catch (Exception e) {
			responseMap.put("status", "Error");
			responseMap.put("message", "Error in saving data.");
			log.error("saveCMAnalysisComments(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCMAnalysisDropdowns(String projectId) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<DropDownDTO> subProjectFilter = new ArrayList<DropDownDTO>();

		// Added by Tushar Chavda
		// Dt : 2022-04-28
		List<DropDownDTO> areaCategoryFilter = new ArrayList<DropDownDTO>();
		List<DropDownDTO> expenditureCategoryFilter = new ArrayList<DropDownDTO>();

		try {
			if (projectId != null) {
				subProjectFilter = iCMTrackerDAO.getCMAnalysisSubProjectFilter(projectId);

				// Added by Tushar Chavda
				// Dt : 2022-04-28
				areaCategoryFilter = iCMTrackerDAO.getAreaCategory(projectId);
				expenditureCategoryFilter = iCMTrackerDAO.getExpenditureCategory(projectId);

				responseMap.put("areaCategoty", areaCategoryFilter);
				responseMap.put("expenditureCategory", expenditureCategoryFilter);

				responseMap.put("subProject", subProjectFilter);
			} else {
				throw new Exception("Error getting cm analysis dropdowns for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCMAnalysisDropdowns(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	// Modified by Tushar Chavda
	// Dt : 2022-04-28
	@Override
	public Map<String, Object> getCmAnalysisExpCatChart(String projectId, String subProject, String categoryList,
			String expenditureCategoryFlag, String fxImpactOnCostFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> chartData = new HashMap<String, Object>();
		String updatedOn = "";
		try {
			if (projectId != null) {
				updatedOn = iCMTrackerDAO.getCmAnalysisExpCatUpdatedOn(projectId);
				if (null != expenditureCategoryFlag && !expenditureCategoryFlag.isEmpty()
						&& !expenditureCategoryFlag.equalsIgnoreCase("")) {
					if (expenditureCategoryFlag.equalsIgnoreCase("Y")) {
						chartData = iCMTrackerDAO.getCmAnalysisExpCatChart(projectId, subProject, categoryList,
								expenditureCategoryFlag, fxImpactOnCostFlag);
					} else if (expenditureCategoryFlag.equalsIgnoreCase("N")) {
						chartData = iCMTrackerDAO.getCmAnalysisAreaChart(projectId, subProject, categoryList,
								expenditureCategoryFlag, fxImpactOnCostFlag);
					}
				}
				responseMap.put("chartData", chartData);
				responseMap.put("updatedOn", updatedOn);
			} else {
				throw new Exception("Error getting cm analysis expenditure category chart for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCmAnalysisExpCatChart(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCmAnalysisExpCatChartPopup(String projectId, String subProject,
			String expenditureCategoryFlag, String category, String level, String expenditureCategory, String job,
			String task) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> engDetailsMap = new HashMap<String, Object>();
		List<CMAnalysisSupplyChainDetailsDTO> supplyChainList = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		List<CMAnalysisEngDetailsDTO> engDetailsList = new ArrayList<CMAnalysisEngDetailsDTO>();
		List<CMAnalysisCostDetailsDTO> costDetailsList = new ArrayList<CMAnalysisCostDetailsDTO>();
		List<CMAnalysisLogisticsDetailsDTO> logisticsDetailsList = new ArrayList<CMAnalysisLogisticsDetailsDTO>();
		List<CMAnalysisOtherDetailsDTO> otherDetailsList = new ArrayList<CMAnalysisOtherDetailsDTO>();
		List<CMAnalysisOtherDetailsDTO> serviceDetailsList = new ArrayList<CMAnalysisOtherDetailsDTO>();
		List<CMAnalysisSupplyChainDetailsDTO> supplyChainTaskTableList = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();

		try {
			if (projectId != null) {
				if (null != category && !category.isEmpty() && !category.equalsIgnoreCase("")) {
					responseMap.put("category", category);
					if (category.equalsIgnoreCase("Supply Chain")) {
						if (null != level && !level.isEmpty() && !level.equalsIgnoreCase("")) {
							if (level.equalsIgnoreCase("Project")) {
								supplyChainList = iCMTrackerDAO.getCmAnalysisSupplyChainProjectDetails(projectId,
										subProject, expenditureCategoryFlag, category);
							} else if (level.equalsIgnoreCase("Job")) {
								supplyChainList = iCMTrackerDAO.getCmAnalysisSupplyChainJobDetails(projectId,
										subProject, expenditureCategoryFlag, category, expenditureCategory);
							} else if (level.equalsIgnoreCase("Task")) {
								supplyChainList = iCMTrackerDAO.getCmAnalysisSupplyChainTaskDetails(projectId,
										subProject, expenditureCategoryFlag, category, expenditureCategory, job);
							} else if (level.equalsIgnoreCase("Item")) {
								supplyChainList = iCMTrackerDAO.getCmAnalysisSupplyChainItemDetails(projectId,
										subProject, expenditureCategoryFlag, category, expenditureCategory, job, task);
							}
							supplyChainTaskTableList = iCMTrackerDAO.exportCmAnalysisSupplyChainTaskDetails(projectId,
									subProject, expenditureCategoryFlag, "Supply Chain");
							responseMap.put(category, supplyChainList);
							responseMap.put("list", supplyChainTaskTableList);
						}
					} else if (category.equalsIgnoreCase("Engineering")) {
						engDetailsList = iCMTrackerDAO.getCmAnalysisEngDetails(projectId, subProject,
								expenditureCategoryFlag, category);
						costDetailsList = iCMTrackerDAO.getCmAnalysisCostDetails(projectId, subProject,
								expenditureCategoryFlag, category);
						engDetailsMap.put("engDetails", engDetailsList);
						engDetailsMap.put("costDetails", costDetailsList);
						responseMap.put(category, engDetailsMap);
					} else if (category.equalsIgnoreCase("Logistics")) {
						logisticsDetailsList = iCMTrackerDAO.getCmAnalysisLogisticsDetails(projectId, subProject,
								expenditureCategoryFlag, category);
						responseMap.put(category, logisticsDetailsList);
					} else if (category.equalsIgnoreCase("String Test") || category.equalsIgnoreCase("Other")) {
						otherDetailsList = iCMTrackerDAO.getCmAnalysisOtherDetails(projectId, subProject,
								expenditureCategoryFlag, category);
						responseMap.put(category, otherDetailsList);
					} else if (category.equalsIgnoreCase("Services")) {
						serviceDetailsList = iCMTrackerDAO.getCmAnalysisServicesDetails(projectId, subProject,
								expenditureCategoryFlag, category);
						responseMap.put(category, serviceDetailsList);
					}

				}
			}
		} catch (Exception e) {
			log.error("getCmAnalysisExpCatChartPopup(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public Map<String, Object> getCMAnalysisTrendChart(String projectId, String cmFlag) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		Map<String, Object> chartData = new HashMap<String, Object>();
		YAxisCMDTO yaxisData = new YAxisCMDTO();
		String updatedOn = "";
		try {
			if (projectId != null) {
				updatedOn = iCMTrackerDAO.getCMAnalysisTrendChartUpdatedOn(projectId);
				chartData = iCMTrackerDAO.getCMAnalysisTrendChart(projectId, cmFlag);
				yaxisData = iCMTrackerDAO.getCMAnalysisTrendChartYaxis(projectId, cmFlag);
				responseMap.put("data", chartData);
				responseMap.put("updatedOn", updatedOn);
				responseMap.put("yaxis", yaxisData);
			} else {
				throw new Exception("Error getting cm analysis trend chart for : " + projectId);
			}
		} catch (Exception e) {
			log.error("getCMAnalysisTrendChart(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

	@Override
	public byte[] downloadCMAnalysisTrendDetails(String projectId) {
		ExportCMTrackerExcel excelDto = new ExportCMTrackerExcel();
		List<CMTrendDetailsDTO> list = new ArrayList<CMTrendDetailsDTO>();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			list = iCMTrackerDAO.getCMAnalysisTrendExcelDetails(projectId);
			excelDto.exportCMTrendDetailsExcel(workbook, list);
			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading cm analysis trend excel file :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading cm analysis trend excel file :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public byte[] downloadCMAnalysisExpCatDetails(String projectId, String subProject, String expenditureCategoryFlag) {
		ExportCMTrackerExcel excelDto = new ExportCMTrackerExcel();
		List<CMAnalysisSupplyChainDetailsDTO> supplyChainList = new ArrayList<CMAnalysisSupplyChainDetailsDTO>();
		List<CMAnalysisEngDetailsDTO> engDetailsList = new ArrayList<CMAnalysisEngDetailsDTO>();
		List<CMAnalysisCostDetailsDTO> costDetailsList = new ArrayList<CMAnalysisCostDetailsDTO>();
		List<CMAnalysisLogisticsDetailsDTO> logisticsDetailsList = new ArrayList<CMAnalysisLogisticsDetailsDTO>();
		List<CMAnalysisOtherDetailsDTO> servicesDetailsList = new ArrayList<CMAnalysisOtherDetailsDTO>();
		List<CMAnalysisOtherDetailsDTO> stringTestDetailsList = new ArrayList<CMAnalysisOtherDetailsDTO>();
		List<CMAnalysisOtherDetailsDTO> otherDetailsList = new ArrayList<CMAnalysisOtherDetailsDTO>();
		List<CMAnalysisContingencyDetailsDTO> contingencyDetailsList = new ArrayList<CMAnalysisContingencyDetailsDTO>();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		SXSSFWorkbook workbook = new SXSSFWorkbook();
		byte[] excelData = null;
		try {
			supplyChainList = iCMTrackerDAO.exportCmAnalysisSupplyChainDetails(projectId, subProject,
					expenditureCategoryFlag, "Supply Chain");
			engDetailsList = iCMTrackerDAO.getCmAnalysisEngDetails(projectId, subProject, expenditureCategoryFlag,
					"Engineering");
			costDetailsList = iCMTrackerDAO.getCmAnalysisCostDetails(projectId, subProject, expenditureCategoryFlag,
					"Engineering");
			logisticsDetailsList = iCMTrackerDAO.getCmAnalysisLogisticsDetails(projectId, subProject,
					expenditureCategoryFlag, "Logistics");
			servicesDetailsList = iCMTrackerDAO.getCmAnalysisServicesDetails(projectId, subProject,
					expenditureCategoryFlag, "Services");
			stringTestDetailsList = iCMTrackerDAO.getCmAnalysisOtherDetails(projectId, subProject,
					expenditureCategoryFlag, "String Test");
			otherDetailsList = iCMTrackerDAO.getCmAnalysisOtherDetails(projectId, subProject, expenditureCategoryFlag,
					"Other");
			contingencyDetailsList = iCMTrackerDAO.getCmAnalysisContingencyDetails(projectId, expenditureCategoryFlag,
					"Contingency");

			log.info("Creating Supply Chain Details Sheet with " + supplyChainList.size() + " rows.");
			excelDto.exportCMAnalysisSupplyChainDetailsExcel(workbook, supplyChainList);

			log.info("Creating Eng Details Sheet with " + engDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisEngDetailsExcel(workbook, engDetailsList);

			log.info("Creating Cost Details Sheet with " + costDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisCostDetailsExcel(workbook, costDetailsList);

			log.info("Creating Logistics Details Sheet with " + logisticsDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisLogisticsDetailsExcel(workbook, logisticsDetailsList);

			log.info("Creating Services Details Sheet with " + servicesDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisServicesDetailsExcel(workbook, "Services Details", servicesDetailsList);

			log.info("Creating String Test Details Sheet with " + stringTestDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisOtherDetailsExcel(workbook, "String Test Details", stringTestDetailsList);

			log.info("Creating Other Details Sheet with " + otherDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisOtherDetailsExcel(workbook, "Other Details", otherDetailsList);

			log.info("Creating Contingency Details Sheet with " + contingencyDetailsList.size() + " rows.");
			excelDto.exportCMAnalysisContingencyDetailsExcel(workbook, "Contingency Details", contingencyDetailsList);

			workbook.write(bos);
			excelData = bos.toByteArray();
		} catch (Exception e) {
			log.error("Error occured when downloading cm analysis exp cat details excel file :: " + e.getMessage());
		} finally {
			try {
				bos.close();
				workbook.close();
			} catch (IOException e) {
				log.error("Error occured when downloading cm analysis exp cat details excel file :: " + e.getMessage());
			}
		}
		return excelData;
	}

	@Override
	public Map<String, Object> getCMAnalysisSourceUpdatedDates() {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		List<SourceUpdatesDTO> sourceUpdates = new ArrayList<SourceUpdatesDTO>();
		try {
			sourceUpdates = iCMTrackerDAO.getCMAnalysisSourceUpdatedDates();
			responseMap.put("data", sourceUpdates);
		} catch (Exception e) {
			log.error("getCMAnalysisSourceUpdatedDates(): Exception occurred : " + e.getMessage());
		}
		return responseMap;
	}

}
