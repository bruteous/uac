package com.elitethought.repository;

import com.elitethought.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by chenm on 6/05/2014.
 */
@Repository
public interface UserRepository extends JpaRepository<Account, Serializable> {

    Account findAccountByEmail(String email);

}
