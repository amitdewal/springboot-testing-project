package com.start.service;

import com.start.exception.ResourceNotFoundException;
import com.start.model.Employee;
import com.start.repository.EmployeeRepository;
import com.start.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private  EmployeeServiceImpl employeeService;
   // private  EmployeeService employeeService;

     private  Employee employee;
    @BeforeEach
    public  void setup(){
       // employeeRepository = Mockito.mock(EmployeeRepository.class);
        //employeeService = new EmployeeServiceImpl(employeeRepository);
         employee = Employee.builder()
                .id(1L)
                .firstName("amit")
                .lastName("dewal")
                .email("amit@gmail.com")
                .build();
    }


    /* Postitive Senario*/
     //Junit test for saveEmployee method
         @DisplayName("Junit test for saveEmployee method")
         @Test
         public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){

             //given - precondtion or setup

             given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
             given(employeeRepository.save(employee)).willReturn(employee);
             System.out.println(employeeRepository);
             System.out.println(employeeService);
             // when - action or behaviour that we are going to test

             Employee savedEmployee = employeeService.saveEmployee(employee);
             System.out.println(savedEmployee);
             //then - verify the output
             assertThat(savedEmployee).isNotNull();
    }

    /*Negative Senario*/
    //Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenReturnException(){

        //given - precondtion or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                                .willReturn(Optional.of(employee));

        //given(employeeRepository.save(employee)).willReturn(employee);

        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or behaviour that we are going to test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class,() -> employeeService.saveEmployee(employee));

        //then - verify the output
        verify(employeeRepository,never()).save(any(Employee.class));
    }


    /* Postitive Senario*/
     //Junit test for getAllEmployees method
         @Test
         @DisplayName("Junit test for getAllEmployees method")
         public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeeList(){

             //given - precondtion or setup
             Employee employee1 = Employee.builder()
                     .id(2L)
                     .firstName("Tony")
                     .lastName("Stark")
                     .email("Tony@gmail.com")
                     .build();

             given(employeeRepository.findAll()).willReturn(List.of(employee,employee1));


             // when - action or behaviour that we are going to test
             List<Employee> employeeList = employeeService.getAllEmployees();

             //then - verify the output
             assertThat(employeeList).isNotNull();
             assertThat(employeeList.size()).isEqualTo(2);
         }


    //Junit test for getAllEmployees method
    /*negative senario*/
    @Test
    @DisplayName("Junit test for getAllEmployees method (negative senario)")
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeeList(){

        //given - precondtion or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("Tony")
                .lastName("Stark")
                .email("Tony@gmail.com")
                .build();

        given(employeeRepository.findAll()).willReturn(Collections.emptyList());


        // when - action or behaviour that we are going to test
        List<Employee> employeeList = employeeService.getAllEmployees();

        //then - verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }


     //Junit test for get employee by id operation
    @DisplayName("Junit test for get employee by id operation")
         @Test
         public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

             //given - precondtion or setup
          given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

             // when - action or behaviour that we are going to test

        Employee saveEmployee = employeeService.getEmployeeById(employee.getId()).get();
        //then - verify the output

        assertThat(saveEmployee).isNotNull();
         }


          //Junit test for update employee method
              @Test
              @DisplayName(("Junit test for update employee method"))
              public void givenEmployeeObject_whenUpdateEmployee_thenUpdatedEmployee(){

                  //given - precondtion or setup

                  given(employeeRepository.save(employee)).willReturn(employee);

                  employee.setEmail("ram@gmail.com");
                  employee.setFirstName("ram");

                  // when - action or behaviour that we are going to test

                  Employee updateEmployee = employeeService.updateEmployee(employee);


                  //then - verify the output

                  assertThat(updateEmployee.getEmail()).isEqualTo("ram@gmail.com");
                  assertThat(updateEmployee.getFirstName()).isEqualTo("ram");
              }

               //Junit test for delete employee method
                    @DisplayName("Junit test for delete employee method")
                   @Test
                   public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){
                        Long employeeId = 1L;
                       //given - precondtion or setup
                        willDoNothing().given(employeeRepository).deleteById(employeeId);

                       // when - action or behaviour that we are going to test

                        employeeService.deleteEmployee(employeeId);

                       //then - verify the output
                        verify(employeeRepository,times(1)).deleteById(employeeId);
                   }
}
