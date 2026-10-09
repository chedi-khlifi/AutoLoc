package tn.esprit.autoloc.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.autoloc.domain.Client;
import tn.esprit.autoloc.repository.IClientRepository;
import tn.esprit.autoloc.service.IClientService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final IClientRepository clientRepository;

    @Override
    @Transactional
    public Client create(Client client) {
        client.setIdClient(null); // force un INSERT
        return clientRepository.save(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable : id = " + id));
    }

    @Override
    @Transactional
    public Client update(Long id, Client client) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client introuvable : id = " + id);
        }
        client.setIdClient(id);
        return clientRepository.save(client);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client introuvable : id = " + id);
        }
        clientRepository.deleteById(id);
    }
}