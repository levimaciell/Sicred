package com.ufpb.sicred.controllers;

import com.ufpb.sicred.dto.credenciamento.CredenciamentoDto;
import com.ufpb.sicred.services.CredenciamentoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/credenciamento")
public class CredenciamentoController {

    private CredenciamentoService credenciamentoService;

    public CredenciamentoController(CredenciamentoService credenciamentoService) {
        this.credenciamentoService = credenciamentoService;
    }

    @PostMapping(path = "/{idInscricao}")
    public CredenciamentoDto credenciar(@PathVariable Long idInscricao){
        return credenciamentoService.credenciar(idInscricao);
    }

    @GetMapping(path = "/{idInscricao}")
    public CredenciamentoDto listarCredenciamento(@PathVariable Long idInscricao){
        return credenciamentoService.listarCredenciamento(idInscricao);
    }

}
