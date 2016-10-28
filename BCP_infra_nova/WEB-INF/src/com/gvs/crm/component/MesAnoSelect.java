package com.gvs.crm.component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.MovimentacaoFinanceiraConta;

import infra.view.Select;

public class MesAnoSelect extends Select {

	public MesAnoSelect(String nome, Entidade entidade) throws Exception {
		super(nome, 1);

		Collection datas = new ArrayList();

		for (Iterator i = entidade.obterEventosComoOrigem().iterator(); i
				.hasNext();) {
			Evento e = (Evento) i.next();
			if (e instanceof MovimentacaoFinanceiraConta) {
				MovimentacaoFinanceiraConta mf = (MovimentacaoFinanceiraConta) e;
				String mes = new SimpleDateFormat("MM").format(mf
						.obterDataPrevista());
				String ano = new SimpleDateFormat("yyyy").format(mf
						.obterDataPrevista());
				String mesAnoEvento = mes + " - " + ano;
				if (!datas.contains(mesAnoEvento))
					datas.add(mesAnoEvento);
			}
		}

		for (Iterator i = datas.iterator(); i.hasNext();) {
			String data = (String) i.next();
			this.add(data, data, true);
		}
	}
}