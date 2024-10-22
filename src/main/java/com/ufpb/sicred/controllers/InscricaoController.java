package com.ufpb.sicred.controllers;

import com.ufpb.sicred.dto.inscricao.InscricaoDto;
import com.ufpb.sicred.services.InscricaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inscricao")
public class InscricaoController {

    private InscricaoService inscricaoService;

    public InscricaoController(InscricaoService inscricaoService) {
        this.inscricaoService = inscricaoService;
    }

    //    @PostMapping()
//    public InscricaoDto createInscricao(@RequestBody InscricaoDto dto){
//
//        return inscricaoService.createInscricao(dto);
//    }
    @PostMapping
    public ResponseEntity<InscricaoDto> createInscricao(@Valid @RequestBody InscricaoDto dto) {
        InscricaoDto createdInscricao = inscricaoService.createInscricao(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInscricao);
    }


//    @DeleteMapping(path = "/{id}")
//    public void deleteInscricao(@PathVariable Long id){
//        inscricaoService.deleteInscricao(id);
//    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteInscricao(@PathVariable Long id) {
        inscricaoService.deleteInscricao(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }


    @GetMapping(path = "/{id}")
    public InscricaoDto listInscricao(@PathVariable Long id) {
        return inscricaoService.listInscricao(id);
    }

    @GetMapping
    public List<InscricaoDto> listInscricoes() {
        return inscricaoService.listAll();
    }

}
