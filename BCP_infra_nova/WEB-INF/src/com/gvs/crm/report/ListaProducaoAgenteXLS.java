package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
import com.gvs.crm.model.AuxiliarSeguro;

import infra.config.InfraProperties;

public class ListaProducaoAgenteXLS extends Excel
{
	public ListaProducaoAgenteXLS(AuxiliarSeguro auxiliar,Date dataInicio, Date dataFim, Collection<Aseguradora> aseguradoras, boolean auxiliarSeguro, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

      HSSFSheet planilha = wb.createSheet("Planilha");
      
      HSSFFont fonteTitulo = wb.createFont();
      fonteTitulo.setFontHeightInPoints((short)10);
      fonteTitulo.setFontName("Arial");
      fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      
      HSSFFont fonteTexto = wb.createFont();
      fonteTexto.setFontHeightInPoints((short)9);
      fonteTexto.setFontName("Arial");
      
      HSSFFont fonteTextoN = wb.createFont();
      fonteTextoN.setFontHeightInPoints((short)9);
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
      
      HSSFCellStyle estiloTextoN_D = wb.createCellStyle();
      estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      estiloTextoN_D.setFont(fonteTextoN);
      
      HSSFFont fonteTituloTabela = wb.createFont();
      fonteTituloTabela.setFontHeightInPoints((short)10);
      fonteTituloTabela.setFontName("Arial");
      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
      
      HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
      estiloTituloTabelaC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTituloTabelaC.setFont(fonteTituloTabela);
      estiloTituloTabelaC.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabelaC.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      estiloTituloTabela.setFont(fonteTituloTabela);
      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
      
      InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
      byte [] bytes = IOUtils.toByteArray (is);
      int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
      is.close();
      
      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 1, 0, (short)3 , 5);  
      anchoVivaBem.setAnchorType(3);  
      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
      
