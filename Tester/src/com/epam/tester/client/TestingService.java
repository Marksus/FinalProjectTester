package com.epam.tester.client;

import java.util.List;

import com.epam.tester.shared.DataObject;
import com.epam.tester.shared.Result;
import com.epam.tester.shared.User;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("test")
public interface TestingService extends RemoteService {
	byte login(String nickname, String password);

	byte registrate(String nickname, String password);

	List<DataObject> getDatas(int prev);

	void insertDataObject(DataObject dataObject);

	void deleteDataObject(int id);

	void updateDataObject(int id, String text, int value);

	boolean isAnswerRight(int id);

	List<DataObject> getTest(int id);

	void sendResult(String name, int testId, Integer[] result);

	List<User> getTestedUsers(int testId);

	List<Result> getResults(int userId, int testId);
}
