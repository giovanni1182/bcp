package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Button;
import infra.view.Table;
import infra.view.View;

public class ApolicesPorAseguradoraView extends BasicView {

	private Collection apolices;

	private String tipoContrato;

	private Date dataInicio;

	private Date dataFim;

	private Entidade reaseguradora;

	public ApolicesPorAseguradoraView(Entidade reaseguradora,
			Collection apolices, String tipoContrato, Date dataInicio,
			Date dataFim) throws Exception {
		this.reaseguradora = reaseguradora;
		this.apolices = apolices;
		this.tipoContrato = tipoContrato;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
	}

	public View execute(User arg0, Locale arg1, Properties arg2)
			throws Exception {
		Table table = new Table(8);
		table.setWidth("100%");

		String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy")
				.format(this.dataInicio);
		String dataFimStr = new SimpleDateFormat("dd/MM/yyyy")
				.format(this.dataFim);

		table.addSubtitle(this.apolices.size() + " Instrumentos - "
				+ this.tipoContrato + " - " + dataInicioStr + " hasta "
				+ dataFimStr);

		table.addStyle(Table.STYLE_ALTERNATE);

		table.addHeader("Vigencia");
		table.addHeader("Numero");
		table.addHeader("Sección");
		table.addHeader("Plan");
		table.addHeader("Asegurado");
		table.addHeader("Tipo");
		table.addHeader("Aseguradora");
		table.addHeader("Situación");

		for (Iterator i = this.apolices.iterator(); i.hasNext();) {
			Apolice apolice = (Apolice) i.next();

			String dataInicioVigencia = new SimpleDateFormat("dd/MM/yyyy")
					.format(apolice.obterDataPrevistaInicio());
			String dataFimVigencia = new SimpleDateFormat("dd/MM/yyyy")
					.format(apolice.obterDataPrevistaConclusao());

			table.add(dataInicioVigencia + " - " + dataFimVigencia);
			table.add(apolice.obterNumeroApolice());
			table.add(apolice.obterSecao().obterApelido());
			table.add(apolice.obterPlano().obterPlano());
			table.add(apolice.obterNomeAsegurado());
			table.add(apolice.obterTipo());
			table.add(apolice.obterOrigem().obterNome());
			table.add(apolice.obterSituacaoSeguro());
		}

		Button voltarButton = new Button("Volver", new Action(
				"visualizarApolicesPorReaseguradora"));
		voltarButton.getAction().add("reaseguradoraId",
				this.reaseguradora.obterId());
		voltarButton.getAction().add("dataInicio", this.dataInicio);
		voltarButton.getAction().add("dataFim", this.dataFim);
		voltarButton.getAction().add("listar", true);

		table.addFooter(voltarButton);

		Border border = new Border(table);
		
		this.apolices.clear();

		return border;
	}

}