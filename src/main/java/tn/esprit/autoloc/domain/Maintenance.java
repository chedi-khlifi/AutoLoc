package tn.esprit.autoloc.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Maintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal cout;
    private String description;

    @ManyToOne
    private Vehicule vehicule;
}
