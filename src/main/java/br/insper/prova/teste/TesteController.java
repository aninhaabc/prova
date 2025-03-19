package br.insper.prova.teste;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TesteController {
    @GetMapping("/teste")
    public String teste(){
        return "HELLO WORLD";
    }
}
