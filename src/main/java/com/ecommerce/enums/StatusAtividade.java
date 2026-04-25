package com.ecommerce.enums;

/**
 * Enum que representa o status de atividade (registro) de uma entidade no sistema.
 */
public enum StatusAtividade {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String descricao;

    StatusAtividade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
