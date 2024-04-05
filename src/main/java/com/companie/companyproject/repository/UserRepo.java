package com.companie.companyproject.repository;

import com.companie.companyproject.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findFirstByEmail(String email);
}
