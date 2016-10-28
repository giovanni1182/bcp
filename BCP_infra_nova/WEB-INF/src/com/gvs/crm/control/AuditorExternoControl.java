package com.gvs.crm.control;

import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuditorExterno;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.AuditorExternoClienteView;
import com.gvs.crm.view.AuditorExternoServicoView;
import com.gvs.crm.view.AuditorExternoServicosView;
import com.gvs.crm.view.EntidadeView;

import infra.control.Action;
import infra.control.Control;

public class AuditorExternoControl extends Control {
	public void incluirAuditorExterno(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AuditorExterno auditor = (AuditorExterno) mm
				.getEntity("AuditorExterno");

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (auditor.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				auditor.atribuirSuperior(superior);
			}

			auditor.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0)
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));

			if (responsavelView != null)
				auditor.atribuirResponsavel(responsavelView);
			else
				auditor.atribuirResponsavel(responsavel);

			auditor.incluir();

			auditor.atualizarRuc(action.getString("ruc"));

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals("")) {
				auditor.adicionarNovoRamo(action.getString("novoRamo"));
				auditor.atualizarRamo(action.getString("novoRamo"));
			} else
				auditor.atualizarRamo(action.getString("ramo"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auditor
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auditor, auditor));
			mm.commitTransaction();

			this.setAlert("Auditor Externo Incluido");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auditor));
			mm.rollbackTransaction();
		}
	}

	public void atualizarAuditorExterno(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			if (action.getString("nome").equals(""))
				throw new Exception("Elegiré el Nombre");

			if (auditor.verificarRuc(action.getString("ruc")))
				throw new Exception("El ruc " + action.getString("ruc")
						+ " ja esta sendo utilizado");

			if (action.getLong("entidadeSuperiorId") > 0) {
				superior = entidadeHome.obterEntidadePorId(action
						.getLong("entidadeSuperiorId"));
				auditor.atribuirSuperior(superior);
			}

			auditor.atribuirNome(action.getString("nome"));

			Usuario responsavelView = null;

			if (action.getLong("responsavelId") > 0) {
				responsavelView = (Usuario) entidadeHome
						.obterEntidadePorId(action.getLong("responsavelId"));
				auditor.atribuirResponsavel(responsavelView);
			}

			auditor.atualizar();

			auditor.atualizarRuc(action.getString("ruc"));

			if (action.getString("novoRamo") != null
					&& !action.getString("novoRamo").equals("")) {
				auditor.adicionarNovoRamo(action.getString("novoRamo"));
				auditor.atualizarRamo(action.getString("novoRamo"));
			} else
				auditor.atualizarRamo(action.getString("ramo"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auditor
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auditor, auditor));
			mm.commitTransaction();

			this.setAlert("Auditor Externo Actualizado");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auditor));
			mm.rollbackTransaction();
		}
	}

	public void atualizarDadosComplementares(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {

			if (action.getLong("aseguradoraId") > 0) {
				Aseguradora aseguradora = (Aseguradora) entidadeHome
						.obterEntidadePorId(action.getLong("aseguradoraId"));

				auditor.atualizarAseguradora(aseguradora);
			}

			if (!action.getString("novoRamo").equals("")) {
				auditor.adicionarNovoRamo(action.getString("novoRamo"));
				auditor.atualizarRamo(action.getString("novoRamo"));
			} else
				auditor.atualizarRamo(action.getString("ramo"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auditor
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auditor, auditor));
			mm.commitTransaction();

			this.setAlert("Datos Complementares Actualizados");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auditor));
			mm.rollbackTransaction();
		}
	}

	public void atualizarIngresso(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auditor
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auditor, auditor));
			mm.commitTransaction();

			this.setAlert("Ingresso Actualizado");

		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auditor));
			mm.rollbackTransaction();
		}
	}

	public void novoCliente(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		this.setResponseView(new AuditorExternoClienteView(auditor));
	}

	public void visualizarCliente(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		AuditorExterno.Cliente cliente = auditor.obterCliente(action
				.getInt("id"));

		this.setResponseView(new AuditorExternoClienteView(cliente));
	}

	public void incluirCliente(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) home
				.obterEntidadePorId(action.getLong("entidadeId"));

		mm.beginTransaction();
		try {

			if (action.getLong("aseguradoraId") == 0)
				throw new Exception("Elegiré a Aseguradora");

			if (action.getDate("dataInicio") == null)
				throw new Exception("Elegiré a Fecha Inicio");

			if (action.getDate("dataFim") == null)
				throw new Exception("Elegiré a Fecha Cierre");

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(action.getLong("aseguradoraId"));

			auditor.adicionarCliente(aseguradora, action
					.getDouble("honorarios"), action.getDate("dataInicio"),
					action.getDate("dataInicio"), action
							.getString("outrosServicos"));
			mm.commitTransaction();

			this.setResponseView(new EntidadeView(auditor));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuditorExternoClienteView(auditor));
			mm.rollbackTransaction();
		}
	}

	public void excluirCliente(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("entidadeId"));

		AuditorExterno.Cliente cliente = auditor.obterCliente(action
				.getInt("id"));

		mm.beginTransaction();
		try {
			auditor.removerCliente(cliente);

			mm.commitTransaction();

			this.setResponseView(new EntidadeView(auditor));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuditorExternoClienteView(auditor));
			mm.rollbackTransaction();
		}
	}

	public void atualizarCliente(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) home
				.obterEntidadePorId(action.getLong("entidadeId"));

		AuditorExterno.Cliente cliente = auditor.obterCliente(action
				.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getLong("aseguradoraId") == 0)
				throw new Exception("Elegiré a Aseguradora");

			if (action.getDate("dataInicio") == null)
				throw new Exception("Elegiré a Fecha Inicio");

			if (action.getDate("dataFim") == null)
				throw new Exception("Elegiré a Fecha Cierre");

			Aseguradora aseguradora = (Aseguradora) home
					.obterEntidadePorId(action.getLong("aseguradoraId"));

			cliente.atualizar(aseguradora, action.getDouble("honorarios"),
					action.getDate("dataInicio"), action.getDate("dataInicio"),
					action.getString("outrosServicos"));

			this.setResponseView(new EntidadeView(auditor));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuditorExternoClienteView(cliente));
			mm.rollbackTransaction();
		}
	}

	public void novoServico(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("auditorId"));

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		this
				.setResponseView(new AuditorExternoServicoView(auditor,
						aseguradora));
	}

	public void visualizarServicos(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("auditorId"));

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		this.setResponseView(new AuditorExternoServicosView(auditor,
				aseguradora));
	}

	public void visualizarServico(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("auditorId"));

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		AuditorExterno.Servico servico = auditor.obterServico(aseguradora,
				action.getInt("id"));

		this.setResponseView(new AuditorExternoServicoView(servico));
	}

	public void incluirServico(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) home
				.obterEntidadePorId(action.getLong("auditorId"));

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("aseguradoraId"));

		mm.beginTransaction();
		try {

			if (action.getString("servico") == null
					|| action.getString("servico").equals(""))
				throw new Exception("Elegiré el Otro Servicio");

			if (action.getDate("dataContrato") == null)
				throw new Exception("Elegiré a Fecha Contrato");

			auditor.adicionarServico(aseguradora, action.getString("servico"),
					action.getDate("dataContrato"), action
							.getDouble("honorarios"), action
							.getString("periodo"));
			mm.commitTransaction();

			this.setResponseView(new AuditorExternoServicosView(auditor,
					aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuditorExternoServicoView(auditor,
					aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void excluirServico(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) entidadeHome
				.obterEntidadePorId(action.getLong("auditorId"));

		Aseguradora aseguradora = (Aseguradora) entidadeHome
				.obterEntidadePorId(action.getLong("aseguradoraId"));

		AuditorExterno.Servico servico = auditor.obterServico(aseguradora,
				action.getInt("id"));

		mm.beginTransaction();
		try {
			auditor.removerServico(servico);

			mm.commitTransaction();

			this.setResponseView(new AuditorExternoServicosView(auditor,
					aseguradora));
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuditorExternoServicosView(auditor,
					aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarServico(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) home
				.obterEntidadePorId(action.getLong("auditorId"));

		Aseguradora aseguradora = (Aseguradora) home.obterEntidadePorId(action
				.getLong("aseguradoraId"));

		AuditorExterno.Servico servico = auditor.obterServico(aseguradora,
				action.getInt("id"));

		mm.beginTransaction();
		try {
			if (action.getString("servico") == null
					|| action.getString("servico").equals(""))
				throw new Exception("Elegiré el Otro Servicio");

			if (action.getDate("dataContrato") == null)
				throw new Exception("Elegiré a Fecha Contrato");

			servico.atualizar(action.getString("servico"), action
					.getDate("dataContrato"), action.getDouble("honorarios"),
					action.getString("periodo"));

			this.setResponseView(new AuditorExternoServicosView(auditor,
					aseguradora));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new AuditorExternoServicosView(auditor,
					aseguradora));
			mm.rollbackTransaction();
		}
	}

	public void atualizarTecnologia(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");

		AuditorExterno auditor = (AuditorExterno) home
				.obterEntidadePorId(action.getLong("id"));

		mm.beginTransaction();
		try {
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());

					Entidade.Atributo entidadeAtributo = auditor
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(auditor));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(auditor));
			mm.rollbackTransaction();
		}
	}
}