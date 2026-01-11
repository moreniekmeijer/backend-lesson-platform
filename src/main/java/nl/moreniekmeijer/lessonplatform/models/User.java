package nl.moreniekmeijer.lessonplatform.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String password;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private Set<Authority> authorities = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_materials",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "materials_id")
    )
    private Set<Material> bookmarkedMaterials = new HashSet<>();

    public User() {
    }

    public User(Long id, String email, String fullName, String password, Set<Authority> authorities) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void addAuthority(String role) {
        authorities.add(new Authority(this, role));
    }

    public void removeAuthority(Authority authority) {
        authorities.remove(authority);
        authority.setUser(null);
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
