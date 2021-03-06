package com.epam.tester.shared;

import java.io.Serializable;

public class Result implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int userId;
	private int testId;
	private int answerId;

	public Result(int userId, int testId, int answerId) {
		this.userId = userId;
		this.testId = testId;
		this.answerId = answerId;
	}

	public Result() {
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getTestId() {
		return testId;
	}

	public void setTestId(int testId) {
		this.testId = testId;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

}
