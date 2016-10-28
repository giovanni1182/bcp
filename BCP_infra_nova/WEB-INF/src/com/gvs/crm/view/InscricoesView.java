package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Inscricao;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class InscricoesView extends Table
{
	public InscricoesView(Entidade entidade) throws Exception
	{
		super(6);

		this.setWidth("100%");

		this.addStyle(super.STYLE_ALTERNATE);

		String inscricaoStr = "";

		for (Iterator i = entidade.obterInscricoes().iterator(); i.hasNext();) 
		{
			Inscricao inscricao = (Inscricao) i.next();

			inscricaoStr = inscricao.obterInscricao();

			break;

		}

		this.addSubtitle("Inscripción " + inscricaoStr);

		this.addHeader("");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Nº Resolución");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Fecha Resolución");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Fecha de Validad");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Situación");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Ramo");

		for (Iterator i = entidade.obterInscricoes().iterator(); i.hasNext();)
		{
			Inscricao inscricao = (Inscricao) i.next();

			if (entidade.permiteAtualizar())
			{
				Action visualizarAction = new Action("visualizarEvento");
				visualizarAction.add("id", inscricao.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

				/*
				 * Action excluirAction = new Action("excluirEvento");
				 * excluirAction.add("id", inscricao.obterId()); this.add(new
				 * Link(new Image("delete.gif"), excluirAction));
				 */

			}
			else
				this.add("");

			Link link = null;

			if (inscricao.obterNumeroResolucao() == null
					|| inscricao.obterNumeroResolucao().equals(""))
				link = new Link("Sem Numero", new Action("visualizarEvento"));
			else
				link = new Link(inscricao.obterNumeroResolucao(), new Action(
						"visualizarEvento"));

			link.getAction().add("id", inscricao.obterId());

			this.setNextHAlign(HALIGN_CENTER);
			this.add(link);

			String dataResolucao = "";
			String dataValidade = "";

			if (inscricao.obterDataResolucao() != null)
				dataResolucao = new SimpleDateFormat("dd/MM/yyyy")
						.format(inscricao.obterDataResolucao());

			if (inscricao.obterDataValidade() != null)
				dataValidade = new SimpleDateFormat("dd/MM/yyyy")
						.format(inscricao.obterDataValidade());

			this.setNextHAlign(HALIGN_CENTER);
			this.add(dataResolucao);
			this.setNextHAlign(HALIGN_CENTER);
			this.add(dataValidade);
			this.setNextHAlign(HALIGN_CENTER);
			this.add(inscricao.obterSituacao().toUpperCase());
			
			Block block = new Block(Block.VERTICAL);
			
			for (Iterator j = inscricao.obterRamos().iterator(); j.hasNext();)
			{
				Inscricao.Ramo ramo = (Inscricao.Ramo) j.next();

				block.add(new Label(ramo.obterRamo()));
			}
			
			this.add(block);
		}

		Button nova = new Button("Nueva Inscripción", new Action("novoEvento"));
		nova.getAction().add("classe", "inscricao");
		nova.getAction().add("origemId", entidade.obterId());

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		this.setNextColSpan(this.getColumns());
		this.add(nova);

	}
}