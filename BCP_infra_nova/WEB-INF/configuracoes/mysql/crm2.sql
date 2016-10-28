create table entidade (
	id int(11) not null,
	classe char(32),
	apelido varchar(32),
	nome char(64),
	superior int(11),
	responsavel int(11),
	criacao bigint(20),
	atualizacao bigint(20),
	primary key  (id),
	key entidade_nome (nome),
	key entidade_superior (superior),
	key entidade_responsavel (responsavel),
	key entidade_criacao (criacao)
) type=innodb; 

create table entidade_atributo (
	entidade int(11) not null,
	nome varchar(32) not null,
	valor varchar(64),
	primary key (entidade, nome),
	key entidade_atributo_nome (nome)
) type=innodb;

create table entidade_contato (
	entidade int(11) not null,
	id int(11) not null,
	nome varchar(32),
	valor varchar(32),
	primary key (entidade, id),
	key entidade_contato_entidade (entidade),
	key entidade_contato_nome (nome)
) type=innodb;

create table entidade_endereco (
	entidade int(11) not null,
	id int(11) not null,
	nome varchar(32),
	cep varchar(16),
	rua varchar(64),
	numero varchar(16),
	complemento varchar(32),
	bairro varchar(32),	
	cidade varchar(32),
	estado varchar(16),
	pais varchar(16),
	primary key (entidade, id),
	key entidade_endereco_entidade (entidade)
) type=innodb;

create table usuario (
	id int(11) not null,
	chave char(16),
	senha char(16),
	primary key (id),
	key usuario_chave (chave)
) type=innodb; 

create table classificacao_produto (
	id int(11) not null,
	descricao varchar(250),
	primary key (id)
) type=innodb;

create table produto (
	id int(11) not null,
	codigo_externo varchar(32),
	unidade varchar(16),
	ativo char(1),
	primary key (id)
) type=innodb;

create table produto_apelido (
	id int(11) not null,
	apelido varchar(32) not null,
	primary key(id, apelido)
) type=innodb;

create table produto_fornecedor (
	id int(11) not null,
	fornecedor int(11) not null,
	codigo_externo varchar(32),
	primary key(id, fornecedor)
) type=innodb;

create table produto_preco (
	id int(11) not null,
	tipo varchar(32) not null,
	moeda varchar(8),
	valor decimal(15,2),
	primary key(id, tipo)
) type=innodb;

create table componente (
	id int(11) not null,
	produto int(11),
	quantidade decimal(15,4),
	primary key (id)
) type=innodb; 

create table estoque_produto (
	estoque int(11) not null,
	produto int(11) not null,
	quantidade double,
	quantidade_minima double,
	custo_medio decimal(15,2),
	primary key (entidade, produto)
) type=innodb;

create table evento (
	id int(11) not null,
	classe char(32),
	criador int(11),
	responsavel int(11),
	responsavel_anterior int(11),
	tipo varchar(32),
	titulo char(64),
	descricao text,
	origem int(11),
	destino int(11),
	data_prevista_inicio bigint(20),
	data_prevista_conclusao bigint(20),
	duracao bigint(20),
	prioridade int(1),
	superior int(11),
	lido char(1),
	criacao bigint(20),
	atualizacao bigint(20),
	primary key (id),
	key evento_titulo (titulo),
	key evento_responsavel (responsavel),
	key evento_origem (origem),
	key evento_destino (destino),
	key evento_criador (criador),
	key evento_criacao (criacao),
	key evento_atualizacao (atualizacao),
) type=innodb;

create table fase (
	id int(11) not null,
	codigo char(16),
	inicio bigint(20) not null,
	termino bigint(20),
	primary key (id, inicio),
	key fase_codigo (codigo),
	key fase_inicio (inicio),
	key fase_termino (termino)
) type=innodb;

create table comentario (
	id int(11) not null,
	titulo varchar(64),
	criacao bigint(20),
	comentario text,
	primary key (id, criacao)
) type=innodb;

create table ocorrencia (
	id int(11) not null,
	produto int(11),
	primary key (id)
) type=innodb;

create table tarefa (
	id int(11) not null,
	data_efetiva_inicio bigint(20),
	data_efetiva_conclusao bigint(20),
	duracao_efetiva bigint(20),
	primary key (id)
) type=innodb;

create table movimentacao_financeira (
	id int(11) not null,
	data_prevista bigint(20),
	data_realizada bigint(20),
	condicao_prevista varchar(32),
	condicao_realizada varchar(32), 
	valor_previsto double,
	valor_realizado double,
	dias double,
	primary key (id)
) type=innodb;

create table pedido (
	id int(11) not null,
	comissionado int(11) not null,
	numero varchar(32), 
	endereco_cobranca int(11),
	endereco_cliente int(11),
 	primary key (id)
 ) type=innodb;  
 
create table pedido_endereco (
	evento int(11) not null,
	cep varchar(16),
	rua varchar(64),
	numero varchar(16),
	complemento varchar(32),
	bairro varchar(32),	
	cidade varchar(32),
	estado varchar(16),
	pais varchar(16),
	primary key (evento),
) type=innodb;

create table item (
	id int(11) not null,
 	valor double,
 	primary key (id)
 ) type=innodb;

create table item_imposto (
 	item int(11) not null,
 	nome varchar(20) not null,
 	percentual decimal(15,4),
 	primary key (item, nome)
 ) type=innodb;

create table item_produto (
	id int(11) not null,
	produto int(11) not null,
 	valor_unitario double,
 	quantidade double, 	
 	primary key (id)
 ) type=innodb;
 
create table item_stella_atributo (
 	item int(11) not null,
 	nome varchar(20) not null,
 	valor varchar(20),
 	primary key (item, nome)
 ) type=innodb;

insert into entidade (id, classe, nome, superior, responsavel, criacao, atualizacao)
	values (1, 'Usuario', 'Administrador', 0, 1, 0, 0);
insert into usuario (id, chave, senha)
	values (1, 'admin', 'admin');
