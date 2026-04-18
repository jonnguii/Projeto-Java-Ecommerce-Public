package com.ecommerce.model;

/**
 * Classe que representa um cliente no sistema.
 * Herda de Pessoa, implementando o pilar de herança.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Cliente extends Pessoa {
    
    private String cpf;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private boolean ativo;
    
    /**
     * Construtor padrão.
     */
    public Cliente() {
        super();
        this.ativo = true;
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome Nome do cliente
     * @param email Email do cliente
     * @param telefone Telefone do cliente
     * @param cpf CPF do cliente
     * @param endereco Endereço do cliente
     */
    public Cliente(String nome, String email, String telefone, String cpf, String endereco) {
        super(nome, email, telefone);
        this.cpf = cpf;
        this.endereco = endereco;
        this.ativo = true;
    }
    
    /**
     * Implementação do método abstrato da classe Pessoa.
     * Retorna o tipo específico para Cliente.
     * 
     * @return String "CLIENTE"
     */
    @Override
    public String getTipoPessoa() {
        return "CLIENTE";
    }
    
    /**
     * Implementação do método abstrato para validação.
     * Valida dados específicos do cliente.
     * 
     * @return true se os dados são válidos, false caso contrário
     */
    @Override
    public boolean validarDados() {
        return getNome() != null && !getNome().trim().isEmpty() &&
               getEmail() != null && !getEmail().trim().isEmpty() &&
               cpf != null && !cpf.trim().isEmpty() &&
               cpf.matches("\\d{11}"); // Validação básica de CPF
    }
    
    /**
     * Ativa o cliente no sistema.
     */
    public void ativar() {
        this.ativo = true;
    }
    
    /**
     * Desativa o cliente no sistema.
     */
    public void desativar() {
        this.ativo = false;
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o CPF do cliente.
     * 
     * @return CPF do cliente
     */
    public String getCpf() {
        return cpf;
    }
    
    /**
     * Define o CPF do cliente.
     * 
     * @param cpf CPF do cliente
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    /**
     * Obtém o endereço do cliente.
     * 
     * @return Endereço do cliente
     */
    public String getEndereco() {
        return endereco;
    }
    
    /**
     * Define o endereço do cliente.
     * 
     * @param endereco Endereço do cliente
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    /**
     * Obtém a cidade do cliente.
     * 
     * @return Cidade do cliente
     */
    public String getCidade() {
        return cidade;
    }
    
    /**
     * Define a cidade do cliente.
     * 
     * @param cidade Cidade do cliente
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    /**
     * Obtém o estado do cliente.
     * 
     * @return Estado do cliente
     */
    public String getEstado() {
        return estado;
    }
    
    /**
     * Define o estado do cliente.
     * 
     * @param estado Estado do cliente
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * Obtém o CEP do cliente.
     * 
     * @return CEP do cliente
     */
    public String getCep() {
        return cep;
    }
    
    /**
     * Define o CEP do cliente.
     * 
     * @param cep CEP do cliente
     */
    public void setCep(String cep) {
        this.cep = cep;
    }
    
    /**
     * Verifica se o cliente está ativo.
     * 
     * @return true se ativo, false caso contrário
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    /**
     * Define o status de ativação do cliente.
     * 
     * @param ativo Status de ativação
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", cpf='" + cpf + '\'' +
                ", endereco='" + endereco + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", cep='" + cep + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
