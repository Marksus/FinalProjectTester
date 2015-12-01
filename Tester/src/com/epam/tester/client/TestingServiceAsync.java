package com.epam.tester.client;

import java.util.List;

import com.epam.tester.shared.DataObject;
import com.epam.tester.shared.Result;
import com.epam.tester.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TestingServiceAsync {
	void login(String nickname, String password, AsyncCallback<Byte> callback);

	void registrate(String nickname, String password, AsyncCallback<Byte> callback);

	void getDatas(int prev, AsyncCallback<List<DataObject>> callback);

	void insertDataObject(DataObject dataObject, AsyncCallback<Void> callback);

	void deleteDataObject(int id, AsyncCallback<Void> callback);

	void updateDataObject(int id, String text, int value, AsyncCallback<Void> asyncCallback);

	void isAnswerRight(int id, AsyncCallback<Boolean> asyncCallback);

	void getTest(int id, AsyncCallback<List<DataObject>> asyncCallback);

	void sendResult(String name, int testId, Integer[] result, AsyncCallback<Void> asyncCallback);

	void getTestedUsers(int testId, AsyncCallback<List<User>> asycnCallback);

	void getResults(int userId, int testId, AsyncCallback<List<Result>> callback);
}
