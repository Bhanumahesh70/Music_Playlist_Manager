package controllers;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.LoginModel;
import models.UserModel;
import models.UserTable_DatabaseModel;

public class LoginController {
	@FXML
	private TextField username_text;

	@FXML
	private PasswordField password_text;

	@FXML
	private Label loginPrompt;
	
	@FXML
	private TextField userNameText;
	
	@FXML
	private TextField emailText;
	
	@FXML
	private TextField passwordText;
	
	@FXML
	private TextField confirmPasswordText;
	
	@FXML
	private Label signupErrorLabel;
	
	
	@FXML
	private Pane loginPane;
	
	@FXML
	private Pane signUpPane;

	private LoginModel model;

	public LoginController() {
		model = new LoginModel();
	}

	public void login() {

		loginPrompt.setText("");
		String username = this.username_text.getText();
		String password = this.password_text.getText();

		// Validations
		if (username == null || username.trim().equals("")) {
			loginPrompt.setText("Username Cannot be empty or spaces");
			return;
		}
		if (password == null || password.trim().equals("")) {
			loginPrompt.setText("Password Cannot be empty or spaces");
			return;
		}
		if (username == null || username.trim().equals("") && (password == null || password.trim().equals(""))) {
			loginPrompt.setText("User name / Password Cannot be empty or spaces");
			return;
		}
		// authentication check
		checkCredentials(username, password);

	}

	public void checkCredentials(String username, String password) {
		Boolean isValid = model.getCredentials(username, password);
		if (!isValid) {
			loginPrompt.setText("User does not exist!");
			return;
		}
		try {
			AnchorPane root;
			if (model.isAdmin() && isValid) {
				// If user is admin, inflate admin view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/AdminView.fxml"));
				Main.stage.setTitle("Admin View");
			} else {
				// If user is customer, inflate customer view
				root = (AnchorPane) FXMLLoader.load(getClass().getResource("/views/ClientView.fxml"));
				// ***Set user ID acquired from db****
				int user_id = LoginModel.getUserId(); // hard coded for testing purposes only!!
				 ClientController.setUser(user_id);
				Main.stage.setTitle("Client View");
			}
			Scene scene = new Scene(root);
			Main.stage.setScene(scene);
		} catch (Exception e) {
			System.out.println("Error occured while inflating view: " + e);
		}

	}
	
	@FXML
	public void signUp() {
		signUpPane.setVisible(true);
		loginPane.setVisible(false);
	}
	
	@FXML
	public void backToLogin() {
		signUpPane.setVisible(false);
		loginPane.setVisible(true);
	}
	
	@FXML
	public void refresh() {
		 // Clear text input fields
		userNameText.clear();
		emailText.clear();
		passwordText.clear();
		confirmPasswordText.clear();
	}
	
	@FXML
	public void signUpNewUser(){
		
		String userName = userNameText.getText().trim();
		String email = emailText.getText().trim() ;
		String password = passwordText.getText().trim();
		String confirmPassword = confirmPasswordText.getText().trim();
		
		if (checkUserInputs(userName, email, password, confirmPassword)) {
			
			UserModel user = new UserModel(-1, userName, confirmPassword, false);
			if (UserTable_DatabaseModel.isUserNameexists(user)) {
				signupErrorLabel.setText("The user '" + userName + "' is already present enter a new user name");
			} else {
				UserTable_DatabaseModel.insertUserInToDatabase(user);
				signupErrorLabel.setText("The user '" + userName + "' is created");
				
			}
		}

	}
	
	public boolean checkUserInputs(String userName,String email,String password,String confirmPassword) {
		boolean status = true;
		if(userName.equals("")){
			signupErrorLabel.setText("User name cannot be empty");
			status = false;
		}
		if(email.equals("")){
			signupErrorLabel.setText("Email address cannot be empty");
			status = false;
		}
		if(password.equals("")){
			signupErrorLabel.setText("Password cannot be empty");
			status = false;
		}
		
		if(userName.equals("") && email.equals("")) {
			signupErrorLabel.setText("User name and Email address cannot be empty");
			status = false;
		}
		if(userName.equals("") && password.equals("")) {
			signupErrorLabel.setText("User name and Password address cannot be empty");
			status = false;
		}
		if( email.equals("")&& password.equals("")) {
			signupErrorLabel.setText("Email address and Password cannot be empty");
			status = false;
		}
		
		
		if(userName.equals("") && email.equals("") && password.equals("")) {
			signupErrorLabel.setText("User name, Email address and password cannot be empty");
			status = false;
		}
		if(!userName.equals("") && !email.equals("")&& !confirmPassword.equals(password)) {
			signupErrorLabel.setText("Confrim password doesnot match");
			status = false;
		}
		return status;
	
	}
	
	

	public void exit() {
		System.exit(0);

	}

}
