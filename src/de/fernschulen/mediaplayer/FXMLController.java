package de.fernschulen.mediaplayer;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

public class FXMLController {
    //für die Bühne
    private Stage meineStage;
    //für den Player
    private MediaPlayer mediaplayer;

    //erzeugen von Instanzen fuer das 'Verkabeln' der UI-Elemente der App:
    @FXML
    private MediaView mediaview;

    //Button + ImageView fuer die Icons 'Ton aus/ ton an'
    @FXML
    private ImageView toggle_mute;
    @FXML
    private Button toggle_mute_btn;

    //Button + ImageView fuer die Icons 'play/pause'
    @FXML
    private ImageView toggle_play;
    @FXML
    private Button toggle_play_btn;

    //Button fuer das Icon 'stop'
    @FXML
    private Button stop_btn;

    //ein Menu fuer die Steuerung der Wiedergabe
    @FXML
    private Menu play_menu;
    //für das Listenfeld (Wiedergabeliste)
    @FXML
    private ListView<String> liste;

    //ein boolean um zu Kontrollieren, ob aktuell grade eine Datei geladen ist
    private boolean hasFileLoaded = false;

    //die Methode setzt die Bühne auf den übergebenen Wert
    public void setMeineStage(Stage meineStage) {
        this.meineStage = meineStage;
        //da zu diesem Zeitpunkt noch keine Datei geladen sein kann, werden die Symbole fuer die Wiedergabe- und
        //Lautstaerkesteuerung auf 'disabled' gesetzt
        uiElementeDEAktivieren();
    }//setMeineStage

    //die Methode zum Laden der Medien-Datei
    @FXML
    protected void ladenKlick() {
        //eine neue Instanz der Klasse FileChooser erzeugen
        FileChooser oeffnenDialog = new FileChooser();
        //den Titel für den Dialog setzen
        oeffnenDialog.setTitle("Datei öffnen");
        //das Setzen der Filter aus dem ILS-Listing hat auf macOS zu Fehlverhalten gefuehrt (es wurde immer nur der Letzte
        //Eintrag beruecksichtigt). Daher musste ich die Methode veraendern:
        oeffnenDialog.getExtensionFilters().add(new ExtensionFilter("Medien-Dateien", "*.mp3", "*.mp4"));
        //den Startordner auf den Benutzerordner setzen
        oeffnenDialog.setInitialDirectory(new File(System.getProperty("user.home")));
        //den Öffnendialog anzeigen und das Ergebnis beschaffen
        File datei = oeffnenDialog.showOpenDialog(meineStage);
        //wurde eine Datei ausgewählt
        if (datei != null)
            //dann über eine eigene Methode laden
            dateiLaden(datei);
    }//ladenKlick

