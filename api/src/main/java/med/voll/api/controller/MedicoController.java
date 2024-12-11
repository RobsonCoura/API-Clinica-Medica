package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    //Injecao de dependencias
    @Autowired
    private MedicoRepository repository;

    //Método para funcionabilidade de cadastro de médico (CREATE)
    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {

        var medico = new Medico(dados);
        repository.save(medico);

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    //Método para funcionabilidade de fazer a listagem de médicos (READ)
    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        return ResponseEntity.ok(page);
    }

    //Método para funcionabilidade de fazer a busca por um médico especifico (READ)
    @GetMapping("/{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        //Carregar o medico no banco de dados
        var medico = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    //Método para funcionabilidade de atualizar um médico (UPDATE)
    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        //Carregar o medico no banco de dados chegando pelo DTO
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    //Método para funcionabilidade de exclusao logica um médico (DELETE)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {
        //Carregar o medico no banco de dados
        var medico = repository.getReferenceById(id);
        //Setando o atributo ativo para inativo
        medico.excluir();

        return ResponseEntity.noContent().build();
    }

}
