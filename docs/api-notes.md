# Notes API — Atelier 5

## A. Endpoints

| Contrôleur | Verbe et route | Service appelé | Succès | Erreurs |
|---|---|---|---|---|
| AgenceController | GET /api/agences | agenceService.findAll() | 200 | — |
| AgenceController | GET /api/agences/{id} | agenceService.findById(id) | 200 | 404 |
| AgenceController | POST /api/agences | agenceService.create(obj) | 201 | 400 |
| AgenceController | PUT /api/agences/{id} | agenceService.update(id, obj) | 200 | 404, 400 |
| AgenceController | DELETE /api/agences/{id} | agenceService.deleteById(id) | 204 | 404 |
| EmployeController | GET /api/employes | employeService.findAll() | 200 | — |
| EmployeController | GET /api/employes/{id} | employeService.findById(id) | 200 | 404 |
| EmployeController | POST /api/employes | employeService.create(obj) | 201 | 400 |
| EmployeController | PUT /api/employes/{id} | employeService.update(id, obj) | 200 | 404, 400 |
| EmployeController | DELETE /api/employes/{id} | employeService.deleteById(id) | 204 | 404 |
| VehiculeController | GET /api/vehicules | vehiculeService.findAll() | 200 | — |
| VehiculeController | GET /api/vehicules/{id} | vehiculeService.findById(id) | 200 | 404 |
| VehiculeController | POST /api/vehicules | vehiculeService.create(obj) | 201 | 400 |
| VehiculeController | PUT /api/vehicules/{id} | vehiculeService.update(id, obj) | 200 | 404, 400 |
| VehiculeController | DELETE /api/vehicules/{id} | vehiculeService.deleteById(id) | 204 | 404 |
| EquipementController | GET /api/equipements | equipementService.findAll() | 200 | — |
| EquipementController | GET /api/equipements/{id} | equipementService.findById(id) | 200 | 404 |
| EquipementController | POST /api/equipements | equipementService.create(obj) | 201 | 400 |
| EquipementController | PUT /api/equipements/{id} | equipementService.update(id, obj) | 200 | 404, 400 |
| EquipementController | DELETE /api/equipements/{id} | equipementService.deleteById(id) | 204 | 404 |
| ClientController | GET /api/clients | clientService.findAll() | 200 | — |
| ClientController | GET /api/clients/{id} | clientService.findById(id) | 200 | 404 |
| ClientController | POST /api/clients | clientService.create(obj) | 201 | 400 |
| ClientController | PUT /api/clients/{id} | clientService.update(id, obj) | 200 | 404, 400 |
| ClientController | DELETE /api/clients/{id} | clientService.deleteById(id) | 204 | 404 |
| ReservationController | GET /api/reservations | reservationService.findAll() | 200 | — |
| ReservationController | GET /api/reservations/{id} | reservationService.findById(id) | 200 | 404 |
| ReservationController | POST /api/reservations | reservationService.create(obj) | 201 | 400 |
| ReservationController | PUT /api/reservations/{id} | reservationService.update(id, obj) | 200 | 404, 400 |
| ReservationController | DELETE /api/reservations/{id} | reservationService.deleteById(id) | 204 | 404 |
| ContratController | GET /api/contrats | contratService.findAll() | 200 | — |
| ContratController | GET /api/contrats/{id} | contratService.findById(id) | 200 | 404 |
| ContratController | POST /api/contrats | contratService.create(obj) | 201 | 400 |
| ContratController | PUT /api/contrats/{id} | contratService.update(id, obj) | 200 | 404, 400 |
| ContratController | DELETE /api/contrats/{id} | contratService.deleteById(id) | 204 | 404 |
| PaiementController | GET /api/paiements | paiementService.findAll() | 200 | — |
| PaiementController | GET /api/paiements/{id} | paiementService.findById(id) | 200 | 404 |
| PaiementController | POST /api/paiements | (aucune méthode) | — | 405 |
| MaintenanceController | GET /api/maintenances | maintenanceService.findAll() | 200 | — |
| MaintenanceController | GET /api/maintenances/{id} | maintenanceService.findById(id) | 200 | 404 |
| MaintenanceController | POST /api/maintenances | maintenanceService.create(obj) | 201 | 400 |
| MaintenanceController | PUT /api/maintenances/{id} | maintenanceService.update(id, obj) | 200 | 404, 400 |
| MaintenanceController | DELETE /api/maintenances/{id} | maintenanceService.deleteById(id) | 204 | 404 |

