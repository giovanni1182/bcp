package com.gvs.crm.model;

import java.util.Collection;

/*
 * Interface que representa um evento de Impressão.
 */
public interface Impressao extends Evento {

	/*
	 * Interface que representa um componente de impressão.
	 */
	public interface Componente {
		String obterComponenteNome();

		String obterDescricao();

		long obterComponenteOrdem();

		Evento obterLinkVariavel();

		boolean permiteNegrito();

		boolean permiteItalico();

		boolean permiteSublinhado();

		boolean estaVisivel();

		int obterSaltoDeLinha();
	}

	/*
	 * Metodo que obtem uma coleção de componentes de impressão.
	 */
	Collection obterComponentes() throws Exception;

	/*
	 * Metodo que inclui um componente.
	 */
	void incluirComponente(String componente, String Descricao,
			boolean negrito, boolean italico, boolean sublinhado)
			throws Exception;

	void incluirComponente(String componente, String Descricao,
			long linkVariavel, boolean negrito, boolean italico,
			boolean sublinhado) throws Exception;

	void atualizarComponente(String componente, long ordem, long ordemAtual)
			throws Exception;

	void incluirComponenteComOrdem(String componente, String descricao,
			long ordem, boolean negrito, boolean italico, boolean sublinhado)
			throws Exception;

	/*
	 * Setar o proximo salto de linha
	 */
	void setNextSalto(int salto) throws Exception;

	/*
	 * Metodo que exclui um componente.
	 */
	void excluirComponente(String componente, long ordem) throws Exception;

	// 0 visivel, 1 invisivel
	void atualizarComponenteVisivel(int visivel, long ordem, Impressao impressao)
			throws Exception;

	/*
	 * Obter lista de componentes disponiveis para impressão.
	 */
	public Collection obterListaComponentes() throws Exception;

	/*
	 * Metodo que ordena um componente.
	 */
	void ordenarComponente(long ordem, String direcao) throws Exception;

	/*
	 * Metodo que ordena varios componentes
	 */
	void ordenarComponenteDistante(long aSer, long aPos) throws Exception;
}