package com.aegis.crmsystem.models;

import com.aegis.crmsystem.constants.SwaggerConst;
import com.aegis.crmsystem.domain.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity{

    @JsonView({Views.Message.class})
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TITLE)
    private String title;

    @JsonView({Views.Message.class})
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private String description;

    @JsonView({Views.Message.class})
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    @Column(name = "delete_status", columnDefinition = "boolean default false")
    private Boolean deleteStatus;

    @JsonView({Views.Message.class})
    @OneToOne
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    @JoinColumn(name = "task_status_id")
    private TaskStatus status;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinColumn(name = "task_id")
    private List<File> files;

    @JsonView({Views.FullMessage.class})
    @OneToMany(mappedBy = "task")
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> comments;

    @JsonView({Views.Message.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "due_date")
    private Date dueDate;

    @JsonView({Views.Message.class})
    @ManyToOne
    @JoinColumn(name = "author_id", updatable = false)
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private User author;

    @JsonView({Views.Message.class})
    @ManyToOne
    @JoinColumn(name = "responsible_id")
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private User responsible;

    @JsonView(Views.Message.class)
    @JsonManagedReference
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    @ManyToMany
    @JoinTable(name = "task_users",
            joinColumns = {@JoinColumn(name = "task_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "users_id", referencedColumnName = "id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> observers;
}
