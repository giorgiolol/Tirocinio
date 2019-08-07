public class Libro {
    private String titolo;
    private String autore;
    private Integer anno;

    public Libro (){
    }

    public Libro (String titolo, String autore, Integer anno) {
        this.titolo=titolo;
        this.autore=autore;
        this.anno=anno;
    }

    public void setTitolo (String titolo){
        this.titolo=titolo;
    }
    public void setAutore (String autore) {
        this.autore=autore;
    }

    public void setAnno (Integer anno){
        this.anno=anno;
     }

    public String getTitolo (){
        return this.titolo;
    }

    public String getAutore(){
        return this.autore;
    }

    public Integer getAnno(){
        return this.anno;
    }

     @Override
    public String toString()
     {
         return "Titolo : "+this.titolo+", Autore : "+this.autore+", Anno :"+this.anno;
     }

     @Override
    public boolean equals(Object obj){
         if((obj == null) || (obj.getClass() != this.getClass())) {
             return false; }
         Libro libro = (Libro) obj;
         if(this.titolo.equalsIgnoreCase(libro.getTitolo())
                && this.autore.equalsIgnoreCase(libro.getAutore())
                && this.anno.equals(libro.getAnno())){
             return true;
         }
         return false;
    }

}
