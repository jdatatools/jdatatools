package com.ainouss.jdatatools.query.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "EMPLOYEE_DETAILS")
public class EmployeeDetails {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "EMPLOYEE_ID")
    private Integer employeeId;

    @Column(name = "DEPARTMENT_ID")
    private Integer departmentId;
}
