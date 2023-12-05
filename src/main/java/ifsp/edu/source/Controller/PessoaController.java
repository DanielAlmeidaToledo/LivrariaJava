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

    // Endpoint para obter uma lista de todas as pessoas
    @GetMapping
    public List<Pessoa> listarPessoas() {
        return DaoPessoa.listar();
    }

    // Endpoint para obter uma pessoa pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Pessoa> obterPessoaPorId(@PathVariable(value = "id") String id) {
        Pessoa pessoa = DaoPessoa.findById(id);
        if (pessoa != null) {
            // Retorna a pessoa com o status HTTP OK (200)
            return new ResponseEntity<>(pessoa, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a pessoa não for encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para criar uma nova pessoa
    @PostMapping
    public ResponseEntity<Pessoa> criarPessoa(@Validated @RequestBody Pessoa pessoa) {
        Pessoa novaPessoa = DaoPessoa.incluir(pessoa);
        if (novaPessoa != null) {
            // Retorna a nova pessoa criada com o status HTTP CREATED (201)
            return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
        } else {
            // Retorna o status HTTP INTERNAL_SERVER_ERROR (500) se a criação falhar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para atualizar uma pessoa existente pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<Pessoa> atualizarPessoa(@PathVariable(value = "id") String id,
            @Validated @RequestBody Pessoa novaPessoa) {
        Pessoa pessoaExistente = DaoPessoa.findById(id);
        if (pessoaExistente != null) {
            // Atualiza a pessoa existente e a retorna com o status HTTP OK (200)
            pessoaExistente.setNome(novaPessoa.getNome());
            DaoPessoa.incluir(pessoaExistente);
            return new ResponseEntity<>(pessoaExistente, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a pessoa a ser atualizada não for
            // encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir uma pessoa pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirPessoa(@PathVariable(value = "id") String id) {
        Pessoa pessoa = DaoPessoa.findById(id);
        if (pessoa != null) {
            // Exclui a pessoa e retorna o status HTTP OK (200)
            DaoPessoa.excluir(pessoa);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a pessoa a ser excluída não for
            // encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
