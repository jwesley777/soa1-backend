package servlets;

import dto.CoordinatesDTO;
import dto.dtoList.CoordinatesDTOList;
import entity.Coordinates;
import mapper.CoordinatesMapper;
import repository.CoordinatesRepository;
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

@WebServlet("/coordinates/*")
public class CoordinatesServlet extends HttpServlet {
    private CoordinatesRepository repository;
    private JavaToXmlConverter javaToXml;
    private XmlToJavaConverter xmlToJava;
    private EntityValidator entityValidator;
    private CoordinatesMapper coordinatesMapper;


    @Override
    public void init() throws ServletException {
        repository = new CoordinatesRepository();
        javaToXml = new JavaToXmlConverter();
        xmlToJava = new XmlToJavaConverter();
        entityValidator = new EntityValidator();
        coordinatesMapper = new CoordinatesMapper();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String id = null;
        if (pathInfo != null)
            id = pathInfo.substring(1);

        if (id != null) {
            Coordinates coordinates = (repository.findById(Integer.parseInt(id)));
            CoordinatesDTOList dto = new CoordinatesDTOList(new ArrayList<>());
            List<CoordinatesDTO> dtoList = new ArrayList<>();
            dtoList.add(coordinatesMapper.mapCoordinatesToCoordinatesDTO(coordinates));
            dto.setCoordinatesList(dtoList);
            response.getWriter().write(javaToXml.coordinatesToXML(dto));
            return;
        }

        List<Coordinates> coordinates = repository.findAll();

        CoordinatesDTOList dto = new CoordinatesDTOList(new ArrayList<>());
        dto.setCoordinatesList(coordinatesMapper.mapCoordinatesListToCoordinatesDTOList(coordinates));

        response.getWriter().write(javaToXml.coordinatesToXML(dto));
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        CoordinatesDTOList coordinatesDTOList = xmlToJava.getCoordinatesDTOFromXml(requestBody);
        Coordinates coordinatesToUpdate = coordinatesMapper.mapCoordinatesDTOToCoordinates(coordinatesDTOList.getCoordinatesList().get(0));
        entityValidator.validateCoordinates(coordinatesToUpdate);
        repository.update(coordinatesToUpdate);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestBody = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        CoordinatesDTOList coordinatesDTOList = xmlToJava.getCoordinatesDTOFromXml(requestBody);
        Coordinates coordinatesToPersist = coordinatesMapper.mapCoordinatesDTOToCoordinates(coordinatesDTOList.getCoordinatesList().get(0));
        entityValidator.validateCoordinates(coordinatesToPersist);
        repository.create(coordinatesToPersist);
    }

}
