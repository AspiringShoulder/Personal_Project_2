package com.example.seller.dao;

import org.hibernate.Session;
import com.example.seller.domain.User;

import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class UserDAO extends AbstractHibernateDao<User>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

    public UserDAO()
    {
        LOGGER.info("UserDAO created");
        setClazz(User.class);
    }

    public List<User> getAllUsers()
    {
        return this.getAll();
    }

    public User getUserById(int userId)
    {
        return this.findById(userId);
    }

    public void addUser(User user)
    {
        this.add(user);
    }

    public Optional<User> getUserByUsername(String username)
    {
        Session session = getCurrentSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);
        List<User> list = query.getResultList();

        if (list.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.ofNullable(list.get(0));
    }

    public Optional<User> getUserByPassword(String password)
    {
        Session session = getCurrentSession();
        Query<User> query = session.createQuery("FROM User u WHERE u.password = :password", User.class);
        query.setParameter("password", password);
        List<User> list = query.getResultList();

        if (list.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.ofNullable(list.get(0));
    }
}
