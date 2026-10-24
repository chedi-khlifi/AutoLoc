# Livrables — Swagger, Postman, Cool Request (Agence, Client, Vehicule)

## URLs (port 8081, context-path /api)
- Swagger UI : http://localhost:8081/api/swagger-ui.html
- OpenAPI JSON : http://localhost:8081/api/v3/api-docs

## Captures d'écran à fournir

### 1. Swagger UI
- [ ] Page d'accueil : titre « AutoLoc API 1.0.0 » et groupes Agences, Clients, Vehicules
- [ ] Groupe Agences déplié (GET, GET/{id}, POST, PUT, DELETE avec résumés)
- [ ] GET /agences : Try it out > Execute (code 200 + corps)
- [ ] POST /clients : Execute (201) ; GET /clients/9999 (404)
- [ ] Un POST /vehicules (201) et un 400 (prixJournalier négatif)
- [ ] Section Schemas (Agence, Client, Vehicule)

### 2. Postman
- [ ] Collection importée : dossiers Agences, Clients, Vehicules, Nettoyage (environnement « local » sélectionné)
- [ ] Une requête par verbe avec statut et onglet Test Results (PASS) : GET 200, POST 201, PUT 200, DELETE 204
- [ ] Cas d'erreur : GET /9999 (404), POST avec id (400), POST invalide (400)
- [ ] Collection Runner : résumé final (tous les tests passés)

### 3. Cool Request (plugin IntelliJ)
- [ ] Settings > Plugins > Marketplace > « Cool Request » > Install, redémarrer IntelliJ
- [ ] Panneau Cool Request (barre latérale) : liste des endpoints /agences, /clients, /vehicules
- [ ] Une requête envoyée depuis le plugin (GET /api/agences) avec la réponse JSON
- [ ] Une requête POST avec corps JSON et la réponse 201

## Ordre conseillé
1. Lancer l'application (console : « Started AutoLocApiApplication »).
2. Swagger : captures. 3. Postman : Runner. 4. Cool Request.
