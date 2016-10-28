package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.component.DuracaoLabel;
import com.gvs.crm.component.EventoImage;
import com.gvs.crm.component.EventoTituloLink;
import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.Renovacao;

import infra.view.Block;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;

public class SubEventosView extends Table {
	public SubEventosView(Evento superior) throws Exception {
		super(6);
		this.setWidth("100%");
		this.addStyle(Table.STYLE_ALTERNATE);
		this.addHeader("Título");
		this.addHeader("Fase");
		this.addHeader("Responsable");
		this.addHeader("Início");
		this.addHeader("Conclusión");
		this.setNextHAlign(Table.HALIGN_RIGHT);
		this.addHeader("Duración");
		this.adicionarInferiores(0, superior);
	}

	private void adicionarInferiores(int nivel, Evento superior)
			throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		DecimalFormat df = new DecimalFormat("0");
		Map inferiores = new TreeMap();
		for (Iterator i = superior.obterInferiores().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			String classe = e.obterClasse();
			if ((!(e instanceof Renovacao)) && !classe.equals("meta")
					&& !classe.equals("itemproduto") && !classe.equals("item")
					&& !classe.equals("movimentacaofinanceira")
					&& !classe.equals("participacao")
					&& !classe.equals("desconto")
					&& !classe.equals("itemstella")
					&& !classe.equals("movimentacaoproduto")) {
				StringBuffer key = new StringBuffer();
				if (e.obterDataPrevistaInicio() == null)
					key.append("99999999");
				else
					key.append(sdf.format(e.obterDataPrevistaInicio()));
				key.append(df.format(e.obterPrioridade()));
				key.append(sdf.format(e.obterCriacao()));
				key.append(Long.toString(e.obterId()));
				inferiores.put(key.toString(), e);
			}
		}
		Space espacos = new Space(nivel * 4);
		for (Iterator i = inferiores.values().iterator(); i.hasNext();) {
			Evento e = (Evento) i.next();
			Block blockTitulo = new Block(Block.HORIZONTAL);
			blockTitulo.add(espacos);
			blockTitulo.add(new EventoImage(e));
			blockTitulo.add(new Space());
			blockTitulo.add(new EventoTituloLink(e));
			if (e instanceof DocumentoProduto) {
				DocumentoProduto documento = (DocumentoProduto) e;

				blockTitulo.add(new Label(documento.obterDocumento()
						.obterNome()));
			}

			this.addData(blockTitulo);
			this.addData(e.obterFase().obterNome());
			if(e.obterResponsavel()!=null)
				this.addData(e.obterResponsavel().obterNome());
			else
				this.addData("");
			
			if (e.obterDataPrevistaInicio() == null)
				this.addData("");
			else
				this.addData(new Label(e.obterDataPrevistaInicio(),
						"EEE dd/MM/yyyy"));
			if (e.obterDataPrevistaConclusao() == null)
				this.addData("");
			else
				this.addData(new Label(e.obterDataPrevistaConclusao(),
						"EEE dd/MM/yyyy"));
			this.setNextHAlign(Table.HALIGN_RIGHT);
			if (e.obterDuracao() == 0)
				this.addData("");
			else
				this.addData(new DuracaoLabel(e.obterDuracao()));
			this.adicionarInferiores(nivel + 1, e);
		}
	}
}