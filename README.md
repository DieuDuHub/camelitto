# 🚀 Spring Boot + Apache Camel Integration Project

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Apache Camel](https://img.shields.io/badge/Apache%20Camel-4.2.0-red.svg)](https://camel.apache.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)

Un projet complet d'intégration Spring Boot et Apache Camel démontrant les patterns d'intégration d'entreprise modernes avec transformation de données JSON et intégration d'APIs externes.

## ✨ Fonctionnalités Principales

- **🏗️ Application Spring Boot 3** - JAR exécutable standalone
- **🔄 Routes Apache Camel** - Transformation de données et intégration
- **🌐 APIs REST** - Endpoints de santé et de gestion
- **📊 Transformation JSON** - Traitement et filtrage de données
- **📚 Documentation PlantUML** - Diagrammes d'architecture complets
- **🧪 Tests Complets** - Tests unitaires et d'intégration
- **⚡ Démarrage Rapide** - Script de démarrage intelligent

## 🚀 Démarrage Rapide

### Prérequis
- Java 17+
- Maven 3.6+
- curl (pour les tests)

### Installation et Lancement

```bash
# Cloner et se déplacer dans le projet
cd /Users/matthieudebray/dev/java/camel

# Démarrage simple
./run.sh start

# Ou démarrage en mode développement
./run.sh dev

# Ou étape par étape
./run.sh build    # Construire le projet
./run.sh start    # Démarrer l'application
```

### Test de l'Application

```bash
# Vérifier que l'application fonctionne
curl http://localhost:8080/api/alive

# Tester l'intégration des données personnelles avec différents IDs
curl http://localhost:8080/api/camel/person/1
curl http://localhost:8080/api/camel/person/42

# Voir les routes Camel actives
curl http://localhost:8080/api/camel/routes
```

## 📁 Structure du Projet

```
camel/
├── src/main/java/com/example/camel/
│   ├── CamelSpringBootApplication.java    # Point d'entrée principal
│   ├── controller/
│   │   ├── HealthController.java          # Endpoints de santé
│   │   ├── ApiController.java             # APIs générales
│   │   └── CamelController.java           # Gestion des routes Camel
│   ├── route/
│   │   └── JsonTransformRoute.java        # Routes de transformation
│   └── processor/
│       ├── JsonTransformProcessor.java    # Transformation JSONPlaceholder
│       └── PersonDataProcessor.java       # Filtrage données personnelles
├── src/test/java/                         # Tests unitaires
├── docs/                                  # Documentation PlantUML
├── target/                               # JAR compilé
├── run.sh                                # Script de démarrage
└── generate-diagrams.sh                 # Génération documentation
```

## 🔗 Endpoints API

### Santé et Statut
- `GET /api/alive` - Vérification de vie de l'application
- `GET /api/health` - Statut détaillé de santé
- `GET /actuator/health` - Endpoint Spring Boot Actuator

### Intégration Camel
- `POST /api/camel/transform` - Transformation JSON manuelle
- `GET /api/camel/person/{id}` - Récupération et filtrage des données personnelles
- `GET /api/camel/routes` - Liste des routes Camel
- `POST /api/camel/routes/{routeId}/start` - Démarrer une route
- `POST /api/camel/routes/{routeId}/stop` - Arrêter une route

## 🔄 Intégrations API Externes

### API JSONPlaceholder
- **URL** : `https://jsonplaceholder.typicode.com/posts/{id}`
- **Usage** : Démonstration de transformation de données
- **Traitement** : Transformation du titre, génération de résumé, ajout de métadonnées

### API Person Data
- **URL** : `http://localhost:8001/person_data/{id}`
- **Méthode** : GET
- **Usage** : Intégration de données personnelles réelles avec ID dynamique
- **Filtrage** : Extraction de `first_name`, `last_name`, et `creation_date`

**Exemples d'appels :**
- `/api/camel/person/1` → `GET http://localhost:8001/person_data/1`
- `/api/camel/person/42` → `GET http://localhost:8001/person_data/42`

**Exemple de réponse filtrée :**
```json
{
  "first_name": "Person1",
  "last_name": "Doe1", 
  "creation_date": "2025-08-19T09:25:30.135028Z"
}
```

## 🛠️ Utilisation du Script de Démarrage

Le script `run.sh` offre plusieurs options :

```bash
./run.sh start      # Démarrage standard
./run.sh dev        # Mode développement
./run.sh test       # Mode test
./run.sh build      # Construction du projet
./run.sh clean      # Construction propre
./run.sh docs       # Génération documentation
./run.sh check      # Vérification santé
./run.sh stop       # Arrêt de l'application
./run.sh help       # Aide complète
```

### Variables d'Environnement

```bash
# Changer le port
SERVER_PORT=8081 ./run.sh start

# Profil personnalisé
SPRING_PROFILES=production ./run.sh start
```

## 📊 Routes Apache Camel

### Route Automatique (Timer)
- **Déclenchement** : Toutes les 30 secondes
- **Source** : API JSONPlaceholder
- **Traitement** : Transformation et enrichissement
- **Destination** : Logs et pipeline interne

### Route Manuelle (REST)
- **Déclenchement** : Appel REST `GET /api/camel/person/{id}`
- **Source** : API Person Data locale avec ID dynamique
- **Traitement** : Filtrage et formatage JSON
- **Destination** : Réponse HTTP directe

## 📚 Documentation

### Diagrammes PlantUML
Documentation complète avec diagrammes :
- Architecture système
- Séquences d'interaction
- Composants et dépendances
- Flux de données
- Spécifications API

```bash
# Générer tous les diagrammes
./run.sh docs

# Ou manuellement
./generate-diagrams.sh
```

### Documentation Technique
- **`docs/PROJECT_DOCUMENTATION.md`** - Documentation complète du projet
- **Diagrammes PlantUML** - Architecture et flux
- **Commentaires Code** - Javadoc et commentaires inline

## 🧪 Tests

### Exécution des Tests
```bash
# Via Maven
mvn test

# Via script
./run.sh build  # Inclut les tests
```

### Couverture
- **Tests unitaires** : Controllers, Processors, Routes
- **Tests d'intégration** : Spring Boot, Camel Context
- **Tests de transformation** : JSON processing, filtrage

## 🏗️ Construction et Déploiement

### Construction
```bash
# Construction standard
mvn clean package

# Ou via script
./run.sh build
```

### JAR Produit
- **Fichier** : `target/camel-springboot-app-1.0.0.jar`
- **Taille** : ~32 MB
- **Type** : JAR exécutable Spring Boot
- **Java** : Nécessite Java 17+

### Déploiement
```bash
# Démarrage direct
java -jar target/camel-springboot-app-1.0.0.jar

# Avec configuration
java -jar target/camel-springboot-app-1.0.0.jar --server.port=8081
```

## ⚙️ Configuration

### Profils Spring
- **Default** : Configuration standard
- **Dev** : Logs détaillés, tous les endpoints actuator
- **Test** : Configuration minimale pour tests
- **Production** : Optimisé pour déploiement

### Propriétés Principales
```yaml
server:
  port: 8080

camel:
  springboot:
    main-run-controller: true

management:
  endpoints:
    web:
      exposure:
        include: health,info,camel
```

## 🔍 Monitoring et Observabilité

### Health Checks
- Spring Boot Actuator intégré
- Endpoints de santé personnalisés
- Monitoring des routes Camel

### Logs
- Logs structurés avec niveaux
- Informations de traitement Camel
- Traces des appels API externes

## 🚀 Patterns d'Intégration Entreprise

Ce projet démontre plusieurs patterns EIP :
- **Message Router** : Routage conditionnel
- **Message Translator** : Transformation de format
- **Content Filter** : Filtrage de champs
- **Message Enricher** : Ajout de métadonnées
- **Polling Consumer** : Consommation basée timer
- **Request-Reply** : Traitement synchrone

## 🔮 Améliorations Futures

### Intégrations Additionnelles
- Connectivité base de données avec JPA
- Intégration files de messages (RabbitMQ, Kafka)
- Traitement de fichiers
- Services de notification

### Fonctionnalités Avancées
- Patterns Circuit Breaker
- Mécanismes de retry
- Pipelines de transformation
- Architecture événementielle

### Monitoring Avancé
- Métriques Prometheus
- Tracing distribué
- Indicateurs de santé personnalisés
- Monitoring de performance

## 📄 Licence

Ce projet est un exemple de démonstration pour l'intégration Spring Boot + Apache Camel.

## 🤝 Support

Pour des questions ou des problèmes :
1. Vérifiez les logs de l'application
2. Testez les endpoints de santé
3. Consultez la documentation PlantUML
4. Utilisez `./run.sh check` pour diagnostiquer

---

**Projet créé avec ❤️ en utilisant Spring Boot 3 et Apache Camel 4.2.0**
