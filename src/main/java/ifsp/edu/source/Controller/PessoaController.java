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

import edu.ifsp.app.PessoaDao;
import ifsp.edu.source.Model.Pessoa;

@RestController
public class PessoaController {

	PessoaDao pessoaDao=new PessoaDao();
	

    @RequestMapping(value = "/compra", method = RequestMethod.GET)
    public List<Pessoa> Get() {
        return pessoaDao.listar();
    }

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.GET)
    public ResponseEntity<Pessoa> GetById(@PathVariable(value = "id") long id)
    {
       Pessoa pessoa = pessoaDao.searchById(id);
        if(pessoa!=null)
            return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/pessoa", method =  RequestMethod.POST)
    public Pessoa Post(@Validated @RequestBody Pessoa pessoa)
    {
        return pessoaDao.incluir(pessoa);
    }

    @RequestMapping(value = "/pessoa/{id}", method =  RequestMethod.PUT)
    public ResponseEntity<Pessoa> Put(@PathVariable(value = "id") long id, @Validated @RequestBody Pessoa newPessoa)
    {
        Pessoa oldPessoa = pessoaDao.searchById(id);
        if(oldPessoa!=null){
            Pessoa pessoa = oldPessoa;
            pessoa.setNome(newPessoa.getNome());
            pessoaDao.incluir(pessoa);
            return new ResponseEntity<Pessoa>(pessoa, HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/pessoa/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> Delete(@PathVariable(value = "id") long id)
    {
        Pessoa pessoa =  pessoaDao.searchById(id);
        if(pessoa!=null){
            pessoaDao.excluir(pessoa);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}