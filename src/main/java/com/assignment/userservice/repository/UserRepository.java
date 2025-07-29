package com.assignment.userservice.repository;

import com.assignment.userservice.entity.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users,Long> {

    Optional<Users> findByUserId(String userId);

    Optional<Users> findByCitizenNumber(String citizenNumber);

}
