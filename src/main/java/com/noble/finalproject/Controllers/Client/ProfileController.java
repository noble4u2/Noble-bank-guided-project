package com.noble.finalproject.Controllers.Client;

import com.noble.finalproject.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    public Circle profile_photo;
    public Label name_lbl;
    public Label ch_acc_num_lbl;
    public Label sv_acc_num_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name_lbl.textProperty().bind(Model.getInstance().getClient().pAddressProperty());
        ch_acc_num_lbl.textProperty().bind(Model.getInstance().getClient().checkingAccountProperty().get().accountNumberProperty());
        sv_acc_num_lbl.textProperty().bind(Model.getInstance().getClient().savingsAccountProperty().get().accountNumberProperty());
    }
}
