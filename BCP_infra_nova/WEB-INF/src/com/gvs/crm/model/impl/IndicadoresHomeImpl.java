package com.gvs.crm.model.impl;

import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.IndicadoresHome;
import com.gvs.crm.model.Usuario;

import infra.model.Home;

public class IndicadoresHomeImpl extends Home implements IndicadoresHome
{
	private EntidadeHome home;
	private AseguradoraHome aseguradorHome;
	private ClassificacaoContas cConta0100;
	private ClassificacaoContas cConta0101;
	private ClassificacaoContas cConta0102;
	private ClassificacaoContas cConta0103;
	private ClassificacaoContas cConta0104;
	private ClassificacaoContas cConta0105;
	private ClassificacaoContas cConta0106;
	private ClassificacaoContas cConta0107;
	private ClassificacaoContas cConta0108;
	private ClassificacaoContas cConta0109;
	private ClassificacaoContas cConta010903;
	private ClassificacaoContas cConta010904;
	private ClassificacaoContas cConta010905;
	private ClassificacaoContas cConta010906;
	private ClassificacaoContas cConta0201;
	private ClassificacaoContas cConta0202;
	private ClassificacaoContas cConta0203;
	private ClassificacaoContas cConta0204;
	private ClassificacaoContas cConta0205;
	private ClassificacaoContas cConta0206;
	private ClassificacaoContas cConta0210;
	private ClassificacaoContas cConta0211;
	private ClassificacaoContas cConta0212;
	private ClassificacaoContas cConta0213;
	private ClassificacaoContas cConta0214;
	private ClassificacaoContas cConta0300;
	private ClassificacaoContas cConta0301;
	private ClassificacaoContas cConta0302;
	private ClassificacaoContas cConta0303;
	private ClassificacaoContas cConta0304;
	private ClassificacaoContas cConta0400;
	private ClassificacaoContas cConta0401;
	private ClassificacaoContas cConta0402;
	private ClassificacaoContas cConta0403;
	private ClassificacaoContas cConta0404;
	private ClassificacaoContas cConta0405;
	private ClassificacaoContas cConta0406;
	private ClassificacaoContas cConta0407;
	private ClassificacaoContas cConta0408;
	private ClassificacaoContas cConta0409;
	private ClassificacaoContas cConta0410;
	private ClassificacaoContas cConta0411;
	private ClassificacaoContas cConta0413;
	private ClassificacaoContas cConta0412;
	private ClassificacaoContas cConta0414;
	private ClassificacaoContas cConta0415;
	private ClassificacaoContas cConta0426;
	private ClassificacaoContas cConta0500;
	private ClassificacaoContas cConta0501;
	private ClassificacaoContas cConta0502;
	private ClassificacaoContas cConta0503;
	private ClassificacaoContas cConta0504;
	private ClassificacaoContas cConta0505;
	private ClassificacaoContas cConta0506;
	private ClassificacaoContas cConta0507;
	private ClassificacaoContas cConta0508;
	private ClassificacaoContas cConta0509;
	private ClassificacaoContas cConta0510;
	private ClassificacaoContas cConta0511;
	private ClassificacaoContas cConta0512;
	private ClassificacaoContas cConta0513;
	private ClassificacaoContas cConta0514;
	private ClassificacaoContas cConta0515;
	private ClassificacaoContas cConta0516;
	private ClassificacaoContas cConta0525;
	private ClassificacaoContas cConta0527;
	private Conta cConta0525010401;
	private DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	private FileWriter file;
	private FileWriter fileLog;
	private Date mesAno;
	private String nomeArquivo;
	private Map aseguradoras = new TreeMap();
	private Usuario usuario;
	
	//Contas para o Rel GEE, pode ser que tenha alguma repetida com relação as de cima
	private ClassificacaoContas c0401010000;
	private ClassificacaoContas c0402000000;
	private ClassificacaoContas c0403000000;
	private ClassificacaoContas c0407000000;
	private ClassificacaoContas c0408000000;
	private ClassificacaoContas c0409000000;
	private ClassificacaoContas c0410010000;
	private ClassificacaoContas c0410040000;
	private ClassificacaoContas c0410050000;
	private ClassificacaoContas c0411010000;
	private ClassificacaoContas c0411040000;
	private ClassificacaoContas c0411050000;
	private ClassificacaoContas c0412000000;
	private ClassificacaoContas c0413010000;
	private ClassificacaoContas c0413020000;
	private ClassificacaoContas c0414000000;
	private ClassificacaoContas c0415010000;
	private ClassificacaoContas c0415020000;
	private ClassificacaoContas c0426050000;
	private ClassificacaoContas c0435000000;
	private ClassificacaoContas c0501150200;
	private ClassificacaoContas c0504010000;
	private ClassificacaoContas c0506000000;
	private ClassificacaoContas c0507000000;
	private ClassificacaoContas c0508000000;
	private ClassificacaoContas c0510020000;
	private ClassificacaoContas c0512020000;
	private ClassificacaoContas c0513000000;
	private ClassificacaoContas c0514010000;
	private ClassificacaoContas c0514040000;
	private ClassificacaoContas c0514050000;
	private ClassificacaoContas c0515000000;
	private ClassificacaoContas c0516010000;
	private ClassificacaoContas c0516040000;
	private ClassificacaoContas c0516050000;
	private ClassificacaoContas c0525010000;
	private ClassificacaoContas c0535000000;
	
