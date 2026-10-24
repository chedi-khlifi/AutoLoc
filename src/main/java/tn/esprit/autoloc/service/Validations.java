package tn.esprit.autoloc.service;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Règles de gestion communes aux services. Chaque méthode lève une
 * IllegalArgumentException au message explicite si la règle est violée.
 */
final class Validations {

    private Validations() {
    }

    static void exigerTexte(String valeur, String champ) {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException(champ + " est obligatoire");
        }
    }

    static void exigerPresent(Object valeur, String champ) {
        if (valeur == null) {
            throw new IllegalArgumentException(champ + " est obligatoire");
        }
    }

    static void exigerPositifOuNul(BigDecimal valeur, String champ) {
        exigerPresent(valeur, champ);
        if (valeur.signum() < 0) {
            throw new IllegalArgumentException(champ + " ne peut pas être négatif");
        }
    }

    static void exigerFinNonAvantDebut(LocalDate debut, LocalDate fin) {
        if (debut != null && fin != null && fin.isBefore(debut)) {
            throw new IllegalArgumentException(
                    "La date de fin ne peut pas être antérieure à la date de début");
        }
    }
}
