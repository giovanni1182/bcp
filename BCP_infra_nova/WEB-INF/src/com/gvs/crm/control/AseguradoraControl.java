package com.gvs.crm.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.Deflater;
import java.util.zip.ZipOutputStream;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.impl.Zip;
import com.gvs.crm.report.AseguradoraPlanosReport;
import com.gvs.crm.report.AseguradoraPlanosXLS;
import com.gvs.crm.report.AseguradoraProdutividadeXSL;
import com.gvs.crm.report.BalancoGeralXLS;
import com.gvs.crm.report.ProducaoAseguradoraXLS;
import com.gvs.crm.report.ProdutocaoAgentesXLS;
import com.gvs.crm.report.RelProdutividadeAgentesCorredoresXLS;
import com.gvs.crm.report.RelatorioReport;
import com.gvs.crm.report.RelatorioSaldosAseguradoraXLS;
import com.gvs.crm.view.AcionistaAseguradoraView;
import com.gvs.crm.view.ApolicesView;
import com.gvs.crm.view.AseguradoraCoaseguradorView;
import com.gvs.crm.view.AseguradoraFilialView;
import com.gvs.crm.view.AseguradoraFusaoView;
import com.gvs.crm.view.AseguradoraProdutividadeView;
import com.gvs.crm.view.AseguradoraUltimaAgendaView;
import com.gvs.crm.view.AseguradorasPorGrupoCoasegurador;
import com.gvs.crm.view.BalancoGeralView;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.GrupoAlertaTrampanaView;
import com.gvs.crm.view.InspecaoSituView;
import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.ReaseguradoraAseguradoraView;
import com.gvs.crm.view.RelProdutividadeAgentesCorredoresView;
import com.gvs.crm.view.RelatorioMenuView;
import com.gvs.crm.view.RelatorioModeloEstadoView;
import com.gvs.crm.view.ResultadoResumidoView;
import com.gvs.crm.view.SelecionarRelAseguradorasPlanosView;
import com.gvs.crm.view.SelecionarUltimasAgendasView;

import infra.control.Action;
import infra.control.Control;

public class AseguradoraControl extends Control {
	public void incluirAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Aseguradora aseguradora = (Aseguradora) mm.getEntity("Aseguradora");
		
		Collection lista = new ArrayList();
		
		lista.add("0");
		lista.add("1");
		lista.add("2");
		lista.add("3");
		lista.add("4");
		lista.add("5");
		lista.add("6");
		lista.add("7");
		lista.add("8");
		lista.add("9");

		mm.beginTransaction();
		try {
			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				aseguradora.atribuirSuperior(superior);
			}

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				aseguradora.atribuirResponsavel(responsavelView);
			else
				aseguradora.atribuirResponsavel(responsavel);

			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (aseguradora.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");
			
			for(int i = 0 ; i <  action.getString("sigla").length() ; i++)
			{
				String c = action.getString("sigla").substring(i, i+1);
				if(!lista.contains(c))
					throw new Exception("O campo Sigla so pode conter numeros");
			}

			aseguradora.atribuirNome(action.getString("nome"));

			aseguradora.atribuirSigla(action.getString("sigla"));
			aseguradora.incluir();

			aseguradora.atualizarRuc(action.getString("ruc"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = aseguradora
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(aseguradora, aseguradora));
			mm.commitTransaction();

			this.setAlert("Aseguradora incluida");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = null;
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("id"));
		
		Collection lista = new ArrayList();
		
		lista.add("0");
		lista.add("1");
		lista.add("2");
		lista.add("3");
		lista.add("4");
		lista.add("5");
		lista.add("6");
		lista.add("7");
		lista.add("8");
		lista.add("9");

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (aseguradora.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				aseguradora.atribuirSuperior(superior);
			}
			
			for(int i = 0 ; i <  action.getString("sigla").length() ; i++)
			{
				String c = action.getString("sigla").substring(i, i+1);
				if(!lista.contains(c))
					throw new Exception("O campo Sigla so pode conter numeros");
			}

			aseguradora.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				aseguradora.atribuirResponsavel(responsavelView);

			//aseguradora.atribuirSigla(action.getString("sigla"));
			aseguradora.atualizar();

			aseguradora.atualizarRuc(action.getString("ruc"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = aseguradora
							.obterAtributo(nome);

					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(aseguradora, aseguradora));
			mm.commitTransaction();

			this.setAlert("Aseguradora Actualizada");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void novoAcionista(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new AcionistaAseguradoraView(aseguradora));
	}

	public void visualizarAcionista(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Acionista acionista = aseguradora.obterAcionista(action
				.getInt("id"));

		this.setResponseView(new AcionistaAseguradoraView(acionista));
	}

	public void incluirAcionistaAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getString("acionista").equals(""))
				throw new Exception("Elegiré el Accionista");

