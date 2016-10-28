package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoCobertura;
import com.gvs.crm.model.CodificacaoRisco;
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

public class CodificacaoRiscoView extends EventoAbstratoView 
{
	public static int DADOS_BASICOS = 1;
	public static int DETALHES = 2;
	
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		CodificacaoRisco risco = (CodificacaoRisco) this.obterEvento();
		CodificacaoCobertura cobertura = (CodificacaoCobertura) risco.obterSuperior();
		CRMModelManager mm = new CRMModelManager(user);
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		
		Table table = new Table(2);
		table.setWidth("50%");
		
		boolean novo = risco.obterId() == 0;
		
		int _pasta = Integer.parseInt(properties.getProperty("_pastaRisco","1"));
		
		if (novo || _pasta > 2)
			_pasta = DADOS_BASICOS;
		
		Link dadosBasicosLink = new Link("Datos Basicos", new Action("visualizarEvento"));
		((Label) dadosBasicosLink.getCaption()).setBold(_pasta == DADOS_BASICOS);
		dadosBasicosLink.getAction().add("id", risco.obterId());
		dadosBasicosLink.getAction().add("_pastaRisco", DADOS_BASICOS);
		dadosBasicosLink.setEnabled(!novo);
		
		Link detalhesLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) detalhesLink.getCaption()).setBold(_pasta == DETALHES);
		detalhesLink.getAction().add("id", risco.obterId());
		detalhesLink.getAction().add("_pastaRisco", DETALHES);
		detalhesLink.setEnabled(!novo);
		
		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosBasicosLink);
		block.add(new SeparadorLabel());
		block.add(detalhesLink);

		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		
		if(_pasta == DADOS_BASICOS)
		{
			table.addSubtitle("");
			
			String codigo = risco.obterCodigo();
			String titulo = risco.obterTitulo();
			
			table.addHeader("Superior:");
			table.add(new EventoSuperiorLabel(risco));
			
			if(!novo)
			{
				table.addHeader("Creación:");
				table.add(new CriacaoLabel(risco));
			}
			else
			{
				codigo = codificacoesHome.obterMaiorCodigoRisco(cobertura);
				titulo = "";
			}
			
			table.addHeader("Código:");
			table.add(new InputString("codigo",codigo,2));
			table.addHeader("Nombre:");
			table.add(new InputText("titulo",titulo,8,60));
			
			if(novo)
			{
				Button incluirButton = new Button("Agregar",new Action("incluirCodificacaoRisco"));
				incluirButton.getAction().add("superiorId", cobertura.obterId());
				table.addFooter(incluirButton);
			}
			else
			{
				Button novoPlanoButton = new Button("Nuevo Riesgo",new Action("novoEvento"));
				novoPlanoButton.getAction().add("classe", "codificacaorisco");
				novoPlanoButton.getAction().add("superiorId", cobertura.obterId());
				table.addFooter(novoPlanoButton);
				
				Button atualizarButton = new Button("Actualizar",new Action("atualizarCodificacaoRisco"));
				atualizarButton.getAction().add("id", risco.obterId());
				table.addFooter(atualizarButton);
				
				Button excluirButton = new Button("Eliminar",new Action("excluirEvento"));
				excluirButton.getAction().add("id", risco.obterId());
				excluirButton.getAction().setConfirmation("Confirma Exclusión?? los Detalles serão eliminados");
				table.addFooter(excluirButton);
			}
		}
		else
		{
			table.setNextColSpan(table.getColumns());
			table.add(new RiscoDetalhesView(risco));
		}
		
		return table;
	}
}