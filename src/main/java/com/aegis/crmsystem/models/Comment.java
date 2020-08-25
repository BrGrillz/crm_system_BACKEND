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
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseEntity{

    @JsonView({Views.Message.class})
    @JoinColumn(name = "text", nullable = false)
    private String text;

    @JsonView({Views.Message.class})
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
