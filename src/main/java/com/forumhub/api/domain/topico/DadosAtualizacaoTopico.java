package com.forumhub.api.domain.topico;

public record DadosAtualizacaoTopico(
        String titulo,
        String mensagem,
        String autor,
        String curso
) {}
