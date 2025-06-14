package com.noble.finalproject.Controllers.Client;

import com.noble.finalproject.Models.Model;
import com.noble.finalproject.Models.Transaction;
import com.noble.finalproject.Views.TransactionCellFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public Text user_name;
    public Label login_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Label income_lbl;
    public Label expense_lbl;
    public ListView transaction_listview;
    public TextField payee_fld;
    public TextField amount_fld;
    public TextArea message_fld;
    public Button send_money_btn;
    public Label success_message_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
          bindData();
          initLatestTransactionsList();
          transaction_listview.setItems(Model.getInstance().getLatestTransactions());
          transaction_listview.setCellFactory(e -> new TransactionCellFactory());
          send_money_btn.setOnAction(event -> onSendMoney());
          accountSummary();
    }

    private void bindData(){
        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getClient().firstNameProperty()));
        login_date.setText("Today, "+ LocalDate.now());
        checking_bal.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().balanceProperty().asString());
        checking_acc_num.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        savings_bal.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().balanceProperty().asString());
        savings_acc_num.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
    }
    private void initLatestTransactionsList(){
        if (Model.getInstance().getLatestTransactions().isEmpty()){
            Model.getInstance().setLatestTransactions();
        }/*else {
            FXCollections.reverse(Model.getInstance().getLatestTransactions());
        }*/
    }

    private void onSendMoney(){
        String receiver = payee_fld.getText();
        double amount;
        String message = message_fld.getText();
        String sender = Model.getInstance().getClient().pAddressProperty().get();

        try {
            amount = Double.parseDouble(amount_fld.getText());
        } catch (NumberFormatException e){
            success_message_lbl.setText("Invalid amount entered");
            success_message_lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            amount_fld.setText("");
            message_fld.setText("");
            return;
        }
//        TO CHECK IF THE RECEIVER EXISTS IN THE DATABASE
        ResultSet resultSet = Model.getInstance().getDatabaseDriver().searchClient(receiver);


        try {
            if (!resultSet.isBeforeFirst()){ // IF THERE IS NO ROW IN THE RESULT, THE RECEIVER IS NOT FOUND
                success_message_lbl.setText("Can't find this user");
                success_message_lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                payee_fld.setText("");
                amount_fld.setText("");
                message_fld.setText("");
            } else {
//                IF THE RECEIVER IS FOUND, THE UPDATES THE RECEIVER'S BALANCE
                Model.getInstance().getDatabaseDriver().updateBalance(receiver,amount,"ADD");
//                SUBTRACT FROM SENDER'S SAVINGS ACCOUNT
                Model.getInstance().getDatabaseDriver().updateBalance(sender,amount,"SUB");
                Model.getInstance().getClient().savingsAccountProperty().get().setBalance(
                        Model.getInstance().getDatabaseDriver().getSavingsAccountBalance(sender)
                );
//                        RECORD NEW TRANSACTION
                Model.getInstance().getDatabaseDriver().newTransaction(sender,receiver,amount,message);
//                        CLEAR THE FIELDS
                payee_fld.setText("");
                amount_fld.setText("");
                message_fld.setText("");

                success_message_lbl.setText("$"+amount+" Sent Successfully");
                success_message_lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            }
        } catch (Exception e) {
            e.printStackTrace();
            success_message_lbl.setText("An error occured, please try again !!");
        }
    }

//    METHOD CALCULATES ALL EXPENSES AND INCOME
    private void accountSummary(){
        double income = 0;
        double expenses = 0;
        if (Model.getInstance().getAllTransaction().isEmpty())
        {
            Model.getInstance().setAllTransaction();
        }
        for (Transaction transaction: Model.getInstance().getAllTransaction()){
            if (transaction.senderProperty().get().equals(Model.getInstance().getClient().pAddressProperty().get())){
                expenses = expenses + transaction.amountProperty().get();
            } else {
                income = income + transaction.amountProperty().get();
            }
        }
        income_lbl.setText("+ $"+income);
        expense_lbl.setText("- $"+expenses);
    }
}
