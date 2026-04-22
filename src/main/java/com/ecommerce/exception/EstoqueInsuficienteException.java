package com.ecommerce.exception;

/**
 * Exceção lançada quando não há estoque suficiente para uma operação.
 * Implementa a regra de negócio complexa de controle de estoque.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class EstoqueInsuficienteException extends EcommerceException {
    
    private int quantidadeDisponivel;
    private int quantidadeSolicitada;
    private String nomeProduto;
    
    /**
     * Construtor padrão.
     */
    public EstoqueInsuficienteException() {
        super();
    }
    
    /**
     * Construtor com mensagem de erro.
     * 
     * @param message Mensagem de erro
     */
    public EstoqueInsuficienteException(String message) {
        super(message);
    }
    
    /**
     * Construtor completo com detalhes do estoque.
     */
    public EstoqueInsuficienteException(String nomeProduto, int quantidadeSolicitada, int quantidadeDisponivel) {
        super(String.format("Estoque insuficiente para o produto '%s'. Solicitado: %d, Disponível: %d", 
                           nomeProduto, quantidadeSolicitada, quantidadeDisponivel));
        this.nomeProduto = nomeProduto;
        this.quantidadeSolicitada = quantidadeSolicitada;
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
    
    /**
     * Construtor com mensagem e causa.
     * 
     * @param message Mensagem de erro
     * @param cause Causa da exceção
     */
    public EstoqueInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Obtém a quantidade disponível em estoque.
     * 
     * @return Quantidade disponível
     */
    public int getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }
    
    /**
     * Define a quantidade disponível.
     * 
     * @param quantidadeDisponivel Quantidade disponível
     */
    public void setQuantidadeDisponivel(int quantidadeDisponivel) {
        this.quantidadeDisponivel = quantidadeDisponivel;
    }
    
    /**
     * Obtém a quantidade solicitada.
     * 
     * @return Quantidade solicitada
     */
    public int getQuantidadeSolicitada() {
        return quantidadeSolicitada;
    }
    
    /**
     * Define a quantidade solicitada.
     * 
     * @param quantidadeSolicitada Quantidade solicitada
     */
    public void setQuantidadeSolicitada(int quantidadeSolicitada) {
        this.quantidadeSolicitada = quantidadeSolicitada;
    }
    
    /**
     * Obtém o nome do produto.
     * 
     * @return Nome do produto
     */
    public String getNomeProduto() {
        return nomeProduto;
    }
    
    /**
     * Define o nome do produto.
     * 
     * @param nomeProduto Nome do produto
     */
    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }
    
    /**
     * Verifica se é possível atender parcialmente a solicitação.
     * 
     * @return true se há estoque parcial, false se não há nada
     */
    public boolean temEstoqueParcial() {
        return quantidadeDisponivel > 0;
    }
    
    /**
     * Gera uma mensagem formatada com sugestão.
     * 
     * @return Mensagem formatada
     */
    public String getMensagemSugestao() {
        if (temEstoqueParcial()) {
            return String.format("Sugestão: Ajuste a quantidade para %d unidades ou aguarde reposição.", 
                               quantidadeDisponivel);
        } else {
            return String.format("Produto '%s' sem estoque disponível. Aguarde reposição.", nomeProduto);
        }
    }
}
