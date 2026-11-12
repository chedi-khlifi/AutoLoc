package tn.esprit.autoloc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.exception.ResourceNotFoundException;
import tn.esprit.autoloc.repository.ClientRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientServiceImpl implements IClientService {

    private final ClientRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable avec l'identifiant " + id));
    }

    @Override
    public Client create(Client o) {
        if (o.getId() != null) {
            throw new IllegalArgumentException("L'identifiant doit être nul à la création");
        }
        validate(o);
        resolve(o);
        return repository.save(o);
    }

    @Override
    public Client update(Long id, Client o) {
        Client existing = findById(id);
        validate(o);
        resolve(o);
        existing.setNom(o.getNom());
        existing.setPrenom(o.getPrenom());
        existing.setEmail(o.getEmail());
        existing.setCin(o.getCin());
        return repository.save(existing);
    }

    @Override
    public void deleteById(Long id) {
        repository.delete(findById(id));
    }

    private void validate(Client o) {
        if (!StringUtils.hasText(o.getNom())) {
            throw new IllegalArgumentException("Le nom du client est obligatoire");
        }
        if (!StringUtils.hasText(o.getCin())) {
            throw new IllegalArgumentException("Le CIN est obligatoire");
        }
    }

    private void resolve(Client o) {
        // aucune référence à résoudre
    }
}
