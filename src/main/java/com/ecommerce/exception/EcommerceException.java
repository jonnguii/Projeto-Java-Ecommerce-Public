package com.ecommerce.exception;

/**
 * Exceção base para o sistema de e-commerce.
 * Todas as exceções específicas do sistema devem herdar desta classe.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class EcommerceException extends Exception {
    
    /**
     * Construtor padrão.
     */
    public EcommerceException() {
        super();
    }
    
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message Mensagem de erro
     */
    public EcommerceException(String message) {
        super(message);
    }
    
    /**
     * Construtor com mensagem e causa.
     * 
     * @param message Mensagem de erro
     * @param cause Causa da exceção
     */
    public EcommerceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Construtor com causa.
     * 
     * @param cause Causa da exceção
     */
    public EcommerceException(Throwable cause) {
        super(cause);
    }
}
