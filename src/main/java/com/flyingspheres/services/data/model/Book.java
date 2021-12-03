package com.flyingspheres.services.data.model;

import javax.persistence.*;

@Entity
@Table(name="books")
@NamedQueries({
        @NamedQuery(
                name = "Book.findBookByName",
                query = "select b from Book b where b.name = :name"
        ),
        @NamedQuery(
                name = "Book.findBookById",
                query = "select b from Book b where b.name = :id"
        ),
        @NamedQuery(
                name = "Book.findAllBooks",
                query = "select b from Book b"
        )
})

public class Book {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;

    private String author;

    private String language;

    private String image;

//    @OneToMany(targetEntity=Chapter.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name="id") //this is the id from this class
//    private List<Chapter> chapters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public List<Chapter> getChapters() {
//        return chapters;
//    }
//
//    public void setChapters(List<Chapter> chapters) {
//        this.chapters = chapters;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}