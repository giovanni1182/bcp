package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CentralRiscoSinistroView extends PortalView 
{

	private Entidade entidade;
	private Collection aseguradoras;
	private String nome,documento;
	private Aseguradora aseguradora;
	private Date dataInicio;
	private Date dataFim;
	
	private static final int FINIQUITADOS = 0;
	private static final int VIGENTES = 1;
	private static final int TOTALIZACAO = 2;
	
	public CentralRiscoSinistroView(Entidade entidade, Collection aseguradoras, String nome, Aseguradora aseguradora, Date dataInicio, Date dataFim, String documento) throws Exception
	{
		this.entidade = entidade;
		this.aseguradoras = aseguradoras;
		this.nome = nome;
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.documento = documento;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		int _pasta = Integer.parseInt(properties.getProperty("_pastaSinistro", "0"));
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		
		if (_pasta > 2)
			_pasta = FINIQUITADOS;

		Link finiquitadosLink = new Link("Siniestros Finiquitados", new Action("visualizarCentralRiscoSinistro"));
		((Label) finiquitadosLink.getCaption()).setBold(_pasta == FINIQUITADOS);
		finiquitadosLink.getAction().add("_pastaSinistro", FINIQUITADOS);
		finiquitadosLink.getAction().add("view", true);
		
		Link vigentesLink = new Link("Siniestros Vigentes", new Action("visualizarCentralRiscoSinistro"));
		((Label) vigentesLink.getCaption()).setBold(_pasta == VIGENTES);
		vigentesLink.getAction().add("_pastaSinistro", VIGENTES);
		vigentesLink.getAction().add("view", true);
		
		Link totalizacaoLink = new Link("Totalización Siniestros", new Action("visualizarCentralRiscoSinistro"));
		((Label) totalizacaoLink.getCaption()).setBold(_pasta == TOTALIZACAO);
		totalizacaoLink.getAction().add("_pastaSinistro", TOTALIZACAO);
		totalizacaoLink.getAction().add("view", true);
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(finiquitadosLink);
		block.add(new SeparadorLabel());
		block.add(vigentesLink);
		block.add(new SeparadorLabel());
		block.add(totalizacaoLink);
		
		//table.setNextColSpan(table.getColumns());
		mainTable.add(block);
		
		//table.setNextColSpan(table.getColumns());
		mainTable.add(new Space());
		
		if(_pasta!=2)
		{
			table.addHeader("Nombre Asegurado:");
			
			block = new Block(Block.HORIZONTAL);
			block.add(new InputString("nome", this.nome, 80));
			block.add(new Space(2));
			block.add(new Label("Al menos 10 caracteres"));
			table.add(block);
			
			table.addHeader("Nº del Documento:");
			
			block = new Block(Block.HORIZONTAL);
			block.add(new InputString("documento", this.documento, 20));
			block.add(new Space(2));
			block.add(new Label("Al menos 5 dígitos"));
			table.add(block);
			
			Button buscarButton = new Button("Buscar Siniestros", new Action("visualizarCentralRiscoSinistro"));
			
			table.addFooter(buscarButton);
			
			mainTable.add(table);
			
			Table table2 = new Table(1);
			table2.setWidth("100%");
			table2.addSubtitle("Siniestros");
		
			if(this.aseguradoras.size() == 0)
			{
				table2.addStyle(Table.STYLE_ALTERNATE);
				table2.setNextHAlign(Table.HALIGN_CENTER);
				table2.addHeader("Ningún Siniestro");
			}
			else
			{
				if(_pasta == FINIQUITADOS)
					table2.add(new CentralRiscoSinistrosFiniquitadosView(user, this.aseguradoras, this.nome, documento));
				else if(_pasta == VIGENTES)
					table2.add(new CentralRiscoSinistrosVigentesView(user, this.aseguradoras, this.nome, documento));
			}
			
			//table.setNextColSpan(table.getColumns());
			//table.add(table2);
			mainTable.add(table2);
		}	
		else
		{
			//Collection aseguradorasSinistro = new ArrayList();
			CRMModelManager mm = new CRMModelManager(user);
			//AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome"); 
			
			//aseguradorasSinistro = aseguradoraHome.obterAseguradoras();
			EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
			
			//table.add(new CentralDeRiscoTotalizacaoSinistrosView(this.aseguradora, this.dataInicio, this.dataFim, home));
			mainTable.add(new CentralDeRiscoTotalizacaoSinistrosView(this.aseguradora, this.dataInicio, this.dataFim, home));
			
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
		return new Label("Lista de Siniestros - " + this.entidade.obterNome());
	}

	public Entidade getOrigemMenu() throws Exception 
	{
		return this.entidade;
	}
}