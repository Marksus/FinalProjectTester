package com.epam.tester.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.epam.tester.shared.DataObject;
import com.epam.tester.shared.Result;
import com.epam.tester.shared.User;

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

	public static void main(String[] args) {
		DataManipulator dm = new DataManipulator();
		for (Result res : dm.getResults(2, 3)) {
			System.out.println(res.getUserId() + " " + res.getTestId() + " " + res.getAnswerId());
		}
	}

	public byte login(String login, String password) {
		try (SqlSession session = sqlSessionFactory.openSession()) {
			UserMapper mapper = session.getMapper(UserMapper.class);
			User user = mapper.getUser(login);
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
			int idPrev = dataObject.getPrev();
			DataObject prev = mapper.getDataObject(idPrev);
			mapper.insertData(dataObject);
			int count = 0;
			for (DataObject data : mapper.getListOfData(idPrev)) {
				if (data.getValue() > 0)
					count++;
			}
			if ((prev != null) && (prev.getValue() != count)) {
				prev.setValue(count);
				mapper.updateData(prev);
			}
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

	public List<User> getTestedUsers(int testId) {
		List<User> list = new LinkedList<>();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ResultMapper resultMapper = session.getMapper(ResultMapper.class);
			UserMapper userMapper = session.getMapper(UserMapper.class);
			List<Integer> userList = resultMapper.selectUsers(testId);
			if (userList != null)
				for (Integer id : userList) {
					list.add(userMapper.getUserId(id));
				}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Result> getResults(int userId, int testId) {
		List<Result> list = new LinkedList<>();
		try (SqlSession session = sqlSessionFactory.openSession()) {
			ResultMapper resultMapper = session.getMapper(ResultMapper.class);
			list = resultMapper.getResults(userId);
			Iterator<Result> iterator = list.iterator();
			while (iterator.hasNext()) {
				Result res = iterator.next();
				if (res.getTestId() != testId)
					iterator.remove();
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		return list;
	}
}
