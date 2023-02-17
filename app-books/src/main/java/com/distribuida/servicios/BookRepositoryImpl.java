package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
@Transactional
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext(unitName = "book")
    private EntityManager servicioEm;

    @Override
    public List<Book> findAll() {
        return servicioEm.createQuery("select b from Book b").getResultList();

    }

    @Override
    public Book findById(Integer id) {
        Book obj = servicioEm.find(Book.class, id);
        return obj;
    }

    @Override
    public void insert(Book book) {
        servicioEm.persist(book);
    }

    @Override
    public void update(Book book) {
        Book bookUpdate = servicioEm.find(Book.class, book.getId());
        if (null != bookUpdate){
            servicioEm.merge(book);
        }
    }

    @Override
    public void delete(Integer id) {
        Book b = servicioEm.find(Book.class, id);
        if (null != b){
            servicioEm.remove(b);
        }
    }
}
