package rmi;

import model.Artikel;
import model.Bestellung;
import model.Kunde;
import service.ArtikelNichtVerfuegbarException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

/**
 * Remote-Interface fuer RMI (siehe Vorlesung: Interfaces + RMI).
 * Beschreibt, WELCHE Dienste der Server anbietet - aber nicht, WIE sie umgesetzt sind.
 * Der Client kennt nur dieses Interface, nicht die Implementierung.
 * Jede Methode muss RemoteException werfen koennen (Vorgabe von RMI).
 */
public interface Bestelldienst extends Remote {

    boolean istKundeRegistriert(String email) throws RemoteException;

    Kunde findeKunde(String email) throws RemoteException;

    Kunde registriereKunde(Kunde kunde) throws RemoteException;

    List<Artikel> holeAlleArtikel() throws RemoteException;

    Bestellung loeseBestellungAus(int kundenID, Map<Integer, Integer> warenkorb)
            throws RemoteException, ArtikelNichtVerfuegbarException;

    boolean storniereBestellung(Bestellung bestellung) throws RemoteException;

    void versendeBestellung(Bestellung bestellung) throws RemoteException;
}
