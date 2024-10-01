package com.project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ItemListCell extends ListCell<Item> {

    private FXMLLoader mLLoader;
    private ItemCellController controller;

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/com/example/item_cell.fxml"));

                try {
                    HBox root = mLLoader.load();
                    controller = mLLoader.getController();
                    setGraphic(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Aquí configuramos los datos en el controlador
            controller.setData(item);
        }
    }
}
