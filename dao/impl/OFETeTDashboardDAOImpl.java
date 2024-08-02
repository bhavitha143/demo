package com.bh.realtrack.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.ServerErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.OFETeTDashboardDAO;
import com.bh.realtrack.dto.AssetDailyAndMonthlyPopupDTO;
import com.bh.realtrack.dto.AssetTrackerDataDTO;
import com.bh.realtrack.dto.AssetTrackerStageDTO;
import com.bh.realtrack.dto.AtCapacityDTO;
import com.bh.realtrack.dto.AtCapacityFiltersDTO;
import com.bh.realtrack.dto.AtLocationDTO;
import com.bh.realtrack.dto.DemandDailyAndMonthPopupDTO;
import com.bh.realtrack.dto.DemandVsAvailablilityDTO;
import com.bh.realtrack.dto.DemandVsCapacityTableDTO;
import com.bh.realtrack.dto.DemandVsCapacityTableMonthDTO;
import com.bh.realtrack.dto.DropDownDTO;
import com.bh.realtrack.dto.EditLocationTableDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.OFETeTDashboardDTO;
import com.bh.realtrack.dto.OFETeTDashboardManageProjectDTO;
import com.bh.realtrack.dto.OFETeTProjectTableDTO;
import com.bh.realtrack.dto.OFETeTProjectTableMonthDTO;
import com.bh.realtrack.dto.PnsDropDownDTO;
import com.bh.realtrack.dto.ResourcePnsTable;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.AssertUtils;
import com.bh.realtrack.util.OFETeTDashboardConstants;

/**
 * @author Thakur Aarthi
 */
