
package ifsp.edu.source.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author aluno
 */
public class DataBaseCom {

    private static Connection connection = null;
    private static Statement statement = null;
    
    public DataBaseCom() {
    	conectar();
    	criarTabelas();
    }

    public static Connection getConnection() {
          conectar();
          return connection;
    }
    
    public static Statement getStatement(){
    	conectar();
        return statement;
    }

    public static Connection conectar() {
        try {
            if(connection==null) {
            	connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            	statement = connection.createStatement();
            	statement.setQueryTimeout(30);  // set timeout to 30 sec.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public boolean criarTabelas() {
        try {
            //statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table if not exists pessoa (id INTEGER PRIMARY KEY, nome TEXT)");
            statement.executeUpdate("create table if not exists venda (id INTEGER  PRIMARY KEY, id_client INTEGER, data TEXT)");
            statement.executeUpdate("create table if not exists produto (id INTEGER  PRIMARY KEY, nome TEXT, qtde INTEGER, preco REAL)");
            statement.executeUpdate("create table if not exists item_produto (id INTEGER  PRIMARY KEY, id_venda INTEGER, id_produto INTEGER)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public static void close(){
        try {
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
