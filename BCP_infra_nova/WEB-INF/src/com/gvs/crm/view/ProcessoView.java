package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.component.TipoPessoaSelect;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Processo;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ProcessoView extends EventoAbstratoView {
	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	private int _pasta;

	private boolean incluir;

	private Processo processo;

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		this.processo = (Processo) this.obterEvento();

		this.incluir = processo.obterId() == 0;

		Table table = new Table(1);

		_pasta = Integer.parseInt(properties.getProperty("_pasta", "1"));

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", processo.obterId());
		basicosLink.getAction().add("_pasta", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", processo.obterId());
		documentosLink.getAction().add("_pasta", DOCUMENTOS);
		documentosLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		block.add(new SeparadorLabel());
		block.add(documentosLink);

		table.add(block);

		switch (_pasta) {
		case DETALLES:
			table.add(this.obterBasicos());
			break;
		case DOCUMENTOS:
			table.add(new AnexosView(this.processo));
			break;
		}

		return table;

	}

	public View obterBasicos() throws Exception {
		Table table = new Table(2);

		table.addSubtitle("Detalles");

		if (!incluir) {
			Table pessoasTable = new Table(3);

			pessoasTable.addSubtitle("Agregar Personas");

			pessoasTable.addHeader("Persona:");
			pessoasTable.setNextColSpan(2);
			pessoasTable
					.add(new EntidadePopup(
							"pessoaId",
							"pessoaNome",
							null,
							"pessoa,pessoafisica,aseguradora,reaseguradora,auditorexterno,usuario",
							true));

			pessoasTable.add("");
			pessoasTable.addHeader("Nombre:");
			pessoasTable.add(new InputString("nome", null, 60));
			pessoasTable.add("");
			pessoasTable.addHeader("Teléfono:");
			pessoasTable.add(new InputString("telefone", null, 15));
			pessoasTable.add("");
			pessoasTable.addHeader("Email:");
			pessoasTable.add(new InputString("email", null, 20));

			pessoasTable.addHeader("Tipo del Persona:");
			pessoasTable.add(new TipoPessoaSelect("tipoPessoa"));

			Button pessoaButton = new Button("Agregar Persona", new Action(
					"incluirPessoaProcesso"));
			pessoaButton.getAction().add("id", processo.obterId());

			pessoasTable.add(pessoaButton);

			pessoasTable.setNextColSpan(pessoasTable.getColumns());
			pessoasTable.add(new Space());

			pessoasTable.addHeader("Personas Participantes:");

			int j = 1;

			Map pessoas = new TreeMap();

			for (Iterator i = processo.obterPessoas().iterator(); i.hasNext();) {
				Processo.Pessoa pessoa = (Processo.Pessoa) i.next();

				pessoas.put(pessoa.obterPessoa().obterDescricaoClasse()
						+ pessoa.obterPessoa().obterNome(), pessoa);
			}

			for (Iterator i = pessoas.values().iterator(); i.hasNext();) {
				Processo.Pessoa pessoa = (Processo.Pessoa) i.next();

				Link link = new Link(new Image("delete.gif"), new Action(
						"excluirPessoaProcesso"));
				link.getAction().add("id", processo.obterId());
				link.getAction().add("pessoaId2", pessoa.obterId());

				Link link2 = new Link(pessoa.obterPessoa().obterNome(),
						new Action("visualizarDetalhesEntidade"));
				link2.getAction().add("id", pessoa.obterPessoa().obterId());

				Block block = new Block(Block.HORIZONTAL);

				block.add(link);
				block.add(new Space(3));
				block.add(link2);

				if (j > 1)
					pessoasTable.add("");

				pessoasTable.add(block);
				pessoasTable.add(pessoa.obterTipo());

				j++;
			}

			table.setNextColSpan(table.getColumns());
			table.add(pessoasTable);

			table.setNextColSpan(pessoasTable.getColumns());
			table.add(new Space());
		}

		if (!incluir) {
			table.addHeader("Creado por:");
			table.add(new CriacaoLabel(processo));

			table.addHeader("Resposable:");

			Link link = new Link(processo.obterResponsavel().obterNome(),
					new Action("visualizarDetalhesEntidade"));
			link.getAction().add("id", processo.obterResponsavel().obterId());

			table.add(link);
		}

		/*
		 * table.addHeader("Demandante:"); table.add(new
		 * EntidadePopup("origemId", "origemNome", processo.obterDestino(),
		 * "corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",
		 * processo.permiteAtualizar()));
		 * 
		 * table.addHeader("Demandado:"); table.add(new
		 * EntidadePopup("destinoId", "destinoNome", processo.obterOrigem(),
		 * "corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa",
		 * processo.permiteAtualizar()));
		 */

		table.addHeader("Acción Expediente:");
		table
				.add(new InputString("expediente", processo.obterExpediente(),
						50));

		//        table.addHeader("Valor de la Acción:");
		//        table.add(new InputDouble("valorAcao", processo.obterValorAcao(),
		// 15));

		/*
		 * table.addHeader("Juzgado:"); table.add(new InputString("julgado",
		 * processo.obterJulgado(), 60));
		 * 
		 * table.addHeader("Juez:"); table.add(new InputString("juiz",
		 * processo.obterJuiz(), 60));
		 */

		//        table.addHeader("Secretaria Nº:");
		//        table.add(new InputString("secretaria", processo.obterSecretaria(),
		// 10));
		//        
		//        table.addHeader("Fiscal en lo:");
		//        table.add(new InputString("fiscal", processo.obterFiscal(), 40));
		//        
		//        table.addHeader("Turno:");
		//        table.add(new InputString("turno", processo.obterTurno(), 30));
		//        
		//        table.addHeader("A cargo de:");
		//        table.add(new InputString("cargo", processo.obterCargo(), 30));
		/*
		 * table.addHeader("Tipo del Processo:");
		 * if(processo.obterProduto()!=null) table.add(new
		 * ProdutoSelect("produto", processo.obterProduto().obterId())); else
		 * table.add(new ProdutoSelect("produto", 0));
		 */

		table.addHeader("Objeto de la causa:");
		table.add(new InputText("objeto", processo.obterObjeto(), 4, 70));

		//        table.addHeader("Circunscripción:");
		//        table.add(new InputString("circunscricao",
		// processo.obterCircunscricao(), 40));
		//        
		//        table.addHeader("Fuero:");
		//        table.add(new InputString("forum", processo.obterForum(), 60));

		table.addHeader("Fecha de la Resolución:");
		table.add(new InputDate("dataDemanda", processo.obterDataDemanda()));

		table.addHeader("Multa:");
		table.add(new InputString("sentenca", processo.obterSentenca(), 20));

		table.addHeader("Fecha pago de la Multa:");
		table.add(new InputDate("dataCancelamento", processo
				.obterDataCancelamento()));

		table.addHeader("Observação:");
		table.add(new EventoDescricaoInput("descricao", processo, processo
				.permiteAtualizar()));

		table.addHeader("Comentários");
		table.setNextColSpan(table.getColumns());
		table.add(new ComentariosView(processo));

		table.addHeader("Sub-eventos:");
		table.setNextColSpan(table.getColumns());
		table.add(new SubEventosView(processo));

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirProcesso"));
			table.addFooter(incluirButton);

			Button cancelarButton = new Button("Voltar", new Action(
					"novoEvento"));
			cancelarButton.getAction().add("passo", 2);
			table.addFooter(cancelarButton);
		} else {
			if (processo.permitePegar()) {
				Button pegarButton = new Button("Pegar", new Action(
						"pegarEvento"));
				pegarButton.getAction().add("id", processo.obterId());

				table.addFooter(pegarButton);
			}

			Button novaAgendaButton = new Button("Sub-Evento", new Action(
					"novoEvento"));
			novaAgendaButton.getAction().add("passo", 3);
			novaAgendaButton.getAction().add("superiorId", processo.obterId());
			novaAgendaButton.setEnabled(processo.permiteAtualizar());
			table.addFooter(novaAgendaButton);

			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarProcesso"));
			atualizarButton.getAction().add("id", processo.obterId());
			atualizarButton.setEnabled(processo.permiteAtualizar());
			table.addFooter(atualizarButton);

			Button comentarButton = new Button("Comentarios", new Action(
					"comentarEvento"));
			comentarButton.getAction().add("id", processo.obterId());
			comentarButton.getAction().add("view", true);
			comentarButton.setEnabled(processo.permiteAtualizar());
			table.addFooter(comentarButton);

			Button encaminharButton = new Button("Remetir", new Action(
					"encaminharEvento"));
			encaminharButton.getAction().add("id", processo.obterId());
			encaminharButton.getAction().add("view", true);
			encaminharButton.setEnabled(processo.permiteAtualizar());
			table.addFooter(encaminharButton);

			if (!processo.obterFase().obterCodigo().equals(Processo.EVENTO_PENDENTE) && !processo.obterFase().obterCodigo().equals(Processo.EVENTO_CONCLUIDO))
			{
				Button faseAnteriorButton = new Button("<<< Fase Anterior",	new Action("faseAnterior"));
				faseAnteriorButton.getAction().add("view", true);
				faseAnteriorButton.getAction().add("id", processo.obterId());
				faseAnteriorButton.setEnabled(processo.permiteAtualizar());
				table.addFooter(faseAnteriorButton);
			}

			if (!processo.obterFase().obterCodigo().equals(Processo.EVENTO_CONCLUIDO) && !processo.obterFase().obterCodigo().equals("superintendente"))
			{
				Button proximaFaseButton = new Button("Próxima Fase >>>",new Action("proximaFase"));
				proximaFaseButton.getAction().add("view", true);
				proximaFaseButton.getAction().add("id", processo.obterId());
				proximaFaseButton.setEnabled(processo.permiteAtualizar());
				table.addFooter(proximaFaseButton);
			}

			if (processo.obterFase().obterCodigo().equals("superintendente"))
			{
				Button concluirButton = new Button("Concluir", new Action("concluirEvento"));
				concluirButton.getAction().add("id", processo.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.setEnabled(processo.permiteAtualizar());
				table.addFooter(concluirButton);
			}

			if (processo.obterFase().obterCodigo().equals(Processo.EVENTO_CONCLUIDO))
			{
				boolean reabrir = false;

				for (Iterator i = processo.obterResponsavel().obterSuperiores().iterator(); i.hasNext();) 
				{
					Entidade e = (Entidade) i.next();

					if (e.obterApelido() != null) 
					{
						if (!e.obterApelido().equals("")) 
						{
							if (e.obterApelido().equals("secretaria")) 
							{
								reabrir = true;
								break;
							}
						}
					}
				}

				if (reabrir) 
				{
					Button reabrirButton = new Button("Reabrir", new Action("reabrirProcesso"));
					reabrirButton.getAction().add("id", processo.obterId());
					table.addFooter(reabrirButton);
				}
			}

			Button cancelarButton = new Button("Cancelar", new Action("cancelarProcesso"));
			cancelarButton.getAction().add("id", processo.obterId());
			cancelarButton.getAction().add("view", true);
			cancelarButton.setEnabled(processo.permiteAtualizar());
			table.addFooter(cancelarButton);

			Action action = new Action("excluirEvento");
			action.setConfirmation("Confirma Exclusão");

			Button excluirButton = new Button("Eliminar", action);
			excluirButton.getAction().add("id", processo.obterId());
			excluirButton.setEnabled(processo.permiteAtualizar());
			table.addFooter(excluirButton);

			/*
			 * Button voltarButton = new Button("Voltar", new
			 * Action("visualizarPaginaInicial"));
			 * table.addFooter(voltarButton);
			 */
		}
		return table;

	}
}