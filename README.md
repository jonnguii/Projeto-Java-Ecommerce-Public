# Sistema de E-commerce Java + PostgreSQL

Sistema completo de e-commerce desenvolvido em Java com integração ao banco de dados PostgreSQL, aplicando conceitos avançados de Programação Orientada a Objetos e boas práticas de desenvolvimento.

## 🎯 Objetivos do Projeto

Este projeto foi desenvolvido como trabalho interdisciplinar para demonstrar a integração entre:

- **Programação Orientada a Objetos (OOP)**: Herança, polimorfismo, encapsulamento e abstração
- **Banco de Dados Relacional**: Modelagem com PostgreSQL e relacionamentos complexos
- **JDBC**: Integração Java com PostgreSQL
- **Regras de Negócio Complexas**: Controle de estoque, cálculos de descontos progressivos e gestão de pedidos

## 📋 Requisitos Implementados

### ✅ OOP - Quatro Pilares
- **Herança**: Classe abstrata `Pessoa` com subclasses `Cliente` e `Funcionario`
- **Polimorfismo**: Interface `IPagamentoStrategy` com múltiplas implementações
- **Encapsulamento**: Todos os atributos privados com getters/setters
- **Abstração**: Classes abstratas e interfaces definindo contratos

### ✅ Estrutura do Projeto
- **6+ Classes Principais**: Produto, Categoria, Cliente, Pedido, Estoque, Funcionario, ItemPedido
- **Herança**: `Pessoa` → `Cliente` e `Funcionario`
- **Relacionamentos N:N**: Produto-Categoria, Pedido-Produto

### ✅ Banco de Dados
- **8 Tabelas** com relacionamentos complexos
- **3 Relacionamentos N:N**
- **Índices** para performance

### ✅ Funcionalidades Implementadas
- **CRUD Completo** para todas as entidades
- **Controle de Estoque** com validações e alertas avançados
- **Cálculo de Descontos Progressivos** (5% acima de R$500, 10% acima de R$1000)
- **Sistema de Pagamentos** com polimorfismo
- **Gestão Completa de Pedidos**: Criação, listagem, busca, atualização de status, cancelamento
- **Status de Pedidos**: PENDENTE, CONFIRMADO, EM_SEPARACAO, ENVIADO, ENTREGUE, CANCELADO
- **Validação de Transições de Status**: Regras de negócio para mudanças de estado
- **Relatórios com JOINs** complexos
- **Tratamento de Exceções** personalizado
- **Interface Interativa** via console
- **Localização de Estoque**: Cadastro e gerenciamento físico
- **Status Inteligente de Estoque**: SEM ESTOQUE, ABAIXO, NORMAL, ACIMA

## 🏗️ Estrutura do Projeto

```
ecommerce/
├── src/main/java/com/ecommerce/
│   ├── Main.java                          # Ponto de entrada
│   ├── MainDemo.java                      # Demonstração do sistema
│   ├── model/                             # Classes de modelo
│   │   ├── Pessoa.java                    # Classe abstrata
│   │   ├── Cliente.java                   # Herda de Pessoa
│   │   ├── Funcionario.java               # Herda de Pessoa
│   │   ├── Produto.java                   # Entidade principal
│   │   ├── Categoria.java                 # Categoria de produtos
│   │   ├── Estoque.java                   # Controle de estoque
│   │   ├── Pedido.java                    # Pedidos de clientes
│   │   └── ItemPedido.java                # Itens do pedido (N:N)
│   ├── dao/                               # Data Access Objects
│   │   ├── ClienteDAO.java
│   │   ├── ProdutoDAO.java
│   │   ├── EstoqueDAO.java
│   │   ├── CategoriaDAO.java
│   │   └── PedidoDAO.java
│   ├── service/                           # Camada de serviço
│   │   ├── ClienteService.java            # Serviço de clientes
│   │   ├── ProdutoService.java            # Serviço de produtos
│   │   ├── PedidoService.java             # Serviço de pedidos
│   │   ├── EstoqueService.java            # Serviço de estoque
│   │   ├── RelatorioService.java          # Serviço de relatórios
│   │   ├── IPagamentoStrategy.java        # Interface de pagamento
│   │   ├── PagamentoCartaoStrategy.java
│   │   ├── PagamentoBoletoStrategy.java
│   │   └── PagamentoPixStrategy.java
│   ├── view/                              # Interface do usuário (console)
│   │   ├── MenuPrincipal.java             # Menu principal
│   │   ├── MenuCliente.java               # Menu de clientes
│   │   ├── MenuProduto.java               # Menu de produtos
│   │   ├── MenuEstoque.java               # Menu de estoque
│   │   ├── MenuPedido.java                # Menu de pedidos
│   │   ├── MenuRelatorio.java             # Menu de relatórios
│   │   └── MenuRN.java                    # Menu regras de negócio
│   ├── enums/                             # Enumerações
│   │   ├── StatusPedido.java              # Status dos pedidos
│   │   └── StatusAtividade.java           # Status de atividade (Ativo/Inativo)
│   ├── util/                              # Utilitários
│   │   ├── DatabaseConnection.java        # Conexão com BD
│   │   └── InputUtils.java                # Validação de dados em tempo real
│   └── exception/                         # Exceções personalizadas
│       ├── EcommerceException.java
│       ├── DatabaseException.java
│       └── EstoqueInsuficienteException.java
├── src/main/resources/
│   ├── database.sql                       # Script SQL completo
│   └── config.properties                  # Configurações do BD
├── iniciar.bat                            # Script para rodar o sistema
├── pom.xml                                # Maven dependencies
└── README.md                              # Este arquivo
```

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Maven** (Gerenciamento de dependências)
- **PostgreSQL** (Banco de dados)
- **JDBC** (Conectividade Java-BD)

