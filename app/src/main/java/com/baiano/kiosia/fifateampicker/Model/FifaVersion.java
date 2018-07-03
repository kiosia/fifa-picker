package com.baiano.kiosia.fifateampicker.Model;

public class FifaVersion {
    private final int id;
    private String gameVersion;
    private String gameRelease;

    public FifaVersion(int id, String gameVersion, String gameRelease) {
        this.id = id;
        this.gameVersion = gameVersion;
        this.gameRelease = gameRelease;
    }

    public int getId() {
        return id;
    }

    public String getGameVersion() {
        return gameVersion;
    }

    public void setGameVersion(String gameVersion) {
        this.gameVersion = gameVersion;
    }

    public String getGameRelease() {
        return gameRelease;
    }

    public void setGameRelease(String gameRelease) {
        this.gameRelease = gameRelease;
    }
}
