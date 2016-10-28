package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.component.TipoEnvioSelect;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ManutencaoSite;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ManutencaoSiteView extends EventoAbstratoView {

	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	private int _pasta;

	private boolean incluir;

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		ManutencaoSite manutencao = (ManutencaoSite) this.obterEvento();

		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorUser(user);

		_pasta = Integer.parseInt(properties.getProperty("_pasta", "1"));

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		boolean incluir = manutencao.obterId() == 0;

		Table table = new Table(2);

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", manutencao.obterId());
		basicosLink.getAction().add("_pasta", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", manutencao.obterId());
		documentosLink.getAction().add("_pasta", DOCUMENTOS);
		documentosLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		block.add(new SeparadorLabel());
		block.add(documentosLink);

		table.add(block);

		if (_pasta == DETALLES) {
			table.add("");
			table.addSubtitle("Detalles");

			if (!incluir) {
				table.addHeader("Creado por:");
				table.add(manutencao.obterCriador().obterNome());

				table.addHeader("Responsable:");
				table.add(manutencao.obterResponsavel().obterNome());
			}

			table.addHeader("Solicitante:");
			table.add(new EntidadePopup("origemId", "origemNome", manutencao
					.obterOrigem(), "Usuario", true));

			table.addHeader("Tipo de Envío:");
			if (incluir)
				table.add(new TipoEnvioSelect("tipo", manutencao.obterTipo()));
			else
				table.add(manutencao.obterTipo());

			table.addHeader("Título:");
			if (incluir)
				table.add(new InputString("titulo", manutencao.obterTitulo(),
						50));
			else
				table.add(manutencao.obterTitulo());

			table.addHeader("Descripción:");
			table.add(new EventoDescricaoInput("descricao", manutencao,true));

			table.addHeader("Comentários");
			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(manutencao));
		}

		else if (_pasta == DOCUMENTOS) {
			table.add("");
			table.setNextColSpan(table.getColumns());
			table.add(new AnexosView(manutencao));
		}

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirManutencaoSite"));
			table.addFooter(incluirButton);

			Button cancelarButton = new Button("Volver", new Action(
					"novoEvento"));
			cancelarButton.getAction().add("passo", 2);
			table.addFooter(cancelarButton);
		} else {
			if (manutencao.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", manutencao.obterId());

				table.addFooter(pegarButton);
			}

			if (usuario.obterSuperior() == null
					|| usuario.obterSuperior() instanceof Raiz) {
				Button enviarButton = new Button("Enviar", new Action(
						"enviarManutencaoSite"));
				enviarButton.getAction().add("id", manutencao.obterId());
				enviarButton.setEnabled(manutencao.permiteAtualizar());
				table.addFooter(enviarButton);
			} else {
				if (!usuario.obterSuperior().obterApelido().equals(
						"informatica")) {
					Button enviarButton = new Button("Enviar", new Action(
							"enviarManutencaoSite"));
					enviarButton.getAction().add("id", manutencao.obterId());
					enviarButton.setEnabled(manutencao.permiteAtualizar());
					table.addFooter(enviarButton);
				}
			}

			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarManutencaoSite"));
			atualizarButton.getAction().add("id", manutencao.obterId());
			atualizarButton.setEnabled(manutencao.permiteAtualizar());

			table.addFooter(atualizarButton);

			Button comentariosButton = new Button("Comentarios", new Action(
					"comentarEvento"));
			comentariosButton.getAction().add("id", manutencao.obterId());
			comentariosButton.getAction().add("view", true);
			comentariosButton.setEnabled(manutencao.permiteAtualizar());

			table.addFooter(comentariosButton);

			Button concluirButton = new Button("Concluir", new Action(
					"concluirEvento"));
			concluirButton.getAction().add("id", manutencao.obterId());
			concluirButton.getAction().add("view", true);
			concluirButton.setEnabled(manutencao.permiteConcluir());

			table.addFooter(concluirButton);

			Button eliminarButton = new Button("Eliminar", new Action(
					"excluirEvento"));
			eliminarButton.getAction().add("id", manutencao.obterId());
			eliminarButton.setEnabled(manutencao.permiteExcluir());

			table.addFooter(eliminarButton);
		}

		return table;
	}
}