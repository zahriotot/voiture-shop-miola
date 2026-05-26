# 🚗 Voiture Shop – Full Stack MIOLA
### Spring Boot · React · MySQL · Docker · Kubernetes

Projet complet développé dans le cadre du Master MIOLA – ENSIAS.
Encadrant : **Pr. Khalid Nafil**

---

## 🛠️ Stack Technique

| Couche | Technologies |
|--------|-------------|
| Backend | Java 11, Spring Boot 2.7, Spring Data JPA, REST |
| Base de données | MySQL 8.0 |
| Frontend | React 17, React-Bootstrap 1.6, Axios |
| Docker | Docker Desktop, Docker Compose |
| Kubernetes | Minikube, kubectl, ConfigMap, Secrets, PVC |

---

## 📁 Structure du Projet

```
SpringDataRest1/
├── src/                    # Code Java Spring Boot
├── reactjs/                # Application React
├── k8s/                    # Fichiers Kubernetes YAML
│   ├── db-deployment.yaml       # MySQL + PVC + Service
│   ├── app-deployment.yaml      # Spring Boot + NodePort
│   ├── mysql-configMap.yaml     # Configuration non sensible
│   └── mysql-secrets.yaml       # Credentials encodés Base64
├── Dockerfile              # Image Docker Spring Boot
├── docker-compose.yml      # Orchestration Docker locale
└── README.md
```

---

## 🚀 Lancement Rapide

### Option 1 : Docker Compose (développement)

```bash
# Cloner le projet
git clone https://github.com/zahriotot/voiture-shop-miola.git
cd voiture-shop-miola

# Construire le JAR
mvn clean package -DskipTests

# Lancer MySQL + Spring Boot
docker-compose up -d --build

# Lancer le Frontend
cd reactjs && npm install && npm start
```

| Service | URL |
|---------|-----|
| Frontend React | http://localhost:3000 |
| API REST | http://localhost:9090/api |
| Swagger | http://localhost:9090/swagger-ui.html |

---

### Option 2 : Kubernetes avec Minikube (production locale)

#### Prérequis
- Docker Desktop installé et ouvert
- Minikube installé : `winget install Kubernetes.minikube`
- kubectl installé : `winget install Kubernetes.kubectl`

#### Déploiement

```bash
# 1. Démarrer Minikube
minikube start --driver=docker

# 2. Connecter Docker à Minikube
minikube docker-env | Invoke-Expression   # Windows PowerShell

# 3. Builder l'image
mvn clean package -DskipTests
docker build -t springboot-crud-k8s:1.0 .

# 4. Déployer MySQL
kubectl apply -f k8s/db-deployment.yaml
kubectl get pods -w   # Attendre STATUS=Running

# 5. Déployer ConfigMap et Secrets
kubectl apply -f k8s/mysql-configMap.yaml
kubectl apply -f k8s/mysql-secrets.yaml

# 6. Déployer Spring Boot (3 replicas)
kubectl apply -f k8s/app-deployment.yaml
kubectl get pods
kubectl get svc

# 7. Obtenir l'URL d'accès
minikube service springboot-crud-svc --url
```

#### Tester l'API

```bash
# Remplacer URL par la valeur retournée par minikube service
GET  http://URL/voitures
POST http://URL/voitures  {"marque":"BMW","modele":"M3",...}
PUT  http://URL/voitures/1
DELETE http://URL/voitures/1
```

---

## 📡 Endpoints API

| Méthode | URL | Description |
|---------|-----|-------------|
| GET | /voitures | Lister toutes les voitures |
| GET | /voitures/{id} | Obtenir une voiture |
| POST | /voitures | Ajouter une voiture |
| PUT | /voitures/{id} | Modifier une voiture |
| DELETE | /voitures/{id} | Supprimer une voiture |
| GET | /api/ia/scoring | Score IA des voitures |

---

## 🏗️ Architecture Kubernetes

```
┌─────────────────────────────────────────────────────────┐
│                   Cluster Minikube                       │
│                                                          │
│  ┌──────────────────────┐   ┌───────────────────────┐  │
│  │  springboot-crud     │   │       mysql           │  │
│  │  Deployment (x3)     │──►│  Deployment (x1)      │  │
│  │  Port: 8080          │   │  Port: 3306           │  │
│  └──────────┬───────────┘   └──────────┬────────────┘  │
│             │                          │                │
│  ┌──────────▼───────────┐   ┌──────────▼────────────┐  │
│  │ Service: NodePort    │   │ Service: ClusterIP    │  │
│  │ Port: 30008          │   │ Name: "mysql"         │  │
│  └──────────────────────┘   └───────────────────────┘  │
│                                          │              │
│                              ┌───────────▼────────────┐ │
│                              │ PersistentVolumeClaim  │ │
│                              │ mysql-pvc (1Gi)        │ │
│                              └────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
         │ NodePort 30008
         ▼
    Votre machine
    http://$(minikube ip):30008
```

---

## 🤖 Fonctionnalité IA

L'API `/api/ia/scoring` retourne un score qualité/prix pour chaque voiture :
- Score de récence (pondération 60%)
- Score de prix (pondération 40%)
- Badge : ⭐ Excellent / ✅ Bon / ⚠️ À considérer

---

## 👨‍🎓 Auteur

**Othman Zahri** – Master MIOLA – ENSIAS  
GitHub : https://github.com/zahriotot/voiture-shop-miola