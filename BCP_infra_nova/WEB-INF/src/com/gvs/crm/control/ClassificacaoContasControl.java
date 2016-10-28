package com.gvs.crm.control;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class ClassificacaoContasControl extends Control {
	public void atualizarClassificacaoContas(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = (Entidade) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));
		ClassificacaoContas classificacaoContas = (ClassificacaoContas) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");
			if (action.getString("codigo").equals(""))
				throw new Exception("Elegiré el Código");

			classificacaoContas.atribuirNome(action.getString("nome"));
			classificacaoContas.atribuirApelido(action.getString("codigo"));
			classificacaoContas.atribuirSuperior(superior);
			classificacaoContas.atualizar();

			classificacaoContas.atualizarDescricao(action
					.getString("descricao"));
			classificacaoContas.atualizarCodigo(action.getString("codigo"));
			classificacaoContas.atualizarNivel(action.getString("nivel"));
			/*
			 * classificacaoContas.atualizarConcepto(action.getString("concepto"));
			 * classificacaoContas.atualizarNoma(action.getString("noma"));
			 * classificacaoContas.atualizarNatureza(action.getString("natureza"));
			 * classificacaoContas.atualizarDinamica(action.getString("dinamica"));
			 */
			mm.commitTransaction();

			this.setAlert("Clasificación de Cuentas actualizada");
			this.setResponseView(new EntidadeView(classificacaoContas));

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(classificacaoContas));
			mm.rollbackTransaction();
		}
	}

	public void incluirClassificacaoContas(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		ClassificacaoContas classificacaoContas = (ClassificacaoContas) mm
				.getEntity("ClassificacaoContas");

		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));

		if (action.getString("nome").equals(""))
			throw new Exception("Elegiré el Nombre");
		if (action.getString("codigo").equals(""))
			throw new Exception("Elegiré el Código");

		classificacaoContas.atribuirCodigo(action.getString("codigo"));
		classificacaoContas.atribuirApelido(action.getString("codigo"));
		classificacaoContas.atribuirNivel(action.getString("nivel"));
		classificacaoContas.atribuirNome(action.getString("nome"));
		classificacaoContas.atribuirDescricao(action.getString("descricao"));
		classificacaoContas.atribuirSuperior(superior);
		classificacaoContas.atribuirResponsavel(responsavel);

		/*
		 * classificacaoContas.atribuirConcepto(action.getString("concepto"));
		 * classificacaoContas.atribuirNoma(action.getString("noma"));
		 * classificacaoContas.atribuirNatureza(action.getString("natureza"));
		 * classificacaoContas.atribuirDinamica(action.getString("dinamica"));
		 */
		mm.beginTransaction();
		try {
			classificacaoContas.incluir();
			this.setAlert("Clasificación de Cuentas incluída");
			this.setResponseView(new EntidadeView(classificacaoContas));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(classificacaoContas));
			mm.rollbackTransaction();
		}
	}

	public void apagarTotalizacao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Entidade entidade = (Entidade) entidadeHome.obterEntidadePorId(action
				.getLong("id"));

		String nivel = action.getString("nivel");
		String mes = action.getString("mes");
		String ano = action.getString("ano");
		boolean usuarioAtual = false;
		if (entidade instanceof Usuario)
			usuarioAtual = ((Usuario) entidade).obterChave().equals(
					this.getUser().getName());

		/*
		 * mm.beginTransaction(); try { String mesAnoModificado="";
		 * 
		 * for(int i = 0 ; i < mesAno.length() ; i++) {
		 * 
		 * String caracter = mesAno.substring(i,i+1);
		 * 
		 * if(caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
		 * mesAnoModificado += caracter; }
		 * 
		 * entidade.apagarTotalizacaoExistente(mesAnoModificado);
		 * 
		 * this.setAlert("Relatório Excluido com Sucesso !");
		 * this.setResponseView(new RelatorioMenuView(entidade, entidade,
		 * usuarioAtual, nivel, mesAno, true)); mm.commitTransaction(); } catch
		 * (Exception exception) {
		 * this.setAlert(Util.translateException(exception));
		 * this.setResponseView(new RelatorioMenuView(entidade, entidade,
		 * usuarioAtual, nivel, mesAno, true)); mm.rollbackTransaction(); }
		 */
	}

}