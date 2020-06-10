package br.com.votacao.votacao.dto;

import br.com.votacao.votacao.entity.Votacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "VotacaoDTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotacaoDTO {
    @ApiModelProperty(value = "ID da votação aberta")
    private Integer id;

    @ApiModelProperty(value = "ID da pauta aberta")
    private Integer idPauta;

    @ApiModelProperty(value = "ID da sessão aberta")
    private Integer idSessaoVotacao;

    @ApiModelProperty(value = "Voto")
    private Boolean voto;

    @ApiModelProperty(value = "Quantidade de votos SIM")
    private Integer quantidadeVotosSim;

    @ApiModelProperty(value = "Quantidade de votos NÃO")
    private Integer quantidadeVotosNao;

    public static Votacao toEntity(VotacaoDTO dto) {
        return Votacao.builder()
                .id(dto.getId())
                .idPauta(dto.getIdPauta())
                .idSessaoVotacao(dto.getIdSessaoVotacao())
                .voto(dto.getVoto())
                .build();
    }
}
