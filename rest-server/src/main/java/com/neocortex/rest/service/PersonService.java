package com.neocortex.rest.service;

import com.neocortex.rest.entity.Person;
import java.util.List;

public interface PersonService {
     List<Person> getAllPersons();

     Person savePerson(Person person);

     Person getPerson(int id);

     void delete(int id);
}
