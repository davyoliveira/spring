package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.alura.forum.repository.UsuarioRepository;

/**
 * 
 * DOCUMENTAÇÃO DA CLASSE <br>
 * ---------------------- <br>
 * FINALIDADE: <br>
 * Configurações de acesso ao servidor <br>
 * HISTÓRICO DE DESENVOLVIMENTO: <br>
 * Sep 20, 2022 - @author davyfonseca - Primeira versão da classe. <br>
 * <br>
 * <br>
 * LISTA DE CLASSES INTERNAS: <br>
 */
@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

    @Autowired
    AutenticacaoService autenticacaoService;

    @Autowired
    UsuarioRepository usuarioRepo;

    @Autowired
    TokenService tokenService;

    @Override
    @Bean
    protected AuthenticationManager authenticationManager () throws Exception {
        return super.authenticationManager();
    }

    // configuracoes de autorizacao
    @Override
    protected void configure ( HttpSecurity http ) throws Exception {

        http.authorizeRequests() //
                .antMatchers( HttpMethod.GET , "/topicos" ).permitAll() //
                .antMatchers( HttpMethod.GET , "/topicos/*" ).permitAll() //
                .antMatchers( HttpMethod.POST , "/auth" ).permitAll() //
                // apenas para teste habilitado pra geral, em prod nao pode
                .antMatchers( HttpMethod.GET , "/actuator/**" ).permitAll() //
                .anyRequest().authenticated() // os outros request tem que ser
                                              // autenticados
                .and().csrf().disable() // como ja vai ser feito via token,
                                         // desabilitar validação de atack
                // necessário para o spring nao criar sessão de usuário na
                // memória
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS ) //
                // adiciona o filtro criado para o token
                .and().addFilterBefore( new AutenticacaoViaTokenFilter( tokenService , usuarioRepo ) , UsernamePasswordAuthenticationFilter.class );
    }

    // configurcoes de autenticação
    @Override
    protected void configure ( AuthenticationManagerBuilder auth ) throws Exception {
        
        // passa o service falando que a senha está encriptografada
        auth.userDetailsService( autenticacaoService ).passwordEncoder( new BCryptPasswordEncoder() );
    }

    // configuracoes de recursos estaticos ( js, css, imagens, etc )
    @Override
    public void configure ( WebSecurity web ) throws Exception {
        // liberando endpoints do swagger, visto que sao muitas urls e ficaria
        // inviavel fazer isso no método linha 52
        web.ignoring().antMatchers( "/swagger-ui.html" , "/**.html" , "/v2/api-docs" , "/webjars/**" , "/configuration/**" , "/swagger-resources/**" );
    }
    // para descobrir a senha criptografada e colocar no banco no lugar de
    // 123456
    // public static void main ( String[] args ) {
    // System.out.print( new BCryptPasswordEncoder().encode( "123456" ) );
    // }
}
