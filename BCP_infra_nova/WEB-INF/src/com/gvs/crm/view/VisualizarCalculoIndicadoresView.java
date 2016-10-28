package com.gvs.crm.view;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.Border;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.IndicadoresHome;
import com.gvs.crm.model.Inscricao;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.impl.ArredondarImpl;

import infra.control.Action;
import infra.security.User;
import infra.view.BasicView;
import infra.view.Block;
import infra.view.Button;
import infra.view.Label;
import infra.view.Link;
import infra.view.Table;
import infra.view.View;

public class VisualizarCalculoIndicadoresView extends BasicView
{

	private Date dataTecnico;
	private DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	private boolean mostraTela;
	private boolean geraArquivo;
	private Usuario usuario;
	
	public VisualizarCalculoIndicadoresView(Usuario usuario, Date dataTecnico, boolean mostraTela, boolean geraArquivo) throws Exception
	{
		this.usuario = usuario;
		this.dataTecnico = dataTecnico;
		this.mostraTela = mostraTela;
		this.geraArquivo = geraArquivo;
	}
	
	public View execute(User user, Locale locale, Properties properties) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(user);
		IndicadoresHome indicadorHome = (IndicadoresHome) mm.getHome("IndicadoresHome");
		indicadorHome.calcularIndicadoresTecnicos(this.dataTecnico, null, false);
		AseguradoraHome aseguradorHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		ArredondarImpl arredonda = new ArredondarImpl();
		
		Table table = new Table(12);
		table.setWidth("100%");
		table.addStyle(Table.STYLE_ALTERNATE);
		
		table.addSubtitle("Calculo Indicadores " + new SimpleDateFormat("dd/MM/yyyy").format(this.dataTecnico));
		
		if(this.geraArquivo)
		{
			if(this.dataTecnico!=null)
			{
				String mesAnoStr = new SimpleDateFormat("MM/yyyy").format(dataTecnico);
				
				String[] s = mesAnoStr.split("/");
				
				String mesAnoM = s[0] + s[1] + ".txt";
				
				String nomeArquivo = "C:/tmp/Ind_Tecnicos_" + this.usuario.obterChave() +"_"+ mesAnoM;
				File file = new File(nomeArquivo);
				
				if(file.exists())
				{
					table.addSubtitle("Archivo Generado");
					
					Block block4 = new Block(Block.HORIZONTAL);
					Link link = new Link(file.getName(),new Action("downloadArquivo"));
					link.getAction().add("arquivoDownload", file.getName());
					block4.add(link);
					
					table.setNextColSpan(table.getColumns());
					table.add(block4);
					
					table.addSubtitle("");
				}
			}
		}
		
		table.setNextColSpan(table.getColumns());
		table.setNextStyle(Table.STYLE_BLANK);
		table.addHeader("La presente publicación se realiza en cumplimiento de la Resolución SS.SG.N° 011/10 del 9 de febrero de 2010 que dispone la publicación bimestral de los indicadores financieros en virtud de la Ley Nº 3899/09 Que Regula a las Sociedades Calificadoras de Riesgos,");
		table.setNextColSpan(table.getColumns());
		table.setNextStyle(Table.STYLE_BLANK);
		table.addHeader("Deroga la Ley Nº 1056/97 y Modifica el Artículo 106 de la Ley Nº 861/96 General de Bancos, Financieras y Otras Entidades de Crédito y el inciso d) del Artículo 61 de la Ley Nº 827/96 De Seguros. Los Indicadores Financieros señalados se basan en");
		table.setNextColSpan(table.getColumns());
		table.setNextStyle(Table.STYLE_BLANK);
		table.addHeader("los datos proveídos a la Superintendencia de Seguros a través de la Central de Información. Los mismos  no constituyen  indicadores de solvencia, que conforme a la Ley citada la elaboración ha quedado reservada  a  las calificadoras");
		table.setNextColSpan(table.getColumns());
		table.setNextStyle(Table.STYLE_BLANK);
		table.addHeader("habilitadas por la Comisión Nacional de Valores. ");
		
		table.addSubtitle("");
		
		table.add("");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("ENTIDAD ASEGURADORA");
		table.setNextColSpan(10);
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("INDICES FINANCIEROS (%)");
		
		table.add("");
		table.addHeader("a) Autorizadas a operar en los Ramos Elementales y Vida");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("1");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("2");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("3");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("4");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("5");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("6");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("7");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("8");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("9");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("10");
		
		int cont = 1;
		
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		Collection<Entidade> aseguradorasVida = new ArrayList<Entidade>();
		Aseguradora real = null; 
		
		aseguradoras = aseguradorHome.obterAseguradorasPorMenor80OrdenadoPorNome();
		
		for(Aseguradora aseg : aseguradoras)
		{
			if(aseg.obterId() == 5228)
				real = aseg;
			
			for(Inscricao inscricao :aseg.obterInscricoes())
			{
				if(inscricao.obterRamo().equals("PATRIMONIALES Y VIDA"))
					aseguradorasVida.add(aseg);
			}
		}
		
