package com.ecommerce.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Implementação de pagamento com boleto bancário.
 * Demonstra polimorfismo através da interface IPagamentoStrategy.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class PagamentoBoletoService implements IPagamento {
    
    private static final String NOME_FORMA = "Boleto Bancário";
    private static final int TEMPO_PROCESSAMENTO = 5;
    
    /**
     * Processa pagamento com boleto.
     * Gera um código de barras e data de vencimento.
     * 
     * @param valor Valor a ser pago
     * @param dadosPagamento CPF do pagador (simulação)
     * @return true se boleto gerado com sucesso, false caso contrário
     */
    @Override
    public boolean processarPagamento(double valor, String dadosPagamento) {
        if (!validarDadosPagamento(dadosPagamento)) {
            return false;
        }
        
        System.out.println("Gerando boleto de R$ " + valor);
        System.out.println("CPF pagador: " + dadosPagamento);
        
        String codigoBarras = gerarCodigoBarras();
        LocalDate dataVencimento = LocalDate.now().plusDays(3);
        
        System.out.println("Código de barras: " + codigoBarras);
        System.out.println("Data de vencimento: " + 
            dataVencimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        
        // Boleto sempre é gerado com sucesso nesta simulação
        return true;
    }
    
    /**
     * Obtém o nome da forma de pagamento.
     * 
     * @return "Boleto Bancário"
     */
    @Override
    public String getNomeFormaPagamento() {
        return NOME_FORMA;
    }
    
    /**
     * Valida CPF do pagador.
     * 
     * @param dadosPagamento CPF do pagador
     * @return true se CPF válido, false caso contrário
     */
    @Override
    public boolean validarDadosPagamento(String dadosPagamento) {
        if (dadosPagamento == null || dadosPagamento.trim().isEmpty()) {
            return false;
        }
        
        // Validação básica de CPF: deve ter 11 dígitos
        String cpf = dadosPagamento.replaceAll("[^0-9]", "");
        return cpf.length() == 11;
    }
    
    /**
     * Obtém tempo de processamento.
     * 
     * @return 5 segundos
     */
    @Override
    public int getTempoProcessamentoSegundos() {
        return TEMPO_PROCESSAMENTO;
    }
    
    /**
     * Gera um código de barras simulado.
     * 
     * @return Código de barras de 47 dígitos
     */
    private String gerarCodigoBarras() {
        StringBuilder codigo = new StringBuilder();
        
        // Gera 47 dígitos aleatórios
        for (int i = 0; i < 47; i++) {
            codigo.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        
        // Formata o código com espaços para melhor visualização
        String formatado = codigo.toString();
        return formatado.substring(0, 5) + " " + 
               formatado.substring(5, 10) + " " + 
               formatado.substring(10, 15) + " " + 
               formatado.substring(15, 21) + " " + 
               formatado.substring(21, 27) + " " + 
               formatado.substring(27, 33) + " " + 
               formatado.substring(33, 39) + " " + 
               formatado.substring(39, 44) + " " + 
               formatado.substring(44, 47);
    }
    
    /**
     * Calcula data de vencimento padrão (3 dias úteis).
     * 
     * @return Data de vencimento
     */
    public LocalDate calcularDataVencimento() {
        LocalDate data = LocalDate.now();
        int diasUteis = 0;
        
        while (diasUteis < 3) {
            data = data.plusDays(1);
            // Considera sábado (6) e domingo (7) como não úteis
            if (data.getDayOfWeek().getValue() <= 5) {
                diasUteis++;
            }
        }
        
        return data;
    }
}
