package com.start.repository;

import com.start.model.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EmployeeRespositoryTests {

    @Autowired
    private  EmployeeRepository employeeRepository;


    //Junit test for save employee operation


    @Test
    @DisplayName("Junit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee()
    {
        //given  -precondition or setup

        Employee employee = Employee.builder()
                                    .firstName("amit")
                                    .lastName("dewal")
                                    .email("adewal@gmail.com")
                                    .build();
        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output

        Assertions.assertThat(savedEmployee).isNotNull();
        Assertions.assertThat(savedEmployee.getId()).isGreaterThan(0);
    }
}
