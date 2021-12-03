package com.flyingspheres.services.data;

import com.flyingspheres.services.data.model.Book;
import com.flyingspheres.services.data.model.Chapter;

import javax.faces.bean.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class BookDao {
    private static Logger log = Logger.getLogger(BookDao.class.getName());

    @PersistenceUnit(unitName="primary")
    private EntityManagerFactory emf;

    public BookDao(){

    }

    public static void saveBook(EntityManager em, Book book){
        log.log(Level.INFO, "Saving Event");

        try {
            em.persist(book);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static Book retrieveBookByName(EntityManager em, String bookName) {
        Book book = null;

        List<Book> tagList = em.createNamedQuery("Book.findBookByName").setParameter("name", bookName).getResultList();
        if (tagList.size() > 0){
            book = tagList.get(0);
        }

        return book;
    }

    public static List<Book> retrieveAllBooks(EntityManager em) {

        List<Book> bookList = em.createNamedQuery("Book.findAllBooks").getResultList();

        return bookList;
    }

    public static Book retrieveBookById(EntityManager em, int bookId) {
        Book book = null;

        /*
        Example to get a single result:
        public Order findOrderSubmittedAt(Date date) throws NonUniqueResultException {
            Query q = entityManager.createQuery(
                "SELECT e FROM " + entityClass.getName() + " e WHERE date = :date_at");
            q.setParameter("date_at", date);
            try {
                return (Order) q.getSingleResult();
            } catch (NoResultException exc) {
                return null;
            }
        }
         */
        List<Book> bookList = em.createNamedQuery("Book.findBookById").setParameter("id", bookId).getResultList();
        if (bookList.size() > 0){
            book = bookList.get(0);
        }

        return book;
    }


//    public static void addChapterToBook(EntityManager em, Book book, Chapter savedChapter) {
//        book.getChapters().add(savedChapter);
//        em.persist(book);
//    }
}