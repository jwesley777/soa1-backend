package mapper;

import dto.PersonDTO;
import entity.Person;
import exceptions.EntityIsNotValidException;
import repository.LocationRepository;
import utils.FieldValidationUtil;

import java.util.ArrayList;
import java.util.List;

public class PersonMapper {

    private LocationMapper locationMapper;
    private LocationRepository locationRepository;

    public PersonMapper() {
        locationMapper = new LocationMapper();
        locationRepository = new LocationRepository();
    }

    public Person mapPersonDTOToPerson(PersonDTO personDTO) {
        Person person = new Person();
        person.setId(FieldValidationUtil.getIntegerFieldValue(personDTO.getId()));
        person.setEyeColor(FieldValidationUtil.getColorValue(personDTO.getEyeColor()));
        person.setHeight(FieldValidationUtil.getDoubleFieldValue(personDTO.getHeight()));
        if (personDTO.getLocation().getId().equals(""))
            throw new EntityIsNotValidException("location must not be null");
        person.setLocation(locationMapper.mapLocationDTOToLocation(personDTO.getLocation()));
        if (person.getLocation().getId() != null) {
            if (!locationRepository.existsById(person.getLocation().getId()))
                throw new EntityIsNotValidException("location with id = " + person.getLocation().getId() + " does not exist");
        }

        return person;
    }

    public PersonDTO mapPersonToPersonDTO(Person person) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(String.valueOf(person.getId()));
        personDTO.setLocation(locationMapper.mapLocationToLocationDTO(person.getLocation()));
        personDTO.setHeight(String.valueOf(person.getHeight()));
        personDTO.setEyeColor(String.valueOf(person.getEyeColor()));
        return personDTO;
    }

    public List<PersonDTO> mapPersonListToPersonDTOList(List<Person> personList) {
        ArrayList<PersonDTO> personDTOArrayList = new ArrayList<>();
        for (Person person : personList) {
            personDTOArrayList.add(mapPersonToPersonDTO(person));
        }
        return personDTOArrayList;
    }
}
