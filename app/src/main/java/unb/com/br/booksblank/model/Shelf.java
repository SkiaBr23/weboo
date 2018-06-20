package unb.com.br.booksblank.model;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.frequency;

public class Shelf {
    private String userId;
    private String shelfId;
    private Integer bookCount;
    private List<String> books;

    public Shelf() {
    }

    public Shelf(String userId, String shelfId) {
        this.userId = userId;
        this.shelfId = shelfId;
        this.bookCount = 0;
        this.books = new ArrayList<>();
    }

    public Shelf(String userId, String shelfId, Integer bookCount, List<String> books) {
        this.userId = userId;
        this.shelfId = shelfId;
        this.bookCount = bookCount;
        this.books = books;
    }

    public void removeBook(String bookId){
        this.books.remove(bookId);
        this.bookCount--;
    }

    public Integer getBookQte(String id){
        return frequency(this.books,id);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShelfId() {
        return shelfId;
    }

    public void setShelfId(String shelfId) {
        this.shelfId = shelfId;
    }

    public Integer getBookCount() {
        return bookCount;
    }

    public void initializeBooks(){
        this.books = new ArrayList<>();
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public List<String> getBooks() {
        return books;
    }

    public void setBooks(List<String> books) {
        this.books = books;
    }
}

