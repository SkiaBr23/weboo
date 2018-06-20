package unb.com.br.booksblank.model;

import com.google.api.services.books.model.Volume;

import java.util.ArrayList;
import java.util.List;

public class BookInfo {

    private String bookId;
    private List<UserInfo> users;
    private Integer bookCount;
    private Integer wishCount;
    private String title;
    private String author;
    private String year;
    private String category;
    private String bookCoverUrl;


    public BookInfo() {
        this.users = new ArrayList<>();
        this.bookCount = 1;
        this.wishCount = 0;
    }

    public BookInfo(String bookId, UserInfo userInfo){
        this.bookId = bookId;
        this.users = new ArrayList<>();
        this.users.add(userInfo);
        this.bookCount = 0;
        this.wishCount = 0;
    }

    public BookInfo(String bookId) {
        this.bookId = bookId;
        this.users = new ArrayList<>();
        this.bookCount = 0;
        this.wishCount = 0;
    }

    public BookInfo(String bookId, List<UserInfo> users, Integer bookCount, Integer wishCount) {
        this.bookId = bookId;
        this.users = users;
        this.bookCount = bookCount;
        this.wishCount = wishCount;
    }

    public BookInfo(String bookId, String title, String author, String year, String category, String bookCoverUrl) {
        this.bookId = bookId;
        this.users = new ArrayList<>();
        this.bookCount = 0;
        this.wishCount = 0;
        this.title = title;
        this.author = author;
        this.year = year;
        this.category = category;
        this.bookCoverUrl = bookCoverUrl;
    }

    public String getBookCoverUrl() {
        return bookCoverUrl;
    }

    public void setBookCoverUrl(String bookCoverUrl) {
        this.bookCoverUrl = bookCoverUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public List<UserInfo> getUsers() {
        return users;
    }

    public boolean hasUser(String userId){
        for(UserInfo userInfo : this.users){
            if(userInfo.getUserId().equals(userId))
                return true;
        }
        return false;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }

    public void addBook(String userId, String condition, String edition){
        this.users.add(new UserInfo(userId,condition,edition,"AVAILABLE"));
        this.bookCount++;
    }

    public void initializeUsers(){
        this.users = new ArrayList<>();
    }

    public void removeBook(String userId){
        for (UserInfo user : this.users){
            if(user.getUserId().equals(userId)){
                this.users.remove(user);
            }
        }
        this.bookCount--;
    }
    public Integer getBookCount() {
        return bookCount;
    }

    public void setBookCount(Integer bookCount) {
        this.bookCount = bookCount;
    }

    public Integer getWishCount() {
        return wishCount;
    }

    public void setWishCount(Integer wishCount) {
        this.wishCount = wishCount;
    }
}