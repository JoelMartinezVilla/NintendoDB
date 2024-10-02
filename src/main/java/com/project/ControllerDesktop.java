package com.project;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import java.io.InputStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ControllerDesktop {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ListView<HBox> listView;

    @FXML
    private ImageView imageView; 

    @FXML
    private Label titleLabel; 

    @FXML
    private Label descriptionLabel; 

    @FXML
    private Label additionalInfoLabel; 


    public static class Item {
        String name;
        String imagePath;
        String description;
        String additionalInfo;

        public Item(String name, String imagePath, String description, String additionalInfo) {
            this.name = name;
            this.imagePath = imagePath;
            this.description = description;
            this.additionalInfo = additionalInfo;
        }
    }


    private ObservableList<Item> itemList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        choiceBox.getItems().addAll("Consolas", "Juegos", "Personajes");
        choiceBox.setValue("Consolas");

        loadJSONData("Consolas");

        choiceBox.setOnAction(event -> handleChoiceBoxAction());


        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                int selectedIndex = listView.getSelectionModel().getSelectedIndex();
       
                Item selectedItem = itemList.get(selectedIndex);
                
                try {
                    updateRightVBox(selectedItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    
    @FXML
    private void handleChoiceBoxAction() {
        String selected = choiceBox.getValue();
        loadJSONData(selected);
    }

    private void loadJSONData(String category) {
        String jsonFilePath = getJSONFilePathForCategory(category);
        if (jsonFilePath == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath))) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            
            JSONArray jsonArray = new JSONArray(jsonContent.toString());

            
            ObservableList<HBox> items = FXCollections.observableArrayList();
            itemList.clear(); 

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String name = jsonObject.getString("nom");
                String imagePath = "assets/images/" + jsonObject.getString("imatge");
                String description = "";

                String additionalInfo = "";
                switch (category) {
                    case "Consolas":
                        description = "Lanzamiento: " + jsonObject.getString("data");
                        additionalInfo = "Procesador: " + jsonObject.getString("procesador") + "\n" +
                                "Color: " + jsonObject.getString("color") + "\n" +
                                "Unidades vendidas: " + jsonObject.getInt("venudes");
                        break;
                    case "Juegos":
                        description = jsonObject.getString("descripcio");
                        additionalInfo = "Año: " + jsonObject.getInt("any") + "\n" +
                                "Tipo: " + jsonObject.getString("tipus");
                        break;
                    case "Personajes":
                        additionalInfo = "Color: " + jsonObject.getString("color") + "\n" +
                                "Juego: " + jsonObject.getString("nom_del_videojoc");
                        break;
                }

                Item item = new Item(name, imagePath, description, additionalInfo);
                itemList.add(item);

                HBox hBox = new HBox(10); 


                ImageView imageView = new ImageView();
                try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(imagePath)) {
                    if (imageStream != null) {
                        Image image = new Image(imageStream);
                        imageView.setImage(image);
                    } else {
                        System.out.println("Error: No se pudo encontrar la imagen en la ruta: " + imagePath);
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error al cargar la imagen: " + imagePath);
                    e.printStackTrace();
                }

                imageView.setFitHeight(50);
                imageView.setFitWidth(50);

                Label nameLabel = new Label(name);

                hBox.getChildren().addAll(imageView, nameLabel);
                items.add(hBox);
            }

            listView.setItems(items);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Actualiza el VBox de la derecha con la información del item seleccionado
private void updateRightVBox(Item item) throws IOException {
    // Cargar la imagen
    try (InputStream imageStream = getClass().getClassLoader().getResourceAsStream(item.imagePath)) {
        if (imageStream != null) {
            Image image = new Image(imageStream);
            imageView.setImage(image);
        } else {
            System.out.println("Error: No se pudo encontrar la imagen en la ruta: " + item.imagePath);
        }
    } catch (IllegalArgumentException e) {
        System.out.println("Error al cargar la imagen: " + item.imagePath);
        e.printStackTrace();
    }

    // Actualizar el título
    titleLabel.setText(item.name);

    // Crear la descripción basada en el tipo de JSON que tenemos
    StringBuilder description = new StringBuilder();

    // Cargar el JSON para obtener información adicional
    String jsonFilePath = getJSONFilePathForCategory(choiceBox.getValue());
    try (BufferedReader reader = new BufferedReader(new FileReader(jsonFilePath))) {
        StringBuilder jsonContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonContent.append(line);
        }


        JSONArray jsonArray = new JSONArray(jsonContent.toString());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            if (jsonObject.getString("nom").equals(item.name)) {

                if (jsonObject.has("procesador")) {

                    description.append("Procesador: ").append(jsonObject.getString("procesador"))
                               .append("\nColor: ").append(jsonObject.getString("color"))
                               .append("\nVendidos: ").append(jsonObject.getInt("venudes"))
                               .append("\nFecha de lanzamiento: ").append(jsonObject.getString("data"));
                } else if (jsonObject.has("any")) {
                    description.append("Año: ").append(jsonObject.getInt("any"))
                               .append("\nTipo: ").append(jsonObject.getString("tipus"))
                               .append("\nDescripción: ").append(jsonObject.getString("descripcio"));
                } else if (jsonObject.has("nom_del_videojoc")) {
                    description.append("Color: ").append(jsonObject.getString("color"))
                               .append("\nJuego: ").append(jsonObject.getString("nom_del_videojoc"));
                }
                break; 
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }


    descriptionLabel.setText(description.toString());
}


    private String getJSONFilePathForCategory(String category) {
        switch (category) {
            case "Consolas":
                return "src/main/resources/assets/data/consoles.json";
            case "Juegos":
                return "src/main/resources/assets/data/jocs.json";
            case "Personajes":
                return "src/main/resources/assets/data/personatges.json";
            default:
                return null;
        }
    }
}
