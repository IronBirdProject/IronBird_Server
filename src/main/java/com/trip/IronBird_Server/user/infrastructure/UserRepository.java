package com.trip.IronBird_Server.user.infrastructure;

import com.trip.IronBird_Server.user.domain.entity.User;
import com.trip.IronBird_Server.user.domain.modeltype.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<User> findByOauthIdAndOauthType(String oauthId, OauthType oauthType);
}
