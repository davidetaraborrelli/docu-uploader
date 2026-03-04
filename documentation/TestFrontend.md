# Test Plan – Piattaforma Upload Documenti

## 1. Introduzione

Questo documento descrive i test relativi all'interfaccia web dell'applicazione.
I test verificano il corretto funzionamento delle pagine, dei form e
dell'interazione tra frontend e backend.

Le funzionalità testate includono:

- registrazione utente
- login
- upload documenti
- visualizzazione documenti
- ricerca semantica
- notifiche di sistema

# 2. Autenticazione

### TEST-01 – Visualizzazione pagina di registrazione
**Descrizione:**  
Verificare che la pagina di registrazione venga visualizzata correttamente.

**Precondizioni:**  
L'utente accede alla piattaforma web.

**Passi del test:**

1. Aprire la pagina di registrazione
2. Verificare la presenza dei campi richiesti

**Risultato atteso:**

- Sono presenti i campi email e password
- È presente il pulsante di registrazione
- La pagina viene visualizzata senza errori

### TEST-02 - Invio form di registrazione
**Descrizione:**  
Verificare che il form di registrazione invii correttamente i dati al backend.

**Precondizioni:**

- L'utente si trova nella pagina di registrazione

**Passi del test:**

1. Inserire email valida
2. Inserire password valida
3. Premere il pulsante di registrazione

**Risultato atteso:**

- Il frontend invia la richiesta al backend
- L'utente viene registrato correttamente
- Il sistema mostra un messaggio di successo

### TEST-03 - Login utente
**Descrizione:**  
Verificare che l'utente possa effettuare il login tramite l'interfaccia web.

**Precondizioni:**

- L'utente è registrato

**Passi del test:**

1. Aprire la pagina di login
2. Inserire email e password
3. Premere il pulsante di login

**Risultato atteso:**

- Il frontend invia la richiesta di autenticazione
- Il login viene effettuato con successo
- L'utente viene reindirizzato alla dashboard

## 3. Gestione documenti
### TEST-04 - Visualizzazione pagina documenti
**Descrizione:**  
Verificare che la pagina dei documenti venga caricata correttamente.

**Precondizioni:**

- L'utente è autenticato

**Passi del test:**

1. Accedere alla pagina documenti

**Risultato atteso:**

- La pagina mostra l'elenco dei documenti
- Sono visibili i metadati dei documenti

### TEST-05 - Upload documento
**Descrizione:**  
Verificare che l'utente possa caricare un documento tramite l'interfaccia web.

**Precondizioni:**

- L'utente è autenticato

**Passi del test:**

1. Accedere alla pagina di upload
2. Selezionare un file
3. Premere il pulsante di caricamento

**Risultato atteso:**

- Il frontend invia il file al backend
- Il sistema mostra un messaggio di upload completato

### TEST-06 - Eliminazione documento
**Descrizione:**  
Verificare che un documento possa essere eliminato tramite l'interfaccia.

**Precondizioni:**

- Il documento esiste
- L'utente è autenticato

**Passi del test:**

1. Aprire la lista documenti
2. Selezionare un documento
3. Premere il pulsante elimina

**Risultato atteso:**

- Il frontend invia la richiesta di eliminazione
- Il documento viene rimosso dalla lista

## 4. Ricerca semantica
### TEST-07 - Inserimento query
**Descrizione:**  
Verificare che l'utente possa inserire una query di ricerca.

**Passi del test:**

1. Accedere alla pagina di ricerca
2. Inserire una query testuale
3. Premere il pulsante di ricerca

**Risultato atteso:**

- Il frontend invia la query al backend
- La richiesta di ricerca viene elaborata

### TEST-08 - Visualizzazione risultati
**Descrizione:**  
Verificare che i risultati della ricerca vengano visualizzati correttamente.

**Precondizioni:**

- La ricerca restituisce risultati

**Passi del test:**

1. Effettuare una ricerca
2. Attendere i risultati

**Risultato atteso:**

- Il sistema mostra una lista di documenti
- I documenti sono ordinati per rilevanza

## 5. Notifiche
### TEST-09 - Notifica upload completato
**Descrizione:**  
Verificare che l'utente riceva una notifica al completamento dell'upload.

**Passi del test:**

1. Caricare un documento
2. Attendere la risposta del sistema

**Risultato atteso:**

- Il sistema mostra una notifica di upload completato

### TEST-10 - Aggiornamento stato documento
**Descrizione:**  
Verificare che lo stato del documento venga aggiornato dopo l'indicizzazione.

**Passi del test:**

1. Caricare un documento
2. Attendere il completamento dell'indicizzazione

**Risultato atteso:**

- Lo stato del documento cambia
- Il frontend aggiorna l'interfaccia


### TEST-11 – Validazione form registrazione

**Descrizione:**  
Verificare che il form di registrazione mostri errori se i dati inseriti non sono validi.

**Precondizioni:**

- L'utente è nella pagina di registrazione

**Passi del test:**

1. Inserire un'email non valida
2. Inserire una password vuota
3. Premere il pulsante di registrazione

**Risultato atteso:**

- Il sistema mostra messaggi di errore
- Il form non viene inviato al backend