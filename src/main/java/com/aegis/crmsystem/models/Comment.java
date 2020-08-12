//package com.aegis.crmsystem.models;
//
//import com.aegis.crmsystem.domain.Views;
//import com.fasterxml.jackson.annotation.JsonView;
//import lombok.Data;
//import lombok.EqualsAndHashCode;
//
//import javax.persistence.*;
//
//@Entity
//@Table
//@Data
//@EqualsAndHashCode(of = { "id" })
//public class Comment {
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private String text;
//
//    @ManyToOne
//    @JoinColumn(name = "message_id")
//    private Message message;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id", nullable = false, updatable = false)
//    @JsonView(Views.FullMessage.class)
//    private User author;
//}
