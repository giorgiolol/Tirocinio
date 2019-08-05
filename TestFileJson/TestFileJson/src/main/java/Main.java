
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;


public class Main {


    public static void main(String[] args) throws  IOException {
        ArrayNode array = new ArrayNode(new JsonNodeFactory(true));
        array= inizio();
        int sw;
        do {
            do {
                sw=-1;
                System.out.println("------- MENU LIBRERIA ------\n" +
                        "1) Aggiungi libro \n" +
                        "2) Rimuovi Libro \n" +
                        "3) Cerca Libro \n" +
                        "4) Visualizza Libreria \n" +
                        "5) Modifica Libro \n" +
                        "6) Salva ed Esci");
                System.out.print("Scelta -> ");
                Scanner sc = new Scanner(System.in);
                    String swst = sc.nextLine();
                    swst=swst.trim();
                    if(swst.matches("\\d+"))
                        sw=Integer.parseInt(swst);
            }while(sw<0);
            switch (sw)
            {
                case 1:
                    inserimento(array);
                    break;

                case 2:
                    rimuoviLibro(array);
                    break;

                case 3:
                    cercaLibro(array);
                    break;

                case 4:
                    mostraLibreria(array);
                    break;

                case 5:
                    modificaLibro(array);
                    break;

                case 6:
                    salva(array);
                    break;

            }
        }while(sw!=6);
    }

    static ArrayNode  inizio  () throws  IOException
    {
        ArrayNode array = new ArrayNode(new JsonNodeFactory(true));
        ObjectMapper mapper = new ObjectMapper(); // Mapper serve per le varie operazioni
        JsonFactory jsonFactory = new JsonFactory(); // Contiene il parser per il json
        JsonParser jp = jsonFactory.createParser(new File("libri.json"));
        jp.setCodec(new ObjectMapper()); // ???
        JsonNode jsonNode = jp.readValueAsTree(); // preinde i dati del parser e li legge come un albero
        JsonNode obj= jsonNode.get("libri"); // prende il campo libri
        if(obj.isArray()){ // vede se il nodo è un vettore
            for (final JsonNode objNode : obj) { // Itero sull array per prendere tutti i dati dell'arry
                array.add(objNode);
            }
        }
        return array;
    }

    static void inserimento ( ArrayNode array) {
        Scanner sc = new Scanner(System.in);
        ObjectMapper mapper = new ObjectMapper();
        String titolo;
        do {
            System.out.print("Inserire titolo, almeno un carattere : ");
             titolo = sc.nextLine();
        }while (titolo.length()<=0);

        String autore;
        do{
            System.out.print("Inserire autore, almeno un carattere  : ");
            autore= sc.nextLine();
        }while (autore.length()<=0);

        Integer anno =-1;
        do {
            System.out.print("Inserire anno, intero positivo : ");
            String annost = sc.nextLine();
            annost=annost.trim();
            if(annost.matches("\\d+"))
                anno=Integer.parseInt(annost);
        }while (anno<0);


        Libro lib = new Libro(titolo,autore,anno);
        JsonNode node = mapper.valueToTree(lib);
        if(!duplicato(array,node)){
        array.add(node);
        System.out.println("Libro inserito con sucesso");
        }
        else
            System.out.println("Libro dupilicato");
    }

    private static boolean duplicato (ArrayNode array, JsonNode node ){
        Iterator<JsonNode> it = array.elements();
        while(it.hasNext()){ // Itero sull array per vedere se è
            JsonNode objNode = it.next();
            if(objNode.equals(node))
                return true;
        }
        return false;
    }

    static void rimuoviLibro ( ArrayNode array) {
        Scanner sc = new Scanner(System.in);
        Integer num= mostraLibreria(array);
        Integer scelta=-1;
        do {
            System.out.println("Inserire l'indice del libro che si vuole eliminare, 0 per anullare l'operazione");
            System.out.print("Scelta -> ");
            String sceltast = sc.nextLine();
            sceltast=sceltast.trim();
            if(sceltast.matches("\\d+"))
                scelta=Integer.parseInt(sceltast);
        }while(scelta<0 || scelta>num);
        scelta--;
        if(scelta<0) {
            System.out.println("Operazione anullata");
            return;
        }
        System.out.println("Libro eliminato con sucesso");
        array.remove(scelta);
        }

