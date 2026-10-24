# AutoLoc API — Ateliers 4 et 5

Projet Spring Boot 3.4 / Java 17 / Maven / MySQL complet : domaine (Atelier 2), couche Service (Atelier 4), API REST + SpringDoc (Atelier 5).

> Le modèle (champs des entités) est une reconstitution cohérente avec les corps JSON de la collection Postman.
> Si vos entités diffèrent (noms de champs, identifiants `idContrat`...), adaptez entités, validations des services et corps Postman.

## Démarrage
1. Démarrer MySQL (XAMPP). La base `autoloc` est créée automatiquement.
   Si MySQL tourne sur le port 3307, modifier `spring.datasource.url`.
2. Ouvrir le dossier dans IntelliJ (projet Maven), lancer `AutoLocApiApplication`.
3. Vérifier : http://localhost:8081/api/contrats -> `[]`.
4. Swagger UI : http://localhost:8081/api/swagger-ui.html — OpenAPI JSON : http://localhost:8081/api/v3/api-docs

## Tester avec Postman
1. Import : `postman/AutoLoc.postman_collection.json` et `postman/local.postman_environment.json`.
2. Sélectionner l'environnement **local**.
3. Collection `AutoLoc_Agence_Client_Vehicule` (version réduite pour la validation en classe) ou `AutoLoc` (complète) > Run : dossiers dans l'ordre, « Z - Nettoyage » en dernier.

## Structure
```
domain/       entités JPA + ModePaiement
repository/   interfaces Spring Data JPA
service/      I...Service + ...ServiceImpl (validation, 404/400)
exception/    ResourceNotFoundException
web/controller/  9 contrôleurs REST, GlobalExceptionHandler, OpenApiConfig
```

## Règles métier (Atelier 4)
- create : identifiant non nul -> 400 ; champs obligatoires, montants/quantités négatifs, dates incohérentes -> 400.
- update : recopie des seuls attributs modifiables (les paiements d'un contrat sont conservés).
- findById / update / delete sur un identifiant inconnu -> 404.
- Paiement : lecture seule (POST -> 405) ; créé uniquement via la cascade du Contrat.

## Git
```
git init
git add .
git commit -m "Atelier 5 : API REST, tests Postman et documentation Swagger"
git branch -M main
git remote add origin https://github.com/<votre-compte>/AutoLoc.git
git push -u origin main
```
