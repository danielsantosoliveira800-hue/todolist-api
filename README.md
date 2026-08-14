# To-Do List API

API REST de gerenciamento de tarefas com autenticação, desenvolvida como projeto de aprendizado prático em Spring Boot — aplicando Clean Architecture, princípios SOLID e boas práticas de mercado desde o início.

> Projeto em desenvolvimento. Consulte [sprints-todolist-api.md](./sprints-todolist-api.md) para o planejamento completo e status de cada etapa.

## Sobre o projeto

Cada usuário se cadastra, faz login e gerencia suas próprias tarefas — criando, editando, listando, concluindo e removendo. Um usuário nunca tem acesso às tarefas de outro (autorização, não só autenticação).

## Tecnologias

- **Java 21**
- **Spring Boot** (Web, Data JPA, Security, Validation)
- **MySQL**
- **JWT** (autenticação stateless)
- **Lombok**
- **Maven**

## Arquitetura

O projeto segue uma adaptação de Clean Architecture, separando responsabilidades em camadas:

```
src
├── domain           → entidades, enums, interfaces de repository
├── application       → services (regras de negócio) e DTOs
├── infrastructure     → implementações técnicas (segurança, persistência)
└── presentation        → controllers (camada HTTP)
```

Princípio central: a camada `domain` não depende de Spring, JPA ou qualquer framework — as dependências apontam sempre para dentro, nunca para fora.

## Modelo de domínio

**Usuario**
- id, nome, email (único), senha (hash)

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

2. Configure as variáveis de ambiente `DB_USERNAME` e `DB_PASSWORD` com suas credenciais do MySQL (na sua IDE, em Run/Debug Configurations → Environment variables).

3. Rode a aplicação:
   ```
   ./mvnw spring-boot:run
   ```

A aplicação sobe em `http://localhost:8080`.

## Decisões de design

- **Senhas** nunca são salvas em texto puro — hash via BCrypt (Spring Security).
- **`dataCriacao`** é preenchida automaticamente pelo sistema, nunca informada pelo cliente da API.
- **`status`** de uma tarefa nasce sempre `PENDENTE` e só é alterado via endpoint dedicado (`PATCH`), não por edição livre.
- **`usuario`** de uma tarefa nunca vem no corpo da requisição — é sempre extraído do token JWT, evitando que um usuário crie/edite recursos em nome de outro.
- Credenciais de banco ficam fora do código-fonte, via variáveis de ambiente.

## Autor

Daniel — projeto de portfólio pessoal, desenvolvido como parte de um roadmap de aprendizado em backend com Spring Boot.