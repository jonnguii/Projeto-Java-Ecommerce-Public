package com.ecommerce.dao;

import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Estoque;
import com.ecommerce.model.Produto;
import com.ecommerce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para manipulação de estoque no banco de dados.
 * Implementa CRUD completo para a entidade Estoque e regras de negócio complexas.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class EstoqueDAO {
    
    private final DatabaseConnection dbConnection;
    
    /**
     * Construtor.
     */
    public EstoqueDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Insere um novo registro de estoque.
     * 
     * @param estoque Estoque a ser inserido
     * @return Estoque com ID gerado
     * @throws DatabaseException em caso de erro
     */
    public Estoque inserir(Estoque estoque) throws DatabaseException {
        String sql = "INSERT INTO estoque (produto_id, quantidade_atual, quantidade_minima, " +
                     "quantidade_maxima, localizacao) VALUES (?, ?, ?, ?, ?) RETURNING id, data_ultima_atualizacao";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, estoque.getProduto().getId());
            stmt.setInt(2, estoque.getQuantidadeAtual());
            stmt.setInt(3, estoque.getQuantidadeMinima());
            stmt.setInt(4, estoque.getQuantidadeMaxima());
            stmt.setString(5, estoque.getLocalizacao());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estoque.setId(rs.getInt("id"));
                    estoque.setDataUltimaAtualizacao(
                        rs.getTimestamp("data_ultima_atualizacao").toLocalDateTime()
                    );
                    return estoque;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.insertError("estoque", e);
        }
        
        throw new DatabaseException("Não foi possível inserir o registro de estoque");
    }
    
    /**
     * Busca um registro de estoque pelo ID.
     * 
     * @param id ID do estoque
     * @return Estoque encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Estoque buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT e.*, p.id as prod_id, p.nome as prod_nome, p.descricao as prod_descricao, " +
                     "p.preco as prod_preco, p.sku as prod_sku, p.ativo as prod_ativo, p.data_cadastro as prod_data_cadastro " +
                     "FROM estoque e JOIN produto p ON e.produto_id = p.id WHERE e.id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairEstoqueDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return null;
    }
    
    /**
     * Busca estoque pelo produto.
     * 
     * @param produto Produto a buscar
     * @return Estoque encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Estoque buscarPorProduto(Produto produto) throws DatabaseException {
        String sql = "SELECT e.*, p.id as prod_id, p.nome as prod_nome, p.descricao as prod_descricao, " +
                     "p.preco as prod_preco, p.sku as prod_sku, p.ativo as prod_ativo, p.data_cadastro as prod_data_cadastro " +
                     "FROM estoque e JOIN produto p ON e.produto_id = p.id WHERE e.produto_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, produto.getId());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairEstoqueDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return null;
    }
    
    /**
     * Lista todos os registros de estoque.
     * 
     * @return Lista de estoques
     * @throws DatabaseException em caso de erro
     */
    public List<Estoque> listarTodos() throws DatabaseException {
        String sql = "SELECT e.*, p.id as prod_id, p.nome as prod_nome, p.descricao as prod_descricao, " +
                     "p.preco as prod_preco, p.sku as prod_sku, p.ativo as prod_ativo, p.data_cadastro as prod_data_cadastro " +
                     "FROM estoque e JOIN produto p ON e.produto_id = p.id ORDER BY p.nome";
        List<Estoque> estoques = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                estoques.add(extrairEstoqueDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return estoques;
    }
    
    /**
     * Lista produtos com estoque baixo.
     * 
     * @return Lista de produtos com estoque baixo
     * @throws DatabaseException em caso de erro
     */
    public List<Estoque> listarEstoqueBaixo() throws DatabaseException {
        String sql = "SELECT e.*, p.id as prod_id, p.nome as prod_nome, p.descricao as prod_descricao, " +
                     "p.preco as prod_preco, p.sku as prod_sku, p.ativo as prod_ativo, p.data_cadastro as prod_data_cadastro " +
                     "FROM estoque e JOIN produto p ON e.produto_id = p.id " +
                     "WHERE e.quantidade_atual <= e.quantidade_minima ORDER BY e.quantidade_atual";
        List<Estoque> estoques = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                estoques.add(extrairEstoqueDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return estoques;
    }
    
    /**
     * Lista produtos sem estoque.
     * 
     * @return Lista de produtos sem estoque
     * @throws DatabaseException em caso de erro
     */
    public List<Estoque> listarSemEstoque() throws DatabaseException {
        String sql = "SELECT e.*, p.id as prod_id, p.nome as prod_nome, p.descricao as prod_descricao, " +
                     "p.preco as prod_preco, p.sku as prod_sku, p.ativo as prod_ativo, p.data_cadastro as prod_data_cadastro " +
                     "FROM estoque e JOIN produto p ON e.produto_id = p.id " +
                     "WHERE e.quantidade_atual = 0 ORDER BY p.nome";
        List<Estoque> estoques = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                estoques.add(extrairEstoqueDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return estoques;
    }
    
    /**
     * Atualiza um registro de estoque.
     * 
     * @param estoque Estoque a ser atualizado
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean atualizar(Estoque estoque) throws DatabaseException {
        String sql = "UPDATE estoque SET quantidade_atual = ?, quantidade_minima = ?, " +
                     "quantidade_maxima = ?, localizacao = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, estoque.getQuantidadeAtual());
            stmt.setInt(2, estoque.getQuantidadeMinima());
            stmt.setInt(3, estoque.getQuantidadeMaxima());
            stmt.setString(4, estoque.getLocalizacao());
            stmt.setInt(5, estoque.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("estoque", e);
        }
    }
    
    /**
     * Adiciona quantidade ao estoque de um produto.
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade a ser adicionada
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean adicionarEstoque(int produtoId, int quantidade) throws DatabaseException {
        String sql = "UPDATE estoque SET quantidade_atual = quantidade_atual + ?, " +
                     "data_ultima_atualizacao = CURRENT_TIMESTAMP WHERE produto_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("estoque", e);
        }
    }
    
    /**
     * Remove quantidade do estoque de um produto.
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade a ser removida
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean removerEstoque(int produtoId, int quantidade) throws DatabaseException {
        String sql = "UPDATE estoque SET quantidade_atual = quantidade_atual - ?, " +
                     "data_ultima_atualizacao = CURRENT_TIMESTAMP WHERE produto_id = ? AND quantidade_atual >= ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantidade);
            stmt.setInt(2, produtoId);
            stmt.setInt(3, quantidade);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("estoque", e);
        }
    }
    
    /**
     * Verifica se há estoque suficiente para um produto.
     * 
     * @param produtoId ID do produto
     * @param quantidade Quantidade desejada
     * @return true se há estoque suficiente, false caso contrário
     * @throws DatabaseException em caso de erro
     */
    public boolean verificarEstoqueSuficiente(int produtoId, int quantidade) throws DatabaseException {
        String sql = "SELECT quantidade_atual FROM estoque WHERE produto_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, produtoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade_atual") >= quantidade;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return false;
    }
    
    /**
     * Obtém a quantidade atual em estoque de um produto.
     * 
     * @param produtoId ID do produto
     * @return Quantidade em estoque
     * @throws DatabaseException em caso de erro
     */
    public int getQuantidadeEstoque(int produtoId) throws DatabaseException {
        String sql = "SELECT quantidade_atual FROM estoque WHERE produto_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, produtoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantidade_atual");
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("estoque", e);
        }
        
        return 0;
    }
    
    /**
     * Exclui um registro de estoque.
     * 
     * @param id ID do estoque a ser excluído
     * @return true se excluído com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean excluir(int id) throws DatabaseException {
        String sql = "DELETE FROM estoque WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.deleteError("estoque", e);
        }
    }
    
    /**
     * Extrai um objeto Estoque do ResultSet.
     * 
     * @param rs ResultSet contendo os dados do estoque
     * @return Estoque extraído
     * @throws SQLException em caso de erro
     */
    private Estoque extrairEstoqueDoResultSet(ResultSet rs) throws SQLException {
        Estoque estoque = new Estoque();
        estoque.setId(rs.getInt("id"));
        estoque.setQuantidadeAtual(rs.getInt("quantidade_atual"));
        estoque.setQuantidadeMinima(rs.getInt("quantidade_minima"));
        estoque.setQuantidadeMaxima(rs.getInt("quantidade_maxima"));
        estoque.setLocalizacao(rs.getString("localizacao"));
        
        Timestamp dataAtualizacao = rs.getTimestamp("data_ultima_atualizacao");
        if (dataAtualizacao != null) {
            estoque.setDataUltimaAtualizacao(dataAtualizacao.toLocalDateTime());
        }
        
        // Cria o objeto Produto associado
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
        
        estoque.setProduto(produto);
        
        return estoque;
    }
}
