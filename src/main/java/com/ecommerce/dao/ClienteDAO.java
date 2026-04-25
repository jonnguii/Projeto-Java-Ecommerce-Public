package com.ecommerce.dao;

import com.ecommerce.exception.DatabaseException;
import com.ecommerce.model.Cliente;
import com.ecommerce.enums.StatusAtividade;
import com.ecommerce.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) para manipulação de clientes no banco de dados.
 * Implementa CRUD completo para a entidade Cliente.
 * 
 * @author Sistema E-commerce
 * @version 1.0
 */
public class ClienteDAO {
    
    private final DatabaseConnection dbConnection;
    
    /**
     * Construtor.
     */
    public ClienteDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    /**
     * Insere um novo cliente no banco de dados.
     * 
     * @param cliente Cliente a ser inserido
     * @return Cliente com ID gerado
     * @throws DatabaseException em caso de erro
     */
    public Cliente inserir(Cliente cliente) throws DatabaseException {
        String sql = "INSERT INTO cliente (nome, email, telefone, cpf, endereco, cidade, estado, cep, ativo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getEstado());
            stmt.setString(8, cliente.getCep());
            stmt.setBoolean(9, cliente.isAtivo());
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cliente.setId(rs.getInt(1));
                        // Define data de cadastro atual
                        cliente.setDataCadastro(java.time.LocalDateTime.now());
                        return cliente;
                    }
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.insertError("cliente", e);
        }
        
        throw new DatabaseException("Não foi possível inserir o cliente");
    }
    
    /**
     * Busca um cliente pelo ID.
     * 
     * @param id ID do cliente
     * @return Cliente encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Cliente buscarPorId(int id) throws DatabaseException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairClienteDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("cliente", e);
        }
        
        return null;
    }
    
    /**
     * Busca um cliente pelo CPF.
     * 
     * @param cpf CPF do cliente
     * @return Cliente encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Cliente buscarPorCpf(String cpf) throws DatabaseException {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cpf);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairClienteDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("cliente", e);
        }
        
        return null;
    }
    
    /**
     * Busca um cliente pelo email.
     * 
     * @param email Email do cliente
     * @return Cliente encontrado ou null
     * @throws DatabaseException em caso de erro
     */
    public Cliente buscarPorEmail(String email) throws DatabaseException {
        String sql = "SELECT * FROM cliente WHERE email = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairClienteDoResultSet(rs);
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("cliente", e);
        }
        
        return null;
    }
    
    /**
     * Lista todos os clientes.
     * 
     * @return Lista de clientes
     * @throws DatabaseException em caso de erro
     */
    public List<Cliente> listarTodos() throws DatabaseException {
        String sql = "SELECT * FROM cliente ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(extrairClienteDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("cliente", e);
        }
        
        return clientes;
    }
    
    /**
     * Lista clientes ativos.
     * 
     * @return Lista de clientes ativos
     * @throws DatabaseException em caso de erro
     */
    public List<Cliente> listarAtivos() throws DatabaseException {
        String sql = "SELECT * FROM cliente WHERE ativo = true ORDER BY nome";
        List<Cliente> clientes = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                clientes.add(extrairClienteDoResultSet(rs));
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("cliente", e);
        }
        
        return clientes;
    }
    
    /**
     * Atualiza um cliente existente.
     * 
     * @param cliente Cliente a ser atualizado
     * @return true se atualizado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean atualizar(Cliente cliente) throws DatabaseException {
        String sql = "UPDATE cliente SET nome = ?, email = ?, telefone = ?, cpf = ?, " +
                     "endereco = ?, cidade = ?, estado = ?, cep = ?, ativo = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getEmail());
            stmt.setString(3, cliente.getTelefone());
            stmt.setString(4, cliente.getCpf());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getCidade());
            stmt.setString(7, cliente.getEstado());
            stmt.setString(8, cliente.getCep());
            stmt.setBoolean(9, cliente.isAtivo());
            stmt.setInt(10, cliente.getId());
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("cliente", e);
        }
    }
    
    /**
     * Exclui um cliente pelo ID.
     * 
     * @param id ID do cliente a ser excluído
     * @return true se excluído com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean excluir(int id) throws DatabaseException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.deleteError("cliente", e);
        }
    }
    
    /**
     * Desativa um cliente (exclusão lógica).
     * 
     * @param id ID do cliente a ser desativado
     * @return true se desativado com sucesso
     * @throws DatabaseException em caso de erro
     */
    public boolean desativar(int id) throws DatabaseException {
        String sql = "UPDATE cliente SET ativo = false WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
            
        } catch (SQLException e) {
            throw DatabaseException.updateError("cliente", e);
        }
    }
    
    /**
     * Verifica se um email já está em uso por outro cliente.
     * 
     * @param email Email a verificar
     * @param idExcluir ID do cliente a excluir da verificação (para update)
     * @return true se email já existe, false caso contrário
     * @throws DatabaseException em caso de erro
     */
    public boolean emailJaExiste(String email, Integer idExcluir) throws DatabaseException {
        String sql;
        if (idExcluir == null) {
            sql = "SELECT COUNT(*) FROM cliente WHERE email = ?";
        } else {
            sql = "SELECT COUNT(*) FROM cliente WHERE email = ? AND id != ?";
        }
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            if (idExcluir != null) {
                stmt.setInt(2, idExcluir);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw DatabaseException.selectError("cliente", e);
        }
        
        return false;
    }
    
    /**
     * Extrai um objeto Cliente do ResultSet.
     * 
     * @param rs ResultSet contendo os dados do cliente
     * @return Cliente extraído
     * @throws SQLException em caso de erro
     */
    private Cliente extrairClienteDoResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setNome(rs.getString("nome"));
        cliente.setEmail(rs.getString("email"));
        cliente.setTelefone(rs.getString("telefone"));
        cliente.setCpf(rs.getString("cpf"));
        cliente.setEndereco(rs.getString("endereco"));
        cliente.setCidade(rs.getString("cidade"));
        cliente.setEstado(rs.getString("estado"));
        cliente.setCep(rs.getString("cep"));
        cliente.setStatusAtividade(rs.getBoolean("ativo") ? StatusAtividade.ATIVO : StatusAtividade.INATIVO);
        
        Timestamp dataCadastro = rs.getTimestamp("data_cadastro");
        if (dataCadastro != null) {
            cliente.setDataCadastro(dataCadastro.toLocalDateTime());
        }
        
        return cliente;
    }
}
