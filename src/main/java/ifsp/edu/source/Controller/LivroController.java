package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ifsp.edu.source.DAL.DaoLivro;
import ifsp.edu.source.DAL.DataBaseCom;
import ifsp.edu.source.Model.Livro;

@RestController
public class LivroController {

    // Instância da classe de acesso ao banco de dados
    DataBaseCom database = new DataBaseCom();
    
    // Instância da classe de acesso aos dados de livros
    DaoLivro cadLivros = new DaoLivro();

    // Endpoint para obter a lista de todos os livros
    @GetMapping(value = "/livro")
    public List<Livro> listar() {
        return cadLivros.listar();
    }

    // Endpoint para obter um livro pelo ID
    @GetMapping("/livro/{id}")
    public ResponseEntity<Livro> GetById(@PathVariable(value = "id") long id) {
        Livro livro = cadLivros.findById(id);
        if (livro != null)
            // Retorna o livro com o status HTTP OK (200) se encontrado
            return new ResponseEntity<Livro>(livro, HttpStatus.OK);
        else
            // Retorna o status HTTP NOT_FOUND (404) se o livro não for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para cadastrar um novo livro
    @PostMapping("/livro")
    public String Post(@Validated @RequestBody Livro livro) {
        // Inclui o livro no banco de dados
        cadLivros.incluir(livro);
        return "Livro Cadastrado";
    }

    // Endpoint para atualizar um livro existente
    @PutMapping("/livro")
    public String Atualizar(@Validated @RequestBody Livro newLivro) {
        // Altera as informações do livro no banco de dados
        cadLivros.alterar(newLivro);
        return "Livro atualizado";
    }

    // Endpoint para excluir um livro pelo ID
    @DeleteMapping("/livro/{id}")
    public String Delete(@PathVariable(value = "id") long id) {
        // Exclui o livro do banco de dados
        cadLivros.excluir(id);
        return "Exclusão realizada";
    }
}
