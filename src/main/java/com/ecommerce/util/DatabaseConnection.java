package com.ecommerce.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Classe utilitária para gerenciamento de conexões com o banco de dados.
 * Implementa o padrão Singleton para controle de conexões.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class DatabaseConnection {
    
    private static DatabaseConnection instance;
    private static final String CONFIG_FILE = "config.properties";
    
    private String url;
    private String username;
    private String password;
    private String driver;
    
    /**
     * Construtor privado (padrão Singleton).
     * Carrega as configurações do arquivo properties.
     */
    private DatabaseConnection() {
        carregarConfiguracoes();
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver do banco de dados não encontrado: " + driver, e);
        }
    }
    
    /**
     * Obtém a instância única da classe (padrão Singleton).
     * 
     * @return Instância de DatabaseConnection
     */
    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    /**
     * Carrega as configurações do arquivo properties.
     */
    private void carregarConfiguracoes() {
        Properties props = new Properties();
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Arquivo de configuração não encontrado: " + CONFIG_FILE);
            }
            
            props.load(input);
            
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driver = props.getProperty("db.driver");
            
            if (url == null || username == null || password == null || driver == null) {
                throw new RuntimeException("Configurações do banco de dados incompletas no arquivo " + CONFIG_FILE);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar arquivo de configuração: " + CONFIG_FILE, e);
        }
    }
    
    /**
     * Obtém uma nova conexão com o banco de dados.
     * 
     * @return Conexão com o banco de dados
     * @throws SQLException em caso de erro na conexão
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    /**
     * Testa a conexão com o banco de dados.
     * 
     * @return true se a conexão foi bem-sucedida, false caso contrário
     */
    public boolean testarConexao() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao testar conexão: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtém a URL de conexão.
     * 
     * @return URL do banco de dados
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Obtém o nome de usuário do banco.
     * 
     * @return Nome de usuário
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Fecha a conexão de forma segura.
     * 
     * @param conn Conexão a ser fechada
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }
    
    /**
     * Reinicia a instância (útil para testes).
     */
    public static void resetInstance() {
        instance = null;
    }
    
    @Override
    public String toString() {
        return "DatabaseConnection{" +
                "url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", driver='" + driver + '\'' +
                '}';
    }
}
