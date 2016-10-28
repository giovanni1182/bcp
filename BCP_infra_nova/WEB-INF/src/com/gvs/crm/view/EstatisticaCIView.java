package com.gvs.crm.view;

import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Label;
import infra.view.Link;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class EstatisticaCIView extends PortalView
{
	private Usuario usuarioAtual;
	
	public EstatisticaCIView(Usuario usuarioAtual) throws Exception
	{
		this.usuarioAtual = usuarioAtual;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(1);
		CRMModelManager mm = new CRMModelManager(user);
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		
		Link link = null;
		
		if(opcaoHome.obterUsuarios(3).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 02 - Datos del Instrumento", new Action("estatisticaApolice"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(4).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 03 - Provisiones y Reservas", new Action("estatisticaReservas"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(5).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 04 - Reaseguros", new Action("estatisticaReaseguros"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(6).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 05 - Coaseguros", new Action("estatisticaCoaseguros"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(7).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 06 - Siniestros", new Action("estatisticaSinistros"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(8).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 07 - Pagos a proveedores", new Action("estatisticaPagos"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(9).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 08 - Anulación de Instrumento", new Action("estatisticaAnulacao"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(10).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 09 - Cobranza", new Action("estatisticaCobranca"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(11).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 10 - Demanda Judicial", new Action("estatisticaDemandaJudicial"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(12).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 11 - Endoso o Suplemento", new Action("estatisticaEndoso"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(13).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 12 - Finalización de Vigencia del Instrumento", new Action("estatisticaFinalizacao"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(14).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 13 - Refinanciación", new Action("estatisticaFinancimento"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(15).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 14 - Gastos", new Action("estatisticaGastos"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(16).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 15 - Anulación de Reaseguro", new Action("estatisticaAnulacaoReaseguros"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(17).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 16 - Morosidad", new Action("estatisticaMorosidade"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(18).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Registro 17 - Datos del Asegurado", new Action("estatisticaAsegurado"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		if(opcaoHome.obterUsuarios(19).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
		{
			link = new Link("Utilización del Sistema", new Action("estatisticaUsuario"));
			link.getAction().add("view", true);
			table.add(link);
			table.add(new Space());
		}
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return null;
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
		return new Label("Estadística CI");
	}
}