package com.ecommerce.model;

/**
 * Classe que representa um item dentro de um pedido.
 * Implementa o relacionamento N:N entre Pedido e Produto.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class ItemPedido {
    
    private int id;
    private Pedido pedido;
    private Produto produto;
    private int quantidade;
    private double precoUnitario;
    private double subtotal;
    
    /**
     * Construtor padrão.
     */
    public ItemPedido() {
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param pedido Pedido ao qual o item pertence
     * @param produto Produto do item
     * @param quantidade Quantidade do produto
     * @param precoUnitario Preço unitário na hora da compra
     */
    public ItemPedido(Pedido pedido, Produto produto, int quantidade, double precoUnitario) {
        this.pedido = pedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subtotal = quantidade * precoUnitario;
    }
    
    /**
     * Calcula o subtotal do item.
     * 
     * @return Subtotal do item
     */
    public double calcularSubtotal() {
        this.subtotal = quantidade * precoUnitario;
        return subtotal;
    }
    
    /**
     * Atualiza a quantidade do item e recalcula o subtotal.
     * 
     * @param novaQuantidade Nova quantidade
     */
    public void atualizarQuantidade(int novaQuantidade) {
        if (novaQuantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        this.quantidade = novaQuantidade;
        calcularSubtotal();
    }
    
    /**
     * Aplica um desconto no preço unitário do item.
     * 
     * @param percentualDesconto Percentual de desconto (0-100)
     */
    public void aplicarDesconto(double percentualDesconto) {
        if (percentualDesconto < 0 || percentualDesconto > 100) {
            throw new IllegalArgumentException("Percentual de desconto deve estar entre 0 e 100");
        }
        this.precoUnitario = this.precoUnitario * (1 - percentualDesconto / 100);
        calcularSubtotal();
    }
    
    /**
     * Obtém o valor total do item formatado.
     * 
     * @return Valor total formatado como string
     */
    public String getSubtotalFormatado() {
        return String.format("R$ %.2f", subtotal);
    }
    
    /**
     * Obtém o preço unitário formatado.
     * 
     * @return Preço unitário formatado como string
     */
    public String getPrecoUnitarioFormatado() {
        return String.format("R$ %.2f", precoUnitario);
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o ID do item do pedido.
     * 
     * @return ID do item
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID do item do pedido.
     * 
     * @param id ID do item
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o pedido do item.
     * 
     * @return Pedido
     */
    public Pedido getPedido() {
        return pedido;
    }
    
    /**
     * Define o pedido do item.
     * 
     * @param pedido Pedido
     */
    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    
    /**
     * Obtém o produto do item.
     * 
     * @return Produto
     */
    public Produto getProduto() {
        return produto;
    }
    
    /**
     * Define o produto do item.
     * 
     * @param produto Produto
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    /**
     * Obtém a quantidade do produto.
     * 
     * @return Quantidade
     */
    public int getQuantidade() {
        return quantidade;
    }
    
    /**
     * Define a quantidade do produto.
     * 
     * @param quantidade Quantidade
     */
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        calcularSubtotal();
    }
    
    /**
     * Obtém o preço unitário do produto.
     * 
     * @return Preço unitário
     */
    public double getPrecoUnitario() {
        return precoUnitario;
    }
    
    /**
     * Define o preço unitário do produto.
     * 
     * @param precoUnitario Preço unitário
     */
    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
        calcularSubtotal();
    }
    
    /**
     * Obtém o subtotal do item.
     * 
     * @return Subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }
    
    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", produto=" + (produto != null ? produto.getNome() : "N/A") +
                ", quantidade=" + quantidade +
                ", precoUnitario=" + precoUnitario +
                ", subtotal=" + subtotal +
                '}';
    }
}
