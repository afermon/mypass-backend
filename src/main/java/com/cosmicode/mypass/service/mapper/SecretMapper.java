package com.cosmicode.mypass.service.mapper;

import com.cosmicode.mypass.domain.*;
import com.cosmicode.mypass.service.dto.SecretDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Secret and its DTO SecretDTO.
 */
@Mapper(componentModel = "spring", uses = {FolderMapper.class, UserMapper.class})
public interface SecretMapper extends EntityMapper<SecretDTO, Secret> {

    @Mapping(source = "folder.id", target = "folderId")
    @Mapping(source = "folder.name", target = "folderName")
    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    SecretDTO toDto(Secret secret);

    @Mapping(source = "folderId", target = "folder")
    @Mapping(source = "ownerId", target = "owner")
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
