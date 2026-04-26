package com.ecommerce.service;

import com.ecommerce.dao.ClienteDAO;
import com.ecommerce.dao.EstoqueDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.enums.StatusEstoque;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Estoque;
import com.ecommerce.model.Produto;
import com.ecommerce.util.ValidatorUtil;

import java.util.List;
import java.util.Scanner;

/**
 * Serviço responsável pelas regras de negócio de Estoque.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */

public class EstoqueService {

    Scanner scanner = new Scanner(System.in);
    ClienteDAO clienteDAO = new ClienteDAO();
    EstoqueDAO estoqueDAO = new EstoqueDAO();
    ProdutoDAO produtoDAO = new ProdutoDAO();

    /**
     * Lista todo o estoque.
     */
    public void listarEstoque() {
        try {
            List<Estoque> estoques = estoqueDAO.listarTodos();

            if (estoques.isEmpty()) {
                System.out.println("Nenhum registro de estoque encontrado.");
                return;
            }

            System.out.println("\n=== ESTOQUE ATUAL ===");
            System.out.printf("%-5s %-25s %-15s %-15s %-15s %-15s %-10s%n",
                    "ID", "Produto", "Quantidade", "Mínima", "Máxima", "Localização", "Status");
            System.out.println("-".repeat(100));

            for (Estoque estoque : estoques) {
                StatusEstoque status = estoque.getStatusEstoque();

                System.out.printf("%-5d %-25s %-15d %-15d %-15d %-15s %-10s%n",
                        estoque.getId(),
                        estoque.getProduto().getNome(),
                        estoque.getQuantidadeAtual(),
                        estoque.getQuantidadeMinima(),
                        estoque.getQuantidadeMaxima(),
                        estoque.getLocalizacao(),
                        status.getDescricao());
            }

        } catch (DatabaseException e) {
            System.err.println("Erro ao listar estoque: " + e.getMessage());
        }
    }

    /**
     * Verifica estoque de um produto específico.
     */
    public void verificarEstoqueProduto() {
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            Estoque estoque = estoqueDAO.buscarPorProduto(produto);

            if (estoque == null) {
                System.out.println("Registro de estoque não encontrado para este produto.");
                return;
            }

            System.out.println(estoque);


        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao verificar estoque: " + e.getMessage());
        }
    }

    /**
     * Adiciona produtos ao estoque.
     */
    public void adicionarEstoque() {
        try {
            int id = ValidatorUtil.lerIntPositivo(scanner, "Digite o ID do produto: ");
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            int quantidade = ValidatorUtil.lerIntPositivo(scanner, "Digite a quantidade a adicionar: ");

            if (estoqueDAO.adicionarEstoque(id, quantidade)) {
                System.out.println("Estoque atualizado com sucesso!");

                // Verifica se atingiu o máximo
                Estoque estoque = estoqueDAO.buscarPorProduto(produto);
                if (estoque != null && estoque.getQuantidadeAtual() > estoque.getQuantidadeMaxima()) {
                    System.out.println("ATENÇÃO: Quantidade atual excede o máximo permitido!");
                }
            } else {
                System.out.println("Erro ao atualizar estoque.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao adicionar estoque: " + e.getMessage());
        }
    }

    /**
     * Remove produtos do estoque.
     */
    public void removerEstoque() {
        try {
            int id = ValidatorUtil.lerIntPositivo(scanner, "Digite o ID do produto: ");
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            Estoque estoque = estoqueDAO.buscarPorProduto(produto);
            if (estoque == null) {
                System.out.println("Registro de estoque não encontrado.");
                return;
            }

            System.out.println("Quantidade atual em estoque: " + estoque.getQuantidadeAtual());
            int quantidade = ValidatorUtil.lerIntPositivo(scanner, "Digite a quantidade a remover: ");

            if (!estoque.temEstoqueSuficiente(quantidade)) {
                System.out.println("Estoque insuficiente! Disponível: " + estoque.getQuantidadeAtual());
                return;
            }

            if (estoqueDAO.removerEstoque(id, quantidade)) {
                System.out.println("Estoque atualizado com sucesso!");

                // Verifica se ficou com estoque baixo
                Estoque estoqueAtualizado = estoqueDAO.buscarPorProduto(produto);
                if (estoqueAtualizado != null && estoqueAtualizado.estaComEstoqueBaixo()) {
                    System.out.println("ALERTA: Estoque baixo após a remoção!");
                }
            } else {
                System.out.println("Erro ao atualizar estoque.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao remover estoque: " + e.getMessage());
        }
    }

    /**
     * Lista produtos com estoque baixo.
     */
    public void listarEstoqueBaixo() {
        try {
            List<Estoque> estoques = estoqueDAO.listarEstoqueBaixo();

            if (estoques.isEmpty()) {
                System.out.println("Nenhum produto com estoque baixo.");
                return;
            }

            System.out.println("\n=== PRODUTOS COM ESTOQUE BAIXO ===");
            System.out.printf("%-5s %-25s %-15s %-15s %-15s%n",
                    "ID", "Produto", "Quantidade", "Mínima", "Localização");
            System.out.println("-".repeat(75));

            for (Estoque estoque : estoques) {
                System.out.printf("%-5d %-25s %-15d %-15d %-15s%n",
                        estoque.getId(),
                        estoque.getProduto().getNome(),
                        estoque.getQuantidadeAtual(),
                        estoque.getQuantidadeMinima(),
                        estoque.getLocalizacao());
            }

        } catch (DatabaseException e) {
            System.err.println("Erro ao listar estoque baixo: " + e.getMessage());
        }
    }

    /**
     * Lista produtos sem estoque.
     */
    public void listarSemEstoque() {
        try {
            List<Estoque> estoques = estoqueDAO.listarSemEstoque();

            if (estoques.isEmpty()) {
                System.out.println("Todos os produtos possuem estoque.");
                return;
            }

            System.out.println("\n=== PRODUTOS SEM ESTOQUE ===");
            System.out.printf("%-5s %-25s %-15s%n",
                    "ID", "Produto", "Localização");
            System.out.println("-".repeat(50));

            for (Estoque estoque : estoques) {
                System.out.printf("%-5d %-25s %-15s%n",
                        estoque.getId(),
                        estoque.getProduto().getNome(),
                        estoque.getLocalizacao());
            }

        } catch (DatabaseException e) {
            System.err.println("Erro ao listar produtos sem estoque: " + e.getMessage());
        }
    }
}
