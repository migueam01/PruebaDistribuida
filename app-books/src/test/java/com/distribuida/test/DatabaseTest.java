package com.distribuida.test;

import com.distribuida.db.Book;
import com.distribuida.servicios.BookRepository;
import com.distribuida.servicios.BookRepositoryImpl;
import io.helidon.microprofile.config.ConfigCdiExtension;
import io.helidon.microprofile.tests.junit5.*;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
@DisableDiscovery
@AddExtension(ConfigCdiExtension.class)
@AddBeans({
        @AddBean(BookRepositoryImpl.class)
})
//
//@AddConfigs({
//        @AddConfig(key = "db.user", value = "postgres"),
//})
public class DatabaseTest {

    @Inject BookRepository repo;

    @Test
    void testPrueba() {

        Book book = repo.findById(10);

        assertEquals(10, book.getId(),"Book found");

    }

    @Test
    void testPrueba2() {

        Book book = repo.findById(100);

        assertEquals(100, book.getId(),"Book found");

    }
}