    static void cercaLibro(ArrayNode array) {
        Scanner sc = new Scanner(System.in);
        Integer sw=0;
        do {
           do{
            System.out.println("-- Specificare il tipo di ricerca -- \n" +
                                "1) Per titolo \n" +
                                "2) Per Autore \n" +
                                "3) Per Entrambi \n" +
                                "4) Uscita senza ricerca");
            System.out.print("Scelta -> ");
            String swst = sc.nextLine();
            swst=swst.trim();
            if(swst.matches("\\d+"))
                sw=Integer.parseInt(swst);
            }while(sw<0);

            switch(sw)
            {
                case 1:
                    String titolo;
                    do {
                        System.out.print("Inserire titolo, almeno un carattere : ");
                        titolo = sc.nextLine();
                    }while (titolo.length()<=0);

                    String titolopt=".*"+titolo+".*";
                    System.out.println("Lista libri trovati che hanno nel titolo "+titolo+" : ");
                    Iterator<JsonNode> it = array.elements();
                    Integer count= 0;
                    while(it.hasNext()){ // Itero l'array
                        JsonNode objNode = it.next();
                        count++;
                        if(objNode.get("titolo").asText().matches(titolopt) ){
                            System.out.println("\t"+count+") "+objNode.get("titolo").asText() + "  ,  "+ objNode.get("autore").asText() + "  ,  "+ objNode.get("anno").asInt()  );
                            return;
                        }
                    }
                    break;

                case 2:
                    String autore;
                    do {
                        System.out.print("Inserire autore, almeno un carattere : ");
                        autore = sc.nextLine();
                    }while (autore.length()<=0);
                    System.out.println("Lista libri trovati con l'autore "+autore+" : ");
                    it = array.elements();
                    count= 0;
                    while(it.hasNext()){ // Itero l'array
                    JsonNode objNode = it.next();
                    count++;
                    if(autore.equals(objNode.get("autore").asText()) ){
                        System.out.println("\t"+count+") "+objNode.get("titolo").asText() + "  ,  "+ objNode.get("autore").asText() + "  ,  "+ objNode.get("anno").asInt()  );
                    }
                    }

                case 3:
                    do {
                        System.out.print("Inserire titolo, almeno un carattere : ");
                        titolo = sc.nextLine();
                    }while (titolo.length()<=0);
                    titolo= sc.nextLine();
                    do {
                        System.out.print("Inserire autore, almeno un carattere : ");
                        autore = sc.nextLine();
                    }while (autore.length()<=0);
                    titolopt=".*"+titolo+".*";

                    System.out.println("Lista libri trovati con l'autore "+autore+" e nel titolo"+titolo+" : ");
                    it = array.elements();
                    count= 0;
                    while(it.hasNext()){ // Itero l'array
                        JsonNode objNode = it.next();
                        count++;
                        if(autore.equals(objNode.get("autore").asText()) && objNode.get("titolo").asText().matches(titolopt)  ){
                            System.out.println("\t"+count+") "+objNode.get("titolo").asText() + "  ,  "+ objNode.get("autore").asText() + "  ,  "+ objNode.get("anno").asInt()  );
                        }
                    }
                    break;

                case 4:
                    break;

                default:
                    System.out.println("Inserire una scelta valida");
                    break;

            }
        }while(sw >=1 && sw<=4);

    }

    static Integer mostraLibreria(ArrayNode array){
        System.out.println("Lista Libri presenti : ");
        Iterator<JsonNode> it = array.elements();
        Integer count=0;
        while(it.hasNext()){ // Itero sull array per vedere se è
            JsonNode objNode = it.next();
            count++;
            System.out.println("\t"+count+") "+objNode.get("titolo").asText() + " , "+ objNode.get("autore").asText() + " , "+ objNode.get("anno").asInt()  );
        }
        return count;
    }

