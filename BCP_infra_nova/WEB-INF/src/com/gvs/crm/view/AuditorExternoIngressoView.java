package com.gvs.crm.view;

import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Button;
import infra.view.InputText;
import infra.view.Table;

public class AuditorExternoIngressoView extends Table {
	public AuditorExternoIngressoView(AuditorExterno auditor) throws Exception {
		super(1);

		this.addSubtitle("Ingreso");

		Entidade.Atributo ingresso = (Entidade.Atributo) auditor
				.obterAtributo("ingresso");

		if (ingresso != null)
			this.add(new InputText("atributo_ingresso", ingresso.obterValor(),
					9, 80));
		else
			this.add(new InputText("atributo_ingresso", null, 9, 80));

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarIngresso"));
		atualizarButton.getAction().add("id", auditor.obterId());
		atualizarButton.setEnabled(auditor.permiteAtualizar());

		this.addFooter(atualizarButton);
	}
}