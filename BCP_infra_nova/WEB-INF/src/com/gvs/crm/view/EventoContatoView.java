package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DataHoraInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoContato;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.Table;
import infra.view.View;

public class EventoContatoView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		EventoContato contato = (EventoContato) this.obterEvento();
		Evento superior = contato.obterSuperior();
		boolean incluir = contato.obterId() == 0;
		Date data = new Date();

		if (superior != null && incluir) {
			contato.atribuirOrigem(superior.obterOrigem());
			contato.atribuirPrioridade(superior.obterPrioridade());
		}

		Table table = new Table(2);
		table.addHeader("Superior:");
		table.add(new EventoSuperiorLabel(contato));
		if (!incluir) {
			table.addHeader("Creado por:");
			table.addData(new CriacaoLabel(contato));

			table.addHeader("Responsable:");
			table.addData(new EntidadePopup("responsavelId", "responsavelNome",
					contato.obterResponsavel(), "Usuario", contato
							.permiteAtualizar()));
		}
		table.addHeader("Nombre:");
		table
				.addData(new EntidadePopup(
						"origemId",
						"origemNome",
						contato.obterOrigem(),
						"corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",
						true, true));
		table.addHeader("Tipo Contacto:");
		table.addData(new EventoTipoSelect("tipo", contato, true));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", contato, true));
		table.addHeader("Descripción:");
		table.addData(new EventoDescricaoInput("descricao", contato, true));
		table.addHeader("Fecha/Hora:");
		table.addData(new DataHoraInput("data", "inicio", contato.obterDataPrevistaInicio(), true));

		if (incluir) 
		{
			Button incluirButton = new Button("Agregar", new Action(
					"incluirEventoContato"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());
			incluirButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			table.addHeader("Comentarios:");
			table.add(new ComentariosView(contato));

			if (contato.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", contato.obterId());
				pegarButton.getAction().add("origemMenuId",
						contato.obterOrigem().obterId());
				table.addFooter(pegarButton);
			}

			if (contato.permiteDevolver()) {
				Button devolverButton = new Button("Devolver", new Action(
						"devolverEvento"));
				devolverButton.getAction().add("id", contato.obterId());
				devolverButton.getAction().add("origemMenuId",
						contato.obterOrigem().obterId());
				table.addFooter(devolverButton);
			}

			if (contato.permiteAtualizar()) {
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarEventoContato"));
				atualizarButton.getAction().add("id", contato.obterId());
				atualizarButton.getAction().add("origemMenuId",
						contato.obterOrigem().obterId());
				table.addFooter(atualizarButton);
			}

			if (contato.permiteEncaminhar()) {
				Button encaminharButton = new Button("Remitir", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", contato.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.getAction().add("origemMenuId",
						contato.obterOrigem().obterId());
				table.addFooter(encaminharButton);
			}

			if (contato.permiteConcluir()) {
				Button concluirButton = new Button("Concluir", new Action(
						"concluirEvento"));
				concluirButton.getAction().add("id", contato.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.getAction().add("origemMenuId",
						contato.obterOrigem().obterId());
				table.addFooter(concluirButton);
			}

			if (contato.permiteExcluir()) {
				Button excluirButton = new Button("Eliminar", new Action(
						"excluirEvento"));
				excluirButton.getAction().add("id", contato.obterId());
				excluirButton.getAction().add("origemMenuId",
						contato.obterOrigem().obterId());
				excluirButton.getAction().setConfirmation(
						"Confirma exclusion ?");
				table.addFooter(excluirButton);
			}

			Button voltarButton = new Button("Volver", new Action(
					"visualizarPaginaInicial"));
			voltarButton.getAction().add("origemMenuId",
					contato.obterOrigem().obterId());
			table.addFooter(voltarButton);
		}
		return table;
	}
}