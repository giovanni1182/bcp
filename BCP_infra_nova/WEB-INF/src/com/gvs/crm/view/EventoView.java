package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;

import infra.config.InfraProperties;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.View;

public class EventoView extends PortalView {
	private Evento evento;

	private Entidade origemMenu;

	public EventoView(Evento evento) throws Exception {
		this.evento = evento;
	}

	public EventoView(Evento evento, Entidade origemMenu) throws Exception {
		this.evento = evento;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		try {
			EventoAbstratoView view = (EventoAbstratoView) Class.forName(
					InfraProperties.getInstance().getProperty(
							this.evento.obterClasse() + ".view")).newInstance();

			view.atribuirEvento(this.evento, this.origemMenu);
			return view;
		} catch (Exception exception) {
			Block block = new Block(Block.VERTICAL);
			block.add(new Label(
					"Ocorreu um na obtenção da visualização do evento do tipo '"
							+ this.evento.obterClasse() + "'"));
			block.add(new Label(exception.getClass().getName() + ": "
					+ exception.getMessage()));
			return block;
		}
	}

	public String getSelectedGroup() throws Exception {
		return null;
	}

	public String getSelectedOption() throws Exception {
		return null;
	}

	public View getTitle() throws Exception
	{
		if (this.evento.obterId() == 0)
			return new Label("Evento - " + this.evento.obterClasseDescricao());
		else
		{
			if(this.evento instanceof Apolice)
			{
				Apolice apolice = (Apolice) this.evento;
				if(apolice.obterOrigem()!=null)
					return new Label("Póliza: " + apolice.obterNumeroApolice() + " - " + apolice.obterOrigem().obterNome());
				else
					return new Label("Póliza: " + apolice.obterNumeroApolice());
			}
			else
				return new Label(this.evento.obterClasseDescricao() + " - "	+ this.evento.obterTitulo() + " / "	+ this.evento.obterFase().obterNome());
		}
	}

	public Entidade getOrigemMenu() throws Exception
	{
		Entidade entidadeMenu = null;

		if (this.origemMenu != null && this.origemMenu.obterId() != 0)
			entidadeMenu = origemMenu;
		else if (this.evento.obterOrigem() != null
				&& this.evento.obterOrigem().obterId() != 0)
			entidadeMenu = this.evento.obterOrigem();
		else
			entidadeMenu = this.evento.obterResponsavel();

		return entidadeMenu;
	}
}