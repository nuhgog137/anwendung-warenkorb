-- Datenbank für das Bestellsystem
-- Dieses Skript kann man in phpMyAdmin im Reiter "SQL" einfügen und ausführen.

CREATE DATABASE IF NOT EXISTS shop;
USE shop;

-- Tabelle für die Kunden (Felder genau wie im Datenmodell: KundenID, Name, Email, Telefon, Adresse)
CREATE TABLE kunde (
    kundenid INTEGER NOT NULL,
    name VARCHAR(50),
    email VARCHAR(50),
    telefon VARCHAR(30),
    adresse VARCHAR(100),
    PRIMARY KEY (kundenid)
);

-- Tabelle für die Artikel (ArtikelNr, Einzelpreis, Lagerbestand, Lieferzeit wie im Datenmodell,
-- "bezeichnung" zusätzlich für eine lesbare Anzeige)
CREATE TABLE artikel (
    artikelnr INTEGER NOT NULL,
    bezeichnung VARCHAR(50),
    einzelpreis DECIMAL(10,2),
    lagerbestand INTEGER,
    lieferzeit INTEGER,
    PRIMARY KEY (artikelnr)
);

-- Tabelle für die Bestellungen (inkl. Bestelldatum wie im Datenmodell)
-- status kann "offen", "storniert" oder "verschickt" sein
CREATE TABLE bestellung (
    bestellnr INTEGER NOT NULL,
    kundenid INTEGER,
    bestelldatum DATE,
    gesamtpreis DECIMAL(10,2),
    versandtermin DATE,
    status VARCHAR(20),
    PRIMARY KEY (bestellnr)
);

-- Tabelle für den Warenkorb (entspricht der Beziehung "enthalten" mit Attribut Menge)
CREATE TABLE bestellung_artikel (
    bestellnr INTEGER NOT NULL,
    artikelnr INTEGER NOT NULL,
    menge INTEGER,
    PRIMARY KEY (bestellnr, artikelnr)
);

-- Ein paar Test-Artikel:
-- Artikel 1 und 2 sind auf Lager  -> sofort lieferbar (Versandtermin = heute)
-- Artikel 3 hat Lagerbestand 0     -> 7 Tage Lieferzeit (Versandtermin später)
INSERT INTO artikel(artikelnr, bezeichnung, einzelpreis, lagerbestand, lieferzeit) VALUES (1, 'USB-Stick', 9.99, 50, 3);
INSERT INTO artikel(artikelnr, bezeichnung, einzelpreis, lagerbestand, lieferzeit) VALUES (2, 'Tastatur', 24.50, 10, 5);
INSERT INTO artikel(artikelnr, bezeichnung, einzelpreis, lagerbestand, lieferzeit) VALUES (3, 'Monitor', 149.00, 0, 7);

-- Ein Test-Kunde, der schon registriert ist:
INSERT INTO kunde(kundenid, name, email, telefon, adresse) VALUES (1, 'Max Mustermann', 'max@example.com', '0221 123456', 'Musterstraße 1, Köln');
