package com.noble.finalproject.Controllers.Client;

import com.noble.finalproject.Models.CheckingAccount;
import com.noble.finalproject.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable

{
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button transfer_to_sv_btn;
    public TextField amount_to_ch;
    public Button transfer_to_ch_btn;
    public Label transac_msg_1;
    public Label transac_msg_2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accountDetails();
        transfer_to_ch_btn.setOnAction(event -> sendFundsToChe());
        transfer_to_sv_btn.setOnAction(event -> sendFundsToSv());
    }

    public void accountDetails(){
        ch_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());


        ch_acc_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        sv_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());


        sv_acc_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
    }

    public void sendFundsToChe (){
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        double amountToChe = 0;
        try{
            amountToChe = Double.parseDouble(amount_to_ch.getText());
        } catch (NumberFormatException e){
            transac_msg_2.setText("Invalid amount entered");
            transac_msg_2.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            transac_msg_2.setText("");
            transac_msg_2.setText("");
        }
        try{
            Model.getInstance().getDatabaseDriver().transToChe(pAddress,amountToChe,"subFromSavings");
            Model.getInstance().getDatabaseDriver().transToChe(pAddress, amountToChe, "addToChecking ");
            transac_msg_2.setText("$"+amountToChe+" Sent Successfully");
            transac_msg_2.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");

            Model.getInstance().getClient().savingsAccountProperty().get().setBalance(
                    Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(pAddress)
            );

        } catch (Exception e) {
            e.printStackTrace();
            transac_msg_2.setText("An error occured, please try again !!");
            transac_msg_2.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            transac_msg_2.setText("");
            transac_msg_2.setText("");
        }

    }

    public void sendFundsToSv (){
        String pAddress = Model.getInstance().getClient().pAddressProperty().get();
        double amountToSv = Double.parseDouble(amount_to_sv.getText());
        Model.getInstance().getDatabaseDriver().transToChe(pAddress,amountToSv,"subFromChecking");
        Model.getInstance().getDatabaseDriver().transToChe(pAddress,amountToSv,"addToSavings");
    }
}
