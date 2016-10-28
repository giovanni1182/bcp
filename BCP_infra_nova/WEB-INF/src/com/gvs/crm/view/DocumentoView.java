package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DiferenciadorSelect;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.RamoDocumentoSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Documento;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class DocumentoView extends EventoAbstratoView {
	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	private int _pasta;

	public View execute(User arg0, Locale arg1, Properties properties)
			throws Exception {

		Documento documento = (Documento) this.obterEvento();

		boolean incluir = documento.obterId() == 0;

		_pasta = Integer.parseInt(properties.getProperty("_pasta", "1"));

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		Table table = new Table(2);

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", documento.obterId());
		basicosLink.getAction().add("_pasta", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", documento.obterId());
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
				table.add(new CriacaoLabel(documento));
			}
			table.addHeader("Superior:");
			table.add(new EventoSuperiorLabel(documento));

			table.addHeader("Fase:");
			if (incluir)
				table.addHeader("PENDIENTE");
			else
				table
						.addHeader(documento.obterFase().obterNome()
								.toUpperCase());

			table.addHeader("De Quién:");
			table
					.add(new EntidadePopup(
							"origemId",
							"origemNome",
							documento.obterOrigem(),
							"corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",
							true));

			/*
			 * table.addHeader("Para Quién:"); table.add(new
			 * EntidadePopup("destinoId", "destinoNome",
			 * documento.obterDestino(),
			 * "corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",documento.permiteAtualizar()));
			 */

			table.addHeader("Responsable:");
			table.add(new EntidadePopup("responsavelId", "responsavelNome",
					documento.obterResponsavel(), "Usuario", true));

			table.addHeader("Fecha de la Agenda:");
			table.add(new InputDate("dataAgenda", documento.obterDataAgenda()));

			table.addHeader("Tipo de documento entregado:");

			Block ramoBlock = new Block(Block.HORIZONTAL);
			ramoBlock.add(new RamoDocumentoSelect("ramo", documento, documento
					.obterRamo()));
			ramoBlock.add(new Space(4));
			ramoBlock.add(new InputString("novoRamo", null, 40));

			table.add(ramoBlock);

			table.addHeader("Diferenciador:");
			table.add(new DiferenciadorSelect("diferenciador", documento
					.obterDiferenciador()));

			table.addHeader("Titulo del Documento:");
			table.add(new InputString("titulo", documento.obterTitulo(), 50));

			table.addHeader("Asunto:");
			table.add(new EventoDescricaoInput("descricao", documento,
					true));

			table.addHeader("Comentários");
			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(documento));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("Sub-eventos:");
			table.setNextColSpan(table.getColumns());
			table.add(new SubEventosView(documento));

		} else if (_pasta == DOCUMENTOS) {
			table.add("");
			table.setNextColSpan(table.getColumns());
			table.add(new AnexosView(documento));
		}

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirDocumento"));
			if (documento.obterSuperior() != null)
				incluirButton.getAction().add("superiorId",
						documento.obterSuperior().obterId());
			table.addFooter(incluirButton);
		} else {
			if (documento.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", documento.obterId());

				table.addFooter(pegarButton);
			}

			Button novaAgendaButton = new Button("Sub-Evento", new Action(
					"novoEvento"));
			novaAgendaButton.getAction().add("passo", 3);
			novaAgendaButton.getAction().add("superiorId", documento.obterId());
			novaAgendaButton.setEnabled(documento.permiteAtualizar());
			table.addFooter(novaAgendaButton);

			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarDocumento"));
			atualizarButton.getAction().add("id", documento.obterId());
			atualizarButton.setEnabled(documento.permiteAtualizar());

			table.addFooter(atualizarButton);

			Button comentariosButton = new Button("Comentarios", new Action(
					"comentarEvento"));
			comentariosButton.getAction().add("id", documento.obterId());
			comentariosButton.getAction().add("view", true);
			comentariosButton.setEnabled(documento.permiteAtualizar());

			table.addFooter(comentariosButton);

			Button concluirButton = new Button("Concluir", new Action(
					"concluirEvento"));
			concluirButton.getAction().add("id", documento.obterId());
			concluirButton.getAction().add("view", true);
			concluirButton.setEnabled(documento.permiteConcluir());

			table.addFooter(concluirButton);

			Button eliminarButton = new Button("Eliminar", new Action(
					"excluirEvento"));
			eliminarButton.getAction().add("id", documento.obterId());
			//eliminarButton.setEnabled(documento.permiteExcluir());

			table.addFooter(eliminarButton);

		}

		if (documento.obterSuperior() == null) {
			Button voltarButton = new Button("Volver", new Action("novoEvento"));
			voltarButton.getAction().add("passo", 2);
			table.addFooter(voltarButton);
		} else {
			Button voltarButton = new Button("Volver", new Action(
					"visualizarEvento"));
			voltarButton.getAction().add("id",
					documento.obterSuperior().obterId());
			table.addFooter(voltarButton);
		}

		return table;
	}
}