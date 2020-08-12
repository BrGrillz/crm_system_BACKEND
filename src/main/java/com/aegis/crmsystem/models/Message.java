//package com.aegis.crmsystem.models;
//
//import com.aegis.crmsystem.domain.Views;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonView;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//import lombok.ToString;
//
//import javax.persistence.*;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Table
//@ToString(of = {"id", "text"})
//@EqualsAndHashCode(of = {"id"})
//@Data
//public class Message {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    private String text;
//
//    @Column(updatable = false)
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonView(Views.FullMessage.class)
//    private LocalDateTime creationDate;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    @JsonView(Views.FullMessage.class)
//    private User author;
//
//    @OneToMany(mappedBy = "message", orphanRemoval = true)
//    @JsonView(Views.FullMessage.class)
//    private List<Comment> comments;
//
////    @JsonView(Views.FullMessage.class)
////    private String link;
////    @JsonView(Views.FullMessage.class)
////    private String linkTitle;
////    @JsonView(Views.FullMessage.class)
////    private String linkDescription;
////    @JsonView(Views.FullMessage.class)
////    private String linkCover;
//}