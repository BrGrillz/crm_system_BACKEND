package com.aegis.crmsystem.models;
//import org.springframework.security.core.GrantedAuthority;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String role;

    @ManyToMany(mappedBy = "roles")
    private List<User> users;

    @Override
    public String toString() {
        return "Role{" +
                "id: " + getId() + ", " +
                "name: " + role + "}";
    }
}