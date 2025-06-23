package com.cliptripbe.feature.user.domain;

import com.cliptripbe.feature.user.domain.type.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String userId;
    private String password;
    protected String nickName;
    //    private Language language;
//    private Age age;
    @Enumerated(EnumType.STRING)
    private Role role;

    public String getRole() {
        return role.getRole();
    }

    @Builder
    public User(String userId, String password, String nickName) {
        this.userId = userId;
        this.password = password;
        this.nickName = nickName;
//        this.language = language;
//        this.age = age;
        this.role = Role.USER;
    }
}
