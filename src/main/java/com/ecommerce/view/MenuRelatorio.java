package com.ecommerce.view;

import com.ecommerce.service.RelatorioService;

import java.util.Scanner;

/**
 * Menu de relatórios.
 */
public class MenuRelatorio {
    Scanner scanner = new Scanner(System.in);
    private final RelatorioService relatorioService = new RelatorioService();

    public void menuRelatorios() {
        System.out.println("\n=== RELATÓRIOS ===");
        System.out.println("1. Produtos mais vendidos");
        System.out.println("2. Produtos com estoque baixo");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    relatorioService.relatorioProdutosMaisVendidos();
                    break;
                case 2:
                    relatorioService.relatorioProdutosEstoqueBaixo();
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
