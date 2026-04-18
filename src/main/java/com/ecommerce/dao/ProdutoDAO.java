package com.ecommerce.dao;

import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Produto;
import com.ecommerce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para manipulação de produtos no banco de dados.
 * Implementa CRUD completo para a entidade Produto.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class ProdutoDAO {
    
    private final DatabaseConnection dbConnection;
    
    /**
     * Construtor.
     */
    public ProdutoDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Insere um novo produto no banco de dados.
     * 
     * @param produto Produto a ser inserido
     * @return Produto com ID gerado
     * @throws DatabaseException em caso de erro
     */
    public Produto inserir(Produto produto) throws DatabaseException {
        String sql = "INSERT INTO produto (nome, descricao, preco, sku, ativo) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getSku());
            stmt.setBoolean(5, produto.isAtivo());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        produto.setId(rs.getInt(1));
                        // Define data de cadastro atual
                        produto.setDataCadastro(java.time.LocalDateTime.now());
                        return produto;
                    }
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.insertError("produto", e);
        }
        
        throw new DatabaseException("Não foi possível inserir o produto");
    }
    
    /**
     * Busca um produto pelo ID.
     * 
     * @param id ID do produto
     * @return Produto encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Produto buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM produto WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairProdutoDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return null;
    }
    
    /**
     * Busca um produto pelo SKU.
     * 
     * @param sku SKU do produto
     * @return Produto encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Produto buscarPorSku(String sku) throws DatabaseException {
        String sql = "SELECT * FROM produto WHERE sku = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sku);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairProdutoDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return null;
    }
    
    /**
     * Lista todos os produtos.
     * 
     * @return Lista de produtos
     * @throws DatabaseException em caso de erro
     */
    public List<Produto> listarTodos() throws DatabaseException {
        String sql = "SELECT * FROM produto ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                produtos.add(extrairProdutoDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return produtos;
    }
    
    /**
     * Lista produtos ativos.
     * 
     * @return Lista de produtos ativos
     * @throws DatabaseException em caso de erro
     */
    public List<Produto> listarAtivos() throws DatabaseException {
        String sql = "SELECT * FROM produto WHERE ativo = true ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                produtos.add(extrairProdutoDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return produtos;
    }
    
    /**
     * Busca produtos por nome (busca parcial).
     * 
     * @param nome Nome ou parte do nome a buscar
     * @return Lista de produtos encontrados
     * @throws DatabaseException em caso de erro
     */
    public List<Produto> buscarPorNome(String nome) throws DatabaseException {
        String sql = "SELECT * FROM produto WHERE nome ILIKE ? ORDER BY nome";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(extrairProdutoDoResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return produtos;
    }
    
    /**
     * Lista produtos por faixa de preço.
     * 
     * @param precoMin Preço mínimo
     * @param precoMax Preço máximo
     * @return Lista de produtos na faixa de preço
     * @throws DatabaseException em caso de erro
     */
    public List<Produto> listarPorFaixaPreco(double precoMin, double precoMax) throws DatabaseException {
        String sql = "SELECT * FROM produto WHERE preco BETWEEN ? AND ? ORDER BY preco";
        List<Produto> produtos = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, precoMin);
            stmt.setDouble(2, precoMax);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(extrairProdutoDoResultSet(rs));
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return produtos;
    }
    
    /**
     * Atualiza um produto existente.
     * 
     * @param produto Produto a ser atualizado
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean atualizar(Produto produto) throws DatabaseException {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, sku = ?, ativo = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produto.getNome());
            stmt.setString(2, produto.getDescricao());
            stmt.setDouble(3, produto.getPreco());
            stmt.setString(4, produto.getSku());
            stmt.setBoolean(5, produto.isAtivo());
            stmt.setInt(6, produto.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("produto", e);
        }
    }
    
    /**
     * Exclui um produto pelo ID.
     * 
     * @param id ID do produto a ser excluído
     * @return true se excluído com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean excluir(int id) throws DatabaseException {
        String sql = "DELETE FROM produto WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.deleteError("produto", e);
        }
    }
    
    /**
     * Desativa um produto (exclusão lógica).
     * 
     * @param id ID do produto a ser desativado
     * @return true se desativado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean desativar(int id) throws DatabaseException {
        String sql = "UPDATE produto SET ativo = false WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("produto", e);
        }
    }
    
    /**
     * Verifica se um SKU já está em uso por outro produto.
     * 
     * @param sku SKU a verificar
     * @param idExcluir ID do produto a excluir da verificação (para update)
     * @return true se SKU já existe, false caso contrário
     * @throws DatabaseException em caso de erro
     */
    public boolean skuJaExiste(String sku, Integer idExcluir) throws DatabaseException {
        String sql;
        if (idExcluir == null) {
            sql = "SELECT COUNT(*) FROM produto WHERE sku = ?";
        } else {
            sql = "SELECT COUNT(*) FROM produto WHERE sku = ? AND id != ?";
        }
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, sku);
            
            if (idExcluir != null) {
                stmt.setInt(2, idExcluir);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("produto", e);
        }
        
        return false;
    }
    
    /**
     * Extrai um objeto Produto do ResultSet.
     * 
     * @param rs ResultSet contendo os dados do produto
     * @return Produto extraído
     * @throws SQLException em caso de erro
     */
    private Produto extrairProdutoDoResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setDescricao(rs.getString("descricao"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setSku(rs.getString("sku"));
        produto.setAtivo(rs.getBoolean("ativo"));
        
        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            produto.setDataCadastro(dataCadastro.toLocalDateTime());
        }
        
        return produto;
    }
}
