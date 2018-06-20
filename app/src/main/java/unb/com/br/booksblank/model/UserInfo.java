package unb.com.br.booksblank.model;

/**
 * Created by x05119695116 on 28/06/2017.
 */

public class UserInfo {

    private String userId;
    private String bookCondition;
    private String bookEdition;
    private String bookStatus;

    public UserInfo() {
    }

    public UserInfo(String userId, String bookCondition, String bookEdition, String bookStatus) {
        this.userId = userId;
        this.bookCondition = bookCondition;
        this.bookEdition = bookEdition;
        this.bookStatus = bookStatus;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public String getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(String bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }
}