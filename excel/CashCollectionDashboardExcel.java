package com.bh.realtrack.excel;

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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bh.realtrack.dto.CashDetailDTO;
import com.bh.realtrack.dto.OFEBusinessSummaryDetailsDTO;
import com.bh.realtrack.dto.OFEProjectDetailsDTO;
import com.bh.realtrack.dto.RegionSummaryDetailDTO;
import com.bh.realtrack.dto.SegmentSummaryDetailDTO;
import com.bh.realtrack.dto.TPSCashDetailDTO;
import com.bh.realtrack.dto.TPSProjectDetailDTO;

public class CashCollectionDashboardExcel {

	int lWidth = ((int) (70 * 1.14388)) * 256;
	int mWidth = ((int) (40 * 1.14388)) * 256;
	int sWidth = ((int) (22 * 1.14388)) * 256;
	int xsWidth = ((int) (18 * 1.14388)) * 256;

	public XSSFWorkbook exportDetailsTableExcel(final XSSFWorkbook workbook,
			List<SegmentSummaryDetailDTO> segmentDTOList, List<RegionSummaryDetailDTO> regionDTOList,
			List<TPSProjectDetailDTO> consolidatedProjectDTOList, List<TPSProjectDetailDTO> projectDetailDTOList,
			List<TPSCashDetailDTO> cashDetialsDTOList) throws ParseException {

		Sheet sheet = workbook.createSheet("SEGMENT SUMMARY");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int dataColumnCount = 0;
		int segmentColumnInt = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);
		CellStyle dateBodyStyle = getCellDateStyle(workbook);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK (PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != segmentDTOList) {

			for (SegmentSummaryDetailDTO segmentSummaryDetailDTO : segmentDTOList) {
				row = sheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getPqCollection().length() != 0
						&& segmentSummaryDetailDTO.getPqCollection() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getPqCollection()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getBillAndCashMMUsd().length() != 0
						&& segmentSummaryDetailDTO.getBillAndCashMMUsd() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getBillAndCashMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getCashLEMMUsd().length() != 0
						&& segmentSummaryDetailDTO.getCashLEMMUsd() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getCashLEMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getTotalCollectionTarget().length() != 0
						&& segmentSummaryDetailDTO.getTotalCollectionTarget() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getTotalCollectionTarget()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getCollectionVTGT().length() != 0
						&& segmentSummaryDetailDTO.getCollectionVTGT() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getCollectionVTGT()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getCollectedMMsd().length() != 0
						&& segmentSummaryDetailDTO.getCollectedMMsd() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getCollectedMMsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getCollecttionToGoLE().length() != 0
						&& segmentSummaryDetailDTO.getCollecttionToGoLE() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getCollecttionToGoLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getCollectedVPW().length() != 0
						&& segmentSummaryDetailDTO.getCollectedVPW() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getCollectedVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getPdLE().length() != 0 && segmentSummaryDetailDTO.getPdLE() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getPdLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getPdTarget().length() != 0
						&& segmentSummaryDetailDTO.getPdTarget() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getPdTarget()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getPdVTGT().length() != 0 && segmentSummaryDetailDTO.getPdVTGT() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getPdVTGT()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getPastDueMMUsd().length() != 0
						&& segmentSummaryDetailDTO.getPastDueMMUsd() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getPastDueMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getPdVPW() != null && segmentSummaryDetailDTO.getPdVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getPdVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getOpp().length() != 0 && segmentSummaryDetailDTO.getOpp() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getOpp()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getMediumHighRisk().length() != 0
						&& segmentSummaryDetailDTO.getMediumHighRisk() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getMediumHighRisk()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getDisputedMMUsd().length() != 0
						&& segmentSummaryDetailDTO.getDisputedMMUsd() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getDisputedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getEscalatedMMUsd().length() != 0
						&& segmentSummaryDetailDTO.getEscalatedMMUsd() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getEscalatedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (segmentSummaryDetailDTO.getLastEstimatePTCW().length() != 0
						&& segmentSummaryDetailDTO.getLastEstimatePTCW() != null) {
					cell.setCellValue(Double.parseDouble(segmentSummaryDetailDTO.getLastEstimatePTCW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

			}
		}

		Sheet regionSheet = workbook.createSheet("REGION SUMMARY");
		regionSheet.createFreezePane(0, 1);
		rowNum = 1;
		dataColumnCount = 0;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = regionSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK(PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			regionSheet.setColumnWidth(i, xsWidth);
		}

		if (null != regionDTOList) {

			for (RegionSummaryDetailDTO regionSummaryDetailDTO : regionDTOList) {
				row = regionSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getPqCollection() != null
						&& regionSummaryDetailDTO.getPqCollection().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getPqCollection()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getBillAndCashMMUsd() != null
						&& regionSummaryDetailDTO.getBillAndCashMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getBillAndCashMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getCashLEMMUsd() != null
						&& regionSummaryDetailDTO.getCashLEMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getCashLEMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getTotalCollectionTarget() != null
						&& regionSummaryDetailDTO.getTotalCollectionTarget().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getTotalCollectionTarget()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getCollectionVTGT() != null
						&& regionSummaryDetailDTO.getCollectionVTGT().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getCollectionVTGT()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getCollectedMMsd() != null
						&& regionSummaryDetailDTO.getCollectedMMsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getCollectedMMsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getCollecttionToGoLE() != null
						&& regionSummaryDetailDTO.getCollecttionToGoLE().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getCollecttionToGoLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getCollectedVPW() != null
						&& regionSummaryDetailDTO.getCollectedVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getCollectedVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getPdLE() != null && regionSummaryDetailDTO.getPdLE().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getPdLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getPdTarget() != null
						&& regionSummaryDetailDTO.getPdTarget().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getPdTarget()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getPdVTGT() != null && regionSummaryDetailDTO.getPdVTGT().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getPdVTGT()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getPastDueMMUsd() != null
						&& regionSummaryDetailDTO.getPastDueMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getPastDueMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getPdVPW() != null && regionSummaryDetailDTO.getPdVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getPdVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getOpp() != null && regionSummaryDetailDTO.getOpp().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getOpp()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getMediumHighRisk() != null
						&& regionSummaryDetailDTO.getMediumHighRisk().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getMediumHighRisk()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getDisputedMMUsd() != null
						&& regionSummaryDetailDTO.getDisputedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getDisputedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getEscalatedMMUsd() != null
						&& regionSummaryDetailDTO.getEscalatedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getEscalatedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (regionSummaryDetailDTO.getLastEstimatePTCW() != null
						&& regionSummaryDetailDTO.getLastEstimatePTCW().length() != 0) {
					cell.setCellValue(Double.parseDouble(regionSummaryDetailDTO.getLastEstimatePTCW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

			}
		}

		Sheet consolidatedProjectSheet = workbook.createSheet("CONSOLIDATED PROJECTS");
		consolidatedProjectSheet.createFreezePane(0, 1);
		rowNum = 1;
		dataColumnCount = 0;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = consolidatedProjectSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CONTRACT CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SPM"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH BILLED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH TO GO"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK(PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			consolidatedProjectSheet.setColumnWidth(i, xsWidth);
		}

		if (null != consolidatedProjectDTOList) {

			for (TPSProjectDetailDTO conProjectDetailDTO : consolidatedProjectDTOList) {
				row = consolidatedProjectSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getContractCustomer());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getPm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getSpm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getFinancialSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getPqCollectionMMUsd() != null
						&& conProjectDetailDTO.getPqCollectionMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getPqCollectionMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getBillAndCashMMUsd() != null
						&& conProjectDetailDTO.getBillAndCashMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getBillAndCashMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getBcBilled() != null && conProjectDetailDTO.getBcBilled().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getBcBilled()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getBcToGo() != null && conProjectDetailDTO.getBcToGo().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getBcToGo()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getCashLEMMUsd() != null
						&& conProjectDetailDTO.getCashLEMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getCashLEMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getTotalCollectionTarget() != null
						&& conProjectDetailDTO.getTotalCollectionTarget().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getTotalCollectionTarget()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getCollectedMMUsd() != null
						&& conProjectDetailDTO.getCollectedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getCollectedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getCollectionToGoLE() != null
						&& conProjectDetailDTO.getCollectionToGoLE().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getCollectionToGoLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getCollectedVPW() != null
						&& conProjectDetailDTO.getCollectedVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getCollectedVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getPdLE() != null && conProjectDetailDTO.getPdLE().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getPdLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getPastDueMMUSD() != null
						&& conProjectDetailDTO.getPastDueMMUSD().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getPastDueMMUSD()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getPdVPW() != null && conProjectDetailDTO.getPdVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getPdVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getOpp() != null && conProjectDetailDTO.getOpp().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getOpp()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getMediumHighRisk() != null
						&& conProjectDetailDTO.getMediumHighRisk().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getMediumHighRisk()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getDisputedMMUsd() != null
						&& conProjectDetailDTO.getDisputedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getDisputedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getEscalatedMMUsd() != null
						&& conProjectDetailDTO.getEscalatedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getEscalatedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (conProjectDetailDTO.getLastEstimatePTCW() != null
						&& conProjectDetailDTO.getLastEstimatePTCW().length() != 0) {
					cell.setCellValue(Double.parseDouble(conProjectDetailDTO.getLastEstimatePTCW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(conProjectDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

			}
		}

		Sheet projectSheet = workbook.createSheet("PROJECTS");
		projectSheet.createFreezePane(0, 1);
		rowNum = 1;
		dataColumnCount = 0;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = projectSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CONTRACT CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SPM"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INST COUNTRY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH BILLED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH TO GO"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK(PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("AR (TOGO + PD LE)"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			projectSheet.setColumnWidth(i, xsWidth);
		}

		if (null != projectDetailDTOList) {

			for (TPSProjectDetailDTO projectDetailDTO : projectDetailDTOList) {
				row = projectSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPrjId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getContractCustomer());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getSpm());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getFinancialSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getInstallCountry());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getPqCollectionMMUsd() != null
						&& projectDetailDTO.getPqCollectionMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getPqCollectionMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getBillAndCashMMUsd() != null
						&& projectDetailDTO.getBillAndCashMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getBillAndCashMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getBcBilled() != null && projectDetailDTO.getBcBilled().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getBcBilled()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getBcToGo() != null && projectDetailDTO.getBcToGo().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getBcToGo()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getCashLEMMUsd() != null && projectDetailDTO.getCashLEMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getCashLEMMUsd()));
				}

				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getTotalCollectionTarget() != null
						&& projectDetailDTO.getTotalCollectionTarget().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getTotalCollectionTarget()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getCollectedMMUsd() != null
						&& projectDetailDTO.getCollectedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getCollectedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getCollectionToGoLE() != null
						&& projectDetailDTO.getCollectionToGoLE().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getCollectionToGoLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getCollectedVPW() != null && projectDetailDTO.getCollectedVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getCollectedVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getPdLE() != null && projectDetailDTO.getPdLE().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getPdLE()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getPastDueMMUSD() != null && projectDetailDTO.getPastDueMMUSD().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getPastDueMMUSD()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getPdVPW() != null && projectDetailDTO.getPdVPW().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getPdVPW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getOpp() != null && projectDetailDTO.getOpp().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getOpp()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getMediumHighRisk() != null
						&& projectDetailDTO.getMediumHighRisk().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getMediumHighRisk()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getDisputedMMUsd() != null && projectDetailDTO.getDisputedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getDisputedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getEscalatedMMUsd() != null
						&& projectDetailDTO.getEscalatedMMUsd().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getEscalatedMMUsd()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getLastEstimatePTCW() != null
						&& projectDetailDTO.getLastEstimatePTCW().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getLastEstimatePTCW()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (projectDetailDTO.getAr() != null && projectDetailDTO.getAr().length() != 0) {
					cell.setCellValue(Double.parseDouble(projectDetailDTO.getAr()));
				}
				cell.setCellStyle(bodyStyle);
			}
		}

		SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
		Sheet cashSheet = workbook.createSheet("CASH DETAILS");
		cashSheet.createFreezePane(0, 1);
		rowNum = 1;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = cashSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE DESC"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("RISK/OPPTY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("RISK COMMENTS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DAYS TO COLLECT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE DUE DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OUTSTANDING AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OUTSTANDING AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH COLLECTED CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH COLLECTED (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH BILLED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH TO GO"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("B&C RISK/OPPTY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FORECAST DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FORECAST COLLECTION DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE AMOUNT"));
		cell.setCellStyle(headStyle);

		// Modified by Tushar Chavda
		// Dt: 2022-07-01
		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION COMMITTED DATE FOR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE STATUS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE NUMBER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE CATEGORY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE CODE DESCRIPTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE DATE - FIRST DISPUTE COMMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LATEST DISPUTE COMMENTS DATE - DISPUTE COMMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE OWNER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE MANAGER LVL 1"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTE AGEING BUCKET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CUSTOMER ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CUSTOMER ESCALATED TO"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			cashSheet.setColumnWidth(i, xsWidth);
		}

		if (null != cashDetialsDTOList) {

			for (TPSCashDetailDTO cashDetailDTO : cashDetialsDTOList) {
				row = cashSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getProjectId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCustomer());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getSegmentBusinessOFE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getFinancialSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getStatus());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getMilestoneDesc());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRiskOppty());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRiskComments());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getDaysToCollect() != null && cashDetailDTO.getDaysToCollect().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getDaysToCollect()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != cashDetailDTO.getInvoiceDate() && !cashDetailDTO.getInvoiceDate().equalsIgnoreCase("")) {
					Date dt = format.parse(cashDetailDTO.getInvoiceDate());
					cell.setCellValue(dt);
					cell.setCellStyle(dateBodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != cashDetailDTO.getInvoiceDueDate()
						&& !cashDetailDTO.getInvoiceDueDate().equalsIgnoreCase("")) {
					Date dt = format.parse(cashDetailDTO.getInvoiceDueDate());
					cell.setCellValue(dt);
					cell.setCellStyle(dateBodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != cashDetailDTO.getCollectionDate()
						&& !cashDetailDTO.getCollectionDate().equalsIgnoreCase("")) {
					Date dt = format.parse(cashDetailDTO.getCollectionDate());
					cell.setCellValue(dt);
					cell.setCellStyle(dateBodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCurrencyBillingData());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceAmountCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getInvoiceAmountDollar() != null
						&& cashDetailDTO.getInvoiceAmountDollar().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getInvoiceAmountDollar()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getOutstandingAmountCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getOutstandingAmountDollar() != null
						&& cashDetailDTO.getOutstandingAmountDollar().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getOutstandingAmountDollar()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCollectedCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getCashCollectedDollar() != null
						&& cashDetailDTO.getCashCollectedDollar().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getCashCollectedDollar()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getBcBilled() != null && cashDetailDTO.getBcBilled().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getBcBilled()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getBcToGo() != null && cashDetailDTO.getBcToGo().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getBcToGo()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getCashLEDollar() != null && cashDetailDTO.getCashLEDollar().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getCashLEDollar()));
				}
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getMilestoneId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getBillingRiskOppty());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != cashDetailDTO.getForecastDate() && !cashDetailDTO.getForecastDate().equalsIgnoreCase("")) {
					Date dt = format.parse(cashDetailDTO.getForecastDate());
					cell.setCellValue(dt);
					cell.setCellStyle(dateBodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (null != cashDetailDTO.getForecastCollectionDate()
						&& !cashDetailDTO.getForecastCollectionDate().equalsIgnoreCase("")) {
					Date dt = format.parse(cashDetailDTO.getForecastCollectionDate());
					cell.setCellValue(dt);
					cell.setCellStyle(dateBodyStyle);
				} else {
					cell.setBlank();
					cell.setCellStyle(bodyStyle);
				}

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCurrency());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getMilestoneAmountCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				if (cashDetailDTO.getMilestoneAmountDollar() != null
						&& cashDetailDTO.getMilestoneAmountDollar().length() != 0) {
					cell.setCellValue(Double.parseDouble(cashDetailDTO.getMilestoneAmountDollar()));
				}
				cell.setCellStyle(bodyStyle);

				// Modified by Tushar Chavda
				// Dt: 2022-07-01
				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getColCommitDtOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispStatusOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispNumberOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispCategoryOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispCodeDescOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispDtFirstCommentOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getLatestDispCmtDtDDispOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispOwnerOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispMgrLvl1Out());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDispAgeingBuckOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCustEscalatedOut());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCustEscalatedToOut());
				cell.setCellStyle(bodyStyle);
			}
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("GE Confidential");

		return workbook;

	}

	public XSSFWorkbook exportInstallDetailsTableExcel(XSSFWorkbook workbook,
			List<SegmentSummaryDetailDTO> segmentDTOList, List<RegionSummaryDetailDTO> regionDTOList,
			List<TPSProjectDetailDTO> projectDetailDTOList, List<TPSCashDetailDTO> cashDetialsDTOList) {

		Sheet sheet = workbook.createSheet("SEGMENT SUMMARY");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int dataColumnCount = 0;
		int segmentColumnInt = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FINANCIAL SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK (PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != segmentDTOList) {

			for (SegmentSummaryDetailDTO segmentSummaryDetailDTO : segmentDTOList) {
				row = sheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPqCollection());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getBillAndCashMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCashLEMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getTotalCollectionTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectionVTGT());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedMMsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollecttionToGoLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPdLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPdTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPdVTGT());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPastDueMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPdVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getOpp());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getMediumHighRisk());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getDisputedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getEscalatedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getLastEstimatePTCW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

			}
		}

		Sheet regionSheet = workbook.createSheet("REGION SUMMARY");
		regionSheet.createFreezePane(0, 1);
		rowNum = 1;
		dataColumnCount = 0;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = regionSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK(PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			regionSheet.setColumnWidth(i, xsWidth);
		}

		if (null != regionDTOList) {

			for (RegionSummaryDetailDTO regionSummaryDetailDTO : regionDTOList) {
				row = regionSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getPqCollection());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getBillAndCashMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCashLEMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getTotalCollectionTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCollectionVTGT());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCollectedMMsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCollecttionToGoLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCollectedVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getPdLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getPdTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getPdVTGT());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getPastDueMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getPdVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getOpp());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getMediumHighRisk());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getDisputedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getEscalatedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getLastEstimatePTCW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(regionSummaryDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

			}
		}

		Sheet projectSheet = workbook.createSheet("PROJECTS");
		projectSheet.createFreezePane(0, 1);
		rowNum = 1;
		dataColumnCount = 0;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = projectSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CONTRACT CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("IM LEADER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INST COUNTRY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK(PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("AR (TOGO + PD LE)"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			projectSheet.setColumnWidth(i, xsWidth);
		}

		if (null != projectDetailDTOList) {

			for (TPSProjectDetailDTO projectDetailDTO : projectDetailDTOList) {
				row = projectSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPrjId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getContractCustomer());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getImLeader());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getSegment());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getInstallCountry());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPqCollectionMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getBillAndCashMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCashLEMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getTotalCollectionTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectionToGoLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPdLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPastDueMMUSD());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPdVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getOpp());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getMediumHighRisk());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getDisputedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getEscalatedMMUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getLastEstimatePTCW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getAr());
				cell.setCellStyle(bodyStyle);
			}
		}

		Sheet cashSheet = workbook.createSheet("CASH DETAILS");
		cashSheet.createFreezePane(0, 1);
		rowNum = 1;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = cashSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("SEGMENT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE DESC"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("RISK/OPPTY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("RISK COMMENTS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DAYS TO COLLECT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE DUE DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OUTSTANDING AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OUTSTANDING AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH COLLECTED CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH COLLECTED (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE (USD)"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			cashSheet.setColumnWidth(i, xsWidth);
		}

		if (null != cashDetialsDTOList) {

			for (TPSCashDetailDTO cashDetailDTO : cashDetialsDTOList) {
				row = cashSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getProjectId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCustomer());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getSegmentBusinessOFE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getStatus());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getMilestoneDesc());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRiskOppty());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRiskComments());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getDaysToCollect());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceDueDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCollectionDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCurrencyBillingData());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceAmountCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getInvoiceAmountDollar());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getOutstandingAmountCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getOutstandingAmountDollar());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCollectedCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCollectedDollar());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashLEDollar());
				cell.setCellStyle(bodyStyle);

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

	public XSSFWorkbook exportDetailsTableExcelForOFE(final XSSFWorkbook workbook,
			List<OFEBusinessSummaryDetailsDTO> businessDTOList, List<OFEProjectDetailsDTO> projectDetailDTOList,
			List<CashDetailDTO> cashDetialsDTOList) {

		Sheet sheet = workbook.createSheet("BUSINESS SUMMARY");
		sheet.createFreezePane(0, 1);
		int rowNum = 1;
		int dataColumnCount = 0;
		int segmentColumnInt = 0;
		Row row = null;
		Cell cell = null;

		row = sheet.createRow(0);
		row.setHeightInPoints(60);
		CellStyle headStyle = getCellHeadStyle(workbook);
		CellStyle bodyStyle = getCellBodyStyle(workbook);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BUSINESS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("TOTAL COLLECTION TARGET"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LE VS TGT"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK (PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			sheet.setColumnWidth(i, xsWidth);
		}

		if (null != businessDTOList) {

			for (OFEBusinessSummaryDetailsDTO segmentSummaryDetailDTO : businessDTOList) {
				row = sheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getBusiness());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPqCollection());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getBillAndCash());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCashLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectionTarget());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getLeVsTgt());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedCashVal());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollecttionToGoLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPastDue());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getPastDueLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getOpp());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getMediumHighRisk());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getDisputed());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getEscalated());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getLastEstimatePTCW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(segmentSummaryDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);

			}
		}

		Sheet projectSheet = workbook.createSheet("PROJECTS");
		projectSheet.createFreezePane(0, 1);
		rowNum = 1;
		dataColumnCount = 0;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = projectSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CONTRACT CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PM"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PQ COLLECTION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BILL & CASH"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED TO GO LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PW"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PAST DUE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PD LE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OPP"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MEDIUM/HIGH RISK"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("DISPUTED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("ESCALATED"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("LAST ESTIMATE PERIOD TO CURRENT WEEK(PTCW)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTED VS PTCW"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			projectSheet.setColumnWidth(i, xsWidth);
		}

		if (null != projectDetailDTOList) {

			for (OFEProjectDetailsDTO projectDetailDTO : projectDetailDTOList) {
				row = projectSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPrjId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCustomerName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPmName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPqCollectionVal());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getBillAndCash());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCashLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedCashVal());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedToGoLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedVPW());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPastDue());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getPastDueLE());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getOpp());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getMediumHighRisk());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getDisputed());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getEscalated());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getLastEstiPerToctw());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(projectDetailDTO.getCollectedVsPTCW());
				cell.setCellStyle(bodyStyle);
			}
		}

		Sheet cashSheet = workbook.createSheet("CASH DETAILS");
		cashSheet.createFreezePane(0, 1);
		rowNum = 1;
		segmentColumnInt = 0;
		row = null;
		cell = null;

		row = cashSheet.createRow(0);
		row.setHeightInPoints(60);

		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("PROJECT NAME"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CUSTOMER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("BUSINESS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("REGION"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE NUMBER"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("RISK/OPPTY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE DUE DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("COLLECTION DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CURRENCY"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("INVOICE AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OUTSTANDING AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("OUTSTANDING AMOUNT (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH COLLECTED CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH COLLECTED (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("CASH LE (USD)"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("STATUS"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE ID"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE DESC"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FORECAST DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("FORECAST COLLECTION DATE"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE AMOUNT CURR"));
		cell.setCellStyle(headStyle);

		segmentColumnInt = segmentColumnInt + 1;
		cell = row.createCell(segmentColumnInt);
		cell.setCellValue(("MILESTONE AMOUNT"));
		cell.setCellStyle(headStyle);

		for (int i = 0; i <= cell.getColumnIndex(); i++) {
			cashSheet.setColumnWidth(i, xsWidth);
		}

		if (null != cashDetialsDTOList) {

			for (CashDetailDTO cashDetailDTO : cashDetialsDTOList) {
				row = cashSheet.createRow(rowNum++);
				dataColumnCount = 0;

				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getPrjId());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getProjectName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCustomerName());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getBu());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getRegion());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashInvoiceNumber());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashRisk());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashInvoiceDt());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashInvoiceDueDt());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCollectionDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCurrency());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashInvCurrencyCode());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashInvAmountUsd());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashFromCurrency());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashOpenInvoiceAmount());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashOpenAmountDocCurr());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCashCollected());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashCashLeAmt());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashStatus());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashProrataLine());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashMilestoneDescription());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashP6Milestoneforecast());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashEstimCashDate());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashMileCurrency());
				cell.setCellStyle(bodyStyle);

				dataColumnCount = dataColumnCount + 1;
				cell = row.createCell(dataColumnCount);
				cell.setCellValue(cashDetailDTO.getCashMilestoneAmountUsd());
				cell.setCellStyle(bodyStyle);

			}
		}

		Footer footer = sheet.getFooter();
		footer.setCenter("GE Confidential");

		return workbook;

	}

}
