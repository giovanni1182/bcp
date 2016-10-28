package com.gvs.crm.view;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeBCP;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class ApolicesRg001View extends PortalView
{
	private Date dataInicio;
	private Date dataFim;
	private Aseguradora aseguradora;
	private Collection<Apolice> apolices;
	private String situacaoSeguro;
	private boolean especial,modificado;
	
	public ApolicesRg001View(Aseguradora aseguradora, String situacaoSeguro, Date dataInicio, Date dataFim, Collection<Apolice> apolices, boolean especial, boolean modificado)
	{
		this.aseguradora = aseguradora;
		this.dataInicio = dataInicio;
		this.dataFim = dataFim;
		this.apolices = apolices;
		this.situacaoSeguro = situacaoSeguro;
		this.especial = especial;
		this.modificado = modificado;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		
		Table mainTable = new Table(1);
		mainTable.setWidth("100%");
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
		Table table = new Table(2); 
		
		table.addHeader("Aseguradora:");
		table.add(this.aseguradora.obterNome());
		table.addHeader("Situacion del Seguro:");
		if(this.situacaoSeguro.equals(""))
			table.add("Todas");
		else
			table.add(this.situacaoSeguro);
		
		table.addHeader("Periodo:");
		table.add(new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
		table.addHeader("Especial:");
		if(especial)
			table.add("Sí");
		else
			table.add("No");
		
		/*if(usuarioAtual.obterId() == 1)
		{
			table.addHeader("Modificado:");
			if(modificado)
				table.add("Sí");
			else
				table.add("No");
		}*/
		
		mainTable.add(table);
		
		table = new Table(5);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		table.addSubtitle(this.apolices.size() + " Póliza(s)");
		
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Número");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Asegurado");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Caiptal Gs");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Início de Vigencia");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("Final de Vigencia");
		
		Link link;
		Collection<EntidadeBCP> pessoas;
		Block blockV ;
		
		for(Apolice apolice : this.apolices)
		{
			pessoas = apolice.obterAsegurados();
			blockV = new Block(Block.HORIZONTAL);
			
			link = new Link(apolice.obterNumeroApolice(), new Action("visualizarEvento"));
			link.getAction().add("id", apolice.obterId());
			link.setNovaJanela(true);
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(link);
			
			blockV.add(new Label(apolice.obterNomeAsegurado()));
			
			for(EntidadeBCP pessoa : pessoas)
				blockV.add(new Label(pessoa.getNome() + " " + pessoa.getSobreNome()));
			
			table.add(blockV);
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(formataValor.format(apolice.obterCapitalGs()));
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new Label(apolice.obterDataPrevistaInicio(), "dd/MM/yyyy"));
			
			table.setNextHAlign(Table.HALIGN_CENTER);
			table.add(new Label(apolice.obterDataPrevistaConclusao(), "dd/MM/yyyy"));
		}
		
		mainTable.add(table);
		
		return mainTable;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return null;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
	}
}