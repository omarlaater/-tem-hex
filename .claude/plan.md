# Plan : Migration vers Template BPCE Spring Boot Hexagonal

## Vue d'ensemble des changements

Migration complète du projet existant (`com.example.demo`, Spring Boot 4.0.3) vers le template BPCE conforme (`fr.bpce.template`, Spring Boot 3.x) avec architecture hexagonale, contrat-first OpenAPI 3, et toutes les règles BPCE.

---

## Étape 1 — pom.xml : groupId, Spring Boot 3.x, nouvelles dépendances

**Fichier :** `pom.xml`

Changements :
- `groupId` : `com.company.template` → `fr.bpce.template`
- `artifactId` : `spring-boot-hexagonal-template` → `bpce-product-catalog-template`
- `<name>` / `<description>` mis à jour BPCE
- Spring Boot parent : `4.0.3` → `3.4.x` (dernière stable 3.x)
- Supprimer `springdoc-openapi-starter-webmvc-ui` (remplacé par contract-first)
- Ajouter `openapi-generator-maven-plugin` (contract-first depuis YAML)
- Ajouter `spring-boot-starter-security` (pour OAuth2 resource server)
- Ajouter `spring-boot-starter-oauth2-resource-server`
- Conserver : validation, data-jpa, actuator, liquibase, micrometer, h2, postgresql, test, mockito
- Propriété `openapi-generator.version`
- Plugin `openapi-generator-maven-plugin` configuré pour générer depuis `src/main/resources/openapi/productCatalog.yaml`

---

## Étape 2 — Fichier OpenAPI YAML (contrat-first, règles BPCE)

**Nouveau fichier :** `src/main/resources/openapi/productCatalog.yaml`

Contenu conforme aux 19 règles BPCE :
- `openapi: 3.0.3`
- Info : `title: productCatalog`, `version: 1.0.0`
- URL : `/productCatalog/v1/products`
- Schemas en PascalCase : `ProductRequest`, `ProductResponse`, `ProductListResponse`, `ErrorResponse`, `ErrorDetail`
- Attributs en lowerCamelCase : `productId`, `productName`, `unitPrice`, `description`, `version`
- `id` : type `string`, format `uuid` (règle bpce-no-numeric-ids)
- `version` : type `integer`, format `int64` (optimistic locking)
- Erreurs RFC 7807 : `{errors: [{code, message, attribute, path}]}`
- Sécurité OAuth2 : scopes `products:read`, `products:manage`
- Opérations : `POST /products` (201), `GET /products` (200)
- Composants réutilisables : `Amount` (BigDecimal avec minimum 0.01), `UUID`

---

## Étape 3 — Réorganisation du package racine

**Action :** Déplacer tout de `com.example.demo` → `fr.bpce.template`

Structure cible :
```
src/main/java/fr/bpce/template/
├── BpceProductCatalogApplication.java
├── domain/
│   ├── model/Product.java
│   └── validation/DomainValidationException.java
├── application/
│   ├── port/in/
│   │   ├── CreateProductUseCase.java
│   │   ├── CreateProductCommand.java
│   │   └── ListProductsUseCase.java
│   └── port/out/
│       └── ProductRepositoryPort.java
│   └── service/
│       └── ProductApplicationService.java
└── infrastructure/
    ├── adapter/
    │   ├── in/web/
    │   │   ├── ProductController.java       ← implémente interface générée
    │   │   ├── ProductWebMapper.java
    │   │   └── GlobalExceptionHandler.java
    │   └── out/persistence/               ← typo "presistance" corrigé
    │       ├── JpaProductRepositoryAdapter.java
    │       ├── ProductJpaEntity.java
    │       ├── SpringDataProductRepository.java
    │       └── mapper/ProductPersistenceMapper.java
    └── configuration/
        ├── SecurityConfiguration.java
        └── OpenApiConfiguration.java      ← simplifié (contrat-first)
```

---

## Étape 4 — Domaine : Product enrichi

**Fichier :** `fr/bpce/template/domain/model/Product.java`

