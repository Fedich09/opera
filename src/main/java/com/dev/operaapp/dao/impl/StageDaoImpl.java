package com.dev.operaapp.dao.impl;

import com.dev.operaapp.dao.StageDao;
import com.dev.operaapp.exception.DataProcessingException;
import com.dev.operaapp.model.Stage;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class StageDaoImpl implements StageDao {
    private final SessionFactory sessionFactory;

    public StageDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Stage add(Stage stage) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.save(stage);
            transaction.commit();
            return stage;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can't add stage " + stage, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<Stage> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Stage> query = session.createQuery("from Stage", Stage.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new DataProcessingException("Can't get all stages ", e);
        }
    }

    @Override
    public Stage get(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Stage.class, id);
        } catch (Exception e) {
            throw new DataProcessingException("Can't get stage by id " + id, e);
        }
    }
}
