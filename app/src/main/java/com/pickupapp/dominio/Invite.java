package com.pickupapp.dominio;

public class Invite {
    private String booking_id;
    private String spot_id;
    private String status;
    private String invite_id;
    private String player_id;

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInvite_id() {
        return invite_id;
    }

    public void setInvite_id(String invite_id) {
        this.invite_id = invite_id;
    }

    public String getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public String getSpot_id() {
        return spot_id;
    }

    public void setSpot_id(String spot_id) {
        this.spot_id = spot_id;
    }
}
