package com.bh.realtrack.excel;

import java.util.List;

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
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import com.bh.realtrack.dto.CMAnalysisContingencyDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisCostDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisEngDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisLogisticsDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisOtherDetailsDTO;
import com.bh.realtrack.dto.CMAnalysisSupplyChainDetailsDTO;
import com.bh.realtrack.dto.CMTrendDetailsDTO;

public class ExportCMTrackerExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (28 * 1.14388)) * 256;
	int sWidth = ((int) (25 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	private XSSFCellStyle getCellHeadStyle(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 0; // red
		rgb[1] = (byte) 111; // green
		rgb[2] = (byte) 121; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		// headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setWrapText(true);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private XSSFCellStyle getCellHeadStyleGrey(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 148; // red
		rgb[1] = (byte) 148; // green
		rgb[2] = (byte) 148; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		// headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setWrapText(true);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private XSSFCellStyle getCellHeadStyleTeal(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 0; // red
		rgb[1] = (byte) 165; // green
		rgb[2] = (byte) 184; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		// headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setWrapText(true);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private XSSFCellStyle getCellHeadStyleGreen(final SXSSFWorkbook workbook) {
		XSSFCellStyle headStyle = (XSSFCellStyle) workbook.createCellStyle();
		byte[] rgb = new byte[3];
		rgb[0] = (byte) 0; // red
		rgb[1] = (byte) 175; // green
		rgb[2] = (byte) 151; // blue
		XSSFColor xssfColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(xssfColor);
		// headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
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
		font.setFontName("Poppins");
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
		font.setFontName("Poppins");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	public void exportCMTrendDetailsExcel(SXSSFWorkbook workbook, List<CMTrendDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("CM Trend Details");
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("YEAR QUARTER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPENDITURE CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("AS BUDGET"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST BCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL COST"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getYearQuarter());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJob());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpenditureCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostAs() && !list.get(i).getBdjCostAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostBce() && !list.get(i).getBdjCostBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTotalCost() && !list.get(i).getTotalCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTotalCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGo() && !list.get(i).getToGo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportCMAnalysisSupplyChainDetailsExcel(SXSSFWorkbook workbook,
			List<CMAnalysisSupplyChainDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Supply Chain Details");
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		XSSFCellStyle headStyleGrey = getCellHeadStyleGrey(workbook);
		XSSFCellStyle headStyleTeal = getCellHeadStyleTeal(workbook);
		XSSFCellStyle headStyleGreen = getCellHeadStyleGreen(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TASK"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INV ORG"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPENDITURE CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUDGET AS SOLD"));
		cell.setCellStyle(headStyleGrey);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST BCE W/O FX IMPACT ON COST"));
		cell.setCellStyle(headStyleTeal);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FX IMPACT ON COST BCE"));
		cell.setCellStyle(headStyleTeal);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST BCE"));
		cell.setCellStyle(headStyleTeal);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACCUMULATED COST"));
		cell.setCellStyle(headStyleGreen);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COMMITMENTS"));
		cell.setCellStyle(headStyleGreen);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TOGO"));
		cell.setCellStyle(headStyleGreen);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LM CREDIT"));
		cell.setCellStyle(headStyleGreen);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FX IMPACT ON COST"));
		cell.setCellStyle(headStyleGreen);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC"));
		cell.setCellStyle(headStyleGreen);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS AS BUDGET"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS LST BCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DUMMY CODE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MAKE/BUY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PEGGED ITEM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SPOT RATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SWAP RATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CYB QTY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CYB SOURCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CYB SUPPLY TYPE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CYB ORDER NUMBER"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJobNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTask());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemDescription());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getInvOrgCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpenditureCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCttBudgetAs() && !list.get(i).getCttBudgetAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCttBudgetAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getLastBceWOFx() && !list.get(i).getLastBceWOFx().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getLastBceWOFx()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFxCostBCE() && !list.get(i).getFxCostBCE().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getFxCostBCE()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCttLastBce() && !list.get(i).getCttLastBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCttLastBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getAccumulatedCost() && !list.get(i).getAccumulatedCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getAccumulatedCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCommitments() && !list.get(i).getCommitments().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCommitments()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGo() && !list.get(i).getToGo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getLmCredit() && !list.get(i).getLmCredit().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getLmCredit()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getFxImpactOnCost() && !list.get(i).getFxImpactOnCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getFxImpactOnCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacCost() && !list.get(i).getEacCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacVsAsBudget() && !list.get(i).getEacVsAsBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacVsAsBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacVsLastBce() && !list.get(i).getEacVsLastBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacVsLastBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getDummyItemCode());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getItemType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getMakeBuy());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getPeggedItem());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCurrency());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getSpotExchangeRate() && !list.get(i).getSpotExchangeRate().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getSpotExchangeRate()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getSwapExchangeRate() && !list.get(i).getSwapExchangeRate().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getSwapExchangeRate()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCybQuantity() && !list.get(i).getCybQuantity().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCybQuantity()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCybSource());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCybSupplyType());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getCybOrderNumber());
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportCMAnalysisEngDetailsExcel(SXSSFWorkbook workbook, List<CMAnalysisEngDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Engineering Summary");
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPENDITURE CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUDGET AS SOLD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST BCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL COST"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpenditureCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostAs() && !list.get(i).getBdjCostAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostBce() && !list.get(i).getBdjCostBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTotalCost() && !list.get(i).getTotalCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTotalCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGo() && !list.get(i).getToGo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEac() && !list.get(i).getEac().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEac()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportCMAnalysisCostDetailsExcel(SXSSFWorkbook workbook, List<CMAnalysisCostDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Engineering Details");
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ROLE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE INTERNAL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE EXTERNAL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL COST INTERNAL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL COST EXTERNAL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO INTERNAL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO EXTERNAL"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getRole1());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getLatestEstimateInt()
					&& !list.get(i).getLatestEstimateInt().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getLatestEstimateInt()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getLatestEstimateExt()
					&& !list.get(i).getLatestEstimateExt().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getLatestEstimateExt()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualSpent() && !list.get(i).getActualSpent().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getActualSpent()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getJotTot() && !list.get(i).getJotTot().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getJotTot()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGoInt() && !list.get(i).getToGoInt().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGoInt()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGoExt() && !list.get(i).getToGoExt().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGoExt()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportCMAnalysisLogisticsDetailsExcel(SXSSFWorkbook workbook,
			List<CMAnalysisLogisticsDetailsDTO> list) {
		Sheet sheet = workbook.createSheet("Logistics Details");
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPENDITURE CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUDGET AS SOLD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TOTAL COST"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ESTIMATED VOLUME OTR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL VOLUME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROGRESS VOLUME %"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COMMITMENTS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS AS BUDGET"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS LAST BCE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJobNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpenditureCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBudgetAs() && !list.get(i).getBudgetAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBudgetAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getLastBce() && !list.get(i).getLastBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getLastBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTotalCost() && !list.get(i).getTotalCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTotalCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getReferenceVolume() && !list.get(i).getReferenceVolume().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getReferenceVolume()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualVolume() && !list.get(i).getActualVolume().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getActualVolume()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProgress());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCommitments() && !list.get(i).getCommitments().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCommitments()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGo() && !list.get(i).getToGo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEac() && !list.get(i).getEac().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEac()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getDeltaEacVsAs() && !list.get(i).getDeltaEacVsAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getDeltaEacVsAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getDeltaEacVsBce() && !list.get(i).getDeltaEacVsBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getDeltaEacVsBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportCMAnalysisOtherDetailsExcel(SXSSFWorkbook workbook, String sheetName,
			List<CMAnalysisOtherDetailsDTO> list) {
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPENDITURE CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUDGET AS SOLD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TOTAL COST"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COMMITMENTS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS AS BUDGET"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS LAST BCE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJobNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpenditureCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostAs() && !list.get(i).getBdjCostAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostBce() && !list.get(i).getBdjCostBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTotalCost() && !list.get(i).getTotalCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTotalCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCommitments() && !list.get(i).getCommitments().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCommitments()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGo() && !list.get(i).getToGo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEac() && !list.get(i).getEac().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEac()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacVsAsBudget() && !list.get(i).getEacVsAsBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacVsAsBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacVsLastBce() && !list.get(i).getEacVsLastBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacVsLastBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportCMAnalysisContingencyDetailsExcel(SXSSFWorkbook workbook, String sheetName,
			List<CMAnalysisContingencyDetailsDTO> list) {

		Sheet sheet = workbook.createSheet(sheetName);
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CONTINGENCY TASK"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("AS SOLD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST BCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TOTAL RELEASED after LAST BCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, mWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getContingencyTask());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getAsSold() && !list.get(i).getAsSold().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getAsSold()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getLastBce() && !list.get(i).getLastBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getLastBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getTotalReleased() && !list.get(i).getTotalReleased().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getTotalReleased()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEac() && !list.get(i).getEac().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEac()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

	}

	public void exportCMAnalysisServicesDetailsExcel(SXSSFWorkbook workbook, String sheetName,
			List<CMAnalysisOtherDetailsDTO> list) {
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.createFreezePane(0, 1);
		XSSFCellStyle headStyle = getCellHeadStyle(workbook);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(30);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("JOB"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TASK"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EXPENDITURE CATEGORY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BUDGET AS SOLD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST BCE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL COST"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COMMITMENTS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS. AS SOLD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("EAC VS. LAST BCE"));
		cell.setCellStyle(headStyle);

		CellStyle bodyStyle = getCellBodyStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getJobNumber());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getTask());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(list.get(i).getExpenditureCategory());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostAs() && !list.get(i).getBdjCostAs().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostAs()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getBdjCostBce() && !list.get(i).getBdjCostBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getBdjCostBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getActualCost() && !list.get(i).getActualCost().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getActualCost()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getCommitments() && !list.get(i).getCommitments().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getCommitments()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getToGo() && !list.get(i).getToGo().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getToGo()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEac() && !list.get(i).getEac().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEac()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacVsAsBudget() && !list.get(i).getEacVsAsBudget().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacVsAsBudget()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != list.get(i).getEacVsLastBce() && !list.get(i).getEacVsLastBce().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(list.get(i).getEacVsLastBce()));
			} else {
				cell.setBlank();
			}
			cell.setCellStyle(bodyStyle);
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

}
