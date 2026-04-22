package com.ecommerce.service;

import com.ecommerce.dao.ClienteDAO;
import com.ecommerce.dao.EstoqueDAO;
import com.ecommerce.dao.PedidoDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Cliente;
import com.ecommerce.model.Estoque;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Produto;

import java.util.List;
import java.util.Scanner;

/**
 * Serviço responsável pelas regras de negócio de Pedido.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */
public class PedidoService {

    Scanner scanner = new Scanner(System.in);
    ClienteDAO clienteDAO = new ClienteDAO();
    ProdutoDAO produtoDAO = new ProdutoDAO();
    EstoqueDAO estoqueDAO = new EstoqueDAO();
    PedidoDAO pedidoDAO = new PedidoDAO();

    /**
     * Cria um novo pedido com verificação de estoque.
     */
    public void criarPedido() {
        try {
            System.out.println("\n=== CRIAR NOVO PEDIDO ===");

            // Selecionar cliente
            System.out.print("Digite o ID do cliente: ");
            int clienteId = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(clienteId);

            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }

            if (!cliente.isAtivo()) {
                System.out.println("Cliente inativo! Não é possível criar pedido.");
                return;
            }

            Pedido pedido = new Pedido(cliente);

            // Adicionar itens ao pedido
            while (true) {
                System.out.println("\nAdicionar item ao pedido (ou 0 para finalizar):");
                System.out.print("ID do produto: ");
                String produtoInput = scanner.nextLine();

                if (produtoInput.equals("0")) {
                    break;
                }

                try {
                    int produtoId = Integer.parseInt(produtoInput);
                    Produto produto = produtoDAO.buscarPorId(produtoId);

                    if (produto == null) {
                        System.out.println("Produto não encontrado.");
                        continue;
                    }

                    if (!produto.isAtivo()) {
                        System.out.println("Produto inativo!");
                        continue;
                    }

                    System.out.print("Quantidade: ");
                    int quantidade = Integer.parseInt(scanner.nextLine());

                    if (quantidade <= 0) {
                        System.out.println("Quantidade deve ser positiva!");
                        continue;
                    }

                    // Verificar estoque
                    Estoque estoque = estoqueDAO.buscarPorProduto(produto);
                    if (estoque == null || !estoque.temEstoqueSuficiente(quantidade)) {
                        int disponivel = estoque != null ? estoque.getQuantidadeAtual() : 0;
                        System.out.printf("Estoque insuficiente! Disponível: %d, Solicitado: %d%n",
                                        disponivel, quantidade);
                        continue;
                    }

                    pedido.adicionarItem(produto, quantidade);
                    System.out.println("Item adicionado: " + produto.getNome() + " x" + quantidade);

                } catch (NumberFormatException e) {
                    System.out.println("Valores inválidos!");
                }
            }

            if (pedido.getItens().isEmpty()) {
                System.out.println("Pedido sem itens. Operação cancelada.");
                return;
            }

            // Exibir resumo do pedido
            System.out.println("\n=== RESUMO DO PEDIDO ===");
            System.out.println("Cliente: " + cliente.getNome());
            System.out.println("Itens:");

            for (var item : pedido.getItens()) {
                System.out.printf("- %s x%d = R$ %.2f%n",
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getSubtotal());
            }

            System.out.printf("Subtotal: R$ %.2f%n", pedido.getSubtotal());
            System.out.printf("Desconto: R$ %.2f (%.1f%%)%n",
                            pedido.getDesconto(), pedido.getPercentualDesconto());
            System.out.printf("Total: R$ %.2f%n", pedido.getValorTotal());

            System.out.print("\nConfirmar pedido? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("S")) {
                // Confirmar pedido e deduzir do estoque
                pedido.confirmarPedido();

                // Deduzir estoque
                for (var item : pedido.getItens()) {
                    estoqueDAO.removerEstoque(item.getProduto().getId(), item.getQuantidade());

                    // Verificar alerta de estoque baixo
                    Estoque estoque = estoqueDAO.buscarPorProduto(item.getProduto());
                    if (estoque != null && estoque.estaComEstoqueBaixo()) {
                        System.out.printf("ALERTA: Estoque baixo para %s! Restam: %d unidades%n",
                                        item.getProduto().getNome(), estoque.getQuantidadeAtual());
                    }
                }

                // Salvar pedido no banco de dados
                try {
                    Pedido pedidoSalvo = pedidoDAO.inserir(pedido);
                    System.out.println("Pedido confirmado com sucesso! ID: " + pedidoSalvo.getId());
                    System.out.println("Estoque atualizado automaticamente.");
                } catch (DatabaseException e) {
                    System.err.println("Erro ao salvar pedido: " + e.getMessage());
                    // Reverter estoque em caso de erro
                    for (var item : pedido.getItens()) {
                        try {
                            estoqueDAO.adicionarEstoque(item.getProduto().getId(), item.getQuantidade());
                        } catch (Exception ex) {
                            System.err.println("Erro ao reverter estoque: " + ex.getMessage());
                        }
                    }
                    return;
                }

            } else {
                System.out.println("Pedido cancelado.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (Exception e) {
            System.err.println("Erro ao criar pedido: " + e.getMessage());
        }
    }

    /**
     * Lista todos os pedidos.
     */
    public void listarPedidos() {
        try {
            System.out.println("\n=== LISTA DE PEDIDOS ===");

            List<Pedido> pedidos = pedidoDAO.listarTodos();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
                return;
            }

            // Cabeçalho da tabela
            System.out.printf("%-5s %-20s %-15s %-12s %-15s %-10s %-15s%n",
                            "ID", "Cliente", "Data Pedido", "Valor Total", "Status", "Itens", "Data Entrega");
            System.out.println("-".repeat(100));

            // Listar pedidos
            for (Pedido pedido : pedidos) {
                String nomeCliente = pedido.getCliente() != null ? pedido.getCliente().getNome() : "N/A";
                String dataPedido = pedido.getDataPedido().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String dataEntrega = pedido.getDataEntrega() != null ?
                                    pedido.getDataEntrega().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";

                // Formatar valor total
                String valorTotal = String.format("R$ %,.2f", pedido.getValorTotal());

                // Cor do status
                String statusComCor = getStatusComCor(pedido.getStatus());

                System.out.printf("%-5d %-20s %-15s %-12s %-15s %-10d %-15s%n",
                                pedido.getId(),
                                nomeCliente,
                                dataPedido,
                                valorTotal,
                                statusComCor,
                                pedido.getQuantidadeTotalItens(),
                                dataEntrega);
            }

            // Resumo estatístico
            System.out.println("-".repeat(100));
            System.out.println("\n=== RESUMO ===");
            System.out.println("Total de pedidos: " + pedidos.size());

            // Contar por status
            java.util.Map<StatusPedido, Long> contagemStatus = pedidos.stream()
                .collect(java.util.stream.Collectors.groupingBy(Pedido::getStatus, java.util.stream.Collectors.counting()));

            for (StatusPedido status : StatusPedido.values()) {
                Long count = contagemStatus.get(status);
                if (count != null && count > 0) {
                    System.out.println(status + ": " + count + " pedido(s)");
                }
            }

            // Valor total
            double valorTotalGeral = pedidos.stream()
                .mapToDouble(Pedido::getValorTotal)
                .sum();
            System.out.println("Valor total geral: " + String.format("R$ %,.2f", valorTotalGeral));

        } catch (DatabaseException e) {
            System.err.println("Erro ao listar pedidos: " + e.getMessage());
        }
    }

