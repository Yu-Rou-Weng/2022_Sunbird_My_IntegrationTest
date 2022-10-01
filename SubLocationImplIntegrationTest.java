package com.raritan.tdz.subLocation.home;

import com.raritan.tdz.common.IntegrationAPITestBase;
import com.raritan.tdz.domain.UserInfo;
import com.raritan.tdz.domain.cmn.User;
import com.raritan.tdz.exception.BusinessInformationException;
import com.raritan.tdz.exception.BusinessValidationException;
import com.raritan.tdz.exception.RemoteDataAccessException;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SubLocationImplIntegrationTest extends IntegrationAPITestBase {
  private SubLocationHome subLocationHome;
  @BeforeClass(groups = "INTEGRATION_TEST")
  public void setUp() throws Throwable {
    super.beforeClass();
    //requestActionMenuHelper = (RequestActionMenuHelperImpl)getObject(applicationContext.getBean("requestActionMenuHelper"));
    subLocationHome = (SubLocationHome) getObject(applicationContext.getBean("subLocationHome"));
    try(Connection conn = datasource.getConnection()) {
      ScriptUtils.executeSqlScript(conn, new ClassPathResource("common/integrationAPIBaseData/dct_sub_location.sql"));
    }
  }

  @AfterClass(groups = "INTEGRATION_TEST")
  public void afterClass() throws SQLException {
    super.afterClass();
  }


  @Test(groups = "INTEGRATION_TEST")
  public void testUnmapSubLocation() throws BusinessValidationException, BusinessInformationException, RemoteDataAccessException {
    Long subLocationId=69L;
    Map<String, Object> subLocationDetails = new HashMap<>();
    UserInfo userInfo = getUserInfo();
    subLocationHome.unmapSubLocation(subLocationId,subLocationDetails,userInfo);
  }




  private UserInfo getUserInfo(){
    User user = new User(1L,"admin","","System","Administrator");
    UserInfo userInfo = new UserInfo(user,"admin","","System","Administrator",1,1,"en-US",null,"584ca090-5a23-455e-883e-2c49910bd42a",1827387392,false);
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
