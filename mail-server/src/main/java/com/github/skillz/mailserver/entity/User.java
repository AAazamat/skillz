package com.github.skillz.mailserver.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "users_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String secret;
}
