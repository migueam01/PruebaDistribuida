package com.distribuida.openapi;

import org.eclipse.microprofile.openapi.OASFactory;
import org.eclipse.microprofile.openapi.OASFilter;
import org.eclipse.microprofile.openapi.models.OpenAPI;
import org.eclipse.microprofile.openapi.models.info.Info;

import java.util.*;

public class BookFilter implements OASFilter {

    @Override
    public void filterOpenAPI(OpenAPI openAPI) {
        List<String> tags = new ArrayList<>();
        tags.add("Books");
        openAPI.setInfo(
                OASFactory.createObject(Info.class).title("Aplicación Libros").version("1.0.0")
                        .description(
                                "Realiza operaciones CRUD para Libros")
        );
        openAPI.setPaths(OASFactory.createPaths()
                .addPathItem("/books", OASFactory.createPathItem()
                        .GET(OASFactory.createOperation()
                                .description("Consulta Libro por id")
                                .tags(tags))));

    }

}
