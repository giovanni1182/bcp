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

public class ReasegurosReaseguradoraView extends BasicView {
	private Entidade entidade;

	private Date dataInicio;

	private Date dataFim;

	private boolean listar;

	private Map<String, String> reaseguradoras;

	public ReasegurosReaseguradoraView(Entidade entidade, Date dataInicio,Date dataFim, boolean listar, Map<String, String> reaseguradoras) throws Exception 
	{
		this.entidade = entidade;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.listar = listar;
		this.reaseguradoras = reaseguradoras;
	}

	public View execute(User user, Locale locale, Properties properties)throws Exception 
	{
		Table mainTable = new Table(1);
		mainTable.setNextWidth("100%");
		mainTable.addSubtitle("Producción - Reaseguradora - Reaseguros");
		
		Table table = new Table(1);

		Block block = new Block(Block.HORIZONTAL);

		Label label1 = new Label("Reaseguradora:");
		label1.setBold(true);

		block.add(label1);
		block.add(new Space(2));
		block.add(new EntidadePopup("reaseguradoraId", "reaseguradoraNome",
				this.entidade, "Aseguradora,Reaseguradora", true));

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

		Button button = new Button("Listar", new Action("visualizarReasegurosReaseguradora"));
		button.getAction().add("listar", true);

		//Button button2 = new Button("Volver", new Action("visualizarInspecaoSitu"));
		
		Button button3 = new Button("Generar Excel", new Action("visualizarReasegurosReaseguradora"));
		button3.getAction().add("listar", true);
		button3.getAction().add("excel", true);

		table.addFooter(button);
		//table.addFooter(button2);
		table.addFooter(button3);

		mainTable.add(table);

		if (this.listar) 
		{
			CRMModelManager mm = new CRMModelManager(user);
			EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
			
			Table table2 = new Table(5);
			table2.addSubtitle("");

			//table2.addSubtitle(this.aseguradoras.size() + " Aseguradora(s)");

			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("Aseguradora");
			table2.addHeader("Tipo de Contrato");
			table2.addHeader("Monto Cap. Reaseg.");
			table2.addHeader("Monto Prima Reaseg.");
			table2.addHeader("Monto Comisión");

			for (Iterator i = reaseguradoras.values().iterator(); i.hasNext();) 
			{
				String chave = (String) i.next();
				
				String[] st = chave.split("_");
				
				long id = Long.parseLong(st[0]);
				String tipoContrato = st[1];
				double capitalGs = Double.parseDouble(st[2]);
				double primaGs = Double.parseDouble(st[3]);
				double comissaoGs = Double.parseDouble(st[4]);
				
				Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);

				/*double capitalGs = aseguradora.obterCapitalGs(this.entidade,this.dataInicio, this.dataFim, tipoContrato);
				double primaGs = aseguradora.obterPrimaGs(this.entidade,this.dataInicio, this.dataFim, tipoContrato);
				double comissaoGs = aseguradora.obterComissaoGs(this.entidade,this.dataInicio, this.dataFim, tipoContrato);*/

				if (capitalGs > 0 || primaGs > 0 || comissaoGs > 0) 
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link(tipoContrato, new Action("visualizarApolicesPorAseguradora"));
					link2.getAction().add("tipoContrato", tipoContrato);
					link2.getAction().add("aseguradoraId",aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs, "#,##0.00"));
					table2.add(new Label(primaGs, "#,##0.00"));
					table2.add(new Label(comissaoGs, "#,##0.00"));
				}

