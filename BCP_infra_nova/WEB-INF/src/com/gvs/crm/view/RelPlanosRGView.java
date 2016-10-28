package com.gvs.crm.view;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.AseguradorasSelect2;
import com.gvs.crm.component.SituacaoSeguroSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class RelPlanosRGView extends PortalView
{
	private Date dataInicio;
	private Date dataFim;
	private Aseguradora aseguradora;
	private Collection<String> informacoes;
	private String situacaoSeguro;
	private boolean especial, modificado;
	
	public RelPlanosRGView(Aseguradora aseguradora, String situacaoSeguro, Date dataInicio, Date dataFim, Collection<String> informacoes, boolean especial, boolean modificado)
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.informacoes = informacoes;
		this.situacaoSeguro = situacaoSeguro;
		this.especial = especial;
		this.modificado = modificado;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2); 
		
		table.addHeader("Aseguradora:");
		table.add(new AseguradorasSelect2("aseguradoraId", this.aseguradora, false, true));
		table.addHeader("Situacion del Seguro:");
		table.add(new SituacaoSeguroSelect("situacaoSeguro", this.situacaoSeguro));
		table.addHeader("Periodo:");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new InputDate("dataInicio", this.dataInicio));
		Label label = new Label("hasta");
		label.setBold(true);
		block.add(new Space(2));
		block.add(label);
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));
		
		table.add(block);
		
		Button button = new Button("Buscar",new Action("relPlanoRG"));
		button.getAction().add("especial", this.especial);
		button.getAction().add("modificado", this.modificado);
		table.addFooter(button);
		
		/*Button excelButton = new Button("Excel",new Action("relPlanoRG"));
		excelButton.getAction().add("excel", true);
		table.addFooter(excelButton);*/
		
		mainTable.add(table);
		
		if(informacoes.size() > 0)
		{
			table = new Table(2);
			table.setWidth("50%");
			table.addStyle(Table.STYLE_ALTERNATE);
			table.addSubtitle("");
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Aseguradora");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Cantidad");
			
			for(Iterator<String> i = informacoes.iterator() ; i.hasNext() ; )
			{
				String inf = i.next();
				
				String[] resultado = inf.split(";");
				
				String nome = resultado[0];
				String qtde = resultado[1];
				long aseguradoraId = Long.valueOf(resultado[2]);
				
				table.add(nome);
				table.setNextHAlign(Table.HALIGN_CENTER);
				Link link = new Link(qtde, new Action("relPlanoRGAseg"));
				link.getAction().add("asegId", aseguradoraId);
				link.getAction().add("inicio", this.dataInicio.getTime());
				link.getAction().add("fim", this.dataFim.getTime());
				link.getAction().add("sitSeguro", this.situacaoSeguro);
				link.getAction().add("especial", this.especial);
				link.getAction().add("modificado", this.modificado);
				table.add(link);
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
		if(especial)
			return new Label("Listado con Plan Especial");
		else if(modificado)
			return new Label("Listado con Plan Modificado");
		else
			return new Label("Listado con Plan RG.0001");
			
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}