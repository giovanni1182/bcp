package com.gvs.crm.model;

import java.math.BigDecimal;
import java.util.Date;

public interface MfAlertaTrempana extends Evento
{
	void incluir() throws Exception;
	void atribuirMes(int mes);
	void atribuirAno(int ano);
	void atribuirCodigoAtivo(int codigoAtivo);
	void atribuirCodigoInstrumento(CodigoInstrumento codigoInstrumento);
	void atribuirDataExtincao(Date dataExtincao);
	void atribuirEmissor(Emissor emissor);
	void atribuirQualificadora(Qualificadora qualificadora);
	void atribuirQualificacao(Qualificacao qualificacao);
	void atribuirValor(BigDecimal valor);
	void atribuirPorcentagemAcoes(BigDecimal porcentagemAcoes);
	void atribuirMercado(String mercado);
	void atribuirPatrimonio(BigDecimal patrimonio);
	void atribuirNumeroFinca(long numeroFinca);
	void atribuirLocalidade(Localidade localidade);
	void atribuirContaCorrente(String contaCorrente);
	void atribuirRestringido(String restringido);
	void atribuirValorRepresentativo(BigDecimal valorRepresentativo);
	void atribuirTipoInversao(int tipoInversao);
}
