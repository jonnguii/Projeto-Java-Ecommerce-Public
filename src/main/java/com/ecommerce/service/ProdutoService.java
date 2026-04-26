package com.ecommerce.service;

import com.ecommerce.dao.EstoqueDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Estoque;
import com.ecommerce.model.Produto;
import com.ecommerce.util.ValidatorUtil;

import java.util.List;
import java.util.Scanner;

/**
 * Serviço responsável pelas regras de negócio de Produto.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */
public class ProdutoService {

    Scanner scanner = new Scanner(System.in);
    ProdutoDAO produtoDAO = new ProdutoDAO();
    EstoqueDAO estoqueDAO = new EstoqueDAO();

    /**
     * Lista todos os produtos.
     */
    public void listarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }

            System.out.println("\n=== LISTA DE PRODUTOS ===");
            System.out.printf("%-5s %-20s %-30s %-10s %-10s %-8s%n",
                    "ID", "Nome", "Descrição", "Preço", "SKU", "Status");
            System.out.println("-".repeat(90));

            for (Produto produto : produtos) {
                String descricao = produto.getDescricao();
                if (descricao != null && descricao.length() > 28) {
                    descricao = descricao.substring(0, 25) + "...";
                }

                System.out.printf("%-5d %-20s %-30s %-10.2f %-10s %-8s%n",
                        produto.getId(),
                        produto.getNome(),
                        descricao,
                        produto.getPreco(),
                        produto.getSku(),
                        produto.isAtivo() ? "Ativo" : "Inativo");
            }

        } catch (DatabaseException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
    }

    /**
     * Busca produto por ID.
     */
    public void buscarProdutoPorId() {
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto != null) {
                System.out.println("\nProduto encontrado:");
                System.out.println(produto);
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
    }

    /**
     * Busca produto por SKU.
     */
    public void buscarProdutoPorSku() {
        System.out.print("Digite o SKU do produto: ");
        String sku = scanner.nextLine();

        try {
            Produto produto = produtoDAO.buscarPorSku(sku);

            if (produto != null) {
                System.out.println("\nProduto encontrado:");
                System.out.println(produto);
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
    }

    /**
     * Busca produto por nome.
     */
    public void buscarProdutoPorNome() {
        System.out.print("Digite o nome ou parte do nome do produto: ");
        String nome = scanner.nextLine();

        try {
            List<Produto> produtos = produtoDAO.buscarPorNome(nome);

            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado com este nome.");
                return;
            }

            System.out.println("\n=== PRODUTOS ENCONTRADOS ===");
            for (Produto produto : produtos) {
                System.out.printf("ID: %d - %s - R$ %.2f - %s%n",
                        produto.getId(),
                        produto.getNome(),
                        produto.getPreco(),
                        produto.getSku());
            }

        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        }
    }

    /**
     * Cadastra um novo produto.
     */
    public void cadastrarProduto() {
        try {
            System.out.println("\n=== CADASTRAR NOVO PRODUTO ===");

            String nome = ValidatorUtil.lerStringObrigatoria(scanner, "Nome: ");
            String descricao = ValidatorUtil.lerStringObrigatoria(scanner, "Descrição: ");
            double preco = ValidatorUtil.lerDoublePositivo(scanner, "Preço: ");
            String sku = ValidatorUtil.lerStringObrigatoria(scanner, "SKU: ");

            Produto produto = new Produto(nome, descricao, preco, sku);

            // Verifica SKU duplicado
            if (produtoDAO.skuJaExiste(sku, null)) {
                System.out.println("SKU já cadastrado!");
                return;
            }

            Produto produtoInserido = produtoDAO.inserir(produto);
            System.out.println("Produto cadastrado com sucesso! ID: " + produtoInserido.getId());

            // Perguntar localização do estoque
            System.out.print("Localização do estoque (ex: Galeria A, Prateleira 1, etc.): ");
            String localizacao = scanner.nextLine();

            System.out.print("Estoque mínimo (padrão 10): ");
            String minStr = scanner.nextLine();
            int min = minStr.isEmpty() ? 10 : Integer.parseInt(minStr);

            System.out.print("Estoque máximo (padrão 1000): ");
            String maxStr = scanner.nextLine();
            int max = maxStr.isEmpty() ? 1000 : Integer.parseInt(maxStr);

            // Cria registro de estoque automaticamente com localização
            Estoque estoque = new Estoque(produtoInserido, 0, min, max, localizacao.isEmpty() ? "Principal" : localizacao);
            estoqueDAO.inserir(estoque);
            System.out
                    .println("Registro de estoque criado automaticamente com localização: " + estoque.getLocalizacao());

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    /**
     * Atualiza um produto existente.
     */
    public void atualizarProduto() {
        System.out.print("Digite o ID do produto a atualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            System.out.println("\nDados atuais:");
            System.out.println(produto);

            System.out.println("\nDigite os novos dados (deixe em branco para manter atual):");

            System.out.print("Nome [" + produto.getNome() + "]: ");
            String nome = scanner.nextLine();
            if (!nome.trim().isEmpty())
                produto.setNome(nome);

            System.out.print("Descrição [" + produto.getDescricao() + "]: ");
            String descricao = scanner.nextLine();
            if (!descricao.trim().isEmpty())
                produto.setDescricao(descricao);

            System.out.print("Preço [" + produto.getPreco() + "]: ");
            String precoStr = scanner.nextLine();
            if (!precoStr.trim().isEmpty()) {
                double preco = Double.parseDouble(precoStr);
                produto.setPreco(preco);
            }

            System.out.print("SKU [" + produto.getSku() + "]: ");
            String sku = scanner.nextLine();
            if (!sku.trim().isEmpty()) {
                if (!sku.equals(produto.getSku()) && produtoDAO.skuJaExiste(sku, id)) {
                    System.out.println("SKU já cadastrado!");
                    return;
                }
                produto.setSku(sku);
            }

            if (produtoDAO.atualizar(produto)) {
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar produto.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    /**
     * Desativa um produto.
     */
    public void desativarProduto() {
        System.out.print("Digite o ID do produto a desativar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);

            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }

            System.out.println("Produto: " + produto.getNome());
            System.out.print("Confirmar desativação? (S/N): ");
            String confirmacao = scanner.nextLine();

            if (confirmacao.equalsIgnoreCase("S")) {
                if (produtoDAO.desativar(id)) {
                    System.out.println("Produto desativado com sucesso!");
                } else {
                    System.out.println("Erro ao desativar produto.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }

        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao desativar produto: " + e.getMessage());
        }
    }
}
