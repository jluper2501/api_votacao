package br.com.votacao.votacao.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@ApiModel(value = "AbrirSessaoVotacaoDTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbrirSessaoVotacaoDTO {
    @ApiModelProperty(value = "ID da Pauta que que será aberta para votação")
    @NotNull(message = "idPauta deve ser digitado")
    private Integer idPauta;

    @ApiModelProperty(value = "Tempo em MINUTOS que a sessão de votação deverá ficar disponível")
    private Integer tempo;
}
