package com.aegis.crmsystem.models;

import com.aegis.crmsystem.domain.Views;
import com.aegis.crmsystem.constants.SwaggerConst;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Message.class)
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.ID)
    private Long id;

    @JsonView(Views.Message.class)
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TITLE)
    private String title;

    @JsonView(Views.Message.class)
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private String text;

//    @OneToMany
//    @JoinColumn(name = "comment_id", nullable = false, updatable = false)
//    private Comment comment;

    @ManyToOne
    @JsonView(Views.Message.class)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private User author;

    @ManyToOne
    @JsonView(Views.Message.class)
    @JoinColumn(name = "responsible_id", nullable = false)
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private User responsible;

    @ManyToOne
    @JsonView(Views.Message.class)
    @JoinColumn(name = "observer_id")
    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.TEXT)
    private User observer;

//    @Column(updatable = false)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(Views.FullMessage.class)
//    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.CREATE_DATE)
//    private LocalDateTime creat_date;
//
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(Views.FullMessage.class)
//    @ApiModelProperty(notes = SwaggerConst.Tasks.Model.UPDATE_DATE)
//    private LocalDateTime update_date;
}
