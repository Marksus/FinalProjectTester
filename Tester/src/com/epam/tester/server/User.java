package com.epam.tester.server;

public class User {

	private int id;
	private String nickname;
	private String password;
	private boolean isTutor;

	public User(String nickname, String password, boolean isTutor) {
		this.nickname = nickname;
		this.password = password;
		this.isTutor = isTutor;
	}

	public User() {
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isTutor() {
		return isTutor;
	}

	public void setTutor(boolean isTutor) {
		this.isTutor = isTutor;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
