package modell;

import java.io.Serializable;

// Diese Klasse beschreibt einen Kunden.
// Die Felder entsprechen genau dem Datenmodell: KundenID, Name, Email, Telefon, Adresse.
// Sie ist "Serializable", weil das Objekt über RMI vom Server zum Client geschickt wird.
public class Kunde implements Serializable
{
    private int kundenId;
    private String name;
    private String email;
    private String telefon;
    private String adresse;

    public Kunde(int kundenId, String name, String email, String telefon, String adresse)
    {
        this.kundenId = kundenId;
        this.name = name;
        this.email = email;
        this.telefon = telefon;
        this.adresse = adresse;
    }

    public int getKundenId()
    {
        return kundenId;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public String getTelefon()
    {
        return telefon;
    }

    public String getAdresse()
    {
        return adresse;
    }
}
