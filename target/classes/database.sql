-- Script SQL para criação do banco de dados do sistema de e-commerce
-- Autor: Sistema E-commerce
-- Versão: 1.0

-- Criação do banco de dados
-- CREATE DATABASE ecommerce_db;
-- \c ecommerce_db;

-- Limpeza de tabelas existentes (para desenvolvimento)
DROP TABLE IF EXISTS pedido_produtos CASCADE;
DROP TABLE IF EXISTS produto_categorias CASCADE;
DROP TABLE IF EXISTS estoque CASCADE;
DROP TABLE IF EXISTS item_pedido CASCADE;
DROP TABLE IF EXISTS pedido CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;
DROP TABLE IF EXISTS funcionario CASCADE;
DROP TABLE IF EXISTS cliente CASCADE;

-- Tabela de Clientes (herança de Pessoa)
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    cpf VARCHAR(11) UNIQUE NOT NULL,
    endereco TEXT,
    cidade VARCHAR(50),
    estado VARCHAR(2),
    cep VARCHAR(9),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Funcionários (herança de Pessoa)
CREATE TABLE funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    telefone VARCHAR(20),
    matricula VARCHAR(20) UNIQUE NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    salario DECIMAL(10,2) NOT NULL,
    departamento VARCHAR(50),
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Categorias
CREATE TABLE categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) UNIQUE NOT NULL,
    descricao TEXT,
    ativa BOOLEAN DEFAULT TRUE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Produtos
CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    sku VARCHAR(50) UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Estoque (relacionamento 1:1 com Produto)
CREATE TABLE estoque (
    id SERIAL PRIMARY KEY,
    produto_id INTEGER UNIQUE NOT NULL REFERENCES produto(id) ON DELETE CASCADE,
    quantidade_atual INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_atual >= 0),
    quantidade_minima INTEGER NOT NULL DEFAULT 10 CHECK (quantidade_minima >= 0),
    quantidade_maxima INTEGER NOT NULL DEFAULT 1000 CHECK (quantidade_maxima > quantidade_minima),
    localizacao VARCHAR(50),
    data_ultima_atualizacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Pedidos
CREATE TABLE pedido (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL REFERENCES cliente(id),
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_entrega TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE' 
        CHECK (status IN ('PENDENTE', 'CONFIRMADO', 'EM_SEPARACAO', 'ENVIADO', 'ENTREGUE', 'CANCELADO')),
    subtotal DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (subtotal >= 0),
    desconto DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (desconto >= 0),
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0 CHECK (valor_total >= 0),
    observacoes TEXT
);

-- Tabela de Itens do Pedido (relacionamento N:N entre Pedido e Produto)
CREATE TABLE item_pedido (
    id SERIAL PRIMARY KEY,
    pedido_id INTEGER NOT NULL REFERENCES pedido(id) ON DELETE CASCADE,
    produto_id INTEGER NOT NULL REFERENCES produto(id),
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),
    UNIQUE(pedido_id, produto_id)
);

-- Tabela de Relacionamento N:N entre Produto e Categoria
CREATE TABLE produto_categorias (
    produto_id INTEGER NOT NULL REFERENCES produto(id) ON DELETE CASCADE,
    categoria_id INTEGER NOT NULL REFERENCES categoria(id) ON DELETE CASCADE,
    PRIMARY KEY (produto_id, categoria_id)
);

-- Índices para melhor performance
CREATE INDEX idx_cliente_email ON cliente(email);
CREATE INDEX idx_cliente_cpf ON cliente(cpf);
CREATE INDEX idx_funcionario_email ON funcionario(email);
CREATE INDEX idx_funcionario_matricula ON funcionario(matricula);
CREATE INDEX idx_produto_sku ON produto(sku);
CREATE INDEX idx_produto_nome ON produto(nome);
CREATE INDEX idx_categoria_nome ON categoria(nome);
CREATE INDEX idx_estoque_produto ON estoque(produto_id);
CREATE INDEX idx_pedido_cliente ON pedido(cliente_id);
CREATE INDEX idx_pedido_data ON pedido(data_pedido);
CREATE INDEX idx_pedido_status ON pedido(status);
CREATE INDEX idx_item_pedido_pedido ON item_pedido(pedido_id);
CREATE INDEX idx_item_pedido_produto ON item_pedido(produto_id);