## 📊 Modelo de Dados

### Tabelas Principais
1. **cliente** - Dados dos clientes
2. **funcionario** - Dados dos funcionários
3. **produto** - Cadastro de produtos
4. **categoria** - Categorias de produtos
5. **estoque** - Controle de estoque (1:1 com produto)
6. **pedido** - Pedidos dos clientes
7. **item_pedido** - Itens dos pedidos (N:N)
8. **produto_categorias** - Relacionamento N:N produto-categoria

### Relacionamentos
- **N:N**: Produto ↔ Categoria
- **N:N**: Pedido ↔ Produto (via ItemPedido)
- **1:1**: Produto ↔ Estoque
- **N:1**: Pedido ↔ Cliente

## 🚀 Como Executar

### Pré-requisitos
1. **Java 17+** instalado
2. **PostgreSQL** rodando
3. **Maven** instalado (opcional)

### 🎮 Formas de Executar:


#### **Opção 1: Manual via Terminal**
#### 1. Configurar o Banco de Dados
```bash
# - Criar banco: ecommerce_db
# - Editar src/main/resources/config.properties
# - Executar: src/main/resources/database.sql
```

#### 2. Compilar:
```bash
javac -cp "lib/*;src/main/java" src/main/java/com/ecommerce/Main.java -d target/classes
```

#### 3. Executar:
```bash
java -cp "target/classes;lib/*" com.ecommerce.Main
```

> Caso haja um erro onde o compilador nao encontre a config properties, rode:
```bash
cp src/main/resources/config.properties target/classes/
```

#### **Opção 2: Script Automático (Recomendado)**
Basta clicar duas vezes no arquivo `iniciar.bat` na raiz do projeto, ou rodar no terminal:
```bash
.\iniciar.bat
```
O script compila e executa o sistema automaticamente, sem precisar do Maven instalado.


## 🎮 Funcionalidades do Sistema

### Menu Principal
1. **Gerenciar Clientes** - CRUD completo de clientes
2. **Gerenciar Produtos** - CRUD de produtos com validação de SKU e localização
3. **Gerenciar Estoque** - Controle de estoque com alertas inteligentes
4. **Gerenciar Pedidos** - Sistema completo de gestão de pedidos
5. **Relatórios** - Relatórios com JOINs complexos
6. **Testar Regras de Negócio** - Demonstração das funcionalidades

### 📦 Gerenciar Produtos
- **Cadastrar Produto**: Nome, descrição, preço, SKU, localização do estoque
- **Listar Produtos**: Visualização completa com informações
- **Buscar por Nome**: Busca parcial de produtos
- **Buscar por ID**: Encontrar produto específico
- **Atualizar Produto**: Modificar dados existentes
- **Excluir Produto**: Remoção com validações

### 📦 Gerenciar Estoque
- **Listar Todo o Estoque**: Tabela completa com status inteligente
  - **SEM ESTOQUE**: Quantidade = 0
  - **ABAIXO**: Quantidade < Mínima
  - **NORMAL**: Mínima ≤ Quantidade ≤ Máxima
  - **ACIMA**: Quantidade > Máxima
