package com.spd.repository;

import com.spd.entity.UserTelephone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTelephoneRepository extends JpaRepository<UserTelephone, Integer> {

    Optional<UserTelephone> findByUserIdAndTelephone(int userId, String telephone);

    Optional<UserTelephone> findOneById(int id);

    List<UserTelephone> findByUserId(int id);
}
