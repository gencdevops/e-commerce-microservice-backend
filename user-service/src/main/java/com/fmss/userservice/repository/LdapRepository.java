package com.fmss.userservice.repository;

import com.fmss.userservice.model.enums.UserStatus;
import com.fmss.userservice.repository.model.LdapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.naming.directory.Attributes;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LdapRepository {
    private static final String BASE_DN = "ou=Users,o=64346c9136393df65a68908f,dc=jumpcloud,dc=com";

    private static final String USERNAME_ATTRIBUTE = "uid";
    private static final String FULLNAME_ATTRIBUTE = "cn";
    private static final String EMAIL_ATTRIBUTE = "mail";
    public static final String USER_PASSWORD_ATTRIBUTE = "userpassword";
    public static final String STATUS_ATTRIBUTE = "status";

    private final LdapTemplate ldapTemplate;

//    @PostConstruct
//    public void init() {
//        LdapUser mahoni = findUser("mahoni");
//    }

    public boolean checkPassword(String username, String password) {
        try {
            final var searchFilter = "(" + USERNAME_ATTRIBUTE + "=" + username + ")";
            ldapTemplate.authenticate(BASE_DN, searchFilter, password);
            return true;
        } catch (Exception ex) {
            //TODO
        }
        return false;
    }

    public LdapUser findUser(String username) {
        final var searchFilter = "(" + USERNAME_ATTRIBUTE + "=" + username + ")";

        return Optional.ofNullable(ldapTemplate.search(BASE_DN, searchFilter, convert()))
                .filter(ldapUsers -> !CollectionUtils.isEmpty(ldapUsers))
                .map(ldapUsers -> ldapUsers.get(0))
                .orElse(null);
    }

    private AttributesMapper<LdapUser> convert() {
        return (Attributes attributes) -> {
            LdapUser user = new LdapUser();
            user.setUsername((String) attributes.get(USERNAME_ATTRIBUTE).get());
            user.setFullName((String) attributes.get(FULLNAME_ATTRIBUTE).get());
            user.setEmail((String) attributes.get(EMAIL_ATTRIBUTE).get());
            user.setPassword(attributes.get(USER_PASSWORD_ATTRIBUTE).get().toString());
            user.setStatus(UserStatus.ACTIVE);
            return user;
        };
    }
}
