package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tn.esprit.autoloc.domain.Vehicule;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.VehiculeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VehiculeServiceImpl implements IVehiculeService {

    private final VehiculeRepository repository;
    private final IAgenceService agenceService;

    @Override
    @Transactional(readOnly = true)
    public List<Vehicule> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicule findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicule introuvable avec l'identifiant " + id));
    }

    @Override
    public Vehicule create(Vehicule o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Vehicule update(Long id, Vehicule o) {
        Vehicule existing = findById(id);
        validate(o);
        resolve(o);
        existing.setImmatriculation(o.getImmatriculation());
        existing.setMarque(o.getMarque());
        existing.setModele(o.getModele());
        existing.setPrixJournalier(o.getPrixJournalier());
        existing.setAgence(o.getAgence());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Vehicule o) {
        if (!StringUtils.hasText(o.getImmatriculation())) {
            throw new IllegalArgumentException("L'immatriculation est obligatoire");
        }
        if (o.getPrixJournalier() == null || o.getPrixJournalier().signum() < 0) {
            throw new IllegalArgumentException("Le prix journalier doit être positif ou nul");
        }
    }

    private void resolve(Vehicule o) {
        if (o.getAgence() != null && o.getAgence().getId() != null) {
            o.setAgence(agenceService.findById(o.getAgence().getId()));
        } else {
            o.setAgence(null);
        }
    }
}
