/**
 *  Classe che permette di modificare dati  di libri presi da un file json e salvarne le modifiche.
 * @see Libro
 * @see Libri
 * @author Giorgio Prestigiacomo
 * @date 29/07/2019
 * @version 1.0
 * @copyright
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Main {
    /**
     * Attributo che ha le informazioni lette dal file json.
     * L'ogetto servia' per le operazioni di:
     * <ul>
     *     <li>Inserimento</li>
     *     <li>Eliminazione</li>
     *     <li>Modifica</li>
     *     <li>Visualizzazione</li>
     * </ul>
     * @see Libri
     */
    private static Libri libreria; // Ogetto che tiene in memoria l'attuale condizione della libreria
    /**
     * Logger per gli errorri
     */
    private static final Log log = LogFactory.getLog(Main.class);

    /**
     * non implementato
     */
    private Main(){

    }
    /**
     * Funzione che testa le operazioni della classe.
     * Per testare le funzionalta' vi sara un menu con le seguenti voci:
     *    <ul>
     *            <li> lettura da file json</li>
     *            <li> Scrittura su file json</li>
     *            <li> Modifica di nodi json</li>
     *           <li> Inseimento di nuovi nodi json;</li>
     *            <li> Modifica di nodi esistenti</li>
     *            <li> Eliminazione di nodi esistenti</li>
     *    </ul>
     * @see #inizializzazione()
     * @see #mostraLibreria()
     * @see #inserimento()
     * @see #modificaLibro()
     * @see #rimuoviLibro()
     * @see #cercaLibro()
     * @see #salva()
     * @param args non in uso
     */
    public static void main(String[] args) {

        int sw; // variabile per lo switch
        try {
            inizializzazione(); // avvio l'inizializzazione delle variabili
        } catch (IOException e) {
            log.error("Errore nella lettura del file : \n" + e + "\nUscita");
            return;
        }
        do {
            sw = -1;
            System.out.println("------- MENU LIBRERIA ------\n" + //menu di scelta
                    "1) Aggiungi libro \n" +
                    "2) Rimuovi Libro \n" +
                    "3) Cerca Libro \n" +
                    "4) Visualizza Libreria \n" +
                    "5) Modifica Libro \n" +
                    "6) Salva ed Esci");
            do {
                //ciclo che controlla se è stato inserirto un numero
                System.out.print("Scelta -> ");
                Scanner sc = new Scanner(System.in);
                String swst = sc.nextLine();
                swst = swst.trim();
                if (swst.matches("\\d+")) {
                    try {
                        sw = Integer.parseInt(swst);
                    } catch (NumberFormatException e) {
                        log.error(("\nInserire un valore che tra : 0 e " + Integer.MAX_VALUE));
                        sw = -1;
                    }
                }
            } while (sw < 0 || sw>6);
            Enums.Menu mn= Enums.Menu.values()[sw];
            switch (mn) {
                case inserimento :
                    inserimento();
                    break;

                case cerca:
                    cercaLibro();
                    break;

                case visualizza:
                    mostraLibreria();
                    break;

                case elimina:
                    rimuoviLibro();
                    break;

                case modifica:
                    modificaLibro();
                    break;

                case salva:
                    try {
                        salva();
                    } catch (IOException e) {
                        log.error("Errore nella scrittura del file : \n" + e + "\nUscita");
                        return;
                    }
                    break;
            }
        } while (sw != 6);
    }

    /**
     * Funzione che legge il file json e salva i suoi elementi in un ogetto di tipo Libri.
     * Viene usato l'attributo libreria per memorizare l'ogetto.
     *
     * @see Libri
     * @throws IOException Errore nella lettura del file
     */
    public static void inizializzazione() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        libreria = objectMapper.readValue(new File("libri.json"), Libri.class);
    }

    /**
     * Funzione che serve per l'inserimento di un nuovo libro nella lista.
     * Richiede le informazioni nella funzione.
     * Se il libro e' gia presente non verra' aggiunto.
     *
     * @see Libri#addLibro(Libro)
     * @see Libri#isDuplicato(Libro)
     */
    public static void inserimento() {
        Scanner sc = new Scanner(System.in);
        ObjectMapper mapper = new ObjectMapper();
        String titolo;
        //ciclo che controlla che nel campo ci sia almeno un carattere
        do {
            System.out.println("Inserire titolo, almeno un carattere : ");
            titolo = sc.nextLine(); // prende una riga intera sara' usata per il titolo
            titolo = titolo.trim();// elimina gli spazzi dalla stringa titolo
        } while (titolo.length() == 0);

        String autore;
        //ciclo che controlla che nel campo ci sia almeno un carattere
        do {
            System.out.println("Inserire autore, almeno un carattere  : ");
            autore = sc.nextLine(); // prende una riga intera, sara usata' per l'autore
            autore = autore.trim();// elimina gli spazzi dalla stringa dell'autore
        } while (autore.length() == 0);

        Integer anno = -1;
        //ciclo che controlla che non venga inserito un numero
        do {
            System.out.println("Inserire anno, intero positivo : ");
            String annost = sc.nextLine(); // prendo una riga intera, sara' usata per l'anno
            annost = annost.trim(); // elimina gli spazzi dalla stringa del anno
            //controlla che sia un numetro tramite l'espessione regolare "\d+"
            if (annost.matches("\\d+")) {
                try {
                    anno = Integer.parseInt(annost);
                } catch (NumberFormatException e) {
                    log.error("Inserire un valore che tra : 0 e " + Integer.MAX_VALUE);
                    anno = -1;
                }
            }
        } while (anno < 0);

        Libro libro = new Libro(titolo, autore, anno); // crea il nuovo libro con le informazioni appena imesse
        //controlla che il nuovo libro non sia gia presente, se non è presente lo inserisce, in caso contrario
        //avverte che e' gia' presente e non lo re-inserisce
        if (!libreria.isDuplicato(libro)) {
            libreria.addLibro(libro);// aggiunta del nuovo libro alla lista
            System.out.println("Libro inserito con sucesso");
        } else {
            System.out.println("Inserimento non consentito, libro dupilicato");
        }
    }

    /**
     * funzione che permette di eliminare un libro.
     * Nella funzione verranno visualizatti tutti i libri con il relativo indice.
     * L'utente potra' sceglierne uno tramite esso ed eliminarlo.
     * L'utenete inoltre potra' anullare l'operazione
     *
     * @see #mostraLibreria()
     * @see Libri#rimuoviLibro(Integer)
     */
    public static void rimuoviLibro() {
        Scanner sc = new Scanner(System.in);
        mostraLibreria(); // funzione che mette in output tutti il libri con il relativo indice
        Integer scelta = -1; //variabile che tiene l'indice scelto dall'utente
        do {
            System.out.println("Inserire l'indice del libro che si vuole eliminare, 0 per anullare l'operazione");
            System.out.print("Scelta -> ");
            String sceltast = sc.nextLine(); //prende un intera riga come input per la scelta
            sceltast = sceltast.trim(); // elimina gli spazzi superflui dalla stringa
            //controlla se rispetta l'espressione regolare "\d+"
            if (sceltast.matches("\\d+"))
                try {
                    scelta = Integer.parseInt(sceltast); // trasforma la stringa in un intero
                } catch (NumberFormatException e) {
                    log.error("Inserire un valore che tra : 0 e " + Integer.MAX_VALUE);
                    scelta = -1;
                }
        } while (scelta < 0 || scelta > libreria.size());
        scelta--;// diminuisce l'intero scelto dall'utente per farlo combaciare con l'indice del vettore
        //controlla se la scelta sia uguale a -1, poiche significherebbe che l'utente ha scelto di anullare l'operazione
        if (scelta == -1) {
            System.out.println("Operazione anullata");
            return;
        }
        System.out.println("Libro eliminato con sucesso");
        libreria.rimuoviLibro(scelta);
    }

    /**
     * funzione che permette la ricerca di un libro.
     * La ricerca puo' avvenire per :
     * <ul>
     * <li>1) titolo</li>
     * <li>2) autore</li>
     * <li>3) titolo e autore</li>
     * </ul>
     *
     * nella ricerca del titolo, si cercheranno i titoli che avranno la frase cercata.
     * l'utente potra anche anullare l'operazione
     * 
     * @see Libri#seeLibro(Integer)
     */
    public static void cercaLibro() {
        Scanner sc = new Scanner(System.in);
        Integer sw = 0; // variabile che avra' la scelta dell'utente
        System.out.println("-- Specificare il tipo di ricerca -- \n" +
                    "1) Per titolo \n" +
                    "2) Per Autore \n" +
                    "3) Per Entrambi \n" +
                    "4) Anulla operazione");
            do {
                System.out.print("Scelta -> ");
                String swst = sc.nextLine();
                swst = swst.trim();
                if (swst.matches("\\d+")) {
                    try {
                        sw = Integer.parseInt(swst); // trasforma la stringa in un intero
                    } catch (NumberFormatException e) {
                        log.error("Inserire un valore che tra : 0 e " + Integer.MAX_VALUE);
                        sw = -1;
                    }
                }
            } while (sw < 0 || sw>4);
        Enums.Ricerca cr= Enums.Ricerca.values()[sw];
        switch (cr) {
                case titolo:
                    //Ricerca per titolo libro
                    String titolo;
                    do {
                        System.out.print("Inserire titolo, almeno un carattere : ");
                        titolo = sc.nextLine();
                        titolo.trim();
                    } while (titolo.length() <= 0);
                    titolo.toLowerCase(); // rendo i caratteri della stringa minuscoli
                    String titolopt = ".*" + titolo + ".*"; // creo la esperssione regolare per trovare i libri
                    System.out.println("Lista libri trovati che hanno nel titolo " + titolo + " : ");
                    Integer count = 0; // contatore che serve per mettere l'indice coretto ai libri
                    // scorre l'iteratore per cercare se c'è qualche elemento che rispecchi la richiesta dell'utente
                    for (int i = 0; i < libreria.size(); i++) {
                        if (libreria.seeLibro(i).getTitolo().toLowerCase().matches(titolopt)) {
                            System.out.println("\t" + i + ") " + libreria.seeLibro(i));
                        }
                    }
                    break;

                case autore:
                    //ricerca per autore
                    String autore;
                    do {
                        System.out.print("Inserire autore, almeno un carattere : ");
                        autore = sc.nextLine();
                    } while (autore.length() <= 0);

                    System.out.println("Lista libri trovati con l'autore " + autore + " : ");
                    for (int i = 0; i < libreria.size(); i++) {
                        if (autore.equalsIgnoreCase(libreria.seeLibro(i).getAutore())) {
                            System.out.println("\t" + i + ") " + libreria.seeLibro(i));
                        }
                    }
                    break;

                case titoloEautore:
                    //ricerca per autore e titolo
                    do {
                        System.out.print("Inserire titolo, almeno un carattere : ");
                        titolo = sc.nextLine();
                    } while (titolo.length() <= 0);
                    titolo.toLowerCase();
                    do {
                        System.out.print("Inserire autore, almeno un carattere : ");
                        autore = sc.nextLine();
                    } while (autore.length() <= 0);
                    autore.toLowerCase();
                    titolopt = ".*" + titolo + ".*";

                    System.out.println("Lista libri trovati con l'autore " + autore + " e nel titolo" + titolo + " : ");
                    for (int i = 0; i < libreria.size(); i++) {
                        if (libreria.seeLibro(i).getTitolo().toLowerCase().matches(titolopt) &&
                                autore.equalsIgnoreCase(libreria.seeLibro(i).getAutore())) {
                            System.out.println("\t" + i + ") " + libreria.seeLibro(i));
                        }
                    }
                    break;

                case uscita:
                    System.out.println("Operazione anullata");
                    break;

            }

    }

    /**
     * funzione che fa vedere tutti i libri con il relativo indice, riferito alla lista
     * 
     * @see Libri#seeLibro(Integer)
     *
     */
    public static void mostraLibreria() {
        for (int i = 0; i < libreria.size(); i++) {
            System.out.println((i + 1) + ") " + libreria.seeLibro(i));
        }
    }

    /**
     * Funzione che Permette di modificare un libro.
     * Verra' visualizata la lista dei libri con il loro indice.
     * L'utenete potra' scegliere il libro da modificare direttamente tramite l'indice e procedere alla modifica
     * Se l'utente modificando un libro ne inserisce uno gia' presente, l'operazione verra' anullata
     * 
     * @see #mostraLibreria()
     * @see Libri#modLibro(Integer, Libro)
     */
    public static void modificaLibro() {
        Scanner sc = new Scanner(System.in);
        mostraLibreria();
        Integer scelta = -1;
        do {
            System.out.println("Inserire l'indice del libro che si vuole modificare, 0 per anullare l'operazione");
            System.out.print("Scelta -> ");
            String sceltast = sc.nextLine();
            sceltast = sceltast.trim();
            if (sceltast.matches("\\d+"))
                try {
                    scelta = Integer.parseInt(sceltast); // trasforma la stringa in un intero
                } catch (NumberFormatException e) {
                    log.error("Inserire un valore che tra : 0 e " + Integer.MAX_VALUE);
                    scelta = -1;
                }
        } while (scelta < 0 || scelta > libreria.size());
        scelta--;
        if (scelta < 0) {
            System.out.println("Operazione anullata");
            return;
        }
        System.out.println("-------- Inserire i nuovi dati --------");

        String titolo;
        do {
            System.out.print("Inserire titolo, almeno un carattere : ");
            titolo = sc.nextLine();
        } while (titolo.length() <= 0);

        String autore;
        do {
            System.out.print("Inserire autore, almeno un carattere  : ");
            autore = sc.nextLine();
        } while (autore.length() <= 0);

        Integer anno = -1;
        do {
            System.out.print("Inserire anno, intero positivo : ");
            String annost = sc.nextLine();
            annost = annost.trim();
            if (annost.matches("\\d+")) {
                try {
                    anno = Integer.parseInt(annost); // trasforma la stringa in un intero
                } catch (NumberFormatException e) {
                    System.out.println("Inserire un valore che tra : 0 e " + Integer.MAX_VALUE);
                    anno = -1;
                }
            }
        } while (anno < 0);
        Libro libro = new Libro(titolo, autore, anno);

        if (!libreria.isDuplicato(libro)) {
            libreria.modLibro(scelta, libro);
            System.out.println("Libro modificato con sucesso");
        } else {
            System.out.println("Modifica rifiutata, la modifica inserisce un libro gia presente");
        }
    }

    /**
     * Funzione che salva le modifiche fatte alla libreria in un file json.
     *
     * @throws IOException Errore nella scrittura del file
     */
    public static void salva() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter()); // ogetto che serve per la scrittura su file json
        writer.writeValue(new File("libri.json"), libreria); // scrive il nodo su file
    }


}