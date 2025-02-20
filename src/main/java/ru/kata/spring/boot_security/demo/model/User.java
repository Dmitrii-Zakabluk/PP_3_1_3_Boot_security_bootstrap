package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Setter
@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Getter
    @NotBlank(message = "The Name field must not be empty ")
    @Size(min = 2, max = 30, message = "The name must contain from 2 to 30 characters")
    @Column(name = "name")
    private String name;

    @Getter
    @NotBlank(message = "The Surname field must not be empty ")
    @Size(min = 2, max = 30, message = "The Surname must contain from 2 to 30 characters")
    @Column(name = "surname")
    private String surname;

    @Getter
    @Min(value = 1, message = "The Department must contain from 2 to 30 characters")
    @Column(name = "age")
    private int age;

    @Getter
    @Column(name = "email")
    private String email;

    //Создайте класс Role и свяжите User с ролями так, чтобы юзер мог иметь несколько ролей.
    @Getter
    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")

    )
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String username,
                String password,
                String name,
                String surname,
                int age,
                String email,
                Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