@Repository
@SuppressWarnings("deprecation")
public class OFETeTDashboardDAOImpl implements OFETeTDashboardDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(OFETeTDashboardDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<DropDownDTO> getIprProjectNameFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_IPR_PROJECT_NAME_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getRoleNameFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ROLE_NAME_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getResourceNameFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_RESOURCE_NAME_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getLocationFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_LOCATION_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getRegionFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_REGION_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getActivityStatusFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ACTIVITY_STATUS_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(2));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getDemandTypeFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_TYPE_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<OFETeTDashboardManageProjectDTO> getManageProjectList(HeaderDashboardDetailsDTO filterValues,
			String projectId) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_MANAGE_PROJECT,
				new Object[] { projectId, filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<OFETeTDashboardManageProjectDTO>>() {
					List<OFETeTDashboardManageProjectDTO> list = new ArrayList<OFETeTDashboardManageProjectDTO>();

					public List<OFETeTDashboardManageProjectDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							OFETeTDashboardManageProjectDTO dto = new OFETeTDashboardManageProjectDTO();
							dto.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setBusinessUnit(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setSegment(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setCustomerName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setCompanyName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setCustomerId(String.valueOf(rs.getInt(7)) != null ? rs.getInt(7) : 0);
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<OFETeTProjectTableDTO> getTeTProjectTable(OFETeTDashboardDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_PROJECT_TABLE,
				new Object[] { filterValues.getProjectName(), filterValues.getRoleId(), filterValues.getResourceId(),
						filterValues.getRegion(), filterValues.getLocation(), filterValues.getActivityStatus(),
						filterValues.getStartDate(), filterValues.getEndDate(), filterValues.getPeriod(),
						filterValues.getDemandType(), filterValues.getProjectId() },
				new ResultSetExtractor<List<OFETeTProjectTableDTO>>() {
					List<OFETeTProjectTableDTO> list = new ArrayList<OFETeTProjectTableDTO>();

					public List<OFETeTProjectTableDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							OFETeTProjectTableDTO dto = new OFETeTProjectTableDTO();

							dto.setDays(String.valueOf(rs.getInt(1)) != null ? rs.getInt(1) : 0);
							dto.setAssetDays(String.valueOf(rs.getInt(2)) != null ? rs.getInt(2) : 0);
							dto.setRemainUnitsDay(String.valueOf(rs.getInt(3)) != null ? rs.getInt(3) : 0);
							dto.setResourceId(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setResourceName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setProjectId(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setActivityId(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setActivityName(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setRegion(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setLocation(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setStartDate(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setFinishDate(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setDayOut(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setRemianUnitsDayOut(String.valueOf(rs.getInt(14)) != null ? rs.getInt(14) : 0);
							dto.setRn(String.valueOf(rs.getInt(15)) != null ? rs.getInt(15) : 0);

							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<OFETeTProjectTableMonthDTO> getTeTProjectTableForMonth(OFETeTDashboardDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_PROJECT_TABLE_FOR_MONTH,
				new Object[] { filterValues.getProjectName(), filterValues.getRoleId(), filterValues.getResourceId(),
						filterValues.getRegion(), filterValues.getLocation(), filterValues.getActivityStatus(),
						filterValues.getStartDate(), filterValues.getEndDate(), filterValues.getPeriod(),
						filterValues.getDemandType(), filterValues.getProjectId() },
				new ResultSetExtractor<List<OFETeTProjectTableMonthDTO>>() {
					List<OFETeTProjectTableMonthDTO> list = new ArrayList<OFETeTProjectTableMonthDTO>();

					public List<OFETeTProjectTableMonthDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							OFETeTProjectTableMonthDTO dto = new OFETeTProjectTableMonthDTO();

							dto.setDays(rs.getInt(1));
							dto.setAssetDays(rs.getInt(2));
							dto.setRemainUnitsDay(rs.getInt(3));
							dto.setResourceId(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setResourceName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setProjectId(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setActivityId(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setActivityName(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setRegion(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setLocation(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setStartDate(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setFinishDate(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setMonthYear(null != rs.getString(13) ? rs.getString(13) : "");
							list.add(dto);
						}

						return list;
					}
				});

	}
	@Override
	public String getDemandLines(OFETeTDashboardDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_LINES_KPI,
				new Object[] { filterValues.getStartDate(), filterValues.getEndDate(), filterValues.getProjectName(),
						filterValues.getProjectName(), filterValues.getProjectId(), filterValues.getProjectId(),
						filterValues.getRoleId(), filterValues.getRoleId(), filterValues.getResourceId(),
						filterValues.getResourceId(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getLocation(), filterValues.getLocation(), filterValues.getActivityStatus(),
						filterValues.getActivityStatus() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getAssetsDemanded(OFETeTDashboardDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ASSETS_DEMANDED_KPI,
				new Object[] { filterValues.getStartDate(), filterValues.getEndDate(), filterValues.getProjectName(),
						filterValues.getProjectName(), filterValues.getProjectId(), filterValues.getProjectId(),
						filterValues.getRoleId(), filterValues.getRoleId(), filterValues.getResourceId(),
						filterValues.getResourceId(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getLocation(), filterValues.getLocation(), filterValues.getActivityStatus(),
						filterValues.getActivityStatus() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getAssetDays(OFETeTDashboardDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ASSET_DAYS_KPI,
				new Object[] { filterValues.getStartDate(), filterValues.getEndDate(), filterValues.getProjectName(),
						filterValues.getProjectName(), filterValues.getProjectId(), filterValues.getProjectId(),
						filterValues.getRoleId(), filterValues.getRoleId(), filterValues.getResourceId(),
						filterValues.getResourceId(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getLocation(), filterValues.getLocation(), filterValues.getActivityStatus(),
						filterValues.getActivityStatus() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getMaxDailyAssets(OFETeTDashboardDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_MAX_DAILY_ASSETS_KPI,
				new Object[] { filterValues.getStartDate(), filterValues.getEndDate(), filterValues.getProjectName(),
						filterValues.getProjectName(), filterValues.getProjectId(), filterValues.getProjectId(),
						filterValues.getRoleId(), filterValues.getRoleId(), filterValues.getResourceId(),
						filterValues.getResourceId(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getLocation(), filterValues.getLocation(), filterValues.getActivityStatus(),
						filterValues.getActivityStatus() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public List<EditLocationTableDTO> getTeTLocationTable() {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_LOCATION_TABLE, new Object[] {},
				new ResultSetExtractor<List<EditLocationTableDTO>>() {
					List<EditLocationTableDTO> list = new ArrayList<EditLocationTableDTO>();

					public List<EditLocationTableDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							EditLocationTableDTO dto = new EditLocationTableDTO();

							dto.setLocation(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setRegion(null != rs.getString(2) ? rs.getString(2) : "");

							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public boolean editLocationTable(List<EditLocationTableDTO> editLocationDto, String sso) {
		boolean result = false;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			for (EditLocationTableDTO dto : editLocationDto) {
				PreparedStatement pstm = con.prepareStatement(OFETeTDashboardConstants.EDIT_LOCATION_TABLE);
				pstm.setString(1, dto.getLocation());
				pstm.setString(2, dto.getRegion());
				pstm.setString(3, sso);

				rs = pstm.executeQuery();

				while (rs.next()) {

					result = rs.getBoolean(1);
				}
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while SAVING LOCATION DETAILS:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;

	}

	@Override
	public String getEditAccessTetDashboard(String sso) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_EDIT_ACCESS_TET_DASHBOARD, new Object[] { sso },
				new ResultSetExtractor<String>() {
					public String extractData(ResultSet rs) throws SQLException {
						String flag = "false";
						while (rs.next()) {
							if (rs.getString(1).equalsIgnoreCase("Y")) {
								flag = "true";
							}

						}
						return flag;
					}
				});
	}

	@Override
	public List<OFETeTProjectTableDTO> exportTeTDashboardExcelDownloadData(OFETeTDashboardDTO dto) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_TET_DASHBOARD_EXCEL,
				new Object[] { dto.getProjectName(), dto.getRoleId(), dto.getResourceId(), dto.getRegion(),
						dto.getLocation(), dto.getActivityStatus(), dto.getStartDate(), dto.getEndDate(),
						dto.getPeriod(), dto.getDemandType(), dto.getProjectId() },

				new ResultSetExtractor<List<OFETeTProjectTableDTO>>() {
					public List<OFETeTProjectTableDTO> extractData(ResultSet rs) throws SQLException {

						List<OFETeTProjectTableDTO> list = new ArrayList<OFETeTProjectTableDTO>();

						while (rs.next()) {

							OFETeTProjectTableDTO dto = new OFETeTProjectTableDTO();
							dto.setDays(String.valueOf(rs.getInt(1)) != null ? rs.getInt(1) : 0);
							dto.setAssetDays(String.valueOf(rs.getInt(2)) != null ? rs.getInt(2) : 0);
							dto.setRemainUnitsDay(String.valueOf(rs.getInt(3)) != null ? rs.getInt(3) : 0);
							dto.setResourceId(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setResourceName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setProjectId(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setActivityId(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setActivityName(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setRegion(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setLocation(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setStartDate(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setFinishDate(null != rs.getString(12) ? rs.getString(12) : "");
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<AssetTrackerDataDTO> readAssetTrackerData() {
		LOGGER.info("Started Reading AssetTrackerData");
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_READ_ASSET_TRACKER_DATA, new Object[] {},

				new ResultSetExtractor<List<AssetTrackerDataDTO>>() {
					public List<AssetTrackerDataDTO> extractData(ResultSet rs) throws SQLException {

						List<AssetTrackerDataDTO> list = new ArrayList<AssetTrackerDataDTO>();

						while (rs.next()) {

							AssetTrackerDataDTO dto = new AssetTrackerDataDTO();
							dto.setId(rs.getString(1) != null ? rs.getString(1) : "");
							dto.setJson(rs.getString(2) != null ? rs.getString(2) : "");
							dto.setInsertDate(rs.getString(3) != null ? rs.getString(3) : "");
							dto.setInsertBy(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setRealtrackStatus(null != rs.getString(5) ? rs.getString(5) : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public int insertDataIntoStageTable(List<AssetTrackerStageDTO> stageDtoList, String sso) {
		int result = 0;
		int batchIndex = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();

			PreparedStatement pstm = con.prepareStatement(OFETeTDashboardConstants.SAVE_ASSET_TRACKER_DATA_INTO_STAGE);
			if (AssertUtils.isListNotEmpty(stageDtoList)) {
				for (int i = 0; i < stageDtoList.size(); i++) {
					// log.info("insert into armDataDetails list");
					pstm.setString(1, stageDtoList.get(i).getId());
					pstm.setString(2, String.valueOf(stageDtoList.get(i).getPart_number()));
					pstm.setString(3, stageDtoList.get(i).getCurrent_facility());
					pstm.setString(4, stageDtoList.get(i).getStatus());
					pstm.setString(5, String.valueOf(stageDtoList.get(i).getSerial_number()));
					pstm.setString(6, String.valueOf(stageDtoList.get(i).getCostnew()));
					pstm.setString(7, String.valueOf(stageDtoList.get(i).getRepair_cost()));
					pstm.setString(8, String.valueOf(stageDtoList.get(i).getCalibration_freq()));
					pstm.setString(9, String.valueOf(stageDtoList.get(i).getCalibration_freq_uom()));
					pstm.setString(10, String.valueOf(stageDtoList.get(i).getMaintainance_freq()));
					pstm.setString(11, String.valueOf(stageDtoList.get(i).getMaintainance_freq_uom()));
					pstm.setString(12, sso);
					pstm.setString(13, String.valueOf(stageDtoList.get(i).getCalib_date()));
					pstm.setString(14, String.valueOf(stageDtoList.get(i).getMaintainance_date()));
					pstm.setString(15, stageDtoList.get(i).getDescription());
					pstm.setString(16, stageDtoList.get(i).getRealtrackStatus());
					pstm.addBatch();
					batchIndex++;
					if (batchIndex % 500 == 0) {
						pstm.executeBatch();
						result = 1;
					}
				}
				pstm.executeBatch();
				result = 1;
			}
		} catch (Exception e) {
			LOGGER.error("Something went wrong while inserting Data Into Stage Table{} :{}" , e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	@Override
	public List<DropDownDTO> getResourceFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_CAPACITY_RESOURSE_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(2));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getTrackIdFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_TRACK_ID_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getStatusFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_CAPACITY_STATUS_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getCapacityAtLocationFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_CAPACITY_AT_LOCATION_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getCapacityP6LocationFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_CAPACITY_P6_LOCATION_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getPartNumberFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_CAPACITY_PART_NO_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getCapacityRegionFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_CAPACITY_REGION_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<ResourcePnsTable> getTeTDashboardResourcePnsTable() {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_RESOURCE_PNS_TABLE_DATA, new Object[] {},

				new ResultSetExtractor<List<ResourcePnsTable>>() {
					public List<ResourcePnsTable> extractData(ResultSet rs) throws SQLException {

						List<ResourcePnsTable> list = new ArrayList<ResourcePnsTable>();

						while (rs.next()) {

							ResourcePnsTable dto = new ResourcePnsTable();
							dto.setResource_id(rs.getString(1) != null ? rs.getString(1) : "");
							dto.setResource_name(rs.getString(2) != null ? rs.getString(2) : "");
							dto.setResource_id_name(rs.getString(3) != null ? rs.getString(3) : "");
							dto.setPart_numbers(rs.getString(4) != null ? rs.getString(4) : "");
							dto.setAttribute1(rs.getString(5) != null ? rs.getString(5) : "");
							dto.setAttribute2(rs.getString(6) != null ? rs.getString(6) : "");
							dto.setAttribute3(rs.getString(7) != null ? rs.getString(7) : "");
							dto.setAttribute4(rs.getString(8) != null ? rs.getString(8) : "");
							dto.setAttribute5(rs.getString(9) != null ? rs.getString(9) : "");
							dto.setAttribute6(rs.getString(10) != null ? rs.getString(10) : "");
							dto.setAttribute7(rs.getString(11) != null ? rs.getString(11) : "");
							dto.setPreference(rs.getString(12) != null ? rs.getString(12) : "");
							dto.setDescription(rs.getString(13) != null ? rs.getString(13) : "");
							dto.setGroup(rs.getString(14) != null ? rs.getString(14) : "");
							
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<PnsDropDownDTO> getResourceIDFilter() {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_RESOURCE_ID_FILTER, new Object[] {},
				new ResultSetExtractor<List<PnsDropDownDTO>>() {
					List<PnsDropDownDTO> list = new ArrayList<PnsDropDownDTO>();

					public List<PnsDropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							PnsDropDownDTO dto = new PnsDropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							dto.setId(rs.getString(2));
							dto.setName(rs.getString(3));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getResourceNameFilter() {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_RESOURCE_NAME_PNS_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getPartNumberFilter() {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_PART_NUMBER_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public int deleteResourcePns(ResourcePnsTable resourcePnsTable) {
		int result = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			PreparedStatement pstm = con.prepareStatement(OFETeTDashboardConstants.DELETE_RESOURCE_PNS);
			pstm.setString(1, resourcePnsTable.getResource_id());
			pstm.setString(2, resourcePnsTable.getResource_name());
			pstm.setString(3, resourcePnsTable.getPart_numbers());
			if (pstm.executeUpdate() > 0) {
				result = 1;
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while DELETING ACTION DETAILS :{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	@Override
	public int updateResourcePns(List<ResourcePnsTable> resourcePnsTable, String sso) {
		int result = 0;
		int batchIndex = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();

			PreparedStatement pstm = con.prepareStatement(OFETeTDashboardConstants.UPDATE_RESOURCE_PNS);
			if (AssertUtils.isListNotEmpty(resourcePnsTable)) {
				for (int i = 0; i < resourcePnsTable.size(); i++) {
					pstm.setString(1, resourcePnsTable.get(i).getResource_id());
					pstm.setString(2, resourcePnsTable.get(i).getResource_name());
					pstm.setString(3, resourcePnsTable.get(i).getPart_numbers());
					pstm.setString(4, resourcePnsTable.get(i).getAttribute1());
					pstm.setString(5, resourcePnsTable.get(i).getAttribute2());
					pstm.setString(6, resourcePnsTable.get(i).getAttribute3());
					pstm.setString(7, resourcePnsTable.get(i).getAttribute4());
					pstm.setString(8, resourcePnsTable.get(i).getAttribute5());
					pstm.setString(9, resourcePnsTable.get(i).getAttribute6());
					pstm.setString(10, resourcePnsTable.get(i).getAttribute7());
					pstm.setString(11, resourcePnsTable.get(i).getPreference());
					pstm.setString(12, resourcePnsTable.get(i).getDescription());
					pstm.setString(13, resourcePnsTable.get(i).getGroup());
					pstm.setString(14, resourcePnsTable.get(i).getOldResource_id());
					pstm.setString(15, resourcePnsTable.get(i).getOldResource_name());
					pstm.setString(16, resourcePnsTable.get(i).getOldPart_numbers());
					pstm.addBatch();
					batchIndex++;
					if (batchIndex % 500 == 0) {
						pstm.executeBatch();
						result = 1;
					}
				}
				pstm.executeBatch();
				result = 1;
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while SAVING armDataDetails :{}" , e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	@Override
	public int saveResourcePns(List<ResourcePnsTable> resourcePnsTable, String sso) {
		int result = 0;
		int batchIndex = 0;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();

			PreparedStatement pstm = con.prepareStatement(OFETeTDashboardConstants.SAVE_RESOURCE_PNS);
			if (AssertUtils.isListNotEmpty(resourcePnsTable)) {
				for (int i = 0; i < resourcePnsTable.size(); i++) {
					pstm.setString(1, resourcePnsTable.get(i).getResource_id());
					pstm.setString(2, resourcePnsTable.get(i).getResource_name());
					pstm.setString(3, resourcePnsTable.get(i).getPart_numbers());
					pstm.setString(4, resourcePnsTable.get(i).getAttribute1());
					pstm.setString(5, resourcePnsTable.get(i).getAttribute2());
					pstm.setString(6, resourcePnsTable.get(i).getAttribute3());
					pstm.setString(7, resourcePnsTable.get(i).getAttribute4());
					pstm.setString(8, resourcePnsTable.get(i).getAttribute5());
					pstm.setString(9, resourcePnsTable.get(i).getAttribute6());
					pstm.setString(10, resourcePnsTable.get(i).getAttribute7());
					pstm.setString(11, resourcePnsTable.get(i).getPreference());
					pstm.setString(12, resourcePnsTable.get(i).getDescription());
					pstm.setString(13, resourcePnsTable.get(i).getGroup());
					pstm.addBatch();
					batchIndex++;
					if (batchIndex % 500 == 0) {
						pstm.executeBatch();
						result = 1;
					}
				}
				pstm.executeBatch();
				result = 1;
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while SAVING armDataDetails :{}" , e.getMessage());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	@Override
	public List<AtLocationDTO> getTeTDashboardATLocationTable() {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_AT_LOCATION_TABLE_DATA, new Object[] {},

				new ResultSetExtractor<List<AtLocationDTO>>() {
					public List<AtLocationDTO> extractData(ResultSet rs) throws SQLException {

						List<AtLocationDTO> list = new ArrayList<AtLocationDTO>();

						while (rs.next()) {

							AtLocationDTO dto = new AtLocationDTO();
							dto.setAt_location(rs.getString(1) != null ? rs.getString(1) : "");
							dto.setRegion(rs.getString(2) != null ? rs.getString(2) : "");
							dto.setP6_location(rs.getString(3) != null ? rs.getString(3) : "");

							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public int updateAtLocationTable(List<AtLocationDTO> atLocationDTO, String sso) {
		boolean result = false;
		int count = 0;
		ResultSet rs = null;
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			for (AtLocationDTO dto : atLocationDTO) {
				PreparedStatement pstm = con.prepareStatement(OFETeTDashboardConstants.UPDATE_AT_LOCATION_TABLE_DATA);
				pstm.setString(1, dto.getAt_location());
				pstm.setString(2, dto.getRegion());
				pstm.setString(3, dto.getP6_location());
				pstm.setString(4, sso);

				rs = pstm.executeQuery();

				while (rs.next()) {

					result = rs.getBoolean(1);
					if (result) {
						count = 1;
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("something went wrong while SAVING LOCATION DETAILS:{}" , e.getMessage());
			throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return count;
	}

	@Override
	public List<AtCapacityDTO> getTeTDashboardAtCapacityTable(AtCapacityFiltersDTO filterValues) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_AT_CAPACITY_TABLE_DATA,
				new Object[] { filterValues.getResource_id(), filterValues.getResource_id(), filterValues.getStatus(),
						filterValues.getStatus(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getCurrent_facility(), filterValues.getCurrent_facility(),
						filterValues.getP6_location(), filterValues.getP6_location(), filterValues.getPart_numbers(),
						filterValues.getPart_numbers(), filterValues.getTrackId(), filterValues.getTrackId() },
				new ResultSetExtractor<List<AtCapacityDTO>>() {
					public List<AtCapacityDTO> extractData(ResultSet rs) throws SQLException {
						List<AtCapacityDTO> list = new ArrayList<AtCapacityDTO>();
						while (rs.next()) {
							AtCapacityDTO dto = new AtCapacityDTO();
							dto.setResource_id(rs.getString(1) != null ? rs.getString(1) : "");
							dto.setResource_name(rs.getString(2) != null ? rs.getString(2) : "");
							dto.setStatus(rs.getString(3) != null ? rs.getString(3) : "");
							dto.setRegion(rs.getString(4) != null ? rs.getString(4) : "");
							dto.setCurrent_facility(rs.getString(5) != null ? rs.getString(5) : "");
							dto.setP6_location(rs.getString(6) != null ? rs.getString(6) : "");
							dto.setPart_numbers(rs.getString(7) != null ? rs.getString(7) : "");
							dto.setId(rs.getString(8) != null ? rs.getString(8) : "");
							dto.setSerial_number(rs.getString(9) != null ? rs.getString(9) : "");
							dto.setCostnew(rs.getString(10) != null ? rs.getString(10) : "");
							dto.setRepair_cost(rs.getString(11) != null ? rs.getString(11) : "");
							dto.setNext_calib_dt(rs.getString(12) != null ? rs.getString(12) : "");
							dto.setCalibration_freq(rs.getString(13) != null ? rs.getString(13) : "");
							dto.setCalibration_freq_uom(rs.getString(14) != null ? rs.getString(14) : "");
							dto.setMaintainance_freq(rs.getString(15) != null ? rs.getString(15) : "");
							dto.setMaintainance_freq_uom(rs.getString(16) != null ? rs.getString(16) : "");
							dto.setNext_maintainance_dt(rs.getString(17) != null ? rs.getString(17) : "");
							dto.setTotal(rs.getString(18) != null ? rs.getString(18) : "");
							dto.setDescription(rs.getString(19) != null ? rs.getString(19) : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public String getAssetCount(AtCapacityFiltersDTO filterValues) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ASSET_COUNT_KPI,
				new Object[] { filterValues.getResource_id(), filterValues.getResource_id(), filterValues.getStatus(),
						filterValues.getStatus(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getCurrent_facility(), filterValues.getCurrent_facility(),
						filterValues.getP6_location(), filterValues.getP6_location(), filterValues.getPart_numbers(),
						filterValues.getPart_numbers(), filterValues.getTrackId(), filterValues.getTrackId() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getActiveAssetCount(AtCapacityFiltersDTO filterValues) {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ACTIVE_ASSET_COUNT_KPI,
				new Object[] { filterValues.getResource_id(), filterValues.getResource_id(), filterValues.getStatus(),
						filterValues.getStatus(), filterValues.getRegion(), filterValues.getRegion(),
						filterValues.getCurrent_facility(), filterValues.getCurrent_facility(),
						filterValues.getP6_location(), filterValues.getP6_location(), filterValues.getPart_numbers(),
						filterValues.getPart_numbers(), filterValues.getTrackId(), filterValues.getTrackId() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public void deleteDataFromAssetTrackerStage() {
		jdbcTemplate.update(OFETeTDashboardConstants.DELETE_DATA_FROM_ASSET_TRACKER_STAGE, new Object[] {});

	}

	@Override
	public void deleteDataFromAssetTrackerTarget() {
		jdbcTemplate.update(OFETeTDashboardConstants.DELETE_DATA_FROM_ASSET_TRACKER_TARGET, new Object[] {});

	}

	@Override
	public int insertDataIntoTargetTable(List<AssetTrackerStageDTO> stageDtoList, String sso) {
		return jdbcTemplate.query(OFETeTDashboardConstants.SAVE_ASSET_TRACKER_INTO_TARGET, new Object[] {},
				new ResultSetExtractor<Integer>() {
					public Integer extractData(ResultSet rs) throws SQLException {
						int result = 0;
						while (rs.next()) {
							LOGGER.info("procedure call result {}" , rs.getString(1));
							if (rs.getString(1).equalsIgnoreCase("true")) {
								result = 1;
							} else {
								result = 0;
							}
							;
						}
						return result;
					}
				});
	}

	@Override
	public List<DropDownDTO> getTeTDashboardCapacityFilters() {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_AT_CAPACITY_P6_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getDCProjectFilters(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_PROJECT_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getDCRoleFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_ROLE_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}
	
	@Override
	public List<DropDownDTO> getP6DemandRegionFilter(HeaderDashboardDetailsDTO filterValues){
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_P6_DEMAND_REGION_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});
	}
	
	@Override
	public List<DropDownDTO> getP6DemandLocationFilter(HeaderDashboardDetailsDTO filterValues){
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_P6_DEMAND_LOCATION_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<DropDownDTO> getDCResourceFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_RESOURCE_FILTER,
				new Object[] { filterValues.getProjectId(), filterValues.getBusiness(), filterValues.getSegment(),
						filterValues.getRegion(), filterValues.getCustomerId(), filterValues.getDemandType() },
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getDCLocationFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_LOCATION_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getDCRegionFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_REGION_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getDCActivityStatusFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_ACTIVITY_STATUS_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DropDownDTO> getMinExcessFilter(HeaderDashboardDetailsDTO filterValues) {

		return jdbcTemplate.query(OFETeTDashboardConstants.GET_DEMAND_CAPACITY_MIN_EXCESS_FILTER, new Object[] {},
				new ResultSetExtractor<List<DropDownDTO>>() {
					List<DropDownDTO> list = new ArrayList<DropDownDTO>();

					public List<DropDownDTO> extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							DropDownDTO dto = new DropDownDTO();
							dto.setKey(rs.getString(1));
							dto.setVal(rs.getString(1));
							list.add(dto);
						}

						return list;
					}
				});

	}

	@Override
	public List<DemandVsCapacityTableDTO> downloadDemandAndCpacityExcel(DemandVsAvailablilityDTO dto) {
		String query = "";

		if (dto.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_DEMAND_CAPACITY_TOGGLE_ON_DETAILS;
		} else {
			query = OFETeTDashboardConstants.GET_DEMAND_CAPACITY_TOGGLE_OFF_DETAILS;
		}
		return jdbcTemplate.query(query,
				new Object[] {},
				new ResultSetExtractor<List<DemandVsCapacityTableDTO>>() {
					public List<DemandVsCapacityTableDTO> extractData(ResultSet rs) throws SQLException {
						List<DemandVsCapacityTableDTO> list = new ArrayList<DemandVsCapacityTableDTO>();
						while (rs.next()) {
							DemandVsCapacityTableDTO dto = new DemandVsCapacityTableDTO();
							dto.setWorstExcess(String.valueOf(rs.getInt(1)) != null ? rs.getInt(1) : 0);
							dto.setWorstGap(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setResourseId(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setResourseName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setDay(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setSurplus(String.valueOf(rs.getInt(6)) != null ? rs.getInt(6) : 0);
							dto.setRegion(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setLocation(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setGlobalP6Demo(String.valueOf(rs.getInt(9)) != null ? rs.getInt(9) : 0);
							dto.setAssets(String.valueOf(rs.getInt(10)) != null ? rs.getInt(10) : 0);
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public String getToCoverGaps(DemandVsAvailablilityDTO filterValues) {
		String query = "";
		if (filterValues.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_TO_COVER_GAPS_KPI_WITH_TOGGLE;
		} else {
			query = OFETeTDashboardConstants.GET_TO_COVER_GAPS_KPI_WITH_OUT_TOGGLE;
		}
		return jdbcTemplate.query(query,
				new Object[] {},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getWorstDayGapCount(DemandVsAvailablilityDTO filterValues) {
		String query = "";
		if (filterValues.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_WORST_DAY_GAP_COUNT_KPI_WITH_TOGGLE;
		} else {
			query = OFETeTDashboardConstants.GET_WORST_DAY_GAP_COUNT_KPI_WITH_OUT_TOGGLE;
		}
		return jdbcTemplate.query(query,
				new Object[] {},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getAssetsCount(DemandVsAvailablilityDTO filterValues) {
		String query = "";
		if (filterValues.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_ASSETS_COUNT_KPI_WITH_TOGGLE;
		} else {
			query = OFETeTDashboardConstants.GET_ASSETS_COUNT_KPI_WITH_OUT_TOGGLE;
		}
		return jdbcTemplate.query(query,
				new Object[] {},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getMinExcessCount(DemandVsAvailablilityDTO filterValues) {
		String query = "";
		if (filterValues.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_MIN_EXCESS_COUNT_KPI_WITH_TOGGLE;
		} else {
			query = OFETeTDashboardConstants.GET_MIN_EXCESS_COUNT_KPI_WITH_OUT_TOGGLE;
		}
		return jdbcTemplate.query(query,
				new Object[] {},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getAssetsWoMinExcessCount(DemandVsAvailablilityDTO filterValues) {
		String query = "";
		if (filterValues.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_ASSETS_WO_MIN_EXCESS_KPI_WITH_TOGGLE;
		} else {
			query = OFETeTDashboardConstants.GET_ASSETS_WO_MIN_EXCESS_KPI_WITH_OUT_TOGGLE;
		}
		return jdbcTemplate.query(query,
				new Object[] {},
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public List<DemandDailyAndMonthPopupDTO> getDemandDailyAndMonthPopupList(String resourceId, String resourceName,
			String projectId, String date, boolean flag,String p6Region, String p6Location) {
		String query = "";

		if (flag == true) {
			query = OFETeTDashboardConstants.GET_DEMAND_DAILY_POPUP_DETAILS;
		} else {
			
			query = OFETeTDashboardConstants.GET_DEMAND_DAILY_POPUP_DETAILS;
		}
		return jdbcTemplate.query(query, new Object[] { resourceId, resourceName, projectId, projectId, p6Region, p6Region, p6Location, p6Location, date },
				new ResultSetExtractor<List<DemandDailyAndMonthPopupDTO>>() {
					public List<DemandDailyAndMonthPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<DemandDailyAndMonthPopupDTO> list = new ArrayList<DemandDailyAndMonthPopupDTO>();
						while (rs.next()) {
							DemandDailyAndMonthPopupDTO dto = new DemandDailyAndMonthPopupDTO();
							dto.setResourseId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setResourseName(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setProjectId(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setActivityId(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setActivityName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setVenderSiteLocation(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setUnitsDay(String.valueOf(rs.getInt(8)) != null ? rs.getInt(8) : 0);
							dto.setStartDate(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setFinishDate(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setDay(null != rs.getString(11) ? rs.getString(11) : "");
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public List<AssetDailyAndMonthlyPopupDTO> getAssetDailyAndMonthlyPopupList(String resourceId, String resourceName, String projectId,
			String status, String date, boolean flag,String atRegion, String atLocation, 
			String p6Region, String p6Location, String role) {
		String query = "";

		if (flag == true) {
			query = OFETeTDashboardConstants.GET_ASSETS_MONTHLY_POPUP_DETAILS;
		} 
		return jdbcTemplate.query(query, 
				new Object[] { projectId, projectId, role, role, resourceId, resourceName,
				p6Region, p6Region, p6Location, p6Location, date, projectId, projectId,
				resourceId, resourceName, atRegion, atRegion, status, status, atLocation,
				atLocation, date },
				new ResultSetExtractor<List<AssetDailyAndMonthlyPopupDTO>>() {
					public List<AssetDailyAndMonthlyPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<AssetDailyAndMonthlyPopupDTO> list = new ArrayList<AssetDailyAndMonthlyPopupDTO>();
						while (rs.next()) {
							AssetDailyAndMonthlyPopupDTO dto = new AssetDailyAndMonthlyPopupDTO();
							dto.setRegion(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setCurrentFacility(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setpSixLocation(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setPartNumber(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setId(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setSerialNumber(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setStatus(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setDescription(null != rs.getString(8) ? rs.getString(8) : "");			
							list.add(dto);
							
						}

						return list;
					}
				});
	}
	
	
	@Override
	public List<AssetDailyAndMonthlyPopupDTO> getAssetDailyPopupList(String resourceId, String resourceName, String projectId, String status,
			String date, boolean flag,String atRegion, String atLocation, String p6Region,
			String p6Location, String role) {
	
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ASSETS_DAILY_POPUP_DETAILS, 
				new Object[] {projectId, projectId, role, role, resourceId, resourceName,
						p6Region, p6Region, p6Location, p6Location, date, projectId, projectId,
						resourceId, resourceName, atRegion, atRegion, status, status, atLocation,
						atLocation, date},
				new ResultSetExtractor<List<AssetDailyAndMonthlyPopupDTO>>() {
					public List<AssetDailyAndMonthlyPopupDTO> extractData(ResultSet rs) throws SQLException {
						List<AssetDailyAndMonthlyPopupDTO> list = new ArrayList<AssetDailyAndMonthlyPopupDTO>();
						while (rs.next()) {
							AssetDailyAndMonthlyPopupDTO dto = new AssetDailyAndMonthlyPopupDTO();
							dto.setRegion(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setCurrentFacility(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setpSixLocation(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setPartNumber(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setId(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setSerialNumber(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setStatus(null != rs.getString(7) ? rs.getString(7) : "");				
							list.add(dto);
						}

						return list;
					}
				});
	}
	
	@Override
	public List<DemandVsCapacityTableMonthDTO> getDemandVsCapacityMonthTableList(DemandVsAvailablilityDTO dto) {
		
		String query = "";
		if (dto.getGapToggle().equalsIgnoreCase("Y")) {
			query = OFETeTDashboardConstants.GET_DEMAND_VS_CAPACITY_MONTH_BOTH_TOGGLE_ON;
		} else {
			query = OFETeTDashboardConstants.GET_DEMAND_VS_CAPACITY_GAP_OFF_AND_MONTH_ON;
		}
		return jdbcTemplate.query(query, new Object[] {},
				new ResultSetExtractor<List<DemandVsCapacityTableMonthDTO>>() {
					public List<DemandVsCapacityTableMonthDTO> extractData(ResultSet rs) throws SQLException {
						List<DemandVsCapacityTableMonthDTO> list = new ArrayList<DemandVsCapacityTableMonthDTO>();
						while (rs.next()) {
							DemandVsCapacityTableMonthDTO dto = new DemandVsCapacityTableMonthDTO();
							dto.setWorstExcess(String.valueOf(rs.getInt(1)) != null ? rs.getInt(1) : 0);
							dto.setWorstGap(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setResourseId(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setResourseName(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setSurplus(String.valueOf(rs.getInt(5)) != null ? rs.getInt(5) : 0);
							dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setLocation(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setGlobalP6Demo(String.valueOf(rs.getInt(8)) != null ? rs.getInt(8) : 0);
							dto.setAssets(String.valueOf(rs.getInt(9)) != null ? rs.getInt(9) : 0);
							dto.setDayOut(null != rs.getString(10) ? rs.getString(10) : "");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public String initialFunctionCallForView(DemandVsAvailablilityDTO dto) {
		String query = "";
			query = OFETeTDashboardConstants.GET_DEMAND_VS_CAPACITY_INITIAL_FUNCTION;
		
		return jdbcTemplate.query(query,
				new Object[] { dto.getProjectId(), dto.getRoleId(), dto.getActivityStatus(), dto.getResourceId(),
						dto.getRegion(), dto.getP6Region(), dto.getLocation(), dto.getP6Location(), dto.getMinExcess(), dto.getStartDate(), dto.getEndDate() },
				new ResultSetExtractor<String>() {
					String count = new String();

					public String extractData(ResultSet rs) throws SQLException {
						while (rs.next()) {
							count = rs.getString(1);
						}
						return count;
					}
				});
	}

	@Override
	public String getP6LastUpdatedDate() {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_P6_LAST_UPDATED_DATE, new Object[]{},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException {
						String date="";
						while(rs.next()){
							date = null != rs.getString(1) ? rs.getString(1):"";
						}
						return date;
					}
				});
	}

	@Override
	public String getAssetTrackerLastUpdatedDate() {
		return jdbcTemplate.query(OFETeTDashboardConstants.GET_ASSET_TRACKER_LAST_UPDATED_DATE, new Object[]{},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException {
						String date="";
						while(rs.next()){
							date = null != rs.getString(1) ? rs.getString(1):"";
						}
						return date;
					}
				});
	}

}
