package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ifsp.edu.source.DAL.DaoItemVenda;
import ifsp.edu.source.Model.ItemProduto;

@RestController
@RequestMapping("/item-venda")
public class ItemVendaController {

    @Autowired
    private DaoItemVenda itemVendaDao;

    // Endpoint para obter uma lista de todos os itens de venda
    @GetMapping
    public List<ItemProduto> listarItensVenda() {
        return itemVendaDao.listar();
    }

    // Endpoint para obter um item de venda pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemProduto> obterItemVendaPorId(@PathVariable String id) {
        ItemProduto ItemProduto = itemVendaDao.findById(id);
        if (ItemProduto != null)
            // Retorna o item de venda com o status HTTP OK (200)
            return new ResponseEntity<>(ItemProduto, HttpStatus.OK);
        else
            // Retorna o status HTTP NOT_FOUND (404) se o item de venda não for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para criar um novo item de venda
    @PostMapping
    public ResponseEntity<ItemProduto> criarItemVenda(@Validated @RequestBody ItemProduto ItemProduto) {
        ItemProduto novoItemVenda = itemVendaDao.incluir(ItemProduto);
        if (novoItemVenda != null) {
            // Retorna o novo item de venda criado com o status HTTP CREATED (201)
            return new ResponseEntity<>(novoItemVenda, HttpStatus.CREATED);
        } else {
            // Retorna o status HTTP INTERNAL_SERVER_ERROR (500) se a criação falhar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para atualizar um item de venda existente pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<ItemProduto> atualizarItemVenda(@PathVariable String id,
            @Validated @RequestBody ItemProduto novoItemVenda) {
        ItemProduto itemVendaExistente = itemVendaDao.findById(id);
        if (itemVendaExistente != null) {
            // Atualiza o item de venda existente e o retorna com o status HTTP OK (200)
            itemVendaExistente.setLivro(novoItemVenda.getLivro());
            itemVendaExistente.setVenda(novoItemVenda.getVenda());
            // Certifique-se de implementar a lógica específica para atualização no seu DAO
            itemVendaDao.alterar(itemVendaExistente);
            return new ResponseEntity<>(itemVendaExistente, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se o item de venda a ser atualizado não
            // for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir um item de venda pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirItemVenda(@PathVariable String id) {
        ItemProduto ItemProduto = itemVendaDao.findById(id);
        if (ItemProduto != null) {
            // Exclui o item de venda e retorna o status HTTP OK (200)
            itemVendaDao.excluir(ItemProduto);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se o item de venda a ser excluído não
            // for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
