package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Iterator;

import com.gvs.crm.component.IndicadoresSelect;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.MeicosAseguradora;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.Check;
import infra.view.Image;
import infra.view.InputDate;
import infra.view.InputInteger;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;

public class AseguradoraMeicosView extends Table {
	public AseguradoraMeicosView(Aseguradora aseguradora, String tipo,
			MeicosAseguradora meicos) throws Exception {
		super(1);

		this.addSubtitle("Meicos");

		this.setWidth("50%");

		Block block = new Block(Block.HORIZONTAL);
		block.add(new IndicadoresSelect("_tipo", tipo));
		block.add(new Space(2));

		Link link = new Link(new Image("visualizar.GIF"), new Action(
				"visualizarDetalhesEntidade"));
		link.getAction().add("id", aseguradora.obterId());
		link.getAction().add("_pastaAseguradora", 10);
		link.getAction().add("_meicos", "False");

		block.add(link);

		this.add(block);

		if (aseguradora.obterMeicos(tipo).size() > 0) {
			Table meicosTable = new Table(2);

			meicosTable.addSubtitle(tipo);
			meicosTable.addStyle(Table.STYLE_ALTERNATE);
			meicosTable.setWidth("100%");

			meicosTable.addHeader("Fecha de Calculo");
			meicosTable.addHeader("Fase");

			for (Iterator i = aseguradora.obterMeicos(tipo).iterator(); i.hasNext();) 
			{
				MeicosAseguradora meicos2 = (MeicosAseguradora) i.next();

				String data = new SimpleDateFormat("dd/MM/yyyy").format(meicos2.obterDataPrevistaInicio());

				Link link2 = new Link(data, new Action("visualizarDetalhesEntidade"));
				link2.getAction().add("_meicosId", meicos2.obterId());
				link2.getAction().add("_pastaAseguradora", 10);
				link2.getAction().add("id", aseguradora.obterId());
				link2.getAction().add("_meicos", "True");

				meicosTable.add(link2);

				meicosTable.add(meicos2.obterFase().obterNome());
			}

			this.add(meicosTable);
		} else {
			this.setNextColSpan(this.getColumns());
			this.setNextHAlign(super.HALIGN_CENTER);
			this.addHeader("Ningún meicos encontrado");
		}

		if (meicos != null) 
		{
			this.add(new Space());

			if (!meicos.obterTipo().equals("Controle de Documentos"))
			{
				Table table2 = new Table(5);

				String data = new SimpleDateFormat("dd/MM/yyyy").format(meicos
						.obterDataPrevistaInicio());

				table2.addSubtitle("Controle Documentação - Fecha de Cálculo: "
						+ data);
				table2.addStyle(Table.STYLE_ALTERNATE);
				table2.setWidth("100%");

				table2.addHeader("");
				table2.addHeader("");
				table2.addHeader("Descripición");
				table2.addHeader("Peso");
				table2.addHeader("Cumplido");

				for (Iterator i = meicos.obterIndicadores().values().iterator(); i
						.hasNext();) {
					MeicosAseguradora.Indicador indicador = (MeicosAseguradora.Indicador) i
							.next();

					InputInteger input = new InputInteger("seq", indicador
							.obterSequencial(), 5);
					input.setEnabled(false);
					input.setVisible(false);

					table2.add(input);
					table2.add(new Check("marcado"+ indicador.obterSequencial(), "Sim", indicador.estaMarcado()));
					table2.add(indicador.obterDescricao());
					table2.add(new Label(indicador.obterPeso()));

					if (indicador.estaMarcado())
						table2.add("Sí");
					else
						table2.add("No");
				}

				this.add(table2);
			}
			else 
			{
				Table table2 = new Table(5);

				String data = new SimpleDateFormat("dd/MM/yyyy").format(meicos
						.obterDataPrevistaInicio());

				table2.addSubtitle(meicos.obterTipo() + " - Fecha de Cálculo: "
						+ data);
				table2.addStyle(Table.STYLE_ALTERNATE);
				table2.setWidth("100%");

				table2.addHeader("");
				table2.addHeader("Fecha Presentación");
				table2.addHeader("Fecha a Presentar");
				table2.addHeader("Descripición");
				table2.addHeader("Cumplido");

				for (Iterator i = meicos.obterDocumentos().values().iterator(); i.hasNext();) 
				{
					MeicosAseguradora.ControleDocumento documento = (MeicosAseguradora.ControleDocumento) i.next();

					InputInteger input = new InputInteger("seq", documento.obterSequencial(), 5);
					input.setEnabled(false);
					input.setVisible(false);

					table2.add(input);

					String dataLimite = new SimpleDateFormat("dd/MM/yyyy")
							.format(documento.obterDataLimite());

					table2.add(new InputDate("dataEntrega"+ documento.obterSequencial(), documento.obterDataEntrega()));
					table2.add(dataLimite);
					table2.add(documento.obterDescricao());

					if (documento.obterDataEntrega() != null) 
					{
						if (documento.obterDataEntrega().before(documento.obterDataLimite())|| documento.obterDataEntrega().equals(documento.obterDataLimite()))
							table2.add("Sí");
						else
							table2.add("No");
					} 
					else
						table2.add("No");
				}

				this.add(table2);
			}

			Button atualizarButton = new Button("Actualizar", new Action("atualizarMeicos"));
			atualizarButton.getAction().add("meicosId", meicos.obterId());
			atualizarButton.getAction().add("id", aseguradora.obterId());
			atualizarButton.setEnabled(aseguradora.permiteAtualizar());

			this.addFooter(atualizarButton);

		}
	}
}