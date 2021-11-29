package xml;

import dto.CountDTO;
import dto.ExceptionDTO;
import dto.dtoList.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;

public class JavaToXmlConverter {
    public String coordinatesToXML(CoordinatesDTOList coordinateDTO) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CoordinatesDTOList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(coordinateDTO, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String ticketsToXml(TicketDTOList employee) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TicketDTOList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(employee, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String locationsToXML(LocationDTOList employee) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(LocationDTOList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(employee, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String personsToXML(PersonDTOList employee) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(PersonDTOList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(employee, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ticketTypesToXML(TicketTypeDTOList ticketTypes) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TicketTypeDTOList.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(ticketTypes, sw);
            return sw.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String countToXML(CountDTO employee) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(CountDTO.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(employee, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String exceprionToXML(ExceptionDTO exception) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ExceptionDTO.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter sw = new StringWriter();
            jaxbMarshaller.marshal(exception, sw);
            return sw.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }

        return null;
    }
}
