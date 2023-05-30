package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.com.alura.forum.modelo.Topico;

public class AtualizarTopicoForm {

    @NotNull
    @NotEmpty
    @Length ( min = 5 )
    private String titulo;

    @NotNull
    @NotEmpty
    @Length ( min = 10 )
    private String mensagem;

    public AtualizarTopicoForm ( @NotNull @NotEmpty @Length ( min = 5 ) String titulo , @NotNull @NotEmpty @Length ( min = 10 ) String mensagem ) {
        super();
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public String getTitulo () {
        return titulo;
    }

    public void setTitulo ( String titulo ) {
        this.titulo = titulo;
    }

    public String getMensagem () {
        return mensagem;
    }

    public void setMensagem ( String mensagem ) {
        this.mensagem = mensagem;
    }

    public Topico atualizar ( Topico topicoAtualizar ) {

        topicoAtualizar.setTitulo( this.titulo );
        topicoAtualizar.setMensagem( this.mensagem );

        // Não precisa, pois o método foi carregado do banco de dados
        // topicoRepository.save(topico);

        return topicoAtualizar;
    }

}
