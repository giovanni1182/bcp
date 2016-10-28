package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoPlano;
import com.gvs.crm.model.CodificacoesHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class CodificacaoPlanoView extends EventoAbstratoView 
{
	public static int DETALHE = 1;
	public static int COBERTURAS = 2;
	
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		CodificacaoPlano plano = (CodificacaoPlano) this.obterEvento();
		CRMModelManager mm = new CRMModelManager(user);
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		
		Table table = new Table(2);
		table.setWidth("50%");
		
		boolean novo = plano.obterId() == 0;
		
		int _pasta = Integer.parseInt(properties.getProperty("_pastaPlano","1"));
		
		if (novo || _pasta > 2)
			_pasta = DETALHE;
		
		Link detalheLink = new Link("Datos Basicos", new Action("visualizarEvento"));
		((Label) detalheLink.getCaption()).setBold(_pasta == DETALHE);
		detalheLink.getAction().add("id", plano.obterId());
		detalheLink.getAction().add("_pastaPlano", DETALHE);
		detalheLink.setEnabled(!novo);
		
		Link coberturaLink = new Link("Coberturas", new Action("visualizarEvento"));
		((Label) coberturaLink.getCaption()).setBold(_pasta == COBERTURAS);
		coberturaLink.getAction().add("id", plano.obterId());
		coberturaLink.getAction().add("_pastaPlano", COBERTURAS);
		coberturaLink.setEnabled(!novo);
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(detalheLink);
		block.add(new SeparadorLabel());
		block.add(coberturaLink);

		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		
		if(_pasta == DETALHE)
		{
			table.addSubtitle("");
			
			String codigo = plano.obterCodigo();
			String titulo = plano.obterTitulo();
			
			if(!novo)
			{
				table.addHeader("Creación:");
				table.add(new CriacaoLabel(plano));
			}
			else
			{
				codigo = codificacoesHome.obterMaiorCodigoPlano();
				titulo = "";
			}
			
			table.addHeader("Código:");
			table.add(new InputString("codigo",codigo,3));
			table.addHeader("Nombre:");
			table.add(new InputText("titulo",titulo,8,60));
			
			if(novo)
			{
				Button incluirButton = new Button("Agregar",new Action("incluirCodificacaoPlano"));
				table.addFooter(incluirButton);
			}
			else
			{
				Button novoPlanoButton = new Button("Nuevo Plan",new Action("novoEvento"));
				novoPlanoButton.getAction().add("classe", "codificacaoplano");
				table.addFooter(novoPlanoButton);
				
				Button atualizarButton = new Button("Actualizar",new Action("atualizarCodificacaoPlano"));
				atualizarButton.getAction().add("id", plano.obterId());
				table.addFooter(atualizarButton);
				
				Button excluirButton = new Button("Eliminar",new Action("excluirEvento"));
				excluirButton.getAction().add("id", plano.obterId());
				excluirButton.getAction().setConfirmation("Confirma Exclusión?? las coberturas serão eliminadas");
				table.addFooter(excluirButton);
			}
		}
		else
		{
			table.setNextColSpan(table.getColumns());
			table.add(new PlanoCoberturasView(plano));
		}
		
		return table;
	}
}
