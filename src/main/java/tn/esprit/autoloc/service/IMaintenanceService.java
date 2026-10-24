package tn.esprit.autoloc.service;

import tn.esprit.autoloc.domain.Maintenance;

import java.util.List;

public interface IMaintenanceService {

    List<Maintenance> findAll();

    Maintenance findById(Long id);

    Maintenance create(Maintenance maintenance);

    Maintenance update(Long id, Maintenance maintenance);

    void deleteById(Long id);
}
