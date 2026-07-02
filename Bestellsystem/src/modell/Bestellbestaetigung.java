package modell;

import java.io.Serializable;

// Die Bestellbestätigung wird dem Kunden am Ende der Bestellung gezeigt.
// Sie enthält Bestellnummer, Gesamtpreis und den voraussichtlichen Versandtermin.
public class Bestellbestaetigung implements Serializable
{
    private int bestellNr;
    private double gesamtpreis;
    private String versandtermin;

    public Bestellbestaetigung(int bestellNr, double gesamtpreis, String versandtermin)
    {
        this.bestellNr = bestellNr;
        this.gesamtpreis = gesamtpreis;
        this.versandtermin = versandtermin;
    }

    public int getBestellNr()
    {
        return bestellNr;
    }

    public double getGesamtpreis()
    {
        return gesamtpreis;
    }

    public String getVersandtermin()
    {
        return versandtermin;
    }
}
