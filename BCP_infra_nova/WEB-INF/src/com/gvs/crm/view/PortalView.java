package com.gvs.crm.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Raiz;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Image;
import infra.view.Label;
import infra.view.Link;
import infra.view.Menu;
import infra.view.MenuPrincipal;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public abstract class PortalView extends BasicView {
	class Option {
		private Action action;

		private String caption;

		private String group;

		Option(String group, String caption, Action action) {
			this.group = group;
			this.caption = caption;
			this.action = action;
		}

		public Action getAction() {
			return this.action;
		}

		public String getCaption() {
			return this.caption;
		}

		public String getGroup() {
			return this.group;
		}
	}

	private Collection options = new ArrayList();

	private void addOption(String group, String caption, Action action) {
		this.options.add(new Option(group, caption, action));
	}

	public final View execute(User user, Locale locale, Properties properties)
			throws Exception {
		String s = properties.getProperty("_entidadeId");
		long entidadeId = 0;
		if (s != null)
			entidadeId = Long.parseLong(s);
		if (getOrigemMenu() != null)
			entidadeId = getOrigemMenu().obterId();

		CRMModelManager mm = new CRMModelManager(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		Entidade entidade = entidadeHome.obterEntidadePorId(entidadeId);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(user);
		OpcaoHome opcaoHome = (OpcaoHome) mm.getHome("OpcaoHome");
		if (usuarioAtual == null)
			usuarioAtual = usuarioHome.obterUsuarioPorChave("admin");
		
		Collection<Integer> opcoes = opcaoHome.obterOpcoes(usuarioAtual);
		String nivel = usuarioAtual.obterNivel();
		
		Table mainTable = new Table(2);
	    mainTable.setWidth("100%");
	    mainTable.addStyle("maintable");
	    
	    Label l = new Label("Usuário: " + usuarioAtual.obterNome());
        l.setBold(true);
        Block block = new Block(Block.HORIZONTAL);
        block.add(new Image("user.gif"));
        block.add(new Space(4));
        block.add(l);
        
        Link link = new Link(new Image("help.gif"), new Action("abrirHelp"));
		link.getAction().add("id", entidade.obterId());
		link.setNote("Help");
		block.add(new Space(10));
		block.add(link);
        
        Table titleTable = new Table(1);
        titleTable.setWidth("100%");
        titleTable.add(block);
        
        MenuPrincipal menuP = new MenuPrincipal();
        Menu menu = new Menu("Inicial");
        menu.addSubMenu(new Menu("Página Inicial", new Action("visualizarPaginaInicial")));
        
        /*Action action = new Action("visualizarEventosEntidade");
        action.add("id", usuarioAtual.obterId());
        menu.addSubMenu(new Menu("Histórico de Eventos", action));*/
        Action action = null;
        
        if (usuarioAtual.obterId() == 1 || nivel.equals(Usuario.ADMINISTRADOR)) 
		{
        	action = new Action("exibirLogs");
        	action.add("id", usuarioAtual.obterId());
        	action.add("view", true);
        	menu.addSubMenu(new Menu("Mirar Logs", action));
        	
        	action = new Action("pesquisaRucCi");
        	action.add("id", usuarioAtual.obterId());
        	action.add("view", true);
        	menu.addSubMenu(new Menu("Pesquisa RUC/CI", action));
		}
        
    	if(opcoes.contains(1) || opcoes.contains(2) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("exibirAnuario");
			action.add("id", usuarioAtual.obterId());
			menu.addSubMenu(new Menu("Boletín", action));
		}
		
    	if(usuarioAtual.obterNome().toLowerCase().indexOf("e70") == -1)
    	{
			action = new Action("alterarSenhaUsuario");
			action.add("id", usuarioAtual.obterId());
			menu.addSubMenu(new Menu("Cambiar Contraseña", action));
    	}
		
		if(opcoes.contains(64) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("novaEntidade");
			action.add("superiorId", -1);
			menu.addSubMenu(new Menu("Nuevo Catastro", action));
		}

		if(opcoes.contains(65) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("novoEvento");
			action.add("passo", 2);
			menu.addSubMenu(new Menu("Nuevo Evento", action));
		}
		
		if(opcoes.contains(105) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("novoEvento");
			action.add("classe", "envioarquivo");
			menu.addSubMenu(new Menu("Nuevo Envio de Archivos", action));
		}
		
		if(opcoes.contains(106) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("relArquivosEnviados");
			action.add("view", true);
			menu.addSubMenu(new Menu("Archivos Enviados", action));
		}
		
		if(nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("relArquivosEmProcessamento");
			menu.addSubMenu(new Menu("Cola de Procesamiento", action));
		}
        
        menuP.addMenu(menu);
        
        boolean criarMenu = false;
        
        if(opcoes.contains(35) || nivel.equals(Usuario.ADMINISTRADOR))
		{
        	menu = new Menu("Entidades/Eventos");
			menu.addSubMenu(new Menu("Buscar", new Action("localizar")));
			menu.addSubMenu(new Menu("Buscar Instrumentos", new Action("localizarApolices")));
			menu.addSubMenu(new Menu("Buscar Siniestros", new Action("localizarSinistros")));
			criarMenu = true;
		}
        
        if(opcoes.contains(79) || nivel.equals(Usuario.ADMINISTRADOR))
        {
        	if(!criarMenu)
        		menu = new Menu("Entidades/Eventos");
        	
        	action = new Action("localizarResolucaoScanner");
			action.add("view", true);
			menu.addSubMenu(new Menu("Buscar Resolución Escaneada", action));
			criarMenu = true;
        }
        
        if(opcoes.contains(104) || nivel.equals(Usuario.ADMINISTRADOR))
        {
        	if(!criarMenu)
        		menu = new Menu("Entidades/Eventos");
        	
        	action = new Action("grupoAlertaTrempana");
			action.add("view", true);
			
			menu.addSubMenu(new Menu("Grupo para Alerta Temprana", action));
			criarMenu = true;
        }
        
        if(opcoes.contains(104) || nivel.equals(Usuario.ADMINISTRADOR))
        {
        	if(!criarMenu)
        		menu = new Menu("Entidades/Eventos");
        	
        	action = new Action("relCodigosInstrumento");
			action.add("view", true);
			
			menu.addSubMenu(new Menu("Tabla de Instrumentos", action));
			criarMenu = true;
        }
        
        if(criarMenu)
        	menuP.addMenu(menu);
        
        criarMenu = false;
        menu = new Menu("Producción");
        
		if(opcoes.contains(20) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarProdutividadeAseguradora");
	    	action.add("lista", false);
			menu.addSubMenu(new Menu("Aseguradora - Pólizas", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(21) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReaseguros");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Aseguradora - Reaseguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(22) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReasegurosReaseguradora");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Reaseguradora - Reaseguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(31) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReasegurosCorredor");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Aseguradora - Corredora de Reaseguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(94) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarProdutividadeAgentesCorredores");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Aseguradora - Agente y Corredor", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(32) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarReasegurosCorredorAseguradora");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Corredora de Reaseguros - Aseguradoras", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(23) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarProdutividadeAuxiliar");
			action.add("view", true);
			action.add("auxiliar", true);
			menu.addSubMenu(new Menu("Agente de Seguro", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(66) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarProdutividadeAuxiliar");
			action.add("view", true);
			menu.addSubMenu(new Menu("Corredor de Seguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(46) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarInsricoes");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Listado Control SIS", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(47) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarApolicesSuspeitas");
			action.add("lista", false);
			menu.addSubMenu(new Menu("Operaciones Sospechosas", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(48) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarPlanos");
			action.add("id", usuarioAtual.obterId());
			action.add("view", true);
			menu.addSubMenu(new Menu("Listado Planes", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(49) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarCodificacaoPlanos");
			action.add("id", usuarioAtual.obterId());
			menu.addSubMenu(new Menu("Listado Cod. Planes Siniestro", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(55) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarUsuariosCadastrados");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listado Usuarios", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(80) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("relPlanoRG");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listado con Plan RG.0001", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(87) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("relPlanoRG");
			action.add("view", true);
			action.add("especial", true);
			menu.addSubMenu(new Menu("Listado con Plan Especial", action));
			criarMenu = true;
		}
		
		/*if(nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("relPlanoRG");
			action.add("view", true);
			action.add("modificado", true);
			menu.addSubMenu(new Menu("Listado con Plan Modificado", action));
			criarMenu = true;
		}*/
		
		if(opcoes.contains(28) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("apolicesPorSecao");
			action.add("view", true);
			menu.addSubMenu(new Menu("Pólizas por Sección", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(29) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("apolicesPorSecaoAnual");
			action.add("view", true);
			menu.addSubMenu(new Menu("Pólizas por Sección Anual", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(92) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("apolicesModalidade");
			action.add("view", true);
			menu.addSubMenu(new Menu("Pólizas por Modalidad", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(93) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("apolicesPorTipoPessoa");
			action.add("view", true);
			menu.addSubMenu(new Menu("Pólizas por Tipo Persona", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(30) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarGEE");
			action.add("view", true);
			menu.addSubMenu(new Menu("Información contable para GEE", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(69) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("alterarPlanos");
			action.add("view", true);
			menu.addSubMenu(new Menu("Inc/Mod de Sec o Modalidad", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(78) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("segmentarPlanos");
			action.add("view", true);
			menu.addSubMenu(new Menu("Estructura de los Planes", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(89) || nivel.equals(Usuario.ADMINISTRADOR))
	    {
			action = new Action("relFmi");
			action.add("view", true);
			menu.addSubMenu(new Menu("Información requerida FMI", action));
			criarMenu = true;
	    }
	    
		if(opcoes.contains(90) || nivel.equals(Usuario.ADMINISTRADOR))
	    {
			action = new Action("relFmi4Nivel");
			action.add("view", true);
			menu.addSubMenu(new Menu("Cuentas hasta cuarto nivel", action));
			criarMenu = true;
	    }
		
	    if(opcoes.contains(91) || nivel.equals(Usuario.ADMINISTRADOR))
	    {
	    	action = new Action("relFmiBancos");
	    	action.add("view", true);
	    	menu.addSubMenu(new Menu("Información Bancos y Financieras", action));
	    	criarMenu = true;
	    }
	    
	    if(opcoes.contains(100) || nivel.equals(Usuario.ADMINISTRADOR))
	    {
	    	action = new Action("relCotacaoDolar");
	    	action.add("view", true);
	    	menu.addSubMenu(new Menu("Listado Cotizaciones del Dólar", action));
	    	criarMenu = true;
	    }
	    
	    if(opcoes.contains(101) || nivel.equals(Usuario.ADMINISTRADOR))
	    {
	    	action = new Action("relVariacaoIPC");
	    	action.add("view", true);
	    	menu.addSubMenu(new Menu("Listado Variaciones IPC", action));
	    	criarMenu = true;
	    }
		
		if(criarMenu)
			menuP.addMenu(menu);
		
		if(opcoes.contains(37) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu = new Menu("Central de Riesgos");
			
			action = new Action("visualizarCentralRiscoSinistro");
			action.add("lista", false);
			action.add("view", true);
			menu.addSubMenu(new Menu("Siniestros", action));
						
			action = new Action("visualizarCentralRiscoMorosidade");
			action.add("lista", false);
			action.add("view", true);
			menu.addSubMenu(new Menu("Morosidades", action));
			
			menuP.addMenu(menu);
		}
		
		criarMenu = false;
		
		menu = new Menu("Pagina WEB");
		if(opcoes.contains(26) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("selecionarRelEntidadesVigentes");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listados Entidades Vigentes", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(27) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("selecionarRelAseguradorasPlanos");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listados Aseguradoras Planes", action));
			criarMenu = true;
		}
		if(criarMenu)
			menuP.addMenu(menu);
		
		criarMenu = false;
		menu = new Menu("Seguimiento");
		
		if(opcoes.contains(20) || opcoes.contains(21) || opcoes.contains(22) || opcoes.contains(23) || opcoes.contains(24) || opcoes.contains(25) || opcoes.contains(26) || opcoes.contains(27) || opcoes.contains(28) || opcoes.contains(29) || opcoes.contains(30) || opcoes.contains(31) || opcoes.contains(32) || opcoes.contains(33) || opcoes.contains(34) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarInspecaoSitu");
			action.add("view", true);
			menu.addSubMenu(new Menu("Inspección in situ", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(24) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarEstatistica");
			action.add("view", true);
			menu.addSubMenu(new Menu("Mayores Ocurrencias Pólizas", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(71) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("maioresSinistros");
			action.add("view", true);
			menu.addSubMenu(new Menu("Mayores Ocurrencias Siniestros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(33) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarConsolidado");
			action.add("view", true);
			menu.addSubMenu(new Menu("Consolidado Póliza/Sección", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(34) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarApoliceSinistroAnual");
			action.add("view", true);
			menu.addSubMenu(new Menu("Histórico Pólizas/Siniestros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(59) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu.addSubMenu(new Menu("Ultimas Agendas", new Action("visualizarCentralRiscoAgendas")));
			criarMenu = true;
		}
		
		if(opcoes.contains(77) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu.addSubMenu(new Menu("Ultimos Libros Electrónicos", new Action("visualizarCentralRiscoLivros")));
			criarMenu = true;
		}
		
		if(opcoes.contains(60) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu.addSubMenu(new Menu("Agendar/Desagendar Validación", new Action("visualizarCentralAgendarDesagendar")));
			criarMenu = true;
		}
		
		if(opcoes.contains(61) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu.addSubMenu(new Menu("Control de Agenda Instrumento", new Action("visualizarControleAgendas")));
			criarMenu = true;
		}
		
		if(opcoes.contains(62) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("demandaJudicial");
			action.add("view", true);
			menu.addSubMenu(new Menu("Demanda Judicial", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(63) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("estatisticaCI");
			action.add("view", true);
			menu.addSubMenu(new Menu("Estadística CI", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(67) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("maioresAgentes");
			action.add("view", true);
			action.add("auxiliar", true);
			menu.addSubMenu(new Menu("Mayores Agentes", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(75) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("comissaoAgentes");
			action.add("view", true);
			action.add("auxiliar", true);
			menu.addSubMenu(new Menu("Comisión Agentes por Sección", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(68) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("maioresAgentes");
			action.add("view", true);
			menu.addSubMenu(new Menu("Mayores Corredores de Seguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(76) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("comissaoAgentes");
			action.add("view", true);
			menu.addSubMenu(new Menu("Comisión Corredores de Seguros por Sección", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(83) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("maioresCorretores");
			action.add("view", true);
			menu.addSubMenu(new Menu("Mayores Corredores de Reaseguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(81) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("maioresRaseguradoras");
			action.add("view", true);
			menu.addSubMenu(new Menu("Mayores Reaseguradoras", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(82) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("maioresRaseguradorasPorSecao");
			action.add("view", true);
			menu.addSubMenu(new Menu("Mayores Reaseguradoras por Sección", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(84) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("selecionarRelEntidadesVigentes");
			action.add("view", true);
			action.add("ci", true);
			menu.addSubMenu(new Menu("Listado Entidades Vigentes con identificación", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(85) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("qtdeApoliceReaseguros");
			action.add("view", true);
			menu.addSubMenu(new Menu("Cantidad de Pólizas/Reaseguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(86) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("coberturaReaseguros");
			action.add("view", true);
			menu.addSubMenu(new Menu("Cobertura de Reaseguros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(88) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("relProcessamentos");
			action.add("view", true);
			menu.addSubMenu(new Menu("Validaciones", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(95) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("calculoRatios");
			action.add("view", true);
			menu.addSubMenu(new Menu("Calculo de Ratios Financieros", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(96) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("calculoRatios");
			action.add("view", true);
			action.add("calculoRatios1", true);
			menu.addSubMenu(new Menu("Calculo de Ratios Financieros 1", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(98) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("calculoRatios");
			action.add("view", true);
			action.add("ratiosAgregados", true);
			menu.addSubMenu(new Menu("Ratios Agregados", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(97) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("calculoRatios");
			action.add("view", true);
			action.add("ratiosAgregados1", true);
			menu.addSubMenu(new Menu("Ratios Agregados 1", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(99) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("calculoMargemSolvencia");
			action.add("view", true);
			menu.addSubMenu(new Menu("Margen de Solvencia", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(103) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("calculoAlertaTrempano");
			action.add("view", true);
			menu.addSubMenu(new Menu("Alerta Temprana", action));
			criarMenu = true;
			
			action = new Action("calculoAlertaTrempano");
			action.add("view", true);
			action.add("alerta2", true);
			menu.addSubMenu(new Menu("Alerta Temprana 2", action));
			criarMenu = true;
		}
		
		if(criarMenu)
			menuP.addMenu(menu);
		
		if(opcoes.contains(39) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu = new Menu("Indicadores");
			menu.addSubMenu(new Menu("Visualizar", new Action("visualizarIndicadores")));
			menuP.addMenu(menu);
		}
		
		if(opcoes.contains(102) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu = new Menu("Infor. del Mercado");
			
			action = new Action("calculoInformacaoMercado");
			action.add("view", true);
			menu.addSubMenu(new Menu("Información del Mercado", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Activos");
			menu.addSubMenu(new Menu("Activo", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Pasivos");
			menu.addSubMenu(new Menu("Pasivo", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Patrimonio Neto");
			menu.addSubMenu(new Menu("Patrimonio Neto", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Ingresos");
			menu.addSubMenu(new Menu("Ingresos", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Egresos");
			menu.addSubMenu(new Menu("Egresos", action));
			
			action = new Action("calculoResultadoResumido");
			action.add("view", true);
			menu.addSubMenu(new Menu("Resultado Resumido", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Siniestros Netos Ocurridos");
			menu.addSubMenu(new Menu("Siniestros Netos Ocurridos", action));
			
			action = new Action("calculoInformacaoMercadoGeral");
			action.add("view", true);
			action.add("tipo", "Otros Ingresos y Egresos Tecnicos");
			menu.addSubMenu(new Menu("Otros Ingresos y Egresos Tecnicos", action));
			
			menuP.addMenu(menu);
		}
		
		if(nivel.equals(Usuario.ADMINISTRADOR) || nivel.equals(Usuario.BANCO_DE_DADOS))
		{
			menu = new Menu("Liber. de Opciones");
			
			action = new Action("liberarOpcoes");
			action.add("view", true);
			menu.addSubMenu(new Menu("Visualizar", action));
			
			action = new Action("relOpcoes");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listado", action));
			
			action = new Action("relOpcoesResEscaneada");
			action.add("view", true);
			menu.addSubMenu(new Menu("Resolución Escaneada", action));
			
			menuP.addMenu(menu);
		}
		
		if(opcoes.contains(1) || opcoes.contains(2) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu = new Menu("Plan de Cuentas");
			
			action = new Action("exibirAnuario");
			action.add("view", true);
			action.add("geral", true);
			menu.addSubMenu(new Menu("Visualizar", action));
			menuP.addMenu(menu);
		}
		
		if(nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("visualizarHierarquiaEntidade");
			action.add("id", entidade.obterId());
			menu = new Menu("Jerarquía", action);
			menuP.addMenu(menu);
			
			action = new Action("manutBase");
			action.add("id", usuarioAtual.obterId());
			menu = new Menu("Manut. BD", action);
			//menuP.addMenu(menu);
		}
		
		criarMenu = false;
		menu = new Menu("Libros Elec.");
		if(opcoes.contains(72) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("novoLivro");
			action.add("view", true);
			menu.addSubMenu(new Menu("Nuevo Libro", action));
			criarMenu = true;
		}
		
		if(opcoes.contains(73) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("consultarLivros");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listado Libros Electrónicos", action));
			criarMenu = true;
		}
			
		if(opcoes.contains(74) || nivel.equals(Usuario.ADMINISTRADOR))
		{
			action = new Action("livrosPendentes");
			action.add("view", true);
			menu.addSubMenu(new Menu("Listado Libros Pendientes", action));
			criarMenu = true;
		}
		
		if(criarMenu)
			menuP.addMenu(menu);
		
		if(nivel.equals(Usuario.ADMINISTRADOR))
		{
			menu = new Menu("Testes");
			
			action = new Action("downloadFtp");
			menu.addSubMenu(new Menu("Archivos FTP", action));
			
			action = new Action("enviarEmail");
			action.add("view", true);
			menu.addSubMenu(new Menu("Envio do Email", action));
			
			menuP.addMenu(menu);
		}

		/*Action paginaInicialAction = new Action("visualizarPaginaInicial");
		paginaInicialAction.add("origemMenuId", entidade.obterId());
		this.addOption("Menú Principal", "Página Inicial", paginaInicialAction);*/
		
		//if(!usuarioAtual.obterNivel().equals(Usuario.EXTERNO))
		//{
			
			/*Action paginaPruebaAction = new Action("visualizarPaginaPrueba");
			paginaPruebaAction .add("origemMenuId", entidade.obterId());
			this.addOption("Menú Principal", "Página de Prueba", paginaPruebaAction );*/        
			
			
			//Action responsabilidadesAction = new
			// Action("visualizarResponsabilidadesUsuario");
			//        responsabilidadesAction.add("id", usuarioAtual.obterId());
			//        responsabilidadesAction.add("origemMenuId", entidade.obterId());
			//        this.addOption("Menú Principal", "Responsabilidades",
			// responsabilidadesAction);
	
			//        Action importarAction = new Action("importar");
			//        this.addOption("Menú Principal", "Importar Contas", importarAction);
	
			/*
			 * Action auxiliaresAction = new Action("visualizarAuxiliares");
			 * auxiliaresAction.add("id", usuarioAtual.obterId());
			 * this.addOption("Menú Principal", "Lista Auxiliares",
			 * auxiliaresAction);
			 */
	
			/*Action eventosUsuarioAction = new Action("visualizarEventosEntidade");
			eventosUsuarioAction.add("id", usuarioAtual.obterId());
			eventosUsuarioAction.add("origemMenuId", entidade.obterId());
			this.addOption("Menú Principal", "Histórico de Eventos",
					eventosUsuarioAction);*/
	
			/*if (usuarioAtual.obterId() == 1 || nivel.equals(Usuario.ADMINISTRADOR)) 
			{
				Action logsAction = new Action("exibirLogs");
				logsAction.add("id", usuarioAtual.obterId());
				logsAction.add("view", true);
				this.addOption("Menú Principal", "Mirar Logs", logsAction);*/
				
				/*Action apoliceAction = new Action("manutBase");
				apoliceAction.add("id", usuarioAtual.obterId());
				apoliceAction.add("origemMenuId", entidade.obterId());
				this.addOption("Menú Principal", "Manut Base", apoliceAction);*/
				
				/*Action apoliceAction = new Action("excluirDuplicidadeApolice");
				apoliceAction.add("id", usuarioAtual.obterId());
				apoliceAction.add("origemMenuId", entidade.obterId());
				apoliceAction.add("excluir", false);
				this.addOption("Menú Principal", "Manut.Anulacao", apoliceAction);*/
	
				//	        Action cargaAction = new Action("carregarPlanos");
				//	        cargaAction.add("id", usuarioAtual.obterId());
				//	        this.addOption("Menú Principal", "Carga Planos", cargaAction);
			//}
			
			/*Entidade ieta = entidadeHome.obterEntidadePorApelido("intendenteieta");
			if(ieta !=null )
			{
				if(usuarioAtual.obterSuperiores().contains(ieta) || usuarioAtual.obterId() == 1 || usuarioAtual.obterChave().toLowerCase().equals("pgonza") || nivel.equals(Usuario.ADMINISTRADOR))
				{
					Action anuarioAction = new Action("exibirAnuario");
					anuarioAction.add("id", usuarioAtual.obterId());
					anuarioAction.add("origemMenuId", entidade.obterId());
					this.addOption("Menú Principal", "Boletín", anuarioAction);
				}
			}
			else if(usuarioAtual.obterId() == 1 || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action anuarioAction = new Action("exibirAnuario");
				anuarioAction.add("id", usuarioAtual.obterId());
				anuarioAction.add("origemMenuId", entidade.obterId());
				this.addOption("Menú Principal", "Anuario", anuarioAction);
			}*/
	
			/*Action alterarSenhaAction = new Action("alterarSenhaUsuario");
			alterarSenhaAction.add("id", usuarioAtual.obterId());
			alterarSenhaAction.add("origemMenuId", entidade.obterId());
			this.addOption("Menú Principal", "Cambiar Contraseña...",
					alterarSenhaAction);*/
	
			/*if (usuarioAtual.obterId() == 1 || usuarioAtual.obterId() == 3 ||nivel.equals(Usuario.ADMINISTRADOR))
			{
				  Action importarAction2 = new Action("initialDownload");
				  importarAction2.add("id", usuarioAtual.obterId());
				  importarAction2.add("origemMenuId", entidade.obterId());
				  this.addOption("Menú Principal", "Importar...", importarAction2);
				  
				  Action agendaAction = new Action("verificarAgendas");
				  agendaAction.add("id", usuarioAtual.obterId());
				  agendaAction.add("origemMenuId", entidade.obterId());
				  this.addOption("Menú Principal", "Agendas em Atraso...",
				  agendaAction);
				 
	
				Action excluirAction = new Action("excluirDuplicidade");
				excluirAction.add("id", usuarioAtual.obterId());
				excluirAction.add("origemMenuId", entidade.obterId());
				this.addOption("Menú Principal", "Excluir Duplicidade...",
						excluirAction);
	
				//	        Action descritivoAction = new Action("descritivo");
				//	        descritivoAction.add("id", usuarioAtual.obterId());
				//	        descritivoAction.add("origemMenuId", entidade.obterId());
				//	        this.addOption("Menú Principal", "Descritivo...",
				// descritivoAction);
				
				Action carregarAction = new Action("carregarRUC");
				carregarAction.add("id", usuarioAtual.obterId());
				carregarAction.add("origemMenuId", entidade.obterId());
				carregarAction.add("view", true);
				this.addOption("Menú Principal", "Cargar RUC...",carregarAction);
	
			}*/
	
			/*if(opcoes.contains(64) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action novaEntidadeAction = new Action("novaEntidade");
				novaEntidadeAction.add("superiorId", -1);
				novaEntidadeAction.add("origemMenuId", entidade.obterId());
				this.addOption("Menú Principal", "Nuevo Catastro...",novaEntidadeAction);
			}
	
			if(opcoes.contains(65) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action novoEventoAction = new Action("novoEvento");
				novoEventoAction.add("passo", 2);
				novoEventoAction.add("origemMenuId", entidade.obterId());
				this.addOption("Menú Principal", "Nuevo Evento...", novoEventoAction);
			}*/
			
			/*if(opcoes.contains(35) || nivel.equals(Usuario.ADMINISTRADOR))
			//if(opcaoHome.obterUsuarios(35).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			{
				Action localizarAction = new Action("localizar");
				localizarAction.add("origemMenuId", entidade.obterId());
				this.addOption("Entidades/Eventos", "Buscar", localizarAction);
		
				Action localizarApoliceAction = new Action("localizarApolices");
				localizarApoliceAction.add("origemMenuId", entidade.obterId());
				this.addOption("Entidades/Eventos", "Buscar Instrumentos",localizarApoliceAction);
				
				Action localizarPlanoAction = new Action("localizarPlanos");
				localizarPlanoAction.add("origemMenuId", entidade.obterId());
				this.addOption("Entidades/Eventos", "Buscar Planes",localizarPlanoAction);
				
				Action localizarPlanoAction = new Action("localizarSinistros");
				localizarPlanoAction.add("origemMenuId", entidade.obterId());
				this.addOption("Entidades/Eventos", "Buscar Siniestros",localizarPlanoAction);
			}*/
			
			/*Action produtot1Action = new Action("visualizarProdutividadeAseguradora");
			produtot1Action.add("origemMenuId", entidade.obterId());
			produtot1Action.add("lista", false);
			//if(opcaoHome.obterUsuarios(40).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(20) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Aseguradora - Pólizas", produtot1Action);*/
	
			/*Action produtot5Action = new Action("visualizarReaseguros");
			produtot5Action.add("origemMenuId", entidade.obterId());
			produtot5Action.add("lista", false);
			//if(opcaoHome.obterUsuarios(41).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(21) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Aseguradora - Reaseguros",produtot5Action);*/
	
			/*Action produtot6Action = new Action("visualizarReasegurosReaseguradora");
			produtot6Action.add("origemMenuId", entidade.obterId());
			produtot6Action.add("lista", false);
			//if(opcaoHome.obterUsuarios(42).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(22) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Reaseguradora - Reaseguros",produtot6Action);*/
	
			//if(opcaoHome.obterUsuarios(43).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(31) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action produtot7Action = new Action("visualizarReasegurosCorredor");
				produtot7Action.add("origemMenuId", entidade.obterId());
				produtot7Action.add("lista", false);
				this.addOption("Producción", "Aseguradora - Corredora de Reaseguros", produtot7Action);
			}*/
	
			/*Action produtot8Action = new Action("visualizarReasegurosCorredorAseguradora");
			produtot8Action.add("origemMenuId", entidade.obterId());
			produtot8Action.add("lista", false);
			//if(opcaoHome.obterUsuarios(44).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(32) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Corredora de Reaseguros - Aseguradoras",produtot8Action);*/
	
			/*Action produtot2Action = new Action("visualizarProdutividadeAuxiliar");
			produtot2Action.add("origemMenuId", entidade.obterId());
			produtot2Action.add("view", true);
			produtot2Action.add("auxiliar", true);
			//if(opcaoHome.obterUsuarios(45).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(23) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Agente de Seguro", produtot2Action);*/
			
			//produtot2Action = new Action("visualizarProdutividadeCorredor");
			
			/*produtot2Action = new Action("visualizarProdutividadeAuxiliar");
			produtot2Action.add("origemMenuId", entidade.obterId());
			produtot2Action.add("view", true);
			//if(opcaoHome.obterUsuarios(45).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(66) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Corredor de Seguros", produtot2Action);*/
	
			//if(opcaoHome.obterUsuarios(46).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(46) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action produtot3Action = new Action("visualizarInsricoes");
				produtot3Action.add("origemMenuId", entidade.obterId());
				produtot3Action.add("lista", false);
				this.addOption("Producción", "Listado Control SIS", produtot3Action);
			}*/
			
			//if(opcaoHome.obterUsuarios(47).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(47) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action produtot4Action = new Action("visualizarApolicesSuspeitas");
				produtot4Action.add("origemMenuId", entidade.obterId());
				produtot4Action.add("lista", false);
				this.addOption("Producción", "Operaciones Sospechosas",	produtot4Action);
			}*/
		
			//if(opcaoHome.obterUsuarios(48).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(48) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action planoAction = new Action("visualizarPlanos");
				planoAction.add("id", usuarioAtual.obterId());
				planoAction.add("view", true);
				this.addOption("Producción", "Listado Planes", planoAction);
			}*/
		
			//if(opcaoHome.obterUsuarios(49).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(49) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action planoSinistroAction = new Action("visualizarCodificacaoPlanos");
				planoSinistroAction.add("id", usuarioAtual.obterId());
				this.addOption("Producción", "Listado Cod. Planes Siniestro", planoSinistroAction);
			}*/
			
			/*Action relListadosAction = new Action("selecionarRelEntidadesVigentes");
			relListadosAction.add("view", true);
			//if(opcaoHome.obterUsuarios(50).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(26) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Listados Entidades Vigentes", relListadosAction);*/
			
			/*Action relListados2Action = new Action("selecionarRelAseguradorasPlanos");
			relListados2Action.add("view", true);
			//if(opcaoHome.obterUsuarios(51).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(27) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Listados Aseguradoras Planes", relListados2Action);*/
			
			/*Action relListados3Action = new Action("apolicesPorSecao");
			relListados3Action.add("view", true);
			//if(opcaoHome.obterUsuarios(52).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(28) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Pólizas por Sección", relListados3Action);*/
			
			/*Action relListados5Action = new Action("apolicesPorSecaoAnual");
			relListados5Action.add("view", true);
			//if(opcaoHome.obterUsuarios(53).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(29) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Producción", "Pólizas por Sección Anual", relListados5Action);*/
			
			//if(opcaoHome.obterUsuarios(54).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(30) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action relListados4Action = new Action("visualizarGEE");
				relListados4Action.add("view", true);
				this.addOption("Producción", "Información contable para GEE", relListados4Action);
			}*/
			
			//if(opcaoHome.obterUsuarios(55).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(55) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action relListados4Action = new Action("visualizarUsuariosCadastrados");
				relListados4Action.add("view", true);
				this.addOption("Producción", "Listado Usuarios", relListados4Action);
			}*/
			
			/*if(opcoes.contains(69) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action relListados4Action = new Action("alterarPlanos");
				relListados4Action.add("view", true);
				this.addOption("Producción", "Inc/Mod de Sec o Modalidad", relListados4Action);
			}*/
			
			//if(opcaoHome.obterUsuarios(37).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(37) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction = new Action("visualizarCentralRiscoSinistro");
				centralAction.add("origemMenuId", entidade.obterId());
				centralAction.add("lista", false);
				this.addOption("Central de Riesgos", "Siniestros", centralAction);
				
				Action centralAction2 = new Action("visualizarCentralRiscoMorosidade");
				centralAction2.add("origemMenuId", entidade.obterId());
				centralAction2.add("lista", false);
				this.addOption("Central de Riesgos", "Morosidades", centralAction2);
			}*/
			
			//if(opcaoHome.obterUsuarios(38).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(20) || opcoes.contains(21) || opcoes.contains(22) || opcoes.contains(23) || opcoes.contains(24) || opcoes.contains(25) || opcoes.contains(26) || opcoes.contains(27) || opcoes.contains(28) || opcoes.contains(29) || opcoes.contains(30) || opcoes.contains(31) || opcoes.contains(32) || opcoes.contains(33) || opcoes.contains(34) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action segAction = new Action("visualizarInspecaoSitu");
				segAction.add("origemMenuId", entidade.obterId());
				segAction.add("view", true);
				this.addOption("Seguimiento", "Inspección in situ", segAction);
			}*/
			
			/*Action centralAction4 = new Action("visualizarEstatistica");
			centralAction4.add("origemMenuId", entidade.obterId());
			centralAction4.add("view", true);
			//if(opcaoHome.obterUsuarios(24).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(24) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Seguimiento", "Mayores Ocurrencias", centralAction4);*/
			
			/*Action centralAction5 = new Action("visualizarTotalApoliceSinistro");
			centralAction5.add("origemMenuId", entidade.obterId());
			centralAction5.add("view", true);
			//this.addOption("Seguimiento", "Cant. Pólizas/Siniestros", centralAction5);
*/			
			/*Action centralAction6 = new Action("visualizarConsolidado");
			centralAction6.add("origemMenuId", entidade.obterId());
			centralAction6.add("view", true);
			//if(opcaoHome.obterUsuarios(33).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(33) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Seguimiento", "Consolidado Póliza/Sección", centralAction6);*/
			
			/*Action centralAction8 = new Action("visualizarApoliceSinistroAnual");
			centralAction8.add("origemMenuId", entidade.obterId());
			centralAction8.add("view", true);
			//if(opcaoHome.obterUsuarios(34).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(34) || nivel.equals(Usuario.ADMINISTRADOR))
				this.addOption("Seguimiento", "Histórico Pólizas/Siniestros", centralAction8);*/
			
			//if(opcaoHome.obterUsuarios(38).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			/*if(opcoes.contains(59) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction3 = new Action("visualizarCentralRiscoAgendas");
				centralAction3.add("origemMenuId", entidade.obterId());
				this.addOption("Seguimiento", "Ultimas Agendas", centralAction3);
			}*/
			
			/*if(opcoes.contains(60) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction7 = new Action("visualizarCentralAgendarDesagendar");
				centralAction7.add("origemMenuId", entidade.obterId());
				this.addOption("Seguimiento", "Agendar/Desagendar Validación", centralAction7);
			}*/
			
			/*if(opcoes.contains(61) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction9 = new Action("visualizarControleAgendas");
				centralAction9.add("origemMenuId", entidade.obterId());
				this.addOption("Seguimiento", "Control de Agenda Instrumento", centralAction9);
			}
			
			if(opcoes.contains(62) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction9 = new Action("demandaJudicial");
				centralAction9.add("origemMenuId", entidade.obterId());
				centralAction9.add("view", true);
				this.addOption("Seguimiento", "Demanda Judicial", centralAction9);
			}
			
			if(opcoes.contains(63) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction9 = new Action("estatisticaCI");
				centralAction9.add("origemMenuId", entidade.obterId());
				centralAction9.add("view", true);
				this.addOption("Seguimiento", "Estadística CI", centralAction9);
			}
			
			if(opcoes.contains(67) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction9 = new Action("maioresAgentes");
				centralAction9.add("origemMenuId", entidade.obterId());
				centralAction9.add("view", true);
				centralAction9.add("auxiliar", true);
				this.addOption("Seguimiento", "Mayores Agentes", centralAction9);
			}
			
			if(opcoes.contains(68) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action centralAction9 = new Action("maioresAgentes");
				centralAction9.add("origemMenuId", entidade.obterId());
				centralAction9.add("view", true);
				this.addOption("Seguimiento", "Mayores Corredores de Seguros", centralAction9);
			}
			
			//if(opcaoHome.obterUsuarios(39).contains(usuarioAtual) || usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			if(opcoes.contains(39) || nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action indicadorAction = new Action("visualizarIndicadores");
				indicadorAction.add("origemMenuId", entidade.obterId());
				this.addOption("Indicadores", "Visualizar", indicadorAction);
			}*/
			
			/*if(nivel.equals(Usuario.ADMINISTRADOR))
			{
				Action indicadorAction = new Action("liberarOpcoes");
				indicadorAction.add("origemMenuId", entidade.obterId());
				indicadorAction.add("view", true);
				this.addOption("Liberación de opciones", "Visualizar", indicadorAction);
				
				indicadorAction = new Action("relOpcoes");
				indicadorAction.add("origemMenuId", entidade.obterId());
				indicadorAction.add("view", true);
				this.addOption("Liberación de opciones", "Listado", indicadorAction);
			}*/
			
			//if(opcaoHome.obterUsuarios(1).contains(usuarioAtual) || opcaoHome.obterUsuarios(2).contains(usuarioAtual))
			/*if(opcoes.contains(1) || opcoes.contains(2))
			{
				Action indicadorAction = new Action("exibirAnuario");
				indicadorAction.add("origemMenuId", entidade.obterId());
				indicadorAction.add("view", true);
				indicadorAction.add("geral", true);
				this.addOption("Plan de Cuentas", "Visualizar", indicadorAction);
			}*/
			
			/*Action emailAction = new Action("enviarEmail");
			emailAction.add("origemMenuId", entidade.obterId());
			this.addOption("Email", "Enviar", emailAction);*/
			
			if (entidade != null) 
			{
				if(entidade instanceof Raiz)
					entidade = usuarioAtual;
				
				Action entidadeAction = new Action("visualizarDetalhesEntidade");
				entidadeAction.add("id", entidade.obterId());
				this.addOption(entidade.obterNome(), "Detalles", entidadeAction);
	
				Action hierarquiaAction = new Action("visualizarHierarquiaEntidade");
				hierarquiaAction.add("id", entidade.obterId());
				this.addOption(entidade.obterNome(), "Jerarquía", hierarquiaAction);
	
				if (entidade instanceof Aseguradora)
				{
					Action relatorioEntidadeAction = new Action("visualizarRelatorioAseguradora");
					relatorioEntidadeAction.add("id", entidade.obterId());
					relatorioEntidadeAction.add("lista", false);
					this.addOption(entidade.obterNome(), "Listados",relatorioEntidadeAction);
					
					/*
					 * Action relatorioEntidadeAction2 = new
					 * Action("visualizarBalancoGeral");
					 * relatorioEntidadeAction2.add("id", entidade.obterId());
					 * relatorioEntidadeAction2.add("lista", false);
					 * this.addOption(entidade.obterNome(), "Balance General",
					 * relatorioEntidadeAction2);
					 * 
					 * Action relatorioEntidadeAction3 = new
					 * Action("visualizarResultadoResumido");
					 * relatorioEntidadeAction3.add("id", entidade.obterId());
					 * relatorioEntidadeAction3.add("lista", false);
					 * this.addOption(entidade.obterNome(), "Resultados Resumido",
					 * relatorioEntidadeAction3);
					 */
				}
	
				if (entidade.obterId() != usuarioAtual.obterId())
				{
					Action eventosEntidadeAction = new Action("visualizarEventosEntidade");
					eventosEntidadeAction.add("id", entidade.obterId());
					this.addOption(entidade.obterNome(), "Histórico de Eventos", eventosEntidadeAction);
				}
	
				if (!(entidade instanceof Raiz)) {
					if (entidade instanceof Usuario
							&& entidade.obterId() != usuarioAtual.obterId()) {
						Action responsabilidadesUsuarioAction = new Action(
								"visualizarResponsabilidadesUsuario");
						responsabilidadesUsuarioAction
								.add("id", entidade.obterId());
						responsabilidadesUsuarioAction.add("origemMenuId", entidade
								.obterId());
						this.addOption(entidade.obterNome(), "Responsabilidades",
								responsabilidadesUsuarioAction);
					}
				}
	
				if (entidade.permiteIncluirEntidadesInferiores()) {
					Action novaEntidadeAction2 = new Action("novaEntidade");
					novaEntidadeAction2.add("superiorId", entidade.obterId());
					novaEntidadeAction2.add("origemMenuId", entidade.obterId());
					this.addOption(entidade.obterNome(), "Nuevo Catastro...",
							novaEntidadeAction2);
				}
	
				if (entidade.permiteAtualizar()) {
					/*
					 * Atenção
					 */
					Action novoEventoOrigemAction = new Action("novoEvento");
					novoEventoOrigemAction.add("passo", 2);
					novoEventoOrigemAction.add("origemId", entidade.obterId());
					novoEventoOrigemAction.add("origemMenuId", entidade.obterId());
					this.addOption(entidade.obterNome(), "Nuevo Evento...",
							novoEventoOrigemAction);
				}
			}
		//}
		/*else
		{
			Action alterarSenhaAction = new Action("alterarSenhaUsuario");
			alterarSenhaAction.add("id", usuarioAtual.obterId());
			alterarSenhaAction.add("origemMenuId", entidade.obterId());
			this.addOption("Menú Principal", "Cambiar Contraseña...",
					alterarSenhaAction);
			
			Action action1 = new Action("visualizarRelEconomico");
			action1.add("view", true);
			
			this.addOption("Listados","Cuentas Contables",action1);
			
			
			
		}*/

		/*// tabela menu
		Table menuTable = new Table(1);
		menuTable.setWidth("100%");
		String group = "";
		for (Iterator i = this.options.iterator(); i.hasNext();) 
		{
			Option o = (Option) i.next();
			if (!group.equals(o.getGroup()))
			{
				menuTable.addSubtitle(o.getGroup());
				group = o.getGroup();
			}
			Label label = new Label(o.getCaption());
			label.setBold(o.getGroup().equals(this.getSelectedGroup()) && o.getCaption().equals(this.getSelectedOption()));
			Block block = new Block(Block.HORIZONTAL);
			block.add(new Link(label, o.getAction()));
			menuTable.addData(block);
		}

		Table mainTable = new Table(2);
		mainTable.setWidth("100%");
		mainTable.addStyle("maintable");

		Table titleTable = new Table(2);
		titleTable.setWidth("100%");
		titleTable.addStyle("maintitle");
		titleTable.setNextStyle("title");

		Block block = new Block(Block.HORIZONTAL);

		Link link2 = new Link(new Image("forum.gif"), new Action("localizarForuns"));
		link2.getAction().add("id", usuarioAtual.obterId());
		link2.setNote("Forun");

		Link link3 = new Link(new Image("help.gif"), new Action("abrirHelp"));
		link3.getAction().add("id", entidade.obterId());
		link3.setNote("Help");
		

		String usuario = "Usuario: " + usuarioAtual.obterNome();
		Label label = new Label(usuario);
		label.setBold(true);

		block.add(label);
		block.add(new Space(10));
		block.add(link2);
		block.add(new Space(5));
		block.add(link3);
		titleTable.addData(block);
		
		titleTable.setNextHAlign(Table.HALIGN_RIGHT);

		//Action action = new Action("_logoff");
		Action action = new Action("sair");
		action.add("logoff", true);
		//action.add("gerarLog", true);

		Link link = new Link("Cerrar Sesión", action);

		titleTable.addData(link);
		mainTable.setNextColSpan(mainTable.getColumns());
		mainTable.add(titleTable);

		mainTable.setNextWidth("200");
		mainTable.setNextVAlign(Table.VALIGN_TOP);
		mainTable.add(new Border(menuTable));
		mainTable.setNextWidth("*");
		mainTable.setNextVAlign(Table.VALIGN_TOP);
		mainTable.addData(new Border(this.getTitle(), this.getBody(user,
				locale, properties)));*/
		
		action = new Action("sair");
		action.add("logoff", true);
		menu = new Menu("Salir", action);
	    menuP.addMenu(menu);
	    
		titleTable.add(new Space());
		titleTable.add(menuP);
		titleTable.add(new Space());
		    
		mainTable.setNextColSpan(mainTable.getColumns());
	    mainTable.add(titleTable);
			
		mainTable.addData(new Border(this.getTitle(), this.getBody(user, locale, properties)));
		return mainTable;
	}

	public abstract View getBody(User user, Locale locale, Properties properties)
			throws Exception;

	public abstract String getSelectedGroup() throws Exception;

	public abstract String getSelectedOption() throws Exception;

	public abstract View getTitle() throws Exception;

	public abstract Entidade getOrigemMenu() throws Exception;
}