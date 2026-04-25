package com.ecommerce.service;

import com.ecommerce.dao.EstoqueDAO;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Estoque;

import java.util.List;
import java.util.Scanner;

/**
 * Serviço responsável pelas regras de negócio de Relatórios.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */
public class RelatorioService {

    Scanner scanner = new Scanner(System.in);
    EstoqueDAO estoqueDAO = new EstoqueDAO();
    com.ecommerce.dao.PedidoDAO pedidoDAO = new com.ecommerce.dao.PedidoDAO();

    /**
     * Gera relatório completo de estoque com JOINs.
     */
    public void relatorioEstoqueCompleto() {
        try {
            List<Estoque> estoques = estoqueDAO.listarTodos();

            if (estoques.isEmpty()) {
                System.out.println("Nenhum registro de estoque encontrado.");
                return;
            }

            System.out.println("\n=== RELATÓRIO COMPLETO DE ESTOQUE ===");
            System.out.println("Data/Hora: " + java.time.LocalDateTime.now());
            System.out.println("-".repeat(120));

            double valorTotalEstoque = 0;
            int totalProdutos = 0;
            int produtosSemEstoque = 0;
            int produtosEstoqueBaixo = 0;

            for (Estoque estoque : estoques) {
                double valorProdutoEstoque = estoque.getQuantidadeAtual() * estoque.getProduto().getPreco();
                valorTotalEstoque += valorProdutoEstoque;
                totalProdutos++;

                if (estoque.getQuantidadeAtual() == 0) {
                    produtosSemEstoque++;
                } else if (estoque.estaComEstoqueBaixo()) {
                    produtosEstoqueBaixo++;
                }

                String status;
                if (estoque.getQuantidadeAtual() == 0) {
                    status = com.ecommerce.enums.StatusEstoque.ZERADO.getDescricao();
                } else if (estoque.estaComEstoqueBaixo()) {
                    status = com.ecommerce.enums.StatusEstoque.BAIXO.getDescricao();
                } else {
                    status = com.ecommerce.enums.StatusEstoque.NORMAL.getDescricao();
                }

                System.out.printf("%-5d %-25s %-10s %-8d %-8d %-8d %-12s %-10.2f %-12s%n",
                        estoque.getId(),
                        estoque.getProduto().getNome(),
                        estoque.getProduto().getSku(),
                        estoque.getQuantidadeAtual(),
                        estoque.getQuantidadeMinima(),
                        estoque.getQuantidadeMaxima(),
                        estoque.getLocalizacao(),
                        valorProdutoEstoque,
                        status);
            }

            System.out.println("-".repeat(120));
            System.out.println("RESUMO:");
            System.out.printf("Total de produtos: %d%n", totalProdutos);
            System.out.printf("Produtos sem estoque: %d%n", produtosSemEstoque);
            System.out.printf("Produtos com estoque baixo: %d%n", produtosEstoqueBaixo);
            System.out.printf("Valor total em estoque: R$ %.2f%n", valorTotalEstoque);

        } catch (DatabaseException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    public void relatorioProdutosMaisVendidos() {
        try {
            System.out.println("\n=== RELATÓRIO DE PRODUTOS MAIS VENDIDOS ===");
            System.out.print("Quantos produtos deseja visualizar? (Padrão 10): ");
            String limiteStr = scanner.nextLine();
            int limite = limiteStr.isEmpty() ? 10 : Integer.parseInt(limiteStr);

            List<Object[]> relatorio = pedidoDAO.listarProdutosMaisVendidos(limite);

            if (relatorio.isEmpty()) {
                System.out.println("Nenhuma venda registrada até o momento.");
                return;
            }

            System.out.println("-".repeat(90));
            System.out.printf("%-5s %-30s %-15s %-15s %-15s%n", "ID", "Produto", "SKU", "Qtd Vendida",
                    "Valor Arrecadado");
            System.out.println("-".repeat(90));

            for (Object[] linha : relatorio) {
                System.out.printf("%-5d %-30s %-15s %-15d R$ %-12.2f%n",
                        (Integer) linha[0],
                        (String) linha[1],
                        (String) linha[2],
                        (Integer) linha[3],
                        (Double) linha[4]);
            }
            System.out.println("-".repeat(90));

        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um número válido.");
        } catch (DatabaseException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    public void relatorioProdutosEstoqueBaixo() {
        try {
            List<Estoque> estoques = estoqueDAO.listarEstoqueBaixo();

            if (estoques.isEmpty()) {
                System.out.println("Nenhum produto com estoque baixo encontrado.");
                return;
            }

            System.out.println("\n=== RELATÓRIO DE PRODUTOS COM ESTOQUE BAIXO ===");
            System.out.println("Data/Hora: " + java.time.LocalDateTime.now());
            System.out.println("-".repeat(90));
            System.out.printf("%-5s %-25s %-15s %-10s %-10s %-10s%n", "ID", "Produto", "Localização", "Atual", "Mínimo",
                    "Status");
            System.out.println("-".repeat(90));

            for (Estoque estoque : estoques) {
                System.out.printf("%-5d %-25s %-15s %-10d %-10d %-10s%n",
                        estoque.getProduto().getId(),
                        estoque.getProduto().getNome(),
                        estoque.getLocalizacao(),
                        estoque.getQuantidadeAtual(),
                        estoque.getQuantidadeMinima(),
                        estoque.getStatusEstoque().getDescricao());
            }
            System.out.println("-".repeat(90));

        } catch (DatabaseException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}