			aseguradora.adicionarAcionista(action.getString("acionista"),
					action.getInt("quantidade"), action.getString("tipo"));
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AcionistaAseguradoraView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirAcionista(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Acionista acionista = aseguradora.obterAcionista(action
				.getInt("id"));

		mm.beginTransaction();
		try {
			aseguradora.removerAcionista(acionista);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AcionistaAseguradoraView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarAcionistaAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		Aseguradora.Acionista acionista = aseguradora.obterAcionista(action
				.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("acionista").equals(""))
				throw new Exception("Elegiré el Accionista");

			acionista.atualizar(action.getString("acionista"), action
					.getInt("quantidade"), action.getString("tipo"));

			this.setResponseView(new EntidadeView(aseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AcionistaAseguradoraView(acionista));
			mm.rollbackTransaction();
		}
	}

	public void novaReaseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new ReaseguradoraAseguradoraView(aseguradora));
	}

	public void visualizarReaseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Reaseguradora reaseguradora = aseguradora
				.obterReaseguradora(action.getInt("id"));

		this.setResponseView(new ReaseguradoraAseguradoraView(reaseguradora));
	}

	public void incluirReaseguradoraAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getLong("reaseguradoraId") == 0)
				throw new Exception("Elegiré a Reaseguradora");

			if (action.getLong("corretoraId") == 0)
				throw new Exception("Elegiré a Corredora");

			if (action.getDate("dataVencimento") == null)
				throw new Exception("Elegiré a Fecha de Vencimiento");

			Entidade reaseguradora = home.obterEntidadePorId(action
					.getLong("reaseguradoraId"));
			Entidade corretora = home.obterEntidadePorId(action
					.getLong("corretoraId"));

			aseguradora.adicionarReaseguradora(reaseguradora, corretora, action
					.getString("tipoContrato"), action
					.getDate("dataVencimento"), action.getInt("participacao"),
					action.getString("observacao"));
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReaseguradoraAseguradoraView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirReaseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Reaseguradora reaseguradora = aseguradora
				.obterReaseguradora(action.getInt("id"));

		mm.beginTransaction();
		try {
			aseguradora.removerReaseguradora(reaseguradora);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ReaseguradoraAseguradoraView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarReaseguradoraAseguradora(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		Aseguradora.Reaseguradora reaseguradora = aseguradora
				.obterReaseguradora(action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getLong("reaseguradoraId") == 0)
				throw new Exception("Elegiré a Reaseguradora");

			if (action.getLong("corretoraId") == 0)
				throw new Exception("Elegiré a Corredora");

			if (action.getDate("dataVencimento") == null)
				throw new Exception("Elegiré a Fecha de Vencimiento");

			Entidade reaseguradora2 = home.obterEntidadePorId(action
					.getLong("reaseguradoraId"));
			Entidade corretora = home.obterEntidadePorId(action
					.getLong("corretoraId"));

			reaseguradora.atualizar(reaseguradora2, corretora, action
					.getString("tipoContrato"), action
					.getDate("dataVencimento"), action.getInt("participacao"),
					action.getString("observacao"));

			this.setResponseView(new EntidadeView(aseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this
					.setResponseView(new ReaseguradoraAseguradoraView(
							reaseguradora));
			mm.rollbackTransaction();
		}
	}

	public void consultarApolices(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());

		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("id"));

		mm.beginTransaction();
		try {
			String secao = "";
			String plano = "";
			String tipo = "";
			String situacao = "";

			if (action.getBoolean("listar")) {
				secao = action.getString("secao");
				plano = action.getString("plano");
				tipo = action.getString("tipo");
				situacao = action.getString("situacao");
			}

			this.setResponseView(new ApolicesView(aseguradora, true, secao,
					plano, tipo, situacao, action.getBoolean("listar")));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void novaFilial(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new AseguradoraFilialView(aseguradora));
	}

	public void visualizarFilial(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Filial filial = aseguradora
				.obterFilial(action.getInt("id"));

		this.setResponseView(new AseguradoraFilialView(filial));
	}

	public void incluirFilialAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getString("filial").equals(""))
				throw new Exception("Elegiré a Sucursales");

			aseguradora.adicionarFilial(action.getString("filial"), action
					.getString("tipo"), action.getString("telefone"), action
					.getString("cidade"), action.getString("endereco"), action
					.getString("email"));
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraFilialView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirFilial(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Filial filial = aseguradora
				.obterFilial(action.getInt("id"));

		mm.beginTransaction();
		try {
			aseguradora.removerFilial(filial);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraFilialView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarFilialAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		Aseguradora.Filial filial = aseguradora
				.obterFilial(action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("filial").equals(""))
				throw new Exception("Elegiré a Sucursales");

			filial.atualizar(action.getString("filial"), action
					.getString("tipo"), action.getString("telefone"), action
					.getString("cidade"), action.getString("endereco"), action
					.getString("email"));

			this.setResponseView(new EntidadeView(aseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraFilialView(filial));
			mm.rollbackTransaction();
		}
	}

	public void novaFusao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new AseguradoraFusaoView(aseguradora));
	}

	public void visualizarFusao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Fusao fusao = aseguradora.obterFusao(action.getInt("id"));

		this.setResponseView(new AseguradoraFusaoView(fusao));
	}

	public void incluirFusaoAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getString("empresa").equals(""))
				throw new Exception("Elegiré el nome da Compañia");

			if (action.getDate("data") == null)
				throw new Exception("Elegiré a Fecha de la Fusión");

			aseguradora.adicionarFusao(action.getString("empresa"), action
					.getDate("data"));
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraFusaoView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirFusao(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Fusao fusao = aseguradora.obterFusao(action.getInt("id"));

		mm.beginTransaction();
		try {
			aseguradora.removerFusao(fusao);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraFusaoView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarFusaoAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		Aseguradora.Fusao fusao = aseguradora.obterFusao(action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("empresa").equals(""))
				throw new Exception("Elegiré el nome da Compañia");

			if (action.getDate("data") == null)
				throw new Exception("Elegiré a Fecha de la Fusión");

			fusao
					.atualizar(action.getString("empresa"), action
							.getDate("data"));

			this.setResponseView(new EntidadeView(aseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraFusaoView(fusao));
			mm.rollbackTransaction();
		}
	}

	/*
	 * public void novoPlano(Action action) throws Exception { CRMModelManager
	 * mm = new CRMModelManager(this.getUser()); EntidadeHome entidadeHome =
	 * (EntidadeHome) mm.getHome("EntidadeHome");
	 * 
	 * Aseguradora aseguradora = (Aseguradora)
	 * entidadeHome.obterEntidadePorId(action.getLong("entidadeId"));
	 * 
	 * this.setResponseView(new AseguradoraPlanoView(aseguradora)); }
	 * 
	 * public void visualizarPlano(Action action) throws Exception {
	 * CRMModelManager mm = new CRMModelManager(this.getUser()); EntidadeHome
	 * entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome"); Aseguradora
	 * aseguradora = (Aseguradora)
	 * entidadeHome.obterEntidadePorId(action.getLong("entidadeId"));
	 * 
	 * Aseguradora.Plano plano = aseguradora.obterPlano(action.getInt("id"));
	 * 
	 * this.setResponseView(new AseguradoraPlanoView(plano)); }
	 * 
	 * public void incluirPlanoAseguradora(Action action) throws Exception {
	 * CRMModelManager mm = new CRMModelManager(this.getUser()); EntidadeHome
	 * home = (EntidadeHome) mm.getHome("EntidadeHome");
	 * 
	 * Aseguradora aseguradora = (Aseguradora)
	 * home.obterEntidadePorId(action.getLong("entidadeId"));
	 * 
	 * mm.beginTransaction(); try {
	 * 
	 * if(action.getString("plano").equals("")) throw new Exception("Elegiré el
	 * Plan");
	 * 
	 * if(action.getDate("data") == null) throw new Exception("Elegiré a Fecha
	 * de la Resolución");
	 * 
	 * if(action.getString("situacao").equals("")) throw new Exception("Elegiré
	 * a Situación");
	 * 
	 * aseguradora.adicionarPlano(action.getString("ramo"),
	 * action.getString("secao"), action.getString("plano"), null,
	 * action.getDate("data"), action.getString("situacao"),
	 * action.getString("descricao")); mm.commitTransaction();
	 * 
	 * this.setResponseView(new EntidadeView(aseguradora)); } catch (Exception
	 * exception) { this.setAlert(Util.translateException(exception));
	 * this.setResponseView(new AseguradoraPlanoView(aseguradora));
	 * mm.rollbackTransaction(); } }
	 * 
	 * public void excluirPlano(Action action) throws Exception {
	 * CRMModelManager mm = new CRMModelManager(this.getUser()); EntidadeHome
	 * entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
	 * 
	 * Aseguradora aseguradora = (Aseguradora)
	 * entidadeHome.obterEntidadePorId(action.getLong("entidadeId"));
	 * 
	 * Aseguradora.Plano plano = aseguradora.obterPlano(action.getInt("id"));
	 * 
	 * mm.beginTransaction(); try { aseguradora.removerPlano(plano);
	 * 
	 * mm.commitTransaction();
	 * 
	 * this.setResponseView(new EntidadeView(aseguradora)); } catch (Exception
	 * exception) { this.setAlert(Util.translateException(exception));
	 * this.setResponseView(new AseguradoraPlanoView(aseguradora));
	 * mm.rollbackTransaction(); } }
	 * 
	 * public void atualizarPlanoAseguradora(Action action) throws Exception {
	 * CRMModelManager mm = new CRMModelManager(this.getUser()); EntidadeHome
	 * home = (EntidadeHome) mm.getHome("EntidadeHome");
	 * 
	 * Aseguradora aseguradora = (Aseguradora)
	 * home.obterEntidadePorId(action.getLong("entidadeId"));
	 * 
	 * Aseguradora.Plano plano = aseguradora.obterPlano(action.getInt("id"));
	 * 
	 * mm.beginTransaction(); try { if(action.getString("plano").equals(""))
	 * throw new Exception("Elegiré el Plan");
	 * 
	 * if(action.getDate("data") == null) throw new Exception("Elegiré a Fecha
	 * de la Resolución");
	 * 
	 * if(action.getString("situacao").equals("")) throw new Exception("Elegiré
	 * a Situación");
	 * 
	 * plano.atualizar(action.getString("ramo"), action.getString("secao"),
	 * action.getString("plano"), null, action.getDate("data"),
	 * action.getString("situacao"), action.getString("descricao"));
	 * 
	 * this.setResponseView(new EntidadeView(aseguradora));
	 * mm.commitTransaction(); } catch (Exception exception) {
	 * this.setAlert(Util.translateException(exception));
	 * this.setResponseView(new AseguradoraPlanoView(plano));
	 * mm.rollbackTransaction(); } }
	 */

	public void incluirMargemSolvencia(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try
		{

			if(action.getDouble("valor") <= 0)
				throw new Exception("Valor no puede ser cero");
			
			String mesAno = action.getString("mesAno");

			for(Iterator i = aseguradora.obterMargensSolvencia().values().iterator() ; i.hasNext() ; )
			{
				Aseguradora.MargemSolvencia ms = (Aseguradora.MargemSolvencia) i.next();
				
				if(ms.obterMesAno().equals(mesAno))
					throw new Exception("Ja existe um valor catastrado para " + mesAno);
			}
			
			aseguradora.adicionarMargemSolvencia(mesAno, action.getDouble("valor"));
			
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}
	
	public void novoCoasegurador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new AseguradoraCoaseguradorView(aseguradora));
	}

	public void visualizarCoasegurador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Coasegurador coasegurador = aseguradora
				.obterCoasegurador(action.getInt("id"));

		this.setResponseView(new AseguradoraCoaseguradorView(coasegurador));
	}

	public void incluirCoaseguradorAseguradora(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getString("grupo").equals(""))
				throw new Exception("Elegiré el Grupo");

			aseguradora.adicionarCoasegurador(action.getString("grupo"));
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraCoaseguradorView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirCoasegurador(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		Aseguradora.Coasegurador coasegurador = aseguradora
				.obterCoasegurador(action.getInt("id"));

		mm.beginTransaction();
		try {
			aseguradora.removerCoasegurador(coasegurador);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraCoaseguradorView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarCoaseguradorAseguradora(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		Aseguradora.Coasegurador coasegurador = aseguradora
				.obterCoasegurador(action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("grupo").equals(""))
				throw new Exception("Elegiré el Grupo");

			coasegurador.atualizar(action.getString("grupo"));

			this.setResponseView(new EntidadeView(aseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraCoaseguradorView(coasegurador));
			mm.rollbackTransaction();
		}
	}

	public void listarCoaseguradoras(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			Collection lista = aseguradora.obterCoaseguradorasPorGrupo(action
					.getString("codigo"));

			this.setResponseView(new AseguradorasPorGrupoCoasegurador(
					aseguradora, lista, action.getString("codigo")));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void visualizarBalancoGeral(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		mm.beginTransaction();
		try {

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(action.getLong("id"));

			String mes = action.getString("mes");
			String ano = action.getString("ano");

			if (mes != null && !mes.equals("")) {
				if (Integer.parseInt(mes) > 12)
					mes = "12";
				else if (Integer.parseInt(mes) < 1)
					mes = "01";

				if (mes.length() == 1)
					mes = "0" + mes;
			}

			this.setResponseView(new BalancoGeralView(aseguradora, action
					.getBoolean("lista"), action.getString("mes"), action
					.getString("ano")));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void abrirResultadoResumido(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		mm.beginTransaction();
		try {

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(action.getLong("id"));

			String mesAno = action.getString("mesAno");

			if (mesAno.length() == 5)
				mesAno = "0" + mesAno;

			this.setResponseReport(new RelatorioReport(home, aseguradora,
					mesAno));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void visualizarRelatorioAseguradora(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		//Entidade entidade = entidadeHome.obterEntidadePorId(action.getLong("id"));
		Entidade entidade = null;
		Usuario usuarioXLS = usuarioHome.obterUsuarioPorUser(getUser());
		
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
		boolean usuarioAtual = false;
		boolean lista = action.getBoolean("lista");
		String nivel = action.getString("nivel");
		String mes = action.getString("mes");
		String ano = action.getString("ano");
		Date dataDe = action.getDate("dataDe");
		Date dataAte = action.getDate("dataAte");
		boolean excel = action.getBoolean("excel");
		boolean excelBalanco = action.getBoolean("excelBalanco");
		try
		{
			if(action.getBoolean("rel"))
			{
				if(action.getLong("id") == 0)
					throw new Exception("Aseguradora en blanco");
			}
			
			entidade = entidadeHome.obterEntidadePorId(action.getLong("id"));
			
			if (mes != null && !mes.equals(""))
			{
				if (Integer.parseInt(mes) > 12)
					mes = "12";
				else if (Integer.parseInt(mes) < 1)
					mes = "01";
	
				if (mes.length() == 1)
					mes = "0" + mes;
			}
			else
			{
				mes = new SimpleDateFormat("MM").format(new Date());
				ano = new SimpleDateFormat("yyyy").format(new Date());
			}
	
			if (entidade instanceof Usuario)
				usuarioAtual = ((Usuario) entidade).obterChave().equals(this.getUser().getName());
	
			if(excel)
			{
				Collection<Entidade> entidades = new ArrayList<Entidade>();
				
				if (nivel.equals("Nivel 1"))
					entidades = entidade.obterEntidadesNivel1(entidade, mes+ano);
				else if (nivel.equals("Nivel 2"))
					entidades = entidade.obterEntidadesNivel2(entidade, mes+ano);
				else if (nivel.equals("Nivel 3"))
					entidades = entidade.obterEntidadesNivel3(entidade, mes+ano);
				else if (nivel.equals("Nivel 4"))
					entidades = entidade.obterEntidadesNivel4(entidade, mes+ano);
				else if (nivel.equals("Nivel 5"))
					entidades = entidade.obterEntidadesNivel5(entidade, mes+ano);
				
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				String nomeArquivo = "Saldos "+ entidade.obterNome() + "_"+usuarioXLS.obterNome()+ "_" + mes + "_" + ano + "_"+hora+".xls";
				
				RelatorioSaldosAseguradoraXLS xls = new RelatorioSaldosAseguradoraXLS(entidades, mes, ano, entidade);
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName(nomeArquivo);
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
				
				this.setResponseView(new RelatorioMenuView(entidade, origemMenu,usuarioAtual, nivel, mes, ano, lista));
			}
			else if(excelBalanco)
			{
				boolean acumulado = false;
				if(action.getString("acumular").equals("True"))
					acumulado = true;
				
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				BalancoGeralXLS xls = new BalancoGeralXLS(entidade, mes, ano, entidadeHome,acumulado, aseguradoraHome);
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
				if(!acumulado)
					this.setResponseFileName("Balance General "+ entidade.obterNome() + "_"+usuarioXLS.obterNome()+ "_" + mes + "_" + ano + "_"+hora+".xls");
				else
					this.setResponseFileName("Balance General acumulado por Aseguradora" + "_"+usuarioXLS.obterNome()+ "_" + mes + "_" + ano + "_"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
				
				this.setResponseView(new RelatorioMenuView(entidade, origemMenu,usuarioAtual, nivel, mes, ano, lista));
			}
			
			if (action.getString("acumular").equals("True"))
				this.setResponseView(new RelatorioMenuView(entidade, origemMenu,usuarioAtual, nivel, mes, ano, lista, true));
			else
				this.setResponseView(new RelatorioMenuView(entidade, origemMenu,usuarioAtual, nivel, mes, ano, lista));
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new InspecaoSituView());
		}

	}

	public void visualizarRelatorioAseguradoraModeloEstado(Action action)
			throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));

		String mes = action.getString("mes");
		String ano = action.getString("ano");

		if (mes != null && !mes.equals("")) {
			if (Integer.parseInt(mes) > 12)
				mes = "12";
			else if (Integer.parseInt(mes) < 1)
				mes = "01";

			if (mes.length() == 1)
				mes = "0" + mes;
		}

		boolean lista = action.getBoolean("lista");

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		this.setResponseView(new RelatorioModeloEstadoView(aseguradora, mes,
				ano, lista));
	}

	public void visualizarResultadoResumido(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		mm.beginTransaction();
		try {

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(action.getLong("id"));

			String mes = action.getString("mes");
			String ano = action.getString("ano");

			if (mes != null && !mes.equals("")) {
				if (Integer.parseInt(mes) > 12)
					mes = "12";
				else if (Integer.parseInt(mes) < 1)
					mes = "01";

				if (mes.length() == 1)
					mes = "0" + mes;
			}

			this.setResponseView(new ResultadoResumidoView(aseguradora, action
					.getBoolean("lista"), action.getString("mes"), action
					.getString("ano")));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void abrirRelatorio(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		mm.beginTransaction();
		try {

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(action.getLong("id"));

			String mesAno = action.getString("mesAno");

			if (mesAno.length() == 5)
				mesAno = "0" + mesAno;

			this.setResponseReport(new RelatorioReport(home, aseguradora,
					mesAno));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, usuario));
			mm.rollbackTransaction();
		}
	}

	public void visualizarProdutividadeAseguradora(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = null;
		Collection agentes = new ArrayList();
		Collection<Apolice> apolicesSemAgentes = new ArrayList<Apolice>();
		
		Date dataInicio = null;
		Date dataFim = null;
		
		mm.beginTransaction();
		try 
		{
			if (action.getBoolean("lista")) 
			{
				if (action.getLong("aseguradoraId") == 0)
					throw new Exception("Elegir la Aseguradora");
				
				aseguradora = (Aseguradora) home.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if (action.getDate("dataInicio") == null)
					throw new Exception("Elegir la Fecha Inicio");
				if (action.getDate("dataFim") == null)
					throw new Exception("Elegir la Fecha Final");

				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
				
				agentes = aseguradora.obterAgentesPorPeridodo(dataInicio, dataFim);
				apolicesSemAgentes = aseguradora.obterApolicesSemAgentesPorPeridodo(dataInicio, dataFim);
			}

			if(!action.getBoolean("excel"))
				this.setResponseView(new AseguradoraProdutividadeView(aseguradora,agentes, dataInicio, dataFim, action.getBoolean("lista"), apolicesSemAgentes));
			else
			{
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				AseguradoraProdutividadeXSL xls = new AseguradoraProdutividadeXSL(aseguradora, agentes, dataInicio, dataFim, textoUsuario,apolicesSemAgentes);
				
				dataInicioStr = dataInicioStr.replace("/", "_");
				dataFimStr = dataFimStr.replace("/", "_");
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Produción Agentes_" + aseguradora.obterNome() +"_"+usuario.obterNome()+ "_" + dataInicioStr + "_" + dataFimStr + "_"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
				
			mm.commitTransaction();
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AseguradoraProdutividadeView(aseguradora,agentes, action.getDate("dataInicio"), action.getDate("dataFim"), false, apolicesSemAgentes));
			mm.rollbackTransaction();
		}
	}
	
	public void visualizarCentralRiscoAgendas(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Entidade entidade = (Entidade) home.obterEntidadePorId(action.getLong("origemMenuId"));
		mm.beginTransaction();
		try 
		{
			if(action.getString("view") == null || action.getString("view").equals(""))
				this.setResponseView(new SelecionarUltimasAgendasView(entidade));
			else
			{
				Collection aseguradoras = new ArrayList();
				
				if(action.getString("view").equals("MCO") || action.getString("view").equals("MCI"))
					aseguradoras = aseguradoraHome.obterAseguradorasPorMenor80OrdenadoPorNome();
				else
					aseguradoras = aseguradoraHome.obterAseguradorasPorMaior80OrdenadasPorNome();
					
				this.setResponseView(new AseguradoraUltimaAgendaView(entidade, aseguradoras, action.getString("view")));
			}
			
			mm.commitTransaction();
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new PaginaInicialView(usuario, entidade));
			mm.rollbackTransaction();
		}
	}
	
	public void gerarCiRucNaoEncontrados(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("id"));
		mm.beginTransaction();
		try 
		{
			String caminho = "/tmp/" + aseguradora.obterSigla() + "CIRUCNoEcontrado.txt";
			
			System.out.println(caminho);
			
			FileWriter file = new FileWriter(caminho);
			
			//System.out.println(System.getProperties().toString());
			
			for(Iterator j = aseguradora.obterApolicesVigentes().iterator() ; j.hasNext() ; )
			{
				Apolice apolice = (Apolice) j.next();
				
				//System.out.println(apolice.obterNumeroApolice());
				
				String tipoPessoa = "";
            	
                if(apolice.obterTipoPessoa()!=null)
                {
					if(apolice.obterTipoPessoa().equals("Persona Fisica"))
                        tipoPessoa = "1";
                    else if(apolice.obterTipoPessoa().equals("Persona Juridic"))
                        tipoPessoa = "2";
                }
                else
                	tipoPessoa = " ";
                
                String tipoIdentificacao = "";
                
                if(apolice.obterTipoIdentificacao()!=null)
                {
                    if(apolice.obterTipoIdentificacao().equals("C\351dula de Identidad Paraguaya"))
                        tipoIdentificacao = "1";
                    else if(apolice.obterTipoIdentificacao().equals("C\351dula de Identidad Extranjera"))
                        tipoIdentificacao = "2";
                    else if(apolice.obterTipoIdentificacao().equals("Passaporte"))
                        tipoIdentificacao = "3";
                    else if(apolice.obterTipoIdentificacao().equals("RUC"))
                        tipoIdentificacao = "4";
                    else if(apolice.obterTipoIdentificacao().equals("Otro"))
                        tipoIdentificacao = "5";
                }
                else
                	tipoIdentificacao = " ";
                
                String situacaoSeguro = "";
                
                if(apolice.obterSituacaoSeguro().equals("Vigente"))
                    situacaoSeguro = "1";
                else if(apolice.obterSituacaoSeguro().equals("No Vigente Pendiente"))
                    situacaoSeguro = "2";
                else if(apolice.obterSituacaoSeguro().equals("No Vigente"))
                    situacaoSeguro = "3";
                else
                	situacaoSeguro = " ";
                
                String tipoInstrumento = "";
                
                if(apolice.obterStatusApolice().equals("P\363liza Individual"))
                    tipoInstrumento = "1";
                else if(apolice.obterStatusApolice().equals("P\363liza Madre"))
                    tipoInstrumento = "2";
                else if(apolice.obterStatusApolice().equals("Certificado de Seguro Colectivo"))
                    tipoInstrumento = "3";
                else if(apolice.obterStatusApolice().equals("Certificado Provisorio"))
                    tipoInstrumento = "4";
                else if(apolice.obterStatusApolice().equals("Nota de Cobertura de Reaseguro"))
                    tipoInstrumento = "5";
                else
                	tipoInstrumento = " ";
                
				if(apolice.obterNumeroIdentificacao()!=null)
				{
                	String numeroIdentificacaoLimpo = "";
                	
                	for (int k = 0; k < apolice.obterNumeroIdentificacao().length(); k++)
                	{
            			String caracter = apolice.obterNumeroIdentificacao().substring(k, k + 1);

            			if (caracter.hashCode() >= 48 && caracter.hashCode() <= 57)
            				numeroIdentificacaoLimpo += caracter;
            		}
                	
                    //Layout 10 Numero apolice, 10 Secao, 1 tipo instrumento, 1 situacao instrumento, 60 nome asegurado, 1 tipo pessoa, 1 tipo identificacao, 15 numero identificacao
                	if(!entidadeHome.existeDocumento(apolice.obterTipoIdentificacao(), numeroIdentificacaoLimpo, apolice.obterTipoPessoa()))
                		file.write(apolice.obterNumeroApolice() + apolice.obterSecao().obterApelido() + tipoInstrumento + situacaoSeguro + apolice.obterNomeAsegurado() + tipoPessoa + tipoIdentificacao + apolice.obterNumeroIdentificacao() + "\r\n");
                }
                else
                {
                	
                	String nomeAsegurado = "";
                	
                	if(apolice.obterNomeAsegurado() == null)
                		nomeAsegurado = this.colocarEspacosADireira(nomeAsegurado, 60);
                	else
                		nomeAsegurado = apolice.obterNomeAsegurado();
                	
                	String numeroIdentificacao = "";
                	
                	numeroIdentificacao = this.colocarEspacosADireira(numeroIdentificacao, 15);
                	
                	file.write(apolice.obterNumeroApolice() + apolice.obterSecao().obterApelido() + tipoInstrumento + situacaoSeguro + nomeAsegurado + tipoPessoa + tipoIdentificacao + numeroIdentificacao + "\r\n");
                }
			}
			
			file.close();
			
			FileInputStream input = new FileInputStream(caminho);
			
			InputStream input2 =  input;
			
			SampleModelManager mm2 = new SampleModelManager();
			UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
			
			home.addUploadedFile(aseguradora, input2, aseguradora.obterSigla() + "CIRUCNoEcontrado.txt", "txt", new Long(input.available()).longValue(), 0);
			
			this.setResponseView(new EntidadeView(aseguradora));
			this.setAlert("Archivo Generado");
			
			mm.commitTransaction();
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(aseguradora));
			mm.rollbackTransaction();
		}
	}
	
	public void selecionarRelAseguradorasPlanos(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser()); 
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Aseguradora aseguradora = null;
		String opcao = action.getString("gerar");
		String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new SelecionarRelAseguradorasPlanosView(opcao));
			else
			{
				if(opcao.equals(""))
					throw new Exception("Opciones en blanco");
				
				String textoUsuario = "Generado : " + usuarioAtual.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				
				if(opcao.equals("pdf"))
				{
					if(action.getLong("id") == 0)
					{
						Zip zipClass = new Zip();
						
						Collection arquivos = new ArrayList();
						 // Arquivo ou diretório de entrada 
						String raiz = "C:/tmp";
						
						 // Arquivo a ser gerado  
						 File saida = new File("C:/tmp/Archivos_Planes.zip");  
						   
						 ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(saida));  
						 zip.setLevel(Deflater.BEST_COMPRESSION);  
						 
						 AseguradoraPlanosReport pdf;
						 
						for(Aseguradora aseguradora2 : aseguradoraHome.obterAseguradoras())
						{
							pdf = new AseguradoraPlanosReport(aseguradora2,usuarioAtual, textoUsuario);
							
							arquivos.add(pdf.obterArquivo2());
						}
						
						zipClass.compress(raiz, arquivos, zip);
						
						zip.finish();  
						zip.flush();  
						zip.close(); 
						
						InputStream input = new FileInputStream(saida);
						
						this.setResponseInputStream(input);
				        this.setResponseFileName("Archivos_Planes"+hora+".zip");
				        this.setResponseContentType("multipart/x-zip");
				        this.setResponseContentSize(saida.length());
					}
					else
					{
						aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("id"));
						
						AseguradoraPlanosReport pdf = new AseguradoraPlanosReport(aseguradora,usuarioAtual, textoUsuario);
						
						this.setResponseInputStream(pdf.obterArquivo());
				        this.setResponseFileName(aseguradora.obterNome() + hora + "_Planes.pdf");
				        this.setResponseContentType("application/pdf");
				        this.setResponseContentSize(pdf.obterArquivo().available());
					}
				}
				else if(opcao.equals("xls"))
				{
					if(action.getLong("id") == 0)
					{
						Zip zipClass = new Zip();
						
						Collection arquivos = new ArrayList();
						 // Arquivo ou diretório de entrada 
						String raiz = "C:/tmp";
						
						 // Arquivo a ser gerado  
						 File saida = new File("C:/tmp/Archivos_Planes.zip");  
						   
						 ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(saida));  
						 zip.setLevel(Deflater.BEST_COMPRESSION);  
						 AseguradoraPlanosXLS xls;
						 
						 for(Aseguradora aseguradora2 : aseguradoraHome.obterAseguradoras())
						{
							xls = new AseguradoraPlanosXLS(aseguradora2,usuarioAtual, textoUsuario);
							
							arquivos.add(xls.obterArquivo2());
						}
						
						zipClass.compress(raiz, arquivos, zip);
						
						zip.finish();  
						zip.flush();  
						zip.close(); 
						
						InputStream input = new FileInputStream(saida);
						
						this.setResponseInputStream(input);
				        this.setResponseFileName("Archivos_Planes"+hora+".zip");
				        this.setResponseContentType("multipart/x-zip");
				        this.setResponseContentSize(saida.length());
					}
					else
					{
						aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("id"));
						
						AseguradoraPlanosXLS xls = new AseguradoraPlanosXLS(aseguradora,usuarioAtual, textoUsuario);
						
						this.setResponseInputStream(xls.obterArquivo());
				        this.setResponseFileName(aseguradora.obterNome() + hora+"_Planes.xls");
				        this.setResponseContentType("application/vnd.ms-excel");
				        this.setResponseContentSize(xls.obterArquivo().available());
					}
				}
			}
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new SelecionarRelAseguradorasPlanosView(opcao));
		}
	}

	private String colocarEspacosADireira(String texto, int tamanhoCampo)
	{
		String espacos = "";
		
		for(int i = texto.length() ; i < tamanhoCampo - texto.length() ; i++)
			espacos+=" ";
		
		return texto + espacos;
	}
	
	public void gerarExcelProducaoAgentes(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action.getLong("id"));
		try
		{
			if(action.getDate("_dataInicio") == null)
				throw new Exception("Fecha inicio en blanco");
			
			if(action.getDate("_dataFim") == null)
				throw new Exception("Fecha fin en blanco");
			
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("_dataFim")) + " 23:59:59";
			Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			ProdutocaoAgentesXLS xls = new ProdutocaoAgentesXLS(aseguradora, action.getDate("_dataInicio"), dataFim);
			
			String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
			
			InputStream arquivo = xls.obterArquivo();
			this.setResponseInputStream(arquivo);
		    this.setResponseFileName("Produción Agentes_" + aseguradora.obterNome().replace(".", "") + hora+".xls");
		    this.setResponseContentType("application/vnd.ms-excel");
		    this.setResponseContentSize(arquivo.available());
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new EntidadeView(aseguradora));
		}
	}
	
	public void gerarExcelProducaoAseguradoras(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
		AuxiliarSeguro auxiliar = (AuxiliarSeguro) home.obterEntidadePorId(action.getLong("id"));
		try
		{
			if(action.getDate("_dataInicio") == null)
				throw new Exception("Fecha inicio en blanco");
			
			if(action.getDate("_dataFim") == null)
				throw new Exception("Fecha fin en blanco");
			
			String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(action.getDate("_dataFim")) + " 23:59:59";
			Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			
			ProducaoAseguradoraXLS xls = new ProducaoAseguradoraXLS(auxiliar, action.getDate("_dataInicio"), dataFim);
			
			String hora = "_" + usuarioAtual.obterNome() + "_" + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
			
			InputStream arquivo = xls.obterArquivo();
			this.setResponseInputStream(arquivo);
		    this.setResponseFileName("Produción Aseguradoras_" + auxiliar.obterNome().replace(".", "") + hora+".xls");
		    this.setResponseContentType("application/vnd.ms-excel");
		    this.setResponseContentSize(arquivo.available());
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new EntidadeView(auxiliar));
		}
	}
	
	public void visualizarProdutividadeAgentesCorredores(Action action) throws Exception 
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Aseguradora aseguradora = null;
		
		Date dataInicio = null;
		Date dataFim = null;
		
		mm.beginTransaction();
		try 
		{
			if (action.getBoolean("lista")) 
			{
				if (action.getLong("aseguradoraId") > 0)
					aseguradora = (Aseguradora) home.obterEntidadePorId(action.getLong("aseguradoraId"));
				
				if (action.getDate("dataInicio") == null)
					throw new Exception("Elegir la Fecha Inicio");
				if (action.getDate("dataFim") == null)
					throw new Exception("Elegir la Fecha Final");

				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " 00:00:00";
				dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataInicioStr);
				
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
				dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
			}

			if(!action.getBoolean("excel"))
				this.setResponseView(new RelProdutividadeAgentesCorredoresView(aseguradora, dataInicio, dataFim, action.getBoolean("lista")));
			else
			{
				AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
				
				String dataInicioStr = new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
				String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
				String hora = new SimpleDateFormat("HH:mm").format(new Date());
				
				String textoUsuario = "Generado : " + usuario.obterNome() + " " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
				RelProdutividadeAgentesCorredoresXLS xls = new RelProdutividadeAgentesCorredoresXLS(aseguradora, dataInicio, dataFim, textoUsuario, home, aseguradoraHome);
				
				dataInicioStr = dataInicioStr.replace("/", "_");
				dataFimStr = dataFimStr.replace("/", "_");
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Produción Agentes y Corredores" + "_"+usuario.obterNome()+ "_" + dataInicioStr + "_" + dataFimStr + "_"+hora+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
				
			mm.commitTransaction();
		} 
		catch (Exception exception) 
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelProdutividadeAgentesCorredoresView(aseguradora, action.getDate("dataInicio"), action.getDate("dataFim"), false));
			mm.rollbackTransaction();
		}
	}
	
	public void grupoAlertaTrempana(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Collection<Entidade> aseguradoras = home.obterAseguradorasSemCoaseguradora();
		mm.beginTransaction();
		try
		{
			if(!action.getBoolean("view"))
			{
				Aseguradora aseg;
				String grupo;
				
				for(Entidade e : aseguradoras)
				{
					aseg = (Aseguradora) e;
					grupo = action.getString("grupo_"+e.obterId());
					aseg.atualizarGrupoAlertaTrempana(grupo);
				}
			}
			
			this.setResponseView(new GrupoAlertaTrampanaView(aseguradoras));
			mm.commitTransaction();
		}
		catch (Exception e)
		{
			this.setAlert(Util.translateException(e));
			this.setResponseView(new GrupoAlertaTrampanaView(aseguradoras));
			mm.rollbackTransaction();
		}
	}
}