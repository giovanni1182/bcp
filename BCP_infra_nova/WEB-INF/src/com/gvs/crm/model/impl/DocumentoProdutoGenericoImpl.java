package com.gvs.crm.model.impl;

import com.gvs.crm.model.DocumentoProdutoGenerico;

public class DocumentoProdutoGenericoImpl extends EventoImpl implements
		DocumentoProdutoGenerico {
	/*
	 * public void atualizarDocumento(EntidadeDocumento documento) throws
	 * Exception { SQLUpdate update =
	 * this.getModelManager().createSQLUpdate("crm", "update documento_produto
	 * set documento=? where id=?"); update.addLong(documento.obterId());
	 * update.addLong(this.obterId()); update.execute(); }
	 * 
	 * public void atualizarNumero(String numero) throws Exception { SQLUpdate
	 * update = this.getModelManager().createSQLUpdate("crm", "update
	 * documento_produto set numero=? where id=?"); update.addString(numero);
	 * update.addLong(this.obterId()); update.execute(); }
	 * 
	 * public void atualizarReferente(String referente) throws Exception {
	 * SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "update
	 * documento_produto set ref=? where id=?"); update.addString(referente);
	 * update.addLong(this.obterId()); update.execute(); }
	 * 
	 * public void atualizarAnalista(Entidade analista) throws Exception {
	 * SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "update
	 * documento_produto set analista=? where id=?");
	 * update.addLong(analista.obterId()); update.addLong(this.obterId());
	 * update.execute(); }
	 * 
	 * public void atualizarChefeDivisao(Entidade chefeDivisao) throws Exception {
	 * SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "update
	 * documento_produto set chefe=? where id=?");
	 * update.addLong(chefeDivisao.obterId()); update.addLong(this.obterId());
	 * update.execute(); }
	 * 
	 * public void atualizarIntendente(Entidade intendente) throws Exception {
	 * SQLUpdate update = this.getModelManager().createSQLUpdate("crm", "update
	 * documento_produto set intendente=? where id=?");
	 * update.addLong(intendente.obterId()); update.addLong(this.obterId());
	 * update.execute(); }
	 * 
	 * public void atualizarSuperIntendente(Entidade superIntendente) throws
	 * Exception { SQLUpdate update =
	 * this.getModelManager().createSQLUpdate("crm", "update documento_produto
	 * set superintendente=? where id=?");
	 * update.addLong(superIntendente.obterId());
	 * update.addLong(this.obterId()); update.execute(); }
	 * 
	 * public void incluir() throws Exception { super.incluir();
	 * 
	 * SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "insert
	 * into documento_produto(id) values(?)"); insert.addLong(this.obterId());
	 * insert.execute(); }
	 * 
	 * public void exclurDocumentoVinculado(DocumentoProduto documentoVinculado)
	 * throws Exception { SQLUpdate delete =
	 * this.getModelManager().createSQLUpdate("crm", "delete evento_entidades
	 * where id=? and sub_evento=?"); delete.addLong(this.obterId());
	 * delete.addLong(documentoVinculado.obterId()); delete.execute(); }
	 * 
	 * public Collection obterDocumentosVinculados() throws Exception { SQLQuery
	 * query = this.getModelManager().createSQLQuery("crm", "select sub_evento
	 * from evento_entidades where id=?"); query.addLong(this.obterId());
	 * 
	 * Collection documentos = new ArrayList();
	 * 
	 * SQLRow[] rows = query.execute();
	 * 
	 * for(int i = 0 ; i < rows.length ; i++) { EventoHome home = (EventoHome)
	 * this.getModelManager().getHome("EventoHome");
	 * 
	 * documentos.add(home.obterEventoPorId(rows[i].getLong("sub_evento"))); }
	 * 
	 * return documentos; }
	 * 
	 * public void adicionarDocumentoVinculado(DocumentoProduto documento)
	 * throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from
	 * evento_entidades where id=?"); query.addLong(this.obterId());
	 * 
	 * int id = query.executeAndGetFirstRow().getInt("MX") + 1;
	 * 
	 * SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "insert
	 * into evento_entidades(id, seq, sub_evento) values (?, ?, ?)");
	 * insert.addLong(this.obterId()); insert.addInt(id);
	 * insert.addLong(documento.obterId()); insert.execute(); }
	 * 
	 * public EntidadeDocumento obterDocumento() throws Exception { SQLQuery
	 * query = this.getModelManager().createSQLQuery("crm", "select documento
	 * from documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * long id = query.executeAndGetFirstRow().getLong("documento");
	 * 
	 * EntidadeDocumento documento = null;
	 * 
	 * if(id > 0) { EntidadeHome home = (EntidadeHome)
	 * this.getModelManager().getHome("EntidadeHome");
	 * 
	 * documento = (EntidadeDocumento) home.obterEntidadePorId(id); }
	 * 
	 * return documento; }
	 * 
	 * public String obterNumero() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select numero from
	 * documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * return query.executeAndGetFirstRow().getString("numero");
	 *  }
	 * 
	 * public String obterReferente() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select ref from
	 * documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * return query.executeAndGetFirstRow().getString("ref");
	 *  }
	 * 
	 * public Entidade obterAnalista() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select analista from
	 * documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * long id = query.executeAndGetFirstRow().getLong("analista");
	 * 
	 * Entidade analista = null;
	 * 
	 * if(id > 0) { EntidadeHome home = (EntidadeHome)
	 * this.getModelManager().getHome("EntidadeHome");
	 * 
	 * analista = home.obterEntidadePorId(id); }
	 * 
	 * return analista; }
	 * 
	 * public Entidade obterChefeDivisao() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select chefe from
	 * documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * long id = query.executeAndGetFirstRow().getLong("chefe");
	 * 
	 * Entidade chefe = null;
	 * 
	 * if(id > 0) { EntidadeHome home = (EntidadeHome)
	 * this.getModelManager().getHome("EntidadeHome");
	 * 
	 * chefe = home.obterEntidadePorId(id); }
	 * 
	 * return chefe; }
	 * 
	 * public Entidade obterIntendente() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select intendente from
	 * documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * long id = query.executeAndGetFirstRow().getLong("intendente");
	 * 
	 * Entidade intendente = null;
	 * 
	 * if(id > 0) { EntidadeHome home = (EntidadeHome)
	 * this.getModelManager().getHome("EntidadeHome");
	 * 
	 * intendente = home.obterEntidadePorId(id); }
	 * 
	 * return intendente; }
	 * 
	 * public Entidade obterSuperIntendente() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select superintendente from
	 * documento_produto where id=?"); query.addLong(this.obterId());
	 * 
	 * long id = query.executeAndGetFirstRow().getLong("superintendente");
	 * 
	 * Entidade superIntendente = null;
	 * 
	 * if(id > 0) { EntidadeHome home = (EntidadeHome)
	 * this.getModelManager().getHome("EntidadeHome");
	 * 
	 * superIntendente = home.obterEntidadePorId(id); }
	 * 
	 * return superIntendente; }
	 * 
	 * public String obterDescricao() throws Exception { SQLQuery query =
	 * this.getModelManager().createSQLQuery("crm", "select descricao from
	 * informe where id=?"); query.addLong(this.obterId());
	 * 
	 * return query.executeAndGetFirstRow().getString("descricao"); }
	 */
}