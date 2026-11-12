package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Contrat;

import java.util.List;

public interface IContratService {

    List<Contrat> findAll();

    Contrat findById(Long id);

    Contrat create(Contrat contrat);

    Contrat update(Long id, Contrat contrat);

    void deleteById(Long id);
}
