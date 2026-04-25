package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ecommerce.enums.StatusAtividade;

/**
 * Classe que representa um produto no sistema de e-commerce.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Produto {
    
    private int id;
    private String nome;
    private String descricao;
    private double preco;
    private String sku;
    private StatusAtividade statusAtividade;
    private LocalDateTime dataCadastro;
    private List<Categoria> categorias;
    
    /**
     * Construtor padrão.
     */
    public Produto() {
        this.dataCadastro = LocalDateTime.now();
        this.statusAtividade = StatusAtividade.ATIVO;
        this.categorias = new ArrayList<>();
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome Nome do produto
     * @param descricao Descrição do produto
     * @param preco Preço do produto
     * @param sku SKU (Stock Keeping Unit) do produto
     */
    public Produto(String nome, String descricao, double preco, String sku) {
        this();
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.sku = sku;
    }
    
    /**
     * Adiciona uma categoria ao produto.
     * Implementa relacionamento N:N com Categoria.
     * 
     * @param categoria Categoria a ser adicionada
     */
    public void adicionarCategoria(Categoria categoria) {
        if (categoria != null && !categorias.contains(categoria)) {
            categorias.add(categoria);
            categoria.adicionarProduto(this);
        }
    }
    
    /**
     * Remove uma categoria do produto.
     * 
     * @param categoria Categoria a ser removida
     */
    public void removerCategoria(Categoria categoria) {
        if (categoria != null && categorias.contains(categoria)) {
            categorias.remove(categoria);
            categoria.removerProduto(this);
        }
    }
    
    /**
     * Ativa o produto no sistema.
     */
    public void ativar() {
        this.statusAtividade = StatusAtividade.ATIVO;
    }
    
    /**
     * Desativa o produto no sistema.
     */
    public void desativar() {
        this.statusAtividade = StatusAtividade.INATIVO;
    }
    
    /**
     * Aplica um desconto percentual ao produto.
     * 
     * @param percentualDesconto Percentual de desconto (0-100)
     * @return Novo preço com desconto
     */
    public double aplicarDesconto(double percentualDesconto) {
        if (percentualDesconto < 0 || percentualDesconto > 100) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }
        return preco * (1 - percentualDesconto / 100);
    }
    
    /**
     * Verifica se o produto pertence a uma categoria específica.
     * 
     * @param nomeCategoria Nome da categoria a verificar
     * @return true se pertence à categoria, false caso contrário
     */
    public boolean pertenceACategoria(String nomeCategoria) {
        return categorias.stream()
                .anyMatch(cat -> cat.getNome().equalsIgnoreCase(nomeCategoria));
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o ID do produto.
     * 
     * @return ID do produto
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID do produto.
     * 
     * @param id ID do produto
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome do produto.
     * 
     * @return Nome do produto
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do produto.
     * 
     * @param nome Nome do produto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém a descrição do produto.
     * 
     * @return Descrição do produto
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Define a descrição do produto.
     * 
     * @param descricao Descrição do produto
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Obtém o preço do produto.
     * 
     * @return Preço do produto
     */
    public double getPreco() {
        return preco;
    }
    
    /**
     * Define o preço do produto.
     * 
     * @param preco Preço do produto
     */
    public void setPreco(double preco) {
        if (preco < 0) {
            throw new IllegalArgumentException("Preço não pode ser negativo");
        }
        this.preco = preco;
    }
    
    /**
     * Obtém o SKU do produto.
     * 
     * @return SKU do produto
     */
    public String getSku() {
        return sku;
    }
    
    /**
     * Define o SKU do produto.
     * 
     * @param sku SKU do produto
     */
    public void setSku(String sku) {
        this.sku = sku;
    }
    
    /**
     * Verifica se o produto está ativo.
     * 
     * @return true se ativo, false caso contrário
     */
    public boolean isAtivo() {
        return statusAtividade == StatusAtividade.ATIVO;
    }
    
    /**
     * Define o status de atividade do produto.
     * 
     * @param statusAtividade Status de atividade
     */
    public void setStatusAtividade(StatusAtividade statusAtividade) {
        this.statusAtividade = statusAtividade;
    }
    
    /**
     * Obtém a data de cadastro do produto.
     * 
     * @return Data de cadastro
     */
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    /**
     * Define a data de cadastro do produto.
     * 
     * @param dataCadastro Data de cadastro
     */
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    /**
     * Obtém a lista de categorias do produto.
     * 
     * @return Lista de categorias
     */
    public List<Categoria> getCategorias() {
        return new ArrayList<>(categorias);
    }
    
    /**
     * Define a lista de categorias do produto.
     * 
     * @param categorias Lista de categorias
     */
    public void setCategorias(List<Categoria> categorias) {
        this.categorias = new ArrayList<>(categorias != null ? categorias : new ArrayList<>());
    }
    
    @Override
    public String toString() {
        String separator = "+" + "-".repeat(40) + "+";
        String status = statusAtividade.getDescricao();
        String dataFormatada = dataCadastro != null ? 
            dataCadastro.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
        
        return "\n" + separator +
               "\n|        PRODUTO #" + id + 
               "\n" + separator +
               "\n| Nome:       " + nome +
               "\n| Descrição:  " + (descricao != null ? descricao : "N/A") +
               "\n| Preço:      R$ " + String.format("%.2f", preco) +
               "\n| SKU:        " + sku +
               "\n| Status:     " + status +
               "\n| Cadastro:   " + dataFormatada +
               "\n| Categorias: " + categorias.size() +
               "\n" + separator;
    }
}
