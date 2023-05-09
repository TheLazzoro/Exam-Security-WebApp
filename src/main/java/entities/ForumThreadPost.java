package entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class ForumThreadPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private ForumThread thread;
    @ManyToOne
    private User user;
    private String postText;
    private Date creationDate;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public ForumThread getThread() {
        return thread;
    }

    public void setThread(ForumThread thread) {
        this.thread = thread;
    }
}
