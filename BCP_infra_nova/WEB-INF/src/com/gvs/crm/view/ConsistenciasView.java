package com.gvs.crm.view;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Parametro;

import infra.control.Action;
import infra.view.Block;
import infra.view.Image;
import infra.view.Link;
import infra.view.Table;

public class ConsistenciasView extends Table {
	public ConsistenciasView(Entidade entidade) throws Exception {
		super(2);

		Parametro parametro = (Parametro) entidade;

		Map m = new TreeMap();

		for (Iterator i = parametro.obterConsistencias().iterator(); i
				.hasNext();) {
			Parametro.Consistencia consistencia = (Parametro.Consistencia) i
					.next();
			m.put(new Integer(consistencia.obterSequencial()), consistencia);
		}

		for (Iterator i = m.values().iterator(); i.hasNext();) {
			Block block = new Block(Block.HORIZONTAL);

			Parametro.Consistencia consistencia = (Parametro.Consistencia) i
					.next();

			if (parametro.permiteAtualizar()) {
				Action visualizarAction = new Action("visualizarConsistencia");
				visualizarAction.add("entidadeId", parametro.obterId());
				visualizarAction.add("id", consistencia.obterSequencial());
				visualizarAction.add("regra", consistencia.obterRegra());
				visualizarAction.add("origemMenuId", parametro.obterId());
				block.add(new Link(new Image("replace.gif"), visualizarAction));

				Action excluirAction = new Action("excluirConsistencia");
				excluirAction.add("entidadeId", parametro.obterId());
				excluirAction.add("id", consistencia.obterSequencial());
				excluirAction.add("regra", consistencia.obterRegra());
				excluirAction.setConfirmation("Confirma exclusión ?");
				block.add(new Link(new Image("delete.gif"), excluirAction));
			}
			this.addData(block);
			this.addHeader(consistencia.obterRegra() + " - "
					+ consistencia.obterSequencial() + " - "
					+ consistencia.obterOperando1() + " "
					+ consistencia.obterOperador() + " "
					+ consistencia.obterOperando2() + " : "
					+ consistencia.obterMensagem());
		}

	}

}