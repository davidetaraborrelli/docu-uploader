Contesto
Progetto scolastico: piattaforma web per upload documenti con ricerca semantica. Questo piano copre il checkpoint relazionale — tutto gira su PostgreSQL. MongoDB e Vector DB verranno integrati in una fase successiva.
Correzione architetturale
I microservizi si dividono per dominio di business, non per database. Ogni servizio possiede i propri dati. Nel checkpoint relazionale tutti usano PostgreSQL, ma ognuno ha il proprio database/schema separato. Più avanti, document-service aggiungerà MongoDB e un nuovo search-service userà il Vector DB.

Architettura
                    ┌──────────────┐
                    │   Frontend   │  (non in scope)
                    └──────┬───────┘
                           │ HTTP/REST
          ┌────────────────┼────────────────┐
          ▼                ▼                ▼
   ┌─────────────┐ ┌──────────────┐ ┌────────────────┐
   │ user-service │ │ document-svc │ │ notification-  │
   │   :8081      │ │   :8082      │ │ service :8083  │
   └──────┬───────┘ └──────┬───────┘ └───────┬────────┘
          │                │    publish       │ consume
          │                │───► RabbitMQ ───►│
          ▼                ▼                  ▼
   ┌─────────────┐ ┌──────────────┐ ┌────────────────┐
   │ db_users     │ │ db_documents │ │ db_notifications│
   │ (PostgreSQL) │ │ (PostgreSQL) │ │  (PostgreSQL)  │
   └─────────────┘ └──────────────┘ └────────────────┘
3 Microservizi
ServizioPortaResponsabilitàDBuser-service8081Registrazione, login, JWTdb_usersdocument-service8082Upload, CRUD documenti, pubblica eventidb_documentsnotification-service8083Consuma eventi RabbitMQ, serve notifichedb_notifications
Modulo condiviso: common
Contiene JWT utilities e security filter riutilizzati da tutti i servizi.

Struttura Progetto (Multi-module Maven)
docu-upload-1.0/
├── pom.xml                          (parent POM)
├── docker-compose.yml
├── common/
│   ├── pom.xml
│   └── src/main/java/com/docuupload/common/
│       ├── dto/
│       │   └── UserInfo.java              (dati utente estratti dal JWT)
│       └── security/
│           ├── JwtUtils.java              (generazione/validazione token)
│           └── JwtAuthFilter.java         (filtro Spring Security)
├── user-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/docuupload/userservice/
│       ├── UserServiceApplication.java
│       ├── config/SecurityConfig.java
│       ├── controller/AuthController.java
│       ├── dto/ (RegisterRequest, LoginRequest, AuthResponse)
│       ├── entity/User.java
│       ├── repository/UserRepository.java
│       └── service/AuthService.java
├── document-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/docuupload/documentservice/
│       ├── DocumentServiceApplication.java
│       ├── config/SecurityConfig.java, RabbitConfig.java
│       ├── controller/DocumentController.java
│       ├── dto/ (DocumentUploadRequest, DocumentResponse)
│       ├── entity/Document.java
│       ├── repository/DocumentRepository.java
│       ├── service/DocumentService.java
│       └── messaging/DocumentEventPublisher.java
├── notification-service/
│   ├── pom.xml
│   ├── Dockerfile
│   └── src/main/java/com/docuupload/notificationservice/
│       ├── NotificationServiceApplication.java
│       ├── config/SecurityConfig.java, RabbitConfig.java
│       ├── controller/NotificationController.java
│       ├── entity/Notification.java
│       ├── repository/NotificationRepository.java
│       ├── service/NotificationService.java
│       └── messaging/NotificationEventConsumer.java
└── documentation/

