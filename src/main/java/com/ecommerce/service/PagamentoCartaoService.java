package com.ecommerce.service;

/**
 * Implementação de pagamento com cartão de crédito.
 * Demonstra polimorfismo através da interface IPagamentoStrategy.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class PagamentoCartaoService implements IPagamento {

    private static final String NOME_FORMA = "Cartão de Crédito";
    private static final int TEMPO_PROCESSAMENTO = 30;

    /**
     * Processa pagamento com cartão de crédito.
     * 
     * @param valor          Valor a ser pago
     * @param dadosPagamento Número do cartão (simulação)
     * @return true se pagamento aprovado, false caso contrário
     */
    @Override
    public boolean processarPagamento(double valor, String dadosPagamento) {
        if (!validarDadosPagamento(dadosPagamento)) {
            return false;
        }

        // Simulação de processamento
        System.out.println("Processando pagamento de R$ " + valor + " com cartão...");

        // Simulação: aprova se valor for positivo e dados válidos
        return valor > 0 && dadosPagamento != null && dadosPagamento.length() >= 16;
    }

    /**
     * Obtém o nome da forma de pagamento.
     * 
     * @return "Cartão de Crédito"
     */
    @Override
    public String getNomeFormaPagamento() {
        return NOME_FORMA;
    }

    /**
     * Valida número do cartão (simulação básica).
     * 
     * @param dadosPagamento Número do cartão
     * @return true se válido, false caso contrário
     */
    @Override
    public boolean validarDadosPagamento(String dadosPagamento) {
        if (dadosPagamento == null || dadosPagamento.trim().isEmpty()) {
            return false;
        }

        // Validação básica: deve ter entre 16 e 19 dígitos
        String numeroLimpo = dadosPagamento.replaceAll("[^0-9]", "");
        return numeroLimpo.length() >= 16 && numeroLimpo.length() <= 19;
    }

    /**
     * Obtém tempo de processamento.
     * 
     * @return 30 segundos
     */
    @Override
    public int getTempoProcessamentoSegundos() {
        return TEMPO_PROCESSAMENTO;
    }

    /**
     * Obtém a bandeira do cartão (simulação).
     * 
     * @param numeroCartao Número do cartão
     * @return Bandira do cartão
     */
    public String detectarBandeira(String numeroCartao) {
        if (numeroCartao == null)
            return "Desconhecida";

        String numero = numeroCartao.replaceAll("[^0-9]", "");

        if (numero.startsWith("4")) {
            return "Visa";
        } else if (numero.startsWith("5")) {
            return "Mastercard";
        } else if (numero.startsWith("3")) {
            return "American Express";
        } else {
            return "Outra";
        }
    }
}
