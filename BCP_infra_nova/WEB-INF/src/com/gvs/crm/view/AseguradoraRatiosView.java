package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.RatioPermanente;
import com.gvs.crm.model.RatioTresAnos;
import com.gvs.crm.model.RatioUmAno;

import infra.control.Action;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AseguradoraRatiosView extends Table 
{
	public AseguradoraRatiosView(Aseguradora aseguradora) throws Exception 
	{
		super(1);

		Table table3 = new Table(1);
		table3.addSubtitle("Ratios Financeiros Permanentes");
		table3.addStyle(Table.STYLE_ALTERNATE);

		if (aseguradora.obterRatiosPermanentes().size() == 0)
			table3.addHeader("Ningúm Ratio Financeiro Permanente");
		else 
		{
			for (Iterator i = aseguradora.obterRatiosPermanentes().iterator(); i.hasNext();) 
			{
				RatioPermanente ratio = (RatioPermanente) i.next();

				String data = new SimpleDateFormat("dd/MM/yyyy").format(ratio.obterDataPrevistaInicio());

				Link link = new Link(ratio.obterTitulo() + " - Fecha Generación: " + data + " - " + ratio.obterFase().obterNome(),new Action("visualizarEvento"));
				link.getAction().add("id", ratio.obterId());

				table3.add(link);
			}
		}

		this.add(table3);

		Table table = new Table(1);
		table.addSubtitle("Ratios Financeiros de Un Año");
		table.addStyle(Table.STYLE_ALTERNATE);

		if (aseguradora.obterRatiosUmAno().size() == 0)
			table.addHeader("Ningúm Ratio Financeiro de Un Año");
		else 
		{
			for (Iterator i = aseguradora.obterRatiosUmAno().iterator(); i.hasNext();) 
			{
				RatioUmAno ratio = (RatioUmAno) i.next();

				String data = new SimpleDateFormat("dd/MM/yyyy").format(ratio
						.obterDataPrevistaInicio());

				Link link = new Link(ratio.obterTitulo() + " - Fecha Generación: " + data + " - " + ratio.obterFase().obterNome(),
						new Action("visualizarEvento"));
				link.getAction().add("id", ratio.obterId());

				table.add(link);
			}

			this.setNextColSpan(this.getColumns());
			this.add(new Space());
		}

		this.add(table);

		Table table2 = new Table(1);
		table2.addSubtitle("Ratios Financeiros de Tres Años");
		table2.addStyle(Table.STYLE_ALTERNATE);

		if (aseguradora.obterRatiosUmAno().size() == 0)
			table2.addHeader("Ningúm Ratio Financeiro de Tres Años");
		else 
		{
			for (Iterator i = aseguradora.obterRatiosTresAnos().iterator(); i.hasNext();) 
			{
				RatioTresAnos ratio = (RatioTresAnos) i.next();

				String data = new SimpleDateFormat("dd/MM/yyyy").format(ratio.obterDataPrevistaInicio());

				Link link = new Link(ratio.obterTitulo() + " - Fecha Generación: " + data + " - " + ratio.obterFase().obterNome(),new Action("visualizarEvento"));
				link.getAction().add("id", ratio.obterId());

				table2.add(link);
			}
		}

		this.add(table2);
	}
}