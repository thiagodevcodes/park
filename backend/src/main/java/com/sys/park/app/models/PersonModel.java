package com.sys.park.app.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "tb_person")
public class PersonModel {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", length = 255, nullable = true, unique = true)
    private String email;

    @Column(name = "cpf", length = 255, nullable = true, unique = true)
    private String cpf;

    @Column(name = "phone", length = 255, nullable = true)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updetedAt;

    public PersonModel(String name, String cpf, String email, String phone) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.phone = phone;
    }

    public PersonModel(String name) {
        this.name = name;
    }
}
