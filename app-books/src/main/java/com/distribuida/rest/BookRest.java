package com.distribuida.rest;

import com.distribuida.clientes.authors.AuthorRestProxy;
import com.distribuida.clientes.authors.AuthorsCliente;
import com.distribuida.db.Book;
import com.distribuida.dtos.BookDto;
import com.distribuida.servicios.BookRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookRest {

    @Inject BookRepository bookService;

    @RestClient
    @Inject AuthorRestProxy proxyAuthor;

    /**
     * GET          /books/{id}     buscar un libro por ID
     * GET          /books          listar todos
     * POST         /books          insertar
     * PUT/PATCH    /books/{id}     actualizar
     * DELETE       /books/{id}     eliminar
     */

    @GET
    @Path("/{id}")
    @Operation(summary = "Devuelve información libro por su id")
    @APIResponse(description = "OK",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Book.class)),
            responseCode = "200")
    @APIResponse(responseCode = "404", description = "NOT FOUND", content = @Content(
                    mediaType = "application/json", schema = @Schema(implementation = String.class,
                    example = "Book[id=%d] not found.")))
    public Response findById(@Parameter(description = "Id Libro buscado", required = true) @PathParam("id") Integer id) {
        Book ret = bookService.findById(id);

        if( ret != null ) {
            return Response.ok(ret).build();
        }
        else {
            String msg = String.format( "Book[id=%d] not found.", id );

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(msg)
                    .build();
        }
    }

    @GET
    @Operation(summary = "Devuelve lista de libros")
    @APIResponse(description = "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Book.class, type = SchemaType.ARRAY)),
            responseCode = "200")
    @APIResponse(responseCode = "404", description = "NOT FOUND")
    public List<Book> findAll() {
        System.out.println("Buscando todos");
        return bookService.findAll();
    }

    @GET
    @Path("/all")
    @Operation(summary = "Obtiene Libros con mas detalles")
    @APIResponse(description = "OK", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookDto.class, type = SchemaType.ARRAY)),
                responseCode = "200")
    @APIResponse(responseCode = "404", description = "NOT FOUND")
    public List<BookDto> findAllCompleto() throws Exception {
        var books = bookService.findAll();
        List<BookDto> ret = books.stream()
                .map(s -> {
                    System.out.println("*********buscando " + s.getId() );
                    AuthorsCliente author = proxyAuthor.findById(s.getId().longValue());
                    return new BookDto(
                            s.getId(),
                            s.getIsbn(),
                            s.getTitle(),
                            s.getAuthor(),
                            s.getPrice(),
                            String.format("%s, %s", author.getLastName(), author.getFirtName())
                    );
                })
                .collect(Collectors.toList());
        return ret;
    }

    @POST
    @Operation(summary = "Creación", description = "Creación Libro")
    @APIResponse(responseCode = "204", description = "NO CONTENT")
    public void insert(@RequestBody(description = "Información libro a crear", required = true,
                                    content = @Content(schema = @Schema(implementation = Book.class))) Book book) {
        bookService.insert(book);
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Actualización", description = "Actualización información por id de un Libro")
    @APIResponse(responseCode = "204", description = "NO CONTENT")
    public void update(@RequestBody(description = "Información libro actualizar", required = true,
                                    content = @Content(schema = @Schema(implementation = Book.class))) Book book,
                       @Parameter(description = "Id Libro a actualizar", required = true) @PathParam("id") Integer id) {
        book.setId(id);
        bookService.update(book);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminación", description = "Elimina libro por id")
    @APIResponse(responseCode = "204", description = "NO CONTENT")
    public void delete(@Parameter(description = "Id libro a eliminar", required = true) @PathParam("id") Integer id ) {
        bookService.delete(id);
    }
}
