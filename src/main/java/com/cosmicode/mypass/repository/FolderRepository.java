package com.cosmicode.mypass.repository;

import com.cosmicode.mypass.domain.Folder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Folder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

    @Query("select folder from Folder folder join fetch folder.secrets where folder.owner.login = ?#{principal.username}")
    List<Folder> findByOwnerIsCurrentUser();

    @Query(value = "select distinct folder from Folder folder left join fetch folder.sharedWiths",
        countQuery = "select count(distinct folder) from Folder folder")
    Page<Folder> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct folder from Folder folder left join fetch folder.sharedWiths")
    List<Folder> findAllWithEagerRelationships();

    @Query("select folder from Folder folder left join fetch folder.sharedWiths where folder.id =:id")
    Optional<Folder> findOneWithEagerRelationships(@Param("id") Long id);

}
