package com.mycloudstorage.mapper;

import com.mycloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) " +
            "VALUES (#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(User user);

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getUser(String username);

    @Select("SELECT * FROM USERS")
    List<User> getUsers();

    @Update("UPDATE USERS SET userid = #{userId} AND salt = #{salt} AND password = #{password}" +
            "AND firstname = #{firstName} AND lastName = #{lastName} " +
            "WHERE username = #{usernameOfUserToUpdate} ")
    void updateUser(String userIdToUpdate, User user);

    @Delete("DELETE FROM USERS WHERE userid = #{userId}")
    void deleteUser(Integer userId);
}
