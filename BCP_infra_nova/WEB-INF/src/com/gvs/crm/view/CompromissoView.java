package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.CriacaoLabel;
import com.gvs.crm.component.DataHoraInput;
import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.component.EventoComentariosLabel;
import com.gvs.crm.component.EventoDescricaoInput;
import com.gvs.crm.component.EventoImage;
import com.gvs.crm.component.EventoSuperiorLabel;
import com.gvs.crm.component.EventoTipoSelect;
import com.gvs.crm.component.EventoTituloInput;
import com.gvs.crm.component.HoraInput;
import com.gvs.crm.component.UsuarioSelect;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Compromisso;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CompromissoView extends EventoAbstratoView {
	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Collection usuario = usuarioHome.obterUsuarios();

		Compromisso compromisso = (Compromisso) this.obterEvento();
		Evento superior = compromisso.obterSuperior();
		boolean incluir = compromisso.obterId() == 0;
		if (superior != null && incluir) {
			compromisso.atribuirOrigem(superior.obterOrigem());
			compromisso.atribuirPrioridade(superior.obterPrioridade());
		}

		Table table = new Table(2);
		table.addHeader("Superior:");
		table.add(new EventoSuperiorLabel(compromisso));
		if (!incluir) {
			table.addHeader("Creado por:");
			table.addData(new CriacaoLabel(compromisso));

			table.addHeader("Responsable:");
			table.addData(new EntidadePopup("responsavelId", "responsavelNome",
					compromisso.obterResponsavel(), "Usuario", compromisso
							.permiteAtualizar()));
		}
		table.addHeader("Nombre:");
		table.addData(new EntidadePopup("origemId", "origemNome", compromisso.obterOrigem(), true, true));
		table.addHeader("Tipo de Compromiso:");
		table.addData(new EventoTipoSelect("tipo", compromisso, true));
		table.addHeader("Título:");
		table.addData(new EventoTituloInput("titulo", compromisso, true));
		table.addHeader("Descripción:");
		table.addData(new EventoDescricaoInput("descricao", compromisso, true));
		table.addHeader("Fecha Prevista:");
		Block block = new Block(Block.HORIZONTAL);
		block.add(new DataHoraInput("data", "inicio", compromisso.obterDataPrevistaInicio(), true));
		block.add(new Space());
		block.add(new Label("hasta"));
		block.add(new Space());
		block.add(new HoraInput("conclusao", compromisso.obterDataPrevistaConclusao(), true));
		table.addData(block);
		table.addHeader("Participantes:");
		table.addHeader(this.participantesView(compromisso));
		if(!incluir)
		{
			table.addHeader("Nuevos Participantes:");
			table.addHeader(this.novosParticipantesView(usuario));
		}

		if (incluir) {
			Button incluirButton = new Button("Agregar", new Action(
					"incluirCompromisso"));
			if (superior != null)
				incluirButton.getAction().add("superiorId", superior.obterId());
			incluirButton.getAction().add("origemMenuId",
					this.obterOrigemMenu().obterId());
			table.addFooter(incluirButton);

			Action voltarAction = new Action("novoEvento");
			voltarAction.add("passo", 2);
			voltarAction.add("origemMenuId", this.obterOrigemMenu().obterId());

			Button voltarButton = new Button("Volver", voltarAction);
			table.addFooter(voltarButton);
		} else {
			table.addHeader("Comentarios:");
			table.add(new ComentariosView(compromisso));

			Button pegarButton = new Button("Pegar", new Action("pegarEvento"));
			pegarButton.getAction().add("id", compromisso.obterId());
			pegarButton.getAction().add("origemMenuId",
					compromisso.obterOrigem().obterId());
			pegarButton.setEnabled(compromisso.permitePegar());
			table.addFooter(pegarButton);

			/*Button devolverButton = new Button("Devolver", new Action("devolverEvento"));
			devolverButton.getAction().add("id", compromisso.obterId());
			devolverButton.getAction().add("origemMenuId",compromisso.obterOrigem().obterId());
			devolverButton.setEnabled(compromisso.permiteDevolver());
			table.addFooter(devolverButton);*/

			Button atualizarButton = new Button("Actualizar", new Action(
					"atualizarCompromisso"));
			atualizarButton.getAction().add("id", compromisso.obterId());
			atualizarButton.getAction().add("origemMenuId",
					compromisso.obterOrigem().obterId());
			atualizarButton.setEnabled(compromisso.permiteAtualizar());
			table.addFooter(atualizarButton);

			Button encaminharButton = new Button("Remitir", new Action(
					"encaminharEvento"));
			encaminharButton.getAction().add("id", compromisso.obterId());
			encaminharButton.getAction().add("origemMenuId",
					compromisso.obterOrigem().obterId());
			encaminharButton.getAction().add("view", true);
			encaminharButton.setEnabled(compromisso.permiteEncaminhar());
			table.addFooter(encaminharButton);

			Button concluirButton = new Button("Concluir", new Action(
					"concluirEvento"));
			concluirButton.getAction().add("id", compromisso.obterId());
			concluirButton.getAction().add("view", true);
			concluirButton.getAction().add("origemMenuId",
					compromisso.obterOrigem().obterId());
			concluirButton.setEnabled(compromisso.permiteConcluir());
			table.addFooter(concluirButton);

			Button excluirButton = new Button("Eliminar", new Action(
					"excluirEvento"));
			excluirButton.getAction().add("id", compromisso.obterId());
			excluirButton.getAction().add("origemMenuId",
					compromisso.obterOrigem().obterId());
			excluirButton.getAction().setConfirmation("Confirma exclusion ?");
			excluirButton.setEnabled(compromisso.permiteExcluir());
			table.addFooter(excluirButton);

			if (superior == null) {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarPaginaInicial"));
				voltarButton.getAction().add("origemMenuId",
						compromisso.obterOrigem().obterId());
				table.addFooter(voltarButton);
			} else {
				Button voltarButton = new Button("Volver", new Action(
						"visualizarEvento"));
				voltarButton.getAction().add("id", superior.obterId());
				voltarButton.getAction().add("origemMenuId",
						compromisso.obterOrigem().obterId());
				table.addFooter(voltarButton);
			}
		}
		return table;
	}

	private View novosParticipantesView(Collection usuarios)
	{
		Block novosParticipantesBlock = new Block(Block.VERTICAL);
		
		for (int i = 0; i < 2; i++)
			novosParticipantesBlock.add(new UsuarioSelect("participantes",usuarios));
		
		return novosParticipantesBlock;
	}

	private View participantesView(Compromisso compromisso) throws Exception {
		Table table = new Table(4);
		table.setWidth("100%");
		table.addHeader(new Space());
		table.addHeader("Participación");
		table.addHeader("Fase");
		table.addHeader("Comentarios");
		for (Iterator i = compromisso.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();

			Label responsavelLabel = new Label(e.obterResponsavel().obterNome());
			responsavelLabel.setBold(!e.foiLido());
			Link responsavelLink = new Link(responsavelLabel, new Action(
					"visualizarEvento"));
			responsavelLink.getAction().add("id", e.obterId());
			responsavelLink.getAction().add("origemMenuId",
					compromisso.obterOrigem().obterId());

			Label faseLabel = new Label(e.obterFase().obterNome());
			faseLabel.setBold(!e.foiLido());

			table.addData(new EventoImage(e));
			table.addData(responsavelLink);
			table.addData(faseLabel);
			table.addData(new EventoComentariosLabel(e));
		}
		return table;
	}
}