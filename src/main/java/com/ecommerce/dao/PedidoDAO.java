package com.ecommerce.dao;

import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.*;
import com.ecommerce.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para manipulação de pedidos no banco de dados.
 * Implementa CRUD completo para a entidade Pedido e relacionamentos.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class PedidoDAO {
    
    private final DatabaseConnection dbConnection;
    private final ClienteDAO clienteDAO;
    
    /**
     * Construtor.
     */
    public PedidoDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
        this.clienteDAO = new ClienteDAO();
    }
    
    /**
     * Insere um novo pedido no banco de dados.
     * 
     * @param pedido Pedido a ser inserido
     * @return Pedido com ID gerado
     * @throws DatabaseException em caso de erro
     */
    public Pedido inserir(Pedido pedido) throws DatabaseException {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Inserir pedido
            String sqlPedido = "INSERT INTO pedido (cliente_id, data_pedido, status, subtotal, desconto, valor_total, observacoes) " +
                              "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id";
            
            int pedidoId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                stmt.setInt(1, pedido.getCliente().getId());
                stmt.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido()));
                stmt.setString(3, pedido.getStatus().name());
                stmt.setDouble(4, pedido.getSubtotal());
                stmt.setDouble(5, pedido.getDesconto());
                stmt.setDouble(6, pedido.getValorTotal());
                stmt.setString(7, pedido.getObservacoes());
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        pedidoId = rs.getInt("id");
                        pedido.setId(pedidoId);
                    } else {
                        throw new DatabaseException("Não foi possível inserir o pedido");
                    }
                }
            }
            
            // Inserir itens do pedido
            String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario, subtotal) " +
                           "VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    stmt.setInt(1, pedidoId);
                    stmt.setInt(2, item.getProduto().getId());
                    stmt.setInt(3, item.getQuantidade());
                    stmt.setDouble(4, item.getPrecoUnitario());
                    stmt.setDouble(5, item.getSubtotal());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            
            conn.commit();
            return pedido;
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Erro ao fazer rollback", ex);
                }
            }
            throw DatabaseException.insertError("pedido", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Log erro
                }
            }
        }
    }
    
    /**
     * Busca um pedido pelo ID.
     * 
     * @param id ID do pedido
     * @return Pedido encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Pedido buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pedido pedido = extrairPedidoDoResultSet(rs);
                    carregarItensDoPedido(pedido);
                    return pedido;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("pedido", e);
        }
        
        return null;
    }
    
    /**
     * Lista todos os pedidos.
     * 
     * @return Lista de pedidos
     * @throws DatabaseException em caso de erro
     */
    public List<Pedido> listarTodos() throws DatabaseException {
        String sql = "SELECT * FROM pedido ORDER BY data_pedido DESC";
        List<Pedido> pedidos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Pedido pedido = extrairPedidoDoResultSet(rs);
                carregarItensDoPedido(pedido);
                pedidos.add(pedido);
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("pedido", e);
        }
        
        return pedidos;
    }
    
    /**
     * Lista pedidos por cliente.
     * 
     * @param clienteId ID do cliente
     * @return Lista de pedidos do cliente
     * @throws DatabaseException em caso de erro
     */
    public List<Pedido> listarPorCliente(int clienteId) throws DatabaseException {
        String sql = "SELECT * FROM pedido WHERE cliente_id = ? ORDER BY data_pedido DESC";
        List<Pedido> pedidos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, clienteId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = extrairPedidoDoResultSet(rs);
                    carregarItensDoPedido(pedido);
                    pedidos.add(pedido);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("pedido", e);
        }
        
        return pedidos;
    }
    
    /**
     * Lista pedidos por status.
     * 
     * @param status Status do pedido
     * @return Lista de pedidos com o status especificado
     * @throws DatabaseException em caso de erro
     */
    public List<Pedido> listarPorStatus(StatusPedido status) throws DatabaseException {
        String sql = "SELECT * FROM pedido WHERE status = ? ORDER BY data_pedido DESC";
        List<Pedido> pedidos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = extrairPedidoDoResultSet(rs);
                    carregarItensDoPedido(pedido);
                    pedidos.add(pedido);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("pedido", e);
        }
        
        return pedidos;
    }
    
    /**
     * Lista pedidos por período.
     * 
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista de pedidos no período
     * @throws DatabaseException em caso de erro
     */
    public List<Pedido> listarPorPeriodo(LocalDateTime dataInicio, LocalDateTime dataFim) throws DatabaseException {
        String sql = "SELECT * FROM pedido WHERE data_pedido BETWEEN ? AND ? ORDER BY data_pedido DESC";
        List<Pedido> pedidos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(dataInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(dataFim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pedido pedido = extrairPedidoDoResultSet(rs);
                    carregarItensDoPedido(pedido);
                    pedidos.add(pedido);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("pedido", e);
        }
        
        return pedidos;
    }
    
    /**
     * Atualiza um pedido existente.
     * 
     * @param pedido Pedido a ser atualizado
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean atualizar(Pedido pedido) throws DatabaseException {
        String sql = "UPDATE pedido SET cliente_id = ?, data_pedido = ?, status = ?, " +
                     "subtotal = ?, desconto = ?, valor_total = ?, observacoes = ?, " +
                     "data_entrega = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pedido.getCliente().getId());
            stmt.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido()));
            stmt.setString(3, pedido.getStatus().name());
            stmt.setDouble(4, pedido.getSubtotal());
            stmt.setDouble(5, pedido.getDesconto());
            stmt.setDouble(6, pedido.getValorTotal());
            stmt.setString(7, pedido.getObservacoes());
            
            if (pedido.getDataEntrega() != null) {
                stmt.setTimestamp(8, Timestamp.valueOf(pedido.getDataEntrega()));
            } else {
                stmt.setNull(8, Types.TIMESTAMP);
            }
            
            stmt.setInt(9, pedido.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("pedido", e);
        }
    }
    
    /**
     * Atualiza o status de um pedido.
     * 
     * @param id ID do pedido
     * @param status Novo status
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean atualizarStatus(int id, StatusPedido status) throws DatabaseException {
        String sql = "UPDATE pedido SET status = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status.name());
            stmt.setInt(2, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("pedido", e);
        }
    }
    
    /**
     * Exclui um pedido pelo ID.
     * 
     * @param id ID do pedido a ser excluído
     * @return true se excluído com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean excluir(int id) throws DatabaseException {
        Connection conn = null;
        try {
            conn = dbConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Excluir itens do pedido
            String sqlItens = "DELETE FROM item_pedido WHERE pedido_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlItens)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
            
            // Excluir pedido
            String sqlPedido = "DELETE FROM pedido WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlPedido)) {
                stmt.setInt(1, id);
                int linhasAfetadas = stmt.executeUpdate();
                
                conn.commit();
                return linhasAfetadas > 0;
            }
            
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Erro ao fazer rollback", ex);
                }
            }
            throw DatabaseException.deleteError("pedido", e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    // Log erro
                }
            }
        }
    }
    
    /**
     * Gera relatório de vendas por período.
     * 
     * @param dataInicio Data inicial
     * @param dataFim Data final
     * @return Lista com resumo de vendas
     * @throws DatabaseException em caso de erro
     */
    public List<Object[]> gerarRelatorioVendas(LocalDateTime dataInicio, LocalDateTime dataFim) throws DatabaseException {
        String sql = "SELECT p.id, p.data_pedido, c.nome as cliente_nome, p.valor_total, p.status " +
                     "FROM pedido p " +
                     "JOIN cliente c ON p.cliente_id = c.id " +
                     "WHERE p.data_pedido BETWEEN ? AND ? " +
                     "ORDER BY p.data_pedido DESC";
        
        List<Object[]> relatorio = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setTimestamp(1, Timestamp.valueOf(dataInicio));
            stmt.setTimestamp(2, Timestamp.valueOf(dataFim));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] linha = new Object[5];
                    linha[0] = rs.getInt("id");
                    linha[1] = rs.getTimestamp("data_pedido").toLocalDateTime();
                    linha[2] = rs.getString("cliente_nome");
                    linha[3] = rs.getDouble("valor_total");
                    linha[4] = rs.getString("status");
                    relatorio.add(linha);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("pedido", e);
        }
        
        return relatorio;
    }
    
    /**
     * Carrega os itens de um pedido.
     * 
     * @param pedido Pedido para carregar os itens
     * @throws DatabaseException em caso de erro
     */
    private void carregarItensDoPedido(Pedido pedido) throws DatabaseException {
        String sql = "SELECT ip.*, p.id as prod_id, p.nome as prod_nome, p.descricao as prod_descricao, " +
                     "p.preco as prod_preco, p.sku as prod_sku, p.ativo as prod_ativo, p.data_cadastro as prod_data_cadastro " +
                     "FROM item_pedido ip " +
                     "JOIN produto p ON ip.produto_id = p.id " +
                     "WHERE ip.pedido_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, pedido.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ItemPedido item = new ItemPedido();
                    item.setId(rs.getInt("id"));
                    item.setQuantidade(rs.getInt("quantidade"));
                    item.setPrecoUnitario(rs.getDouble("preco_unitario"));
                    
                    // Criar produto
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("prod_id"));
                    produto.setNome(rs.getString("prod_nome"));
                    produto.setDescricao(rs.getString("prod_descricao"));
                    produto.setPreco(rs.getDouble("prod_preco"));
                    produto.setSku(rs.getString("prod_sku"));
                    produto.setAtivo(rs.getBoolean("prod_ativo"));
                    
                    Timestamp dataCadastro = rs.getTimestamp("prod_data_cadastro");
                    if (dataCadastro != null) {
                        produto.setDataCadastro(dataCadastro.toLocalDateTime());
                    }
                    
                    item.setProduto(produto);
                    item.calcularSubtotal();
                    pedido.adicionarItem(produto, item.getQuantidade());
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("item_pedido", e);
        }
    }


    
    /**
     * Extrai um objeto Pedido do ResultSet.
     * 
     * @param rs ResultSet contendo os dados do pedido
     * @return Pedido extraído
     * @throws SQLException em caso de erro
     */
    private Pedido extrairPedidoDoResultSet(ResultSet rs) throws SQLException, DatabaseException {
        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("id"));
        
        // Carregar cliente
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("cliente_id"));
        pedido.setCliente(cliente);
        
        pedido.setDataPedido(rs.getTimestamp("data_pedido").toLocalDateTime());
        pedido.setStatus(StatusPedido.valueOf(rs.getString("status")));
        pedido.calcularTotais();
        pedido.setObservacoes(rs.getString("observacoes"));
        
        Timestamp dataEntrega = rs.getTimestamp("data_entrega");
        if (dataEntrega != null) {
            pedido.setDataEntrega(dataEntrega.toLocalDateTime());
        }
        
        return pedido;
    }
}
