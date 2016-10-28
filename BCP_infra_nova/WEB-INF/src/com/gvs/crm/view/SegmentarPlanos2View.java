package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.PlanoRamoSelect;
import com.gvs.crm.component.PlanoSecao2Select;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.PlanoHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class SegmentarPlanos2View extends PortalView
{
	private String tipo;
	private Collection<String> dados;
	
	public SegmentarPlanos2View(String tipo, Collection<String> dados)
	{
		this.tipo = tipo;
		this.dados = dados;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		PlanoHome planoHome = (PlanoHome) mm.getHome("PlanoHome");
		
		Table table = new Table(2);
		table.addStyle(Table.STYLE_ALTERNATE);
		int cont = 1;
		
		if(this.tipo.equals("secao"))
		{
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Sección");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Ramo");
			
			InputString input;
			for(String dado : this.dados)
			{
				input = new InputString("secao"+cont, dado, 40);
				input.setEnabled(false);
				table.add(input);
				
				table.add(new PlanoRamoSelect("ramo"+cont, planoHome.obterSegRamo(dado), null, true, false));
				
				cont++;
			}
		}
		
		if(this.tipo.equals("modalidade"))
		{
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Modalidad");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Sección");
			
			InputString input;
			for(String dado : this.dados)
			{
				input = new InputString("modalidade"+cont, dado, 40);
				input.setEnabled(false);
				table.add(input);
				
				table.add(new PlanoSecao2Select("secao"+cont, "",  planoHome.obterSegSecao(dado), null));
				
				cont++;
			}
		}
		
		Button atualizarButton = new Button("Actualizar", new Action("atualizarSegmentosPlano"));
		atualizarButton.getAction().add("tipo", this.tipo);
		
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
		return new Label("Segmentar Planos");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}