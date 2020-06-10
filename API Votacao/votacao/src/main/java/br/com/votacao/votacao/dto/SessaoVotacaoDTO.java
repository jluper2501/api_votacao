package br.com.votacao.votacao.dto;

import br.com.votacao.votacao.entity.SessaoVotacao;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(value = "SessaoVotacaoDTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessaoVotacaoDTO {
    @ApiModelProperty(value = "ID da sessão aberta")
    private Integer id;

    @ApiModelProperty(value = "Data Hora de início da sessão aberta")
    private LocalDateTime dataHoraInicio;

    @ApiModelProperty(value = "Data Hora de fim sessão aberta")
    private LocalDateTime dataHoraFim;

    @ApiModelProperty(value = "Status da sessão aberta")
    private Boolean ativa;


    public static SessaoVotacao toEntity(SessaoVotacaoDTO dto) {
        return SessaoVotacao.builder()
                .id(dto.getId())
                .dataHoraInicio(dto.getDataHoraInicio())
                .dataHoraFim(dto.getDataHoraFim())
                .ativa(dto.getAtiva())
                .build();
    }

    public static SessaoVotacaoDTO toDTO(SessaoVotacao sessaoVotacao) {
        return SessaoVotacaoDTO.builder()
                .id(sessaoVotacao.getId())
                .dataHoraInicio(sessaoVotacao.getDataHoraInicio())
                .dataHoraFim(sessaoVotacao.getDataHoraFim())
                .ativa(sessaoVotacao.getAtiva())
                .build();

    }
}
