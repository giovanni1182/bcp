create table desconto( 
	id int(11) not null, 
	valor double, 
	percentual double, 
	primary key (id) 
) type = innodb;

create table ordem_compra_nota_fiscal{
	id int(11) not null,
	numero_nota_fiscal varchar(30) not null,
	data_vencimento bigint(20),
	razao_social varchar(30),
	data_emissao bigint(20),
	inscricao_estadual varchar(30),
	natureza_operacao varchar(30),
	data_conferencia bigint(20),
	primary key (id, numero_nota_fiscal) 
}type=innodb;

create pedido_analise{
	id int(11) not null,
	valor double,
	primary key (id)
}type=innodb;


alter table movimentacao_produto add estoque int(11);
alter table movimentacao_produto add quantidade_expedida int(11);
alter table movimentacao_produto add fornecedor int(11);

create table ordem_compra_endereco (
	evento int(11) not null,
	cep varchar(16),
	rua varchar(64),
	numero varchar(16),
	complemento varchar(32),
	bairro varchar(32),	
	cidade varchar(32),
	estado varchar(16),
	pais varchar(16),
	contato varchar(32),
	telefone varchar(32),
	tipo_endereco varchar(32),
	primary key (evento,tipo_endereco)
) type=innodb;

alter table ordem_compra_endereco add tipo_endereco varchar(32);
alter table ordem_compra_endereco drop primary key;
alter table ordem_compra_endereco add primary key (evento,tipo_endereco);
alter table estoque_produto add reserva_entrada double;
alter table estoque_produto add quantidade_disponivel double;
alter table estoque_produto add reserva_saida double;

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
	rua varchar(32),
	numero varchar(16),
	complemento varchar(32),
	bairro varchar(32),	
	cidade varchar(32),
	estado varchar(16),
	pais varchar(16),
	primary key (entidade, id),
	key entidade_endereco_entidade (entidade)
) type=innodb;

insert into entidade_atributo (entidade, nome, valor) select entidade, tipo, valor from atributo;
insert into entidade_contato (entidade, id, nome, valor) select id, entidade, tipo, contato from contato;
insert into entidade_endereco (entidade, id, nome, cep, rua, numero, complemento, bairro, cidade, estado, pais) select entidade, id, tipo, cep, rua, numero, complemento, bairro, cidade, estado, pais from endereco;
insert into entidade_atributo (entidade, nome, valor) select id, 'localizacao', localizacao from departamento;
insert into entidade_atributo (entidade, nome, valor) select id, 'cnpj', cnpj from empresa;
insert into entidade_atributo (entidade, nome, valor) select id, 'razaosocial', razao_social from empresa;
insert into entidade_atributo (entidade, nome, valor) select id, 'cpf', cpf from pessoa_fisica;
insert into entidade_atributo (entidade, nome, valor) select id, 'sexo', sexo from pessoa_fisica;
insert into entidade_atributo (entidade, nome, valor) select id, 'nascimento',date_format(date_add(  '1970-01-01 00:00:00',  interval nascimento/1000 second), '%d/%m/%Y') from pessoa_fisica;

drop table atributo;
drop table contato;
drop table endereco;
drop table departamento;
drop table empresa;
drop table pessoa_fisica;
drop table grupo;

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
 
create table pedido (
	id int(11) not null,
	comissionado int(11) not null,
	numero varchar(32), 
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
	primary key (evento)
) type=innodb;

create table Meta
(
   id int(11) not null,
   valor double,
   quantidade double,
   classe varchar(32),
   tipo varchar(32),
   primary key (id)
)  type = innodb;

create table item_stella_atributo (
 	item int(11) not null,
 	nome varchar(20) not null,
 	valor varchar(20),
 	primary key (item, nome)
 ) type=innodb;

create table estoque_produto (
	estoque int(11) not null,
	produto int(11) not null,
	quantidade double,
	quantidade_minima double,
	custo_medio decimal(15,2),
	primary key (estoque, produto)
) type=innodb;

create table movimentacao_financeira (
	id int(11) not null,
	data_prevista bigint(20),
	data_realizada bigint(20),
	condicao_prevista varchar(32),
	condicao_realizada varchar(32), 
	valor_previsto double,
	valor_realizado double,
	primary key (id)
) type=innodb;  

create table movimentacao_produto (
	id int(11) not null,
	produto int(11) not null,
	quantidade double,
	valor_unitario double,
	primary key (id, produto)
) type=innodb;

alter table pedido add reserva_tecnica double;

alter table pedido add comissao double;

alter table pedido add valor_frete double;

alter table pedido add tipo_negocio varchar(32);

alter table pedido add tipo_perspectiva varchar(32);

alter table pedido add tipo_produto varchar(32);

alter table pedido add tipo_frete varchar(32);

alter table pedido add obra varchar(64);

alter table pedido add codigo_pedido int(11) not null;

alter table pedido add contato_cobranca int(11);

alter table pedido add contato_cliente int(11);

alter table pedido add motivo_cancelamento varchar(32);

alter table item add descricao_engetrel text;

alter table item add sobrepor_valor varchar(5);

alter table pedido_endereco add contato varchar(32);

alter table pedido_endereco add telefone varchar(32);

alter table pedido_endereco add tipo_endereco varchar(32);

alter table pedido_endereco drop primary key;

alter table pedido_endereco add primary key (evento,tipo_endereco);

alter table movimentacao_financeira add dias double;

alter table produto_fornecedor add valor double;

ALTER TABLE `movimentacao_financeira` CHANGE `valor_previsto` `valor_previsto` DECIMAL(15,2)

ALTER TABLE `movimentacao_financeira` CHANGE `valor_realizado` `valor_realizado` DECIMAL(15,2)

ALTER TABLE `produto_fornecedor` CHANGE `valor` `valor` DECIMAL(15,2)

alter table pedido add tipo_pedido varchar(32);

update evento set tipo = 'Proposta' where classe = 'Pedido';

alter table evento add ordem bigint(20);

alter table pedido add indicador int(11);
alter table pedido add prazo varchar(32);
alter table pedido add mao_obra varchar(32);
alter table pedido add valor_mao_obra double;
alter table pedido add mao_obra_descricao text;

alter table desconto add sinal int(1) default 0;

create table expedicao_item( 
	expedicao int(11) not null, 
	item int(11) not null, 
	superior int(11) not null, 		
	primary key (expedicao,item) 
) type = innodb;

create table pedido_analise( 
	id int(11) not null, 
	valor double, 
	primary key (id) 
) type = innodb;

create table expedicao( 
	id int(11) not null, 
	cfop varchar(32),
	data_emissao bigint(20),
	data_saida_entrada bigint(20),
	natureza varchar(64),
	numero_nota int(11),
	transportadora int(11),
	valor_frete double,
	valor_seguro double,	
	valor_total_nota double,
	valor_total_produtos double,	
	primary key (id) 
) type = innodb;
