import java.security.PublicKey;
import java.util.IdentityHashMap;
import java.util.List;

public class Libri {
    private List<Libro> libri = null;

    public void setLibri (List<Libro> libri){
        this.libri = libri;
    }

    public List<Libro> getLibri(){
        return libri;
    }

    public void addLibro(Libro libro) {
        this.libri.add(libro);
    }

    public Libro seeLibro(Integer index){
        return this.libri.get(index);
    }

    public Integer size(){
        return this.libri.size();
    }

    public Libro rimuoviLibro (Integer index){
        return libri.remove((int) index);
    }

    public boolean rimuoviLibro (Libro libro){
        return libri.remove(libro);
    }

    public boolean isDuplicato (Libro libro){
        return libri.contains(libro);
    }

    public Libro modLibro (Integer index,Libro libro){
        return this.libri.set(index,libro);
    }
}
