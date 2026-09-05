# To-Do List API

API REST de gerenciamento de tarefas com autenticação JWT, desenvolvida como projeto de aprendizado prático em Spring Boot — aplicando Clean Architecture, princípios SOLID e boas práticas de mercado do início ao fim.

> Projeto concluído. Consulte [sprints-todolist-api.md](./sprints-todolist-api.md) para o histórico completo de desenvolvimento, sprint a sprint.

## Sobre o projeto

Cada usuário se cadastra, faz login e gerencia suas próprias tarefas — criando, editando, listando, concluindo e removendo. Um usuário nunca tem acesso às tarefas de outro (autorização, não só autenticação).

## Tecnologias

- **Java 21**
- **Spring Boot 4** (Web, Data JPA, Security, Validation)
- **MySQL**
- **JWT** (autenticação stateless, via biblioteca jjwt)
- **Springdoc OpenAPI / Swagger UI** (documentação interativa)
- **JUnit 5 + Mockito** (testes unitários)
- **Lombok**
- **Maven**

## Arquitetura

O projeto segue uma adaptação de Clean Architecture, separando responsabilidades em camadas:

```
src
├── domain             → entidades, enums, interfaces de repository
├── application         → services (regras de negócio) e DTOs
├── infrastructure        → segurança (JWT, filtros), configuração técnica
└── presentation           → controllers e tratamento global de exceções
```

Princípio central: a camada `domain` não depende de Spring, JPA ou qualquer framework — as dependências apontam sempre para dentro, nunca para fora.

## Modelo de domínio

**Usuario**
- id, nome, email (único), senha (hash BCrypt)

**Tarefa**
- id, titulo, descricao, status (`PENDENTE` / `CONCLUIDA`), dataCriacao, usuario (dono)

Relação: um usuário possui várias tarefas (1:N).

## Endpoints

| Método | Rota | Descrição | Autenticação |
|---|---|---|---|
| POST | `/auth/registro` | Cria um novo usuário | Não |
| POST | `/auth/login` | Autentica e retorna um token JWT | Não |
| GET | `/tarefas` | Lista as tarefas do usuário logado | Sim |
| GET | `/tarefas/{id}` | Detalha uma tarefa (se for do dono) | Sim |
| POST | `/tarefas` | Cria uma nova tarefa | Sim |
| PUT | `/tarefas/{id}` | Edita título/descrição | Sim |
| PATCH | `/tarefas/{id}/status` | Alterna o status da tarefa | Sim |
| DELETE | `/tarefas/{id}` | Remove uma tarefa | Sim |

## Como rodar localmente

1. Crie um banco MySQL vazio:
   ```sql
   CREATE DATABASE todolist_db;
   ```

2. Configure as variáveis de ambiente `DB_USERNAME`, `DB_PASSWORD` e `JWT_SECRET` (na sua IDE, em Run/Debug Configurations → Environment variables).

3. Rode a aplicação:
   ```
   ./mvnw spring-boot:run
   ```

A aplicação sobe em `http://localhost:8080`.

## Documentação interativa (Swagger)

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

Todos os endpoints podem ser testados diretamente pela interface. Para rotas protegidas, gere um token via `POST /auth/login`, clique em **Authorize** no topo da página e cole o token (sem o prefixo `Bearer`).

## Testes

Testes unitários cobrindo as regras de negócio do `TarefaService`, com foco na regra de autorização (um usuário não pode acessar/alterar tarefas de outro):

```
./mvnw test
```

## Tratamento de erros

A API responde com um formato padronizado de erro (`timestamp`, `status`, `erro`, `mensagem`) para os principais cenários:

- **400** — dados inválidos ou regra de negócio violada (ex: e-mail já cadastrado)
- **401** — credenciais inválidas no login
- **403** — tentativa de acessar recurso de outro usuário

## Decisões de design

- **Senhas** nunca são salvas em texto puro — hash via BCrypt (Spring Security).
- **`dataCriacao`** é preenchida automaticamente pelo sistema, nunca informada pelo cliente da API.
- **`status`** de uma tarefa nasce sempre `PENDENTE` e só é alterado via endpoint dedicado (`PATCH`), não por edição livre.
- **`usuario`** de uma tarefa nunca vem no corpo da requisição — é sempre extraído do token JWT, evitando que um usuário crie/edite recursos em nome de outro.
- Credenciais de banco e chave JWT ficam fora do código-fonte, via variáveis de ambiente.
- Autenticação é **stateless**: nenhuma sessão é criada no servidor, cada requisição prova sua identidade via token.

## Próximos passos (fora do escopo deste projeto)

Este projeto foi desenhado como aquecimento prático em Spring Boot antes de um projeto maior de arquitetura (e-commerce), e como aquecimento antes de um projeto de dados/ML em Python. Não há roadmap de novas features planejado para este repositório.

## Autor

Daniel — projeto de portfólio pessoal, desenvolvido como parte de um roadmap de aprendizado em backend com Spring Boot.