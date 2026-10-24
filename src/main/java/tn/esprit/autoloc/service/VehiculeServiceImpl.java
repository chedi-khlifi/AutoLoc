package tn.esprit.autoloc.service;

import static tn.esprit.autoloc.service.Validations.exigerPositifOuNul;
import static tn.esprit.autoloc.service.Validations.exigerPresent;
import static tn.esprit.autoloc.service.Validations.exigerTexte;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.IVehiculeRepository;

@Service
@RequiredArgsConstructor
public class VehiculeServiceImpl implements IVehiculeService {

    private final IVehiculeRepository vehiculeRepository;

    @Override
    @Transactional
    public Vehicule create(Vehicule vehicule) {
        if (vehicule.getIdVehicule() != null) {
            throw new IllegalArgumentException(
                    "Un nouveau véhicule ne doit pas avoir d'identifiant");
        }
        verifierRegles(vehicule);
        return vehiculeRepository.save(vehicule);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return vehiculeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return vehiculeRepository.findAll();
    }

    @Override
    @Transactional
    public Vehicule update(Long id, Vehicule vehicule) {
        Vehicule existant = findById(id);
        verifierRegles(vehicule);
        existant.setImmatriculation(vehicule.getImmatriculation());
        existant.setMarque(vehicule.getMarque());
        existant.setModele(vehicule.getModele());
        existant.setCategorie(vehicule.getCategorie());
        existant.setTarifJournalier(vehicule.getTarifJournalier());
        existant.setStatut(vehicule.getStatut());
        existant.setAgence(vehicule.getAgence());
        return vehiculeRepository.save(existant);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!vehiculeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicule", id);
        }
        vehiculeRepository.deleteById(id);
    }

    private void verifierRegles(Vehicule vehicule) {
        exigerTexte(vehicule.getImmatriculation(), "L'immatriculation");
        exigerTexte(vehicule.getMarque(), "La marque");
        exigerTexte(vehicule.getModele(), "Le modèle");
        exigerPresent(vehicule.getCategorie(), "La catégorie");
        exigerPresent(vehicule.getStatut(), "Le statut");
        exigerPositifOuNul(vehicule.getTarifJournalier(), "Le tarif journalier");
    }
}
