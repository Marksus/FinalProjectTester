package com.epam.tester.server;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.epam.tester.shared.Result;

public interface ResultMapper {

	@Insert("insert into results (userId, testId, answerId) values (#{userId}, #{testId}, #{answerId})")
	public void insertResult(Result result);

	@Delete("delete from results where userId = #{userId} and testId = #{testId}")
	public void deleteOldAnswers(Result result);

	@Select("select distinct (userId) from results where testId = #{testId}")
	public List<Integer> selectUsers(int testId);

	@Select("select * from results where userId = #{user}")
	public List<Result> getResults(int userId);

}
