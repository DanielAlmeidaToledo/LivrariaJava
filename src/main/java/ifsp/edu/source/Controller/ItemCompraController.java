package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ifsp.edu.source.DAL.DaoItemCompra;
import ifsp.edu.source.Model.ItemProduto;

@RestController
@RequestMapping("/item-produto")
public class ItemCompraController {

    @Autowired
    private DaoItemCompra itemCompraDao;

    // Endpoint para obter uma lista de todos os itens de Compra
    @GetMapping
    public List<ItemProduto> listarItensCompra() {
        return itemCompraDao.listar();
    }

    // Endpoint para obter um item de Compra pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemProduto> obterItemCompraPorId(@PathVariable String id) {
        ItemProduto itemProduto = itemCompraDao.findById(id);
        if (itemProduto != null)
            // Retorna o item de Compra com o status HTTP OK (200)
            return new ResponseEntity<>(itemProduto, HttpStatus.OK);
        else
            // Retorna o status HTTP NOT_FOUND (404) se o item de Compra não for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para criar um novo item de Compra
    @PostMapping
    public ResponseEntity<ItemProduto> criarItemCompra(@RequestBody ItemProduto itemProduto) {
        System.out.println("------->>>>>> " + itemProduto);

        ItemProduto novoItemCompra = itemCompraDao.incluir(itemProduto);
        if (novoItemCompra != null) {
            // Retorna o novo item de Compra criado com o status HTTP CREATED (201)
            return new ResponseEntity<>(novoItemCompra, HttpStatus.CREATED);
        } else {
            // Retorna o status HTTP INTERNAL_SERVER_ERROR (500) se a criação falhar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para atualizar um item de Compra existente pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<ItemProduto> atualizarItemCompra(@PathVariable String id,
            @Validated @RequestBody ItemProduto novoItemCompra) {
        ItemProduto itemCompraExistente = itemCompraDao.findById(id);
        if (itemCompraExistente != null) {
            // Atualiza o item de Compra existente e o retorna com o status HTTP OK (200)
            itemCompraExistente.setLivro(novoItemCompra.getLivro());
            itemCompraExistente.setCompra(novoItemCompra.getCompra());
            itemCompraExistente.setQuantidade(novoItemCompra.getQuantidade());
            // Certifique-se de implementar a lógica específica para atualização no seu DAO
            itemCompraDao.alterar(itemCompraExistente);
            return new ResponseEntity<>(itemCompraExistente, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se o item de Compra a ser atualizado não
            // for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir um item de Compra pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirItemCompra(@PathVariable String id) {
        ItemProduto itemProduto = itemCompraDao.findById(id);
        if (itemProduto != null) {
            // Exclui o item de Compra e retorna o status HTTP OK (200)
            itemCompraDao.excluir(itemProduto);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se o item de Compra a ser excluído não
            // for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
