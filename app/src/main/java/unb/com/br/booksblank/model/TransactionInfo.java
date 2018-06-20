package unb.com.br.booksblank.model;

public class TransactionInfo {

    private String user1Id;
    private String user2Id;
    private String transactionId;
    private String status;
    private String bookCondition;
    private String bookEdition;
    private String bookTitle;
    private String bookCoverUrl;

    private String bookId;

    /*
    * STATUS:
    * ----> REQUESTED
    * ACCEPTED
    * DECLINED
    * CANCELED
    * ----> COMPLETED
    *
    * */

    public TransactionInfo() {
    }

    public TransactionInfo(String transactionId,String user1Id, String user2Id, String bookId, String bookTitle, String bookCoverUrl,String bookCondition, String bookEdition) {
        this.transactionId = transactionId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.status = "REQUESTED";
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookCoverUrl = bookCoverUrl;
        this.bookCondition = bookCondition;
        this.bookEdition = bookEdition;
    }

    public TransactionInfo(String transactionId,String user1Id, String user2Id, String status, String bookId) {
        this.transactionId = transactionId;
        this.user1Id = user1Id;
        this.user2Id = user2Id;
        this.status = status;
        this.bookId = bookId;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookCoverUrl() {
        return bookCoverUrl;
    }

    public void setBookCoverUrl(String bookCoverUrl) {
        this.bookCoverUrl = bookCoverUrl;
    }

    public String getUser1Id() {
        return user1Id;
    }

    public void setUser1Id(String user1Id) {
        this.user1Id = user1Id;
    }

    public String getUser2Id() {
        return user2Id;
    }

    public void setUser2Id(String user2Id) {
        this.user2Id = user2Id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

}
