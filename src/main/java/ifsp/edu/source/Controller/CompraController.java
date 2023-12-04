package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ifsp.edu.source.DAL.DaoCompra;
import ifsp.edu.source.Model.Compra;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private DaoCompra compraDao;

    // Endpoint para obter uma lista de todas as compras
    @GetMapping
    public List<Compra> listarCompras() {
        return compraDao.listar();
    }

    // Endpoint para obter uma compra pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<Compra> obterCompraPorId(@PathVariable long id) {
        Compra compra = compraDao.findById(id);
        if (compra != null)
            // Retorna a compra com o status HTTP OK (200)
            return new ResponseEntity<>(compra, HttpStatus.OK);
        else
            // Retorna o status HTTP NOT_FOUND (404) se a compra não for encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para criar uma nova compra
    @PostMapping
    public ResponseEntity<Compra> criarCompra(@Validated @RequestBody Compra compra) {
        Compra novaCompra = compraDao.incluir(compra);
        if (novaCompra != null) {
            // Retorna a nova compra criada com o status HTTP CREATED (201)
            return new ResponseEntity<>(novaCompra, HttpStatus.CREATED);
        } else {
            // Retorna o status HTTP INTERNAL_SERVER_ERROR (500) se a criação falhar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para atualizar uma compra existente pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<Compra> atualizarCompra(@PathVariable long id, @Validated @RequestBody Compra novaCompra) {
        Compra compraExistente = compraDao.findById(id);
        if (compraExistente != null) {
            // Atualiza a compra existente e a retorna com o status HTTP OK (200)
            compraExistente.setIdCliente(novaCompra.getIdCliente());
            compraExistente.setData(novaCompra.getData());
            compraDao.alterar(compraExistente);
            return new ResponseEntity<>(compraExistente, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a compra a ser atualizada não for encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir uma compra pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirCompra(@PathVariable long id) {
        Compra compra = compraDao.findById(id);
        if (compra != null) {
            // Exclui a compra e retorna o status HTTP OK (200)
            compraDao.excluir(compra);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se a compra a ser excluída não for encontrada
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
