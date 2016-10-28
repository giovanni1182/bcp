package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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

import infra.config.InfraProperties;

public class ApolicesPorPessoaViewXLS extends Excel
{
	public ApolicesPorPessoaViewXLS(Aseguradora aseguradora, Date dataInicio, Date dataFim, Collection<Aseguradora> aseguradoras, String textoUsuario, String ramo, String secao,String modalidade) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet planilha = wb.createSheet("Planilha");
		
		 HSSFFont fonteTitulo = wb.createFont();
	     fonteTitulo.setFontHeightInPoints((short)10);
	     fonteTitulo.setFontName("Arial");
	     fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      
	     HSSFFont fonteTituloTabela = wb.createFont();
	     fonteTituloTabela.setFontHeightInPoints((short)8);
	     fonteTituloTabela.setFontName("Arial");
	     fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	      
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
	      
	     HSSFCellStyle estiloTextoN = wb.createCellStyle();
	     estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	     estiloTextoN.setFont(fonteTextoN);
	      
	     HSSFCellStyle estiloTextoE = wb.createCellStyle();
	     estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	     estiloTextoE.setFont(fonteTexto);
	      
	     HSSFCellStyle estiloTextoD = wb.createCellStyle();
	     estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	     estiloTextoD.setFont(fonteTexto);
	      
	     HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	     estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	     estiloTituloTabela.setFont(fonteTituloTabela);
	     estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	     estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     
	     HSSFCellStyle estiloTituloTabelaE = wb.createCellStyle();
	     estiloTituloTabelaE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	     estiloTituloTabelaE.setFont(fonteTituloTabela);
	     estiloTituloTabelaE.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	     estiloTituloTabelaE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	     
	     String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
	      
	     InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
	     byte [] bytes = IOUtils.toByteArray (is);
	     int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
	     is.close();
	      
	     HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
	     anchoVivaBem.setAnchorType(3);  
	     planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
	     
	     HSSFRow row = planilha.createRow(1);
	     HSSFCell celula = row.createCell(5);
	        
	     celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
	     celula.setCellStyle(estiloTitulo);
	     Region r = new Region(1, (short)5, 1, (short)8);
	     planilha.addMergedRegion(r);
	     
	     int linha = 6;
	     
	     row = planilha.createRow(linha);
	     celula = row.createCell(0);
	     
	     Calendar c = Calendar.getInstance();
	     c.setTime(dataInicio);
	     int primeiroDia = c.getActualMinimum(Calendar.DAY_OF_MONTH);
			
	     c.setTime(dataFim);
	     int ultimoDia = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			
	     String mesAnoInicio = new SimpleDateFormat("MM/yyyy").format(dataInicio);
	     String mesAnoFim = new SimpleDateFormat("MM/yyyy").format(dataFim);
			
	     Date dataInicioReal = new SimpleDateFormat("dd/MM/yyyy").parse(primeiroDia + "/"+mesAnoInicio);
	     Date dataFimReal = new SimpleDateFormat("dd/MM/yyyy").parse(ultimoDia + "/"+mesAnoFim);
			
	     celula.setCellValue("Aseguradora");
	     celula.setCellStyle(estiloTituloTabela);
	     r = new Region(linha, (short)0, linha, (short)2);
	     planilha.addMergedRegion(r);
	     
	     int coluna = 3;
	     
