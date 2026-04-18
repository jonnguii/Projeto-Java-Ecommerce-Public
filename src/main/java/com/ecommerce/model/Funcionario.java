package com.ecommerce.model;

/**
 * Classe que representa um funcionário no sistema.
 * Herda de Pessoa, demonstrando o pilar de herança.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class Funcionario extends Pessoa {
    
    private String matricula;
    private String cargo;
    private double salario;
    private String departamento;
    private boolean ativo;
    
    /**
     * Construtor padrão.
     */
    public Funcionario() {
        super();
        this.ativo = true;
    }
    
    /**
     * Construtor com parâmetros.
     * 
     * @param nome Nome do funcionário
     * @param email Email do funcionário
     * @param telefone Telefone do funcionário
     * @param matricula Matrícula do funcionário
     * @param cargo Cargo do funcionário
     * @param salario Salário do funcionário
     */
    public Funcionario(String nome, String email, String telefone, 
                      String matricula, String cargo, double salario) {
        super(nome, email, telefone);
        this.matricula = matricula;
        this.cargo = cargo;
        this.salario = salario;
        this.ativo = true;
    }
    
    /**
     * Implementação do método abstrato da classe Pessoa.
     * Retorna o tipo específico para Funcionário.
     * 
     * @return String "FUNCIONARIO"
     */
    @Override
    public String getTipoPessoa() {
        return "FUNCIONARIO";
    }
    
    /**
     * Implementação do método abstrato para validação.
     * Valida dados específicos do funcionário.
     * 
     * @return true se os dados são válidos, false caso contrário
     */
    @Override
    public boolean validarDados() {
        return getNome() != null && !getNome().trim().isEmpty() &&
               getEmail() != null && !getEmail().trim().isEmpty() &&
               matricula != null && !matricula.trim().isEmpty() &&
               cargo != null && !cargo.trim().isEmpty() &&
               salario > 0;
    }
    
    /**
     * Aplica um aumento percentual no salário.
     * 
     * @param percentual Percentual de aumento
     */
    public void aplicarAumento(double percentual) {
        if (percentual <= 0) {
            throw new IllegalArgumentException("Percentual de aumento deve ser positivo");
        }
        this.salario = this.salario * (1 + percentual / 100);
    }
    
    /**
     * Ativa o funcionário no sistema.
     */
    public void ativar() {
        this.ativo = true;
    }
    
    /**
     * Desativa o funcionário no sistema.
     */
    public void desativar() {
        this.ativo = false;
    }
    
    /**
     * Promove o funcionário para um novo cargo.
     * 
     * @param novoCargo Novo cargo
     * @param novoSalario Novo salário
     */
    public void promover(String novoCargo, double novoSalario) {
        this.cargo = novoCargo;
        this.salario = novoSalario;
    }
    
    // Getters e Setters com encapsulamento completo
    
    /**
     * Obtém a matrícula do funcionário.
     * 
     * @return Matrícula
     */
    public String getMatricula() {
        return matricula;
    }
    
    /**
     * Define a matrícula do funcionário.
     * 
     * @param matricula Matrícula
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    /**
     * Obtém o cargo do funcionário.
     * 
     * @return Cargo
     */
    public String getCargo() {
        return cargo;
    }
    
    /**
     * Define o cargo do funcionário.
     * 
     * @param cargo Cargo
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    /**
     * Obtém o salário do funcionário.
     * 
     * @return Salário
     */
    public double getSalario() {
        return salario;
    }
    
    /**
     * Define o salário do funcionário.
     * 
     * @param salario Salário
     */
    public void setSalario(double salario) {
        if (salario < 0) {
            throw new IllegalArgumentException("Salário não pode ser negativo");
        }
        this.salario = salario;
    }
    
    /**
     * Obtém o departamento do funcionário.
     * 
     * @return Departamento
     */
    public String getDepartamento() {
        return departamento;
    }
    
    /**
     * Define o departamento do funcionário.
     * 
     * @param departamento Departamento
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    
    /**
     * Verifica se o funcionário está ativo.
     * 
     * @return true se ativo, false caso contrário
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    /**
     * Define o status de ativação do funcionário.
     * 
     * @param ativo Status de ativação
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + getId() +
                ", nome='" + getNome() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", matricula='" + matricula + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                ", departamento='" + departamento + '\'' +
                ", ativo=" + ativo +
                '}';
    }
}
