package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.OpcaoHome;
import com.gvs.crm.model.Usuario;

import infra.model.Home;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class OpcaoHomeImpl extends Home implements OpcaoHome
{
	public void incluirUsuario(int opcao, Usuario usuario) throws Exception
	{
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm", "insert into opcoes(opcao,usuario) values(?,?)");
		insert.addInt(opcao);
		insert.addLong(usuario.obterId());
		
		insert.execute();
	}

	public Collection<Usuario> obterUsuarios(int opcao) throws Exception
	{
		Collection<Usuario> usuarios = new ArrayList<Usuario>();
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select usuario from opcoes where opcao = ?");
		query.addInt(opcao);
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("usuario");
			
			Usuario usuario = (Usuario) entidadeHome.obterEntidadePorId(id);
			
			usuarios.add(usuario);
		}
		
		return usuarios;
	}
	
	public Collection<Integer> obterOpcoes(Usuario usuario) throws Exception
	{
		Collection<Integer> opcoes = new ArrayList<Integer>();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select opcao from opcoes where usuario = ?");
		query.addLong(usuario.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int opcao = rows[i].getInt("opcao");
			
			opcoes.add(opcao);
		}
		
		return opcoes;
	}
	
	public Collection<Integer> obterOpcoes() throws Exception
	{
		Collection<Integer> opcoes = new ArrayList<Integer>();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select opcao from opcoes group by opcao");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int opcao = rows[i].getInt("opcao");
			
			opcoes.add(opcao);
		}
		
		return opcoes;
	}
	
	public Collection<String> obterOpcoesPorNome(Usuario usuario) throws Exception
	{
		Map<String,String> opcoes = new TreeMap<String,String>();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select opcao from opcoes where usuario = ?");
		query.addLong(usuario.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			int opcao = rows[i].getInt("opcao");
			
			String nome = this.obterNome(opcao); 
			opcoes.put(nome,nome);
		}
		
		return opcoes.values();
	}

	public void excluirUsuario(int opcao, Usuario usuario) throws Exception
	{
		SQLUpdate delete = this.getModelManager().createSQLUpdate("crm","delete from opcoes where opcao = ? and usuario = ?");
		delete.addInt(opcao);
		delete.addLong(usuario.obterId());
		
		delete.execute();
	}
	
	public String obterNome(int opcao)
	{
		String nome = "";
		
		if(opcao == 1)
			nome = "Plan de Cuentas (Aseguradoras)";
		else if(opcao == 2)
			nome = "Plan de Cuentas (Grupo Coasegurador)";
		else if(opcao == 3)
			nome = "Registro 02 - Datos del Instrumento";
		else if(opcao == 4)
			nome = "Registro 03 - Provisiones y Reservas";
		else if(opcao == 5)
			nome = "Registro 04 - Reaseguros";
		else if(opcao == 6)
			nome = "Registro 05 - Coaseguros";
		else if(opcao == 7)
			nome = "Registro 06 - Siniestros";
		else if(opcao == 8)
			nome = "Registro 07 - Pagos a proveedores";
		else if(opcao == 9)
			nome = "Registro 08 - Anulación de Instrumento";
		else if(opcao == 10)
			nome = "Registro 09 - Cobranza";
		else if(opcao == 11)
			nome = "Registro 10 - Demanda Judicial";
		else if(opcao == 12)
			nome = "Registro 11 - Endoso o Suplemento";
		else if(opcao == 13)
			nome = "Registro 12 - Finalización de Vigencia del Instrumento";
		else if(opcao == 14)
			nome = "Registro 13 - Refinanciación";
		else if(opcao == 15)
			nome = "Registro 14 - Gastos";
		else if(opcao == 16)
			nome = "Registro 15 - Anulación de Reaseguro";
		else if(opcao == 17)
			nome = "Registro 16 - Morosidad";
		else if(opcao == 18)
			nome = "Registro 17 - Datos del Asegurado";
		else if(opcao == 19)
			nome = "Utilización del Sistema";
		else if(opcao == 20)
			nome = "Aseguradora - Pólizas";
		else if(opcao == 21)
			nome = "Aseguradora - Reaseguros";
		else if(opcao == 22)
			nome = "Reaseguradora - Reaseguros";
		else if(opcao == 23)
			nome = "Agente de Seguro";
		else if(opcao == 24)
			nome = "Mayores Ocurrencias Pólizas";
		else if(opcao == 25)
			nome = "Listados";
		else if(opcao == 26)
			nome = "Listados Entidades Vigentes";
		else if(opcao == 27)
			nome = "Listados Aseguradoras Planes";
		else if(opcao == 28)
			nome = "Pólizas por Sección";
		else if(opcao == 29)
			nome = "Pólizas por Sección Anual";
		else if(opcao == 30)
			nome = "Información Contable Consolidada";
		else if(opcao == 31)
			nome = "Aseguradora - Corredora de Reaseguros";
		else if(opcao == 32)
			nome = "Corredora de Reaseguros - Aseguradoras";
		else if(opcao == 33)
			nome = "Consolidado Póliza/Sección";
		else if(opcao == 34)
			nome = "Histórico Pólizas/Siniestros";
		else if(opcao == 35)
			nome = "Entidades/Eventos";
		else if(opcao == 37)
			nome = "Central de Riesgos";
		else if(opcao == 38)
			nome = "Seguimiento";
		else if(opcao == 39)
			nome = "Indicadores";
		else if(opcao == 40)
			nome = "Aseguradora - Pólizas";
		else if(opcao == 41)
			nome = "Aseguradora - Reaseguros";
		else if(opcao == 42)
			nome = "Reaseguradora - Reaseguros";
		else if(opcao == 43)
			nome = "Aseguradora - Corredora de Reaseguros";
		else if(opcao == 44)
			nome = "Corredora de Reaseguros - Aseguradoras";
		else if(opcao == 45)
			nome = "Agente de Seguro";
		else if(opcao == 46)
			nome = "Listado Control SIS";
		else if(opcao == 47)
			nome = "Operaciones Sospechosas";
		else if(opcao == 48)
			nome = "Listado Planes";
		else if(opcao == 49)
			nome = "Listado Cod. Planes Siniestro";
		else if(opcao == 50)
			nome = "Listados Entidades Vigentes";
		else if(opcao == 51)
			nome = "Listados Aseguradoras Planes";
		else if(opcao == 52)
			nome = "Pólizas por Sección";
		else if(opcao == 53)
			nome = "Pólizas por Sección Anual";
		else if(opcao == 54)
			nome = "Información contable para GEE";
		else if(opcao == 55)
			nome = "Listado Usuarios";
		else if(opcao == 56)
			nome = "Mayores Ocurrencias";
		else if(opcao == 57)
			nome = "Consolidado Póliza/Sección";
		else if(opcao == 58)
			nome = "Histórico Pólizas/Siniestros";
		else if(opcao == 59)
			nome = "Ultimas Agendas";
		else if(opcao == 60)
			nome = "Agendar/Desagendar Validación";
		else if(opcao == 61)
			nome = "Control de Agenda Instrumento";
		else if(opcao == 62)
			nome = "Demanda Judicial";
		else if(opcao == 63)
			nome = "Estadística CI";
		else if(opcao == 64)
			nome = "Nuevo Catastro";
		else if(opcao == 65)
			nome = "Nuevo Evento";
		else if(opcao == 66)
			nome = "Corredor de Seguros";
		else if(opcao == 67)
			nome = "Mayores Agentes";
		else if(opcao == 68)
			nome = "Mayores Corredores de Seguros";
		else if(opcao == 69)
			nome = "Inc/Mod de Sec o Modalidad";
		else if(opcao == 70)
			nome = "Agenda Movimiento especial";
		else if(opcao == 71)
			nome = "Mayores Ocurrencias Siniestros";
		else if(opcao == 72)
			nome = "Nuevo Libro";
		else if(opcao == 73)
			nome = "Listado Libros Electrónicos";
		else if(opcao == 74)
			nome = "Listado Libros Pendientes";
		else if(opcao == 75)
			nome = "Comisión Agentes por Sección";
		else if(opcao == 76)
			nome = "Comisión Corredores de Seguros por Sección";
		else if(opcao == 77)
			nome = "Ultimos Libros Electrónicos";
		else if(opcao == 78)
			nome = "Estructura de los Planes";
		else if(opcao == 79)
			nome = "Buscar Resolución Escaneada";
		else if(opcao == 80)
			nome = "Listado con Plan RG.0001";
		else if(opcao == 81)
			nome = "Mayores Reaseguradoras";
		else if(opcao == 82)
			nome = "Mayores Reaseguradoras por Sección";
		else if(opcao == 83)
			nome = "Mayores Corredores de Reaseguros";
		else if(opcao == 84)
			nome = "Listado Entidades Vigentes con identificación";
		else if(opcao == 85)
			nome = "Cantidad de Pólizas/Reaseguros";
		else if(opcao == 86)
			nome = "Cobertura de Reaseguros";
		else if(opcao == 87)
			nome = "Planes Especiales";
		else if(opcao == 88)
			nome = "Validaciones";
		else if (opcao == 89)
	      nome = "Información requerida FMI";
		else if (opcao == 90)
			nome = "Cuentas hasta cuarto nivel";
		else if (opcao == 91)
			nome = "Información Bancos y Financieras";
		else if (opcao == 92)
			nome = "Pólizas por Modalidad";
		else if (opcao == 93)
			nome = "Pólizas por Tipo de Persona";
		else if (opcao == 94)
			nome = "Aseguradora - Agente y Corredor";
		else if (opcao == 95)
			nome = "Calculo de Ratios Financieros";
		else if (opcao == 96)
			nome = "Calculo de Ratios Financieros 1";
		else if (opcao == 97)
			nome = "Ratios Agregados 1";
		else if (opcao == 98)
			nome = "Ratios Agregados";
		else if (opcao == 99)
			nome = "Margen de Solvencia";
		else if (opcao == 100)
			nome = "Listado Cotizaciones del Dólar";
		else if (opcao == 101)
			nome = "Listado Variaciones IPC";
		else if (opcao == 102)
			nome = "Información del Mercado";
		else if (opcao == 103)
			nome = "Alerta Temprana";
		else if (opcao == 104)
			nome = "Grupo para Alerta Temprana";
		else if (opcao == 105)
			nome = "Nuevo Envio de Archivos";
		else if (opcao == 106)
			nome = "Archivos Enviados";
		
		return nome;
	}
}