package com.aegis.crmsystem.models;

import com.aegis.crmsystem.domain.Views;
import com.fasterxml.jackson.annotation.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class User extends BaseEntity {

    @JsonView({Views.Message.class})
    @Column(name = "first_name")
    private String firstName;

    @JsonView({Views.Message.class})
    @Column(name = "last_name")
    private String lastName;

    @JsonView({Views.Message.class})
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password", unique = true)
    private String password;

    @ManyToOne
    @JsonView({Views.Message.class})
    @JoinColumn(name = "avatar")
    private File avatar;
//    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
//    @Enumerated(EnumType.STRING)
//    private Set<UserRole> roles;

    @JsonView({Views.Message.class})
    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Role> roles;

    @JsonView({Views.Message.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

//    @JsonView(Views.Message.class)
//    @ManyToMany(mappedBy = "observers")
//    @JsonBackReference
//    @LazyCollection(LazyCollectionOption.FALSE)
//    private List<Task> tasks;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(Views.FullMessage.class)
//    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.UPDATE_DATE)
//    private LocalDateTime update_date;
//
//    @Column(updatable = false)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(Views.FullMessage.class)
//    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.CREATE_DATE)
//    private LocalDateTime creat_date;
}
