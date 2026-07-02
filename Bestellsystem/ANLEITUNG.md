# Bestellsystem mit Kundenregistrierung

Java-Projekt zur Aufgabe "Bestellung mit Kundenregistrierung". Aufgebaut als
Drei-Schichten-Architektur mit RMI und JDBC, und zwar genau nach den drei
Unterlagen aus der Veranstaltung: dem Architekturdiagramm, dem Ablaufdiagramm
und dem Datenmodell.

## Aufbau nach dem Architekturdiagramm

**Präsentationsschicht** (Paket `praesentation`)
- `Client`: enthält die vier Methoden `zeigeWarenkorb()`, `zeigeFormular()`,
  `zeigeBestaetigung()` und `zeigeStornierung()`, passend zu den vier Kästen
  im Architekturdiagramm.

**Anwendungsschicht** (Paket `anwendung`)
- `KundenService`: prüft die Registrierung und legt Kunden an.
- `BestellService`: legt Bestellungen an, storniert oder versendet sie.
- `VersandService`: prüft die Verfügbarkeit und berechnet den Versandtermin.
- `PreisService`: berechnet den Gesamtpreis.
- `BenachrichtigungsService`: gibt eine Meldung aus (keine echte Email).
- `Bestelldienst` / `BestelldienstImpl`: das RMI-Interface und seine
  Implementierung. `BestelldienstImpl` ruft die fünf Services oben auf,
  damit der Client nur eine einzige Verbindung braucht.
- `Server`: startet die RMI-Registry.

**Datenhaltungsschicht** (Paket `datenhaltung`)
- `KundenRepository`, `BestellRepository`, `ArtikelRepository`: je eine
  Klasse pro Tabelle, genau wie im Architekturdiagramm.

**Paket `modell`**
- `Kunde`, `Artikel`, `Position`, `Bestellbestaetigung`: die Datenklassen,
  mit Feldern passend zum Datenmodell (inklusive Telefon bei Kunde und
  Bestelldatum bei Bestellung).

## Der Ablauf (nach dem Ablaufdiagramm)

1. Warenkorb füllen (`zeigeWarenkorb`).
2. Email eingeben, Prüfung ob Kunde vorhanden (`pruefeRegistrierung`).
3. Falls nicht: Kundendaten erfassen (`zeigeFormular`). Sind Name oder
   Adresse leer, kommt eine Fehlermeldung und man landet wieder bei der
   Dateneingabe, genau wie im Diagramm mit dem Pfeil zurück zu
   "Kundendaten erfassen".
4. Verfügbarkeit prüfen und Gesamtpreis berechnen (beides in
   `erstelleBestellung` in `BestelldienstImpl`).
5. Bestellung anlegen, Bestellbestätigung anzeigen (`zeigeBestaetigung`).
6. Stornieren oder Ware versenden (`zeigeStornierung`,
   `bestaetigeStornierung`).

## Vorbereitung

1. XAMPP starten (mariaDB und Apache), am besten als Administrator.
2. In phpMyAdmin (http://localhost/phpmyadmin) den Inhalt von `datenbank.sql`
   ausführen.
3. Die Datei `mariadb-java-client-3.5.9.jar` als Library zum Projekt
   hinzufügen (in IntelliJ: File → Project Structure → Libraries → + → Java).

## Starten (mit IntelliJ)

1. Projektordner über "Open" in IntelliJ öffnen.
2. `Server.java` mit Rechtsklick → "Run" starten.
3. Danach `Client.java` mit Rechtsklick → "Run" starten (Server bleibt an).

## Starten (über die Kommandozeile, alternativ)

```
javac -d out src/modell/*.java src/datenhaltung/*.java src/anwendung/*.java src/praesentation/*.java
java -cp "out;mariadb-java-client-3.5.9.jar" anwendung.Server
java -cp "out;mariadb-java-client-3.5.9.jar" praesentation.Client
```

Unter Windows wird im Klassenpfad `;` benutzt, unter Linux/Mac `:`.

## Was bewusst einfacher gehalten wurde

- Das Passwort aus dem Registrierungsformular wird abgefragt, aber nicht
  gespeichert, da es in diesem Projekt keine Anmeldung/kein Login gibt.
- Die Benachrichtigung ist nur eine Konsolenausgabe, es wird keine echte
  Email verschickt.
- Jedes Repository baut seine eigene Datenbankverbindung auf. Das ist
  etwas wiederholt, aber macht jede Klasse für sich verständlich und
  unabhängig von den anderen.
