package com.cliptripbe.feature.user.domain;

import com.cliptripbe.feature.user.domain.type.AgeGroup;
import com.cliptripbe.feature.user.domain.type.Gender;
import com.cliptripbe.feature.user.domain.type.Language;
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
    private String email;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Language language;
    @Enumerated(value = EnumType.STRING)
    private AgeGroup ageGroup;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Role role;

    public String getRole() {
        return role.getRole();
    }

    @Builder
    public User(String email, String password, Language language, AgeGroup ageGroup,
        Gender gender) {
        this.email = email;
        this.password = password;
        this.language = language;
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.role = Role.USER;
    }
}
