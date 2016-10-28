package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.PlanoRamoSelect;
import com.gvs.crm.component.PlanoSecao2Select;
import com.gvs.crm.component.PlanoSelect;
import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.component.SimNaoSelect3;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.InputLong;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class PlanoView extends EventoAbstratoView {
	public static final int DETALLES = 1;

	public static final int DOCUMENTOS = 2;

	private int _pasta;

	private boolean incluir;

	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		Plano plano = (Plano) this.obterEvento();

		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);

		incluir = plano.obterId() == 0;

		Table table = new Table(2);

		_pasta = Integer.parseInt(properties.getProperty("_pastaPlano", "1"));

		if (incluir || _pasta > 2)
			this._pasta = DETALLES;

		Link basicosLink = new Link("Detalles", new Action("visualizarEvento"));
		((Label) basicosLink.getCaption()).setBold(_pasta == DETALLES);
		basicosLink.getAction().add("id", plano.obterId());
		basicosLink.getAction().add("_pastaPlano", DETALLES);
		basicosLink.setEnabled(!incluir);

		Link documentosLink = new Link("Anexos", new Action("visualizarEvento"));
		((Label) documentosLink.getCaption()).setBold(_pasta == DOCUMENTOS);
		documentosLink.getAction().add("id", plano.obterId());
		documentosLink.getAction().add("_pastaPlano", DOCUMENTOS);
		documentosLink.setEnabled(!incluir);

		Block block = new Block(Block.HORIZONTAL);
		block.add(basicosLink);
		block.add(new SeparadorLabel());
		block.add(documentosLink);
		table.setNextColSpan(table.getColumns());
		table.add(block);

		if (_pasta == DETALLES) 
		{
			table.addSubtitle("");
			
			table.addHeader("Especial:");
			table.add(new SimNaoSelect3("especial", plano.obterEspecial(), usuarioAtual));

			if(plano.obterOrigem() != null && !(plano.obterOrigem() instanceof Raiz)) 
			{
				InputLong input2 = new InputLong("aseguradoraId", plano.obterOrigem().obterId(), 10);
				input2.setEnabled(false);
				input2.setVisible(false);
				table.add(input2);

				Link link = new Link(plano.obterOrigem().obterNome(),new Action("visualizarDetalhesEntidade"));
				link.getAction().add("id", plano.obterOrigem().obterId());

				table.addData(link);
			}
			else
			{
				table.addHeader("Entidad:");
				table.add(new EntidadePopup("aseguradoraId","aseguradoraNome",null,"Aseguradora,Reaseguradora,Corretora,AuditorExterno,AuxiliarSeguro",true));
			}
			
			table.addHeader("Responsable:");
			table.add(new EntidadePopup("responsavelId", "responsavelNome",plano.obterResponsavel(), "Usuario", true));

			table.addHeader("Ramo");

			Block ramoBlock = new Block(Block.HORIZONTAL);
			Action action = new Action("selecionaRamo");
			if(!incluir)
				action.add("id", plano.obterId());
			
			ramoBlock.add(new PlanoRamoSelect("ramo", plano.obterRamo(),action, true, false));
			
			if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_IETA) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			{
				ramoBlock.add(new Space(4));
				ramoBlock.add(new InputString("novoRamo", null, 20));
			}

			table.add(ramoBlock);

			table.addHeader("Sección:");

			Block secaoBlock = new Block(Block.HORIZONTAL);
			action = new Action("selecionaSecao");
			if(!incluir)
				action.add("id", plano.obterId());
			
			System.out.println(plano.obterSecao());
			
			secaoBlock.add(new PlanoSecao2Select("secao", plano.obterRamo(), plano.obterSecao(), action));
			if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_IETA) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			{
				secaoBlock.add(new Space(4));
				secaoBlock.add(new InputString("novaSecao", null, 35));
			}

			table.add(secaoBlock);

			table.addHeader("Modalidad:");

			Block planoBlock = new Block(Block.HORIZONTAL);

			/*Select planoSelect = new Select("plano", 1);
			planoSelect.add("", "", false);

			for (Iterator i = plano.obterNomePlanos().iterator(); i.hasNext();)
			{
				String codigoPlano = (String) i.next();

				planoSelect.add(codigoPlano, codigoPlano, codigoPlano.equals(plano.obterPlano()));
			}

			planoBlock.add(planoSelect);*/
			
			action = new Action("selecionaModalidade");
			if(!incluir)
				action.add("id", plano.obterId());
			
			planoBlock.add(new PlanoSelect("plano", plano.obterSecao(), plano.obterPlano(), action, false));
			
			if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_IETA) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			{
				planoBlock.add(new Space(2));
				planoBlock.add(new InputString("novoPlano", null, 50));
			}
			table.add(planoBlock);

			table.addHeader("Denominación:");
			table.add(new InputString("denominacao", plano.obterTitulo(), 60));

			table.addHeader("Identificador:");
			table.add(new InputString("identificador", plano
					.obterIdentificador(), 12));

			table.addHeader("Resolución:");
			table.addData(new InputString("resolucao", plano.obterResolucao(),
					10));

			table.addHeader("Fecha de la Resolución:");
			table.addData(new InputDate("data", plano.obterDataResolucao()));

			table.addHeader("Situación:");
			if (incluir) {
				InputString input = new InputString("situacao", "No Activo", 15);
				input.setEnabled(false);

				table.addHeader(input);
			} else
				table.addHeader(plano.obterSituacao());
			//table.addData(new SituacaoPlanoSelect("situacao",
			// plano.obterSituacao()));

			table.addHeader("Referencia:");
			table.addData(new InputText("descricao", plano.obterDescricao(), 5,
					80));

			table.addHeader("Sub-eventos:");
			table.setNextColSpan(table.getColumns());
			table.add(new SubEventosView(plano));

			table.setNextColSpan(table.getColumns());
			table.add(new Space());

			table.addHeader("Comentarios:");
			table.setNextColSpan(table.getColumns());
			table.add(new ComentariosView(plano));

			if (incluir) 
			{
				Action incluirAction = new Action("incluirPlano");
				Button incluirButton = new Button("Agregar", incluirAction);
				table.addFooter(incluirButton);

				Action cancelarAction = new Action("visualizarDetalhesEntidade");
				cancelarAction.add("id", plano.obterOrigem().obterId());
				table.addFooter(new Button("Volver", cancelarAction));
			}
			else 
			{
				if (!plano.obterFase().obterCodigo().equals(Plano.APROVADA)	&& !plano.obterFase().obterCodigo().equals(Plano.EVENTO_CONCLUIDO)) 
				{
					Button aprovarButton = new Button("Aprovar por Fora",new Action("aprovarPlano"));
					aprovarButton.getAction().add("view", true);
					aprovarButton.getAction().add("id", plano.obterId());
					table.addFooter(aprovarButton);
				}

				Button novaAgendaButton = new Button("Sub-Evento", new Action("novoEvento"));
				novaAgendaButton.getAction().add("passo", 3);
				novaAgendaButton.getAction().add("superiorId", plano.obterId());
				novaAgendaButton.setEnabled(plano.permiteAtualizar());
				table.addFooter(novaAgendaButton);

				Action atualizarAction = new Action("atualizarPlano");
				atualizarAction.add("id", plano.obterId());
				Button atualizarButton = new Button("Actualizar",atualizarAction);
				atualizarButton.setEnabled(plano.permiteAtualizar());
				
				table.addFooter(atualizarButton);

				/*
				 * if(!plano.obterFase().obterCodigo().equals(Plano.EVENTO_PENDENTE) &&
				 * !plano.obterFase().obterCodigo().equals(Plano.EVENTO_CONCLUIDO)) {
				 * Button faseAnteriorButton = new Button(" < < < Fase
				 * Anterior", new Action("faseAnterior"));
				 * faseAnteriorButton.getAction().add("view", true);
				 * faseAnteriorButton.getAction().add("id", plano.obterId());
				 * faseAnteriorButton.setEnabled(plano.permiteAtualizar());
				 * table.addFooter(faseAnteriorButton); }
				 * 
				 * if(!plano.obterFase().obterCodigo().equals(Plano.SUPERINTENDENTE) &&
				 * !plano.obterFase().obterCodigo().equals(Plano.EVENTO_CONCLUIDO)) {
				 * Button proximaFaseButton = new Button("Próxima Fase >>>", new
				 * Action("proximaFase"));
				 * proximaFaseButton.getAction().add("view", true);
				 * proximaFaseButton.getAction().add("id", plano.obterId());
				 * proximaFaseButton.setEnabled(plano.permiteAtualizar());
				 * table.addFooter(proximaFaseButton); }
				 */

				/*
				 * if(plano.obterFase().obterCodigo().equals(Plano.SUPERINTENDENTE)) {
				 * Button aprovarButton = new Button("Aprovar", new
				 * Action("aprovarPlano")); aprovarButton.getAction().add("id",
				 * plano.obterId()); aprovarButton.getAction().add("view",
				 * true); table.addFooter(aprovarButton);
				 * 
				 * Button rejeitarButton = new Button("Rejeitar", new
				 * Action("rejeitarPlano"));
				 * rejeitarButton.getAction().add("id", plano.obterId());
				 * rejeitarButton.getAction().add("view", true);
				 * table.addFooter(rejeitarButton); }
				 */

				Button ativarButton = new Button("Reativar", new Action(
						"ativarPlano"));
				ativarButton.getAction().add("id", plano.obterId());
				ativarButton.getAction().add("view", true);
				ativarButton.setEnabled(plano.obterFase().obterCodigo().equals(
						Plano.SUSPENSA)
						|| plano.obterFase().obterCodigo().equals(
								Plano.CANCELADA));
				table.addFooter(ativarButton);

				if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_IETA) || usuarioAtual.obterNivel().equals(Usuario.DIVISAO_ESTUDOS_ATUAIS) || usuarioAtual.obterNivel().equals(Usuario.DIVISAO_ESTUDOS_TECNICOS) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
				{
					Button suspenderButton = new Button("Suspender", new Action("suspenderPlano"));
					suspenderButton.getAction().add("id", plano.obterId());
					suspenderButton.getAction().add("view", true);
					suspenderButton.setEnabled(!plano.obterFase().obterCodigo().equals(Plano.SUSPENSA) && !plano.obterFase().obterCodigo().equals(Plano.CANCELADA));
					table.addFooter(suspenderButton);
				

					Button cancelarButton = new Button("Cancelación", new Action("cancelarPlano"));
					cancelarButton.getAction().add("id", plano.obterId());
					cancelarButton.getAction().add("view", true);
					cancelarButton.setEnabled(!plano.obterFase().obterCodigo().equals(Plano.SUSPENSA) && !plano.obterFase().obterCodigo().equals(Plano.CANCELADA));
					table.addFooter(cancelarButton);
				}

				Button encaminharButton = new Button("Remetir", new Action("encaminharEvento"));
				encaminharButton.getAction().add("id", plano.obterId());
				encaminharButton.getAction().add("view", true);
				encaminharButton.setEnabled(plano.permiteEncaminhar() && !plano.obterFase().obterCodigo().equals(Plano.SUSPENSA) && !plano.obterFase().obterCodigo().equals(Plano.CANCELADA));
				table.addFooter(encaminharButton);

				if (plano.obterFase().obterCodigo().equals(Plano.APROVADA))
				{
					Button concluirButton = new Button("Concluir", new Action("concluirEvento"));
					concluirButton.getAction().add("id", plano.obterId());
					concluirButton.getAction().add("view", true);
					concluirButton.setEnabled(plano.permiteAtualizar());
					table.addFooter(concluirButton);
				}

				if(usuarioAtual.obterNivel().equals(Usuario.INTENDENTE_IETA) || usuarioAtual.obterNivel().equals(Usuario.DIVISAO_ESTUDOS_ATUAIS) || usuarioAtual.obterNivel().equals(Usuario.DIVISAO_ESTUDOS_TECNICOS) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
				{
					Button comentarButton = new Button("Comentar", new Action("comentarEvento"));
					comentarButton.getAction().add("id", plano.obterId());
					comentarButton.getAction().add("view", true);
					table.addFooter(comentarButton);
				}

				/*if (usuarioAtual.obterId() == 1) 
				{
					Action excluirAction = new Action("excluirEvento");
					excluirAction.add("id", plano.obterId());
					excluirAction.setConfirmation("Confirma exclusión ?");
					Button excluirButton = new Button("Eliminar", excluirAction);
					table.addFooter(excluirButton);
				}*/

				if (plano.obterOrigem() != null) {
					Action cancelarAction = new Action(
							"visualizarDetalhesEntidade");
					cancelarAction.add("id", plano.obterOrigem().obterId());
					table.addFooter(new Button("Volver", cancelarAction));
				}
			}
		} else if (_pasta == DOCUMENTOS) {
			table.setNextColSpan(table.getColumns());
			table.add(new AnexosView(plano));
		}

		return table;
	}
}