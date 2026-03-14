package com.davidetaraborrelli.common.dto;

/**
 * Dati utente estratti dal JWT token.
 * Usato da tutti i servizi per identificare l'utente autenticato.
 */
public record UserInfo(Long id, String username, String email) {
}
