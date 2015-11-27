package com.epam.tester.server;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

public interface ResultMapper {

	@Insert("insert into results (userId, testId, answerId) values (#{userId}, #{testId}, #{answerId})")
	public void insertResult(Result result);

	@Delete("delete from results where userId = #{userId} and testId = #{testId}")
	public void deleteOldAnswers(Result result);

}
