-- SCRIPT COMPLETO DO BANCO DE DADOS SCR 3044 (ATUALIZADO COM FOREIGN KEYS)

-- Avaliar a necessidade dessa tabela que serviria para controle do arquivo.
-- Se mudarmos a abordagem, pensar na possibilidade de retorno de processamento (se for sbatch direto banco, por exemplo).
-- Se for sbatch -> banco, avaliar a necessidade do uuid, usar somente o codigo de idempotencia

-- Tabela TbSistemaOrigem
-- Se o email do resposavel for externo ao BV? Como build block deve ser implementada.
-- Pegar as regras de classificação do email com a área usuária.
CREATE TABLE TbSistemaOrigem (
    CdSistemaOrigem SERIAL PRIMARY KEY,
    SgSistemaOrigem VARCHAR(255) NOT NULL,
    NmOrigem VARCHAR(255) NOT NULL,
    FlObrigatoria CHAR(1) NOT NULL,
    DsEmailResponsavel VARCHAR(255) NOT NULL,
    DsEmailGrupo VARCHAR(255) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbArquivoRemessaOrigem
-- utilizado para gravar informações do recebimento dos arquivos
-- avaliar utilização dessa tabela para dashboard de farol da aplicação (recebimento de arquivos)
-- Remessa pode ter N operacoes e N pagAgregados
CREATE TABLE TbArquivoRemessaOrigem (
    CdArquivoRemessaOrigem SERIAL PRIMARY KEY,
    CdCnpjIF VARCHAR(20) NOT NULL,
    DtHoraRemessa TIMESTAMP NOT NULL,
    NmArquivo VARCHAR(255) NOT NULL,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem),
    DtReferencia TIMESTAMP NOT NULL,
    DtBase TIMESTAMP NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbArquivoRemessaEnvio
-- avaliar utilização dessa tabela para dashboard de farol da aplicação (geracao de arquivos)
CREATE TABLE TbArquivoRemessaEnvio (
    CdArquivoRemessaEnvio SERIAL PRIMARY KEY,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE, -- validar se podemos dar ao arquivo final mesmo nome com estrutura dos recebidos, onde meu sistemaOrigem é x para 655 e y para 413
    NmArquivo VARCHAR(255) NOT NULL,
    DtBase DATE NOT NULL,
    DtHoraRemessa DATETIME NOT NULL,
    DtProximaRemessa DATETIME NOT NULL,
    FlEnviado CHAR(1) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);


-- Tabela TbOperacao
CREATE TABLE TbOperacao (
    CdOperacao SERIAL PRIMARY KEY,
    CdEventoOperacao VARCHAR(255) NOT NULL, --chave idempotencia
    Ipoc VARCHAR(100) NOT NULL,
    CdAcao CHAR(1) NOT NULL,
    DtEvento DATE NOT NULL,
    VrSaldoDevedor NUMERIC(18,2) NOT NULL,
    FlAtraso  CHAR(1) NOT NULL,
    CdArquivoRemessaOrigem INT REFERENCES TbArquivoRemessaOrigem(CdArquivoRemessaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    CdArquivoRemessaEnvio  INT REFERENCES TbArquivoRemessaEnvio(CdArquivoRemessaEnvio) ON DELETE SET NULL ON UPDATE CASCADE,
    FlConsolidado TIMESTAMP NOT NULL,
    DtHoraConsolidado TIMESTAMP NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbPagamento
-- Cada operacao pode ter N pagamentos
CREATE TABLE TbPagamento (
    CdPagamento SERIAL PRIMARY KEY,
    CdOperacao INT REFERENCES TbOperacao(CdOperacao),
    CdEventoPagamento VARCHAR(255) NOT NULL,
    CdAcao CHAR(1) NOT NULL,
    DtPagamento DATE NOT NULL,
    CdClass3050 VARCHAR(50) NOT NULL,
    VrPagamento NUMERIC(18, 2) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbConcessao
-- Cada operacao pode ter N pagamentos
CREATE TABLE TbConcessao (
    CdConcessao SERIAL PRIMARY KEY,
    CdOperacao INT REFERENCES TbOperacao(CdOperacao),
    CdEventoConcessao VARCHAR(255) NOT NULL,
    CdAcao CHAR(1) NOT NULL,
    DtConcessao DATE NOT NULL,
    CdClass3050 VARCHAR(50) NOT NULL,
    VrConcessao NUMERIC(18, 2) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbPagAgregados
-- cada remessa pode ter N PagAgregado
CREATE TABLE TbPagAgregados (
    CdPagAgregado SERIAL PRIMARY KEY,
    DtReferencia DATE NOT NULL,
    CdClass3050 VARCHAR(50) NOT NULL,
    VlAgregado NUMERIC(18,2) NOT NULL,
    CdAcao CHAR(1) NOT NULL,
    CdArquivoRemessaOrigem INT REFERENCES TbArquivoRemessaOrigem(CdArquivoRemessaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    CdArquivoRemessaEnvio  INT REFERENCES TbArquivoRemessaEnvio(CdArquivoRemessaEnvio) ON DELETE SET NULL ON UPDATE CASCADE,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);


-- Tabela TbMotivoRejeicao
CREATE TABLE TbMotivoRejeicao (
    CdMotivoRejeicao SERIAL PRIMARY KEY,
    CdMotivo VARCHAR(10) UNIQUE NOT NULL,
    DsMotivo VARCHAR(255) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbCampoErro
CREATE TABLE TbCampoErro (
    NmCampo VARCHAR(100) PRIMARY KEY,
    DsCampo VARCHAR(255),
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- Tabela TbEventoRejeitado
CREATE TABLE TbEventoRejeitado (
    CdEventoRejeitado SERIAL PRIMARY KEY,
    DtHora TIMESTAMP NOT NULL,
    NmArquivo VARCHAR(255),
    NrLinhaArquivo INT,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    CdAcao CHAR(1) NOT NULL,
    JsDadosRecebidos JSONB NOT NULL,
    CdMotivoRejeicao INT REFERENCES TbMotivoRejeicao(CdMotivoRejeicao) ON DELETE SET NULL ON UPDATE CASCADE,
  	CdArquivoRemessaOrigem INT REFERENCES TbArquivoRemessaOrigem(CdArquivoRemessaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    NmCampoErro VARCHAR(100) REFERENCES TbCampoErro(NmCampo) ON DELETE SET NULL ON UPDATE CASCADE,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);


CREATE Table TbEventoRejeitadoMotivo {
  CdEventoRejeitado int [ref: > TbEventoRejeitado.CdEventoRejeitado]
  CdMotivoRejeicao int [ref: > TbMotivoRejeicao.CdMotivoRejeicao]
  DtInclusao timestamp
  Note: "Relacionamento N:N entre evento rejeitado e motivos de rejeição"
  PrimaryKey (CdEventoRejeitado, CdMotivoRejeicao)
}

-- Tabela TbRelatorioEnvioOrigens
CREATE TABLE TbRelatorioEnvioOrigens (
    CdRelatorioEnvio SERIAL PRIMARY KEY,
    DtGeracao TIMESTAMP NOT NULL,
    DsCaminhoArquivo VARCHAR(255) NOT NULL,
    DsTipoRelatorio VARCHAR(255) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

CREATE TABLE TbEventoRejeitadoMotivo (
    CdEventoRejeitado INT NOT NULL REFERENCES TbEventoRejeitado(CdEventoRejeitado) ON DELETE CASCADE,
    CdMotivoRejeicao INT NOT NULL REFERENCES TbMotivoRejeicao(CdMotivoRejeicao) ON DELETE CASCADE,
    DtInclusao TIMESTAMP NOT NULL,
    PRIMARY KEY (CdEventoRejeitado, CdMotivoRejeicao)
);



----------------------------- DADOS MOCKADOS PARA TESTAR MODELAGEM

INSERT INTO TbSistemaOrigem (SgSistemaOrigem, NmOrigem, FlObrigatoria, DsEmailResponsavel, DsEmailGrupo, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('SIS01', 'Sistema Origem 1', 'S', 'responsavel1@empresa.com', 'grupo1@empresa.com', 'S', datetime(), datetime()),
('SIS02', 'Sistema Origem 2', 'N', 'responsavel2@empresa.com', 'grupo2@empresa.com', 'S', datetime(), datetime());


INSERT INTO TbArquivoRemessaOrigem (CdCnpjIF, DtHoraRemessa, NmArquivo, CdSistemaOrigem, DtReferencia, DtBase, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('12345678000100', datetime(), 'REMESSA_20250615_IF1.json', 1, '2025-06-15', '2025-06-15', 'S', datetime(), datetime()),
('98765432000199', datetime(), 'REMESSA_20250615_IF2.json', 2, '2025-06-15', '2025-06-15', 'S', datetime(), datetime());

INSERT INTO TbArquivoRemessaEnvio (CdSistemaOrigem, NmArquivo, DtBase, DtHoraRemessa, DtProximaRemessa, FlEnviado, FlAtivo, DtInclusao, DtAlteracao)
VALUES
(1, 'ENVIO_20250615_IF1.json', '2025-06-15', datetime(), '2025-06-18', 'S', 'S', datetime(), datetime());

INSERT INTO TbOperacao (CdEventoOperacao, Ipoc, CdAcao, DtEvento, VrSaldoDevedor, FlAtraso, CdArquivoRemessaOrigem, CdArquivoRemessaEnvio, FlConsolidado, DtHoraConsolidado, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('EVT_OP_001', 'BR12345678', 'I', '2025-06-15', 10000.00, 'N', 1, 1, datetime(), datetime(), 'S', datetime(), datetime()),
('EVT_OP_002', 'BR87654321', 'A', '2025-06-15', 8500.00, 'S', 1, 1, datetime(), datetime(), 'S', datetime(), datetime());


INSERT INTO TbPagamento (CdOperacao, CdEventoPagamento, CdAcao, DtPagamento, CdClass3050, VrPagamento, FlAtivo, DtInclusao, DtAlteracao)
VALUES
(1, 'EVT_PAG_001', 'I', '2025-06-16', '3050-A', 1500.00, 'S', datetime(), datetime()),
(1, 'EVT_PAG_002', 'A', '2025-06-17', '3050-B', 2000.00, 'S', datetime(), datetime());


INSERT INTO TbConcessao (CdOperacao, CdEventoConcessao, CdAcao, DtConcessao, CdClass3050, VrConcessao, FlAtivo, DtInclusao, DtAlteracao)
VALUES
(2, 'EVT_CON_001', 'I', '2025-06-15', '3050-C', 5000.00, 'S', datetime(), datetime());


INSERT INTO TbMotivoRejeicao (CdMotivo, DsMotivo, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('M001', 'CNPJ inválido', 'S', datetime(), datetime()),
('M002', 'Saldo devedor negativo', 'S', datetime(), datetime());


INSERT INTO TbEventoRejeitado (DtHora, NmArquivo, NrLinhaArquivo, CdSistemaOrigem, CdAcao, JsDadosRecebidos, NmCampoErro, CdArquivoRemessaOrigem, FlAtivo, DtInclusao, DtAlteracao)
VALUES
(datetime(), 'REMESSA_20250615_IF1.json', 12, 1, 'I', '{"ipoc":"BR123","valor":-100}', 'VrSaldoDevedor', 1, 'S', datetime(), datetime()),
(datetime(), 'REMESSA_20250615_IF1.json', 17, 1, 'I', '{"cnpj":"00000000"}', 'CdCnpjIF', 1, 'S', datetime(), datetime());

INSERT INTO TbEventoRejeitadoMotivo (CdEventoRejeitado, CdMotivoRejeicao, Dtinclusao)
VALUES
(1, 2, datetime()),
(2, 1, datetime());


INSERT INTO TbRelatorioEnvioOrigens (DtGeracao, DsCaminhoArquivo, DsTipoRelatorio, FlAtivo, DtInclusao, DtAlteracao)
VALUES
(datetime(), '/relatorios/envio_20250615.csv', 'Resumo Diário', 'S', datetime(), datetime());

----------------------- SCRIPTS REJEITADOS
INSERT INTO TbSistemaOrigem (SgSistemaOrigem, NmOrigem, FlObrigatoria, DsEmailResponsavel, DsEmailGrupo, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('SCR1', 'Sistema X', 'S', 'x@empresa.com', 'grupo_x@empresa.com', 'S', datetime(), datetime()),
('SCR2', 'Sistema Y', 'N', 'y@empresa.com', 'grupo_y@empresa.com', 'S', datetime(), datetime());

INSERT INTO TbArquivoRemessaOrigem (CdCnpjIF, DtHoraRemessa, NmArquivo, CdSistemaOrigem, DtReferencia, DtBase, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('11111111000191', '2025-06-14 10:00:00', 'REM_SISX_20250614.json', 1, '2025-06-14', '2025-06-14', 'S', datetime(), datetime()),
('22222222000191', '2025-06-15 11:00:00', 'REM_SISY_20250615.json', 2, '2025-06-15', '2025-06-15', 'S', datetime(), datetime());


INSERT INTO TbCampoErro (NmCampo, DsCampo, FlAtivo, DtInclusao, DtAlteracao)
VALUES
('CdCnpjIF', 'CNPJ inválido', 'S', datetime(), datetime()),
('VrSaldoDevedor', 'Saldo devedor negativo', 'S', datetime(), datetime()),
('Ipoc', 'IPOC inexistente', 'S', datetime(), datetime());

INSERT INTO TbEventoRejeitado (CdEventoRejeitado, DtHora, NmArquivo, NrLinhaArquivo, CdSistemaOrigem, CdAcao, JsDadosRecebidos, NmCampoErro, CdArquivoRemessaOrigem, FlAtivo, DtInclusao, DtAlteracao)
VALUES
(1001, '2025-06-14 10:10:00', 'REM_SISX_20250614.json', 5, 1, 'I', '{"cnpj":"111"}', 'CdCnpjIF', 1, 'S', datetime(), datetime()),
(1002, '2025-06-14 10:15:00', 'REM_SISX_20250614.json', 9, 1, 'A', '{"valor":-100.0}', 'VrSaldoDevedor', 1, 'S', datetime(), datetime()),
(1003, '2025-06-14 10:20:00', 'REM_SISX_20250614.json', 11, 1, 'A', '{"ipoc":null}', 'Ipoc', 1, 'S', datetime(), datetime()),
(1004, '2025-06-15 11:10:00', 'REM_SISY_20250615.json', 3, 2, 'I', '{"cnpj":"00000000"}', 'CdCnpjIF', 2, 'S', datetime(), datetime());


INSERT INTO TbEventoRejeitadoMotivo (CdEventoRejeitado, CdMotivoRejeicao, dtinclusao)
VALUES
(1001, 1, datetime()),  -- CNPJ inválido
(1002, 2, datetime()),  -- Saldo negativo
(1003, 3, datetime()),  -- Formato inválido
(1003, 1, datetime()),  -- CNPJ inválido também
(1004, 1, datetime());  -- CNPJ inválido


---------------------------------- SELECTS SIMPLES
SELECT * from TbArquivoRemessaEnvio
SELECT * from TbArquivoRemessaOrigem
SELECT * FROM TbCampoErro
SELECT * FROM TbConcessao
SELECT * from TbEventoRejeitado
SELECT * from TbEventoRejeitadoMotivo
SELECT * from TbMotivoRejeicao
SELECT * from TbOperacao
SELECT * from TbPagAgregados
SELECT * from TbPagamento
SELECT * from TbRelatorioEnvioOrigens
SELECT * from TbSistemaOrigem

---------------------------------------- SELECTS DAS TABELAS ANALISES
-- Total de eventos rejeitados por remessa
SELECT
    r.CdArquivoRemessaOrigem,
    r.NmArquivo,
    COUNT(er.CdEventoRejeitado) AS total_rejeitados
FROM TbEventoRejeitado er
JOIN TbArquivoRemessaOrigem r ON er.CdArquivoRemessaOrigem = r.CdArquivoRemessaOrigem
GROUP BY r.CdArquivoRemessaOrigem, r.NmArquivo
ORDER BY total_rejeitados DESC;

-- Eventos rejeitados por sistema de origem
SELECT
    so.NmOrigem,
    COUNT(er.CdEventoRejeitado) AS total_rejeitados
FROM TbEventoRejeitado er
JOIN TbSistemaOrigem so ON er.CdSistemaOrigem = so.CdSistemaOrigem
GROUP BY so.NmOrigem
ORDER BY total_rejeitados DESC;


-- Detalhes dos eventos rejeitados com motivos (1 evento pode ter vários motivos)
SELECT
    er.CdEventoRejeitado,
    er.NmArquivo,
    er.NrLinhaArquivo,
    mr.CdMotivo,
    mr.DsMotivo,
    ce.NmCampo,
    er.DtHora
FROM TbEventoRejeitado er
JOIN TbEventoRejeitadoMotivo erm ON er.CdEventoRejeitado = erm.CdEventoRejeitado
JOIN TbMotivoRejeicao mr ON erm.CdMotivoRejeicao = mr.CdMotivoRejeicao
LEFT JOIN TbCampoErro ce ON er.NmCampoErro = ce.NmCampo
ORDER BY er.DtHora DESC;

-- Eventos rejeitados em uma remessa específica
SELECT
    er.CdEventoRejeitado,
    er.NrLinhaArquivo,
    mr.CdMotivo,
    mr.DsMotivo,
    er.JsDadosRecebidos->>'ipoc' AS Ipoc

FROM TbEventoRejeitado er
JOIN TbEventoRejeitadoMotivo erm ON er.CdEventoRejeitado = erm.CdEventoRejeitado
JOIN TbMotivoRejeicao mr ON erm.CdMotivoRejeicao = mr.CdMotivoRejeicao
WHERE er.CdArquivoRemessaOrigem = 1; -- Substitua pelo ID desejado


-- Ranking de motivos de rejeição mais comuns
SELECT
    mr.CdMotivo,
    mr.DsMotivo,
    COUNT(*) AS qtd_ocorrencias
FROM TbEventoRejeitadoMotivo erm
JOIN TbMotivoRejeicao mr ON erm.CdMotivoRejeicao = mr.CdMotivoRejeicao
GROUP BY mr.CdMotivo, mr.DsMotivo
ORDER BY qtd_ocorrencias DESC;

-- Eventos rejeitados com o campo que causou erro
SELECT *
    --er.CdEventoRejeitado,
    --er.NrLinhaArquivo,
    --ce.NmCampo,
    --ce.DsCampo
FROM TbEventoRejeitado er
JOIN TbCampoErro ce ON er.NmCampoErro = ce.NmCampo
ORDER BY er.DtHora DESC;


---------------------- RELACIONAMENTOS NO https://dbdiagram.io/


Table TbSistemaOrigem {
  CdSistemaOrigem SERIAL [pk]
  SgSistemaOrigem VARCHAR(255)
  NmOrigem VARCHAR(255)
  FlObrigatoria CHAR(1)
  DsEmailResponsavel VARCHAR(255)
  DsEmailGrupo VARCHAR(255)
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbArquivoRemessaOrigem {
  CdArquivoRemessaOrigem SERIAL [pk]
  CdCnpjIF VARCHAR(20)
  DtHoraRemessa TIMESTAMP
  NmArquivo VARCHAR(255)
  CdSistemaOrigem INT [ref: > TbSistemaOrigem.CdSistemaOrigem]
  DtReferencia TIMESTAMP
  DtBase TIMESTAMP
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbArquivoRemessaEnvio {
  CdArquivoRemessaEnvio SERIAL [pk]
  CdSistemaOrigem INT [ref: > TbSistemaOrigem.CdSistemaOrigem]
  NmArquivo VARCHAR(255)
  DtBase DATE
  DtHoraRemessa DATETIME
  DtProximaRemessa DATETIME
  FlEnviado CHAR(1)
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbOperacao {
  CdOperacao SERIAL [pk]
  CdEventoOperacao VARCHAR(255)
  Ipoc VARCHAR(100)
  CdAcao CHAR(1)
  DtEvento DATE
  VrSaldoDevedor NUMERIC(18,2)
  FlAtraso CHAR(1)
  CdArquivoRemessaOrigem INT [ref: > TbArquivoRemessaOrigem.CdArquivoRemessaOrigem]
  CdArquivoRemessaEnvio INT [ref: > TbArquivoRemessaEnvio.CdArquivoRemessaEnvio]
  FlConsolidado TIMESTAMP
  DtHoraConsolidado TIMESTAMP
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbPagamento {
  CdPagamento SERIAL [pk]
  CdOperacao INT [ref: > TbOperacao.CdOperacao]
  CdEventoPagamento VARCHAR(255)
  CdAcao CHAR(1)
  DtPagamento DATE
  CdClass3050 VARCHAR(50)
  VrPagamento NUMERIC(18,2)
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbConcessao {
  CdConcessao SERIAL [pk]
  CdOperacao INT [ref: > TbOperacao.CdOperacao]
  CdEventoConcessao VARCHAR(255)
  CdAcao CHAR(1)
  DtConcessao DATE
  CdClass3050 VARCHAR(50)
  VrConcessao NUMERIC(18,2)
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbMotivoRejeicao {
  CdMotivoRejeicao SERIAL [pk]
  CdMotivo VARCHAR(10) [unique]
  DsMotivo VARCHAR(255)
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}

Table TbEventoRejeitado {
  CdEventoRejeitado int [pk]
  DtHora timestamp
  NmArquivo varchar
  NrLinhaArquivo int
  CdSistemaOrigem int [ref: > TbSistemaOrigem.CdSistemaOrigem]
  CdAcao char
  JsDadosRecebidos jsonb
  CdArquivoRemessaOrigem int [ref: > TbArquivoRemessaOrigem.CdArquivoRemessaOrigem]
  FlAtivo char
  DtInclusao timestamp
  DtAlteracao timestamp
}

Table TbEventoRejeitadoMotivo {
  CdEventoRejeitado int [ref: > TbEventoRejeitado.CdEventoRejeitado]
  CdMotivoRejeicao int [ref: > TbMotivoRejeicao.CdMotivoRejeicao]
  DtInclusao timestamp
}

Table TbRelatorioEnvioOrigens {
  CdRelatorioEnvio SERIAL [pk]
  DtGeracao TIMESTAMP
  DsCaminhoArquivo VARCHAR(255)
  DsTipoRelatorio VARCHAR(255)
  FlAtivo CHAR(1)
  DtInclusao TIMESTAMP
  DtAlteracao TIMESTAMP
}


TbSistemaOrigem
 ├── 1:N TbArquivoRemessaOrigem (via CdSistemaOrigem)
 ├── 1:N TbArquivoRemessaEnvio (via CdSistemaOrigem)
 └── 1:N TbEventoRejeitado (via CdSistemaOrigem)

TbArquivoRemessaOrigem
 ├── 1:N TbOperacao (via CdArquivoRemessaOrigem)
 └── 1:N TbEventoRejeitado (via CdArquivoRemessaOrigem)

TbArquivoRemessaEnvio
 └── 1:N TbOperacao (via CdArquivoRemessaEnvio)

TbOperacao
 ├── 1:N TbPagamento (via CdOperacao)
 └── 1:N TbConcessao (via CdOperacao)

TbEventoRejeitado
 └── 1:N TbEventoRejeitadoMotivo (via CdEventoRejeitado)

TbMotivoRejeicao
 └── 1:N TbEventoRejeitadoMotivo (via CdMotivoRejeicao)


| Tabela                   | Relacionada com           | Tipo de relação | Descrição                                                    |
| ------------------------ | ------------------------- | --------------- | ------------------------------------------------------------ |
| `TbSistemaOrigem`        | `TbArquivoRemessaOrigem`  | 1\:N            | Um sistema de origem pode gerar várias remessas              |
| `TbSistemaOrigem`        | `TbArquivoRemessaEnvio`   | 1\:N            | Um sistema pode enviar várias remessas                       |
| `TbSistemaOrigem`        | `TbEventoRejeitado`       | 1\:N            | Eventos rejeitados referem-se a um sistema origem            |
| `TbArquivoRemessaOrigem` | `TbOperacao`              | 1\:N            | Uma remessa de origem pode conter várias operações           |
| `TbArquivoRemessaOrigem` | `TbEventoRejeitado`       | 1\:N            | Uma remessa pode conter vários eventos rejeitados            |
| `TbArquivoRemessaEnvio`  | `TbOperacao`              | 1\:N            | Operações enviadas referenciam uma remessa de envio          |
| `TbOperacao`             | `TbPagamento`             | 1\:N            | Uma operação pode conter vários pagamentos                   |
| `TbOperacao`             | `TbConcessao`             | 1\:N            | Uma operação pode conter várias concessões                   |
| `TbEventoRejeitado`      | `TbEventoRejeitadoMotivo` | 1\:N            | Um evento pode ter vários motivos de rejeição                |
| `TbMotivoRejeicao`       | `TbEventoRejeitadoMotivo` | 1\:N            | Um motivo pode estar relacionado a vários eventos rejeitados |




---------------------TbSistemaOrigem e TbArquivoRemessaOrigem
| Campo              | Tipo         | Descrição                                |
| ------------------ | ------------ | ---------------------------------------- |
| CdSistemaOrigem    | SERIAL (PK)  | Identificador único do sistema de origem |
| SgSistemaOrigem    | VARCHAR(255) | Sigla do sistema de origem               |
| NmOrigem           | VARCHAR(255) | Nome do sistema ou origem                |
| FlObrigatoria      | CHAR(1)      | Flag que indica se o envio é obrigatório |
| DsEmailResponsavel | VARCHAR(255) | E-mail do responsável pela origem        |
| DsEmailGrupo       | VARCHAR(255) | E-mail do grupo responsável              |
| FlAtivo            | CHAR(1)      | Flag de status (ativo/inativo)           |
| DtInclusao         | TIMESTAMP    | Data de inclusão do registro             |
| DtAlteracao        | TIMESTAMP    | Data da última alteração                 |



| Campo                  | Tipo         | Descrição                                     |
| ---------------------- | ------------ | --------------------------------------------- |
| CdArquivoRemessaOrigem | SERIAL (PK)  | Identificador do arquivo de remessa da origem |
| CdCnpjIF               | VARCHAR(20)  | CNPJ da Instituição Financeira remetente      |
| DtHoraRemessa          | TIMESTAMP    | Data/hora da remessa recebida                 |
| NmArquivo              | VARCHAR(255) | Nome do arquivo de remessa                    |
| CdSistemaOrigem        | INT (FK)     | Chave estrangeira para TbSistemaOrigem        |
| DtReferencia           | TIMESTAMP    | Data de referência do conteúdo                |
| DtBase                 | TIMESTAMP    | Data-base do conteúdo                         |
| FlAtivo                | CHAR(1)      | Flag de status (ativo/inativo)                |
| DtInclusao             | TIMESTAMP    | Data de inclusão                              |
| DtAlteracao            | TIMESTAMP    | Data da última alteração                      |


--------------------- TbArquivoRemessaEnvio
| Campo                 | Tipo         | Descrição                                |
| --------------------- | ------------ | ---------------------------------------- |
| CdArquivoRemessaEnvio | SERIAL (PK)  | Identificador da remessa de envio        |
| CdSistemaOrigem       | INT (FK)     | Chave estrangeira para TbSistemaOrigem   |
| NmArquivo             | VARCHAR(255) | Nome do arquivo gerado para envio        |
| DtBase                | DATE         | Data-base da remessa                     |
| DtHoraRemessa         | DATETIME     | Data/hora do envio                       |
| DtProximaRemessa      | DATETIME     | Data/hora da próxima remessa esperada    |
| FlEnviado             | CHAR(1)      | Flag que indica se o arquivo foi enviado |
| FlAtivo               | CHAR(1)      | Flag de status (ativo/inativo)           |
| DtInclusao            | TIMESTAMP    | Data de inclusão                         |
| DtAlteracao           | TIMESTAMP    | Data da última alteração                 |


--------------------- TbOperacao
| Campo                  | Tipo          | Descrição                                   |
| ---------------------- | ------------- | ------------------------------------------- |
| CdOperacao             | SERIAL (PK)   | Identificador único da operação             |
| CdEventoOperacao       | VARCHAR(255)  | Código do evento da operação                |
| Ipoc                   | VARCHAR(100)  | Identificador da operação de crédito        |
| CdAcao                 | CHAR(1)       | Código da ação (inclui/atualiza/remove etc) |
| DtEvento               | DATE          | Data do evento                              |
| VrSaldoDevedor         | NUMERIC(18,2) | Valor do saldo devedor                      |
| FlAtraso               | CHAR(1)       | Indica se está em atraso                    |
| CdArquivoRemessaOrigem | INT (FK)      | Referência ao arquivo recebido              |
| CdArquivoRemessaEnvio  | INT (FK)      | Referência ao arquivo de envio              |
| FlConsolidado          | TIMESTAMP     | Data/hora em que foi consolidado            |
| DtHoraConsolidado      | TIMESTAMP     | Data/hora da consolidação efetiva           |
| FlAtivo                | CHAR(1)       | Flag de status (ativo/inativo)              |
| DtInclusao             | TIMESTAMP     | Data de inclusão                            |
| DtAlteracao            | TIMESTAMP     | Data da última alteração                    |

--------------------- TbPagamento
| Campo             | Tipo          | Descrição                         |
| ----------------- | ------------- | --------------------------------- |
| CdPagamento       | SERIAL (PK)   | Identificador do pagamento        |
| CdOperacao        | INT (FK)      | Referência à operação relacionada |
| CdEventoPagamento | VARCHAR(255)  | Código do evento de pagamento     |
| CdAcao            | CHAR(1)       | Código da ação do pagamento       |
| DtPagamento       | DATE          | Data do pagamento                 |
| CdClass3050       | VARCHAR(50)   | Classificação 3050                |
| VrPagamento       | NUMERIC(18,2) | Valor do pagamento                |
| FlAtivo           | CHAR(1)       | Flag de status                    |
| DtInclusao        | TIMESTAMP     | Data de inclusão                  |
| DtAlteracao       | TIMESTAMP     | Data da última alteração          |

--------------------- TbConcessao
| Campo             | Tipo          | Descrição                     |
| ----------------- | ------------- | ----------------------------- |
| CdConcessao       | SERIAL (PK)   | Identificador da concessão    |
| CdOperacao        | INT (FK)      | Referência à operação         |
| CdEventoConcessao | VARCHAR(255)  | Código do evento da concessão |
| CdAcao            | CHAR(1)       | Código da ação                |
| DtConcessao       | DATE          | Data da concessão             |
| CdClass3050       | VARCHAR(50)   | Classificação 3050            |
| VrConcessao       | NUMERIC(18,2) | Valor concedido               |
| FlAtivo           | CHAR(1)       | Flag de status                |
| DtInclusao        | TIMESTAMP     | Data de inclusão              |
| DtAlteracao       | TIMESTAMP     | Data da última alteração      |

--------------------- TbMotivoRejeicao
| Campo            | Tipo         | Descrição                           |
| ---------------- | ------------ | ----------------------------------- |
| CdMotivoRejeicao | SERIAL (PK)  | Identificador do motivo de rejeição |
| CdMotivo         | VARCHAR(10)  | Código do motivo (único)            |
| DsMotivo         | VARCHAR(255) | Descrição do motivo                 |
| FlAtivo          | CHAR(1)      | Flag de status                      |
| DtInclusao       | TIMESTAMP    | Data de inclusão                    |
| DtAlteracao      | TIMESTAMP    | Data da última alteração            |

--------------------- TbEventoRejeitado
| Campo                  | Tipo      | Descrição                             |
| ---------------------- | --------- | ------------------------------------- |
| CdEventoRejeitado      | INT (PK)  | Identificador do evento rejeitado     |
| DtHora                 | TIMESTAMP | Data/hora do recebimento do evento    |
| NmArquivo              | VARCHAR   | Nome do arquivo onde ocorreu o erro   |
| NrLinhaArquivo         | INT       | Número da linha com problema          |
| CdSistemaOrigem        | INT (FK)  | Sistema de origem que enviou o evento |
| CdAcao                 | CHAR      | Código da ação                        |
| JsDadosRecebidos       | JSONB     | Dados recebidos (JSON bruto)          |
| CdArquivoRemessaOrigem | INT (FK)  | Referência ao arquivo recebido        |
| FlAtivo                | CHAR      | Flag de status                        |
| DtInclusao             | TIMESTAMP | Data de inclusão                      |
| DtAlteracao            | TIMESTAMP | Data da última alteração              |

--------------------- TbEventoRejeitadoMotivo
| Campo             | Tipo      | Descrição                        |
| ----------------- | --------- | -------------------------------- |
| CdEventoRejeitado | INT (FK)  | Referência ao evento rejeitado   |
| CdMotivoRejeicao  | INT (FK)  | Referência ao motivo de rejeição |
| DtInclusao        | TIMESTAMP | Data de inclusão                 |

--------------------- TbRelatorioEnvioOrigens
| Campo            | Tipo         | Descrição                              |
| ---------------- | ------------ | -------------------------------------- |
| CdRelatorioEnvio | SERIAL (PK)  | Identificador do relatório             |
| DtGeracao        | TIMESTAMP    | Data/hora de geração do relatório      |
| DsCaminhoArquivo | VARCHAR(255) | Caminho onde o arquivo está armazenado |
| DsTipoRelatorio  | VARCHAR(255) | Tipo do relatório gerado               |
| FlAtivo          | CHAR(1)      | Flag de status                         |
| DtInclusao       | TIMESTAMP    | Data de inclusão                       |
| DtAlteracao      | TIMESTAMP    | Data da última alteração               |
