package com.ives.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class JdbcRealmTest {

   DruidDataSource dataSource = new DruidDataSource();

   {
      dataSource.setUrl("jdbc:mysql://localhost:3306/ives");
      dataSource.setUsername("root");
      dataSource.setPassword("mz7758258");
   }

   @Test
   public void testAuthentication() {
      JdbcRealm jdbcRealm = new JdbcRealm();
      jdbcRealm.setDataSource(dataSource);
      jdbcRealm.setPermissionsLookupEnabled(true); // 查询权限的时候需要设置的属性

      String sql = "select pwd from ives_user where name = ?";
      jdbcRealm.setAuthenticationQuery(sql) ;

      String roleSql = "select role_name from ives_user_role where user_name = ?";
      jdbcRealm.setUserRolesQuery(roleSql);

      String permissionSql = "select permission from ives_permission where role_name = ?";
      jdbcRealm.setPermissionsQuery(permissionSql);

      // 1. 构建SecurityManager环境
      DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
      defaultSecurityManager.setRealm(jdbcRealm);

      // 2. 主体提交认证请求
      SecurityUtils.setSecurityManager(defaultSecurityManager);
      Subject subject = SecurityUtils.getSubject();
      UsernamePasswordToken token = new UsernamePasswordToken("ives", "123456");

      subject.login(token);
      System.out.println("authenticated:" + subject.isAuthenticated());
      subject.checkRole("admin");
      subject.checkPermission("user:update");
   }
}
