 package com.example.sendit_chatapp.packages.Models;

public class Users {

String profilePic, username, email , password, userId,lastMessage, status;

    public Users(String username, String email, String password, String userId, String lastMessage) {
        this.profilePic = profilePic;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public Users(String username, String email, String password ) {

        this.username = username;
        this.email = email;
        this.password = password;

    }

    public Users() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
