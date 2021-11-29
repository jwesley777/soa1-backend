package servlets;

import dto.PersonDTO;
import dto.dtoList.PersonDTOList;
import entity.Person;
import mapper.LocationMapper;
import mapper.PersonMapper;
import repository.PersonRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/persons/*")
public class PersonServlet extends HttpServlet {
    private PersonRepository repository;
    private JavaToXmlConverter javaToXml;
    private XmlToJavaConverter xmlToJava;
    private LocationMapper locationMapper;
    private EntityValidator entityValidator;
    private PersonMapper personMapper;

    @Override
    public void init() throws ServletException {
        repository = new PersonRepository();
        javaToXml = new JavaToXmlConverter();
        xmlToJava = new XmlToJavaConverter();
        locationMapper = new LocationMapper();
        entityValidator = new EntityValidator();
        personMapper = new PersonMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String id = null;
        if (pathInfo != null)
            id = pathInfo.substring(1);

        if (id != null) {
            Person person = repository.findById(Integer.parseInt(id));
            PersonDTOList dto = new PersonDTOList(new ArrayList<>());
            List<PersonDTO> dtoList = dto.getPersonsList();
            dtoList.add(personMapper.mapPersonToPersonDTO(person));
            response.getWriter().write(javaToXml.personsToXML(dto));
            return;
        }

        List<Person> personList = repository.findAll();
        PersonDTOList dto = new PersonDTOList(new ArrayList<>());
        dto.setPersonsList(personMapper.mapPersonListToPersonDTOList(personList));
        response.getWriter().write(javaToXml.personsToXML(dto));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        PersonDTOList personDTOList = xmlToJava.getPersonDTOFromXml(requestBody);
        Person personToUpdate = personMapper.mapPersonDTOToPerson(personDTOList.getPersonsList().get(0));
        entityValidator.validatePerson(personToUpdate);
        repository.update(personToUpdate);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        PersonDTOList personDTOList = xmlToJava.getPersonDTOFromXml(requestBody);
        Person personToPersist = personMapper.mapPersonDTOToPerson(personDTOList.getPersonsList().get(0));
        entityValidator.validatePerson(personToPersist);
        repository.create(personToPersist);
    }

}
