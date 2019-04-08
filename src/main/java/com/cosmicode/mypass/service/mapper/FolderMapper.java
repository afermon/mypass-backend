package com.cosmicode.mypass.service.mapper;

import com.cosmicode.mypass.domain.*;
import com.cosmicode.mypass.service.dto.FolderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Folder and its DTO FolderDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface FolderMapper extends EntityMapper<FolderDTO, Folder> {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner.login", target = "ownerLogin")
    FolderDTO toDto(Folder folder);

    @Mapping(source = "ownerId", target = "owner")
    @Mapping(target = "secrets", ignore = true)
    Folder toEntity(FolderDTO folderDTO);

    default Folder fromId(Long id) {
        if (id == null) {
            return null;
        }
        Folder folder = new Folder();
        folder.setId(id);
        return folder;
    }
}
