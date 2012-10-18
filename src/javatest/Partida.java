
package javatest;

import java.util.ArrayList;
import java.util.HashMap;

public class Partida {
    private static int NumRespuestas; //número de respuestas de cada test
    private Test test = new Test();
    private Jugador jugador= new Jugador();
    private Tema tema = new Tema();
    private Nivel nivel = new Nivel();
    private Historial histo = new Historial();
    private Pregunta preguntasIn = new Pregunta();

    
    public static int setNumRespuestas(int NumRespuestas) {
        int ret;
        if((NumRespuestas > 1) && (NumRespuestas <= 100)){
            Partida.NumRespuestas = NumRespuestas;
            Test.setNumRespuestas(NumRespuestas);
            Pregunta.setNumRespuestas(NumRespuestas);
            ret = 1;
        }
        else{
            ret = 0;
            System.out.println("Número erroneo de respuestas. El mínimo es 2.");
        }
        
        return ret;
    }
    
    //BASE DE DATOS
    public void crearBaseDatos(){
        test.crearDataBase();
    }
    public int configurarNombreBBDD(String nombreBaseDatos, int NumRespuestas){
        int ret;
        if(setNumRespuestas(NumRespuestas) == 1){
            Historial.setNombreBBDD(nombreBaseDatos);
            Jugador.setNombreBBDD(nombreBaseDatos);
            Nivel.setNombreBBDD(nombreBaseDatos);
            Tema.setNombreBBDD(nombreBaseDatos);
            Test.setNombreBBDD(nombreBaseDatos);
            ret = 1;
        }
        else{
            ret = 0;
        }
        return ret;
    }
    //Cuando se cierra la aplicación se debe cerrar la BBDD
    public void cerrarBaseDatos(){
        test.cerrarDataBase();
    }
    
    
    //JUGADOR
    public ArrayList obtenerListaJugadores(){  
        //Consigue la lista de temas consultando la BD y lo devuelve en un ArrayList      
        ArrayList listaJugadores;
        listaJugadores = jugador.getJugadores();
        
        return listaJugadores; 
    }   
    public int introNuevoJugador(String nombre){
        int ret = jugador.setJugador(nombre);
        return ret; //si ret=0 ha habido un error
    }
    public int eliminarJugador(String nombre){
        int ret = jugador.deleteJugador(nombre);
        return ret; //si ret=0 ha habido un error
    }

    //TEMAS
    public ArrayList obtenerListaTemas(){  
        //Consigue la lista de temas consultando la BD y lo devuelve en un ArrayList      
        ArrayList listaTemas ;
        listaTemas = tema.getTemas();
        
        return listaTemas; 
    }   
    public int introNuevoTema(String nombre){
        int ret = tema.setTema(nombre);
        return ret; //si ret=0 ha habido un error
    }
    public int eliminarTema(String nombre){
        int ret = tema.deleteTema(nombre);
        return ret; //si ret=0 ha habido un error
    }
    
    //NIVELES
    public ArrayList obtenerListaNiveles(String tema) {        
        ArrayList<String> listaNiveles ;
        // Consigue la lista de niveles consultando la BD y lo devuelve en un ArrayList
        listaNiveles = nivel.getNiveles(tema);
        return listaNiveles;
    }
    public int introNuevoNivel(String tema, String nombreNivel){
        int ret = nivel.setNivel(tema, nombreNivel);
        return ret; //si ret=0 ha habido un error
    }
    public int eliminarNivel(String nombreNivel, String tema){
        int ret = nivel.deleteNivel(nombreNivel, tema);
        return ret; //si ret=0 ha habido un error
    }
    
    //TEST
    public ArrayList obtenerListaTest(String tema, String nivel) {        
        ArrayList<String> listaTest ;
        // Consigue la lista de tests consultando la BD y lo devuelve en un ArrayList
        listaTest = test.getTests(tema, nivel);
        return listaTest;
    }
    public int introNuevoTest(String tema, String nombreNivel, String nombreTest, HashMap preguntas){
        int ret = test.setTest(tema, nombreNivel, nombreTest, preguntas);
        return ret; //si ret=0 ha habido un error
    }
    public int eliminarTest(String tema, String nivel, String nombreTest){
        int idTest = test.getIdTest(tema, nivel, nombreTest);
        int ret = test.deleteTest(idTest);
        return ret; //si ret=0 ha habido un error
    }
    public int modificarTest(String tema, String nombreNivel, String nombreTest, HashMap preguntas){
        int ret = test.modificarTest(tema, nombreNivel, nombreTest, preguntas);
        return ret; //si ret=0 ha habido un error
    }
    
    //PREGUNTAS
    public HashMap obtenerPreguntas(String tema, String nivel, String nombreTest){
        String pregTemp[] = new String [NumRespuestas+1];
        ArrayList<String> pregOut = new ArrayList();
        int correcta = 0;
        HashMap<Integer, Pregunta> listaPreguntasIn;
        HashMap<Integer, Pregunta> listaPreguntasOut = new HashMap<>();
        int idTest = test.getIdTest(tema, nivel, nombreTest);
        listaPreguntasIn = test.getPreguntaTest(idTest);  
        
        for(int i=1 ; i <= listaPreguntasIn.size() ; i++){
            ArrayList<Integer> listaAleatoria = aleatorio();
            preguntasIn = (Pregunta) listaPreguntasIn.get(i);
            for(int aleat = 0; aleat<NumRespuestas;aleat++){
                int temp = (int)listaAleatoria.get(aleat);
                pregTemp[(int)listaAleatoria.get(aleat)-1] = preguntasIn.devuelveRespuesta(aleat+1) ;
                if(aleat == 0){
                    correcta = temp; //Posición de la correcta en el HashMap
                } 
            }
            for(int t = 0; t<NumRespuestas;t++){
                pregOut.add(t, pregTemp[t]);
            }
            
                //Se asigna al nuevo HashMap la colocación aleatoria de las respuestas
                listaPreguntasOut.put(i, new Pregunta (preguntasIn.devuelveNombre(),pregOut, correcta)); 
                //'correcta' indica la posición de la respuesta correcta 
        }
        return listaPreguntasOut;
    }
    
    public ArrayList<Integer> aleatorio() {
        //Lista enlazada
        java.util.LinkedList<Integer> lista = new java.util.LinkedList<>();
        ArrayList<Integer> numeros = new ArrayList();
        Integer[] result;
        for (int i = 0; i < NumRespuestas; lista.add(++i));
            java.util.Collections.shuffle(lista);
        result = lista.toArray(new Integer[0]);
        for (Integer i : result){ 
            //System.out.println(i);
            numeros.add(i);
            
        }
        return numeros;
    }
    
    
    //HISTORIAL
    public HashMap<Integer, Historial> obtenerEstadisticas(String jugador){
        HashMap<Integer, Historial> listaEstadistica;
        listaEstadistica = histo.getEstadistica(jugador);        
        return listaEstadistica;
    }
    public int introEstadistica(String jugador, String tema, String nivel, String test, 
                                int correctas, int incorrectas){
        int ret = histo.setEstadistica(jugador, tema, nivel, test, correctas, incorrectas);
        return ret; //si ret=0 ha habido un error
    }
    public int eliminarEstadistica(String jugador){
        int ret = histo.deleteEstadistica(jugador);
        return ret; //si ret=0 ha habido un error
    }
    
    
}