    //die Methode zum Stoppen
    @FXML
    protected void stoppKlick() {
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null) {
            //wenn die Wiedergabe entweder laeuft, oder grade pausiert
            MediaPlayer.Status status = mediaplayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING || status == MediaPlayer.Status.PAUSED) {
                //dann anhalten
                mediaplayer.stop();
                //... und das Symbol 'Play' setzen
                String dateiname = "icons/play.gif";
                //das Bild erzeugen und anzeigen
                File bilddatei = new File(dateiname);
                Image bild = new Image(bilddatei.toURI().toString());
                toggle_play.setImage(bild);
            }
        }
    }//stoppKlick

    //die Methode für die Pause
    @FXML
    protected void pauseKlick() {
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null)
            //wenn die Wiedergabe grade laeuft
            if (mediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                //dann unterbrechen
                mpCustomPauseMethod();
            }
    }//pauseKlick

    //die Methode für die Wiedergabe
    @FXML
    protected void playKlick() {
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null)
            //dann wiedergeben
            mpCustomPlayMethod();
    }//playKlick

    //die Methode für das Togglen der Wiedergabe
    @FXML
    protected void playPauseKlick() {
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null) {
            //wenn grade die Wiedegabe laeuft
            if (mediaplayer.getStatus() == MediaPlayer.Status.PLAYING) {
                //diese unterbrechen
                mpCustomPauseMethod();
            } else {
                //... ansonsten die Wiedergabe starten
                mpCustomPlayMethod();
            }
        }
    }//playPauseKlick

    //in der Methode wird die Wiedergabe gestartet, und das Icon 'Wiedergabe' durch das Icon 'Pause ersetzt'
    private void mpCustomPlayMethod() {
        mediaplayer.play();
        //... und das Symbol 'Pause' setzen
        String dateiname = "icons/pause.gif";
        //das Bild erzeugen und anzeigen
        File bilddatei = new File(dateiname);
        Image bild = new Image(bilddatei.toURI().toString());
        toggle_play.setImage(bild);
    }//mpCustomPlayMethod

    //die Methode pausiert die Wiedergabe, und ersetzt das Icon 'Pause' durch das Icon 'Wiedergabe'
    private void mpCustomPauseMethod() {
        mediaplayer.pause();
        //... und das Symbol 'Play' setzen
        String dateiname = "icons/play.gif";
        //das Bild erzeugen und anzeigen
        File bilddatei = new File(dateiname);
        Image bild = new Image(bilddatei.toURI().toString());
        toggle_play.setImage(bild);
    }//mpCustomPauseMethod

    //mit der Methode laesste sich das Stumm-schalten der Lautstärke togglen
    @FXML
    protected void lautsprecherKlick() {
        String dateiname;
        //gibt es überhaupt einen Mediaplayer?
        if (mediaplayer != null) {
            //ist die Lautstärke 0?
            if (mediaplayer.getVolume() == 0) {
                //dann auf 100 setzen
                mediaplayer.setVolume(100);
                //und das "normale" Symbol setzen
                dateiname = "icons/mute.gif";
            } else {
                //sonst auf 0 setzen
                mediaplayer.setVolume(0);
                //und das durchgestrichene Symbol setzen
                dateiname = "icons/mute_off.gif";
            }
            //das Bild erzeugen und anzeigen
            File bilddatei = new File(dateiname);
            Image bild = new Image(bilddatei.toURI().toString());
            toggle_mute.setImage(bild);
        }
    }//lautsprecherKlick

    //die Methode zum Beenden des MediaPlayers
    @FXML
    protected void beendenKlick() {
        Platform.exit();
    }//beendenKlick

    //die Methode DISabled die Icons sowie die Menu-Liste fuer die Wiedergabe, und blendet die elemente AUS
    private void uiElementeDEAktivieren() {
        //die Buttons disablen
        toggle_play_btn.setDisable(true);
        stop_btn.setDisable(true);
        toggle_mute_btn.setDisable(true);
        //das Wiedergabemenu disablen
        play_menu.setDisable(true);
    }//uiElementeDEAktivieren

    //die Methode enabled die Icons sowie die Menu-Liste fuer die Wiedergabe, und blendet diese Elemente EIN
    private void uiElementeAktivieren() {
        //die Buttons enablen
        toggle_play_btn.setDisable(false);
        stop_btn.setDisable(false);
        toggle_mute_btn.setDisable(false);
        //das Wiedergabemenu enablen
        play_menu.setDisable(false);
    }//uiElementeAktivieren

    //die Methode oeffnet den Dialog 'Datei laden', und versucht eine Medien-Datei zu laden
    public void dateiLaden(File datei) {
        //wenn die Wiedergabe grade laeuft, muss sie zunaechst gestoppt werden
        stoppKlick();
        //das Medium, den Mediaplayer und die MediaView erzeugen
        try {
            Media medium = new Media(datei.toURI().toString());
            mediaplayer = new MediaPlayer(medium);
            mediaview.setMediaPlayer(mediaplayer);
            //die Wiedergabe starten
            mpCustomPlayMethod();
            //wenn vorher noch keine Datei geladen war, die Symbole fuer die Wiedergabesteuerung aktivieren
            if (!hasFileLoaded) {
                uiElementeAktivieren();
                hasFileLoaded = true;
            }
            String strDatei = datei.toString();
            //den Namen der Median-Datei in die Titelleiste schreiben
            meineStage.setTitle(cutMediafileNameOutOfFullPath(strDatei));
            //die Medien-Datei der Wiedergabeliste hinzufuegen
            addPathToPlaylist(strDatei);
        } catch (Exception ex) {
            createErrorMessageDialog(ex);
        }//try/ catch
    }//dateiLaden

    //diese Methode fuegt die Medien-Datei der Wiedergabeliste hinzu, und achtet dabei darauf, dass es in der Liste keine
    //Doubletten gibt
    private void addPathToPlaylist(String strPath) {
        //wir erzeugen einen Boolean, welcher true wird, wenn der FilePath bereits in der Wiedfergabeliste enthalten ist
        boolean vorhanden = false;
        //wir gehen in einer Schleife alle Items der Wiedergabeliste durch
        for (String item : liste.getItems()) {
            //ist die Medien-Datei bereits in der Wiedergabeliste vorhanden?
            if (item.contains(strPath)) {
                //falls ja, setzen wir den boolean auf 'true'
                vorhanden = true;
                //und steigen hier direkt aus der Schleife aus
                break;
            }
        }
        //wenn die Medien-Datei noch nicht in der Wiedergabeliste enthalten ist...
        if (!vorhanden) {
            //dann tragen wir den Pfad in das Listenfeld des MediaPlayers ein
            liste.getItems().add(strPath);
            //das neue LIstenelement mit einem click-Listener versehen
            addClickListener();
        }
    }//addPathToPlaylist

    //wir fuegen der Wiedergabeliste einen click-Listener hinzu, damit wir bei Mausklick-Events auf ein Element der Liste
    //reagieren koennen
    private void addClickListener() {
        //wir detecten den Maus-Klick
        liste.setOnMouseClicked(mouseEvent -> {
            //wir holen uns den Path des angeklickten Listen-Elements
            String strMediaDatei = "file:" + liste.getSelectionModel().getSelectedItem();
            //wenn die Wiedergabe grade laeuft, muss sie zunaechst gestoppt werden
            stoppKlick();
            //wir versuchen Instanzen einer Mediendatei sowie eines MediaPlayers zu erzeugen
            try {
                Media medium = new Media(strMediaDatei);
                mediaplayer = new MediaPlayer(medium);
                mediaview.setMediaPlayer(mediaplayer);
                //die Wiedergabe starten
                mpCustomPlayMethod();
                //und die Titelleiste anpassen
                meineStage.setTitle(cutMediafileNameOutOfFullPath(strMediaDatei));
            } catch (Exception ex) {
                createErrorMessageDialog(ex);
            }//try/ catch
        });
    }//addClickListener

    //da es 2 try/catch Bloecke gibt, die den Fehler-Dialog auf die selbe Weise erzeugen, ziehen wir den Fehlerdialog
    //hier for die Klammer
    private void createErrorMessageDialog(Exception ex) {
        //den Dialog erzeugen und anzeigen
        Alert meinDialog = new Alert(AlertType.INFORMATION, "Beim Laden hat es ein Problem gegeben.\n" + ex.getMessage());
        //den Text setzen
        meinDialog.setHeaderText("Bitte beachten: ");
        meinDialog.initOwner(meineStage);
        //den Dialog anzeigen
        meinDialog.showAndWait();
    }//createErrorMessageDialog

    //Erzeugen eines Titels fuer die Titelleiste der App mithilfe des Filepaths
    private String cutMediafileNameOutOfFullPath(String filePath) {
        //wir holen uns die Stelle, an welcher der letzte '/'-Character im String des FilePaths steht
        int lastOccurenceOfChar = filePath.lastIndexOf("/");
        //und Formen aus dem Substring (allem, was dahinter steht) einen charmanten Titel fuer die Titelleiste
        return "JavaFX MediaPlayer | Now playing - " + filePath.substring(lastOccurenceOfChar + 1) + " -";
    }

}//end class FXMLController
