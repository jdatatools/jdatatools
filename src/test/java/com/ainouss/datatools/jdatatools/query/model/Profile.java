package com.ainouss.datatools.jdatatools.query.model;


import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "PROFILES")
public class Profile {

    @Column(name = "ID")
    private Long id;

    @Column(name = "VALUE")
    private Long value;

}
