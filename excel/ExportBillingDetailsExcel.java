package com.bh.realtrack.excel;

import java.awt.Color;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.IndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bh.realtrack.dto.AtCapacityDTO;
import com.bh.realtrack.dto.BillingLinearityChartPopupDTO;
import com.bh.realtrack.dto.BillingMilestoneListDTO;
import com.bh.realtrack.dto.BillingProjectListDTO;
import com.bh.realtrack.dto.ExceptionCategoryDTO;
import com.bh.realtrack.dto.ExceptionDTO;
import com.bh.realtrack.dto.ForecastChartDTO;
import com.bh.realtrack.dto.OFEOpenInvoiceDataTableDTO;
import com.bh.realtrack.dto.OFETeTProjectTableDTO;
import com.bh.realtrack.dto.OFETeTProjectTableMonthDTO;
import com.bh.realtrack.dto.ProjectTargetDTO;
import com.bh.realtrack.dto.SegmentSummaryDTO;
import com.bh.realtrack.dto.TPSBillingOutOfRTProjectDetailsDTO;
import com.bh.realtrack.dto.ViewInvoiceDTO;

public class ExportBillingDetailsExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (22 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	public XSSFWorkbook exportDetailsTableExcel(final XSSFWorkbook workbook, List<ViewInvoiceDTO> invoiceList,
			List<ExceptionDTO> exceptionList) {

		Sheet sheet = workbook.createSheet("Invoice Details");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int dataCoulmnCount = 0;
		int invoiceColumnInt = 0;
		int exceptionColumnInt = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Project ID"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Project Name"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("PM Name"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("PM Ldr Name"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Business"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Segment"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Milestone ID"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Description"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Actual Invoice Y-FW"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Invoice no."));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Latest Predecessor Actual Finish Date w/Lag"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("PM Request Date"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Invoice Date"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Billing CT"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("PM Request CT"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Invoice Issue CT"));
		cell.setCellStyle(headStyle);

		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Billing Cycle RCA"));
		cell.setCellStyle(headStyle);
		
		invoiceColumnInt = invoiceColumnInt + 1;
		cell = row.createCell(invoiceColumnInt);
		cell.setCellValue(("Billing Cycle RCA Comment"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != invoiceList) {

			for (ViewInvoiceDTO viewInvoiceDTO : invoiceList) {
				row = sheet.createRow(rowNum++);
				dataCoulmnCount = 0;

				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getProjectId());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getMasterProjectName());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getPmName());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getPmLeaderName());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getBusinessUnit());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getSegment());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getCashMilestoneActivityId());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getMilestoneDesc());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getActualInvoiceYFw());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getInvoiceNo());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getMaxActualFinishDt());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getPmRequestDt());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getActualInvoiceDt());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getBillingCT());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getPmRequestCT());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getInvoiceIssueCT());
				cell.setCellStyle(bodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getRemarks());
				cell.setCellStyle(bodyStyle);
				
				dataCoulmnCount = dataCoulmnCount + 1;
				cell = row.createCell(dataCoulmnCount);
				cell.setCellValue(viewInvoiceDTO.getRemarksComments());
				cell.setCellStyle(bodyStyle);
			}
		}

		Sheet sheet1 = workbook.createSheet("Exception Details");
		sheet1.createFreezePane(0, 1);
		int eReqrowNum = 1;
		Row eReqrow = null;
		Cell eReqcell = null;

		eReqrow = sheet1.createRow(0);
		eReqrow.setHeightInPoints(60);
		CellStyle eReqheadStyle = getCellHeadStyle(workbook);
		CellStyle eReqbodyStyle = getCellBodyStyle(workbook);

		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Project ID"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Project Name"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("PM Name"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("PM Ldr Name"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Bussiness"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Segment"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Reason for Exception"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Milestone ID"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Description"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Latest Predecessor Actual Finish Date w/Lag"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("PM Request Date"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Invoice Date"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("invoice"));
		eReqcell.setCellStyle(eReqheadStyle);

		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Actual Invoice Y-FW"));
		eReqcell.setCellStyle(eReqheadStyle);
		
		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Exceptions RCA"));
		eReqcell.setCellStyle(eReqheadStyle);
		
		exceptionColumnInt = exceptionColumnInt + 1;
		eReqcell = eReqrow.createCell(exceptionColumnInt);
		eReqcell.setCellValue(("Exceptions RCA Comment"));
		eReqcell.setCellStyle(eReqheadStyle);

		for (int j = 0; j <= cell.getColumnIndex(); j++) {
			sheet1.setColumnWidth(j, xsWidth);
		}

		if (null != exceptionList) {
			for (ExceptionDTO exceptionDTO : exceptionList) {
				eReqrow = sheet1.createRow(eReqrowNum++);
				dataCoulmnCount = 0;

				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getProjectId());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getMasterProjectName());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getPmName());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getPmLeaderName());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getBusinessUnit());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getSegment());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getReasonForException());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getCashMilestoneActivityId());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getMilestoneDesc());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getMaxActualFinishDt());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getPmRequestDt());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getActualInvoiceDt());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getInvoiceNumber());
				eReqcell.setCellStyle(eReqbodyStyle);

				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getActualInvoiceYFw());
				eReqcell.setCellStyle(eReqbodyStyle);
				
				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getRemarks());
				eReqcell.setCellStyle(eReqbodyStyle);
				
				dataCoulmnCount = dataCoulmnCount + 1;
				eReqcell = eReqrow.createCell(dataCoulmnCount);
				eReqcell.setCellValue(exceptionDTO.getRemarksComments());
				eReqcell.setCellStyle(eReqbodyStyle);

			}

		}

		Footer footer = sheet.getFooter();
		footer.setCenter("GE Confidential");

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

	private CellStyle getCellBodyColorStyle(final XSSFWorkbook workbook, String color) {
		XSSFCellStyle headStyle = workbook.createCellStyle();
		byte[] rgb = new byte[3];
		if (color.equalsIgnoreCase("#00cc00")) {
			rgb[0] = (byte) 0; // red
			rgb[1] = (byte) 204; // green
			rgb[2] = (byte) 0; // blue
		}
		if (color.equalsIgnoreCase("#ff0000")) {
			rgb[0] = (byte) 255; // red
			rgb[1] = (byte) 0; // green
			rgb[2] = (byte) 0; // blue
		}
		XSSFColor myColor = new XSSFColor(rgb);
		headStyle.setFillForegroundColor(myColor);
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
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
		bodyStyle.getWrapText();
		bodyStyle.setFont(getFontContent(workbook));
		return bodyStyle;
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

	private Font getFontContent(final XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 8);
		font.setFontName("GE Inspira Sans");
		font.setColor(IndexedColors.BLACK.getIndex());
		return font;
	}

	public XSSFWorkbook exportTargetProjectExcel(final XSSFWorkbook workbook,
			List<ProjectTargetDTO> projectTargetList) {

		Sheet sheet = workbook.createSheet("Project Target List");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int headerColumnCount = 0;
		int dataColumnCount = 0;

		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("WACT(DAYS)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("P50(DAYS)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("P90(DAYS)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT BILLING CYCLE TARGET (DAYS)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MONTHLY BILLING SLOT (DAYS)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT PAYMENT TERM (DAYS)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CURR QTR BILLING TARGET (MM USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CURR QTR COLLECTION TARGET (MM USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("%TAX WITHHOLDING"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("UPDATED ON"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != projectTargetList) {

			for (ProjectTargetDTO projectTargetDTO : projectTargetList) {
				row = sheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getProjectId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getWeightedAvgCt());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getP50OfBm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getP90OfBm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getProjectTargetDay());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getMonthlyBillingSlot());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getProjectPaymentTerm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getCurQtrBillingTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getCurQtrCollectionTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getTaxWithholdingPercentage());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectTargetDTO.getUpdatedOn());
				cell.setCellStyle(bodyStyle);

			}
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;

	}

	public XSSFWorkbook exportOpenInvoiceDatatableExcel(XSSFWorkbook workbook,
			List<OFEOpenInvoiceDataTableDTO> tableDetails) {

		Sheet sheet = workbook.createSheet("Table Details");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;

		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("TYPE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(1);
		cell.setCellValue(("CATEGORY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(2);
		cell.setCellValue(("AGING"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(3);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(4);
		cell.setCellValue(("INVOICE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(5);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(6);
		cell.setCellValue(("OPEN INVOICE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(7);
		cell.setCellValue(("AR USD"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(8);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(9);
		cell.setCellValue(("INVOICE DUE DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(10);
		cell.setCellValue(("BILLING MILESTONE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(11);
		cell.setCellValue(("RISK OPTY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(12);
		cell.setCellValue(("COMMENTS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(13);
		cell.setCellValue(("COLLECTION CODE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(14);
		cell.setCellValue(("COLLECTION COMMENTS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(15);
		cell.setCellValue(("COLLECTION NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(16);
		cell.setCellValue(("DISPUTE CODE DESC"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(17);
		cell.setCellValue(("DISPUTE COMMENTS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(18);
		cell.setCellValue(("DISPUTE DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(19);
		cell.setCellValue(("DISPUTE NUMBER"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(20);
		cell.setCellValue(("DISPUTE OWNER"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(21);
		cell.setCellValue(("DISPUTE RESOLUTION DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(22);
		cell.setCellValue(("COMMITED ON"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(23);
		cell.setCellValue(("DISPUTE STATUS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(24);
		cell.setCellValue(("LATEST ACTION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(25);
		cell.setCellValue(("CUSTOMER SEGMENT"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(26);
		cell.setCellValue(("INVOICE AMOUNT USD"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(27);
		cell.setCellValue(("PAYMENT TERM"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(28);
		cell.setCellValue(("PO"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(29);
		cell.setCellValue(("LE NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(30);
		cell.setCellValue(("CREDIT LIMIT"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != tableDetails) {

			for (OFEOpenInvoiceDataTableDTO oFEOpenInvoiceDataTableDTO : tableDetails) {
				row = sheet.createRow(rowNum++);

				cell = row.createCell(0);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getType());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(1);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCategory());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(2);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getAging());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(3);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(4);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getInvoiceAmountCurr());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(5);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCurrency());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(6);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getOpenInvoiceAmountCurr());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(7);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getArUSD());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(8);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getInvoiceDate());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(9);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getInvoiceDueDate());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(10);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getBillingMilestone());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(11);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getRiskOpty());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(12);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getComments());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(13);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCollectionCode());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(14);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCollectionComments());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(15);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCollectionName());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(16);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeCodeDesc());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(17);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeComments());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(18);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeDate());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(19);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeNumber());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(20);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeOwner());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(21);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeResolutionDate());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(22);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCommitedOn());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(23);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getDisputeStatus());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(24);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getLatestAction());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(25);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCustomerSegment());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(26);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getInvoiceAmountUSD());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(27);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getPaymentTerm());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(28);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getPo());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(29);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getLeName());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(30);
				cell.setCellValue(oFEOpenInvoiceDataTableDTO.getCreditLimit());
				cell.setCellStyle(bodyStyle);

			}
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");

		return workbook;

	}

	public void exportBillingMilestoneDetailsExcel(XSSFWorkbook workbook, List<BillingMilestoneListDTO> milestoneList,
			List<ExceptionCategoryDTO> categoryList) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet sheet = workbook.createSheet("MILESTONES");
		int rowNum = 1;
		int dataColumnCount = 0;
		int categoryColumnInt = 0;
		int milestoneColumnInt = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(40);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateStyle = getCellDateStyle(workbook);

		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("TO BILL CATEGORY"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("NO OF MILESTONES"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("# %"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("$ %"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != categoryList) {
			for (ExceptionCategoryDTO categoryDTO : categoryList) {
				row = sheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(categoryDTO.getExceptionCategory());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != categoryDTO.getNoOfInvoice() && !categoryDTO.getNoOfInvoice().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(categoryDTO.getNoOfInvoice()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != categoryDTO.getAmount() && !categoryDTO.getAmount().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(categoryDTO.getAmount()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != categoryDTO.getNoOfInvoicePercent()
						&& !categoryDTO.getNoOfInvoicePercent().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(categoryDTO.getNoOfInvoicePercent()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != categoryDTO.getAmountPercent() && !categoryDTO.getAmountPercent().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(categoryDTO.getAmountPercent()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

			}
		}
		rowNum = rowNum + 2;
		row = sheet.createRow(rowNum++);
		row.setHeightInPoints(40);

		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);
		
		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("MILESTONE"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("PM LEADER"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("USD AMOUNT"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("F-BL"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("LAST ESTIMATE"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("MAX PRED ACTUAL DATE W/LAG"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("RISK/OPPTY"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("COMMENT"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("CHECK ON LE"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("INVOICE"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("INVOICED AMOUNT(USD)"));
		cell.setCellStyle(headStyle);

		milestoneColumnInt = milestoneColumnInt + 1;
		cell = row.createCell(milestoneColumnInt);
		cell.setCellValue(("MILESTONE STATUS"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}
		if (null != categoryList) {
			for (BillingMilestoneListDTO milestoneDTO : milestoneList) {
				row = sheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getProjectId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getProjectName());
				cell.setCellStyle(bodyStyle);
				
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getFinancialSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getMilestone());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getPmName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getPmLeader());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getDescription());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != milestoneDTO.getAmountUsd() && !milestoneDTO.getAmountUsd().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(milestoneDTO.getAmountUsd()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != milestoneDTO.getFinBlDt() && !milestoneDTO.getFinBlDt().equalsIgnoreCase("")) {
					Date date = format.parse(milestoneDTO.getFinBlDt());
					cell.setCellValue(date);
					cell.setCellStyle(dateStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != milestoneDTO.getForecastDt() && !milestoneDTO.getForecastDt().equalsIgnoreCase("")) {
					Date date = format.parse(milestoneDTO.getForecastDt());
					cell.setCellValue(date);
					cell.setCellStyle(dateStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != milestoneDTO.getActualFinishDt()
						&& !milestoneDTO.getActualFinishDt().equalsIgnoreCase("")) {
					Date date = format.parse(milestoneDTO.getActualFinishDt());
					cell.setCellValue(date);
					cell.setCellStyle(dateStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getRiskOppty());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getComments());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getForecastCheck());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != milestoneDTO.getInvoiceDate() && !milestoneDTO.getInvoiceDate().equalsIgnoreCase("")) {
					Date date = format.parse(milestoneDTO.getInvoiceDate());
					cell.setCellValue(date);
					cell.setCellStyle(dateStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellStyle(bodyStyle);
				if (null != milestoneDTO.getInvAmountUsd() && !milestoneDTO.getInvAmountUsd().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(milestoneDTO.getInvAmountUsd()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(milestoneDTO.getMilStatus());
				cell.setCellStyle(bodyStyle);
			}
		}
	}

	public void exportSegmentDetailsExcel(XSSFWorkbook workbook, List<SegmentSummaryDTO> segmentList) {
		Sheet sheet = workbook.createSheet("FINANCIAL SEGMENT SUMMARY");
		sheet.createFreezePane(0, 1);
		Row row = null;
		Cell cell = null;
		int rowNum = 1;
		int headerColumnCount = 0, dataColumnCount = 0;

		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		row = sheet.createRow(0);
		row.setHeightInPoints(20);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FINANCIAL ITO BL (MM USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LE VS F-BL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FINANCIAL OTR BL (MM USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE (MM USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING TGT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACTUAL (MM USD)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("TO GO (VS F-BL)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE PERIOD-TO-CURRENT WEEK (PTCW)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("ACT VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < segmentList.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getSegment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getFinancialITOBl());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != segmentList.get(i).getLastEstimateVsFinancialBL()
					&& !segmentList.get(i).getLastEstimateVsFinancialBL().equalsIgnoreCase("")) {
				cell.setCellStyle(getCellBodyColorStyle(workbook, segmentList.get(i).getLastEstimateVsFinancialBL()));
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getFinancialBl());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getForecast());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != segmentList.get(i).getBillingTgt()
					&& !segmentList.get(i).getBillingTgt().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(segmentList.get(i).getBillingTgt()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getActual());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getFinancialBl());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != segmentList.get(i).getOpp() && !segmentList.get(i).getOpp().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(segmentList.get(i).getOpp()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != segmentList.get(i).getMediumHighRisk()
					&& !segmentList.get(i).getMediumHighRisk().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(segmentList.get(i).getMediumHighRisk()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getLastEstimatePeriodToCurrentWeek());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(segmentList.get(i).getStatus());
			cell.setCellStyle(bodyStyle);
		}
	}

	public void exportProjectDetailsExcel(XSSFWorkbook workbook, List<BillingProjectListDTO> projectList) {
		Sheet sheet = workbook.createSheet("PROJECTS");
		sheet.createFreezePane(0, 1);
		Row row = null;
		Cell cell = null;
		int rowNum = 1;
		int headerColumnCount = 0, dataColumnCount = 0;

		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		row = sheet.createRow(0);
		row.setHeightInPoints(20);

		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CONTRACT CUSTOMER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SPM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);
		
		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FINANCIAL BL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING FORECAST"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST VS F-BL"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING LE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICED"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING TO GO(vLE)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LE PERIOD TO CURRENT WEEK"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("STATUS(INV-LE)"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, sWidth);
		}

		for (int i = 0; i < projectList.size(); i++) {
			row = sheet.createRow(rowNum++);
			dataColumnCount = 0;

			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getProjectId());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getMasterProjectname());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getCustomeName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getPmName());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getSpm());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getSegment());
			cell.setCellStyle(bodyStyle);
			
			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getFinancialSegment());
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getFinBlAmountUsd()
					&& !projectList.get(i).getFinBlAmountUsd().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getFinBlAmountUsd()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getForecast() && !projectList.get(i).getForecast().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getForecast()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getVarianceToBL()
					&& !projectList.get(i).getVarianceToBL().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getVarianceToBL()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getBillingLE() && !projectList.get(i).getBillingLE().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getBillingLE()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getActual() && !projectList.get(i).getActual().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getActual()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getBillingLE() && !projectList.get(i).getBillingLE().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getBillingLE()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getOpp() && !projectList.get(i).getOpp().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getOpp()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getMediumHighRisk()
					&& !projectList.get(i).getMediumHighRisk().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getMediumHighRisk()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			if (null != projectList.get(i).getFctsAmtAsToday()
					&& !projectList.get(i).getFctsAmtAsToday().equalsIgnoreCase("")) {
				cell.setCellValue(Double.parseDouble(projectList.get(i).getFctsAmtAsToday()));
				cell.setCellStyle(bodyStyle);
			} else {
				cell.setBlank();
				cell.setCellStyle(bodyStyle);
			}
			cell.setCellStyle(bodyStyle);

			dataColumnCount = dataColumnCount + 1;
			cell = row.createCell(dataColumnCount);
			cell.setCellValue(projectList.get(i).getStatus());
			cell.setCellStyle(bodyStyle);
		}
	}

	public void exportForecastChartExcel(XSSFWorkbook workbook, List<ForecastChartDTO> tableDetails) {
		Sheet sheet = workbook.createSheet("Forecast Tab");
		int rowNum = 1;
		int categoryColumnInt = 0;
		int dataColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(40);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("MILESTONE ID"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("ESTIM CASH DATE"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("ORIGIN AMOUNT"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("CURRENCY CODE"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("%TAX WITHHOLDING"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("NET CASH FC USD"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("MILESTONE STATUS"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("LINK STATUS"));
		cell.setCellStyle(headStyle);

		categoryColumnInt = categoryColumnInt + 1;
		cell = row.createCell(categoryColumnInt);
		cell.setCellValue(("FORECAST SOURCE"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != tableDetails) {
			for (ForecastChartDTO dto : tableDetails) {
				row = sheet.createRow(rowNum++);

				dataColumnCount = 0;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getMilestoneId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getEstimCashDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getOrigAmount());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getCurrencyCode());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getAmountInUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getTaxWithholdingPercentage());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getNetCashFcUSD());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getDescription());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceDt());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getMilestoneStatus());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getLinkStatus());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getForecastSource());
				cell.setCellStyle(bodyStyle);

			}
		}

	}

	public void exportOfeTeTDashboardDayExcel(XSSFWorkbook workbook,
			List<OFETeTProjectTableDTO> projectExcelDay) {

		Sheet sheet = workbook.createSheet("Ofe TeT Dashboard Excel");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;

		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		int count = 0;

		cell = row.createCell(count++);
		cell.setCellValue(("DAYS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("ASSET DAYS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("REMAIN UNITS DAY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("RESOURCE ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("RESOURCE NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("ACTIVITY ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("ACTIVITY NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("LOCATION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("START DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("FINISH DATE"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != projectExcelDay) {
			for (OFETeTProjectTableDTO dto : projectExcelDay) {
				row = sheet.createRow(rowNum++);

				int cnt = 0;

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getDays());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getAssetDays());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getRemainUnitsDay());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getResourceId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getResourceName());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getProjectId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getActivityId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getActivityName());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getRegion());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getLocation());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getStartDate());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getFinishDate());
				cell.setCellStyle(bodyStyle);

			}
		}
	}
	
	
	
	public void exportOfeTeTDashboardMonExcel(XSSFWorkbook workbook,
			List<OFETeTProjectTableMonthDTO> projectExcelMon) {

		Sheet sheet = workbook.createSheet("Ofe TeT Dashboard Excel");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;

		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		int count = 0;

		cell = row.createCell(count++);
		cell.setCellValue(("DAYS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("ASSET DAYS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("REMAIN UNITS DAY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("RESOURCE ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("RESOURCE NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("ACTIVITY ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("ACTIVITY NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("LOCATION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("START DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("FINISH DATE"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != projectExcelMon) {
			for (OFETeTProjectTableMonthDTO dto : projectExcelMon) {
				row = sheet.createRow(rowNum++);

				int cnt = 0;

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getDays());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getAssetDays());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getRemainUnitsDay());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getResourceId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getResourceName());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getProjectId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getActivityId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getActivityName());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getRegion());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getLocation());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getStartDate());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getFinishDate());
				cell.setCellStyle(bodyStyle);

			}
		}
	}

	public void getAtCapacityExcelDownload(XSSFWorkbook workbook, List<AtCapacityDTO> atCapacityTable) {
		Sheet sheet = workbook.createSheet("Ofe At Capacity Excel");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;

		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		int count = 0;

		cell = row.createCell(count++);
		cell.setCellValue(("RESOURCE ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("RESOURCE NAME"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("CURRENT FACILITY"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("P6 LOCATION"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("PART NUMBERS"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(count++);
		cell.setCellValue(("PN DESCRIPTION"));
		cell.setCellStyle(headStyle);
		
		cell = row.createCell(count++);
		cell.setCellValue(("ID"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("SERIAL NUMBER"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("COST NEW"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("REPAIR COST"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("NEXT CALIB DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("CALIBRATION FREQ"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("CALIBRATION FREQ UOM"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("MAINTAINANCE FREQ"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("MAINTAINANCE FREQ UOM"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("NEXT MAINTAINANCE DATE"));
		cell.setCellStyle(headStyle);

		cell = row.createCell(count++);
		cell.setCellValue(("TOTAL"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != atCapacityTable) {
			for (AtCapacityDTO dto : atCapacityTable) {
				row = sheet.createRow(rowNum++);

				int cnt = 0;

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getResource_id());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getResource_name());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getStatus());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getRegion());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getCurrent_facility());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getP6_location());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getPart_numbers());
				cell.setCellStyle(bodyStyle);
				
				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getDescription());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getId());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getSerial_number());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getCostnew());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getRepair_cost());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getNext_calib_dt());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getCalibration_freq());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getCalibration_freq_uom());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getMaintainance_freq());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getMaintainance_freq_uom());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getNext_maintainance_dt());
				cell.setCellStyle(bodyStyle);

				cell = row.createCell(cnt++);
				cell.setCellValue(dto.getTotal());
				cell.setCellStyle(bodyStyle);

			}
		}
	}

	public void exportBillingOutOfProjectExcel(XSSFWorkbook workbook, List<TPSBillingOutOfRTProjectDetailsDTO> list)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
		Sheet sheet = workbook.createSheet("Milestone Tab");
		int rowNum = 1;
		int headerColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateStyle = getCellDateStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("BUSINESS"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CUSTOMER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INSTALLATION COUNTRY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("MILESTONE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("SPM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM LEADER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("DESCRIPTION"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("USD MILESTONE AMOUNT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("LAST ESTIMATE (FORECAST DATE)"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("RISK/OPPTY"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("COMMENT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICED AMOUNT(USD)"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != list) {
			for (TPSBillingOutOfRTProjectDetailsDTO dto : list) {
				row = sheet.createRow(rowNum++);
				int dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getBusiness());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getCustomer());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInstallationCountry());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getProject());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getMilestone());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getPm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getSpm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getPmLeader());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getDescription());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != dto.getUsdMilestoneAmount() && !dto.getUsdMilestoneAmount().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(dto.getUsdMilestoneAmount()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != dto.getLastEstimate() && !dto.getLastEstimate().equalsIgnoreCase("")) {
					Date date = format.parse(dto.getLastEstimate());
					cell.setCellValue(date);
					cell.setCellStyle(dateStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getRiskOppty());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getComment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != dto.getInvoiceDate() && !dto.getInvoiceDate().equalsIgnoreCase("")) {
					Date date = format.parse(dto.getInvoiceDate());
					cell.setCellValue(date);
					cell.setCellStyle(dateStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != dto.getInvoicedAmountUSD() && !dto.getInvoicedAmountUSD().equalsIgnoreCase("")) {
					cell.setCellValue(Double.parseDouble(dto.getInvoicedAmountUSD()));
					cell.setCellStyle(bodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}
			}
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	public void exportBillingLinearityDetailsExcel(SXSSFWorkbook workbook,
			List<BillingLinearityChartPopupDTO> linearityList) throws ParseException {
		SXSSFSheet sheet = workbook.createSheet("Linearity Tab");
		int rowNum = 1;
		int headerColumnCount = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(20);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(0);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE DUE DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE AMOUNT"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("CURR"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("INVOICE AMOUNT USD"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING MILESTONE ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING MILESTONE DESC"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM REQUEST DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("FORECAST DATE"));
		cell.setCellStyle(headStyle);

		headerColumnCount = headerColumnCount + 1;
		cell = row.createCell(headerColumnCount);
		cell.setCellValue(("BILLING AMOUNT USD"));
		cell.setCellStyle(headStyle);

		if (null != linearityList) {
			for (BillingLinearityChartPopupDTO dto : linearityList) {
				row = sheet.createRow(rowNum++);
				int dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceDueDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceAmount());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getInvoiceAmountUSD());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getBillingMilestoneId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getBillingMilestoneDesc());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getPmRequestDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getProjectId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getPm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getForcastDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(dto.getBillingAmountUSD());
				cell.setCellStyle(bodyStyle);

				sheet.trackAllColumnsForAutoSizing();
			}
		}
		Footer footer = sheet.getFooter();
		footer.setCenter("BH Confidential");
	}

	private CellStyle getCellHeadStyle(final SXSSFWorkbook workbook) {
		CellStyle headStyle = workbook.createCellStyle();
		headStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		headStyle.setVerticalAlignment(VerticalAlignment.TOP);
		setBorderStyle(headStyle);
		headStyle.setFont(getFontHeader(workbook));
		return headStyle;
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

}
