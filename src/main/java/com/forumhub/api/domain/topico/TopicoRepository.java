package com.forumhub.api.domain.topico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TopicoRepository extends JpaRepository<Topico, Long> {
    
    boolean existsByTituloAndMensagem(String titulo, String mensagem);
    
    @Query("SELECT t FROM Topico t WHERE t.curso = :curso AND YEAR(t.dataCriacao) = :ano")
    Page<Topico> findByCursoAndAno(@Param("curso") String curso, @Param("ano") int ano, Pageable pageable);
    
    @Query("SELECT t FROM Topico t WHERE t.curso = :curso")
    Page<Topico> findByCurso(@Param("curso") String curso, Pageable pageable);
    
    @Query("SELECT t FROM Topico t WHERE YEAR(t.dataCriacao) = :ano")
    Page<Topico> findByAno(@Param("ano") int ano, Pageable pageable);
}
