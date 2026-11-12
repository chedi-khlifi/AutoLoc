package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Maintenance;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.MaintenanceRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceServiceImpl implements IMaintenanceService {

    private final MaintenanceRepository repository;
    private final IVehiculeService vehiculeService;

    @Override
    @Transactional(readOnly = true)
    public List<Maintenance> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Maintenance findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance introuvable avec l'identifiant " + id));
    }

    @Override
    public Maintenance create(Maintenance o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Maintenance update(Long id, Maintenance o) {
        Maintenance existing = findById(id);
        validate(o);
        resolve(o);
        existing.setDateDebut(o.getDateDebut());
        existing.setDateFin(o.getDateFin());
        existing.setCout(o.getCout());
        existing.setDescription(o.getDescription());
        existing.setVehicule(o.getVehicule());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Maintenance o) {
        if (o.getDateDebut() == null || o.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        if (o.getDateFin().isBefore(o.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas précéder la date de début");
        }
        if (o.getCout() == null || o.getCout().signum() < 0) {
            throw new IllegalArgumentException("Le coût doit être positif ou nul");
        }
        if (o.getVehicule() == null || o.getVehicule().getId() == null) {
            throw new IllegalArgumentException("Le véhicule est obligatoire");
        }
    }

    private void resolve(Maintenance o) {
        o.setVehicule(vehiculeService.findById(o.getVehicule().getId()));
    }
}
