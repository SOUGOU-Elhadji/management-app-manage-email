package com.management.app.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.management.app.domain.Departement} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DepartementDTO implements Serializable {

    private Long id;

    private String name_departement;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName_departement() {
        return name_departement;
    }

    public void setName_departement(String name_departement) {
        this.name_departement = name_departement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DepartementDTO)) {
            return false;
        }

        DepartementDTO departementDTO = (DepartementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, departementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DepartementDTO{" +
            "id=" + getId() +
            ", name_departement='" + getName_departement() + "'" +
            "}";
    }
}
