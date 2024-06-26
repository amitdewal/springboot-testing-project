package com.start.model;

import jakarta.persistence.*;
import lombok.*;
@ToString
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Column(name = "first_name",nullable = false)
    private  String firstName;
    @Column(name = "last_name",nullable = false)
    private String lastName;
    private String email;
}
