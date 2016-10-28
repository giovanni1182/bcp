package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoCobertura;
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

public class CodificacaoCoberturaView extends EventoAbstratoView 
{

	public static int DETALHE = 1;
	public static int RISCO = 2;
	
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		CodificacaoCobertura cobertura = (CodificacaoCobertura) this.obterEvento();
		CodificacaoPlano plano = (CodificacaoPlano) cobertura.obterSuperior();
		CRMModelManager mm = new CRMModelManager(user);
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		
		Table table = new Table(2);
		table.setWidth("50%");
		
		boolean novo = cobertura.obterId() == 0;
		
		int _pasta = Integer.parseInt(properties.getProperty("_pastaCobertura","1"));
		
		if (novo || _pasta > 2)
			_pasta = DETALHE;
		
		Link detalheLink = new Link("Datos Basicos", new Action("visualizarEvento"));
		((Label) detalheLink.getCaption()).setBold(_pasta == DETALHE);
		detalheLink.getAction().add("id", cobertura.obterId());
		detalheLink.getAction().add("_pastaCobertura", DETALHE);
		detalheLink.setEnabled(!novo);
		
		Link coberturaLink = new Link("Riesgos", new Action("visualizarEvento"));
		((Label) coberturaLink.getCaption()).setBold(_pasta == RISCO);
		coberturaLink.getAction().add("id", cobertura.obterId());
		coberturaLink.getAction().add("_pastaCobertura", RISCO);
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
			
			String codigo = cobertura.obterCodigo();
			String titulo = cobertura.obterTitulo();
			
			table.addHeader("Superior:");
			table.add(new EventoSuperiorLabel(cobertura));
			
			if(!novo)
			{
				table.addHeader("Creación:");
				table.add(new CriacaoLabel(cobertura));
			}
			else
			{
				codigo = codificacoesHome.obterMaiorCodigoCobertura(plano);
				titulo = "";
			}
			
			table.addHeader("Código:");
			table.add(new InputString("codigo",codigo,2));
			table.addHeader("Nombre:");
			table.add(new InputText("titulo",titulo,8,60));
			
			if(novo)
			{
				Button incluirButton = new Button("Agregar",new Action("incluirCodificacaoCobertura"));
				incluirButton.getAction().add("superiorId", plano.obterId());
				table.addFooter(incluirButton);
			}
			else
			{
				Button novoPlanoButton = new Button("Nueva Cobertura",new Action("novoEvento"));
				novoPlanoButton.getAction().add("classe", "codificacaocobertura");
				novoPlanoButton.getAction().add("superiorId", plano.obterId());
				table.addFooter(novoPlanoButton);
				
				Button atualizarButton = new Button("Actualizar",new Action("atualizarCodificacaoCobertura"));
				atualizarButton.getAction().add("id", cobertura.obterId());
				table.addFooter(atualizarButton);
				
				Button excluirButton = new Button("Eliminar",new Action("excluirEvento"));
				excluirButton.getAction().add("id", cobertura.obterId());
				excluirButton.getAction().setConfirmation("Confirma Exclusión?? los Riesgos serão eliminados");
				table.addFooter(excluirButton);
			}
		}
		else
		{
			table.setNextColSpan(table.getColumns());
			table.add(new CoberturaRiscosView(cobertura));
		}
		
		return table;
	}

}
