package com.start.repository;


import com.start.integration.AbstractionContainerBaseTest;
import com.start.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//this below line disabled the in memory database for testing purpose
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryIT  extends AbstractionContainerBaseTest {

    @Autowired
    private  EmployeeRepository employeeRepository;

    private  Employee employee;

    //this @BeforeEach annotated method run before each @Test annotated method
    @BeforeEach
    public void setup(){
         employee = Employee.builder()
                .firstName("amit")
                .lastName("dewal")
                .email("adewal@gmail.com")
                .build();
    }



    //Junit test for save employee operation
    @Test
    @DisplayName("Junit test for save employee operation")
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee()
    {
        //given - precondition or setup
//        Employee employee = Employee.builder()
//                                    .firstName("amit")
//                                    .lastName("dewal")
//                                    .email("adewal@gmail.com")
//                                    .build();
        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    //Junit test for get all employees operation
    @DisplayName("Junit test for get all employees operation")
    @Test
    public void givenEmployeeList_whenFinalAll_thenEmployeeList(){

        //given - precondtion or setup
//        Employee employeeOne = Employee.builder()
//                .firstName("Sachin")
//                .lastName("Tendulkar")
//                .email("Sachin@gmail.com")
//                .build();

        Employee employeeTwo = Employee.builder()
                .firstName("MS")
                .lastName("Dhoni")
                .email("Dhoni@gmail.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employeeTwo);

        // when - action or behaviour that we are going to
        List<Employee> employeeList = employeeRepository.findAll();
        System.out.println(employeeList.size());
        System.out.println(employeeList);
        //then - verify the output
        assertThat(employeeList).isNotNull();
       assertThat(employeeList).hasSize(2);

    }


 //Junit test for get employee by id operation
    @DisplayName("Junit test for get employee by id operation")
     @Test
     public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject(){

         //given - precondtion or setup
//         Employee employee = Employee.builder()
//                                         .firstName("Sachin")
//                                         .lastName("Tendulkar")
//                                         .email("Sachin@gmail.com")
//                                         .build();

//         Employee employeeTwo = Employee.builder()
//                                         .firstName("MS")
//                                         .lastName("Dhoni")
//                                         .email("Dhoni@gmail.com")
//                                         .build();
         employeeRepository.save(employee);
        // employeeRepository.save(employeeOne);

         // when - action or behaviour that we are going to test
         Employee employeeDB = employeeRepository.findById(employee.getId()).get();



         //then - verify the output
         assertThat(employeeDB).isNotNull();
     }

 //Junit test for get employee by email operation
    @DisplayName("Junit test for get employee by email operation")
     @Test
     public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject(){

         //given - precondtion or setup
//         Employee employee = Employee.builder()
//                 .firstName("Sachin")
//                 .lastName("Tendulkar")
//                 .email("Sachin@gmail.com")
//                 .build();
        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println(savedEmployee);

        // when - action or behaviour that we are going to test
         Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

         //then - verify the output

        assertThat(employeeDB).isNotNull();
    }

     //Junit test for update employee operation
         @DisplayName("Junit test for update employee operation")
         @Test
         public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

             //given - precondtion or setup
//             Employee employee = Employee.builder()
//                     .firstName("Sachin")
//                     .lastName("Tendulkar")
//                     .email("Sachin@gmail.com")
//                     .build();
             employeeRepository.save(employee);

             // when - action or behaviour that we are going to test

             Employee saveEmployee = employeeRepository.findById(employee.getId()).get();

             saveEmployee.setEmail("Ramesh@gmail.com");
             saveEmployee.setFirstName("Ramesh");
             Employee updatedEmployee = employeeRepository.save(saveEmployee);

             //then - verify the output
             assertThat(updatedEmployee.getEmail()).isEqualTo(saveEmployee.getEmail());
             assertThat(updatedEmployee.getFirstName()).isEqualTo(saveEmployee.getFirstName());
         }


          //Junit test for delete employee operation
              @DisplayName("Junit test for delete employee operation")
              @Test
              public void givenEmployeeObject_whenDelete_thenEmployee(){

                  //given - precondtion or setup
//                  Employee employee = Employee.builder()
//                          .firstName("Sachin")
//                          .lastName("Tendulkar")
//                          .email("Sachin@gmail.com")
//                          .build();
                  employeeRepository.save(employee);

                  // when - action or behaviour that we are going to test

                  //employeeRepository.delete(employeeOne);
                  employeeRepository.deleteById(employee.getId());
                  Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

                  //then - verify the output
                  assertThat(employeeOptional).isEmpty();

              }

               //Junit test for custom query using jpql with index
                   @Test
                   @DisplayName("Junit test for custom query using jpql with index")
                   public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject(){

                       //given - precondtion or setup
//                       Employee employee = Employee.builder()
//                               .firstName("Sachin")
//                               .lastName("Tendulkar")
//                               .email("Sachin@gmail.com")
//                               .build();
                       //save to db
                       employeeRepository.save(employee);
//                       String firstName = "Sachin";
//                       String lastName = "Tendulkar";

                       // when - action or behaviour that we are going to test

                       Employee savedEmployee = employeeRepository.findByJPQL(employee.getFirstName(), employee.getLastName());

                       //then - verify the output
                       assertThat(savedEmployee).isNotNull();
                   }

    //Junit test for custom query using jpql with named params
    @Test
    @DisplayName("Junit test for custom query using jpql with named params")
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject(){

        //given - precondtion or setup
//        Employee employee = Employee.builder()
//                .firstName("Sachin")
//                .lastName("Tendulkar")
//                .email("Sachin@gmail.com")
//                .build();
        //save to db
        employeeRepository.save(employee);
//        String firstName = "Sachin";
//        String lastName = "Tendulkar";

        // when - action or behaviour that we are going to test

        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(employee.getFirstName(), employee.getLastName());

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

     //Junit test for custom query using native SQL with index
    @DisplayName("Junit test for custom query using native SQL with index")
         @Test
         public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject(){

             //given - precondtion or setup
//             Employee employee = Employee.builder()
//                     .firstName("Sachin")
//                     .lastName("Tendulkar")
//                     .email("Sachin@gmail.com")
//                     .build();
             //save to db
             employeeRepository.save(employee);

             // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        //then - verify the output

             assertThat(savedEmployee).isNotNull();
         }
    //Junit test for custom query using native SQL with named params
    @DisplayName("Junit test for custom query using native SQL with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject(){

        //given - precondtion or setup
//        Employee employee = Employee.builder()
//                .firstName("Sachin")
//                .lastName("Tendulkar")
//                .email("Sachin@gmail.com")
//                .build();
        //save to db
        employeeRepository.save(employee);

        // when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstName(), employee.getLastName());

        //then - verify the output

        assertThat(savedEmployee).isNotNull();
    }

}
