package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class AnuarioView extends PortalView {

	private Entidade entidade;
	private String mes;
	private String ano;
	private Aseguradora aseguradora;
	private String tipoPessoa;
	private String tipoValor;
	private Date dataInicio;
	private Date dataFim;
	private int qtde;
	private String situacao;
	private boolean geral;
	
	private static final int DETALHE = 0;

	//private static final int LAVAGEM_DE_DINHEIRO = 1;

	public AnuarioView(Entidade entidade, String mes, String ano,Aseguradora aseguradora,String tipoPessoa,String tipoValor,Date dataInicio,Date dataFim,int qtde, String situacao, boolean geral)throws Exception 
	{
		this.entidade = entidade;
		this.mes = mes;
		this.ano = ano;
		this.aseguradora = aseguradora;
		this.tipoPessoa = tipoPessoa;
		this.tipoValor = tipoValor;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.qtde = qtde;
		this.situacao = situacao;
		this.geral = geral;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		Table table = new Table(6);
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome home = (UsuarioHome) mm.getHome("UsuarioHome");
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		Usuario usuario = home.obterUsuarioPorUser(user);
		
		int _pasta = Integer.parseInt(properties.getProperty("_pastaBoletin","0"));
		
		/*Link detalheLink = new Link("Detalles", new Action("exibirAnuario"));
		((Label) detalheLink.getCaption()).setBold(_pasta == DETALHE);
		detalheLink.getAction().add("_pastaBoletin", DETALHE);*/

		/*Link lavagemLink = new Link("Lavado de Dinero", new Action("exibirAnuario"));
		((Label) lavagemLink.getCaption()).setBold(_pasta == LAVAGEM_DE_DINHEIRO);
		lavagemLink.getAction().add("_pastaBoletin", LAVAGEM_DE_DINHEIRO);*/

		/*Block block2 = new Block(Block.HORIZONTAL);
		block2.add(detalheLink);*/
		/*block2.add(new SeparadorLabel());
		block2.add(lavagemLink);*/
		//table.setNextColSpan(table.getColumns());
		//table.add(block2);
		
		//if(_pasta > 1)
		//	_pasta = DETALHE;

		if(_pasta == DETALHE)
		{
			table.addSubtitle("Generar Boletíns");
			
			if(geral)
			{
				/*if(opcaoHome.obterUsuarios(99).contains(usuario))
				{
					table.add(new Check("agentesDeSeguros", "True", false));
					table.add("Agentes De Seguros");
			
					table.add(new Check("aseguradorasEndereco", "True", false));
					table.add("Aseguradoras");
			
					table.add(new Check("auditores", "True", false));
					table.add("Auditores");
			
					table.add(new Check("corredoresDeSeguros", "True", false));
					table.add("Corredores de Seguros");
			
					table.add(new Check("grupoCoasegurador", "True", false));
					table.add("Grupo Coasegurador");
			
					table.add(new Check("liquidadores", "True", false));
					table.add("Liquidadores");
			
					table.add(new Check("reaseguradora", "True", false));
					table.add("Reaseguradora");
					
					table.add(new Check("ativos", "True", false));
					table.add("Activos");
					
					table.add(new Check("pasivos", "True", false));
					table.add("Pasivos");
					
					table.add(new Check("patrimonioNeto", "True", false));
					table.add("Patrimonio Neto");
					
					table.add(new Check("ingresos", "True", false));
					table.add("Ingresos");
					
					table.add(new Check("engresos", "True", false));
					table.add("Egresos");
				}*/
				
				boolean contem = false;
				
				if(opcaoHome.obterUsuarios(1).contains(usuario) || usuario.obterNivel().equals(Usuario.ADMINISTRADOR))
				{
					table.add(new Check("contas", "True", false));
					table.add("Plan de Cuentas(Aseguradoras)");
					contem = true;
				}
				
				if(opcaoHome.obterUsuarios(2).contains(usuario)  || usuario.obterNivel().equals(Usuario.ADMINISTRADOR))
				{
					table.add(new Check("contas2", "True", false));
					table.add("Plan de Cuentas(Grupo Coasegurador)");
					contem = true;
				}
				
				/*if(opcaoHome.obterUsuarios(99).contains(usuario))
				{
					table.add(new Check("nomesBranco", "True", false));
					table.add("Nombre Asegurado en blanco");
					
					table.add(new Check("ciRucNaoEncontrados", "True", false));
					table.setNextColSpan(table.getColumns() - 1);
					table.add("CI y RUC no encontrados en Base de datos CRM(todas Aseguradoras)");
				}*/
				
				if(contem)
				{
					table.add("");
					table.add("");
					table.add("");
					table.add("");
			
					table.setNextColSpan(table.getColumns());
					table.add(new Space());
			
					Block block = new Block(Block.HORIZONTAL);
			
					Label label = new Label("Mes:");
					label.setBold(true);
					Label label2 = new Label("Año:");
					label2.setBold(true);
			
					block.add(label);
					block.add(new InputString("mes", this.mes, 2));
					block.add(new Space(3));
					block.add(label2);
					block.add(new InputString("ano", this.ano, 4));
			
					table.setNextColSpan(table.getColumns());
					table.add(block);
					
					Button button = new Button("Generar Boletín", new Action("gerarAnuario"));
					button.getAction().add("geral", geral);
					
					table.addFooter(button);
				}
			}
			else
			{
				table.add(new Check("agentesDeSeguros", "True", false));
				table.add("Agentes De Seguros");
		
				table.add(new Check("aseguradorasEndereco", "True", false));
				table.add("Aseguradoras");
		
				table.add(new Check("auditores", "True", false));
				table.add("Auditores");
		
				table.add(new Check("corredoresDeSeguros", "True", false));
				table.add("Corredores de Seguros");
		
				table.add(new Check("grupoCoasegurador", "True", false));
				table.add("Grupo Coasegurador");
		
				table.add(new Check("liquidadores", "True", false));
				table.add("Liquidadores");
		
				table.add(new Check("reaseguradora", "True", false));
				table.add("Reaseguradora");
				
				table.add(new Check("ativos", "True", false));
				table.add("Activos");
				
				table.add(new Check("pasivos", "True", false));
				table.add("Pasivos");
				
				table.add(new Check("patrimonioNeto", "True", false));
				table.add("Patrimonio Neto");
				
				table.add(new Check("ingresos", "True", false));
				table.add("Ingresos");
				
				table.add(new Check("engresos", "True", false));
				table.add("Egresos");
				
				table.add(new Check("contas", "True", false));
				table.add("Plan de Cuentas(Aseguradoras)");
				
				table.add(new Check("contas2", "True", false));
				table.add("Plan de Cuentas(Grupo Coasegurador)");
				
				table.add(new Check("nomesBranco", "True", false));
				table.add("Nombre Asegurado en blanco");
				
				table.add(new Check("ciRucNaoEncontrados", "True", false));
				table.setNextColSpan(table.getColumns() - 1);
				table.add("CI y RUC no encontrados en Base de datos CRM(todas Aseguradoras)");
				
				table.add("");
				table.add("");
				table.add("");
				table.add("");
		
				table.setNextColSpan(table.getColumns());
				table.add(new Space());
		
				Block block = new Block(Block.HORIZONTAL);
		
				Label label = new Label("Mes:");
				label.setBold(true);
				Label label2 = new Label("Año:");
				label2.setBold(true);
		
				block.add(label);
				block.add(new InputString("mes", this.mes, 2));
				block.add(new Space(3));
				block.add(label2);
				block.add(new InputString("ano", this.ano, 4));
		
				table.setNextColSpan(table.getColumns());
				table.add(block);
				
				Button button = new Button("Generar Boletín", new Action("gerarAnuario"));
				button.getAction().add("geral", geral);
				
				table.addFooter(button);
			}
	
			/*table.add("");
			table.add("");
			table.add("");
			table.add("");
	
			table.setNextColSpan(table.getColumns());
			table.add(new Space());
	
			Block block = new Block(Block.HORIZONTAL);
	
			Label label = new Label("Mes:");
			label.setBold(true);
			Label label2 = new Label("Año:");
			label2.setBold(true);
	
			block.add(label);
			block.add(new InputString("mes", this.mes, 2));
			block.add(new Space(3));
			block.add(label2);
			block.add(new InputString("ano", this.ano, 4));
	
			table.setNextColSpan(table.getColumns());
			table.add(block);*/
		}
		/*else if(_pasta == LAVAGEM_DE_DINHEIRO)
		{
			table.setNextColSpan(table.getColumns());
			table.add(new LavagemDinheiroView(this.aseguradora,this.tipoPessoa,this.tipoValor,this.dataInicio,this.dataFim,this.qtde, this.situacao));
		}*/

		/*Button button = new Button("Generar Boletín", new Action("gerarAnuario"));
		button.getAction().add("geral", geral);*/
		/*if(_pasta == LAVAGEM_DE_DINHEIRO)
			button.getAction().add("lavagem","True");*/
		
		//table.addFooter(button);

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Boletín");
	}

	public Entidade getOrigemMenu() throws Exception {
		return this.entidade;
	}
}