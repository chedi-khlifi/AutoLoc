package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Contrat;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ContratRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratServiceImpl implements IContratService {

    private final ContratRepository repository;
    private final IReservationService reservationService;

    @Override
    @Transactional(readOnly = true)
    public List<Contrat> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Contrat findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrat introuvable avec l'identifiant " + id));
    }

    @Override
    public Contrat create(Contrat o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        if (o.getPaiements() == null) {
            o.setPaiements(new ArrayList<>());
        }
        for (Paiement p : o.getPaiements()) {
            validatePaiement(p);
            p.setContrat(o);
        }
        return repository.save(o);
    }

    @Override
    public Contrat update(Long id, Contrat o) {
        Contrat existing = findById(id);
        validate(o);
        resolve(o);
        // Seuls les attributs modifiables sont recopiés : les paiements sont conservés.
        existing.setDateSignature(o.getDateSignature());
        existing.setMontantTotal(o.getMontantTotal());
        existing.setValide(o.getValide());
        if (o.getReservation() != null) {
            existing.setReservation(o.getReservation());
        }
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Contrat o) {
        if (o.getDateSignature() == null) {
            throw new IllegalArgumentException("La date de signature est obligatoire");
        }
        if (o.getMontantTotal() == null || o.getMontantTotal().signum() < 0) {
            throw new IllegalArgumentException("Le montant total doit être positif ou nul");
        }
    }

    private void resolve(Contrat o) {
        if (o.getReservation() != null && o.getReservation().getId() != null) {
            o.setReservation(reservationService.findById(o.getReservation().getId()));
        } else {
            o.setReservation(null);
        }
    }

    private void validatePaiement(Paiement p) {
        if (p.getMontant() == null || p.getMontant().signum() <= 0) {
            throw new IllegalArgumentException("Le montant d'un paiement doit être strictement positif");
        }
        if (p.getDatePaiement() == null || p.getModePaiement() == null) {
            throw new IllegalArgumentException("La date et le mode de paiement sont obligatoires");
        }
    }
}
