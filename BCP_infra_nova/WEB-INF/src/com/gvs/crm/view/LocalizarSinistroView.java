package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.PlanoSecaoSelect;
import com.gvs.crm.component.SituacaoApoliceSelect2;
import com.gvs.crm.component.SituacaoSinistroSelect;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Sinistro;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class LocalizarSinistroView extends PortalView
{
	private Aseguradora aseguradora;
	private String secao;
	private String situacao;
	private Date dataInicio, dataFim;
	private Collection<Sinistro> sinistros;
	private boolean listar;
	private int qtde;
	private String nomeAsegurado,situacaoSinistro;
	
	public LocalizarSinistroView(Aseguradora aseguradora, String secao, String situacao, Date dataInicio, Date dataFim, Collection<Sinistro> sinistros, boolean listar, String nomeAsegurado, String situacaoSinistro)
	{
		this.aseguradora = aseguradora;
		this.secao = secao;
		this.situacao = situacao;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.sinistros = sinistros;
		this.listar = listar;
		this.qtde = sinistros.size();
		this.nomeAsegurado = nomeAsegurado;
		this.situacaoSinistro = situacaoSinistro;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId",aseguradora, false, true));
		table.addHeader("Situación del Siniestro:");
		table.add(new SituacaoSinistroSelect("situacaoSinistro", situacaoSinistro, true));
		table.addHeader("Modalidad:");
		table.add(new PlanoSecaoSelect("secao", this.secao, null));
		table.addHeader("Situación:");
		table.add(new SituacaoApoliceSelect2("situacao",this.situacao,true));
		table.addHeader("Fecha Emisión:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio",this.dataInicio));
		block.add(new Space(2));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim",this.dataFim));
		
		table.add(block);
		
		table.addHeader("Nombre Asegurado:");
		table.add(new InputString("nomeAsegurado", this.nomeAsegurado, 60));
		
		Action localizarEntidadeAction = new Action("localizarSinistros");
		localizarEntidadeAction.add("listar", true);
		table.addFooter(new Button("Buscar Siniestros", localizarEntidadeAction));
		
		localizarEntidadeAction = new Action("localizarSinistros");
		localizarEntidadeAction.add("listar", true);
		localizarEntidadeAction.add("excel", true);
		table.addFooter(new Button("Generar excel", localizarEntidadeAction));
		
		mainTable.add(table);
		
		if(qtde > 0)
		{
			table = new Table(15);
			table.setWidth("100%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle(qtde + " Siniestros");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Póliza");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Siniestro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Modalidad");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fecha Siniestro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fecha Denuncia");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Liquidador");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Monto Gs");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Monto ME");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Situación del Siniestro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Recup. Terceiro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Fecha Recupero");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Finaliz. Pago");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Recup. Reaseguro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Partic. Reaseguro");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Descripción");
			
			Map<Long, Sinistro> sinistrosOrdenados = new TreeMap<>();
			
			int cont = 1;
			
			//DEIXAR ESSA ORDENAÇÃO POR CAUSA DAS DUPLICIDADES
			for(Sinistro sinistro : this.sinistros)
			{
				sinistrosOrdenados.put(sinistro.obterDataPrevistaInicio().getTime() + cont, sinistro);
				cont++;
			}
			
			Apolice apolice;
			Plano plano;
			Link link;
			Date dataDenuncia,dataRecupero,dataPagamento;
			
			for(Sinistro sinistro : sinistrosOrdenados.values())
			{
				apolice = (Apolice) sinistro.obterSuperior();
				plano = apolice.obterPlano();
				link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
				link.getAction().add("id", apolice.obterId());
				link.setNovaJanela(true);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(link);
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(sinistro.obterNumero());
				table.setNextHAlign(Table.HALIGN_CENTER);
				if(plano!=null)
					table.add(plano.obterSecao());
				else
					table.add("");
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(sinistro.obterDataSinistro(),"dd/MM/yyyy"));
				table.setNextHAlign(Table.HALIGN_CENTER);
				
				dataDenuncia = sinistro.obterDataDenuncia(); 
				
				if(dataDenuncia!=null)
					table.add(new Label(dataDenuncia,"dd/MM/yyyy"));
				else
					table.add("");
				
				if (sinistro.obterAuxiliar() != null)
					table.add(sinistro.obterAuxiliar().obterInscricaoAtiva().obterInscricao() + " - " + sinistro.obterAuxiliar().obterNome());
				else
					table.add("");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(sinistro.obterMontanteGs(),"#,##0.00"));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(sinistro.obterMontanteMe(),"#,##0.00"));
				table.add(sinistro.obterSituacao());
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(sinistro.obterValorRecuperacaoTerceiro(), "#,##0.00"));
				table.setNextHAlign(Table.HALIGN_CENTER);
				dataRecupero = sinistro.obterDataRecuperacao(); 
				if (dataRecupero!= null)
					table.add(new Label(dataRecupero,"dd/MM/yyyy"));
				else
					table.add("");
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				dataPagamento = sinistro.obterDataPagamento(); 
				if (dataPagamento != null)
					table.add(new Label(dataPagamento,"dd/MM/yyyy"));
				else
					table.add("");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(sinistro.obterValorRecuperacao(),"#,##0.00"));
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(new Label(sinistro.obterParticipacao(),"#,##0.00"));
				table.add(sinistro.obterDescricao());
			}
			
			mainTable.add(table);
		}
		else
		{
			if(listar)
			{
				mainTable.setNextHAlign(Table.HALIGN_CENTER);
				mainTable.addHeader("No fue encontrado");
			}
		}
		
		return mainTable;
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
		return new Label("Buscar Siniestros");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}