    /**
     * Retorna o status formatado com cor para melhor visualização.
     *
     * @param status Status do pedido
     * @return Status formatado com código de cor
     */
    public String getStatusComCor(StatusPedido status) {
        switch (status) {
            case PENDENTE:
                return "\u001B[33m" + status + "\u001B[0m"; // Amarelo
            case CONFIRMADO:
                return "\u001B[32m" + status + "\u001B[0m"; // Verde
            case EM_SEPARACAO:
                return "\u001B[34m" + status + "\u001B[0m"; // Azul
            case ENVIADO:
                return "\u001B[36m" + status + "\u001B[0m"; // Ciano
            case ENTREGUE:
                return "\u001B[32m" + status + "\u001B[0m"; // Verde forte
            case CANCELADO:
                return "\u001B[31m" + status + "\u001B[0m"; // Vermelho
            default:
                return status.toString();
        }
    }

    /**
     * Busca pedido por ID.
     */
    public void buscarPedidoPorId() {
        System.out.println("Funcionalidade em desenvolvimento...");
    }

    /**
     * Atualiza status do pedido.
     */
    public void atualizarStatusPedido() {
        try {
            System.out.println("\n=== ATUALIZAR STATUS DO PEDIDO ===");

            // Listar pedidos para escolha
            List<Pedido> pedidos = pedidoDAO.listarTodos();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
                return;
            }

            System.out.println("\nPedidos disponíveis:");
            System.out.printf("%-5s %-15s %-20s %-15s %-12s %-15s%n",
                            "ID", "Cliente", "Data", "Valor Total", "Status", "Itens");
            System.out.println("-".repeat(85));

            for (Pedido pedido : pedidos) {
                String nomeCliente = pedido.getCliente() != null ? pedido.getCliente().getNome() : "N/A";
                String dataFormatada = pedido.getDataPedido().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                System.out.printf("%-5d %-15s %-20s %-12.2f %-15s %-10d%n",
                                pedido.getId(),
                                nomeCliente,
                                dataFormatada,
                                pedido.getValorTotal(),
                                pedido.getStatus(),
                                pedido.getQuantidadeTotalItens());
            }

            // Selecionar pedido
            System.out.print("\nDigite o ID do pedido: ");
            int pedidoId = Integer.parseInt(scanner.nextLine());

            Pedido pedido = pedidoDAO.buscarPorId(pedidoId);
            if (pedido == null) {
                System.out.println("Pedido não encontrado.");
                return;
            }

            // Exibir informações atuais
            System.out.println("\n=== PEDIDO SELECIONADO ===");
            System.out.println("ID: " + pedido.getId());
            System.out.println("Cliente: " + (pedido.getCliente() != null ? pedido.getCliente().getNome() : "N/A"));
            System.out.println("Status Atual: " + pedido.getStatus());
            System.out.println("Valor Total: R$ " + String.format("%.2f", pedido.getValorTotal()));
            System.out.println("Data: " + pedido.getDataPedido().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));

