
package javatest;

import java.util.ArrayList;

public class Pregunta {
    private static int NumRespuestas; //número de respuestas de cada test
    private String test,nombre; 
    private ArrayList<String> Respuesta = new ArrayList();
    private int numPreg, pregCorrecta;
    
    
    
    public Pregunta(){
        
    }
    //En la base de datos la correcta es la 1
    public Pregunta (String nombrePregunta, ArrayList<String> respuesta, int preguntaCorrecta){
        this.nombre=nombrePregunta;
        for(int i=0; i<NumRespuestas; i++){
            this.Respuesta.add(i, respuesta.get(i));
        }
        this.pregCorrecta=preguntaCorrecta;
    }
    
    
    
    
    public static void setNumRespuestas(int NumRespuestas) {
        Pregunta.NumRespuestas = NumRespuestas;
    }
    
    public String devuelveTest(){
        return this.test;
    }
    public int devuelveNumPreg(){
        return this.numPreg;
    }
    public String devuelveNombre(){
        return this.nombre;
    }
    
    public String devuelveRespuesta(int i){
        return this.Respuesta.get(i-1);
    }
    
    
    
    public int devuelvePregCorrecta(){
        return this.pregCorrecta;
    }
}