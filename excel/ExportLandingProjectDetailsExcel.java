package com.bh.realtrack.excel;

import java.util.List;

import com.bh.realtrack.dto.MyProjectsExcelDTO;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.bh.realtrack.dto.LandingProjectDetailsDTO;

public class ExportLandingProjectDetailsExcel {
	
	int sWidth = ((int) (22 * 1.14388)) * 256;
	
	private CellStyle getCellHeadStyle(final SXSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setWrapText(true);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private void setBorderStyle(final CellStyle headStyle) {
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);
	}

	private Font getFontHeader(final SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private CellStyle getCellBodyStyle(final SXSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(final SXSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	public void exportLandingProjectDetailsExcel(SXSSFWorkbook workbook, List<LandingProjectDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Project Details");
		sheet.createFreezePane(0, 1);
		CellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUSINESS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("END USER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT MANAGER"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT VALUE(M USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("VAR TO BL"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("%CM"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("%CM EXPANSION"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OTD-Customer OTD % over Last 12 Months"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OTD-Internal OTD % over Last 12 Months"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Quality-Overdue CIRs"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Quality-Overdue NCRs"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Quality-CoPQ ($)"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Billing-Billing Overdue"));
		cell.setCellStyle(headStyle);	
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Billing-Current QTR LE vs Comm"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Billing-Actual vs LE-CW"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Risk-Current EMV of Open Risks relative to Prj Values(%)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Risk-% of Open Risk Actions that are Overdue"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Risk-Post-Mitig. EMV of Open Risks relative to Prj Values(%)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Opportunities-Current EMV of Open Opp relative to Prj Values(%)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Opportunities-% of Open Opp. Actions that are Overdue"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Opportunities-Post-Mitig. EMV of Open Opp. relative to Prj Values(%)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Documentation-Last 12 Month OTD %"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Documentation-% Rework"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Documentation-Median Customer Review (Days)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Changes-In Process Change Amt relative to Prj Values (%)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Changes-Approved Change Amt relative to Prj Value (%)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("Changes-Final CM% Approved Changes"));
		cell.setCellStyle(headStyle);


		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBusiness());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getSegment());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getEndUser());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRegion());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectManager());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectValue());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getVarToBL());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCm());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCmExpansion());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if(list.get(i).getOtdCustOtdOverLast12Months().equals("NA")) {
				cell.setBlank();
			} else {
				cell.setCellValue(list.get(i).getOtdCustOtdOverLast12Months());
			}
			cell.setCellStyle(bodyStyle);
				
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if(list.get(i).getOtdInternalOTDOverLast12Months().equals("NA")) {
				cell.setBlank();
			} else {
				cell.setCellValue(list.get(i).getOtdInternalOTDOverLast12Months());
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getQualityOverdueCIR());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getQualityOverdueNCR());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getQualityCoPQ());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBillingOverdue());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBillingCurrentQuarterLE());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getBillingActualVsLE());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRiskCurrentEMVOfOpenRisks());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRiskOpenRiskActionsOverdue());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRiskPostMitigationEMVOfOpenRisk());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getOppCurrentEMVOfOpenOpp());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getOppOpenOppActionOverdue());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getOppPostMitigationEMVOfOpenOpp());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getDocLast12MonthOTD());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if(list.get(i).getDocRework().equals("NA")) {
				cell.setBlank();
			} else {
				cell.setCellValue(list.get(i).getDocRework());
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if(list.get(i).getDocMedianCustomerReview().equals("NA")) {
				cell.setBlank();
			} else {
				cell.setCellValue(list.get(i).getDocMedianCustomerReview());
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getChangesInProcessChangeAmount());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getChangesApprovedChangeAmount());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getChangesFinalCMApprovedChanges());
			cell.setCellStyle(bodyStyle);
			
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportOfeMyProjectsExcel(SXSSFWorkbook workbook, List<MyProjectsExcelDTO> list, String preferenceValues) {
		Sheet sheet = workbook.createSheet("Sheet 1");
		sheet.createFreezePane(0, 1);
		CellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		System.out.println(preferenceValues);
		if(preferenceValues.contains("favLink")) {
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("FAV"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("projLink")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PROJECT NO"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("projectName")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PROJECT NAME"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("businessUnit")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("BUSINESS"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("segment")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("SEGMENT"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("customerName")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("END USER"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("region")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("REGION"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("projectManager")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PROJECT MANAGER"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("po")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PROJECT VALUE(M$)"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("hseColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("HSE"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("customerHealthColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("CUSTOMER HEALTH"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("actionsColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("ACTIONS & ESCALATIONS"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("qualityColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("QUALITY"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("schedule")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("SCHEDULE"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("contractColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("CONTRACT"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("riskColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("RISK"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("financeColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("FINANCIALS"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("documentColor")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("DOC MANAGEMENT"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("customerOtd")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("CUSTOMER OTD TREND"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("customerOtd")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("CUSTOMER OTD %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("internalOtd")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("INTERNAL OTD TREND"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("internalOtd")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("INTERNAL OTD %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("cmAsSold")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("CM AS %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("cmAsActual")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("CM AD %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("deltaCm")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("DELTA CM % vs PRIOR PMR TREND"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("deltaCm")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("DELTA CM % vs PRIOR PMR"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("billed")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("BILLED %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("overallPp")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PP %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("overallAp")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("POC %"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("highlights")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("HIGHLIGHTS"));
			cell.setCellStyle(headStyle);
		}

		if(preferenceValues.contains("publishDate")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PUBLISH DATE"));
			cell.setCellStyle(headStyle);
		}

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			if(preferenceValues.contains("favLink")) {
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getFavorite());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("projLink")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getProjectId());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("projectName")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getProjectName());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("businessUnit")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getBusiness());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("segment")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getSegment());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("customerName")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getEndUser());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("region")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getRegion());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("projectManager")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getProjectManager());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("po")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getProjectValue());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("hseColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getHse());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("customerHealthColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getCustomerHealth());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("actionsColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getActionsAndEscalations());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("qualityColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getQuality());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("schedule")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getSchedule());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("contractColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getContract());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("riskColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getRisk());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("financeColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getFinancials());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("documentColor")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getDocManagement());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("customerOtd")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getCustomerOTDTrend());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("customerOtd")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if(list.get(i).getCustomerOTDPercent().equals("NA")) {
					cell.setBlank();
				} else {
					cell.setCellValue(list.get(i).getCustomerOTDPercent());
				}
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("internalOtd")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getInternalOTDTrend());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("internalOtd")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if(list.get(i).getInternalOTDPercent().equals("NA")) {
					cell.setBlank();
				} else {
					cell.setCellValue(list.get(i).getInternalOTDPercent());
				}
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("cmAsSold")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getCmAsPercent());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("cmAsActual")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getCmADPercent());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("deltaCm")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getDeltaCMTrend());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("deltaCm")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getDeltaCMPercent());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("billed")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getBilledPercent());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("overallPp")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getPpPercent());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("overallAp")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getPocPercent());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("highlights")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getHighlights());
				cell.setCellStyle(bodyStyle);
			}

			if(preferenceValues.contains("publishDate")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(list.get(i).getPublishDate());
				cell.setCellStyle(bodyStyle);
			}

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	}
