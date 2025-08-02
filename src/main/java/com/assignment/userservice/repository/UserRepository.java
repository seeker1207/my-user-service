package com.assignment.userservice.repository;

import com.assignment.userservice.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<Users,Long>, PagingAndSortingRepository<Users, Long> {

    Optional<Users> findByUserId(String userId);

    Optional<Users> findByCitizenNumber(String citizenNumber);

    Page<Users> findByBirthDateBetween(LocalDate birthDateAfter, LocalDate birthDateBefore, Pageable pageable);

}
