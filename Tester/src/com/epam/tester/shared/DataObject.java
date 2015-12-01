package com.epam.tester.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DataObject implements Serializable {

	private int id;
	private int prev;
	private String text;
	private int value;

	public DataObject() {
	}

	public DataObject(int prev, String data, int value) {
		this.prev = prev;
		this.text = data;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrev() {
		return prev;
	}

	public void setPrev(int prev) {
		this.prev = prev;
	}

	public String getText() {
		return text;
	}

	public void setText(String data) {
		this.text = data;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
