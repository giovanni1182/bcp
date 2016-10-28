package com.gvs.crm.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.IndicadoresHome;
import com.gvs.crm.model.Inscricao;

import infra.view.report.Border;
import infra.view.report.Font;
import infra.view.report.Image;
import infra.view.report.Region;
import infra.view.report.Table;
import infra.view.report.TableRow;
import infra.view.report.Text;

public class IndicadoresReport extends A4Report
{
	public IndicadoresReport(Collection<Aseguradora> aseguradoras, IndicadoresHome indicadorHome, Date data)  throws Exception
	{
		//DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		//ArredondarImpl arredonda = new ArredondarImpl();
		
		Table tablePrincipal = new Table();
		tablePrincipal.addColumnWidth("21cm");
		
		Collection<Aseguradora> aseguradorasVida = new ArrayList<>();
		Aseguradora real = null; 
		Aseguradora imperio = null;
		
		for(Aseguradora aseg : aseguradoras)
		{
			if(aseg.obterId() == 5228)//Seguradora Real
				real = aseg;
			else if(aseg.obterId() == 5225)//imperio
				imperio = aseg;
			
			for(Inscricao inscricao : aseg.obterInscricoes())
			{
				if(inscricao.obterRamo().equals("PATRIMONIALES Y VIDA"))
					aseguradorasVida.add(aseg);
			}
		}
		
		aseguradoras.removeAll(aseguradorasVida);
		aseguradoras.remove(real);
		aseguradoras.remove(imperio);
		
		// INICIO DO CABEÇALHO
		Table tableCab = new Table();
		tableCab.setFont(new Font("sans-serif", "11pt", null, "bold"));
		tableCab.addColumnWidth("10cm");
		tableCab.addColumnWidth("10cm");
		
		TableRow espaco = new TableRow();
		espaco.addCell(new Text("", 2));
		
		Table tableCab1 = new Table();
		tableCab1.addColumnWidth("5cm");
		
		Table tableCab2 = new Table();
		tableCab2.addColumnWidth("10cm");
		
		TableRow rowCab1 = new TableRow();
		rowCab1.setTextAlign("center");
		rowCab1.addCell(new Image("bcp.jpg"));
		
		tableCab1.addBodyRow(rowCab1);
		
		TableRow rowCab2 = new TableRow();
		rowCab2.setTextAlign("center");
		rowCab2.addCell(new Text("SUPERINTENDENCIA  DE  SEGUROS"));
		
		TableRow rowCab3 = new TableRow();
		rowCab3.setTextAlign("center");
		rowCab3.addCell(new Text("INDICADORES FINANCIEROS"));
		
		TableRow rowCab4 = new TableRow();
		rowCab4.setTextAlign("center");
		rowCab4.addCell(new Text("DE LAS EMPRESAS DE SEGUROS AL:"));
		
		TableRow rowCab5 = new TableRow();
		rowCab5.setTextAlign("center");
		rowCab5.addCell(new Text(new SimpleDateFormat("dd/MM/yyyy").format(data)));
		
		tableCab2.addBodyRow(rowCab2);
		tableCab2.addBodyRow(espaco);
		tableCab2.addBodyRow(rowCab3);
		tableCab2.addBodyRow(espaco);
		tableCab2.addBodyRow(rowCab4);
		tableCab2.addBodyRow(espaco);
		tableCab2.addBodyRow(rowCab5);
		
		TableRow rowAux = new TableRow();
		rowAux.addCell(tableCab1);
		rowAux.addCell(tableCab2);
		
		tableCab.addBodyRow(rowAux);
		
		TableRow rowAux2 = new TableRow();
		rowAux2.addCell(tableCab);
		
		tablePrincipal.addBodyRow(rowAux2);
		// FIM DO CABEÇALHO
		

		//INICIO DO TEXTO
		Table tableTexto = new Table();
		tableTexto.setFont(new Font("sans-serif", "10pt", null, "bold"));
		tableTexto.addColumnWidth("19cm");
		
		TableRow texto = new TableRow();
		texto.setTextAlign("center");
		texto.addCell(new Text("La presente publicación se realiza en cumplimiento de la Resolución SS.SG.N° 011/10 del 9 de"));
		
		tablePrincipal.addBodyRow(espaco);
		tablePrincipal.addBodyRow(espaco);
		tableTexto.addBodyRow(texto);		
		
		TableRow texto2 = new TableRow();
		texto2.addCell(new Text("febrero de 2010 que dispone la publicación bimestral de los indicadores financieros en virtud de la Ley"));
		
		tableTexto.addBodyRow(texto2);
		
		TableRow texto3 = new TableRow();
		texto3.addCell(new Text("Nº 3899/09 Que Regula a las Sociedades Calificadoras de Riesgos, Deroga la Ley Nº 1056/97 y Modifica"));
		
		tableTexto.addBodyRow(texto3);
		
		TableRow texto4 = new TableRow();
		texto4.addCell(new Text("el Artículo 106 de la Ley Nº 861/96 General de Bancos, Financieras y Otras Entidades de Crédito y el"));
		
		tableTexto.addBodyRow(texto4);
		
		TableRow texto5 = new TableRow();
		texto5.addCell(new Text("inciso d) del Artículo 61 de la Ley Nº 827/96 De Seguros. Los Indicadores Financieros señalados se"));
		
		tableTexto.addBodyRow(texto5);
		
		TableRow texto6 = new TableRow();
		texto6.addCell(new Text("basan en los datos proveídos a la Superintendencia de Seguros a través de la Central de Información."));
		
		tableTexto.addBodyRow(texto6);
		
		TableRow texto7 = new TableRow();
		texto7.addCell(new Text("Los mismos no constituyen indicadores de solvencia, que conforme a la Ley citada la elaboración ha"));
		
		tableTexto.addBodyRow(texto7);
		
		TableRow texto8 = new TableRow();
		texto8.addCell(new Text("quedado reservada a las calificadoras habilitadas por la Comisión Nacional de Valores."));
		
		tableTexto.addBodyRow(texto8);
		
		TableRow rowAux3 = new TableRow();
		rowAux3.addCell(tableTexto);
		
		tablePrincipal.addBodyRow(rowAux3);
		tablePrincipal.addBodyRow(espaco);
		tablePrincipal.addBodyRow(espaco);
		//FIM DO TEXTO
		
		
		//INICIO TABELA DE INDICADORES
		
		//String top, String left, String bottom, String right, String style, String color
		
		Table tableInd = new Table();
		tableInd.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		tableInd.addColumnWidth("0.5cm");
		tableInd.addColumnWidth("7.5cm");
		tableInd.addColumnWidth("10cm");
		
		TableRow rowCabInd = new TableRow();
		//rowCabInd.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		rowCabInd.setFont(new Font("sans-serif", "10pt", null, "bold"));
		rowCabInd.addCell(new Text(" "));
		rowCabInd.setTextAlign("center");
		rowCabInd.addCell(new Text("ENTIDAD ASEGURADORA"));
		rowCabInd.setTextAlign("center");
		rowCabInd.addCell(new Text("INDICES FINANCIEROS (%)"));
		
		tableInd.addBodyRow(rowCabInd);
		
		TableRow rowCabInd2 = new TableRow();
		rowCabInd2.addCell(new Text("",1));
		rowCabInd2.addCell(new Text("",1));
		rowCabInd2.addCell(new Text("",1));
		
		tableInd.addBodyRow(rowCabInd2);
		
		Table tableInd1 = new Table();
		//tableInd1.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		tableInd1.setFont(new Font("sans-serif", "7pt", null, null));
		tableInd1.addColumnWidth("0.5cm");
		
		Table tableInd2 = new Table();
		//tableInd2.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		tableInd2.setFont(new Font("sans-serif", "7pt", null, null));
		tableInd2.addColumnWidth("7.5cm");
		
		Table tableInd3 = new Table();
		//tableInd3.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		tableInd3.setFont(new Font("sans-serif", "7pt", null, null));
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		tableInd3.addColumnWidth("1cm");
		
		TableRow row1 = new TableRow();
		row1.setBackgroundColor("#f0f0f0");
		//row1.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		row1.setFont(new Font("sans-serif", "7pt", null, "bold"));
		row1.addCell(new Text("."));
		tableInd1.addBodyRow(row1);
		
		TableRow row2 = new TableRow();
		row2.setBackgroundColor("#f0f0f0");
		row2.setFont(new Font("sans-serif", "7pt", null, "bold"));
		//row2.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		row2.addCell(new Text("a) Autorizadas a operar en los Ramos Elementales y Vida"));
		tableInd2.addBodyRow(row2);
		
		TableRow row3 = new TableRow();
		row3.setBackgroundColor("#f0f0f0");
		row3.setFont(new Font("sans-serif", "7pt", null, "bold"));
		//row3.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		row3.setTextAlign("right");
		row3.addCell(new Text("1"));
		row3.setTextAlign("right");
		row3.addCell(new Text("2"));
		row3.setTextAlign("right");
		row3.addCell(new Text("3"));
		row3.setTextAlign("right");
		row3.addCell(new Text("4"));
		row3.setTextAlign("right");
		row3.addCell(new Text("5"));
		row3.setTextAlign("right");
		row3.addCell(new Text("6"));
		row3.setTextAlign("right");
		row3.addCell(new Text("7"));
		row3.setTextAlign("right");
		row3.addCell(new Text("8"));
		row3.setTextAlign("right");
		row3.addCell(new Text("9"));
		row3.setTextAlign("right");
		row3.addCell(new Text("10"));
		tableInd3.addBodyRow(row3);
		
		int cont = 1;
		
		for(Aseguradora aseguradora : aseguradorasVida)
		{
			/*if(cont == 4)
				break;*/
			
			TableRow row4 = new TableRow();
			row4.addCell(new Text(new Integer(cont).toString()));
			tableInd1.addBodyRow(row4);
			
			TableRow row5 = new TableRow();
			/*if(aseguradora.obterId() == 5205)
				row5.addCell(new Text(aseguradora.obterNome() + " (*)"));
			else*/
				row5.addCell(new Text(aseguradora.obterNome()));
			tableInd2.addBodyRow(row5);
			
			TableRow row6 = new TableRow();
			
			double valor1 = indicadorHome.obterSinistrosBrutosPD(aseguradora);
			String valor1Str = "";
			if(valor1!=-714)
			{
				int i2 = 0;
				if(valor1>0)
					i2 = new Double((valor1*100) + 0.5).intValue();
				else
					i2 = new Double((valor1*100) - 0.5).intValue();
				
				valor1Str = new Integer(i2).toString();
			}
			else
				valor1Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor1Str));
			
