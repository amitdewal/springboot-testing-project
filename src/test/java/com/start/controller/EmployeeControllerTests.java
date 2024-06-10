package com.start.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.start.model.Employee;
import com.start.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static  org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;



     //Junit test for
         @Test
         public void givenEmployeeObject_whenCreateEmployee_thenSavedEmployee() throws  Exception{


             //given - precondtion or setup
             Employee employee = Employee.builder()
                                        .firstName("amit")
                                        .lastName("dewal")
                                         .email("amit@pmail.com")
                                         .build();

             BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                                              .willAnswer((invocation) -> invocation.getArgument(0));

             // when - action or behaviour that we are going to test
             ResultActions response = mockMvc.perform(post("/api/employees")
                                             .contentType(MediaType.APPLICATION_JSON)
                                             .content(objectMapper.writeValueAsString(employee)));

             //then - verify the output

             response.andDo(MockMvcResultHandlers.print())
                     .andExpect(MockMvcResultMatchers.status().isCreated())
                     .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                     .andExpect(MockMvcResultMatchers.jsonPath("$.lastName",CoreMatchers.is(employee.getLastName())))
                     .andExpect(MockMvcResultMatchers.jsonPath("$.email",CoreMatchers.is(employee.getEmail())));
         }
}
