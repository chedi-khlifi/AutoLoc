package tn.esprit.autoloc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Agence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String adresse;
    private String ville;

    @JsonIgnore
    @OneToMany(mappedBy = "agence")
    private List<Employe> employes = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "agence")
    private List<Vehicule> vehicules = new ArrayList<>();
}
