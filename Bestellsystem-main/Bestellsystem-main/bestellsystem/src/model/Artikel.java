package model;

import java.io.Serializable;

/**
 * Datenklasse fuer einen Artikel.
 * Die Attribute entsprechen exakt der Tabelle Artikel aus dem relationalen Schema.
 */
public class Artikel implements Serializable {

    private int artikelNr;
    private String bezeichnung;
    private double preis;
    private int lagerbestand;
    private int lieferzeitTage;

    public Artikel(int artikelNr, String bezeichnung, double preis, int lagerbestand, int lieferzeitTage) {
        this.artikelNr = artikelNr;
        this.bezeichnung = bezeichnung;
        this.preis = preis;
        this.lagerbestand = lagerbestand;
        this.lieferzeitTage = lieferzeitTage;
    }

    public int getArtikelNr() {
        return artikelNr;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public double getPreis() {
        return preis;
    }

    public int getLagerbestand() {
        return lagerbestand;
    }

    public int getLieferzeitTage() {
        return lieferzeitTage;
    }
}
