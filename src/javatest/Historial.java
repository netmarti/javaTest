

package javatest;

import java.sql.*;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;


public class Historial {
    private String jugador; 
    private String tema;
    private String nivel;
    private String test;
    private int correctas;
    private int incorrectas;
    private String fecha;
    private static String nombreBBDD;
    
    public Historial(){
        
    }
    
    public Historial (String jugador, String tema, String nivel, String test, 
                        int correctas, int incorrectas,String fecha){
        this.jugador=jugador;
        this.tema=tema;
        this.nivel=nivel;
        this.test=test;
        this.correctas=correctas;
        this.incorrectas=incorrectas;
        this.fecha=fecha;
    }

    public static void setNombreBBDD(String nombreBBDD) {
        Historial.nombreBBDD = nombreBBDD;
    }
    
    
    public String devuelveJugador(){
        return this.jugador;
    }
    public String devuelveTema(){
        return this.tema;
    }
    public String devuelveNivel(){
        return this.nivel;
    }  
    public String devuelveTest(){
        return this.test;
    }
    public int devuelveCorrectas(){
        return this.correctas;
    }
    public int devuelveIncorrectas(){
        return this.incorrectas;
    }
    
    public String devuelveFecha(){
        return this.fecha;
    }
    
    public String fecha(){        
        Date fechaAhora = new Date();
        String dateString = DateFormat.getInstance().format(fechaAhora);
        
        return dateString;
    }
    
    public int setEstadistica(String jugador, String tema, String nivel, String test, 
                                int correctas, int incorrectas){
        String Fecha = fecha();
        int error=1; 
        
        try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");

            try {
                PreparedStatement pr = conn.prepareStatement("INSERT INTO estadistica (nombre_jugador,tema,nivel,test,resp_correctas,resp_incorrectas,fecha) VALUES(?,?,?,?,?,?,?)");
                pr.setString(1, jugador); 
                pr.setString(2, tema);
                pr.setString(3, nivel);
                pr.setString(4, test);
                pr.setInt(5, correctas);
                pr.setInt(6,incorrectas);
                pr.setString(7,Fecha);
                pr.executeUpdate();
                  
            } catch (Throwable e){
                System.out.println("Error al introducir datos");
                e.printStackTrace();
                error=0;//Error en la insercion
            } finally {
                try { conn.close(); }
                catch (Throwable t){
                    System.out.println("Error al al cerrar Connection");
                    }
            }
        } catch (Exception e) {
            
            e.printStackTrace();
            error=0;//Error 
        }       
        return error;        
    }
    
    public HashMap<Integer, Historial> getEstadistica(String jugador){
        int num=1;
        HashMap<Integer, Historial> estadistica = new HashMap();
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                Statement st = conn.createStatement();


            try {

                ResultSet rs=st.executeQuery("SELECT * FROM estadistica WHERE nombre_jugador='" + jugador+"'");

                while (rs.next()){
                    estadistica.put(num,new Historial(rs.getString("nombre_jugador"),rs.getString("tema"),rs.getString("nivel"),
                            rs.getString("test"),rs.getInt("resp_correctas"),rs.getInt("resp_incorrectas"),rs.getString("fecha")));
            
                    num++;
                    
                }rs.close();
            } catch (Throwable e){
            System.out.println("Ha fallado la consulta de datos");
            e.printStackTrace();
            } finally {
            try { conn.close(); }
            catch (Throwable t){
                System.out.println("Error al al cerrar Connection");
                }
            }
        } catch (Exception e) {
                
                e.printStackTrace();
        }  

        return estadistica;
        
    }
    
 
    public int deleteEstadistica(String nombreJugador){
        int error=1;
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            try { 
                PreparedStatement pr = conn.prepareStatement("DELETE FROM estadistica WHERE nombre_jugador='" + nombreJugador + "'");              
                pr.executeUpdate();
                

            } catch (Throwable e){
                System.out.println("Ha fallado la eliminación de datos");
                error=0;
                e.printStackTrace();
            } finally {
            try { conn.close(); }
            catch (Throwable t){
                System.out.println("Error al al cerrar Connection");
                }
            }
        } catch (Exception e) {
                
                e.printStackTrace();
        }  
        return error;
    }
    
    
    
    
}
