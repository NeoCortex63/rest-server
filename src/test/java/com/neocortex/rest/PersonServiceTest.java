package com.neocortex.rest;

import com.neocortex.rest.config.PersonServiceTestConfiguration;
import com.neocortex.rest.entity.Person;
import com.neocortex.rest.repositories.PersonRepository;
import com.neocortex.rest.service.PersonService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@Import(PersonServiceTestConfiguration.class)
public class PersonServiceTest {

    @Autowired
    private PersonService personService;

    @MockBean
    PersonRepository personRepository;

    @Before
    public void setUp(){

        List<Person> personList = new ArrayList<>();

        Person harryPotter = new Person("Harry", "Potter", " 4 Privet Drive, Surrey", 11);

        Person sherlockHolmes = new Person("Sherlock", "Holmes", "221B Baker Street", 30);

        harryPotter.setId(1);

        sherlockHolmes.setId(2);

        Collections.addAll(personList,harryPotter,sherlockHolmes);

        when(personRepository.findAll()).thenReturn(personList);

        when(personRepository.findById(anyInt())).thenAnswer(i -> {

            int id= i.getArgument(0);

            return personList.stream().filter(person -> person.getId() == id).findFirst();
        });

        
        when(personRepository.save(any(Person.class))).thenAnswer(i -> {

            Person person = i.getArgument(0);

            person.setId(3);

            personList.add(i.getArgument(0));

            return person;
        });

        doAnswer(i -> {

            int id = i.getArgument(0);
            personList.removeIf(p -> p.getId() == id);

            return null;
        }).when(personRepository).deleteById(anyInt());
    }

    @Test
    public void savePersonTest(){
        Person johnSnow = new Person("John","Snow","Castle Black",14);

        personService.savePerson(johnSnow);

        Mockito.verify(personRepository, Mockito.times(1)).save(ArgumentMatchers.any(Person.class));
        assertEquals(johnSnow,personService.getPerson(3));
    }

    @Test
    public void getPersonTest(){
        Person person = personService.getPerson(1);

        Mockito.verify(personRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
        assertNotNull(person,"Bad attempt to get a Person");
        assertEquals(person.getName(),"Harry");
    }

    @Test
    public void getAllPersonsTest(){
        List<Person> personList = personService.getAllPersons();
        Person fromDb = personService.getPerson(2);

        assertNotNull(personList,"Could not get all persons from DB");
        assertEquals(personList.size(),2,"Wrong number of persons in DB");
        assertTrue(personList.contains(fromDb),"List doesn't contain existing person");
    }

    @Test
    public void deletePersonTest(){
        Person fromDb = personService.getPerson(1);
        assertNotNull(fromDb);

        personService.delete(1);
        Mockito.verify(personRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyInt());
        assertNull(personService.getPerson(1),"Person is not removed");
    }

    @Test
    public void getPersonThatDoNotExistTest(){
        Person person = personService.getPerson(50);

        Mockito.verify(personRepository, Mockito.times(1)).findById(ArgumentMatchers.anyInt());
        assertNull(person);
    }

}