Les 404 viennent de `ResourceNotFoundException` et les 400 de `IllegalArgumentException`, traduites par `GlobalExceptionHandler` : les contrôleurs ne produisent jamais ces codes eux-mêmes.

## B. Annotations Jackson

| Association | Annotation retenue | Justification |
|---|---|---|
| Contrat.paiements ↔ Paiement.contrat | `@JsonManagedReference("contrat-paiements")` / `@JsonBackReference("contrat-paiements")` | Vraie composition (`cascade = ALL`) : les paiements se créent dans le JSON du contrat, et le lien retour est rétabli à la désérialisation. |
| Agence.employes, Agence.vehicules | `@JsonIgnore` sur la liste | L'enfant est créé seul et désigne son parent par `{"agence": {"id": 1}}` ; la liste inverse n'est pas utile dans la réponse. |
| Client.reservations, Vehicule.reservations | `@JsonIgnore` sur la liste | Même logique : `Reservation` est créée seule avec `client` et `vehicule` par identifiant. |
| Vehicule.maintenances | `@JsonIgnore` sur la liste | Idem pour `Maintenance`. |

> À adapter aux noms exacts de votre modèle ; ne traiter une association que lorsque l'erreur apparaît dans Postman.

**`@JsonBackReference` vs `@JsonIgnore`** : `@JsonBackReference` exclut la propriété de la sérialisation **et** la reconstruit à la désérialisation à partir du parent (utile en composition). `@JsonIgnore` ignore simplement la propriété dans les deux sens. Sur `Reservation.client`, `@JsonBackReference` ferait ignorer le client à la lecture et le POST créerait une réservation sans client.

## C. Postman et Swagger

- **Postman** sert à *tester* : requêtes rejouables avec scripts de vérification. Exemple : `GET /api/contrats/9999` renvoie 404 avec le corps JSON d'erreur, vérifié par `pm.response.to.have.status(404)`.
- **Swagger UI** sert à *documenter et explorer* : contrat synchronisé avec le code, essai direct des endpoints. Exemple : `/api/swagger-ui.html` liste les groupes « Contrats » et « Paiements », leurs réponses 200/404, et le schéma `Contrat` déduit de l'entité.

## D. Anomalies SonarQube

> **À remplacer par vos propres constats** après « Analyze with SonarQube for IDE » sur `web.controller`. Exemples fréquents :

| Règle | Explication | Correction |
|---|---|---|
| Injection par attribut (`@Autowired` sur un champ) | Rend la classe difficile à tester et masque les dépendances. | Champ `final` + `@RequiredArgsConstructor`. |
| Import inutilisé | Code mort, bruit à la lecture. | Supprimer l'import (Ctrl+Alt+O). |
| Entité JPA exposée dans l'API | Couplage API/base, champs internes visibles. | Provisoire ; remplacée par des DTO au Chapitre 10. |

## E. Questions

1. Le **DispatcherServlet** reçoit toutes les requêtes ; le **HandlerMapping** retrouve le contrôleur et la méthode à partir du verbe HTTP et de l'URL.
2. Le contrôleur ne contient aucune logique métier : il délègue à la couche Service. Dépendre de l'**interface** `IContratService` (et non de `ContratServiceImpl` ou du repository) découple les couches, permet de changer d'implémentation et respecte l'architecture web → service → repository.
3. **201** signale qu'une ressource a été créée ; **204** signale un succès sans corps à renvoyer. Une ressource absente donne **404** (erreur client prévue, `ResourceNotFoundException` traduite par le `GlobalExceptionHandler`) et non 500, qui désigne une erreur serveur imprévue.
4. `Paiement` est en lecture seule : c'est le `Contrat` qui porte ses paiements (`cascade = ALL`). `PaiementController` n'a donc que deux GET, et `POST /api/paiements` renvoie **405 Method Not Allowed**.
5. **Risques** : couplage API/schéma de base, champs internes exposés, boucles de sérialisation, proxys LAZY, modification involontaire de champs (mass assignment). `Contrat.paiements` utilise `@JsonManagedReference` car c'est une composition : les paiements naissent dans le JSON du contrat. `Agence.vehicules` utilise `@JsonIgnore` car un véhicule est créé seul et référence son agence par identifiant.
