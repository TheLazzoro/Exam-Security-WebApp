package dtos;

import entities.ForumThread;

public class ForumThreadDTO {
    private UserSafeDTO author;
    private String title;
    private String content;

    public ForumThreadDTO(ForumThread thread) {
        this.author = new UserSafeDTO(thread.getAuthor());
    }

    public ForumThreadDTO(UserSafeDTO author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public UserSafeDTO getAuthor() {
        return author;
    }
}