	private void instanciarContas() throws Exception
	{
		home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		aseguradorHome = (AseguradoraHome) this.getModelManager().getHome("AseguradoraHome");
		
		cConta0100 = (ClassificacaoContas) home.obterEntidadePorApelido("0100000000");
		cConta0101 = (ClassificacaoContas) home.obterEntidadePorApelido("0101000000");
		cConta0102 = (ClassificacaoContas) home.obterEntidadePorApelido("0102000000");
		cConta0103 = (ClassificacaoContas) home.obterEntidadePorApelido("0103000000");
		cConta0104 = (ClassificacaoContas) home.obterEntidadePorApelido("0104000000");
		cConta0105 = (ClassificacaoContas) home.obterEntidadePorApelido("0105000000");
		cConta0106 = (ClassificacaoContas) home.obterEntidadePorApelido("0106000000");
		cConta0107 = (ClassificacaoContas) home.obterEntidadePorApelido("0107000000");
		cConta0108 = (ClassificacaoContas) home.obterEntidadePorApelido("0108000000");
		cConta0109 = (ClassificacaoContas) home.obterEntidadePorApelido("0109000000");
		cConta010903 = (ClassificacaoContas) home.obterEntidadePorApelido("0109030000");
		cConta010904 = (ClassificacaoContas) home.obterEntidadePorApelido("0109040000");
		cConta010905 = (ClassificacaoContas) home.obterEntidadePorApelido("0109050000");
		cConta010906 = (ClassificacaoContas) home.obterEntidadePorApelido("0109060000");
		cConta0201 = (ClassificacaoContas) home.obterEntidadePorApelido("0201000000");
		cConta0202 = (ClassificacaoContas) home.obterEntidadePorApelido("0202000000");
		cConta0203 = (ClassificacaoContas) home.obterEntidadePorApelido("0203000000");
		cConta0204 = (ClassificacaoContas) home.obterEntidadePorApelido("0204000000");
		cConta0205 = (ClassificacaoContas) home.obterEntidadePorApelido("0205000000");
		cConta0206 = (ClassificacaoContas) home.obterEntidadePorApelido("0206000000");
		cConta0210 = (ClassificacaoContas) home.obterEntidadePorApelido("0210000000");
		cConta0211 = (ClassificacaoContas) home.obterEntidadePorApelido("0211000000");
		cConta0212 = (ClassificacaoContas) home.obterEntidadePorApelido("0212000000");
		cConta0213 = (ClassificacaoContas) home.obterEntidadePorApelido("0213000000");
		cConta0214 = (ClassificacaoContas) home.obterEntidadePorApelido("0214000000");
		cConta0300 = (ClassificacaoContas) home.obterEntidadePorApelido("0300000000");
		cConta0301 = (ClassificacaoContas) home.obterEntidadePorApelido("0301000000");
		cConta0302 = (ClassificacaoContas) home.obterEntidadePorApelido("0302000000");
		cConta0303 = (ClassificacaoContas) home.obterEntidadePorApelido("0303000000");
		cConta0304 = (ClassificacaoContas) home.obterEntidadePorApelido("0304000000");
		cConta0400 = (ClassificacaoContas) home.obterEntidadePorApelido("0400000000");
		cConta0401 = (ClassificacaoContas) home.obterEntidadePorApelido("0401000000");
		cConta0402 = (ClassificacaoContas) home.obterEntidadePorApelido("0402000000");
		cConta0403 = (ClassificacaoContas) home.obterEntidadePorApelido("0403000000");
		cConta0404 = (ClassificacaoContas) home.obterEntidadePorApelido("0404000000");
		cConta0405 = (ClassificacaoContas) home.obterEntidadePorApelido("0405000000");
		cConta0406 = (ClassificacaoContas) home.obterEntidadePorApelido("0406000000");
		cConta0407 = (ClassificacaoContas) home.obterEntidadePorApelido("0407000000");
		cConta0408 = (ClassificacaoContas) home.obterEntidadePorApelido("0408000000");
		cConta0409 = (ClassificacaoContas) home.obterEntidadePorApelido("0409000000");
		cConta0410 = (ClassificacaoContas) home.obterEntidadePorApelido("0410000000");
		cConta0411 = (ClassificacaoContas) home.obterEntidadePorApelido("0411000000");
		cConta0413 = (ClassificacaoContas) home.obterEntidadePorApelido("0413000000");
		cConta0412 = (ClassificacaoContas) home.obterEntidadePorApelido("0412000000");
		cConta0414 = (ClassificacaoContas) home.obterEntidadePorApelido("0414000000");
		cConta0415 = (ClassificacaoContas) home.obterEntidadePorApelido("0415000000");
		cConta0426 = (ClassificacaoContas) home.obterEntidadePorApelido("0426000000");
		cConta0500 = (ClassificacaoContas) home.obterEntidadePorApelido("0500000000");
		cConta0501 = (ClassificacaoContas) home.obterEntidadePorApelido("0501000000");
		cConta0502 = (ClassificacaoContas) home.obterEntidadePorApelido("0502000000");
		cConta0503 = (ClassificacaoContas) home.obterEntidadePorApelido("0503000000");
		cConta0504 = (ClassificacaoContas) home.obterEntidadePorApelido("0504000000");
		cConta0505 = (ClassificacaoContas) home.obterEntidadePorApelido("0505000000");
		cConta0506 = (ClassificacaoContas) home.obterEntidadePorApelido("0506000000");
		cConta0507 = (ClassificacaoContas) home.obterEntidadePorApelido("0507000000");
		cConta0508 = (ClassificacaoContas) home.obterEntidadePorApelido("0508000000");
		cConta0509 = (ClassificacaoContas) home.obterEntidadePorApelido("0509000000");
		cConta0510 = (ClassificacaoContas) home.obterEntidadePorApelido("0510000000");
		cConta0511 = (ClassificacaoContas) home.obterEntidadePorApelido("0511000000");
		cConta0512 = (ClassificacaoContas) home.obterEntidadePorApelido("0512000000");
		cConta0513 = (ClassificacaoContas) home.obterEntidadePorApelido("0513000000");
		cConta0514 = (ClassificacaoContas) home.obterEntidadePorApelido("0514000000");
		cConta0515 = (ClassificacaoContas) home.obterEntidadePorApelido("0515000000");
		cConta0516 = (ClassificacaoContas) home.obterEntidadePorApelido("0516000000");
		cConta0525 = (ClassificacaoContas) home.obterEntidadePorApelido("0525000000");
		cConta0527 = (ClassificacaoContas) home.obterEntidadePorApelido("0527000000");
		cConta0525010401 = (Conta) home.obterEntidadePorApelido("0525010401");
		
		for(Iterator i = aseguradorHome.obterAseguradorasPorMenor80OrdenadoPorNome().iterator(); i.hasNext() ; )
		{
			Aseguradora aseg = (Aseguradora) i.next();
			
			aseguradoras.put(aseg.obterId(), aseg);
		}	
		 
	}
	
	public void setMesAno(Date data)
	{
		this.mesAno = data;
	}
	
	public void instanciarContasGEE() throws Exception
	{
		home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		aseguradorHome = (AseguradoraHome) this.getModelManager().getHome("AseguradoraHome");
		
		this.c0401010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0401010000");
		this.c0402000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0402000000");
		this.c0403000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0403000000");
		this.c0407000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0407000000");
		this.c0408000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0408000000");
		this.c0409000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0409000000");
		this.c0410010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0410010000");
		this.c0410040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0410040000");
		this.c0411010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0411010000");
		this.c0411040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0411040000");
		this.c0411050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0411050000");
		this.c0410050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0410050000");
		this.c0412000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0412000000");
		this.c0413010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0413010000");
		this.c0413020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0413020000");
		this.c0414000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0414000000");
		this.c0415010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0415010000");
		this.c0415020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0415020000");
		this.c0426050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0426050000");
		this.c0435000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0435000000");
		this.c0501150200 = (ClassificacaoContas) home.obterEntidadePorApelido("0501150200");
		this.c0504010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0504010000");
		this.c0506000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0506000000");
		this.c0507000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0507000000");
		this.c0508000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0508000000");
		this.c0510020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0510020000");
		this.c0512020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0512020000");
		this.c0513000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0513000000");
		this.c0514010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0514010000");
		this.c0514040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0514040000");
		this.c0514050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0514050000");
		this.c0515000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0515000000");
		this.c0516010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0516010000");
		this.c0516040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0516040000");
		this.c0516050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0516050000");
		this.c0525010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0525010000");
		this.c0535000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0535000000");
		
		for(Iterator i = aseguradorHome.obterAseguradorasPorMenor80OrdenadoPorNome().iterator(); i.hasNext() ; )
		{
			Aseguradora aseg = (Aseguradora) i.next();
			
			aseguradoras.put(aseg.obterId(), aseg);
		}	
	}
	
