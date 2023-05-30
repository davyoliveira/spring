package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDTO;
import br.com.alura.forum.controller.form.LoginForm;

@RestController
@RequestMapping ( "/auth" )
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity < ? > autenticar ( @RequestBody @Valid LoginForm form ) {

        // converte dados login para entidade spring de login
        UsernamePasswordAuthenticationToken dadosLogin = form.converter();
        
        try {
            
            // autentica com entidade de login do spring
            Authentication authentication = authManager.authenticate( dadosLogin );

            // gera token co base na authentication
            String token = tokenService.gerarToken( authentication );

            // retorna token e tipo de algoritmo de token
            return ResponseEntity.ok( new TokenDTO( token , "Bearer" ) );
            
        } catch ( AuthenticationException e ) {
            
            return ResponseEntity.badRequest().build();
        }

    }
}
