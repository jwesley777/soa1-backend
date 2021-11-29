package repository;

import entity.Coordinates;
import exceptions.EntityIsNotValidException;
import org.hibernate.Session;
import utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CoordinatesRepository {
    private Session session;
    private EntityManager em;

    public CoordinatesRepository() {
        session = HibernateUtil.getSessionFactory().openSession();
        em = session.getEntityManagerFactory().createEntityManager();
    }

    public Coordinates findById(Integer id) {
        if (existsById(id)){
            Query query = em.createQuery("SELECT c FROM Coordinates c WHERE c.id = ?1", Coordinates.class);
            return (Coordinates) query.setParameter(1, id).getSingleResult();
        }else
            throw new EntityIsNotValidException("coordinates with id = " + id + " does not exist");
    }

    public boolean existsById(Integer id) {
        org.hibernate.query.Query query = session.createQuery("SELECT 1 FROM Coordinates l WHERE l.id = ?1");
        query.setParameter(1, id);
        return (query.uniqueResult() != null);
    }

    public List<Coordinates> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Coordinates> criteriaQuery = criteriaBuilder.createQuery(Coordinates.class);
        Root<Coordinates> from = criteriaQuery.from(Coordinates.class);
        CriteriaQuery<Coordinates> select = criteriaQuery.select(from);
        TypedQuery<Coordinates> typedQuery = em.createQuery(select);
        return typedQuery.getResultList();
    }

    public void create(Coordinates coordinates) {
        em.getTransaction().begin();
        em.persist(coordinates);
        em.getTransaction().commit();
        em.clear();
    }

    public void update(Coordinates coordinates) {
        em.getTransaction().begin();
        em.merge(coordinates);
        em.getTransaction().commit();
        em.clear();
    }


}
