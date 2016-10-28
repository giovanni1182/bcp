package com.gvs.crm.view;

import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.DateInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDouble;
import infra.view.Table;
import infra.view.View;

public class MovimentacaoFinanceiraContaView extends EventoAbstratoView {

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		MovimentacaoFinanceiraConta movimentacao = (MovimentacaoFinanceiraConta) this
				.obterEvento();

		Evento superior = movimentacao.obterSuperior();
		boolean incluir = movimentacao.obterId() == 0;

		if (superior != null && incluir) {
			movimentacao.atribuirOrigem(superior.obterOrigem());
			movimentacao.atribuirPrioridade(superior.obterPrioridade());
		}
		Table table = new Table(2);

		table.addHeader("Aseguradora");
		table.addData(new EntidadePopup("seguradoraId", "seguradoraNome",
				movimentacao.obterOrigem(), "aseguradora", movimentacao
						.permiteAtualizar()));

		table.addHeader("Responsable");
		table.addData(new EntidadePopup("responsavelId", "responsavelNome",
				movimentacao.obterResponsavel(), "Usuario", movimentacao
						.permiteAtualizar()));

		table.addHeader("Fecha Prevista:");

		if (incluir)
			table.addData(new DateInput("data_prevista", new Date(),
					movimentacao.permiteAtualizar()));
		else
			table.addData(new DateInput("data_prevista", movimentacao
					.obterDataPrevista(), movimentacao.permiteAtualizar()));

		table.addHeader("Cuenta:");
		table.addData(new EntidadePopup("contaId", "conta", movimentacao
				.obterConta(), "Conta", movimentacao.permiteAtualizar()));

		table.addHeader("Saldo Anterior:");
		table.addData(new InputDouble("saldo_anterior", movimentacao
				.obterSaldoAnterior().doubleValue(), 30));

		table.addHeader("Débito:");
		table
				.addData(new InputDouble("debito", movimentacao.obterDebito().doubleValue(),
						30));

		table.addHeader("Crédito:");
		table.addData(new InputDouble("credito", movimentacao.obterCredito().doubleValue(),
				30));

		table.addHeader("Saldo Actual:");
		table.addData(new InputDouble("saldo_atual", movimentacao
				.obterSaldoAtual().doubleValue(), 30));

		table.addHeader("Saldo en Moneda Extranjera:");

		table.addData(new InputDouble("saldo_estrangeiro", movimentacao
				.obterSaldoEstrangeiro().doubleValue(), 30));

		table.addHeader("Descripción:");
		table
				.addData(new EventoDescricaoInput("descricao", movimentacao,
						true));

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirMovimentacaoFinanceiraConta"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());
			//incluirButton.getAction().add("origemMenuId",
			// this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);
		} else {
			if (movimentacao.permiteAtualizar()) {
				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarMovimentacaoFinanceiraConta"));
				atualizarButton.getAction().add("id", movimentacao.obterId());
				atualizarButton.getAction().add("origemMenuId",
						movimentacao.obterOrigem().obterId());
				table.addFooter(atualizarButton);
			}
		}

		if (movimentacao.permiteExcluir()) {
			Button excluirButton = new Button("Eliminar", new Action(
					"excluirEvento"));
			excluirButton.getAction().add("id", movimentacao.obterId());
			excluirButton.getAction().add("origemMenuId",
					movimentacao.obterOrigem().obterId());
			excluirButton.getAction().setConfirmation("Confirma exclusion ?");
			table.addFooter(excluirButton);
		}

		return table;
	}
}