package anwendung;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import modell.Kunde;
import modell.Artikel;
import modell.Position;
import modell.Bestellbestaetigung;

// Das RMI-Interface: Es legt fest, was der Client über das Netzwerk aufrufen darf.
// Der Client redet nur mit diesem einen Interface, nicht direkt mit den einzelnen
// Services (KundenService, BestellService, ...). Das hält die Verbindung einfach.
public interface Bestelldienst extends Remote
{
    boolean pruefeRegistrierung(String email) throws RemoteException;

    Kunde legeKundeAn(String name, String email, String telefon, String adresse) throws RemoteException;

    Kunde holeKunde(String email) throws RemoteException;

    List<Artikel> alleArtikel() throws RemoteException;

    Bestellbestaetigung erstelleBestellung(int kundenId, String email, List<Position> warenkorb) throws RemoteException;

    void storniereBestellung(int bestellNr) throws RemoteException;

    void versendeBestellung(int bestellNr) throws RemoteException;
}
