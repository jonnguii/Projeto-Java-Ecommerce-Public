package com.ecommerce.model;

import java.time.LocalDateTime;

/**
 * Classe abstrata que representa uma pessoa no sistema.
 * Implementa o pilar de herança da POO.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public abstract class Pessoa {
    
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCadastro;
    
    /**
     * Construtor padrão.
     */
    public Pessoa() {
        this.dataCadastro = LocalDateTime.now();
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome Nome da pessoa
     * @param email Email da pessoa
     * @param telefone Telefone da pessoa
     */
    public Pessoa(String nome, String email, String telefone) {
        this();
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }
    
    /**
     * Método abstrato para obter o tipo da pessoa.
     * Implementa polimorfismo.
     * 
     * @return String com o tipo da pessoa
     */
    public abstract String getTipoPessoa();
    
    /**
     * Método abstrato para validar dados da pessoa.
     * 
     * @return true se os dados são válidos, false caso contrário
     */
    public abstract boolean validarDados();
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém o ID da pessoa.
     * 
     * @return ID da pessoa
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID da pessoa.
     * 
     * @param id ID da pessoa
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtém o nome da pessoa.
     * 
     * @return Nome da pessoa
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome da pessoa.
     * 
     * @param nome Nome da pessoa
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * Obtém o email da pessoa.
     * 
     * @return Email da pessoa
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Define o email da pessoa.
     * 
     * @param email Email da pessoa
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Obtém o telefone da pessoa.
     * 
     * @return Telefone da pessoa
     */
    public String getTelefone() {
        return telefone;
    }
    
    /**
     * Define o telefone da pessoa.
     * 
     * @param telefone Telefone da pessoa
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    /**
     * Obtém a data de cadastro da pessoa.
     * 
     * @return Data de cadastro
     */
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    /**
     * Define a data de cadastro da pessoa.
     * 
     * @param dataCadastro Data de cadastro
     */
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    @Override
    public String toString() {
        return "Pessoa: " +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", dataCadastro=" + dataCadastro;
    }
}