	     c.setTime(dataInicioReal);
	     while(c.getTime().compareTo(dataFimReal)<=0)
	     {
	    	 int colunaFinal = coluna+2;
	    	 
	    	 celula = row.createCell(coluna);
	    	 celula.setCellValue(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
		     celula.setCellStyle(estiloTituloTabela);
		     r = new Region(linha, (short)coluna, linha, (short)colunaFinal);
		     planilha.addMergedRegion(r);
		     
		     coluna = ++colunaFinal;
				
	    	 c.add(Calendar.MONTH, 1);
	     }
	     
	     row = planilha.createRow(++linha);
	     coluna = 2;
	     
	     c.setTime(dataInicioReal);
	     while(c.getTime().compareTo(dataFimReal)<=0)
	     {
	    	 celula = row.createCell(++coluna);
	    	 if(secao.equals(""))
	    		 celula.setCellValue("Sección");
	    	 else if(modalidade.equals(""))
	    		 celula.setCellValue("Modalidad");
		     celula.setCellStyle(estiloTituloTabela);
		     
	    	 celula = row.createCell(++coluna);
	    	 celula.setCellValue("PF");
		     celula.setCellStyle(estiloTituloTabela);
		     
		     celula = row.createCell(++coluna);
	    	 celula.setCellValue("PJ");
		     celula.setCellStyle(estiloTituloTabela);
		     
	    	 c.add(Calendar.MONTH, 1);
	     }
	     
	     Map<String,Integer> pfMap;
	     Map<String,Integer> pjMap;
	     int qtdePf, qtdePj;
	     Map<String, Integer> totais = new TreeMap<String, Integer>();
	     Aseguradora aseguradora2;
	     int ultimoDiaMes,qtde;
	     String mesAno,keyPF,keyPJ, qtdeStr;
	     Date dataFim2;
	     int cont = 0, cont2 = 0;
	     
	     Collection<String> planosCol;
	     
	     linha++;
	     
	     for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
	     {
			aseguradora2 = i.next();
			
			String nomeAseg = aseguradora2.obterNome();
			planosCol = new ArrayList<String>();
			
			System.out.println(nomeAseg);
			cont = 0;
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(nomeAseg);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linha, (short)2);
			planilha.addMergedRegion(r);
			
			pfMap = new TreeMap<String, Integer>();
			pjMap = new TreeMap<String, Integer>();
			
			if(!secao.equals(""))
				planosCol.add(secao);
			else
				planosCol = aseguradora2.obterPlanosPorTipoPessoa(dataInicioReal, dataFimReal, ramo, secao, modalidade);
			
			if(planosCol.size() > 0)
			{
				for(Iterator<String> j = planosCol.iterator() ; j.hasNext() ; )
				{
					String planoCol = j.next();
					
					coluna = 2;
					qtdePf = 0;
					qtdePj = 0;
					
					if(cont > 0)
					{
						linha++;
						row = planilha.createRow(linha);
					}
					c.setTime(dataInicioReal);
					
					 while(c.getTime().compareTo(dataFimReal)<=0)
					 {
						 ultimoDiaMes = c.getActualMaximum(Calendar.DAY_OF_MONTH);
						 mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
						 dataFim2 = new SimpleDateFormat("dd/MM/yyyy").parse(ultimoDiaMes + "/"+mesAno);
							
						 keyPF = "pf"+mesAno;
						 keyPJ = "pj"+mesAno;
						 
						 celula = row.createCell(++coluna);
						 celula.setCellValue(planoCol);
						 celula.setCellStyle(estiloTextoE);
						 
						 qtdeStr = aseguradora2.obterQtdePorTipoPessoa2(c.getTime(), dataFim2, ramo, planoCol, modalidade);
						 if(qtdeStr.length() > 0)
						 {
							 String[] qtdeV = qtdeStr.split("@");
							 
							 int qtdeV2 = qtdeV.length;
							 if(qtdeV2 == 1)
								 qtdePf = Integer.valueOf(qtdeV[0]);
							 else if(qtdeV2 == 2)
							 {
								 qtdePf = Integer.valueOf(qtdeV[0]);
								 qtdePj = Integer.valueOf(qtdeV[1]);
							 }
						 }
						 
						 celula = row.createCell(++coluna);
						 celula.setCellValue(qtdePf);
						 celula.setCellStyle(estiloTexto);
						 
						 celula = row.createCell(++coluna);
						 celula.setCellValue(qtdePj);
						 celula.setCellStyle(estiloTexto);
						 
						 c.add(Calendar.MONTH, 1);
						 
						 if(totais.containsKey(keyPF))
						 {
							 qtde = totais.get(keyPF);
							 qtde+=qtdePf;
								
							 totais.put(keyPF, qtde);
						 }
						 else
							 totais.put(keyPF, qtdePf);
							
						 if(totais.containsKey(keyPJ))
						 {
							 qtde = totais.get(keyPJ);
							 qtde+=qtdePj;
								
							 totais.put(keyPJ, qtde);
						 }
						 else
							 totais.put(keyPJ, qtdePj);
						 
						 if(pfMap.containsKey(keyPF))
						 {
							 qtde = pfMap.get(keyPF);
							 qtde+=qtdePf;
								
							 pfMap.put(keyPF, qtde);
						 }
						 else
							 pfMap.put(keyPF, qtdePf);
							
						 if(pjMap.containsKey(keyPJ))
						 {
							 qtde = pjMap.get(keyPJ);
							 qtde+=qtdePj;
								
							 pjMap.put(keyPJ, qtde);
						 }
						 else
							 pjMap.put(keyPJ, qtdePj);
					 }
					 
					 cont++;
				}
				
				linha++;
				row = planilha.createRow(linha);
				celula = row.createCell(0);
				celula.setCellValue("TOTAL");
				celula.setCellStyle(estiloTextoN);
			    r = new Region(linha, (short)0, linha, (short)2);
			    planilha.addMergedRegion(r);
			    
			    coluna = 3;
			    c.setTime(dataInicioReal);
				while(c.getTime().compareTo(dataFimReal)<=0)
				{
					ultimoDiaMes = c.getActualMaximum(Calendar.DAY_OF_MONTH);
					mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					dataFim2 = new SimpleDateFormat("dd/MM/yyyy").parse(ultimoDiaMes + "/"+mesAno);
						
					keyPF = "pf"+mesAno;
					keyPJ = "pj"+mesAno;
				    
				    int totalPF = 0; 
					int totalPJ = 0;
					
					if(pfMap.containsKey(keyPF))
						totalPF = pfMap.get(keyPF);
					if(pjMap.containsKey(keyPJ))
						totalPJ = pjMap.get(keyPJ);
					
					celula = row.createCell(++coluna);
			    	celula.setCellValue(totalPF);
				    celula.setCellStyle(estiloTextoN);
				     
				    celula = row.createCell(++coluna);
			    	celula.setCellValue(totalPJ);
				    celula.setCellStyle(estiloTextoN);
				    
				    coluna++;
					
					c.add(Calendar.MONTH, 1);
				}
		     }
			linha++;
			/*cont2++;
			if(cont2 > 2)
				break;*/
	     }
	     
