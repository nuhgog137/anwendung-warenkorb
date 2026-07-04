# Bestellsystem (Java RMI + JDBC + MariaDB)

Einfache Client-Server-Anwendung fuer den Bestellprozess.
Vollstaendig getestet: Registrierung, Bestellung, Stornierung,
Verfuegbarkeitspruefung, Versandterminberechnung.

## Architektur (Schichten duerfen nicht uebersprungen werden)

    Client -> RMI -> Service -> DAO -> Datenbank

## Packages

| Package   | Aufgabe                                              |
|-----------|------------------------------------------------------|
| client    | Menues, Tastatureingaben, Aufrufe an den Server      |
| server    | Startet die RMI-Registry und den Dienst              |
| rmi       | Remote-Interface (Bestelldienst) + Implementierung   |
| model     | Kunde, Artikel, Bestellung, BestellPosition          |
| datenhaltung       | Datenbankzugriffe mit JDBC (PreparedStatement)       |
| service   | Geschaeftslogik (BestellService) + eigene Exception  |
| database  | Zentrale Datenbankverbindung                         |

## Einrichtung (einmalig)

1. Datenbank-Schema einspielen:

       mysql -u root -p < sql/schema.sql

2. MariaDB-Treiber in den Ordner lib/ legen (siehe lib/HIER_TREIBER_ABLEGEN.txt)

3. Passwort pruefen in: src/database/DatenbankVerbindung.java

4. Ordner in VS Code oeffnen und warten, bis unten "Java: Ready" steht

## Starten (Reihenfolge wichtig!)

1. Zuerst:  Run and Debug -> "Server starten"
   Warten auf: "Server gestartet. Bestelldienst auf Port 1099 registriert."
2. Dann:    Run and Debug -> "Client starten"
   Die Eingaben erfolgen im integrierten Terminal von VS Code.

## Ablauf im Client (entspricht der Aufgabenstellung)

1.  E-Mail eingeben -> Pruefung, ob Kunde registriert ist
2.  Falls nein: Kundenkonto anlegen (Name, Vorname, Telefon, Adresse)
3.  Artikel werden angezeigt (Nr, Bezeichnung, Preis, Lager, Lieferzeit)
4.  Artikel per Nummer + Menge in den Warenkorb legen (0 = Bestellen)
5.  Server prueft Verfuegbarkeit, berechnet Gesamtpreis und Versandtermin
    (Versandtermin = heute + laengste Lieferzeit im Warenkorb)
6.  Bestellbestaetigung: Bestellnummer, Gesamtpreis, Versandtermin
7.  Frage: Stornieren? j = Status "Storniert", n = Status "Versendet"

## Getestete Szenarien

- Neukunde registriert sich und bestellt            -> OK
- Bestandskunde wird an der E-Mail erkannt           -> OK
- Stornierung vor dem Versandtermin                  -> OK
- Artikel nicht verfuegbar -> eigene Exception       -> OK
- Tippfehler bei Zahleneingaben -> keine Abstuerze   -> OK
- Gleicher Artikel mehrfach -> Mengen werden addiert -> OK
- Leerer Warenkorb -> sauberes Programmende          -> OK
