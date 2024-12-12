package com.ainouss.datatools.jdatatools.query.model;

import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "EMPLOYEES")
public class Employee {

    @Column(name = "ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String name;

    @Column(name = "AGE")
    private int age;

    @Column(name = "ENABLED")
    private String enabled;

}
