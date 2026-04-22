package com.ecommerce.service;

import com.ecommerce.dao.ClienteDAO;
import com.ecommerce.dao.EstoqueDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Estoque;
import com.ecommerce.model.Produto;

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
                String status;
                if (estoque.getQuantidadeAtual() == 0) {
                    status = "SEM ESTOQUE";
                } else if (estoque.getQuantidadeAtual() < estoque.getQuantidadeMinima()) {
                    status = "ABAIXO";
                } else if (estoque.getQuantidadeAtual() > estoque.getQuantidadeMaxima()) {
                    status = "ACIMA";
                } else {
                    status = "NORMAL";
                }

                System.out.printf("%-5d %-25s %-15d %-15d %-15d %-15s %-10s%n",
                        estoque.getId(),
                        estoque.getProduto().getNome(),
                        estoque.getQuantidadeAtual(),
                        estoque.getQuantidadeMinima(),
                        estoque.getQuantidadeMaxima(),
                        estoque.getLocalizacao(),
                        status);
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

            System.out.println("\n=== ESTOQUE DO PRODUTO ===");
            System.out.println("Produto: " + estoque.getProduto().getNome());
            System.out.println("Quantidade atual: " + estoque.getQuantidadeAtual());
            System.out.println("Quantidade mínima: " + estoque.getQuantidadeMinima());
            System.out.println("Quantidade máxima: " + estoque.getQuantidadeMaxima());
            System.out.println("Localização: " + estoque.getLocalizacao());

            // Determinar status correto
            String status;
            if (estoque.getQuantidadeAtual() == 0) {
                status = "SEM ESTOQUE";
            } else if (estoque.getQuantidadeAtual() < estoque.getQuantidadeMinima()) {
                status = "ABAIXO";
            } else if (estoque.getQuantidadeAtual() > estoque.getQuantidadeMaxima()) {
                status = "ACIMA";
            } else {
                status = "NORMAL";
            }

            System.out.println("Status: " + status);
            System.out.println("Última atualização: " + estoque.getDataUltimaAtualizacao());

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
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            System.out.print("Digite a quantidade a adicionar: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            if (quantidade <= 0) {
                System.out.println("Quantidade deve ser positiva!");
                return;
            }

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
                System.out.println("Registro de estoque não encontrado.");
                return;
            }

            System.out.println("Quantidade atual em estoque: " + estoque.getQuantidadeAtual());
            System.out.print("Digite a quantidade a remover: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            if (quantidade <= 0) {
                System.out.println("Quantidade deve ser positiva!");
                return;
            }

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
