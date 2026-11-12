# 🛒 Gerenciamento de Loja API

Projeto de gerenciamento de loja em evolução de **Java SE/JDBC** para uma **API REST com Spring Boot**, utilizando **Java 21**.

A base original reúne regras de negócio, persistência em MySQL, interface pelo terminal e testes automatizados. A migração reaproveita esses conhecimentos e acrescenta, por etapas, recursos do ecossistema Spring e contratos HTTP.

## 📌 Estado atual — API-002

A base Spring Boot está configurada e executa um servidor HTTP na porta **8080**.

- Spring Boot **4.1.2** com Java **21** e Spring MVC.
- Inicialização pela classe `GerenciamentoLojaApplication`.
- Maven Wrapper incluído no projeto.
- Geração e execução de JAR pelo terminal.
- **118 testes aprovados no IntelliJ**, build Maven concluído com `BUILD SUCCESS` e JAR executado com sucesso.

**Os endpoints de negócio ainda serão implementados.** As operações da loja descritas abaixo pertencem à implementação Java SE/JDBC, acessível pelo console. A configuração do datasource pelo Spring e a migração da persistência serão realizadas nas próximas etapas.

## 🛠️ Tecnologias utilizadas

| Tecnologia | Uso atual |
|---|---|
| Java 21 | Regras de negócio e implementação da aplicação |
| Spring Boot 4.1.2 | Inicialização e configuração da base web |
| Spring MVC | Base para os futuros endpoints HTTP |
| Tomcat embutido | Servidor HTTP da aplicação |
| Maven e Maven Wrapper | Dependências, compilação, testes e empacotamento |
| JDBC | Persistência e transações da implementação existente |
| MySQL | Armazenamento dos dados da loja |
| Docker | Execução local do MySQL em contêiner |
| Lombok | Geração de métodos auxiliares nos modelos |
| JUnit Jupiter / Spring Boot Starter Test | Testes de unidade e integração |
| Java NIO.2 | Manipulação de arquivos e exportação CSV |
| Git e GitHub | Versionamento e acompanhamento da evolução do projeto |

## ⚙️ Funcionalidades da implementação Java SE/JDBC

- Cadastro, listagem, busca e remoção de produtos e clientes.
- Criação e finalização de pedidos com múltiplos produtos.
- Cálculo de subtotais e totais utilizando `BigDecimal`.
- Preservação do preço histórico dos itens vendidos.
- Atualização do estoque com verificação da quantidade disponível.
- Transações com `commit` e `rollback`.
- Validação de dados e exceções personalizadas.
- Tratamento de cadastros duplicados e exclusões com vínculos.
- Consulta do faturamento total.
- Exportação de produtos para CSV.

Essas funcionalidades serão disponibilizadas por HTTP durante a migração.

## 🧠 Arquitetura e conceitos aplicados

O projeto utiliza orientação a objetos, encapsulamento, interfaces, generics e polimorfismo. A interface `Repository<T, ID>` define operações comuns de persistência, utilizadas por implementações em memória ou JDBC.

O código é organizado em modelos, serviços, repositórios, configuração, exceções e interface de usuário. Na implementação JDBC, a finalização de pedidos utiliza uma transação para registrar o pedido, seus itens e a atualização do estoque em conjunto. A redução de estoque utiliza SQL condicional para verificar a quantidade disponível.

Também são utilizados `BigDecimal` para valores monetários, `PreparedStatement` para consultas parametrizadas e `try-with-resources` para gerenciar recursos.

A classe `GerenciamentoLojaApplication` inicia a aplicação Spring Boot. A classe `Main` permanece como entrada do console durante a migração.

## 📂 Estrutura do projeto

Os pacotes Java estão em `src/main/java/com/gabriel/gerenciamentoloja/`.

| Caminho ou classe | Responsabilidade |
|---|---|
| `database/schema.sql` | Definições SQL do banco existente |
| `config/` | Configuração da conexão JDBC |
| `exception/` | Exceções personalizadas |
| `model/` | Modelos e regras do domínio |
| `repository/` | Interface genérica e repositórios |
| `repository/jdbc/` | Implementações de persistência JDBC |
| `service/` | Serviços e operações de negócio |
| `ui/` | Interface interativa pelo terminal |
| `GerenciamentoLojaApplication.java` | Inicialização do Spring Boot |
| `Main.java` | Inicialização do console e configuração manual das dependências |
| `src/test/java/` | Testes de unidade e integração |
| `.mvn/wrapper/maven-wrapper.properties` | Configuração da distribuição Maven usada pelo Wrapper |
| `mvnw` e `mvnw.cmd` | Scripts do Maven Wrapper |
| `pom.xml` | Dependências e plugins do projeto |