            // Mostrar opções de status
            System.out.println("\n=== OPÇÕES DE STATUS ===");
            StatusPedido[] statusOptions = StatusPedido.values();
            for (int i = 0; i < statusOptions.length; i++) {
                System.out.println((i + 1) + ". " + statusOptions[i]);
            }

            System.out.print("Escolha o novo status: ");
            int statusOpcao = Integer.parseInt(scanner.nextLine());

            if (statusOpcao < 1 || statusOpcao > statusOptions.length) {
                System.out.println("Opção inválida!");
                return;
            }

            StatusPedido novoStatus = statusOptions[statusOpcao - 1];

            // Validar transição de status
            if (!validarTransicaoStatus(pedido.getStatus(), novoStatus)) {
                System.out.println("Transição de status inválida!");
                System.out.println("Status atual: " + pedido.getStatus());
                System.out.println("Status desejado: " + novoStatus);
                System.out.println("Use 'avancarStatus()' para transições válidas.");
                return;
            }

            // Confirmar alteração
            System.out.print("\nConfirmar alteração de status para " + novoStatus + "? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("Operação cancelada.");
                return;
            }

            // Atualizar no banco
            if (pedidoDAO.atualizarStatus(pedidoId, novoStatus)) {
                System.out.println("Status do pedido atualizado com sucesso!");
                System.out.println("Novo status: " + novoStatus);

                // Se for entregue, atualizar data de entrega
                if (novoStatus == StatusPedido.ENTREGUE) {
                    System.out.println("Pedido marcado como entregue em: " +
                                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }

            } else {
                System.out.println("Erro ao atualizar status do pedido.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
        }
    }

    /**
     * Valida se a transição de status é permitida.
     *
     * @param statusAtual Status atual do pedido
     * @param novoStatus Novo status desejado
     * @return true se a transição é válida
     */
    public boolean validarTransicaoStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Cancelado não pode mudar para nenhum outro status
        if (statusAtual == StatusPedido.CANCELADO) {
            return false;
        }

        // Entregue não pode mudar para nenhum outro status
        if (statusAtual == StatusPedido.ENTREGUE) {
            return false;
        }

        // Permite qualquer transição que não seja para cancelado ou entregue
        // (exceto as regras acima)
        return true;
    }

    /**
     * Cancela um pedido.
     */
    public void cancelarPedido() {
        System.out.println("Funcionalidade em desenvolvimento...");
    }
}
