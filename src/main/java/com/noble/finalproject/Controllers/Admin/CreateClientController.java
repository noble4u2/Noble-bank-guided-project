package com.noble.finalproject.Controllers.Admin;

import com.noble.finalproject.Models.Model;
import com.noble.finalproject.Views.AccountType;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Random;
import java.util.ResourceBundle;

public class CreateClientController implements Initializable {
    public TextField fname_fld;
    public TextField lname_fld;
    public TextField password_fld;
    public CheckBox pAddress_box;
    public Label pAddress_lbl;
    public CheckBox ch_acc_box;
    public TextField ch_amount_fld;
    public CheckBox sv_acc_box;
    public TextField sv_amount_fld;
    public Button create_client_btn;
    public Label error_lbl;

    private String payeeAddress;
    private boolean createCheckingAccountFlag = false;
    private boolean createSavingAccountFlag = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        create_client_btn.setOnAction(event -> createClient());
        pAddress_box.selectedProperty().addListener((observable, oldVal, newVal) ->{
            if (newVal){
                payeeAddress = createPayeeAddress();
                onCreatePayeeAddress();
            }
        } );
        ch_acc_box.selectedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal){
                createCheckingAccountFlag = true;
            }
        } );
        sv_acc_box.selectedProperty().addListener((observableValue, oldVal, newVal) -> {
            if (newVal){
                createSavingAccountFlag = true;
            }
        } );
    }

    private void createClient(){
//        Create Checking Account
        if (createCheckingAccountFlag){
            createAccount("Checking");
        }

//        Create saving account
         if (createSavingAccountFlag ){
             createAccount("Savings");
         }
//         Create Client
        String fName = fname_fld.getText();
         String lName = lname_fld.getText();
         String password = password_fld.getText();
         String currentDate = LocalDate.now().toString();
         Model.getInstance().getDatabaseDriver().createClient(fName,lName,payeeAddress, password, currentDate);
         error_lbl.setStyle("-fx-text-fill: blue; -fx-font-size: 1.3em; -fx-font-weight: bold");
         error_lbl.setText("Client Created successfully");
         emptyField();
    }

    private void createAccount(String accountType){
        double balance = Double.parseDouble(ch_amount_fld.getText());
//        GENERATE ACCOUNT NUMBER
        String firstSection = "3201";
        String lastSection = Integer.toString((new Random()).nextInt(9999) + 1000);
        String accountNumber = firstSection + " "+ lastSection;

//        CREATE CHECKING ACCOUNT   OR SAVINGS ACCOUNT
        if (accountType.equals("Checking")){
            Model.getInstance().getDatabaseDriver().createCheckingAccount(payeeAddress, accountNumber, 10, balance);
        } else {
            Model.getInstance().getDatabaseDriver().createSavingsAccount(payeeAddress, accountNumber, 2000, balance);
        }
    }

    private void onCreatePayeeAddress(){
        if (fname_fld.getText() != null && lname_fld.getText() != null){
            pAddress_lbl.setText(payeeAddress);
        }
    }

    private String createPayeeAddress(){
        int id = Model.getInstance().getDatabaseDriver().getLastClientId() + 1;
        char fChar = Character.toLowerCase(fname_fld.getText().charAt(0));
        return "@" + fChar + lname_fld.getText()+id;
    }

    private void emptyField(){
        fname_fld.setText("");
        lname_fld.setText("");
        password_fld.setText("");
        pAddress_lbl.setText("");
        ch_acc_box.setSelected(false);
        ch_amount_fld.setText("");
        sv_acc_box.setSelected(false);
        sv_amount_fld.setText("");
    }
}
