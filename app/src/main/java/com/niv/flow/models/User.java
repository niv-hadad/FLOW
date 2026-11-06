package com.niv.flow.models;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String createdAt;
    private int flowState;
    private String lastWinDate;
    private String weeklyReflection;
    private String lastReflectionDate;

    public User(String userId, String firstName, String lastName, String email, String password, String createdAt, int flowState, String lastWinDate, String weeklyReflection, String lastReflectionDate) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.flowState = flowState;
        this.lastWinDate = lastWinDate;
        this.weeklyReflection = weeklyReflection;
        this.lastReflectionDate = lastReflectionDate;
    }

    public User(String firstName, String lastName, String email, String password, String createdAt) {
        this.userId = "Temp";
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
        this.flowState = 0;
        this.lastWinDate = null;
        this.weeklyReflection = null;
        this.lastReflectionDate =  null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFlowState() {
        return flowState;
    }

    public void setFlowState(int flowState) {
        this.flowState = flowState;
    }

    public String getLastWinDate() {
        return lastWinDate;
    }

    public void setLastWinDate(String lastWinDate) {
        this.lastWinDate = lastWinDate;
    }

    public String getWeeklyReflection() {
        return weeklyReflection;
    }

    public void setWeeklyReflection(String weeklyReflection) {
        this.weeklyReflection = weeklyReflection;
    }

    public String getLastReflectionDate() {
        return lastReflectionDate;
    }

    public void setLastReflectionDate(String lastReflectionDate) {
        this.lastReflectionDate = lastReflectionDate;
    }
}
