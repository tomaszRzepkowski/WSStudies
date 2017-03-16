package hello.pojo.service;

import hello.pojo.dto.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

/**
 * Created by tomaszrzepkowski on 09.03.2017.
 */
@Service
public class DataProvider {

    private List<Person> people;

    public DataProvider() {
        initialize();
    }

    public Person getPersonById(Long id) {
        if( id == null ) {
            return null;
        }
        for( Person person : people ) {
            if (person.getId().equals(id)) {
                return person;
            }
        }
        return null;
    }

    private void initialize() {
        people = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            people.add(new Person((long) i, "Bolek " + i, "Lolek"));
        }
    }

    public void removeIfExists(Long personId) {
        if( personId != null ) {
            people = people.stream()
                            .filter(person -> !person.getId().equals(personId))
                            .collect(Collectors.toList());
        }
    }

    public List<Person> getAllPeople() {
        return people;
    }

    public void addNewPerson(Person person) {
        people.add(person);
    }

    public Person modifyPerson(Person person) {
        for(Person p : people) {
            if( person.getId().equals(p.getId()) ) {
                p.setFirstName(person.getFirstName());
                p.setLastName(person.getLastName());
                return p;
            }
        }
        return new Person();
    }
}
