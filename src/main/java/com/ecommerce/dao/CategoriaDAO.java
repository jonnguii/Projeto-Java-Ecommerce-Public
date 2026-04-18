package com.ecommerce.dao;

import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Categoria;
import com.ecommerce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para manipulação de categorias no banco de dados.
 * Implementa CRUD completo para a entidade Categoria.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class CategoriaDAO {
    
    private final DatabaseConnection dbConnection;
    
    /**
     * Construtor.
     */
    public CategoriaDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Insere uma nova categoria no banco de dados.
     * 
     * @param categoria Categoria a ser inserida
     * @return Categoria com ID gerado
     * @throws DatabaseException em caso de erro
     */
    public Categoria inserir(Categoria categoria) throws DatabaseException {
        String sql = "INSERT INTO categoria (nome, descricao, ativa) " +
                     "VALUES (?, ?, ?) RETURNING id, data_criacao";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setBoolean(3, categoria.isAtiva());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    categoria.setId(rs.getInt("id"));
                    categoria.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                    return categoria;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.insertError("categoria", e);
        }
        
        throw new DatabaseException("Não foi possível inserir a categoria");
    }
    
    /**
     * Busca uma categoria pelo ID.
     * 
     * @param id ID da categoria
     * @return Categoria encontrada ou null
     * @throws DatabaseException em caso de erro
     */
    public Categoria buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairCategoriaDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("categoria", e);
        }
        
        return null;
    }
    
    /**
     * Busca uma categoria pelo nome.
     * 
     * @param nome Nome da categoria
     * @return Categoria encontrada ou null
     * @throws DatabaseException em caso de erro
     */
    public Categoria buscarPorNome(String nome) throws DatabaseException {
        String sql = "SELECT * FROM categoria WHERE nome = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairCategoriaDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("categoria", e);
        }
        
        return null;
    }
    
    /**
     * Lista todas as categorias.
     * 
     * @return Lista de categorias
     * @throws DatabaseException em caso de erro
     */
    public List<Categoria> listarTodas() throws DatabaseException {
        String sql = "SELECT * FROM categoria ORDER BY nome";
        List<Categoria> categorias = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(extrairCategoriaDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("categoria", e);
        }
        
        return categorias;
    }
    
    /**
     * Lista categorias ativas.
     * 
     * @return Lista de categorias ativas
     * @throws DatabaseException em caso de erro
     */
    public List<Categoria> listarAtivas() throws DatabaseException {
        String sql = "SELECT * FROM categoria WHERE ativa = true ORDER BY nome";
        List<Categoria> categorias = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                categorias.add(extrairCategoriaDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("categoria", e);
        }
        
        return categorias;
    }
    
    /**
     * Atualiza uma categoria existente.
     * 
     * @param categoria Categoria a ser atualizada
     * @return true se atualizada com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean atualizar(Categoria categoria) throws DatabaseException {
        String sql = "UPDATE categoria SET nome = ?, descricao = ?, ativa = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setBoolean(3, categoria.isAtiva());
            stmt.setInt(4, categoria.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("categoria", e);
        }
    }
    
    /**
     * Exclui uma categoria pelo ID.
     * 
     * @param id ID da categoria a ser excluída
     * @return true se excluída com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean excluir(int id) throws DatabaseException {
        String sql = "DELETE FROM categoria WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.deleteError("categoria", e);
        }
    }
    
    /**
     * Desativa uma categoria (exclusão lógica).
     * 
     * @param id ID da categoria a ser desativada
     * @return true se desativada com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean desativar(int id) throws DatabaseException {
        String sql = "UPDATE categoria SET ativa = false WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("categoria", e);
        }
    }
    
    /**
     * Verifica se um nome já está em uso por outra categoria.
     * 
     * @param nome Nome a verificar
     * @param idExcluir ID da categoria a excluir da verificação (para update)
     * @return true se nome já existe, false caso contrário
     * @throws DatabaseException em caso de erro
     */
    public boolean nomeJaExiste(String nome, Integer idExcluir) throws DatabaseException {
        String sql = "SELECT COUNT(*) FROM categoria WHERE nome = ? AND (? IS NULL OR id != ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nome);
            stmt.setObject(2, idExcluir);
            stmt.setObject(3, idExcluir);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("categoria", e);
        }
        
        return false;
    }
    
    /**
     * Associa um produto a uma categoria.
     * 
     * @param produtoId ID do produto
     * @param categoriaId ID da categoria
     * @return true se associado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean associarProduto(int produtoId, int categoriaId) throws DatabaseException {
        String sql = "INSERT INTO produto_categorias (produto_id, categoria_id) VALUES (?, ?) " +
                     "ON CONFLICT DO NOTHING";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, produtoId);
            stmt.setInt(2, categoriaId);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.insertError("produto_categorias", e);
        }
    }
    
    /**
     * Remove associação entre produto e categoria.
     * 
     * @param produtoId ID do produto
     * @param categoriaId ID da categoria
     * @return true se removido com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean desassociarProduto(int produtoId, int categoriaId) throws DatabaseException {
        String sql = "DELETE FROM produto_categorias WHERE produto_id = ? AND categoria_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, produtoId);
            stmt.setInt(2, categoriaId);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.deleteError("produto_categorias", e);
        }
    }
    
    /**
     * Extrai um objeto Categoria do ResultSet.
     * 
     * @param rs ResultSet contendo os dados da categoria
     * @return Categoria extraída
     * @throws SQLException em caso de erro
     */
    private Categoria extrairCategoriaDoResultSet(ResultSet rs) throws SQLException {
        Categoria categoria = new Categoria();
        categoria.setId(rs.getInt("id"));
        categoria.setNome(rs.getString("nome"));
        categoria.setDescricao(rs.getString("descricao"));
        categoria.setAtiva(rs.getBoolean("ativa"));
        
        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            categoria.setDataCriacao(dataCriacao.toLocalDateTime());
        }
        
        return categoria;
    }
}
