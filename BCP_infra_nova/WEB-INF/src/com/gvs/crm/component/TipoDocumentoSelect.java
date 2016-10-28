package com.gvs.crm.component;

import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoDocumento;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeDocumento;
import com.gvs.crm.model.EntidadeHome;

import infra.security.User;
import infra.view.BasicView;
import infra.view.Select;
import infra.view.View;

public class TipoDocumentoSelect extends BasicView {
	private String nome;

	private EntidadeDocumento documento;

	public TipoDocumentoSelect(String nome, EntidadeDocumento documento)
			throws Exception {
		this.nome = nome;
		this.documento = documento;
	}

	public View execute(User user, Locale locale, Properties properties)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Select select = new Select(this.nome, 1);
		select.add("", "", false);

		ClassificacaoDocumento cDocumento = (ClassificacaoDocumento) entidadeHome
				.obterEntidadePorApelido("documentos");

		for (Iterator i = cDocumento.obterInferiores().iterator(); i.hasNext();) {
			Entidade entidade = (Entidade) i.next();

			if (documento != null)
				select.add(entidade.obterNome(), entidade.obterId(), entidade
						.obterId() == documento.obterId());
			else
				select.add(entidade.obterNome(), entidade.obterId(), false);
		}
		return select;
	}
}