package dev.ifrs.data;

import java.util.ArrayList;
import java.util.List;

import dev.ifrs.model.Book;
import jakarta.inject.Singleton;

@Singleton
public class DataBase {
    private List<Book> books;

    public DataBase() {
       
        this.books = new ArrayList<>();
        
        Book book1 = new Book();
        book1.setTitle("Effective Java");
        book1.setAuthor("Joshua Bloch");
        book1.setIsbn("978-0134686097");
        books.add(book1);

        Book book2 = new Book();
        book2.setTitle("Clean Code");
        book2.setAuthor("Robert C. Martin");
        book2.setIsbn("978-0136083238");
        books.add(book2);

        Book book3 = new Book();
        book3.setTitle("Design Patterns");
        book3.setAuthor("Erich Gamma");
        book3.setIsbn("978-0201633610");
        books.add(book3);

    }

    public List<Book> getBooks() {
        return books;
    }

}
