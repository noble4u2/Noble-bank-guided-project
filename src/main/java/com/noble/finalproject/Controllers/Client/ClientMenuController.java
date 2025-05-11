package com.noble.finalproject.Controllers.Client;

import com.noble.finalproject.Models.Model;
import com.noble.finalproject.Views.ClientMenuOptions;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    @FXML
    public Button report_btn;
    @FXML
    public Button dashboard_btn;
    @FXML
    public Button transaction_btn;
    @FXML
    public Button accounts_btn;
    @FXML
    public Button profile_btn;
    @FXML
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
    }

    @FXML
    private void addListeners (){
        dashboard_btn.setOnAction(event -> onDashboard() );
        transaction_btn.setOnAction(event -> onTransaction() );
        accounts_btn.setOnAction(events -> onAccounts());
        profile_btn.setOnAction(event -> onProfile());
        logout_btn.setOnAction(event -> onLogout());
    }

    @FXML
    private void onDashboard (){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    @FXML
    private void onTransaction () {
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    private void onAccounts (){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    private void onProfile (){
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.PROFILE);
    }

    private void onLogout (){
//        Get the stage
        Stage stage = (Stage) dashboard_btn.getScene().getWindow();
//        Close the  client window
        Model.getInstance().getViewFactory().closeStage(stage);
//        Show login window
        Model.getInstance().getViewFactory().showLoginWindow();
//        Set client Login success flag tp false
        Model.getInstance().setClientLoginSuccessFlag(false);
        Model.getInstance().getDatabaseDriver().closeConnection();
    }
}
