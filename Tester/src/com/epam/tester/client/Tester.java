package com.epam.tester.client;

import java.util.LinkedList;
import java.util.List;

import com.epam.tester.shared.DataObject;
import com.epam.tester.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Tester implements EntryPoint {

	private final TestingServiceAsync testingService = GWT.create(TestingService.class);

	private String login;
	private boolean isTutor;

	private Label info;
	private Label error;
	private Label logged;
	private FlexTable loginForm;
	private TextBox loginBox;
	private PasswordTextBox passwordBox;
	private Button signIn;
	private Button registration;
	private Button signOut;

	private Label chooseTheme;
	private final ListBox themeList = new ListBox();
	private TextBox newTheme;
	private Button addNewTheme;
	private Button changeTheme;
	private Button deleteTheme;

	private Label chooseTest;
	private final ListBox testList = new ListBox();
	private TextBox newTest;
	private Button addNewTest;
	private Button changeTest;
	private Button deleteTest;

	private Label chooseQuestion;
	private final ListBox questionList = new ListBox();
	private TextBox newQuestion;
	private Button addNewQuestion;
	private Button changeQuestion;
	private Button deleteQuestion;

	private Label chooseAnswer;
	private final ListBox answerList = new ListBox();
	private TextBox newAnswer;
	private Button addNewAnswer;
	private Button changeAnswer;
	private Button deleteAnswer;
	private CheckBox isRight;

	private Button startButton;
	private Button endButton;

	private List<CheckBox> listOfCheckBox = new LinkedList<>();
	private FlexTable testBody;

	public void onModuleLoad() {

		loginBox = new TextBox();
		passwordBox = new PasswordTextBox();
		loginBox.setMaxLength(20);
		loginBox.setWidth("15em");
		passwordBox.setMaxLength(20);
		passwordBox.setWidth("15em");

		loginForm = new FlexTable();
		loginForm.setText(0, 0, "Введите логин:");
		loginForm.setWidget(0, 1, loginBox);
		loginForm.setText(1, 0, "Введите пароль:");
		loginForm.setWidget(1, 1, passwordBox);

		signIn = new Button("Войти");
		signIn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				login(loginBox.getText(), passwordBox.getText());
			}

		});

		registration = new Button("Зарегистрироваться");
		registration.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				registrate(loginBox.getText(), passwordBox.getText());
			}
		});

		info = new Label(
				"Размеры логина и пароля должны содержать от3 до 20 символов и содержать только латинские буквы и цифры");
		error = new Label("");
		logged = new Label("");

		signOut = new Button("Выход");
		signOut.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("ChooseForm").setVisible(false);
				RootPanel.get("TestForm").setVisible(false);
				RootPanel.get("SignOutForm").setVisible(false);
				logged.setText("");
				error.setText("");
				testBody.clear(true);
				startButton.setVisible(true);

				RootPanel.get("LoginForm").setVisible(true);
				login = "";
				isTutor = false;
			}
		});

		chooseTheme = new Label("\n Выберите предмет:\n");
		themeList.setVisibleItemCount(1);
		themeList.setWidth("25em");
		getDatas(0, themeList);
		themeList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getDatas(Integer.parseInt(themeList.getSelectedValue()), testList);
				newTheme.setText(themeList.getSelectedItemText());
				questionList.clear();
				answerList.clear();
			}
		});
		newTheme = new TextBox();
		newTheme.setWidth("25em");
		addNewTheme = new Button("Добавить предмет");
		addNewTheme.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				testingService.insertDataObject(new DataObject(0, newTheme.getText(), 0), new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newTheme.setText("");
						getDatas(0, themeList);
						testList.clear();
						questionList.clear();
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});
		changeTheme = new Button("Переименовать");
		changeTheme.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedTheme = Integer.parseInt(themeList.getSelectedValue());
				testingService.updateDataObject(selectedTheme, newTheme.getText(), 0, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newTheme.setText("");
						getDatas(0, themeList);
						testList.clear();
						questionList.clear();
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});
		deleteTheme = new Button("Удалить предмет");
		deleteTheme.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedTheme = Integer.parseInt(themeList.getSelectedValue());
				testingService.deleteDataObject(selectedTheme, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						getDatas(0, themeList);
						testList.clear();
						questionList.clear();
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});

		chooseTest = new Label("\n Выберите тему теста:\n");
		testList.setVisibleItemCount(1);
		testList.setWidth("25em");
		testList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getDatas(Integer.parseInt(testList.getSelectedValue()), questionList);
				newTest.setText(testList.getSelectedItemText());
				answerList.clear();
			}
		});
		newTest = new TextBox();
		newTest.setWidth("25em");
		addNewTest = new Button("Добавить тест");
		addNewTest.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedTheme = Integer.parseInt(themeList.getSelectedValue());
				testingService.insertDataObject(new DataObject(selectedTheme, newTest.getText(), 0),
						new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newTest.setText("");
						getDatas(Integer.parseInt(themeList.getSelectedValue()), testList);
						questionList.clear();
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});
		changeTest = new Button("Переименовать");
		changeTest.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedTest = Integer.parseInt(testList.getSelectedValue());
				testingService.updateDataObject(selectedTest, newTest.getText(), 0, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newTest.setText("");
						getDatas(Integer.parseInt(themeList.getSelectedValue()), testList);
						questionList.clear();
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});
		deleteTest = new Button("Удалить тест");
		deleteTest.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedTest = Integer.parseInt(testList.getSelectedValue());
				testingService.deleteDataObject(selectedTest, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						getDatas(Integer.parseInt(themeList.getSelectedValue()), testList);
						questionList.clear();
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});

		chooseQuestion = new Label("\n Выберите вопрос:\n");
		questionList.setVisibleItemCount(1);
		questionList.setWidth("40em");
		questionList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getDatas(Integer.parseInt(questionList.getSelectedValue()), answerList);
				newQuestion.setText(questionList.getSelectedItemText());
			}
		});
		newQuestion = new TextBox();
		newQuestion.setWidth("40em");
		addNewQuestion = new Button("Добавить вопрос");
		addNewQuestion.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedTest = Integer.parseInt(testList.getSelectedValue());
				testingService.insertDataObject(new DataObject(selectedTest, newQuestion.getText(), 0),
						new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newQuestion.setText("");
						getDatas(Integer.parseInt(testList.getSelectedValue()), questionList);
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});
		changeQuestion = new Button("Изменить вопрос");
		changeQuestion.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedQuestion = Integer.parseInt(questionList.getSelectedValue());
				testingService.updateDataObject(selectedQuestion, newQuestion.getText(), 0, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newQuestion.setText("");
						getDatas(Integer.parseInt(testList.getSelectedValue()), questionList);
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});
		deleteQuestion = new Button("Удалить вопрос");
		deleteQuestion.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedQuestion = Integer.parseInt(questionList.getSelectedValue());
				testingService.deleteDataObject(selectedQuestion, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						getDatas(Integer.parseInt(testList.getSelectedValue()), questionList);
						answerList.clear();
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});

		chooseAnswer = new Label("\n Выберите вопрос:\n");
		answerList.setVisibleItemCount(1);
		answerList.setWidth("40em");
		answerList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				newAnswer.setText(answerList.getSelectedItemText());
				isAnswerRight(Integer.parseInt(answerList.getSelectedValue()));
			}
		});
		newAnswer = new TextBox();
		newAnswer.setWidth("40em");
		isRight = new CheckBox();
		isRight.setText("Верный ответ");
		addNewAnswer = new Button("Добавить ответ");
		addNewAnswer.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedQuestion = Integer.parseInt(questionList.getSelectedValue());
				testingService.insertDataObject(
						new DataObject(selectedQuestion, newAnswer.getText(), isRight.getValue() ? 1 : 0),
						new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newAnswer.setText("");
						getDatas(Integer.parseInt(questionList.getSelectedValue()), answerList);
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
				isRight.setValue(false);
			}
		});
		changeAnswer = new Button("Изменить ответ");
		changeAnswer.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedAnswer = Integer.parseInt(answerList.getSelectedValue());
				testingService.updateDataObject(selectedAnswer, newAnswer.getText(), (isRight.getValue() ? 1 : 0),
						new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						newAnswer.setText("");
						getDatas(Integer.parseInt(questionList.getSelectedValue()), answerList);
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
				isRight.setValue(false);
			}
		});
		deleteAnswer = new Button("Удалить ответ");
		deleteAnswer.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				int selectedAnswer = Integer.parseInt(answerList.getSelectedValue());
				testingService.deleteDataObject(selectedAnswer, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						getDatas(Integer.parseInt(questionList.getSelectedValue()), answerList);
						error.setText("");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка!");
					}
				});
			}
		});

		startButton = new Button("Начать тест!");
		startButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				startButton.setVisible(false);
				fillTheTable();
				endButton.setVisible(!isTutor);
				RootPanel.get("TestForm").setVisible(true);
			}
		});

		testBody = new FlexTable();

		endButton = new Button("Отправить ответы!");
		endButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				sendResult();
			}
		});
		endButton.setVisible(false);

		RootPanel.get("ErrorForm").add(error);
		RootPanel.get("SignOutForm").setVisible(false);
		RootPanel.get("SignOutForm").add(logged);
		RootPanel.get("SignOutForm").add(signOut);
		RootPanel.get("LoginForm").add(loginForm);
		RootPanel.get("LoginForm").add(info);
		RootPanel.get("LoginForm").add(signIn);
		RootPanel.get("LoginForm").add(registration);

		RootPanel.get("ChooseForm").setVisible(false);

		RootPanel.get("ChooseForm").add(chooseTheme);
		RootPanel.get("ChooseForm").add(themeList);
		RootPanel.get("ChooseForm").add(deleteTheme);
		RootPanel.get("ChooseForm").add(new Label("\n"));
		RootPanel.get("ChooseForm").add(newTheme);
		RootPanel.get("ChooseForm").add(addNewTheme);
		RootPanel.get("ChooseForm").add(changeTheme);
		RootPanel.get("ChooseForm").add(new Label("\n"));

		RootPanel.get("ChooseForm").add(chooseTest);
		RootPanel.get("ChooseForm").add(testList);
		RootPanel.get("ChooseForm").add(deleteTest);
		RootPanel.get("ChooseForm").add(new Label("\n"));
		RootPanel.get("ChooseForm").add(newTest);
		RootPanel.get("ChooseForm").add(addNewTest);
		RootPanel.get("ChooseForm").add(changeTest);
		RootPanel.get("ChooseForm").add(new Label("\n"));

		RootPanel.get("ChooseForm").add(chooseQuestion);
		RootPanel.get("ChooseForm").add(questionList);
		RootPanel.get("ChooseForm").add(deleteQuestion);
		RootPanel.get("ChooseForm").add(new Label("\n"));
		RootPanel.get("ChooseForm").add(newQuestion);
		RootPanel.get("ChooseForm").add(addNewQuestion);
		RootPanel.get("ChooseForm").add(changeQuestion);
		RootPanel.get("ChooseForm").add(new Label("\n"));

		RootPanel.get("ChooseForm").add(chooseAnswer);
		RootPanel.get("ChooseForm").add(answerList);
		RootPanel.get("ChooseForm").add(deleteAnswer);
		RootPanel.get("ChooseForm").add(new Label("\n"));
		RootPanel.get("ChooseForm").add(newAnswer);
		RootPanel.get("ChooseForm").add(addNewAnswer);
		RootPanel.get("ChooseForm").add(changeAnswer);
		RootPanel.get("ChooseForm").add(new Label("\n"));
		RootPanel.get("ChooseForm").add(isRight);
		RootPanel.get("ChooseForm").add(new Label("\n"));
		RootPanel.get("ChooseForm").add(startButton);

		RootPanel.get("TestForm").setVisible(false);
		RootPanel.get("TestForm").add(testBody);
		RootPanel.get("TestForm").add(endButton);

	}

	private void login(String nickname, String password) {
		disableLoginForm();
		if (!(FieldVerifier.isValidName(nickname) && FieldVerifier.isValidName(password))) {
			onFailureLogin();
			return;
		}
		login = nickname;

		testingService.login(nickname, password, new AsyncCallback<Byte>() {

			@Override
			public void onSuccess(Byte result) {
				switch (result) {
				case 1:
					onSuccessLogin();
					isTutor = false;
					showChooseMenu();
					RootPanel.get("ChooseForm").setVisible(true);
					break;
				case 2:
					onSuccessLogin();
					isTutor = true;
					showChooseMenu();
					RootPanel.get("ChooseForm").setVisible(true);
					break;
				default:
					onFailure(new Throwable());
					break;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				onFailureLogin();
			}

		});
	}

	private void registrate(String nickname, String password) {
		disableLoginForm();
		if (!(FieldVerifier.isValidName(nickname) && FieldVerifier.isValidName(password))) {
			onFailureLogin();
			return;
		}
		login = nickname;

		testingService.registrate(nickname, password, new AsyncCallback<Byte>() {

			@Override
			public void onSuccess(Byte result) {
				switch (result) {
				case 1:
					onSuccessLogin();
					isTutor = false;
					showChooseMenu();
					break;
				default:
					onFailure(new Throwable());
					break;
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				onFailureLogin();
			}
		});
	}

	private void onSuccessLogin() {
		enableLoginForm();
		logged.setText(login);
		RootPanel.get("LoginForm").setVisible(false);
		passwordBox.setText("");
		error.setText("");
		RootPanel.get("SignOutForm").setVisible(true);
	}

	private void onFailureLogin() {
		error.setText("Ошибка! Проверьте правильность ввода логина и пароля!");
		login = "";
		passwordBox.setText("");
		enableLoginForm();
	}

	private void enableLoginForm() {
		loginBox.setEnabled(true);
		passwordBox.setEnabled(true);
		signIn.setEnabled(true);
		registration.setEnabled(true);
	}

	private void disableLoginForm() {
		loginBox.setEnabled(false);
		passwordBox.setEnabled(false);
		signIn.setEnabled(false);
		registration.setEnabled(false);
	}

	private void showChooseMenu() {
		newTheme.setVisible(isTutor);
		addNewTheme.setVisible(isTutor);
		changeTheme.setVisible(isTutor);
		deleteTheme.setVisible(isTutor);

		newTest.setVisible(isTutor);
		addNewTest.setVisible(isTutor);
		changeTest.setVisible(isTutor);
		deleteTest.setVisible(isTutor);

		chooseQuestion.setVisible(isTutor);
		questionList.setVisible(isTutor);
		newQuestion.setVisible(isTutor);
		addNewQuestion.setVisible(isTutor);
		changeQuestion.setVisible(isTutor);
		deleteQuestion.setVisible(isTutor);

		chooseAnswer.setVisible(isTutor);
		answerList.setVisible(isTutor);
		newAnswer.setVisible(isTutor);
		addNewAnswer.setVisible(isTutor);
		changeAnswer.setVisible(isTutor);
		deleteAnswer.setVisible(isTutor);
		isRight.setVisible(isTutor);

		RootPanel.get("ChooseForm").setVisible(true);
	}

	public void isAnswerRight(int id) {
		testingService.isAnswerRight(id, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				error.setText("");
				if (result)
					isRight.setValue(true);
				else
					isRight.setValue(false);
			}

			@Override
			public void onFailure(Throwable caught) {
				error.setText("Ошибка загрузки данных!");
			}
		});
	}

	public void getDatas(int prev, ListBox list) {
		list.clear();
		testingService.getDatas(prev, new MyAsyncCallback(list));
	}

	private class MyAsyncCallback implements AsyncCallback<List<DataObject>> {
		ListBox list;

		public MyAsyncCallback(ListBox list) {
			super();
			this.list = list;
		}

		@Override
		public void onSuccess(List<DataObject> result) {
			error.setText("");
			if (result != null)
				for (DataObject data : result) {
					list.addItem(data.getText(), data.getId() + "");
				}
			list.setSelectedIndex(0);
		}

		@Override
		public void onFailure(Throwable caught) {
			error.setText("Ошибка загрузки данных!");
		}
	}

	private void sendResult() {
		List<Integer> answers = new LinkedList<>();
		for (CheckBox box : listOfCheckBox) {
			if (box.getValue()) {
				answers.add(Integer.parseInt(box.getFormValue()));
			}
		}
		error.setText(answers.get(0) + "");
		Integer[] result = answers.toArray(new Integer[answers.size()]);
		error.setText("dva");

		testingService.sendResult(login, Integer.parseInt(testList.getSelectedValue()), result,
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						error.setText("Отправлено!");
					}

					@Override
					public void onFailure(Throwable caught) {
						error.setText("Ошибка, отправьте еще раз!");
					}

				});

	}

	private void fillTheTable() {
		error.setText("");
		testBody.clear();
		listOfCheckBox.clear();
		testingService.getTest(Integer.parseInt(testList.getSelectedValue()), new TestAsyncCallback());
	}

	private class TestAsyncCallback implements AsyncCallback<List<DataObject>> {

		@Override
		public void onSuccess(List<DataObject> result) {
			error.setText("");
			int testId = Integer.parseInt(testList.getSelectedValue());
			int questionId = 0;
			for (DataObject data : result) {
				int rowCount = testBody.getRowCount();
				if (data.getPrev() == testId) {
					testBody.setHTML(rowCount, 0, "<hr noshade size=\"1\">");
					rowCount++;
					questionId = data.getId();
					testBody.setHTML(rowCount, 0, data.getText());
				}
				if (data.getPrev() == questionId) {
					listOfCheckBox.add(new CheckBox(data.getText()));
					listOfCheckBox.get(listOfCheckBox.size() - 1).setFormValue(data.getId() + "");
					testBody.setWidget(rowCount, 0, listOfCheckBox.get(listOfCheckBox.size() - 1));
				}
			}
			testBody.setHTML(testBody.getRowCount(), 0, "<hr noshade size=\"1\">");
		}

		@Override
		public void onFailure(Throwable caught) {
			error.setText("Ошибка загрузки теста!");
		}

	}

}
