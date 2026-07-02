package modell;

import java.io.Serializable;

// Eine Position ist eine Zeile im Warenkorb: welcher Artikel und wie viele Stück davon.
public class Position implements Serializable
{
    private int artikelNr;
    private int menge;

    public Position(int artikelNr, int menge)
    {
        this.artikelNr = artikelNr;
        this.menge = menge;
    }

    public int getArtikelNr()
    {
        return artikelNr;
    }

    public int getMenge()
    {
        return menge;
    }
}
