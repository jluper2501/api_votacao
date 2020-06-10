package br.com.votacao.votacao.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "VotoDTO")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotoDTO {
    @ApiModelProperty(value = "ID da pauta aberta")
    @NotNull(message = "idPauta deve ser digitado")
    private Integer idPauta;

    @ApiModelProperty(value = "ID da sessão aberta")
    @NotNull(message = "idSessaoVotacao deve ser digitado")
    private Integer idSessaoVotacao;

    @ApiModelProperty(value = "Voto")
    @NotNull(message = "Voto deve ser marcado")
    private Boolean voto;

    @ApiModelProperty(value = "CPF valido")
    @CPF(message = "Não é um CPF valido")
    @NotBlank(message = "cpf do associado deve ser digitado")
    private String cpfAssociado;
}