- **Verificar Estoque de Produto**: Consulta individual detalhada
- **Adicionar Produtos ao Estoque**: Incremento com validações
- **Remover Produtos do Estoque**: Dedução com controle
- **Listar Produtos com Estoque Baixo**: Alertas automáticos
- **Listar Produtos Sem Estoque**: Gestão de rupturas

### 📦 Gerenciar Pedidos
- **Criar Novo Pedido**: Seleção de cliente, produtos e quantidades
- **Listar Pedidos**: Visualização completa com cores por status
- **Buscar Pedido por ID**: Consulta individual detalhada
- **Atualizar Status do Pedido**: 
  - **Status Disponíveis**: PENDENTE, CONFIRMADO, EM_SEPARACAO, ENVIADO, ENTREGUE, CANCELADO
  - **Validação de Transições**: Regras de negócio aplicadas
  - **Confirmação do Usuário**: Segurança nas alterações
- **Cancelar Pedido**: Anulação com reposição de estoque

### Regras de Negócio Implementadas

#### 📦 Controle de Estoque Avançado
- **Validação de Estoque**: Impede venda sem estoque disponível
- **Alerta de Estoque Baixo**: Notifica quando quantidade < mínima
- **Status Inteligente**: Classificação automática (ABAIXO, NORMAL, ACIMA)
- **Dedução Automática**: Estoque deduzido ao confirmar pedido
- **Reposição Automática**: Estoque restaurado ao cancelar pedido
- **Localização Física**: Cadastro e gerenciamento de posições
- **Relatório de Estoque**: Status completo com valor total

#### 🛒 Gestão de Pedidos
- **Criação com Validação**: Verifica disponibilidade de estoque
- **Cálculo Automático**: Subtotal, descontos e valor total
- **Status com Cores**: Interface visual diferenciada
- **Transições Controladas**: Regras para mudanças de estado
- **Histórico Completo**: Data de pedido e entrega
- **Cancelamento Seguro**: Reversão automática de estoque

#### 💰 Descontos Progressivos
- **Sem desconto**: Até R$ 500
- **5% de desconto**: Acima de R$ 500
- **10% de desconto**: Acima de R$ 1000



## 📈 Relatórios Implementados

### Relatório de Estoque Completo
- JOIN entre Estoque, Produto e Categoria
- Cálculo do valor total em estoque
- Status inteligente de cada produto
- Resumo estatístico detalhado

### Relatório de Pedidos
- Listagem completa com cores por status
- Informações de cliente e valores
- Data de pedido e entrega
- Quantidade de itens

## 🔧 Configuração

### Banco de Dados (config.properties)
```properties
db.url=jdbc:postgresql://localhost:5432/ecommerce_db
db.username=xxxxxx
db.password=xxxxxx
db.driver=org.postgresql.Driver
```

## 📝 Exemplos de Uso

### Cadastrando um Produto
```java
Produto produto = new Produto("Smartphone", "128GB", 2500.00, "SKU001");
ProdutoDAO dao = new ProdutoDAO();
Produto inserido = dao.inserir(produto);

// Estoque criado automaticamente com localização
Estoque estoque = new Estoque(inserido, 50);
estoque.setLocalizacao("Galeria A");
new EstoqueDAO().inserir(estoque);
```

### Criando um Pedido
```java
Pedido pedido = new Pedido(cliente);
pedido.adicionarItem(produto, 2);

// Desconto aplicado automaticamente
// Estoque validado e deduzido
pedido.confirmarPedido();
```

### Atualizando Status de Pedido
```java
PedidoDAO pedidoDAO = new PedidoDAO();
boolean sucesso = pedidoDAO.atualizarStatus(1, StatusPedido.CONFIRMADO);
```

### Usando Polimorfismo
```java
IPagamentoStrategy pagamento = new PagamentoCartaoStrategy();
if (pagamento.validarDadosPagamento("4111111111111111")) {
    pagamento.processarPagamento(500.00, "4111111111111111");
}
```

## 🎯 Destaques Técnicos

### Boas Práticas e UX
- **Validação em Tempo Real**: Classe `InputUtils` força loops de inserção (com Regex) até o usuário acertar formatos de e-mail, CPF, CEP, UF, etc.
- **Tratamento Amigável de Erros**: O sistema captura violações de restrição do PostgreSQL (ex: CPF duplicado) e traduz para mensagens compreensíveis ("O CPF informado já está cadastrado").
- **Enumerações Padronizadas**: Uso de `StatusAtividade` e `StatusPedido` no lugar de strings ou booleans.
- **Javadoc** completo em todas as classes/métodos públicos



