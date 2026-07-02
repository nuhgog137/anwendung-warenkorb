package modell;

import java.io.Serializable;

// Diese Klasse beschreibt einen Artikel im Shop.
// Die Felder ArtikelNr, Einzelpreis, Lagerbestand und Lieferzeit stehen genau so im Datenmodell.
// "bezeichnung" wurde zusätzlich ergänzt, damit der Artikel beim Anzeigen einen lesbaren
// Namen hat. Das steht nicht im Datenmodell, ist aber für die Anzeige im Client nötig.
public class Artikel implements Serializable
{
    private int artikelNr;
    private String bezeichnung;
    private double einzelpreis;
    private int lagerbestand;
    private int lieferzeit;

    public Artikel(int artikelNr, String bezeichnung, double einzelpreis, int lagerbestand, int lieferzeit)
    {
        this.artikelNr = artikelNr;
        this.bezeichnung = bezeichnung;
        this.einzelpreis = einzelpreis;
        this.lagerbestand = lagerbestand;
        this.lieferzeit = lieferzeit;
    }

    public int getArtikelNr()
    {
        return artikelNr;
    }

    public String getBezeichnung()
    {
        return bezeichnung;
    }

    public double getEinzelpreis()
    {
        return einzelpreis;
    }

    public int getLagerbestand()
    {
        return lagerbestand;
    }

    public int getLieferzeit()
    {
        return lieferzeit;
    }
}
