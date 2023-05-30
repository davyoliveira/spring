package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotNull;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class LoginForm {

    @NotNull
    private String email;

    @NotNull
    private String senha;

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getSenha () {
        return senha;
    }

    public void setSenha ( String senha ) {
        this.senha = senha;
    }

    public UsernamePasswordAuthenticationToken converter () {
        return new UsernamePasswordAuthenticationToken( email , senha );
    }

}
