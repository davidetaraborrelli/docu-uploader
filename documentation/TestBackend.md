# Test Plan – Piattaforma Upload Documenti

## 1. Introduzione

Questo documento descrive i test previsti per verificare il corretto funzionamento del sistema.

I test coprono le principali funzionalità del sistema:
- gestione utenti
- gestione documenti
- ricerca semantica
- notifiche asincrone
- integrazione con database

I test verificano il comportamento del backend e della persistenza dei dati nei database.

## 2. Gestione utenti

### TEST-01 – Registrazione utente


**Descrizione:**  
Verificare che il sistema consenta la registrazione di un nuovo utente.

**Precondizioni:**
- Il sistema è attivo
- L'utente non è registrato

**Passi del test:**

1. Inviare una richiesta di registrazione con email e password valide
2. Il sistema valida i dati
3. Il sistema salva l'utente nel database

**Risultato atteso:**

- L'utente viene registrato correttamente
- L'utente viene salvato nel database PostgreSQL
- Il sistema restituisce risposta di successo

### TEST-02 – Autenticazione utente


**Descrizione:**  
Verificare che un utente registrato possa autenticarsi nel sistema.

**Precondizioni:**

- L'utente è registrato nel sistema

**Passi del test:**

1. Inviare una richiesta di login con email e password
2. Il sistema verifica le credenziali
3. Il sistema genera un token di autenticazione

**Risultato atteso:**

- L'autenticazione ha successo
- Il sistema restituisce un token valido

## 3. Gestione documenti
### TEST-03 – Upload documento


**Descrizione:**  
Verificare che il sistema consenta il caricamento di documenti testuali.

**Precondizioni:**

- L'utente è autenticato

**Passi del test:**

1. L'utente seleziona un documento
2. L'utente invia il file al sistema
3. Il sistema riceve il documento
4. Il sistema salva il documento nel database MongoDB

**Risultato atteso:**

- Il documento viene salvato correttamente
- I metadati del documento vengono registrati

### TEST-04 – Visualizzazione documenti


**Descrizione:**  
Verificare che l'utente possa visualizzare l'elenco dei documenti caricati.

**Precondizioni:**

- L'utente è autenticato
- Sono presenti documenti caricati

**Passi del test:**

1. L'utente richiede la lista dei documenti
2. Il sistema recupera i documenti dal database

**Risultato atteso:**

- Il sistema restituisce la lista dei documenti
- I metadati dei documenti sono corretti

### TEST-05 – Eliminazione documento


**Descrizione:**  
Verificare che un utente possa eliminare un documento caricato.

**Precondizioni:**

- L'utente è autenticato
- Il documento esiste

**Passi del test:**

1. L'utente seleziona il documento
2. L'utente richiede l'eliminazione
3. Il sistema elimina il documento

**Risultato atteso:**

- Il documento viene rimosso dal database

## 4. Ricerca semantica

### TEST-06 – Ricerca semantica

**Descrizione:**  
Verificare che il sistema consenta di effettuare una ricerca semantica sui documenti.

**Precondizioni:**

- Sono presenti documenti indicizzati
- Gli embedding sono stati generati

**Passi del test:**

1. L'utente inserisce una query di ricerca
2. Il sistema genera l'embedding della query
3. Il sistema interroga il database vettoriale
4. Il sistema restituisce i documenti simili

**Risultato atteso:**

- Il sistema restituisce una lista di documenti ordinati per similarità

## 5. Notifiche asincrone
### TEST-07 – Notifica upload

**Descrizione:**  
Verificare che il sistema invii una notifica al completamento dell'upload di un documento.

**Precondizioni:**

- L'utente è autenticato

**Passi del test:**

1. L'utente carica un documento
2. Il sistema completa l'upload
3. Il sistema genera un evento tramite message broker

**Risultato atteso:**

- L'utente riceve una notifica di upload completato

## 6. Test integrazione database
### TEST-08 – Salvataggio utente nel database

**Descrizione:**

Verificare che gli utenti registrati vengano salvati correttamente nel database PostgreSQL.

**Risultato atteso:**

- I dati dell'utente sono presenti nel database

### TEST-09 – Generazione embedding documento

**Descrizione:**  
Verificare che il sistema generi correttamente l'embedding per un documento caricato.

**Precondizioni:**

- Il documento è stato caricato
- Il servizio di indicizzazione è attivo

**Passi del test:**

1. Caricare un documento testuale
2. Il sistema avvia il processo di indicizzazione
3. Il sistema genera l'embedding del documento
4. L'embedding viene salvato nel database vettoriale

**Risultato atteso:**

- L'embedding del documento viene generato correttamente
- L'embedding viene salvato nel database vettoriale