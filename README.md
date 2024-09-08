
---

# Documentação do Backend - Projeto de Iniciação Científica: Comparação entre SQL e NoSQL

## 1. Visão Geral do Projeto

Este projeto tem como objetivo explorar as diferenças entre bancos de dados SQL e NoSQL ao criar uma aplicação que utiliza o **PostgreSQL** para o armazenamento de informações de usuários e o **MongoDB** para gerenciar dados relacionados a filmes e favoritos dos usuários.

### Estrutura do Projeto
- **Banco de dados SQL (PostgreSQL)**: Utilizado para armazenar os dados de cadastro dos usuários (nome, login, senha e papéis).
- **Banco de dados NoSQL (MongoDB)**: Utilizado para armazenar informações sobre os filmes (título, descrição, gêneros, etc.) e os favoritos dos usuários.

### Tecnologias Usadas
- **Spring Boot**: Framework para a criação da aplicação backend.
- **PostgreSQL**: Banco de dados relacional para o armazenamento dos dados de usuários.
- **MongoDB**: Banco de dados NoSQL para armazenar os filmes e favoritos.
- **JWT (JSON Web Token)**: Usado para autenticação e autorização de usuários.
- **BCrypt**: Utilizado para criptografar senhas.

---

## 2. Escolha dos Bancos de Dados: PostgreSQL e MongoDB

### 2.1 PostgreSQL para Dados de Usuários
**Por que SQL?**
O banco de dados relacional **PostgreSQL** foi escolhido para armazenar os dados dos usuários devido à natureza sensível e estruturada dessas informações. Os principais motivos são:
- **Segurança e Integridade dos Dados**: A utilização de transações ACID garante a integridade e a segurança dos dados sensíveis.
- **Esquema Estruturado**: O PostgreSQL fornece um esquema rígido que garante consistência para dados bem definidos, como os dos usuários.
- **Validações de Integridade**: Validações como chaves primárias e login único ajudam a garantir a consistência dos dados.

### 2.2 MongoDB para Filmes e Favoritos
**Por que NoSQL?**
O banco de dados **MongoDB** foi escolhido para armazenar informações sobre filmes e favoritos dos usuários devido à sua flexibilidade e alta performance. Os principais motivos são:
- **Alta Performance e Escalabilidade**: MongoDB é eficiente no gerenciamento de grandes volumes de dados e consultas rápidas, adequando-se ao caso de uso de filmes e favoritos.
- **Flexibilidade do Esquema**: A ausência de um esquema rígido no MongoDB facilita o armazenamento de dados dinâmicos, como filmes com diferentes atributos.
- **Suporte a Estrutura Hierárquica**: O MongoDB é ideal para armazenar dados complexos e aninhados, como informações de filmes e listas de gêneros.
- **Escalabilidade Horizontal**: MongoDB facilita a distribuição dos dados em servidores múltiplos, garantindo desempenho e escalabilidade.

---

## 3. Endpoints da API

### 3.1 Autenticação
#### `/auth/login` - POST
- **Descrição**: Autentica um usuário com base no login e senha fornecidos.
- **Request Body**: `AuthenticationDTO`
  - `login`: (String) - Login do usuário.
  - `password`: (String) - Senha do usuário.
- **Response**: Retorna um `token` JWT em caso de sucesso.

#### `/auth/register` - POST
- **Descrição**: Registra um novo usuário.
- **Request Body**: `RegisterDTO`
  - `nome`: (String) - Nome do usuário.
  - `login`: (String) - Login do usuário.
  - `password`: (String) - Senha do usuário.
  - `role`: (String) - Papel do usuário (`ADMIN` ou `USER`).
- **Response**: Retorna um `token` JWT em caso de sucesso.

---

### 3.2 Filmes e Favoritos

#### `/favs` - POST
- **Descrição**: Adiciona ou remove um filme da lista de favoritos do usuário.
- **Request Body**: `FavRequestDTO`
  - `id`: (String) - ID do filme.
  - `token`: (String) - Token JWT do usuário.
- **Response**: Mensagem de sucesso ou erro.

