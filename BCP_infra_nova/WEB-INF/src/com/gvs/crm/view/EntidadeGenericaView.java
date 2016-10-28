package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.ResponsavelLabel;
import com.gvs.crm.component.SuperiorLabel;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Table;
import infra.view.View;

public class EntidadeGenericaView extends EntidadeAbstrataView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Entidade entidade = this.obterEntidade();
		boolean novo = entidade.obterId() == 0;

		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);

		Table table = new Table(2);

		table.addSubtitle("Datos Básicos");
		if (!novo) {
			table.addHeader("Creado en:");
			table.addData(new DateLabel(entidade.obterCriacao()));
		}
		table.addHeader("Superior:");
		//table.addData(new SuperiorPopup(entidade));

		if (usuarioAtual.obterId() == 1)
			table.addData(new SuperiorLabel(entidade));
		else if (entidade.obterSuperior().obterId() > 0)
			table.addData(new EntidadePopup("entidadeSuperiorId",
					"entidadeSuperiorNome", entidade.obterSuperior(), true));
		else
			table.addData(new SuperiorLabel(entidade));

		if (!novo) {
			table.addHeader("Responsable:");
			table.addData(new ResponsavelLabel(entidade));
		}

		table.addHeader("Nombre:");
		table.addData(new InputString("nome", entidade.obterNome(), 64));

		if (!entidade.obterClasse().equals("pessoafisica")
				&& !entidade.obterClasse().equals("departamento")) {
			table.addHeader("RUC:");
			table.addData(new InputString("ruc", entidade.obterRuc(), 25));
		}
		table.addHeader("Denominación:");
		table.addData(new InputString("apelido", entidade.obterApelido(), 32));
		table.addHeader("Sigla:");
		table.addData(new InputString("sigla", entidade.obterSigla(), 32));

		table.addHeader("Datos Adicionales:");
		table.addData(new EntidadeAtributosView(entidade));

		table.addSubtitle("Contactos:");
		table.addHeader("Contactos:");
		table.addData(new EntidadeContatosView(entidade));

		table.addSubtitle("Direcciones:");
		table.addHeader("Direcciones:");
		table.add(new EntidadeEnderecosView(entidade));

		if (novo) {
			Action incluirAction = new Action("incluirEntidade");
			incluirAction.add("superiorId", entidade.obterSuperior().obterId());
			incluirAction.add("classe", entidade.obterClasse());
			Button incluirButton = new Button("Agregar", incluirAction);
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novaEntidade");
			voltarAction.add("superiorId", entidade.obterSuperior().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} 
		else 
		{
			if (entidade.permiteAtualizar())
			{
				Action atualizarAction = new Action("atualizarEntidade");
				atualizarAction.add("id", entidade.obterId());
				Button atualizarButton = new Button("Actualizar",atualizarAction);
				table.addFooter(atualizarButton);
			}

			/*Action excluirAction = new Action("excluirEntidade");
			excluirAction.add("id", entidade.obterId());
			excluirAction.setConfirmation("Confirma exclusion ?");
			Button excluirButton = new Button("Eliminar", excluirAction);
			excluirButton.setEnabled(entidade.permiteExcluir());
			table.addFooter(excluirButton);*/
			
		}
		return table;
	}
}