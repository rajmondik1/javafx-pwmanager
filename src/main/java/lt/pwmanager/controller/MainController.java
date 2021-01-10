package lt.pwmanager.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    private Text credentialsName;

    @FXML
    private Button addGroupBtn;

    @FXML
    private Button removeGroupBtn;

    @FXML
    private Button addCredentialBtn;

    @FXML
    private Button removeCredentialBtn;

    @FXML
    private Button toggleShowPassword;

    @FXML
    private Button copyPassword;

    @FXML
    private TextField passwordTextInput;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        passwordInput.textProperty().bindBidirectional(passwordTextInput.textProperty());

        credentialsListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Credentials>() {
            @Override
            public void changed(ObservableValue<? extends Credentials> observableValue, Credentials old, Credentials newValue) {
                if (newValue != null) {
                    emailInput.setDisable(false);
                    passwordInput.setDisable(false);
                    emailInput.setText(newValue.getEmail());
                    passwordInput.setText(newValue.getPassword());
                    credentialsName.setText(newValue.getName());
                } else {
                    emailInput.clear();
                    passwordInput.clear();
                    emailInput.setDisable(true);
                    passwordInput.setDisable(true);
                    credentialsName.setText("");
                }
            }
        });
    }

    @FXML
    void credentialSelected(MouseEvent event) {
    }

    @FXML
    void credentialGroupSelected(MouseEvent event) {
        CredentialGroup selected = getSelectedGroup();
        if (selected != null) {
            credentialsListView.getItems().clear();
            credentialsListView.getItems().addAll(selected.getCredentials());
        }
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
        credentialsGroupListView.getItems().clear();
        credentialsListView.getItems().clear();
        groups.forEach(group -> credentialsGroupListView.getItems().add(group));

        setTitle(fileService.getFileName());
    }

    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void save(ActionEvent event) {
        List<CredentialGroup> credentials = credentialsGroupListView.getItems();
        FileService fileService = FileService.getInstance();
        fileService.writeToFile(credentials);
        setTitle(fileService.getFileName());
    }

    @FXML
    void onAddCredentialBtnClick(ActionEvent event) {
        CredentialGroup selected = credentialsGroupListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            alertNoItemSelected("group", "Select existing group or create new in order to add credentials.");
        } else {
            Optional<String> result = addNewModal("credentials");
            result.ifPresent(this::addNewCredentials);
        }
    }

    @FXML
    void onAddGroupBtnClick(ActionEvent event) {
        Optional<String> result = addNewModal("group");
        result.ifPresent(this::addNewGroup);
    }

    @FXML
    void onRemoveCredentialButtonClick(ActionEvent event) {
        Credentials selected = getSelectedCredential();
        if (selected == null) {
            alertNoItemSelected("credentials", "Please select credentials you want to delete!");
        } else {
            Optional<ButtonType> result = removeItemDialog();
            if (result.get() == ButtonType.OK) {
                removeCredentials();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    @FXML
    void onRemoveGroupBtnClick(ActionEvent event) {
        CredentialGroup selected = getSelectedGroup();
        if (selected == null) {
            alertNoItemSelected("group", "Please select group you want to delete!");
        } else {
            Optional<ButtonType> result = removeItemDialog();
            if (result.get() == ButtonType.OK) {
                removeCredentialsGroup();
            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    @FXML
    void emailTextChanged(KeyEvent event) {
        getSelectedCredential().setEmail(emailInput.getText());
    }


    @FXML
    void passwordTextChanged(KeyEvent event) {
        getSelectedCredential().setPassword(passwordInput.getText());
    }

    @FXML
    void onToggleShowPasswordClicked(ActionEvent event) {
        if (passwordInput.isVisible()) {
            passwordInput.setVisible(false);
            passwordTextInput.setVisible(true);
        } else {
            passwordInput.setVisible(true);
            passwordTextInput.setVisible(false);
        }
    }

    @FXML
    void onCopyPasswordClicked(ActionEvent event) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(passwordInput.getText());
        clipboard.setContent(content);
    }

    private void setTitle(String filename) {
        Stage stage = (Stage) credentialsListView.getScene().getWindow();
        stage.setTitle("Password manager   -   " + filename);
    }

    private void addNewGroup(String name) {
        CredentialGroup cg = new CredentialGroup();
        cg.setName(name);
        credentialsGroupListView.getItems().add(cg);
    }

    private void addNewCredentials(String name) {
        CredentialGroup selected = credentialsGroupListView.getSelectionModel().getSelectedItem();

        Credentials cred = new Credentials();
        cred.setName(name);
        selected.addCredentials(cred);
        credentialsListView.getItems().add(cred);
    }

    private Optional<String> addNewModal(String resource) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setTitle("Add new " + resource);
        dialog.setHeaderText("Enter a new " + resource + " name");
        dialog.setContentText("New " + resource + " name:");
        return dialog.showAndWait();
    }

    private void alertNoItemSelected(String name, String text) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No " + name + " selected");
        alert.setHeaderText(null);
        alert.setContentText(text);

        alert.showAndWait();
    }

    private Optional<ButtonType> removeItemDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete item");
        alert.setHeaderText("You are deleting item");
        alert.setContentText("Are you sure you want to delete this?");

        return alert.showAndWait();
    }

    private CredentialGroup getSelectedGroup() {
        return credentialsGroupListView.getSelectionModel().getSelectedItem();
    }

    private Credentials getSelectedCredential() {
        return credentialsListView.getSelectionModel().getSelectedItem();
    }

    private void removeCredentials() {
        List<Credentials> group = credentialsGroupListView.getSelectionModel().getSelectedItem().getCredentials();
        group.remove(credentialsListView.getSelectionModel().getSelectedItem());
        int index = credentialsListView.getSelectionModel().getSelectedIndex();
        credentialsListView.getItems().remove(index);
    }

    private void removeCredentialsGroup() {
        int index = credentialsGroupListView.getSelectionModel().getSelectedIndex();
        credentialsGroupListView.getItems().remove(index);
        credentialsListView.getItems().clear();
    }
}
