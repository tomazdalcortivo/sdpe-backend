# SDPE - Sistema de Divulgação de Projetos de Extensão (Backend)

Este repositório contém o **backend** do sistema SDPE, uma API RESTful desenvolvida para gerenciar, divulgar e monitorar projetos de extensão acadêmica. O sistema oferece controle de acesso baseado em perfis, gestão de conteúdo e relatórios estatísticos.

## 🚀 Tecnologias Utilizadas

O projeto foi construído utilizando as seguintes tecnologias e bibliotecas principais:

* **Java 21** & **Spring Boot 3.5.4**
* **Segurança:** Spring Security + JWT (JSON Web Token) para autenticação stateless.
* **Banco de Dados:** MySQL (Driver `mysql-connector-j`) com Spring Data JPA e Hibernate.
* **Documentação da API:** SpringDoc OpenAPI (Swagger UI).
* **Segurança Anti-Spam:** Integração com [Altcha](https://altcha.org/).
* **E-mail:** Java Mail Sender (SMTP Google).
* **Utilitários:** Lombok, Bean Validation.

## 🛠️ Funcionalidades

### 1. Gestão de Projetos e Conteúdo
* Cadastro completo de projetos com upload de imagens (armazenamento local/banco).
* Listagem paginada de projetos disponíveis.
* Sistema de busca e filtros.

### 2. Controle de Acesso (RBAC)
* **Perfis:** `ADMIN`, `COORDENADOR` e `PARTICIPANTE`.
* Registro de usuários com validação de dados.
* Recuperação de senha via e-mail.

### 3. Área Administrativa e Coordenação
* Painel de controle para aprovação e gestão de projetos.
* Vínculo de coordenadores e bolsistas a projetos.

### 4. Relatórios e Estatísticas
* Geração de estatísticas de visualização e inscrições.
* Endpoints dedicados para alimentar dashboards.

## 📋 Pré-requisitos

* **Java JDK 21** instalado.
* **Maven 3.9.x** (ou utilizar o wrapper `./mvnw` incluso).
* **MySQL Server** rodando na porta `3306`.

## ⚙️ Configuração

Antes de executar, verifique o arquivo `src/main/resources/application.yaml`. As configurações padrão esperam um banco MySQL local:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sdpe-bd
    username: root
    password: root # Altere conforme seu ambiente