	public void calcularIndicadoresTecnicos(Date mesAno, Usuario usuario, boolean montarArquivo) throws Exception
	{
		this.mesAno = mesAno;
		this.usuario = usuario;
		this.instanciarContas();
		if(montarArquivo)
		{
			this.montarArquivo();
			//this.gerarLog();
			
			file.close();
			//fileLog.close();
		}
		/*this.calcularSinistrosBrutosPD();
		this.calcularSinistrosNetosPDNR();
		this.calcularGastosOperativosPD();
		this.calcularGastosDeProducaoPNA();
		this.calcularGastosDeExportacaoPNA();
		this.calcularProvisoesTecnicas();
		
		//Não Acumulados
		this.calcularPNAtivoTotal();
		this.calcularRetornoSemPN();
		this.calcularResultadoTecnicoSemPN();
		this.calcularMagemDeGanancia();
		//this.calcularMargemSolvencia();
*/		
		
		
		//Runtime.getRuntime().exec("cmd.exe /C start EXCEL.exe " + this.nomeArquivo);
		
	}
	
	public double obterGastosDeExportacaoPNA(Entidade aseg) throws Exception
	{
		double total = 0;
		
		Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
		
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		boolean existeAgenda = true;
		
		Calendar c = Calendar.getInstance();
		c.setTime(mesAno);
		
		String mesAnoFOR,mesStr,anoStr;
		int mes, ano;
		
		for(int j = 0 ; j < 12 ; j++)
		{
			mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
			
			mesStr = new SimpleDateFormat("MM").format(c.getTime());
			anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
			
			mes = Integer.parseInt(mesStr);
			ano  = Integer.parseInt(anoStr);
			
			if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
			{
				/*totalNumerador += cConta0525.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0525.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
				
				totalDenominador += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
				totalDenominador += cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
				totalDenominador += cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);*/
				
				totalNumerador += cConta0525.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				
				totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			}
			
			c.add(Calendar.MONTH, -1);
			
			/*else
			{
				existeAgenda = false;
				break;
			}*/
		}
		
		if(totalDenominador == 0)
			total = -714;
		//else if(existeAgenda)
		else
			total = (totalNumerador / totalDenominador);
		/*else
			total = -714;*/
		
		return total;
	}
	
	public double obterMargemPonderadaGastosDeExportacaoPNA() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					
					String mesStr = new SimpleDateFormat("MM").format(c.getTime());
					String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*totalNumerador += cConta0525.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0525.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;
						totalDenominador += cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;
						totalDenominador += cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;*/
						
