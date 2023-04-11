package com.fmss.userservice.repository;

import com.fmss.userservice.repository.model.LdapUser;
import lombok.RequiredArgsConstructor;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import javax.naming.Name;
import javax.naming.directory.*;
import java.util.List;
import java.util.Optional;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Repository
@RequiredArgsConstructor
public class LdapRepository {
    private static final String BASE_DN = "ou=Users,o=64346c9136393df65a68908f,dc=jumpcloud,dc=com";

    private static final String PASSWORD = "userPassword";
    private static final String FIRST_NAME = "givenName";
    private static final String LAST_NAME = "sn";


    private static final String USERNAME_ATTRIBUTE = "uid";
    private static final String FULLNAME_ATTRIBUTE = "cn";
    private static final String EMAIL_ATTRIBUTE = "mail";
    public static final String USER_PASSWORD_ATTRIBUTE = "userpassword";
    public static final String LDAP_USER = "user";


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

        return Optional.ofNullable(ldapTemplate.search(BASE_DN, searchFilter, convert())).filter(ldapUsers -> !CollectionUtils.isEmpty(ldapUsers)).map(ldapUsers -> ldapUsers.get(0)).orElse(null);
    }

    private AttributesMapper<LdapUser> convert() {
        return (Attributes attributes) -> {
            LdapUser user = new LdapUser();
            user.setUserPrincipalName((String) attributes.get(USERNAME_ATTRIBUTE).get());
            user.setMail((String) attributes.get(EMAIL_ATTRIBUTE).get());
            user.setUserPassword(attributes.get(USER_PASSWORD_ATTRIBUTE).get().toString());
            return user;
        };
    }

    public void create(LdapUser p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public List<LdapUser> findAll() {
        EqualsFilter filter = new EqualsFilter("objectclass", LDAP_USER);
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), new PersonContextMapper());
    }

    public LdapUser findOne(String email) {
        Name dn = LdapNameBuilder.newInstance(BASE_DN).add("ou", LDAP_USER).add(EMAIL_ATTRIBUTE, email).build();
        return ldapTemplate.lookup(dn, new PersonContextMapper());
    }

    public List<LdapUser> findByName(String name) {
        LdapQuery q = query().where("objectclass").is(LDAP_USER).and(FULLNAME_ATTRIBUTE).whitespaceWildcardsLike(name);
        return ldapTemplate.search(q, new PersonContextMapper());
    }

    public void update(LdapUser p) {
        ldapTemplate.rebind(buildDn(p), null, buildAttributes(p));
    }

    public void updateLastName(LdapUser p) {
        Attribute attr = new BasicAttribute(LAST_NAME, p.getSn());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(buildDn(p), new ModificationItem[]{item});
    }

    public void delete(LdapUser p) {
        ldapTemplate.unbind(buildDn(p));
    }

    private Name buildDn(LdapUser p) {
        return LdapNameBuilder.newInstance(BASE_DN).add("ou", LDAP_USER).add(EMAIL_ATTRIBUTE, p.getMail()).build();
    }

    private Attributes buildAttributes(LdapUser p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocAttr = new BasicAttribute("objectclass");
        ocAttr.add("top");
        ocAttr.add(LDAP_USER);
        attrs.put(ocAttr);
        attrs.put(EMAIL_ATTRIBUTE, p.getMail());
        attrs.put(PASSWORD, p.getUserPassword());
        attrs.put(FIRST_NAME, p.getGivenName());
        attrs.put(LAST_NAME, p.getSn());
        return attrs;
    }


    private static class PersonContextMapper extends AbstractContextMapper<LdapUser> {
        public LdapUser doMapFromContext(DirContextOperations context) {
            LdapUser person = new LdapUser();
            person.setUserPrincipalName(context.getStringAttribute(EMAIL_ATTRIBUTE));
            person.setMail(context.getStringAttribute(EMAIL_ATTRIBUTE));
            person.setUserPassword(context.getStringAttribute(PASSWORD));
            person.setGivenName(context.getStringAttribute(FIRST_NAME));
            person.setSn(context.getStringAttribute(LAST_NAME));
            return person;
        }
    }
}
