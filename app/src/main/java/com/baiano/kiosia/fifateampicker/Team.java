package com.baiano.kiosia.fifateampicker;

class Team {
    private String name;
    private String attack;
    private String midfield;
    private String defense;
    private String overall;
    private String rating;
    private String league;
    private String country;
    private String crestId;

    public String getName() {
        return name;
    }

    public String getAttack() {
        return attack;
    }

    public String getMidfield() {
        return midfield;
    }

    public String getDefense() {
        return defense;
    }

    public String getOverall() {
        return overall;
    }

    public String getRating() {
        return rating;
    }

    public String getLeague() {
        return league;
    }

    public String getCountry() {
        return country;
    }

    public String getCrestId() {
        return crestId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public void setMidfield(String midfield) {
        this.midfield = midfield;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCrestId(String crestId) {
        this.crestId = crestId;
    }

    @Override
    public String toString() {
        return "{teamName:"+getName()+", teamRating:"+getRating()+"}";
    }
}
