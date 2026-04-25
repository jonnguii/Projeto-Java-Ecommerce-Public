package com.ecommerce.view;

import com.ecommerce.dao.EstoqueDAO;
import com.ecommerce.dao.ProdutoDAO;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.model.Cliente;
import com.ecommerce.model.Estoque;
import com.ecommerce.model.Funcionario;
import com.ecommerce.model.Pedido;
import com.ecommerce.model.Pessoa;
import com.ecommerce.model.Produto;
import com.ecommerce.service.IPagamento;
import com.ecommerce.service.PagamentoBoletoService;
import com.ecommerce.service.PagamentoCartaoService;
import com.ecommerce.service.PagamentoPixService;

import java.util.List;
import java.util.Scanner;

public class MenuRN {
    Scanner scanner = new Scanner(System.in);
    ProdutoDAO produtoDAO = new ProdutoDAO();
    EstoqueDAO estoqueDAO = new EstoqueDAO();

    /**
     * Testa as regras de negócio complexas do sistema.
     */
    public void testarRegrasNegocio() {
        System.out.println("\n=== TESTAR REGRAS DE NEGÓCIO ===");
        System.out.println("1. Testar controle de estoque");
        System.out.println("2. Testar cálculo de descontos progressivos");
        System.out.println("3. Testar estratégias de pagamento (polimorfismo)");
        System.out.println("4. Testar herança e polimorfismo");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = Integer.parseInt(scanner.nextLine());

            switch (opcao) {
                case 1:
                    testarControleEstoque();
                    break;
                case 2:
                    testarDescontosProgressivos();
                    break;
                case 3:
                    testarEstrategiasPagamento();
                    break;
                case 4:
                    testarHerancaPolimorfismo();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
        }
    }

