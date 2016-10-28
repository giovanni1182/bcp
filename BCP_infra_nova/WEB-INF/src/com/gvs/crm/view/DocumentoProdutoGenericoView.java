package com.gvs.crm.view;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.component.TipoDocumentoSelect;
import com.gvs.crm.component.TipoPessoasDocumentoSelect;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Inscricao;

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

public class DocumentoProdutoGenericoView extends EventoAbstratoView {

	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	public static final int RESOLUCOES = 3;

	private int _pasta;

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		DocumentoProduto documento = (DocumentoProduto) this.obterEvento();

		boolean incluir = documento.obterId() == 0;

		Table table = new Table(2);

		this._pasta = Integer.parseInt(properties.getProperty("_pasta", "1"));

		if (incluir || _pasta > 3)
			this._pasta = DETALLES;

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", documento.obterId());
		basicosLink.getAction().add("_pasta", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link resolucoesLink = new Link("Inscripciones", new Action(
				"visualizarEvento"));
		((Label) resolucoesLink.getCaption()).setBold(_pasta == RESOLUCOES);
		resolucoesLink.getAction().add("id", documento.obterId());
		resolucoesLink.getAction().add("_pasta", RESOLUCOES);
		resolucoesLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", documento.obterId());
		documentosLink.getAction().add("_pasta", DOCUMENTOS);
		documentosLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		if (!incluir) {
			if (documento.obterDocumento().obterApelido().equals(
					"informeinscripcion")) {
				block.add(new SeparadorLabel());
				block.add(resolucoesLink);
			}
		}

		block.add(new SeparadorLabel());
		block.add(documentosLink);

		table.add(block);

		if (_pasta == DETALLES) {
			table.add("");
			table.addSubtitle("Detalles");

			if (!incluir) {
				table.addHeader("Creado por:");
				table.add(new CriacaoLabel(documento));

				table.addHeader("Responsable:");
				table.add(documento.obterResponsavel().obterNome());
			}

			table.addHeader("Superior:");
			table.add(new EventoSuperiorLabel(documento));

			table.addHeader("Fase:");
			if (incluir)
				table.addHeader("PENDIENTE");
			else
				table.addHeader(documento.obterFase().obterNome());

			table.addHeader("Tipo del Documento:");
			table.add(new TipoDocumentoSelect("documento", documento
					.obterDocumento()));

			table.addHeader("Descripción:");
			table.add(new EventoDescricaoInput("descricao", documento, true));

			/*
			 * table.addHeader("Para Quién:"); table.add(new
			 * EntidadePopup("origemId", "origemNome", documento.obterOrigem(),
			 * "corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa,departamento",
			 * documento.permiteAtualizar()));
			 */

			if (!incluir) {
				Table personasTable = new Table(4);

				personasTable
						.addSubtitle("Agregar Personas Antes da Descripción");

				personasTable.addHeader("Personas:");
				personasTable.setNextColSpan(4);
				personasTable
						.add(new EntidadePopup(
								"pessoaAntesId",
								"pessoaAntesNome",
								null,
								"pessoa,pessoafisica,aseguradora,auditorexterno,departamento,oficialcumprimento,usuario",
								true));

				personasTable.addHeader("Tipo del Persona:");
				personasTable.add(new TipoPessoasDocumentoSelect(
						"tipoPessoaAntes", documento, ""));
				personasTable
						.add(new InputString("novoTipoPessoaAntes", "", 40));

				Button novoPessoaButton = new Button("Agregar Persona",
						new Action("incluirPessoaAntesDocumento"));
				novoPessoaButton.getAction().add("id", documento.obterId());
				novoPessoaButton.getAction().add("posicao", 0);
				novoPessoaButton.setEnabled(documento.permiteAtualizar());

				personasTable.add(novoPessoaButton);

				table.setNextColSpan(table.getColumns());
				table.add(personasTable);

				table.setNextColSpan(table.getColumns());
				table.add(new Space());

				table.addHeader("Pessoa");
				table.addHeader("Tipo");

				for (Iterator i = documento.obterPessoasAntes().iterator(); i
						.hasNext();) {
					DocumentoProduto.Pessoa pessoa = (DocumentoProduto.Pessoa) i
							.next();

					Link link = new Link(new Image("delete.gif"), new Action(
							"excluirPessoaDocumento"));
					link.getAction().add("id", documento.obterId());
					link.getAction().add("pessoaId2",
							pessoa.obterPessoa().obterId());
					link.getAction().add("posicao", pessoa.obterPosicao());

					Link link2 = new Link(pessoa.obterPessoa().obterNome(),
							new Action("visualizarDetalhesEntidade"));
					link2.getAction().add("id", pessoa.obterPessoa().obterId());

					Block block2 = new Block(Block.HORIZONTAL);
					block2.add(link);
					block2.add(new Space(3));
					block2.add(link2);

					table.add(block2);

					table.add(pessoa.obterTipo());
				}

				table.setNextColSpan(table.getColumns());
				table.add(new Space());
			}

			table.addHeader("Fecha Documento:");
			if (documento.obterDataPrevistaInicio() != null)
				table.add(new InputDate("data", documento
						.obterDataPrevistaInicio()));
			else
				table.add(new InputDate("data", new Date()));

			table.addHeader("Nº Documento:");
			table.add(new InputString("numero", documento.obterNumero(), 10));

			table.addHeader("Titulo del Documento:");
			table.add(new InputString("titulo", documento
					.obterTituloDocumento(), 50));

			table.addHeader("Referente:");
			table.add(new InputText("referente", documento.obterReferente(), 4,
					70));

			if (!incluir) {
				String descricao = "";

				if (documento.obterTexto() != null
						&& !documento.obterTexto().trim().equals(""))
					descricao = documento.obterTexto();
				else {
					descricao = documento.obterDocumento().obterAtributo(
							"descricao").obterValor();

					int cont = 1;

					String pessoas = "\n";

					for (Iterator i = documento.obterInscricoesVinculadas()
							.iterator(); i.hasNext();) {
						Inscricao inscricao = (Inscricao) i.next();

						pessoas += new Integer(cont).toString() + " - "
								+ "C.I nº " + inscricao.obterInscricao()
								+ " - " + inscricao.obterOrigem().obterNome()
								+ "\n";

						cont++;
					}

					descricao += pessoas;
				}

				table.addHeader("Texto do Documento:");
				table.add(new InputText("descricao", descricao, 20, 110));
			}

			if (!incluir) {
				Table personasTable = new Table(4);

				personasTable
						.addSubtitle("Agregar Personas Depois da Descripción");

				personasTable.addHeader("Personas:");
				personasTable.setNextColSpan(4);
				personasTable
						.add(new EntidadePopup(
								"pessoaDepoisId",
								"pessoaDepoisNome",
								null,
								"pessoa,pessoafisica,aseguradora,auditorexterno,departamento,oficialcumprimento,usuario",
								true));

				personasTable.addHeader("Tipo del Persona:");
				personasTable.add(new TipoPessoasDocumentoSelect(
						"tipoPessoaDepois", documento, ""));
				personasTable.add(new InputString("novoTipoPessoaDepois", "",
						40));

				Button novoPessoaButton = new Button("Agregar Persona",
						new Action("incluirPessoaDepoisDocumento"));
				novoPessoaButton.getAction().add("id", documento.obterId());
				novoPessoaButton.getAction().add("posicao", 1);
				novoPessoaButton.setEnabled(documento.permiteAtualizar());

				personasTable.add(novoPessoaButton);

				table.setNextColSpan(table.getColumns());
				table.add(personasTable);

				table.setNextColSpan(table.getColumns());
				table.add(new Space());

				table.addHeader("Pessoa");
				table.addHeader("Tipo");

				for (Iterator i = documento.obterPessoasDepois().iterator(); i
						.hasNext();) {
					DocumentoProduto.Pessoa pessoa = (DocumentoProduto.Pessoa) i
							.next();

					Link link = new Link(new Image("delete.gif"), new Action(
							"excluirPessoaDocumento"));
					link.getAction().add("id", documento.obterId());
					link.getAction().add("pessoaId2",
							pessoa.obterPessoa().obterId());
					link.getAction().add("posicao", pessoa.obterPosicao());

					Link link2 = new Link(pessoa.obterPessoa().obterNome(),
							new Action("visualizarDetalhesEntidade"));
					link2.getAction().add("id", pessoa.obterPessoa().obterId());

					Block block2 = new Block(Block.HORIZONTAL);
					block2.add(link);
					block2.add(new Space(3));
					block2.add(link2);

					table.add(block2);

					table.add(pessoa.obterTipo());
				}

				table.setNextColSpan(table.getColumns());
				table.add(new Space());
			}

			/*
			 * if(!incluir) {
			 * if(documento.obterDocumento().obterNome().equals("Informe")) {
			 * table.addHeader("Analista:"); table.add(new
			 * EntidadePopup("analistaId", "analistaNome",
			 * documento.obterAnalista(), "Usuario", true));
			 * 
			 * table.addHeader("Jefe de División:"); table.add(new
			 * EntidadePopup("chefeId", "chefeNome",
			 * documento.obterChefeDivisao(), "Usuario", true));
			 * 
			 * table.addHeader("Intendente:"); table.add(new
			 * EntidadePopup("intendenteId", "intendenteNome",
			 * documento.obterIntendente(), "Usuario", true)); } else
			 * if(documento.obterDocumento().obterNome().equals("Dictame")) {
			 * table.addHeader("Superintendente:"); table.add(new
			 * EntidadePopup("superintendenteId", "superintendenteNome",
			 * documento.obterSuperIntendente(), "Usuario", true)); } }
			 */

			/*
			 * table.setNextColSpan(table.getColumns()); table.add(new Space());
			 */

			/*
			 * table.addHeader("Documentos:");
			 * 
			 * Table table3 = new Table(1);
			 * 
			 * Block block2 = new Block(Block.HORIZONTAL);
			 * 
			 * for(Iterator i = documento.obterInferiores().iterator() ;
			 * i.hasNext() ; ) { Evento e = (Evento) i.next();
			 * 
			 * Link link2 = null;
			 * 
			 * link2 = new Link(e.obterTitulo(), new
			 * Action("visualizarEvento"));
			 * 
			 * link2.getAction().add("id", e.obterId());
			 * 
			 * block2.add(link2); }
			 * 
			 * table3.add(block2); table.add(table3);
			 */

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("Comentarios");
			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(documento));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("Sub-eventos:");
			table.setNextColSpan(table.getColumns());
			table.add(new SubEventosView(documento));

			if (incluir) {
				Button incluirButton = new Button("Agregar", new Action(
						"incluirDocumentoProduto"));
				if (documento.obterSuperior() != null)
					incluirButton.getAction().add("superiorId",
							documento.obterSuperior().obterId());

				table.addFooter(incluirButton);
			} else {
				if (documento.permitePegar()
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.INFORMATIVO)) {
					Button pegarButton = new Button("Pegar", new Action(
							"pegarEvento"));
					pegarButton.getAction().add("id", documento.obterId());
					table.addFooter(pegarButton);
				}

				Button novaAgendaButton = new Button("Sub-Evento", new Action(
						"novoEvento"));
				novaAgendaButton.getAction().add("passo", 3);
				novaAgendaButton.getAction().add("superiorId",
						documento.obterId());
				novaAgendaButton.setEnabled(documento.permiteAtualizar()
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.INFORMATIVO));
				table.addFooter(novaAgendaButton);

