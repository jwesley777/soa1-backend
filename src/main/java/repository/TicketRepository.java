package repository;

import dto.PagedTicketList;
import entity.Ticket;
import enums.TicketType;
import exceptions.EntityIsNotValidException;
import org.hibernate.Session;
import utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.UrlParametersUtil.parseInteger;

public class TicketRepository {
    private Session session;
    private EntityManager em;

    public TicketRepository() {
        session = HibernateUtil.getSessionFactory().openSession();
        em = session.getEntityManagerFactory().createEntityManager();
    }

    private boolean existsById(Integer id) {
        org.hibernate.query.Query query = session.createQuery("SELECT 1 FROM Ticket l WHERE l.id = ?1");
        query.setParameter(1, id);
        return (query.uniqueResult() != null);
    }


    public Ticket findById(Integer id) {
        if (existsById(id)) {
            Query query = em.createQuery("SELECT c FROM Ticket c WHERE c.id = ?1", Ticket.class);
            return (Ticket) query.setParameter(1, id).getSingleResult();
        } else
            throw new EntityIsNotValidException("ticket with id = " + id + " does not exist");
    }

    public PagedTicketList findAll(String perPage, String curPage, String sortBy, String filterBy) {

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> criteriaQuery = criteriaBuilder.createQuery(Ticket.class);
        Root<Ticket> from = criteriaQuery.from(Ticket.class);
        CriteriaQuery<Ticket> select = criteriaQuery.select(from);

        List<Order> orderList = getOrderList(sortBy, criteriaBuilder, from);
        if (!orderList.isEmpty())
            criteriaQuery.orderBy(orderList);

        ArrayList<Predicate> predicates = getPredicatesList(filterBy, criteriaBuilder, from);
        if (!predicates.isEmpty())
            select.where(predicates.toArray(new Predicate[]{}));

        PagedTicketList pagedTicketList = new PagedTicketList();

        pagedTicketList.setCount(getOverallCount(criteriaBuilder, predicates));
        pagedTicketList.setTicketList(findAll(perPage, curPage, select));

        return pagedTicketList;

    }

    public void clearEntityManager() {
        em.clear();
    }

    private List<Ticket> findAll(String perPage, String curPage, CriteriaQuery<Ticket> select) {
        if (perPage != null && curPage != null) {
            int pageNumber = parseInteger(curPage);
            int pageSize = parseInteger(perPage);
            TypedQuery<Ticket> typedQuery = em.createQuery(select);
            typedQuery.setFirstResult((pageNumber - 1) * pageSize);
            typedQuery.setMaxResults(pageSize);
            return typedQuery.getResultList();
        } else
            return findAll(select);
    }

    private List<Ticket> findAll(CriteriaQuery<Ticket> select) {
        TypedQuery<Ticket> typedQuery = em.createQuery(select);
        return typedQuery.getResultList();
    }

