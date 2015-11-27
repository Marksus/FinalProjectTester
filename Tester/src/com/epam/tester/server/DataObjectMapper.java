package com.epam.tester.server;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.epam.tester.shared.DataObject;

public interface DataObjectMapper {

	@Select("select * from data where id = #{id}")
	DataObject getDataObject(int id);

	@Select("select * from data where prev = #{prev}")
	List<DataObject> getListOfData(int prev);

	@Insert("insert into data (prev, text, value) values (#{prev}, #{text}, #{value})")
	void insertData(DataObject dataObject);

	@Delete("delete from data where id = #{id}")
	void deleteData(int id);

	@Update("update data set text = #{text}, value = #{value} where id = #{id}")
	void updateData(DataObject dataObject);

}
