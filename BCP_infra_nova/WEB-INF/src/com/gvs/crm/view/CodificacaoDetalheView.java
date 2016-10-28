package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CodificacaoDetalhe;
import com.gvs.crm.model.CodificacaoRisco;
import com.gvs.crm.model.CodificacoesHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Table;
import infra.view.View;

public class CodificacaoDetalheView extends EventoAbstratoView 
{
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		CodificacaoDetalhe detalhe = (CodificacaoDetalhe) this.obterEvento();
		CodificacaoRisco risco = (CodificacaoRisco) detalhe.obterSuperior();
		CRMModelManager mm = new CRMModelManager(user);
		CodificacoesHome codificacoesHome = (CodificacoesHome) mm.getHome("CodificacoesHome");
		
		Table table = new Table(2);
		table.setWidth("50%");
		
		boolean novo = detalhe.obterId() == 0;
		
		String codigo = detalhe.obterCodigo();
		String titulo = detalhe.obterTitulo();
			
		table.addHeader("Superior:");
		table.add(new EventoSuperiorLabel(detalhe));
			
		if(!novo)
		{
			table.addHeader("Creación:");
			table.add(new CriacaoLabel(detalhe));
		}
		else
		{
			codigo = codificacoesHome.obterMaiorCodigoDetalhe(risco);
			titulo = "";
		}
		
		table.addHeader("Código:");
		table.add(new InputString("codigo",codigo,3));
		table.addHeader("Nombre:");
		table.add(new InputText("titulo",titulo,8,60));
		
		if(novo)
		{
			Button incluirButton = new Button("Agregar",new Action("incluirCodificacaoDetalhe"));
			incluirButton.getAction().add("superiorId", risco.obterId());
			table.addFooter(incluirButton);
		}
		else
		{
			Button novoPlanoButton = new Button("Nuevo Detalle",new Action("novoEvento"));
			novoPlanoButton.getAction().add("classe", "codificacaodetalhe");
			novoPlanoButton.getAction().add("superiorId", risco.obterId());
			table.addFooter(novoPlanoButton);
			
			Button atualizarButton = new Button("Actualizar",new Action("atualizarCodificacaoDetalhe"));
			atualizarButton.getAction().add("id", detalhe.obterId());
			table.addFooter(atualizarButton);
			
			Button excluirButton = new Button("Eliminar",new Action("excluirEvento"));
			excluirButton.getAction().add("id", detalhe.obterId());
			excluirButton.getAction().setConfirmation("Confirma Exclusión??");
			table.addFooter(excluirButton);
		}
		
		return table;
	}

}
