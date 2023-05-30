package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {
    
    private TokenService tokenService;

    private UsuarioRepository usuarioRepo;

    public AutenticacaoViaTokenFilter ( TokenService tokenService , UsuarioRepository usuarioRepo ) {
        this.tokenService = tokenService;
        this.usuarioRepo = usuarioRepo;
    }

    // método que valida token rebecido a cada chamada e libera ou nao a entrada
    // na area restrita
    @Override
    protected void doFilterInternal ( HttpServletRequest request , HttpServletResponse response , FilterChain filterChain ) throws ServletException , IOException {

        String token = recuperarToken( request );

        if ( tokenService.isTokenValido( token ) ) {
            autenticarCliente( token );
        }

        filterChain.doFilter( request , response );
    }

    // autentica cliente
    private void autenticarCliente ( String token ) {

        Long idUsuario = tokenService.getIdUsuario( token );

        Usuario usuario = usuarioRepo.findById( idUsuario ).get();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( usuario , null , usuario.getAuthorities() );

        SecurityContextHolder.getContext().setAuthentication( authentication );
    }

    // recupera token de request
    private String recuperarToken ( HttpServletRequest request ) {

        String token = request.getHeader( "Authorization" );

        if ( token == null || token.isEmpty() || ! token.startsWith( "Bearer" ) ) {
            return null;
        }

        return token.substring( 7 , token.length() );
    }

}
