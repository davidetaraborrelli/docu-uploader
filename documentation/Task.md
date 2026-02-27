## TASK GROUP 1 – Gestione Utenti

### TASK 1.1 – Registrazione utenti
- Implementare endpoint di registrazione utente  
- Validare i dati inseriti (email, password, campi obbligatori)  
- Salvare l’utente nel database relazionale  

**FR coperti:** FR-01  

---

### TASK 1.2 – Autenticazione utenti
- Implementare login con credenziali  
- Gestire token/sessione di autenticazione  
- Consentire l’accesso alle funzionalità solo ad utenti autenticati  

**FR coperti:** FR-02  

---

### TASK 1.3 – Associazione operazioni all’utente
- Associare ogni operazione all’utente autenticato  
- Tracciare l’identità dell’utente nelle operazioni di sistema  
- Verificare l’autorizzazione dell’utente alle operazioni richieste  

**FR coperti:** FR-03  

---

## TASK GROUP 2 – Gestione Documenti

### TASK 2.1 – Upload documenti
- Implementare endpoint di upload documenti  
- Consentire il caricamento di documenti testuali  
- Validare formato e dimensione dei file  

**FR coperti:** FR-04  

---

### TASK 2.2 – Persistenza documenti
- Salvare i documenti nel database NoSQL  
- Gestire i metadati del documento (nome, data, utente)  

**FR coperti:** FR-05  

---

### TASK 2.3 – Associazione documento–utente
- Associare ogni documento all’utente che lo ha caricato  
- Verificare l’ownership del documento  

**FR coperti:** FR-06  

---

### TASK 2.4 – Visualizzazione elenco documenti
- Recuperare l’elenco dei documenti caricati dall’utente  
- Visualizzare i metadati dei documenti  

**FR coperti:** FR-07  

---

### TASK 2.5 – Visualizzazione contenuto documento
- Recuperare il contenuto del documento  
- Visualizzare il testo del documento selezionato  

**FR coperti:** FR-08  

---

### TASK 2.6 – Eliminazione documenti
- Eliminare documenti caricati dall’utente  
- Aggiornare lo stato del sistema dopo la cancellazione  

**FR coperti:** FR-09  

---

## TASK GROUP 3 – Indicizzazione e Generazione degli Embedding

### TASK 3.1 – Estrazione contenuto testuale
- Estrarre il contenuto testuale dai documenti caricati  
- Preparare il testo per l’elaborazione  

**FR coperti:** FR-10  

---

### TASK 3.2 – Generazione embedding documento
- Generare embedding vettoriali per ogni documento  
- Gestire eventuali errori di generazione  

**FR coperti:** FR-11  

---

### TASK 3.3 – Persistenza embedding
- Salvare gli embedding nel database vettoriale  
- Associare embedding al documento originale  

**FR coperti:** FR-12  

---

### TASK 3.4 – Elaborazione asincrona indicizzazione
- Avviare il processo di indicizzazione in modo asincrono  
- Separare upload e indicizzazione tramite message broker  

**FR coperti:** FR-13  

---

## TASK GROUP 4 – Ricerca Semantica

### TASK 4.1 – Inserimento query di ricerca
- Consentire all’utente di inserire una query testuale  
- Validare l’input della query  

**FR coperti:** FR-14  

---

### TASK 4.2 – Generazione embedding della query
- Generare embedding vettoriale a partire dalla query  
- Preparare la query per la ricerca semantica  

**FR coperti:** FR-15  

---

### TASK 4.3 – Interrogazione database vettoriale
- Interrogare il database vettoriale tramite API REST  
- Recuperare i risultati di similarità  

**FR coperti:** FR-16  

---

### TASK 4.4 – Ordinamento risultati
- Calcolare il grado di similarità  
- Ordinare i documenti per rilevanza  
- Restituire la lista ordinata all’utente  

**FR coperti:** FR-17  

---

## TASK GROUP 5 – Notifiche Asincrone

### TASK 5.1 – Notifica completamento upload
- Generare evento di upload completato  
- Inviare notifica all’utente  

**FR coperti:** FR-18  

---

### TASK 5.2 – Notifica completamento indicizzazione
- Generare evento di indicizzazione completata  
- Inviare notifica all’utente  

**FR coperti:** FR-19  

---

### TASK 5.3 – Integrazione messaggistica asincrona
- Configurare il message broker (RabbitMQ o Kafka)  
- Gestire producer e consumer dei messaggi  

**FR coperti:** FR-20  



