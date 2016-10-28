package com.gvs.crm.view;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class ReasegurosView extends BasicView {

	private Aseguradora aseguradora;

	private Date dataInicio;

	private Date dataFim;

	private boolean listar;

	private Map reaseguradoras;

	public ReasegurosView(Aseguradora aseguradora, Date dataInicio,Date dataFim, boolean listar, Map reaseguros) throws Exception
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.listar = listar;
		this.reaseguradoras = reaseguros;
	}

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);

		table.setNextWidth("100%");

		table.addSubtitle("Producción - Aseguradora - Reaseguros");

		Block block = new Block(Block.HORIZONTAL);

		Label label1 = new Label("Aseguradora:");
		label1.setBold(true);

		block.add(label1);
		block.add(new Space(2));
		block.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
				this.aseguradora, "Aseguradora", true));

		block.add(new Space(5));

		Label label2 = new Label("Periodo:");
		label2.setBold(true);

		block.add(label2);
		block.add(new Space(2));
		block.add(new InputDate("dataInicio", this.dataInicio));

		block.add(new Space(2));

		Label label3 = new Label("hasta el:");
		label3.setBold(true);

		block.add(label3);
		block.add(new Space(2));
		block.add(new InputDate("dataFim", this.dataFim));

		table.add(block);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		Block block2 = new Block(Block.HORIZONTAL);

		Button button = new Button("Listar", new Action("visualizarReaseguros"));
		button.getAction().add("listar", true);
		
		Button button3 = new Button("Generar Excel", new Action("visualizarReaseguros"));
		button3.getAction().add("listar", true);
		button3.getAction().add("excel", true);

		Button button2 = new Button("Volver", new Action("visualizarInspecaoSitu"));

		block2.add(button);
		block2.add(new Space(8));
		block2.add(button3);
		block2.add(new Space(8));
		block2.add(button2);

		table.setNextColSpan(table.getColumns());
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.add(block2);

		table.setNextColSpan(table.getColumns());
		table.add(new Space());

		if (this.listar)
		{
			CRMModelManager mm = new CRMModelManager(user);
			EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
			
			Table table2 = new Table(5);

			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.setNextWidth("55%");

			table2.addHeader("Reaseguradora");
			table2.addHeader("Tipo de Contrato");
			table2.addHeader("Monto Cap. Reaseg.");
			table2.addHeader("Monto Prima Reaseg.");
			table2.addHeader("Monto Comisión");

			for (Iterator i = this.reaseguradoras.values().iterator(); i.hasNext();) 
			{
				String chave = (String) i.next();
				
				String[] st = chave.split("_");
				
				long id = Long.parseLong(st[0]);
				String tipoContrato = st[1];
				double capitalGs = Double.parseDouble(st[2]);
				double primaGs = Double.parseDouble(st[3]);
				double comissaoGs = Double.parseDouble(st[4]);
				
				Entidade reaseguradora = home.obterEntidadePorId(id);

				/*double capitalGs = reaseguradora.obterCapitalGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,tipoContrato);
				double primaGs = reaseguradora.obterPrimaGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,tipoContrato);
				double comissaoGs = reaseguradora.obterComissaoGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,tipoContrato);*/

				if (capitalGs > 0 || primaGs > 0 || comissaoGs > 0) 
				{
					table2.add(reaseguradora.obterNome());

					Link link2 = new Link(tipoContrato, new Action("visualizarApolicesPorReaseguradora"));
					link2.getAction().add("tipoContrato", tipoContrato);
					link2.getAction().add("reaseguradoraId",reaseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs, "#,##0.00"));
					table2.add(new Label(primaGs, "#,##0.00"));
					table2.add(new Label(comissaoGs, "#,##0.00"));
				}

				/*double capitalGs2 = reaseguradora.obterCapitalGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,"Excedente");
				double primaGs2 = reaseguradora.obterPrimaGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,"Excedente");
				double comissaoGs2 = reaseguradora.obterComissaoGsReaseguradora(this.aseguradora,this.dataInicio, this.dataFim, "Excedente");

				if (capitalGs2 > 0 || primaGs2 > 0 || comissaoGs2 > 0) 
				{
					table2.add(reaseguradora.obterNome());

					Link link2 = new Link("Excedente", new Action("visualizarApolicesPorReaseguradora"));
					link2.getAction().add("tipoContrato", "Excedente");
					link2.getAction().add("reaseguradoraId",reaseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs2, "#,##0.00"));
					table2.add(new Label(primaGs2, "#,##0.00"));
					table2.add(new Label(comissaoGs2, "#,##0.00"));
				}

				double capitalGs3 = reaseguradora.obterCapitalGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,"Exceso de pérdida");
				double primaGs3 = reaseguradora.obterPrimaGsReaseguradora(this.aseguradora, this.dataInicio, this.dataFim,"Exceso de pérdida");
				double comissaoGs3 = reaseguradora.obterComissaoGsReaseguradora(this.aseguradora,this.dataInicio, this.dataFim,"Exceso de pérdida");

				if (capitalGs3 > 0 || primaGs3 > 0 || comissaoGs3 > 0) 
				{
					table2.add(reaseguradora.obterNome());

					Link link2 = new Link("Exceso de pérdida", new Action(
							"visualizarApolicesPorReaseguradora"));
					link2.getAction().add("tipoContrato", "Exceso de pérdida");
					link2.getAction().add("reaseguradoraId",
							reaseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs3, "#,##0.00"));
					table2.add(new Label(primaGs3, "#,##0.00"));
					table2.add(new Label(comissaoGs3, "#,##0.00"));
				}

				double capitalGs4 = reaseguradora.obterCapitalGsReaseguradora(
						this.aseguradora, this.dataInicio, this.dataFim,
						"Facultativo no Proporcional");
				double primaGs4 = reaseguradora.obterPrimaGsReaseguradora(
						this.aseguradora, this.dataInicio, this.dataFim,
						"Facultativo no Proporcional");
				double comissaoGs4 = reaseguradora
						.obterComissaoGsReaseguradora(this.aseguradora,
								this.dataInicio, this.dataFim,
								"Facultativo no Proporcional");

				if (capitalGs4 > 0 || primaGs4 > 0 || comissaoGs4 > 0) {
					table2.add(reaseguradora.obterNome());

					Link link2 = new Link("Facultativo no Proporcional",
							new Action("visualizarApolicesPorReaseguradora"));
					link2.getAction().add("tipoContrato",
							"Facultativo no Proporcional");
					link2.getAction().add("reaseguradoraId",
							reaseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs4, "#,##0.00"));
					table2.add(new Label(primaGs4, "#,##0.00"));
					table2.add(new Label(comissaoGs4, "#,##0.00"));
				}

				double capitalGs5 = reaseguradora.obterCapitalGsReaseguradora(
						this.aseguradora, this.dataInicio, this.dataFim,
						"Facultativo Proporcional");
				double primaGs5 = reaseguradora.obterPrimaGsReaseguradora(
						this.aseguradora, this.dataInicio, this.dataFim,
						"Facultativo Proporcional");
				double comissaoGs5 = reaseguradora
						.obterComissaoGsReaseguradora(this.aseguradora,
								this.dataInicio, this.dataFim,
								"Facultativo Proporcional");

				if (capitalGs5 > 0 || primaGs5 > 0 || comissaoGs5 > 0) {
					table2.add(reaseguradora.obterNome());

					Link link2 = new Link("Facultativo Proporcional",
							new Action("visualizarApolicesPorReaseguradora"));
					link2.getAction().add("tipoContrato",
							"Facultativo Proporcional");
					link2.getAction().add("reaseguradoraId",
							reaseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs5, "#,##0.00"));
					table2.add(new Label(primaGs5, "#,##0.00"));
					table2.add(new Label(comissaoGs5, "#,##0.00"));
				}

				double capitalGs6 = reaseguradora.obterCapitalGsReaseguradora(
						this.aseguradora, this.dataInicio, this.dataFim,
						"Limitación de Siniestralidad");
				double primaGs6 = reaseguradora.obterPrimaGsReaseguradora(
						this.aseguradora, this.dataInicio, this.dataFim,
						"Limitación de Siniestralidad");
				double comissaoGs6 = reaseguradora
						.obterComissaoGsReaseguradora(this.aseguradora,
								this.dataInicio, this.dataFim,
								"Limitación de Siniestralidad");

				if (capitalGs6 > 0 || primaGs6 > 0 || comissaoGs6 > 0) {
					table2.add(reaseguradora.obterNome());

					Link link2 = new Link("Limitación de Siniestralidad",
							new Action("visualizarApolicesPorReaseguradora"));
					link2.getAction().add("tipoContrato",
							"Limitación de Siniestralidad");
					link2.getAction().add("reaseguradoraId",
							reaseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs6, "#,##0.00"));
					table2.add(new Label(primaGs6, "#,##0.00"));
					table2.add(new Label(comissaoGs6, "#,##0.00"));
				}*/
			}

			table.add(table2);
		}
		
		this.reaseguradoras.clear();

		Border border = new Border(table);

		return border;
	}
}