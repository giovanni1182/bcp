package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoPopup;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Reclamacao;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ReclamacaoView extends EventoAbstratoView {
	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	private int _pasta;

	private boolean incluir;

	public View execute(User arg0, Locale arg1, Properties properties)
			throws Exception {

		Reclamacao reclamacao = (Reclamacao) this.obterEvento();

		Table table = new Table(2);

		_pasta = Integer.parseInt(properties.getProperty("_pastaReclamacao",
				"1"));

		this.incluir = reclamacao.obterId() == 0;

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", reclamacao.obterId());
		basicosLink.getAction().add("_pastaReclamacao", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", reclamacao.obterId());
		documentosLink.getAction().add("_pastaReclamacao", DOCUMENTOS);
		documentosLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		block.add(new SeparadorLabel());
		block.add(documentosLink);
		table.setNextColSpan(table.getColumns());
		table.add(block);

		incluir = reclamacao.obterId() == 0;

		if (_pasta == DETALLES) {
			table.addSubtitle("");
			if (!incluir) {
				table.addHeader("Creado por");
				table.add(new CriacaoLabel(reclamacao));
			}

			table.addHeader("Superior");
			table.add(new EventoSuperiorLabel(reclamacao));

			table.addHeader("Fase:");

			if (incluir)
				table.add("Pendiente");
			else
				table.addHeader(reclamacao.obterFase().obterNome()
						.toUpperCase());

			table.addHeader("Solicitante:");
			table
					.add(new EntidadePopup(
							"origemId",
							"origemNome",
							reclamacao.obterOrigem(),
							"corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",
							true));

			if (incluir) {
				Table table3 = new Table(2);

				table3.addHeader("Nombre:");
				table3.add(new InputString("paraQuem", null, 50));
				table3.addHeader("Teléfono:");
				table3.add(new InputString("telefone", null, 20));
				table3.addHeader("Email:");
				table3.add(new InputString("email", null, 30));

				table.add("");
				table.add(table3);
			}

			table.addHeader("Objeto del Reclamo:");
			table
					.add(new EntidadePopup(
							"destinoId",
							"destinoNome",
							reclamacao.obterDestino(),
							"corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",
							true));

			table.addHeader("Responsable:");
			table.add(new EntidadePopup("responsavelId", "responsavelNome",
					reclamacao.obterResponsavel(), "Usuario", true));

			table.addHeader("Tipo de Reclamo:");
			table.add(new EventoTipoSelect("tipo", reclamacao, true));

			table.addHeader("Titulo del Documento:");
			table.add(new InputString("titulo", reclamacao.obterTitulo(), 50));

			table.addHeader("Número de la Póliza:");
			table.add(new EventoPopup("apoliceId", "apoliceTitulo", reclamacao
					.obterApolice(), "Apolice", true));

			table.addHeader("Asunto:");
			table.add(new EventoDescricaoInput("descricao", reclamacao,
					true));

			table.addHeader("Comentários");
			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(reclamacao));

			table.addHeader("Sub Eventos");
			table.setNextColSpan(table.getColumns());
			table.add(new SubEventosView(reclamacao));

			if (!incluir) {
				Table table2 = new Table(2);

				table2.setNextColSpan(table2.getColumns());
				table2.add(new AnexosView(reclamacao));
			}

			if (incluir) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirReclamacao"));
				table.addFooter(incluirButton);

				Button cancelarButton = new Button("Volver", new Action(
						"novoEvento"));
				cancelarButton.getAction().add("passo", 2);
				table.addFooter(cancelarButton);
			} else {
				if (reclamacao.permitePegar()) {
					Button pegarButton = new Button("Pegar", new Action(
							"pegarEvento"));
					pegarButton.getAction().add("id", reclamacao.obterId());
					table.addFooter(pegarButton);
				}

				Button subEventoButton = new Button("Sub Evento", new Action(
						"novoEvento"));
				subEventoButton.getAction().add("superiorId",
						reclamacao.obterId());
				subEventoButton.getAction().add("passo", 3);
				subEventoButton.setEnabled(reclamacao.permiteAtualizar());
				table.addFooter(subEventoButton);

				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarReclamacao"));
				atualizarButton.getAction().add("id", reclamacao.obterId());
				atualizarButton.setEnabled(reclamacao.permiteAtualizar());
				table.addFooter(atualizarButton);

//				Button comentarioButton = new Button("Comentarios", new Action(
//						"comentarEvento"));
//				comentarioButton.getAction().add("id", reclamacao.obterId());
//				comentarioButton.getAction().add("view", true);
//				comentarioButton.setEnabled(reclamacao.permiteAtualizar());
//
//				table.addFooter(comentarioButton);

				/*
				 * if(!reclamacao.obterFase().obterCodigo().equals(Reclamacao.EVENTO_PENDENTE) &&
				 * !reclamacao.obterFase().obterCodigo().equals(Reclamacao.EVENTO_CONCLUIDO)) {
				 * Button faseAnteriorButton = new Button(" < < < Fase
				 * Anterior", new Action("faseAnterior"));
				 * faseAnteriorButton.getAction().add("view", true);
				 * faseAnteriorButton.getAction().add("id",
				 * reclamacao.obterId());
				 * faseAnteriorButton.setEnabled(reclamacao.permiteAtualizar());
				 * table.addFooter(faseAnteriorButton); }
				 * 
				 * if(!reclamacao.obterFase().obterCodigo().equals(Reclamacao.EVENTO_CONCLUIDO) &&
				 * !reclamacao.obterFase().obterCodigo().equals("superintendente")) {
				 * Button proximaFaseButton = new Button("Próxima Fase >>>", new
				 * Action("proximaFase"));
				 * proximaFaseButton.getAction().add("view", true);
				 * proximaFaseButton.getAction().add("id",
				 * reclamacao.obterId());
				 * proximaFaseButton.setEnabled(reclamacao.permiteAtualizar());
				 * table.addFooter(proximaFaseButton); }
				 */

				/*
				 * if(!reclamacao.obterFase().obterCodigo().equals("superintendente") ||
				 * reclamacao.obterResponsavel().equals(reclamacao.obterCriador())) {
				 */
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", reclamacao.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.setEnabled(reclamacao.permiteConcluir());
				table.addFooter(concluirButton);
				//}

				Button encaminharButton = new Button("Encaminar", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", reclamacao.obterId());
				encaminharButton.getAction().add("view", true);
				//encaminharButton.setEnabled(reclamacao.permiteEncaminhar());
				table.addFooter(encaminharButton);

				Button excluirButton = new Button("Eliminar", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", reclamacao.obterId());
				//excluirButton.setEnabled(reclamacao.permiteExcluir());
				table.addFooter(excluirButton);
			}
		} else if (_pasta == DOCUMENTOS) {
			table.addSubtitle("Anexos");
			table.setNextColSpan(table.getColumns());
			table.add(new AnexosView(reclamacao));
		}
		return table;
	}
}