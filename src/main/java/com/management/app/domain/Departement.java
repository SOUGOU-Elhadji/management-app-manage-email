package com.management.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Departement.
 */
@Entity
@Table(name = "departement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Departement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_departement")
    private String name_departement;

    // @OneToMany(mappedBy = "departement")
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    // @JsonIgnoreProperties(value = { "departement" }, allowSetters = true)
    // private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    // @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    // @JsonIgnoreProperties(value = { "departement" }, allowSetters = true)
    private List<Post> posts = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Departement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName_departement() {
        return this.name_departement;
    }

    public Departement name_departement(String name_departement) {
        this.setName_departement(name_departement);
        return this;
    }

    public void setName_departement(String name_departement) {
        this.name_departement = name_departement;
    }

    // public Set<Post> getPosts() {
    //     return this.posts;
    // }

    // public void setPosts(Set<Post> posts) {
    //     if (this.posts != null) {
    //         this.posts.forEach(i -> i.setDepartement(null));
    //     }
    //     if (posts != null) {
    //         posts.forEach(i -> i.setDepartement(this));
    //     }
    //     this.posts = posts;
    // }

    // public Departement posts(Set<Post> posts) {
    //     this.setPosts(posts);
    //     return this;
    // }

    

    public Departement addPost(Post post) {
        this.posts.add(post);
        post.setDepartement(this);
        return this;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Departement removePost(Post post) {
        this.posts.remove(post);
        post.setDepartement(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departement)) {
            return false;
        }
        return id != null && id.equals(((Departement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Departement{" +
            "id=" + getId() +
            ", name_departement='" + getName_departement() + "'" +
            "}";
    }
}