				if (!documento.obterFase().obterCodigo().equals(
						DocumentoProduto.EVENTO_PENDENTE)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.EVENTO_CONCLUIDO)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.INFORMATIVO)) {
					Button faseAnteriorButton = new Button("<<< Fase Anterior",
							new Action("faseAnterior"));
					faseAnteriorButton.getAction().add("view", true);
					faseAnteriorButton.getAction().add("id",
							documento.obterId());
					faseAnteriorButton.setEnabled(documento.permiteAtualizar());
					table.addFooter(faseAnteriorButton);
				}

				if (!documento.obterFase().obterCodigo().equals(
						DocumentoProduto.EVENTO_CONCLUIDO)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.SUPERINTENDENTE)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.INFORMATIVO)) {
					Button proximaFaseButton = new Button("Próxima Fase >>>",
							new Action("proximaFase"));
					proximaFaseButton.getAction().add("view", true);
					proximaFaseButton.getAction()
							.add("id", documento.obterId());
					proximaFaseButton.setEnabled(documento.permiteAtualizar());
					table.addFooter(proximaFaseButton);
				}

				if (documento.obterFase().obterCodigo().equals(
						DocumentoProduto.SUPERINTENDENTE)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.INFORMATIVO)) {
					Button aprovarButton = new Button("Aprovar", new Action(
							"aprovarDocumentoProduto"));
					aprovarButton.getAction().add("id", documento.obterId());
					aprovarButton.getAction().add("view", true);
					aprovarButton.setEnabled(documento.permiteAtualizar());
					table.addFooter(aprovarButton);

					Button rejeitarButton = new Button("Rejeitar", new Action(
							"rejeitarDocumentoProduto"));
					rejeitarButton.getAction().add("id", documento.obterId());
					rejeitarButton.getAction().add("view", true);
					rejeitarButton.setEnabled(documento.permiteAtualizar());
					table.addFooter(rejeitarButton);
				}

				Button atualizarButton = new Button("Actualizar", new Action(
						"atualizarDocumentoProduto"));
				atualizarButton.getAction().add("id", documento.obterId());
				atualizarButton.setEnabled(documento.permiteAtualizar()
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA));
				table.addFooter(atualizarButton);

				if (documento.permiteAtualizar()
						&& (documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
								|| documento.obterFase().obterCodigo().equals(
										DocumentoProduto.REJEITADA) || documento
								.obterFase().obterCodigo().equals(
										DocumentoProduto.INFORMATIVO))) {
					Button concluirButton = new Button("Concluir", new Action(
							"concluirEvento"));
					concluirButton.getAction().add("id", documento.obterId());
					concluirButton.getAction().add("view", true);
					table.addFooter(concluirButton);
				}

				Button comentarButton = new Button("Comentarios", new Action(
						"comentarEvento"));
				comentarButton.getAction().add("id", documento.obterId());
				comentarButton.getAction().add("view", true);
				comentarButton.setEnabled(documento.permiteAtualizar()
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA));
				table.addFooter(comentarButton);

				Button encaminharButton = new Button("Remetir", new Action(
						"encaminharEvento"));
				encaminharButton.getAction().add("id", documento.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.setEnabled(documento.permiteAtualizar()
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.APROVADA)
						&& !documento.obterFase().obterCodigo().equals(
								DocumentoProduto.REJEITADA));
				table.addFooter(encaminharButton);

				Action action = new Action("excluirEvento");
				action.setConfirmation("Confirma Exclusión");

				Button excluirButton = new Button("Eliminar", action);
				excluirButton.getAction().add("id", documento.obterId());
				//excluirButton.setEnabled(documento.permiteExcluir());
				table.addFooter(excluirButton);

				Button imprimirButton = new Button("Imprimir", new Action(
						"imprimirDocumento"));
				imprimirButton.getAction().add("id", documento.obterId());
				table.addFooter(imprimirButton);
			}

			if (documento.obterSuperior() != null) {
				Button cancelarButton = new Button("Volver", new Action(
						"visualizarEvento"));
				cancelarButton.getAction().add("id",
						documento.obterSuperior().obterId());
				table.addFooter(cancelarButton);
			} else {
				Button cancelarButton = new Button("Volver", new Action(
						"novoEvento"));
				cancelarButton.getAction().add("passo", 2);
				table.addFooter(cancelarButton);
			}

		}

		else if (_pasta == DOCUMENTOS) {
			table.add("");
			table.setNextColSpan(table.getColumns());
			table.add(new AnexosView(documento));
		}

		else if (_pasta == RESOLUCOES) {
			table.add("");
			table.setNextColSpan(table.getColumns());
			table.add(new InscricoesPendentesView(documento, user));
		}

		return table;
	}
}