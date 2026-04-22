package com.ecommerce.view;


import com.ecommerce.service.ClienteService;

import java.util.Scanner;

/**
 * Menu de gerenciamento de clientes.
 */
public class MenuCliente {

    Scanner scanner = new Scanner(System.in);
    private final ClienteService clienteService = new ClienteService();

    public void menuClientes() {
        System.out.println("\n=== GERENCIAR CLIENTES ===");
        System.out.println("1. Listar todos os clientes");
        System.out.println("2. Buscar cliente por ID");
        System.out.println("3. Buscar cliente por CPF");
        System.out.println("4. Cadastrar novo cliente");
        System.out.println("5. Atualizar cliente");
        System.out.println("6. Desativar cliente");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    clienteService.listarClientes();
                    break;
                case 2:
                    clienteService.buscarClientePorId();
                    break;
                case 3:
                    clienteService.buscarClientePorCpf();
                    break;
                case 4:
                    clienteService.cadastrarCliente();
                    break;
                case 5:
                    clienteService.atualizarCliente();
                    break;
                case 6:
                    clienteService.desativarCliente();
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
