package model;

import java.io.Serializable;

/**
 * Datenklasse fuer eine Bestellposition.
 * Die Attribute entsprechen exakt der Tabelle BestellPosition aus dem relationalen Schema.
 * Der Einzelpreis wird beim Bestellen gespeichert, damit spaetere
 * Preisaenderungen am Artikel alte Bestellungen nicht veraendern.
 */
public class BestellPosition implements Serializable {

    private int bestellNr;
    private int artikelNr;
    private int menge;
    private double einzelpreis;

    public BestellPosition(int bestellNr, int artikelNr, int menge, double einzelpreis) {
        this.bestellNr = bestellNr;
        this.artikelNr = artikelNr;
        this.menge = menge;
        this.einzelpreis = einzelpreis;
    }

    public int getBestellNr() {
        return bestellNr;
    }

    public void setBestellNr(int bestellNr) {
        this.bestellNr = bestellNr;
    }

    public int getArtikelNr() {
        return artikelNr;
    }

    public int getMenge() {
        return menge;
    }

    public double getEinzelpreis() {
        return einzelpreis;
    }
}
