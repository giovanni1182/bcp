create database crm2;
use crm2;

begin;

create table entidade (
  id int(11) not null,
  classe char(32),
  nome char(64),
  superior int(11),
  responsavel int(11),
  criacao bigint(20),
  atualizacao bigint(20),
  primary key  (id),
  key entidade_nome (nome),
  key entidade_superior (superior),
  key entidade_responsavel (responsavel),
  key entidade_criacao (criacao),
) type=innodb; 

create table departamento (
  id int(11) not null,
  localizacao varchar(64),
  primary key (id)
) type=innodb; 

create table empresa (
  id int(11) not null,
  cnpj varchar(32),
  razao_social varchar(132),
  primary key (id),
  key empresa_cnpj (cnpj)
) type=innodb; 

create table pessoa_fisica (
  id int(11) not null,
  cpf char(16),
  sexo char(1),
  nascimento bigint,
  primary key (id),
  key pessoafisica_cpf (cpf)
) type=innodb; 

create table usuario (
  id int(11) not null,
  chave char(16),
  senha char(16),
  primary key (id),
  key usuario_chave (chave)
) type=innodb; 

create table grupo (
  chave char(16) not null,
  grupo char(16) not null,
  primary key (chave,grupo)
) type=innodb;

create table contato (
	id int(11) not null,
	entidade int(11) not null,
	tipo varchar(32),
	contato varchar(32),
	primary key (id),
	key contato_entidade (entidade)
) type=innodb;

create table endereco (
	id int(11) not null,
	entidade int(11) not null,
	cep varchar(16),
	tipo varchar(32),
	rua varchar(32),
	numero varchar(16),
	complemento varchar(32),
	bairro varchar(32),	
	cidade varchar(32),
	estado varchar(16),
	pais varchar(16),
	primary key (id),
	key endereco_entidade (entidade)
) type=innodb;

create table atributo (
	id int(11) not null,
	entidade int(11) not null,
	tipo varchar(32) not null,
	publico char(1),
	valor varchar(250),
	primary key (id, tipo),
	key atributo_entidade (entidade)
) type=innodb;

create table evento (
  id int(11) not null,
  classe char(32),
  nome char(64),
  objeto int(11),
  responsavel int(11),
  criador int(11),
  criacao bigint(20),
  atualizacao bigint(20),
  primary key (id),
  key evento_nome (nome),
  key evento_responsavel (responsavel),
  key evento_objeto (objeto),
  key evento_criador (criador),
  key evento_criacao (criacao),
  key evento_atualizacao (atualizacao),
) type=innodb;

create table fase (
  id int(11) not null,
  inicio bigint(20) not null,
  fase char(16),
  termino_previsto bigint(20),
  termino bigint(20),
  primary key (id, inicio),
  key fase_fase (fase),
  key fase_inicio (inicio),
  key fase_termino (termino)
) type=innodb;

create table comentario (
  id int(11) not null,
  criacao bigint(20),
  criador int(11),
  comentario varchar(250),
  primary key (id, criacao)
) type=innodb;

create table compromisso (
  id int(11) not null,
  descricao char(250),
  inicio bigint(20),
  termino bigint(20),
  primary key (id),
  key compromisso_inicio (inicio)
) type=innodb;

create table agenda_ocorrencia (
  id int(11) not null,
  ocorrencia char(32),
  tipo char(32),
  primary key (id),
) type=innodb;

create table agenda_tarefa (
  id int(11) not null,
  tipo char(32),
  dias_previstos int(11),
  dias_trabalhados int(11),
  horas_previstos int(11),
  horas_trabalhados int(11),      
  primary key (id),
) type=innodb;

create table agendafinanceira (
  id int(11) not null,
  tipooperacao varchar(32),
  observacao char(250),
  inicio bigint(20),
  termino bigint(20),
  dtvencimento bigint(20),
  dtrealizado bigint(20),
  valorprevisto double,
  valorrealizado double,
  primary key (id),
  key afinanceira_inicio (inicio)
) type=innodb;


insert into entidade (id, classe, nome, superior, responsavel, criacao, atualizacao)
	values (1, 'Usuario', 'Administrador', 0, 1, 0, 0);
insert into pessoa_fisica (id, cpf, sexo, nascimento)
	values (1, '', 'M', 0);
insert into usuario (id, chave, senha)
	values (1, 'admin', 'admin');

grant all privileges on crm2.* to crm2@localhost identified by 'crm2';

commit;
