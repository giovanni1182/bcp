package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DateInput;
import com.gvs.crm.component.DuracaoInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.component.PrioridadeSelect;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Tarefa;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Label;
import infra.view.Table;
import infra.view.View;

public class TarefaView extends EventoAbstratoView {

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Tarefa tarefa = (Tarefa) this.obterEvento();
		Evento superior = tarefa.obterSuperior();
		boolean incluir = tarefa.obterId() == 0;
		if (superior != null && incluir) {
			tarefa.atribuirOrigem(superior.obterOrigem());
			tarefa.atribuirPrioridade(superior.obterPrioridade());
		}

		Table table = new Table(2);

		table.addHeader("Superior:");
		table.addData(new EventoSuperiorLabel(tarefa));
		if (!incluir) {
			table.addHeader("Criado por:");
			table.addData(new CriacaoLabel(tarefa));
		}
		table.addHeader("Origen:");
		table.add(new EntidadePopup("origemId", "origemNome", tarefa
				.obterOrigem(), tarefa.permiteAtualizar(), true));
		if (incluir) {
			table.addHeader("Responsable:");
			table.addData(new EntidadePopup("responsavelId", "responsavelNome",
					tarefa.obterResponsavel(), "Usuario", tarefa
							.permiteAtualizar()));
		}
		table.addHeader("Prioridade:");
		table.add(new PrioridadeSelect("prioridade", tarefa.obterPrioridade(),
				tarefa.permiteAtualizar()));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", tarefa, tarefa
				.permiteAtualizar()));
		table.addHeader("Descripción:");
		table.addData(new EventoDescricaoInput("descricao", tarefa, tarefa
				.permiteAtualizar()));
		table.addHeader("Previsão:");
		Table previsaoTable = new Table(6);
		previsaoTable.addHeader("Início:");
		previsaoTable.addData(new DateInput("dataPrevistaInicio", tarefa
				.obterDataPrevistaInicio(), tarefa.permiteAtualizar()));
		previsaoTable.addHeader(new Label("Conclusão:"));
		previsaoTable.addData(new DateInput("dataPrevistaConclusao", tarefa
				.obterDataPrevistaConclusao(), tarefa.permiteAtualizar()));
		previsaoTable.addHeader(new Label("Duração (horas):"));
		previsaoTable.addData(new DuracaoInput("duracaoPrevista", tarefa
				.obterDuracao(), tarefa.obterInferiores().isEmpty()
				&& tarefa.permiteAtualizar()));
		table.add(previsaoTable);

		if (incluir) {
			Button incluirButton = new Button("Incluir", new Action(
					"incluirTarefa"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());
			incluirButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);
		} else {
			if (!tarefa.obterInferiores().isEmpty()) {
				table.addHeader("Sub eventos:");
				table.add(new SubEventosView(tarefa));
			}
			table.addHeader("Comentarios:");
			table.add(new ComentariosView(tarefa));

			if (tarefa.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", tarefa.obterId());
				pegarButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(pegarButton);
			}

			if (tarefa.permiteDevolver()) {
				Button devolverButton = new Button("Devolver", new Action(
						"devolverEvento"));
				devolverButton.getAction().add("id", tarefa.obterId());
				devolverButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(devolverButton);
			}

			if (tarefa.permiteIncluirEventoInferior()) {
				Button novoSubEvento = new Button("Sub evento", new Action(
						"novoEvento"));
				novoSubEvento.getAction().add("passo", 3);
				novoSubEvento.getAction().add("superiorId", tarefa.obterId());
				novoSubEvento.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(novoSubEvento);
			}

			if (tarefa.permiteAtualizar()) {
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarTarefa"));
				atualizarButton.getAction().add("id", tarefa.obterId());
				atualizarButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(atualizarButton);
			}

			if (tarefa.permiteEncaminhar()) {
				Button encaminharButton = new Button("Remitir", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", tarefa.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(encaminharButton);
			}

			if (tarefa.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", tarefa.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(concluirButton);
			}

			if (tarefa.permiteExcluir()) {
				Button excluirButton = new Button("Eliminar", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", tarefa.obterId());
				excluirButton.getAction().setConfirmation(
						"Confirma exclusão da tarefa ?");
				table.addFooter(excluirButton);
			}

			if (superior == null) {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarPaginaInicial"));
				voltarButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(voltarButton);
			} else {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarEvento"));
				voltarButton.getAction().add("id", superior.obterId());
				voltarButton.getAction().add("origemMenuId",
						tarefa.obterOrigem().obterId());
				table.addFooter(voltarButton);
			}
		}

		return table;
	}
}