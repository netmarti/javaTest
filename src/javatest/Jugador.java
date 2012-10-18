

package javatest;

import java.sql.*;
import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private static String nombreBBDD;

    
    public static void setNombreBBDD(String nombreBBDD) {
        Jugador.nombreBBDD = nombreBBDD;
    }
    
    public int setJugador(String nombreJugador){
        int error=1; 

        try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");

            try {
                PreparedStatement pr = conn.prepareStatement("INSERT INTO jugador (nombre) VALUES(?)");
                pr.setString(1, nombreJugador);                
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
    
    public ArrayList getJugadores(){
        int num=0;
        ArrayList losJugadores = new ArrayList();
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                Statement st = conn.createStatement();


            try {

                ResultSet rs=st.executeQuery("SELECT * FROM jugador");

                while (rs.next()){
                    losJugadores.add(num, rs.getString("nombre"));
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

        return losJugadores;
        
    }
    
 
    public int deleteJugador(String nombre){
        int error=1;
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            try { 
                Historial nuevoHist = new Historial();
                
                PreparedStatement pr = conn.prepareStatement("DELETE FROM jugador WHERE nombre='" + nombre + "'");              
                pr.executeUpdate();
                
	        nuevoHist.deleteEstadistica(nombre);
                    

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
