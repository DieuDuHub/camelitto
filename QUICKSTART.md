# 📋 Guide de Démarrage Rapide

## 🎯 Commandes Essentielles

### Démarrage de l'Application
```bash
# Démarrage simple (recommandé)
./run.sh start

# Mode développement (logs détaillés)
./run.sh dev

# Construction puis démarrage
./run.sh build && ./run.sh start
```

### Test de l'Application
```bash
# Vérifier que l'app fonctionne
curl http://localhost:8080/api/alive

# Tester l'intégration Person Data
curl http://localhost:8080/api/camel/person/1

# Tester avec différents IDs
curl http://localhost:8080/api/camel/person/42

# Voir les routes Camel
curl http://localhost:8080/api/camel/routes

# Health check automatique
./run.sh check
```

### Gestion de l'Application
```bash
# Arrêter l'application
./run.sh stop

# Nettoyer et reconstruire
./run.sh clean

# Générer la documentation
./run.sh docs
```

## 🔗 Endpoints Principaux

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/alive` | GET | Test de vie |
| `/api/health` | GET | Statut détaillé |
| `/api/camel/person/{id}` | GET | Données personnelles filtrées |
| `/api/camel/transform` | POST | Transformation JSON |
| `/api/camel/routes` | GET | Liste des routes |

## 📊 Statut du Projet

✅ **Application Spring Boot 3** - Fonctionnelle  
✅ **Intégration Apache Camel** - 4 routes actives  
✅ **API Person Data** - Filtrage first_name, last_name, creation_date  
✅ **Tests Complets** - 6 tests passants  
✅ **Documentation PlantUML** - 6 diagrammes  
✅ **JAR Exécutable** - 32 MB prêt pour déploiement  
✅ **Script de Démarrage** - Interface simple  

## 🚀 Résumé Technique

- **Framework** : Spring Boot 3.2.0 + Apache Camel 4.2.0
- **Java** : Version 17+
- **Build** : Maven 3.6+
- **Taille JAR** : ~32 MB
- **Port** : 8080 (configurable)
- **Routes Camel** : 4 routes (timer + manual + person + direct)

## 🎉 Projet Complété !

L'application est maintenant complètement fonctionnelle avec toutes les fonctionnalités demandées :

1. **JAR Spring Boot 3** standard ✅
2. **Connecteur REST HTTP** avec API alive ✅  
3. **Route Apache Camel** pour transformation JSON ✅
4. **API Person Data** avec filtrage spécifique ✅
5. **Documentation PlantUML** complète ✅

**Commande finale recommandée :**
```bash
./run.sh start
```

L'application sera accessible sur http://localhost:8080
