package xml;

import dto.dtoList.CoordinatesDTOList;
import dto.dtoList.LocationDTOList;
import dto.dtoList.TicketDTOList;
import dto.dtoList.PersonDTOList;
import exceptions.XmlParseException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class XmlToJavaConverter {

    public CoordinatesDTOList getCoordinatesDTOFromXml(String xml) {
        CoordinatesDTOList coordinatesDTOList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CoordinatesDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            coordinatesDTOList = (CoordinatesDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return coordinatesDTOList;
    }

    public TicketDTOList getTicketDTOFromXml(String xml) {
        TicketDTOList ticketDTOList;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TicketDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ticketDTOList = (TicketDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            throw new XmlParseException(e.getMessage());
        }
        return ticketDTOList;
    }

    public LocationDTOList getLocationDTOFromXml(String xml) {
        LocationDTOList ticketDTO = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LocationDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            ticketDTO = (LocationDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return ticketDTO;
    }

    public PersonDTOList getPersonDTOFromXml(String xml) {
        PersonDTOList personDTOList = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonDTOList.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            personDTOList = (PersonDTOList) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return personDTOList;
    }
}
