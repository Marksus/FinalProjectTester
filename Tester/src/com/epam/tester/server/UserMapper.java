package com.epam.tester.server;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.epam.tester.shared.User;

public interface UserMapper {

	@Select("select * from users where login = #{login}")
	User getUser(String login);

	@Select("select * from users where id = #{userId}")
	User getUserId(int userId);

	@Insert("insert into users (login,password,isTutor) values( #{login} , #{password} , #{isTutor})")
	void insertUser(User user);

}
