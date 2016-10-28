package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.PeriodoDatasBlock;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EnvioArquivos;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class RelEnvioArquivosView extends PortalView 
{
	private Date dataInicio, dataFim;
	private Collection<EnvioArquivos> envios;
	private Aseguradora aseguradora;
	private Usuario usuario;
	
	public RelEnvioArquivosView(Entidade aseguradora, Date dataInicio, Date dataFim, Collection<EnvioArquivos> envios, Usuario usuario)
	{
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.envios = envios;
		this.aseguradora = (Aseguradora) aseguradora;
		this.usuario = usuario;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		
		boolean admin = usuarioAtual.obterId() == 1;
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		if(admin)
		{
			table.addHeader("Aseguradora:");
			table.add(new AseguradorasSelect("aseguradoraId", aseguradora, true));
			table.addHeader("Usuário:");
			table.add(new EntidadePopup("usuarioId", "usuarioNome", usuario, "usuario", true));
		}
		
		table.addHeader("Periodo:");
		table.add(new PeriodoDatasBlock("dataInicio", dataInicio, "dataFim", dataFim));
		
		Button consultarButton = new Button("Buscar", new Action("relArquivosEnviados"));
		table.addFooter(consultarButton);
		
		mainTable.add(table);
		
		int qtde = envios.size();
		if(qtde > 0)
		{
			table = new Table(3);
			
			table.setWidth("40%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle(qtde + " Envio(s)");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Seguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Envio");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Archivos");
			
			Link link;
			
			for(EnvioArquivos envio : envios)
			{
				table.add(envio.obterOrigem().obterNome());
				
				link = new Link(envio.obterTitulo(), new Action("visualizarEvento"));
				link.getAction().add("id", envio.obterId());
				link.setNovaJanela(true);
				
				table.add(link);
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(new Label(envio.obterArquivos().size()));
			}
			
			mainTable.add(table);
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
		return new Label("Archivos Enviados");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}