package facades;

import dtos.UserDTO;
import entities.Role;
import entities.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import errorhandling.NotFoundException;
import errorhandling.UserAlreadyExistsException;
import security.errorhandling.AuthenticationException;

public class UserFacade {

    private static EntityManagerFactory emf;
    private static UserFacade instance;

    private UserFacade() {
    }

    /**
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    public boolean adminExists() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Number> tq = em.createQuery("select count(u) from User u", Number.class);
            Number n = tq.getSingleResult();
            if(n.intValue() > 0) {
                return true;
            }
        } finally {
            em.close();
        }

        return false;
    }

    public User getUser(String username) throws NotFoundException {
        EntityManager em = emf.createEntityManager();
        TypedQuery<User> query = em.createQuery("select u from User u where u.userName = :username", User.class);
        query.setParameter("username", username);

        try {
            User user = query.getSingleResult();
            return user;
        } catch (Exception ex) {
            throw new NotFoundException("User '" + username + "' not found.");
        }
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public void createAdmin(UserDTO userDTO) throws UserAlreadyExistsException {
        EntityManager em = emf.createEntityManager();

        // we only want ONE admin user in this application. Therefore, we throw here.
        if(adminExists()) {
            throw new UserAlreadyExistsException("User already exists");
        }

        // In case we want to expand the app with more users at a later point in time.
        User alreadyExists = em.find(User.class, userDTO.getUsername());
        if (alreadyExists != null) {
            throw new UserAlreadyExistsException("Username '" + userDTO.getUsername() + "' already exists.");
        }

        try {
            User user = new User(userDTO.getUsername(), userDTO.getPassword());
            Role userRole = em.find(Role.class, "admin");
            user.addRole(userRole);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

}
