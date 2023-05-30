package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 
 * DOCUMENTAÇÃO DA CLASSE <br>
 * ---------------------- <br>
 * FINALIDADE: <br>
 * Service gerador de token de API REST <br>
 * HISTÓRICO DE DESENVOLVIMENTO: <br>
 * Sep 20, 2022 - @author davyfonseca - Primeira versão da classe. <br>
 * <br>
 * <br>
 * LISTA DE CLASSES INTERNAS: <br>
 */
@Service
public class TokenService {

    @Value ( "${forum.jwt.expiration}" )
    private String expiration;

    @Value ( "${forum.jwt.secret}" )
    private String secret;

    public String gerarToken ( Authentication authentication ) {

        Usuario usuario = ( Usuario ) authentication.getPrincipal();

        Date hoje = new Date();

        Date dataExpiracao = new Date( hoje.getTime() + Long.parseLong( expiration ) );

        return Jwts.builder() //
                .setIssuer( "API forum da Alura" ) //
                .setSubject( usuario.getId().toString() ) //
                .setIssuedAt( hoje ) //
                .setExpiration( dataExpiracao ) //
                .signWith( SignatureAlgorithm.HS256 , secret ) //
                .compact();
    }

    public boolean isTokenValido ( String token ) {

        try {

            Jwts.parser().setSigningKey( this.secret ).parseClaimsJws( token );

            return true;

        } catch ( Exception e ) {

            return false;
        }
    }

    public Long getIdUsuario ( String token ) {

        Claims body = Jwts.parser().setSigningKey( this.secret ).parseClaimsJws( token ).getBody();

        return Long.parseLong( body.getSubject() );
    }

}
