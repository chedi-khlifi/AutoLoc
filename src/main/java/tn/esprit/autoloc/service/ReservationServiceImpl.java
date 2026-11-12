package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Reservation;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {

    private final ReservationRepository repository;
    private final IClientService clientService;
    private final IVehiculeService vehiculeService;

    @Override
    @Transactional(readOnly = true)
    public List<Reservation> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Reservation findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation introuvable avec l'identifiant " + id));
    }

    @Override
    public Reservation create(Reservation o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Reservation update(Long id, Reservation o) {
        Reservation existing = findById(id);
        validate(o);
        resolve(o);
        existing.setDateDebut(o.getDateDebut());
        existing.setDateFin(o.getDateFin());
        existing.setClient(o.getClient());
        existing.setVehicule(o.getVehicule());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Reservation o) {
        if (o.getDateDebut() == null || o.getDateFin() == null) {
            throw new IllegalArgumentException("Les dates de début et de fin sont obligatoires");
        }
        if (o.getDateFin().isBefore(o.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas précéder la date de début");
        }
        if (o.getClient() == null || o.getClient().getId() == null
                || o.getVehicule() == null || o.getVehicule().getId() == null) {
            throw new IllegalArgumentException("Le client et le véhicule sont obligatoires");
        }
    }

    private void resolve(Reservation o) {
        o.setClient(clientService.findById(o.getClient().getId()));
        o.setVehicule(vehiculeService.findById(o.getVehicule().getId()));
    }
}
