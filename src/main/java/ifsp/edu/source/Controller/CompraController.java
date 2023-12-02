package ifsp.edu.source.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ifsp.edu.source.DAL.DaoCompra;

@RestController
public class CompraController {

	DaoCompra compraDao=new DaoCompra();
	

    @RequestMapping(value = "/compra", method = RequestMethod.GET)
    public List<Compra> Get() {
        return compraDao.listar();
    }

    @RequestMapping(value = "/compra/{id}", method = RequestMethod.GET)
    public ResponseEntity<Compra> GetById(@PathVariable(value = "id") long id)
    {
       Compra compra = compraDao.searchById(id);
        if(compra!=null)
            return new ResponseEntity<Compra>(compra, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/compra", method =  RequestMethod.POST)
    public Compra Post(@Validated @RequestBody Compra compra)
    {
        return compraDao.incluir(compra);
    }

    @RequestMapping(value = "/compra/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<Compra> Put(@PathVariable(value = "id") long id, @Validated @RequestBody Compra newCompra)
    {
        Compra oldCompra = compraDao.searchById(id);
        if(oldCompra!=null){
            Compra compra = oldCompra;
            compra.setNome(newCompra.getNome());
            compraDao.incluir(compra);
            return new ResponseEntity<Compra>(compra, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/compra/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id)
    {
        Compra compra =  compraDao.searchById(id);
        if(compra!=null){
            compraDao.excluir(compra);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}