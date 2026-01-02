# TorqueOS

Controle total da sua oficina.

# Oficina JSP CRUD - SaaS (Multiempresa + Usuários + Permissões + Assinatura)

## Rodar
```bash
mvn spring-boot:run
```

## Login
Abra: http://localhost:8080/auth/login

### Usuários seed (senha: 123)
- Empresa 1:
  - admin@demo1.com (ADMIN)
  - op@demo1.com (OPERADOR)
- Empresa 2:
  - admin@demo2.com (ADMIN)
  - op@demo2.com (OPERADOR)

## Permissões
- ADMIN: acessa /usuarios e /planos
- OPERADOR: acessa o resto (clientes/veículos/OS/itens/assinatura/pagamentos)

## Assinatura (demo)
- Cada empresa tem 1 assinatura (`assinaturas` com UNIQUE(id_empresa)).
- Validações simplificadas:
  - status precisa ser ATIVA
  - proximo_ciclo não pode estar no passado
- Limites:
  - usuários ativos <= limite do plano
  - OS total da empresa <= limite do plano (simplificado; em produção você contaria por mês)

## Observação importante (produção)
Para SaaS real:
- Use Spring Security + sessão/JWT.
- Use um gateway de cobrança (Stripe/Mercado Pago).
- A troca de plano e ativação deveriam depender do webhook de pagamento.

## SUPERADMIN
- Login: superadmin@saas.com (senha: 123)
- No login, deixe o campo **Empresa ID vazio** para entrar como SUPERADMIN.
- Após logar, escolha a empresa em /admin/empresas.

## Landing Page
- http://localhost:8080/landing/index.html

## Login
- SUPERADMIN: superadmin@saas.com / 123 (deixe Empresa ID vazio)
- Depois selecione a empresa em: /admin/empresas

## Docker
```bash
docker compose up --build
```


## Docker (PostgreSQL)
Este projeto acompanha um `docker-compose.yml` com **PostgreSQL** e scripts de inicialização em `db/init`.

### Subir aplicação + banco
```bash
docker compose up --build
```

### Acessar
- Landing: http://localhost:8080/landing/index.html
- Login:  http://localhost:8080/auth/login

SUPERADMIN:
- Empresa ID: (vazio)
- Email: superadmin@saas.com
- Senha: 123

### Resetar banco (apagar dados)
```bash
docker compose down -v
```
