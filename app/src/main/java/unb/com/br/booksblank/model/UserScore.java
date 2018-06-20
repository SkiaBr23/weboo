package unb.com.br.booksblank.model;

import java.util.HashMap;
import java.util.Map;

public class UserScore {

    private Integer coins;
    private Integer review;
    private Integer points;
    private Integer level;
    private Integer followers;
    private Integer activeTransactions;
    private Integer closedTransactions;
    private Integer booksGiven;
    private Integer booksTaken;
    private Integer reviewsGiven;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public UserScore() {
        this.coins = 0;
        this.review = 0;
        this.points = 0;
        this.level = 1;
        this.followers = 0;
        this.activeTransactions = 0;
        this.closedTransactions = 0;
        this.booksGiven = 0;
        this.booksTaken = 0;
        this.reviewsGiven = 0;
    }


    public UserScore(Integer coins, Integer review, Integer points, Integer level, Integer followers, Integer activeTransactions, Integer closedTransactions, Integer booksGiven, Integer booksTaken, Integer reviewsGiven) {
        super();
        this.coins = coins;
        this.review = review;
        this.points = points;
        this.level = level;
        this.followers = followers;
        this.activeTransactions = activeTransactions;
        this.closedTransactions = closedTransactions;
        this.booksGiven = booksGiven;
        this.booksTaken = booksTaken;
        this.reviewsGiven = reviewsGiven;
    }

    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
    }

    public Integer getReview() {
        return review;
    }

    public void setReview(Integer review) {
        this.review = review;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getActiveTransactions() {
        return activeTransactions;
    }

    public void setActiveTransactions(Integer activeTransactions) {
        this.activeTransactions = activeTransactions;
    }

    public Integer getClosedTransactions() {
        return closedTransactions;
    }

    public void setClosedTransactions(Integer closedTransactions) {
        this.closedTransactions = closedTransactions;
    }

    public Integer getBooksGiven() {
        return booksGiven;
    }

    public void setBooksGiven(Integer booksGiven) {
        this.booksGiven = booksGiven;
    }

    public Integer getBooksTaken() {
        return booksTaken;
    }

    public void setBooksTaken(Integer booksTaken) {
        this.booksTaken = booksTaken;
    }

    public Integer getReviewsGiven() {
        return reviewsGiven;
    }

    public void setReviewsGiven(Integer reviewsGiven) {
        this.reviewsGiven = reviewsGiven;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}