-- Triggers para atualização automática de timestamps
CREATE OR REPLACE FUNCTION atualizar_timestamp_estoque()
RETURNS TRIGGER AS $$
BEGIN
    NEW.data_ultima_atualizacao = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_estoque
    BEFORE UPDATE ON estoque
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_timestamp_estoque();

-- Triggers para validação de estoque
CREATE OR REPLACE FUNCTION validar_estoque_negativo()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.quantidade_atual < 0 THEN
        RAISE EXCEPTION 'Estoque não pode ser negativo';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_validar_estoque
    BEFORE INSERT OR UPDATE ON estoque
    FOR EACH ROW
    EXECUTE FUNCTION validar_estoque_negativo();

-- Triggers para cálculo automático de subtotal em item_pedido
CREATE OR REPLACE FUNCTION calcular_subtotal_item()
RETURNS TRIGGER AS $$
BEGIN
    NEW.subtotal = NEW.quantidade * NEW.preco_unitario;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_calcular_subtotal_item
    BEFORE INSERT OR UPDATE ON item_pedido
    FOR EACH ROW
    EXECUTE FUNCTION calcular_subtotal_item();

-- Triggers para atualização automática dos totais do pedido
CREATE OR REPLACE FUNCTION atualizar_totais_pedido()
RETURNS TRIGGER AS $$
DECLARE
    v_subtotal DECIMAL;
    v_desconto DECIMAL;
    v_total DECIMAL;
BEGIN
    -- Calcula subtotal
    SELECT COALESCE(SUM(subtotal), 0) INTO v_subtotal
    FROM item_pedido
    WHERE pedido_id = COALESCE(NEW.pedido_id, OLD.pedido_id);
    
    -- Calcula desconto progressivo
    IF v_subtotal > 1000 THEN
        v_desconto := v_subtotal * 0.10;
    ELSIF v_subtotal > 500 THEN
        v_desconto := v_subtotal * 0.05;
    ELSE
        v_desconto := 0;
    END IF;
    
    -- Calcula total
    v_total := v_subtotal - v_desconto;
    
    -- Atualiza o pedido
    UPDATE pedido 
    SET subtotal = v_subtotal,
        desconto = v_desconto,
        valor_total = v_total
    WHERE id = COALESCE(NEW.pedido_id, OLD.pedido_id);
    
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_atualizar_totais_pedido_insert
    AFTER INSERT ON item_pedido
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_totais_pedido();

CREATE TRIGGER trigger_atualizar_totais_pedido_update
    AFTER UPDATE ON item_pedido
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_totais_pedido();

CREATE TRIGGER trigger_atualizar_totais_pedido_delete
    AFTER DELETE ON item_pedido
    FOR EACH ROW
    EXECUTE FUNCTION atualizar_totais_pedido();

-- Inserção de dados para teste
INSERT INTO categoria (nome, descricao) VALUES
('Eletrônicos', 'Produtos eletrônicos em geral'),
('Roupas', 'Vestuário e acessórios'),
('Alimentos', 'Produtos alimentícios'),
('Livros', 'Livros e materiais de leitura'),
('Informática', 'Computadores e periféricos');

INSERT INTO cliente (nome, email, telefone, cpf, endereco, cidade, estado, cep) VALUES
('João Silva', 'joao@email.com', '11999999999', '12345678901', 'Rua A, 123', 'São Paulo', 'SP', '01234567'),
('Maria Santos', 'maria@email.com', '21988888888', '98765432100', 'Av B, 456', 'Rio de Janeiro', 'RJ', '87654321'),
('Carlos Oliveira', 'carlos@email.com', '31977777777', '11122233344', 'Rua C, 789', 'Belo Horizonte', 'MG', '34567890');

