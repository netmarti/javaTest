 
package javatest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Test {
    private static int NumRespuestas; //número de respuestas de cada test
    private String nombre,tema, nivel;
    private static String nombreBBDD; 
    private String cadenaSQL;
    private Pregunta nuevaPregunta= new Pregunta();
    
    
    public Test(){
        
    }
    //Solo para las listas
    public Test(String nombre){
        this.nombre=nombre;

    }
    public static void setNumRespuestas(int NumRespuestas) {
        Test.NumRespuestas = NumRespuestas;
    }

    public static void setNombreBBDD(String nombreBBDD) {
        Test.nombreBBDD = nombreBBDD;
    }
    
    
    public Test(String nombre, String tema, String nivel){
        this.nombre=nombre;
        this.tema=tema;
        this.nivel=nivel;

    }
    
    
    public String devuelveNombre(){
        return this.nombre;
    }
    public String devuelveTema(){
        return this.tema;
    }
    public String devuelveNivel(){
        return this.nivel;
    }
             
    

    public int setTest(String tema, String nivel, String nombreTest, HashMap  preguntas){
        //Segun los parametros que recibe se hace un alta de test con sus preguntas
        //Devuelve exito o fracaso
        int error=1; 
        int num,i;
        int idTest;
        String cadenaRespuestas = "", cadenaInterrogantes = "";
        String temporalResp = "", temporalInterrog = "";
        
        try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            Statement st = conn.createStatement();

            try {
                
                PreparedStatement pr = conn.prepareStatement("INSERT INTO test (nombre,tema,nivel) VALUES(?,?,?)");
                pr.setString(1, nombreTest);
                pr.setString(2, tema);
                pr.setString(3, nivel);
                pr.executeUpdate();
                
                
                idTest=getIdTest(tema, nivel, nombreTest);
                for(int j=1; j<=NumRespuestas; j++){
                        temporalResp = ",respuesta"+j;
                        cadenaRespuestas = cadenaRespuestas + temporalResp; 
                        temporalInterrog = ",?";
                        cadenaInterrogantes = cadenaInterrogantes + temporalInterrog;
                }
                cadenaSQL =  "INSERT INTO pregunta (id_test,num_pregunta, "
                                + "nombre" +cadenaRespuestas+   ") VALUES(?,?,?"
                                + "" +cadenaInterrogantes+ ")";
                
                num=preguntas.size();
                for(i=1; i<=num;i++){
                    nuevaPregunta = (Pregunta)preguntas.get(i);  
                    PreparedStatement p = conn.prepareStatement(cadenaSQL);
                            
                    p.setInt(1, idTest);
                    p.setInt(2, i);
                    p.setString(3, nuevaPregunta.devuelveNombre());
                    for(int k=4; k<NumRespuestas+4; k++){
                        p.setString(k, nuevaPregunta.devuelveRespuesta(k-3));
                    }
                    int retorno = p.executeUpdate();
				      
		System.out.println("éxito al introducir test:"+retorno); 
                    
                   
                }  
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
    
    public ArrayList getTests(String tema, String nivel){
        //Segun el test pasado se busca en la BD y se devuelven los nombres de los tests
        ArrayList tests = new ArrayList(); 
        int num=0;
    
     try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
            DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            Statement st = conn.createStatement();
            

        try {
        
            ResultSet rs=st.executeQuery("SELECT * FROM test WHERE tema='" + tema+"' AND nivel='" + nivel+"'");
            
            while (rs.next()){
                
                tests.add(num,rs.getString("nombre"));
            
                num++;

            }rs.close();
        } catch (Throwable e){
        System.out.println("Ha fallado la consulta de datos de test");
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
        
        return tests;
    }
    
    public int getIdTest(String tema, String nivel, String test){
        int idTest = 0;
            
     try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
            DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            Statement st = conn.createStatement();
            

        try {
        
            ResultSet rs=st.executeQuery("SELECT * FROM test WHERE nombre='" + test+"' AND tema='" + tema+"' AND nivel='" + nivel+"'");
                        
            while (rs.next()){
                
                idTest=rs.getInt("id_test");

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
        
        return idTest;
    }
    

       
    public HashMap getPreguntaTest(int idTest){
        HashMap<Integer, Pregunta> preguntas = new HashMap<>(); 
        ArrayList<String> preguntasArray = new ArrayList();;
        int num=1;
    
     try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
            DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            Statement st = conn.createStatement();
            

        try {
            ResultSet rs=st.executeQuery("SELECT * FROM pregunta WHERE id_test=" + idTest);
            while (rs.next()){
                for (int i= 0; i<NumRespuestas; i++){
                    int j=i+1;
                    preguntasArray.add(i, rs.getString("respuesta"+j));
                }
                preguntas.put(rs.getInt("num_pregunta"), new Pregunta(rs.getString("nombre"),
                        preguntasArray,1));
                
                //num++;

            }rs.close();
        } catch (Throwable e){
        System.out.println("Ha fallado la consulta de datos [getPreguntasTest(nombre)]");
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
        
        return preguntas;
    }
    
    
    
   

    public int modificarTest(String tema, String nivel, String nombreTest, HashMap preguntas){ 
        //Para modificar un test en la BD según los parámetros recibidos
        //Devuelve exito o fracaso
        int error=1; 
        int idTest;
        int num,i;
        String cadenaRespuestas = "";
        String temporalResp = "";

        try{
            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");

            try {
                idTest=getIdTest(tema, nivel, nombreTest);
                for(int j=1; j<=NumRespuestas; j++){
                        temporalResp = ",respuesta"+j+"=?";
                        cadenaRespuestas = cadenaRespuestas + temporalResp; 
                }
                num=preguntas.size();
                for(i=1; i<=num;i++){
                    nuevaPregunta = (Pregunta)preguntas.get(i);
                    PreparedStatement p = conn.prepareStatement("UPDATE pregunta SET nombre=?"+cadenaRespuestas+ "WHERE id_test="+ idTest +" AND num_pregunta="  +i );

                    p.setString(1, nuevaPregunta.devuelveNombre());
                    for(int k=2; k<NumRespuestas+2; k++){
                        p.setString(k, nuevaPregunta.devuelveRespuesta(k-1));
                    }

                    int retorno = p.executeUpdate();
				      
		System.out.println("éxito al modificar test, retorno:"+retorno); 
                    //p.executeUpdate();
                }  
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
    
    public int deleteTest(int idTest){
        //Elimina el test de la BD y devuelve exito o fracaso
        int error=1;
    
        try{
                String driver = "org.apache.derby.jdbc.EmbeddedDriver";
                Class.forName(driver).newInstance();
                String protocolo = "jdbc:derby:";
                Connection conn =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
                
            try { 
                PreparedStatement pr = conn.prepareStatement("DELETE FROM test WHERE id_test=" + idTest);              
                pr.executeUpdate();
                PreparedStatement pr2 = conn.prepareStatement("DELETE FROM pregunta WHERE id_test=" + idTest);             
                pr2.executeUpdate();
                
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
    
    
    public void crearDataBase(){
        String cadenaRespuestas = "";
        String temporalResp = "" ;
	System.out.println("Espere por favor...");

        try {

            String driver = "org.apache.derby.jdbc.EmbeddedDriver";
            Class.forName(driver).newInstance();
            String protocolo = "jdbc:derby:";
            Connection con =
                DriverManager.getConnection(protocolo + nombreBBDD + ";create=true");
            Statement s = con.createStatement();
            // creación de las tablas
            try {
                System.out.println("Creando base de datos...");
                
                String query1 ="CREATE  TABLE tema ("
                                +"id_tema INT  NOT NULL generated always as identity ,"
                                +"nombre VARCHAR(45) UNIQUE NOT NULL ,"                                
                                +"PRIMARY KEY (id_tema) )";
                s.execute(query1);


                String query2 ="CREATE  TABLE nivel ("
                                +"id_nivel INT  NOT NULL generated always as identity ,"
                                +"nombre VARCHAR(45) NOT NULL ,"
                                +"tema VARCHAR(45) NOT NULL ,"
                                +"PRIMARY KEY (id_nivel) )";
                s.execute(query2);  


                String query3 ="CREATE  TABLE test ("
                                +"id_test INT NOT NULL generated always as identity ,"
                                +"nombre VARCHAR(45) NOT NULL ,"
                                +"tema VARCHAR(45) NOT NULL ,"
                                +"nivel VARCHAR(45) NOT NULL ,"
                                +"PRIMARY KEY (id_test) )";
                s.execute(query3);  
                
                for(int j=1; j<=NumRespuestas; j++){
                        temporalResp = ", respuesta"+j+ "  VARCHAR(250) NOT NULL ";
                        cadenaRespuestas = cadenaRespuestas + temporalResp; 
                }
                System.out.println("rewpuestaas:"+NumRespuestas);
                System.out.println("cadena:"+cadenaRespuestas);

                
                String query6="CREATE  TABLE pregunta ("                               
                                +"id_test INT NOT NULL ,"
                                +"num_pregunta INT NOT NULL ,"
                                +"nombre VARCHAR(250) NOT NULL "+cadenaRespuestas+")";
                System.out.println("cadenaEntera:"+query6);
                s.execute(query6);

                String query4 ="CREATE  TABLE jugador ("
                                +"nombre VARCHAR(45) NOT NULL ,"
                                +"PRIMARY KEY(nombre))";
                s.execute(query4); 


                String query5 ="CREATE  TABLE estadistica ("
                                +"nombre_jugador VARCHAR(45) NOT NULL ,"
                                +"tema VARCHAR(45) NOT NULL ,"
                                +"nivel VARCHAR(45) NOT NULL ,"
                                +"test VARCHAR(45) NOT NULL ,"
                                +"resp_correctas INT ,"
                                +"resp_incorrectas INT ,"
                                +"fecha VARCHAR(45) )";
                s.execute(query5);
                               
                System.out.println("Base de datos creada");
                
            } catch (Exception e) {
                
                e.printStackTrace();
                System.out.println("error, no se pudo crear la tabla, "
                        + "posiblemente ya existe");
            }

            if (driver.equals("org.apache.derby.jdbc.EmbeddedDriver")) {
                cerrarDataBase();
            }

        } catch (Exception e) {
            
            e.printStackTrace();
        }

    }
//cerrar la conexión, según el manual de derby ( administracion )     
    public void cerrarDataBase(){   
                boolean gotSQLExc = false;
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                } catch (SQLException se) {
                    if (se.getSQLState().equals("XJ015")) {
                        gotSQLExc = true;
                    }
                }
                if (!gotSQLExc) {
                    System.out.println("La base de datos no se pudo cerrar correctamente");
                } else {
                    System.out.println("Base de datos cerrada correctamente");
                }
        
    }
   
}


    
