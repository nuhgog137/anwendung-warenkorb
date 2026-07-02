package praesentation;

import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import anwendung.Bestelldienst;
import modell.Kunde;
import modell.Artikel;
import modell.Position;
import modell.Bestellbestaetigung;

// Die Präsentationsschicht. Die vier Methoden zeigeWarenkorb(), zeigeFormular(),
// zeigeBestaetigung() und zeigeStornierung() entsprechen genau den vier Kästen
// "Warenkorb-Ansicht", "Registrierungsformular", "Bestellbestätigung" und
// "Stornierungsseite" aus dem Architekturdiagramm. In diesem einfachen
// Konsolenprogramm sind es Methoden einer einzigen Klasse statt eigener Fenster.
public class Client
{
    private static Bestelldienst dienst;
    private static Scanner tastatur = new Scanner(System.in);

    public static void main(String[] args)
    {
        try
        {
            dienst = (Bestelldienst) Naming.lookup("rmi://localhost/Bestelldienst");

            List<Position> warenkorb = zeigeWarenkorb();

            System.out.print("Ihre Email: ");
            String email = tastatur.nextLine();

            Kunde kunde;
            if (dienst.pruefeRegistrierung(email))
            {
                kunde = dienst.holeKunde(email);
                System.out.println("Willkommen zurück, " + kunde.getName() + "!");
            }
            else
            {
                kunde = zeigeFormular(email);
            }

            Bestellbestaetigung bestaetigung = dienst.erstelleBestellung(kunde.getKundenId(), kunde.getEmail(), warenkorb);
            zeigeBestaetigung(bestaetigung);

            zeigeStornierung(bestaetigung);
        }
        catch (Exception e)
        {
            System.out.println("Fehler im Client: " + e.getMessage());
        }
    }

    // Entspricht der "Warenkorb-Ansicht": zeigt die Artikel an und lässt den
    // Kunden Artikelnummer und Menge eingeben, bis er 0 eingibt.
    private static List<Position> zeigeWarenkorb() throws Exception
    {
        System.out.println("Verfügbare Artikel:");
        List<Artikel> artikelliste = dienst.alleArtikel();
        for (Artikel a : artikelliste)
        {
            System.out.println(a.getArtikelNr() + ": " + a.getBezeichnung()
                    + " (" + a.getEinzelpreis() + " Euro, Lagerbestand: " + a.getLagerbestand() + ")");
        }

        List<Position> warenkorb = new ArrayList<Position>();
        while (true)
        {
            System.out.print("Artikelnummer eingeben (0 = fertig): ");
            int nr = Integer.parseInt(tastatur.nextLine());
            if (nr == 0)
            {
                break;
            }
            System.out.print("Menge: ");
            int menge = Integer.parseInt(tastatur.nextLine());
            warenkorb.add(new Position(nr, menge));
        }
        return warenkorb;
    }

    // Entspricht dem "Registrierungsformular": fragt Name, Adresse, Telefon und
    // Passwort ab und prüft, ob die Pflichtangaben vollständig sind. Das Passwort
    // wird nur abgefragt, weil es im Referenzformular steht; da dieses Projekt
    // keine Anmeldung/kein Login hat, wird es nicht gespeichert.
    private static Kunde zeigeFormular(String email) throws Exception
    {
        while (true)
        {
            System.out.println("Sie sind noch nicht registriert. Bitte Daten eingeben:");
            System.out.print("Name: ");
            String name = tastatur.nextLine();
            System.out.print("Telefon: ");
            String telefon = tastatur.nextLine();
            System.out.print("Adresse: ");
            String adresse = tastatur.nextLine();
            System.out.print("Passwort (wird nicht gespeichert): ");
            tastatur.nextLine();

            if (name.isEmpty() || adresse.isEmpty())
            {
                System.out.println("Fehler: Name und Adresse dürfen nicht leer sein. Bitte erneut eingeben.");
                continue; // zurück zu "Kundendaten erfassen", wie im Ablaufdiagramm
            }

            Kunde kunde = dienst.legeKundeAn(name, email, telefon, adresse);
            System.out.println("Sie wurden als Kunde Nr. " + kunde.getKundenId() + " angelegt.");
            return kunde;
        }
    }

    // Entspricht der "Bestellbestätigung": zeigt Bestellnummer, Preis und Versandtermin.
    private static void zeigeBestaetigung(Bestellbestaetigung b)
    {
        System.out.println("=== Bestellbestätigung ===");
        System.out.println("Bestellnummer: " + b.getBestellNr());
        System.out.println("Gesamtpreis: " + b.getGesamtpreis() + " Euro");
        System.out.println("Voraussichtlicher Versandtermin: " + b.getVersandtermin());
    }

    // Entspricht der "Stornierungsseite": fragt, ob der Kunde bis zum Versandtermin
    // stornieren möchte, und ruft je nach Antwort storniereBestellung() oder
    // versendeBestellung() auf.
    private static void zeigeStornierung(Bestellbestaetigung b) throws Exception
    {
        System.out.print("Möchten Sie bis zum Versandtermin (" + b.getVersandtermin() + ") stornieren? (j/n): ");
        String antwort = tastatur.nextLine();
        if (antwort.equals("j"))
        {
            bestaetigeStornierung(b.getBestellNr());
        }
        else
        {
            dienst.versendeBestellung(b.getBestellNr());
            System.out.println("Die Ware wird verschickt. Vielen Dank!");
        }
    }

    // Bestätigt die Stornierung und schließt den Prozess ab.
    private static void bestaetigeStornierung(int bestellNr) throws Exception
    {
        dienst.storniereBestellung(bestellNr);
        System.out.println("Ihre Bestellung wurde storniert.");
    }
}
