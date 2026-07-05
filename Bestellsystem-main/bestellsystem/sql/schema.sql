-- Schema fuer das Bestellsystem (MariaDB)
-- Einspielen mit: mysql -u root -p < schema.sql
-- Exakt das vorgegebene relationale Schema, keine zusaetzlichen Tabellen oder Attribute.

CREATE DATABASE IF NOT EXISTS bestellsystem CHARACTER SET utf8mb4;
USE bestellsystem;

CREATE TABLE IF NOT EXISTS Kunde (
    KundenID        INT AUTO_INCREMENT PRIMARY KEY,
    Name            VARCHAR(100),
    Vorname         VARCHAR(100),
    Email           VARCHAR(150) NOT NULL UNIQUE,
    Telefon         VARCHAR(50),
    Strasse         VARCHAR(150),
    PLZ             VARCHAR(10),
    Ort             VARCHAR(100),
    Registrierdatum DATE
);

CREATE TABLE IF NOT EXISTS Artikel (
    ArtikelNr       INT PRIMARY KEY,
    Bezeichnung     VARCHAR(150) NOT NULL,
    Preis           DECIMAL(10,2) NOT NULL,
    Lagerbestand    INT NOT NULL,
    Lieferzeit_Tage INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Bestellung (
    BestellNr     INT AUTO_INCREMENT PRIMARY KEY,
    KundenID      INT NOT NULL,
    Bestelldatum  DATE NOT NULL,
    Gesamtpreis   DECIMAL(10,2) NOT NULL,
    Versandtermin DATE NOT NULL,
    Status        VARCHAR(20) NOT NULL,
    FOREIGN KEY (KundenID) REFERENCES Kunde(KundenID)
);

CREATE TABLE IF NOT EXISTS BestellPosition (
    BestellNr   INT NOT NULL,
    ArtikelNr   INT NOT NULL,
    Menge       INT NOT NULL,
    Einzelpreis DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (BestellNr, ArtikelNr),
    FOREIGN KEY (BestellNr) REFERENCES Bestellung(BestellNr),
    FOREIGN KEY (ArtikelNr) REFERENCES Artikel(ArtikelNr)
);

-- Beispielartikel zum Testen
INSERT INTO Artikel (ArtikelNr, Bezeichnung, Preis, Lagerbestand, Lieferzeit_Tage) VALUES
    (1, 'Tastatur', 29.99, 10, 2),
    (2, 'Monitor', 199.99, 3, 5),
    (3, 'Maus', 14.99, 25, 1),
    (4, 'USB-Kabel', 7.49, 50, 1)
ON DUPLICATE KEY UPDATE Bezeichnung = VALUES(Bezeichnung);
