package com.neocortex.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neocortex.rest.entity.Person;
import com.neocortex.rest.service.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-person-before.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"delete-person-after.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MyRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonService personService;

    private final String URL = "/api/persons/";

    @Test
    public void showAllPersonsTest() throws Exception{

        mvc.perform(get(URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void getPersonTest() throws Exception{
        Person fromDb = personService.getPerson(1);

        mvc.perform(get(URL+fromDb.getId()))
                .andExpect(status().isOk())
                .andExpect(person("$",fromDb));

    }

    @Test
    public void addNewPersonTest() throws Exception{
        Person johnSnow = new Person("John","Snow","Castle Black",14);

        mvc.perform(postJson(URL,johnSnow))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(person("$",johnSnow));

    }

    @Test
    public void updatePersonTest() throws Exception{
        Person personToUpdate= personService.getPerson(1);
        personToUpdate.setName("new name");

        mvc.perform(putJson(URL,personToUpdate))
                .andExpect(status().isOk())
                .andExpect(person("$",personToUpdate));

        assertTrue(personService.getAllPersons().contains(personToUpdate));
    }

    @Test
    public void deletePersonTest() throws Exception{
        Person fromDb = personService.getPerson(1);

        mvc.perform(delete(URL+fromDb.getId()))
                .andExpect(status().isOk());

        assertFalse(personService.getAllPersons().contains(fromDb));
    }

    @Test
    public void personNotExistTest() throws Exception {
        mvc.perform(
                get(URL+"100"))
                .andExpect(status().isNotFound());

    }

    @Test
    public void BadRequestTest() throws Exception {
        mvc.perform(
                get(URL+"bad-request"))
                .andExpect(status().isBadRequest());

    }


    public static ResultMatcher person(String prefix, Person person) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".name").value(person.getName()),
                jsonPath(prefix + ".surname").value(person.getSurname()),
                jsonPath(prefix + ".address").value(person.getAddress()),
                jsonPath(prefix + ".age").value(person.getAge())
        );
    }

    public static MockHttpServletRequestBuilder postJson(String url, Object body) {
        try {
            String json = new ObjectMapper().writeValueAsString(body);
            return post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public static MockHttpServletRequestBuilder putJson(String url, Object body) {
        try {
            String json = new ObjectMapper().writeValueAsString(body);
            return put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
