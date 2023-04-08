package com.fmss.userservice.model.entity;

import com.fmss.commondata.model.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractEntity {

    @Column(nullable = false, unique = true)
    protected String userName;

    @Column(nullable = false, unique = true)
    protected String birthDate;

    @Email
    @Size(max = 100)
    @Column(name = "email", nullable = false, unique = true)
    protected String email;

    @Column
    @NotNull
    @Size(max = 128)
    protected String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public String generateCreatePasswordToken() {
        return DigestUtils.md5Hex("CreatePassword|" + getEmail() + "|" + getUserName() + "|" + password);
    }

    public String generateResetPasswordToken() {
        return DigestUtils.md5Hex("Reset|" + getEmail() + "|" + getUserName() + "|" + password);
    }
}
