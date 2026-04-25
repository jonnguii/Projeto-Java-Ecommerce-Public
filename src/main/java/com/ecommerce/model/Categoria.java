package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.ecommerce.enums.StatusAtividade;

/**
 * Classe que representa uma categoria de produtos no sistema.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Categoria {
    
    private int id;
    private String nome;
    private String descricao;
    private StatusAtividade statusAtividade;
    private LocalDateTime dataCriacao;
    private List<Produto> produtos;
    
    /**
     * Construtor padrão.
     */
    public Categoria() {
        this.dataCriacao = LocalDateTime.now();
        this.statusAtividade = StatusAtividade.ATIVO;
        this.produtos = new ArrayList<>();
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome Nome da categoria
     * @param descricao Descrição da categoria
     */
    public Categoria(String nome, String descricao) {
        this();
        this.nome = nome;
        this.descricao = descricao;
    }
    
    /**
     * Adiciona um produto à categoria.
     * Implementa relacionamento bidirecional.
     * 
     * @param produto Produto a ser adicionado
     */
    public void adicionarProduto(Produto produto) {
        if (produto != null && !produtos.contains(produto)) {
            produtos.add(produto);
            produto.adicionarCategoria(this);
        }
    }
    
    /**
     * Remove um produto da categoria.
     * 
     * @param produto Produto a ser removido
     */
    public void removerProduto(Produto produto) {
        if (produto != null && produtos.contains(produto)) {
            produtos.remove(produto);
            produto.removerCategoria(this);
        }
    }
    
    /**
     * Ativa a categoria no sistema.
     */
    public void ativar() {
        this.statusAtividade = StatusAtividade.ATIVO;
    }
    
    /**
     * Desativa a categoria no sistema.
     */
    public void desativar() {
        this.statusAtividade = StatusAtividade.INATIVO;
    }
    
    /**
     * Obtém a quantidade de produtos na categoria.
     * 
     * @return Quantidade de produtos
     */
    public int getQuantidadeProdutos() {
        return produtos.size();
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o ID da categoria.
     * 
     * @return ID da categoria
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID da categoria.
     * 
     * @param id ID da categoria
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome da categoria.
     * 
     * @return Nome da categoria
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome da categoria.
     * 
     * @param nome Nome da categoria
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém a descrição da categoria.
     * 
     * @return Descrição da categoria
     */
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Define a descrição da categoria.
     * 
     * @param descricao Descrição da categoria
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    /**
     * Verifica se a categoria está ativa.
     * 
     * @return true se ativa, false caso contrário
     */
    public boolean isAtiva() {
        return statusAtividade == StatusAtividade.ATIVO;
    }
    
    /**
     * Define o status de atividade da categoria.
     * 
     * @param statusAtividade Status de atividade
     */
    public void setStatusAtividade(StatusAtividade statusAtividade) {
        this.statusAtividade = statusAtividade;
    }
    
    /**
     * Obtém a data de criação da categoria.
     * 
     * @return Data de criação
     */
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    /**
     * Define a data de criação da categoria.
     * 
     * @param dataCriacao Data de criação
     */
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    /**
     * Obtém a lista de produtos da categoria.
     * 
     * @return Lista de produtos
     */
    public List<Produto> getProdutos() {
        return new ArrayList<>(produtos);
    }
    
    /**
     * Define a lista de produtos da categoria.
     * 
     * @param produtos Lista de produtos
     */
    public void setProdutos(List<Produto> produtos) {
        this.produtos = new ArrayList<>(produtos != null ? produtos : new ArrayList<>());
    }
    
    @Override
    public String toString() {
        String separator = "+" + "-".repeat(40) + "+";
        String status = statusAtividade.getDescricao();
        String dataFormatada = dataCriacao != null ? 
            dataCriacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
        
        return "\n" + separator +
               "\n|     CATEGORIA #" + id + 
               "\n" + separator +
               "\n| Nome:       " + nome +
               "\n| Descrição:  " + (descricao != null ? descricao : "N/A") +
               "\n| Produtos:   " + produtos.size() +
               "\n| Status:     " + status +
               "\n| Criação:    " + dataFormatada +
               "\n" + separator;
    }
}
