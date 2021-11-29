package servlets;

import dto.TicketDTO;
import dto.PagedTicketList;
import dto.dtoList.TicketDTOList;
import entity.Ticket;
import mapper.TicketMapper;
import repository.TicketRepository;
import utils.UrlParametersUtil;
import validation.EntityValidator;
import xml.JavaToXmlConverter;
import xml.XmlToJavaConverter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(value = "/tickets/*", loadOnStartup = 1)
public class TicketServlet extends HttpServlet {
    private TicketRepository repository;
    private JavaToXmlConverter javaToXml;
    private XmlToJavaConverter xmlToJava;
    private EntityValidator entityValidator;
    private TicketMapper ticketMapper;

    @Override
    public void init() throws ServletException {
        repository = new TicketRepository();
        javaToXml = new JavaToXmlConverter();
        xmlToJava = new XmlToJavaConverter();
        entityValidator = new EntityValidator();
        ticketMapper = new TicketMapper();
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TicketDTOList ticketDTOList = xmlToJava.getTicketDTOFromXml(requestBody);
        Ticket ticketToUpdate = ticketMapper.mapTicketDTOToTicket(ticketDTOList.getTicketList().get(0));
        Ticket existingTicket = repository.findById(ticketToUpdate.getId());
        ticketToUpdate.setCreationDate(existingTicket.getCreationDate());
        entityValidator.validateTicket(ticketToUpdate);
        repository.update(ticketToUpdate);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        Ticket ticketToPersist = ticketMapper.mapTicketDTOToTicket(xmlToJava.getTicketDTOFromXml(requestBody).getTicketList().get(0));
        ticketToPersist.setCreationDate(new Date());
        entityValidator.validateTicket(ticketToPersist);
        repository.create(ticketToPersist);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String perPage = UrlParametersUtil.getField(request, "perPage");
        String curPage = UrlParametersUtil.getField(request, "curPage");
        String sortBy = UrlParametersUtil.getField(request, "sortBy");
        String filterBy = UrlParametersUtil.getField(request, "filterBy");

        String pathInfo = request.getPathInfo();

        String ticketId = null;
        if (pathInfo != null)
            ticketId = pathInfo.substring(1);

        if (ticketId != null) {
            Ticket ticket = repository.findById(Integer.parseInt(ticketId));
            TicketDTOList dto = new TicketDTOList(new ArrayList<>());
            List<TicketDTO> dtoList = dto.getTicketList();
            dtoList.add(ticketMapper.mapTicketToTicketDTO(ticket));
            response.getWriter().write(javaToXml.ticketsToXml(dto));
            return;
        }

        PagedTicketList pagedTicketList = repository.findAll(perPage, curPage, sortBy, filterBy);
        TicketDTOList dto = new TicketDTOList((ticketMapper.mapTicketListToTicketDTOList(pagedTicketList.getTicketList())));
        response.getWriter().write(javaToXml.ticketsToXml(dto));

        repository.clearEntityManager();

    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String ticketId = null;
        if (pathInfo != null)
            ticketId = pathInfo.substring(1);
        Ticket ticket = repository.findById(Integer.parseInt(ticketId));
        repository.delete(ticket);
    }

}