    private Long getOverallCount(CriteriaBuilder criteriaBuilder, ArrayList<Predicate> predicates) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Ticket.class)));
        em.createQuery(countQuery);
        if (predicates.size() > 0)
            countQuery.where(predicates.toArray(new Predicate[]{}));
        return em.createQuery(countQuery).getSingleResult();

    }

    private ArrayList<Predicate> getPredicatesList(String filterBy, CriteriaBuilder criteriaBuilder, Root<Ticket> from) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        if (filterBy != null) {
            List<String> notParsedFilters = new ArrayList<>(Arrays.asList(filterBy.split(";")));
            for (String filterString : notParsedFilters) {
                List<String> filter = new ArrayList<>(Arrays.asList(filterString.split(",")));
                switch (filter.get(0)) {
                    case ("id"):
                        if (filter.size() < 3) throw new EntityIsNotValidException("number of arguments less than required");
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.get("id"), Integer.parseInt(filter.get(1))));
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(from.get("id"), Integer.parseInt(filter.get(2))));
                        break;
                    case ("name"):
                        if (filter.size() < 2) throw new EntityIsNotValidException("number of arguments less than required");
                        predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("name")),
                                filter.get(1).toUpperCase() + "%"));
                        break;
                    case ("discount"):
                        if (filter.size() < 3) throw new EntityIsNotValidException("number of arguments less than required");
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.get("discount"), Integer.parseInt(filter.get(1))));
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(from.get("discount"), Integer.parseInt(filter.get(2))));
                        break;
                    case ("price"):
                        if (filter.size() < 3) throw new EntityIsNotValidException("number of arguments less than required");
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.get("price"), Integer.parseInt(filter.get(1))));
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(from.get("price"), Integer.parseInt(filter.get(2))));
                        break;
                    case ("type"):
                        if (filter.size() < 2) throw new EntityIsNotValidException("number of arguments less than required");
                        predicates.add(criteriaBuilder.equal(from.get("type"), TicketType.valueOf(filter.get(1))));
                        break;
                    case ("date"):
                        if (filter.size() < 3) throw new EntityIsNotValidException("number of arguments less than required");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/y");
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.get("creationDate"), LocalDate.parse(filter.get(1), formatter)));
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(from.get("creationDate"), LocalDate.parse(filter.get(2), formatter)));
                        break;
                    case ("coordinate"):
                        if (filter.size() < 5) throw new EntityIsNotValidException("number of arguments less than required");
                        Predicate x = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get("coordinates").get("x"), Double.parseDouble(filter.get(1))),
                                criteriaBuilder.lessThanOrEqualTo(from.get("coordinates").get("x"), Double.parseDouble(filter.get(2))));
                        Predicate y = criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(from.get("coordinates").get("y"), Double.parseDouble(filter.get(3))),
                                criteriaBuilder.lessThanOrEqualTo(from.get("coordinates").get("y"), Double.parseDouble(filter.get(4))));
                        predicates.add(criteriaBuilder.and(x, y));
                        break;
                    case ("person"):
//                        predicates.add(criteriaBuilder.like(criteriaBuilder.upper(from.get("person").get("id")),
//                                filter.get(1).toUpperCase() + "%"));
//                        break;
                        if (filter.size() < 3) throw new EntityIsNotValidException("number of arguments less than required");
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(from.get("person").get("id"), Integer.parseInt(filter.get(1))));
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(from.get("person").get("id"), Integer.parseInt(filter.get(2))));
                        break;
                }
            }
        }

        return predicates;
    }

    private List<Order> getOrderList(String sortBy, CriteriaBuilder criteriaBuilder, Root<Ticket> from) {
        List<Order> orderList = new ArrayList();
        if (sortBy != null) {
            List<String> criteria = new ArrayList<>(Arrays.asList(sortBy.split(";")));
            for (String criterion : criteria) {
                switch (criterion) {
                    case ("id"):
                        orderList.add(criteriaBuilder.asc(from.get("id")));
                        break;
                    case ("name"):
                        orderList.add(criteriaBuilder.asc(from.get("name")));
                        break;
                    case ("price"):
                        orderList.add(criteriaBuilder.asc(from.get("price")));
                        break;
                    case ("discount"):
                        orderList.add(criteriaBuilder.asc(from.get("discount")));
                        break;
                    case ("type"):
                        orderList.add(criteriaBuilder.asc(from.get("type")));
                        break;
                    case ("date"):
                        orderList.add(criteriaBuilder.asc(from.get("creationDate")));
                        break;
                    case ("coordinate"):
                        orderList.add(criteriaBuilder.asc(from.get("coordinates").get("x")));
                        break;
                    case ("person"):
                        orderList.add(criteriaBuilder.asc(from.get("person").get("id")));
                        break;
                }
            }
        }
        return orderList;
    }

    public void create(Ticket ticket) {
        em.getTransaction().begin();
        em.persist(ticket);
        em.getTransaction().commit();
        em.clear();
    }

    public void update(Ticket ticket) {
        em.getTransaction().begin();
        em.merge(ticket);
        em.getTransaction().commit();
        em.clear();
    }

    public void delete(Ticket ticket) {
        em.getTransaction().begin();
        em.remove(ticket);
        em.getTransaction().commit();
        em.clear();
    }
}
