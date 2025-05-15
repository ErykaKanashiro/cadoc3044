-- SCRIPT COMPLETO DO BANCO DE DADOS SCR 3044 (ATUALIZADO COM FOREIGN KEYS)

-- Avaliar a necessidade dessa tabela que serviria para controle do arquivo.
-- Se mudarmos a abordagem, pensar na possibilidade de retorno de processamento (se for sbatch direto banco, por exemplo).
-- Se for sbatch -> banco, avaliar a necessidade do uuid, usar somente o codigo de idempotencia

-- 1. Tabela TbSistemaOrigem
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

-- 2. Tabela TbArquivoRemessa
CREATE TABLE TbArquivoRemessa (
    CdArquivoRemessa SERIAL PRIMARY KEY,
    NmArquivo VARCHAR(255) NOT NULL,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    DtRecebimento TIMESTAMP NOT NULL,
    DsStatus VARCHAR(50) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- 3. Tabela TbOperacoes
CREATE TABLE TbOperacoes (
    CdOperacao SERIAL PRIMARY KEY,
    UuidEvento UUID UNIQUE NULL,
    CdEvento VARCHAR(255) NOT NULL,
    Ipoc VARCHAR(100) NOT NULL,
    DtEvento DATE NOT NULL,
    VlOperacao NUMERIC(18,2) NOT NULL,
    CdClass3050 VARCHAR(50) NOT NULL,
    CdAcao CHAR(1) NOT NULL,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- 4. Tabela TbPagAgregados
CREATE TABLE TbPagAgregados (
    CdPagAgregado SERIAL PRIMARY KEY,
    DtReferencia DATE NOT NULL,
    CdClass3050 VARCHAR(50) NOT NULL,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    VlAgregado NUMERIC(18,2) NOT NULL,
    CdAcao CHAR(1) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);


-- 6. Tabela TbMotivoRejeicao
CREATE TABLE TbMotivoRejeicao (
    CdMotivoRejeicao SERIAL PRIMARY KEY,
    CdMotivo VARCHAR(10) UNIQUE NOT NULL,
    DsMotivo VARCHAR(255) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- 7. Tabela TbCampoErro
CREATE TABLE TbCampoErro (
    NmCampo VARCHAR(100) PRIMARY KEY,
    DsCampo VARCHAR(255),
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- 8. Tabela TbEventoRejeitado
CREATE TABLE TbEventoRejeitado (
    CdEventoRejeitado SERIAL PRIMARY KEY,
    DtHora TIMESTAMP NOT NULL,
    NmArquivo VARCHAR(255),
    NrLinhaArquivo INT,
    UuidEvento UUID, -- se necessario, avaliar a abordagem
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    CdTipoEvento  -- -- Tipos de eventos válidos do sistema. -- Campos: id: ex: 'operacao', 'pagAgregado'
    CdAcao CHAR(1) NOT NULL,
    JsDadosRecebidos JSONB NOT NULL,
    CdMotivoRejeicao INT REFERENCES TbMotivoRejeicao(CdMotivoRejeicao) ON DELETE SET NULL ON UPDATE CASCADE,
    NmCampoErro VARCHAR(100) REFERENCES TbCampoErro(NmCampo) ON DELETE SET NULL ON UPDATE CASCADE,
    NmProcessadoPor VARCHAR(100),
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- 9. Tabela TbEnvioOrigem
-- avaliar utilização dessa tabela para dashboard de farol da aplicação
CREATE TABLE TbEnvioOrigem (
    CdEnvioOrigem SERIAL PRIMARY KEY,
    CdSistemaOrigem INT REFERENCES TbSistemaOrigem(CdSistemaOrigem) ON DELETE SET NULL ON UPDATE CASCADE,
    DtBase DATE NOT NULL,
    DtHoraEnvio DATETIME NOT NULL,
    FlEnviado CHAR(1) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

-- 10. Tabela TbRelatorioEnvioOrigens
CREATE TABLE TbRelatorioEnvioOrigens (
    CdRelatorioEnvio SERIAL PRIMARY KEY,
    DtGeracao TIMESTAMP NOT NULL,
    DsCaminhoArquivo VARCHAR(255) NOT NULL,
    DsTipoRelatorio VARCHAR(255) NOT NULL,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);