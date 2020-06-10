API Votação: br.com.votacao.votacao
foi utilizado o spring.io para criação do projeto em springboot.

Diretórios:
-config
-controllers
-dto
-entity
-exception
-repository
-schedule
-services

Tecnologias
-Java 8
-SpringBoot
-Mongodb 4.2
-Insonmia
-Intellij IDEA

Bando de dados: Mongodb

A Solução

Modelos/entidades
Pauta: é a pauta cadastrada
Associado: é o associado do cooperativismo cadastrado
Sessão de Votação: é a pauta aberta para votação
Voto: é o voto de cada associado para a pauta em sessão

A Aplicação

-Criação de nova pauta: O usuário responsável pela votação cria uma nova pauta adicionando título, descrição e o tempo de duração;

-Abertura da sessão para votação: Uma nova sessão de votação é aberta para uma pauta existente, adicionando-se um tempo limite para que a mesma fique aberta, 
caso o tempo não seja informado, o valor de 1 minuto é definido como padrão.

-Fechamento da sessão de votação: Uma sessão é fechada automaticamente após transcorrido o tempo previamente definido, com uso da referência de data de abertura 
acrescido do tempo em minutos definido para tempo de abertura.

-Cadastro dos associados para votação: Um associado pode ser previamente cadastrado no sistema, com nome e documento (CPF) para o seu registro, o mesmo será
validado para confirmação do seu cadastro.

-Computação do voto na sessão: O voto em uma sessão de votação pode ser feito com a opção: SIM ou NÃO, juntamente com a identificação do Associado e da pauta, 
sendo este por referência ou apenas com o número do documento, em caso de associado previamente cadastrado no sistema. Também é possível votar com 
informações de um usuário que ainda não está cadastrado, neste caso informa-se o Nome e o Documento (CPF) e o mesmo será automaticamente cadastrado 
e seu voto contabilizado.
Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta);

-Validação do CPF associado: O sistema, internamente, realizada validação do documento (CPF) do Associado da Cooperativa, caso o documento 
não esteja válido, a sua solicitação é recusada. Em caso positivo, o mesmo torna-se habilitado para votação.

Versionamento
Os endpoints são versionados pelo número major da versão (v1) diretamente na URL de acesso.
