package com.ecommerce.service;

/**
 * Interface que define estratégias de pagamento.
 * Implementa o padrão Strategy, demonstrando polimorfismo.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public interface IPagamento {

    /**
     * Processa o pagamento de um pedido.
     * 
     * @param valor          Valor a ser pago
     * @param dadosPagamento Dados necessários para o pagamentoS
     * @return true se pagamento foi aprovado, false caso contrário
     */
    boolean processarPagamento(double valor, String dadosPagamento);

    /**
     * Obtém o nome da forma de pagamento.
     * 
     * @return Nome da forma de pagamento
     */
    String getNomeFormaPagamento();

    /**
     * Valida os dados de pagamento.
     * 
     * @param dadosPagamento Dados a serem validados
     * @return true se dados são válidos, false caso contrário
     */
    boolean validarDadosPagamento(String dadosPagamento);

    /**
     * Obtém o tempo estimado para processamento.
     * 
     * @return Tempo em segundos
     */
    int getTempoProcessamentoSegundos();
}
