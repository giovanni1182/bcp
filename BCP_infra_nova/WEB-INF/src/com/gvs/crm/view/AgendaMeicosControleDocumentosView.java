package com.gvs.crm.view;

import java.util.Iterator;

import com.gvs.crm.model.AgendaMeicos;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputInteger;
import infra.view.InputString;
import infra.view.Table;

public class AgendaMeicosControleDocumentosView extends Table {

	public AgendaMeicosControleDocumentosView(AgendaMeicos agenda, Parametro parametro) throws Exception
	{
		super(1);

		if (parametro.obterControleDocumentos().size() > 0)
		{
			Table table2 = new Table(3);

			table2.addSubtitle("Documentos");
			table2.addStyle(Table.STYLE_ALTERNATE);

			table2.addHeader("Nº");
			table2.addHeader("Documento");
			table2.addHeader("Fecha a Presentar");

			for (Iterator i = parametro.obterControleDocumentos().values().iterator(); i.hasNext();)
			{
				Parametro.ControleDocumento controle = (Parametro.ControleDocumento) i.next();

				InputInteger inputInteger = new InputInteger("seq", controle.obterSequencial(), 4);
				inputInteger.setEnabled(false);

				InputString inputString = new InputString("descricao"+ controle.obterSequencial(),controle.obterDescricao(), 80);
				inputString.setEnabled(false);

				table2.add(inputInteger);
				table2.add(inputString);
				table2.add(new InputDate("dataLimite"+ controle.obterSequencial(), controle.obterDataLimite()));
			}

			this.setNextColSpan(this.getColumns());
			this.add(table2);

			Button atualizarButton = new Button("Actualizar", new Action("atualizarControleDocumento"));
			atualizarButton.getAction().add("id", parametro.obterId());
			atualizarButton.getAction().add("agendaId", agenda.obterId());

			this.addFooter(atualizarButton);
		}
	}
}