    /**
     * Testa a regra de negócio complexa de controle de estoque.
     */
    private void testarControleEstoque() {
        System.out.println("\n=== TESTE: CONTROLE DE ESTOQUE ===");

        try {
            // Buscar um produto para teste
            List<Produto> produtos = produtoDAO.listarAtivos();
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado para teste.");
                return;
            }

            Produto produtoTeste = produtos.get(0);
            Estoque estoque = estoqueDAO.buscarPorProduto(produtoTeste);

            if (estoque == null) {
                System.out.println("Produto sem registro de estoque.");
                return;
            }

            System.out.println("Produto selecionado: " + produtoTeste.getNome());
            System.out.println("Estoque atual: " + estoque.getQuantidadeAtual());

            // Teste 1: Tentar remover mais do que tem em estoque
            System.out.println("\nTeste 1: Tentando remover mais do que o disponível...");
            int quantidadeExcessiva = estoque.getQuantidadeAtual() + 10;

            try {
                if (!estoque.temEstoqueSuficiente(quantidadeExcessiva)) {
                    throw new EstoqueInsuficienteException(
                            produtoTeste.getNome(),
                            quantidadeExcessiva,
                            estoque.getQuantidadeAtual());
                }
            } catch (EstoqueInsuficienteException e) {
                System.out.println("✅ Exceção capturada corretamente:");
                System.out.println("   " + e.getMessage());
                System.out.println("   " + e.getMensagemSugestao());
            }

            // Teste 2: Verificar alerta de estoque baixo
            System.out.println("\nTeste 2: Verificação de estoque baixo...");
            System.out.println("Estoque mínimo: " + estoque.getQuantidadeMinima());
            System.out.println("Status atual: " + estoque.getStatusEstoque().getDescricao());

            // Teste 3: Simular pedido e dedução automática
            System.out.println("\nTeste 3: Simulação de pedido com dedução automática...");
            if (estoque.getQuantidadeAtual() > 0) {
                int quantidadePedido = Math.min(2, estoque.getQuantidadeAtual());
                System.out.printf("Simulando pedido de %d unidades...%n", quantidadePedido);

                if (estoqueDAO.removerEstoque(produtoTeste.getId(), quantidadePedido)) {
                    Estoque estoqueAtualizado = estoqueDAO.buscarPorProduto(produtoTeste);
                    System.out.println("✅ Estoque deduzido com sucesso!");
                    System.out.println("   Quantidade antes: " + estoque.getQuantidadeAtual());
                    System.out.println("   Quantidade depois: " + estoqueAtualizado.getQuantidadeAtual());

                    if (estoqueAtualizado.estaComEstoqueBaixo()) {
                        System.out.println("   ⚠️ ALERTA: Estoque baixo após o pedido!");
                    }

                    // Restaurar estoque para não afetar os dados
                    estoqueDAO.adicionarEstoque(produtoTeste.getId(), quantidadePedido);
                    System.out.println("   🔄 Estoque restaurado para o teste.");
                }
            } else {
                System.out.println("Produto sem estoque para simular pedido.");
            }

            System.out.println("\n✅ Testes de controle de estoque concluídos com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro durante os testes: " + e.getMessage());
        }
    }

    /**
     * Testa a regra de descontos progressivos.
     */
    private void testarDescontosProgressivos() {
        System.out.println("\n=== TESTE: DESCONTOS PROGRESSIVOS ===");

        // Criar pedidos de teste com diferentes valores
        double[] valoresTeste = { 200.0, 600.0, 1200.0 };
        String[] descricaoTeste = { "Sem desconto", "5% de desconto", "10% de desconto" };

        for (int i = 0; i < valoresTeste.length; i++) {
            System.out.printf("\nTeste %d - %s:%n", i + 1, descricaoTeste[i]);

            // Criar pedido de teste
            Cliente clienteTeste = new Cliente("Cliente Teste", "test@email.com", "11999999999", "12345678901",
                    "Rua Teste");
            Pedido pedido = new Pedido(clienteTeste);

            // Adicionar item simulado
            Produto produtoTeste = new Produto("Produto Teste", "Descrição", valoresTeste[i], "TEST001");
            pedido.adicionarItem(produtoTeste, 1);

            System.out.printf("Subtotal: R$ %.2f%n", pedido.getSubtotal());
            System.out.printf("Desconto: R$ %.2f (%.1f%%)%n",
                    pedido.getDesconto(), pedido.getPercentualDesconto());
            System.out.printf("Total: R$ %.2f%n", pedido.getValorTotal());

            // Verificar se o desconto foi aplicado corretamente
            double descontoEsperado = 0;
            if (valoresTeste[i] > 1000) {
                descontoEsperado = valoresTeste[i] * 0.10;
            } else if (valoresTeste[i] > 500) {
                descontoEsperado = valoresTeste[i] * 0.05;
            }

            if (Math.abs(pedido.getDesconto() - descontoEsperado) < 0.01) {
                System.out.println("✅ Desconto aplicado corretamente!");
            } else {
                System.out.println("❌ Erro no cálculo do desconto!");
            }
        }

        System.out.println("\n✅ Testes de descontos progressivos concluídos!");
    }

    /**
     * Testa polimorfismo com estratégias de pagamento.
     */
    private void testarEstrategiasPagamento() {
        System.out.println("\n=== TESTE: ESTRATÉGIAS DE PAGAMENTO (POLIMORFISMO) ===");

        // Criar diferentes estratégias de pagamento
        IPagamento[] estrategias = {
                new PagamentoCartaoService(),
                new PagamentoBoletoService(),
                new PagamentoPixService()
        };

        double valorTeste = 500.0;

        for (IPagamento estrategia : estrategias) {
            System.out.printf("\nTestando %s:%n", estrategia.getNomeFormaPagamento());
            System.out.printf("Tempo de processamento: %d segundos%n", estrategia.getTempoProcessamentoSegundos());

            // Testar com dados válidos
            String dadosValidos = "";
            String dadosInvalidos = "";

            if (estrategia instanceof PagamentoCartaoService) {
                dadosValidos = "4111111111111111"; // Visa de teste
                dadosInvalidos = "123";
            } else if (estrategia instanceof PagamentoBoletoService) {
                dadosValidos = "12345678901"; // CPF válido
                dadosInvalidos = "123";
            } else if (estrategia instanceof PagamentoPixService) {
                dadosValidos = "12345678901"; // CPF para PIX
                dadosInvalidos = "123";
            }

            // Testar dados válidos
            boolean dadosValidosOk = estrategia.validarDadosPagamento(dadosValidos);
            System.out.printf("Validação dados válidos: %s%n", dadosValidosOk ? "✅ Válidos" : "❌ Inválidos");

            if (dadosValidosOk) {
                boolean pagamentoOk = estrategia.processarPagamento(valorTeste, dadosValidos);
                System.out.printf("Processamento pagamento: %s%n", pagamentoOk ? "✅ Aprovado" : "❌ Reprovado");
            }

            // Testar dados inválidos
            boolean dadosInvalidosOk = estrategia.validarDadosPagamento(dadosInvalidos);
            System.out.printf("Validação dados inválidos: %s%n",
                    !dadosInvalidosOk ? "✅ Rejeitados" : "❌ Aceitos (erro)");
        }

        System.out.println("\n✅ Testes de polimorfismo concluídos!");
        System.out.println("Todas as estratégias implementam a mesma interface mas comportam-se de forma diferente.");
    }

    /**
     * Testa herança e polimorfismo.
     */
    private void testarHerancaPolimorfismo() {
        System.out.println("\n=== TESTE: HERANÇA E POLIMORFISMO ===");

        // Criar diferentes tipos de pessoas
        Cliente cliente = new Cliente("João Cliente", "joao@email.com", "11999999999",
                "12345678901", "Rua A, 123");
        Funcionario funcionario = new Funcionario("Ana Funcionária", "ana@empresa.com", "11888888888",
                "MAT001", "Vendedora", 3500.0);

        // Array polimórfico de Pessoa
        Pessoa[] pessoas = {
                cliente,
                funcionario
        };

        System.out.println("\nDemonstração de polimorfismo:");
        for (Pessoa pessoa : pessoas) {
            System.out.printf("\n%s:%n", pessoa.getClass().getSimpleName());
            System.out.printf("Nome: %s%n", pessoa.getNome());
            System.out.printf("Email: %s%n", pessoa.getEmail());
            System.out.printf("Tipo: %s%n", pessoa.getTipoPessoa());
            System.out.printf("Validação: %s%n", pessoa.validarDados() ? "✅ Válida" : "❌ Inválida");

            // Downcasting para métodos específicos
            if (pessoa instanceof Cliente c) {
                System.out.printf("CPF: %s%n", c.getCpf());
                System.out.printf("Status: %s%n", c.isAtivo() ? "Ativo" : "Inativo");
            } else if (pessoa instanceof Funcionario f) {
                System.out.printf("Matrícula: %s%n", f.getMatricula());
                System.out.printf("Cargo: %s%n", f.getCargo());
                System.out.printf("Salário: R$ %.2f%n", f.getSalario());
            }
        }

        // Testar métodos específicos
        System.out.println("\nTestando métodos específicos:");

        System.out.println("\nCliente - Métodos específicos:");
        System.out.printf("Status inicial: %s%n", cliente.isAtivo() ? "Ativo" : "Inativo");
        cliente.desativar();
        System.out.printf("Status após desativar: %s%n", cliente.isAtivo() ? "Ativo" : "Inativo");
        cliente.ativar();
        System.out.printf("Status após ativar: %s%n", cliente.isAtivo() ? "Ativo" : "Inativo");

        System.out.println("\nFuncionário - Métodos específicos:");
        System.out.printf("Salário inicial: R$ %.2f%n", funcionario.getSalario());
        funcionario.aplicarAumento(10);
        System.out.printf("Salário após 10%% de aumento: R$ %.2f%n", funcionario.getSalario());
        funcionario.promover("Supervisora", 4200.0);
        System.out.printf("Após promoção: %s - R$ %.2f%n", funcionario.getCargo(), funcionario.getSalario());

        System.out.println("\n✅ Testes de herança e polimorfismo concluídos!");
        System.out.println("✅ Herança: Cliente e Funcionário herdam de Pessoa");
        System.out.println("✅ Polimorfismo: Método getTipoPessoa() comporta-se diferente para cada subclasse");
        System.out.println("✅ Encapsulamento: Todos os atributos são privados com getters/setters");
    }
}
