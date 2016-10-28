package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Reaseguradora;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputString;
import infra.view.Label;
import infra.view.Link;
import infra.view.Select;
import infra.view.Table;
import infra.view.View;

public class ReaseguradoraView extends EntidadeAbstrataView {

	private static final int DETALHE = 0;

	private static final int CALIFICACION = 1;

	private static final int CONTROLE = 2;

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Reaseguradora reaseguradora = (Reaseguradora) this.obterEntidade();

		boolean incluir = reaseguradora.obterId() == 0;

		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		Table table = new Table(2);

		int _pasta = Integer.parseInt(properties.getProperty("_pastaReaseguro",
				"0"));

		if (incluir || _pasta > 2)
			_pasta = DETALHE;

		Link dadosLink = new Link("Detalles", new Action(
				"visualizarDetalhesEntidade"));
		((Label) dadosLink.getCaption()).setBold(_pasta == DETALHE);
		dadosLink.getAction().add("id", reaseguradora.obterId());
		dadosLink.getAction().add("_pastaReaseguro", DETALHE);
		dadosLink.setEnabled(!incluir);

		Link componentesLink = new Link("Calificación", new Action(
				"visualizarDetalhesEntidade"));
		((Label) componentesLink.getCaption()).setBold(_pasta == CALIFICACION);
		componentesLink.getAction().add("id", reaseguradora.obterId());
		componentesLink.getAction().add("_pastaReaseguro", CALIFICACION);
		componentesLink.setEnabled(!incluir);

		Link controleLink = new Link("Control SIS", new Action(
				"visualizarDetalhesEntidade"));
		((Label) controleLink.getCaption()).setBold(_pasta == CONTROLE);
		controleLink.getAction().add("id", reaseguradora.obterId());
		controleLink.getAction().add("_pastaReaseguro", CONTROLE);
		controleLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(dadosLink);
		block.add(new SeparadorLabel());
		block.add(componentesLink);
		block.add(new SeparadorLabel());
		block.add(controleLink);

		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		mainTable.add(table);
		table = new Table(2);

		if (_pasta == DETALHE) {

			table.addSubtitle("Detalle");

			if (!incluir) {
				table.addHeader("Creado en:");
				String data = new SimpleDateFormat("dd/MM/yyyy")
						.format(reaseguradora.obterCriacao());
				table.add(data);
			}

			table.addHeader("Superior:");

			if (incluir)
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", reaseguradora.obterSuperior(), true));
			else
				table.add(new EntidadePopup("entidadeSuperiorId",
						"superiorNome", reaseguradora.obterSuperior(),
						reaseguradora.permiteAtualizar()));

			table.addHeader("Responsable:");
			if (incluir)
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						reaseguradora.obterResponsavel(), "Usuario", true));
			else
				table.add(new EntidadePopup("responsavelId", "responsavelNome",
						reaseguradora.obterResponsavel(), "Usuario",
						reaseguradora.permiteAtualizar()));

			Entidade.Atributo tipo = (Entidade.Atributo) reaseguradora
					.obterAtributo("tipo");

			table.addHeader("Tipo:");
			Select select = new Select("atributo_tipo", 1);
			select.add("", "", false);
			if (tipo != null) {
				select.add("Reaseguradora", "Reaseguradora", "Reaseguradora"
						.equals(tipo.obterValor()));
				select.add("Seguradora", "Seguradora", "Seguradora".equals(tipo
						.obterValor()));
			} else {
				select.add("Reaseguradora", "Reaseguradora", false);
				select.add("Seguradora", "Seguradora", false);
			}

			table.add(select);

			table.addHeader("Nombre:");
			table.add(new InputString("nome", reaseguradora.obterNome(), 60));

			Entidade.Atributo nomeAbreviado = (Entidade.Atributo) reaseguradora
					.obterAtributo("nomeabreviado");

			String nomeAbreviadoStr = "";
			if (nomeAbreviado != null)
				nomeAbreviadoStr = nomeAbreviado.obterValor();

			table.addHeader("Denominácion:");
			table.add(new InputString("atributo_nomeabreviado",
					nomeAbreviadoStr, 15));

			Entidade.Atributo pais = (Entidade.Atributo) reaseguradora
					.obterAtributo("paisorigem");

			String paisStr = "";
			if (pais != null)
				paisStr = pais.obterValor();

			table.addHeader("Pais de Origen:");
			table.add(new InputString("atributo_paisorigem", paisStr, 40));

			//			table.addHeader("Nº de Inscripciòn:");
			//			if(reaseguradora.obterSigla()!=null)
			//				table.add(reaseguradora.obterSigla());
			//			else
			//				table.add("");

			Entidade.Atributo situacao = (Entidade.Atributo) reaseguradora
					.obterAtributo("situacao");

			table.addHeader("Stuación:");
			if (situacao != null) {
				if (situacao.obterValor() == null) {
					InputString input = new InputString("atributo_situacao",
							"No Activa", 20);
					input.setEnabled(false);

					table.addHeader(input);
				} else
					table.addHeader(situacao.obterValor());
			} else {
				InputString input = new InputString("atributo_situacao",
						"No Activa", 20);
				input.setEnabled(false);

				table.addHeader(input);
			}

			table.addSubtitle("Contactos:");
			table.addHeader("Contactos:");
			table.addData(new EntidadeContatosView(reaseguradora));

			table.addSubtitle("Direcciones:");
			table.addHeader("Direcciones:");
			table.add(new EntidadeEnderecosView(reaseguradora));

			if (incluir) 
			{
				Button incluirButton = new Button("Agregar", new Action("incluirReaseguradora"));
				table.addFooter(incluirButton);
			} 
			else 
			{
				Button atualizarButton = new Button("Actualizar", new Action("atualizarReaseguradora"));
				atualizarButton.getAction().add("id", reaseguradora.obterId());
				atualizarButton.setEnabled(reaseguradora.permiteAtualizar());
				table.addFooter(atualizarButton);

				/*Button excluirButton = new Button("Eliminar", new Action("excluirEntidade"));
				excluirButton.getAction().add("id", reaseguradora.obterId());
				excluirButton.setEnabled(reaseguradora.permiteExcluir() && reaseguradora.permiteAtualizar());

				table.addFooter(excluirButton);*/
			}
			
			mainTable.add(table);
		}

		else if (_pasta == CALIFICACION) {
			mainTable.setNextColSpan(table.getColumns());
			mainTable.add(new ReaseguradoraClassificacoesView(reaseguradora));
		} else if (_pasta == CONTROLE) {
			mainTable.setNextColSpan(table.getColumns());
			mainTable.addSubtitle("Control SIS");
			mainTable.add(new InscricoesView(reaseguradora));
		}

		return mainTable;
	}

}