package Utils;

public interface Constantes {
    
    public final char CAMINO='C';    
    public final char ENTIDAD='E';    
    public final char PARED='P';    
    public final char RECOMPENSA = 'R';
    public final char FANTASMA = 'F';

    public final static int ANCHO = 4;
    public final static int LARGO = 4;
    public final static int ACCIONES = 4;
    public final static int CANTIDAD_ESTADOS = 16;
    
    public final float LEARNING_RATE = 0.01f;
    public final float DISCOUNT_FACTOR = 0.99f;
    public final float EPSYLON = 0.2f;
    
    public final String RESOURCES_DIR = "src/Resources/";
    public final String ARCHIVO_POLITICAS = "politicas.txt";
    public final String ARCHIVO_ESCENARIO = "escenario.txt";
    public final String ARCHIVO_RECOMPENSAS = "recompensas.txt";
    
    public final int PENALIZACION=-10;
    public final int INICIO_X=0;
    public final int INICIO_Y=2;
}

