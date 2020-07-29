package com.example.churros2;

public class UserProfile {
    private String userEmail;
    private String userName;
    private String userAge;
    private String userGender;


    public UserProfile(){

    }

    public UserProfile(String userEmail, String userName, String userAge, String userGender) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userAge = userAge;
        this.userGender = userGender;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

}
