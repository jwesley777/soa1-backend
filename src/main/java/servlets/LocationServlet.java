package servlets;

import dto.dtoList.LocationDTOList;
import entity.Location;
import mapper.LocationMapper;
import repository.LocationRepository;
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

@WebServlet("/locations/*")
public class LocationServlet extends HttpServlet {
    private LocationRepository repository;
    private JavaToXmlConverter javaToXml;
    private XmlToJavaConverter xmlToJava;
    private LocationMapper locationMapper;
    private EntityValidator entityValidator;

    @Override
    public void init() throws ServletException {
        repository = new LocationRepository();
        javaToXml = new JavaToXmlConverter();
        xmlToJava = new XmlToJavaConverter();
        locationMapper = new LocationMapper();
        entityValidator = new EntityValidator();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String id = null;
        if (pathInfo != null)
            id = pathInfo.substring(1);

        if (id != null) {
            Location location = repository.findById(Integer.parseInt(id));
            LocationDTOList dto = new LocationDTOList(new ArrayList<>());
            dto.getLocationsList().add(locationMapper.mapLocationToLocationDTO(location));
            response.getWriter().write(javaToXml.locationsToXML(dto));
            return;
        }

        List<Location> locations = repository.findAll();
        LocationDTOList dto = new LocationDTOList(new ArrayList<>());
        dto.setLocationsList(locationMapper.mapLocationListToLocationDTOList(locations));
        response.getWriter().write(javaToXml.locationsToXML(dto));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LocationDTOList locationDTOList = xmlToJava.getLocationDTOFromXml(requestBody);
        Location locationToUpdate = locationMapper.mapLocationDTOToLocation(locationDTOList.getLocationsList().get(0));
        entityValidator.validateLocation(locationToUpdate);
        repository.create(locationToUpdate);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        LocationDTOList locationDTOList = xmlToJava.getLocationDTOFromXml(requestBody);
        Location locationToPersist = locationMapper.mapLocationDTOToLocation(locationDTOList.getLocationsList().get(0));
        entityValidator.validateLocation(locationToPersist);
        repository.update(locationToPersist);
    }

}
