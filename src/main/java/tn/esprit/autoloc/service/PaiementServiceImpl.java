package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Paiement;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.PaiementRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaiementServiceImpl implements IPaiementService {

    private final PaiementRepository repository;

    @Override
    public List<Paiement> findAll() {
        return repository.findAll();
    }

    @Override
    public Paiement findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paiement introuvable avec l'identifiant " + id));
    }
}
