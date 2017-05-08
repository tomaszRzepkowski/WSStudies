package hello;

import hello.pojo.dto.Person;
import hello.pojo.service.DataProvider;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @Autowired
    DataProvider dataProvider;
//TODO odkomentowac zmienic mapping i bedzie smigac
//    @RequestMapping("/person")
//    public Person getPerson(@RequestParam(value="personId") Long personId) {
//        return dataProvider.getPersonById(personId);
//    }

    @RequestMapping("/removePerson")
    public void removePerson(@RequestParam(value="personId") Long personId) {
        dataProvider.removeIfExists(personId);
    }

    @RequestMapping(value = "/listPeople", method = RequestMethod.GET, produces = "application/json")
    public List<Person> listPeople() {
        return dataProvider.getAllPeople();
    }

    @RequestMapping(value = "/addPerson", method = RequestMethod.PUT, consumes = "application/json")
    public void addPerson(@RequestBody Person person) {
        dataProvider.addNewPerson(person);
    }

    @RequestMapping(value = "/modify", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    public Person modifyPerson(@RequestBody Person person) {
        return dataProvider.modifyPerson(person);
    }

}
