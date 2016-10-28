package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class ContaControl extends Control {
	public void incluirConta(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		Conta conta = (Conta) mm.getEntity("Conta");
		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré o Nombre");
			if (action.getString("codigo").equals(""))
				throw new Exception("Elegiré o Código");

			conta.atribuirSuperior(superior);
			conta.atribuirCodigo(action.getString("codigo"));
			conta.atribuirApelido(action.getString("codigo"));
			conta.atribuirNivel(action.getString("nivel"));
			conta.atribuirNome(action.getString("nome"));
			conta.atribuirAtivo(action.getString("ativo"));
			conta.atribuirDescricao(action.getString("descricao"));
			conta.atribuirResponsavel(responsavel);

			/*
			 * conta.atribuirConcepto(action.getString("concepto"));
			 * conta.atribuirNoma(action.getString("noma"));
			 * conta.atribuirNatureza(action.getString("natureza"));
			 * conta.atribuirDinamica(action.getString("dinamica"));
			 */
			conta.incluir();
			this.setAlert("Cuenta incluida");
			this.setResponseView(new EntidadeView(conta, origemMenu));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(conta, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void atualizarConta(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		Conta conta = (Conta) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));
		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré o Nombre");
			if (action.getString("codigo").equals(""))
				throw new Exception("Elegiré o Código");

			conta.atribuirNome(action.getString("nome"));
			conta.atribuirSuperior(superior);
			conta.atribuirApelido(action.getString("codigo"));
			conta.atualizar();
			conta.atualizarCodigo(action.getString("codigo"));
			conta.atualizarDinamica(action.getString("dinamica"));
			conta.atualizarAtivo(action.getString("ativo"));
			conta.atualizarDescricao(action.getString("descricao"));
			conta.atualizarNivel(action.getString("nivel"));

			/*
			 * conta.atualizarConcepto(action.getString("concepto"));
			 * conta.atualizarNoma(action.getString("noma"));
			 * conta.atualizarNatureza(action.getString("natureza"));
			 * conta.atualizarDinamica(action.getString("dinamica"));
			 */
			mm.commitTransaction();

			this.setAlert("Cuenta Actualizada");
			this.setResponseView(new EntidadeView(conta));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(conta));
			mm.rollbackTransaction();
		}
	}

	public void importar(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		Conta conta = (Conta) mm.getEntity("Conta");
		Conta conta3 = (Conta) mm.getEntity("Conta");
		ClassificacaoContas cContas = (ClassificacaoContas) mm
				.getEntity("ClassificacaoContas");
		ClassificacaoContas cContas3 = (ClassificacaoContas) mm
				.getEntity("ClassificacaoContas");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));
		mm.beginTransaction();
		try {
			/*
			 * for(Iterator i = cContas.obterClassificacaoContas().iterator() ;
			 * i.hasNext() ; ) { ClassificacaoContas.ClassificacaoConta2
			 * cContas2 = (ClassificacaoContas.ClassificacaoConta2) i.next();
			 * cContas3.atribuirId(cContas2.obterContaId());
			 * cContas3.atribuirSuperior(entidadeHome.obterEntidadePorId(cContas2.obterSuperiorId()));
			 * cContas3.atribuirCodigo(cContas2.obterCodigoClassificacaoConta());
			 * cContas3.atribuirNome(cContas2.obterNomeClassificacaoConta());
			 * cContas3.atribuirResponsavel(usuarioHome.obterUsuarioPorChave("edgar"));
			 * cContas3.atribuirApelido(cContas2.obterCodigoClassificacaoConta());
			 * cContas3.incluir(); }
			 */

			for (Iterator i = conta.obterContas().iterator(); i.hasNext();) {
				Conta.Conta2 conta2 = (Conta.Conta2) i.next();
				//conta3.atribuirId(conta2.obterContaId());
				System.out.println("Id: " + conta2.obterContaId());
				conta3.atribuirSuperior(entidadeHome.obterEntidadePorId(conta2
						.obterSuperiorId()));
				System.out.println("Superior: " + conta2.obterSuperiorId());
				conta3.atribuirCodigo(conta2.obterCodigoConta());
				System.out.println("Codigo: " + conta2.obterCodigoConta());
				conta3.atribuirNome(conta2.obterNomeConta());
				System.out.println("Nome: " + conta2.obterNomeConta());
				conta3.atribuirResponsavel(usuarioHome
						.obterUsuarioPorChave("edgar"));
				conta3.atribuirApelido(conta2.obterCodigoConta());
				conta3.incluir();

				//System.out.println("Incluiu");
			}

			mm.commitTransaction();

			this.setAlert("Pronto");
			this
					.setResponseView(new PaginaInicialView(responsavel,
							responsavel));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this
					.setResponseView(new PaginaInicialView(responsavel,
							responsavel));
			mm.rollbackTransaction();
		}
	}

}

