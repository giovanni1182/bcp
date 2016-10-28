package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.component.PrioridadeLabel;
import com.gvs.crm.component.PrioridadeSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Mensagem;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class MensagemView extends EventoAbstratoView
{
	private int _pasta;

	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Mensagem mensagem = (Mensagem) this.obterEvento();

		boolean incluir = mensagem.obterId() == 0;
		
		boolean notificacao = false;
		
		if(mensagem.obterTipo()!=null)
		{
			if(mensagem.obterTipo().equals("Notificación"))
				notificacao = true;
		}

		Table table = new Table(2);

		_pasta = Integer.parseInt(properties.getProperty("_pasta", "1"));

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", mensagem.obterId());
		basicosLink.getAction().add("_pasta", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Documentos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", mensagem.obterId());
		documentosLink.getAction().add("_pasta", DOCUMENTOS);
		documentosLink.setEnabled(!incluir && !notificacao);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		block.add(new SeparadorLabel());
		block.add(documentosLink);
		table.setNextColSpan(table.getColumns());

		table.add(block);

		if (_pasta == DETALLES)
		{
			table.addSubtitle("Detalles");

			if (incluir)
			{
				table.setWidth("100%");
				table.addHeader("Enviar para:");
				Table emailTable = new Table(2);
				table.addData(new EntidadePopup("responsavel","responsavelNome",null,
								"corredora,reaseguradora,auxiliarseguro,oficialcumprimento,aseguradora,pessoafisica,auditorexterno,usuario,empresa,GrupoMensagem",
								true));
				table.add("");

				Label emailLabel = new Label("Enviar Email(s)");
				emailLabel.setBold(true);
				emailTable.add(new Check("email", "true", false));
				emailTable.add(emailLabel);
				table.setNextColSpan(table.getColumns());
				table.add(emailTable);

				table.addHeader("Prioridad:");
				table.addData(new PrioridadeSelect("prioridade", mensagem.obterPrioridade(), true));
				table.addHeader("Título:");
				table.addData(new EventoTituloInput("titulo", mensagem, true));
				table.addHeader("Descripción:");
				table.addData(new EventoDescricaoInput("descricao", mensagem,true));

				Button incluirButton = new Button("Enviar", new Action("incluirEvento"));
				incluirButton.getAction().add("classe", "mensagem");
				table.addFooter(incluirButton);
			} 
			else
			{
				table.addHeader("Creado por:");
				table.addData(new CriacaoLabel(mensagem));
				table.addHeader("Prioridade:");
				table.addData(new PrioridadeLabel(mensagem.obterPrioridade()));
				table.addHeader("Título:");
				table.addHeader(mensagem.obterTitulo());
				table.addHeader("Descripción:");
				table.addData(mensagem.obterDescricao());
				table.addHeader("Comentarios:");
				table.add(new ComentariosView(mensagem));

				if (mensagem.permitePegar() && !notificacao)
				{
					Button pegarButton = new Button("Pegar", new Action("pegarEvento"));
					pegarButton.getAction().add("id", mensagem.obterId());
					table.addFooter(pegarButton);
				}

				if (mensagem.permiteDevolver() && !notificacao)
				{
					Button devolverButton = new Button("Devolver", new Action("devolverEvento"));
					devolverButton.getAction().add("id", mensagem.obterId());
					table.addFooter(devolverButton);
				}

				if (mensagem.permiteResponder() && !notificacao)
				{
					Button responderButton = new Button("Responder",new Action("responderEvento"));
					responderButton.getAction().add("id", mensagem.obterId());
					responderButton.getAction().add("view", true);
					responderButton.getAction().add("origemMenuId",mensagem.obterResponsavel().obterId());
					table.addFooter(responderButton);
				}

				if (mensagem.permiteEncaminhar() && !notificacao)
				{
					Button encaminharButton = new Button("Remitir", new Action("encaminharEvento"));
					encaminharButton.getAction().add("id", mensagem.obterId());
					encaminharButton.getAction().add("view", true);
					encaminharButton.getAction().add("origemMenuId",mensagem.obterResponsavel().obterId());
					table.addFooter(encaminharButton);
				}

				if (mensagem.permiteConcluir() || notificacao)
				{
					Button concluirButton = new Button("Concluir", new Action("concluirEvento"));
					concluirButton.getAction().add("id", mensagem.obterId());
					concluirButton.getAction().add("view", true);
					concluirButton.getAction().add("origemMenuId",mensagem.obterResponsavel().obterId());
					table.addFooter(concluirButton);
				}

				if (mensagem.permiteExcluir() && !notificacao)
				{
					Button excluirButton = new Button("Eliminar", new Action("excluirEvento"));
					excluirButton.getAction().add("id", mensagem.obterId());
					excluirButton.getAction().add("origemMenuId",mensagem.obterOrigem().obterId());
					excluirButton.getAction().setConfirmation("Confirma exclusión ?");
					table.addFooter(excluirButton);
				}

				Button voltarButton = new Button("Volver", new Action("visualizarPaginaInicial"));
				voltarButton.getAction().add("origemMenuId",mensagem.obterResponsavel().obterId());
				table.addFooter(voltarButton);
			}
		}

		else if (_pasta == DOCUMENTOS)
		{
			table.setNextColSpan(table.getColumns());
			table.add(new AnexosView(mensagem));
		}

		return table;
	}
}