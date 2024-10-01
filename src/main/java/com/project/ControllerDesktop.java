package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;

public class ControllerDesktop {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    public void initialize() {
        choiceBox.getItems().addAll("Consolas", "Juegos", "Personajes");
        choiceBox.setValue("Consolas");
    }

    @FXML
    private void handleChoiceBoxAction() {
        String selected = choiceBox.getValue();
        System.out.println("Seleccionado: " + selected);
    }
}
