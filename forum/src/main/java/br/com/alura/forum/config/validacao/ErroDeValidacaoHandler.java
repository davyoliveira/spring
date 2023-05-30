package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.alura.forum.controller.TopicosController;

/**
 * 
 * DOCUMENTAÇÃO DA CLASSE <br>
 * ---------------------- <br>
 * FINALIDADE: <br>
 * Classe de definição de mensagens de erro quando argumento não for válido em
 * chamada REST, vide {@link TopicosController}} <br>
 * <br>
 * HISTÓRICO DE DESENVOLVIMENTO: <br>
 * Sep 16, 2022 - @author davyfonseca - Primeira versão da classe. <br>
 * <br>
 * <br>
 * LISTA DE CLASSES INTERNAS: <br>
 */
@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource;

    @ResponseStatus ( code = HttpStatus.BAD_REQUEST )
    @ExceptionHandler ( MethodArgumentNotValidException.class )
    public List < ErroFormDTO > handle ( MethodArgumentNotValidException ex ) {

        List < ErroFormDTO > dtos = new ArrayList <>();

        List < FieldError > fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.forEach( erro -> {
            String mensagem = messageSource.getMessage( erro, LocaleContextHolder.getLocale()  );
            ErroFormDTO erroDto = new ErroFormDTO( erro.getField() , mensagem );
            dtos.add( erroDto );
        });
        
        return dtos;
    }
}