INSERT INTO funcionario (nome, email, telefone, matricula, cargo, salario, departamento) VALUES
('Ana Gerente', 'ana@empresa.com', '11966666666', 'MAT001', 'Gerente', 8000.00, 'Administração'),
('Bruno Vendedor', 'bruno@empresa.com', '11955555555', 'MAT002', 'Vendedor', 3500.00, 'Vendas'),
('Carla Estoque', 'carla@empresa.com', '11944444444', 'MAT003', 'Analista de Estoque', 4000.00, 'Logística');

INSERT INTO produto (nome, descricao, preco, sku) VALUES
('Smartphone Galaxy', 'Smartphone 128GB', 2500.00, 'SKU001'),
('Notebook Dell', 'Notebook Core i5 8GB', 3500.00, 'SKU002'),
('Camiseta Polo', 'Camiseta azul tamanho M', 89.90, 'SKU003'),
('Livro POO', 'Programação Orientada a Objetos', 120.00, 'SKU004'),
('Mouse Wireless', 'Mouse sem fio USB', 45.00, 'SKU005');

-- Associar produtos a categorias
INSERT INTO produto_categorias (produto_id, categoria_id) VALUES
(1, 1), -- Smartphone -> Eletrônicos
(1, 5), -- Smartphone -> Informática
(2, 5), -- Notebook -> Informática
(3, 2), -- Camiseta -> Roupas
(4, 4), -- Livro -> Livros
(5, 5); -- Mouse -> Informática

-- Inserir dados no estoque
INSERT INTO estoque (produto_id, quantidade_atual, quantidade_minima, quantidade_maxima, localizacao) VALUES
(1, 50, 10, 200, 'A1-B2'),
(2, 25, 5, 100, 'A1-C3'),
(3, 100, 20, 500, 'B2-D1'),
(4, 30, 10, 150, 'C1-A2'),
(5, 75, 15, 300, 'D3-B1');

-- Visualização dos dados inseridos
SELECT '=== CLIENTES ===' as info;
SELECT * FROM cliente;

SELECT '=== FUNCIONÁRIOS ===' as info;
SELECT * FROM funcionario;

SELECT '=== CATEGORIAS ===' as info;
SELECT * FROM categoria;

SELECT '=== PRODUTOS ===' as info;
SELECT * FROM produto;

SELECT '=== ESTOQUE ===' as info;
SELECT e.*, p.nome as produto_nome FROM estoque e JOIN produto p ON e.produto_id = p.id;

SELECT '=== PRODUTOS POR CATEGORIA ===' as info;
SELECT p.nome as produto, c.nome as categoria 
FROM produto p 
JOIN produto_categorias pc ON p.id = pc.produto_id 
JOIN categoria c ON pc.categoria_id = c.id;

-- Exemplo de consultas JOIN complexas
SELECT '=== RELATÓRIO DE ESTOQUE COM PRODUTOS E CATEGORIAS ===' as info;
SELECT 
    p.nome as produto,
    p.preco,
    e.quantidade_atual,
    e.quantidade_minima,
    CASE 
        WHEN e.quantidade_atual <= e.quantidade_minima THEN 'ESTOQUE BAIXO'
        WHEN e.quantidade_atual = 0 THEN 'SEM ESTOQUE'
        ELSE 'ESTOQUE NORMAL'
    END as status_estoque,
    STRING_AGG(c.nome, ', ') as categorias
FROM produto p
JOIN estoque e ON p.id = e.produto_id
LEFT JOIN produto_categorias pc ON p.id = pc.produto_id
LEFT JOIN categoria c ON pc.categoria_id = c.id
GROUP BY p.id, e.quantidade_atual, e.quantidade_minima
ORDER BY e.quantidade_atual ASC;

SELECT '=== BANCO DE DADOS CRIADO COM SUCESSO ===' as info;
