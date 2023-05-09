package facades;

import dtos.ForumThreadDTO;
import dtos.ForumThreadsDTO;
import entities.ForumThread;
import entities.User;
import errorhandling.API_Exception;
import errorhandling.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class Forum_Thread_Facade {

    private static EntityManagerFactory emf;
    private static Forum_Thread_Facade instance;

    private Forum_Thread_Facade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static Forum_Thread_Facade getForumThreadFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new Forum_Thread_Facade();
        }
        return instance;
    }

    public ForumThreadsDTO getAll()  {
        EntityManager em = emf.createEntityManager();
        TypedQuery<ForumThread> query = em.createQuery("SELECT t FROM ForumThread t", ForumThread.class);
        List<ForumThread> threads = query.getResultList();
        List<ForumThreadDTO> dtos = new ArrayList<>();
        threads.forEach(t -> {
            ForumThreadDTO dto = new ForumThreadDTO(t);
            dtos.add(dto);
        });

        return new ForumThreadsDTO(dtos);
    }

    public ForumThreadDTO getById(long id) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        ForumThread thread = em.find(ForumThread.class, id);
        if(thread == null) {
            throw new NotFoundException("Forum thread not found.");
        }

        return new ForumThreadDTO(thread);
    }

    public ForumThreadDTO create(ForumThreadDTO dto) throws NotFoundException, API_Exception {
        EntityManager em = emf.createEntityManager();

        String username = dto.getAuthor().getUsername();
        String title = dto.getTitle();
        String content = dto.getContent();

        UserFacade userFacade = UserFacade.getUserFacade(emf);
        User user = userFacade.getUser(username);
        if(user == null) {
            throw new NotFoundException("User not found.");
        }
        if(title == null || title.length() == 0) {
            throw new API_Exception("No title.");
        }
        if(content == null || content.length() == 0) {
            throw new API_Exception("No content.");
        }

        ForumThread thread = new ForumThread(user, title, content);
        try {
            em.getTransaction().begin();
            em.persist(thread);
            em.getTransaction().commit();
            ForumThread created = em.find(ForumThread.class, thread.getId());
            return new ForumThreadDTO(created);
        } finally {
            em.close();
        }
    }
}
