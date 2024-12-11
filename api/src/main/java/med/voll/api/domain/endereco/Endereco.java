package med.voll.api.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {

    //Atributos
    private String logradouro;
    private String bairro;
    private String cep;
    private String numero;
    private String complemento;
    private String cidade;
    private String uf;

    //Construtor com argumentos
    public Endereco(DadosEndereco dados) {
        this.logradouro = dados.logradouro();
        this.cidade = dados.cidade();
        this.uf = dados.uf();
        this.cep = dados.cep();
        this.bairro = dados.bairro();
        this.numero = dados.numero();
        this.complemento = dados.complemento();
    }

    //Método para atualizar informacoes
    public void atualizarInformacoes(DadosEndereco dados) {
        //Condicao atualizar apenas se o campo estiver vindo no JSON
        if (dados.logradouro() != null) {
            this.logradouro = dados.logradouro();
        }
        if (dados.bairro() != null) {
            this.bairro = dados.bairro();
        }
        if (dados.cep() != null) {
            this.cep = dados.cep();
        }
        if (dados.uf() != null) {
            this.uf = dados.uf();
        }
        if (dados.numero() != null) {
            this.numero = dados.numero();
        }
        if (dados.complemento() != null) {
            this.complemento = dados.complemento();
        }
    }
}