		aseguradoras.removeAll(aseguradorasVida);
		aseguradoras.remove(real);
		
		for(Entidade aseguradora : aseguradorasVida)
		{
			table.add(new Label(cont));
			
			table.add(aseguradora.obterNome());
			
			double valor1 = indicadorHome.obterSinistrosBrutosPD(aseguradora);
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
				valor1Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor1Str);
			
			double valor2 = indicadorHome.obterSinistrosNetosPDNR(aseguradora);
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
				valor2Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor2Str);
			
			double valor3 = indicadorHome.obterGastosOperativosPD(aseguradora);
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
				valor3Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor3Str);
			
			double valor4 = indicadorHome.obterGastosDeProducaoPNA(aseguradora);
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
				valor4Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor4Str);
			
			double valor5 = indicadorHome.obterGastosDeExportacaoPNA(aseguradora);
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
				valor5Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor5Str);
			
			double valor6 = indicadorHome.obterProvisoesTecnicas(aseguradora);
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
				valor6Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor6Str);
			
			double valor7 = indicadorHome.obterPNAtivoTotal(aseguradora);
			String valor7Str = "";
			if(valor7!=-714)
			{
				String teste = formataValor.format(valor7);
				
				teste = teste.replace(',', '.');
				double d = Double.parseDouble(teste) * 100;
				
				d = arredonda.retornaValorArredondadoPraCima(d);
				
				int i2 = new Double(d).intValue();
				valor7Str = new Integer(i2).toString();
				//valor7Str = formataValor.format(valor7);
			}
			else
				valor7Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor7Str);
			
			double valor8 = indicadorHome.obterRetornoSemPN(aseguradora);
			String valor8Str = "";
			if(valor8!=-714)
			{
				String teste = formataValor.format(valor8);
				teste = teste.replace(',', '.');
				double d = Double.parseDouble(teste) * 100;
				
				d = arredonda.retornaValorArredondadoPraCima(d);
				
				int i2 = new Double(d).intValue();
				valor8Str = new Integer(i2).toString();
				
			}
			else
				valor8Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor8Str);
			
			double valor9 = indicadorHome.obterResultadoTecnicoSemPN(aseguradora);
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
				valor9Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor9Str);
			
			double valor10 = indicadorHome.obterMagemDeGanancia(aseguradora);
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
				valor10Str = "0";
			
			table.setNextHAlign(Table.HALIGN_RIGHT);
			table.add(valor10Str);
			
			cont++;
		}
		
		table.add("");
		table.addHeader("b) Autorizadas a operar en los Ramos Elementales o Patrimoniales");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("1");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("2");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("3");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("4");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("5");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("6");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("7");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("8");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("9");
		table.setNextHAlign(Table.HALIGN_CENTER);
		table.addHeader("10");
		
		cont = 1;
		
		for(Entidade aseguradora : aseguradoras)
		{
			table.add(new Label(cont));
			
			/*if(aseguradora.obterId() == 5205)
			{
				table.add(aseguradora.obterNome() + " (*)");
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add("0");
				
				cont++;
			}
			else
			{*/
				table.add(aseguradora.obterNome());
				
				double valor1 = indicadorHome.obterSinistrosBrutosPD(aseguradora);
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
					valor1Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor1Str);
				
				double valor2 = indicadorHome.obterSinistrosNetosPDNR(aseguradora);
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
					valor2Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor2Str);
				
				double valor3 = indicadorHome.obterGastosOperativosPD(aseguradora);
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
					valor3Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor3Str);
				
				double valor4 = indicadorHome.obterGastosDeProducaoPNA(aseguradora);
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
					valor4Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor4Str);
				
				double valor5 = indicadorHome.obterGastosDeExportacaoPNA(aseguradora);
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
					valor5Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor5Str);
				
				double valor6 = indicadorHome.obterProvisoesTecnicas(aseguradora);
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
					valor6Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor6Str);
				
				double valor7 = indicadorHome.obterPNAtivoTotal(aseguradora);
				String valor7Str = "";
				if(valor7!=-714)
				{
					String teste = formataValor.format(valor7);
					
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					
					d = arredonda.retornaValorArredondadoPraCima(d);
					
					int i2 = new Double(d).intValue();
					valor7Str = new Integer(i2).toString();
					//valor7Str = formataValor.format(valor7);
				}
				else
					valor7Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor7Str);
				
				double valor8 = indicadorHome.obterRetornoSemPN(aseguradora);
				String valor8Str = "";
				if(valor8!=-714)
				{
					String teste = formataValor.format(valor8);
					teste = teste.replace(',', '.');
					double d = Double.parseDouble(teste) * 100;
					
					d = arredonda.retornaValorArredondadoPraCima(d);
					
					int i2 = new Double(d).intValue();
					valor8Str = new Integer(i2).toString();
					
				}
				else
					valor8Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor8Str);
				
				double valor9 = indicadorHome.obterResultadoTecnicoSemPN(aseguradora);
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
					valor9Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor9Str);
				
				double valor10 = indicadorHome.obterMagemDeGanancia(aseguradora);
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
					valor10Str = "0";
				
				table.setNextHAlign(Table.HALIGN_RIGHT);
				table.add(valor10Str);
				
				cont++;
			//}
		}
			
		table.add("");
		table.addHeader("Promedio Ponderado del Mercado");
		
		double valor11 = indicadorHome.obterMagemPonderadaSinistrosBrutosPD();
		String valor11Str = "";
		if(valor11!=0)
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
			valor11Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor11Str);
		
		double valor12 = indicadorHome.obterMargemPonderadaSinistrosNetosPDNR();
		String valor12Str = "";
		if(valor12!=0)
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
			valor12Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor12Str);
		
		double valor13 = indicadorHome.obterMargemPonderadaGastosOperativosPD();
		String valor13Str = "";
		if(valor13!=0)
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
			valor13Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor13Str);
		
		double valor14 = indicadorHome.obterMargemPonderadaGastosDeProducaoPNA();
		String valor14Str = "";
		if(valor14!=0)
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
			valor14Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor14Str);
		
		double valor15 = indicadorHome.obterMargemPonderadaGastosDeExportacaoPNA();
		String valor15Str = "";
		if(valor15!=0)
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
			valor15Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor15Str);
		
		double valor16 = indicadorHome.obterMargemPonderadaProvisoesTecnicas();
		String valor16Str = "";
		if(valor16!=0)
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
			valor16Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor16Str);
		
		double valor17 = indicadorHome.obterMagemPonderadaPNAtivoTotal();
		String valor17Str = "";
		if(valor17!=0)
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
			valor17Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor17Str);
		
		double valor18 = indicadorHome.obterMargemPonderadaRetornoSemPN();
		String valor18Str = "";
		if(valor18!=0)
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
			valor18Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor18Str);
		
		double valor19 = indicadorHome.obterMargemPonderadaResultadoTecnicoSemPN();
		String valor19Str = "";
		if(valor19!=0)
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
			valor19Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor19Str);
		
		double valor20 = indicadorHome.obterMargemPonderadaMagemDeGanancia();
		String valor20Str = "";
		if(valor20!=0)
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
			valor20Str = "0";
		
		table.setNextHAlign(Table.HALIGN_RIGHT);
		table.addHeader(valor20Str);
		
		
		table.addSubtitle("");
		
		Table table2 = new Table(1);
		//table2.addHeader("INDICES TÉCNICOS");
		//table2.addHeader("INDICES PATRIMONIALES Y DE RENTABILIDAD");
		table2.add("1 - SINIESTRALIDAD BRUTA: Porción de prima ganada consumida por siniestros (%).");
		table2.add("2 - SINIESTRALIDAD NETA: Porción de prima ganada por riesgo no cedido, que fue consumida por siniestros de su retención (%).");
		table2.add("3 - INDICE DE GASTO OPERATIVO: Porción de primas ganadas insumidas por el total de gastos operativos (%).");
		table2.add("4 - INDICE DE  GASTO DE PRODUCCIÓN: Porción de primas ganadas insumidas por el gasto de producción (%).");
		table2.add("5 - INDICE DE GASTO DE EXPLOTACIÓN: Porción de primas ganadas insumidas por el gasto de explotación (%).");
		table2.add("6 - REPRESENTATIVIDAD DE LAS INVERSIONES: Porción de las inversiones que representan a las provisiones técnicas (%).");
		table2.add("7 - INDICE DE REPRESENTATIVIDAD DEL ACTIVO: Porción del Activo que representa al Patrimonio Neto (%)");
		table2.add("8 - INDICE GENERAL DE RENDIMIENTO: Relación entre el resultado del ejercicio y el volúmen del Patrimonio Neto (%).");
		table2.add("9 - INDICE TÉCNICO DE RENDIMIENTO: Relación entre el resultado técnico y el volúmen del Patrimonio Neto (%).");
		table2.add("10 - INDICE RENDIMIENTO S/VOLÚMEN OPERACIONES TÉCNICAS: Relación entre el Resultado del Ejercicio y el volúmen del primaje (%).");
		/*table2.add("");
		table2.add("(*) Suspendida para emitir pòlizas como medida cautelar, por déficit de su Fondo de Garantía (Res. SS.SG. Nº 045/06 de fecha 20 de enero de 2006).");*/
		
		table.setNextColSpan(table.getColumns());
		table.add(table2);
		
		Button voltarButton = new Button("Volver",new Action("visualizarIndicadores"));
		voltarButton.getAction().add("tela", this.mostraTela);
		voltarButton.getAction().add("arquivo", this.geraArquivo);
		voltarButton.getAction().add("dataTecnicos", this.dataTecnico);
		
		table.addFooter(voltarButton);
		
		return new Border(table);
	}

}
