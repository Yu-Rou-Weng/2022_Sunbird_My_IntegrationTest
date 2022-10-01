package com.raritan.tdz.item.home;

import com.raritan.tdz.json.BasicJSON;
import com.raritan.tdz.common.IntegrationAPITestBase;
import com.raritan.tdz.exception.BusinessValidationException;
import com.raritan.tdz.exception.DataAccessException;
import com.raritan.tdz.lookup.FieldLookup;
import com.raritan.tdz.request.dto.RequestDTO;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.junit.Assert.assertEquals;

public class ItemHomeImplIntegrationTest extends IntegrationAPITestBase {

  private ItemHome itemHome;

  @BeforeClass(groups = "INTEGRATION_TEST")
  protected void beforeClass() throws Exception {

    super.beforeClass();

    itemHome = applicationContext.getBean("itemHome", ItemHome.class);

    try(Connection conn = datasource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_item_details.sql"));
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_models.sql"));
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_items.sql"));
    }
  }

  @AfterClass(groups = "INTEGRATION_TEST")
  protected void afterClass() throws SQLException {

    super.afterClass();

  }

  @Test(groups = "INTEGRATION_TEST")
  public void testItemRequest() throws BusinessValidationException, DataAccessException {
    List<Long> itemId = new ArrayList<>();
    itemId.add(15388L);

    Map<String, Object> requestMap = itemHome.itemRequest(itemId, "req_install", false, createParamMap("08/25/2022"));

    List<RequestDTO> reqList = new ArrayList<>();
    reqList = (List<RequestDTO>)requestMap.get("java.util.List");

    RequestDTO requestDTO = reqList.get(0);

    Assert.assertEquals(new Long(15388), requestDTO.getItemId());

  }

  @Test(groups = "INTEGRATION_TEST")
  public void testItemRequest2() throws BusinessValidationException, DataAccessException {
    List<Long> itemId = new ArrayList<>();
    itemId.add(15388L);

    Map<String, Object> requestMap = itemHome.itemRequest(itemId, "req_install", false, createParamMap("08/25/2022"), true);

    List<RequestDTO> reqList = new ArrayList<>();
    reqList = (List<RequestDTO>)requestMap.get("java.util.List");

    RequestDTO requestDTO = reqList.get(0);

    Assert.assertEquals(new Long(15388), requestDTO.getItemId());

  }

  @Test(groups = "INTEGRATION_TEST")
  public void testUpdateProjNo() throws Throwable{
    List<BasicJSON<String>> basicJSONs= new ArrayList<>();
    assertEquals(true,itemHome.updateProjNo(basicJSONs));
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testUpdItemNameById() throws Throwable{
    BasicJSON<String> basicJSON= new BasicJSON<>();
    assertEquals(false,itemHome.updItemNameById(basicJSON));
  }

  private Map<String, Object> createParamMap(String plannedDecommDate) {
    Map<String, Object> paramMap = new HashMap<String, Object>();
    paramMap.put(FieldLookup.FieldName.PLANNED_DECOMM_DATE, plannedDecommDate);
    return paramMap;
  }

}
