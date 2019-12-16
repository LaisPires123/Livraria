/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contoller;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.bean.Livro;
import model.DAO.LivroDAO;

public class LivroController {
    
    private Livro livroSelecionado;
    private List<Livro> tabelaDeLivro;
    private LivroDAO LVDAO;

    public LivroController() {
        LVDAO = new LivroDAO();
    }

    public void listarTodos(DefaultTableModel modeloTabela) {
        modeloTabela.setNumRows(0);
        List<Livro> listaLivros = LVDAO.buscarTodos();

        for (Livro v : listaLivros) {
            modeloTabela.addRow(new Object[]{v.getId(), v.getAutor(),
                v.getTitulo(), v.getCategoria(), v.getPreco(), v.getPaginas(), v.getISBN(), v.isStatus() ? "1 - Ativo" : "2 - Inativo"});
        }
    }

    public void listarPorId(DefaultTableModel modeloTabela, int id) {
        modeloTabela.setNumRows(0);
        Livro livroBuscado = LVDAO.buscarPorId(id);

        modeloTabela.addRow(new Object[]{livroBuscado.getId(), livroBuscado.getAutor(),
               livroBuscado.getTitulo(), livroBuscado.getCategoria(), livroBuscado.getPreco(), livroBuscado.getPaginas(), livroBuscado.getISBN(), livroBuscado.isStatus() ? "1 - Ativo" : "2 - Inativo"});
    }
    
    
    public void listarPorTitulo(DefaultTableModel modeloTabela, String Titulo) {
        modeloTabela.setNumRows(0);
        Livro livroBuscado = LVDAO.buscarPorTitulo(Titulo);

        modeloTabela.addRow(new Object[]{livroBuscado.getId(), livroBuscado.getAutor(),
               livroBuscado.getTitulo(), livroBuscado.getCategoria(), livroBuscado.getPreco(), livroBuscado.getPaginas(), livroBuscado.getISBN(), livroBuscado.isStatus() ? "1 - Ativo" : "2 - Inativo"});
    }
    
    public void salvar(DefaultTableModel modeloTabela, Livro livro, boolean novo ) {
        if( novo ) {
            LVDAO.inserir(livro);
        } else {
            LVDAO.atualizar(livro);
        }
        this.listarTodos(modeloTabela);
    }
    
    public void excluir(DefaultTableModel modeloTabela, Livro livro ) {
        System.out.println("Excluindo livro No.: " + livro.getId());
        if( livro.getId() != 0 ) {
            LVDAO.excluir(livro.getId());
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível excluir as informações.\nLivro não localizado.", "Erro ao excluir", JOptionPane.ERROR_MESSAGE);
        }
        this.listarTodos(modeloTabela);
    }

    
}
