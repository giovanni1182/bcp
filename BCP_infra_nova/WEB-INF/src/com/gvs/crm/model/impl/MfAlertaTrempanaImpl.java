package com.gvs.crm.model.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gvs.crm.model.CodigoInstrumento;
import com.gvs.crm.model.Emissor;
import com.gvs.crm.model.Localidade;
import com.gvs.crm.model.MfAlertaTrempana;
import com.gvs.crm.model.Qualificacao;
import com.gvs.crm.model.Qualificadora;

import infra.sql.SQLUpdate;

public class MfAlertaTrempanaImpl extends EventoImpl implements MfAlertaTrempana
{
	private int codigoAtivo,tipoInversao,mes,ano;
	private CodigoInstrumento codigoInstrumento;
	private Date dataExtincao;
	private Emissor emissor;
	private Qualificadora qualificadora;
	private Qualificacao qualificacao;
	private BigDecimal valor,porcentagemAcoes,patrimonio,valorRepresentativo;
	private String mercado,contaCorrente,restringido;
	private long numeroFinca;
	private Localidade localidade;
	
	public void incluir() throws Exception
	{
		super.incluir();
		
		String mesAno = "01/"+mes+"/"+ano;
		
		Date dataMesAno = new SimpleDateFormat("dd/MM/yyyy").parse(mesAno);
		
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into mf_alerta_temprana(id,mes,ano,mes_ano,cod_ativo,cod_instrumento,data_extincao,emissor,qualificadora,qualificacao,valor,porcentagem_acoes,mercado,patrimonio,numero_finca,localidade,conta_corrente,restringido,valor_representativo,tipo_inversao) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		insert.addLong(this.obterId());
		insert.addInt(mes);
		insert.addInt(ano);
		insert.addLong(dataMesAno.getTime());
		insert.addInt(codigoAtivo);
		insert.addLong(codigoInstrumento.obterId());
		if(dataExtincao!=null)
			insert.addLong(dataExtincao.getTime());
		else
			insert.addLong(null);
		if(emissor!=null)
			insert.addLong(emissor.obterId());
		else
			insert.addLong(null);
		if(qualificadora!=null)
			insert.addLong(qualificadora.obterId());
		else
			insert.addLong(null);
		if(qualificacao!=null)
			insert.addLong(qualificacao.obterId());
		else
			insert.addLong(null);
		
		insert.addDouble(valor.doubleValue());
		insert.addDouble(porcentagemAcoes.doubleValue());
		insert.addString(mercado);
		insert.addDouble(patrimonio.doubleValue());
		insert.addLong(numeroFinca);
		
		if(localidade!=null)
			insert.addLong(localidade.obterId());
		else
			insert.addLong(null);
		
		insert.addString(contaCorrente);
		insert.addString(restringido);
		insert.addDouble(valorRepresentativo.doubleValue());
		insert.addInt(tipoInversao);
		
		insert.execute();
	}
	
	public void atribuirMes(int mes)
	{
		this.mes = mes;
	}
	
	public void atribuirAno(int ano)
	{
		this.ano = ano;
	}
	
	public void atribuirCodigoAtivo(int codigoAtivo)
	{
		this.codigoAtivo = codigoAtivo;
	}

	public void atribuirCodigoInstrumento(CodigoInstrumento codigoInstrumento) 
	{
		this.codigoInstrumento = codigoInstrumento;
	}

	public void atribuirDataExtincao(Date dataExtincao)
	{
		this.dataExtincao = dataExtincao;
	}

	public void atribuirEmissor(Emissor emissor)
	{
		this.emissor = emissor;
	}

	public void atribuirQualificadora(Qualificadora qualificadora)
	{
		this.qualificadora = qualificadora;
	}

	public void atribuirQualificacao(Qualificacao qualificacao)
	{	
		this.qualificacao = qualificacao;
	}

	public void atribuirValor(BigDecimal valor)
	{
		this.valor = valor;
	}

	public void atribuirPorcentagemAcoes(BigDecimal porcentagemAcoes)
	{
		this.porcentagemAcoes = porcentagemAcoes;
	}

	public void atribuirMercado(String mercado)
	{
		this.mercado = mercado;
	}

	public void atribuirPatrimonio(BigDecimal patrimonio)
	{
		this.patrimonio = patrimonio;
	}

	public void atribuirNumeroFinca(long numeroFinca)
	{
		this.numeroFinca = numeroFinca;
	}

	public void atribuirLocalidade(Localidade localidade)
	{
		this.localidade = localidade;
	}

	public void atribuirContaCorrente(String contaCorrente)
	{
		this.contaCorrente = contaCorrente;
	}

	public void atribuirRestringido(String restringido)
	{
		this.restringido = restringido;
	}

	public void atribuirValorRepresentativo(BigDecimal valorRepresentativo)
	{
		this.valorRepresentativo = valorRepresentativo;
	}

	public void atribuirTipoInversao(int tipoInversao)
	{
		this.tipoInversao = tipoInversao;
	}

}
