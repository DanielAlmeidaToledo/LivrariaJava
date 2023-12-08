package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ifsp.edu.source.DAL.DaoItemVenda;
import ifsp.edu.source.Model.ItemVenda;

@RestController
@RequestMapping("/item-venda")
public class ItemVendaController {

    @Autowired
    private DaoItemVenda itemVendaDao;

    // Endpoint para obter uma lista de todos os itens de Venda
    @GetMapping
    public List<ItemVenda> listarItensVenda() {
        return itemVendaDao.listar();
    }

    // Endpoint para obter um item de Venda pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<ItemVenda> obterItemVendaPorId(@PathVariable String id) {
        ItemVenda itemVenda = itemVendaDao.findById(id);
        if (itemVenda != null)
            // Retorna o item de Venda com o status HTTP OK (200)
            return new ResponseEntity<>(itemVenda, HttpStatus.OK);
        else
            // Retorna o status HTTP NOT_FOUND (404) se o item de Venda não for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para criar um novo item de Venda
    @PostMapping
    public ResponseEntity<ItemVenda> criarItemVenda(@RequestBody ItemVenda itemVenda) {
        ItemVenda novoItemVenda = itemVendaDao.incluir(itemVenda);
        if (novoItemVenda != null) {
            // Retorna o novo item de Venda criado com o status HTTP CREATED (201)
            return new ResponseEntity<>(novoItemVenda, HttpStatus.CREATED);
        } else {
            // Retorna o status HTTP INTERNAL_SERVER_ERROR (500) se a criação falhar
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint para atualizar um item de Venda existente pelo ID
    @PutMapping("/{id}")
    public ResponseEntity<ItemVenda> atualizarItemVenda(@PathVariable String id,
            @Validated @RequestBody ItemVenda novoItemVenda) {
        ItemVenda itemVendaExistente = itemVendaDao.findById(id);
        if (itemVendaExistente != null) {
            // Atualiza o item de Venda existente e o retorna com o status HTTP OK (200)
            itemVendaExistente.setLivro(novoItemVenda.getLivro());
            itemVendaExistente.setVenda(novoItemVenda.getVenda());
            itemVendaExistente.setQuantidade(novoItemVenda.getQuantidade());
            // Certifique-se de implementar a lógica específica para atualização no seu DAO
            itemVendaDao.alterar(itemVendaExistente);
            return new ResponseEntity<>(itemVendaExistente, HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se o item de Venda a ser atualizado não
            // for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint para excluir um item de Venda pelo ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirItemVenda(@PathVariable String id) {
        ItemVenda itemVenda = itemVendaDao.findById(id);
        if (itemVenda != null) {
            // Exclui o item de Venda e retorna o status HTTP OK (200)
            itemVendaDao.excluir(itemVenda);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // Retorna o status HTTP NOT_FOUND (404) se o item de Venda a ser excluído não
            // for encontrado
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
