# Arquitetura proposta — Controle Financeiro (simples)

Este documento descreve uma forma prática de organizar o projeto existente no programa Moneysy

**Visão geral**
- Objetivo: permitir que usuários registrem despesas com valor e tags separadas por Tags
- Microserviços disponiveis:
  - `users` — registro, login e ajuste de dados.
  - `expenses` - registro de valores de crédito e débito
    - `Balance` - Entidade que controla entrada e saida de valores.
    - `Spend` - Entidade de controle de Gastos contabiliza se trata-se de Um debito ou Crédito na expense Crédito.
    - `Tag` - Entidade que controla as Tags criadas pelo usuario podendo, viabilizando a seção de CRUD de tags.
    Obs: importante ressaltar que somente o usuario pode tirar ou adicionar gastos referente ao Id que é setado nas chamadas de API usando o token gerado no login

**Modelos de domínio (essencial)**
- User
  - id (UUID/Long)
  - email
  - passwordHash
  - createdAt
- Expenses
  - id
  - userId (não-null)
  - amount (decimal)
  - description
  - occurredAt (datetime)
  - tags: String

**Autenticação e autorização**
- Fluxo: usuário se registra no `users` -> ao fazer login recebe JWT com `sub=userId` e claims mínimos.
- `expenses` e `tags` validam o JWT em cada request e extraem `userId` do token.
- Regras básicas:
  - Apenas dono da despesa pode editar/remover.
  - Tags podem ser globais (visíveis a todos) ou privadas (ownerId diferente de null).

**Endpoints sugeridos (REST)**
- Serviço `users` (base `/api/users`)
  - POST `/register` -> body: {email, password, name} -> 201
  - POST `/login` -> body: {email, password} -> 200 {token}
  - GET `/me` -> retorna perfil (auth required)

- Serviço `tags` (base `/api/tags`) (JWT required)
  - POST `/` -> criar tag {name, scope} -> retorna tag
  - GET `/` -> listar tags (podendo filtrar por owner/global)
  - GET `/{id}` -> ver tag
  - DELETE `/{id}` -> remover (se owner)

- Serviço `expenses` (base `/api/expenses`) (JWT required)
  - POST `/` -> criar despesa {amount, description, occurredAt, tagIds} -> 201
  - GET `/` -> listar despesas do usuário (filtros: date range, tag, page/size)
  - GET `/{id}` -> ver despesa (se owner)
  - PUT `/{id}` -> atualizar (se owner)
  - DELETE `/{id}` -> remover (se owner)

Exemplos de query params: `?from=2025-01-01&to=2025-01-31&tag=alimentacao&page=0&size=20`

**Considerações de modelagem e performance**
- Relacionamento `expense_tags` como tabela many-to-many com FK para `expenses` e `tags`.
- Indexes: `expenses(user_id, occurred_at)`, `expense_tags(tag_id)`.
- Para grandes volumes, considerar proibição de joins pesados e usar agregações pré-computadas (materialized views) ou OLAP separado.

Observações específicas para MySQL:
- Tipo numérico: use `DECIMAL(13,2)` (ou apropriado) para valores monetários para evitar imprecisão.
- Datas/horas: `TIMESTAMP`/`DATETIME(6)` com fuso definido via conexão (usar UTC idealmente).
- Charset: configure `utf8mb4` e `COLLATE utf8mb4_unicode_ci` para suportar emojis e acentuação.
- Engine: use `InnoDB` para suporte a FK e transações.
- UUIDs: se optar por `UUID`, armazene como `CHAR(36)` ou `BINARY(16)` (melhor performance com `BINARY(16)`). Alternativa: `BIGINT` auto-increment para chaves primárias.
- Indexes e performance: evite `TEXT` em colunas indexadas; prefira `VARCHAR(191)` para índices em `utf8mb4` em versões antigas do MySQL/MariaDB.

**Mapeamento para o projeto atual**
- `src/main/java/dev/ifrs/UsersResource.java` já existente: reutilizar e transformar em módulo `users` com endpoints de auth.
- Arquivos `Acount.java` e `User.java` em `model/` provavelmente precisam ser revisados (nome `Acount` parece typo). Normalizar para `Account` ou `UserAccount`.
- Crie novos módulos `expenses` e `tags`. Copie convenções do `users` (padrões Quarkus, `application.properties` por módulo ou profiles).

**Exemplo mínimo de entidade (Expense) — Quarkus + Panache**
```java
@Entity
public class Expense extends PanacheEntityBase {
    @Id
    @GeneratedValue
    public Long id;

    public Long userId;
    public BigDecimal amount;
    public String description;
    public Instant occurredAt;

    @ManyToMany
    public Set<Tag> tags;
}
```

**Segurança do password**
- Armazenar `passwordHash` com `BCrypt` (ou Argon2). Nunca guardar senhas em texto.

**Docker / docker-compose (sugestão breve)**
- Serviços: `users`, `expenses`, `tags`, `db` (MySQL)
- Definir variáveis de ambiente para `QUARKUS_DATASOURCE_*` por serviço e usar `JWT_PUBLIC_KEY` para validação compartilhada.

Exemplo mínimo `docker-compose` (snippet):
```yaml
services:
  db:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: rootpass
      MYSQL_DATABASE: finance
      MYSQL_USER: appuser
      MYSQL_PASSWORD: apppass
    ports:
      - "3306:3306"
    command: --default-authentication-plugin=mysql_native_password --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

  users:
    build: ./users
    environment:
      QUARKUS_DATASOURCE_URL: jdbc:mysql://db:3306/finance?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
      QUARKUS_DATASOURCE_USERNAME: appuser
      QUARKUS_DATASOURCE_PASSWORD: apppass
    depends_on:
      - db
```

Nota: para desenvolvimento local você também pode usar um container MySQL em vez de H2.

Exemplo de propriedades Quarkus (application.properties) para MySQL:
```
quarkus.datasource.db-kind=mysql
quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/finance?useSSL=false&serverTimezone=UTC&characterEncoding=utf8
quarkus.datasource.username=appuser
quarkus.datasource.password=apppass
quarkus.hibernate-orm.database.generation=update
```

Dependência Maven (connectors):
```xml
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>8.0.34</version>
</dependency>
```

Migrações: recomendar `Flyway` ou `Liquibase` para controlar schema em MySQL.

**Passos de implementação (priorizados)**
1. Extrair `users` para módulo `users` (ou manter e adaptar código atual). Ajustar `User`/`Acount`.
2. Implementar autenticação JWT no `users` (endpoints register/login). Testar token issuance.
3. Criar módulo `tags` com endpoints simples CRUD. Proteger com JWT.
4. Criar módulo `expenses` com CRUD, associação com `tags` e validação de dono.
5. Criar `docker-compose` com Postgres e executar testes integrados locais.
6. Adicionar scripts de migração (Flyway/Liquibase) para criação de schema.

**Observações finais e trade-offs**
- Single DB vs DB por serviço: para simplicidade inicial, usar um único schema/DB compartilhado; para isolação, use DBs separados por serviço.
- Versionamento da API: prefixar com `/v1/` se planeja breaking changes.
- Considerar API Gateway (NGINX ou Quarkus gateway) futuramente para roteamento e autenticação central.

---

## Próximos passos recomendados (posso executar)
- Posso esboçar o módulo `expenses` com recursos mínimos (entity, repository, resource), ou
- Posso ajustar o `users` atual para emitir JWT e fornecer exemplos de login/registro.

Diga qual você prefere que eu implemente primeiro. 