						totalNumerador += cConta0525.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						c.add(Calendar.MONTH, -1);
					}
				}
			}
		}
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterGastosDeProducaoPNA(Entidade aseg) throws Exception
	{
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double totalDenominador = 0;
				double total2 = 0;
				boolean existeAgenda = true;
				
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				String mesAnoFOR,mesStr,anoStr;
				int mes, ano;
				
				for(int j = 0 ; j < 12 ; j++)
				{
					mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					
					mesStr = new SimpleDateFormat("MM").format(c.getTime());
					anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					mes = Integer.parseInt(mesStr);
					ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*double credito0401 = cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						double debito0401 = cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						double debito0504 = cConta0504.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						double credito0504 = cConta0504.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);*/
						
						double saldo0504 = cConta0504.obterTotalizacaoExistente(aseguradora, mesAnoFOR); 
						double saldo0401 = cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador+=saldo0504;
						totalDenominador+=saldo0401;
						
						//totalNumerador += cConta0504.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0504.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						//totalDenominador += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						//totalNumerador+= debito0504 - credito0504;
						//totalDenominador+= credito0401 - debito0401;
						
						/*if(aseguradora.obterId() == 7949)
						{
							System.out.println("Cuenta " + cConta0504.obterApelido() + " " + mesStr+"/"+anoStr+ " " + formataValor.format(debito0504) + " (debito) - " + formataValor.format(credito0504) + " (credito) / Cuenta " + cConta0401.obterApelido()+" " + formataValor.format(credito0401) +" (credito) - " + formataValor.format(debito0401) + " (debito) = " + formataValor.format((debito0504 - credito0504) /(credito0401 - debito0401)));
							System.out.println("Cuenta " + cConta0504.obterApelido() + " " + mesStr+"/"+anoStr+ " " + formataValor.format(debito0504) + " (debito) - " + formataValor.format(credito0504) + " (credito) / Cuenta " + cConta0401.obterApelido()+" " + formataValor.format(credito0401) +" (credito) - " + formataValor.format(debito0401) + " (debito)");
							System.out.println("Cuenta " + cConta0504.obterApelido() + " " + mesStr+"/"+anoStr+ " " + formataValor.format(saldo0504) +" / Cuenta " + cConta0401.obterApelido() +" " + formataValor.format(saldo0401) +" (saldo) = " + formataValor.format(saldo0504 /saldo0401));
						}*/
					}
					
					c.add(Calendar.MONTH, -1);
					
					/*else
					{
						existeAgenda = false;
						break;
					}*/
				}
				
				if(totalDenominador == 0)
					total = -714;
				//else if(existeAgenda)
				else
				{
					total = (totalNumerador / totalDenominador);
				}
				
				/*if(aseguradora.obterId() == 7949)
				{
					DecimalFormat formataValor = new DecimalFormat("#,##0.00");
					
					System.out.println("totalNumerador " + formataValor.format(totalNumerador));
					System.out.println("totalDenominador " + formataValor.format(totalDenominador));
					System.out.println("Total " + formataValor.format(totalNumerador / totalDenominador));
					System.out.println("Total2 " + formataValor.format(total2));
				}*/
				
				/*else
					total = -714;*/
			//}
		//}
		
		return total;
	}
	
	public double obterMargemPonderadaGastosDeProducaoPNA() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					
					String mesStr = new SimpleDateFormat("MM").format(c.getTime());
					String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*totalNumerador += cConta0504.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0504.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;*/
						
						totalNumerador += cConta0504.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						c.add(Calendar.MONTH, -1);
					}
				}
			}
		}
				
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterGastosOperativosPD(Entidade aseg) throws Exception
	{
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double totalDenominador = 0;
				
				boolean existeAgenda = true;
				
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				String mesAnoFOR,mesStr,anoStr;
				int mes, ano;
				
				for(int j = 0 ; j < 12 ; j++)
				{
					mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					mesStr = new SimpleDateFormat("MM").format(c.getTime());
					anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					mes = Integer.parseInt(mesStr);
					ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*totalNumerador += cConta0504.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0504.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0525.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0525.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;
						totalDenominador += cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;
						totalDenominador += cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) ;*/
						
						totalNumerador += cConta0504.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0525.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					}
					
					c.add(Calendar.MONTH, -1);
					
					/*else
					{
						existeAgenda = false;
						break;
					}*/
				}
				
				if(totalDenominador == 0)
					total = -714;
				//else if(existeAgenda)
				else
				{
					total = (totalNumerador / totalDenominador);
				}
				/*else
					total = -714;*/
			//}
		//}
		
		return total;
	}
	
	public double obterMargemPonderadaGastosOperativosPD() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					String mesStr = new SimpleDateFormat("MM").format(c.getTime());
					String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*totalNumerador += cConta0504.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0504.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0525.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0525.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);*/
						
						totalNumerador += cConta0504.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0525.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						c.add(Calendar.MONTH, -1);
					}
				}
				
				if(totalDenominador!=0)
					System.out.println("Aseg: " + aseguradora.obterNome() + " Total = " + formataValor.format(totalNumerador / totalDenominador));
			}
		}
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterMagemDeGanancia(Entidade aseg) throws Exception
	{
		//INDICADORES DE 6 A 10 SÃO CALCULADOS SOMENTE O MES ATUAL, E NÃO OS ULTIMOS 12
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double totalDenominador = 0;
				boolean existeAgenda = true;
				
				/*Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{*/
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
					
					String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
					String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
					
						totalNumerador += cConta0400.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador -= cConta0500.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						//totalNumerador += this.obterNumeradorBG(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					}
				//}
				//else
					//existeAgenda = false;
				
				if(totalDenominador == 0)
					total=-714;
				//else if(existeAgenda)
				else
					total = (totalNumerador / totalDenominador);
				/*else
					total=-714;*/
			//}
		//}
		
		return total;
	}
	
	public double obterMargemPonderadaMagemDeGanancia() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
				
				String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
				String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
				{
					totalNumerador += cConta0400.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalNumerador -= cConta0500.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					//totalNumerador += this.obterNumeradorBG(aseguradora, mesAnoFOR);
					
					totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				}
			}
		}
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterPNAtivoTotal(Entidade aseg) throws Exception
	{
		//INDICADORES DE 6 A 10 SÃO CALCULADOS SOMENTE O MES ATUAL, E NÃO OS ULTIMOS 12
		
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double totalDenominador = 0;
				boolean existeAgenda = true;
				
				/*Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{*/
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
					
					String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
					String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						//totalNumerador += cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR) + this.obterNumeradorBG(aseguradora, mesAnoFOR);
						totalNumerador += this.obterNumeradorBG(aseguradora, mesAnoFOR);
						
						totalDenominador+= cConta0100.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					}
				//}
				/*else
					existeAgenda = false;*/
				
				if(totalDenominador == 0)
					total = -714;
				//else if(existeAgenda)
				else
					total = (totalNumerador / totalDenominador);
				/*else
					total = -714;*/
			//}
		//}
		
		return total;
	}
	
	public double obterMagemPonderadaPNAtivoTotal() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
				
				String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
				String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
				{
					//totalNumerador += cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR) + this.obterNumeradorBG(aseguradora, mesAnoFOR);
					totalNumerador += this.obterNumeradorBG(aseguradora, mesAnoFOR);
					
					totalDenominador+= cConta0100.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				}
			}
		}
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterProvisoesTecnicas(Entidade aseg) throws Exception
	{
		//INDICADORES DE 6 A 10 SÃO CALCULADOS SOMENTE O MES ATUAL, E NÃO OS ULTIMOS 12
		double total = 0;
		
		Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
		double totalNumerador = 0;
		double totalDenominador = 0;
		double totalDenominador1 = 0;
		double totalDenominador2 = 0;
				
		String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
		String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
		String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
		
		int mes = Integer.parseInt(mesStr);
		int ano  = Integer.parseInt(anoStr);
		
		if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
		{
			totalNumerador += cConta0107.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			
			totalDenominador1 += cConta0212.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			totalDenominador1 += cConta0213.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			
			totalDenominador2 += cConta010903.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			totalDenominador2 += cConta010904.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			totalDenominador2 += cConta010905.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			totalDenominador2 += cConta010906.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
			
			/*System.out.println("Ind aseguradora = " + aseguradora.obterNome());
			System.out.println("Ind cConta0107 = " + cConta0107.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("Ind cConta0212 = " + cConta0212.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("Ind cConta0213 = " + cConta0213.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("Ind cConta010903 = " + cConta010903.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("Ind cConta010904 = " + cConta010904.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("Ind cConta010905 = " + cConta010905.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("Ind cConta010906 = " + cConta010906.obterTotalizacaoExistente(aseguradora, mesAnoFOR));
			System.out.println("---------------------");*/
		}
				
		totalDenominador = totalDenominador1 - totalDenominador2;
				
		if(totalDenominador == 0)
			total=-714;
		else
			total = (totalNumerador / totalDenominador);
		
		return total;
	}
	
	public double obterMargemPonderadaProvisoesTecnicas() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
				String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
				String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				double totalDenominador1 = 0;
				double totalDenominador2 = 0;
				
				if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
				{
					totalNumerador += cConta0107.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					totalDenominador1 += cConta0212.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalDenominador1 += cConta0213.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					totalDenominador2 += cConta010903.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalDenominador2 += cConta010904.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalDenominador2 += cConta010905.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalDenominador2 += cConta010906.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				}
				
				totalDenominador += totalDenominador1 - totalDenominador2;
			}
		}
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterResultadoTecnicoSemPN(Entidade aseg) throws Exception
	{
		//INDICADORES DE 6 A 10 SÃO CALCULADOS SOMENTE O MES ATUAL, E NÃO OS ULTIMOS 12
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double primasNetas = 0;
				double sinistroNeto = 0;
				double totalUtilidade = 0;
				double totalIngressoTecnico = 0;
				double totalEgressoTecnico = 0;
				double totalUtilidadeNeta = 0;
				double totalDenominador = 0;
				boolean existeAgenda = true;
				
				/*Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{*/
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
					String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
					String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						// Calculo Primas Netas
						double c0401 = cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0402 =  cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0403 = cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0404 = cConta0404.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						double c0501 = cConta0501.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0502 = cConta0502.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0503 = cConta0503.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						primasNetas+=(c0401 + c0402 + c0403 + c0404) - (c0501 + c0502 + c0503);
						
						// Calculo Sinistro Neto 
						double c0506 = cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0507 = cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0508 = cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0509 = cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0511 = cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0513 = cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0515 = cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0505 = cConta0505.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						double c0407 = cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0408 = cConta0408.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0409 = cConta0409.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0412 = cConta0412.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0414 = cConta0414.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0406 = cConta0406.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						sinistroNeto+=(c0506 + c0507 + c0508 + c0509 + c0511 + c0513 + c0515 + c0505) - (c0407 + c0408 + c0409 + c0412 + c0414 + c0406);
						
						totalUtilidade = primasNetas - sinistroNeto;
						
						// Calculo Ingresso Técnico
						double c0405 = cConta0405.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0410 = cConta0410.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0411 = cConta0411.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0413 = cConta0413.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0415 = cConta0415.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0426 = cConta0426.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalIngressoTecnico = c0405 + c0410 + c0411 + c0413 + c0415 + c0426;
						
						// Calculo Egresso Técnico
						double c0504 = cConta0504.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0510 = cConta0510.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0512 = cConta0512.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0514 = cConta0514.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0516 = cConta0516.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0525 = cConta0525.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0525010401 = cConta0525010401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						double c0527 = cConta0527.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalEgressoTecnico = c0504 + c0510 + c0512 + c0514 + c0516 + c0525 - c0525010401 + c0527;
						
						totalUtilidadeNeta = (totalUtilidade + totalIngressoTecnico) - totalEgressoTecnico;
						
						totalNumerador = totalUtilidadeNeta;
						
						totalDenominador = cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					}
				//}
				//else
					//existeAgenda = false;
				
				if(totalDenominador == 0)
					total=-714;
				//else if(existeAgenda)
				else
					total = (totalNumerador / totalDenominador);
				/*else
					total=-714;*/
			//}
		//}
		
		return total;
		
	}
	
	public double obterMargemPonderadaResultadoTecnicoSemPN() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
				String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
				String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				double primasNetas = 0;
				double sinistroNeto = 0;
				double totalUtilidade = 0;
				double totalIngressoTecnico = 0;
				double totalEgressoTecnico = 0;
				double totalUtilidadeNeta = 0;
				
				if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
				{
				
					// Calculo Primas Netas
					double c0401 = cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0402 =  cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0403 = cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0404 = cConta0404.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					double c0501 = cConta0501.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0502 = cConta0502.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0503 = cConta0503.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					primasNetas+=(c0401 + c0402 + c0403 + c0404) - (c0501 + c0502 + c0503);
					
					// Calculo Sinistro Neto 
					double c0506 = cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0507 = cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0508 = cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0509 = cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0511 = cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0513 = cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0515 = cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0505 = cConta0505.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					double c0407 = cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0408 = cConta0408.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0409 = cConta0409.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0412 = cConta0412.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0414 = cConta0414.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0406 = cConta0406.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					sinistroNeto+=(c0506 + c0507 + c0508 + c0509 + c0511 + c0513 + c0515 + c0505) - (c0407 + c0408 + c0409 + c0412 + c0414 + c0406);
					
					totalUtilidade = primasNetas - sinistroNeto;
					
					// Calculo Ingresso Técnico
					double c0405 = cConta0405.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0410 = cConta0410.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0411 = cConta0411.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0413 = cConta0413.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0415 = cConta0415.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0426 = cConta0426.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					totalIngressoTecnico = c0405 + c0410 + c0411 + c0413 + c0415 + c0426;
					
					// Calculo Egresso Técnico
					double c0504 = cConta0504.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0510 = cConta0510.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0512 = cConta0512.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0514 = cConta0514.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0516 = cConta0516.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0525 = cConta0525.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0525010401 = cConta0525010401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					double c0527 = cConta0527.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					totalEgressoTecnico = c0504 + c0510 + c0512 + c0514 + c0516 + c0525 - c0525010401 + c0527;
					
					totalUtilidadeNeta = (totalUtilidade + totalIngressoTecnico) - totalEgressoTecnico;
					
					totalNumerador += totalUtilidadeNeta;
					
					totalDenominador += cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				}
			}
		}			
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
		
	}

	private double obterNumeradorBG(Entidade aseguradora, String mesAno)throws Exception
	{
		double totalPatrimonio = 0;
		double totalPassivo = 0;
		double totalAtivo = 0;
		double totalExercicio = 0;
		double totalPatrimonioNeto = 0;
		
		//Calculo Total Patrimonio
		double c0301 = cConta0301.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0302 = cConta0302.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0303 = cConta0303.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0304 = cConta0304.obterTotalizacaoExistente(aseguradora, mesAno);
		
		totalPatrimonio = c0301 + c0302 + c0303 + c0304;
		
		//Calculo Total Passivo
		double c0201 = cConta0201.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0202 = cConta0202.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0203 = cConta0203.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0204 = cConta0204.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0205 = cConta0205.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0206 = cConta0206.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0210 = cConta0210.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0211 = cConta0211.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0212 = cConta0212.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0213 = cConta0213.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0214 = cConta0214.obterTotalizacaoExistente(aseguradora, mesAno);
		
		totalPassivo = c0201 + c0202 + c0203 + c0204 + c0205 + c0206 + c0210 + c0211 + c0212 + c0213 + c0214;
		
		//Calculo Total Ativo
		double c0101 = cConta0101.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0102 = cConta0102.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0103 = cConta0103.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0104 = cConta0104.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0105 = cConta0105.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0106 = cConta0106.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0107 = cConta0107.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0108 = cConta0108.obterTotalizacaoExistente(aseguradora, mesAno);
		double c0109 = cConta0109.obterTotalizacaoExistente(aseguradora, mesAno);
		
		totalAtivo = c0101 + c0102 + c0103 + c0104 + c0105 + c0106 + c0107 + c0108 + c0109;
		
		totalExercicio = totalAtivo - (totalPassivo + totalPatrimonio);
		
		totalPatrimonioNeto = totalPatrimonio + totalExercicio;
		
		return totalPatrimonioNeto;
	}
	
	public double obterRetornoSemPN(Entidade aseg) throws Exception
	{
		//INDICADORES DE 6 A 10 SÃO CALCULADOS SOMENTE O MES ATUAL, E NÃO OS ULTIMOS 12
		
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double totalDenominador = 0;
				boolean existeAgenda = true;
				
				/*Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{*/
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
					
					String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
					String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						totalNumerador += cConta0400.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador -= cConta0500.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						//totalNumerador += this.obterNumeradorBG(aseguradora, mesAnoFOR) + cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					}
				//}
				//else
					//existeAgenda = false;
				
				if(totalDenominador == 0)
					total=-714;
				//else if(existeAgenda)
				else
					total = (totalNumerador / totalDenominador);
				/*else
					total=-714;*/
			//}
		//}
		
		return total;
	}
	
	public double obterMargemPonderadaRetornoSemPN() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(this.mesAno);
				
				String mesStr = new SimpleDateFormat("MM").format(this.mesAno);
				String anoStr = new SimpleDateFormat("yyyy").format(this.mesAno);
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
				{
					totalNumerador += cConta0400.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					totalNumerador -= cConta0500.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					//totalNumerador += this.obterNumeradorBG(aseguradora, mesAnoFOR) + cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					
					totalDenominador += cConta0300.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
				}
			}
		}
		
		if(totalDenominador!=0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}

	public double obterSinistrosBrutosPD(Entidade aseg) throws Exception
	{
		double total = 0;
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
		
		Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
		double totalNumerador = 0;
		double totalDenominador = 0;
				
		boolean existeAgenda = true;
				
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				String mesAnoFOR,mesStr,anoStr;
				int mes,ano;
				
				for(int j = 0 ; j < 12 ; j++)
				{
					mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					mesStr = new SimpleDateFormat("MM").format(c.getTime());
					anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					mes = Integer.parseInt(mesStr);
					ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						totalNumerador += cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalNumerador -= cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						/*System.out.println(mesAnoFOR +" " + cConta0506.obterApelido() + " " + formataValor.format(cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0507.obterApelido() + " " + formataValor.format(cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0508.obterApelido() + " " + formataValor.format(cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0509.obterApelido() + " " + formataValor.format(cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0511.obterApelido() + " " + formataValor.format(cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0513.obterApelido() + " " + formataValor.format(cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0515.obterApelido() + " " + formataValor.format(cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0407.obterApelido() + " " + formataValor.format(cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0401.obterApelido() + " " + formataValor.format(cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						 * 
						 * 
						System.out.println(mesAnoFOR +" " + cConta0402.obterApelido() + " " + formataValor.format(cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));
						System.out.println(mesAnoFOR +" " + cConta0403.obterApelido() + " " + formataValor.format(cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR)));*/
						
					}
					
					c.add(Calendar.MONTH, -1);
				}
				
				//System.out.println("totalNumerador " + totalNumerador);
				//System.out.println("totalDenominador " + totalDenominador);
				//System.out.println("totalNumerador / totalDenominador " + totalNumerador.divide(totalDenominador));
				
				if(totalDenominador == 0)
				{
					total = -714;
				}
				else
				{
					total = (totalNumerador / totalDenominador);
				}
				
				return total;
		
		//file.write("\r\n");
		
	}
	
	public double obterMagemPonderadaSinistrosBrutosPD() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				for(int j = 0 ; j < 12 ; j++)
				{
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					String mesStr = new SimpleDateFormat("MM").format(c.getTime());
					String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*totalNumerador += cConta0506.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0506.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0507.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0507.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0508.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0508.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0509.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0509.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0511.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0511.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0513.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0513.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0515.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0515.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador -= cConta0407.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0407.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador+= cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalDenominador+= cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalDenominador+= cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);*/
						
						totalNumerador += cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador += cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador -= cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador+= cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador+= cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador+= cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						c.add(Calendar.MONTH, -1);
					}
				}
			}
		}
		
		if(totalDenominador != 0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
		
		//file.write("\r\n");
		
	}

	public double obterSinistrosNetosPDNR(Entidade aseg) throws Exception
	{
		double total = 0;
		
		//for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
		//{
			Aseguradora aseguradora = (Aseguradora) this.aseguradoras.get(aseg.obterId());
			
			//if(aseg.obterId() == aseguradora.obterId())
			//{
				double totalNumerador = 0;
				double totalNumerador1 = 0;
				double totalNumerador2 = 0;
				double totalDenominador = 0;
				double totalDenominador1 = 0;
				double totalDenominador2 = 0;
				boolean existeAgenda = true;
				
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				String mesAnoFOR,mesStr,anoStr;
				int mes, ano;
				
				for(int j = 0 ; j < 12 ; j++)
				{
					mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					
					mesStr = new SimpleDateFormat("MM").format(c.getTime());
					anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					mes = Integer.parseInt(mesStr);
					ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						totalNumerador1 += cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalNumerador2 += cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador2 += cConta0408.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador2 += cConta0409.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador1 += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador1 += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador1 += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador2 += cConta0501.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador2 += cConta0502.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
					}
					
					c.add(Calendar.MONTH, -1);
				}
				
				totalNumerador = totalNumerador1 - totalNumerador2;
				totalDenominador = totalDenominador1 - totalDenominador2;
				
				if(totalDenominador == 0)
					total = -714;
				//else if(existeAgenda)
				else
				{
					total = (totalNumerador / totalDenominador);
				}
				/*else
					total = -714;*/
			//}
		//}
		
		return total;
	}
	
	public double obterMargemPonderadaSinistrosNetosPDNR() throws Exception
	{
		double total = 0;
		double totalNumerador = 0;
		double totalDenominador = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205 && aseguradora.obterId()!=5228 && aseguradora.obterId()!=5225)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(mesAno);
				
				double totalNumerador1 = 0;
				double totalNumerador2 = 0;
				double totalDenominador1 = 0;
				double totalDenominador2 = 0;
				
				for(int j = 0 ; j < 12 ; j++)
				{
					String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					
					String mesStr = new SimpleDateFormat("MM").format(c.getTime());
					String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
					
					int mes = Integer.parseInt(mesStr);
					int ano  = Integer.parseInt(anoStr);
					
					if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					{
						/*totalNumerador1 += cConta0506.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0506.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0507.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0507.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0508.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0508.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0509.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0509.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0511.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0511.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0513.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0513.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0515.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0515.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						
						totalNumerador2 += cConta0407.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0407.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalNumerador2 += cConta0408.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0408.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalNumerador2 += cConta0409.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0409.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador1 += cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalDenominador1 += cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						totalDenominador1 += cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR) - cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador2 += cConta0501.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0501.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
						totalDenominador2 += cConta0502.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR) - cConta0502.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);*/
						
						totalNumerador1 += cConta0506.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0507.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0508.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0509.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0511.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0513.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador1 += cConta0515.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalNumerador2 += cConta0407.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador2 += cConta0408.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalNumerador2 += cConta0409.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador1 += cConta0401.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador1 += cConta0402.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador1 += cConta0403.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						totalDenominador2 += cConta0501.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						totalDenominador2 += cConta0502.obterTotalizacaoExistente(aseguradora, mesAnoFOR);
						
						
						
						c.add(Calendar.MONTH, -1);
					}
				}
				
				totalNumerador += totalNumerador1 - totalNumerador2;
				totalDenominador += totalDenominador1 - totalDenominador2;
			}
		}
		
		if(totalDenominador != 0)
			total = (totalNumerador / totalDenominador);
		//else
			//total = -714;
		
		return total;
	}
	
	private void gerarLog() throws Exception
	{
		fileLog = new FileWriter("C:/tmp/LogIndicadores.txt");
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			Calendar c = Calendar.getInstance();
			c.setTime(mesAno);
			
			//c.add(Calendar.MONTH, -1);
			
			for(int j = 0 ; j < 12 ; j++)
			{
				String mesAnoFOR = new SimpleDateFormat("MM/yyyy").format(c.getTime());
				
				String mesStr = new SimpleDateFormat("MM").format(c.getTime());
				String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				fileLog.write(aseguradora.obterNome() + " MES_ANO: " + mesAnoFOR +  "\r\n");
				fileLog.write("\r\n");
				
				fileLog.write(cConta0401.obterApelido() +" : Debito = "+ formataValor.format(cConta0401.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0401.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0402.obterApelido() +" : Debito = "+ formataValor.format(cConta0402.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0402.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0403.obterApelido() +" : Debito = "+ formataValor.format(cConta0403.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0403.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0407.obterApelido() +" : Debito = "+ formataValor.format(cConta0407.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0407.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0408.obterApelido() +" : Debito = "+ formataValor.format(cConta0408.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0408.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0409.obterApelido() +" : Debito = "+ formataValor.format(cConta0409.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0409.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				
				fileLog.write(cConta0501.obterApelido() +" : Debito = "+ formataValor.format(cConta0501.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0501.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0502.obterApelido() +" : Debito = "+ formataValor.format(cConta0502.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0502.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0504.obterApelido() +" : Debito = "+ formataValor.format(cConta0504.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito = " + formataValor.format(cConta0504.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0506.obterApelido() +" : Debito = "+ formataValor.format(cConta0506.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito = " + formataValor.format(cConta0506.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0507.obterApelido() +" : Debito = "+ formataValor.format(cConta0507.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0507.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0508.obterApelido() +" : Debito = "+ formataValor.format(cConta0508.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0508.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0509.obterApelido() +" : Debito = "+ formataValor.format(cConta0509.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0509.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0511.obterApelido() +" : Debito = "+ formataValor.format(cConta0511.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0511.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0513.obterApelido() +" : Debito = "+ formataValor.format(cConta0513.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0513.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0515.obterApelido() +" : Debito = "+ formataValor.format(cConta0515.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito =  " + formataValor.format(cConta0515.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write(cConta0525.obterApelido() +" : Debito = "+ formataValor.format(cConta0525.obterTotalizacaoDebitoExistente(aseguradora, mesAnoFOR)) + " Credito = " + formataValor.format(cConta0525.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR))  + "\r\n");
				fileLog.write("\r\n");
				
				c.add(Calendar.MONTH, -1);
			}
		}
	}
	
	private void montarArquivo() throws Exception
	{
		ArredondarImpl arredonda = new ArredondarImpl();
		
		String mesAnoStr = new SimpleDateFormat("MM/yyyy").format(mesAno);
		
		//String mesAnoStr2 = new SimpleDateFormat("dd/MM/yyyy").format(mesAno);
		
		String[] s = mesAnoStr.split("/");
		
		String mesAnoM = s[0] + s[1] + ".txt";
		
		nomeArquivo = "C:/tmp/Ind_Tecnicos_" + this.usuario.obterChave() +"_"+ mesAnoM;
		//nomeArquivo = "D:/Projetos/BCP_infra_nova/images/Ind_Tecnicos_" + this.usuario.obterChave() +"_"+ mesAnoM;
		
		file = new FileWriter(nomeArquivo);
		
		//file.write(";INDICADORES TÉCNICOS AL: "+mesAnoStr2+ "\r\n");
		//file.write(";(RES. SS.SG. N° 011/2010)" + "\r\n");
		
		file.write("La presente publicación se realiza en cumplimiento de la Resolución SS.SG.N° 011/10 del 9 de febrero de 2010 que dispone la publicación bimestral de los indicadores financieros" + "\r\n");
		file.write("en virtud de la Ley Nº 3899/09 Que Regula a las Sociedades Calificadoras de Riesgos, Deroga la Ley Nº 1056/97 y Modifica el Artículo 106 de la Ley Nº 861/96" + "\r\n");
		file.write("General de Bancos, Financieras y Otras Entidades de Crédito y el inciso d) del Artículo 61 de la Ley Nº 827/96 De Seguros. Los Indicadores Financieros señalados se basan en" + "\r\n");
		file.write("los datos proveídos a la Superintendencia de Seguros a través de la Central de Información." + "\r\n");
		file.write("ATENCIÓN: Los Indicadores Tècnicos de la Situación Financiera de las Empresas de Seguros  no son sustitutos de la calificación de las Aseguradoras, lo cual estará a cargo" + "\r\n");
		file.write("de las entidades especializadas habilitadas por la Comisión Nacional de Valores." + "\r\n");
		
		file.write("\r\n");
		file.write("\r\n");
		  		
		file.write(";ENTIDAD ASEGURADORA;INDICES TÉCNICOS;;;;;;;INDICES PATRIMONIALES  Y DE RENTABILIDAD;;" + "\r\n");
		file.write(";;1;2;3;4;5;6;7;8;9;10" + "\r\n");
		
		int cont = 1;
		
		for(Iterator i = aseguradorHome.obterAseguradorasPorMenor80OrdenadoPorNome().iterator() ; i.hasNext() ; )
		{
			Entidade aseguradora = (Entidade) i.next();
			
			if(aseguradora.obterId()==5205)
			{
				file.write(cont + ";" + aseguradora.obterNome() + " (*);" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0 + "\r\n");
				cont++;
			}
			else
			{
				double valor1 = this.obterSinistrosBrutosPD(aseguradora);
				String valor1Str = "";
				if(valor1!=-714)
				{
					String teste = formataValor.format(valor1);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor1Str = new Integer(i2).toString();
					//valor1Str = formataValor.format(valor1);
				}
				else
					valor1Str = "*";
				
				double valor2 = this.obterSinistrosNetosPDNR(aseguradora);
				String valor2Str = "";
				if(valor2!=-714)
				{
					String teste = formataValor.format(valor2);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor2Str = new Integer(i2).toString();
					//valor2Str = formataValor.format(valor2);
				}				
				else
					valor2Str = "*";
				
				double valor3 = this.obterGastosOperativosPD(aseguradora);
				String valor3Str = "";
				if(valor3!=-714)
				{
					String teste = formataValor.format(valor3);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor3Str = new Integer(i2).toString();
					//valor3Str = formataValor.format(valor3);
				}
				else
					valor3Str = "*";
				
				double valor4 = this.obterGastosDeProducaoPNA(aseguradora);
				String valor4Str = "";
				if(valor4!=-714)
				{
					String teste = formataValor.format(valor4);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor4Str = new Integer(i2).toString();
					//valor4Str = formataValor.format(valor4);
				}
				else
					valor4Str = "*";
				
				double valor5 = this.obterGastosDeExportacaoPNA(aseguradora);
				String valor5Str = "";
				if(valor5!=-714)
				{
					String teste = formataValor.format(valor5);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor5Str = new Integer(i2).toString();
					//valor5Str = formataValor.format(valor5);
				}
				else
					valor5Str = "*";
				
				double valor6 = this.obterProvisoesTecnicas(aseguradora);
				String valor6Str = "";
				if(valor6!=-714)
				{
					String teste = formataValor.format(valor6);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor6Str = new Integer(i2).toString();
					//valor6Str = formataValor.format(valor6);
				}
				else
					valor6Str = "*";
				
				double valor7 = this.obterPNAtivoTotal(aseguradora);
				String valor7Str = "";
				if(valor7!=-714)
				{
					String teste = formataValor.format(valor7);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					
					d = arredonda.retornaValorArredondadoPraCima(d);
					
					int i2 = new Double(d).intValue();
					valor7Str = new Integer(i2).toString();
				}
				else
					valor7Str = "*";
				
				double valor8 = this.obterRetornoSemPN(aseguradora);
				String valor8Str = "";
				if(valor8!=-714)
				{
					String teste = formataValor.format(valor8);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor8Str = new Integer(i2).toString();
					//valor8Str = formataValor.format(valor8);
				}
				else
					valor8Str = "*";
				
				double valor9 = this.obterResultadoTecnicoSemPN(aseguradora);
				String valor9Str = "";
				if(valor9!=-714)
				{
					String teste = formataValor.format(valor9);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor9Str = new Integer(i2).toString();
					//valor9Str = formataValor.format(valor9);
				}
				else
					valor9Str = "*";
				
				double valor10 = this.obterMagemDeGanancia(aseguradora);
				String valor10Str = "";
				if(valor10!=-714)
				{
					String teste = formataValor.format(valor10);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					d = arredonda.retornaValorArredondadoPraCima(d);
					int i2 = new Double(d).intValue();
					valor10Str = new Integer(i2).toString();
					//valor10Str = formataValor.format(valor10);
				}
				else
					valor10Str = "*";
				
				file.write(cont + ";" + aseguradora.obterNome() + ";" + valor1Str + ";" + valor2Str + ";" + valor3Str + ";" + valor4Str + ";" + valor5Str + ";" + valor6Str + ";" + valor7Str + ";" + valor8Str + ";" + valor9Str + ";" + valor10Str + "\r\n");
				
				cont++;
			}
		}
		
		double valor11 = this.obterMagemPonderadaSinistrosBrutosPD();
		String valor11Str = "";
		if(valor11!=-714)
		{
			String teste = formataValor.format(valor11);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor11Str = new Integer(i2).toString();
			//valor1Str = formataValor.format(valor1);
		}
		else
			valor11Str = "*";
		
		double valor12 = this.obterMargemPonderadaSinistrosNetosPDNR();
		String valor12Str = "";
		if(valor12!=-714)
		{
			String teste = formataValor.format(valor12);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor12Str = new Integer(i2).toString();
			//valor2Str = formataValor.format(valor2);
		}				
		else
			valor12Str = "*";
		
		double valor13 = this.obterMargemPonderadaGastosOperativosPD();
		String valor13Str = "";
		if(valor13!=-714)
		{
			String teste = formataValor.format(valor13);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor13Str = new Integer(i2).toString();
			//valor3Str = formataValor.format(valor3);
		}
		else
			valor13Str = "*";
		
		double valor14 = this.obterMargemPonderadaGastosDeProducaoPNA();
		String valor14Str = "";
		if(valor14!=-714)
		{
			String teste = formataValor.format(valor14);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor14Str = new Integer(i2).toString();
			//valor4Str = formataValor.format(valor4);
		}
		else
			valor14Str = "*";
		
		double valor15 = this.obterMargemPonderadaGastosDeExportacaoPNA();
		String valor15Str = "";
		if(valor15!=-714)
		{
			String teste = formataValor.format(valor15);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor15Str = new Integer(i2).toString();
			//valor5Str = formataValor.format(valor5);
		}
		else
			valor15Str = "*";
		
		double valor16 = this.obterMargemPonderadaProvisoesTecnicas();
		String valor16Str = "";
		if(valor16!=-714)
		{
			String teste = formataValor.format(valor16);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor16Str = new Integer(i2).toString();
			//valor6Str = formataValor.format(valor6);
		}
		else
			valor16Str = "*";
		
		double valor17 = this.obterMagemPonderadaPNAtivoTotal();
		String valor17Str = "";
		if(valor17!=-714)
		{
			String teste = formataValor.format(valor17);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor17Str = new Integer(i2).toString();
			//valor7Str = formataValor.format(valor7);
		}
		else
			valor17Str = "*";
		
		double valor18 = this.obterMargemPonderadaRetornoSemPN();
		String valor18Str = "";
		if(valor18!=-714)
		{
			String teste = formataValor.format(valor18);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor18Str = new Integer(i2).toString();
			//valor8Str = formataValor.format(valor8);
		}
		else
			valor18Str = "*";
		
		double valor19 = this.obterMargemPonderadaResultadoTecnicoSemPN();
		String valor19Str = "";
		if(valor19!=-714)
		{
			String teste = formataValor.format(valor19);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor19Str = new Integer(i2).toString();
			//valor9Str = formataValor.format(valor9);
		}
		else
			valor19Str = "*";
		
		double valor20 = this.obterMargemPonderadaMagemDeGanancia();
		String valor20Str = "";
		if(valor20!=-714)
		{
			String teste = formataValor.format(valor20);
			teste = teste.replace(',', '.');
			double d = Double.parseDouble(teste) * 100;
			d = arredonda.retornaValorArredondadoPraCima(d);
			int i2 = new Double(d).intValue();
			valor20Str = new Integer(i2).toString();
			//valor10Str = formataValor.format(valor10);
		}
		else
			valor20Str = "*";
		
		file.write(";Margem Ponderada do Mercado;" + valor11Str + ";" + valor12Str + ";" + valor13Str + ";" + valor14Str + ";" + valor15Str + ";" + valor16Str + ";" + valor17Str + ";" + valor18Str + ";" + valor19Str + ";" + valor20Str + "\r\n");
		
		
		
		file.write("\r\n");
		
		//file.write(";INDICES TECNICOS" + "\r\n");
		file.write("1 - SINIESTRALIDAD BRUTA: Porción de prima ganada consumida por siniestros (%)." + "\r\n");
		file.write("2 - SINIESTRALIDAD NETA: Porción de prima ganada por riesgo no cedido, que fue consumida por siniestros de su retención (%)." + "\r\n");
		file.write("3 - INDICE DE GASTO OPERATIVO: Porción de primas ganadas insumidas por el total de gastos operativos (%)." + "\r\n");
		file.write("4 - INDICE DE  GASTO DE PRODUCCIÓN: Porción de primas ganadas insumidas por el gasto de producción (%)." + "\r\n");
		file.write("5 - INDICE DE GASTO DE EXPLOTACIÓN: Porción de primas ganadas insumidas por el gasto de explotación (%)." + "\r\n");
		file.write("6 - REPRESENTATIVIDAD DE LAS INVERSIONES: Porción de las inversiones que representan a las provisiones técnicas (%)." + "\r\n");
		file.write("7 - INDICE DE REPRESENTATIVIDAD DEL ACTIVO: Porción del Activo que representa al Patrimonio Neto (%)" + "\r\n");
		
		//file.write("\r\n");
		
		//file.write(";INDICES PATRIMONIALES Y DE RENTABILIDAD" + "\r\n");
		file.write("8 - INDICE GENERAL DE RENDIMIENTO: Relación entre el resultado del ejercicio y el volúmen del Patrimonio Neto (%)." + "\r\n");
		file.write("9 - INDICE TÉCNICO DE RENDIMIENTO: Relación entre el resultado técnico y el volúmen del Patrimonio Neto (%)." + "\r\n");
		file.write("10 - INDICE RENDIMIENTO S/VOLÚMEN OPERACIONES TÉCNICAS: Relación entre el Resultado del Ejercicio y el volúmen del primaje (%)." + "\r\n");
		
		file.write("\r\n");
		
		//file.write(";* - La Aseguradora no tiene datos suficientes para realizar el calculo" + "\r\n");
		file.write("(*) Suspendida para emitir pòlizas como medida cautelar, por déficit de su Fondo de Garantía (Res. SS.SG. Nº 045/06 de fecha 20 de enero de 2006)." + "\r\n");
		
	}
	
	public double obterPrimasSegurosGEE(Date data) throws Exception
	{
		double total = 0;
		
		for(Iterator i = aseguradoras.values().iterator() ; i.hasNext() ; )
		{
			Aseguradora aseguradora = (Aseguradora) i.next();
			
			if(aseguradora.obterId()!=5205)
			{
				String mesAnoFOR = new SimpleDateFormat("MMyyyy").format(data);
				System.out.println(mesAnoFOR);
				
				String mesStr = new SimpleDateFormat("MM").format(data);
				String anoStr = new SimpleDateFormat("yyyy").format(data);
				
				int mes = Integer.parseInt(mesStr);
				int ano  = Integer.parseInt(anoStr);
				
				if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					total+=this.c0401010000.obterTotalizacaoCreditoExistente(aseguradora, mesAnoFOR);
			}
		}
		
		return total;
	}
}