# Suddivisione Task per Ruolo

---

## 🔧 TASK BACKEND

### Backend – Gestione Utenti
- **TASK 1.1** – Implementare endpoint di registrazione utenti  
- **TASK 1.2** – Implementare login e gestione autenticazione  
- **TASK 1.2** – Gestire token/sessioni di autenticazione  
- **TASK 1.3** – Associare ogni operazione all’utente autenticato  
- **TASK 1.3** – Applicare controlli di autorizzazione alle operazioni  

---

### Backend – Gestione Documenti
- **TASK 2.1** – Implementare endpoint di upload documenti  
- **TASK 2.1** – Validare formato e dimensione dei documenti  
- **TASK 2.4** – Implementare endpoint per recupero elenco documenti  
- **TASK 2.5** – Implementare endpoint per visualizzazione contenuto documento  
- **TASK 2.6** – Implementare endpoint di eliminazione documenti  
- **TASK 2.3** – Verificare ownership dei documenti  
- **TASK 2.2** – Gestire persistenza documenti su DB NoSQL  

---

### Backend – Indicizzazione ed Embedding
- **TASK 3.1** – Estrarre contenuto testuale dai documenti  
- **TASK 3.2** – Integrare servizio di generazione embedding  
- **TASK 3.3** – Salvare embedding nel database vettoriale  
- **TASK 3.4** – Implementare producer eventi di indicizzazione  
- **TASK 3.4** – Implementare consumer per elaborazione asincrona  

---

### Backend – Ricerca Semantica
- **TASK 4.1** – Implementare endpoint di ricerca semantica  
- **TASK 4.2** – Generare embedding a partire dalla query utente  
- **TASK 4.3** – Interrogare il database vettoriale tramite API REST  
- **TASK 4.4** – Ordinare e aggregare i risultati per similarità  

---

### Backend – Notifiche Asincrone
- **TASK 5.1** – Pubblicare evento di upload completato  
- **TASK 5.2** – Pubblicare evento di indicizzazione completata  
- **TASK 5.3** – Integrare message broker (RabbitMQ/Kafka)  
- **TASK 5.3** – Gestire consumer delle notifiche  

---

## 🎨 TASK FRONTEND

### Frontend – Autenticazione
- **TASK 1.1** – Realizzare interfaccia di registrazione utenti  
- **TASK 1.2** – Realizzare interfaccia di login  
- **TASK 1.2** – Gestire stato di autenticazione utente  
- **TASK 1.3** – Proteggere le viste riservate agli utenti autenticati  

---

### Frontend – Gestione Documenti
- **TASK 2.1** – Realizzare form di upload documenti  
- **TASK 2.4** – Visualizzare elenco documenti dell’utente  
- **TASK 2.5** – Visualizzare contenuto del documento  
- **TASK 2.6** – Consentire eliminazione documenti  
- **TASK 2.6** – Gestire feedback utente (successo/errore)  

---

### Frontend – Ricerca Semantica
- **TASK 4.1** – Realizzare interfaccia per inserimento query  
- **TASK 4.4** – Visualizzare risultati ordinati per rilevanza  
- **TASK 4.4** – Mostrare metadati e anteprima documenti  

---

### Frontend – Notifiche
- **TASK 5.1** – Visualizzare notifica di upload completato  
- **TASK 5.2** – Visualizzare notifica di indicizzazione completata  
- **TASK 5.3** – Aggiornare dinamicamente lo stato dei documenti  

---

## 🗄️ TASK DATABASE

### Database Relazionale (PostgreSQL)
- **TASK 1.1** – Progettare schema utenti  
- **TASK 1.2** – Gestire dati di autenticazione  
- **TASK 1.3** – Gestire associazione utenti–operazioni  
- **TASK 2.3** – Gestire associazione utenti–documenti  

---

### Database NoSQL (MongoDB)
- **TASK 2.2** – Progettare schema documenti  
- **TASK 2.2** – Salvare contenuto testuale e metadati  
- **TASK 2.6** – Eliminare documenti dal database  

---

### Database Vettoriale
- **TASK 3.3** – Definire schema e index degli embedding  
- **TASK 3.3** – Salvare embedding associati ai documenti  
- **TASK 4.3** – Supportare query di similarità vettoriale  

---

### Message Broker (RabbitMQ / Kafka)
- **TASK 3.4** – Gestire eventi di indicizzazione asincrona  
- **TASK 5.3** – Gestire eventi di notifica asincrona  
