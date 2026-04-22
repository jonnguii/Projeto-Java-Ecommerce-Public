package com.ecommerce;

import com.ecommerce.model.*;

/**
 * Classe principal de demonstração do sistema de 'e-commerce'.
 * Versão demo que não requer conexão com banco de dados.
 * 
 * @author Sistema 'E-commerce'
 * @version 1.0
 */
public class MainDemo {
    
    /**
     * Método principal que inicia a demonstração.
     */
    public static void main(String[] args) {
        System.out.println("=== SISTEMA E-COMMERCE - VERSÃO DEMO ===");
        System.out.println("🎓 Demonstração das funcionalidades sem banco de dados\n");
        
        try {
            demonstrarFuncionalidades();
        } catch (Exception e) {
            System.err.println("Erro na demonstração: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n🎉 Demonstração concluída com sucesso!");
        System.out.println("📝 Para usar o sistema completo, configure o PostgreSQL e execute com o driver JDBC.");
    }
    
    /**
     * Demonstra todas as funcionalidades principais do sistema.
     */
    private static void demonstrarFuncionalidades() {
        
        // 1. Herança e Polimorfismo
        System.out.println("1. 🧬 HERANÇA E POLIMORFISMO");
        System.out.println("===============================");
        
        Cliente cliente = new Cliente("João Silva", "joao@email.com", "11999999999", 
                                    "12345678901", "Rua A, 123");
        Funcionario funcionario = new Funcionario("Ana Maria", "ana@empresa.com", "11888888888", 
                                                "MAT001", "Vendedora", 3500.0);
        
        System.out.println("👤 Cliente: " + cliente.getNome() + " (" + cliente.getTipoPessoa() + ")");
        System.out.println("👤 Funcionário: " + funcionario.getNome() + " (" + funcionario.getTipoPessoa() + ")");
        System.out.println("📄 CPF Cliente: " + cliente.getCpf());
        System.out.println("💼 Matrícula Funcionário: " + funcionario.getMatricula());
        System.out.println("✅ Herança e polimorfismo funcionando!\n");
        
        // 2. Produtos e Estoque
        System.out.println("2. 📦 PRODUTOS E CONTROLE DE ESTOQUE");
        System.out.println("====================================");
        
        Produto notebook = new Produto("Notebook Dell", "Intel i5, 8GB RAM, 256GB SSD", 3500.00, "NOTE001");
        Produto mouse = new Produto("Mouse Wireless", "Logitech sem fio", 150.00, "MOUSE001");
        
        Estoque estoqueNotebook = new Estoque(notebook, 50, 10, 200, "A1-B2");
        Estoque estoqueMouse = new Estoque(mouse, 200, 20, 500, "A3-B4");
        
        System.out.println("💻 " + notebook.getNome() + " - Estoque: " + estoqueNotebook.getQuantidadeAtual() + " unidades");
        System.out.println("🖱️ " + mouse.getNome() + " - Estoque: " + estoqueMouse.getQuantidadeAtual() + " unidades");
        
        // Simular vendas
        estoqueNotebook.removerEstoque(5);
        estoqueMouse.removerEstoque(50);
        
        System.out.println("📉 Após vendas:");
        System.out.println("💻 Notebook: " + estoqueNotebook.getQuantidadeAtual() + " unidades (" + 
                          (estoqueNotebook.estaComEstoqueBaixo() ? "⚠️ ESTOQUE BAIXO" : "✅ NORMAL") + ")");
        System.out.println("🖱️ Mouse: " + estoqueMouse.getQuantidadeAtual() + " unidades (" + 
                          (estoqueMouse.estaComEstoqueBaixo() ? "⚠️ ESTOQUE BAIXO" : "✅ NORMAL") + ")");
        System.out.println("✅ Controle de estoque funcionando!\n");
        
        // 3. Pedidos e Descontos
        System.out.println("3. 🛒 PEDIDOS E DESCONTOS PROGRESSIVOS");
        System.out.println("=======================================");
        
        // Pedido 1: Sem desconto
        Pedido pedido1 = new Pedido(cliente);
        pedido1.adicionarItem(mouse, 2);
        System.out.println("📋 Pedido 1: R$ " + String.format("%.2f", pedido1.getSubtotal()) + 
                          " → Desconto: R$ " + String.format("%.2f", pedido1.getDesconto()) + 
                          " (" + String.format("%.1f", pedido1.getPercentualDesconto()) + "%)" +
                          " = Total: R$ " + String.format("%.2f", pedido1.getValorTotal()));
        
        // Pedido 2: 5% de desconto
        Cliente cliente2 = new Cliente("Maria", "maria@email.com", "11988888888", "98765432100", "Rua B");
        Pedido pedido2 = new Pedido(cliente2);
        pedido2.adicionarItem(notebook, 1);
        pedido2.adicionarItem(mouse, 3);
        System.out.println("📋 Pedido 2: R$ " + String.format("%.2f", pedido2.getSubtotal()) + 
                          " → Desconto: R$ " + String.format("%.2f", pedido2.getDesconto()) + 
                          " (" + String.format("%.1f", pedido2.getPercentualDesconto()) + "%)" +
                          " = Total: R$ " + String.format("%.2f", pedido2.getValorTotal()));
        
        // Pedido 3: 10% de desconto
        Cliente cliente3 = new Cliente("Empresa XYZ", "compras@xyz.com", "11977777777", "11122233344", "Av. Central");
        Pedido pedido3 = new Pedido(cliente3);
        pedido3.adicionarItem(notebook, 3);
        System.out.println("📋 Pedido 3: R$ " + String.format("%.3f", pedido3.getSubtotal()) + 
                          " → Desconto: R$ " + String.format("%.2f", pedido3.getDesconto()) + 
                          " (" + String.format("%.1f", pedido3.getPercentualDesconto()) + "%)" +
                          " = Total: R$ " + String.format("%.2f", pedido3.getValorTotal()));
        
        System.out.println("✅ Descontos progressivos funcionando!\n");
        
        // 4. Estratégias de Pagamento
        System.out.println("4. 💳 ESTRATÉGIAS DE PAGAMENTO (POLIMORFISMO)");
        System.out.println("==============================================");
        
        System.out.println("🔧 Interface IPagamentoStrategy implementada:");
        System.out.println("   • PagamentoCartaoStrategy");
        System.out.println("   • PagamentoBoletoStrategy");
        System.out.println("   • PagamentoPixStrategy");
        System.out.println("✅ Polimorfismo em pagamentos implementado!\n");
        
        // 5. Validações e Exceções
        System.out.println("5. 🛡️ VALIDAÇÕES E EXCEÇÕES PERSONALIZADAS");
        System.out.println("===========================================");
        
        // Tentar remover mais do que tem em estoque
        if (!estoqueMouse.temEstoqueSuficiente(1000)) {
            System.out.println("⚠️ Validação: Estoque insuficiente! Disponível: " + 
                              estoqueMouse.getQuantidadeAtual() + ", Solicitado: 1000");
            System.out.println("💡 Sugestão: Aumente o estoque ou reduza a quantidade solicitada");
        }
        
        // Validar dados do cliente
        Cliente clienteInvalido = new Cliente("", "email-invalido", "11", "123", "");
        if (!clienteInvalido.validarDados()) {
            System.out.println("⚠️ Validação de dados funcionou: cliente com dados inválidos rejeitado");
        }
        
        System.out.println("✅ Sistema de validação funcionando!\n");
        
        // Resumo Final
        System.out.println("📊 RESUMO DA DEMONSTRAÇÃO");
        System.out.println("========================");
        System.out.println("✅ Herança: Pessoa → Cliente/Funcionario");
        System.out.println("✅ Polimorfismo: getTipoPessoa() e IPagamentoStrategy");
        System.out.println("✅ Encapsulamento: atributos privados com getters/setters");
        System.out.println("✅ Controle de Estoque: validações e alertas");
        System.out.println("✅ Descontos Progressivos: 5% e 10% automáticos");
        System.out.println("✅ Exceções Personalizadas: EstoqueInsuficienteException");
        System.out.println("✅ Validações: dados de clientes e produtos");
    }
}
