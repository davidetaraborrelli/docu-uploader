# Piattaforma Web per Upload Documenti


# 1. Introduzione

## 1.1 Scopo del documento

Il presente documento di analisi ha lo scopo di descrivere in modo formale e strutturato i requisiti del sistema software da realizzare, a partire dal documento di elicitazione fornito.

In particolare, il documento si pone i seguenti obiettivi:

- comprendere il dominio applicativo di riferimento;
- identificare gli attori coinvolti e le loro interazioni con il sistema;
- definire i requisiti funzionali del sistema;
- individuare i requisiti non funzionali e i vincoli tecnologici.

Il documento di analisi rappresenta quindi un punto di riferimento fondamentale per garantire coerenza tra le esigenze iniziali e le soluzioni implementative adottate.

---

## 1.2 Descrizione generale del sistema

Il sistema oggetto di analisi è una piattaforma web progettata per la gestione e l’analisi di documenti testuali.

Le principali funzionalità offerte dal sistema includono:

- upload e gestione di documenti testuali;
- indicizzazione semantica dei contenuti tramite embedding vettoriali;
- ricerca avanzata e semantica sui documenti caricati;
- gestione degli utenti;
- invio di notifiche di sistema in modalità asincrona.

Il sistema è sviluppato secondo un’architettura a microservizi, al fine di garantire scalabilità, manutenibilità e separazione delle responsabilità.

L’infrastruttura tecnologica prevede l’integrazione dei seguenti componenti:

- **Database relazionale** (PostgreSQL);
- **Database NoSQL** (MongoDB);
- **Database vettoriale** accessibile tramite API REST;
- **Message Broker** (RabbitMQ o Kafka);
- pratiche **DevOps** (containerizzazione, CI/CD, test automatici).

# 2. Analisi del dominio applicativo

## 2.1 Contesto applicativo

Il dominio applicativo del sistema riguarda la gestione e la consultazione di documenti testuali tramite una piattaforma web.  
Il sistema consente agli utenti di caricare documenti, conservarli in formato digitale e accedere ai contenuti attraverso funzionalità di ricerca.

Il contesto di utilizzo è caratterizzato dalla presenza di documenti testuali che devono essere organizzati e resi consultabili tramite un’interfaccia web, supportando operazioni di caricamento, archiviazione e recupero delle informazioni.

---

## 2.2 Problema da risolvere

Il problema affrontato dal sistema riguarda la gestione e la consultazione di documenti testuali tramite una piattaforma web.  
In particolare, il sistema deve consentire agli utenti di caricare documenti e di effettuare ricerche sui contenuti testuali utilizzando un approccio di tipo semantico.

La ricerca semantica richiede che i documenti siano rappresentati attraverso embedding vettoriali, in modo da permettere il confronto tra i contenuti dei documenti e le query di ricerca sulla base del significato e non della sola corrispondenza testuale.

Il sistema deve inoltre supportare la gestione dei documenti e la generazione di notifiche di sistema in modo asincrono, in coerenza con le funzionalità richieste nella fase di elicitazione.

---

## 2.3 Tipologia di dati gestiti

Il sistema gestisce diverse tipologie di dati, tra cui:

- documenti testuali caricati dagli utenti;
- metadati associati ai documenti (titolo, autore, data di caricamento, ecc.);
- rappresentazioni vettoriali dei contenuti testuali (embedding);
- informazioni relative agli utenti del sistema;
- eventi e notifiche di sistema generate in modo asincrono.

La corretta gestione e correlazione di tali dati è fondamentale per garantire il funzionamento efficace del sistema.

---

## 2.4 Obiettivi generali del dominio

Gli obiettivi principali del dominio applicativo sono:

- consentire il caricamento e la gestione di documenti testuali tramite una piattaforma web;
- permettere la ricerca dei documenti utilizzando un approccio semantico;
- supportare l’accesso multiutente al sistema;
- gestire la generazione e la distribuzione di notifiche di sistema in modalità asincrona.

Questi obiettivi definiscono il contesto concettuale entro il quale verranno individuati e descritti i requisiti funzionali e non funzionali del sistema.


# 3. Attori del sistema

## 3.1 Utente

L’attore principale del sistema è l’Utente.  
L’Utente rappresenta qualsiasi soggetto che accede alla piattaforma web per interagire con le funzionalità messe a disposizione dal sistema.

L’Utente può eseguire operazioni di caricamento e gestione dei documenti testuali, effettuare ricerche semantiche sui contenuti disponibili e ricevere notifiche generate dal sistema.  
Tutti gli utenti del sistema dispongono delle stesse funzionalità e non sono previsti ruoli o livelli di autorizzazione differenti.

L’interazione tra l’Utente e il sistema avviene tramite un’interfaccia web.


# 4. Requisiti funzionali

La presente sezione descrive i requisiti funzionali del sistema, individuati a partire dall’analisi del dominio applicativo e dagli attori del sistema.

---

## 4.1 Gestione utenti

| ID  | Nome requisito              | Descrizione                                                                 |
|-------|-----------------------------|------------------------------------------------------------------------------|
| FR-01 | Registrazione utenti        | Il sistema deve consentire la registrazione di nuovi utenti.                |
| FR-02 | Autenticazione utenti       | Il sistema deve consentire l’autenticazione degli utenti registrati.        |
| FR-03 | Associazione operazioni     | Il sistema deve associare le operazioni eseguite agli utenti autenticati.   |

---

## 4.2 Gestione documenti

