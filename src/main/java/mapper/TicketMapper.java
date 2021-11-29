package mapper;

import dto.TicketDTO;
import dto.TicketTypeDTO;
import dto.dtoList.TicketTypeDTOList;
import entity.Ticket;
import entity.Person;
import enums.TicketType;
import exceptions.EntityIsNotValidException;
import repository.CoordinatesRepository;
import repository.PersonRepository;
import utils.FieldValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class TicketMapper {
    private CoordinatesMapper coordinatesMapper;
    private PersonMapper personMapper;
    private CoordinatesRepository coordinatesRepository;
    private PersonRepository personRepository;

    public TicketMapper() {
        coordinatesMapper = new CoordinatesMapper();
        personMapper = new PersonMapper();
        coordinatesRepository = new CoordinatesRepository();
        personRepository = new PersonRepository();
    }

    public Ticket mapTicketDTOToTicket(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(FieldValidationUtil.getIntegerFieldValue(ticketDTO.getId()));
        ticket.setCoordinates(coordinatesMapper.mapCoordinatesDTOToCoordinates(ticketDTO.getCoordinates()));
        if (ticket.getCoordinates().getId() == null) throw new EntityIsNotValidException("coordinates must not be null");
        if (ticket.getCoordinates() != null && !coordinatesRepository.existsById(ticket.getCoordinates().getId()))
            throw new EntityIsNotValidException("coordinates with id = " + ticket.getCoordinates().getId() + " does not exist");
        ticket.setPrice(FieldValidationUtil.getDoubleFieldValue(ticketDTO.getPrice()));
        ticket.setType(FieldValidationUtil.getTypeValue(ticketDTO.getType()));
        ticket.setName(FieldValidationUtil.getStringValue(ticketDTO.getName()));
        ticket.setDiscount(FieldValidationUtil.getLongFieldValue(ticketDTO.getDiscount()));
        Person person;
        if (!ticketDTO.getPerson().getId().equals("")){
            person = personRepository.findById(Integer.parseInt(ticketDTO.getPerson().getId()));
        }else {
            throw new EntityIsNotValidException("person must not be null");
        }
        ticket.setPerson(person);
        return ticket;
    }

    public TicketDTO mapTicketToTicketDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(String.valueOf(ticket.getId()));
        ticketDTO.setName(ticket.getName());
        ticketDTO.setCoordinates(coordinatesMapper.mapCoordinatesToCoordinatesDTO(ticket.getCoordinates()));
        ticketDTO.setPrice(String.valueOf(ticket.getPrice()));
        ticketDTO.setCreationDate(String.valueOf(ticket.getCreationDate()));
        ticketDTO.setPerson(personMapper.mapPersonToPersonDTO(ticket.getPerson()));
        ticketDTO.setType(String.valueOf(ticket.getType()));
        ticketDTO.setDiscount(String.valueOf(ticket.getDiscount()));
        return ticketDTO;
    }

    public TicketTypeDTO mapTicketTypeToTicketTypeDTO(TicketType ticketType) {
        TicketTypeDTO ticketTypeDTO = new TicketTypeDTO();
        ticketTypeDTO.setType(ticketType.toString());
        return ticketTypeDTO;
    }

    public List<TicketDTO> mapTicketListToTicketDTOList(List<Ticket> ticketList) {
        ArrayList<TicketDTO> ticketDTOArrayList = new ArrayList<>();
        for (Ticket ticket : ticketList) {
            ticketDTOArrayList.add(mapTicketToTicketDTO(ticket));
        }
        return ticketDTOArrayList;
    }


    public List<TicketTypeDTO> mapTicketTypesListToTicketTypesDTOList (List<TicketType> ticketTypesList) {
        ArrayList<TicketTypeDTO> ticketTypesDTOArrayList = new ArrayList<>();
        for (TicketType ticketType: ticketTypesList) {
            ticketTypesDTOArrayList.add(mapTicketTypeToTicketTypeDTO(ticketType));
        }
        return ticketTypesDTOArrayList;
    }

}
