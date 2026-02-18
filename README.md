# SDPE - Backend API

**SDPE - Sistema de Divulgação de Projetos de Extensão (Backend)**

Este repositório contém o backend do sistema **SDPE**, uma API RESTful desenvolvida para **gerenciar, divulgar e monitorar projetos de extensão acadêmica**. O sistema oferece **controle de acesso baseado em perfis**, **gestão de conteúdo** e **relatórios estatísticos**.

O **SDPE (Sistema de Divulgação de Projetos de Extensão)** é uma API RESTful desenvolvida para gerenciar o ciclo de vida de projetos de extensão acadêmica do IFPR. O sistema fornece endpoints para autenticação segura, gestão de usuários (RBAC), submissão de projetos, upload de documentos e geração de estatísticas.

---

## 📋 Índice

* [Visão Geral](#-visão-geral)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Pré-requisitos](#-pré-requisitos)
* [Configuração do Ambiente](#-configuração-do-ambiente)
* [Instalação e Execução](#-instalação-e-execução)
* [Documentação da API (Swagger)](#-documentação-da-api-swagger)
* [Estrutura do Projeto](#-estrutura-do-projeto)

---

## 🔭 Visão Geral

O backend serve como o núcleo de processamento para a plataforma SDPE, gerenciando:

* **Autenticação e Autorização:** Controle de acesso baseado em cargos (`ADMIN`, `COORDENADOR`, `PARTICIPANTE`) via JWT.
* **Gestão de Projetos:** CRUD completo de projetos, incluindo upload de imagens e arquivos PDF.
* **Relatórios:** Geração de dados estatísticos sobre visualizações e inscrições.
* **Segurança:** Proteção contra ataques automatizados via integração com Altcha.

---

## 🚀 Tecnologias Utilizadas

O projeto foi construído utilizando as seguintes tecnologias e bibliotecas principais:

* **Java 21** & **Spring Boot 3.5.4**
* **Segurança:** Spring Security + [JWT](https://www.jwt.io/) (JSON Web Token) para autenticação stateless
* **Banco de Dados:** MySQL (Driver `mysql-connector-j`) com Spring Data JPA e Hibernate
* **Documentação da API:** SpringDoc OpenAPI [(Swagger UI)](https://swagger.io/)
* **Segurança Anti-Spam:** Integração com [Altcha](https://altcha.org/)
* **E-mail:** Java Mail Sender (SMTP Google)
* **Utilitários:** Lombok, Bean Validation

---

## 🛠️ Funcionalidades

### 1. Gestão de Projetos e Conteúdo

* Cadastro completo de projetos com upload de imagens (armazenamento local/banco)
* Listagem paginada de projetos disponíveis
* Sistema de busca e filtros

### 2. Controle de Acesso (RBAC)

* Perfis: `ADMIN`, `COORDENADOR` e `PARTICIPANTE`
* Registro de usuários com validação de dados
* Recuperação de senha via e-mail

### 3. Área Administrativa e Coordenação

* Painel de controle para aprovação e gestão de projetos
* Vínculo de coordenadores e bolsistas a projetos

### 4. Relatórios e Estatísticas

* Geração de estatísticas de visualização e inscrições
* Endpoints dedicados para alimentar dashboards

---

## 📦 Pré-requisitos

Antes de iniciar, certifique-se de ter instalado em sua máquina:

1. **Java JDK 21** ou superior
2. **MySQL Server** rodando na porta `3306`
3. **Maven 3.9+** (opcional, pois o projeto inclui o `mvnw`)
4. **Git**

---

## ⚙️ Configuração do Ambiente

### 1. Arquivo de Configuração (`application.yaml`)

As configurações principais estão em `src/main/resources/application.yaml`. Verifique se as credenciais do banco correspondem ao seu ambiente local:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sdpe-bd
    username: root   # Ajuste conforme seu usuário
    password: root   # Ajuste conforme sua senha
```

### 3. Variáveis de Ambiente (Recomendado)

Para funcionalidades de e-mail e segurança (Altcha), configure as seguintes variáveis de ambiente ou edite o arquivo `application.yaml` diretamente (não recomendado para produção):

* `spring.mail.username`: Seu e-mail remetente
* `spring.mail.password`: Senha de aplicativo do e-mail
* `altcha.secret-key`: Chave secreta para validação do widget Altcha

---

## 🛠️ Instalação e Execução

Siga os passos abaixo para rodar a aplicação localmente:

### 1. Clone o repositório

```bash
git clone https://github.com/tomazdalcortivo/sdpe-backend.git
cd sdpe-backend
```

### 2. Instale as dependências

Utilize o Maven Wrapper incluído para garantir a compatibilidade:

```bash
./mvnw clean install
```

> No Windows, utilize:
>
> ```bash
> mvnw.cmd clean install
> ```

### 3. Execute a aplicação

```bash
./mvnw spring-boot:run
```

### 4. Verifique a execução

O servidor iniciará na porta **8080**. Acesse:

```
http://localhost:8080
```

---

## 📖 Documentação da API (Swagger)

A API possui documentação interativa gerada automaticamente pelo Swagger. Com o servidor rodando, acesse:

👉 `http://localhost:8080/swagger-ui/index.html`

### Principais Endpoints

| Módulo   | Método | Rota                            | Descrição                                                 |
| -------- | ------ | ------------------------------- | --------------------------------------------------------- |
| Auth     | POST   | `/auth/login`                   | Realiza login e retorna o Token JWT                       |
| Auth     | POST   | `/auth/registrar`               | Cria uma nova conta de usuário                            |
| Projetos | GET    | `/api/projetos`                 | Lista todos os projetos (público)                         |
| Projetos | POST   | `/api/projetos`                 | Cria um novo projeto requer o coordenador (requer token)  |
| Admin    | GET    | ` /api/admin/painel-dministrativo ` | Dados para dashboard administrativo                   |

---

## 📂 Estrutura do Projeto

```plaintext
src/main/java/br/com/ifpr/edu/sdpe_backend
├── controller       # Camada de endpoints REST (API)
├── domain           # Entidades JPA e DTOs
├── repository       # Interfaces de acesso ao banco de dados
├── service          # Regras de negócio
├── infra
│   ├── config       # Configurações (Swagger, Web, etc.)
│   └── security     # Configuração de Segurança e Filtros JWT
└── SdpeBackendApplication.java
```
# 🔐 Criação do Usuário ADMIN Inicial

Este documento descreve **apenas o processo de criação do usuário ADMIN inicial** do sistema.  
Esse usuário será responsável por aprovar outros usuários e realizar cadastros administrativos via interface do sistema.

---

## 1. Instalação do Postman CLI

Para realizar a chamada de registro via terminal, instale o Postman CLI:

```bash
npm install -g postman-cli
```
2. Registro do Usuário ADMIN via API

Execute o comando abaixo para criar o usuário administrador inicial.

Importante:
Ajuste o caminho do arquivo PDF em arquivo=@"/C:/Users/seu_arquivo.pdf" para um arquivo existente em sua máquina.
```bash
postman request POST 'http://localhost:8080/auth/registrar' \
  --form 'dados={
    "email":"admin.sistema@ifpr.edu.br",
    "senha":"admin123",
    "perfil":"ADMIN",
    "nome":"Roberto Henrique Lima",
    "dataNascimento":"1975-06-15",
    "cpf":"604.059.350-48",
    "cidade":"Curitiba",
    "estado":"PR",
    "ativo":true,
    "vinculoInstitucional":true
  }' \
  --form 'arquivo=@"/C:/Users/seu_arquivo.pdf"'
```
3. Ativação Manual do Usuário no Banco de Dados

Caso o usuário seja criado com o status inativo, ative-o diretamente no banco de dados
(apenas para ambiente de desenvolvimento):

```
UPDATE tb_conta SET ativo = true;
```
4. Login com o Usuário ADMIN

Após a ativação, utilize as credenciais abaixo para acessar o sistema:

Email: admin.sistema@ifpr.edu.br
Senha: admin123

5. Função do Usuário ADMIN

Após o primeiro login, o usuário ADMIN poderá:
  - Aprovar usuários cadastrados no sistema
  - Cadastrar professores, alunos e coordenadores
  - Aprovar cadastros diretamente pela interface administrativa
  - Gerenciar acessos e permissões do sistema
