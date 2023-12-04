package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ifsp.edu.source.DAL.DaoVenda;
import ifsp.edu.source.Model.Venda;

@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private DaoVenda vendaDao;

    // Endpoint para obter uma lista de todas as vendas
    @GetMapping
    public List<Venda> listarVendas() {
        return vendaDao.listar();
    }

    // Endpoint para obter uma venda pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Venda> obterVendaPorId(@PathVariable String id) {
        Venda venda = vendaDao.findById(id);
        if (venda != null)
            // Retorna a venda com o status HTTP OK (200)
            return new ResponseEntity<>(venda, HttpStatus.OK);
        else
            // Retorna o status HTTP NOT_FOUND (404) se a venda não for encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para criar uma nova venda
    @PostMapping
    public ResponseEntity<Venda> criarVenda(@Validated @RequestBody Venda venda) {
        Venda novaVenda = vendaDao.incluir(venda);
        if (novaVenda != null) {
            // Retorna a nova venda criada com o status HTTP CREATED (201)
            return new ResponseEntity<>(novaVenda, HttpStatus.CREATED);
        } else {
            // Retorna o status HTTP INTERNAL_SERVER_ERROR (500) se a criação falhar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para atualizar uma venda existente pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<Venda> atualizarVenda(@PathVariable String id, @Validated @RequestBody Venda novaVenda) {
        Venda vendaExistente = vendaDao.findById(id);
        if (vendaExistente != null) {
            // Atualiza a venda existente e a retorna com o status HTTP OK (200)
            vendaExistente.setIdCliente(novaVenda.getIdCliente());
            vendaExistente.setData(novaVenda.getData());
            vendaDao.alterar(vendaExistente);
            return new ResponseEntity<>(vendaExistente, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a venda a ser atualizada não for
            // encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir uma venda pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirVenda(@PathVariable String id) {
        Venda venda = vendaDao.findById(id);
        if (venda != null) {
            // Exclui a venda e retorna o status HTTP OK (200)
            vendaDao.excluir(venda);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a venda a ser excluída não for
            // encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
