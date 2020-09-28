package com.aegis.crmsystem.models;

import com.aegis.crmsystem.domain.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "files")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseEntity{

    @JsonView(Views.Message.class)
    @Column(name = "name")
    private String name;

    @JsonView(Views.Message.class)
    @Column(name = "uuid")
    private String uuid;

    @JsonView(Views.Message.class)
    @Column(name = "download")
    private String download;

    @JsonView(Views.Message.class)
    @Column(name = "size")
    private Long size;

    @JsonView(Views.Message.class)
    @Column(name = "path")
    private String path;

    @JsonView(Views.Message.class)
    @Column(name = "format")
    private String format;
}