# ForumHub API

## Descrição
ForumHub é uma API REST desenvolvida em Spring Boot para gerenciamento de tópicos de um fórum. A aplicação oferece funcionalidades completas de CRUD (Create, Read, Update, Delete) para tópicos, além de autenticação e autorização via JWT (JSON Web Token).

## Funcionalidades

### 🔐 Autenticação
- Login de usuários com JWT
- Proteção de endpoints via token Bearer
- Validação automática de tokens em todas as requisições protegidas

### 📝 CRUD de Tópicos
- **Criar**: Cadastro de novos tópicos no fórum
- **Listar**: Listagem de tópicos com paginação e filtros
- **Detalhar**: Visualização de um tópico específico
- **Atualizar**: Edição de tópicos existentes
- **Excluir**: Remoção de tópicos

### 🔍 Funcionalidades Avançadas
- Paginação de resultados
- Filtros por curso e ano
- Ordenação por data de criação
- Validação de duplicação (mesmo título e mensagem)
- Documentação automática com Swagger/OpenAPI

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Security** (autenticação e autorização)
- **Spring Data JPA** (persistência de dados)
- **Spring Validation** (validação de dados)
- **MySQL** (banco de dados)
- **Flyway** (migração de banco de dados)
- **JWT** (JSON Web Token)
- **Lombok** (redução de boilerplate)
- **SpringDoc OpenAPI** (documentação da API)
- **Maven** (gerenciamento de dependências)

## Configuração do Ambiente

### Pré-requisitos
- Java 17 ou superior
- MySQL 8.0 ou superior
- Maven 3.6 ou superior

### Configuração do Banco de Dados
1. Crie um banco de dados MySQL chamado `forumhub_db`
2. Configure as credenciais no arquivo `application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/forumhub_db
spring.datasource.username=root
spring.datasource.password=root
```

### Executando a Aplicação
1. Clone o repositório
2. Configure o banco de dados
3. Execute o comando:
```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

## Documentação da API

A documentação completa da API está disponível via Swagger UI:
- **URL**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8080/v3/api-docs`

## Endpoints Principais

### Autenticação
- `POST /login` - Realizar login e obter token JWT

### Tópicos
- `POST /topicos` - Criar novo tópico
- `GET /topicos` - Listar tópicos (com paginação e filtros)
- `GET /topicos/{id}` - Detalhar tópico específico
- `PUT /topicos/{id}` - Atualizar tópico
- `DELETE /topicos/{id}` - Excluir tópico

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/forumhub/api/
│   │   ├── controller/          # Controllers REST
│   │   ├── domain/              # Entidades e regras de negócio
│   │   │   ├── topico/         # Domain do Tópico
│   │   │   └── usuario/        # Domain do Usuário
│   │   ├── infra/              # Infraestrutura
│   │   │   ├── exception/      # Tratamento de exceções
│   │   │   ├── security/       # Configurações de segurança
│   │   │   └── springdoc/      # Configurações do Swagger
│   │   └── ForumhubApplication.java
│   └── resources/
│       ├── db/migration/       # Scripts Flyway
│       └── application.properties
└── test/                       # Testes automatizados
```

## Regras de Negócio

### Tópicos
1. Todos os campos (título, mensagem, autor, curso) são obrigatórios
2. Não é permitido cadastrar tópicos duplicados (mesmo título e mensagem)
3. Tópicos são criados com status "NAO_RESPONDIDO" por padrão
4. Data de criação é definida automaticamente

### Autenticação
1. Apenas usuários autenticados podem acessar os endpoints de tópicos
2. Tokens JWT têm validade de 2 horas
3. Tokens devem ser enviados no header Authorization como "Bearer {token}"

## Usuários de Teste

A aplicação vem com usuários pré-cadastrados para teste:
- **Login**: `admin` | **Senha**: `123456`
- **Login**: `user` | **Senha**: `123456`

## Exemplo de Uso

### 1. Realizar Login
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{
    "login": "admin",
    "senha": "123456"
  }'
```

### 2. Criar Tópico
```bash
curl -X POST http://localhost:8080/topicos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {SEU_TOKEN}" \
  -d '{
    "titulo": "Dúvida sobre Spring Boot",
    "mensagem": "Como configurar autenticação JWT?",
    "autor": "João Silva",
    "curso": "Spring Boot"
  }'
```

### 3. Listar Tópicos
```bash
curl -X GET "http://localhost:8080/topicos?page=0&size=10" \
  -H "Authorization: Bearer {SEU_TOKEN}"
```

## Status Codes

- `200 OK` - Sucesso
- `201 Created` - Recurso criado com sucesso
- `400 Bad Request` - Dados inválidos ou duplicação
- `401 Unauthorized` - Token inválido ou ausente
- `404 Not Found` - Recurso não encontrado
- `500 Internal Server Error` - Erro interno do servidor

## Contribuição

1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença Apache 2.0. Veja o arquivo `LICENSE` para mais detalhes.
