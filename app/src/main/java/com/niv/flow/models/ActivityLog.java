package com.niv.flow.models;

public class ActivityLog {
    private String logId;
    private String userId;
    private String activityType;
    private int dayOfWeek;
    private int hourOfDay;
    private String createdAt;

    public ActivityLog(String logId, String userId, String activityType, int dayOfWeek, int hourOfDay, String createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.activityType = activityType;
        this.dayOfWeek = dayOfWeek;
        this.hourOfDay = hourOfDay;
        this.createdAt = createdAt;
    }

    public ActivityLog(String userId, String activityType, int dayOfWeek, int hourOfDay, String createdAt) {
        this.userId = userId;
        this.activityType = activityType;
        this.dayOfWeek = dayOfWeek;
        this.hourOfDay = hourOfDay;
        this.createdAt = createdAt;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
