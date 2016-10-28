package com.gvs.crm.view;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

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

public class CorretorReasegurosAseguradoraView extends BasicView {
	private Entidade corretora;

	private Date dataInicio;

	private Date dataFim;

	private boolean listar;

	private Map aseguradoras;

	public CorretorReasegurosAseguradoraView(Entidade corretora, Date dataInicio, Date dataFim, boolean listar, Map aseguradoras) throws Exception
	{
		this.corretora = corretora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.listar = listar;
		this.aseguradoras = aseguradoras;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		Table table = new Table(1);

		table.setNextWidth("100%");

		table.addSubtitle("Corredora de Reaseguros - Aseguradoras");

		Block block = new Block(Block.HORIZONTAL);

		Label label1 = new Label("Corredora de Reaseguros:");
		label1.setBold(true);

		block.add(label1);
		block.add(new Space(2));
		block.add(new EntidadePopup("corretorId", "corretorNome",
				this.corretora, "Aseguradora,Corretora,Reaseguradora", true));

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

		Button button = new Button("Listar", new Action("visualizarReasegurosCorredorAseguradora"));
		button.getAction().add("listar", true);
		
		Button button3 = new Button("Generar Excel", new Action("visualizarReasegurosCorredorAseguradora"));
		button3.getAction().add("listar", true);
		button3.getAction().add("excel", true);

		Button button2 = new Button("Volver", new Action("visualizarPaginaInicial"));

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

			//table2.addSubtitle(this.aseguradoras.size() + " Aseguradora(s)");

			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.setNextWidth("55%");

			table2.addHeader("Aseguradora");
			table2.addHeader("Tipo de Contrato");
			table2.addHeader("Monto Cap. Reaseg.");
			table2.addHeader("Monto Prima Reaseg.");
			table2.addHeader("Monto Comisión");
			
			for (Iterator i = this.aseguradoras.values().iterator(); i.hasNext();) 
			{
				String chave = (String) i.next();
				
				StringTokenizer st = new StringTokenizer(chave,"_");
				
				long id = Long.parseLong(st.nextToken());
				String tipoContrato = st.nextToken();
				
				Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(id);

				double capitalGs = aseguradora.obterCapitalGsCorretora(this.corretora, this.dataInicio, this.dataFim,tipoContrato);
				double primaGs = aseguradora.obterPrimaGsCorretora(this.corretora, this.dataInicio, this.dataFim,tipoContrato);
				double comissaoGs = aseguradora.obterComissaoGsCorretora(this.corretora, this.dataInicio, this.dataFim,tipoContrato);

				if (capitalGs > 0 || primaGs > 0 || comissaoGs > 0)
				{
					table2.add(aseguradora.obterNome());

					Link link2 = new Link(tipoContrato, new Action("visualizarApolicesPorCorretorAseguradora"));
					link2.getAction().add("tipoContrato", tipoContrato);
					link2.getAction().add("aseguradoraId", aseguradora.obterId());

					table2.add(link2);
					table2.add(new Label(capitalGs, "#,##0.00"));
					table2.add(new Label(primaGs, "#,##0.00"));
					table2.add(new Label(comissaoGs, "#,##0.00"));
				}
			}
			
			table.add(table2);
		}

		Border border = new Border(table);
		
		this.aseguradoras.clear();

		return border;
	}

}