package com.baiano.kiosia.fifateampicker.Model;

public class Team {
    private final int id;
    private String name;
    private Integer attack;
    private Integer midfield;
    private Integer defense;
    private Integer overall;
    private String rating;
    private String type;
    private String league;
    private String country;
    private Integer version;

    public Team(int id, String name, Integer attack, Integer midfield, Integer defense, Integer overall, String rating, String type, String league, String country, Integer version) {
        this.id = id;
        this.name = name;
        this.attack = attack;
        this.midfield = midfield;
        this.defense = defense;
        this.overall = overall;
        this.rating = rating;
        this.type = type;
        this.league = league;
        this.country = country;
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getMidfield() {
        return midfield;
    }

    public void setMidfield(Integer midfield) {
        this.midfield = midfield;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getOverall() {
        return overall;
    }

    public void setOverall(Integer overall) {
        this.overall = overall;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getRatingIndex() {
        switch (this.rating) {
            case "5":
                return 0;
            case "10":
                return 1;
            case "15":
                return 2;
            case "20":
                return 3;
            case "25":
                return 4;
            case "30":
                return 5;
            case "35":
                return 6;
            case "40":
                return 7;
            case "45":
                return 8;
            default:
                return 9;
        }
    }
}
