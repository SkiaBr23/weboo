package unb.com.br.booksblank.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {

    private String userId;
    private String fullName;
    private String name;
    private String email;
    private String password;
    private Boolean linkFacebook;
    private Boolean linkGoogle;
    private String profilePictureUrl;
    private String phoneNumber;
    private String location;
    private String currentLocation;
    private UserScore score;
    private List<TransactionInfo> transactions;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String fullName, String email, String profilePictureUrl) {
        this.transactions = new ArrayList<>();
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.score = new UserScore();
    }

    public User(String userId, String fullName, String name, String email, String password, Boolean linkFacebook, Boolean linkGoogle, String profilePictureUrl, String phoneNumber, String location, String currentLocation, UserScore score) {
        this.transactions = new ArrayList<>();
        this.userId = userId;
        this.fullName = fullName;
        this.name = name;
        this.email = email;
        this.password = password;
        this.linkFacebook = linkFacebook;
        this.linkGoogle = linkGoogle;
        this.profilePictureUrl = profilePictureUrl;
        this.phoneNumber = phoneNumber;
        this.location = location;
        this.currentLocation = currentLocation;
        this.score = score;
    }

    public List<TransactionInfo> getTransactions() {
        return transactions;
    }

    public void addTransaction(TransactionInfo transactionInfo){
        if(this.transactions == null){
            this.transactions = new ArrayList<>();
        }
        this.transactions.add(transactionInfo);
    }

    public void removeTransaction(TransactionInfo transactionInfo){
        if(this.transactions != null && this.transactions.contains(transactionInfo)){
            this.transactions.remove(transactionInfo);
        }
    }

    public void removeTransactionById(String transactionId){
        TransactionInfo toRemove = new TransactionInfo();
        for (TransactionInfo t : this.transactions){
            if (t.getTransactionId().equals(transactionId)){
                toRemove = t;
            }
        }
        this.transactions.remove(toRemove);
    }
    public boolean haveRequestTransaction(String userId, String bookId){
        if(this.transactions != null) {
            for (TransactionInfo t : this.transactions) {
                if (t.getUser2Id().equals(userId) && t.getBookId().equals(bookId)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean haveDeliverTransaction(String userId, String bookId){
        if(this.transactions != null) {
            for (TransactionInfo t : this.transactions) {
                if (t.getUser1Id().equals(userId) && t.getBookId().equals(bookId)) {
                    return true;
                }
            }
        }
        return false;

    }

    public void setTransactions(List<TransactionInfo> transactions) {
        this.transactions = transactions;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getLinkFacebook() {
        return linkFacebook;
    }

    public void setLinkFacebook(Boolean linkFacebook) {
        this.linkFacebook = linkFacebook;
    }

    public Boolean getLinkGoogle() {
        return linkGoogle;
    }

    public void setLinkGoogle(Boolean linkGoogle) {
        this.linkGoogle = linkGoogle;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public UserScore getScore() {
        return score;
    }

    public void setScore(UserScore score) {
        this.score = score;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}