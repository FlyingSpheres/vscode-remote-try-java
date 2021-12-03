package com.flyingspheres.services.data.model;

import javax.persistence.*;

@Entity
@Table(name="chapter")
@NamedQueries({
        @NamedQuery(
                name = "Chapter.findChaptersByBook",
                query = "select ch from Chapter ch where ch.book.id = :bookId"
        ),
        @NamedQuery(
                name = "Chapter.findChapterById",
                query = "select ch from Chapter ch where ch.id = :id"
        )
})
public class Chapter {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="bookId", nullable=false)
    private Book book;
    private String chapterName;
    private String chapterNumber;

    @Basic(fetch = FetchType.LAZY)
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}