package com.niv.flow.models;

// DESIGN PROTOTYPE:
// Wins from database with RecyclerView integration not yet implemented.
public class Win {
    String winTitle;
    String winDate;

    public Win(String winTitle, String winDate) {
        this.winTitle = winTitle;
        this.winDate = winDate;
    }

    public Win() {
    }

    public String getWinDate() {
        return winDate;
    }

    public void setWinDate(String winDate) {
        this.winDate = winDate;
    }

    public String getWinTitle() {
        return winTitle;
    }

    public void setWinTitle(String winTitle) {
        this.winTitle = winTitle;
    }
}
