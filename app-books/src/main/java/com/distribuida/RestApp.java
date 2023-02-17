package com.distribuida;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@ApplicationPath("/")
@OpenAPIDefinition(info = @Info(title="APLICACIÓN LIBROS", version = "1.0.0",
                description = "Contiene operaciones CRUD de Libros"))
public class RestApp extends Application {
}
