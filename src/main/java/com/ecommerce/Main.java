package com.ecommerce;

import com.ecommerce.view.MenuPrincipal;

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
     */
    public static void main(String[] args) {
        try {
            // Inicia o menu principal do e-commerce
            MenuPrincipal menuPrincipal = new MenuPrincipal();
            menuPrincipal.iniciarSistema();

        } catch (Exception e) {
            System.err.println("Erro fatal ao iniciar o sistema: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
