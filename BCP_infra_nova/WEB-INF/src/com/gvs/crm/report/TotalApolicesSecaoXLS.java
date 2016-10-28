package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;

import infra.config.InfraProperties;

public class TotalApolicesSecaoXLS extends Excel 
{
	/*private Map totalGeralA = new TreeMap();
	private Map totalGeralS = new TreeMap();*/
	
	public TotalApolicesSecaoXLS(Collection aseguradoras, Date mesAno, ApoliceHome home, AseguradoraHome aseguradoraHome, String textoUsuario, boolean admin) throws Exception
	{
		String ano = new SimpleDateFormat("yyyy").format(mesAno);
		String mes = new SimpleDateFormat("MM").format(mesAno);		
		
		Calendar c = Calendar.getInstance();
		c.setTime(mesAno);
		
		Date dataInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/" + mes + "/" + ano + " 00:00:00");
		Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + ano + " 23:59:59");
		
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
       
       HSSFCellStyle estiloData = wb.createCellStyle();
       estiloData.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
       
       HSSFCellStyle estiloTitulo = wb.createCellStyle();
       estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTitulo.setFont(fonteTitulo);
       
       HSSFCellStyle estiloTexto = wb.createCellStyle();
       estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTexto.setFont(fonteTexto);
       
       HSSFCellStyle estiloTextoN = wb.createCellStyle();
       estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTextoN.setFont(fonteTextoN);
       
       HSSFCellStyle estiloTextoCor = wb.createCellStyle();
       estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTextoCor.setFont(fonteTexto);
       estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
       HSSFCellStyle estiloTextoE = wb.createCellStyle();
       estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
       estiloTextoE.setFont(fonteTexto);
       
       HSSFCellStyle estiloTextoCorE = wb.createCellStyle();
       estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTextoCorE.setFont(fonteTexto);
       estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
       HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
       estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTituloTabela.setFont(fonteTituloTabela);
       estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
       estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
       String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
       
       InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
       byte [] bytes = IOUtils.toByteArray (is);
       int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
       is.close();
       
       HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
       anchoVivaBem.setAnchorType(3);  
       planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
       
       HSSFRow row = planilha.createRow(6);
       HSSFCell celula = row.createCell(0);
       
       celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
       celula.setCellStyle(estiloTitulo);
       
       Region r = new Region(6, (short)0, 6, (short)9);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(8);
       celula = row.createCell(0);
       celula.setCellValue("Total de pólizas emitidas y siniestros ocurridos en: " + mes+"/"+ano);
       celula.setCellStyle(estiloTitulo);
       r = new Region(8, (short)0, 8, (short)9);
       planilha.addMergedRegion(r);
       
       int linha = 10;
       
       for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
       {
    	   Aseguradora aseg = (Aseguradora) i.next();
    	   
    	   row = planilha.createRow(linha);
           celula = row.createCell(0);
           
           celula.setCellValue(aseg.obterNome().toUpperCase());
           celula.setCellStyle(estiloTituloTabela);
           
           r = new Region(linha, (short)0, linha, (short)9);
           planilha.addMergedRegion(r);
    	   
    	   linha++;
    	   
    	   row = planilha.createRow(linha);
           celula = row.createCell(0);
           
           celula.setCellValue("SECCIÓN");
           celula.setCellStyle(estiloTextoCor);
           
           r = new Region(linha, (short)0, linha, (short)3);
           planilha.addMergedRegion(r);
    	   
           celula = row.createCell(4);
           celula.setCellValue("Cant. Pólizas emitidas");
           celula.setCellStyle(estiloTextoCor);
           
           r = new Region(linha, (short)4, linha, (short)6);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(7);
           celula.setCellValue("Cantidad de Siniestros");
           celula.setCellStyle(estiloTextoCor);
           
           r = new Region(linha, (short)7, linha, (short)9);
           planilha.addMergedRegion(r);
           
           linha++;
           
           Map informacoes = home.obterQtdeApolicesPeriodo(aseg,dataInicio,dataFim, admin);
           Map informacoes2 = home.obterQtdeSinistrosPeriodo(aseg, dataInicio, dataFim, admin);
           
           /*System.out.println("Qtde A: " + informacoes.size());
           System.out.println("Qtde S: " + informacoes2.size());*/
           
           int totalA = 0;
           int totalS = 0;
           
           for(Iterator j = informacoes.values().iterator() ; j.hasNext() ; )
           {
        	   String inf = (String) j.next();
        	   
        	   String[] infLinha = inf.split("_");
        	   
        	   String secao = infLinha[0];
        	   int qtde = Integer.parseInt(infLinha[1]);
        	   
        	   totalA+=qtde;
        	   
        	   row = planilha.createRow(linha);
               celula = row.createCell(0);
               
               celula.setCellValue(secao);
               celula.setCellStyle(estiloTextoE);
               
               r = new Region(linha, (short)0, linha, (short)3);
               planilha.addMergedRegion(r);
               
               celula = row.createCell(4);
               celula.setCellValue(qtde);
               celula.setCellStyle(estiloTexto);
               
               r = new Region(linha, (short)4, linha, (short)6);
               planilha.addMergedRegion(r);
               
               int qtdeS = 0;
               if(informacoes2.containsKey(secao))
               {
            	   String inf2 = informacoes2.get(secao).toString();
            	   
            	   String[] infLinha2 = inf2.split("_");
            	   
            	   qtdeS = Integer.parseInt(infLinha2[1]);
               }
               
               totalS+=qtdeS;
               
               celula = row.createCell(7);
               celula.setCellValue(qtdeS);
               celula.setCellStyle(estiloTexto);
               
               r = new Region(linha, (short)7, linha, (short)9);
               planilha.addMergedRegion(r);
               
               linha++;
           }
           
           row = planilha.createRow(linha);
           celula = row.createCell(0);
           celula.setCellValue("TOTAL");
           celula.setCellStyle(estiloTextoN);
           
           r = new Region(linha, (short)0, linha, (short)3);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(4);
           celula.setCellValue(totalA);
           celula.setCellStyle(estiloTextoN);
           
           r = new Region(linha, (short)4, linha, (short)6);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(7);
           celula.setCellValue(totalS);
           celula.setCellStyle(estiloTextoN);
           
           r = new Region(linha, (short)7, linha, (short)9);
           planilha.addMergedRegion(r);
           
           //PULA LINHA
           linha+=2;
           
           row = planilha.createRow(linha);
           celula = row.createCell(0);
           celula.setCellValue("");
           
           r = new Region(linha, (short)0, linha, (short)9);
           planilha.addMergedRegion(r);
       }
       
