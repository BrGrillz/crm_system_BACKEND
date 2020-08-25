package com.aegis.crmsystem.models;

import com.aegis.crmsystem.domain.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table
@Data
public class TaskStatus {
    @JsonView({Views.Message.class})
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView({Views.Message.class})
    @Column(name = "status")
    private String status;

    @JsonView({Views.Message.class})
    @Column(name = "name")
    private String name;
}
