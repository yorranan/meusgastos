package com.example.meusgastos.domain.dto.centrodecusto;

public class CentroDeCustoResponseDTO {
    private Long id;
    private String descricao;
    private String observacao;  

    public String getObservacao() {
        return observacao;
    }
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public String getDescricao() {
        return descricao;
    }
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
 
}

