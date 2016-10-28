package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.SeparadorLabel;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class RelatorioMenuView extends PortalView {
	private final String SALDOS = "1";

	private final String OUTRA_MOEDA = "2";

	private final String BALANCE_GENERAL = "3";

	private final String RESULTADOS_RESUMIDOS = "4";

	private final String MODELO = "5";

	private final String CLASSIFICACAO = "6";

	private Entidade entidade, origemMenu;

	private boolean usuarioAtual;

	private String nivel;

	private String mesAno;

	private String mes;

	private String ano;

	private Date dataDe;

	private Date dataAte;

	private boolean lista;

	private boolean acumuladorPorAseguradora;

	public RelatorioMenuView(Entidade entidade, Entidade origemMenu,
			boolean usuarioAtual, String nivel, String mes, String ano,
			boolean lista) throws Exception {
		this.entidade = entidade;
		this.origemMenu = origemMenu;
		this.usuarioAtual = usuarioAtual;
		this.nivel = nivel;
		this.mes = mes;
		this.ano = ano;
		this.mesAno = mes + ano;
		this.lista = lista;
	}

	public RelatorioMenuView(Entidade entidade, Entidade origemMenu,
			boolean usuarioAtual, String nivel, String mes, String ano,
			boolean lista, boolean acumuladorPorAseguradora) throws Exception {
		this.entidade = entidade;
		this.origemMenu = origemMenu;
		this.usuarioAtual = usuarioAtual;
		this.nivel = nivel;
		this.mes = mes;
		this.ano = ano;
		this.mesAno = mes + ano;
		this.lista = lista;
		this.acumuladorPorAseguradora = acumuladorPorAseguradora;
	}

	/*
	 * public RelatorioMenuView(Entidade entidade, Entidade origemMenu, boolean
	 * usuarioAtual, String nivel, Date dataDe, Date dataAte, boolean lista)
	 * throws Exception { this.entidade = entidade; this.origemMenu =
	 * origemMenu; this.usuarioAtual = usuarioAtual; this.nivel = nivel;
	 * this.dataDe = dataDe; this.dataAte = dataAte; this.mesAno = mes + ano;
	 * this.lista = lista; }
	 * 
	 * public RelatorioMenuView(Entidade entidade, Entidade origemMenu, boolean
	 * usuarioAtual, String nivel, Date dataDe, Date dataAte, boolean lista,
	 * boolean acumuladorPorAseguradora) throws Exception { this.entidade =
	 * entidade; this.origemMenu = origemMenu; this.usuarioAtual = usuarioAtual;
	 * this.nivel = nivel; this.dataDe = dataDe; this.dataAte = dataAte;
	 * this.mesAno = mes + ano; this.lista = lista;
	 * this.acumuladorPorAseguradora = acumuladorPorAseguradora; }
	 */

	public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
		String _eventos = properties.getProperty("_eventos", this.SALDOS);
		String _tipoRelatorio = properties.getProperty("_tipo", "");

		if (this.nivel.equals(""))
			this.nivel = "Capítulo";

		// criar as opções de filtro por tipo de evento
		Block block1 = new Block(Block.HORIZONTAL);

		Label saldosLabel = new Label("Saldos");
		saldosLabel.setBold(_eventos.equals(this.SALDOS));
		Link saldosLink = new Link(saldosLabel, new Action(
				"visualizarRelatorioAseguradora"));
		saldosLink.getAction().add("id", this.entidade.obterId());
		saldosLink.getAction().add("_eventos", this.SALDOS);
		saldosLink.getAction().add("lista", true);
		block1.add(saldosLink);
		block1.add(new SeparadorLabel());

		Label outraMoedaLabel = new Label("Otra Moneda");
		outraMoedaLabel.setBold(_eventos.equals(this.OUTRA_MOEDA));
		Link outraMoedaLink = new Link(outraMoedaLabel, new Action(
				"visualizarRelatorioAseguradora"));
		outraMoedaLink.getAction().add("id", this.entidade.obterId());
		outraMoedaLink.getAction().add("_eventos", this.OUTRA_MOEDA);
		outraMoedaLink.getAction().add("lista", true);
		block1.add(outraMoedaLink);
		block1.add(new SeparadorLabel());

		Label balancoLabel = new Label("Balance General");
		balancoLabel.setBold(_eventos.equals(this.BALANCE_GENERAL));
		Link balancoLink = new Link(balancoLabel, new Action(
				"visualizarRelatorioAseguradora"));
		balancoLink.getAction().add("id", this.entidade.obterId());
		balancoLink.getAction().add("_eventos", this.BALANCE_GENERAL);
		block1.add(balancoLink);
		block1.add(new SeparadorLabel());

		Label resumidoLabel = new Label("Resultados Resumidos");
		resumidoLabel.setBold(_eventos.equals(this.RESULTADOS_RESUMIDOS));
		Link resumidoLink = new Link(resumidoLabel, new Action(
				"visualizarRelatorioAseguradora"));
		resumidoLink.getAction().add("id", this.entidade.obterId());
		resumidoLink.getAction().add("_eventos", this.RESULTADOS_RESUMIDOS);
		block1.add(resumidoLink);
		block1.add(new SeparadorLabel());

		Label modeloLabel = new Label("Modelo del Estado de Variación");
		modeloLabel.setBold(_eventos.equals(this.MODELO));
		Link modeloLink = new Link(modeloLabel, new Action(
				"visualizarRelatorioAseguradoraModeloEstado"));
		modeloLink.getAction().add("id", this.entidade.obterId());
		modeloLink.getAction().add("_eventos", this.MODELO);
		modeloLink.getAction().add("lista", false);
		block1.add(modeloLink);
		block1.add(new SeparadorLabel());

		Label classificacaoLabel = new Label("Clasificación de Empresas");
		classificacaoLabel.setBold(_eventos.equals(this.CLASSIFICACAO));
		Link classificacaoLink = new Link(classificacaoLabel, new Action(
				"visualizarRelatorioAseguradora"));
		classificacaoLink.getAction().add("id", this.entidade.obterId());
		classificacaoLink.getAction().add("_eventos", this.CLASSIFICACAO);
		//classificacaoLink.getAction().add("lista", false);
		block1.add(classificacaoLink);

		Collection entidades = new ArrayList();

		if (this.lista)
		{
			if (this.nivel.equals("Nivel 1"))
				entidades = this.entidade.obterEntidadesNivel1(entidade, this.mesAno);
			else if (this.nivel.equals("Nivel 2"))
				entidades = this.entidade.obterEntidadesNivel2(entidade, this.mesAno);
			else if (this.nivel.equals("Nivel 3"))
				entidades = this.entidade.obterEntidadesNivel3(entidade, this.mesAno);
			else if (this.nivel.equals("Nivel 4"))
				entidades = this.entidade.obterEntidadesNivel4(entidade, this.mesAno);
			else if (this.nivel.equals("Nivel 5"))
				entidades = this.entidade.obterEntidadesNivel5(entidade, this.mesAno);
			else
				entidades = new ArrayList();
		}

		// cria a tabela ser retornada

		Table table = new Table(1);
		table.addStyle(Table.STYLE_BLANK);
		table.setWidth("100%");
		table.add(block1);

		Action action = new Action("visualizarRelatorioAseguradora");
		action.add("id", this.entidade.obterId());

		if (_eventos.equals(SALDOS))
			table.add(new RelatorioView(entidade, entidades, origemMenu,
					usuarioAtual, this.nivel, this.mes, this.ano));
		else if (_eventos.equals(OUTRA_MOEDA))
			table.add(new RelatorioOutraMoedaView(entidade, entidades,
					origemMenu, usuarioAtual, this.nivel, this.mes, this.ano));
		else if (_eventos.equals(BALANCE_GENERAL))
		{
			Aseguradora aseguradora = (Aseguradora) entidade;

			if (this.lista && this.acumuladorPorAseguradora)
			{
				CRMModelManager mm = new CRMModelManager(user);

				AseguradoraHome home = (AseguradoraHome) mm.getHome("AseguradoraHome");

				Collection aseguradoras = new ArrayList();

				aseguradoras = home.obterAseguradorasRelatorio(this.mes	+ this.ano);

				table.add(new BalancoGeralAcumuladorPorAseguradoraView(aseguradora, this.lista, this.mes, this.ano,this.acumuladorPorAseguradora, aseguradoras));
			}
			else if (_tipoRelatorio.equals("acumulado"))
				table.add(new BalancoGeralAcumuladoView(aseguradora,this.lista, this.dataDe, this.dataAte));
			else
				table.add(new BalancoGeralView(aseguradora, this.lista,	this.mes, this.ano));
		}
		else if (_eventos.equals(RESULTADOS_RESUMIDOS))
		{
			Aseguradora aseguradora = (Aseguradora) entidade;

			if (this.lista && this.acumuladorPorAseguradora) {
				CRMModelManager mm = new CRMModelManager(user);

				AseguradoraHome home = (AseguradoraHome) mm
						.getHome("AseguradoraHome");

				Collection aseguradoras = new ArrayList();

				//aseguradoras = home.obterAseguradoras();

				aseguradoras = home.obterAseguradorasRelatorio(this.mes
						+ this.ano);

				table.add(new ResultadoResumidoAcumuladoPorAseguradoraView(
						aseguradora, this.lista, this.mes, this.ano,
						this.acumuladorPorAseguradora, aseguradoras));
			} else if (_tipoRelatorio.equals("acumulado"))
				table.add(new ResultadoResumidoAcumuladoView(aseguradora,
						this.lista, this.dataDe, this.dataAte));
			else
				table.add(new ResultadoResumidoView(aseguradora, this.lista,
						this.mes, this.ano));
		} else if (_eventos.equals(CLASSIFICACAO)) {
			Aseguradora aseguradora = (Aseguradora) entidade;

			table.add(new ResultadoResumidoView(aseguradora, this.lista,
					this.mes, this.ano));
		}

		return table;

	}

	public String getSelectedGroup() throws Exception {
		return this.entidade.obterNome();
	}

	public String getSelectedOption() throws Exception {
		return "Entidades";
	}

	public View getTitle() throws Exception {
		if (this.acumuladorPorAseguradora)
			return new Label("Acumulado por Aseguradoras");
		else
			return new Label("Listados - " + this.entidade.obterNome());
	}

	public Entidade getOrigemMenu() throws Exception {
		if (this.origemMenu != null && this.origemMenu.obterId() != 0)
			return this.origemMenu;
		else
			return this.entidade;
	}

}