Schema Database (PostgreSQL)
db_users
sqlCREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50) UNIQUE NOT NULL,
    email       VARCHAR(100) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,        -- BCrypt hash
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
db_documents
sqlCREATE TABLE documents (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,              -- ID logico dall'utente JWT
    title       VARCHAR(255) NOT NULL,
    content     TEXT,                          -- temporaneo, migra a MongoDB dopo
    file_size   BIGINT,
    mime_type   VARCHAR(100),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
db_notifications
sqlCREATE TABLE notifications (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    type        VARCHAR(50) NOT NULL,         -- UPLOAD_COMPLETED, INDEXING_COMPLETED
    message     TEXT NOT NULL,
    is_read     BOOLEAN DEFAULT FALSE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

REST API
user-service (:8081)
MetodoEndpointDescrizioneAuthPOST/api/auth/registerRegistrazioneNoPOST/api/auth/loginLogin, ritorna JWTNoGET/api/users/meProfilo utente correnteSi
document-service (:8082)
MetodoEndpointDescrizioneAuthPOST/api/documentsUpload documentoSiGET/api/documentsLista documenti utenteSiGET/api/documents/{id}Dettaglio + contenutoSiDELETE/api/documents/{id}Cancella documentoSi
notification-service (:8083)
MetodoEndpointDescrizioneAuthGET/api/notificationsNotifiche dell'utenteSiPUT/api/notifications/{id}/readSegna come lettaSi

RabbitMQ

Exchange: docuupload.events (tipo: topic)
Eventi:

document.uploaded — pubblicato da document-service dopo upload riuscito
document.indexed — stub per ora (verrà dal futuro indexing-service)


Consumer: notification-service ascolta sull'exchange e crea record notifica

Payload evento:
json{
  "userId": 1,
  "documentId": 42,
  "documentTitle": "relazione.txt",
  "eventType": "UPLOAD_COMPLETED",
  "timestamp": "2026-03-04T10:00:00Z"
}

Flusso Autenticazione (JWT)

Utente chiama POST /api/auth/register → crea account
Utente chiama POST /api/auth/login → riceve JWT token
Per ogni richiesta protetta: header Authorization: Bearer <token>
Ogni servizio ha JwtAuthFilter (dal modulo common) che valida il token ed estrae userId e username
Il userId viene usato dal servizio per filtrare dati (ownership documenti, notifiche)


Docker Compose
Servizi container:
ContainerImmaginePortepostgrespostgres:165432pgadmindpage/pgadmin45050rabbitmqrabbitmq:3-management5672, 15672user-servicebuild locale8081document-servicebuild locale8082notification-servicebuild locale8083
PostgreSQL avrà un init script che crea i 3 database (db_users, db_documents, db_notifications).

Ordine di Implementazione
Fase 1 — Scaffolding

Parent POM Maven con Java 21, Spring Boot 3.x, moduli
docker-compose.yml con PostgreSQL, pgAdmin, RabbitMQ
Script init DB per creare i 3 database
.gitignore

Fase 2 — Modulo Common

JwtUtils — generazione e validazione JWT (jjwt library)
JwtAuthFilter — filtro servlet Spring Security
UserInfo DTO — dati utente dal token

Fase 3 — User Service

Entity User + UserRepository (Spring Data JPA)
AuthService — registrazione (BCrypt) e login
AuthController — endpoint REST
SecurityConfig — endpoint pubblici vs protetti
application.yml — connessione a db_users
Test unitari AuthService

Fase 4 — Document Service

Entity Document + DocumentRepository
DocumentService — CRUD con ownership check (userId dal JWT)
DocumentController — endpoint REST
RabbitConfig — configurazione exchange
DocumentEventPublisher — pubblica evento dopo upload
application.yml — connessione a db_documents + RabbitMQ
Test unitari DocumentService

Fase 5 — Notification Service

Entity Notification + NotificationRepository
NotificationEventConsumer — consumer RabbitMQ
NotificationService — logica business
NotificationController — endpoint REST
application.yml — connessione a db_notifications + RabbitMQ
Test unitari NotificationService

Fase 6 — Dockerizzazione

Dockerfile per ogni servizio (multi-stage build)
Integrazione completa docker-compose
Test end-to-end manuale con curl/Postman


Testing

Unit test: JUnit 5 + Mockito per service layer di ogni microservizio
Integration test: @SpringBootTest con Testcontainers (PostgreSQL) per i repository
Test manuali: curl / Postman per verificare flussi end-to-end


Predisposizione per fasi future

Document.content (TEXT in PostgreSQL) verrà sostituito da riferimento a MongoDB ObjectId
L'interfaccia DocumentService è progettata per rendere trasparente il cambio di storage
L'evento document.uploaded su RabbitMQ verrà consumato anche dal futuro indexing-service
La struttura a exchange topic permette di aggiungere consumer senza modificare il publisher


Verifica (come testare che tutto funziona)

docker-compose up → tutti i container partono senza errori
pgAdmin accessibile su localhost:5050, si vedono i 3 database
RabbitMQ management su localhost:15672, exchange e code visibili
POST /api/auth/register → 201 Created
POST /api/auth/login → 200 con JWT token
POST /api/documents con JWT → 201, documento salvato
GET /api/documents con JWT → lista documenti dell'utente
GET /api/notifications con JWT → notifica di upload completato presente
DELETE /api/documents/{id} → documento rimosso
Test unitari passano: mvn test