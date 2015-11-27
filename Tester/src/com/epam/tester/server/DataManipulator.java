package com.epam.tester.server;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.epam.tester.shared.DataObject;

public class DataManipulator {

	private static SqlSessionFactory sqlSessionFactory;

	public DataManipulator() {
		try {
			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(Resources.getResourceAsReader("com/epam/tester/server/config/config.xml"));
			sqlSessionFactory.getConfiguration().addMapper(DataObjectMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(UserMapper.class);
			sqlSessionFactory.getConfiguration().addMapper(ResultMapper.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// DataManipulator dm = new DataManipulator();
	// System.out.println(dm.getThemes().getClass());
	// }

	public byte login(String nickname, String password) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User user = mapper.getUser(nickname);
			if (user == null)
				return 0;
			if (!user.getPassword().equals(password))
				return 0;
			if (user.isTutor())
				return 2;
			return 1;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public byte registrate(String nickname, String password) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User user = mapper.getUser(nickname);
			if (user != null) {
				return 0;
			}
			mapper.insertUser(new User(nickname, password, false));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public List<DataObject> getDatas(int prev) {
		List<DataObject> datas;
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DataObjectMapper mapper = session.getMapper(DataObjectMapper.class);
			datas = mapper.getListOfData(prev);
			return datas;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void insertDataObject(DataObject dataObject) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DataObjectMapper mapper = session.getMapper(DataObjectMapper.class);
			mapper.insertData(dataObject);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void deleteDataObject(int id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DataObjectMapper mapper = session.getMapper(DataObjectMapper.class);
			List<DataObject> list = mapper.getListOfData(id);
			if (list != null)
				for (DataObject data : list) {
					deleteDataObject(data.getId());
				}
			mapper.deleteData(id);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void updateDataObject(int id, String text, int value) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DataObjectMapper mapper = session.getMapper(DataObjectMapper.class);
			DataObject data = mapper.getDataObject(id);
			data.setText(text);
			data.setValue(value);
			mapper.updateData(data);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public boolean isAnswerRight(int id) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			DataObjectMapper mapper = session.getMapper(DataObjectMapper.class);
			return (mapper.getDataObject(id).getValue() > 0) ? true : false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<DataObject> getTest(int id) {
		List<DataObject> test = new LinkedList<DataObject>();
		List<DataObject> questions = getDatas(id);
		for (DataObject question : questions) {
			test.add(question);
			test.addAll(getDatas(question.getId()));
		}
		return test;
	}

	public void sendResult(String name, int testId, Integer[] result) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ResultMapper resultMapper = session.getMapper(ResultMapper.class);
			UserMapper userMapper = session.getMapper(UserMapper.class);
			int userId = userMapper.getUser(name).getId();
			Result temp = new Result();
			temp.setUserId(userId);
			temp.setTestId(testId);
			resultMapper.deleteOldAnswers(temp);
			for (Integer answerId : result) {
				resultMapper.insertResult(new Result(userId, testId, answerId.intValue()));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

}
