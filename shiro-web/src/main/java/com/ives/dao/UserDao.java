package com.ives.dao;

import com.ives.domain.User;

import java.util.List;

public interface UserDao {
   User getUserByUserName(String userName);

   List<String> queryRolesByUserName(String userName);
}