## 🚀 Preparar o ambiente

### Pré-requisitos

- **JDK 21** disponível no terminal.
- **Git** para clonar o repositório.
- **MySQL 8.4**, ou uma instalação compatível, para os testes JDBC e o console.
- **Docker**, caso o MySQL seja executado em contêiner.
- Uma IDE Java, como IntelliJ IDEA, é opcional para o build e a execução do JAR.

O Maven Wrapper baixa a distribuição Maven configurada pelo projeto. Não é necessário instalar o Maven separadamente; o primeiro uso requer acesso à internet.

Os comandos abaixo utilizam **Windows PowerShell**, inclusive o terminal integrado do IntelliJ. Execute-os na pasta que contém o `pom.xml`.

### 1. Clonar o repositório

```powershell
git clone https://github.com/GabrielGalaso/gerenciamento-loja-api.git
cd gerenciamento-loja-api
```

### 2. Conferir o Java e o Maven Wrapper

```powershell
java -version
.\mvnw.cmd -v
```

Confira se o Java utilizado pelo terminal e pelo Maven é o **Java 21**.

### 3. Preparar o MySQL

Nesta etapa, os testes JDBC e o console utilizam dois bancos separados:

| Banco | Finalidade |
|---|---|
| `gerenciamento_loja` | Uso da aplicação pelo console |
| `gerenciamento_loja_teste` | Execução dos testes de integração |

Se já houver uma instalação configurada, reutilize seu host, porta e usuário. Para um ambiente novo com Docker, este é um exemplo:

```powershell
docker run --name mysql-loja -e MYSQL_ROOT_PASSWORD=SENHA_ROOT -e MYSQL_DATABASE=gerenciamento_loja -e MYSQL_USER=loja_app -e MYSQL_PASSWORD=SENHA_APP -p 3307:3306 -v mysql-loja-dados:/var/lib/mysql -d mysql:8.4
```

Substitua `SENHA_ROOT` e `SENHA_APP` pelos valores do seu ambiente. O exemplo publica o MySQL na porta **3307** do computador e utiliza um volume para os dados.

Depois que o MySQL estiver pronto, entre no cliente SQL do contêiner:

```powershell
docker exec -it mysql-loja mysql -u root -p
```

Informe a senha de administrador escolhida na criação do contêiner. Para esse exemplo, crie o banco de testes e conceda acesso ao usuário `loja_app`:

```sql
CREATE DATABASE IF NOT EXISTS gerenciamento_loja_teste;
GRANT ALL PRIVILEGES ON gerenciamento_loja_teste.* TO 'loja_app'@'%';
```

O usuário criado pelo contêiner já recebe acesso ao banco informado em `MYSQL_DATABASE`; o acesso ao banco de testes é concedido separadamente. Em outra instalação, ajuste o usuário e o host da conta conforme sua configuração.

#### Preparar as tabelas

As definições das tabelas estão em `database/schema.sql`. Os dois bancos devem conter as tabelas `clientes`, `produtos`, `pedidos` e `itens_pedido`.

Nesta etapa, a preparação do esquema é manual. Em um cliente SQL, selecione o banco de destino e aplique as definições de criação das tabelas do arquivo, respeitando a ordem das dependências: clientes e produtos, depois pedidos e itens de pedido.

Para essa preparação, execute somente as definições de tabelas. A revisão da carga de exemplos e a inicialização reproduzível do banco fazem parte da **API-003**.

Utilize um banco exclusivo de testes, sem pedidos cadastrados manualmente: alguns cenários verificam que a listagem de pedidos está vazia. O isolamento e a limpeza do ambiente de testes também serão aprimorados na API-003.

### 4. Configurar o acesso ao banco de testes

Antes de executar a suíte ou gerar o JAR, configure as variáveis na mesma sessão do PowerShell:

```powershell
$env:LOJA_DB_URL = 'jdbc:mysql://127.0.0.1:3307/gerenciamento_loja_teste'
$env:LOJA_DB_USER = 'loja_app'
$env:LOJA_DB_PASSWORD = 'SENHA_APP'
```

