package com.start.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.start.model.Employee;
import com.start.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
 class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
        ;
    }


    //Junit test for create employee REST api
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws Exception {


        //given - precondition or setup
        Employee employee = Employee.builder()
                                    .firstName("amit")
                                    .lastName("dewal")
                                    .email("amit@pmail.com").build();


        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(employee)));

        //then - verify the output

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }


    //Junit test for get all employees REST api
    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Amit").lastName("Dewal").email("amit@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Sachin").lastName("Kumar").email("sachin@gmail.com").build());

        employeeRepository.saveAll(listOfEmployees);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        //then - verify the output
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.size()", is(listOfEmployees.size())));
    }


    //Positive Scenario - valid employee id
    //Junit test for get employee by id REST API
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenEmployeeeObject() throws Exception {

        //given - precondition or setup

        Employee employee = Employee.builder()
                                    .firstName("Amit")
                                    .lastName("Dewal")
                                    .email("amit@gmail.com").build();

       employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        //then - verify the output
        response.andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }




    //Negative Scenario  - Valid employee id
    //Junit test for get employee by id REST API
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given -precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                                    .firstName("Amit")
                                    .lastName("Dewal")
                                    .email("amit@gmail.com").build();

        employeeRepository.save(employee);
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound()).andDo(print());
    }


    //Positive Scenario
    //Junit test for update employee REST API
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        //given - precondition or setup


        //consider below object as it is coming from DB
        Employee savedEmployee = Employee.builder()
                                         .firstName("Amit")
                                          .lastName("Dewal")
                                          .email("amit@gmail.com")
                                            .build();
        employeeRepository.save(savedEmployee);

        //consider below object coming from api call or say having updated employee information
        Employee updatedEmployee = Employee.builder()
                                            .firstName("Sachin")
                                            .lastName("Kumar")
                                            .email("sachin@gmail.com")
                                            .build();


        // when - action or behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output

        response.andExpect(status().isOk())
                                    .andDo(print()).andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                                    .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                                    .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }



    //Negative Scenario
    //Junit test for update employee REST API
    @Test
     void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;

        //consider below object as it is coming from DB
        Employee savedEmployee = Employee.builder()
                                        .firstName("Amit")
                                        .lastName("Dewal")
                                        .email("amit@gmail.com")
                                        .build();

        employeeRepository.save(savedEmployee);

        //consider below object coming from api call or say having updated employee information
        Employee updatedEmployee = Employee.builder()
                                            .firstName("Sachin")
                                            .lastName("Kumar")
                                            .email("sachin@gmail.com").build();


        // when - action or behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output
        response.andExpect(status()
                .isNotFound())
                .andDo(print());

    }


    //Junit test for delete Employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

        //given - precondition or setup

        Employee savedEmployee = Employee.builder()
                .firstName("Amit")
                .lastName("Dewal")
                .email("amit@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);




        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));


        //then - verify the

        response.andExpect(status().isOk())
                .andDo(print());

    }

}
