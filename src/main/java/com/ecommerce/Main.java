package com.ecommerce;

import com.ecommerce.service.EcommerceService;

/**
 * Classe principal do sistema de e-commerce.
 * Ponto de entrada da aplicação Java.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Main {
    
    /**
     * Método principal que inicia a aplicação.
     * 
     * @param args Argumentos da linha de comando (não utilizados)
     */
    public static void main(String[] args) {
        try {
            // Inicia o serviço principal do e-commerce
            EcommerceService ecommerceService = new EcommerceService();
            ecommerceService.iniciarSistema();
            
        } catch (Exception e) {
            System.err.println("Erro fatal ao iniciar o sistema: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
