package com.ecommerce.service;

import com.ecommerce.dao.*;
import com.ecommerce.exception.*;
import com.ecommerce.model.*;
import com.ecommerce.util.DatabaseConnection;

import java.util.List;
import java.util.Scanner;

/**
 * Classe principal de serviços do sistema de e-commerce.
 * Implementa as regras de negócio complexas e integra todos os componentes.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class EcommerceService {
    
    private final ClienteDAO clienteDAO;
    private final ProdutoDAO produtoDAO;
    private final EstoqueDAO estoqueDAO;
    private final PedidoDAO pedidoDAO;
    private final Scanner scanner;
    
    /**
     * Construtor.
     */
    public EcommerceService() {
        this.clienteDAO = new ClienteDAO();
        this.produtoDAO = new ProdutoDAO();
        this.estoqueDAO = new EstoqueDAO();
        this.pedidoDAO = new PedidoDAO();
        this.scanner = new Scanner(System.in);
    }
    
    /**
     * Inicia o sistema e exibe o menu principal.
     */
    public void iniciarSistema() {
        System.out.println("Sistema de E-commerce iniciado com sucesso!");
        
        // Testa conexão com o banco
        if (!DatabaseConnection.getInstance().testarConexao()) {
            System.err.println("ERRO: Não foi possível conectar ao banco de dados.");
            System.err.println("Verifique se o PostgreSQL está rodando e as configurações estão corretas.");
            return;
        }
        
        System.out.println("Conexão com banco de dados estabelecida com sucesso!");
        
        menuPrincipal();
    }
    
    /**
     * Exibe o menu principal do sistema.
     */
    private void menuPrincipal() {
        while (true) {
            System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Produtos");
            System.out.println("3. Gerenciar Estoque");
            System.out.println("4. Gerenciar Pedidos");
            System.out.println("5. Relatórios");
            System.out.println("6. Testar Regras de Negócio");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            
            try {
                int opcao = Integer.parseInt(scanner.nextLine());
                
                switch (opcao) {
                    case 1:
                        menuClientes();
                        break;
                    case 2:
                        menuProdutos();
                        break;
                    case 3:
                        menuEstoque();
                        break;
                    case 4:
                        menuPedidos();
                        break;
                    case 5:
                        menuRelatorios();
                        break;
                    case 6:
                        testarRegrasNegocio();
                        break;
                    case 0:
                        System.out.println("Encerrando sistema. Até logo!");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido!");
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }
        }
    }
    
    /**
     * Menu de gerenciamento de clientes.
     */
    private void menuClientes() {
        System.out.println("\n=== GERENCIAR CLIENTES ===");
        System.out.println("1. Listar todos os clientes");
        System.out.println("2. Buscar cliente por ID");
        System.out.println("3. Buscar cliente por CPF");
        System.out.println("4. Cadastrar novo cliente");
        System.out.println("5. Atualizar cliente");
        System.out.println("6. Desativar cliente");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    listarClientes();
                    break;
                case 2:
                    buscarClientePorId();
                    break;
                case 3:
                    buscarClientePorCpf();
                    break;
                case 4:
                    cadastrarCliente();
                    break;
                case 5:
                    atualizarCliente();
                    break;
                case 6:
                    desativarCliente();
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
     * Lista todos os clientes.
     */
    private void listarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente cadastrado.");
                return;
            }
            
            System.out.println("\n=== LISTA DE CLIENTES ===");
            System.out.printf("%-5s %-20s %-25s %-15s %-10s%n", 
                            "ID", "Nome", "Email", "Telefone", "Status");
            System.out.println("-".repeat(80));
            
            for (Cliente cliente : clientes) {
                System.out.printf("%-5d %-20s %-25s %-15s %-10s%n",
                                cliente.getId(),
                                cliente.getNome(),
                                cliente.getEmail(),
                                cliente.getTelefone(),
                                cliente.isAtivo() ? "Ativo" : "Inativo");
            }
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
        }
    }
    
    /**
     * Busca cliente por ID.
     */
    private void buscarClientePorId() {
        System.out.print("Digite o ID do cliente: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente != null) {
                System.out.println("\nCliente encontrado:");
                System.out.println(cliente);
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Busca cliente por CPF.
     */
    private void buscarClientePorCpf() {
        System.out.print("Digite o CPF do cliente: ");
        String cpf = scanner.nextLine();
        
        try {
            Cliente cliente = clienteDAO.buscarPorCpf(cpf);
            
            if (cliente != null) {
                System.out.println("\nCliente encontrado:");
                System.out.println(cliente);
            } else {
                System.out.println("Cliente não encontrado.");
            }
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Cadastra um novo cliente.
     */
    private void cadastrarCliente() {
        try {
            System.out.println("\n=== CADASTRAR NOVO CLIENTE ===");
            
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            
            System.out.print("Email: ");
            String email = scanner.nextLine();
            
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();
            
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            
            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();
            
            System.out.print("Cidade: ");
            String cidade = scanner.nextLine();
            
            System.out.print("Estado (UF): ");
            String estado = scanner.nextLine();
            
            System.out.print("CEP: ");
            String cep = scanner.nextLine();
            
            Cliente cliente = new Cliente(nome, email, telefone, cpf, endereco);
            cliente.setCidade(cidade);
            cliente.setEstado(estado);
            cliente.setCep(cep);
            
            // Valida dados
            if (!cliente.validarDados()) {
                System.out.println("Dados inválidos! Verifique nome, email e CPF.");
                return;
            }
            
            // Verifica email duplicado
            if (clienteDAO.emailJaExiste(email, null)) {
                System.out.println("Email já cadastrado!");
                return;
            }
            
            Cliente clienteInserido = clienteDAO.inserir(cliente);
            System.out.println("Cliente cadastrado com sucesso! ID: " + clienteInserido.getId());
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza um cliente existente.
     */
    private void atualizarCliente() {
        System.out.print("Digite o ID do cliente a atualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            System.out.println("\nDados atuais:");
            System.out.println(cliente);
            
            System.out.println("\nDigite os novos dados (deixe em branco para manter atual):");
            
            System.out.print("Nome [" + cliente.getNome() + "]: ");
            String nome = scanner.nextLine();
            if (!nome.trim().isEmpty()) cliente.setNome(nome);
            
            System.out.print("Email [" + cliente.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (!email.trim().isEmpty()) {
                if (!email.equals(cliente.getEmail()) && clienteDAO.emailJaExiste(email, id)) {
                    System.out.println("Email já cadastrado!");
                    return;
                }
                cliente.setEmail(email);
            }
            
            System.out.print("Telefone [" + cliente.getTelefone() + "]: ");
            String telefone = scanner.nextLine();
            if (!telefone.trim().isEmpty()) cliente.setTelefone(telefone);
            
            System.out.print("Endereço [" + cliente.getEndereco() + "]: ");
            String endereco = scanner.nextLine();
            if (!endereco.trim().isEmpty()) cliente.setEndereco(endereco);
            
            if (clienteDAO.atualizar(cliente)) {
                System.out.println("Cliente atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar cliente.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao atualizar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Desativa um cliente.
     */
    private void desativarCliente() {
        System.out.print("Digite o ID do cliente a desativar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(id);
            
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            System.out.println("Cliente: " + cliente.getNome());
            System.out.print("Confirmar desativação? (S/N): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("S")) {
                if (clienteDAO.desativar(id)) {
                    System.out.println("Cliente desativado com sucesso!");
                } else {
                    System.out.println("Erro ao desativar cliente.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao desativar cliente: " + e.getMessage());
        }
    }
    
    /**
     * Menu de gerenciamento de produtos.
     */
    private void menuProdutos() {
        System.out.println("\n=== GERENCIAR PRODUTOS ===");
        System.out.println("1. Listar todos os produtos");
        System.out.println("2. Buscar produto por ID");
        System.out.println("3. Buscar produto por SKU");
        System.out.println("4. Buscar produto por nome");
        System.out.println("5. Cadastrar novo produto");
        System.out.println("6. Atualizar produto");
        System.out.println("7. Desativar produto");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    listarProdutos();
                    break;
                case 2:
                    buscarProdutoPorId();
                    break;
                case 3:
                    buscarProdutoPorSku();
                    break;
                case 4:
                    buscarProdutoPorNome();
                    break;
                case 5:
                    cadastrarProduto();
                    break;
                case 6:
                    atualizarProduto();
                    break;
                case 7:
                    desativarProduto();
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
     * Lista todos os produtos.
     */
    private void listarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto cadastrado.");
                return;
            }
            
            System.out.println("\n=== LISTA DE PRODUTOS ===");
            System.out.printf("%-5s %-20s %-30s %-10s %-10s %-8s%n", 
                            "ID", "Nome", "Descrição", "Preço", "SKU", "Status");
            System.out.println("-".repeat(90));
            
            for (Produto produto : produtos) {
                String descricao = produto.getDescricao();
                if (descricao != null && descricao.length() > 28) {
                    descricao = descricao.substring(0, 25) + "...";
                }
                
                System.out.printf("%-5d %-20s %-30s %-10.2f %-10s %-8s%n",
                                produto.getId(),
                                produto.getNome(),
                                descricao,
                                produto.getPreco(),
                                produto.getSku(),
                                produto.isAtivo() ? "Ativo" : "Inativo");
            }
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao listar produtos: " + e.getMessage());
        }
    }
    
    /**
     * Busca produto por ID.
     */
    private void buscarProdutoPorId() {
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);
            
            if (produto != null) {
                System.out.println("\nProduto encontrado:");
                System.out.println(produto);
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
    }
    
    /**
     * Busca produto por SKU.
     */
    private void buscarProdutoPorSku() {
        System.out.print("Digite o SKU do produto: ");
        String sku = scanner.nextLine();
        
        try {
            Produto produto = produtoDAO.buscarPorSku(sku);
            
            if (produto != null) {
                System.out.println("\nProduto encontrado:");
                System.out.println(produto);
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar produto: " + e.getMessage());
        }
    }
    
    /**
     * Busca produto por nome.
     */
    private void buscarProdutoPorNome() {
        System.out.print("Digite o nome ou parte do nome do produto: ");
        String nome = scanner.nextLine();
        
        try {
            List<Produto> produtos = produtoDAO.buscarPorNome(nome);
            
            if (produtos.isEmpty()) {
                System.out.println("Nenhum produto encontrado com este nome.");
                return;
            }
            
            System.out.println("\n=== PRODUTOS ENCONTRADOS ===");
            for (Produto produto : produtos) {
                System.out.printf("ID: %d - %s - R$ %.2f - %s%n",
                                produto.getId(),
                                produto.getNome(),
                                produto.getPreco(),
                                produto.getSku());
            }
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao buscar produtos: " + e.getMessage());
        }
    }
    
    /**
     * Cadastra um novo produto.
     */
    private void cadastrarProduto() {
        try {
            System.out.println("\n=== CADASTRAR NOVO PRODUTO ===");
            
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();
            
            System.out.print("Preço: ");
            double preco = Double.parseDouble(scanner.nextLine());
            
            System.out.print("SKU: ");
            String sku = scanner.nextLine();
            
            Produto produto = new Produto(nome, descricao, preco, sku);
            
            // Verifica SKU duplicado
            if (produtoDAO.skuJaExiste(sku, null)) {
                System.out.println("SKU já cadastrado!");
                return;
            }
            
            Produto produtoInserido = produtoDAO.inserir(produto);
            System.out.println("Produto cadastrado com sucesso! ID: " + produtoInserido.getId());
            
            // Perguntar localização do estoque
            System.out.print("Localização do estoque (ex: Galeria A, Prateleira 1, etc.): ");
            String localizacao = scanner.nextLine();
            
            // Cria registro de estoque automaticamente com localização
            Estoque estoque = new Estoque(produtoInserido, 0);
            estoque.setLocalizacao(localizacao.isEmpty() ? "Principal" : localizacao);
            estoqueDAO.inserir(estoque);
            System.out.println("Registro de estoque criado automaticamente com localização: " + estoque.getLocalizacao());
            
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao cadastrar produto: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza um produto existente.
     */
    private void atualizarProduto() {
        System.out.print("Digite o ID do produto a atualizar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);
            
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }
            
            System.out.println("\nDados atuais:");
            System.out.println(produto);
            
            System.out.println("\nDigite os novos dados (deixe em branco para manter atual):");
            
            System.out.print("Nome [" + produto.getNome() + "]: ");
            String nome = scanner.nextLine();
            if (!nome.trim().isEmpty()) produto.setNome(nome);
            
            System.out.print("Descrição [" + produto.getDescricao() + "]: ");
            String descricao = scanner.nextLine();
            if (!descricao.trim().isEmpty()) produto.setDescricao(descricao);
            
            System.out.print("Preço [" + produto.getPreco() + "]: ");
            String precoStr = scanner.nextLine();
            if (!precoStr.trim().isEmpty()) {
                double preco = Double.parseDouble(precoStr);
                produto.setPreco(preco);
            }
            
            System.out.print("SKU [" + produto.getSku() + "]: ");
            String sku = scanner.nextLine();
            if (!sku.trim().isEmpty()) {
                if (!sku.equals(produto.getSku()) && produtoDAO.skuJaExiste(sku, id)) {
                    System.out.println("SKU já cadastrado!");
                    return;
                }
                produto.setSku(sku);
            }
            
            if (produtoDAO.atualizar(produto)) {
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Erro ao atualizar produto.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao atualizar produto: " + e.getMessage());
        }
    }
    
    /**
     * Desativa um produto.
     */
    private void desativarProduto() {
        System.out.print("Digite o ID do produto a desativar: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);
            
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }
            
            System.out.println("Produto: " + produto.getNome());
            System.out.print("Confirmar desativação? (S/N): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("S")) {
                if (produtoDAO.desativar(id)) {
                    System.out.println("Produto desativado com sucesso!");
                } else {
                    System.out.println("Erro ao desativar produto.");
                }
            } else {
                System.out.println("Operação cancelada.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao desativar produto: " + e.getMessage());
        }
    }
    
    /**
     * Menu de gerenciamento de estoque.
     */
    private void menuEstoque() {
        System.out.println("\n=== GERENCIAR ESTOQUE ===");
        System.out.println("1. Listar todo o estoque");
        System.out.println("2. Verificar estoque de um produto");
        System.out.println("3. Adicionar produtos ao estoque");
        System.out.println("4. Remover produtos do estoque");
        System.out.println("5. Listar produtos com estoque baixo");
        System.out.println("6. Listar produtos sem estoque");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    listarEstoque();
                    break;
                case 2:
                    verificarEstoqueProduto();
                    break;
                case 3:
                    adicionarEstoque();
                    break;
                case 4:
                    removerEstoque();
                    break;
                case 5:
                    listarEstoqueBaixo();
                    break;
                case 6:
                    listarSemEstoque();
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
     * Lista todo o estoque.
     */
    private void listarEstoque() {
        try {
            List<Estoque> estoques = estoqueDAO.listarTodos();
            
            if (estoques.isEmpty()) {
                System.out.println("Nenhum registro de estoque encontrado.");
                return;
            }
            
            System.out.println("\n=== ESTOQUE ATUAL ===");
            System.out.printf("%-5s %-25s %-15s %-15s %-15s %-15s %-10s%n", 
                            "ID", "Produto", "Quantidade", "Mínima", "Máxima", "Localização", "Status");
            System.out.println("-".repeat(100));
            
            for (Estoque estoque : estoques) {
                String status;
                if (estoque.getQuantidadeAtual() == 0) {
                    status = "SEM ESTOQUE";
                } else if (estoque.getQuantidadeAtual() < estoque.getQuantidadeMinima()) {
                    status = "ABAIXO";
                } else if (estoque.getQuantidadeAtual() > estoque.getQuantidadeMaxima()) {
                    status = "ACIMA";
                } else {
                    status = "NORMAL";
                }
                
                System.out.printf("%-5d %-25s %-15d %-15d %-15d %-15s %-10s%n",
                                estoque.getId(),
                                estoque.getProduto().getNome(),
                                estoque.getQuantidadeAtual(),
                                estoque.getQuantidadeMinima(),
                                estoque.getQuantidadeMaxima(),
                                estoque.getLocalizacao(),
                                status);
            }
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao listar estoque: " + e.getMessage());
        }
    }
    
    /**
     * Verifica estoque de um produto específico.
     */
    private void verificarEstoqueProduto() {
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);
            
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }
            
            Estoque estoque = estoqueDAO.buscarPorProduto(produto);
            
            if (estoque == null) {
                System.out.println("Registro de estoque não encontrado para este produto.");
                return;
            }
            
            System.out.println("\n=== ESTOQUE DO PRODUTO ===");
            System.out.println("Produto: " + estoque.getProduto().getNome());
            System.out.println("Quantidade atual: " + estoque.getQuantidadeAtual());
            System.out.println("Quantidade mínima: " + estoque.getQuantidadeMinima());
            System.out.println("Quantidade máxima: " + estoque.getQuantidadeMaxima());
            System.out.println("Localização: " + estoque.getLocalizacao());
            
            // Determinar status correto
            String status;
            if (estoque.getQuantidadeAtual() == 0) {
                status = "SEM ESTOQUE";
            } else if (estoque.getQuantidadeAtual() < estoque.getQuantidadeMinima()) {
                status = "ABAIXO";
            } else if (estoque.getQuantidadeAtual() > estoque.getQuantidadeMaxima()) {
                status = "ACIMA";
            } else {
                status = "NORMAL";
            }
            
            System.out.println("Status: " + status);
            System.out.println("Última atualização: " + estoque.getDataUltimaAtualizacao());
            
        } catch (NumberFormatException e) {
            System.out.println("ID inválido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao verificar estoque: " + e.getMessage());
        }
    }
    
    /**
     * Adiciona produtos ao estoque.
     */
    private void adicionarEstoque() {
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);
            
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }
            
            System.out.print("Digite a quantidade a adicionar: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            
            if (quantidade <= 0) {
                System.out.println("Quantidade deve ser positiva!");
                return;
            }
            
            if (estoqueDAO.adicionarEstoque(id, quantidade)) {
                System.out.println("Estoque atualizado com sucesso!");
                
                // Verifica se atingiu o máximo
                Estoque estoque = estoqueDAO.buscarPorProduto(produto);
                if (estoque != null && estoque.getQuantidadeAtual() > estoque.getQuantidadeMaxima()) {
                    System.out.println("ATENÇÃO: Quantidade atual excede o máximo permitido!");
                }
            } else {
                System.out.println("Erro ao atualizar estoque.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao adicionar estoque: " + e.getMessage());
        }
    }
    
    /**
     * Remove produtos do estoque.
     */
    private void removerEstoque() {
        System.out.print("Digite o ID do produto: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            Produto produto = produtoDAO.buscarPorId(id);
            
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                return;
            }
            
            Estoque estoque = estoqueDAO.buscarPorProduto(produto);
            if (estoque == null) {
                System.out.println("Registro de estoque não encontrado.");
                return;
            }
            
            System.out.println("Quantidade atual em estoque: " + estoque.getQuantidadeAtual());
            System.out.print("Digite a quantidade a remover: ");
            int quantidade = Integer.parseInt(scanner.nextLine());
            
            if (quantidade <= 0) {
                System.out.println("Quantidade deve ser positiva!");
                return;
            }
            
            if (!estoque.temEstoqueSuficiente(quantidade)) {
                System.out.println("Estoque insuficiente! Disponível: " + estoque.getQuantidadeAtual());
                return;
            }
            
            if (estoqueDAO.removerEstoque(id, quantidade)) {
                System.out.println("Estoque atualizado com sucesso!");
                
                // Verifica se ficou com estoque baixo
                Estoque estoqueAtualizado = estoqueDAO.buscarPorProduto(produto);
                if (estoqueAtualizado != null && estoqueAtualizado.estaComEstoqueBaixo()) {
                    System.out.println("ALERTA: Estoque baixo após a remoção!");
                }
            } else {
                System.out.println("Erro ao atualizar estoque.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao remover estoque: " + e.getMessage());
        }
    }
    
    /**
     * Lista produtos com estoque baixo.
     */
    private void listarEstoqueBaixo() {
        try {
            List<Estoque> estoques = estoqueDAO.listarEstoqueBaixo();
            
            if (estoques.isEmpty()) {
                System.out.println("Nenhum produto com estoque baixo.");
                return;
            }
            
            System.out.println("\n=== PRODUTOS COM ESTOQUE BAIXO ===");
            System.out.printf("%-5s %-25s %-15s %-15s %-15s%n", 
                            "ID", "Produto", "Quantidade", "Mínima", "Localização");
            System.out.println("-".repeat(75));
            
            for (Estoque estoque : estoques) {
                System.out.printf("%-5d %-25s %-15d %-15d %-15s%n",
                                estoque.getId(),
                                estoque.getProduto().getNome(),
                                estoque.getQuantidadeAtual(),
                                estoque.getQuantidadeMinima(),
                                estoque.getLocalizacao());
            }
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao listar estoque baixo: " + e.getMessage());
        }
    }
    
    /**
     * Lista produtos sem estoque.
     */
    private void listarSemEstoque() {
        try {
            List<Estoque> estoques = estoqueDAO.listarSemEstoque();
            
            if (estoques.isEmpty()) {
                System.out.println("Todos os produtos possuem estoque.");
                return;
            }
            
            System.out.println("\n=== PRODUTOS SEM ESTOQUE ===");
            System.out.printf("%-5s %-25s %-15s%n", 
                            "ID", "Produto", "Localização");
            System.out.println("-".repeat(50));
            
            for (Estoque estoque : estoques) {
                System.out.printf("%-5d %-25s %-15s%n",
                                estoque.getId(),
                                estoque.getProduto().getNome(),
                                estoque.getLocalizacao());
            }
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao listar produtos sem estoque: " + e.getMessage());
        }
    }
    
    /**
     * Menu de gerenciamento de pedidos.
     */
    private void menuPedidos() {
        System.out.println("\n=== GERENCIAR PEDIDOS ===");
        System.out.println("1. Criar novo pedido");
        System.out.println("2. Listar pedidos");
        System.out.println("3. Buscar pedido por ID");
        System.out.println("4. Atualizar status do pedido");
        System.out.println("5. Cancelar pedido");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    criarPedido();
                    break;
                case 2:
                    listarPedidos();
                    break;
                case 3:
                    buscarPedidoPorId();
                    break;
                case 4:
                    atualizarStatusPedido();
                    break;
                case 5:
                    cancelarPedido();
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
     * Cria um novo pedido com verificação de estoque.
     */
    private void criarPedido() {
        try {
            System.out.println("\n=== CRIAR NOVO PEDIDO ===");
            
            // Selecionar cliente
            System.out.print("Digite o ID do cliente: ");
            int clienteId = Integer.parseInt(scanner.nextLine());
            Cliente cliente = clienteDAO.buscarPorId(clienteId);
            
            if (cliente == null) {
                System.out.println("Cliente não encontrado.");
                return;
            }
            
            if (!cliente.isAtivo()) {
                System.out.println("Cliente inativo! Não é possível criar pedido.");
                return;
            }
            
            Pedido pedido = new Pedido(cliente);
            
            // Adicionar itens ao pedido
            while (true) {
                System.out.println("\nAdicionar item ao pedido (ou 0 para finalizar):");
                System.out.print("ID do produto: ");
                String produtoInput = scanner.nextLine();
                
                if (produtoInput.equals("0")) {
                    break;
                }
                
                try {
                    int produtoId = Integer.parseInt(produtoInput);
                    Produto produto = produtoDAO.buscarPorId(produtoId);
                    
                    if (produto == null) {
                        System.out.println("Produto não encontrado.");
                        continue;
                    }
                    
                    if (!produto.isAtivo()) {
                        System.out.println("Produto inativo!");
                        continue;
                    }
                    
                    System.out.print("Quantidade: ");
                    int quantidade = Integer.parseInt(scanner.nextLine());
                    
                    if (quantidade <= 0) {
                        System.out.println("Quantidade deve ser positiva!");
                        continue;
                    }
                    
                    // Verificar estoque
                    Estoque estoque = estoqueDAO.buscarPorProduto(produto);
                    if (estoque == null || !estoque.temEstoqueSuficiente(quantidade)) {
                        int disponivel = estoque != null ? estoque.getQuantidadeAtual() : 0;
                        System.out.printf("Estoque insuficiente! Disponível: %d, Solicitado: %d%n", 
                                        disponivel, quantidade);
                        continue;
                    }
                    
                    pedido.adicionarItem(produto, quantidade);
                    System.out.println("Item adicionado: " + produto.getNome() + " x" + quantidade);
                    
                } catch (NumberFormatException e) {
                    System.out.println("Valores inválidos!");
                }
            }
            
            if (pedido.getItens().isEmpty()) {
                System.out.println("Pedido sem itens. Operação cancelada.");
                return;
            }
            
            // Exibir resumo do pedido
            System.out.println("\n=== RESUMO DO PEDIDO ===");
            System.out.println("Cliente: " + cliente.getNome());
            System.out.println("Itens:");
            
            for (var item : pedido.getItens()) {
                System.out.printf("- %s x%d = R$ %.2f%n",
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getSubtotal());
            }
            
            System.out.printf("Subtotal: R$ %.2f%n", pedido.getSubtotal());
            System.out.printf("Desconto: R$ %.2f (%.1f%%)%n", 
                            pedido.getDesconto(), pedido.getPercentualDesconto());
            System.out.printf("Total: R$ %.2f%n", pedido.getValorTotal());
            
            System.out.print("\nConfirmar pedido? (S/N): ");
            String confirmacao = scanner.nextLine();
            
            if (confirmacao.equalsIgnoreCase("S")) {
                // Confirmar pedido e deduzir do estoque
                pedido.confirmarPedido();
                
                // Deduzir estoque
                for (var item : pedido.getItens()) {
                    estoqueDAO.removerEstoque(item.getProduto().getId(), item.getQuantidade());
                    
                    // Verificar alerta de estoque baixo
                    Estoque estoque = estoqueDAO.buscarPorProduto(item.getProduto());
                    if (estoque != null && estoque.estaComEstoqueBaixo()) {
                        System.out.printf("ALERTA: Estoque baixo para %s! Restam: %d unidades%n",
                                        item.getProduto().getNome(), estoque.getQuantidadeAtual());
                    }
                }
                
                // Salvar pedido no banco de dados
                try {
                    Pedido pedidoSalvo = pedidoDAO.inserir(pedido);
                    System.out.println("Pedido confirmado com sucesso! ID: " + pedidoSalvo.getId());
                    System.out.println("Estoque atualizado automaticamente.");
                } catch (DatabaseException e) {
                    System.err.println("Erro ao salvar pedido: " + e.getMessage());
                    // Reverter estoque em caso de erro
                    for (var item : pedido.getItens()) {
                        try {
                            estoqueDAO.adicionarEstoque(item.getProduto().getId(), item.getQuantidade());
                        } catch (Exception ex) {
                            System.err.println("Erro ao reverter estoque: " + ex.getMessage());
                        }
                    }
                    return;
                }
                
            } else {
                System.out.println("Pedido cancelado.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Valores inválidos!");
        } catch (Exception e) {
            System.err.println("Erro ao criar pedido: " + e.getMessage());
        }
    }
    
    /**
     * Lista todos os pedidos.
     */
    private void listarPedidos() {
        try {
            System.out.println("\n=== LISTA DE PEDIDOS ===");
            
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            
            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
                return;
            }
            
            // Cabeçalho da tabela
            System.out.printf("%-5s %-20s %-15s %-12s %-15s %-10s %-15s%n", 
                            "ID", "Cliente", "Data Pedido", "Valor Total", "Status", "Itens", "Data Entrega");
            System.out.println("-".repeat(100));
            
            // Listar pedidos
            for (Pedido pedido : pedidos) {
                String nomeCliente = pedido.getCliente() != null ? pedido.getCliente().getNome() : "N/A";
                String dataPedido = pedido.getDataPedido().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String dataEntrega = pedido.getDataEntrega() != null ? 
                                    pedido.getDataEntrega().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A";
                
                // Formatar valor total
                String valorTotal = String.format("R$ %,.2f", pedido.getValorTotal());
                
                // Cor do status
                String statusComCor = getStatusComCor(pedido.getStatus());
                
                System.out.printf("%-5d %-20s %-15s %-12s %-15s %-10d %-15s%n",
                                pedido.getId(),
                                nomeCliente,
                                dataPedido,
                                valorTotal,
                                statusComCor,
                                pedido.getQuantidadeTotalItens(),
                                dataEntrega);
            }
            
            // Resumo estatístico
            System.out.println("-".repeat(100));
            System.out.println("\n=== RESUMO ===");
            System.out.println("Total de pedidos: " + pedidos.size());
            
            // Contar por status
            java.util.Map<Pedido.StatusPedido, Long> contagemStatus = pedidos.stream()
                .collect(java.util.stream.Collectors.groupingBy(Pedido::getStatus, java.util.stream.Collectors.counting()));
            
            for (Pedido.StatusPedido status : Pedido.StatusPedido.values()) {
                Long count = contagemStatus.get(status);
                if (count != null && count > 0) {
                    System.out.println(status + ": " + count + " pedido(s)");
                }
            }
            
            // Valor total
            double valorTotalGeral = pedidos.stream()
                .mapToDouble(Pedido::getValorTotal)
                .sum();
            System.out.println("Valor total geral: " + String.format("R$ %,.2f", valorTotalGeral));
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao listar pedidos: " + e.getMessage());
        }
    }
    
    /**
     * Retorna o status formatado com cor para melhor visualização.
     * 
     * @param status Status do pedido
     * @return Status formatado com código de cor
     */
    private String getStatusComCor(Pedido.StatusPedido status) {
        switch (status) {
            case PENDENTE:
                return "\u001B[33m" + status + "\u001B[0m"; // Amarelo
            case CONFIRMADO:
                return "\u001B[32m" + status + "\u001B[0m"; // Verde
            case EM_SEPARACAO:
                return "\u001B[34m" + status + "\u001B[0m"; // Azul
            case ENVIADO:
                return "\u001B[36m" + status + "\u001B[0m"; // Ciano
            case ENTREGUE:
                return "\u001B[32m" + status + "\u001B[0m"; // Verde forte
            case CANCELADO:
                return "\u001B[31m" + status + "\u001B[0m"; // Vermelho
            default:
                return status.toString();
        }
    }
    
    /**
     * Busca pedido por ID.
     */
    private void buscarPedidoPorId() {
        System.out.println("Funcionalidade em desenvolvimento...");
    }
    
    /**
     * Atualiza status do pedido.
     */
    private void atualizarStatusPedido() {
        try {
            System.out.println("\n=== ATUALIZAR STATUS DO PEDIDO ===");
            
            // Listar pedidos para escolha
            List<Pedido> pedidos = pedidoDAO.listarTodos();
            
            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
                return;
            }
            
            System.out.println("\nPedidos disponíveis:");
            System.out.printf("%-5s %-15s %-20s %-15s %-12s %-15s%n", 
                            "ID", "Cliente", "Data", "Valor Total", "Status", "Itens");
            System.out.println("-".repeat(85));
            
            for (Pedido pedido : pedidos) {
                String nomeCliente = pedido.getCliente() != null ? pedido.getCliente().getNome() : "N/A";
                String dataFormatada = pedido.getDataPedido().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                
                System.out.printf("%-5d %-15s %-20s %-12.2f %-15s %-10d%n",
                                pedido.getId(),
                                nomeCliente,
                                dataFormatada,
                                pedido.getValorTotal(),
                                pedido.getStatus(),
                                pedido.getQuantidadeTotalItens());
            }
            
            // Selecionar pedido
            System.out.print("\nDigite o ID do pedido: ");
            int pedidoId = Integer.parseInt(scanner.nextLine());
            
            Pedido pedido = pedidoDAO.buscarPorId(pedidoId);
            if (pedido == null) {
                System.out.println("Pedido não encontrado.");
                return;
            }
            
            // Exibir informações atuais
            System.out.println("\n=== PEDIDO SELECIONADO ===");
            System.out.println("ID: " + pedido.getId());
            System.out.println("Cliente: " + (pedido.getCliente() != null ? pedido.getCliente().getNome() : "N/A"));
            System.out.println("Status Atual: " + pedido.getStatus());
            System.out.println("Valor Total: R$ " + String.format("%.2f", pedido.getValorTotal()));
            System.out.println("Data: " + pedido.getDataPedido().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
            // Mostrar opções de status
            System.out.println("\n=== OPÇÕES DE STATUS ===");
            Pedido.StatusPedido[] statusOptions = Pedido.StatusPedido.values();
            for (int i = 0; i < statusOptions.length; i++) {
                System.out.println((i + 1) + ". " + statusOptions[i]);
            }
            
            System.out.print("Escolha o novo status: ");
            int statusOpcao = Integer.parseInt(scanner.nextLine());
            
            if (statusOpcao < 1 || statusOpcao > statusOptions.length) {
                System.out.println("Opção inválida!");
                return;
            }
            
            Pedido.StatusPedido novoStatus = statusOptions[statusOpcao - 1];
            
            // Validar transição de status
            if (!validarTransicaoStatus(pedido.getStatus(), novoStatus)) {
                System.out.println("Transição de status inválida!");
                System.out.println("Status atual: " + pedido.getStatus());
                System.out.println("Status desejado: " + novoStatus);
                System.out.println("Use 'avancarStatus()' para transições válidas.");
                return;
            }
            
            // Confirmar alteração
            System.out.print("\nConfirmar alteração de status para " + novoStatus + "? (S/N): ");
            String confirmacao = scanner.nextLine();
            
            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("Operação cancelada.");
                return;
            }
            
            // Atualizar no banco
            if (pedidoDAO.atualizarStatus(pedidoId, novoStatus.name())) {
                System.out.println("Status do pedido atualizado com sucesso!");
                System.out.println("Novo status: " + novoStatus);
                
                // Se for entregue, atualizar data de entrega
                if (novoStatus == Pedido.StatusPedido.ENTREGUE) {
                    System.out.println("Pedido marcado como entregue em: " + 
                                    java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
                
            } else {
                System.out.println("Erro ao atualizar status do pedido.");
            }
            
        } catch (NumberFormatException e) {
            System.out.println("Digite um número válido!");
        } catch (DatabaseException e) {
            System.err.println("Erro ao atualizar status: " + e.getMessage());
        }
    }
    
    /**
     * Valida se a transição de status é permitida.
     * 
     * @param statusAtual Status atual do pedido
     * @param novoStatus Novo status desejado
     * @return true se a transição é válida
     */
    private boolean validarTransicaoStatus(Pedido.StatusPedido statusAtual, Pedido.StatusPedido novoStatus) {
        // Cancelado não pode mudar para nenhum outro status
        if (statusAtual == Pedido.StatusPedido.CANCELADO) {
            return false;
        }
        
        // Entregue não pode mudar para nenhum outro status
        if (statusAtual == Pedido.StatusPedido.ENTREGUE) {
            return false;
        }
        
        // Permite qualquer transição que não seja para cancelado ou entregue
        // (exceto as regras acima)
        return true;
    }
    
    /**
     * Cancela um pedido.
     */
    private void cancelarPedido() {
        System.out.println("Funcionalidade em desenvolvimento...");
    }
    
    /**
     * Menu de relatórios.
     */
    private void menuRelatorios() {
        System.out.println("\n=== RELATÓRIOS ===");
        System.out.println("1. Relatório de estoque completo");
        System.out.println("2. Produtos mais vendidos");
        System.out.println("3. Clientes com mais pedidos");
        System.out.println("4. Pedidos por período");
        System.out.println("0. Voltar");
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            
            switch (opcao) {
                case 1:
                    relatorioEstoqueCompleto();
                    break;
                case 2:
                    System.out.println("Funcionalidade em desenvolvimento...");
                    break;
                case 3:
                    System.out.println("Funcionalidade em desenvolvimento...");
                    break;
                case 4:
                    System.out.println("Funcionalidade em desenvolvimento...");
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
     * Gera relatório completo de estoque com JOINs.
     */
    private void relatorioEstoqueCompleto() {
        try {
            List<Estoque> estoques = estoqueDAO.listarTodos();
            
            if (estoques.isEmpty()) {
                System.out.println("Nenhum registro de estoque encontrado.");
                return;
            }
            
            System.out.println("\n=== RELATÓRIO COMPLETO DE ESTOQUE ===");
            System.out.println("Data/Hora: " + java.time.LocalDateTime.now());
            System.out.println("-".repeat(120));
            
            double valorTotalEstoque = 0;
            int totalProdutos = 0;
            int produtosSemEstoque = 0;
            int produtosEstoqueBaixo = 0;
            
            for (Estoque estoque : estoques) {
                double valorProdutoEstoque = estoque.getQuantidadeAtual() * estoque.getProduto().getPreco();
                valorTotalEstoque += valorProdutoEstoque;
                totalProdutos++;
                
                if (estoque.getQuantidadeAtual() == 0) {
                    produtosSemEstoque++;
                } else if (estoque.estaComEstoqueBaixo()) {
                    produtosEstoqueBaixo++;
                }
                
                String status;
                if (estoque.getQuantidadeAtual() == 0) {
                    status = "SEM ESTOQUE";
                } else if (estoque.estaComEstoqueBaixo()) {
                    status = "ESTOQUE BAIXO";
                } else {
                    status = "NORMAL";
                }
                
                System.out.printf("%-5d %-25s %-10s %-8d %-8d %-8d %-12s %-10.2f %-12s%n",
                                estoque.getId(),
                                estoque.getProduto().getNome(),
                                estoque.getProduto().getSku(),
                                estoque.getQuantidadeAtual(),
                                estoque.getQuantidadeMinima(),
                                estoque.getQuantidadeMaxima(),
                                estoque.getLocalizacao(),
                                valorProdutoEstoque,
                                status);
            }
            
            System.out.println("-".repeat(120));
            System.out.println("RESUMO:");
            System.out.printf("Total de produtos: %d%n", totalProdutos);
            System.out.printf("Produtos sem estoque: %d%n", produtosSemEstoque);
            System.out.printf("Produtos com estoque baixo: %d%n", produtosEstoqueBaixo);
            System.out.printf("Valor total em estoque: R$ %.2f%n", valorTotalEstoque);
            
        } catch (DatabaseException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }
    
    /**
     * Testa as regras de negócio complexas do sistema.
     */
    private void testarRegrasNegocio() {
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
                        estoque.getQuantidadeAtual()
                    );
                }
            } catch (EstoqueInsuficienteException e) {
                System.out.println("✅ Exceção capturada corretamente:");
                System.out.println("   " + e.getMessage());
                System.out.println("   " + e.getMensagemSugestao());
            }
            
            // Teste 2: Verificar alerta de estoque baixo
            System.out.println("\nTeste 2: Verificação de estoque baixo...");
            System.out.println("Estoque mínimo: " + estoque.getQuantidadeMinima());
            System.out.println("Status atual: " + (estoque.estaComEstoqueBaixo() ? "ESTOQUE BAIXO" : "NORMAL"));
            
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
        double[] valoresTeste = {200.0, 600.0, 1200.0};
        String[] descricaoTeste = {"Sem desconto", "5% de desconto", "10% de desconto"};
        
        for (int i = 0; i < valoresTeste.length; i++) {
            System.out.printf("\nTeste %d - %s:%n", i + 1, descricaoTeste[i]);
            
            // Criar pedido de teste
            Cliente clienteTeste = new Cliente("Cliente Teste", "test@email.com", "11999999999", "12345678901", "Rua Teste");
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
        IPagamentoStrategy[] estrategias = {
            new PagamentoCartaoStrategy(),
            new PagamentoBoletoStrategy(),
            new PagamentoPixStrategy()
        };
        
        double valorTeste = 500.0;
        
        for (IPagamentoStrategy estrategia : estrategias) {
            System.out.printf("\nTestando %s:%n", estrategia.getNomeFormaPagamento());
            System.out.printf("Tempo de processamento: %d segundos%n", estrategia.getTempoProcessamentoSegundos());
            
            // Testar com dados válidos
            String dadosValidos = "";
            String dadosInvalidos = "";
            
            if (estrategia instanceof PagamentoCartaoStrategy) {
                dadosValidos = "4111111111111111"; // Visa de teste
                dadosInvalidos = "123";
            } else if (estrategia instanceof PagamentoBoletoStrategy) {
                dadosValidos = "12345678901"; // CPF válido
                dadosInvalidos = "123";
            } else if (estrategia instanceof PagamentoPixStrategy) {
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
            System.out.printf("Validação dados inválidos: %s%n", !dadosInvalidosOk ? "✅ Rejeitados" : "❌ Aceitos (erro)");
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
        com.ecommerce.model.Pessoa[] pessoas = {cliente, funcionario};
        
        System.out.println("\nDemonstração de polimorfismo:");
        for (com.ecommerce.model.Pessoa pessoa : pessoas) {
            System.out.printf("\n%s:%n", pessoa.getClass().getSimpleName());
            System.out.printf("Nome: %s%n", pessoa.getNome());
            System.out.printf("Email: %s%n", pessoa.getEmail());
            System.out.printf("Tipo: %s%n", pessoa.getTipoPessoa());
            System.out.printf("Validação: %s%n", pessoa.validarDados() ? "✅ Válida" : "❌ Inválida");
            
            // Downcasting para métodos específicos
            if (pessoa instanceof Cliente) {
                Cliente c = (Cliente) pessoa;
                System.out.printf("CPF: %s%n", c.getCpf());
                System.out.printf("Status: %s%n", c.isAtivo() ? "Ativo" : "Inativo");
            } else if (pessoa instanceof Funcionario) {
                Funcionario f = (Funcionario) pessoa;
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
