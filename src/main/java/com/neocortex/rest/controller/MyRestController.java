package com.neocortex.rest.controller;

import com.neocortex.rest.entity.Person;
import com.neocortex.rest.exception_handling.NoSuchPersonException;
import com.neocortex.rest.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {


    PersonService personService;

    @GetMapping("/persons")
    public List<Person> showAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("/persons/{id}")
    public  Person getPerson(@PathVariable int id ){
        final Person person = personService.getPerson(id);

        if(person == null){
            throw new NoSuchPersonException("There is no person with ID="+id);
        }

        return person;
    }

    @PostMapping("/persons")
    public Person addNewPerson(@RequestBody Person person){
        personService.savePerson(person);
        return person;
    }

    @PutMapping("/persons")
    public Person updatePerson(@RequestBody Person person){
       return personService.savePerson(person);
    }

    @DeleteMapping("persons/{id}")
    public String deletePerson(@PathVariable int id){
        Person person = personService.getPerson(id);

        if(person == null){
            throw  new NoSuchPersonException("Person with id ="+id+" doesn't exist");
        }

        personService.delete(id);
        return "Person was deleted";
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }
}
