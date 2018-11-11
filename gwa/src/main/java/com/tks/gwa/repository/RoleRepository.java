package com.tks.gwa.repository;

import com.tks.gwa.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends GenericRepository<Role, Integer>{
    Role getRoleByName(String roleName);
}
