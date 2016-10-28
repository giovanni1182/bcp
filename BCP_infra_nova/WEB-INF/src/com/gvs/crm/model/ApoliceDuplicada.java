package com.gvs.crm.model;

public interface ApoliceDuplicada extends Evento 
{
	void atribuirQtde(int qtde) throws Exception;
	void atribuirOrigem(Entidade entidade) throws Exception;
	void atribuirNumero(String numero) throws Exception;
	void atribuirSecao(ClassificacaoContas conta) throws Exception;
	void atribuirStatus(String status) throws Exception;
	void atribuirPlano(Plano plano) throws Exception;
	void atribuirEndoso(double endoso) throws Exception;
	void atribuirCertificado(double certificado) throws Exception;
	
	int obterQtde() throws Exception;
	Entidade obterOrigem() throws Exception;
	String obterNumero() throws Exception;
	ClassificacaoContas obterSecao() throws Exception;
	String obterStatus() throws Exception;
	Plano obterPlano() throws Exception;
	double obterEndoso() throws Exception;
	double obterCertificado() throws Exception;
}
