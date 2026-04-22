package com.ecommerce.exception;

/**
 * Exceção lançada em caso de erros de banco de dados.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */
public class DatabaseException extends EcommerceException {
    
    /**
     * Construtor padrão.
     */
    public DatabaseException() {
        super();
    }
    
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message Mensagem de erro
     */
    public DatabaseException(String message) {
        super(message);
    }
    
    /**
     * Construtor com mensagem e causa.
     * 
     * @param message Mensagem de erro
     * @param cause Causa da exceção
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construtor com causa.
     * 
     */
    public DatabaseException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Construtor específico para erros de conexão.
     * 
     * @param detalhe Detalhe do erro de conexão
     */
    public DatabaseException(String detalhe, String tipoErro) {
        super("Erro de banco de dados - " + tipoErro + ": " + detalhe);
    }
    
    /**
     * Factory method para exceções de INSERT.
     * 
     * @param tabela Nome da tabela
     * @param causa Causa original
     * @return DatabaseException
     */
    public static DatabaseException insertError(String tabela, Throwable causa) {
        return new DatabaseException(
            "Erro ao inserir dados na tabela " + tabela + ": " + causa.getMessage(), 
            causa
        );
    }
    
    /**
     * Factory method para exceções de UPDATE.
     * 
     * @param tabela Nome da tabela
     * @param causa Causa original
     * @return DatabaseException
     */
    public static DatabaseException updateError(String tabela, Throwable causa) {
        return new DatabaseException(
            "Erro ao atualizar dados na tabela " + tabela + ": " + causa.getMessage(), 
            causa
        );
    }
    
    /**
     * Factory method para exceções de DELETE.
     * 
     * @param tabela Nome da tabela
     * @param causa Causa original
     * @return DatabaseException
     */
    public static DatabaseException deleteError(String tabela, Throwable causa) {
        return new DatabaseException(
            "Erro ao excluir dados da tabela " + tabela + ": " + causa.getMessage(), 
            causa
        );
    }
    
    /**
     * Factory method para exceções de SELECT.
     * 
     * @param tabela Nome da tabela
     * @param causa Causa original
     * @return DatabaseException
     */
    public static DatabaseException selectError(String tabela, Throwable causa) {
        return new DatabaseException(
            "Erro ao consultar dados da tabela " + tabela + ": " + causa.getMessage(), 
            causa
        );
    }
    
    /**
     * Factory method para exceções de conexão.
     * 
     * @param causa Causa original
     * @return DatabaseException
     */
    public static DatabaseException connectionError(Throwable causa) {
        return new DatabaseException(
            "Erro ao conectar ao banco de dados: " + causa.getMessage(), 
            causa
        );
    }
}
