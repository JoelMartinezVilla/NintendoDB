package com.project;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonLoader {
    public static List<Item> cargarDatos() throws IOException {
        // Ruta al archivo JSON
        File file = new File("datos.json");

        // Instancia de ObjectMapper de Jackson para leer el archivo JSON
        ObjectMapper mapper = new ObjectMapper();

        // Leer la lista de objetos Item desde el JSON
        List<Item> items = mapper.readValue(file, new TypeReference<List<Item>>() {});
        return items;
    }
}
