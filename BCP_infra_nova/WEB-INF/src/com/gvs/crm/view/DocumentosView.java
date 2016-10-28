package com.gvs.crm.view;

import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Processo;

import infra.control.Action;
import infra.view.Button;
import infra.view.InputFile;
import infra.view.Table;

public class DocumentosView extends Table {
	public DocumentosView(Evento evento) throws Exception {
		super(2);

		Processo processo = (Processo) evento;

		this.setNextColSpan(this.getColumns());
		this.add(new AnexosView(processo));

		this.addHeader("Anexar:");
		this.addData(new InputFile("file", ""));

		Action action = new Action("upload");
		action.add("idEvento", processo.obterId());
		this.addFooter(new Button("Upload", action));
	}
}