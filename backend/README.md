# Sistema de Hospedagem - Backend

API REST do sistema de hospedagem desenvolvido na disciplina de Programação Modular - PUC Minas - Engenharia de Software.

## Stack

- Java 17 (compila tambem em JDK 21/25)
- Spring Boot 3.4.0
- Spring Data JPA + Hibernate
- MySQL 8 (producao) / H2 em memoria (desenvolvimento)
- Maven

## Como rodar

O projeto inclui o Maven Wrapper, entao **nao precisa instalar Maven**. Voce so precisa ter o JDK 17+ instalado.

### Modo desenvolvimento (H2 em memoria - padrao)

Nao precisa de banco de dados rodando. Basta executar:

```powershell
.\mvnw spring-boot:run
```

A aplicacao sobe na porta 8080. Console do H2 disponivel em:
http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:hospedagem`
- User: `sa`
- Password: (vazio)

### Modo MySQL

1. Suba um MySQL local na porta 3306 com usuario `root` e senha `root` (ou ajuste em `src/main/resources/application-mysql.properties`).
2. Execute com o profile `mysql`:

```powershell
.\mvnw spring-boot:run -Dspring-boot.run.profiles=mysql
```

O banco `hospedagem` sera criado automaticamente na primeira execucao.

## Endpoints

Todos os endpoints retornam JSON.

### Quartos - `/quartos`

| Metodo | Path                            | Descricao                                  |
| ------ | ------------------------------- | ------------------------------------------ |
| GET    | `/quartos`                      | Lista todos os quartos                     |
| GET    | `/quartos/{id}`                 | Busca quarto por ID                        |
| POST   | `/quartos`                      | Cria um quarto                             |
| PUT    | `/quartos/{id}`                 | Atualiza um quarto                         |
| DELETE | `/quartos/{id}`                 | Remove um quarto                           |
| GET    | `/quartos/{id}/disponibilidade` | Verifica disponibilidade em um periodo     |
| GET    | `/quartos/{id}/diaria`          | Calcula a diaria prevista                  |

#### Exemplos de criacao

**Quarto Individual**:
```json
POST /quartos
{
  "tipo": "INDIVIDUAL",
  "valorBase": 120.00,
  "possuiAR": true,
  "possuiHidro": false,
  "quantidadeCamasSolteiro": 2,
  "adicionalPorCamaExtra": 30.00
}
```

**Quarto Duplo (Casal)**:
```json
POST /quartos
{
  "tipo": "DUPLO",
  "valorBase": 200.00,
  "possuiAR": true,
  "possuiHidro": true,
  "tipoCama": "QUEEN",
  "possuiBerco": true
}
```

**Quarto Familia**:
```json
POST /quartos
{
  "tipo": "FAMILIA",
  "valorBase": 350.00,
  "possuiAR": true,
  "possuiHidro": false,
  "quantidadeAmbientes": 2,
  "camas": [
    { "tipo": "CASAL",   "quantidade": 1 },
    { "tipo": "SOLTEIRO", "quantidade": 3 }
  ]
}
```

### Alugueis - `/alugueis`

| Metodo | Path                    | Descricao                                       |
| ------ | ----------------------- | ----------------------------------------------- |
| GET    | `/alugueis`             | Lista todos os alugueis                         |
| GET    | `/alugueis/{id}`        | Busca aluguel por ID                            |
| POST   | `/alugueis`             | Cria um aluguel (calcula valor automaticamente) |
| DELETE | `/alugueis/{id}`        | Cancela um aluguel                              |
| GET    | `/alugueis/{id}/recibo` | Imprime o formulario do aluguel                 |

#### Exemplo de criacao

```json
POST /alugueis
{
  "clienteId": 1,
  "quartoId": 1,
  "residenciaId": 1,
  "dataEntrada": "2026-06-01T14:00:00",
  "dataSaida": "2026-06-05T11:00:00",
  "numeroHospedes": 2
}
```

### Clientes - `/clientes` *(implementado pelo Pedro)*

| Metodo | Path             | Descricao                |
| ------ | ---------------- | ------------------------ |
| GET    | `/clientes`      | Lista todos              |
| GET    | `/clientes/{id}` | Busca por ID             |
| GET    | `/clientes/{id}/historico` | Lista o historico de alugueis |
| POST   | `/clientes`      | Cria um cliente          |
| PUT    | `/clientes/{id}` | Atualiza um cliente      |
| DELETE | `/clientes/{id}` | Remove um cliente        |

### Residencias - `/residencias` *(implementado pelo Pedro)*

| Metodo | Path                                  | Descricao                              |
| ------ | ------------------------------------- | -------------------------------------- |
| GET    | `/residencias`                        | Lista todas                            |
| GET    | `/residencias/{id}`                   | Busca por ID                           |
| POST   | `/residencias`                        | Cria uma residencia                    |
| PUT    | `/residencias/{id}`                   | Atualiza uma residencia                |
| DELETE | `/residencias/{id}`                   | Remove uma residencia                  |
| POST   | `/residencias/{id}/quartos`           | Adiciona um quarto a residencia        |
| GET    | `/residencias/{id}/quartos-disponiveis` | Lista quartos disponiveis no periodo |
| GET    | `/residencias/{id}/historico`         | Historico de alugueis da residencia    |

## Regras de calculo

### Quarto Individual
- Diaria = `valorBase + (camasExtras * adicionalPorCamaExtra) + adicionais`
- `camasExtras = max(0, quantidadeCamasSolteiro - 1)` (a primeira cama ja esta no valor base)
- Capacidade = numero de camas
- Nao permite berco

### Quarto Duplo
- Diaria = `valorBase + adicionalConforto + (taxaBerco se possuir) + adicionais`
- Adicional de conforto: CASAL=0, QUEEN=60, KING=100
- Taxa berco = 40 (so cobrada se `possuiBerco=true`)
- Capacidade = 2 hospedes

### Quarto Familia
- Diaria base = `valorBase * (1 + 0.05 * numeroHospedes)` (5% a mais por hospede)
- Soma valor por ambiente extra (70 por ambiente)
- Soma adicionais (AR, hidromassagem)
- Aplica desconto progressivo no total:
  - 4-5 hospedes: 5%
  - 6-7 hospedes: 10%
  - 8+ hospedes: 15%
- Capacidade = soma das camas (solteiro=1, casal/queen/king=2 cada)

### Adicionais comuns
- Ar condicionado: +50
- Hidromassagem: +80

### Calculo de diarias (Aluguel)
- Numero de dias entre entrada e saida
- Se saida apos 12h, soma +1 diaria
- Valor final = `diariaComTarifa * quantidadeDiarias`

## Tarifacao flexivel (Sprint 4)

O valor da diaria passa por uma politica de tarifacao antes de compor o valor final.
As politicas seguem o padrao **Strategy** (`PoliticaTarifa`) e a politica vigente e
mantida pelo **Singleton** `GerenciadorTarifas` — a tarifa e um recurso global: todos
os calculos do sistema (previsao de diaria e valor final do aluguel) precisam aplicar
a mesma regra ao mesmo tempo.

Politicas disponiveis:

| Nome              | Ajuste                    |
| ----------------- | ------------------------- |
| `PADRAO`          | sem ajuste (padrao)       |
| `ALTA_TEMPORADA`  | +30% na diaria            |
| `BAIXA_TEMPORADA` | -20% na diaria            |
| `PROMOCIONAL`     | -10% na diaria            |

Novas regras (feriados, eventos, descontos especiais) sao adicionadas criando uma
classe que implementa `PoliticaTarifa` e registrando-a no gerenciador, sem alterar
o codigo existente.

### Endpoints - `/tarifas`

| Metodo | Path             | Descricao                                    |
| ------ | ---------------- | -------------------------------------------- |
| GET    | `/tarifas`       | Lista as politicas e mostra qual esta ativa  |
| PUT    | `/tarifas/ativa` | Ativa uma politica (`?politica=NOME`)        |

Exemplo:

```bash
# Ativa alta temporada
curl -X PUT "http://localhost:8080/tarifas/ativa?politica=ALTA_TEMPORADA"

