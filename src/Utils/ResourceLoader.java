
package Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceLoader implements Constantes{

    public static int[][] cargarRecompensas(){
        List<int[]> valores = new ArrayList<>();
        try{
            File archivo = new File(RESOURCES_DIR + ARCHIVO_RECOMPENSAS);
            Scanner sc = new Scanner(archivo);
            for(int i = 0; sc.hasNextLine() ; i++){
                String linea = sc.nextLine();
                String[] strValues = linea.split(",");
                int[] intLine = new int[ANCHO];
                for(int j = 0; j < strValues.length; j++){
                    intLine[j] = Integer.parseInt(strValues[j]);
                }
                valores.add(intLine);
            }
            sc.close();
        }catch(Exception e){
            System.out.println("Problemas leyendo el archivo: "+e);
            System.exit(-1);
        }
        int[][] recompensas = new int[LARGO][ANCHO];
        return valores.toArray(recompensas);
    }

    public static char[][] cargarEscenario(){
        List<char[]> valores = new ArrayList<>();
        try{
            File archivo = new File(RESOURCES_DIR + ARCHIVO_ESCENARIO);
            Scanner sc = new Scanner(archivo);
            for(int i = 0; sc.hasNextLine() ; i++){
                String linea = sc.nextLine();
                valores.add(linea.toCharArray());
            }
            sc.close();
        }catch(Exception e){
            System.out.println("Problemas leyendo el archivo: "+e);
            System.exit(-1);
        }
        char[][] escenario = new char[LARGO][ANCHO];
        return valores.toArray(escenario);
    }

    public static float[][] cargarPoliticas(){
        List<float[]> values = new ArrayList<>();
        
        try{
            File politicasFile = new File(RESOURCES_DIR + ARCHIVO_POLITICAS);
            Scanner sc = new Scanner(politicasFile);
            for(int i = 0; sc.hasNextLine() ; i++){
                String line = sc.nextLine();
                String[] strValues = line.split(" ");
                float[] floatLine = new float[ACCIONES];
                for(int j = 0; j < strValues.length; j++){
                    floatLine[j] = Float.parseFloat(strValues[j]);
                }
                values.add(floatLine);
            }
            sc.close();
        }catch(Exception e){
            float[][] politics = new float[CANTIDAD_ESTADOS][ACCIONES];
            return politics;
        }
        float[][] politics = new float[values.size()][ACCIONES];
        return values.toArray(politics);
    }
}
