/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.DAO;

import java.util.List;
import model.bean.Livro;
import DB.Connect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class LivroDAO implements iDAO<Livro> {

    private final String INSERT = "INSERT INTO livro(AUTOR, TÍTULO, PRECO, STATUS, PÁGINA, CATEGORIA, ISBN) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private final String UPDATE = "UPDATE livro SET AUTOR=?, TÍTULO=?, PRECO=?, STATUS=?, PÁGINA=?, CATEGORIA=?, ISBN=?  WHERE ID =?";
    private final String DELETE = "DELETE FROM livro WHERE ID =?";
    private final String LISTALL = "SELECT * FROM livro";
    private final String LISTBYID = "SELECT * FROM livro WHERE ID=?";
    private final String LISTBYTITULO = "SELECT * FROM livro WHERE TÍTULO=?";

    private Connect conn = null;
    private Connection conexao = null;

    public Livro inserir(Livro livro) {
        conexao = this.getConnect().connection;
        if (livro != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

                transacaoSQL.setString(1, livro.getAutor());
                transacaoSQL.setString(2, livro.getTitulo());
                transacaoSQL.setString(3, livro.getCategoria());
                transacaoSQL.setBoolean(4, livro.isStatus());
                transacaoSQL.setInt(4, livro.getPaginas());
                transacaoSQL.setDouble(4, livro.getPreco());
                transacaoSQL.setInt(4, livro.getISBN());

                transacaoSQL.execute();
                JOptionPane.showMessageDialog(null, "Livro cadastrado com sucesso", "Registro inserido", JOptionPane.INFORMATION_MESSAGE);

                try (ResultSet generatedKeys = transacaoSQL.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        livro.setId(generatedKeys.getInt(1));
                    } else {
                        throw new SQLException("Não foi possível recuperar o ID.");
                    }
                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o livro no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do livro não podem estar vazios.", "Livro não informado", JOptionPane.ERROR_MESSAGE);
        }

        return livro;
    }

    public Livro atualizar(Livro livroNovo) {

        conexao = this.getConnect().connection;
        if (livroNovo != null && conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(UPDATE);

                transacaoSQL.setString(1, livroNovo.getAutor());
                transacaoSQL.setString(2, livroNovo.getTitulo());
                transacaoSQL.setString(3, livroNovo.getCategoria());
                transacaoSQL.setBoolean(4, livroNovo.isStatus());
                transacaoSQL.setInt(4, livroNovo.getPaginas());
                transacaoSQL.setDouble(4, livroNovo.getPreco());
                transacaoSQL.setInt(4, livroNovo.getISBN());

                transacaoSQL.setInt(5, livroNovo.getId());

                int resultado = transacaoSQL.executeUpdate();

                if (resultado == 0) {
                    JOptionPane.showMessageDialog(null, "Não foi possível atualizar as informações", "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Livro atualizado com sucesso", "Registro atualizado", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro ao inserir o Livro no banco de" + "dados. \n" + e.getMessage(), "Erro na transação SQL", JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Os dados do Livro não podem estar vazios.", "Vendedor não informado", JOptionPane.ERROR_MESSAGE);
        }

        return livroNovo;
    }

    public void excluir(int id) {

        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este livro?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        // 0 - Sim  1 - Não
        if (confirmar == 1) {
            return;
        }
        conexao = this.getConnect().connection;
        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(DELETE);

                transacaoSQL.setInt(1, id);

                boolean erroAoExcluir = transacaoSQL.execute();

                if (erroAoExcluir) {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir", "Não foi possível excluir as informações", JOptionPane.ERROR_MESSAGE);
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                JOptionPane.showMessageDialog(null, "Registro excluido", "Livro excluido com sucesso", JOptionPane.INFORMATION_MESSAGE);

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao excluir o livro no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

    }

    public List<Livro> buscarTodos() {

        conexao = this.getConnect().connection;

        ResultSet resultado = null;
        ArrayList<Livro> livros = new ArrayList<Livro>();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTALL);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {
                    Livro livroEncontrado = new Livro();

                    livroEncontrado.setId(resultado.getInt("id"));
                    livroEncontrado.setAutor(resultado.getString("Autor"));
                    livroEncontrado.setTitulo(resultado.getString("Título"));
                    livroEncontrado.setCategoria(resultado.getString("Categoria"));
                    livroEncontrado.setStatus(resultado.getBoolean("status"));
                    livroEncontrado.setISBN(resultado.getInt("ISBN"));
                    livroEncontrado.setPreco(resultado.getDouble("Preço"));
                    livroEncontrado.setPaginas(resultado.getInt("Páginas"));

                    livros.add(livroEncontrado);
                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedores no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return livros;
    }

    public Livro buscarPorId(int id) {

        conexao = this.getConnect().connection;

        ResultSet resultado = null;
        Livro livroEncontrado = new Livro();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYID);
                transacaoSQL.setInt(1, id);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    livroEncontrado.setId(resultado.getInt("id"));
                    livroEncontrado.setAutor(resultado.getString("Autor"));
                    livroEncontrado.setTitulo(resultado.getString("Título"));
                    livroEncontrado.setCategoria(resultado.getString("Categoria"));
                    livroEncontrado.setStatus(resultado.getBoolean("status"));
                    livroEncontrado.setISBN(resultado.getInt("ISBN"));
                    livroEncontrado.setPreco(resultado.getDouble("Preço"));
                    livroEncontrado.setPaginas(resultado.getInt("Páginas"));

                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return livroEncontrado;
    }

    public Livro buscarPorTitulo(String Titulo) {

        conexao = this.getConnect().connection;

        ResultSet resultado = null;
        Livro livroEncontrado = new Livro();

        if (conexao != null) {
            try {
                PreparedStatement transacaoSQL;
                transacaoSQL = conexao.prepareStatement(LISTBYTITULO);
                transacaoSQL.setString(1, Titulo);

                resultado = transacaoSQL.executeQuery();

                while (resultado.next()) {

                    livroEncontrado.setId(resultado.getInt("id"));
                    livroEncontrado.setAutor(resultado.getString("Autor"));
                    livroEncontrado.setTitulo(resultado.getString("Título"));
                    livroEncontrado.setCategoria(resultado.getString("Categoria"));
                    livroEncontrado.setStatus(resultado.getBoolean("status"));
                    livroEncontrado.setISBN(resultado.getInt("ISBN"));
                    livroEncontrado.setPreco(resultado.getDouble("Preço"));
                    livroEncontrado.setPaginas(resultado.getInt("Páginas"));

                }

                conn.fechaConexao(conexao, transacaoSQL);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Erro na transação SQL", "Erro ao procurar vendedor no banco de" + "dados. \n" + e.getMessage(), JOptionPane.ERROR_MESSAGE);
                System.out.println(e.getMessage());
            }

        } else {
            JOptionPane.showMessageDialog(null, "Problemas de conexão", "Não foi possível se conectar ao banco.", JOptionPane.ERROR_MESSAGE);
        }

        return livroEncontrado;
    }

    public Connect getConnect() {
        this.conn = new Connect("root", "", "NovaLivraria");
        return this.conn;
    }
}
