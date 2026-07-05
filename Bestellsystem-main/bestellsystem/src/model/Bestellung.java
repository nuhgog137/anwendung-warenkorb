package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Datenklasse fuer eine Bestellung.
 * Die Attribute entsprechen exakt der Tabelle Bestellung aus dem relationalen Schema.
 * Zusaetzlich haelt die Bestellung ihre Positionen (Beziehung zur Tabelle BestellPosition).
 */
public class Bestellung implements Serializable {

    private int bestellNr;
    private int kundenID;
    private LocalDate bestelldatum;
    private double gesamtpreis;
    private LocalDate versandtermin;
    private String status; // "Offen", "Storniert" oder "Versendet"

    // Positionen der Bestellung (typisierte Collection, siehe Vorlesung "Generics")
    private List<BestellPosition> positionen = new ArrayList<>();

    public Bestellung(int bestellNr, int kundenID, LocalDate bestelldatum,
                      double gesamtpreis, LocalDate versandtermin, String status) {
        this.bestellNr = bestellNr;
        this.kundenID = kundenID;
        this.bestelldatum = bestelldatum;
        this.gesamtpreis = gesamtpreis;
        this.versandtermin = versandtermin;
        this.status = status;
    }

    public int getBestellNr() {
        return bestellNr;
    }

    public void setBestellNr(int bestellNr) {
        this.bestellNr = bestellNr;
    }

    public int getKundenID() {
        return kundenID;
    }

    public LocalDate getBestelldatum() {
        return bestelldatum;
    }

    public double getGesamtpreis() {
        return gesamtpreis;
    }

    public LocalDate getVersandtermin() {
        return versandtermin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BestellPosition> getPositionen() {
        return positionen;
    }
}
