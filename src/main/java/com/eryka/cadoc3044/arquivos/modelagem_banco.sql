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
    CdOperacao INT REFERENCES TbOperacao(CdOperacao)
    CdEventoPagamento VARCHAR(255) NOT NULL, --chave idempotencia
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
    CdOperacao INT REFERENCES TbOperacao(CdOperacao)
    CdEventoConcessao VARCHAR(255) NOT NULL, --chave idempotencia
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
    NmCampoErro VARCHAR(100) REFERENCES TbCampoErro(NmCampo) ON DELETE SET NULL ON UPDATE CASCADE,
    FlAtivo CHAR(1) NOT NULL,
    DtInclusao TIMESTAMP NOT NULL,
    DtAlteracao TIMESTAMP NOT NULL
);

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