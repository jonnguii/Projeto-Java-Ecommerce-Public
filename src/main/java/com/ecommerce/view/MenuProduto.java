package com.ecommerce.view;

import com.ecommerce.service.ProdutoService;

import java.util.Scanner;

/**
 * Menu de gerenciamento de produtos.
 */
public class MenuProduto {
    Scanner scanner = new Scanner(System.in);
    private final ProdutoService produtoService = new ProdutoService();

    public void menuProdutos() {
        System.out.println("\n=== GERENCIAR PRODUTOS ===");
        System.out.println("1. Listar todos os produtos");
        System.out.println("2. Buscar produto por ID");
        System.out.println("3. Buscar produto por SKU");
        System.out.println("4. Buscar produto por nome");
        System.out.println("5. Cadastrar novo produto");
        System.out.println("6. Atualizar produto");
        System.out.println("7. Desativar produto");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    produtoService.listarProdutos();
                    break;
                case 2:
                    produtoService.buscarProdutoPorId();
                    break;
                case 3:
                    produtoService.buscarProdutoPorSku();
                    break;
                case 4:
                    produtoService.buscarProdutoPorNome();
                    break;
                case 5:
                    produtoService.cadastrarProduto();
                    break;
                case 6:
                    produtoService.atualizarProduto();
                    break;
                case 7:
                    produtoService.desativarProduto();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
        }
    }
}