#### `/favs/top10` - GET
- **Descrição**: Retorna os 10 filmes mais favoritos da plataforma.
- **Response**: Lista de objetos `PosterDTO` representando os 10 filmes mais populares.

#### `/movies/{id}` - GET
- **Descrição**: Retorna as informações detalhadas de um filme com base no ID.
- **Response**: Objeto `MovieResponseDTO` contendo as informações do filme.

#### `/movies/movies` - GET
- **Descrição**: Retorna os primeiros 10 filmes disponíveis na plataforma.
- **Response**: Lista de objetos `Movies`.

#### `/movies/search/{title}` - GET
- **Descrição**: Pesquisa filmes por título.
- **Response**: Lista de objetos `PosterDTO` contendo os resultados da pesquisa.

#### `/movies/homepage` - GET
- **Descrição**: Retorna uma lista personalizada de seções de filmes para a página inicial com base no histórico do usuário.
- **Response**: Lista de objetos `MovieSectionDTO` com seções de filmes, como favoritos, lançamentos e populares.

---

## 4. Modelos de Dados

### 4.1 **Usuário (SQL - PostgreSQL)**
A classe `Usuario` representa os dados do usuário no banco de dados relacional PostgreSQL.

- **Atributos**:
  - `id` (Integer): ID do usuário.
  - `nome` (String): Nome do usuário.
  - `login` (String): Login do usuário (único).
  - `password` (String): Senha criptografada.
  - `role` (UsuarioRole): Papel do usuário (ADMIN ou USER).

### 4.2 **Filmes (NoSQL - MongoDB)**
A classe `Movies` representa os dados de filmes armazenados no banco de dados NoSQL MongoDB.

- **Atributos**:
  - `id` (String): ID do filme.
  - `filmeId` (Integer): Identificador do filme.
  - `title` (String): Título do filme.
  - `overview` (String): Descrição do filme.
  - `posterPath` (String): Caminho da imagem do pôster.
  - `releaseDate` (String): Data de lançamento.
  - `voteAverage` (double): Nota média.
  - `voteCount` (int): Número de votos.
  - `popularity` (double): Popularidade do filme.
  - `adult` (boolean): Indica se o filme é para maiores de idade.
  - `genreIds` (List<Integer>): Lista de gêneros do filme.

### 4.3 **Favoritos (NoSQL - MongoDB)**
A classe `Favoritos` representa a relação de filmes favoritos dos usuários.

- **Atributos**:
  - `id` (String): ID do favorito.
  - `usuarioId` (int): ID do usuário que favoritou o filme.
  - `movieId` (String): ID do filme favorito.

---

## 5. Serviços

### 5.1 FavoritosService
Responsável pela lógica de negócio relacionada aos favoritos dos usuários.
- **Funções**:
  - `postFavoriteMovie(FavRequestDTO)`: Adiciona ou remove um filme dos favoritos.
  - `getTop10Movies()`: Retorna os 10 filmes mais favoritados da plataforma.

### 5.2 MoviesService
Responsável pela lógica de negócios relacionada aos filmes.
- **Funções**:
  - `findMovieById(String id)`: Retorna os detalhes de um filme pelo ID.
  - `getIndicacoes(List<Integer> genreIds)`: Retorna filmes baseados em preferências de gênero.
  - `getHomePage(String token)`: Retorna as seções de filmes personalizadas para a homepage.

---

## 6. Segurança

### 6.1 JWT (JSON Web Token)
O sistema utiliza JWT para autenticação e autorização. O token é gerado no login e registrado nas requisições subsequentes, garantindo que apenas usuários autenticados acessem os recursos protegidos.

### 6.2 BCrypt
O BCrypt é utilizado para criptografar as senhas antes de serem armazenadas no banco de dados PostgreSQL, garantindo segurança adicional aos dados dos usuários.

---

## 7. Configurações

### application.properties
Configuração para conectar com os bancos de dados PostgreSQL e MongoDB:
```properties
spring.data.mongodb.uri=mongodb://localhost:27017/netflix-clone
spring.datasource.url=jdbc:postgresql://localhost:5432/netflix-clone
spring.datasource.username=postgres
spring.datasource.password=senha
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=update
server.port=8081
```

---

