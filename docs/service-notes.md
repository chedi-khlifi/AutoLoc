# Atelier 4 — Notes : IoC, DI et couche Service

## A. Modes d'injection comparés

| Critère | Constructeur (`@RequiredArgsConstructor`) | Attribut (`@Autowired` sur le champ) |
|---|---|---|
| Champ `final` possible ? | Oui | Non |
| Dépendance visible dans la signature de la classe ? | Oui (paramètre du constructeur) | Non (dépendance cachée) |
| `new` hors conteneur sans dépendance `null` ? | Impossible de l'oublier : le constructeur impose la dépendance | Le champ reste `null` → `NullPointerException` |
| Signalé par SonarQube for IDE ? | Non | Oui — règle S6813 *(à vérifier dans le panneau)* |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Une seule dépendance, `final`, typée par interface ; service sans état. |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Idem. |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Idem. |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Idem. |
| ClientServiceImpl | IClientRepository | Constructeur | Idem. |
| ReservationServiceImpl | IReservationRepository | Constructeur | Idem. |
| ContratServiceImpl | IContratRepository | Constructeur | Idem. |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Lecture seule : le Paiement est modifié uniquement à travers le Contrat. |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Idem. |

Règles de gestion (classe utilitaire `Validations`) : champs obligatoires non vides (Agence, Employe, Client, Equipement, Vehicule), tarif journalier / montant total non négatifs (Vehicule, Contrat), date de fin non antérieure à la date de début (Reservation, Maintenance).

## C. Messages d'erreur du conteneur

**Message 1 — `NoSuchBeanDefinitionException` (bean `IContratService` introuvable).**
Causes probables : (1) `@Service` oublié sur `ContratServiceImpl` ; (2) classe placée hors du package de scan `tn.esprit.autoloc`. Correction : ajouter `@Service`, vérifier le package.

**Message 2 — `NoUniqueBeanDefinitionException` (`emailNotificateur` et `smsNotificateur`).**
Correction 1 : `@Primary` sur la **classe** du bean par défaut (ex. `EmailNotificateur`). Correction 2 : `@Qualifier("emailNotificateur")` sur le **paramètre du constructeur** du contrôleur.

**Message 3 — dépendance circulaire (`clientServiceImpl` ↔ `reservationServiceImpl`).**
Avec l'injection par constructeur, chaque bean doit être entièrement construit avant d'être fourni à l'autre : aucun des deux ne peut être créé en premier, le cycle est donc impossible à contourner. Piste : extraire la responsabilité commune dans un troisième service dont les deux dépendent, ou n'avoir qu'un sens de dépendance (Reservation → Client).

## D. Anomalies SonarQube for IDE

*À compléter après l'analyse réelle dans IntelliJ (au moins trois anomalies).*

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Injection par attribut (étape 2) | S6813 *(à vérifier)* — injection par attribut déconseillée | Retour à l'injection par constructeur + champ `final` |
| `RuntimeException` levée dans `findById` (étape 4) | S112 *(à vérifier)* — exception générique | Retour à `ResourceNotFoundException` |
| à compléter | à compléter | à compléter |

## E. Questions de compréhension

1. Le `new` de `ContratServiceImpl` est exécuté par le conteneur Spring (`ApplicationContext`) au démarrage : le scan détecte `@Service`, puis il appelle le constructeur en lui fournissant le proxy de `IContratRepository`.
2. Pour dépendre d'une abstraction : on peut changer d'implémentation (ou fournir un remplacement en test) sans modifier le contrôleur ; c'est ce qui assure le couplage faible.
3. Un singleton est partagé par tous les threads. Un attribut modifiable (ex. `private Contrat contratCourant`) serait écrasé par une requête concurrente : le client A pourrait alors modifier ou lire le contrat du client B.
4. Passer directement l'objet reçu à `save` (id renseigné → `merge`) écraserait toutes les colonnes avec les valeurs reçues (y compris les `null`) et, avec `orphanRemoval`, pourrait vider la collection de paiements. On charge donc l'existant et on recopie uniquement les attributs modifiables.
5. `@Component` se pose sur une classe qu'on possède ; `@Bean` se pose sur une méthode d'une classe `@Configuration`, pour une classe qu'on ne peut pas annoter (bibliothèque, JDK) ou une construction particulière. `@Primary` (sur la classe) désigne le candidat par défaut ; `@Qualifier` (à l'injection) choisit un bean précis par son nom. `IPaiementService` est en lecture seule car, dans la composition Contrat ↔ Paiement, un paiement n'est créé ou retiré qu'à travers son Contrat.