       linha++;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       
       celula.setCellValue("Total general de pólizas emitidas y siniestros ocurridos en: " + mes+"/"+ano);
       celula.setCellStyle(estiloTitulo);
       
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha++;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       
       celula.setCellValue("SECCIÓN");
       celula.setCellStyle(estiloTextoCor);
       
       r = new Region(linha, (short)0, linha, (short)3);
       planilha.addMergedRegion(r);
	   
       celula = row.createCell(4);
       celula.setCellValue("Cant. Pólizas emitidas");
       celula.setCellStyle(estiloTextoCor);
       
       r = new Region(linha, (short)4, linha, (short)6);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(7);
       celula.setCellValue("Cantidad de Siniestros");
       celula.setCellStyle(estiloTextoCor);
       
       r = new Region(linha, (short)7, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha++;
       
       Map<String, Double> mapAux = new TreeMap<String, Double>();
       
       Collection<String> planosApolicesAseguradora = aseguradoraHome.obterQtdeApolicesPorPeriodoTODAS(dataInicio, dataFim, false, admin);
       Collection<String> planosSinistrosAseguradora = aseguradoraHome.obterQtdeSinistrosPorPeriodoTODAS(dataInicio, dataFim, admin);
       
       //Map<String, Integer> planosApolicesAseguradoraRG001 = aseguradoraHome.obterQtdeApolicesPorPeriodoTODASRG001(dataInicio, dataFim);
       //Map<String, Integer> planosSinistrosAseguradoraRG001 = aseguradoraHome.obterQtdeSinistrosPorPeriodoTODASRG001(dataInicio, dataFim);
       
       for(Iterator<String> j = planosSinistrosAseguradora.iterator() ; j.hasNext() ; )
       {
    	   String dados = j.next();
    	   
    	   String[] dadosSujos = dados.split(";");
    	   
    	   String secao = dadosSujos[0];
    	   double qtde = Double.parseDouble(dadosSujos[1]);
    	   
    	   mapAux.put(secao, qtde);
       }
       
       int totalGA = 0;
       int totalGS = 0;
       
       for(Iterator<String> j = planosApolicesAseguradora.iterator() ; j.hasNext() ; )
       {
    	   String dados = j.next();
    	   
    	   String[] dadosSujos = dados.split(";");
    	   
    	   String secao = dadosSujos[0];
    	   double qtde = Double.parseDouble(dadosSujos[1]);
    	   double qtdeS = 0;
    	   if(mapAux.containsKey(secao))
    		   qtdeS = mapAux.get(secao);
    	   
    	   row = planilha.createRow(linha);
           celula = row.createCell(0);
           
           celula.setCellValue(secao);
           celula.setCellStyle(estiloTextoE);
           
           r = new Region(linha, (short)0, linha, (short)3);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(4);
           celula.setCellValue(qtde);
           celula.setCellStyle(estiloTexto);
           
           r = new Region(linha, (short)4, linha, (short)6);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(7);
           celula.setCellValue(qtdeS);
           celula.setCellStyle(estiloTexto);
           
           r = new Region(linha, (short)7, linha, (short)9);
           planilha.addMergedRegion(r);
           
           totalGA+=qtde;
           totalGS+=qtdeS;
           
           linha++;
       }
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("TOTAL GENERAL");
       celula.setCellStyle(estiloTextoN);
       
       r = new Region(linha, (short)0, linha, (short)3);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(4);
       celula.setCellValue(totalGA);
       celula.setCellStyle(estiloTextoN);
       
       r = new Region(linha, (short)4, linha, (short)6);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(7);
       celula.setCellValue(totalGS);
       celula.setCellStyle(estiloTextoN);
       
       r = new Region(linha, (short)7, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha+=2;
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue(textoUsuario);
       celula.setCellStyle(estiloTitulo);
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       wb.write(stream);

       stream.flush();

       stream.close();
	}
}
