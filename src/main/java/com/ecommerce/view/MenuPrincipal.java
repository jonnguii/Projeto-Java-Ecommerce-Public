package com.ecommerce.view;

import com.ecommerce.util.DatabaseConnection;

import java.util.Scanner;

/**
 * Inicia o sistema e exibe o menu principal.
 */

public class MenuPrincipal {

    Scanner scanner = new Scanner(System.in);
    MenuCliente menuClientes = new MenuCliente();
    MenuEstoque menuEstoque = new MenuEstoque();
    MenuPedido menuPedidos = new MenuPedido();
    MenuProduto menuProdutos = new MenuProduto();
    MenuRelatorio menuRelatorios = new MenuRelatorio();
    MenuRN menuRN = new MenuRN();

    public void iniciarSistema() {

        // Testa conexão com o banco
        if (!DatabaseConnection.getInstance().testarConexao()) {
            System.err.println("ERRO: Não foi possível conectar ao banco de dados.");
            System.err.println("Verifique se o PostgreSQL está rodando e as configurações estão corretas.");
            return;
        }

        System.out.println("=".repeat(40));
        System.out.println("Iniciando Sistema de E-commerce...");
        System.out.println("=".repeat(40) + "\n");
        System.out.println("Testando conexão com o banco de dados...");
        System.out.println("Conexão com banco de dados estabelecida com sucesso!" + "\n");
        System.out.println("=".repeat(45));
        System.out.println("Sistema de E-commerce iniciado com sucesso!");
        System.out.println("=".repeat(45));

        menuPrincipal();
    }

    /**
     * Exibe o menu principal do sistema.
     */
    private void menuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Produtos");
            System.out.println("3. Gerenciar Estoque");
            System.out.println("4. Gerenciar Pedidos");
            System.out.println("5. Relatórios");
            System.out.println("6. Testar Regras de Negócio");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                int opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        menuClientes.menuClientes();
                        break;
                    case 2:
                        menuProdutos.menuProdutos();
                        break;
                    case 3:
                        menuEstoque.menuEstoque();
                        break;
                    case 4:
                        menuPedidos.menuPedidos();
                        break;
                    case 5:
                        menuRelatorios.menuRelatorios();
                        break;
                    case 6:
                        menuRN.testarRegrasNegocio();
                        break;
                    case 0:
                        System.out.println("Encerrando sistema. Até logo!");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
}
