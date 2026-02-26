package com.nimblesoftwares.ttrilha_api.adapter.out.persistence.user.interfaces;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.user.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {
}
