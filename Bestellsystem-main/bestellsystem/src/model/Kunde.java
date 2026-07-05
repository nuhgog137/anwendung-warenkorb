package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Datenklasse fuer einen Kunden.
 * Die Attribute entsprechen exakt der Tabelle Kunde aus dem relationalen Schema.
 * Serializable, damit Objekte per RMI zwischen Client und Server uebertragen werden koennen.
 */
public class Kunde implements Serializable {

    private int kundenID;
    private String name;
    private String vorname;
    private String email;
    private String telefon;
    private String strasse;
    private String plz;
    private String ort;
    private LocalDate registrierdatum;

    public Kunde(int kundenID, String name, String vorname, String email, String telefon,
                 String strasse, String plz, String ort, LocalDate registrierdatum) {
        this.kundenID = kundenID;
        this.name = name;
        this.vorname = vorname;
        this.email = email;
        this.telefon = telefon;
        this.strasse = strasse;
        this.plz = plz;
        this.ort = ort;
        this.registrierdatum = registrierdatum;
    }

    public int getKundenID() {
        return kundenID;
    }

    public void setKundenID(int kundenID) {
        this.kundenID = kundenID;
    }

    public String getName() {
        return name;
    }

    public String getVorname() {
        return vorname;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getStrasse() {
        return strasse;
    }

    public String getPlz() {
        return plz;
    }

    public String getOrt() {
        return ort;
    }

    public LocalDate getRegistrierdatum() {
        return registrierdatum;
    }
}
