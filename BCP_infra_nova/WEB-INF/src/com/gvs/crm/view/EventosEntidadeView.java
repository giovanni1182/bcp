package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class EventosEntidadeView extends PortalView {
	private final String CRIADOR = "1";

	private final String DESTINO = "3";

	private final String ORIGEM = "2";

	private final String RESPONSAVEL = "4";

	private final String AGENDAS = "5";

	private Entidade entidade, origemMenu;

	private boolean usuarioAtual;

	private String classeEvento;

	private String mesAno;
	private int pagina;

	public EventosEntidadeView(Entidade entidade, boolean usuarioAtual,	String classeEvento, Entidade origemMenu, String mesAno)throws Exception 
	{
		this.entidade = entidade;
		this.usuarioAtual = usuarioAtual;
		this.classeEvento = classeEvento;
		this.origemMenu = origemMenu;
		this.mesAno = mesAno;
	}
	
	public EventosEntidadeView(Entidade entidade, boolean usuarioAtual,	String classeEvento, Entidade origemMenu, String mesAno, int pagina)throws Exception 
	{
		this.entidade = entidade;
		this.usuarioAtual = usuarioAtual;
		this.classeEvento = classeEvento;
		this.origemMenu = origemMenu;
		this.mesAno = mesAno;
		this.pagina = pagina;
	}

	public View getBody(User user, Locale locale, Properties properties)throws Exception 
	{
		String _eventos = properties.getProperty("_eventos", this.ORIGEM);

		//if (this.entidade instanceof Usuario)
			//if (_eventos == null)
				//_eventos = this.CRIADOR;

		// criar as opções de filtro por tipo de evento
		Block block1 = new Block(Block.HORIZONTAL);
		/*if (this.entidade instanceof Usuario)
		{
			Label criadorLabel = new Label("Creador");
			criadorLabel.setBold(_eventos.equals(this.CRIADOR));
			Link criadorLink = new Link(criadorLabel, new Action("visualizarEventosEntidade"));
			criadorLink.getAction().add("id", this.entidade.obterId());
			criadorLink.getAction().add("_eventos", this.CRIADOR);
			criadorLink.getAction().add("_pagina", 0);
			block1.add(criadorLink);
			block1.add(new SeparadorLabel());
		}*/

		Label responsavelLabel = new Label("Responsable");
		responsavelLabel.setBold(_eventos.equals(this.RESPONSAVEL));
		Link responsavelLink = new Link(responsavelLabel, new Action("visualizarEventosEntidade"));
		responsavelLink.getAction().add("id", this.entidade.obterId());
		responsavelLink.getAction().add("_eventos", this.RESPONSAVEL);
		responsavelLink.getAction().add("_pagina", 0);
		block1.add(responsavelLink);
		block1.add(new SeparadorLabel());

		Label origemLabel = new Label("Origen");
		origemLabel.setBold(_eventos.equals(this.ORIGEM));
		Link origemLink = new Link(origemLabel, new Action("visualizarEventosEntidade"));
		origemLink.getAction().add("id", this.entidade.obterId());
		origemLink.getAction().add("_eventos", this.ORIGEM);
		origemLink.getAction().add("_pagina", 0);
		block1.add(origemLink);

		block1.add(new SeparadorLabel());

		Label agendasLabel = new Label("Agendas");
		agendasLabel.setBold(_eventos.equals(this.AGENDAS));
		Link agendaLink = new Link(agendasLabel, new Action("visualizarEventosEntidade"));
		agendaLink.getAction().add("id", this.entidade.obterId());
		agendaLink.getAction().add("_eventos", this.AGENDAS);
		agendaLink.getAction().add("_pagina", 0);
		block1.add(agendaLink);

		//block1.add(new SeparadorLabel());

		// obtém todos os eventos
		Collection eventos = null;
		if (_eventos.equals(this.ORIGEM))
			eventos = this.entidade.obterEventosComoOrigemHistorico(this.pagina);
		else if (_eventos.equals(this.DESTINO))
			eventos = this.entidade.obterEventosComoDestinoHistorico(this.pagina);
		else if (_eventos.equals(this.RESPONSAVEL))
			eventos = this.entidade.obterEventosDeResponsabilidadeHistorico(this.pagina);
		else if (_eventos.equals(this.AGENDAS) && mesAno != null)
			eventos = this.entidade.obterEventosAgenda(mesAno, this.pagina);
		/*else if (this.entidade instanceof Usuario) 
		{
			Usuario usuario = (Usuario) this.entidade;
			if (_eventos.equals(this.CRIADOR))
				eventos = usuario.obterEventosCriadosHistorico(this.pagina);
		}*/ 
		else
			eventos = new ArrayList();

		// cria a tabela ser retornada
		Table table = new Table(1);
		table.addStyle(Table.STYLE_BLANK);
		table.setWidth("100%");
		table.add(block1);
		Action action = new Action("visualizarEventosEntidade");
		action.add("id", this.entidade.obterId());
		action.add("classeEvento", this.classeEvento);

		if (_eventos.equals(this.AGENDAS))
			table.add(new AgendasView(eventos, action, entidade, mesAno, this.pagina));
		else
			table.add(new EventosView(eventos, action, this.pagina));
		
		eventos = null;

		return table;
	}

	public String getSelectedGroup() throws Exception {
		if (this.usuarioAtual)
			return "Menu Principal";
		else
			return this.entidade.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Eventos";
	}

	public View getTitle() throws Exception {
		return new Label(this.entidade.obterNome() + " - Eventos");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		if (this.origemMenu != null && this.origemMenu.obterId() != 0)
			return this.origemMenu;
		else
			return this.entidade;
	}
}