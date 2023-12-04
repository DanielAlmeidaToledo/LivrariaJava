package ifsp.edu.source.DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

// Gerencia a conexão e criação de tabelas no banco de dados SQLite.
// Singleton para garantir uma única instância de conexão durante a execução.
// Autor: Daniel Toledo
public class DataBaseCom {

    private static Connection connection = null;
    private static Statement statement = null;

    // Conecta ao banco de dados e cria as tabelas.
    public DataBaseCom() {
        conectar();
        criarTabelas();
    }

    // Obtém a instância da conexão.
    public static Connection getConnection() {
        conectar();
        return connection;
    }

    // Obtém a instância do objeto Statement.
    public static Statement getStatement() {
        conectar();
        return statement;
    }

    // Estabelece a conexão com o SQLite.
    public static Connection conectar() {
        try {
            if (connection == null) {
                connection = DriverManager.getConnection("jdbc:sqlite:livraria.db");
                statement = connection.createStatement();
                statement.setQueryTimeout(30); // timeout de 30 segundos.
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    // Cria as tabelas no banco de dados se não existirem.
    public boolean criarTabelas() {
        try {
            statement.executeUpdate("create table if not exists pessoa (id TEXT PRIMARY KEY, nome TEXT)");
            statement.executeUpdate(
                    "create table if not exists venda (id TEXT  PRIMARY KEY, id_client TEXT, data TEXT)");
            statement.executeUpdate(
                    "create table if not exists produto (id TEXT  PRIMARY KEY, nome TEXT, qtde INTEGER, preco REAL)");
            statement.executeUpdate(
                    "create table if not exists item_produto (id TEXT  PRIMARY KEY, id_venda TEXT, id_produto TEXT)");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    // Fecha a conexão com o banco de dados.
    public static void close() {
        try {
            getConnection().close();
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseCom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
