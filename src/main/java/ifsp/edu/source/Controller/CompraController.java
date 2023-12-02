package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ifsp.edu.source.DAL.DaoCompra;
import ifsp.edu.source.Model.Compra;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private DaoCompra compraDao;

    @GetMapping
    public List<Compra> listarCompras() {
        return compraDao.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> obterCompraPorId(@PathVariable long id) {
        Compra compra = compraDao.findById(id);
        if (compra != null)
            return new ResponseEntity<>(compra, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<Compra> criarCompra(@Validated @RequestBody Compra compra) {
        Compra novaCompra = compraDao.incluir(compra);
        if (novaCompra != null) {
            return new ResponseEntity<>(novaCompra, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Compra> atualizarCompra(@PathVariable long id, @Validated @RequestBody Compra novaCompra) {
        Compra compraExistente = compraDao.findById(id);
        if (compraExistente != null) {
            compraExistente.setIdCliente(novaCompra.getIdCliente());
            compraExistente.setData(novaCompra.getData());
            compraDao.alterar(compraExistente);
            return new ResponseEntity<>(compraExistente, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirCompra(@PathVariable long id) {
        Compra compra = compraDao.findById(id);
        if (compra != null) {
            compraDao.excluir(compra);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}