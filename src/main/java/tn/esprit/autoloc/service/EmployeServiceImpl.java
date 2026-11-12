package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tn.esprit.autoloc.domain.Employe;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.EmployeRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeServiceImpl implements IEmployeService {

    private final EmployeRepository repository;
    private final IAgenceService agenceService;

    @Override
    @Transactional(readOnly = true)
    public List<Employe> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Employe findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employe introuvable avec l'identifiant " + id));
    }

    @Override
    public Employe create(Employe o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Employe update(Long id, Employe o) {
        Employe existing = findById(id);
        validate(o);
        resolve(o);
        existing.setNom(o.getNom());
        existing.setPrenom(o.getPrenom());
        existing.setPoste(o.getPoste());
        existing.setAgence(o.getAgence());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Employe o) {
        if (!StringUtils.hasText(o.getNom())) {
            throw new IllegalArgumentException("Le nom de l'employé est obligatoire");
        }
        if (o.getAgence() == null || o.getAgence().getId() == null) {
            throw new IllegalArgumentException("L'agence est obligatoire");
        }
    }

    private void resolve(Employe o) {
        o.setAgence(agenceService.findById(o.getAgence().getId()));
    }
}
