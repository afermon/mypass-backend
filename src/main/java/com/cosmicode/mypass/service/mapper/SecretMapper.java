package com.cosmicode.mypass.service.mapper;

import com.cosmicode.mypass.domain.*;
import com.cosmicode.mypass.service.dto.SecretDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Secret and its DTO SecretDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, FolderMapper.class})
public interface SecretMapper extends EntityMapper<SecretDTO, Secret> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    @Mapping(source = "folder.id", target = "folderId")
    @Mapping(source = "folder.name", target = "folderName")
    SecretDTO toDto(Secret secret);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(source = "folderId", target = "folder")
    Secret toEntity(SecretDTO secretDTO);

    default Secret fromId(Long id) {
        if (id == null) {
            return null;
        }
        Secret secret = new Secret();
        secret.setId(id);
        return secret;
    }
}
