package com.aegis.crmsystem.models;

import com.aegis.crmsystem.domain.Views;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@AllArgsConstructor
public class User extends BaseEntity implements Serializable{

    @JsonView(Views.Message.class)
    @Column(name = "first_name")
    private String firstName;

    @JsonView(Views.Message.class)
    @Column(name = "last_name")
    private String lastName;

    @JsonView(Views.Message.class)
    @Column(name = "email")
    private String email;

    @JsonIgnore
    @Column(name = "password")
    private String password;

//    @ManyToOne
//    @JoinColumn(name = "user_role_id", nullable = false)
//    private UserRole user_role_id;

//    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
//    @Enumerated(EnumType.STRING)
//    private Set<UserRole> roles;

    @JsonView(Views.Message.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @UpdateTimestamp
    @JsonView(Views.Message.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

//    @OneToMany(cascade = CascadeType.ALL,
//            fetch = FetchType.LAZY,
//            mappedBy = "user")
//    private Set<Task> task = new HashSet<>();

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
