# Domande emerse dall’analisi dei requisiti


## Domande sul dominio e sul contesto applicativo

1. Quali formati di documenti testuali sono supportati dal sistema (ad esempio `.txt`, `.pdf`, `.docx`)?
2. Sono previsti limiti sulla dimensione massima dei documenti caricabili?
3. I documenti caricati dagli utenti sono visibili solo al proprietario o possono essere condivisi con altri utenti?
4. Il sistema è pensato per un contesto di utilizzo specifico (accademico, aziendale, generico) o per un pubblico generico?

---

## Domande sugli attori del sistema

5. È stato individuato un unico attore. È neccessaria anche una distinzione di ruoli o livelli di autenticazione?  
6. È prevista la possibilità per un utente di modificare un documento già caricato o solo di cancellarlo e ricaricarlo?

---

## Domande sui requisiti funzionali – Gestione documenti

7. La cancellazione di un documento comporta anche la rimozione degli embedding associati dal database vettoriale?

---

## Domande su indicizzazione ed embedding

8. Cosa accade se il processo di generazione degli embedding fallisce?
9. In caso di modifica di un documento, gli embedding vengono rigenerati o rimangono invariati?

---

## Domande sulla ricerca semantica

10. La ricerca supporta esclusivamente un approccio semantico o anche una ricerca testuale tradizionale?

---

## Domande sulle notifiche asincrone

11. In che modo le notifiche di sistema vengono rese disponibili all’utente (interfaccia web, email, altro)?
12. Le notifiche vengono salvate o sono solo eventi temporanei?
