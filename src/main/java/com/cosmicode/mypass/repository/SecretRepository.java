package com.cosmicode.mypass.repository;

import com.cosmicode.mypass.domain.Secret;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Secret entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {

    @Query("select secret from Secret secret where secret.owner.login = ?#{principal.username}")
    List<Secret> findByOwnerIsCurrentUser();

    @Query("select secret from Secret secret where secret.folder.id = :id")
    List<Secret> findByFolderId(@Param("id") Long id);
}
