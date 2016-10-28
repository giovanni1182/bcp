package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.IndicadoresHome;
import com.gvs.crm.model.Inscricao;

import infra.config.InfraProperties;

public class IndicadoresXLS extends Excel 
{
	public IndicadoresXLS(Collection<Aseguradora> aseguradoras, Date data, IndicadoresHome indicadorHome) throws Exception
	{
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		formataValor.setRoundingMode(RoundingMode.HALF_EVEN);
		//formataValor.setRoundingMode(RoundingMode.HALF_UP);
		
		//ArredondarImpl arredonda = new ArredondarImpl();
		
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
		
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet planilha = wb.createSheet("Planilha");
		
		HSSFFont fonteTitulo = wb.createFont();
        fonteTitulo.setFontHeightInPoints((short)10);
        fonteTitulo.setFontName("Arial");
        fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)8);
        fonteTexto.setFontName("Arial");
        
        HSSFFont fonteTextoN = wb.createFont();
        fonteTextoN.setFontHeightInPoints((short)8);
        fonteTextoN.setFontName("Arial");
        fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle estiloTitulo = wb.createCellStyle();
        estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitulo.setFont(fonteTitulo);
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoD = wb.createCellStyle();
        estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTextoD.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoE = wb.createCellStyle();
        estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
        estiloTextoE.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoN = wb.createCellStyle();
        estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoN.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
        estiloTextoN_E.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloTextoN_E.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
        estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTituloTabela.setFont(fonteTextoN);
        estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle estiloTituloTabelaE = wb.createCellStyle();
        estiloTituloTabelaE.setFont(fonteTextoN);
        estiloTituloTabelaE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTituloTabelaE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle estiloTituloTabelaD = wb.createCellStyle();
        estiloTituloTabelaD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTituloTabelaD.setFont(fonteTextoN);
        estiloTituloTabelaD.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTituloTabelaD.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
        
        InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
        byte [] bytes = IOUtils.toByteArray (is);
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
        is.close();
        
        HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 6);  
        anchoVivaBem.setAnchorType(3);  
        planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
        
        HSSFRow row = planilha.createRow(1);
        HSSFCell celula = row.createCell(5);
        
        celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
        celula.setCellStyle(estiloTitulo);
        
        Region r = new Region(1, (short)5, 1, (short)10);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(2);
        celula = row.createCell(5);
        
        celula.setCellValue("INDICADORES FINANCIEROS");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(2, (short)5, 2, (short)10);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(3);
        celula = row.createCell(5);
        
        celula.setCellValue("DE LAS EMPRESAS DE SEGUROS AL:");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(3, (short)5, 3, (short)10);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(4);
        celula = row.createCell(5);
        
        celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(data));
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(4, (short)5, 4, (short)10);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(6);
        celula = row.createCell(0);
        
        celula.setCellValue("La presente publicación se realiza en cumplimiento de la Resolución SS.SG.N° 011/10 del 9 de "+
"febrero de 2010 que dispone la publicación bimestral de los indicadores financieros en virtud de la Ley "+
"Nº 3899/09 Que Regula a las Sociedades Calificadoras de Riesgos, Deroga la Ley Nº 1056/97 y Modifica "+
"el Artículo 106 de la Ley Nº 861/96 General de Bancos, Financieras y Otras Entidades de Crédito y el "+
"inciso d) del Artículo 61 de la Ley Nº 827/96 De Seguros. Los Indicadores Financieros señalados se "+
"basan en los datos proveídos a la Superintendencia de Seguros a través de la Central de Información. "+
"Los mismos no constituyen indicadores de solvencia, que conforme a la Ley citada la elaboración ha "+
"quedado reservada a las calificadoras habilitadas por la Comisión Nacional de Valores.");
        celula.setCellStyle(estiloTextoE);
        
        r = new Region(6, (short)0, 10, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(12);
        celula = row.createCell(1);
        celula.setCellValue("ENTIDAD ASEGURADORA");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(12, (short)1, 12, (short)4);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(5);
        celula.setCellValue("INDICES FINANCIEROS (%)");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(12, (short)5, 12, (short)13);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(14);
        celula = row.createCell(1);
        celula.setCellValue("a) Autorizadas a operar en los Ramos Elementales y Vida");
        celula.setCellStyle(estiloTituloTabelaE);
        
        r = new Region(14, (short)1, 14, (short)4);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(5);
        celula.setCellValue("1");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(6);
        celula.setCellValue("2");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(7);
        celula.setCellValue("3");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(8);
        celula.setCellValue("4");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(9);
        celula.setCellValue("5");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(10);
        celula.setCellValue("6");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(11);
        celula.setCellValue("7");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(12);
        celula.setCellValue("8");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(13);
        celula.setCellValue("9");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(14);
        celula.setCellValue("10");
        celula.setCellStyle(estiloTituloTabela);
        
        int linha = 15;
        int cont = 1;
        
        for(Aseguradora aseguradora : aseguradorasVida)
		{
			row = planilha.createRow(linha);
			
			celula = row.createCell(0);
	        celula.setCellValue(cont);
	        celula.setCellStyle(estiloTexto);
			
	        String nome = "";
	        if(aseguradora.obterId() == 5205)
	        	nome = aseguradora.obterNome() + " (*)";
			else
				nome = aseguradora.obterNome();
	        
	        celula = row.createCell(1);
	        celula.setCellValue(nome);
	        celula.setCellStyle(estiloTextoE);
	        
	        r = new Region(linha, (short)1, linha, (short)4);
	        planilha.addMergedRegion(r);
			
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
			
			celula = row.createCell(5);
	        celula.setCellValue(valor1Str);
	        celula.setCellStyle(estiloTextoD);
	        
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
			
			celula = row.createCell(6);
	        celula.setCellValue(valor2Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor3 = indicadorHome.obterGastosOperativosPD(aseguradora);
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
			
			celula = row.createCell(7);
	        celula.setCellValue(valor3Str);
	        celula.setCellStyle(estiloTextoD);
			
	        double valor4 = indicadorHome.obterGastosDeProducaoPNA(aseguradora);
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
			
			celula = row.createCell(8);
	        celula.setCellValue(valor4Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor5 = indicadorHome.obterGastosDeExportacaoPNA(aseguradora);
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
			
			celula = row.createCell(9);
	        celula.setCellValue(valor5Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor6 = indicadorHome.obterProvisoesTecnicas(aseguradora);
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
			
			celula = row.createCell(10);
	        celula.setCellValue(valor6Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor7 = indicadorHome.obterPNAtivoTotal(aseguradora);
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
			
			celula = row.createCell(11);
	        celula.setCellValue(valor7Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor8 = indicadorHome.obterRetornoSemPN(aseguradora);
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
			

			celula = row.createCell(12);
	        celula.setCellValue(valor8Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor9 = indicadorHome.obterResultadoTecnicoSemPN(aseguradora);
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
			
			celula = row.createCell(13);
	        celula.setCellValue(valor9Str);
	        celula.setCellStyle(estiloTextoD);
	        
	        double valor10 = indicadorHome.obterMagemDeGanancia(aseguradora);
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
			
			celula = row.createCell(14);
	        celula.setCellValue(valor10Str);
	        celula.setCellStyle(estiloTextoD);
			
			cont++;
			linha++;
		}
        
        row = planilha.createRow(linha);
        celula = row.createCell(1);
        celula.setCellValue("b) Aut. a operar en los Ramos Elem. o Patrimoniales");
        celula.setCellStyle(estiloTituloTabelaE);
        
        r = new Region(linha, (short)1, linha, (short)4);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(5);
        celula.setCellValue("1");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(6);
        celula.setCellValue("2");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(7);
        celula.setCellValue("3");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(8);
        celula.setCellValue("4");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(9);
        celula.setCellValue("5");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(10);
        celula.setCellValue("6");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(11);
        celula.setCellValue("7");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(12);
        celula.setCellValue("8");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(13);
        celula.setCellValue("9");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(14);
        celula.setCellValue("10");
        celula.setCellStyle(estiloTituloTabela);
        
        linha++;
        cont = 1;
        
        for(Aseguradora aseguradora : aseguradoras)
		{
			//if(aseguradora.obterId() == 5234)
			//{
				row = planilha.createRow(linha);
				
				celula = row.createCell(0);
		        celula.setCellValue(cont);
		        celula.setCellStyle(estiloTexto);
				
		        String nome = "";
		        /*if(aseguradora.obterId() == 5205)
		        	nome = aseguradora.obterNome() + " (*)";
				else*/
					nome = aseguradora.obterNome();
		        
		        celula = row.createCell(1);
		        celula.setCellValue(nome);
		        celula.setCellStyle(estiloTextoE);
		        
		        r = new Region(linha, (short)1, linha, (short)4);
		        planilha.addMergedRegion(r);
		        
		        if(aseguradora.obterId() == 5205)
				{
		        	celula = row.createCell(5);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(6);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(7);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(8);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(9);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(10);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(11);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(12);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(13);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
			        
			        celula = row.createCell(14);
			        celula.setCellValue(0);
			        celula.setCellStyle(estiloTextoD);
				}
		        else
		        {
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
					
					celula = row.createCell(5);
			        celula.setCellValue(valor1Str);
			        celula.setCellStyle(estiloTextoD);
			        
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
					
					celula = row.createCell(6);
			        celula.setCellValue(valor2Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor3 = indicadorHome.obterGastosOperativosPD(aseguradora);
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
					
					celula = row.createCell(7);
			        celula.setCellValue(valor3Str);
			        celula.setCellStyle(estiloTextoD);
					
			        double valor4 = indicadorHome.obterGastosDeProducaoPNA(aseguradora);
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
					
					celula = row.createCell(8);
			        celula.setCellValue(valor4Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor5 = indicadorHome.obterGastosDeExportacaoPNA(aseguradora);
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
					
					celula = row.createCell(9);
			        celula.setCellValue(valor5Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor6 = indicadorHome.obterProvisoesTecnicas(aseguradora);
			        
			        //System.out.println(aseguradora.obterNome() +  " valor6 " + valor6);
			        
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
					
					celula = row.createCell(10);
			        celula.setCellValue(valor6Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor7 = indicadorHome.obterPNAtivoTotal(aseguradora);
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
					
					celula = row.createCell(11);
			        celula.setCellValue(valor7Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor8 = indicadorHome.obterRetornoSemPN(aseguradora);
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
					
			
					celula = row.createCell(12);
			        celula.setCellValue(valor8Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor9 = indicadorHome.obterResultadoTecnicoSemPN(aseguradora);
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
					
					celula = row.createCell(13);
			        celula.setCellValue(valor9Str);
			        celula.setCellStyle(estiloTextoD);
			        
			        double valor10 = indicadorHome.obterMagemDeGanancia(aseguradora);
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
					
					celula = row.createCell(14);
			        celula.setCellValue(valor10Str);
			        celula.setCellStyle(estiloTextoD);
		        }
				
				cont++;
				linha++;
			//}
		}
        
        row = planilha.createRow(linha);
		celula = row.createCell(1);
        celula.setCellValue("Promedio Ponderado del Mercado");
        celula.setCellStyle(estiloTituloTabelaE);
        r = new Region(linha, (short)1, linha, (short)4);
        planilha.addMergedRegion(r);
        
        double valor11 = indicadorHome.obterMagemPonderadaSinistrosBrutosPD();
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
        
		celula = row.createCell(5);
        celula.setCellValue(valor11Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor12 = indicadorHome.obterMargemPonderadaSinistrosNetosPDNR();
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
		
		celula = row.createCell(6);
        celula.setCellValue(valor12Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor13 = indicadorHome.obterMargemPonderadaGastosOperativosPD();
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
		
		celula = row.createCell(7);
        celula.setCellValue(valor13Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor14 = indicadorHome.obterMargemPonderadaGastosDeProducaoPNA();
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
		
		celula = row.createCell(8);
        celula.setCellValue(valor14Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor15 = indicadorHome.obterMargemPonderadaGastosDeExportacaoPNA();
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
		
		celula = row.createCell(9);
        celula.setCellValue(valor15Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor16 = indicadorHome.obterMargemPonderadaProvisoesTecnicas();
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
		
		celula = row.createCell(10);
        celula.setCellValue(valor16Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor17 = indicadorHome.obterMagemPonderadaPNAtivoTotal();
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
		
		celula = row.createCell(11);
        celula.setCellValue(valor17Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor18 = indicadorHome.obterMargemPonderadaRetornoSemPN();
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
		
		celula = row.createCell(12);
        celula.setCellValue(valor18Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor19 = indicadorHome.obterMargemPonderadaResultadoTecnicoSemPN();
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
		
		celula = row.createCell(13);
        celula.setCellValue(valor19Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        double valor20 = indicadorHome.obterMargemPonderadaMagemDeGanancia();
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
		
		celula = row.createCell(14);
        celula.setCellValue(valor20Str);
        celula.setCellStyle(estiloTituloTabelaD);
        
        
        row = planilha.createRow(linha+2);
		celula = row.createCell(0);
        celula.setCellValue("1 - SINIESTRALIDAD BRUTA: Porción de prima ganada consumida por siniestros (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+2, (short)0, linha+2, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+3);
		celula = row.createCell(0);
        celula.setCellValue("2 - SINIESTRALIDAD NETA: Porción de prima ganada por riesgo no cedido, que fue consumida por siniestros de su retención (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+3, (short)0, linha+3, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+4);
		celula = row.createCell(0);
        celula.setCellValue("3 - INDICE DE GASTO OPERATIVO: Porción de primas ganadas insumidas por el total de gastos operativos (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+4, (short)0, linha+4, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+5);
		celula = row.createCell(0);
        celula.setCellValue("4 - INDICE DE GASTO DE PRODUCCIÓN: Porción de primas ganadas insumidas por el gasto de producción (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+5, (short)0, linha+5, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+6);
		celula = row.createCell(0);
        celula.setCellValue("5 - INDICE DE GASTO DE EXPLOTACIÓN: Porción de primas ganadas insumidas por el gasto de explotación (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+6, (short)0, linha+6, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+7);
		celula = row.createCell(0);
        celula.setCellValue("6 - REPRESENTATIVIDAD DE LAS INVERSIONES: Porción de las inversiones que representan a las provisiones técnicas (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+7, (short)0, linha+7, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+8);
		celula = row.createCell(0);
        celula.setCellValue("7 - INDICE DE REPRESENTATIVIDAD DEL ACTIVO: Porción del Activo que representa al Patrimonio Neto (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+8, (short)0, linha+8, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+9);
		celula = row.createCell(0);
        celula.setCellValue("8 - INDICE GENERAL DE RENDIMIENTO PATRIMONIAL: Relación entre el resultado del ejercicio y el volúmen del Patrimonio Neto (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+9, (short)0, linha+9, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+10);
		celula = row.createCell(0);
        celula.setCellValue("9 - INDICE TÉCNICO DE RENDIMIENTO PATRIMONIAL: Relación entre el resultado técnico y el volúmen del Patrimonio Neto (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+10, (short)0, linha+10, (short)14);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+11);
		celula = row.createCell(0);
        celula.setCellValue("10 - INDICE RENDIMIENTO S/VOLÚMEN OPERACIONES TÉCNICAS: Relación entre el Resultado del Ejercicio y el volúmen del primaje (%).");
        celula.setCellStyle(estiloTextoE);
        r = new Region(linha+11, (short)0, linha+11, (short)14);
        planilha.addMergedRegion(r);
        
        
        wb.write(stream);

        stream.flush();

        stream.close();
	}
}
