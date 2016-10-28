package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.MeicosCalculo;

import infra.control.Action;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;

public class AgendaMeicosTabuleiroView extends Table {
	public AgendaMeicosTabuleiroView(Collection aseguradoras,AgendaMeicos agenda) throws Exception 
	{
		super(6);

		this.addSubtitle("Tabuleiro");
		this.addStyle(super.STYLE_ALTERNATE);

		this.setWidth("100%");

		this.addHeader("Aseguradora");
		this.addHeader("Ratios Financeiros");
		this.addHeader("Margen Solvencia");
		this.addHeader("In-Situ");
		this.addHeader("Otros Indicadores");
		this.addHeader("Clasificación");

		for (Iterator i = aseguradoras.iterator(); i.hasNext();) 
		{
			Aseguradora aseguradora = (Aseguradora) i.next();

			Link link = new Link(aseguradora.obterNome(), new Action("visualizarDetalhesEntidade"));
			link.getAction().add("id", aseguradora.obterId());

			this.add(link);

			if (aseguradora.obterCalculoMeicos(agenda).size() == 0)
			{
				this.add("");
				this.add("");
				this.add("");
				this.add("");
				this.add("");
			}
			else 
			{
				Block block1 = new Block(Block.HORIZONTAL);
				Block block2 = new Block(Block.HORIZONTAL);
				Block block3 = new Block(Block.HORIZONTAL);
				Block block4 = new Block(Block.HORIZONTAL);
				
				for (Iterator j = aseguradora.obterCalculoMeicos(agenda).iterator(); j.hasNext();) 
				{
					MeicosCalculo calculo = (MeicosCalculo) j.next();

					if (calculo.obterTipo().equals("Ratios Financeiros"))
						block1.add(new Label(calculo.obterValorIndicador()));

					if (calculo.obterTipo().equals("Margen Solvencia"))
						block2.add(new Label(calculo.obterValorIndicador()));

					if (calculo.obterTipo().equals("In-Situ"))
						block3.add(new Label(calculo.obterValorIndicador()));

					if (calculo.obterTipo().equals("Otros Indicadores"))
						block4.add(new Label(calculo.obterValorIndicador()));

				}
				
				this.setNextHAlign(super.HALIGN_CENTER);
				this.add(block1);
				this.setNextHAlign(super.HALIGN_CENTER);
				this.add(block2);
				this.setNextHAlign(super.HALIGN_CENTER);
				this.add(block3);
				this.setNextHAlign(super.HALIGN_CENTER);
				this.add(block4);
				this.setNextHAlign(super.HALIGN_CENTER);
				this.add(new Label(agenda.obterValorQualificacao(aseguradora)));
			}
		}
	}
}