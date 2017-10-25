package com.onecosys.fahri_bla;

/**
 * Created by User on 26.10.2017.
 */

public class Book {

    private String bookImage;
    private String bookTitle;
    private String publisher;
    private String author;
    private double rating;
    private String category;

    public Book(String bookImage, String bookTitle, String publisher, String author, double rating, String category) {
        this.bookImage = bookImage;
        this.bookTitle = bookTitle;
        this.publisher = publisher;
        this.author = author;
        this.rating = rating;
        this.category = category;
    }

    public String getBookImage() {
        return bookImage;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getAuthor() {
        return author;
    }

    public double getRating() {
        return rating;
    }

    public String getCategory() {
        return category;
    }
}//end of class Book
