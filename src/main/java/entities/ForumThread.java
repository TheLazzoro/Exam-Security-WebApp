package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ForumThread {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    private User author;
    private String title;
    private String content;
    private Date creationDate;

    public ForumThread() {}

    public ForumThread(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.creationDate = new Date();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
