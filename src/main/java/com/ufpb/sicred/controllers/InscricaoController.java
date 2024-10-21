package com.ufpb.sicred.controllers;

import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.model.StatusInscricao;
import com.ufpb.sicred.services.InscricaoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricao")
public class InscricaoController {

    private InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    @PostMapping()
    public InscricaoDto createInscricao(@RequestBody InscricaoDto dto){

        return inscricaoService.createUser(dto);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteInscricao(@PathVariable Long id){
        inscricaoService.deleteInscricao(id);
    }

    @GetMapping(path = "/{id}")
    public InscricaoDto listInscricao(@PathVariable Long id){
        return inscricaoService.listInscricao(id);
    }

    @GetMapping
    public List<InscricaoDto> listInscricoes(){
        return inscricaoService.listAll();
    }

}
