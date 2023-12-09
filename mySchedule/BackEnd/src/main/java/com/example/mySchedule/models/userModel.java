package com.example.mySchedule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter                     //Lombok: Todos los getters
@Setter                     //Lombok: Todos los setters
@NoArgsConstructor          //Lombok: Constructor sin argumentos
@AllArgsConstructor         //Lombok: Constructor con todos los argumentos
@CrossOrigin(origins = "*") //Acepta peticiones desde cualquier origen
@Table(name="Users")
public class userModel implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @Column(name = "Nif", unique = true)
    private String nif;

    @Column(name="Nombre")
    private String name;

    @Column(name="Apellido1")
    private String surname1;

    @Column(name="Apellido2")
    private String surname2;

    @Column(name="Alias")
    private String alias;

    @Column(name="Email")
    private String email;

    @Column(name="Telefono")
    private String phone;

    @Column(name="Password")
    private String password;

    @Column(name = "Rol")
    @Enumerated(EnumType.STRING)
    private UserType rol;

    @Column(name="Notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name="Price")
    private int price;

    @OneToMany(mappedBy = "userID",cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("userID")
    private List<appointmentModel> appointmentsList;

    //Si el usuario no tiene un rol asignado previamente, se le asigna el rol "Usuario" por defecto
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String myRol=UserType.Usuario.toString();
        if(this.getRol()!=null){
            myRol= this.getRol().toString();
        }
        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(myRol));
        return roles;
    }

    //Usare el nif como identificador del User de UserDetails, porque es unico
    @JsonIgnore
    @Override
    public String getUsername() {
        return this.getNif();
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum UserType
    {
        Admin,
        Usuario
    }
}
