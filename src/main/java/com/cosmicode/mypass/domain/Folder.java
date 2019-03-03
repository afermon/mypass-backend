package com.cosmicode.mypass.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Folder.
 */
@Entity
@Table(name = "folder")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "folder")
public class Folder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name", length = 250, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "icon", length = 20, nullable = false)
    private String icon;

    @NotNull
    @Size(min = 10, max = 250)
    @Column(name = "jhi_key", length = 250, nullable = false)
    private String key;

    @NotNull
    @Column(name = "created", nullable = false)
    private Instant created;

    @NotNull
    @Column(name = "modified", nullable = false)
    private Instant modified;

    @OneToMany(mappedBy = "folder")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Secret> secrets = new HashSet<>();
    @ManyToOne
    @JsonIgnoreProperties("")
    private User owner;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "folder_shared_with",
               joinColumns = @JoinColumn(name = "folders_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "shared_withs_id", referencedColumnName = "id"))
    private Set<User> sharedWiths = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Folder name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public Folder icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getKey() {
        return key;
    }

    public Folder key(String key) {
        this.key = key;
        return this;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Instant getCreated() {
        return created;
    }

    public Folder created(Instant created) {
        this.created = created;
        return this;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Instant getModified() {
        return modified;
    }

    public Folder modified(Instant modified) {
        this.modified = modified;
        return this;
    }

    public void setModified(Instant modified) {
        this.modified = modified;
    }

    public Set<Secret> getSecrets() {
        return secrets;
    }

    public Folder secrets(Set<Secret> secrets) {
        this.secrets = secrets;
        return this;
    }

    public Folder addSecrets(Secret secret) {
        this.secrets.add(secret);
        secret.setFolder(this);
        return this;
    }

    public Folder removeSecrets(Secret secret) {
        this.secrets.remove(secret);
        secret.setFolder(null);
        return this;
    }

    public void setSecrets(Set<Secret> secrets) {
        this.secrets = secrets;
    }

    public User getOwner() {
        return owner;
    }

    public Folder owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Set<User> getSharedWiths() {
        return sharedWiths;
    }

    public Folder sharedWiths(Set<User> users) {
        this.sharedWiths = users;
        return this;
    }

    public Folder addSharedWith(User user) {
        this.sharedWiths.add(user);
        return this;
    }

    public Folder removeSharedWith(User user) {
        this.sharedWiths.remove(user);
        return this;
    }

    public void setSharedWiths(Set<User> users) {
        this.sharedWiths = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Folder folder = (Folder) o;
        if (folder.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), folder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Folder{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", key='" + getKey() + "'" +
            ", created='" + getCreated() + "'" +
            ", modified='" + getModified() + "'" +
            "}";
    }
}
