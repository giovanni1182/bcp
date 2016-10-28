package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class AdvogadosSelect2 extends BasicView {
	private String nome;

	private String advogado;

	public AdvogadosSelect2(String nome, String advogado) throws Exception {
		this.nome = nome;
		this.advogado = advogado;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		select.add("[Todos]", "", false);

		Entidade departamento = entidadeHome
				.obterEntidadePorApelido("juridico");

		for (Iterator i = departamento.obterInferiores().iterator(); i
				.hasNext();) {
			Entidade entidade = (Entidade) i.next();

			if (entidade instanceof Usuario) {
				Usuario usuario = (Usuario) entidade;

				if (usuario.obterAtributo("cargo").obterValor().toLowerCase()
						.equals("advogado(a)"))
					select.add(usuario.obterNome(), usuario.obterNome(),
							usuario.obterNome().equals(advogado));
			}
		}
		return select;
	}
}