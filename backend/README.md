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
- Valor final = `diariaCalculadaParaHospedes * quantidadeDiarias`

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
└── dto/                           # DTOs de request/response
```
