INSERT INTO empresas (nome, cnpj, status) VALUES ('Oficina Demo 1', '00.000.000/0001-00', 'ATIVA');
INSERT INTO empresas (nome, cnpj, status) VALUES ('Oficina Demo 2', '11.111.111/0001-11', 'ATIVA');

INSERT INTO planos (nome, preco_mensal, limite_usuarios, limite_os_mes, ativo) VALUES ('Básico', 49.90, 3, 200, TRUE);
INSERT INTO planos (nome, preco_mensal, limite_usuarios, limite_os_mes, ativo) VALUES ('Pro', 99.90, 10, 2000, TRUE);

-- Assinatura inicial por empresa
INSERT INTO assinaturas (id_empresa, id_plano, status, data_inicio, proximo_ciclo, gateway_ref)
VALUES (1, 1, 'ATIVA', CURRENT_DATE, DATEADD('MONTH', 1, CURRENT_DATE), 'DEMO-EMP1');

INSERT INTO assinaturas (id_empresa, id_plano, status, data_inicio, proximo_ciclo, gateway_ref)
VALUES (2, 2, 'ATIVA', CURRENT_DATE, DATEADD('MONTH', 1, CURRENT_DATE), 'DEMO-EMP2');

-- Usuários (senha: 123)
INSERT INTO usuarios (id_empresa, nome, email, senha_hash, role, ativo)
VALUES (1, 'Admin 1', 'admin@demo1.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'ADMIN', TRUE);
INSERT INTO usuarios (id_empresa, nome, email, senha_hash, role, ativo)
VALUES (1, 'Operador 1', 'op@demo1.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'OPERADOR', TRUE);

INSERT INTO usuarios (id_empresa, nome, email, senha_hash, role, ativo)
VALUES (2, 'Admin 2', 'admin@demo2.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'ADMIN', TRUE);
INSERT INTO usuarios (id_empresa, nome, email, senha_hash, role, ativo)
VALUES (2, 'Operador 2', 'op@demo2.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'OPERADOR', TRUE);
-- SUPERADMIN global (senha: 123) - não pertence a nenhuma empresa
INSERT INTO usuarios (id_empresa, nome, email, senha_hash, role, ativo)
VALUES (NULL, 'Super Admin', 'superadmin@saas.com', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'SUPERADMIN', TRUE);
