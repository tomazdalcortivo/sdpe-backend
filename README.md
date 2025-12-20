# SDPE - Sistema de Divulgação de Projetos de Extensão (Backend)

Este é o backend do sistema **SDPE**, desenvolvido para gerenciar e divulgar projetos de extensão acadêmica. A aplicação permite o cadastro de instituições, projetos, participantes e coordenadores, contando com um sistema de autenticação robusto e suporte para upload de imagens.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21
* **Framework:** Spring Boot 3.5.4
* **Segurança:** Spring Security com autenticação JWT (JSON Web Token)
* **Banco de Dados:** H2 Database (em memória para testes) e suporte para MySQL
* **Persistência:** Spring Data JPA com Hibernate
* **Outros:**
    * Lombok para redução de código boilerplate
    * Bean Validation para validação de dados
    * Maven como gerenciador de dependências

## 🛠️ Funcionalidades Principais

### 1. Gestão de Projetos
* Cadastro de projetos com suporte a upload de imagem.
* Listagem paginada e busca por ID.
* Download de imagens vinculadas aos projetos.
* Associação de coordenadores e participantes a projetos específicos.

### 2. Autenticação e Segurança
* Sistema de login e registro diferenciado por perfis: `ADMIN`, `COORDENADOR` e `PARTICIPANTE`.
* Geração e validação de tokens JWT com expiração de 2 horas.
* Criptografia de senhas com BCrypt.

### 3. Gestão de Usuários
* **Participantes:** Cadastro com validação de CPF e vínculo institucional.
* **Coordenadores:** Extensão de participantes com atribuições de cargo e função (Geral ou Adjunto).

### 4. Comunicação e Relatórios
* Sistema de contatos (Feedback e Chamados) vinculados aos projetos.
* Estrutura para geração de relatórios de inscritos e visualizações.

## 📄 Documentação Completa

O projeto conta com uma documentação acadêmica detalhando requisitos e modelagem:
[**➡ Clique aqui para acessar o PDF do Sistema (SDPE)**](./Sistema%20para%20Divulgação%20de%20Projetos%20de%20Extensão%20(SDPE).pdf)

## 📋 Pré-requisitos

* JDK 21 ou superior.
* Maven 3.9.11 (incluído via Maven Wrapper).

## 🛣️ Endpoints Principais (Exemplos)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| POST | `/auth/registrar` | Registra um novo usuário (Conta + Perfil). |
| POST | `/auth/login` | Realiza login e retorna o token JWT. |
| GET | `/api/projetos` | Lista todos os projetos (Paginado). |
| POST | `/api/projetos` | Cria um projeto (Multipart: JSON + Imagem). |
| GET | `/api/participantes/cpf/{cpf}` | Busca participante por CPF. |

## 📦 Como Executar

1.  Clone o repositório:
    ```bash
    git clone https://github.com/tomazdalcortivo/sdpe-backend.git
    ```
2.  Navegue até a pasta do projeto e execute:
    ```bash
    ./mvnw spring-boot:run
    ```
3.  Acesse o console do H2 em: `http://localhost:8080/h2-console` (Username: `sa`, sem senha).

---
*Este projeto foi desenvolvido como parte do sistema inicial de gerenciamento de extensão do IFPR.*
