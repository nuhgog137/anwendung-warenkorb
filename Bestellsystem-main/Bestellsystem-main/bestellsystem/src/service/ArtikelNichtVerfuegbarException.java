package service;

/**
 * Eigene checked Exception (abgeleitet von Exception, siehe Vorlesung Ausnahmenbehandlung).
 * Wird geworfen, wenn ein Artikel nicht in der gewuenschten Menge auf Lager ist.
 */
public class ArtikelNichtVerfuegbarException extends Exception {

    public ArtikelNichtVerfuegbarException() {
        super("Ein Artikel ist nicht in ausreichender Menge verfügbar!");
    }

    public ArtikelNichtVerfuegbarException(String meldung) {
        super(meldung);
    }
}
