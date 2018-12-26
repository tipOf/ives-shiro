package com.ives.shiro.realm;

import com.ives.dao.UserDao;
import com.ives.domain.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {

   @Resource
   private UserDao userDao;

//   Map<String, String> userMap = new HashMap();
//
//   {
//      userMap.put("ives", "42736dc88b4d80c1f15a315b72d98583");
//      super.setName("customRealm");
//   }

   // 获取加密后的值
   public static void main(String[] args) {
      Md5Hash md5Hash = new Md5Hash("123456", "ives");// 加盐
      System.out.println(md5Hash.toString());
   }

   // 授权
   protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
      String userName = (String) principalCollection.getPrimaryPrincipal();
      Set<String> roles = getRolesByUserName(userName);
      Set<String> permissions = getPermissionByUserName(userName);

      SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
      info.setRoles(roles);
      info.setStringPermissions(permissions);

      return info;
   }

   private Set<String> getPermissionByUserName(String userName) {
      Set<String> sets = new LinkedHashSet();
      sets.add("user:delete");
      sets.add("user:create");

      return sets;
   }

   // 从数据库中或者缓存中获取数据
   private Set<String> getRolesByUserName(String userName) {
      System.out.println("从数据库中或者缓存中获取数据");
      List<String> list = userDao.queryRolesByUserName(userName);
      Set<String> sets = new LinkedHashSet(list);
//      sets.add("admin");
//      sets.add("user");

      return sets;
   }

   // 认证 参数为主体传递进来的认证信息
   protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws
      AuthenticationException {
      // 1、从主体传递过来的认证信息获取用户名
      String userName = (String) authenticationToken.getPrincipal();

      // 2、通过用户名从数据库中获取凭证

      String password = getPasswordByUserName(userName);

      if(password == null) {
         return null;
      }

      SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, "customRealm");

      // 加盐
      authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));

      return authenticationInfo;
   }

   // 模拟数据库
   private String getPasswordByUserName(String userName) {
      User user = userDao.getUserByUserName(userName);
      if(user != null) {
         return user.getPassword();
      }

      return null;
//      return userMap.get(userName);
   }
}
