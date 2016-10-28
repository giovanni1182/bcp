package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import com.gvs.crm.model.Corretora;
import com.gvs.crm.model.Entidade;

import infra.view.InputDate;
import infra.view.InputPassword;
import infra.view.InputString;
import infra.view.InputText;
import infra.view.Select;
import infra.view.Table;

public class EntidadeAtributosView extends Table {
	public EntidadeAtributosView(Entidade entidade) throws Exception
	{
		super(2);

		Map m = new TreeMap();

		for (Iterator i = entidade.obterAtributos().iterator(); i.hasNext();)
		{
			Entidade.Atributo atributo = (Entidade.Atributo) i.next();
			if (entidade instanceof Corretora)
			{
				if (!atributo.obterNome().equals("nomeabreviado")
						&& !atributo.obterNome().equals("tipo"))
					m.put(new Integer(atributo.obterOrdem()), atributo);
			}
			else
				m.put(new Integer(atributo.obterOrdem()), atributo);
		}

		for (Iterator i = m.values().iterator(); i.hasNext();)
		{
			Entidade.Atributo atributo = (Entidade.Atributo) i.next();
			this.addHeader(atributo.obterTitulo() + ":");
			String valor = atributo.obterValor();
			switch (atributo.obterTipo())
			{
			case 'C':
				this.addData(new InputString(
						"atributo_" + atributo.obterNome(), valor, atributo
								.obterTamanho()));
				break;
			case 'N':
				this.addData(new InputString(
						"atributo_" + atributo.obterNome(), valor, atributo
								.obterTamanho()));
				break;
			case 'D':
				Date valorDate = null;
				if (valor != null && !valor.equals(""))
					valorDate = new SimpleDateFormat("dd/MM/yyyy")
							.parse(atributo.obterValor());
				this.addData(new InputDate("atributo_" + atributo.obterNome(),
						valorDate));
				break;
			case 'T':
				this.addData(new InputText("atributo_" + atributo.obterNome(),
						valor, 15, atributo.obterTamanho()));
				break;
			case 'P':
				String asteriscos = "";
				String senha = atributo.obterValor();
				if (senha == null || senha.length() == 0)
					this.addData(new InputPassword("atributo_senha", 32));
				else {
					for (int j = 0; j < atributo.obterValor().length(); j++)
						asteriscos += "*";

					this.addData(new InputString("atributo_"
							+ atributo.obterNome(), asteriscos, atributo
							.obterTamanho()));
				}
				break;
			case 'S':
				Collection valoresTitulos = new ArrayList();
				StringTokenizer st = new StringTokenizer(atributo
						.obterValoresTitulos(), ";");
				while (st.hasMoreTokens())
				{
					valoresTitulos.add(st.nextToken());
				}
				
				Select select = new Select("atributo_" + atributo.obterNome(),1);
				for (Iterator i2 = valoresTitulos.iterator(); i2.hasNext();)
				{
					String nomeValor = (String) i2.next();
					StringTokenizer st2 = new StringTokenizer(nomeValor, ":");
					String valorTitulo = st2.nextToken();
					String valorSelect = st2.nextToken();
					select.add(valorTitulo, valorSelect, valorSelect.equals(valor));
				}
				this.addData(select);
				break;
			}
		}
	}
}