    static void modificaLibro(ArrayNode array) {

        Scanner sc = new Scanner(System.in);
        Integer num= mostraLibreria(array);
        Integer scelta=-1;
        do {
            System.out.println("Inserire l'indice del libro che si vuole modificare, 0 per anullare l'operazione");
            System.out.print("Scelta -> ");
            String sceltast = sc.nextLine();
            sceltast=sceltast.trim();
            if(sceltast.matches("\\d+"))
                scelta=Integer.parseInt(sceltast);
        }while(scelta<0 || scelta>num);
        scelta--;
        if(scelta<0) {
            System.out.println("Operazione anullata");
            return;
        }
        System.out.println("-------- Inserire i nuovi dati --------");

        String titolo;
        do {
            System.out.print("Inserire titolo, almeno un carattere : ");
            titolo = sc.nextLine();
        }while (titolo.length()<=0);

        String autore;
        do{
            System.out.print("Inserire autore, almeno un carattere  : ");
            autore= sc.nextLine();
        }while (autore.length()<=0);

        Integer anno =-1;
        do {
            System.out.print("Inserire anno, intero positivo : ");
            String annost = sc.nextLine();
            annost=annost.trim();
            if(annost.matches("\\d+"))
                anno=Integer.parseInt(annost);
        }while (anno<0);

        ObjectMapper mapper = new ObjectMapper();
        Libro lib = new Libro(titolo,autore,anno);
        JsonNode node = mapper.valueToTree(lib);
        if(!duplicato(array,node)){
            array.set(scelta,node);
            System.out.println("Libro modificato con sucesso");
        }
        else{
            System.out.println("Modifica rifiutata, la modifica inserisce un libro gia presente");
        }



    }

    static void salva (ArrayNode array) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        ObjectNode jNode = mapper.createObjectNode();

        jNode.set("libri",array);
        writer.writeValue(new File("libri.json"), jNode);
    }

    static void stuff (JsonNode jsonNode) throws IOException { // fa varie operazioni di test
        JsonNode obj= jsonNode.get("libri"); // prende il campo libri
        ObjectMapper mapper = new ObjectMapper();

        if(obj.isArray()){ // vede se il nodo è un vettore
            ArrayNode ars = new ArrayNode(new JsonNodeFactory(true)); // crea un arrayNode che terrà gli elementi dell' array
            for (final JsonNode objNode : obj) { // Itero sull array per vedere se è
                System.out.println(objNode);
                ars.add(objNode);
                System.out.println(objNode.get("titolo") + "  ,  "+ objNode.get("autore") + "  ,  "+ Integer.parseInt(objNode.get("anno").toString())  );
            }

        Libro lib= new Libro("va", "bene",8521); // creo un libro da aggiungere
        JsonNode node = mapper.valueToTree(lib); // transforma il libro in un nodo
        ars.add(node); // aggiungo il nodo al vettore
        ObjectNode jNode = mapper.createObjectNode(); // creo un ogetto objectnode da poter scrivere nel file
        jNode.set("libri",ars); // inserisco il vettore nel campo libri
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter()); // creo l'ogetto che mi permetteà di scrive in un file json
        writer.writeValue(new File("libri.json"), jNode); // scrive il nodo ogetto nel file
        System.out.println("PROVA LETTURA ARRAY");
        Iterator<JsonNode> it = ars.elements();

        while(it.hasNext()){ // Itero sull array per vedere se è
            JsonNode objNode = it.next();
            System.out.println(objNode.get("titolo").asText() + "  ,  "+ objNode.get("autore") + "  ,  "+ Integer.parseInt(objNode.get("anno").toString())  );
            }
        }
    }
}







    /*static void readJsonData(JsonNode jsonNode) throws IOException {
    System.out.println(objNode.get("titolo") + "  ,  "+ objNode.get("autore") + "  ,  "+ Integer.parseInt(objNode.get("anno").toString())  );
        Iterator<Map.Entry<String, JsonNode>> ite = jsonNode.fields();
        while(ite.hasNext()) {
            Map.Entry<String, JsonNode> entry = ite.next();
            if (entry.getValue().isObject()) {
                //entry.getValue()
                // System.out.println(entry.getValue());
            } else {
                JsonNode js = entry.getValue();
                ArrayNode ars = new ArrayNode(new JsonNodeFactory(true));
                System.out.println(" Array? : " + js.isArray());
                for (final JsonNode objNode : js) {
                    System.out.println(objNode);
                    ars.add(objNode);
                    System.out.println(objNode.get("titolo") + "  ,  "+ objNode.get("autore") + "  ,  "+ Integer.parseInt(objNode.get("anno").toString())  );
                }
            }
            System.out.println("key:" + entry.getKey() + ", value:" + entry.getValue());
        }
    }

    }
     */

    /*  Scrive il vettore di array node
        ArrayNode ars = new ArrayNode(new JsonNodeFactory(true));
        .
        .
        .
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        writer.writeValue(new File("libri.json"), ars);
        System.out.println(ars.toString());
    */


