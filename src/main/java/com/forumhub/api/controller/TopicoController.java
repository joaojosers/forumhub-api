package com.forumhub.api.controller;

import com.forumhub.api.domain.topico.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Tópicos", description = "Endpoints relacionados ao gerenciamento de tópicos do fórum")
public class TopicoController {

    @Autowired
    private TopicoRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastrar tópico", description = "Cadastra um novo tópico no fórum")
    public ResponseEntity<DadosDetalhamentoTopico> cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder) {
        // Verificar se já existe um tópico com o mesmo título e mensagem
        if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
            return ResponseEntity.badRequest().build();
        }

        var topico = new Topico(dados);
        repository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));
    }

    @GetMapping
    @Operation(summary = "Listar tópicos", description = "Lista todos os tópicos com paginação e filtros opcionais")
    public ResponseEntity<Page<DadosListagemTopico>> listar(
            @PageableDefault(size = 10, sort = {"dataCriacao"}, direction = Sort.Direction.ASC) Pageable paginacao,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer ano) {

        Page<DadosListagemTopico> page;

        if (curso != null && ano != null) {
            page = repository.findByCursoAndAno(curso, ano, paginacao)
                    .map(DadosListagemTopico::new);
        } else if (curso != null) {
            page = repository.findByCurso(curso, paginacao)
                    .map(DadosListagemTopico::new);
        } else if (ano != null) {
            page = repository.findByAno(ano, paginacao)
                    .map(DadosListagemTopico::new);
        } else {
            page = repository.findAll(paginacao)
                    .map(DadosListagemTopico::new);
        }

        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar tópico", description = "Obtém os detalhes de um tópico específico")
    public ResponseEntity<DadosDetalhamentoTopico> detalhar(@PathVariable Long id) {
        var topico = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualizar tópico", description = "Atualiza os dados de um tópico existente")
    public ResponseEntity<DadosDetalhamentoTopico> atualizar(@PathVariable Long id, @RequestBody @Valid DadosAtualizacaoTopico dados) {
        var topico = repository.getReferenceById(id);
        
        // Verificar se a atualização criaria um duplicado
        if (dados.titulo() != null && dados.mensagem() != null) {
            if (repository.existsByTituloAndMensagem(dados.titulo(), dados.mensagem())) {
                // Verificar se não é o próprio tópico
                var topicoExistente = repository.findAll().stream()
                        .filter(t -> t.getTitulo().equals(dados.titulo()) && t.getMensagem().equals(dados.mensagem()))
                        .findFirst();
                if (topicoExistente.isPresent() && !topicoExistente.get().getId().equals(id)) {
                    return ResponseEntity.badRequest().build();
                }
            }
        }
        
        topico.atualizarInformacoes(dados);
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Excluir tópico", description = "Exclui um tópico do fórum")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        var topico = repository.getReferenceById(id);
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
