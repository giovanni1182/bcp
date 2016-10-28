package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Produto;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class ClassificacaoProdutoControl extends Control {
	public void atualizarClassificacaoProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ClassificacaoProduto classificacaoProduto = (ClassificacaoProduto) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		classificacaoProduto.atribuirApelido(action.getString("apelido"));
		classificacaoProduto.atribuirNome(action.getString("nome"));
		classificacaoProduto.atribuirDescricao(action.getString("descricao"));
		mm.beginTransaction();
		try {
			classificacaoProduto.atualizar();
			this.setAlert("Classificação de produto atualizado");
			this.setResponseView(new EntidadeView(classificacaoProduto));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(classificacaoProduto));
			mm.rollbackTransaction();
		}
	}

	public void atualizarTabelaPrecos(Action action) throws Exception {
		String tabelaPrecos = action.getString("tabelaPrecos");
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ClassificacaoProduto classificacaoProduto = (ClassificacaoProduto) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		mm.beginTransaction();
		try {
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("valor_")) {
					long produtoId = Long.parseLong(key.substring(6, key
							.length()));
					Produto produto = (Produto) entidadeHome
							.obterEntidadePorId(produtoId);
					produto.adicionarPreco(tabelaPrecos, action
							.getString("moeda_" + produtoId), action
							.getDouble("valor_" + produtoId));
					produto.atualizarIpi(action.getString("ipi_" + produtoId));
					produto
							.atualizarPeso(action
									.getDouble("peso_" + produtoId));
				}
			}
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}

	}

	public void incluirClassificacaoProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		ClassificacaoProduto classificacaoProduto = (ClassificacaoProduto) mm
				.getEntity("ClassificacaoProduto");
		classificacaoProduto.atribuirApelido(action.getString("apelido"));
		classificacaoProduto.atribuirNome(action.getString("nome"));
		classificacaoProduto.atribuirDescricao(action.getString("descricao"));
		classificacaoProduto.atribuirSuperior(superior);
		classificacaoProduto.atribuirResponsavel(responsavel);
		mm.beginTransaction();
		try {
			classificacaoProduto.incluir();
			this.setAlert("Classificação de produto incluído");
			this.setResponseView(new EntidadeView(classificacaoProduto));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(classificacaoProduto));
			mm.rollbackTransaction();
		}
	}

	public void visualizarTabelasPrecos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ClassificacaoProduto classificacaoProduto = (ClassificacaoProduto) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

	}

	public void visualizarTabelaPrecos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ClassificacaoProduto classificacaoProduto = (ClassificacaoProduto) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

	}

}