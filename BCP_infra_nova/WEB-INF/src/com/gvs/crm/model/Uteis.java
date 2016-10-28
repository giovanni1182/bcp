package com.gvs.crm.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Uteis 
{
	public Map<String, String> nomeLivrosCombo()
	{
		Map<String,String> nomeLivros = new TreeMap<String,String>();
		
		nomeLivros.put("SISLIBA10 SEGUROS DIRECTOS", "SEGUROS DIRECTOS SISLIBA10");
		nomeLivros.put("SISLIBA20 REASEGUROS ACEPTADOS", "REASEGUROS ACEPTADOS SISLIBA20");
		nomeLivros.put("SISLIBA30 REASEGUROS CEDIDOS/RETROCEDIDOS", "REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30");
		nomeLivros.put("SISLIBB00 EXTRACTO DEL LIBRO DE SINIESTROS", "EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00");
		nomeLivros.put("SISLIBC00 EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES", "EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00");
		nomeLivros.put("SISLIBD10 DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES", "DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10");
		nomeLivros.put("SISLIBD20 DETALLE DE PROVISIONES Y RESERVAS-VIDA", "DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20");
		nomeLivros.put("SISLIBD30 DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30");
		nomeLivros.put("SISLIBD40 DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40");
		nomeLivros.put("SISLIBD51 RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION", "RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51");
		nomeLivros.put("SISLIBD52 RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52");
		nomeLivros.put("SISLIBD53 RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53");
		nomeLivros.put("SISLIBD54 RESUMEN DEL ESTADO DE LIQUIDEZ", "RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54");
		nomeLivros.put("SISLIBE00 ROBO DE VEHÍCULOS", "ROBO DE VEHÍCULOS SISLIBE00");
		nomeLivros.put("SISLIBF01 Patrimonio Propio no comprometido o Patrimonio Técnico".toUpperCase(), "Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase());
		nomeLivros.put("SISLIBF02 Cálculo del Factor de Retención".toUpperCase(), "Cálculo del Factor de Retención SISLIBF02".toUpperCase());
		nomeLivros.put("SISLIBF03 Margen de Solvencia requerido en función de las primas".toUpperCase(), "Margen de Solvencia requerido en función de las primas SISLIBF03".toUpperCase());
		nomeLivros.put("SISLIBF04 Margen de Solvencia requerido en función de los siniestros".toUpperCase(), "Margen de Solvencia requerido en función de los siniestros SISLIBF04".toUpperCase());
		nomeLivros.put("SISLIBF05 Seg. Vida - Patr. prop. no comprom. o patr. téc.".toUpperCase(), "Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase());
		nomeLivros.put("SISLIBF06 Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo".toUpperCase(), "Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase());
		nomeLivros.put("SISLIBF07 Cia operam ambas ramas - Patr. prop no comprom. o patr. téc.".toUpperCase(), "Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase());
		nomeLivros.put("SISLIBF08 Fondo de Garantía".toUpperCase(), "Fondo de Garantía SISLIBF08".toUpperCase());
		
		nomeLivros.put("SISLIBF21 Cálculo del factor de retención - seccción automóviles".toUpperCase(), "Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase());
		nomeLivros.put("SISLIBF22 Cálculo del factor de retención - seccción de incendio".toUpperCase(), "Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase());
		nomeLivros.put("SISLIBF23 Cálculo del factor de retención - seccción riesgos varios".toUpperCase(), "Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase());
		nomeLivros.put("SISLIBF24 Cálculo del factor de retención - seccción personales a corto plazo".toUpperCase(), "Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase());
		nomeLivros.put("SISLIBF25 Cálculo del factor de retención - seccción varias".toUpperCase(), "Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase());
		
		nomeLivros.put("SISLIBF31 Margen de solvencia requerido en función de las primas - seccción automóviles".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase());
		nomeLivros.put("SISLIBF32 Margen de solvencia requerido en función de las primas - seccción de incendio".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase());
		nomeLivros.put("SISLIBF33 Margen de solvencia requerido en función de las primas - seccción riesgos varios".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase());
		nomeLivros.put("SISLIBF34 Margen de solvencia requerido en función de las primas - seccción personales a corto plazo".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase());
		nomeLivros.put("SISLIBF35 Margen de solvencia requerido en función de las primas - seccción varias".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase());
		
		nomeLivros.put("SISLIBF41 Margen de solvencia requerido en función de los siniestros - seccción automóviles".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase());
		nomeLivros.put("SISLIBF42 Margen de solvencia requerido en función de los siniestros - seccción de incendio".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase());
		nomeLivros.put("SISLIBF43 Margen de solvencia requerido en función de los siniestros - seccción riesgos varios".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase());
		nomeLivros.put("SISLIBF44 Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase());
		nomeLivros.put("SISLIBF45 Margen de solvencia requerido en función de los siniestros - seccción varias".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase());
		
		nomeLivros.put("SISLIBA40 Libro de Producción por Agentes y Corredores de Seguros".toUpperCase(), "Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase());
		
		
		return nomeLivros;
	}
	
	private Map<String, String> nomeLivros()
	{
		Map<String,String> nomeLivros = new TreeMap<String,String>();
		
		nomeLivros.put("SEGUROS DIRECTOS SISLIBA10", "SEG. DIRE.");
		nomeLivros.put("REASEGUROS ACEPTADOS SISLIBA20", "REASE. ACEP.");
		nomeLivros.put("REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30", "REASE. CED./RETR.");
		nomeLivros.put("EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00", "EXTRA. LIB. SIN.");
		nomeLivros.put("EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00", "EXTRA. LIB. ACTUA. JUD.");
		nomeLivros.put("DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10", "DET. PROV. RES.-PATR.");
		nomeLivros.put("DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20", "DET. PROV. RES.-VIDA");
		nomeLivros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30", "DET. PROV. SINI. PEND. TRIM.");
		nomeLivros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40", "DET. PROV. SINI. PEND VIDA");
		nomeLivros.put("RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51", "RESU. COMP. CART. INVER.");
		nomeLivros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52", "RESU. EST. REPRES. SEG. VIDA");
		nomeLivros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53", "RESU. EST. REPRES. SEG. PATRI.");
		nomeLivros.put("RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54", "RESU. EST. LIQUIDEZ");
		nomeLivros.put("ROBO DE VEHÍCULOS SISLIBE00", "ROBO DE VEHÍCULOS");
		nomeLivros.put("Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase(), "Patr. Pr. No Compr.".toUpperCase());
		//nomeLivros.put("Cálculo del Factor de Retención SISLIBF02".toUpperCase(), "Cálc. Fact. Ret.".toUpperCase());
		//nomeLivros.put("Margen de Solvencia requerido en función de las primas SISLIBF03".toUpperCase(), "Marg. Solv. Primas".toUpperCase());
		//nomeLivros.put("Margen de Solvencia requerido en función de los siniestros SISLIBF04".toUpperCase(), "Marg. Solv. Siniest.".toUpperCase());
		nomeLivros.put("Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase(), "Seg. Vida - Patr. prop. no comprom. o patr. téc.".toUpperCase());
		nomeLivros.put("Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase(), "Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo".toUpperCase());
		nomeLivros.put("Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase(), "Cia operam ambas ramas - Patr. prop no comprom. o patr. téc.".toUpperCase());
		nomeLivros.put("Fondo de Garantía SISLIBF08".toUpperCase(), "Fondo de Garantía".toUpperCase());
		
		nomeLivros.put("Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase(), "Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase());
		nomeLivros.put("Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase(), "Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase());
		nomeLivros.put("Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase(), "Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase());
		nomeLivros.put("Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase(), "Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase());
		nomeLivros.put("Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase(), "Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase());
		
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase());
		
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase());
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase());
		
		nomeLivros.put("Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase(), "Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase());
		
		return nomeLivros;
	}
	
	public Map<String,Map<String,String>> obterGruposELivros()
	{
		Map<String,Map<String,String>> grupos = new TreeMap<String, Map<String,String>>();
		
		Map<String,String> livros = new TreeMap<String, String>();
		livros.put("SEGUROS DIRECTOS SISLIBA10", "SEGUROS DIRECTOS SISLIBA10");
		livros.put("REASEGUROS ACEPTADOS SISLIBA20", "REASEGUROS ACEPTADOS SISLIBA20");
		livros.put("REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30", "REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30");
		livros.put("Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase(), "Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase());
		grupos.put("A) EXTRACTO DE LOS LIBROS DE PRODUCCION Y DE REASEGURO", livros);
		
		livros = new TreeMap<String, String>();
		livros.put("EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00", "EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00");
		grupos.put("B) EXTRACTO DEL LIBRO DE SINIESTROS", livros);
		
		livros = new TreeMap<String, String>();
		livros.put("EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00", "EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00");
		grupos.put("C) EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES", livros);
		
		livros = new TreeMap<String, String>();
		livros.put("DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10", "DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10");
		livros.put("DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20", "DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20");
		livros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30");
		livros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40");
		livros.put("RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51", "RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51");
		livros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52");
		livros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53");
		livros.put("RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54", "RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54");
		grupos.put("D) EXTRACTO DEL LIBRO DE BIENES REPRESENTATIVOS DE PROVISIONES TECNICAS", livros);
		
		livros = new TreeMap<String, String>();
		livros.put("ROBO DE VEHÍCULOS SISLIBE00", "ROBO DE VEHÍCULOS SISLIBE00");
		grupos.put("E) LISTADO DE DATOS CORRESPONDIENTES A LOS CASOS DE ROBOS DE VEHÍCULOS REGISTRADOS ", livros);
		
		livros = new TreeMap<String, String>();
		livros.put("Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase(), "Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase());
		livros.put("Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase(), "Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase());
		livros.put("Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase(), "Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase());
		livros.put("Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase(), "Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase());
		livros.put("Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase(), "Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase());
		livros.put("Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase(), "Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase());
		livros.put("Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase());
		livros.put("Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase());
		livros.put("Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase());
		livros.put("Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase());
		livros.put("Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase(), "Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase());
		livros.put("Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase());
		livros.put("Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase());
		livros.put("Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase());
		livros.put("Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase());
		livros.put("Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase(), "Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase());
		livros.put("Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase(), "Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase());
		livros.put("Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase(), "Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase());
		livros.put("Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase(), "Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase());
		livros.put("Fondo de Garantía SISLIBF08".toUpperCase(), "Fondo de Garantía SISLIBF08".toUpperCase());
		grupos.put("F) MARGEN DE SOLVENCIA ", livros);
		
		
		return grupos;
	}
	
	public String obterFrequenciaLivro(String nomeLivro)
	{
		Map<String,String> nomeLivros = new TreeMap<String, String>();
		
		nomeLivros.put("SEGUROS DIRECTOS SISLIBA10", "MENSUAL");
		nomeLivros.put("REASEGUROS ACEPTADOS SISLIBA20", "MENSUAL");
		nomeLivros.put("REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30", "MENSUAL");
		nomeLivros.put("EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00", "MENSUAL");
		nomeLivros.put("EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00", "MENSUAL");
		nomeLivros.put("DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10", "TRIMESTRAL");
		nomeLivros.put("DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20", "MENSUAL");
		nomeLivros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30", "TRIMESTRAL");
		nomeLivros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40", "MENSUAL");
		nomeLivros.put("RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51", "TRIMESTRAL");
		nomeLivros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52", "TRIMESTRAL");
		nomeLivros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53", "TRIMESTRAL");
		nomeLivros.put("RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54", "TRIMESTRAL");
		nomeLivros.put("ROBO DE VEHÍCULOS SISLIBE00", "MENSUAL");
		nomeLivros.put("Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Fondo de Garantía SISLIBF08".toUpperCase(), "TRIMESTRAL");
		
		nomeLivros.put("Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase(), "TRIMESTRAL");
		
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase(), "TRIMESTRAL");
		
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase(), "TRIMESTRAL");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase(), "TRIMESTRAL");
		
		nomeLivros.put("Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase(), "MENSUAL");
		
		return nomeLivros.get(nomeLivro);
	}
	
	private Map<String, String> nomeArquivos()
	{
		Map<String,String> nomeLivros = new TreeMap<String,String>();
		
		nomeLivros.put("SEGUROS DIRECTOS SISLIBA10", "SISLIBA10");
		nomeLivros.put("REASEGUROS ACEPTADOS SISLIBA20", "SISLIBA20");
		nomeLivros.put("REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30", "SISLIBA30");
		nomeLivros.put("EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00", "SISLIBB00");
		nomeLivros.put("EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00", "SISLIBC00");
		nomeLivros.put("DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10", "SISLIBD10");
		nomeLivros.put("DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20", "SISLIBD20");
		nomeLivros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30", "SISLIBD30");
		nomeLivros.put("DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40", "SISLIBD40");
		nomeLivros.put("RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51", "SISLIBD51");
		nomeLivros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52", "SISLIBD52");
		nomeLivros.put("RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53", "SISLIBD53");
		nomeLivros.put("RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54", "SISLIBD54");
		nomeLivros.put("ROBO DE VEHÍCULOS SISLIBE00", "SISLIBE00");
		nomeLivros.put("Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase(), "SISLIBF01");
		nomeLivros.put("Cálculo del Factor de Retención SISLIBF02".toUpperCase(), "SISLIBF02");
		nomeLivros.put("Margen de Solvencia requerido en función de las primas SISLIBF03".toUpperCase(), "SISLIBF03");
		nomeLivros.put("Margen de Solvencia requerido en función de los siniestros SISLIBF04".toUpperCase(), "SISLIBF04");
		nomeLivros.put("Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase(), "SISLIBF05");
		nomeLivros.put("Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase(), "SISLIBF06");
		nomeLivros.put("Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase(), "SISLIBF07");
		nomeLivros.put("Fondo de Garantía SISLIBF08".toUpperCase(), "SISLIBF08");
		
		nomeLivros.put("Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase(), "SISLIBF21");
		nomeLivros.put("Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase(), "SISLIBF22");
		nomeLivros.put("Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase(), "SISLIBF23");
		nomeLivros.put("Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase(), "SISLIBF24");
		nomeLivros.put("Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase(), "SISLIBF25");
		
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase(), "SISLIBF31");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase(), "SISLIBF32");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase(), "SISLIBF33");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase(), "SISLIBF34");
		nomeLivros.put("Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase(), "SISLIBF35");
		
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase(), "SISLIBF41");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase(), "SISLIBF42");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase(), "SISLIBF43");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase(), "SISLIBF44");
		nomeLivros.put("Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase(), "SISLIBF45");
		
		nomeLivros.put("Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase(), "SISLIBA40");
		
		return nomeLivros;
	}
	
	private Map<String, String> nomeArquivos2()
	{
		Map<String,String> nomeLivros = new TreeMap<String,String>();
		
		nomeLivros.put("SISLIBA10", "SEGUROS DIRECTOS SISLIBA10");
		nomeLivros.put("SISLIBA20", "REASEGUROS ACEPTADOS SISLIBA20");
		nomeLivros.put("SISLIBA30", "REASEGUROS CEDIDOS/RETROCEDIDOS SISLIBA30");
		nomeLivros.put("SISLIBB00", "EXTRACTO DEL LIBRO DE SINIESTROS SISLIBB00");
		nomeLivros.put("SISLIBC00", "EXTRACTO DEL LIBRO DE ACTUACIONES JUDICIALES SISLIBC00");
		nomeLivros.put("SISLIBD10", "DETALLE DE PROVISIONES Y RESERVAS-PATRIMONIALES SISLIBD10");
		nomeLivros.put("SISLIBD20", "DETALLE DE PROVISIONES Y RESERVAS-VIDA SISLIBD20");
		nomeLivros.put("SISLIBD30", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-TRIMESTRAL SISLIBD30");
		nomeLivros.put("SISLIBD40", "DETALLE DE PROVISIONES SINIESTROS PENDIENTES-VIDA SISLIBD40");
		nomeLivros.put("SISLIBD51", "RESUMEN DE LA COMPOSICION DE LA CARTERA DE INVERSION SISLIBD51");
		nomeLivros.put("SISLIBD52", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS DE VIDA SISLIBD52");
		nomeLivros.put("SISLIBD53", "RESUMEN DEL ESTADO DE REPRESENTATIVIDAD SEGUROS PATRIMONIALES SISLIBD53");
		nomeLivros.put("SISLIBD54", "RESUMEN DEL ESTADO DE LIQUIDEZ SISLIBD54");
		nomeLivros.put("SISLIBE00", "ROBO DE VEHÍCULOS SISLIBE00");
		nomeLivros.put("SISLIBF01", "Patrimonio Propio no comprometido o Patrimonio Técnico SISLIBF01".toUpperCase());
		nomeLivros.put("SISLIBF02", "Cálculo del Factor de Retención SISLIBF02".toUpperCase());
		nomeLivros.put("SISLIBF03", "Margen de Solvencia requerido en función de las primas SISLIBF03".toUpperCase());
		nomeLivros.put("SISLIBF04", "Margen de Solvencia requerido en función de los siniestros SISLIBF04".toUpperCase());
		nomeLivros.put("SISLIBF05", "Seg. Vida - Patr. prop. no comprom. o patr. téc. SISLIBF05".toUpperCase());
		nomeLivros.put("SISLIBF06", "Seg. Vida - Margen de solvencia - Res. matem. - Cap. en riesgo SISLIBF06".toUpperCase());
		nomeLivros.put("SISLIBF07", "Cia operam ambas ramas - Patr. prop no comprom. o patr. téc. SISLIBF07".toUpperCase());
		nomeLivros.put("SISLIBF08", "Fondo de Garantía SISLIBF08".toUpperCase());
		
		nomeLivros.put("SISLIBF21", "Cálculo del factor de retención - seccción automóviles SISLIBF21".toUpperCase());
		nomeLivros.put("SISLIBF22", "Cálculo del factor de retención - seccción de incendio SISLIBF22".toUpperCase());
		nomeLivros.put("SISLIBF23", "Cálculo del factor de retención - seccción riesgos varios SISLIBF23".toUpperCase());
		nomeLivros.put("SISLIBF24", "Cálculo del factor de retención - seccción personales a corto plazo SISLIBF24".toUpperCase());
		nomeLivros.put("SISLIBF25", "Cálculo del factor de retención - seccción varias SISLIBF25".toUpperCase());
		
		nomeLivros.put("SISLIBF31", "Margen de solvencia requerido en función de las primas - seccción automóviles SISLIBF31".toUpperCase());
		nomeLivros.put("SISLIBF32", "Margen de solvencia requerido en función de las primas - seccción de incendio SISLIBF32".toUpperCase());
		nomeLivros.put("SISLIBF33", "Margen de solvencia requerido en función de las primas - seccción riesgos varios SISLIBF33".toUpperCase());
		nomeLivros.put("SISLIBF34", "Margen de solvencia requerido en función de las primas - seccción personales a corto plazo SISLIBF34".toUpperCase());
		nomeLivros.put("SISLIBF35", "Margen de solvencia requerido en función de las primas - seccción varias SISLIBF35".toUpperCase());
		
		nomeLivros.put("SISLIBF41", "Margen de solvencia requerido en función de los siniestros - seccción automóviles SISLIBF41".toUpperCase());
		nomeLivros.put("SISLIBF42", "Margen de solvencia requerido en función de los siniestros - seccción de incendio SISLIBF42".toUpperCase());
		nomeLivros.put("SISLIBF43", "Margen de solvencia requerido en función de los siniestros - seccción riesgos varios SISLIBF43".toUpperCase());
		nomeLivros.put("SISLIBF44", "Margen de solvencia requerido en función de los siniestros - seccción personales a corto plazo SISLIBF44".toUpperCase());
		nomeLivros.put("SISLIBF45", "Margen de solvencia requerido en función de los siniestros - seccción varias SISLIBF45".toUpperCase());
		
		nomeLivros.put("SISLIBA40","Libro de Producción por Agentes y Corredores de Seguros SISLIBA40".toUpperCase());
		
		return nomeLivros;
	}
	
	public String obterNomeArquivo(String tipo)
	{
		return this.nomeArquivos().get(tipo);
	}
	
	public String obterNomeArquivo2(String tipo)
	{
		return this.nomeArquivos2().get(tipo);
	}
	
	public Collection<String> obterNomeLivros()
	{
		Collection<String> nomeLivros = new ArrayList<String>();
		
		for(Iterator<String> i = this.nomeLivros().keySet().iterator() ; i.hasNext() ; )
		{
			String nome = i.next();
			
			nomeLivros.add(nome);
		}
		
		return nomeLivros;
	}
	
	public String obterNomeLivroAreviado(String nomeNormal)
	{
		return this.nomeLivros().get(nomeNormal);
	}
	
	public Collection<String> obterMimeTypes()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		
		mimeTypes.addAll(this.obterMimeTypesPDF());
		mimeTypes.addAll(this.obterMimeTypesWord());
		mimeTypes.addAll(this.obterMimeTypesExcel());
		
		return mimeTypes;
	}
	
	public Collection<String> obterMimeTypesPDF()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("application/pdf");
		mimeTypes.add("application/x-pdf");
		mimeTypes.add("application/acrobat");
		mimeTypes.add("applications/vnd.pdf");
		mimeTypes.add("text/pdf");
		mimeTypes.add("text/x-pdf");
		
		return mimeTypes;
	}
	
	public Collection<String> obterMimeTypesWord()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("application/msword");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.wordprocessingml.template");
		mimeTypes.add("application/vnd.ms-word.document.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-word.template.macroEnabled.12");
		
		return mimeTypes;
	}
	
	public Collection<String> obterMimeTypesExcel()
	{
		Collection<String> mimeTypes = new ArrayList<String>();
		mimeTypes.add("application/vnd.ms-excel");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		mimeTypes.add("application/vnd.openxmlformats-officedocument.spreadsheetml.template");
		mimeTypes.add("application/vnd.ms-excel.sheet.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-excel.template.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-excel.addin.macroEnabled.12");
		mimeTypes.add("application/vnd.ms-excel.sheet.binary.macroEnabled.12");
		
		return mimeTypes;
	}
	
	public void copiarArquivos() throws Exception
	{
		File diretorio = new File("C:/Archivos Generales/");
		String DIRETORIO_TXT = "C:/Aseguradoras/Archivos";
		String DIRETORIO_LIVROS = "C:/Aseguradoras/libros";
		String diretorioFinal = "";
		Collection<String> tipos = this.obterMimeTypes();
		
		InputStream is = null;
		OutputStream os = null;
		byte[] buffer;
		
		File[] arquivos = diretorio.listFiles();
		
		for(int i = 0 ; i < arquivos.length ; i++)
		{
			File arquivo = arquivos[i];
			
			is = null;
			os = null;
			diretorioFinal = "";
			boolean podeCopiar = false;
			
			Path path = Paths.get(arquivo.getAbsolutePath());
			
			String nomeArquivo = arquivo.getName();
			
			String primeiraLetra = nomeArquivo.substring(0,1);
			
			String mimeType = Files.probeContentType(path);
			
			System.out.println(nomeArquivo + " "+ mimeType);
			try
			{
				if(primeiraLetra.equals("0") || primeiraLetra.toLowerCase().equals("a"))
				{
					if(mimeType.equals("text/plain"))
					{
						diretorioFinal = DIRETORIO_TXT;
						System.out.println("txt");
						podeCopiar = true;
					}
				}
				else if(primeiraLetra.toLowerCase().equals("l"))
				{
					if(mimeType == null)
					{
						String extensao = nomeArquivo.substring(20,nomeArquivo.length());
						
						if(extensao.startsWith("xl"))
							mimeType = "application/vnd.ms-excel";
	            		else if(extensao.startsWith("pd"))
	            			mimeType = "application/pdf";
	            		else if(extensao.startsWith("do"))
	            			mimeType = "application/msword";
					}
					
					diretorioFinal = DIRETORIO_LIVROS;
					System.out.println("livro");
					podeCopiar = true;
				}
				
				if(podeCopiar)
				{
					is = new FileInputStream(arquivo);
					os = new FileOutputStream(diretorioFinal+"/"+nomeArquivo);
					buffer = new byte[is.available()];
					is.read(buffer);
					os.write(buffer);
				}
			}
			catch (IOException e)
			{
				System.out.println(e.getMessage());
			}
			catch (OutOfMemoryError e)
			{
				System.out.println(e.getMessage());
			}
			finally
			{
				try
				{
					if (is != null)
						is.close();
					if (os != null)
						os.close();
				}
				catch (IOException e)
				{
					System.out.println(e.getMessage());
				}
			}
			arquivo.delete();
		}
	}
	
	public boolean validaData(String dataStr)
    {
        final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])/(0?[1-9]|1[012])/((19|20)\\d\\d)";
        Pattern pattern = Pattern.compile(DATE_PATTERN);;
        Matcher matcher  = pattern.matcher(dataStr);

        if(matcher.matches())
        {
            matcher.reset();

            if(matcher.find())
            {
                String day = matcher.group(1);
                String month = matcher.group(2);
                int year = Integer.parseInt(matcher.group(3));

                if (day.equals("31") && (month.equals("4") || month .equals("6") || month.equals("9") || month.equals("11") || month.equals("04") || month .equals("06") || month.equals("09")))
                {
                    return false; // only 1,3,5,7,8,10,12 has 31 days
                }
                else if (month.equals("2") || month.equals("02"))
                {
                    //leap year
                    if(year % 4==0)
                    {
                        if(day.equals("30") || day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    else
                    {
                        if(day.equals("29")||day.equals("30")||day.equals("31"))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
	
	public boolean eNumero(String valor) throws Exception
	{
		boolean retorno = true;
		
		try
		{
			valor = valor.replace(",", ".");
			Double.valueOf(valor);
		}
		catch(Exception e)
		{
			retorno = false;
		}
		
		return retorno;
	}
	
	public boolean eDataContabil(String valor) throws Exception
	{
		boolean retorno = true;
		
		try
		{
			DateFormat formatoData = new SimpleDateFormat("yyyy/MM/dd");
			formatoData.setLenient(false);
			
			String data = valor.substring(0,4)+"/"+valor.substring(4,6)+"/"+valor.substring(6,8);
			Date d = formatoData.parse(data);
		}
		catch(Exception e)
		{
			retorno = false;
		}
		
		return retorno;
	}
	
}
