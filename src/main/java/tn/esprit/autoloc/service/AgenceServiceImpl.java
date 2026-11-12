package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tn.esprit.autoloc.domain.Agence;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.AgenceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AgenceServiceImpl implements IAgenceService {

    private final AgenceRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Agence> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Agence findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Agence introuvable avec l'identifiant " + id));
    }

    @Override
    public Agence create(Agence o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Agence update(Long id, Agence o) {
        Agence existing = findById(id);
        validate(o);
        resolve(o);
        existing.setNom(o.getNom());
        existing.setAdresse(o.getAdresse());
        existing.setVille(o.getVille());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Agence o) {
        if (!StringUtils.hasText(o.getNom())) {
            throw new IllegalArgumentException("Le nom de l'agence est obligatoire");
        }
    }

    private void resolve(Agence o) {
        // aucune référence à résoudre
    }
}