Changements :
- Ajouter `description` (String, nullable)
- Ajouter `version` (Long, pour optimistic locking)
- `id` reste UUID
- `price` reste BigDecimal (renommé sémantiquement `unitPrice` dans l'API, mais `price` en domaine)
- Validation inchangée (name non blank, price > 0)

---

## Étape 5 — Application layer : commandes et ports mis à jour

**Fichiers :**
- `CreateProductCommand` : ajouter `description` (nullable)
- `ProductRepositoryPort` : `findById(UUID)` ajouté (pour futur GET by ID)
- `ProductApplicationService` : adapté aux nouveaux champs

---

## Étape 6 — Infrastructure Web : contract-first

**Fichier :** `ProductController.java`

- Implémente l'interface Java générée par `openapi-generator` depuis le YAML
- Ne plus déclarer `@RequestMapping` manuellement (vient du YAML via code généré)
- Supprimer `CreateProductRequest`, `ProductResponse` manuels → remplacés par classes générées
- `ProductWebMapper` : mapper entre commandes/domaine et DTOs générés
- Garder `GlobalExceptionHandler` adapté au format RFC 7807 BPCE

**Erreur format BPCE :** `{errors: [{code, message, attribute, path}]}`
- Remplacer `ApiErrorResponse` / `ApiValidationError` par les types générés ou nouveaux records BPCE

---

## Étape 7 — Infrastructure Persistence : UUID + description + version

**Fichiers :**
- `ProductJpaEntity` : ajouter `description` (VARCHAR 500, nullable), `version` (`@Version Long`)
- `ProductPersistenceMapper` : adapté
- Liquibase changeset : `002-add-description-and-version-to-products.yaml`

---

## Étape 8 — Sécurité OAuth2

**Fichier :** `SecurityConfiguration.java`

- `@EnableWebSecurity`
- Resource server JWT
- Scopes `products:read` pour GET, `products:manage` pour POST
- Actuator exposé sans auth (ou avec rôle interne)
- H2 console accessible en dev uniquement

---

## Étape 9 — Configuration mis à jour

**Fichiers :**
- `application.properties` : `spring.application.name=bpce-product-catalog-template`, logging package `fr.bpce.template`
- `application-dev.properties` : inchangé (H2)
- `application-prod.properties` : inchangé (PostgreSQL)
- `application-test.properties` : `spring.security.oauth2.resourceserver.jwt.jwk-set-uri` désactivé pour tests

---

## Étape 10 — Tests mis à jour

**Fichiers :**
- Tous les packages `com.example.demo` → `fr.bpce.template`
- `ProductTest` : ajouter tests pour `description` et `version`
- `ProductApplicationServiceTest` : adapter commandes
- `ProductControllerIntegrationTest` :
  - URL `/productCatalog/v1/products`
  - Format erreur RFC 7807 : `$.errors[0].code`
  - Désactiver security en test (`@WithMockUser` ou config test)
- `ProductWebMapperTest` / `ProductPersistenceMapperTest` : adapter nouveaux champs

---

## Fichiers à créer (nouveaux)

| Fichier | Raison |
|---------|--------|
| `src/main/resources/openapi/productCatalog.yaml` | Contrat OpenAPI BPCE |
| `fr/bpce/template/infrastructure/configuration/SecurityConfiguration.java` | OAuth2 |
| `src/main/resources/db/changelog/002-add-description-version.yaml` | Migration Liquibase |

## Fichiers à supprimer (obsolètes)

| Fichier | Raison |
|---------|--------|
| `com/example/demo/...` tous | Package renommé |
| `infrastructure/adapter/in/web/error/ApiErrorResponse.java` | Remplacé RFC 7807 BPCE |
| `infrastructure/adapter/in/web/error/ApiValidationError.java` | Remplacé RFC 7807 BPCE |
| `infrastructure/adapter/in/web/CreateProductRequest.java` | Généré par OpenAPI |
| `infrastructure/adapter/in/web/ProductResponse.java` | Généré par OpenAPI |
| `infrastructure/configuration/OpenApiConfiguration.java` | Plus nécessaire (contract-first) |

## Fichiers à renommer/déplacer

| Avant | Après |
|-------|-------|
| `DemoApplication.java` | `BpceProductCatalogApplication.java` |
| `adapter/Out/presistance/` | `adapter/out/persistence/` (casse + typo) |

---

## Ordre d'exécution

1. pom.xml (groupId, Spring Boot 3.x, plugins)
2. OpenAPI YAML (contrat)
3. Domaine (`Product` enrichi, `DomainValidationException`)
4. Application layer (commandes, ports, service)
5. Infrastructure persistence (JPA entity, mapper, Liquibase)
6. Infrastructure web (controller contract-first, mapper, erreurs RFC 7807)
7. Security configuration
8. Configuration files (properties, logback)
9. Application entry point (renommage)
10. Tests (tous mis à jour)
