package com.ives.test;

import com.ives.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class CustomRealmTest {

   @Test
   public void testAuthentication() {
      CustomRealm customRealm = new CustomRealm();
      // 1. 构建SecurityManager环境
      DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
      defaultSecurityManager.setRealm(customRealm);

      // 密码加密
      HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
      credentialsMatcher.setHashAlgorithmName("md5"); //加密算法名称
      credentialsMatcher.setHashIterations(1);  // 加密次数
      customRealm.setCredentialsMatcher(credentialsMatcher); // realm 设置credentialsMatcher


      // 2. 主体提交认证请求
      SecurityUtils.setSecurityManager(defaultSecurityManager);
      Subject subject = SecurityUtils.getSubject();
      UsernamePasswordToken token = new UsernamePasswordToken("ives", "123456");

      subject.login(token);
      System.out.println("authenticated:" + subject.isAuthenticated());
      subject.checkRole("admin");
      subject.checkPermission("user:create");
   }
}
