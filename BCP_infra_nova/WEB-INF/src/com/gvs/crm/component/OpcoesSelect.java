package com.gvs.crm.component;

import infra.view.Select;

public class OpcoesSelect extends Select
{
	public OpcoesSelect(String nome, int valor, boolean todas) throws Exception
	{
		super(nome,1);
		
		this.add("", 0, false);
		if(todas)
			this.add("Todas", -1, false);
		
		this.add("Nuevo Catastro", 64, 64 == valor);
		this.add("Nuevo Evento", 65, 65 == valor);
		this.add("Nuevo Envio de Archivos", 105, 105 == valor);
		this.add("Archivos Enviados", 106, 106 == valor);
		this.add("Indicadores", 39, 39 == valor);
		this.add("Central de Riesgos", 37, 37 == valor);
		this.add("Entidades/Eventos", 35, 35 == valor);
		this.add("Buscar Resolución Escaneada", 79, 79 == valor);
		this.add("Grupo para Alerta Temprana", 104, 104 == valor);
		//this.add("Libros Electrónicos", 72, 72 == valor);
		
		this.add("Boletín", 0, false);
		this.add("&nbsp;&nbsp;&nbsp;Plan de Cuentas (Aseguradoras)", 1, 1 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Plan de Cuentas (Grupo Coasegurador)", 2, 2 == valor);
		this.add("Estadística CI", 0, false);
		this.add("&nbsp;&nbsp;&nbsp;Registro 02 - Datos del Instrumento", 3, 3 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 03 - Provisiones y Reservas", 4, 4 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 04 - Reaseguros", 5, 5 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 05 - Coaseguros", 6, 6 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 06 - Siniestros", 7, 7 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 07 - Pagos a proveedores", 8, 8 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 08 - Anulación de Instrumento", 9, 9 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 09 - Cobranza", 10, 10 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 10 - Demanda Judicial", 11, 11 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 11 - Endoso o Suplemento", 12, 12 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 12 - Finalización de Vigencia del Instrumento", 13, 13 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 13 - Refinanciación", 14, 14 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 14 - Gastos", 15, 15 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 15 - Anulación de Reaseguro", 16, 16 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 16 - Morosidad", 17, 17 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Registro 17 - Datos del Asegurado", 18, 18 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Utilización del Sistema", 19, 19 == valor);
		this.add("Inspección in situ", 0, false);
		/*this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Pólizas", 20, 20 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Reaseguros", 21, 21 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Reaseguradora - Reaseguros", 22, 22 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Agente de Seguro", 23, 23 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Ocurrencias", 24, 24 == valor);*/
		this.add("&nbsp;&nbsp;&nbsp;Listados", 25, 25 == valor);
		/*this.add("&nbsp;&nbsp;&nbsp;Listados Entidades Vigentes", 26, 26 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listados Aseguradoras Planes", 27, 27 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Pólizas por Sección", 28, 28 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Pólizas por Sección Anual", 29, 29 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Información Contable Consolidada", 30, 30 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Corredora de Reaseguros", 31, 31 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Corredora de Reaseguros - Aseguradoras", 32, 32 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Consolidado Póliza/Sección", 33, 33 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Histórico Pólizas/Siniestros", 34, 34 == valor);*/
		//this.add("Producción", 36, 36 == valor);
		this.add("Producción", 0, false);
		this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Pólizas", 20, 20 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Reaseguros", 21, 21 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Agente y Corredor", 94, 94 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Reaseguradora - Reaseguros", 22, 22 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Aseguradora - Corredora de Reaseguros", 31, 31 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Corredora de Reaseguros - Aseguradoras", 32, 32 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Agente de Seguro", 23, 23 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Corredor de Seguros", 66, 66 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Control SIS", 46, 46 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Operaciones Sospechosas", 47, 47 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Planes", 48, 48 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Planes Especiales", 87, 87 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Cod. Planes Siniestro", 49, 49 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listados Entidades Vigentes", 26, 26 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listados Aseguradoras Planes", 27, 27 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Pólizas por Sección", 28, 28 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Pólizas por Sección Anual", 29, 29 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Pólizas por Modalidad", 92, 92 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Pólizas por Tipo de Persona", 93, 93 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Información contable para GEE", 30, 30 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Usuarios", 55, 55 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado con Plan RG.0001", 80, 80 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Inc/Mod de Sec o Modalidad", 69, 69 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Estructura de los Planes", 78, 78 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Información requerida FMI", 89, 89 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Cuentas hasta cuarto nivel", 90, 90 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Información Bancos y Financieras", 91, 91 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Cotizaciones del Dólar", 100, 100 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Variaciones IPC", 101, 101 == valor);
		//this.add("Seguimiento", 38, 38 == valor);
		this.add("Seguimiento", 0, false);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Ocurrencias Pólizas", 24, 24 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Ocurrencias Siniestros", 71, 71 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Consolidado Póliza/Sección", 33, 33 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Histórico Pólizas/Siniestros", 34, 34 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Ultimas Agendas", 59, 59 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Ultimos Libros Electrónicos", 77, 77 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Agendar/Desagendar Validación", 60, 60 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Control de Agenda Instrumento", 61, 61 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Demanda Judicial", 62, 62 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Estadística CI", 63, 63 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Agentes", 67, 67 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Corredores de Seguros", 68, 68 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Reaseguradoras", 81, 81 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Reaseguradoras por Sección", 82, 82 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Mayores Corredores de Reaseguros", 83, 83 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Entidades Vigentes con identificación", 84, 84 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Cantidad de Pólizas/Reaseguros", 85, 85 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Cobertura de Reaseguros", 86, 86 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Validaciones", 88, 88 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Calculo de Ratios Financieros", 95, 95 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Calculo de Ratios Financieros 1", 96, 96 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Ratios Agregados 1", 97, 97 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Ratios Agregados", 98, 98 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Margen de Solvencias", 99, 99 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Información del Mercado", 102, 102 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Alerta Temprano", 103, 103 == valor);
		this.add("Agenda Movimiento", 0, false);
		this.add("&nbsp;&nbsp;&nbsp;Especial", 70, 70 == valor);
		this.add("Libros Electrónicos", 0, false);
		this.add("&nbsp;&nbsp;&nbsp;Nuevo Libro", 72, 72 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Libros Electrónicos", 73, 73 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Listado Libros Pendientes", 74, 74 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Comisión Agentes por Sección", 75, 75 == valor);
		this.add("&nbsp;&nbsp;&nbsp;Comisión Corredores de Seguros por Sección", 76, 76 == valor);
	}
}