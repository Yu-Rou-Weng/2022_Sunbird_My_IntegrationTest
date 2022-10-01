package com.raritan.tdz.page.home;

import com.raritan.tdz.circuit.home.CircuitPDHomeImpl;
import com.raritan.tdz.common.IntegrationAPITestBase;
import com.raritan.tdz.domain.UserInfo;
import com.raritan.tdz.domain.cmn.User;
import com.raritan.tdz.exception.BusinessValidationException;
import com.raritan.tdz.exception.DataAccessException;
import com.raritan.tdz.item.home.ItemHomeImpl;
import com.raritan.tdz.page.dto.ColumnCriteriaDTO;
import com.raritan.tdz.page.dto.ColumnDTO;
import com.raritan.tdz.page.dto.FilterDTO;
import com.raritan.tdz.page.dto.ListCriteriaDTO;
import com.raritan.tdz.page.dto.ListResultDTO;
import com.raritan.tdz.projectmanagement.home.ProjectHomeImpl;
import com.raritan.tdz.util.GlobalConstants;
import org.hibernate.SessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaginatedHomeIntegrationTest extends IntegrationAPITestBase {

  private PaginatedHome itemHome;
  private PaginatedHome circuitListPaginatedHome;
  private PaginatedHome circuitlistpaginatedhome;
  private PaginatedHome projectHome;
  private PaginatedHome circuitPDHome;
  private ExportHome itemListExportHome;

  @BeforeClass(groups = "INTEGRATION_TEST")
  public void setUp() throws Throwable {
    super.beforeClass();
    circuitPDHome = (CircuitPDHomeImpl)getObject(applicationContext.getBean("circuitPDHome"));
    itemHome = (ItemHomeImpl)getObject(applicationContext.getBean("itemHome"));
    circuitListPaginatedHome = (PaginatedHomeCircuitListImpl)getObject(applicationContext.getBean("circuitListPaginatedHome"));
    projectHome = (ProjectHomeImpl)getObject(applicationContext.getBean("projectHome"));
    circuitlistpaginatedhome= applicationContext.getBean("circuitListPaginatedHome", PaginatedHome.class);
    itemListExportHome=(ExportHomeItemListImpl)getObject(applicationContext.getBean("itemListExportHome"));

    try(Connection conn = datasource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/dct_pglist_settings.sql"));
    }
  }

  @AfterClass(groups = "INTEGRATION_TEST")
  public void afterClass() throws SQLException {

    super.afterClass();

  }

  @Test(groups = "INTEGRATION_TEST", expectedExceptions = {BusinessValidationException.class})
  public void testPaginatedHomeAllItemListImplGetPageList() throws Throwable {
    UserInfo userInfo = getUserInfo();
    ListCriteriaDTO filterCriteria = new ListCriteriaDTO();
    filterCriteria.setFirstQuery(false);
    filterCriteria.setFitType(0);
    filterCriteria.setMaxLinesPerPage(12);;
    filterCriteria.setPageNumber(1);
    filterCriteria.setUserAddColumn(false);

    ArrayList<ColumnCriteriaDTO> columnCriteria = new ArrayList<>();
    ColumnCriteriaDTO c1 = new 	ColumnCriteriaDTO();
    FilterDTO filter1 = new FilterDTO();
    filter1.setEqual("DL380");
    c1.setFilter(filter1);
    c1.setName("itemName");
    c1.setSortDescending(false);
    c1.setToSort(true);
    c1.visible = false;
    columnCriteria.add(c1);

    filterCriteria.setColumnCriteria(columnCriteria);

    ArrayList<ColumnDTO> columns = new ArrayList<>();
    ColumnDTO co1 = new ColumnDTO();
    ColumnDTO co2 = new ColumnDTO();
    columns.add(co1);
    columns.add(co2);
    filterCriteria.setColumns(columns);

    filterCriteria.setCurrentUtcTimeString("6/22/2021 8:30:30 -0500");

    itemListExportHome.exportToCSVForImportAngular(filterCriteria, GlobalConstants.PG_LIST_NAME_ITEM, null, userInfo);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testItemHomeImplGetPageList() throws DataAccessException, BusinessValidationException {
    UserInfo userInfo =getUserInfo();
    ListCriteriaDTO listCriteriaDTO = createListCriteria();
    listCriteriaDTO.setFitType(-1);
    String pageType = "test";

    ListResultDTO Final = itemHome.getPageList(listCriteriaDTO, pageType, userInfo);
    Assert.assertNotNull(Final);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testCircuitGetPageList() throws DataAccessException, BusinessValidationException {
    UserInfo userInfo =getUserInfo();
    ListCriteriaDTO listCriteriaDTO = new ListCriteriaDTO();
    listCriteriaDTO.setFitType(-1);
    String pageType = "test";

    ListResultDTO Final =circuitlistpaginatedhome.getPageList(listCriteriaDTO, pageType, userInfo);

    Assert.assertNotNull(Final);
  }
  @Test(groups = "INTEGRATION_TEST")
  public void testProjectHomeGetPageList() throws DataAccessException, BusinessValidationException {
    UserInfo userInfo =getUserInfo();
    ListCriteriaDTO listCriteriaDTO = new ListCriteriaDTO();
    listCriteriaDTO.setFitType(-1);
    String pageType = "test";

    ListResultDTO Final = projectHome.getPageList(listCriteriaDTO, pageType, userInfo);

    Assert.assertNotNull(Final);
  }



  @Test(groups = "INTEGRATION_TEST")
  public void testCircuitPDHomeImplSaveUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result =  circuitPDHome.saveUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testItemHomeImplSaveUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result = itemHome.saveUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testPaginatedHomeCircuitListImplSaveUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result = circuitListPaginatedHome.saveUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testProjectHomeImplSaveUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result = projectHome.saveUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testCircuitPDHomeImplDeleteUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result =  circuitPDHome.deleteUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testItemHomeImplDeleteUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result = itemHome.deleteUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testPaginatedHomeCircuitListImplDeleteUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result = circuitListPaginatedHome.deleteUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testProjectHomeImplDeleteUserConfig() throws DataAccessException {

    ListCriteriaDTO itemListCriteria = new ListCriteriaDTO();
    itemListCriteria.setFitType(-1);
    String pageType = "test";

    int result = projectHome.deleteUserConfig(itemListCriteria, pageType);
    Assert.assertEquals(1, result);
  }
  private ListCriteriaDTO createListCriteria() {
    ListCriteriaDTO filterCriteria = new ListCriteriaDTO();
    filterCriteria.setFirstQuery(false);
    filterCriteria.setFitType(0);
    filterCriteria.setMaxLinesPerPage(12);
    filterCriteria.setPageNumber(1);
    filterCriteria.setUserAddColumn(false);

    // ColumnCriteria -> Index是ColumnCriteriaDTO
    ArrayList<ColumnCriteriaDTO> columnCriteria = new ArrayList<ColumnCriteriaDTO>();
    ColumnCriteriaDTO c1 = new 	ColumnCriteriaDTO();
    c1.setFilter(null);
    c1.setName("Name");
    c1.setSortDescending(false);
    c1.setToSort(true);
    c1.visible = false;
    columnCriteria.add(c1);

    ColumnCriteriaDTO c2 = new 	ColumnCriteriaDTO();
    FilterDTO filter1 = new FilterDTO();
    filter1.setEqual("00");
    filter1.setGreaterThan(null);
    filter1.setGroupType(0);
    filter1.setLessThan(null);
    filter1.setIsLookup(false);
    filter1.setLookupCodes(null);
    c2.setFilter(filter1);
    c2.setName("Request Number");
    c2.setSortDescending(false);
    c2.setToSort(true);
    c2.visible = false;
    columnCriteria.add(c2);

    ColumnCriteriaDTO c3 = new 	ColumnCriteriaDTO();
    FilterDTO filter2 = new FilterDTO();
    filter2.setEqual("");
    filter2.setGreaterThan(null);
    filter2.setGroupType(0);
    filter2.setLessThan(null);
    filter2.setIsLookup(true);
    filter2.setLookupCodes("Request Issued");
    c3.setFilter(filter2);
    c3.setName("Request Stage");
    c3.setSortDescending(false);
    c3.setToSort(false);
    c3.visible = false;
    columnCriteria.add(c3);

    filterCriteria.setColumnCriteria(columnCriteria);

    ArrayList<ColumnDTO> columns = new ArrayList<ColumnDTO>();
    ColumnDTO co1 = new ColumnDTO();
    ColumnDTO co2 = new ColumnDTO();
    columns.add(co1);
    columns.add(co2);
    filterCriteria.setColumns(columns);

    filterCriteria.setCurrentUtcTimeString("6/22/2021 8:30:30 -0500");
    return filterCriteria;
  }
  private UserInfo getUserInfo(){
    User user = new User(1L,"IntegrationTest","TEST","test","test");
    UserInfo userInfo = new UserInfo(user,"IntegrationTest","TEST","test","test",1,null,null,null,null,0,false);
    return userInfo;
  }

  public Object getObject(Object o) throws Exception{
    for (PropertyDescriptor pd: Introspector.getBeanInfo(o.getClass()).getPropertyDescriptors()){
      if (pd.getReadMethod() != null && "targetSource".equals(pd.getName()) ){
        Object existing = pd.getReadMethod().invoke(o);
        for (PropertyDescriptor pd2: Introspector.getBeanInfo(existing.getClass()).getPropertyDescriptors()){
          if (pd2.getReadMethod() != null && "target".equals(pd2.getName()) ){
            return pd2.getReadMethod().invoke(existing);
          }
        }
      }
    }
    return null;
  }
}