| ID    | Nome requisito               | Descrizione                                                                 |
|-------|------------------------------|------------------------------------------------------------------------------|
| FR-04 | Upload documenti             | Il sistema deve consentire agli utenti di caricare documenti testuali.      |
| FR-05 | Memorizzazione documenti     | Il sistema deve memorizzare i documenti caricati in un database NoSQL.      |
| FR-06 | Associazione documento-utente| Il sistema deve associare ogni documento all’utente che lo ha caricato.    |
| FR-07 | Elenco documenti             | Il sistema deve consentire la visualizzazione dei documenti caricati.      |
| FR-08 | Visualizzazione documenti    | Il sistema deve consentire la visualizzazione del contenuto dei documenti. |
| FR-09 | Cancellazione documenti      | Il sistema deve consentire la cancellazione dei documenti caricati.        |

---

## 4.3 Indicizzazione e generazione degli embedding

| ID    | Nome requisito                 | Descrizione                                                                 |
|-------|--------------------------------|------------------------------------------------------------------------------|
| FR-10 | Estrazione testo               | Il sistema deve estrarre il contenuto testuale dai documenti caricati.     |
| FR-11 | Generazione embedding          | Il sistema deve generare embedding vettoriali per ciascun documento.       |
| FR-12 | Memorizzazione embedding       | Il sistema deve memorizzare gli embedding nel database vettoriale.         |
| FR-13 | Indicizzazione asincrona       | Il processo di indicizzazione deve avvenire in modo asincrono.             |

---

## 4.4 Ricerca semantica

| ID    | Nome requisito                 | Descrizione                                                                 |
|-------|--------------------------------|------------------------------------------------------------------------------|
| FR-14 | Ricerca semantica               | Il sistema deve consentire la ricerca semantica sui documenti.             |
| FR-15 | Embedding delle query           | Il sistema deve generare embedding a partire dalle query degli utenti.     |
| FR-16 | Interrogazione DB vettoriale    | Il sistema deve interrogare il database vettoriale tramite API REST.       |
| FR-17 | Ordinamento risultati           | Il sistema deve restituire i risultati ordinati per grado di similarità.   |

---

## 4.5 Notifiche asincrone

| ID    | Nome requisito                 | Descrizione                                                                 |
|-------|--------------------------------|------------------------------------------------------------------------------|
| FR-18 | Notifica upload                | Il sistema deve inviare una notifica al completamento dell’upload.         |
| FR-19 | Notifica indicizzazione        | Il sistema deve inviare una notifica al completamento dell’indicizzazione. |
| FR-20 | Messaggistica asincrona        | Le notifiche devono essere gestite tramite un meccanismo asincrono.        |

# 5. Requisiti non funzionali

La presente sezione descrive i requisiti non funzionali (Non-Functional Requirements, NFR) del sistema, intesi come vincoli e caratteristiche di qualità che devono essere rispettati indipendentemente dalle funzionalità offerte.

---

## 5.1 Vincoli tecnologici e architetturali

| ID     | Categoria                     | Requisito |
|--------|-------------------------------|----------|
| NFR-01 | Architettura                  | Il sistema deve essere sviluppato secondo un’architettura a microservizi. |
| NFR-02 | DB Relazionale                | Il sistema deve utilizzare PostgreSQL come DBMS per i dati strutturati. |
| NFR-03 | DB NoSQL                      | Il sistema deve utilizzare MongoDB per la memorizzazione dei documenti. |
| NFR-04 | DB Vettoriale                 | Il sistema deve utilizzare un database vettoriale accessibile tramite API REST. |
| NFR-05 | Messaggistica                 | Il sistema deve utilizzare un message broker (RabbitMQ o Kafka) per la comunicazione asincrona. |
| NFR-06 | Frontend                      | L’interfaccia web deve utilizzare, ove possibile, tecnologie simili a HTMX. |

---

## 5.2 DevOps, CI/CD e containerizzazione

| ID     | Categoria                     | Requisito |
|--------|-------------------------------|----------|
| NFR-07 | DevOps                        | Il progetto deve essere sviluppato seguendo pratiche DevOps. |
| NFR-08 | Containerizzazione            | I componenti del sistema devono essere containerizzati (es. tramite Docker). |
| NFR-09 | CI/CD                         | Deve essere predisposta una pipeline di CI/CD per build, test e rilascio. |
| NFR-10 | Ambienti                      | Devono essere previsti ambienti separati almeno per sviluppo e produzione. |

---

## 5.3 Qualità del software e test

| ID     | Categoria                     | Requisito |
|--------|-------------------------------|----------|
| NFR-11 | Testing                       | Devono essere implementati test automatici per i servizi principali. |
| NFR-12 | Manutenibilità                | Il sistema deve essere progettato per favorire manutenibilità ed evoluzione dei microservizi. |
| NFR-13 | Osservabilità                 | Devono essere disponibili log utili a diagnosticare errori e verificare il comportamento dei servizi. |

---

## 5.4 Sicurezza e gestione accessi

| ID     | Categoria                     | Requisito |
|--------|-------------------------------|----------|
| NFR-14 | Autenticazione                | L’accesso alle funzionalità riservate deve essere consentito solo ad utenti autenticati. |
| NFR-15 | Protezione dati               | I dati degli utenti e i documenti devono essere protetti da accessi non autorizzati. |

---

## 5.5 Prestazioni e scalabilità

| ID     | Categoria                     | Requisito |
|--------|-------------------------------|----------|
| NFR-16 | Scalabilità                   | Il sistema deve poter scalare orizzontalmente almeno a livello di microservizi. |
| NFR-17 | Prestazioni                   | La ricerca semantica deve restituire risultati in tempi compatibili con l’utilizzo web. |
