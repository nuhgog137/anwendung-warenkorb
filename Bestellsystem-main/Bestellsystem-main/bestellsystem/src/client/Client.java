package client;

import model.Artikel;
import model.Bestellung;
import model.Kunde;
import rmi.Bestelldienst;
import service.ArtikelNichtVerfuegbarException;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Client der Anwendung (Praesentationsschicht).
 * Liest Benutzereingaben von der Tastatur (Scanner, siehe Vorlesung Ein-/Ausgabe)
 * und kommuniziert ausschliesslich ueber das Remote-Interface mit dem Server.
 * Der Client enthaelt KEINE Geschaeftslogik und KEINE Datenbankzugriffe.
 */
public class Client {

    public static void main(String[] args) throws Exception {

        // Terminal auf UTF-8 umstellen, damit Umlaute (ä, ö, ü) korrekt angezeigt werden
        umlauteAktivieren();

        Scanner eingabe = new Scanner(System.in);

        // Schritt 2: Verbindung zum Server ueber RMI herstellen
        Bestelldienst dienst = (Bestelldienst) Naming.lookup("rmi://localhost:1099/Bestelldienst");
        System.out.println("Verbindung zum Server hergestellt.");
        System.out.println("===== Willkommen im Bestellsystem =====");

        // Schritt 3: Pruefen, ob der Kunde bereits registriert ist
        System.out.print("Bitte geben Sie Ihre E-Mail-Adresse ein: ");
        String email = eingabe.nextLine();

        Kunde kunde;
        if (dienst.istKundeRegistriert(email)) {
            kunde = dienst.findeKunde(email);
            System.out.println("Willkommen zurück, " + kunde.getVorname() + " " + kunde.getName() + "!");
        } else {
            // Schritt 4: Kundenkonto erstellen
            System.out.println("Sie sind noch nicht registriert. Bitte legen Sie ein Konto an.");
            System.out.print("Name: ");
            String name = eingabe.nextLine();
            System.out.print("Vorname: ");
            String vorname = eingabe.nextLine();
            System.out.print("Telefon: ");
            String telefon = eingabe.nextLine();
            System.out.print("Strasse: ");
            String strasse = eingabe.nextLine();
            System.out.print("PLZ: ");
            String plz = eingabe.nextLine();
            System.out.print("Ort: ");
            String ort = eingabe.nextLine();

            // KundenID 0 und Registrierdatum null: beides setzt der Server bzw. die Datenbank
            Kunde neuerKunde = new Kunde(0, name, vorname, email, telefon, strasse, plz, ort, null);
            kunde = dienst.registriereKunde(neuerKunde);
            System.out.println("Registrierung erfolgreich! Ihre Kundennummer: " + kunde.getKundenID());
        }

        // Schritt 5: Artikel anzeigen
        System.out.println("\n===== Verfügbare Artikel =====");
        List<Artikel> artikelListe = dienst.holeAlleArtikel();
        for (Artikel artikel : artikelListe) {
            System.out.println("Nr. " + artikel.getArtikelNr()
                    + " | " + artikel.getBezeichnung()
                    + " | " + String.format("%.2f", artikel.getPreis()) + " EUR"
                    + " | Lagerbestand: " + artikel.getLagerbestand()
                    + " | Lieferzeit: " + artikel.getLieferzeitTage() + " Tage");
        }

        // Schritt 6: Artikel in den Warenkorb legen
        // Der Warenkorb ist eine Map: ArtikelNr -> Menge (Generics, siehe Vorlesung Collections)
        Map<Integer, Integer> warenkorb = new HashMap<>();

        boolean weiterEinkaufen = true;
        while (weiterEinkaufen) {
            int artikelNr = leseZahl(eingabe, "\nArtikelnummer eingeben (oder 0 zum Bestellen): ");

            if (artikelNr == 0) {
                weiterEinkaufen = false;
            } else {
                int menge = leseZahl(eingabe, "Menge: ");

                // Liegt der Artikel schon im Warenkorb, wird die Menge addiert
                if (warenkorb.containsKey(artikelNr)) {
                    menge = menge + warenkorb.get(artikelNr);
                }
                warenkorb.put(artikelNr, menge);
                System.out.println("Artikel " + artikelNr + " (jetzt " + menge + " Stück) im Warenkorb.");
            }
        }

        if (warenkorb.isEmpty()) {
            System.out.println("Der Warenkorb ist leer. Programm wird beendet.");
            return;
        }

        // Schritte 7-12: Bestellung ausloesen
        // (Bestellnummer, Gesamtpreis, Verfuegbarkeit, Versandtermin, Speichern macht der Server)
        Bestellung bestellung;
        try {
            bestellung = dienst.loeseBestellungAus(kunde.getKundenID(), warenkorb);
        } catch (ArtikelNichtVerfuegbarException e) {
            System.out.println("Bestellung nicht möglich: " + e.getMessage());
            return;
        }

        // Schritt 13: Bestellbestaetigung anzeigen
        System.out.println("\n===== Bestellbestaetigung =====");
        System.out.println("Bestellnummer: " + bestellung.getBestellNr());
        System.out.println("Gesamtpreis:   " + String.format("%.2f", bestellung.getGesamtpreis()) + " EUR");
        System.out.println("Versandtermin: " + bestellung.getVersandtermin());

        // Schritt 14: Bis zum Versandtermin darf storniert werden
        System.out.print("\nMöchten Sie die Bestellung stornieren? (j/n): ");
        String antwort = eingabe.nextLine();

        if (antwort.equalsIgnoreCase("j")) {
            boolean erfolgreich = dienst.storniereBestellung(bestellung);
            if (erfolgreich) {
                System.out.println("Ihre Bestellung wurde storniert.");
            } else {
                System.out.println("Stornierung nicht mehr möglich, der Versandtermin ist erreicht.");
            }
        } else {
            // Schritt 15: Keine Stornierung -> Bestellung wird als "Versendet" markiert
            dienst.versendeBestellung(bestellung);
            System.out.println("Ihre Bestellung wurde versendet. Vielen Dank für Ihren Einkauf!");
        }
    }

    /**
     * Liest eine ganze Zahl von der Tastatur ein.
     * Bei einer ungueltigen Eingabe (z.B. Buchstaben) wird die Frage wiederholt,
     * statt dass das Programm mit einer Exception abstuerzt.
     */
    private static int leseZahl(Scanner eingabe, String frage) {
        while (true) {
            System.out.print(frage);
            String text = eingabe.nextLine();
            try {
                return Integer.parseInt(text.trim());
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe, bitte eine Zahl eingeben.");
            }
        }
    }

    /**
     * Stellt die Konsole auf UTF-8 um (Windows-Konsolen nutzen sonst eine andere
     * Codepage, wodurch Umlaute wie ae/oe/ue statt ä/ö/ü angezeigt werden).
     */
    private static void umlauteAktivieren() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "chcp 65001 >nul").inheritIO().start().waitFor();
            }
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(System.err, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            // Falls die Konsole nicht umgestellt werden kann, laeuft das Programm trotzdem weiter
        }
    }
}
