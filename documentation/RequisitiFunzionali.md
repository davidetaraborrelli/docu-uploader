# 4. Requisiti funzionali

La presente sezione descrive i requisiti funzionali del sistema, individuati a partire dall’analisi del dominio applicativo e dagli attori del sistema.

---

## 4.1 Gestione utenti

FR-01 – Il sistema deve consentire la registrazione di nuovi utenti.

FR-02 – Il sistema deve consentire l’autenticazione degli utenti registrati.

FR-03 – Il sistema deve associare le operazioni eseguite ai rispettivi utenti autenticati.

---

## 4.2 Gestione documenti

FR-04 – Il sistema deve consentire agli utenti di caricare documenti testuali.

FR-05 – Il sistema deve memorizzare i documenti caricati in un database NoSQL.

FR-06 – Il sistema deve associare ogni documento all’utente che lo ha caricato.

FR-07 – Il sistema deve consentire agli utenti di visualizzare l’elenco dei documenti caricati.

FR-08 – Il sistema deve consentire la visualizzazione dei contenuti dei documenti.

FR-09 – Il sistema deve consentire la cancellazione dei documenti caricati.

---

## 4.3 Indicizzazione e generazione degli embedding

FR-10 – Il sistema deve estrarre il contenuto testuale dai documenti caricati.

FR-11 – Il sistema deve generare una rappresentazione vettoriale (embedding) per ciascun documento.

FR-12 – Il sistema deve memorizzare gli embedding nel database vettoriale.

FR-13 – Il processo di indicizzazione e generazione degli embedding deve avvenire in modo asincrono.

---

## 4.4 Ricerca semantica

FR-14 – Il sistema deve consentire agli utenti di effettuare ricerche semantiche sui documenti.

FR-15 – Il sistema deve generare embedding vettoriali a partire dalle query di ricerca degli utenti.

FR-16 – Il sistema deve interrogare il database vettoriale tramite API REST.

FR-17 – Il sistema deve restituire una lista di documenti ordinata in base al grado di similarità.

---

## 4.5 Notifiche asincrone

FR-18 – Il sistema deve inviare una notifica al completamento del caricamento di un documento.

FR-19 – Il sistema deve inviare una notifica al completamento del processo di indicizzazione.

FR-20 – La gestione delle notifiche deve avvenire tramite un meccanismo di messaggistica asincrona.

