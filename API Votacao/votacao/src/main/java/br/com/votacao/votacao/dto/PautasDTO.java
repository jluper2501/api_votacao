package br.com.votacao.votacao.dto;

import br.com.votacao.votacao.entity.Pautas;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "PautasDTO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PautasDTO {
    @ApiModelProperty(value = "ID Pauta", required = true)
    private Integer id;

    @ApiModelProperty(value = "Descrição referente o que será votado")
    @NotBlank(message = "Descrição deve ser digitada")
    private String descricao;

    public static Pautas toEntity(PautasDTO dto) {
        return Pauta.builder()
                .id(dto.getId())
                .descricao(dto.getDescricao())
                .build();
    }

    public static PautasDTO toDTO(Pautas pauta) {
        return PautasDTO.builder()
                .id(pauta.getId())
                .descricao(pauta.getDescricao())
                .build();
    }
}
