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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.bh.realtrack.dao.OfeLandingDashboardDAO;
import com.bh.realtrack.dto.BillingDashboardFilterDTO;
import com.bh.realtrack.dto.HeaderDashboardDetailsDTO;
import com.bh.realtrack.dto.LandingBillingKpiDTO;
import com.bh.realtrack.dto.LandingBubbleChartDTO;
import com.bh.realtrack.dto.LandingBubbleTrendChartDTO;
import com.bh.realtrack.dto.LandingKPITrendChartDTO;
import com.bh.realtrack.dto.LandingManageKPI;
import com.bh.realtrack.dto.LandingManageKPIDisplayData;
import com.bh.realtrack.dto.LandingProjectDetailsDTO;
import com.bh.realtrack.dto.ManageProjectResponseDTO;
import com.bh.realtrack.enumerators.ErrorCode;
import com.bh.realtrack.util.OfeLandingDashboardConstants;

@Repository
public class OfeLandingDashboardDAOImpl implements OfeLandingDashboardDAO{

	private static final Logger LOGGER = LoggerFactory.getLogger(OfeBillingDashboardDAOImpl.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<ManageProjectResponseDTO> getmanageProjectList(HeaderDashboardDetailsDTO headerDetails,
			String projectId) {
		
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_MANAGE_PROJECTS,
				new Object[] {projectId, headerDetails.getCompanyId(), headerDetails.getBusiness(),
						headerDetails.getSegment(), headerDetails.getRegion(), headerDetails.getCustomerId()},
				new ResultSetExtractor<List<ManageProjectResponseDTO>>() {
					public List<ManageProjectResponseDTO> extractData(ResultSet rs) throws SQLException {
						List<ManageProjectResponseDTO> list = new ArrayList<ManageProjectResponseDTO>();
						while (rs.next()) {
							ManageProjectResponseDTO dto = new ManageProjectResponseDTO();

							dto.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setMasterProjectName(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setBusinessUnit(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setSegment(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setCustomerName(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setCompanyName(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setRegion(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setCustomerId(rs.getInt(8));
							list.add(dto);
						}

						return list;
					}
				});
	}

	@Override
	public String getOverdueKpi(BillingDashboardFilterDTO filterValues, String startDate) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_OVERDUE_KPI,
				new Object[] {filterValues.getProjectList(), filterValues.getProjectList(),startDate},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) {
						String kpi = new String();
						try {
							while (rs.next()) {
								kpi = rs.getString(1);
							}
						} catch (SQLException e) {
							throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
						}
						return kpi;
					}	
		});
	}

	@Override
	public String getP90CycleTime(BillingDashboardFilterDTO filterValues, String endDate) {		
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_P90_CYCLE_TIME,
			new Object[] {filterValues.getProjectList(),filterValues.getProjectList(),filterValues.getProjectList(),
					filterValues.getProjectList(),endDate, endDate, endDate, endDate},
			new ResultSetExtractor<String>() {
			@Override
				public String extractData(ResultSet rs) throws SQLException, DataAccessException {
					String kpi = new String();
					try {
						while(rs.next()){
							kpi= rs.getString("p90of_bm");
						}
					}catch(SQLException e) {
						throw new ServerErrorException(ErrorCode.INTERNAL.getResponseStatus(), e);
					}				
					return kpi;
				}	
		}) ;
	}

	@Override
	public List<LandingBillingKpiDTO> getCurrentActualOtrDifference(BillingDashboardFilterDTO filterValues, String startDate, String endDate) {
		
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_CURRENT_ACTUAL_OTR_DIFFERENCE,
				new Object[] {filterValues.getProjectList(),filterValues.getProjectList(), startDate, endDate, 
					filterValues.getProjectList(), filterValues.getProjectList(),filterValues.getBillingDateFlag()
					,startDate, endDate, startDate, endDate, filterValues.getProjectList(), filterValues.getProjectList(), 
					startDate, endDate, filterValues.getBillingDateFlag(), startDate, 
					filterValues.getBillingDateFlag(), endDate, filterValues.getBillingDateFlag(), startDate, 
					filterValues.getProjectList(), filterValues.getProjectList(), startDate, 
					filterValues.getProjectList(), filterValues.getProjectList(), startDate, filterValues.getProjectList(),
					filterValues.getProjectList(), filterValues.getBillingDateFlag(), filterValues.getBillingDateFlag()
					,startDate, filterValues.getBillingDateFlag(), endDate},
				new ResultSetExtractor<List<LandingBillingKpiDTO>>() {
					@Override
					public List<LandingBillingKpiDTO> extractData(ResultSet rs) throws SQLException {
						List<LandingBillingKpiDTO> list = new ArrayList<LandingBillingKpiDTO>();
						while(rs.next()) {
							LandingBillingKpiDTO dto = new LandingBillingKpiDTO();
							dto.setLeVsFBlDifference(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setLeVsFBl(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setActualVsLeCwDifference(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setActualVsLeCwStatus(null != rs.getString(4) ? rs.getString(4) : "");
							list.add(dto);
						}
						return list;
					}	
			});
	}

	@Override
	public List<LandingBubbleChartDTO> getBubbleChartData(LandingBubbleChartDTO chartValues) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_BUBBLE_CHART, 
				new Object[] {chartValues.getProjectName()},
				new ResultSetExtractor<List<LandingBubbleChartDTO>>() {
					@Override
					public List<LandingBubbleChartDTO> extractData(ResultSet rs) throws SQLException{
						List<LandingBubbleChartDTO> list = new ArrayList<LandingBubbleChartDTO>();
						while(rs.next()) {
							LandingBubbleChartDTO dto = new LandingBubbleChartDTO();
							dto.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setProjectManager(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setProgressChange(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setMarginChange(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setCurrentPMR(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setCM(null != rs.getString(7) ? rs.getString(7) : "");
							list.add(dto);
						}
						return list;
					}		
		});
	}

	@Override
	public List<LandingBubbleTrendChartDTO> getTrendingChartData(String projectId) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_TRENDING_CHART_DATA,
				new Object [] {projectId},
				new ResultSetExtractor<List<LandingBubbleTrendChartDTO>>() {

					@Override
					public List<LandingBubbleTrendChartDTO> extractData(ResultSet rs)
							throws SQLException{
						List<LandingBubbleTrendChartDTO> list = new ArrayList<LandingBubbleTrendChartDTO>();
						while(rs.next()) {
							LandingBubbleTrendChartDTO dto = new LandingBubbleTrendChartDTO();
							dto.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setProjectManager(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setProjectValue(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setVarToBL(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setCmExpansion(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setCM(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setInsert_dt(null != rs.getString(8) ? rs.getString(8) : "");
							list.add(dto);
						}
						return list;
					}		
		});
	}
	
	@Override
	public List<String> getKPICodeDropdown() {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_CODE_DROPDOWN,
				new Object[] {},
				new ResultSetExtractor<List<String>>() {

					@Override
					public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<String> list = new ArrayList<String>();
						while(rs.next()) {
							String kpiCode = rs.getString(1);
							list.add(kpiCode);
						}
						return list;
					}
			
		});
	} 
	
	@Override
	public List<String> getBusinessDropdown(int companyId) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_BUSINESS_DROPDOWN,
				new Object[] {companyId},
				new ResultSetExtractor<List<String>>() {

					@Override
					public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<String> list = new ArrayList<String>();
						while(rs.next()) {
							String businessUnit = rs.getString(1);
							list.add(businessUnit);
						}
						return list;
					}	
		});
	}

	@Override
	public List<LandingManageKPIDisplayData> getManageKPIData(int companyId,String kpiCode, String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_MANAGE_KPI_DATA,
				new Object[] {companyId,kpiCode,businessUnit},
				new ResultSetExtractor<List<LandingManageKPIDisplayData>>() {

					@Override
					public List<LandingManageKPIDisplayData> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingManageKPIDisplayData> list = new ArrayList<LandingManageKPIDisplayData>();
						while(rs.next()) {
							LandingManageKPIDisplayData dto = new LandingManageKPIDisplayData();
							dto.setKpiCode(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setBusinessUnit(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setTargetValue(rs.getDouble(3));
							dto.setRedThreshold(null !=rs.getString(4) ? rs.getString(4): "");
							dto.setAmberThreshold(null !=rs.getString(5) ? rs.getString(5): "");
							dto.setGreenThreshold(null !=rs.getString(6) ? rs.getString(6): "");
							dto.setDefaultThreshold(rs.getBoolean(7));
							dto.setCompanyId(companyId);
							list.add(dto);
						}
						return list;
					}		
		});
	}

	@Override
	public boolean saveLandingManageKPI(LandingManageKPIDisplayData manageKPIDTO, String sso) {
		boolean resultFlag = false;
		
		int count = checkIfKPIAndBusinessExist(manageKPIDTO);
		LOGGER.info("Check KPI and Business Exist Count = {}",count);
		Connection con = null;
		if(count>=1) {
			
		  try{
				con = jdbcTemplate.getDataSource().getConnection();
				PreparedStatement pstmt = con.prepareStatement(OfeLandingDashboardConstants.UPDATE_MANAGE_KPI);
				pstmt.setString(1, manageKPIDTO.getRedThreshold());
				pstmt.setString(2, manageKPIDTO.getAmberThreshold());
				pstmt.setString(3, manageKPIDTO.getGreenThreshold());
				pstmt.setString(4, sso);
				pstmt.setInt(5, manageKPIDTO.getCompanyId());
				pstmt.setString(6, manageKPIDTO.getKpiCode());
				
				if(pstmt.executeUpdate()>0) {
					System.out.println("Manage Threshold KPI updated");
					resultFlag = true;
				}
				
				PreparedStatement pstmt1 = con.prepareStatement(OfeLandingDashboardConstants.UPDATE_TARGET_VALUE);
				pstmt1.setDouble(1, manageKPIDTO.getTargetValue());
				pstmt1.setString(2, sso);
				pstmt1.setInt(3, manageKPIDTO.getCompanyId());
				pstmt1.setString(4, manageKPIDTO.getBusinessUnit());
				pstmt1.setString(5, manageKPIDTO.getKpiCode());
				
				if(pstmt1.executeUpdate()>0) {
					System.out.println("Target Value Updated");
					resultFlag = true;
				}
			}
			catch(Exception e)
			{
				LOGGER.error("Something went wrong while updating Manage KPI {}",e.getMessage());
			}	
		  finally {
				if (con != null) {
					try {
						con.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		else {
			   try{
					con = jdbcTemplate.getDataSource().getConnection();
					PreparedStatement pstmt = con.prepareStatement(OfeLandingDashboardConstants.SAVE_MANAGE_KPI);
					pstmt.setString(1, manageKPIDTO.getKpiCode());
					pstmt.setString(2, manageKPIDTO.getRedThreshold());
					pstmt.setString(3, manageKPIDTO.getAmberThreshold());
					pstmt.setString(4, manageKPIDTO.getGreenThreshold());
					pstmt.setString(5, sso);
					pstmt.setString(6, manageKPIDTO.getBusinessUnit());
					pstmt.setDouble(7, manageKPIDTO.getTargetValue());
					pstmt.setInt(8, manageKPIDTO.getCompanyId());
						
					if(pstmt.executeUpdate()>0) {
						resultFlag = true;
					}
					
					PreparedStatement pstmt1 = con.prepareStatement(OfeLandingDashboardConstants.UPDATE_THRESHOLD_VALUES);
					pstmt1.setString(1, manageKPIDTO.getRedThreshold());
					pstmt1.setString(2, manageKPIDTO.getAmberThreshold());
					pstmt1.setString(3, manageKPIDTO.getGreenThreshold());
					pstmt1.setString(4, sso);
					pstmt1.setInt(5, manageKPIDTO.getCompanyId());
					pstmt1.setString(6, manageKPIDTO.getKpiCode());
					
					if(pstmt1.executeUpdate()>0) {
						resultFlag = true;
					}
					
				}
				catch(Exception e)
				{
					LOGGER.error("Something went wrong while saving and Updating Manage KPI {}",e.getMessage());
				}
			   finally {
					if (con != null) {
						try {
							con.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
				}
			   
			}
		
		return resultFlag;
	}

	@Override
	public int checkIfKPIAndBusinessExist(LandingManageKPIDisplayData manageKPI) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.CHECK_KPI_AND_BUSINESS_EXIST,
				new Object[] {manageKPI.getKpiCode(),manageKPI.getBusinessUnit()},
				new ResultSetExtractor<Integer>() {

					@Override
					public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
						int count = 0;
						while(rs.next())
						{
							count =rs.getInt(1);
						}
						return count;
					}
			
		});
	}
	

	@Override
	public List<LandingManageKPI> getManageKPI(int companyId) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_LANDING_MANAGE_KPI,
				new Object[] {companyId},
				new ResultSetExtractor<List<LandingManageKPI>>() {
					@Override
					public List<LandingManageKPI> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingManageKPI> list = new ArrayList<LandingManageKPI>();
						while(rs.next()) {
							LandingManageKPI dto = new LandingManageKPI();
							dto.setKpiCode(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setBusinessUnit(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setTargetValue(rs.getDouble(3));
							dto.setRedThreshold(null !=rs.getString(4) ? rs.getString(4): "");
							dto.setAmberThreshold(null !=rs.getString(5) ? rs.getString(5): "");
							dto.setGreenThreshold(null !=rs.getString(6) ? rs.getString(6): "");
							dto.setDefaultThreshold(rs.getBoolean(7));
							dto.setKpiCategory(null !=rs.getString(8) ? rs.getString(8): "");
							dto.setKpiValueFormat(null !=rs.getString(9) ? rs.getString(9): "");
							list.add(dto);
						}
						return list;
					}
			
		});
	}

	@Override
	public String getUpdatedOn() {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_UPDATED_ON,
				new Object[] {},
				new ResultSetExtractor<String>() {

					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String updatedOn = new String();
						while(rs.next()) {
						 updatedOn = rs.getString(1);
						}
						return updatedOn;
					}
			
		});
	}

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForOTD(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_OTD,
				new Object[] {businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {

					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()) {
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
			
		});
	}
	

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForBilling(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_BILLING,
				new Object[] {businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {

					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()) {
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
			
		});
	}

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForRisk(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_RISK,
				new Object[]{businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {
					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()){
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForOpportunities(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_OPPORTUNITIES,
				new Object[]{businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {
					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()){
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForDocumentation(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_DOCUMENTATION,
				new Object[]{businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {
					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()){
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForChanges(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_CHANGES,
				new Object[]{businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {
					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()){
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
				});
	}

	@Override
	public List<LandingKPITrendChartDTO> getKPITrendChartForQuality(String businessUnit) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_KPI_TREND_CHART_FOR_QUALITY,
				new Object[]{businessUnit},
				new ResultSetExtractor<List<LandingKPITrendChartDTO>>() {
					@Override
					public List<LandingKPITrendChartDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<LandingKPITrendChartDTO> list = new ArrayList<LandingKPITrendChartDTO>();
						while(rs.next()){
							LandingKPITrendChartDTO dto = new LandingKPITrendChartDTO();
							dto.setKpiId(rs.getInt(1));
							dto.setKpiCode(null != rs.getString(2) ? rs.getString(2):"");
							dto.setKpiValue(null != rs.getString(3) ? rs.getString(3):"");
							dto.setDate(null != rs.getString(4) ? rs.getString(4):"");
							dto.setKpiValueFormat(null != rs.getString(5) ? rs.getString(5):"");
							dto.setKpiShortName(null != rs.getString(6) ? rs.getString(6):"");
							dto.setBusinessUnit(null != rs.getString(7) ? rs.getString(7):"");
							list.add(dto);
						}
						return list;
					}
				});
	}


	@Override
	public List<LandingProjectDetailsDTO> getLandingprojectDetails(HeaderDashboardDetailsDTO projectDetails) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_LANDING_PROJECT_DETAILS,
				new Object[] {projectDetails.getProjectId(), projectDetails.getCompanyId(), 
						projectDetails.getBusiness(),projectDetails.getSegment(),projectDetails.getRegion(),
						projectDetails.getCustomerId()},
				new ResultSetExtractor<List<LandingProjectDetailsDTO>>() {

					@Override
					public List<LandingProjectDetailsDTO> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<LandingProjectDetailsDTO> list = new ArrayList<LandingProjectDetailsDTO>();
						while(rs.next()) {
							LandingProjectDetailsDTO dto = new LandingProjectDetailsDTO();
							dto.setProjectId(null != rs.getString(1) ? rs.getString(1) : "");
							dto.setProjectName(null != rs.getString(2) ? rs.getString(2) : "");
							dto.setBusiness(null != rs.getString(3) ? rs.getString(3) : "");
							dto.setSegment(null != rs.getString(4) ? rs.getString(4) : "");
							dto.setEndUser(null != rs.getString(5) ? rs.getString(5) : "");
							dto.setRegion(null != rs.getString(6) ? rs.getString(6) : "");
							dto.setProjectManager(null != rs.getString(7) ? rs.getString(7) : "");
							dto.setProjectValue(null != rs.getString(8) ? rs.getString(8) : "");
							dto.setVarToBL(null != rs.getString(9) ? rs.getString(9) : "");
							dto.setCm(null != rs.getString(10) ? rs.getString(10) : "");
							dto.setCmExpansion(null != rs.getString(11) ? rs.getString(11) : "");
							dto.setOtdCustOtdOverLast12Months(null != rs.getString(12) ? rs.getString(12) : "");
							dto.setOtdInternalOTDOverLast12Months(null != rs.getString(13) ? rs.getString(13) : "");
							dto.setBillingCurrentQuarterLE(null != rs.getString(14) ? rs.getString(14) : "");
							dto.setBillingActualVsLE(null != rs.getString(15) ? rs.getString(15) : "");
							dto.setBillingOverdue(null != rs.getString(16) ? rs.getString(16) : "");
							dto.setRiskCurrentEMVOfOpenRisks(null != rs.getString(17) ? rs.getString(17) : "");
							dto.setRiskOpenRiskActionsOverdue(null != rs.getString(18) ? rs.getString(18) : "");
							dto.setRiskPostMitigationEMVOfOpenRisk(null != rs.getString(19) ? rs.getString(19) : "");
							dto.setOppCurrentEMVOfOpenOpp(null != rs.getString(20) ? rs.getString(20) : "");
							dto.setOppOpenOppActionOverdue(null != rs.getString(21) ? rs.getString(21) : "");
							dto.setOppPostMitigationEMVOfOpenOpp(null != rs.getString(22) ? rs.getString(22) : "");
							dto.setDocLast12MonthOTD(null != rs.getString(23) ? rs.getString(23) : "");
							dto.setDocRework(null != rs.getString(24) ? rs.getString(24) : "");
							dto.setDocMedianCustomerReview(null != rs.getString(25) ? rs.getString(25) : "");
							dto.setChangesInProcessChangeAmount(null != rs.getString(26) ? rs.getString(26) : "");
							dto.setChangesApprovedChangeAmount(null != rs.getString(27) ? rs.getString(27) : "");
							dto.setChangesFinalCMApprovedChanges(null != rs.getString(28) ? rs.getString(28) : "");
							dto.setQualityOverdueCIR(null != rs.getString(29) ? rs.getString(29) : "");
							dto.setQualityOverdueNCR(null != rs.getString(30) ? rs.getString(30) : "");
							dto.setQualityCoPQ(null != rs.getString(31) ? rs.getString(31) : "");
							list.add(dto);
						}
						return list;
					}
			
		});
	}

	@Override
	public List<LandingProjectDetailsDTO> getLandingProjectDetailsExcel(String projectId, int companyId, String businessUnit, String segment, String region, int customerId) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_LANDING_PROJECT_DETAILS,
				new Object[] {projectId, companyId,businessUnit,segment,region,customerId},
				new ResultSetExtractor<List<LandingProjectDetailsDTO>>() {

					@Override
					public List<LandingProjectDetailsDTO> extractData(ResultSet rs)
							throws SQLException, DataAccessException {
						List<LandingProjectDetailsDTO> list = new ArrayList<LandingProjectDetailsDTO>();
						while(rs.next()) {
							LandingProjectDetailsDTO dto = new LandingProjectDetailsDTO();
							dto.setProjectName(null != rs.getString("project_name") ? rs.getString("project_name") : "");
							dto.setBusiness(null != rs.getString("business") ? rs.getString("business") : "");
							dto.setSegment(null != rs.getString("segment") ? rs.getString("segment") : "");
							dto.setEndUser(null != rs.getString("end_user") ? rs.getString("end_user") : "");
							dto.setRegion(null != rs.getString("region") ? rs.getString("region") : "");
							dto.setProjectManager(null != rs.getString("project_manager") ? rs.getString("project_manager") : "");
							dto.setProjectValue(null != rs.getString("project_value") ? rs.getString("project_value") : "");
							dto.setVarToBL(null != rs.getString("var_to_bl") ? rs.getString("var_to_bl") : "");
							dto.setCm(null != rs.getString("cm") ? rs.getString("cm") : "");
							dto.setCmExpansion(null != rs.getString("cm_expansion") ? rs.getString("cm_expansion") : "");
							dto.setOtdCustOtdOverLast12Months(null != rs.getString("OTD-cust_otd_%_over_last_12months") ? rs.getString("OTD-cust_otd_%_over_last_12months") : "");
							dto.setOtdInternalOTDOverLast12Months(null != rs.getString("OTD-internal_otd_%_over_last_12months") ? rs.getString("OTD-internal_otd_%_over_last_12months") : "");
							dto.setBillingCurrentQuarterLE(null != rs.getString("Billing-Current_qtr_LE_Vs_Comm") ? rs.getString("Billing-Current_qtr_LE_Vs_Comm") : "");
							dto.setBillingActualVsLE(null != rs.getString("Billing-Actual_Vs_LE_CW") ? rs.getString("Billing-Actual_Vs_LE_CW") : "");
							dto.setBillingOverdue(null != rs.getString("Billing-billing_overdue") ? rs.getString("Billing-billing_overdue") : "");
							dto.setRiskCurrentEMVOfOpenRisks(null != rs.getString("Risk-current_EMV_of_Open_Risks_relative_to_Prj_Values") ? rs.getString("Risk-current_EMV_of_Open_Risks_relative_to_Prj_Values") : "");
							dto.setRiskOpenRiskActionsOverdue(null != rs.getString("Risk-Open_Risk_Actions_that_are_Overdue") ? rs.getString("Risk-Open_Risk_Actions_that_are_Overdue") : "");
							dto.setRiskPostMitigationEMVOfOpenRisk(null != rs.getString("Risk-Post_Mitig_EMV_of_open_Risks_relative_to_Prj_value") ? rs.getString("Risk-Post_Mitig_EMV_of_open_Risks_relative_to_Prj_value") : "");
							dto.setOppCurrentEMVOfOpenOpp(null != rs.getString("Opp-Current_EMV_of_Open_Opp_relative_to_Prj_Values_%") ? rs.getString("Opp-Current_EMV_of_Open_Opp_relative_to_Prj_Values_%") : "");
							dto.setOppOpenOppActionOverdue(null != rs.getString("Opp-%_of_Open_Opp_Actions_that_are_Overdue") ? rs.getString("Opp-%_of_Open_Opp_Actions_that_are_Overdue") : "");
							dto.setOppPostMitigationEMVOfOpenOpp(null != rs.getString("Opp-Post_Mitig_EMV_of_Open_Opp_relative_to_Prj_Values_%") ? rs.getString("Opp-Post_Mitig_EMV_of_Open_Opp_relative_to_Prj_Values_%") : "");
							dto.setDocLast12MonthOTD(null != rs.getString("Docu-Last_12_Month_OTD_%") ? rs.getString("Docu-Last_12_Month_OTD_%") : "");
							dto.setDocRework(null != rs.getString("Docu-%_Rework") ? rs.getString("Docu-%_Rework") : "");
							dto.setDocMedianCustomerReview(null != rs.getString("Docu-Median_Customer_Review_Days") ? rs.getString("Docu-Median_Customer_Review_Days") : "");
							dto.setChangesInProcessChangeAmount(null != rs.getString("Changes-In_Process_Change_Amt_relative_to_Prj_Values_%") ? rs.getString("Changes-In_Process_Change_Amt_relative_to_Prj_Values_%") : "");
							dto.setChangesApprovedChangeAmount(null != rs.getString("Changes-Approved_Change_Amt_relative_to_Prj_Value_%") ? rs.getString("Changes-Approved_Change_Amt_relative_to_Prj_Value_%") : "");
							dto.setChangesFinalCMApprovedChanges(null != rs.getString("Changes-Final_CM%_Approved_Changes") ? rs.getString("Changes-Final_CM%_Approved_Changes") : "");
							dto.setQualityOverdueCIR(null != rs.getString("Quality-CIR_Overdue") ? rs.getString("Quality-CIR_Overdue") : "");
							dto.setQualityOverdueNCR(null != rs.getString("Quality-NCR_Overdue") ? rs.getString("Quality-NCR_Overdue") : "");
							dto.setQualityCoPQ(null != rs.getString("Quality-CoPQ") ? rs.getString("Quality-CoPQ") : "");
							list.add(dto);
						}
						return list;
					}
		});
	}

	@Override
	public String qualityOverdueCIRKPI(BillingDashboardFilterDTO filterValues) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_QUALITY_OVERDUE_CIR_KPI,
				new Object[]{filterValues.getProjectList(),filterValues.getProjectList()},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String count = null;
						while(rs.next()){
							count = rs.getString("sum");
						}
						return count;
					}
				});
	}

	@Override
	public String qualityOverdueNCRKPI(BillingDashboardFilterDTO filterValues) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_QUALITY_OVERDUE_NCR_KPI,
				new Object[]{filterValues.getProjectList(),filterValues.getProjectList()},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String count = null;
						while(rs.next()){
							count = rs.getString("sum");
						}
						return count;
					}
				});
	}

	@Override
	public String qualityCoPQKPI(BillingDashboardFilterDTO filterValues) {
		return jdbcTemplate.query(OfeLandingDashboardConstants.GET_QUALITY_COPQKPI,
				new Object[]{filterValues.getProjectList(),filterValues.getProjectList()},
				new ResultSetExtractor<String>() {
					@Override
					public String extractData(ResultSet rs) throws SQLException, DataAccessException {
						String count = null;
						while(rs.next()){
							count = rs.getString("sum");
						}
						return count;
					}
				});
	}
}
