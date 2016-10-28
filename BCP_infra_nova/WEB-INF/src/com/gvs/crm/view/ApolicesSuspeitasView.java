package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SuspeitaSelect;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ApolicesSuspeitasView extends PortalView {

	private Collection apolices;

	private long valor;

	private boolean lista;

	public ApolicesSuspeitasView(Collection apolices, long valor, boolean lista)throws Exception 
	{
		this.apolices = apolices;
		this.valor = valor;
		this.lista = lista;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table2 = new Table(7);
		table2.setWidth("100%");
		table2.addSubtitle("");
		table2.addStyle(Table.STYLE_ALTERNATE);
		
		Table table = new Table(1);
		table.setWidth("100%");
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(new SuspeitaSelect("valor", this.valor));
		block.add(new Space(2));
		
		Button button = new Button("Listar",new Action("visualizarApolicesSuspeitas"));
		button.getAction().add("lista", true);
		block.add(button);
		block.add(new Space(5));
		
		button = new Button("Generar Excel",new Action("visualizarApolicesSuspeitas"));
		button.getAction().add("lista", true);
		button.getAction().add("excel", true);
		block.add(button);
		
		table.add(block);

		if (this.lista) 
		{
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Aseguradora");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Prima M.E.");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Póliza");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Asegurado");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Plan");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Sección");
			table2.setNextHAlign(Table.HALIGN_CENTER);
			table2.addHeader("Vigencia");
			
			for (Iterator i = this.apolices.iterator(); i.hasNext();) 
			{
				Apolice apolice = (Apolice) i.next();

				table2.add(apolice.obterOrigem().obterNome());
				table2.setNextHAlign(Table.HALIGN_RIGHT);
				table2.add(new Label(apolice.obterPrimaMe(), "#,##0.00"));

				Link link2 = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
				link2.getAction().add("id", apolice.obterId());

				table2.add(link2);

				table2.add(apolice.obterNomeAsegurado());
				table2.add(apolice.obterPlano().obterPlano());
				table2.add(apolice.obterSecao().obterNome());

				String data1 = new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio());
				String data2 = new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao());

				table2.add(data1 + " - " + data2);
			}
			
			table.add(table2);
		}
		
		this.apolices.clear();

		return table;
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception {
		return new Label("Lista de Operaciones Sospechosas");
	}

	public Entidade getOrigemMenu() throws Exception {
		return null;
	}
}