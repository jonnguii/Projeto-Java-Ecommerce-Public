package com.ecommerce.view;

import com.ecommerce.service.EstoqueService;

import java.util.Scanner;

/**
 * Menu de gerenciamento de estoque.
 */
public class MenuEstoque {
    Scanner scanner = new Scanner(System.in);
    private final EstoqueService estoqueService = new EstoqueService();

    public void menuEstoque() {
        System.out.println("\n=== GERENCIAR ESTOQUE ===");
        System.out.println("1. Listar todo o estoque");
        System.out.println("2. Verificar estoque de um produto");
        System.out.println("3. Adicionar produtos ao estoque");
        System.out.println("4. Remover produtos do estoque");
        System.out.println("5. Listar produtos com estoque baixo");
        System.out.println("6. Listar produtos sem estoque");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    estoqueService.listarEstoque();
                    break;
                case 2:
                    estoqueService.verificarEstoqueProduto();
                    break;
                case 3:
                    estoqueService.adicionarEstoque();
                    break;
                case 4:
                    estoqueService.removerEstoque();
                    break;
                case 5:
                    estoqueService.listarEstoqueBaixo();
                    break;
                case 6:
                    estoqueService.listarSemEstoque();
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
