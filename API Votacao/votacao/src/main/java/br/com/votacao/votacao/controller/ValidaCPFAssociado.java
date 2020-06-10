package br.com.votacao.votacao.controller;

import br.com.votacao.votacao.exception.NotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Component
public class ValidaCPFAssociado {

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";

    /**
     * Valida se o CPF do associado esta habilitado para votacao.
     *
     * ABLE_TO_VOTE - Valido para votar
     *
     * UNABLE_TO_VOTE - Invalido para votar
     *
     * @param cpf - @{@link br.com.votacao.votacao.entity.Associado} CPF valido
     * @return - boolean
     */
    public boolean isVerificaCPFHabilitadoVotacao(String cpf) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl = "https://user-info.herokuapp.com/users/{cpf}";
            ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl, String.class, cpf);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode status = root.path("status");

            if (ABLE_TO_VOTE.equals(status.textValue())) {
                return Boolean.TRUE;
            }

            return Boolean.FALSE;

        } catch (HttpClientErrorException | IOException ex) {
            throw new NotFoundException("Não foi possivel localizar o CPF do associado");
        }
    }
}

