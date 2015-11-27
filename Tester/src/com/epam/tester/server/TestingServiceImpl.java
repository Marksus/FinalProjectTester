package com.epam.tester.server;

import java.util.List;

import com.epam.tester.client.TestingService;
import com.epam.tester.shared.DataObject;
import com.epam.tester.shared.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TestingServiceImpl extends RemoteServiceServlet implements TestingService {

	DataManipulator dm = new DataManipulator();

	@Override
	public byte login(String nickname, String password) {
		if (!FieldVerifier.isValidName(nickname))
			return 0;
		return dm.login(nickname, password);
	}

	@Override
	public byte registrate(String nickname, String password) {
		if (!FieldVerifier.isValidName(nickname))
			return 0;
		return dm.registrate(nickname, password);
	}

	@Override
	public List<DataObject> getDatas(int prev) {
		return dm.getDatas(prev);
	}

	@Override
	public void insertDataObject(DataObject dataObject) {
		dm.insertDataObject(dataObject);
	}

	@Override
	public void deleteDataObject(int id) {
		dm.deleteDataObject(id);
	}

	@Override
	public void updateDataObject(int id, String text, int value) {
		dm.updateDataObject(id, text, value);
	}

	@Override
	public boolean isAnswerRight(int id) {
		return dm.isAnswerRight(id);
	}

	@Override
	public List<DataObject> getTest(int id) {
		return dm.getTest(id);
	}

	@Override
	public void sendResult(String name, int testId, Integer[] result) {
		dm.sendResult(name, testId, result);
	}
}
