package com.gvs.crm.view;

import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CodigoInstrumento;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputFile;
import infra.view.InputInteger;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class CodigosInstrumentoView extends PortalView 
{
	private Collection<CodigoInstrumento> codigos;
	private int codigo;
	private String descricao;
	
	public CodigosInstrumentoView(Collection<CodigoInstrumento> codigos, int codigo, String descricao)
	{
		this.codigos = codigos;
		this.codigo = codigo;
		this.descricao = descricao;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);
		table.addSubtitle("Archivo de Importación");
		table.addHeader("Archivo:");
		InputFile inputFile = new InputFile("arquivo", null);
		inputFile.setAccept(".txt");
		table.add(inputFile);
		
		Button incluirButton = new Button("Upload", new Action("relCodigosInstrumento"));
		incluirButton.getAction().add("upload", true);
		
		table.addFooter(incluirButton);
		
		mainTable.add(table);
		
		table = new Table(2);
		table.addSubtitle("Nuevo Código");
		table.addHeader("Código:");
		table.add(new InputInteger("codigo", this.codigo, 5));
		table.addHeader("Descripción:");
		table.add(new InputText("descricao", this.descricao, 5, 70));
		
		incluirButton = new Button("Incluir", new Action("relCodigosInstrumento"));
		incluirButton.getAction().add("incluir", true);
		
		table.addFooter(incluirButton);
		
		mainTable.add(table);
		
		table = new Table(3);
		table.setWidth("40%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addSubtitle(this.codigos.size() + " Código(s)");
		
		if(this.codigos.size() > 0)
		{
			table.add("");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Código");
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.addHeader("Descripción");
			
			String codigoStr;
			int codigo;
			Link link;
			InputText inputText;
			
			for(CodigoInstrumento c : this.codigos)
			{
				codigoStr = "";
				codigo = c.obterCodigo();
				
				if(c.permiteExcluir())
				{
					link = new Link(new Image("delete.gif"), new Action("relCodigosInstrumento"));
					link.getAction().add("id", c.obterId());
					link.getAction().add("excluir", true);
					
					table.setNextHAlign(Table.HALIGN_CENTER);
					table.add(link);
				}
				else
					table.add("");
				
				if(new Integer(codigo).toString().length() == 1)
					codigoStr = "00"+codigo;
				else if(new Integer(codigo).toString().length() == 2)
					codigoStr = "0"+codigo;
				else
					codigoStr = codigo+"";
				
				table.setNextHAlign(Table.HALIGN_CENTER);
				table.add(codigoStr);
				
				inputText = new InputText("descricaoReal_"+c.obterId(), c.obterDescricao(), 2,70);
				table.add(inputText);
			}
			
			Button atualizarButton = new Button("Actualizar", new Action("relCodigosInstrumento"));
			atualizarButton.getAction().add("atualizar", true);
			
			table.addFooter(atualizarButton);
			
		}
		
		mainTable.add(table);
		
		
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
		return new Label("Tabla de Instrumentos");
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}