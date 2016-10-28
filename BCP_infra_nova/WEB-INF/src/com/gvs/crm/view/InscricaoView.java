package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.RamoInscricaoSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Renovacao;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.InputInteger;
import infra.view.InputLong;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class InscricaoView extends EventoAbstratoView {

	private static final int DETALHE = 0;

	private static final int VINCULOS = 1;

	public View execute(User arg0, Locale arg1, Properties properties) throws Exception
	{
		Inscricao inscricao = (Inscricao) this.obterEvento();

		boolean incluir = inscricao.obterId() == 0;

		Table table = new Table(2);

		int _pasta = Integer.parseInt(properties.getProperty("_pasta", "0"));

		if (incluir || _pasta > 1)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", inscricao.obterId());
		dadosLink.getAction().add("_pasta", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link componentesLink = new Link("Documentos Vinculados", new Action(
				"visualizarEvento"));
		((Label) componentesLink.getCaption()).setBold(_pasta == VINCULOS);
		componentesLink.getAction().add("id", inscricao.obterId());
		componentesLink.getAction().add("_pasta", VINCULOS);
		componentesLink.setEnabled(!incluir);

		Block block2 = new Block(Block.HORIZONTAL);
		block2.add(dadosLink);
		block2.add(new SeparadorLabel());
		block2.add(componentesLink);
		table.setNextColSpan(table.getColumns());
		table.add(block2);

		if (_pasta == DETALHE) {

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			if (!incluir) {
				table.addHeader("Creado por:");
				table.addData(new CriacaoLabel(inscricao));

				table.addHeader("Responsable:");
				table.addHeader(inscricao.obterResponsavel().obterNome());
			}

			if (incluir) {
				InputLong input2 = new InputLong("origemId", inscricao
						.obterOrigem().obterId(), 10);
				input2.setEnabled(false);
				input2.setVisible(false);
				table.add(input2);
			} else
				table.addHeader("Solicitante");

			if (inscricao.obterOrigem() != null) {
				Link link2 = new Link(inscricao.obterOrigem().obterNome(),
						new Action("visualizarDetalhesEntidade"));
				link2.getAction().add("id", inscricao.obterOrigem().obterId());

				table.addData(link2);
			} else
				table.add("");

			table.addHeader("Inscripción:");
			table.add(new InputString("inscricao", inscricao.obterInscricao(), 4));

			table.addHeader("Nº de la Resolución:");
			table.add(new InputString("resolucao", inscricao
					.obterNumeroResolucao(), 20));

			table.addHeader("Fecha de la Resolución:");
			table.add(new InputDate("dataResolucao", inscricao
					.obterDataResolucao()));

			table.addHeader("Fecha de Validad:");
			table.add(new InputDate("dataValidade", inscricao
					.obterDataValidade()));
			
			Block cesionBlock = new Block(Block.HORIZONTAL);
			
			table.addHeader("Cesión hasta:");
			cesionBlock.add(new InputInteger("cesion", inscricao.obterCesion(),3));
			cesionBlock.add(new Space(2));
			cesionBlock.add(new Label("%"));
			table.add(cesionBlock);

			table.addHeader("Situación:");
			if(!incluir)
				table.addHeader(inscricao.obterSituacao().toUpperCase());
			else
				table.addHeader("");

			table.addHeader("Ramo en que opera:");

			Block ramoBlock = new Block(Block.HORIZONTAL);

			ramoBlock.add(new RamoInscricaoSelect("ramo", inscricao, null));
			ramoBlock.add(new Space(4));
			ramoBlock.add(new InputString("novoRamo", null, 50));
			if (!incluir)
			{
				ramoBlock.add(new Space(2));

				Button incluirRamoButton = new Button("Agregar Ramo",new Action("incluirRamoInscricao"));
				incluirRamoButton.getAction().add("id", inscricao.obterId());
				incluirRamoButton.setEnabled(inscricao.permiteAtualizar());

				ramoBlock.add(incluirRamoButton);
			}

			table.add(ramoBlock);
			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			if (inscricao.obterRamos().size() > 0)
			{
				table.addHeader("Ramos:");

				Table tableRamo = new Table(2);

				for (Iterator i = inscricao.obterRamos().iterator(); i.hasNext();)
				{
					Inscricao.Ramo ramo = (Inscricao.Ramo) i.next();

					Link link3 = new Link(new Image("delete.gif"), new Action("excluirRamoInscricao"));
					link3.getAction().add("id", inscricao.obterId());
					link3.getAction().add("seq", ramo.obterSeq());

					if (inscricao.permiteAtualizar())
						tableRamo.add(link3);
					else
						tableRamo.add("");

					tableRamo.add(ramo.obterRamo());
				}

				table.add(tableRamo);
			}

			Table table2 = new Table(2);

			table2.addSubtitle("Seguro Caución");

			table2.addHeader("Aseguradora:");
			table2.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",inscricao.obterAseguradora(), "aseguradora", true));

			table2.addHeader("Sección:");
			table2.add(new InputString("secao", inscricao.obterNumeroSecao(),15));

			table2.addHeader("Nº del Instrumento:");
			table2.add(new InputString("numeroApolice", inscricao.obterNumeroApolice(), 15));

			table2.addHeader("Fecha Inicio Vigencia:");
			table2.add(new InputDate("dataEmissao", inscricao.obterDataEmissao()));

			table2.addHeader("Fecha Termino Vigencia:");
			table2.add(new InputDate("dataVencimento", inscricao.obterDataVencimento()));

			table.setNextColSpan(table.getColumns());
			table.add(table2);

			if (incluir)
			{
				Button incluirButton = new Button("Agregar", new Action("incluirInscricao"));
				table.addFooter(incluirButton);
			}
			else
			{
				if (inscricao.permitePegar())
				{
					Button pegarButton = new Button("Pegar", new Action("pegarEvento"));
					pegarButton.getAction().add("id", inscricao.obterId());
					table.addFooter(pegarButton);
				}
				
				String codigofase = inscricao.obterFase().obterCodigo();
				boolean podeAtualizar = inscricao.permiteAtualizar()&& !codigofase.equals(Inscricao.SUSPENSA) && !codigofase.equals(Inscricao.CANCELADA);

				Button aprovarButton = new Button("Aprovar Por Fora",new Action("aprovarInscricao"));
				aprovarButton.getAction().add("id", inscricao.obterId());
				aprovarButton.getAction().add("view", true);
				aprovarButton.setEnabled(codigofase.equals(Inscricao.EVENTO_PENDENTE));
				table.addFooter(aprovarButton);

				Button novaAgendaButton = new Button("Sub-Evento", new Action("novoEvento"));
				novaAgendaButton.getAction().add("passo", 3);
				novaAgendaButton.getAction().add("superiorId",inscricao.obterId());
				novaAgendaButton.setEnabled(podeAtualizar);
				table.addFooter(novaAgendaButton);

				Button atualizarButton = new Button("Actualizar",new Action("atualizarInscricao"));
				atualizarButton.getAction().add("id", inscricao.obterId());
				if(inscricao.obterCesion() > 0)
					atualizarButton.setEnabled(podeAtualizar);
					
				table.addFooter(atualizarButton);
				
				Button suspenderButton = new Button("Suspender", new Action("suspenderInscricao"));
				suspenderButton.getAction().add("id", inscricao.obterId());
				suspenderButton.getAction().add("view", true);
				suspenderButton.setEnabled(podeAtualizar);
				table.addFooter(suspenderButton);

				Button comentarButton = new Button("Comentar", new Action("comentarEvento"));
				comentarButton.getAction().add("id", inscricao.obterId());
				comentarButton.setEnabled(inscricao.permiteAtualizar());
				comentarButton.getAction().add("view", true);
				table.addFooter(comentarButton);

				Button cancelarButton = new Button("Cancelar", new Action("cancelarInscricao"));
				cancelarButton.getAction().add("id", inscricao.obterId());
				cancelarButton.getAction().add("view", true);
				cancelarButton.setEnabled(podeAtualizar);
				table.addFooter(cancelarButton);

				Button encaminharButton = new Button("Remetir", new Action("encaminharEvento"));
				encaminharButton.getAction().add("id", inscricao.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.setEnabled(podeAtualizar);
				table.addFooter(encaminharButton);

				Button concluirButton = new Button("Concluir", new Action("concluirEvento"));
				concluirButton.getAction().add("id", inscricao.obterId());
				concluirButton.getAction().add("view", true);
				concluirButton.setEnabled(inscricao.permiteAtualizar());
				table.addFooter(concluirButton);

				Action action = new Action("excluirEvento");
				action.setConfirmation("Confirma Exclusión");

				Button excluirButton = new Button("Eliminar", action);
				excluirButton.setEnabled(inscricao.permiteExcluir());
				excluirButton.getAction().add("id", inscricao.obterId());
				excluirButton.getAction().add("entidadeId", inscricao.obterOrigem().obterId());
				table.addFooter(excluirButton);

				if (inscricao.obterOrigem() != null)
				{
					Button voltarButton = new Button("Volver", new Action("visualizarDetalhesEntidade"));
					voltarButton.getAction().add("id",inscricao.obterOrigem().obterId());
					table.addFooter(voltarButton);
				}
			}

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("Comentarios");
			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(inscricao));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("Documento:");
			for (Iterator i = inscricao.obterInferiores().iterator(); i.hasNext();)
			{
				Evento e = (Evento) i.next();

				if (e instanceof Renovacao)
				{
					Link link = new Link(e.obterTitulo(), new Action("visualizarEvento"));
					link.getAction().add("id", e.obterId());

					table.add(link);
				}
			}

			table.addHeader("Sub-eventos:");
			table.setNextColSpan(table.getColumns());
			table.add(new SubEventosView(inscricao));

		}

		else if (_pasta == VINCULOS)
		{
			table.setNextColSpan(table.getColumns());
			table.add(new VinculosIncricoesView(inscricao));
		}

		return table;
	}
}