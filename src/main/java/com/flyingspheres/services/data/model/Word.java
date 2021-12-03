package com.flyingspheres.services.data.model;

import javax.persistence.*;
@Entity
@Table(name="words")
@NamedQueries({
        @NamedQuery(
                name = "Word.findWordById",
                query = "select w from Word w where w.id = :id"
        ),
        @NamedQuery(
                name = "Word.FindWordByText",
                query = "select w from Word w where w.word = :word"
        )
})
public class Word {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;
    private String word;
    private String translation;
    private int knownStatusId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getKnownStatusId() {
        return knownStatusId;
    }

    public void setKnownStatusId(int knownStatusId) {
        this.knownStatusId = knownStatusId;
    }
}