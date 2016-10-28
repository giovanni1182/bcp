package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CondicaoPagamentoSelect;
import com.gvs.crm.component.DateInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.OperacaoSelect;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.MovimentacaoFinanceira;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Button;
import infra.view.InputDouble;
import infra.view.Table;
import infra.view.View;

public class MovimentacaoFinanceiraView extends EventoAbstratoView {

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		MovimentacaoFinanceira movimentacao = (MovimentacaoFinanceira) this
				.obterEvento();
		Evento superior = null;

		if (movimentacao.obterSuperior() != null)
			superior = movimentacao.obterSuperior();

		boolean incluir = movimentacao.obterId() == 0;
		if (superior != null && incluir) {
			movimentacao.atribuirOrigem(superior.obterOrigem());
			movimentacao.atribuirPrioridade(superior.obterPrioridade());
		}
		Table table = new Table(2);

		table.addHeader("Origem:");
		table.addData(new EntidadePopup("origemId", "origem", movimentacao
				.obterOrigem(), true, true));

		table.addHeader("Destino:");
		table.addData(new EntidadePopup("destinoId", "destino", movimentacao
				.obterDestino(), true));

		table.addHeader("Conta");
		if (movimentacao.obterConta() == null)
			table.addData(new EntidadePopup("contaId", "conta", null, "Conta",
					true));
		else
			table.addData(new EntidadePopup("contaId", "conta", movimentacao
					.obterConta(), "Conta", true));

		table.addHeader("Responsável:");
		table.addData(new EntidadePopup("responsavelId", "responsavel",
				movimentacao.obterResponsavel(), "Usuario", movimentacao
						.permiteAtualizar()));

		table.addHeader("Condição Prevista:");
		table.addData(new CondicaoPagamentoSelect("condicao_prevista",
				movimentacao.obterCondicaoPrevista(), movimentacao));

		table.addHeader("Data Prevista:");
		table.addData(new DateInput("data_prevista", movimentacao
				.obterDataPrevista(), true));

		table.addHeader("Valor Previsto:");
		table.addData(new InputDouble("valor_previsto", movimentacao
				.obterValorPrevisto(), 10));

		/*
		 * table.addHeader("Banco:"); if(movimentacao.obterBanco()!=null)
		 * table.addData(new EntidadePopup("bancoId", "bancoNome",
		 * movimentacao.obterBanco(), "Banco",
		 * movimentacao.permiteAtualizar())); else table.addData(new
		 * EntidadePopup("bancoId", "bancoNome", movimentacao.obterBanco(),
		 * "Banco", true));
		 * 
		 * table.addHeader("Nº do Cheque:"); table.addData(new
		 * InputString("numeroCheque", movimentacao.obterNumeroCheque(), 20));
		 */
		if (!incluir) {
			if (movimentacao.obterFase().obterCodigo().equals(
					MovimentacaoFinanceira.EVENTO_CONCLUIDO)) {
				table.addHeader("Condição Realizada:");
				table.addData(new CondicaoPagamentoSelect("condicao_realizada",
						movimentacao.obterCondicaoRealizada(), movimentacao));

				table.addHeader("Data Realizada:");
				table
						.addData(new DateInput("data_realizada", movimentacao
								.obterDataRealizada(), movimentacao
								.permiteAtualizar()));

				table.addHeader("Valor Realizado:");
				table.addData(new InputDouble("valor_realizado", movimentacao
						.obterValorRealizado(), 10));
			}
		}

		table.addHeader("Tipo Operação:");
		table
				.addData(new OperacaoSelect("tipo", movimentacao.obterTipo(),
						true));

		if (incluir) {
			Button incluirButton = new Button("Incluir", new Action(
					"incluirMovimentacaoFinanceira"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());

			table.addFooter(incluirButton);
		} else {
			Button atualizarButton = new Button("Atualizar", new Action(
					"atualizarMovimentacaoFinanceira"));
			atualizarButton.getAction().add("id", movimentacao.obterId());
			atualizarButton.setEnabled(movimentacao.permiteAtualizar());
			atualizarButton.getAction().add("origemMenuId",
					movimentacao.obterOrigem().obterId());
			table.addFooter(atualizarButton);

			/*
			 * if(movimentacao.permitePrever()) { Button preverButton = new
			 * Button("Prever", new Action("preverMovimentacaoFinanceira"));
			 * preverButton.getAction().add("id", movimentacao.obterId());
			 * preverButton.getAction().add("origemMenuId",
			 * movimentacao.obterOrigem().obterId());
			 * preverButton.getAction().setConfirmation("Confirma prever a
			 * Movimentação Financeira ?");
			 * preverButton.setEnabled(movimentacao.permiteAtualizar());
			 * table.addFooter(preverButton); }
			 */

			Button reservarButton = new Button("Realizar", new Action(
					"realizarMovimentacaoFinanceira"));
			reservarButton.getAction().add("id", movimentacao.obterId());
			reservarButton.getAction().add("origemMenuId",
					movimentacao.obterOrigem().obterId());
			reservarButton.getAction().add("view", true);
			reservarButton.getAction().setConfirmation(
					"Confirma realizar a Movimentação Financeira ?");
			reservarButton.setEnabled(movimentacao.obterFase().obterCodigo()
					.equals(MovimentacaoFinanceira.EVENTO_PREVISTO));
			table.addFooter(reservarButton);

			Button excluirButton = new Button("Excluir", new Action(
					"excluirEvento"));
			excluirButton.getAction().add("id", movimentacao.obterId());
			excluirButton.getAction().add("origemMenuId",
					movimentacao.obterOrigem().obterId());
			excluirButton.getAction().setConfirmation(
					"Confirma exclusão do Evento ?");
			//excluirButton.setEnabled(movimentacao.permiteExcluir());
			table.addFooter(excluirButton);

		}
		return table;
	}
}