	    linha++;
     	c.setTime(dataInicioReal);
		row = planilha.createRow(linha);
		celula = row.createCell(0);
		celula.setCellValue("TOTAL GENERAL");
		celula.setCellStyle(estiloTextoN);
	    r = new Region(linha, (short)0, linha, (short)2);
	    planilha.addMergedRegion(r);
	    coluna = 3;
	    
		while(c.getTime().compareTo(dataFimReal)<=0)
		{
			ultimoDiaMes = c.getActualMaximum(Calendar.DAY_OF_MONTH);
			mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
			dataFim2 = new SimpleDateFormat("dd/MM/yyyy").parse(ultimoDiaMes + "/"+mesAno);
				
			keyPF = "pf"+mesAno;
			keyPJ = "pj"+mesAno;
		    
		    int totalPF = 0; 
			int totalPJ = 0;
			
			if(totais.containsKey(keyPF))
				totalPF = totais.get(keyPF);
			if(totais.containsKey(keyPJ))
				totalPJ = totais.get(keyPJ);
			
			celula = row.createCell(++coluna);
	    	celula.setCellValue(totalPF);
		    celula.setCellStyle(estiloTextoN);
		     
		    celula = row.createCell(++coluna);
	    	celula.setCellValue(totalPJ);
		    celula.setCellStyle(estiloTextoN);
		    
		    coluna++;
			
			c.add(Calendar.MONTH, 1);
		}
     
	     wb.write(stream);

	     stream.flush();

	     stream.close();
	}
}
