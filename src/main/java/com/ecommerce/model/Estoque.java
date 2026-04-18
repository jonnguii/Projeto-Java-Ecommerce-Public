package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Classe que representa o controle de estoque de um produto.
 * Implementa a regra de negócio complexa de controle de estoque.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Estoque {
    
    private int id;
    private Produto produto;
    private int quantidadeAtual;
    private int quantidadeMinima;
    private int quantidadeMaxima;
    private LocalDateTime dataUltimaAtualizacao;
    private String localizacao;
    
    /**
     * Construtor padrão.
     */
    public Estoque() {
        this.dataUltimaAtualizacao = LocalDateTime.now();
        this.quantidadeMinima = 10; // Valor padrão para alerta de estoque baixo
        this.quantidadeMaxima = 1000; // Valor padrão
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param produto Produto do estoque
     * @param quantidadeAtual Quantidade atual em estoque
     */
    public Estoque(Produto produto, int quantidadeAtual) {
        this();
        this.produto = produto;
        this.quantidadeAtual = quantidadeAtual;
    }
    
    /**
     * Construtor completo.
     * 
     * @param produto Produto do estoque
     * @param quantidadeAtual Quantidade atual
     * @param quantidadeMinima Quantidade mínima
     * @param quantidadeMaxima Quantidade máxima
     * @param localizacao Localização no armazém
     */
    public Estoque(Produto produto, int quantidadeAtual, int quantidadeMinima, 
                   int quantidadeMaxima, String localizacao) {
        this(produto, quantidadeAtual);
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.localizacao = localizacao;
    }
    
    /**
     * Adiciona quantidade ao estoque.
     * 
     * @param quantidade Quantidade a ser adicionada
     * @throws IllegalArgumentException se quantidade for negativa
     */
    public void adicionarEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        
        int novaQuantidade = this.quantidadeAtual + quantidade;
        
        if (novaQuantidade > quantidadeMaxima) {
            throw new IllegalArgumentException(
                "Quantidade excede o máximo permitido (" + quantidadeMaxima + ")");
        }
        
        this.quantidadeAtual = novaQuantidade;
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Remove quantidade do estoque.
     * 
     * @param quantidade Quantidade a ser removida
     * @throws IllegalArgumentException se não houver estoque suficiente
     */
    public void removerEstoque(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        
        if (quantidade > this.quantidadeAtual) {
            throw new IllegalArgumentException(
                "Estoque insuficiente. Disponível: " + this.quantidadeAtual + 
                ", Solicitado: " + quantidade);
        }
        
        this.quantidadeAtual -= quantidade;
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Verifica se há estoque suficiente para a quantidade solicitada.
     * 
     * @param quantidade Quantidade desejada
     * @return true se há estoque suficiente, false caso contrário
     */
    public boolean temEstoqueSuficiente(int quantidade) {
        return quantidade > 0 && quantidade <= this.quantidadeAtual;
    }
    
    /**
     * Verifica se o estoque está baixo (abaixo do mínimo).
     * 
     * @return true se estoque está baixo, false caso contrário
     */
    public boolean estaComEstoqueBaixo() {
        return quantidadeAtual <= quantidadeMinima;
    }
    
    /**
     * Verifica se o estoque está zerado.
     * 
     * @return true se estoque está zerado, false caso contrário
     */
    public boolean estaZerado() {
        return quantidadeAtual == 0;
    }
    
    /**
     * Calcula o percentual de ocupação do estoque.
     * 
     * @return Percentual de ocupação (0-100)
     */
    public double getPercentualOcupacao() {
        if (quantidadeMaxima == 0) return 0;
        return (double) quantidadeAtual / quantidadeMaxima * 100;
    }
    
    /**
     * Obtém a quantidade disponível para venda.
     * 
     * @return Quantidade disponível
     */
    public int getQuantidadeDisponivel() {
        return Math.max(0, quantidadeAtual);
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o ID do registro de estoque.
     * 
     * @return ID do estoque
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID do registro de estoque.
     * 
     * @param id ID do estoque
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o produto do estoque.
     * 
     * @return Produto
     */
    public Produto getProduto() {
        return produto;
    }
    
    /**
     * Define o produto do estoque.
     * 
     * @param produto Produto
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    /**
     * Obtém a quantidade atual em estoque.
     * 
     * @return Quantidade atual
     */
    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }
    
    /**
     * Define a quantidade atual em estoque.
     * 
     * @param quantidadeAtual Quantidade atual
     */
    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
        this.dataUltimaAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Obtém a quantidade mínima de estoque.
     * 
     * @return Quantidade mínima
     */
    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }
    
    /**
     * Define a quantidade mínima de estoque.
     * 
     * @param quantidadeMinima Quantidade mínima
     */
    public void setQuantidadeMinima(int quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
    
    /**
     * Obtém a quantidade máxima de estoque.
     * 
     * @return Quantidade máxima
     */
    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }
    
    /**
     * Define a quantidade máxima de estoque.
     * 
     * @param quantidadeMaxima Quantidade máxima
     */
    public void setQuantidadeMaxima(int quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }
    
    /**
     * Obtém a data da última atualização.
     * 
     * @return Data da última atualização
     */
    public LocalDateTime getDataUltimaAtualizacao() {
        return dataUltimaAtualizacao;
    }
    
    /**
     * Define a data da última atualização.
     * 
     * @param dataUltimaAtualizacao Data da última atualização
     */
    public void setDataUltimaAtualizacao(LocalDateTime dataUltimaAtualizacao) {
        this.dataUltimaAtualizacao = dataUltimaAtualizacao;
    }
    
    /**
     * Obtém a localização do produto no armazém.
     * 
     * @return Localização
     */
    public String getLocalizacao() {
        return localizacao;
    }
    
    /**
     * Define a localização do produto no armazém.
     * 
     * @param localizacao Localização
     */
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    
    @Override
    public String toString() {
        return "Estoque{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : "N/A") +
                ", quantidadeAtual=" + quantidadeAtual +
                ", quantidadeMinima=" + quantidadeMinima +
                ", quantidadeMaxima=" + quantidadeMaxima +
                ", localizacao='" + localizacao + '\'' +
                ", dataUltimaAtualizacao=" + dataUltimaAtualizacao +
                ", estoqueBaixo=" + estaComEstoqueBaixo() +
                '}';
    }
}
