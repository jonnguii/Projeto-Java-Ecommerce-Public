package com.ecommerce.enums;

/**
 * Enum que representa os possíveis status de estoque de um produto.
 *
 * @author Sistema E-commerce
 * @version 1.0
 */
public enum StatusEstoque {
    NORMAL("Normal", "Normal"),
    BAIXO("Estoque Baixo", "Estoque Baixo"),
    ZERADO("Sem Estoque", "Sem Estoque"),
    ACIMA_DO_MAXIMO("Acima do Máximo", "Acima do Máximo");

    private final String descricao;
    private final String descricaoColorida;

    StatusEstoque(String descricao, String descricaoColorida) {
        this.descricao = descricao;
        this.descricaoColorida = descricaoColorida;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getDescricaoColorida() {
        return descricaoColorida;
    }
}
