package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tn.esprit.autoloc.domain.Equipement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.EquipementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EquipementServiceImpl implements IEquipementService {

    private final EquipementRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Equipement> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Equipement findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipement introuvable avec l'identifiant " + id));
    }

    @Override
    public Equipement create(Equipement o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Equipement update(Long id, Equipement o) {
        Equipement existing = findById(id);
        validate(o);
        resolve(o);
        existing.setLibelle(o.getLibelle());
        existing.setQuantite(o.getQuantite());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Equipement o) {
        if (!StringUtils.hasText(o.getLibelle())) {
            throw new IllegalArgumentException("Le libellé est obligatoire");
        }
        if (o.getQuantite() == null || o.getQuantite() < 0) {
            throw new IllegalArgumentException("La quantité doit être positive ou nulle");
        }
    }

    private void resolve(Equipement o) {
        // aucune référence à résoudre
    }
}
