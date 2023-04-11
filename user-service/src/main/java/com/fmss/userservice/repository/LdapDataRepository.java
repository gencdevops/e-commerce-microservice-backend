package com.fmss.userservice.repository;

import com.fmss.userservice.repository.model.LdapUser;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LdapDataRepository extends LdapRepository<LdapUser> {
    LdapUser findByMail(String mail);
}
