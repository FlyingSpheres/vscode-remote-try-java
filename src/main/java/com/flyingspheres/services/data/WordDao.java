package com.flyingspheres.services.data;

import com.flyingspheres.services.data.model.Word;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class WordDao {

    public static void saveWord(EntityManager em, Word word){
        em.persist(word);
        em.flush();
    }

    public static Word retrieveWordByValue(EntityManager em, String word){
        Word retrievedWord = null;
        Query q = em.createNamedQuery("Word.FindWordByText");
        q.setParameter("word", word);
        try {
            retrievedWord = (Word)q.getSingleResult();
        } catch (Throwable t){
            System.out.println("Word: " + word + " Not found in database");
        }
        return retrievedWord;
    }

    public static Word retrieveWordById(EntityManager em, Integer id) {
        Word retrievedWord = null;
        Query q = em.createNamedQuery("Word.findWordById");
        q.setParameter("id", id);
        try {
            retrievedWord = (Word)q.getSingleResult();
        } catch (Throwable t){
            t.printStackTrace();
        }
        return retrievedWord;
    }
}
