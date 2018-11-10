package com.tks.gwa.repository.repositoryImpl;

import com.tks.gwa.entity.Role;
import com.tks.gwa.repository.RoleRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;

@Repository
public class RoleRepositoryImpl extends GenericRepositoryImpl<Role, Integer> implements RoleRepository {
    public RoleRepositoryImpl() {
        super(Role.class);
    }

    @Override
    public Role getRoleByName(String roleName) {
        String sql = "SELECT r FROM " + Role.class.getName() + " AS r WHERE r.name=:name";
        Query query = this.entityManager.createQuery(sql);
        query.setParameter("name", roleName);

        return (Role) query.getSingleResult();
    }
}
