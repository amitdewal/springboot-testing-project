package com.start.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.start.model.Employee;
import com.start.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;


import org.junit.platform.engine.SelectorResolutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;


    //Junit test for create employee REST api
    @Test
    void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws Exception {


        //given - precondition or setup
        Employee employee = Employee.builder().firstName("amit").lastName("dewal").email("amit@pmail.com").build();

        given(employeeService.saveEmployee(any(Employee.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(post("/api/employees").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(employee)));

        //then - verify the output

        response.andDo(print()).andExpect(status().isCreated()).andExpect(jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName()))).andExpect(jsonPath("$.lastName", CoreMatchers.is(employee.getLastName()))).andExpect(jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }


    //Junit test for get all employees REST api
    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeeList() throws Exception {

        //given - precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Amit").lastName("Dewal").email("amit@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Sachin").lastName("Kumar").email("sachin@gmail.com").build());

        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);

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
        Long employeeId = 1l;
        Employee employee = Employee.builder().firstName("Amit").lastName("Dewal").email("amit@gmail.com").build();


        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isOk()).andDo(print()).andExpect(jsonPath("$.firstName", is(employee.getFirstName()))).andExpect(jsonPath("$.lastName", is(employee.getLastName()))).andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //Negative Scenario  - Valid employee id
    //Junit test for get employee by id REST API
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given -precondition or setup
        Long employeeId = 1L;

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        // when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(get("/api/employee/{id}", employeeId));

        //then - verify the output
        response.andExpect(status().isNotFound()).andDo(print());
    }


    //Positive Scenario
    //Junit test for update employee REST API
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception {

        //given - precondtion or setup
        Long employeeId = 1L;

        //consider below object as it is coming from DB
        Employee savedEmployee = Employee.builder()
                .firstName("Amit")
                .lastName("Dewal")
                .email("amit@gmail.com")
                .build();

        //consider below object coming from api call or say having updated employee information
        Employee updatedEmployee = Employee.builder()
                .firstName("Sachin")
                .lastName("Kumar")
                .email("sachin@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation -> invocation.getArgument(0)));
        // when - action or behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output

        response.andExpect(status()
                .isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName",is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email",is(updatedEmployee.getEmail())));
         }


    //Negative Scenario
    //Junit test for update employee REST API
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {

        //given - precondtion or setup
        Long employeeId = 1L;

        //consider below object as it is coming from DB
        Employee savedEmployee = Employee.builder()
                .firstName("Amit")
                .lastName("Dewal")
                .email("amit@gmail.com")
                .build();

        //consider below object coming from api call or say having updated employee information
        Employee updatedEmployee = Employee.builder()
                .firstName("Sachin")
                .lastName("Kumar")
                .email("sachin@gmail.com")
                .build();

        given(employeeService.getEmployeeById(employeeId))
                             .willReturn(Optional.empty());
        given(employeeService.updateEmployee(any(Employee.class))).willAnswer((invocation -> invocation.getArgument(0)));
        // when - action or behaviour that we are going to test

        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        //then - verify the output

        response.andExpect(status().isNotFound()).andDo(print());

    }


     //Junit test for delete Employee REST API
         @Test
         public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {

             //given - precondition or setup
             Long employeeId = 1L;
             willDoNothing().given(employeeService).deleteEmployee(employeeId);


             // when - action or behaviour that we are going to test
             ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));

             //then - verify the

             response.andExpect(status().isOk()).andDo(print());

         }
}
