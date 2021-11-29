package repository;

import entity.Location;
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

public class LocationRepository {
    private Session session;
    private EntityManager em;

    public LocationRepository() {
        session = HibernateUtil.getSessionFactory().openSession();
        em = session.getEntityManagerFactory().createEntityManager();
    }

    public Location findById(Integer id) {
        if (existsById(id)){
            Query query = em.createQuery("SELECT c FROM Location c WHERE c.id = ?1", Location.class);
            return (Location) query.setParameter(1, id).getSingleResult();
        }else
            throw new EntityIsNotValidException("location with id = " + id + " does not exist");

    }

    public boolean existsById(Integer id) {
        org.hibernate.query.Query query = session.createQuery("SELECT 1 FROM Location l WHERE l.id = ?1");
        query.setParameter(1, id);
        return (query.uniqueResult() != null);
    }

    public List<Location> findAll() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Location> criteriaQuery = criteriaBuilder.createQuery(Location.class);
        Root<Location> from = criteriaQuery.from(Location.class);
        CriteriaQuery<Location> select = criteriaQuery.select(from);
        TypedQuery<Location> typedQuery = em.createQuery(select);
        return typedQuery.getResultList();
    }

    public void create(Location location) {
        em.getTransaction().begin();
        em.persist(location);
        em.getTransaction().commit();
        em.clear();
    }

    public void update(Location location) {
        em.getTransaction().begin();
        em.merge(location);
        em.getTransaction().commit();
        em.clear();
    }
}
