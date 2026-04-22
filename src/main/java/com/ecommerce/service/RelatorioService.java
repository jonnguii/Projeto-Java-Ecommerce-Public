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
                    status = "SEM ESTOQUE";
                } else if (estoque.estaComEstoqueBaixo()) {
                    status = "ESTOQUE BAIXO";
                } else {
                    status = "NORMAL";
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
}
