package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ifsp.edu.source.DAL.DaoLivro;
import ifsp.edu.source.DAL.DataBaseCom;
import ifsp.edu.source.Model.Livro;

@RestController
public class LivroController {

	DataBaseCom database = new DataBaseCom();
	DaoLivro cadLivros = new DaoLivro();

	@GetMapping(value = "/livro")
	public List<Livro> listar() {
		return cadLivros.listar();
	}

	@GetMapping("/livro/{id}")
	public ResponseEntity<Livro> GetById(@PathVariable(value = "id") long id) {
		Livro livro = cadLivros.findById(id);
		if (livro != null)
			return new ResponseEntity<Livro>(livro, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PostMapping("/livro") // incluir
	public String Post(@Validated @RequestBody Livro livro) {
		cadLivros.incluir(livro);
		return "Livro Cadastrado";
	}

	@PutMapping("/livro") // update
	public String Atualizar(@Validated @RequestBody Livro newLivro) {
		cadLivros.alterar(newLivro);
		return "Livro atualizado";
	}

	@DeleteMapping("/livro/{id}") // delete
	public String Delete(@PathVariable(value = "id") long id) {
		cadLivros.excluir(id);
		return "Exclusão realizada";

	}

}
