package br.com.votacao.votacao.dto;

import br.com.votacao.votacao.entity.Associado;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "AssociadoDTO")
@Data
@Builder
@AllArgsConstructor
@NoAr
@NoArgsConstructor
public class AssociadoDTO {
    private Integer id;

    @ApiModelProperty(value = "CPF válido referente ao associado")
    @CPF(message = "Não é um CPF valido")
    @NotBlank(message = "CPF do associado deve ser digitado")
    private String cpfAssociado;

    @ApiModelProperty(value = "ID da pauta a ser votada")
    @NotNull(message = "idPauta deve ser digitado")
    private Integer idPauta;

    public static Associado toEntity(AssociadoDTO dto) {
        return Associado.builder()
                .id(dto.getId())
                .cpfAssociado(dto.getCpfAssociado())
                .idPauta(dto.getIdPauta())
                .build();
    }
}
