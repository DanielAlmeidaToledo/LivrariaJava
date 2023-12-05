package ifsp.edu.source;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

	@GetMapping("/hello")
	public String hello() {
		return "Hello Wolrd";
	}

	/*
	 * Estão faltando algumas coisas no sistema.
	 * -Dao de Produtos
	 * -Dao de ItensCompra
	 * -Dao de Compra
	 * -Model de Compra
	 * -Entre outros.
	 * O trabalho consiste em terminar de elaborar o sistema para realizar uma
	 * compra
	 * Depois, elaborar a parte completa para a venda, usando a abstração para
	 * reaproveitar o codigo da compra.
	 */
}
