package anwendung;

// Kümmert sich nur um eine Sache: den Kunden über seine Bestellung zu informieren.
// In diesem einfachen Projekt wird keine echte Email verschickt, sondern nur eine
// Meldung auf der Konsole ausgegeben, um den Ablauf aus dem Referenzdiagramm zu zeigen.
public class BenachrichtigungsService
{
    public void sendeBestellbestaetigung(String email, int bestellNr)
    {
        System.out.println("(Hinweis: Bestellbestätigung für Bestellung " + bestellNr
                + " würde an " + email + " verschickt werden.)");
    }
}
