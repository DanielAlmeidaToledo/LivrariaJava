package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ifsp.edu.source.DAL.DaoPessoa;
import ifsp.edu.source.Model.Pessoa;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    DaoPessoa DaoPessoa;

    @GetMapping
    public List<Pessoa> listarPessoas() {
        return DaoPessoa.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> obterPessoaPorId(@PathVariable(value = "id") long id) {
        Pessoa pessoa = DaoPessoa.findById(id);
        if (pessoa != null) {
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@Validated @RequestBody Pessoa pessoa) {
        Pessoa novaPessoa = DaoPessoa.incluir(pessoa);
        if (novaPessoa != null) {
            return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable(value = "id") long id, @Validated @RequestBody Pessoa novaPessoa) {
        Pessoa pessoaExistente = DaoPessoa.findById(id);
        if (pessoaExistente != null) {
            pessoaExistente.setNome(novaPessoa.getNome());
            DaoPessoa.incluir(pessoaExistente);
            return new ResponseEntity<>(pessoaExistente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirPessoa(@PathVariable(value = "id") long id) {
        Pessoa pessoa = DaoPessoa.findById(id);
        if (pessoa != null) {
            DaoPessoa.excluir(pessoa);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
