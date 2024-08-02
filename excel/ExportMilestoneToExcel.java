/**
 * 
 */
package com.bh.realtrack.excel;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bh.realtrack.dto.ActivitiesDTO;
import com.bh.realtrack.dto.AllMilestonesDTO;

public class ExportMilestoneToExcel {

	public XSSFWorkbook exportAllMilestoneDetailsExcel(final XSSFWorkbook workbook,
			final List<AllMilestonesDTO> milestoneDTOList, String flag) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");

		Sheet sheet = workbook.createSheet("MILESTONE DETAILS");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MILESTONE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BANK GUARANTEE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MILESTONE AMOUNT"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CURR."));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("USD CONV AMOUNT"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BASELINE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST DATE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COMMENTS"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE AMOUNT"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM REQUEST DATE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		if (flag.equalsIgnoreCase("PM")) {
			headerColumnCount = headerColumnCount + 1;
			cell = row.createCell(headerColumnCount);
			cell.setCellValue(("PUBLISHED FORECAST DATE"));
			cell.setCellStyle(getCellHeadStyle(workbook));
		}

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COLLECTION STATUS"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE DUE DATE "));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COLLECTION DATE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CASH COLLECTED "));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OUTSTANDING AMOUNT"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < milestoneDTOList.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell = row.createCell(0);
			cell.setCellValue(milestoneDTOList.get(i).getMilestoneId());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getProrataDesc());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getBankGuaranteeFlag());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getAmount().equalsIgnoreCase("") ? ""
					: NumberFormat.getNumberInstance(Locale.US)
							.format(Double.parseDouble(milestoneDTOList.get(i).getAmount())));
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getCurrency());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getAmountUsd().equalsIgnoreCase("") ? ""
					: NumberFormat.getNumberInstance(Locale.US)
							.format(Double.parseDouble(milestoneDTOList.get(i).getAmountUsd())));
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != milestoneDTOList.get(i).getBaselineDt()
					&& !milestoneDTOList.get(i).getBaselineDt().equalsIgnoreCase("")) {
				Date dt = format.parse(milestoneDTOList.get(i).getBaselineDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != milestoneDTOList.get(i).getForecastDt()
					&& !milestoneDTOList.get(i).getForecastDt().equalsIgnoreCase("")) {
				Date dt = format.parse(milestoneDTOList.get(i).getForecastDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getComments());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getCalcStatus());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getInvoiceNumber());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != milestoneDTOList.get(i).getActualInvoiceDt()
					&& !milestoneDTOList.get(i).getActualInvoiceDt().equalsIgnoreCase("")) {
				Date dt = format.parse(milestoneDTOList.get(i).getActualInvoiceDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getInvoiceAmount().equalsIgnoreCase("") ? ""
					: NumberFormat.getNumberInstance(Locale.US)
							.format(Double.parseDouble(milestoneDTOList.get(i).getInvoiceAmount())));
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != milestoneDTOList.get(i).getPmRequestDt()
					&& !milestoneDTOList.get(i).getPmRequestDt().equalsIgnoreCase("")) {
				Date dt = format.parse(milestoneDTOList.get(i).getPmRequestDt());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			if (flag.equalsIgnoreCase("PM")) {
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTOList.get(i).getPublishedForecastDt());
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getCollectionStatus());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != milestoneDTOList.get(i).getInvoiceDueDate()
					&& !milestoneDTOList.get(i).getInvoiceDueDate().equalsIgnoreCase("")) {
				Date dt = format.parse(milestoneDTOList.get(i).getInvoiceDueDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != milestoneDTOList.get(i).getCollectionDate()
					&& !milestoneDTOList.get(i).getCollectionDate().equalsIgnoreCase("")) {
				Date dt = format.parse(milestoneDTOList.get(i).getCollectionDate());
				cell.setCellValue(dt);
				cell.setCellStyle(dateBodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(getCellBodyStyle(workbook));
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getCashCollected().equalsIgnoreCase("") ? ""
					: NumberFormat.getNumberInstance(Locale.US)
							.format(Double.parseDouble(milestoneDTOList.get(i).getCashCollected())));
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(milestoneDTOList.get(i).getOutstandingAmount().equalsIgnoreCase("") ? ""
					: NumberFormat.getNumberInstance(Locale.US)
							.format(Double.parseDouble(milestoneDTOList.get(i).getOutstandingAmount())));
			cell.setCellStyle(getCellBodyStyle(workbook));

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;
	}

	private CellStyle getCellHeadStyle(final XSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
	}

	private void setBorderStyle(final CellStyle headStyle) {
		headStyle.setBorderBottom(BorderStyle.THIN);
		headStyle.setBorderLeft(BorderStyle.THIN);
		headStyle.setBorderRight(BorderStyle.THIN);
		headStyle.setBorderTop(BorderStyle.THIN);

	}

	private Font getFontHeader(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 10);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.WHITE.getIndex());
		return font;
	}

	private CellStyle getCellBodyStyle(final XSSFWorkbook workbook) {
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.setWrapText(true);
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
	}

	private Font getFontContent(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	private CellStyle getCellDateStyle(final XSSFWorkbook workbook) {
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle bodyStyle = workbook.createCellStyle();
		setBorderStyle(bodyStyle);
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		bodyStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MMM-yyyy"));
		return bodyStyle;
	}

	public XSSFWorkbook exportAllActivitiesExcel(XSSFWorkbook workbook, List<ActivitiesDTO> activityList) {

		Sheet sheet = workbook.createSheet("ACTIVITIES DETAILS");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COSTING PROJECT"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("REF. MILESTONE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MILESTONE DESCRIPTION"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTIVITY ID"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TYPE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DUMMY CODE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ITEM CODE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BASELINE FINISH DATE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL FINISH DATE WITH LAG"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST FINISH DATE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAG DAYS (WORKING DAYS)"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WIP"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("POR"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO NUMBER-LINE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PO ACCEPTANCE"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("RESOURCE NAME"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DOC ISSUE DATE PLANNED REV0"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DOC FIN. DATE PLANNED"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SUPPLIER DOC Y/N"));
		cell.setCellStyle(getCellHeadStyle(workbook));

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < activityList.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getProjectId());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getProjectName());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getCostingProject());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getCashMilestoneActivityId());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getProrataDesc());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getActivityId());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getActivityDesc());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getActivityType());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getDummyCodeRef1());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getItemCode());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getBlFinishDt());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getActualFinishDt());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getForecastFinishDt());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getLagN());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getWip());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getPor());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getPoNumberLine());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getPoAcceptanceFlag());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getResourceName());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getIssuePlannedDt());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getFinishPlannedDt());
			cell.setCellStyle(getCellBodyStyle(workbook));

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(activityList.get(i).getDocSupplier());
			cell.setCellStyle(getCellBodyStyle(workbook));

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;

	}

}
