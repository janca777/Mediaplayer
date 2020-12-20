package de.fernschulen.mediaplayer;

//Wrapper fuer die Main-Klasse
//wird beim Einsatz von JavaFX (11!) benoetigt, das Projekt zu einem 'executable jar-file' exportieren zu koennen
public class MainWrapper {
    public static void main(String[] args) {
        de.fernschulen.mediaplayer.Mediaplayer.main(args);

    }
}
