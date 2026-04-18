package com.ecommerce.service;

import java.util.UUID;

/**
 * Implementação de pagamento via PIX.
 * Demonstra polimorfismo através da interface IPagamentoStrategy.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class PagamentoPixStrategy implements IPagamentoStrategy {
    
    private static final String NOME_FORMA = "PIX";
    private static final int TEMPO_PROCESSAMENTO = 10;
    
    /**
     * Processa pagamento via PIX.
     * Gera uma chave PIX aleatória para simulação.
     * 
     * @param valor Valor a ser pago
     * @param dadosPagamento Chave PIX do pagador (simulação)
     * @return true se pagamento processado, false caso contrário
     */
    @Override
    public boolean processarPagamento(double valor, String dadosPagamento) {
        if (!validarDadosPagamento(dadosPagamento)) {
            return false;
        }
        
        System.out.println("Processando pagamento PIX de R$ " + valor);
        System.out.println("Chave PIX origem: " + dadosPagamento);
        
        String chaveDestino = gerarChavePix();
        System.out.println("Chave PIX destino: " + chaveDestino);
        System.out.println("Aguardando confirmação...");
        
        // Simulação: PIX é aprovado instantaneamente
        return valor > 0;
    }
    
    /**
     * Obtém o nome da forma de pagamento.
     * 
     * @return "PIX"
     */
    @Override
    public String getNomeFormaPagamento() {
        return NOME_FORMA;
    }
    
    /**
     * Valida chave PIX (simulação básica).
     * 
     * @param dadosPagamento Chave PIX
     * @return true se válida, false caso contrário
     */
    @Override
    public boolean validarDadosPagamento(String dadosPagamento) {
        if (dadosPagamento == null || dadosPagamento.trim().isEmpty()) {
            return false;
        }
        
        String chave = dadosPagamento.trim();
        
        // Validação básica: pode ser CPF, CNPJ, email ou telefone
        if (chave.matches("\\d{11}")) { // CPF
            return true;
        } else if (chave.matches("\\d{14}")) { // CNPJ
            return true;
        } else if (chave.contains("@")) { // Email
            return chave.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
        } else if (chave.matches("\\d{10,11}")) { // Telefone
            return true;
        } else if (chave.matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")) { // UUID
            return true;
        }
        
        return false;
    }
    
    /**
     * Obtém tempo de processamento.
     * 
     * @return 10 segundos
     */
    @Override
    public int getTempoProcessamentoSegundos() {
        return TEMPO_PROCESSAMENTO;
    }
    
    /**
     * Gera uma chave PIX aleatória (UUID).
     * 
     * @return Chave PIX gerada
     */
    private String gerarChavePix() {
        return UUID.randomUUID().toString();
    }
    
    /**
     * Identifica o tipo de chave PIX.
     * 
     * @param chave Chave PIX a ser analisada
     * @return Tipo da chave
     */
    public String identificarTipoChave(String chave) {
        if (chave == null) return "Desconhecida";
        
        chave = chave.trim();
        
        if (chave.matches("\\d{11}")) {
            return "CPF";
        } else if (chave.matches("\\d{14}")) {
            return "CNPJ";
        } else if (chave.contains("@")) {
            return "E-mail";
        } else if (chave.matches("\\d{10,11}")) {
            return "Telefone";
        } else if (chave.matches("[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}")) {
            return "Chave Aleatória";
        } else {
            return "Desconhecida";
        }
    }
    
    /**
     * Verifica se o valor está dentro dos limites do PIX.
     * 
     * @param valor Valor a ser verificado
     * @return true se dentro dos limites, false caso contrário
     */
    public boolean validarLimitePix(double valor) {
        // Limite diário do PIX (simulação)
        final double LIMITE_DIARIO = 50000.0;
        return valor > 0 && valor <= LIMITE_DIARIO;
    }
}
