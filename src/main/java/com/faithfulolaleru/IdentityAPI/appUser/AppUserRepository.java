package com.faithfulolaleru.IdentityAPI.appUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AppUserRepository extends JpaRepository<AppUserEntity, Long> {

    Optional<AppUserEntity> findByEmail(String email);

    Optional<AppUserEntity> findByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE app_users a SET a.isActive = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}