			double valor2 = indicadorHome.obterSinistrosNetosPDNR(aseguradora);
			String valor2Str = "";
			if(valor2!=-714)
			{
				int i2 = 0;
				if(valor2>0)
					i2 = new Double((valor2*100) + 0.5).intValue();
				else
					i2 = new Double((valor2*100) - 0.5).intValue();
				
				valor2Str = new Integer(i2).toString();
			}
			else
				valor2Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor2Str));
			
			double valor3 = indicadorHome.obterGastosOperativosPD(aseguradora)  ;
			String valor3Str = "";
			if(valor3!=-714)
			{
				int i2 = 0;
				if(valor3>0)
					i2 = new Double((valor3*100) + 0.5).intValue();
				else
					i2 = new Double((valor3*100) - 0.5).intValue();
				
				valor3Str = new Integer(i2).toString();
			}
			else
				valor3Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor3Str));
			
			double valor4 = indicadorHome.obterGastosDeProducaoPNA(aseguradora)  ;
			String valor4Str = "";
			if(valor4!=-714)
			{				
				int i2 = 0;
				if(valor4>0)
					i2 = new Double((valor4*100) + 0.5).intValue();
				else
					i2 = new Double((valor4*100) - 0.5).intValue();
				
				valor4Str = new Integer(i2).toString();
			}
			else
				valor4Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor4Str));
			
			double valor5 = indicadorHome.obterGastosDeExportacaoPNA(aseguradora)  ;
			String valor5Str = "";
			if(valor5!=-714)
			{
				int i2 = 0;
				if(valor5>0)
					i2 = new Double((valor5*100) + 0.5).intValue();
				else
					i2 = new Double((valor5*100) - 0.5).intValue();
				
				valor5Str = new Integer(i2).toString();
			}
			else
				valor5Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor5Str));
			
			double valor6 = indicadorHome.obterProvisoesTecnicas(aseguradora)  ;
			String valor6Str = "";
			if(valor6!=-714)
			{
				int i2 = 0;
				if(valor6>0)
					i2 = new Double((valor6*100) + 0.5).intValue();
				else
					i2 = new Double((valor6*100) - 0.5).intValue();
				
				valor6Str = new Integer(i2).toString();
			}
			else
				valor6Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor6Str));
			
			double valor7 = indicadorHome.obterPNAtivoTotal(aseguradora)  ;
			String valor7Str = "";
			if(valor7!=-714)
			{
				int i2 = 0;
				if(valor7>0)
					i2 = new Double((valor7*100) + 0.5).intValue();
				else
					i2 = new Double((valor7*100) - 0.5).intValue();
				
				valor7Str = new Integer(i2).toString();
			}
			else
				valor7Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor7Str));
			
			double valor8 = indicadorHome.obterRetornoSemPN(aseguradora)  ;
			String valor8Str = "";
			if(valor8!=-714)
			{
				int i2 = 0;
				if(valor8>0)
					i2 = new Double((valor8*100) + 0.5).intValue();
				else
					i2 = new Double((valor8*100) - 0.5).intValue();
				
				valor8Str = new Integer(i2).toString();
			}
			else
				valor8Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor8Str));
			
			double valor9 = indicadorHome.obterResultadoTecnicoSemPN(aseguradora)  ;
			String valor9Str = "";
			if(valor9!=-714)
			{
				int i2 = 0;
				if(valor9>0)
					i2 = new Double((valor9*100) + 0.5).intValue();
				else
					i2 = new Double((valor9*100) - 0.5).intValue();
				
				valor9Str = new Integer(i2).toString();
			}
			else
				valor9Str = "0";
			
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor9Str));
			
			double valor10 = indicadorHome.obterMagemDeGanancia(aseguradora)  ;
			String valor10Str = "";
			if(valor10!=-714)
			{
				int i2 = 0;
				if(valor10>0)
					i2 = new Double((valor10*100) + 0.5).intValue();
				else
					i2 = new Double((valor10*100) - 0.5).intValue();
				
				valor10Str = new Integer(i2).toString();
			}
			else
				valor10Str = "0";
			
			row6.setTextAlign("right");
			row6.addCell(new Text(valor10Str));
			
			cont++;
			
			tableInd3.addBodyRow(row6);
		}
		
		cont = 1;
		
		TableRow row100 = new TableRow();
		row100.setBackgroundColor("#f0f0f0");
		//row1.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		row100.setFont(new Font("sans-serif", "7pt", null, "bold"));
		row100.addCell(new Text("."));
		tableInd1.addBodyRow(row100);
		
		TableRow row200 = new TableRow();
		row200.setBackgroundColor("#f0f0f0");
		row200.setFont(new Font("sans-serif", "7pt", null, "bold"));
		//row2.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		row200.addCell(new Text("b) Aut. a operar en los Ramos Elem. o Patrimoniales"));
		tableInd2.addBodyRow(row200);
		
		TableRow row300 = new TableRow();
		row300.setFont(new Font("sans-serif", "7pt", null, "bold"));
		row300.setBackgroundColor("#f0f0f0");
		//row3.setBorder(new Border("1pt", "1pt", "1pt", "1pt", "solid", "black"));
		row300.setTextAlign("right");
		row300.addCell(new Text("1"));
		row300.setTextAlign("right");
		row300.addCell(new Text("2"));
		row300.setTextAlign("right");
		row300.addCell(new Text("3"));
		row300.setTextAlign("right");
		row300.addCell(new Text("4"));
		row300.setTextAlign("right");
		row300.addCell(new Text("5"));
		row300.setTextAlign("right");
		row300.addCell(new Text("6"));
		row300.setTextAlign("right");
		row300.addCell(new Text("7"));
		row300.setTextAlign("right");
		row300.addCell(new Text("8"));
		row300.setTextAlign("right");
		row300.addCell(new Text("9"));
		row300.setTextAlign("right");
		row300.addCell(new Text("10"));
		tableInd3.addBodyRow(row300);
		
		boolean cor = false;
		
		for(Aseguradora aseguradora : aseguradoras)
		{
			//if(cont == 4)
				//break;
			
			TableRow row4 = new TableRow();
			if(cor)
				row4.setBackgroundColor("#f0f0f0");
			row4.addCell(new Text(new Integer(cont).toString()));
			tableInd1.addBodyRow(row4);
			
			TableRow row5 = new TableRow();
			if(cor)
				row5.setBackgroundColor("#f0f0f0");
			/*if(aseguradora.obterId() == 5205)
				row5.addCell(new Text(aseguradora.obterNome() + " (*)"));
			else*/
				row5.addCell(new Text(aseguradora.obterNome()));
			tableInd2.addBodyRow(row5);
			
			TableRow row6 = new TableRow();
			if(cor)
				row6.setBackgroundColor("#f0f0f0");
			
			/*if(aseguradora.obterId() == 5205)
			{
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				row6.addCell(new Text("0"));
				row6.setTextAlign("right");
				cont++;
				
				tableInd3.addBodyRow(row6);
			}
			else
			{*/
				double valor1 = indicadorHome.obterSinistrosBrutosPD(aseguradora)  ;
				String valor1Str = "";
				if(valor1!=-714)
				{
					int i2 = 0;
					if(valor1>0)
						i2 = new Double((valor1*100) + 0.5).intValue();
					else
						i2 = new Double((valor1*100) - 0.5).intValue();
					
					valor1Str = new Integer(i2).toString();
				}
				else
					valor1Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor1Str));
				
				double valor2 = indicadorHome.obterSinistrosNetosPDNR(aseguradora)   ;
				String valor2Str = "";
				if(valor2!=-714)
				{
					int i2 = 0;
					if(valor2>0)
						i2 = new Double((valor2*100) + 0.5).intValue();
					else
						i2 = new Double((valor2*100) - 0.5).intValue();
					
					valor2Str = new Integer(i2).toString();
				}
				else
					valor2Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor2Str));
				
				double valor3 = indicadorHome.obterGastosOperativosPD(aseguradora)  ;
				String valor3Str = "";
				if(valor3!=-714)
				{
					int i2 = 0;
					if(valor3>0)
						i2 = new Double((valor3*100) + 0.5).intValue();
					else
						i2 = new Double((valor3*100) - 0.5).intValue();
					
					valor3Str = new Integer(i2).toString();
				}
				else
					valor3Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor3Str));
				
				double valor4 = indicadorHome.obterGastosDeProducaoPNA(aseguradora)  ;
				String valor4Str = "";
				if(valor4!=-714)
				{
					int i2 = 0;
					if(valor4>0)
						i2 = new Double((valor4*100) + 0.5).intValue();
					else
						i2 = new Double((valor4*100) - 0.5).intValue();
					
					valor4Str = new Integer(i2).toString();
				}
				else
					valor4Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor4Str));
				
				double valor5 = indicadorHome.obterGastosDeExportacaoPNA(aseguradora)  ;
				String valor5Str = "";
				if(valor5!=-714)
				{
					int i2 = 0;
					if(valor5>0)
						i2 = new Double((valor5*100) + 0.5).intValue();
					else
						i2 = new Double((valor5*100) - 0.5).intValue();
					
					valor5Str = new Integer(i2).toString();
				}
				else
					valor5Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor5Str));
				
				double valor6 = indicadorHome.obterProvisoesTecnicas(aseguradora)  ;
				String valor6Str = "";
				if(valor6!=-714)
				{
					int i2 = 0;
					if(valor6>0)
						i2 = new Double((valor6*100) + 0.5).intValue();
					else
						i2 = new Double((valor6*100) - 0.5).intValue();
					
					valor6Str = new Integer(i2).toString();
				}
				else
					valor6Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor6Str));
				
				double valor7 = indicadorHome.obterPNAtivoTotal(aseguradora)  ;
				String valor7Str = "";
				if(valor7!=-714)
				{
					int i2 = 0;
					if(valor7>0)
						i2 = new Double((valor7*100) + 0.5).intValue();
					else
						i2 = new Double((valor7*100) - 0.5).intValue();
					
					valor7Str = new Integer(i2).toString();
				}
				else
					valor7Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor7Str));
				
				double valor8 = indicadorHome.obterRetornoSemPN(aseguradora)  ;
				String valor8Str = "";
				if(valor8!=-714)
				{
					int i2 = 0;
					if(valor8>0)
						i2 = new Double((valor8*100) + 0.5).intValue();
					else
						i2 = new Double((valor8*100) - 0.5).intValue();
					
					valor8Str = new Integer(i2).toString();
					
				}
				else
					valor8Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor8Str));
				
				double valor9 = indicadorHome.obterResultadoTecnicoSemPN(aseguradora)  ;
				String valor9Str = "";
				if(valor9!=-714)
				{
					int i2 = 0;
					if(valor9>0)
						i2 = new Double((valor9*100) + 0.5).intValue();
					else
						i2 = new Double((valor9*100) - 0.5).intValue();
					
					valor9Str = new Integer(i2).toString();
				}
				else
					valor9Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor9Str));
				
				double valor10 = indicadorHome.obterMagemDeGanancia(aseguradora)  ;
				String valor10Str = "";
				if(valor10!=-714)
				{
					int i2 = 0;
					if(valor10>0)
						i2 = new Double((valor10*100) + 0.5).intValue();
					else
						i2 = new Double((valor10*100) - 0.5).intValue();
					
					valor10Str = new Integer(i2).toString();
				}
				else
					valor10Str = "0";
				
				row6.setTextAlign("right");
				row6.addCell(new Text(valor10Str));
				
				cont++;
				
				tableInd3.addBodyRow(row6);
			//}
			
			cor=!cor;
		}
		
		TableRow row7 = new TableRow();
		row7.addCell(new Text(" "));
		tableInd1.addBodyRow(row7);
		
		TableRow row8 = new TableRow();
		row8.setFont(new Font("sans-serif", "7pt", null, "bold"));
		row8.addCell(new Text("Promedio Ponderado del Mercado"));
		tableInd2.addBodyRow(row8);
		
		TableRow row9 = new TableRow();
		row9.setFont(new Font("sans-serif", "7pt", null, "bold"));
		
		double valor11 = indicadorHome.obterMagemPonderadaSinistrosBrutosPD()  ;
		String valor11Str = "";
		if(valor11!=-714)
		{
			int i2 = 0;
			if(valor11>0)
				i2 = new Double((valor11*100) + 0.5).intValue();
			else
				i2 = new Double((valor11*100) - 0.5).intValue();
			
			valor11Str = new Integer(i2).toString();
		}
		else
			valor11Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor11Str));
		
		double valor12 = indicadorHome.obterMargemPonderadaSinistrosNetosPDNR()  ;
		String valor12Str = "";
		if(valor12!=-714)
		{
			int i2 = 0;
			if(valor12>0)
				i2 = new Double((valor12*100) + 0.5).intValue();
			else
				i2 = new Double((valor12*100) - 0.5).intValue();
			
			valor12Str = new Integer(i2).toString();
		}				
		else
			valor12Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor12Str));
		
		double valor13 = indicadorHome.obterMargemPonderadaGastosOperativosPD()  ;
		String valor13Str = "";
		if(valor13!=-714)
		{
			int i2 = 0;
			if(valor13>0)
				i2 = new Double((valor13*100) + 0.5).intValue();
			else
				i2 = new Double((valor13*100) - 0.5).intValue();
			
			valor13Str = new Integer(i2).toString();
		}
		else
			valor13Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor13Str));
		
		double valor14 = indicadorHome.obterMargemPonderadaGastosDeProducaoPNA()  ;
		String valor14Str = "";
		if(valor14!=-714)
		{
			int i2 = 0;
			if(valor14>0)
				i2 = new Double((valor14*100) + 0.5).intValue();
			else
				i2 = new Double((valor14*100) - 0.5).intValue();
			
			valor14Str = new Integer(i2).toString();
		}
		else
			valor14Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor14Str));
		
		double valor15 = indicadorHome.obterMargemPonderadaGastosDeExportacaoPNA()  ;
		String valor15Str = "";
		if(valor15!=-714)
		{
			int i2 = 0;
			if(valor15>0)
				i2 = new Double((valor15*100) + 0.5).intValue();
			else
				i2 = new Double((valor15*100) - 0.5).intValue();
			
			valor15Str = new Integer(i2).toString();
		}
		else
			valor15Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor15Str));
		
		double valor16 = indicadorHome.obterMargemPonderadaProvisoesTecnicas()  ;
		String valor16Str = "";
		if(valor16!=-714)
		{
			int i2 = 0;
			if(valor16>0)
				i2 = new Double((valor16*100) + 0.5).intValue();
			else
				i2 = new Double((valor16*100) - 0.5).intValue();
			
			valor16Str = new Integer(i2).toString();
		}
		else
			valor16Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor16Str));
		
		double valor17 = indicadorHome.obterMagemPonderadaPNAtivoTotal()  ;
		String valor17Str = "";
		if(valor17!=-714)
		{
			int i2 = 0;
			if(valor17>0)
				i2 = new Double((valor17*100) + 0.5).intValue();
			else
				i2 = new Double((valor17*100) - 0.5).intValue();
			
			valor17Str = new Integer(i2).toString();
		}
		else
			valor17Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor17Str));
		
		double valor18 = indicadorHome.obterMargemPonderadaRetornoSemPN()  ;
		String valor18Str = "";
		if(valor18!=-714)
		{
			int i2 = 0;
			if(valor18>0)
				i2 = new Double((valor18*100) + 0.5).intValue();
			else
				i2 = new Double((valor18*100) - 0.5).intValue();
			
			valor18Str = new Integer(i2).toString();
		}
		else
			valor18Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor18Str));
		
		double valor19 = indicadorHome.obterMargemPonderadaResultadoTecnicoSemPN()  ;
		String valor19Str = "";
		if(valor19!=-714)
		{
			int i2 = 0;
			if(valor19>0)
				i2 = new Double((valor19*100) + 0.5).intValue();
			else
				i2 = new Double((valor19*100) - 0.5).intValue();
			
			valor19Str = new Integer(i2).toString();
		}
		else
			valor19Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor19Str));
		
		double valor20 = indicadorHome.obterMargemPonderadaMagemDeGanancia()  ;
		String valor20Str = "";
		if(valor20!=-714)
		{
			int i2 = 0;
			if(valor20>0)
				i2 = new Double((valor20*100) + 0.5).intValue();
			else
				i2 = new Double((valor20*100) - 0.5).intValue();
			
			valor20Str = new Integer(i2).toString();
		}
		else
			valor20Str = "0";
		
		row9.setTextAlign("right");
		row9.addCell(new Text(valor20Str));
		
		tableInd3.addBodyRow(row9);
				
		TableRow aux7 = new TableRow();
		aux7.addCell(tableInd1);
		aux7.addCell(tableInd2);
		aux7.addCell(tableInd3);
		
		tableInd.addBodyRow(aux7);
		
		TableRow aux8 = new TableRow();
		aux8.addCell(tableInd);
		
		tablePrincipal.addBodyRow(aux8);
		
		tablePrincipal.addBodyRow(espaco);
		//FIM TABELA DE INDICADORES
		
		
		//INICIO LEGENDA TABELA DE INDICADORES
		TableRow row10 = new TableRow();
		row10.setFont(new Font("sans-serif", "8pt", null, null));
		row10.addCell(new Text("1 - SINIESTRALIDAD BRUTA: Porción de prima ganada consumida por siniestros (%)."));
		
		TableRow row11 = new TableRow();
		row11.setFont(new Font("sans-serif", "8pt", null, null));
		row11.addCell(new Text("2 - SINIESTRALIDAD NETA: Porción de prima ganada por riesgo no cedido, que fue consumida por siniestros de su retención (%)."));
		
		TableRow row12 = new TableRow();
		row12.setFont(new Font("sans-serif", "8pt", null, null));
		row12.addCell(new Text("3 - INDICE DE GASTO OPERATIVO: Porción de primas ganadas insumidas por el total de gastos operativos (%)."));
		
		TableRow row13 = new TableRow();
		row13.setFont(new Font("sans-serif", "8pt", null, null));
		row13.addCell(new Text("4 - INDICE DE  GASTO DE PRODUCCIÓN: Porción de primas ganadas insumidas por el gasto de producción (%)."));
		
		TableRow row14 = new TableRow();
		row14.setFont(new Font("sans-serif", "8pt", null, null));
		row14.addCell(new Text("5 - INDICE DE GASTO DE EXPLOTACIÓN: Porción de primas ganadas insumidas por el gasto de explotación (%)."));
		
		TableRow row15 = new TableRow();
		row15.setFont(new Font("sans-serif", "8pt", null, null));
		row15.addCell(new Text("6 - REPRESENTATIVIDAD DE LAS INVERSIONES: Porción de las inversiones que representan a las provisiones técnicas (%)."));
		
		TableRow row16 = new TableRow();
		row16.setFont(new Font("sans-serif", "8pt", null, null));
		row16.addCell(new Text("7 - INDICE DE REPRESENTATIVIDAD DEL ACTIVO: Porción del Activo que representa al Patrimonio Neto (%)."));
		
		TableRow row17 = new TableRow();
		row17.setFont(new Font("sans-serif", "8pt", null, null));
		row17.addCell(new Text("8 - INDICE GENERAL DE RENDIMIENTO PATRIMONIAL: Relación entre el resultado del ejercicio y el volúmen del Patrimonio Neto (%)."));
		
		TableRow row18 = new TableRow();
		row18.setFont(new Font("sans-serif", "8pt", null, null));
		row18.addCell(new Text("9 - INDICE TÉCNICO DE RENDIMIENTO PATRIMONIAL: Relación entre el resultado técnico y el volúmen del Patrimonio Neto (%)."));
		
		TableRow row19 = new TableRow();
		row19.setFont(new Font("sans-serif", "8pt", null, null));
		row19.addCell(new Text("10 - INDICE RENDIMIENTO S/VOLÚMEN OPERACIONES TÉCNICAS: Relación entre el Resultado del Ejercicio y el volúmen del primaje (%)."));
		
		/*TableRow row20 = new TableRow();
		row20.setFont(new Font("sans-serif", "8pt", null, null));
		row20.addCell(new Text("(*) Suspendida para emitir pòlizas como medida cautelar, por déficit de su Fondo de Garantía (Res. SS.SG. Nº 045/06 de fecha 20 de enero de 2006)."));*/
		
		tablePrincipal.addBodyRow(row10);
		tablePrincipal.addBodyRow(row11);
		tablePrincipal.addBodyRow(row12);
		tablePrincipal.addBodyRow(row13);
		tablePrincipal.addBodyRow(row14);
		tablePrincipal.addBodyRow(row15);
		tablePrincipal.addBodyRow(row16);
		tablePrincipal.addBodyRow(row17);
		tablePrincipal.addBodyRow(row18);
		tablePrincipal.addBodyRow(row19);
		//tablePrincipal.addBodyRow(espaco);
		//tablePrincipal.addBodyRow(row20);
		//FIM LEGENDA TABELA DE INDICADORES
		
		/*this.setGerarArquivoPDF(true);
		
		this.setNomeArquivoPDF("CalcIndicadores.pdf");*/
		
		Region bodyRegion = new Region();
		bodyRegion.addBlock(tablePrincipal);
		this.setBody(bodyRegion);
		
		
	}
}
