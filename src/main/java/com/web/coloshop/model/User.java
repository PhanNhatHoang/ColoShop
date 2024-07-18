package com.web.coloshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ID")
    private Long id;
    private String fullname;
    private String Phone;
    private String Address;
    private String email;
    private String pass;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn( name = "role_ID",referencedColumnName = "role_ID")
    private Role role;
    private boolean is_deleted;
    private boolean is_activated;
}