# A partir daqui as diarias e novos alugueis usam +30%
curl "http://localhost:8080/quartos/1/diaria"
```

## Meios de pagamento (Sprint 4)

Cada meio de pagamento possui uma Strategy responsável por suas regras de
validação e processamento. A `ProcessadorPagamentoFactory` seleciona a Strategy
correta para `DINHEIRO`, `CARTAO_CREDITO`, `CARTAO_DEBITO` ou `PIX`.

### Endpoints - `/pagamentos`

| Metodo | Path                      | Descricao                        |
| ------ | ------------------------- | -------------------------------- |
| POST   | `/pagamentos`             | Processa e confirma um pagamento |
| GET    | `/pagamentos`             | Lista os pagamentos              |
| GET    | `/pagamentos/{id}`        | Busca um pagamento por ID        |
| GET    | `/pagamentos/{id}/recibo` | Emite o recibo em texto           |

Exemplo:

```bash
curl -X POST "http://localhost:8080/pagamentos" \
  -H "Content-Type: application/json" \
  -d '{"aluguelId":1,"formaPagamento":"PIX"}'

curl "http://localhost:8080/pagamentos/1/recibo"
```

## Estrutura

```
src/main/java/br/pucminas/hospedagem/
├── HospedagemApplication.java     # Main
├── model/                         # Entidades JPA
│   ├── Quarto.java (abstract)
│   ├── QuartoIndividual.java
│   ├── QuartoDuplo.java
│   ├── QuartoFamilia.java
│   ├── Cama.java
│   ├── TipoCama.java (enum)
│   ├── TipoCamaCasal.java (enum)
│   ├── Aluguel.java
│   ├── Cliente.java
│   └── Residencia.java
├── repository/                    # Spring Data JPA
├── service/                       # Regras de negocio
├── controller/                    # Endpoints REST
├── dto/                           # DTOs de request/response
├── exception/                     # Excecoes personalizadas + handler global
└── tarifa/                        # Strategy de tarifacao + Singleton GerenciadorTarifas
```
