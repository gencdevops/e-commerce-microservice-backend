package com.fmss.userservice.model.entity;

import com.fmss.userservice.model.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "users")
public class User  {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    protected String firstName;

    protected String lastName;

    protected String birthDate;

    protected String userName;

    @Email
    @Size(max = 100)
    @Column(name = "email")
    protected String email;

    @Column
    @Size(max = 128)
    protected String password;

//    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "role",
//            joinColumns = @JoinColumn(
//                    name = "user_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(
//                    name = "role_id", referencedColumnName = "id"))
    //private Collection<Role> roles;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column
    @Size(max = 128)
    protected String beforePassword;

    @CreationTimestamp
    @Column(updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDateTime;

    @Column(columnDefinition = "TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime changeDayLastTime;

    public String getUserName() {
        return getFirstName() + getLastName();
    }

    public String generateCreatePasswordToken() {
        return DigestUtils.md5Hex("CreatePassword|" + getEmail() + "|" + getUserName() + "|" + password);
    }

    public String generateResetPasswordToken() {
        return DigestUtils.md5Hex("Reset|" + getEmail() + "|" + getUserName() + "|" + password);
    }
}
