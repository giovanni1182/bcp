package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.OficialCumprimento;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Select;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class OficialCumprimentoView extends EntidadeAbstrataView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		OficialCumprimento oficial = (OficialCumprimento) this.obterEntidade();

		boolean incluir = oficial.obterId() == 0;

		Table table = new Table(2);
		table.setWidth("100%");

		if (!incluir) {
			table.addHeader("Creado en:");
			String data = new SimpleDateFormat("dd/MM/yyyy").format(oficial
					.obterCriacao());
			table.add(data);
		}

		table.addHeader("Superior:");

		if (incluir)
			table.add(new EntidadePopup("entidadeSuperiorId", "superiorNome",
					oficial.obterSuperior(), true));
		else
			table.add(new EntidadePopup("entidadeSuperiorId", "superiorNome",
					oficial.obterSuperior(), oficial.permiteAtualizar()));

		table.addHeader("Responsable:");
		if (incluir)
			table.add(new EntidadePopup("responsavelId", "responsavelNome",
					oficial.obterResponsavel(), "Usuario", true));
		else
			table.add(new EntidadePopup("responsavelId", "responsavelNome",
					oficial.obterResponsavel(), "Usuario", oficial
							.permiteAtualizar()));

		table.addHeader("Aseguradora:");
		if (incluir)
			table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
					oficial.obterAseguradora(), "Aseguradora", true));
		else
			table.add(new EntidadePopup("aseguradoraId", "aseguradoraNome",
					oficial.obterAseguradora(), "Aseguradora", oficial
							.permiteAtualizar()));

		table.addHeader("Nombre:");
		table.add(new InputString("nome", oficial.obterNome(), 60));

		Entidade.Atributo funcao = (Entidade.Atributo) oficial
				.obterAtributo("funcao");

		table.addHeader("Función en la Aseguradora:");
		Select select = new Select("atributo_funcao", 1);
		select.add("", "", false);
		if (funcao != null) {
			select.add("Gerente", "Gerente", "Gerente".equals(funcao
					.obterValor()));
			select.add("Director", "Director", "Director".equals(funcao
					.obterValor()));
			select.add("Auxiliar", "Auxiliar", "Auxiliar".equals(funcao
					.obterValor()));
			select.add("Jefe Depto", "Jefe Depto", "Jefe Depto".equals(funcao
					.obterValor()));
			select.add("Jefe División", "Jefe División", "Jefe División"
					.equals(funcao.obterValor()));
		} else {
			select.add("Gerente", "Gerente", false);
			select.add("Director", "Director", false);
			select.add("Auxiliar", "Auxiliar", false);
			select.add("Jefe Depto", "Jefe Depto", false);
			select.add("Jefe División", "Jefe División", false);
		}

		table.add(select);

		Entidade.Atributo nomeAbreviado = (Entidade.Atributo) oficial
				.obterAtributo("nomeabreviado");

		String nomeAbreviadoStr = "";
		if (nomeAbreviado != null)
			nomeAbreviadoStr = nomeAbreviado.obterValor();

		table.addHeader("Denominácion:");
		table.add(new InputString("atributo_nomeabreviado", nomeAbreviadoStr,
				15));

		Entidade.Atributo nacionalidade = (Entidade.Atributo) oficial
				.obterAtributo("nacionalidade");

		String nacionalidadeStr = "";
		if (nacionalidade != null)
			nacionalidadeStr = nacionalidade.obterValor();

		table.addHeader("Pais de Origen:");
		table.add(new InputString("atributo_nacionalidade", nacionalidadeStr,
				30));

		//		table.addHeader("Nº de Inscripciòn:");
		//		if(oficial.obterSigla()!=null)
		//			table.add(oficial.obterSigla());
		//		else
		//			table.add("");

		table.addHeader("Identificación:");

		Block block = new Block(Block.HORIZONTAL);

		block.add(new InputString("ruc", oficial.obterRuc(), 15));
		block.add(new Space());
		block.add(new Label("Cédula de identidad(CI) o RUC"));

		table.add(block);

		Entidade.Atributo dataFuncao = (Entidade.Atributo) oficial
				.obterAtributo("datafuncao");

		table.addHeader("Fecha en la Función:");

		if (dataFuncao != null && !dataFuncao.obterValor().equals(""))
			table.add(new InputDate("atributo_datafuncao",
					new SimpleDateFormat("dd/MM/yyyy").parse(dataFuncao
							.obterValor())));
		else
			table.add(new InputDate("atributo_datafuncao", null));

		Entidade.Atributo situacao = (Entidade.Atributo) oficial
				.obterAtributo("situacao");

		table.addHeader("Stuación:");
		Select selectSituacao = new Select("atributo_situacao", 1);
		selectSituacao.add("", "", false);

		if (situacao != null) {
			selectSituacao.add("Activa", "Activa", "Activa".equals(situacao
					.obterValor()));
			selectSituacao.add("No Activa", "No Activa", "No Activa"
					.equals(situacao.obterValor()));
			selectSituacao.add("Suspensa", "Suspensa", "Suspensa"
					.equals(situacao.obterValor()));
		} else {
			selectSituacao.add("Activa", "Activa", false);
			selectSituacao.add("No Activa", "No Activa", false);
			selectSituacao.add("Suspensa", "Suspensa", false);
		}

		table.add(selectSituacao);

		table.addSubtitle("Contactos:");
		table.addHeader("Contactos:");
		table.addData(new EntidadeContatosView(oficial));

		table.addSubtitle("Direcciones:");
		table.addHeader("Direcciones:");
		table.add(new EntidadeEnderecosView(oficial));

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirOficialCumprimento"));

			table.addFooter(incluirButton);
		} 
		else 
		{
			Button atualizarButton = new Button("Actualizar", new Action("atualizarOficialCumprimento"));
			atualizarButton.getAction().add("id", oficial.obterId());
			atualizarButton.setEnabled(oficial.permiteAtualizar());
			table.addFooter(atualizarButton);

			Button excluirButton = new Button("Eliminar", new Action("excluirEntidade"));
			excluirButton.getAction().add("id", oficial.obterId());
			excluirButton.setEnabled(oficial.permiteAtualizar() && oficial.permiteExcluir());
			table.addFooter(excluirButton);
		}

		return table;
	}

}