package com.ecommerce.service;

import com.ecommerce.dao.ClienteDAO;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Cliente;

import java.util.List;
import java.util.Scanner;

/**
 * Serviço responsável pelas regras de negócio de Cliente.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */

public class ClienteService {
    Scanner scanner = new Scanner(System.in);
    ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Lista todos os clientes.
     */
    public void listarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();

            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
                return;
            }

            System.out.println("\n=== LISTA DE CLIENTES ===");
            System.out.printf("%-5s %-20s %-25s %-15s %-10s%n",
                    "ID", "Nome", "Email", "Telefone", "Status");
            System.out.println("-".repeat(80));

            for (Cliente cliente : clientes) {
                System.out.printf("%-5d %-20s %-25s %-15s %-10s%n",
                        cliente.getId(),
                        cliente.getNome(),
                        cliente.getEmail(),
                        cliente.getTelefone(),
                        cliente.isAtivo() ? "Ativo" : "Inativo");
            }

        } catch (DatabaseException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
    }

    /**
     * Busca cliente por ID.
     */
    public void buscarClientePorId() {
        System.out.print("Digite o ID do cliente: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);

            if (cliente != null) {
                System.out.println("\nCliente encontrado:");
                System.out.println(cliente);
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    /**
     * Busca cliente por CPF.
     */
    public void buscarClientePorCpf() {
        System.out.print("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();

        try {
            Cliente cliente = clienteDAO.buscarPorCpf(cpf);

            if (cliente != null) {
                System.out.println("\nCliente encontrado:");
                System.out.println(cliente);
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
    }

    /**
     * Cadastra um novo cliente.
     */
    public void cadastrarCliente() {
        try {
            System.out.println("\n=== CADASTRAR NOVO CLIENTE ===");

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            System.out.print("CPF: ");
            String cpf = scanner.nextLine();

            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();

            System.out.print("Cidade: ");
            String cidade = scanner.nextLine();

            System.out.print("Estado (UF): ");
            String estado = scanner.nextLine();

            System.out.print("CEP: ");
            String cep = scanner.nextLine();

            Cliente cliente = new Cliente(nome, email, telefone, cpf, endereco);
            cliente.setCidade(cidade);
            cliente.setEstado(estado);
            cliente.setCep(cep);

            // Valida dados
            if (!cliente.validarDados()) {
                System.out.println("Dados inválidos! Verifique nome, email e CPF.");
                return;
            }

            // Verifica email duplicado
            if (clienteDAO.emailJaExiste(email, null)) {
                System.out.println("Email já cadastrado!");
                return;
            }

            Cliente clienteInserido = clienteDAO.inserir(cliente);
            System.out.println("Cliente cadastrado com sucesso! ID: " + clienteInserido.getId());

        } catch (DatabaseException e) {
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    /**
     * Atualiza um cliente existente.
     */
    public void atualizarCliente() {
        System.out.print("Digite o ID do cliente a atualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);

            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.println("\nDados atuais:");
            System.out.println(cliente);

            System.out.println("\nDigite os novos dados (deixe em branco para manter atual):");

            System.out.print("Nome [" + cliente.getNome() + "]: ");
            String nome = scanner.nextLine();
            if (!nome.trim().isEmpty())
                cliente.setNome(nome);

            System.out.print("Email [" + cliente.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.trim().isEmpty()) {
                if (!email.equals(cliente.getEmail()) && clienteDAO.emailJaExiste(email, id)) {
                    System.out.println("Email já cadastrado!");
                    return;
                }
                cliente.setEmail(email);
            }

            System.out.print("Telefone [" + cliente.getTelefone() + "]: ");
            String telefone = scanner.nextLine();
            if (!telefone.trim().isEmpty())
                cliente.setTelefone(telefone);

            System.out.print("Endereço [" + cliente.getEndereco() + "]: ");
            String endereco = scanner.nextLine();
            if (!endereco.trim().isEmpty())
                cliente.setEndereco(endereco);

            if (clienteDAO.atualizar(cliente)) {
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar cliente.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    /**
     * Desativa um cliente.
     */
    public void desativarCliente() {
        System.out.print("Digite o ID do cliente a desativar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);

            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            System.out.println("Cliente: " + cliente.getNome());
            System.out.print("Confirmar desativação? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("S")) {
                if (clienteDAO.desativar(id)) {
                    System.out.println("Cliente desativado com sucesso!");
                } else {
                    System.out.println("Erro ao desativar cliente.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao desativar cliente: " + e.getMessage());
        }
    }
}
