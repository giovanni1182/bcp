package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.view.Button;
import infra.view.Image;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class PlanosView extends Table
{
	public PlanosView(Entidade entidade, Usuario usuario) throws Exception
	{
		super(8);

		this.setWidth("100%");

		this.addStyle(super.STYLE_ALTERNATE);

		Collection planos = new ArrayList();

		String inscricaoStr = "";

		if (entidade instanceof Aseguradora) {
			Aseguradora aseguradora = (Aseguradora) entidade;

			planos = aseguradora.obterPlanosOrdenadorPorSecao();
		} else {
			for (Iterator i = entidade.obterEventosComoOrigem().iterator(); i
					.hasNext();) {
				Evento e = (Evento) i.next();

				if (e instanceof Plano) {
					Plano plano = (Plano) e;

					planos.add(e);
				}
			}
		}

		this.addSubtitle("");

		this.addHeader("");
		this.addHeader("Denominación");
		this.addHeader("Sección");
		this.addHeader("Plan");
		this.addHeader("Identificador");
		this.addHeader("Resolución");
		this.addHeader("Fecha Res.");
		this.addHeader("Situación");

		for (Iterator i = planos.iterator(); i.hasNext();) {
			Plano plano = (Plano) i.next();

			if (entidade.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarEvento");
				visualizarAction.add("id", plano.obterId());
				this.add(new Link(new Image("replace.gif"), visualizarAction));

			} else
				this.add("");

			this.add(plano.obterTitulo());
			this.add(plano.obterSecao());
			this.add(plano.obterPlano());

			Link link = new Link(plano.obterIdentificador(), new Action(
					"visualizarEvento"));
			link.getAction().add("id", plano.obterId());

			this.add(link);

			this.add(plano.obterResolucao());

			String dataResolucao = "";
			String dataValidade = "";

			if (plano.obterDataResolucao() != null)
				dataResolucao = new SimpleDateFormat("dd/MM/yyyy").format(plano
						.obterDataResolucao());

			this.add(dataResolucao);
			this.add(plano.obterSituacao());
		}

		if(usuario.obterNivel().equals(Usuario.INTENDENTE_IETA) || usuario.obterNivel().equals(Usuario.DIVISAO_ESTUDOS_ATUAIS) || usuario.obterNivel().equals(Usuario.DIVISAO_ESTUDOS_TECNICOS) || usuario.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			Button nova = new Button("Nuevo Plan", new Action("novoEvento"));
			nova.getAction().add("classe", "plano");
			nova.getAction().add("origemId", entidade.obterId());
	
			this.setNextColSpan(this.getColumns());
			this.add(new Space());
	
			this.setNextColSpan(this.getColumns());
			this.add(nova);
		}
	}
}