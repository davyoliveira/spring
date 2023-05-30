package br.com.alura.forum.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/**
 * 
 * DOCUMENTAÇÃO DA CLASSE <br>
 * ---------------------- <br>
 * FINALIDADE: <br>
 * Validação de usuário em banco <br>
 * HISTÓRICO DE DESENVOLVIMENTO: <br>
 * Sep 20, 2022 - @author davyfonseca - Primeira versão da classe. <br>
 * <br>
 * <br>
 * LISTA DE CLASSES INTERNAS: <br>
 */
@Service
public class AutenticacaoService implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername ( String username ) throws UsernameNotFoundException {

        Optional < Usuario > optUsuario = usuarioRepo.findByEmail( username );

        if ( optUsuario.isPresent() ) {
            return optUsuario.get();
        }

        throw new UsernameNotFoundException( "Dados inválidos!" );
    }

}
