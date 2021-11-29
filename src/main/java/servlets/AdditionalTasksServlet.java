package servlets;

import dto.CountDTO;
import dto.dtoList.PersonDTOList;
import dto.dtoList.TicketDTOList;
import dto.dtoList.TicketTypeDTOList;
import entity.Ticket;
import entity.Person;
import enums.TicketType;
import mapper.PersonMapper;
import mapper.TicketMapper;
import org.hibernate.Session;
import org.hibernate.query.Query;
import utils.HibernateUtil;
import xml.JavaToXmlConverter;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/additional")
public class AdditionalTasksServlet extends HttpServlet {
    private Session session;
    private EntityManager em;
    private JavaToXmlConverter javaToXml;
    private TicketMapper ticketMapper;

    @Override
    public void init() throws ServletException {
        session = HibernateUtil.getSessionFactory().openSession();
        em = session.getEntityManagerFactory().createEntityManager();
        javaToXml = new JavaToXmlConverter();
        ticketMapper = new TicketMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String discount = request.getParameter("discount");
        String person_ = request.getParameter("person");

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        CriteriaQuery<Ticket> criteriaQuery = criteriaBuilder.createQuery(Ticket.class);
        Root<Ticket> from = criteriaQuery.from(Ticket.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Ticket.class)));
        em.createQuery(countQuery);

//        if (type != null) {
////            countQuery.where(criteriaBuilder.equal(from.get("type"), TicketType.valueOf(type)));
////            Long countResult = em.createQuery(countQuery).getSingleResult();
////            CountDTO countDTO = new CountDTO();
////            countDTO.setCount(countResult);
////            response.getWriter().write(javaToXml.countToXML(countDTO));
////            return;
//        }
        if (discount != null) {
//            CriteriaQuery<Person> criteriaQueryPerson = criteriaBuilder.createQuery(Long.class);
//            Root<Person> root = criteriaQueryPerson.from(Person.class);
//            criteriaQueryPerson.select(root).where(criteriaBuilder.lessThan(root.get("height"), person.getHeight()));
//            Query<Person> query = session.createQuery(criteriaQueryPerson);
//            List<Person> personList = query.getResultList();
//            PersonDTOList dto = new PersonDTOList(new ArrayList<>());
//            dto.setPersonsList(personMapper.mapPersonListToPersonDTOList(personList));
//            response.getWriter().write(javaToXml.personsToXML(dto));
            CriteriaQuery<Ticket> criteriaQueryTicket = criteriaBuilder.createQuery(Ticket.class);
            Root<Ticket> root = criteriaQueryTicket.from(Ticket.class);
            criteriaQueryTicket.select(root).where(criteriaBuilder.lessThan(root.get("discount"), Integer.parseInt(discount)));
            Query<Ticket> query = session.createQuery(criteriaQueryTicket);
            List<Ticket> ticketList = query.getResultList();
            TicketDTOList dto = new TicketDTOList(
                    ticketMapper.mapTicketListToTicketDTOList(ticketList)
            );
            response.getWriter().write(javaToXml.ticketsToXml(dto));



//            countQuery.where(criteriaBuilder.greaterThan(from.get("price"), Integer.parseInt(discount)));
//            Long countResult = em.createQuery(countQuery).getSingleResult();
//            CountDTO countDTO = new CountDTO();
//            countDTO.setCount(countResult);
//            response.getWriter().write(javaToXml.countToXML(countDTO));
            return;
        }
        else if (person_ != null) {
            javax.persistence.Query singleQuery = em.createQuery("SELECT c FROM Person c WHERE c.id = ?1", Person.class);
            Person person = (Person) singleQuery.setParameter(1, Integer.parseInt(person_)).getSingleResult();

            countQuery.where(criteriaBuilder.lessThan(from.get("person").get("id"), person.getId()));
            Long countResult = em.createQuery(countQuery).getSingleResult();
            CountDTO countDTO = new CountDTO();
            countDTO.setCount(countResult);
            response.getWriter().write(javaToXml.countToXML(countDTO));


        }
        else {
            javax.persistence.Query query = em.createQuery("SELECT DISTINCT c.type FROM Ticket c");
            List<TicketType> ticketTypes = query.getResultList();
            TicketTypeDTOList dto = new TicketTypeDTOList(new ArrayList<>());
            dto.setTicketTypeList(ticketMapper.mapTicketTypesListToTicketTypesDTOList(ticketTypes));
            response.getWriter().write(javaToXml.ticketTypesToXML(dto));
            return;
        }
    }
}
