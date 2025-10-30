package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(
            targetEntity = Authority.class,
            mappedBy = "username",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_materials",
            joinColumns = @JoinColumn(name = "username"),
            inverseJoinColumns = @JoinColumn(name = "materials_id")
    )
    private Set<Material> bookmarkedMaterials = new HashSet<>();

    public User() {
    }

    public User(String username, String email, String password, Set<Authority> authorities) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(Authority authority) {
        this.authorities.add(authority);
    }

    public void removeAuthority(Authority authority) {
        this.authorities.remove(authority);
    }

    public void addMaterial(Material material) {
        bookmarkedMaterials.add(material);
    }

    public void removeMaterial(Material material) {
        bookmarkedMaterials.remove(material);
    }

    public Set<Material> getBookmarkedMaterials() {
        return bookmarkedMaterials;
    }
}
