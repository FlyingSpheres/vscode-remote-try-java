package com.flyingspheres.services.data;

import com.flyingspheres.services.data.model.ChapterWords;

import javax.persistence.EntityManager;

public class ChapterWordsDao {
    public static void saveXref(EntityManager em, ChapterWords xref) {
        em.persist(xref);
        em.flush();
    }
}