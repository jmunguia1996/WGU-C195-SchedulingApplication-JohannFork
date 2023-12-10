package controller;

import dataAccessObject.userDAO;

import devTools.DevToolC;

import devTools.JDBTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;




import java.io.IOException;


public class Login {


    @FXML public Label loginTitle;
    @FXML public Label usernameLabel;
    @FXML private TextField usernameInput;
    @FXML public Label passwordLabel;
    @FXML private TextField passwordInput;
    @FXML public Label locationLabel;
    @FXML private TextField loginScreenLocationField;
    @FXML public Button loginButton;
    @FXML public Label darkModeLabel;
    @FXML public Button darkModeButton; //FXML fx:id References
    @FXML public Button exitButton;

    Stage stage;

    private void loginMethod() throws IOException, InterruptedException {

        // TODO [c] Get login info from input.
        String nameInput = usernameInput.getText();
        String passInput = passwordInput.getText();

        // TODO [cu] solve login on no input error.
        if (nameInput.isEmpty() || passInput.isEmpty()) {
            DevToolC.println("Input Values Found Empty, login attempt abandoned.");
            return;
        }

        JDBTools.openConnection();

        //Missing Skill,
        //Skill Found: DAO Use Case:
        // Need to create a Data Access Object(DAO).
        //DAOs are abstractions used to access persistent mechanisms
        //such as Data Bases. Focused often on CRUD (Create, Read,
        // Update, Delete) operations on data entities.

        // TODO [c] create a dataAccessObject package

        // TODO [c] create a DAO to access user date inside the SQL DB
        //Validates user input
        boolean loginValidated = userDAO.validateUserLogin(nameInput,passInput);

        // TODO [cu] create a incorrect password popup
        if (! loginValidated) {
            DevToolC.println("Input on Password or Username not found. Login attempt abandoned");
            // TODO [cu] schedulingApplicationPopup is not firing -The version of java alerts was out of date in the example I was looking at.
            // TODO [cu] create an class to store popups settings for this application -SchedulingApplicationPrompt
            //showIncorrectPasswordPopup();
            SchedulingApplicationPrompt popup = new SchedulingApplicationPrompt();
            popup.loginFailedPopup();


//

            DevToolC.println("SchedulingApplicationPrompt Now!");
        }


        if (loginValidated){ DevToolC.println("User input Validated, Logging in."); }


        //TODO [c] create home menu fxml
        //TODO [c] create load home menu on validation logic //Committing
        if (loginValidated) {
            Home.testMethod();
            //TODO [Extra] Complete dark mode pass through from this scene to home
            Home.loadHomeFXML(stage, loginButton, getClass());
        }

        //showIncorrectPasswordPopup();


    }

    public void loginClick(ActionEvent actionEvent) throws IOException, InterruptedException {
        System.out.println("Executing loginClick method: ");
        loginMethod();

    }

    public void loginEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {

            DevToolC.println("Enter key pressed. Preforming Login Action.");

        }
    }

    public void handleDarkButtonClick(ActionEvent actionEvent) {
        System.out.println("Executing handleDarkButtonClick");
    }

    public void exitClick(ActionEvent actionEvent) {
        System.out.println("Executing exitClick");
    }

}