

package javatest;

import java.sql.*;
import java.util.ArrayList;


public class Nivel {
    private String nombre;
    private static String nombreBBDD;

    
    public static void setNombreBBDD(String nombreBBDD) {
        Nivel.nombreBBDD = nombreBBDD;
    }
    
    public int setNivel(String tema, String nombreNivel){
        //Segun los parametros que recibe se hace un alta de nivel según el tema  
        //Devuelve exito o fracaso
        int error=1; 

        try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");

            try {
                PreparedStatement pr = conn.prepareStatement("INSERT INTO nivel (nombre,tema) VALUES(?,?)");
                pr.setString(1, nombreNivel); 
                pr.setString(2, tema); 
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
    
    public ArrayList getNiveles(String tema){
        //Se devuelve la lista de los nombres de los niveles disponibles de un tema
        //según el tema pasado
        int num=0;
        ArrayList losNiveles = new ArrayList();
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                Statement st = conn.createStatement();


            try {

                ResultSet rs=st.executeQuery("SELECT * FROM nivel WHERE tema='" + tema+"'");

                while (rs.next()){
                    losNiveles.add(num, rs.getString("nombre"));
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

        return losNiveles;
        
    }
    

    public int deleteNivel(String nivel, String tema){
        //Elimina el nivel de ese tema de la BD y sus tests y devuelve exito o fracaso
        int error=1;
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                Statement st = conn.createStatement();
            try { 
                PreparedStatement pr = conn.prepareStatement("DELETE FROM nivel WHERE nombre='" + nivel+"' AND tema='" + tema + "'");              
                pr.executeUpdate();
                Test nuevoTest = new Test();
                try (ResultSet rs = st.executeQuery("SELECT id_test FROM test WHERE nivel='" + nivel+"' AND tema='" + tema + "'")) {
                    while (rs.next()){
                            nuevoTest.deleteTest(rs.getInt("id_test"));

                    }rs.close();
                }

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


