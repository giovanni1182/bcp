package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.CotacaoDolarHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Uteis;
import com.gvs.crm.model.VariacaoIPCHome;
import com.gvs.crm.report.CalculoRatios1XLS;
import com.gvs.crm.report.CalculoRatiosAgregadosXLS;
import com.gvs.crm.report.CalculoRatiosXLS;
import com.gvs.crm.report.MargemSolvenciaXLS;
import com.gvs.crm.report.RatiosAgregadosXLS;
import com.gvs.crm.report.RelAlertaTrempano2XLS;
import com.gvs.crm.report.RelAlertaTrempanoXLS;
import com.gvs.crm.report.RelInformacaoMercado2XLS;
import com.gvs.crm.report.RelInformacaoMercadoGeralXLS;
import com.gvs.crm.report.ResultadoResumidoXLS;
import com.gvs.crm.view.CalculoRatiosView;
import com.gvs.crm.view.MargemSolvenciaView;
import com.gvs.crm.view.RelAlertaTrempanoView;
import com.gvs.crm.view.RelInformacaoMercadoGeralView;
import com.gvs.crm.view.RelInformacaoMercadoView;
import com.gvs.crm.view.ResultResumidoView;

import infra.control.Action;
import infra.control.Control;

public class RatioFinanceiroControl extends Control 
{
	public void calculoRatios(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		
		Date dataInicio = action.getDate("dataInicio");
		Date dataFim = action.getDate("dataFim");
		Collection<Entidade> aseguradorasMarcadas = new ArrayList<Entidade>();
		boolean todas = action.getBoolean("todas");
		boolean calculoRatio1 = action.getBoolean("calculoRatios1");
		boolean ratiosAgregados = action.getBoolean("ratiosAgregados");
		boolean ratiosAgregados1 = action.getBoolean("ratiosAgregados1");
		
		try 
		{
			String[] aseguradoras = action.getStringArray("aseguradoraId");
			Aseguradora aseguradora;
			
			for(int i = 0 ; i < aseguradoras.length ; i++)
			{
				String idStr = aseguradoras[i];
				
				aseguradora = (Aseguradora) home.obterEntidadePorId(Long.valueOf(idStr));
				
				aseguradorasMarcadas.add(aseguradora);
			}
			
			if(action.getBoolean("view"))
			{
				if(!todas)
					aseguradorasMarcadas = new ArrayList<Entidade>();
				
				this.setResponseView(new CalculoRatiosView(dataInicio, dataFim, aseguradorasMarcadas, todas, calculoRatio1, ratiosAgregados, ratiosAgregados1));
			}
			else
			{
				if(aseguradorasMarcadas.size() == 0)
					throw new Exception("Elija las Aseguradoras");
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en blanco");
				
				if(ratiosAgregados1)
				{
					if(aseguradorasMarcadas.size() == 1)
						throw new Exception("Esta opción es utilizada para realizar cálculos para más de una Aseguradora, elegir más de una Aseguradora");
				}
				
				if(calculoRatio1)
				{
					CalculoRatios1XLS xls = new CalculoRatios1XLS(dataInicio, dataFim, aseguradorasMarcadas, home);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Calculo de Ratios Financieros.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else if(ratiosAgregados)
				{
					RatiosAgregadosXLS xls = new RatiosAgregadosXLS(dataInicio, dataFim, aseguradorasMarcadas, home);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Calculo de Ratios Agregados.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else if(ratiosAgregados1)
				{
					CalculoRatiosAgregadosXLS xls = new CalculoRatiosAgregadosXLS(dataInicio, dataFim, aseguradorasMarcadas, home);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Calculo de Ratios Agregados1.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
				{
					CalculoRatiosXLS xls = new CalculoRatiosXLS(dataInicio, dataFim, aseguradorasMarcadas, home);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Calculo de Ratios Financieros1.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
			}
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new CalculoRatiosView(dataInicio, dataFim, aseguradorasMarcadas, todas, calculoRatio1, ratiosAgregados, ratiosAgregados1));
		}
	}
	
	public void calculoMargemSolvencia(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		CotacaoDolarHome cotacaoHome = (CotacaoDolarHome) mm.getHome("CotacaoDolarHome");
		VariacaoIPCHome variacaoHome = (VariacaoIPCHome) mm.getHome("VariacaoIPCHome");
		
		Aseguradora aseguradora = null;
		Date dataInicio = null;
		Date dataFim = null;
		try 
		{
			if(!action.getBoolean("view"))
			{
				long aseguradoraId = action.getLong("aseguradoraId2");
				if(aseguradoraId > 0)
					aseguradora = (Aseguradora) home.obterEntidadePorId(aseguradoraId);
				
				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				
				if(dataInicio == null)
					throw new Exception("Fecha Inicio en Blanco");
				if(dataFim == null)
					throw new Exception("Fecha Fin en Blanco");
				
				MargemSolvenciaXLS xls = new MargemSolvenciaXLS(aseguradora, dataInicio, dataFim, home, cotacaoHome, variacaoHome);
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("MargemSolvencia.xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			this.setResponseView(new MargemSolvenciaView(aseguradora, dataInicio, dataFim));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new MargemSolvenciaView(aseguradora, dataInicio, dataFim));
		}
	}
	
	public void calculoInformacaoMercado(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Uteis uteis = new Uteis();
		
		Date dataInicio = null;
		Date dataFim = null;
		int anoInicio = 0;
		int anoFim = 0;
		String dataStr, anoFiscal = "";
		try 
		{
			if(!action.getBoolean("view"))
			{
				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				anoInicio = action.getInt("anoInicio");
				anoFim = action.getInt("anoFim");
				anoFiscal = action.getString("anoFiscal");
				
				if(dataInicio == null && dataFim == null && anoInicio == 0 && anoFim == 0)
					throw new Exception("Periodos en Blanco");
				
				if(dataInicio != null)
				{
					if(dataFim == null)
						throw new Exception("Fecha Fin en Blanco");
				}
				
				if(dataFim != null)
				{
					if(dataInicio == null)
						throw new Exception("Fecha Inicio en Blanco");
					
				}
				
				if(anoInicio > 0)
				{
					dataStr = "01/01/"+anoInicio;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Inicio no es correcto");
					
					if(anoFim == 0)
						throw new Exception("Año Fin en Blanco");
				}
				
				if(anoFim > 0)
				{
					dataStr = "01/01/"+anoFim;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Fin no es correcto");
					
					if(anoInicio == 0)
						throw new Exception("Año Inicio en Blanco");
				}
				
				boolean anoFiscalBoolean = anoFiscal.equals("Sim");
				
				RelInformacaoMercado2XLS xls = new RelInformacaoMercado2XLS(dataInicio, dataFim, anoInicio,anoFim,home, anoFiscalBoolean);
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Información del Mercado.xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			this.setResponseView(new RelInformacaoMercadoView(dataInicio, dataFim, anoInicio, anoFim, anoFiscal));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelInformacaoMercadoView(dataInicio, dataFim, anoInicio, anoFim, anoFiscal));
		}
	}
	
	public void calculoInformacaoMercadoGeral(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Uteis uteis = new Uteis();
		
		Date dataInicio = null;
		Date dataFim = null;
		int anoInicio = 0;
		int anoFim = 0;
		String tipo = action.getString("tipo");
		String dataStr, anoFiscal = "";
		try 
		{
			if(!action.getBoolean("view"))
			{
				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				anoInicio = action.getInt("anoInicio");
				anoFim = action.getInt("anoFim");
				anoFiscal = action.getString("anoFiscal");
				
				if(dataInicio == null && dataFim == null && anoInicio == 0 && anoFim == 0)
					throw new Exception("Periodos en Blanco");
				
				if(dataInicio != null)
				{
					if(dataFim == null)
						throw new Exception("Fecha Fin en Blanco");
				}
				
				if(dataFim != null)
				{
					if(dataInicio == null)
						throw new Exception("Fecha Inicio en Blanco");
					
				}
				
				if(anoInicio > 0)
				{
					dataStr = "01/01/"+anoInicio;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Inicio no es correcto");
					
					if(anoFim == 0)
						throw new Exception("Año Fin en Blanco");
				}
				
				if(anoFim > 0)
				{
					dataStr = "01/01/"+anoFim;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Fin no es correcto");
					
					if(anoInicio == 0)
						throw new Exception("Año Inicio en Blanco");
				}
				
				boolean totalNormal = true;
				boolean totalPorcentagem = true;
				Collection<String> calculoTotal = null;
				
				Map<String,String> contas = new TreeMap<>();
				if(tipo.equals("Activos"))
				{
					contas.put("0101000000","Disponibilidad");
					contas.put("0102000000","Créditos Técnicos Vigentes");
					contas.put("0103000000","Créditos Técnicos Vencidos");
					contas.put("0104000000","Créditos Administrativos");
					contas.put("0105000000","Gastos Pagados por Adelantado");
					contas.put("0106000000","Bienes y Derechos Recibidos en Pago");
					contas.put("0107000000","Inversiones");
					contas.put("0108000000","Bienes de Uso");
					contas.put("0109000000","Activos Diferidos");
				}
				else if(tipo.equals("Pasivos"))
				{
					contas.put("0201000000","Deudas Financieras");
					contas.put("0202000000","Deudas con Asegurados");
					contas.put("0203000000","Deudas por Coaseguros");
					contas.put("0204000000","Deudas por Reaseguros - Local");
					contas.put("0205000000","Deudas por Reaseguros - Exterior");
					contas.put("0206000000","Deudas con Intermediarios");
					contas.put("0210000000","Otras Deudas Técnicas");
					contas.put("0211000000","Obligaciones Admininstrativas");
					contas.put("0212000000","Provisiones Técnicas de Seguros");
					contas.put("0213000000","Provisiones Técnicas por Siniestros");
					contas.put("0214000000","Utilidades Diferidas");
				}
				else if(tipo.equals("Patrimonio Neto"))
				{
					contas.put("0301000000","Capital Social");
					contas.put("0302000000","Cuentas Pendientes de Capitalización");
					contas.put("0303000000","Reservas");
					contas.put("0304000000","Resultados Acumulados");
					contas.put("0305000000","Resultados del Ejercicio");
				}
				else if(tipo.equals("Ingresos"))
				{
					contas.put("0401000000","Primas Directas");
					contas.put("0402000000","Primas Reas. Aceptadas - Local");
					contas.put("0403000000","Primas Reas. Aceptadas - Exterior");
					contas.put("0404000000","Desafect. De Porvis. Técn. de Seguros");
					contas.put("0405000000","Reintegro Gtos. de Producción");
					contas.put("0406000000","Desafect. De Porvis. Técn. por Siniestros");
					contas.put("0407000000","Recupero de Siniestros");
					contas.put("0408000000","Stros. Recup. Reas. Cedidos - Local");
					contas.put("0409000000","Stros. Recup. Reas. Cedidos - Exterior");
					contas.put("0410000000","Otros Ingresos. Reas. Cedidos - Local");
					contas.put("0411000000","Otros Ingresos. Reas. Cedidos - Exterior");
					contas.put("0412000000","Part. Recup. Reas. Aceptad. - Local");
					contas.put("0413000000","Part. Ingres.. Reas. Aceptad. - Local");
					contas.put("0415000000","Part. Ingres.. Reas. Aceptad. - Exterior");
					contas.put("0425000000","Ingresos de Inversión");
					contas.put("0426000000","Desafectación de Previsiones");
					contas.put("0435000000","Ganancias Extraordinarias");
				}
				else if(tipo.equals("Egresos"))
				{
					contas.put("0501000000","Primas Reas. Cedidas - Local");
					contas.put("0502000000","Primas Reas. Cedidas - Exterior");
					contas.put("0503000000","Constitución Provis. Técn. de Seguros");
					contas.put("0504000000","Gastos de Producción");
					contas.put("0505000000","Constitución Provis. Técn. por Siniestros");
					contas.put("0506000000","Siniestros");
				}
				else if(tipo.equals("Siniestros Netos Ocurridos"))
				{
					contas.put("aa_0506000000","Siniestros");
					contas.put("ab_0507000000","Prestaciones e Indemnizaciones Seguros de Vida");
					contas.put("ac_0508000000","Gastos Liquidación de Siniestro, Salvataje y Recupero");
					contas.put("ad_0509000000","Participación Recupero Reaseguros Cedidos Local");
					contas.put("ae_0511000000","Participación Recupero Reaseguros Cedidos Exterior");
					contas.put("af_0513000000","Siniestros Reaseguros Aceptados Local");
					contas.put("ag_0515000000","Siniestros Reaseguros Aceptados Exterior");
					contas.put("ah_0505000000","Const. De Prov. Técnicas de Siniestros");
					contas.put("ai_0407000000","Recupero de Siniestros");
					contas.put("aj_0408000000","Siniestros Recuperados Reaseguros Cedidos Local");
					contas.put("ak_0409000000","Siniestros Recuperados Reaseguros Cedidos Exterior");
					contas.put("al_0412000000","Participación Recupero Reaseguros Aceptados Local");
					contas.put("am_0414000000","Participación Recupero Reaseguros Aceptados Exterior");
					contas.put("an_0406000000","Desafectación de Provisiones Técnicas por Siniestros");
					
					totalNormal = false;
					totalPorcentagem = false;
					
					calculoTotal = new ArrayList<>();
					calculoTotal.add("0506000000");
					calculoTotal.add("+");
					calculoTotal.add("0507000000");
					calculoTotal.add("+");
					calculoTotal.add("0508000000");
					calculoTotal.add("+");
					calculoTotal.add("0509000000");
					calculoTotal.add("+");
					calculoTotal.add("0511000000");
					calculoTotal.add("+");
					calculoTotal.add("0513000000");
					calculoTotal.add("+");
					calculoTotal.add("0515000000");
					calculoTotal.add("+");
					calculoTotal.add("0505000000");
					calculoTotal.add("-");
					calculoTotal.add("0407000000");
					calculoTotal.add("-");
					calculoTotal.add("0408000000");
					calculoTotal.add("-");
					calculoTotal.add("0409000000");
					calculoTotal.add("-");
					calculoTotal.add("0412000000");
					calculoTotal.add("-");
					calculoTotal.add("0414000000");
					calculoTotal.add("-");
					calculoTotal.add("0406000000");
				}
				else if(tipo.equals("Otros Ingresos y Egresos Tecnicos"))
				{
					contas.put("aa_0405000000","Reintegro de Gastos de Producción");
					contas.put("ab_0410000000","Otros Ingresos por Reaseguros Cedidos Local");
					contas.put("ac_0411000000","Otros Ingresos por Reaseguros Cedidos Exterior");
					contas.put("ad_0413000000","Otros Ingresos por Reaseguros Aceptados Local");
					contas.put("ae_0415000000","Otros Ingresos por Reaseguros Aceptados Exterior");
					contas.put("af_0426000000","Desafectación de Previsiones");
					contas.put("ag_0504000000","Gastos de Producción");
					contas.put("ah_0510000000","Gastos de Cesión de Reaseguros Local");
					contas.put("ai_0512000000","Gastos de Cesión de Reaseguros Exterior");
					contas.put("aj_0514000000","Gastos de Reaseguros Aceptados Local");
					contas.put("ak_0516000000","Gastos de Reaseguros Aceptados Exterior");
					contas.put("al_Composta1","Gastos Técnicos de Explotación / Excluido I.R.");
					contas.put("am_0527000000","Constitución de Previsiones");
					contas.put("an_Composta2 ","OTROS RESULTADOS TÉCNICOS");
					contas.put("ao_0435000000","Ganancias Extra Ordinarias");
					contas.put("ap_0535000000","Perdidas Extra Ordinarias");
					contas.put("aq_Composta3","RESULTADOS EXTRA ORDINARIOS NETOS");
					contas.put("ar_0525000000","Gastos Técnicos de Explotación / Inlcuido I.R.");
					contas.put("as_0525010401","Impuesto a la RENTA");
					
					totalPorcentagem = false;
				}
				
				boolean anoFiscalBoolean = anoFiscal.equals("Sim");
				
				RelInformacaoMercadoGeralXLS xls = new RelInformacaoMercadoGeralXLS(dataInicio, dataFim, anoInicio,anoFim,home, tipo, contas, anoFiscalBoolean, totalNormal, totalPorcentagem, calculoTotal);
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Información del Mercado - "+tipo+".xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
			
			this.setResponseView(new RelInformacaoMercadoGeralView(dataInicio, dataFim, anoInicio, anoFim, tipo, anoFiscal));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelInformacaoMercadoGeralView(dataInicio, dataFim, anoInicio, anoFim, tipo, anoFiscal));
		}
	}
	
	public void calculoAlertaTrempano(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Uteis uteis = new Uteis();
		Collection<Aseguradora> aseguradorasMarcadas = new ArrayList<Aseguradora>();
		boolean todas = action.getBoolean("todas");
		Date dataInicio = null;
		Date dataFim = null;
		int anoInicio = 0;
		int anoFim = 0;
		boolean alerta2 = action.getBoolean("alerta2");
		String dataStr, anoFiscal = "";
		try 
		{
			String[] aseguradoras = action.getStringArray("aseguradoraId");
			Aseguradora aseg;
			String idStr;
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
			dateFormat.setLenient(false);
			
			dataStr =  action.getString("dataInicio");
			if(!dataStr.equals(""))
			{
				try
				{
					dataInicio = dateFormat.parse(dataStr);
				}
				catch(Exception e)
				{
					throw new Exception("Mes Inicio no es correcto");
				}
			}
			
			dataStr =  action.getString("dataFim");
			if(!dataStr.equals(""))
			{
				try
				{
					dataFim = dateFormat.parse(dataStr);
				}
				catch(Exception e)
				{
					throw new Exception("Mes Fin no es correcto");
				}
			}
			
			anoInicio = action.getInt("anoInicio");
			anoFim = action.getInt("anoFim");
			
			anoFiscal = action.getString("anoFiscal");
			
			for(int i = 0 ; i < aseguradoras.length ; i++)
			{
				idStr = aseguradoras[i];
				
				aseg = (Aseguradora) home.obterEntidadePorId(Long.valueOf(idStr));
				
				aseguradorasMarcadas.add(aseg);
			}
			
			if(!action.getBoolean("view"))
			{
				if(aseguradorasMarcadas.size() == 0)
					throw new Exception("Elija las Aseguradoras");
				
				if(dataInicio == null && dataFim == null && anoInicio == 0 && anoFim == 0)
					throw new Exception("Periodos en Blanco");
				
				if(aseguradoras.length == 0)
					throw new Exception("Elegir Aseguradoras");
				
				if(dataInicio != null)
				{
					if(dataFim == null)
						throw new Exception("Fecha Fin en Blanco");
				}
				
				if(dataFim != null)
				{
					if(dataInicio == null)
						throw new Exception("Fecha Inicio en Blanco");
				}
				
				if(anoInicio > 0)
				{
					dataStr = "01/01/"+anoInicio;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Inicio no es correcto");
					
					if(anoFim == 0)
						throw new Exception("Año Fin en Blanco");
				}
				
				if(anoFim > 0)
				{
					dataStr = "01/01/"+anoFim;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Fin no es correcto");
					
					if(anoInicio == 0)
						throw new Exception("Año Inicio en Blanco");
				}
				
				boolean anoFiscalBoolean = anoFiscal.equals("Sim");
				
				if(!alerta2)
				{
					RelAlertaTrempanoXLS xls = new RelAlertaTrempanoXLS(aseguradorasMarcadas, dataInicio, dataFim, anoInicio, anoFim, home, aseguradoraHome, anoFiscalBoolean);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Alerta Temprana.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
				else
				{
					RelAlertaTrempano2XLS xls = new RelAlertaTrempano2XLS(aseguradorasMarcadas, dataInicio, dataFim, anoInicio, anoFim, home, aseguradoraHome, anoFiscalBoolean);
					
					InputStream arquivo = xls.obterArquivo();
					this.setResponseInputStream(arquivo);
			        this.setResponseFileName("Alerta Temprana2.xls");
			        this.setResponseContentType("application/vnd.ms-excel");
			        this.setResponseContentSize(arquivo.available());
				}
			}
			
			this.setResponseView(new RelAlertaTrempanoView(dataInicio, dataFim, anoInicio, anoFim, aseguradorasMarcadas, todas, false, anoFiscal, alerta2));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelAlertaTrempanoView(dataInicio, dataFim, anoInicio, anoFim, aseguradorasMarcadas, todas, true, anoFiscal, alerta2));
		}
	}
	
	public void calculoResultadoResumido(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
		Uteis uteis = new Uteis();
		Date dataInicio = null;
		Date dataFim = null;
		int anoInicio = 0;
		int anoFim = 0;
		String dataStr, anoFiscal = "";
		try 
		{
			if(!action.getBoolean("view"))
			{
				dataInicio = action.getDate("dataInicio");
				dataFim = action.getDate("dataFim");
				anoInicio = action.getInt("anoInicio");
				anoFim = action.getInt("anoFim");
				
				if(dataInicio == null && dataFim == null && anoInicio == 0 && anoFim == 0)
					throw new Exception("Periodos en Blanco");
				
				if(dataInicio != null)
				{
					if(dataFim == null)
						throw new Exception("Fecha Fin en Blanco");
				}
				
				if(dataFim != null)
				{
					if(dataInicio == null)
						throw new Exception("Fecha Inicio en Blanco");
					
				}
				
				if(anoInicio > 0)
				{
					dataStr = "01/01/"+anoInicio;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Inicio no es correcto");
					
					if(anoFim == 0)
						throw new Exception("Año Fin en Blanco");
				}
				
				if(anoFim > 0)
				{
					dataStr = "01/01/"+anoFim;
					if(!uteis.validaData(dataStr))
						throw new Exception("Año Fin no es correcto");
					
					if(anoInicio == 0)
						throw new Exception("Año Inicio en Blanco");
				}
				
				ResultadoResumidoXLS xls = new ResultadoResumidoXLS(dataInicio, dataFim, anoInicio, anoFim, home, false);
				
				InputStream arquivo = xls.obterArquivo();
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName("Resultado Resumido.xls");
		        this.setResponseContentType("application/vnd.ms-excel");
		        this.setResponseContentSize(arquivo.available());
			}
				
			this.setResponseView(new ResultResumidoView(dataInicio, dataFim, anoInicio, anoFim));
		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new ResultResumidoView(dataInicio, dataFim, anoInicio, anoFim));
		}
	}
}
