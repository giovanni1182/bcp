package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.PlanoSecaoSelect;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.TipoInstrumentosSelect;
import com.gvs.crm.component.TipoPessoaFJSelect;
import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeBCP;
import com.gvs.crm.model.Sinistro;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputDouble;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class MaioresOcorrenciaSinistroView extends PortalView
{
	private Aseguradora aseguradora;
	private String tipoPessoa, modalidade;
	private Date dataInicio;
	private Date dataFim;
	private int qtde;
	private String situacao;
	private Collection<Sinistro> sinistros = new ArrayList<Sinistro>();
	private String secao,tipoInstrumento;
	private double monto;
	
	public MaioresOcorrenciaSinistroView(Aseguradora aseguradora,String tipoPessoa,Date dataInicio,Date dataFim,int qtde, String situacao, Collection<Sinistro> sinistros, String secao, double monto, String modalidade, String tipoInstrumento) throws Exception
	{
		this.aseguradora = aseguradora;
		this.tipoPessoa = tipoPessoa;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.qtde = qtde;
		this.situacao = situacao;
		this.sinistros = sinistros;
		this.secao = secao;
		this.monto = monto;
		this.modalidade = modalidade;
		this.tipoInstrumento = tipoInstrumento;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradora",aseguradora, false, true));
		
		Action action = new Action("maioresSinistros");
		action.add("view", true);
		table.addHeader("Seccion:");
		table.add(new PlanoSecaoSelect("secao", this.secao, action));
		
		table.addHeader("Modalidad:");
		if(!this.secao.equals(""))
			table.add(new PlanoSelect("modalidade", this.secao, modalidade, null, true));
		else
			table.add("");
		
		table.addHeader("Instrumento:");
		table.add(new TipoInstrumentosSelect("tipoInstrumento", this.tipoInstrumento));
		
		table.addHeader("Tipo de Persona:");
		table.add(new TipoPessoaFJSelect("tipoPessoa",tipoPessoa));
		table.addHeader("Siniestros Ocurridos desde:");
		
		Block block = new Block(Block.HORIZONTAL);
		
		block.add(new InputDate("dataInicio",dataInicio));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim",dataFim));
		table.add(block);
		
		table.addHeader("Situacion:");
		table.add(new SituacaoApoliceSelect2("situacao",situacao,true));
		table.addHeader("Monto en Guaraníes:");
		table.add(new InputDouble("monto",this.monto,15));
		table.addHeader("Cantidad Solicitada:");
		table.add(new InputInteger("qtde",qtde,7));
		
		Button button = new Button("Visualizar en la pantalla",new Action("maioresSinistros"));
		Button button2 = new Button("Generar Excel",new Action("maioresSinistros"));
		button2.getAction().add("excel", true);
		
		table.addFooter(button);
		table.addFooter(button2);
		
		mainTable.add(table);
		
		int qtdeSinistros = this.sinistros.size(); 
		Collection<EntidadeBCP> pessoas;
		Block blockV,blockVTp,blockVDoc,blockVTipoDoc;
		String tipoDoc,numeroDoc;
		
		if(qtdeSinistros > 0)
		{
			table = new Table(16);
			table.addSubtitle(qtdeSinistros + " Siniestros");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.setWidth("100%");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradoras");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Instrumento");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Sección");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Asegurado");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tp. Persona");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tp. Doc");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Identificación");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tomador");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tp. Persona");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Tp. Doc");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Identificación");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Montante Gs");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Situacion");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ini. Vigencia");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fin Vigencia");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Últ. Mes Grabado");
			
			Apolice apolice;
			
			for(Sinistro sinistro : this.sinistros)
			{
				blockV = new Block(Block.VERTICAL);
				blockVTp = new Block(Block.VERTICAL);
				blockVDoc = new Block(Block.VERTICAL);
				blockVTipoDoc = new Block(Block.VERTICAL);
				
				tipoDoc = "";
				numeroDoc = "";
				
				apolice = (Apolice) sinistro.obterSuperior();
				
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
				
				if(tipoDoc.indexOf("Cédula de Identidad")>-1)
					tipoDoc = "CI";
				
				String numeroIdentificacao = apolice.obterNumeroIdentificacao();
				if(numeroIdentificacao!=null)
					numeroDoc = numeroIdentificacao.trim();
				
				blockV.add(new Label(nomeAsegurado));
				blockVTipoDoc.add(new Label(tipoDoc));
				blockVDoc.add(new Label(numeroDoc));
				
				pessoas = apolice.obterAsegurados();
				
				for(EntidadeBCP pessoa : pessoas)
				{
					blockV.add(new Label(pessoa.getNome()));
					blockVTp.add(new Label(pessoa.getTipoPessoa()));
					blockVTipoDoc.add(new Label(pessoa.getTipoDocumento()));
					blockVDoc.add(new Label(pessoa.getNumeroDoc()));
				}
				
				table.add(blockV);
				table.add(blockVTp);
				table.add(blockVTipoDoc);
				table.add(blockVDoc);
				
				blockV = new Block(Block.VERTICAL);
				blockVTp = new Block(Block.VERTICAL);
				blockVDoc = new Block(Block.VERTICAL);
				blockVTipoDoc = new Block(Block.VERTICAL);
				
				String nomeTomador = apolice.obterNomeTomador();
				blockV.add(new Label(nomeTomador));
				blockVTp.add(new Label(""));
				blockVTipoDoc.add(new Label(""));
				blockVDoc.add(new Label(""));
				
				pessoas = apolice.obterTomadores();
				for(EntidadeBCP pessoa : pessoas)
				{
					blockV.add(new Label(pessoa.getNome()));
					blockVTp.add(new Label(pessoa.getTipoPessoa()));
					blockVTipoDoc.add(new Label(pessoa.getTipoDocumento()));
					blockVDoc.add(new Label(pessoa.getNumeroDoc()));
				}
				
				table.add(blockV);
				table.add(blockVTp);
				table.add(blockVTipoDoc);
				table.add(blockVDoc);
				
				double valor = sinistro.obterMontanteGs();
					
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
			
			mainTable.add(table);
		}
		
		return mainTable;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}

	public String getSelectedGroup() throws Exception 
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return new Label("Mayores Ocurrencias de Siniestros");
	}
}