package com.neocortex.rest.service;

import com.neocortex.rest.repositories.PersonRepository;
import com.neocortex.rest.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {


    private PersonRepository personRepository;

    @Override
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Override
    public Person savePerson(Person person) {

       return personRepository.save(person);

    }

    @Override
    public Person getPerson(int id) {
        Optional<Person> personOptional = personRepository.findById(id);
        Person person = null;

        if(personOptional.isPresent()) person = personOptional.get();

        return person;

    }

    @Override
    public void delete(int id) { personRepository.deleteById(id);
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
