package com.gvs.crm.control;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.ClassificacaoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Produto;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.view.EntidadeView;
import com.gvs.crm.view.OcorrenciasView;
import com.gvs.crm.view.PaginaInicialView;

import infra.control.Action;
import infra.control.Control;

public class ProdutoControl extends Control {
	public void atualizarFornecedor(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade fornecedor = entidadeHome.obterEntidadePorId(action
				.getLong("fornecedor"));
		double valor = action.getDouble("valor");
		mm.beginTransaction();
		try {
			produto.adicionarFornecedor(fornecedor, action
					.getString("codigoExterno"), valor);
			this.setResponseView(new EntidadeView(produto));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));

			mm.rollbackTransaction();
		}
	}

	public void atualizarPrecosProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		mm.beginTransaction();
		try {
			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("valor_")) {
					String tipo = key.substring(6, key.length());
					produto.adicionarPreco(tipo, action.getString("moeda_"
							+ tipo), action.getDouble("valor_" + tipo));
				}
			}
			String tabelaPrecos = action.getString("tabelaPrecos");
			if (!tabelaPrecos.equals(""))
				produto.adicionarPreco(tabelaPrecos, action.getString("moeda"),
						action.getDouble("valor"));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(produto));
	}

	public void atualizarProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade superior = (Entidade) entidadeHome.obterEntidadePorId(action
				.getLong("entidadeSuperiorId"));
		Entidade responsavelTecnico = (Entidade) entidadeHome
				.obterEntidadePorId(action.getLong("responsavelTecnicoId"));

		produto.atribuirApelido(action.getString("apelido"));
		produto.atribuirNome(action.getString("nome"));
		produto.atribuirSuperior(superior);

		if (action.getString("codigoExterno") == null
				|| action.getString("codigoExterno").equals(""))
			produto.atribuirCodigoExterno(action.getString("nome"));
		else
			produto.atribuirCodigoExterno(action.getString("codigoExterno"));

		String unidade = action.getString("unidade");
		if (unidade.equals(""))
			unidade = action.getString("novaunidade");
		produto.atribuirUnidade(unidade);
		produto.atribuirAtivo(action.getBoolean("ativo"));
		Set apelidos = new HashSet();
		String[] aps = action.getStringArray("apelidos");
		for (int i = 0; i < aps.length; i++)
			apelidos.add(aps[i]);
		produto.atribuirApelidos(apelidos);

		mm.beginTransaction();
		try {
			produto.atualizarPeso(action.getDouble("peso"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo")) {
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = produto
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			produto.atualizar();
			produto.atualizarTipoProduto(action.getString("tipoProduto"));

			this.setAlert("Produto atualizado");
			this.setResponseView(new EntidadeView(produto));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(produto));
			mm.rollbackTransaction();
		}
	}

	public void excluirFornecedor(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade fornecedor = entidadeHome.obterEntidadePorId(action
				.getLong("fornecedor"));
		mm.beginTransaction();
		try {
			produto.removerFornecedor(fornecedor);
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
		this.setResponseView(new EntidadeView(produto));
	}

	public void excluirProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		mm.beginTransaction();
		try {
			ClassificacaoProduto superior = (ClassificacaoProduto) entidadeHome
					.obterEntidadePorApelido("produtosexcluidos");

			produto.atribuirSuperior(superior);
			produto.atribuirAtivo(false);
			produto.atualizar();

			this.setResponseView(new PaginaInicialView(usuario, origemMenu));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(produto, origemMenu));
			mm.rollbackTransaction();
		}
	}

	public void incluirFornecedor(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade fornecedor = entidadeHome.obterEntidadePorId(action
				.getLong("fornecedorId"));

		Evento retorno = null;
		if (action.getLong("retornoId") != 0)
			retorno = (Evento) eventoHome.obterEventoPorId(action
					.getLong("retornoId"));

		double valor = action.getDouble("valor");
		mm.beginTransaction();
		try {
			produto.adicionarFornecedor(fornecedor, action
					.getString("codigoExterno"), valor);

			this.setResponseView(new EntidadeView(produto));

			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));

			mm.rollbackTransaction();
		}
	}

	public void incluirProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade superior = entidadeHome.obterEntidadePorId(action
				.getLong("superiorId"));
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario responsavel = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		Produto produto = (Produto) mm.getEntity("Produto");

		Entidade responsavelTecnico = (Entidade) entidadeHome
				.obterEntidadePorId(action.getLong("responsavelTecnicoId"));

		produto.atribuirApelido(action.getString("apelido"));
		produto.atribuirNome(action.getString("nome"));
		produto.atribuirSuperior(superior);
		produto.atribuirResponsavel(responsavel);
		Date data = new Date();

		produto.atribuirCodigoExterno("P" + data.getTime());

		String unidade = action.getString("unidade");
		if (unidade.equals(""))
			unidade = action.getString("novaunidade");
		produto.atribuirUnidade(unidade);
		produto.atribuirAtivo(true);
		Set apelidos = new HashSet();
		String[] aps = action.getStringArray("apelidos");
		for (int i = 0; i < aps.length; i++)
			apelidos.add(aps[i]);
		produto.atribuirApelidos(apelidos);
		mm.beginTransaction();
		try {
			produto.incluir();

			produto.atualizarTipoProduto(action.getString("tipoProduto"));

			produto.atualizarPeso(action.getDouble("peso"));

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo")) {
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = produto
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}
			this.setAlert("Produto incluído");
			this.setResponseView(new EntidadeView(produto));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(produto));
			mm.rollbackTransaction();
		}
	}

	public void novoFornecedor(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));

		Evento retorno = null;

		if (action.getLong("retornoId") != 0)
			retorno = (Evento) eventoHome.obterEventoPorId(action
					.getLong("retornoId"));

	}

	public void visualizarFornecedor(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		Entidade fornecedor = entidadeHome.obterEntidadePorId(action
				.getLong("fornecedor"));
	}

	public void visualizarOcorrencias(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
		this.setResponseView(new OcorrenciasView(produto));
	}

	public void visualizarPrecosProduto(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Produto produto = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("id"));
	}

	/*
	 * Atenção
	 */

	public void replicarProduto2(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade origemMenu = entidadeHome.obterEntidadePorId(action
				.getLong("origemMenuId"));
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");

		Usuario responsavelTecnico = (Usuario) usuarioHome
				.obterUsuarioPorUser(this.getUser());

		Produto produtoBase = (Produto) entidadeHome.obterEntidadePorId(action
				.getLong("produtoBase"));

		mm.beginTransaction();
		try {
			Produto produto = (Produto) mm.getEntity("Produto");

			String dimensao = produtoBase.obterAtributo("dimensao")
					.obterValor();

			if (action.getString("atributo_dimensao").equals(dimensao))
				throw new Exception(
						"Dimensão do produto Similar é igual ao do produto Base");

			/*
			 * System.out.println("Tela: " +
			 * action.getString("atributo_dimensao"));
			 * System.out.println("Produto Base: " + dimensao);
			 */

			produto.atribuirNome(produtoBase.obterNome());
			produto.atribuirSuperior(produtoBase.obterSuperior());
			produto.atribuirApelido("");
			produto.atribuirResponsavel(produtoBase.obterResponsavel());
			produto.atribuirUnidade(produtoBase.obterUnidade());
			produto.atribuirAtivo(true);
			produto.atribuirCodigoExterno("P" + new Date().getTime());
			produto.incluir();

			produto.atualizarPeso(action.getDouble("peso"));

			for (Iterator i = produtoBase.obterPrecos().iterator(); i.hasNext();) {
				Produto.Preco preco = (Produto.Preco) i.next();
				produto.adicionarPreco(preco.obterTipo(), preco.obterMoeda(),
						preco.obterValor());
			}

			for (Iterator i = produtoBase.obterFornecedores().iterator(); i
					.hasNext();) {
				Produto.Fornecedor fornecedor = (Produto.Fornecedor) i.next();
				produto.adicionarFornecedor(fornecedor.obterEntidade(),
						fornecedor.obterCodigoExterno(), fornecedor
								.obterValorFornecedor());
			}

			for (Iterator i = action.getParameters().keySet().iterator(); i
					.hasNext();) {
				String key = (String) i.next();
				if (key.startsWith("atributo_")) {
					String nome = key.substring(9, key.length());
					Entidade.Atributo entidadeAtributo = produto
							.obterAtributo(nome);
					entidadeAtributo.atualizarValor(action.getString(key));
				}
			}

			this.setResponseView(new EntidadeView(produto));
			mm.commitTransaction();
		} catch (Exception exception) {
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new EntidadeView(produtoBase));
			mm.rollbackTransaction();
		}
	}

	/*
	 * public void peso(Action action) throws Exception {
	 * 
	 * CRMModelManager mm = new CRMModelManager(this.getUser()); EntidadeHome
	 * entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome"); Entidade
	 * origemMenu =
	 * entidadeHome.obterEntidadePorId(action.getLong("origemMenuId"));
	 * UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
	 * EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
	 * ProdutoHome produtoHome = (ProdutoHome) mm.getHome("ProdutoHome");
	 * Usuario usuario = usuarioHome.obterUsuarioPorUser(this.getUser());
	 * 
	 * mm.beginTransaction();
	 * 
	 * try { for(Iterator i = produtoHome.obterProdutos().iterator() ;
	 * i.hasNext() ; ) { Produto produto = (Produto) i.next();
	 * 
	 * System.out.println("Produto Id: " + produto.obterId());
	 * 
	 * System.out.println("Produto Nome: " + produto.obterNome() + " " +
	 * produto.obterDimensao());
	 * 
	 * 
	 * String pesoAtributo = produto.obterAtributo("peso").obterValor();
	 * 
	 * System.out.println("Produto Peso: " + pesoAtributo);
	 * 
	 * if(pesoAtributo!=null && !pesoAtributo.equals("")) { double peso =
	 * Double.parseDouble(pesoAtributo); produto.atualizarPeso(peso); }
	 *  } mm.commitTransaction();
	 * 
	 * this.setResponseView(new PaginaInicialView(usuario, origemMenu)); } catch
	 * (Exception exception) {
	 * this.setAlert(Util.translateException(exception));
	 * this.setResponseView(new PaginaInicialView(usuario, origemMenu));
	 * mm.rollbackTransaction(); } }
	 */
}