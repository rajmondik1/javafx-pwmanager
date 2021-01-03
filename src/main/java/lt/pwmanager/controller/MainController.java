package lt.pwmanager.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lt.pwmanager.model.CredentialGroup;
import lt.pwmanager.model.Credentials;
import lt.pwmanager.service.FileService;

public class MainController implements Initializable {

    @FXML
    private ListView<CredentialGroup> credentialsGroupListView;

    @FXML
    private ListView<Credentials> credentialsListView;

    @FXML
    private TextField emailInput;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button mygtukas;

    @FXML
    private Text credentialsName;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
//
//        CredentialGroup cg = new CredentialGroup();
//        cg.setName("new credentials group");
//
//
//        Credentials cred = new Credentials();
//        cred.setName("credentials1");
//        cred.setEmail("email");
//        cred.setPassword("password");
//
//
//        Credentials cred2 = new Credentials();
//        cred2.setName("credentials2");
//        cred2.setEmail("email2");
//        cred2.setPassword("password2");
//
//        cg.addCredentials(cred);
//        cg.addCredentials(cred2);
//
//        credentialsGroupListView.getItems().add(cg);
////        credentialGroupSelected
//        credentialsListView.getItems().add(cred);
//        credentialsListView.getItems().add(cred2);


        emailInput.setText("Hi");
        System.out.println("Hi");

        mygtukas.setOnMouseClicked(e -> System.out.println("hi"));
    }

    @FXML
    void credentialSelected(MouseEvent event) {
        Credentials selected = credentialsListView.getSelectionModel().getSelectedItem();
        if (selected != null) {

            emailInput.setText(selected.getEmail());
            passwordInput.setText(selected.getPassword());
            credentialsName.setText(selected.getName());
            System.out.println(credentialsListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    void credentialGroupSelected(MouseEvent event) {
        System.out.println("credential selected");
    }

    @FXML
    void newFile(ActionEvent event) {
        FileService fileService = FileService.getInstance();
        fileService.newFile();

        setTitle(fileService.getFileName());
    }

    @FXML
    void open(ActionEvent event) {
        FileService fileService = FileService.getInstance();
        fileService.open();
        List<CredentialGroup> groups = fileService.getContents();
        groups.forEach(group -> credentialsGroupListView.getItems().add(group));

        setTitle(fileService.getFileName());
    }

    @FXML
    void quit(ActionEvent event) {

    }

    @FXML
    void save(ActionEvent event) {
        List<CredentialGroup> credentials = credentialsGroupListView.getItems();
        FileService fileService = FileService.getInstance();
        fileService.writeToFile(credentials);
    }

    private void setTitle(String filename) {
        Stage stage = (Stage) credentialsListView.getScene().getWindow();
        stage.setTitle("Password manager   -   " + filename);
    }
}
