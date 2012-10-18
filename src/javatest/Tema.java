

package javatest;

import java.sql.*;
import java.util.ArrayList;


public class Tema {
    private String nombre;
    private static String nombreBBDD;

    
    
    public static void setNombreBBDD(String nombreBBDD) {
        Tema.nombreBBDD = nombreBBDD;
    }
    
    public int setTema(String nombreTema){
        //Segun los parametros que recibe se hace un alta de tema 
        //Devuelve exito o fracaso
        int error=1; 

        try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");

            try {
                PreparedStatement pr = conn.prepareStatement("INSERT INTO tema (nombre) VALUES(?)");
                pr.setString(1, nombreTema);                
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
    
    public ArrayList getTemas(){
        //Se devuelve la lista de los nombres de los temas disponibles 
        int num=0;
        ArrayList losTemas = new ArrayList();
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                Statement st = conn.createStatement();


            try {

                ResultSet rs=st.executeQuery("SELECT * FROM tema");

                while (rs.next()){
                    losTemas.add(num, rs.getString("nombre"));
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

        return losTemas;
        
    }
    

    public int deleteTema(String tema){
        //Elimina el tema de la BD y todos sus niveles y tests y devuelve exito o fracaso
        int error=1;
        
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                    DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                Statement st = conn.createStatement();
            try { 
                Test nuevoTest = new Test();
                PreparedStatement pr = conn.prepareStatement("DELETE FROM tema WHERE nombre='" + tema + "'");              
                pr.executeUpdate();
                PreparedStatement pr2 = conn.prepareStatement("DELETE FROM nivel WHERE tema='" + tema + "'");              
                pr2.executeUpdate();
                ResultSet rs=st.executeQuery("SELECT id_test FROM test WHERE tema='" + tema + "'");              
                
                    while (rs.next()){
			nuevoTest.deleteTest(rs.getInt("id_test"));
                    }rs.close();

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
