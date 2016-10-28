package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Inscricao;

import infra.config.InfraProperties;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.View;

public class EntidadeView extends PortalView {
	private Entidade entidade, origemMenu;

	public EntidadeView(Entidade entidade) throws Exception {
		this.entidade = entidade;
	}

	public EntidadeView(Entidade entidade, Entidade origemMenu)
			throws Exception {
		this.entidade = entidade;
		this.origemMenu = origemMenu;
	}

	public View getBody(User user, Locale locale, Properties properties) throws Exception 
	{
		try {
			EntidadeAbstrataView view = (EntidadeAbstrataView) Class.forName(InfraProperties.getInstance().getProperty(this.entidade.obterClasse().toLowerCase()+ ".view")).newInstance();
			view.atribuirEntidade(this.entidade, this.origemMenu);
			return view;
		} catch (Exception exception) {
			Block block = new Block(Block.VERTICAL);
			block.add(new Label(
					"Ocorreu um na obtenção da visualização da entidade do tipo '"
							+ this.entidade.obterClasse() + "'"));
			block.add(new Label(exception.getClass().getName() + ": "
					+ exception.getMessage()));
			return block;
		}
	}

	public String getSelectedGroup() throws Exception {
		return this.entidade.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Detalhes";
	}

	public View getTitle() throws Exception
	{
		if(this.entidade instanceof AuxiliarSeguro)
		{
			Entidade.Atributo atividade = (Entidade.Atributo) entidade.obterAtributo("atividade");
			
			Inscricao inscricao = this.entidade.obterUltimaInscricao();
			if(inscricao!=null)
			{
				String ramo = inscricao.obterRamo();
				return new Label(this.entidade.obterNome() + " - "	+ atividade.obterValor() + " - Detalles " + " - Ramo: " + ramo);
			}
			else
				return new Label(this.entidade.obterNome() + " - "	+ atividade.obterValor() + " - Detalles");
		}
		else
			return new Label(this.entidade.obterNome() + " - "	+ this.entidade.obterDescricaoClasse() + " - Detalles");
		
	}

	public Entidade getOrigemMenu() throws Exception {
		if (this.entidade != null && this.entidade.obterId() != 0)
			return this.entidade;
		else
			return this.origemMenu;
	}
}