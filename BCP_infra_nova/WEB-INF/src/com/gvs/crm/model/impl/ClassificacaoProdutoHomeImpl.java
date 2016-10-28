package com.gvs.crm.model.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import com.gvs.crm.model.ClassificacaoProduto;
import com.gvs.crm.model.ClassificacaoProdutoHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Produto;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class ClassificacaoProdutoHomeImpl extends Home implements
		ClassificacaoProdutoHome {
	public Collection obterTabelasPreco(
			ClassificacaoProduto classificacaoProduto) throws Exception {
		Collection tipos = new HashSet();
		for (Iterator i = classificacaoProduto.obterInferiores().iterator(); i
				.hasNext();) {
			Entidade e = (Entidade) i.next();
			if (e instanceof ClassificacaoProduto) {
				tipos.addAll(this.obterTabelasPreco((ClassificacaoProduto) e));
			} else if (e instanceof Produto) {
				SQLQuery query = this
						.getModelManager()
						.createSQLQuery(
								"crm",
								"select produto_preco.tipo from produto_preco where produto_preco.id=? group by produto_preco.tipo");
				query.addLong(e.obterId());
				SQLRow[] rows = query.execute();
				for (int i2 = 0; i2 < rows.length; i2++)
					tipos.add(rows[i2].getString("tipo"));
			}
		}
		return tipos;
	}
}