      HSSFRow row = planilha.createRow(1);
      HSSFCell celula = row.createCell(5);
      
      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
      celula.setCellStyle(estiloTitulo);
      Region r = new Region(1, (short)5, 1, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(2);
      celula = row.createCell(5);
      if(auxiliarSeguro)
    	  celula.setCellValue("Lista de productividade Agente Seguro");
      else
    	  celula.setCellValue("Lista de productividade Corredor de Seguros");
      celula.setCellStyle(estiloTitulo);
      r = new Region(2, (short)5, 2, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
   	  celula.setCellValue("Agente: " + auxiliar.obterNome());
      celula.setCellStyle(estiloTitulo);
      r = new Region(3, (short)5, 3, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(4);
      celula = row.createCell(5);
   	  celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTitulo);
      r = new Region(4, (short)5, 4, (short)11);
      planilha.addMergedRegion(r);
      
      int linha = 6;
      
      double totalGeralComissaoGs = 0;
      double totalGeralComissaoMe = 0;
      double totalGeralPrima = 0;
      double totalGeralPremio = 0;
      double totalGeralCapitalGs = 0;
      double totalGeralCapitalMe = 0;
      
      for (Iterator<Aseguradora> i = aseguradoras.iterator(); i.hasNext();)
      {
    	  	Aseguradora aseguradora = i.next();
			
			Calendar c = Calendar.getInstance();
			c.setTime(dataInicio);
			
			double totalComissaoGs = 0;
			double totalComissaoMe = 0;
			double totalPrima = 0;
			double totalPremio = 0;
			double totalCapitalGs = 0;
			double totalCapitalMe = 0;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
		   	celula.setCellValue(aseguradora.obterNome());
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)0, linha, (short)4);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(5);
		   	celula.setCellValue("Mes/Año");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)5, linha, (short)6);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(7);
		   	celula.setCellValue("Comissão Gs");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)7, linha, (short)8);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(9);
		   	celula.setCellValue("Comissão ME");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)9, linha, (short)10);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(11);
		   	celula.setCellValue("Prima");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)11, linha, (short)12);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(13);
		   	celula.setCellValue("Premio");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)13, linha, (short)14);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(15);
		   	celula.setCellValue("Capital  Gs");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)15, linha, (short)16);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(17);
		   	celula.setCellValue("Capital  ME");
		    celula.setCellStyle(estiloTituloTabelaC);
		    r = new Region(linha, (short)17, linha, (short)18);
		    planilha.addMergedRegion(r);
		    
		    linha++;
		    
			while(c.getTime().compareTo(dataFim)<=0)
			{
				double comissaoGs = 0;
				double comissaoMe = 0;
				double prima = 0;
				double premio = 0;
				double capitalGs = 0;
				double capitalMe = 0;
				
				String diaInicio = new Integer(c.getActualMinimum(Calendar.DAY_OF_MONTH)).toString();
				String diaFim = new Integer(c.getActualMaximum(Calendar.DAY_OF_MONTH)).toString();
				
				if(diaInicio.toString().length()==1)
					diaInicio = "0" + diaInicio;
				
				String mes = new SimpleDateFormat("MM/yyyy").format(c.getTime());
				Date dataInicio2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(diaInicio+"/"+mes +" 00:00:00");
				Date dataFim2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(diaFim+"/"+mes +" 23:59:59");
				
				//System.out.println(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataInicio2) + " - " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataFim2));
				
				Collection<String> dados = aseguradora.obterApolicesAcumuladas(auxiliar, dataInicio2, dataFim2, auxiliarSeguro);
				
				for(Iterator<String> j = dados.iterator() ; j.hasNext() ; )
				{
					String linhaSuja = j.next();
					
					String[] linhaT = linhaSuja.split(";");
					
					comissaoGs = Double.parseDouble(linhaT[0]);
					comissaoMe = Double.parseDouble(linhaT[1]);
					prima = Double.parseDouble(linhaT[2]);
					premio = Double.parseDouble(linhaT[3]);
					capitalGs = Double.parseDouble(linhaT[4]);
					capitalMe = Double.parseDouble(linhaT[5]);
					
					totalComissaoGs+=comissaoGs;
					totalComissaoMe+=comissaoMe;
					totalPrima+=prima;
					totalPremio+=premio;
					totalCapitalGs+=capitalGs;
					totalCapitalMe+=capitalMe;
					
					totalGeralComissaoGs+=comissaoGs;
					totalGeralComissaoMe+=comissaoMe;
					totalGeralPrima+=prima;
					totalGeralPremio+=premio;
					totalGeralCapitalGs+=capitalGs;
					totalGeralCapitalMe+=capitalMe;
				}
				
				row = planilha.createRow(linha);
				celula = row.createCell(5);
				celula.setCellValue(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
				celula.setCellStyle(estiloTexto);
				r = new Region(linha, (short)5, linha, (short)6);
				planilha.addMergedRegion(r);
				
				celula = row.createCell(7);
				celula.setCellValue(formataValor.format(comissaoGs));
				celula.setCellStyle(estiloTextoD);
				r = new Region(linha, (short)7, linha, (short)8);
				planilha.addMergedRegion(r);
				
				celula = row.createCell(9);
				celula.setCellValue(formataValor.format(comissaoMe));
				celula.setCellStyle(estiloTextoD);
				r = new Region(linha, (short)9, linha, (short)10);
				planilha.addMergedRegion(r);
				
				celula = row.createCell(11);
			   	celula.setCellValue(formataValor.format(prima));
			    celula.setCellStyle(estiloTextoD);
			    r = new Region(linha, (short)11, linha, (short)12);
			    planilha.addMergedRegion(r);
			    
			    celula = row.createCell(13);
			   	celula.setCellValue(formataValor.format(premio));
			    celula.setCellStyle(estiloTextoD);
			    r = new Region(linha, (short)13, linha, (short)14);
			    planilha.addMergedRegion(r);
			    
			    celula = row.createCell(15);
			   	celula.setCellValue(formataValor.format(capitalGs));
			    celula.setCellStyle(estiloTextoD);
			    r = new Region(linha, (short)15, linha, (short)16);
			    planilha.addMergedRegion(r);
			    
			    celula = row.createCell(17);
			   	celula.setCellValue(formataValor.format(capitalMe));
			    celula.setCellStyle(estiloTextoD);
			    r = new Region(linha, (short)17, linha, (short)18);
			    planilha.addMergedRegion(r);
			    
			    c.add(Calendar.MONTH, 1);
			    
			    linha++;
			}
			
			row = planilha.createRow(linha);
			celula = row.createCell(5);
			celula.setCellValue("TOTAL");
			celula.setCellStyle(estiloTextoN);
			r = new Region(linha, (short)5, linha, (short)6);
			planilha.addMergedRegion(r);
			
			celula = row.createCell(7);
			celula.setCellValue(formataValor.format(totalComissaoGs));
			celula.setCellStyle(estiloTextoN_D);
			r = new Region(linha, (short)7, linha, (short)8);
			planilha.addMergedRegion(r);
			
			celula = row.createCell(9);
			celula.setCellValue(formataValor.format(totalComissaoMe));
			celula.setCellStyle(estiloTextoN_D);
			r = new Region(linha, (short)9, linha, (short)10);
			planilha.addMergedRegion(r);
			
			celula = row.createCell(11);
		   	celula.setCellValue(formataValor.format(totalPrima));
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)11, linha, (short)12);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(13);
		   	celula.setCellValue(formataValor.format(totalPremio));
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)13, linha, (short)14);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(15);
		   	celula.setCellValue(formataValor.format(totalCapitalGs));
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)15, linha, (short)16);
		    planilha.addMergedRegion(r);
		    
		    celula = row.createCell(17);
		   	celula.setCellValue(formataValor.format(totalCapitalMe));
		    celula.setCellStyle(estiloTextoN_D);
		    r = new Region(linha, (short)17, linha, (short)18);
		    planilha.addMergedRegion(r);
		    
		    linha++;
      }
      
      row = planilha.createRow(linha);
      celula = row.createCell(5);
      celula.setCellValue("TOTAL GENERAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linha, (short)5, linha, (short)6);
      planilha.addMergedRegion(r);
		
      celula = row.createCell(7);
      celula.setCellValue(formataValor.format(totalGeralComissaoGs));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linha, (short)7, linha, (short)8);
      planilha.addMergedRegion(r);
		
      celula = row.createCell(9);
      celula.setCellValue(formataValor.format(totalGeralComissaoMe));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linha, (short)9, linha, (short)10);
      planilha.addMergedRegion(r);
		
      celula = row.createCell(11);
      celula.setCellValue(formataValor.format(totalGeralPrima));
	  celula.setCellStyle(estiloTextoN_D);
	  r = new Region(linha, (short)11, linha, (short)12);
	  planilha.addMergedRegion(r);
	    
	  celula = row.createCell(13);
	  celula.setCellValue(formataValor.format(totalGeralPremio));
	  celula.setCellStyle(estiloTextoN_D);
	  r = new Region(linha, (short)13, linha, (short)14);
	  planilha.addMergedRegion(r);
	    
	  celula = row.createCell(15);
	  celula.setCellValue(formataValor.format(totalGeralCapitalGs));
	  celula.setCellStyle(estiloTextoN_D);
	  r = new Region(linha, (short)15, linha, (short)16);
	  planilha.addMergedRegion(r);
	    
	  celula = row.createCell(17);
	  celula.setCellValue(formataValor.format(totalGeralCapitalMe));
	  celula.setCellStyle(estiloTextoN_D);
	  r = new Region(linha, (short)17, linha, (short)18);
	  planilha.addMergedRegion(r);
	  
	  linha+=2;
	  
	  row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue(textoUsuario);
      celula.setCellStyle(estiloTitulo);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}
