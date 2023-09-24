package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    //Injecao de dependencias
    @Autowired
    private MedicoRepository repository;

    //Método para funcionabilidade de cadastro de médico (CREATE)
    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {
        repository.save(new Medico(dados));
    }

    //Método para funcionabilidade de fazer a listagem de médicos (READ)
    @GetMapping
    public Page<DadosListagemMedico> listar(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao) {
        return repository.findAll(paginacao).map(DadosListagemMedico::new);
    }

    //Método para funcionabilidade de atualizar um médico (UPDATE)
    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {
        //Carregar o medico no banco de dados chegando pelo DTO
        var medico = repository.getReferenceById(dados.id());
        medico.atualizarInformacoes(dados);
    }

    //Método para funcionabilidade de exclusao logica um médico (DELETE)
    @DeleteMapping("/{id}")
    @Transactional
    public void excluir(@PathVariable Long id) {
        //Carregar o medico no banco de dados
        var medico = repository.getReferenceById(id);
        //Setando o atributo ativo para inativo
        medico.excluir();
    }

}
