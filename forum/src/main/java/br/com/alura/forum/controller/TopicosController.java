package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.TopicoDTO;
import br.com.alura.forum.controller.form.AtualizarTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

// REst controller nao evita o uso de responsebody em cada método do controller
@RestController
@RequestMapping ( value = "/topicos" )
public class TopicosController {
    
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    /**
     * How to use o Pageable
     * 
     * localhost:8080/topicos?page=0&size=3&sort=id,desc
     * 
     * se quiser ordenar mais de um atributo
     * 
     * localhost:8080/topicos?page=0&size=3&sort=id,desc&sort=dataCriacao,asc
     * Obs.: pode-se usar infinitos &sort=campo,'ordem ordenação'
     */
    @Cacheable ( value = "listaTopicos" )
    public Page < TopicoDTO > lista ( @RequestParam ( required = false ) String nomeCurso ,
            @PageableDefault ( sort = "id" , direction = Direction.DESC , page = 0 , size = 10 ) Pageable paginacao ) {
        
        // @RequestParam int pagina , @RequestParam int qtd , @RequestParam
        // String ordenacao
        // Pageable paginacao = PageRequest.of( pagina , qtd , Direction.DESC ,
        // ordenacao );

        if ( nomeCurso == null ) {
            Page < Topico > topicos = topicoRepository.findAll( paginacao );
            return TopicoDTO.from( topicos );
        } else {

            Page < Topico > topicos = topicoRepository.findByCursoNome( nomeCurso , paginacao );
            return TopicoDTO.from( topicos );
        }
    }
    
    @PostMapping
    // @Valid aplica as validações descritas no TopicoForm (@NotNull e etc)
    @Transactional
    @CacheEvict ( value = "listaTopicos" , allEntries = true )
    public ResponseEntity < TopicoDTO > cadastrar ( @RequestBody @Valid TopicoForm form , UriComponentsBuilder uriBuilder ) {
        
        Topico topico = form.toTopico( cursoRepository );

        topicoRepository.save( topico );
        
        URI uri = uriBuilder.path( "/topicos/{id}" ).buildAndExpand( topico.getId() ).toUri();
        return ResponseEntity.created( uri ).body( new TopicoDTO( topico ) );
    }

    @GetMapping ( "/{id}" )
    public ResponseEntity < TopicoDTO > detalhar ( @PathVariable Long id ) {
        
        Optional<Topico> optTopico = topicoRepository.findById( id );

        if ( optTopico.isPresent() )
            return ResponseEntity.ok( new TopicoDTO( optTopico.get() ) );
            
        return ResponseEntity.notFound().build();
    }

    @PutMapping ( "/{id}" )
    @Transactional
    @CacheEvict ( value = "listaTopicos" , allEntries = true )
    public ResponseEntity < TopicoDTO > atualizar ( @PathVariable Long id , @RequestBody @Valid AtualizarTopicoForm form ) {

        Optional < Topico > optTopico = topicoRepository.findById( id );

        if ( optTopico.isPresent() ) {

            Topico topico = form.atualizar( optTopico.get() );

            return ResponseEntity.ok( new TopicoDTO( topico ) );
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping ( "/{id}" )
    @Transactional
    @CacheEvict ( value = "listaTopicos" , allEntries = true )
    public ResponseEntity < ? > deletar ( @PathVariable Long id ) {

        Optional < Topico > optTopico = topicoRepository.findById( id );

        if ( optTopico.isPresent() ) {

            topicoRepository.deleteById( id );

            return ResponseEntity.ok().build();

        }

        return ResponseEntity.notFound().build();
    }
}
