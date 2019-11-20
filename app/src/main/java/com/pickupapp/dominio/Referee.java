package com.pickupapp.dominio;

import java.util.List;

public class Referee {
    private float averageRating;
    private int matchesCount;
    private int id;
    private User user;

    public List<RefereeRating> getRefereeRatings() {
        return refereeRatings;
    }

    public void setRefereeRatings(List<RefereeRating> refereeRatings) {
        this.refereeRatings = refereeRatings;
    }

    private List<RefereeRating> refereeRatings;

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getMatchesCount() {
        return matchesCount;
    }

    public void setMatchesCount(int matchesCount) {
        this.matchesCount = matchesCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
