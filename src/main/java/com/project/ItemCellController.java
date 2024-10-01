package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemCellController {
    
    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descriptionLabel;

    // Método para cargar los datos en la celda
    public void setData(Item item) {
        nameLabel.setText(item.getNombre());
        descriptionLabel.setText(item.getDescripcion());
        // Cargar la imagen (puedes ajustar la ruta según sea necesario)
        imageView.setImage(new Image("file:" + item.getImagen()));
    }
}
