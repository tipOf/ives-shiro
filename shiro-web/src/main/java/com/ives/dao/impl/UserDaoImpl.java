package com.ives.dao.impl;

import com.ives.dao.UserDao;
import com.ives.domain.User;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

   @Resource
   private JdbcTemplate jdbcTemplate;

   public User getUserByUserName(String userName) {
      String sql = "select name, pwd from ives_user where name = ?";

      List<User> users = jdbcTemplate.query(sql, new String[] {userName}, new RowMapper<User>() {
         public User mapRow(ResultSet resultSet, int i) throws SQLException {
            User user = new User();
            user.setUsername(resultSet.getString("name"));
            user.setPassword(resultSet.getString("pwd"));

            return user;
         }
      });

      if(CollectionUtils.isEmpty(users)) {
         return null;
      }

      return users.get(0);
   }

   public List<String> queryRolesByUserName(String userName) {
      String sql = "select role_name from ives_user_role where user_name = ?";
      List<String> list = jdbcTemplate.query(sql, new String[] {userName}, new RowMapper<String>() {
         public String mapRow(ResultSet resultSet, int i) throws SQLException {
            return resultSet.getString("role_name");
         }
      });

      return list;
   }
}
