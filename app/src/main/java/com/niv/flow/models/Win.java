package com.niv.flow.models;

public class Win {
    private String winId;
    private String userId;
    private String winTitle;
    private String winContent;
    private String createdAt;

    public Win(String winId, String userId, String winTitle, String winContent, String createdAt) {
        this.winId = winId;
        this.userId = userId;
        this.winTitle = winTitle;
        this.winContent = winContent;
        this.createdAt = createdAt;
    }

    public Win(String userId, String winTitle, String winContent, String createdAt) {
        this.userId = userId;
        this.winTitle = winTitle;
        this.winContent = winContent;
        this.createdAt = createdAt;
    }

    public String generateWinTitle(String winContent) {
        // Special method that returns the AI title generated for the Win's content
        return "AI Generated Title";
    }

    public String getWinId() {
        return winId;
    }

    public void setWinId(String winId) {
        this.winId = winId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWinTitle() {
        return winTitle;
    }

    public void setWinTitle(String winTitle) {
        this.winTitle = winTitle;
    }

    public String getWinContent() {
        return winContent;
    }

    public void setWinContent(String winContent) {
        this.winContent = winContent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
