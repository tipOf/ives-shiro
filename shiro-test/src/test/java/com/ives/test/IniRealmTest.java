package com.ives.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

public class IniRealmTest {

   IniRealm iniRealm = new IniRealm("classpath:user.ini");

   @Test
   public void testAuthentication() {
      // 1. 构建SecurityManager环境
      DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
      defaultSecurityManager.setRealm(iniRealm);

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
