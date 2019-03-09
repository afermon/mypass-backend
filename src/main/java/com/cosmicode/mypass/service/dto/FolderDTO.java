package com.cosmicode.mypass.service.dto;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the Folder entity.
 */
public class FolderDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    private String name;

    @NotNull
    @Size(min = 1, max = 20)
    private String icon;

    @NotNull
    @Size(min = 10, max = 250)
    private String key;

    @NotNull
    private Instant modified;

    private Long ownerId;

    private String ownerLogin;

    private Set<UserDTO> sharedWiths = new HashSet<>();

    private  Set<SecretDTO> secrets = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Instant getModified() {
        return modified;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long userId) {
        this.ownerId = userId;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String userLogin) {
        this.ownerLogin = userLogin;
    }

    public Set<UserDTO> getSharedWiths() {
        return sharedWiths;
    }

    public void setSharedWiths(Set<UserDTO> users) {
        this.sharedWiths = users;
    }

    public Set<SecretDTO> getSecrets() {
        return secrets;
    }

    public void setSecrets(Set<SecretDTO> secrets) {
        this.secrets = secrets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FolderDTO folderDTO = (FolderDTO) o;
        if (folderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), folderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FolderDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", key='" + getKey() + "'" +
            ", modified='" + getModified() + "'" +
            ", owner=" + getOwnerId() +
            ", owner='" + getOwnerLogin() + "'" +
            "}";
    }
}
