package com.gvs.crm.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EntidadeBCP;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class MaioresOcorrenciasView extends BasicView
{

	private Aseguradora aseguradora;
	private String tipoPessoa, modalidade;
	private String tipoValor;
	private Date dataInicio;
	private Date dataFim;
	private int qtde;
	private String situacao;
	private File fileLavagem;
	private boolean mostrarTela;
	private boolean gerarArquivo;
	private Collection<Apolice> apolices = new ArrayList<Apolice>();
	private String secao, tipoInstrumento;
	
	public MaioresOcorrenciasView(Aseguradora aseguradora,String tipoPessoa,String tipoValor,Date dataInicio,Date dataFim,int qtde, String situacao, File fileLavagem, boolean mostrarTela, boolean gerarArquivo, Collection<Apolice> apolices, String secao, String modalidade, String tipoInstrumento) throws Exception
	{
		this.aseguradora = aseguradora;
		this.tipoPessoa = tipoPessoa;
		this.tipoValor = tipoValor;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.qtde = qtde;
		this.situacao = situacao;
		this.fileLavagem = fileLavagem;
		this.mostrarTela = mostrarTela;
		this.gerarArquivo = gerarArquivo;
		this.apolices = apolices;
		this.secao = secao;
		this.modalidade = modalidade;
		this.tipoInstrumento = tipoInstrumento;
	}
	
	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(13);
		table.setWidth("100%");
		table.addSubtitle("Mayores Pólizas - " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataInicio) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataFim));
		table.addStyle(Table.STYLE_ALTERNATE);
		
		if(this.apolices.size() > 0)
		{
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Instrumento");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Sección");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Asegurado");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tp. Persona");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Identificación");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tomador");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			if(tipoValor.equals("valorPrima"))
				table.addHeader("Vl. Prima Gs");
			else if(tipoValor.equals("valorSinistro"))
				table.addHeader("Vl. Siniestros Gs");
			else
				table.addHeader("Vl. Capital en Riesgo Gs");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Situacion");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ini. de Vigencia");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fin de Vigencia");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Últ.Mes Grabado");
			
			Collection<EntidadeBCP> pessoas;
			Block blockV,blockVTp,blockVDoc;
			String tipoDoc;
			
			for(Apolice apolice : this.apolices)
			{
				blockV = new Block(Block.VERTICAL);
				blockVTp = new Block(Block.VERTICAL);
				blockVDoc = new Block(Block.VERTICAL);
				
				tipoDoc = "";
				
				table.add(apolice.obterOrigem().obterNome());
				
				Link link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
				link.getAction().add("id", apolice.obterId());
				
				table.add(link);
				table.add(apolice.obterPlano().obterSecao());
				
				String nomeAsegurado = apolice.obterNomeAsegurado();
				
				String tipoPessoa = apolice.obterTipoPessoa();
				if(tipoPessoa!=null)
					blockVTp.add(new Label(tipoPessoa.trim()));
				
				if(apolice.obterTipoIdentificacao()!=null)
					tipoDoc = apolice.obterTipoIdentificacao();
				
				String numeroIdentificacao = apolice.obterNumeroIdentificacao();
				if(numeroIdentificacao!=null)
					tipoDoc+=": " + numeroIdentificacao.trim();
				
				blockV.add(new Label(nomeAsegurado));
				blockVDoc.add(new Label(tipoDoc));
				
				pessoas = apolice.obterAsegurados();
				for(EntidadeBCP pessoa : pessoas)
				{
					blockV.add(new Label(pessoa.getNome()));
					
					blockVTp.add(new Label(pessoa.getTipoPessoa()));
					blockVDoc.add(new Label(pessoa.getTipoDocumento() + ": " + pessoa.getNumeroDoc()));
				}
				
				table.add(blockV);
				table.add(blockVTp);
				table.add(blockVDoc);
				
				blockV = new Block(Block.VERTICAL);
				
				String nomeTomador = apolice.obterNomeTomador();
				
				blockV.add(new Label(nomeTomador));
				pessoas = apolice.obterTomadores();
				for(EntidadeBCP pessoa : pessoas)
				{
					blockV.add(new Label(pessoa.getNome()));
					/*tomador+=" - " + pessoa.getTipoPessoa();
					tomador+=" - " + pessoa.getTipoDocumento();
					tomador+=": " + pessoa.getNumeroDoc();*/
				}
				
				table.add(blockV);
				
				double valor = 0;
				if(tipoValor.equals("valorPrima"))
					valor = apolice.obterPrimaGs();
				else if(tipoValor.equals("valorSinistro"))
					valor = apolice.obterValorTotalDosSinistros(this.dataInicio,this.dataFim);
				else
					valor = apolice.obterCapitalGs();
					
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(valor,"#,##0.00"));
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(apolice.obterSituacaoSeguro());
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()));
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
				
				Aseguradora aseg = (Aseguradora) apolice.obterOrigem();
				AgendaMovimentacao ag = aseg.obterUltimaAgendaMCI();
				String mesAno = "";
				if(ag!=null)
					mesAno = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(mesAno);
			}
		}
		else
		{
			table.setNextColSpan(table.getColumns());
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Niguna poliza en el periodo");
		}
		
		Button voltarButton = new Button("Volver",new Action("visualizarEstatistica"));
		if(this.aseguradora!=null)
			voltarButton.getAction().add("aseguradora", this.aseguradora.obterId());
		voltarButton.getAction().add("situacao", this.situacao);
		voltarButton.getAction().add("dataInicio", this.dataInicio);
		voltarButton.getAction().add("dataFim", this.dataFim);
		voltarButton.getAction().add("tipoPessoa", this.tipoPessoa);
		voltarButton.getAction().add("tipoValor", this.tipoValor);
		voltarButton.getAction().add("qtde", this.qtde);
		voltarButton.getAction().add("tela", this.mostrarTela);
		voltarButton.getAction().add("modalidade", this.modalidade);
		voltarButton.getAction().add("tipoInstrumento", this.tipoInstrumento);
		//voltarButton.getAction().add("arquivo", this.gerarArquivo);
		if(this.secao!=null)
			voltarButton.getAction().add("secao", this.secao);
		voltarButton.getAction().add("view", true);
		
		table.addFooter(voltarButton);
		
		return new Border(table);
	}

}
