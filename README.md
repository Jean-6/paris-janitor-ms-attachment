# ParisJanitor - Microservice des Pièces Jointes


## 📖Table des matières

1. ✅[Introduction](#introduction)
2. 📦[Prérequis](#prérequis)
3. 🧱[Architecture](#architecture)
3. ⚙️[Installation](#installation)
4. 🔧[Configuration](#configuration)
5. 🚀[Utilisation](#utilisation)
6. 🧪[Tests](#tests)
7. 🧑‍💻[Contribuer](#contribuer)
8. 📄[Licence](#licence)
9. 📦[Deploiement](#deploiement)
10. ⭐[Points importants](#Terminaison API)
11. 🔐[Authentification](#Authentification)
12. 🛠️[Dépannage](#Dépannage)


## Introduction

Ce microservice est une composante autonome conçue selon l'architecture en couche, elle se concentre uniquement sur la gestion du stockage, la récupération et la suppression des pièces jointes associées à différentes entités comme les biens immobiliers, les factures et les pièces justificatives.

## Prérequis

- Java 17
- Maven
- Docker
- Spring Boot
- OpenAPI
- Base de données non-relationnelles: Mongo Atlas
- Systèmes de stockage en cloud (AWS S3)
- Spring Security (Basic Auth)
- RabbitMQ


## Configuration


#### application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/nom_de_la_base
spring.datasource.username=utilisateur
spring.datasource.password=mot_de_passe
spring.jpa.hibernate.ddl-auto=update

## Utilisation

mvn spring-boot:run


## Points de terminaison

### Image

- POST /img : Poster les images d'une propriété.
- POST /img : Créer une nouvelle réservation.
- GET /img/{id} : Récupérer toules immages d'une propriété.
- GET /img/{id} : Récupérer uen seule image.
- PUT /img/{id} : Mettre à jour une nouvelle image
- DELETE /img/{id} : Supprimer les images du propriete



## Tests

mvn test

## Licence

MIT License

