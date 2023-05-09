package dtos;

import entities.User;

public class UserSafeDTO {
    private String username;

    public UserSafeDTO(String username) {
        this.username = username;
    }

    public UserSafeDTO(User user) {
        this.username = user.getUserName();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
