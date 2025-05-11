package com.noble.finalproject.Controllers.Client;

import com.noble.finalproject.Models.Model;
import com.noble.finalproject.Views.TransactionCellFactory;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionsController implements Initializable {

    public ListView trasactions_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAllTransactionsList();
        trasactions_listview.setItems(Model.getInstance().getAllTransaction());
        trasactions_listview.setCellFactory(e -> new TransactionCellFactory()
        );
    }

    private void initAllTransactionsList(){
        if (Model.getInstance().getAllTransaction().isEmpty()){
            Model.getInstance().setAllTransaction();
        } else {
            FXCollections.reverse(Model.getInstance().getAllTransaction());
        }
    }
}
