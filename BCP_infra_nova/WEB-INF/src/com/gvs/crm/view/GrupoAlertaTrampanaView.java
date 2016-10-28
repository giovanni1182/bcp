package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.GrupoSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class GrupoAlertaTrampanaView extends PortalView
{
	private Collection<Entidade> aseguradoras;
	
	public GrupoAlertaTrampanaView(Collection<Entidade> aseguradoras)
	{
		this.aseguradoras = aseguradoras;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Aseguradora");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Grupo");
		Aseguradora aseg;
		
		for(Entidade e : aseguradoras)
		{
			aseg = (Aseguradora) e;
			
			table.add(e.obterNome());
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new GrupoSelect("grupo_"+e.obterId(), aseg.obterGrupoAlertaTrempana()));
		}
		
		Button atualizarButton = new Button("Actualizar",new Action("grupoAlertaTrempana"));
		table.addFooter(atualizarButton);
		
		return table;
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
		return new Label("Grupo para Alerta Temprana");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}