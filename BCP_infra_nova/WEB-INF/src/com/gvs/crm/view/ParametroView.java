package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Parametro;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Table;
import infra.view.View;

public class ParametroView extends EntidadeAbstrataView 
{
	public View execute(User user, Locale locale, Properties properties) throws Exception 
	{
		Parametro parametro = (Parametro) this.obterEntidade();
		
		CRMModelManager mm = new CRMModelManager(user);
		
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		
		Entidade icf = home.obterEntidadePorApelido("intendenteicf");
		
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user); 
		
		boolean novo = parametro.obterId() == 0;

		Table table = new Table(2);

		table.addSubtitle("Datos Básicos");

		if (!novo) {
			table.addHeader("Creado en:");
			table.addData(new DateLabel(parametro.obterCriacao()));
		}

		table.addHeader("Superior");
		table.addData(new SuperiorLabel(parametro));

		if (!novo) {
			table.addHeader("Responsable:");
			table.addData(new ResponsavelLabel(parametro));
		}

		table.addHeader("Nombre:");
		table.addData(new InputString("nome", parametro.obterNome(), 40));
		table.addHeader("Apodo:");
		table.addData(new InputString("apelido", parametro.obterApelido(), 40));

		table.addHeader("Feriados:");

		Button feriadoButton = new Button("[Feriado]",new Action("novoFeriado"));
		feriadoButton.getAction().add("entidadeId", parametro.obterId());
		feriadoButton.getAction().add("origemMenuId", parametro.obterId());
		feriadoButton.setEnabled(parametro.permiteAtualizar());
		table.setNextColSpan(table.getColumns());
		table.add(feriadoButton);

		table.addHeader("Consistência:");

		Button novoButton = new Button("[Nueva Consistencia]", new Action("novaConsistencia"));
		novoButton.getAction().add("entidadeId", parametro.obterId());
		novoButton.getAction().add("origemMenuId", parametro.obterId());
		novoButton.setEnabled(parametro.permiteAtualizar());
		table.setNextColSpan(table.getColumns());
		table.add(novoButton);

		table.addHeader("Controle Documento:");

		Button documentoButton = new Button("[Controle Documento]", new Action("novoControleDocumento"));
		documentoButton.getAction().add("entidadeId", parametro.obterId());
		documentoButton.getAction().add("origemMenuId", parametro.obterId());
		documentoButton.setEnabled(parametro.permiteAtualizar() || usuarioAtual.obterSuperiores().contains(icf));
		table.setNextColSpan(table.getColumns());
		table.add(documentoButton);

		table.addHeader("Avaliacion Indicadores:");

		Button avaliacaoButton = new Button("[Avaliacion Indicadores]",	new Action("novoIndicador"));
		avaliacaoButton.getAction().add("entidadeId", parametro.obterId());
		avaliacaoButton.getAction().add("origemMenuId", parametro.obterId());
		avaliacaoButton.setEnabled(parametro.permiteAtualizar() || usuarioAtual.obterSuperiores().contains(icf));
		table.setNextColSpan(table.getColumns());
		table.add(avaliacaoButton);

		/*
		 * table.addHeader("Configurações:");
		 * 
		 * Button batchButton = new Button("[Configurações]", new
		 * Action("novaConfiguracao"));
		 * batchButton.getAction().add("entidadeId", parametro.obterId());
		 * batchButton.getAction().add("origemMenuId", parametro.obterId());
		 * batchButton.setEnabled(parametro.permiteAtualizar());
		 * table.setNextColSpan(table.getColumns()); table.add(batchButton);
		 */

		table.addHeader("Datos Adicionales:");

		table.addData(new EntidadeAtributosView(parametro));

		if (novo) {
			Action incluirAction = new Action("incluirEntidade");
			incluirAction
					.add("superiorId", parametro.obterSuperior().obterId());
			incluirAction.add("classe", parametro.obterClasse());
			incluirAction.add("origemMenuId", this.obterOrigemMenu().obterId());
			Button incluirButton = new Button("Incluir", incluirAction);
			table.addFooter(incluirButton);

			Action cancelarAction = new Action("visualizarPaginaInicial");
			table.addFooter(new Button("Cancelar", cancelarAction));

			Action voltarAction = new Action("novaEntidade");
			voltarAction.add("superiorId", parametro.obterSuperior().obterId());
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			Action atualizarAction = new Action("atualizarEntidade");
			atualizarAction.add("id", parametro.obterId());
			Button atualizarButton = new Button("Actualizar", atualizarAction);
			atualizarButton.setEnabled(parametro.permiteAtualizar());
			table.addFooter(atualizarButton);

			/*Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", parametro.obterId());
			excluirAction.setConfirmation("Confirma exclusão da entidade ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(parametro.permiteExcluir());
			table.addFooter(excluirButton);*/
		}

		return table;
	}

}