Ajuste a porta, o usuário e a senha para o seu ambiente. Os valores acima correspondem ao exemplo com Docker.

Essas variáveis são utilizadas pela conexão JDBC existente. Variáveis definidas apenas na configuração JUnit do IntelliJ não são automaticamente aplicadas ao terminal. Mantenha credenciais reais fora do código e dos arquivos versionados.

### 5. Compilar, testar e gerar o JAR

Com o MySQL em execução e o banco de testes preparado:

```powershell
.\mvnw.cmd clean package
```

O comando limpa os resultados anteriores, compila o projeto, executa os testes e gera o JAR executável. Ao terminar com `BUILD SUCCESS`, o arquivo esperado é:

```text
target/gerenciamento-loja-api-1.0-SNAPSHOT.jar
```

O build precisa do banco porque executa os testes JDBC. Na API-002, a inicialização da base web ainda não estabelece uma conexão de datasource gerenciada pelo Spring.

## ▶️ Executar a aplicação Spring Boot

Após gerar o JAR:

```powershell
java -jar .\target\gerenciamento-loja-api-1.0-SNAPSHOT.jar
```

O console deve apresentar mensagens semelhantes a:

```text
Tomcat started on port 8080
Started GerenciamentoLojaApplication
```

O servidor fica disponível em [http://localhost:8080](http://localhost:8080) e permanece em execução até ser encerrado com **Ctrl+C**.

Como os endpoints de negócio ainda não foram criados, uma resposta **404** na raiz é esperada nesta etapa.

No IntelliJ, também é possível executar a classe `com.gabriel.gerenciamentoloja.GerenciamentoLojaApplication`. Para iniciar uma segunda execução na porta 8080, encerre a instância anterior.

## 🧪 Testes

A suíte utiliza **JUnit Jupiter**, fornecido pelo `spring-boot-starter-test`, para validar regras de negócio e operações de integração JDBC.

Para executar somente os testes, mantenha o banco de testes e as variáveis de ambiente preparados e rode:

```powershell
.\mvnw.cmd test
```

No IntelliJ, configure `LOJA_DB_URL`, `LOJA_DB_USER` e `LOJA_DB_PASSWORD` na configuração de execução dos testes, apontando para `gerenciamento_loja_teste`.

### Validação da API-002

- **118 testes aprovados no IntelliJ.**
- Build pelo Maven Wrapper concluído com **`BUILD SUCCESS`**.
- JAR gerado e executado pelo terminal com Tomcat na porta **8080**.

## 💻 Executar o console Java SE/JDBC

O menu interativo continua disponível durante a migração. Para utilizá-lo no IntelliJ, execute:

```text
com.gabriel.gerenciamentoloja.Main
```

Na configuração de execução dessa classe, utilize as variáveis correspondentes ao banco de uso do console:

```env
LOJA_DB_URL=jdbc:mysql://127.0.0.1:3307/gerenciamento_loja
LOJA_DB_USER=loja_app
LOJA_DB_PASSWORD=SENHA_APP
```

Ajuste os valores conforme sua instalação. A classe `Main` abre o menu de produtos, clientes, pedidos, relatórios e exportação; `GerenciamentoLojaApplication` inicia o servidor HTTP.

A exportação pelo console gera `exportacoes/produtos.csv`, com campos separados por ponto e vírgula, em relação ao diretório de execução.


## 🎯 Objetivo do projeto

Consolidar conhecimentos de **Java, orientação a objetos, JDBC, SQL, Spring Boot, APIs REST, testes automatizados e Git/GitHub**, construindo um projeto de portfólio e acompanhando sua evolução por entregas.

Este repositório evolui a partir do [projeto original em Java SE/JDBC](https://github.com/GabrielGalaso/gerenciamento-loja), preservando seu histórico como base para a migração.

## 📚 Documentação de referência

- [Maven Wrapper](https://maven.apache.org/tools/wrapper/)
- [Empacotamento executável com Spring Boot](https://docs.spring.io/spring-boot/maven-plugin/packaging.html)
- [MySQL com Docker](https://dev.mysql.com/doc/refman/8.4/en/docker-mysql-more-topics.html)
- [Permissões de acesso no MySQL](https://dev.mysql.com/doc/refman/8.4/en/grant.html)
