package com.ecommerce.model;

import com.ecommerce.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um pedido no sistema de e-commerce.
 * Implementa a regra de negócio complexa de cálculo de pedidos com descontos progressivos.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Pedido {

    
    private int id;
    private Cliente cliente;
    private LocalDateTime dataPedido;
    private LocalDateTime dataEntrega;
    private StatusPedido status;
    private List<ItemPedido> itens;
    private double subtotal;
    private double desconto;
    private double valorTotal;
    private String observacoes;
    
    /**
     * Construtor padrão.
     */
    public Pedido() {
        this.dataPedido = LocalDateTime.now();
        this.status = StatusPedido.PENDENTE;
        this.itens = new ArrayList<>();
        this.subtotal = 0.0;
        this.desconto = 0.0;
        this.valorTotal = 0.0;
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param cliente Cliente do pedido
     */
    public Pedido(Cliente cliente) {
        this();
        this.cliente = cliente;
    }
    
    /**
     * Adiciona um item ao pedido.
     * 
     * @param produto Produto a ser adicionado
     * @param quantidade Quantidade do produto
     */
    public void adicionarItem(Produto produto, int quantidade) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser positiva");
        }
        
        // Verifica se já existe um item com o mesmo produto
        for (ItemPedido item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                // Atualiza a quantidade do item existente
                item.setQuantidade(item.getQuantidade() + quantidade);
                calcularTotais();
                return;
            }
        }
        
        // Cria novo item
        ItemPedido novoItem = new ItemPedido(this, produto, quantidade, produto.getPreco());
        itens.add(novoItem);
        calcularTotais();
    }
    
    /**
     * Remove um item do pedido.
     * 
     * @param produto Produto a ser removido
     */
    public void removerItem(Produto produto) {
        itens.removeIf(item -> item.getProduto().getId() == produto.getId());
        calcularTotais();
    }
    
    /**
     * Calcula os valores totais do pedido (subtotal, desconto e total).
     * Implementa a regra de descontos progressivos.
     */
    public void calcularTotais() {
        // Calcula subtotal
        this.subtotal = itens.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();
        
        // Aplica descontos progressivos
        this.desconto = calcularDescontoProgressivo(this.subtotal);
        
        // Calcula valor total
        this.valorTotal = this.subtotal - this.desconto;
    }
    
    /**
     * Calcula desconto progressivo baseado no valor do pedido.
     * Regra: 5% para pedidos acima de R$ 500, 10% acima de R$ 1.000
     * 
     * @param valorPedido Valor do pedido
     * @return Valor do desconto
     */
    private double calcularDescontoProgressivo(double valorPedido) {
        if (valorPedido > 1000.0) {
            return valorPedido * 0.10; // 10% de desconto
        } else if (valorPedido > 500.0) {
            return valorPedido * 0.05; // 5% de desconto
        }
        return 0.0; // Sem desconto
    }
    
    /**
     * Confirma o pedido, alterando o status para CONFIRMADO.
     * 
     * @throws IllegalStateException se o pedido não tiver itens
     */
    public void confirmarPedido() {
        if (itens.isEmpty()) {
            throw new IllegalStateException("Não é possível confirmar um pedido sem itens");
        }
        this.status = StatusPedido.CONFIRMADO;
    }
    
    /**
     * Cancela o pedido.
     */
    public void cancelarPedido() {
        this.status = StatusPedido.CANCELADO;
    }
    
    /**
     * Avança o status do pedido para a próxima etapa.
     */
    public void avancarStatus() {
        switch (status) {
            case PENDENTE:
                status = StatusPedido.CONFIRMADO;
                break;
            case CONFIRMADO:
                status = StatusPedido.EM_SEPARACAO;
                break;
            case EM_SEPARACAO:
                status = StatusPedido.ENVIADO;
                break;
            case ENVIADO:
                status = StatusPedido.ENTREGUE;
                this.dataEntrega = LocalDateTime.now();
                break;
            case ENTREGUE:
            case CANCELADO:
                // Não faz nada
                break;
        }
    }
    
    /**
     * Obtém a quantidade total de itens no pedido.
     * 
     * @return Quantidade total de itens
     */
    public int getQuantidadeTotalItens() {
        return itens.stream()
                .mapToInt(ItemPedido::getQuantidade)
                .sum();
    }
    
    /**
     * Verifica se o pedido contém um produto específico.
     * 
     * @param produto Produto a verificar
     * @return true se contém o produto, false caso contrário
     */
    public boolean contemProduto(Produto produto) {
        return itens.stream()
                .anyMatch(item -> item.getProduto().getId() == produto.getId());
    }
    
    /**
     * Obtém o percentual de desconto aplicado.
     * 
     * @return Percentual de desconto
     */
    public double getPercentualDesconto() {
        if (subtotal == 0) return 0;
        return (desconto / subtotal) * 100;
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o ID do pedido.
     * 
     * @return ID do pedido
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID do pedido.
     * 
     * @param id ID do pedido
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o cliente do pedido.
     * 
     * @return Cliente
     */
    public Cliente getCliente() {
        return cliente;
    }
    
    /**
     * Define o cliente do pedido.
     * 
     * @param cliente Cliente
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    /**
     * Obtém a data do pedido.
     * 
     * @return Data do pedido
     */
    public LocalDateTime getDataPedido() {
        return dataPedido;
    }
    
    /**
     * Define a data do pedido.
     * 
     * @param dataPedido Data do pedido
     */
    public void setDataPedido(LocalDateTime dataPedido) {
        this.dataPedido = dataPedido;
    }
    
    /**
     * Obtém a data de entrega.
     * 
     * @return Data de entrega
     */
    public LocalDateTime getDataEntrega() {
        return dataEntrega;
    }
    
    /**
     * Define a data de entrega.
     * 
     * @param dataEntrega Data de entrega
     */
    public void setDataEntrega(LocalDateTime dataEntrega) {
        this.dataEntrega = dataEntrega;
    }
    
    /**
     * Obtém o status do pedido.
     * 
     * @return Status do pedido
     */
    public StatusPedido getStatus() {
        return status;
    }
    
    /**
     * Define o status do pedido.
     * 
     * @param status Status do pedido
     */
    public void setStatus(StatusPedido status) {
        this.status = status;
    }
    
    /**
     * Obtém a lista de itens do pedido.
     * 
     * @return Lista de itens
     */
    public List<ItemPedido> getItens() {
        return new ArrayList<>(itens);
    }
    
    /**
     * Define a lista de itens do pedido.
     * 
     * @param itens Lista de itens
     */
    public void setItens(List<ItemPedido> itens) {
        this.itens = new ArrayList<>(itens != null ? itens : new ArrayList<>());
        calcularTotais();
    }
    
    /**
     * Obtém o subtotal do pedido.
     * 
     * @return Subtotal
     */
    public double getSubtotal() {
        return subtotal;
    }
    
    /**
     * Obtém o desconto do pedido.
     * 
     * @return Desconto
     */
    public double getDesconto() {
        return desconto;
    }
    
    /**
     * Obtém o valor total do pedido.
     * 
     * @return Valor total
     */
    public double getValorTotal() {
        return valorTotal;
    }
    
    /**
     * Obtém as observações do pedido.
     * 
     * @return Observações
     */
    public String getObservacoes() {
        return observacoes;
    }
    
    /**
     * Define as observações do pedido.
     * 
     * @param observações Observações
     */
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    
    @Override
    public String toString() {
        String separator = "+" + "-".repeat(40) + "+";
        String dataFormatada = dataPedido != null ? 
            dataPedido.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "N/A";
        String dataEntregaFormatada = dataEntrega != null ? 
            dataEntrega.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Pendente";
        
        return "\n" + separator +
               "\n|        PEDIDO #" + id + 
               "\n" + separator +
               "\n| Cliente:    " + (cliente != null ? cliente.getNome() : "N/A") +
               "\n| Data:       " + dataFormatada +
               "\n| Status:     " + (status != null ? status.getDescricao() : "N/A") +
               "\n| Itens:      " + itens.size() + " produto(s)" +
               "\n| Subtotal:   R$ " + String.format("%.2f", subtotal) +
               "\n| Desconto:   R$ " + String.format("%.2f", desconto) + " (" + String.format("%.1f%%", getPercentualDesconto()) + ")" +
               "\n| Total:      R$ " + String.format("%.2f", valorTotal) +
               "\n| Entrega:    " + dataEntregaFormatada +
               "\n" + separator;
    }
}
