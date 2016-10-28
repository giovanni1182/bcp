package com.gvs.crm.view;

import java.util.Collection;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;

import infra.view.Label;
import infra.view.Table;

public class RelatorioClassificacaoEmpresasView extends Table
{
	public RelatorioClassificacaoEmpresasView(Collection aseguradorasVida,Collection aseguradorasPatrimonio, String mes, String ano) throws Exception 
	{
		super(6);

		this.addSubtitle("");

		this.setNextColSpan(this.getColumns());
		this.setNextHAlign(super.HALIGN_CENTER);
		this.addHeader("AUTORIZADAS A OPERAR EN LOS RAMOS ELEMETALES Y VIDA");

		this.add("");
		this.add("");
		this.addHeader("M.S");
		this.addHeader("F.G");
		this.addHeader("F.R");
		this.addHeader("P.P.N.C");

		int cont = 1;

		for (Iterator i = aseguradorasVida.iterator(); i.hasNext();) 
		{
			Aseguradora aseguradora = (Aseguradora) i.next();

			this.add(new Label(cont));

			this.add(aseguradora.obterNome());

			this.add(new Label(aseguradora.obterMargemSolvencia()));

			for (Iterator j = aseguradora.obterClassificacao(mes, ano).iterator(); j.hasNext();) 
			{
				Aseguradora.Classificacao classificacao = (Aseguradora.Classificacao) j.next();

				if (classificacao.obterNome().equals("Fundo de Garantia"))
					this.add(new Label(classificacao.obterValor()));
				else if (classificacao.obterNome().equals("FRR"))
					this.add(new Label(classificacao.obterValor()));
				else if (classificacao.obterNome().equals("FRP"))
					this.add(new Label(classificacao.obterValor()));
				else if (classificacao.obterNome().equals("PPNC"))
					this.add(new Label(classificacao.obterValor()));
			}
		}
	}
}