package com.flyingspheres.services.data;

import com.flyingspheres.services.data.model.Chapter;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.util.List;

public class ChapterDao {
    public static void saveChapter(EntityManager em, Chapter chapter) {
        em.persist(chapter);
        em.flush();
    }

    public static List<Chapter> retrieveChaptersByBook(EntityManager em, int bookId) {
        List<Chapter> chapters = null;
        try {
             chapters = em.createNamedQuery("Chapter.findChaptersByBook").setParameter("bookId", bookId).getResultList();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return chapters;
    }

    public static Chapter retrieveChapterById(EntityManager em, int chapterId) {
        Chapter chapter = null;
        Query q = em.createNamedQuery("Chapter.findChapterById");
        q.setParameter("id", chapterId);
        try {
            chapter = (Chapter) q.getSingleResult();
        } catch (NoResultException ex){
            ex.printStackTrace();
        }
        return chapter;
    }
}