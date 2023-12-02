package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ifsp.edu.source.Model.Livro;

public class DaoLivro {

	public boolean incluir(Livro v) {
		DataBaseCom.conectar();

		String sqlString = "insert into produto values(?,?,?,?)";
		try {
			PreparedStatement ps=DataBaseCom.getConnection().prepareStatement(sqlString);
			ps.setInt(1, v.getId());
			ps.setString(2, v.getNome());
			ps.setInt(3, v.getQuantidade());
			ps.setDouble(4, v.getPreco());
			//ps.execute();
			
			System.out.println("======================"+v.getQuantidade());
            boolean ri=ps.execute(); 
            return ri;
//			int ri = DataBaseCom.getStatement().executeUpdate(sqlString);
//			if (ri > 0)
//				return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public boolean alterar(Livro v) {
		DataBaseCom.conectar();
		if (findById(v.getId()) == null) {
			return false;
		}
		try {
			String sqlString = "update produto set nome=? where id=?";
			PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

			ps.setString(1, v.getNome());
			ps.setInt(2, v.getId());
			
			ps.execute();
			// statement.executeUpdate(sqlString);
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public Livro findById(long id) {
		DataBaseCom.conectar();
		Livro p = null;
		try {
			ResultSet rs = DataBaseCom.getStatement().executeQuery("select * from produto where id=" + id);
			while (rs.next()) {
				p = new Livro();
				p.setId(rs.getInt("id"));
				p.setNome(rs.getString("nome"));
				p.setQuantidade(rs.getInt("qtde"));
				p.setPreco(rs.getDouble("preco"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}

	public boolean excluir(Livro v) {
		DataBaseCom.conectar();
		String sqlString = "delete from produto where id=" + v.getId();
		try {
			DataBaseCom.getStatement().executeUpdate(sqlString);
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	public boolean excluir(long id) {
		DataBaseCom.conectar();
		String sqlString = "delete from produto where id=" + id;
		try {
			DataBaseCom.getStatement().executeUpdate(sqlString);
			return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public List<Livro> listar() {
		List<Livro> lista = new ArrayList<>();
		try {
			ResultSet rs = DataBaseCom.getStatement().executeQuery("select * from produto");
			while (rs.next()) {
				Livro p = new Livro();
				p.setId(rs.getInt("id"));
				p.setNome(rs.getString("nome"));
				p.setQuantidade(rs.getInt("qtde"));
				p.setPreco(rs.getDouble("preco"));
				lista.add(p);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}
}