				/*double capitalGs2 = aseguradora.obterCapitalGs(this.entidade,this.dataInicio, this.dataFim, "Excedente");
				double primaGs2 = aseguradora.obterPrimaGs(this.entidade,this.dataInicio, this.dataFim, "Excedente");
				double comissaoGs2 = aseguradora.obterComissaoGs(this.entidade,this.dataInicio, this.dataFim, "Excedente");

				if (capitalGs2 > 0 || primaGs2 > 0 || comissaoGs2 > 0 && !aseguradorasVistas.contains(aseguradora.obterNome() + "Excedente")) 
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link("Excedente", new Action("visualizarApolicesPorAseguradora"));
					link2.getAction().add("tipoContrato", "Excedente");
					link2.getAction().add("aseguradoraId",aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs2, "#,##0.00"));
					table2.add(new Label(primaGs2, "#,##0.00"));
					table2.add(new Label(comissaoGs2, "#,##0.00"));
					
					aseguradorasVistas.add(aseguradora.obterNome() + "Excedente");
				}

				double capitalGs3 = aseguradora.obterCapitalGs(this.entidade,this.dataInicio, this.dataFim, "Exceso de pérdida");
				double primaGs3 = aseguradora.obterPrimaGs(this.entidade,this.dataInicio, this.dataFim, "Exceso de pérdida");
				double comissaoGs3 = aseguradora.obterComissaoGs(this.entidade,	this.dataInicio, this.dataFim, "Exceso de pérdida");

				if (capitalGs3 > 0 || primaGs3 > 0 || comissaoGs3 > 0 && !aseguradorasVistas.contains(aseguradora.obterNome() + "Exceso de pérdida")) 
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link("Exceso de pérdida", new Action("visualizarApolicesPorAseguradora"));
					link2.getAction().add("tipoContrato", "Exceso de pérdida");
					link2.getAction().add("aseguradoraId",aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs3, "#,##0.00"));
					table2.add(new Label(primaGs3, "#,##0.00"));
					table2.add(new Label(comissaoGs3, "#,##0.00"));
					
					aseguradorasVistas.add(aseguradora.obterNome() + "Exceso de pérdida");
				}

				double capitalGs4 = aseguradora.obterCapitalGs(this.entidade,this.dataInicio, this.dataFim,
						"Facultativo no Proporcional");
				double primaGs4 = aseguradora.obterPrimaGs(this.entidade,this.dataInicio, this.dataFim,
						"Facultativo no Proporcional");
				double comissaoGs4 = aseguradora.obterComissaoGs(this.entidade,	this.dataInicio, this.dataFim,
						"Facultativo no Proporcional");

				if (capitalGs4 > 0 || primaGs4 > 0 || comissaoGs4 > 0 && !aseguradorasVistas.contains(aseguradora.obterNome() + "Facultativo no Proporcional"))
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link("Facultativo no Proporcional",new Action("visualizarApolicesPorAseguradora"));
					link2.getAction().add("tipoContrato","Facultativo no Proporcional");
					link2.getAction().add("aseguradoraId",aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs4, "#,##0.00"));
					table2.add(new Label(primaGs4, "#,##0.00"));
					table2.add(new Label(comissaoGs4, "#,##0.00"));
					
					aseguradorasVistas.add(aseguradora.obterNome() + "Facultativo no Proporcional");
				}

				double capitalGs5 = aseguradora.obterCapitalGs(this.entidade,this.dataInicio, this.dataFim,
						"Facultativo Proporcional");
				double primaGs5 = aseguradora.obterPrimaGs(this.entidade,this.dataInicio, this.dataFim,
						"Facultativo Proporcional");
				double comissaoGs5 = aseguradora.obterComissaoGs(this.entidade,this.dataInicio, this.dataFim,
						"Facultativo Proporcional");

				if (capitalGs5 > 0 || primaGs5 > 0 || comissaoGs5 > 0 && !aseguradorasVistas.contains(aseguradora.obterNome() + "Facultativo Proporcional")) 
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link("Facultativo Proporcional",new Action("visualizarApolicesPorAseguradora"));
					link2.getAction().add("tipoContrato","Facultativo Proporcional");
					link2.getAction().add("aseguradoraId",aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs5, "#,##0.00"));
					table2.add(new Label(primaGs5, "#,##0.00"));
					table2.add(new Label(comissaoGs5, "#,##0.00"));
					
					aseguradorasVistas.add(aseguradora.obterNome() + "Facultativo Proporcional");
				}

				double capitalGs6 = aseguradora.obterCapitalGs(this.entidade,this.dataInicio, this.dataFim,
						"Limitación de Siniestralidad");
				double primaGs6 = aseguradora.obterPrimaGs(this.entidade,this.dataInicio, this.dataFim,
						"Limitación de Siniestralidad");
				double comissaoGs6 = aseguradora.obterComissaoGs(this.entidade,this.dataInicio, this.dataFim,
						"Limitación de Siniestralidad");

				if (capitalGs6 > 0 || primaGs6 > 0 || comissaoGs6 > 0 && !aseguradorasVistas.contains(aseguradora.obterNome() + "Limitación de Siniestralidad")) 
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link("Limitación de Siniestralidad",new Action("visualizarApolicesPorAseguradora"));
					link2.getAction().add("tipoContrato","Limitación de Siniestralidad");
					link2.getAction().add("aseguradoraId",aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs6, "#,##0.00"));
					table2.add(new Label(primaGs6, "#,##0.00"));
					table2.add(new Label(comissaoGs6, "#,##0.00"));
					
					aseguradorasVistas.add(aseguradora.obterNome() + "Limitación de Siniestralidad");
				}*/
			}

			mainTable.add(table2);
			//table.add(table2);
		}

		Border border = new Border(mainTable);
		
		//this.aseguradoras.clear();

		return border;
	}
}