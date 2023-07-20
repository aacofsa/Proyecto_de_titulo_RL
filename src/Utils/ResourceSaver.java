
package Utils;

import java.io.File;
import java.io.PrintWriter;


public final class ResourceSaver implements Constantes{
    
    public void crearPoliticas(){
        try{
            File politicasFile = new File(RESOURCES_DIR + ARCHIVO_POLITICAS);
            PrintWriter pw = new PrintWriter(politicasFile);
            for (int i = 0; i < CANTIDAD_ESTADOS; i++) {
                String values = "";
                for (int j = 0; j < ACCIONES; j++) {
                    values = values.concat("0 ");
                }
                values = values.trim();
                pw.println(values);
            }
            pw.close();
        }catch(Exception e){
            System.out.println("Problemas creando el archivo");
            System.exit(-1);
        }
       
    }
    
    public static void guardarPoliticas(float[][] politics){
        try{
            File politicasFile = new File(RESOURCES_DIR + ARCHIVO_POLITICAS);
            PrintWriter pw = new PrintWriter(politicasFile);
            for(float[] politic : politics){
                String values = "";
                for(int j = 0; j < politic.length; j++){
                    values = values.concat(politic[j]+" ");
                }
                values = values.trim();
                pw.println(values);                
            }
            pw.close();
        }catch(Exception e){
            System.out.println("Problemas creando el archivo");
            System.exit(-1);
        }
       
    }
    
    public static void guardarPoliticasLatex(String tableName, float[][] politics){
        String data = "";
        data = data.concat("\\begin{table}[h]\n");
        data = data.concat("\t\\begin{center}\n");
        data = data.concat("\t\t\\begin{tabular}{| c | c | c | c | c |}\\hline\n");
        data = data.concat("\t\t\tEstados & Izquierda & Arriba & Derecha & Abajo \\\\ \\hline\n");

        for(int i = 0; i < politics.length; i++){
            data = data.concat("\t\t\t");
            data = data.concat(i+" & ");
            for(int j = 0; j < ACCIONES; j++){
                data = data.concat(politics[i][j]+" ");
                if(j < ACCIONES-1){
                    data = data.concat("& ");
                }
            }
            data = data.concat("\\\\ \n");
        }
        data = data.concat("\t\t\t\\hline\n");
        data = data.concat("\t\t\\end{tabular}\n");
        data = data.concat("\t\t\\caption{"+tableName+"}\n");
        data = data.concat("\t\t\\label{tab:"+tableName+"}\n");
        data = data.concat("\t\\end{center}\n");
        data = data.concat("\\end{table}\n");

        try{
            File politicasFile = new File(RESOURCES_DIR + tableName+".txt");
            PrintWriter pw = new PrintWriter(politicasFile);
            pw.println(data);
            pw.close();
        }catch(Exception e){
            System.out.println("Problemas creando el archivo");
            System.exit(-1);
        }
    }
    
    public static void guardarTexto(String filename, String messsage){
        try{
            File textFile = new File(RESOURCES_DIR + filename);
            PrintWriter pw = new PrintWriter(textFile);
            pw.println(messsage);
            pw.close();
        }catch(Exception e){
            System.out.println("Problemas creando el archivo");
            System.exit(-1);
        }
    }
}
