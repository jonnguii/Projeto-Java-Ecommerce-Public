package com.ecommerce.view;

import com.ecommerce.service.PedidoService;

import java.util.Scanner;

/**
 * Menu de gerenciamento de pedidos.
 */
public class MenuPedido {
    Scanner scanner = new Scanner(System.in);
    private final PedidoService pedidoService = new PedidoService();

    public void menuPedidos() {
        System.out.println("\n=== GERENCIAR PEDIDOS ===");
        System.out.println("1. Criar novo pedido");
        System.out.println("2. Listar pedidos");
        System.out.println("3. Buscar pedido por ID");
        System.out.println("4. Atualizar status do pedido");
        System.out.println("5. Cancelar pedido");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    pedidoService.criarPedido();
                    break;
                case 2:
                    pedidoService.listarPedidos();
                    break;
                case 3:
                    pedidoService.buscarPedidoPorId();
                    break;
                case 4:
                    pedidoService.atualizarStatusPedido();
                    break;
                case 5:
                    pedidoService.cancelarPedido();
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
