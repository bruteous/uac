package com.elitethought.repository;

import com.elitethought.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

/**
 * Created by chenm on 7/05/2014.
 */
public interface RoleRepository extends JpaRepository<Role, Serializable> {
    Role findRoleByRoleName(String roleName);
}
