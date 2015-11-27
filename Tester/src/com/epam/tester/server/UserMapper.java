package com.epam.tester.server;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

	@Select("select * from users where login = #{nickname}")
	User getUser(String nickname);

	@Insert("insert into users (login,password,isTutor) values( #{nickname} , #{password} , #{isTutor})")
	void insertUser(User user);

}
