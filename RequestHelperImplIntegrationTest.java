package com.raritan.tdz.request.home;

import com.raritan.tdz.common.IntegrationAPITestBase;
import com.raritan.tdz.domain.UserInfo;
import com.raritan.tdz.request.util.WorkflowAction;
import com.raritan.tdz.session.RESTAPIUserSessionContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.core.io.ClassPathResource;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class RequestHelperImplIntegrationTest extends IntegrationAPITestBase {
  private UserInfo userInfo;
  private RequestHelper requestHelper;

  @BeforeClass(groups = "INTEGRATION_TEST")
  protected void beforeClass() throws Exception {

    super.beforeClass();

    userInfo = RESTAPIUserSessionContext.getUser();
    requestHelper = applicationContext.getBean("requestHelper", RequestHelper.class);

    try(Connection conn = datasource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_item_details.sql"));
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_models.sql"));
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_items.sql"));
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_work_orders.sql"));
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/request/dct_requests.sql"));
    }
  }

  @AfterClass(groups = "INTEGRATION_TEST")
  protected void afterClass() throws SQLException {

    super.afterClass();

  }

  @Test(groups = "INTEGRATION_TEST")
  public void testSendRequestNotification(){
    List<Long> requestIds = new ArrayList<>();
    requestIds.add(1211L);
    requestHelper.sendRequestNotification(null, requestIds, userInfo, WorkflowAction.getStageCodeFromAction(WorkflowAction.REQ_ACTION_APPROVE), false);
    Assert.assertEquals(new Long(1211), requestIds.get(0));
  }

  @Test(groups = "INTEGRATION_TEST")
  public void testSendWorkOrderNotification(){
    List<Long> workOrderIds = new ArrayList<>();
    workOrderIds.add(700L);

    List<Long> requestIds = new ArrayList<>();
    requestIds.add(1211L);

    Map<Long, List<Long>>  woReqMap = new HashMap<Long, List<Long>>(){{
      put(700L, requestIds);
    }};
    requestHelper.sendWorkOrderNotification(workOrderIds, userInfo, WorkflowAction.getStageCodeFromAction(WorkflowAction.WO_ACTION_ISSUE), false, woReqMap);
    Assert.assertEquals(new Long(700), workOrderIds.get(0));
  }


}
