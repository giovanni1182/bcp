package com.gvs.crm.view;

import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;

public class AuditorExternoTecnologiaView extends Table {
	public AuditorExternoTecnologiaView(AuditorExterno auditor)
			throws Exception {
		super(1);

		this.addSubtitle("Tecnologia y Seguridad");

		Block block = new Block(Block.HORIZONTAL);

		Label label = new Label("Hardware:");
		label.setBold(true);
		block.add(label);

		Entidade.Atributo hardware = (Entidade.Atributo) auditor
				.obterAtributo("hardware");

		if (hardware != null)
			block.add(new InputString("atributo_hardware", hardware
					.obterValor(), 8));
		else
			block.add(new InputString("atributo_hardware", null, 8));

		this.add(block);

		Entidade.Atributo hardwareDescricao = (Entidade.Atributo) auditor
				.obterAtributo("hadwaredescricao");

		this.setNextColSpan(this.getColumns());

		if (hardwareDescricao != null)
			this.add(new InputText("atributo_hadwaredescricao",
					hardwareDescricao.obterValor(), 10, 90));
		else
			this.add(new InputText("atributo_hadwaredescricao", null, 10, 90));

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Block block2 = new Block(Block.HORIZONTAL);

		Label label2 = new Label("Software:");
		label2.setBold(true);
		block2.add(label2);

		Entidade.Atributo software = (Entidade.Atributo) auditor
				.obterAtributo("software");

		if (software != null)
			block2.add(new InputString("atributo_software", software
					.obterValor(), 8));
		else
			block2.add(new InputString("atributo_software", null, 8));

		this.add(block2);

		Entidade.Atributo softwareDescricao = (Entidade.Atributo) auditor
				.obterAtributo("softwaredescricao");

		this.setNextColSpan(this.getColumns());

		if (softwareDescricao != null)
			this.add(new InputText("atributo_softwaredescricao",
					softwareDescricao.obterValor(), 10, 90));
		else
			this.add(new InputText("atributo_softwaredescricao", null, 10, 90));

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Block block3 = new Block(Block.HORIZONTAL);

		Label label3 = new Label("Software Auditoria:");
		label3.setBold(true);
		block3.add(label3);

		Entidade.Atributo softwareAuditoria = (Entidade.Atributo) auditor
				.obterAtributo("softwareauditoria");

		Select select = new Select("atributo_softwareauditoria", 1);
		select.add("", "", false);

		if (softwareAuditoria != null) {
			select.add("Si", "Si", "Si".equals(softwareAuditoria.obterValor()));
			select.add("No", "No", "No".equals(softwareAuditoria.obterValor()));
		} else {
			select.add("Si", "Si", false);
			select.add("No", "No", false);
		}

		block3.add(select);

		this.add(block3);

		Entidade.Atributo softwareAuditoriaDescricao = (Entidade.Atributo) auditor
				.obterAtributo("softwareauditoriadescricao");

		this.setNextColSpan(this.getColumns());

		if (softwareAuditoriaDescricao != null)
			this.add(new InputText("atributo_softwareauditoriadescricao",
					softwareAuditoriaDescricao.obterValor(), 10, 90));
		else
			this.add(new InputText("atributo_softwareauditoriadescricao", null,
					10, 90));

		this.setNextColSpan(this.getColumns());
		this.add(new Space());

		Block block4 = new Block(Block.HORIZONTAL);

		Label label4 = new Label("Aplicativo de Contingencia:");
		label4.setBold(true);
		block4.add(label4);

		Entidade.Atributo aplicativo = (Entidade.Atributo) auditor
				.obterAtributo("aplicativo");

		Select select2 = new Select("atributo_aplicativo", 1);
		select2.add("", "", false);

		if (aplicativo != null) {
			select2.add("Si", "Si", "Si".equals(aplicativo.obterValor()));
			select2.add("No", "No", "No".equals(aplicativo.obterValor()));
		} else {
			select2.add("Si", "Si", false);
			select2.add("No", "No", false);
		}

		block4.add(select2);

		this.add(block4);

		Entidade.Atributo aplicativoDescricao = (Entidade.Atributo) auditor
				.obterAtributo("aplicativodescricao");

		this.setNextColSpan(this.getColumns());

		if (softwareAuditoriaDescricao != null)
			this.add(new InputText("atributo_aplicativodescricao",
					aplicativoDescricao.obterValor(), 10, 90));
		else
			this
					.add(new InputText("atributo_aplicativodescricao", null,
							10, 90));

		Button atualizarButton = new Button("Actualizar", new Action(
				"atualizarTecnologia"));
		atualizarButton.getAction().add("id", auditor.obterId());

		this.addFooter(atualizarButton);
	}
}