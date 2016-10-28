package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Uteis;

import infra.config.InfraProperties;
import infra.view.Link;

public class LivrosXLS extends Excel 
{
	public LivrosXLS(Aseguradora aseguradora, String tipo, int mes, int ano, AseguradoraHome aseguradoraHome, LivroHome livroHome, UploadedFileHome uploadedFileHome, String textoUsuario) throws Exception
	{
		Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		if(aseguradora == null)
			aseguradoras = aseguradoraHome.obterAseguradoras();
		else
			aseguradoras.add(aseguradora);
		
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
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoJ = wb.createCellStyle();
      estiloTextoJ.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
      estiloTextoJ.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoCor = wb.createCellStyle();
      estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoCor.setFont(fonteTexto);
      estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
      estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTextoE = wb.createCellStyle();
      estiloTextoE.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoCorE = wb.createCellStyle();
      estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
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
      
      HSSFRow row = planilha.createRow(0);
      HSSFCell celula = row.createCell(3);
      
      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
      celula.setCellStyle(estiloTitulo);
      Region r = new Region(0, (short)3, 0, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(1);
      celula = row.createCell(3);
   	  celula.setCellValue("Libros Electrónicos");
      celula.setCellStyle(estiloTitulo);
      r = new Region(1, (short)3, 1, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(2);
      celula = row.createCell(3);
      
      if(aseguradora!=null)
    	  celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
      else
    	  celula.setCellValue("Aseguradora: Todas");
      celula.setCellStyle(estiloTitulo);
      r = new Region(2, (short)3, 2, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(3);
      
      if(!tipo.equals(""))
    	  celula.setCellValue("Tipo: " + tipo);
      else
    	  celula.setCellValue("Tipo: Todos");
      celula.setCellStyle(estiloTitulo);
      r = new Region(3, (short)3, 3, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(4);
      celula = row.createCell(3);
      
   	  celula.setCellValue("Mes: " + mes + " Año: " + ano);
      celula.setCellStyle(estiloTitulo);
      r = new Region(4, (short)3, 4, (short)20);
      planilha.addMergedRegion(r);
      
      int linha = 6;
      
      Uteis uteis = new Uteis();
      Collection<String> mimeTypesPDF = uteis.obterMimeTypesPDF();
      Collection<String> mimeTypesWord = uteis.obterMimeTypesWord();
      Collection<String> mimeTypesExcel = uteis.obterMimeTypesExcel();
      Map<String,Map<String,String>> grupoLivros = uteis.obterGruposELivros();
		
      if(!tipo.equals(""))
      {
    	  for(Iterator<String> i = grupoLivros.keySet().iterator() ; i.hasNext() ; )
    	  {
    		  String nomeGrupo = i.next();
				
    		  if(grupoLivros.get(nomeGrupo).containsKey(tipo))
    		  {
    			  String nomeLivro = grupoLivros.get(nomeGrupo).get(tipo);
					
    			  Map<String,String> livros = new TreeMap<String, String>();
    			  livros.put(nomeLivro, nomeLivro);
					
    			  grupoLivros.clear();
    			  grupoLivros.put(nomeGrupo, livros);
    			  break;
    		  }
    	  }
      }
      
      Link link, link2;
      Livro livro,ultimoLivro; 
      Collection arquivos;
      long idPdf;
      long idWord;
      long idExcel;
      UploadedFile arquivo;
      String mimeType;
      
      for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
      {
    	  Aseguradora aseg = i.next();
    	  
    	  row = planilha.createRow(linha);
          celula = row.createCell(0);
       	  celula.setCellValue(aseg.obterNome());
          celula.setCellStyle(estiloTituloTabela);
          r = new Region(linha, (short)0, linha, (short)5);
          planilha.addMergedRegion(r);
          
          linha++;
          
          row = planilha.createRow(linha);
          celula = row.createCell(0);
       	  celula.setCellValue("LIBROS ELECTRÓNICOS");
          celula.setCellStyle(estiloTituloTabela);
          
          celula = row.createCell(1);
       	  celula.setCellValue("FRECUENCIA");
          celula.setCellStyle(estiloTituloTabela);
          
          celula = row.createCell(2);
       	  celula.setCellValue("WORD");
          celula.setCellStyle(estiloTituloTabela);
          
          celula = row.createCell(3);
       	  celula.setCellValue("EXCEL");
          celula.setCellStyle(estiloTituloTabela);
          
          celula = row.createCell(4);
       	  celula.setCellValue("PDF");
          celula.setCellStyle(estiloTituloTabela);
          
          celula = row.createCell(5);
       	  celula.setCellValue("ÚLTIMO ENVÍO");
          celula.setCellStyle(estiloTituloTabela);
          
          linha++;
          
          for(Iterator<String> w = grupoLivros.keySet().iterator() ; w.hasNext() ; )
          {
        	  String nomeGrupo = w.next();
        	  
        	  row = planilha.createRow(linha);
              celula = row.createCell(0);
           	  celula.setCellValue(nomeGrupo);
              celula.setCellStyle(estiloTextoN);
              r = new Region(linha, (short)0, linha, (short)5);
              planilha.addMergedRegion(r);
              
              linha++;
              
              Map<String,String> nomeLivros = grupoLivros.get(nomeGrupo);
              
              for(Iterator<String> j = nomeLivros.values().iterator() ; j.hasNext() ; )
              {
            	  String nomeLivro = j.next();
            	  
            	  row = planilha.createRow(linha);
                  celula = row.createCell(0);
               	  celula.setCellValue("    " + nomeLivro);
                  celula.setCellStyle(estiloTextoE);
                  
                  celula = row.createCell(1);
               	  celula.setCellValue(uteis.obterFrequenciaLivro(nomeLivro));
                  celula.setCellStyle(estiloTexto);
            	  
                  livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
                  
                  if(livro!=null)
                  {
                	  arquivos = uploadedFileHome.getAllUploadedFiles(livro);
						
                	  idPdf = 0;
                	  idWord = 0;
                	  idExcel = 0;
						
                	  for(Iterator k = arquivos.iterator() ; k.hasNext() ; )
                	  {
                		  arquivo = (UploadedFile) k.next();
							
                		  mimeType = arquivo.getType();
							
                		  if(mimeTypesPDF.contains(mimeType))
                			  idPdf = arquivo.getId();
                		  else if(mimeTypesWord.contains(mimeType))
                			  idWord = arquivo.getId();
                		  else if(mimeTypesExcel.contains(mimeType))
                			  idExcel = arquivo.getId();
                	  }
                	  
                	  celula = row.createCell(2);
                	  celula.setCellStyle(estiloTexto);
                	  if(idWord > 0)
                		  celula.setCellValue("Sí");
                	  else
                		  celula.setCellValue("No");
                	  
                	  celula = row.createCell(3);
                	  celula.setCellStyle(estiloTexto);
                	  if(idExcel > 0)
                		  celula.setCellValue("Sí");
                	  else
                		  celula.setCellValue("No");
                	  
                	  celula = row.createCell(4);
                	  celula.setCellStyle(estiloTexto);
                	  if(idPdf > 0)
                		  celula.setCellValue("Sí");
                	  else
                		  celula.setCellValue("No");
                  }
                  else
                  {
                	  celula = row.createCell(2);
                	  celula.setCellStyle(estiloTexto);
               		  celula.setCellValue("No");
                	  
                	  celula = row.createCell(3);
                	  celula.setCellStyle(estiloTexto);
               		  celula.setCellValue("No");
                	  
                	  celula = row.createCell(4);
                	  celula.setCellStyle(estiloTexto);
               		  celula.setCellValue("No");
                  }
                  
                  ultimoLivro = livroHome.obterUltimoLivro(aseg, nomeLivro);
                  celula = row.createCell(5);
                  celula.setCellStyle(estiloTexto);
                  if(ultimoLivro!=null)
                  {
                	  String mesStr = ultimoLivro.obterMes()+"";
                	  if(mesStr.length() == 1)
                		  mesStr = "0"+mesStr;
                	  
                	  celula.setCellValue("Ultimo: " + mesStr + "/" + ultimoLivro.obterAno());
                  }
                  else
                	  celula.setCellValue("");
                  
                  linha++;
              }
              linha++;
          }
      }
			
      
     /* row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue("ASEGURADORAS");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linha, (short)0, linha, (short)1);
      planilha.addMergedRegion(r);
      
      int colunaInicial = 2;
      int colunaFinal = 4;
      for(Iterator<String> i = nomeLivros.iterator() ; i.hasNext() ; )
      {
    	  String nomeLivro = i.next();
  		  celula = row.createCell(colunaInicial);
  		  
  		  celula.setCellValue(uteis.obterNomeLivroAreviado(nomeLivro));
  	      celula.setCellStyle(estiloTituloTabela);
  	      r = new Region(linha, (short)colunaInicial, linha, (short)colunaFinal);
  	      planilha.addMergedRegion(r);
  	      
  	      colunaInicial+=3;
  	      colunaFinal+=3;
      }
      
      linha++;
      
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue("");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(linha, (short)0, linha, (short)1);
      planilha.addMergedRegion(r);
      
      colunaInicial = 2;
      for(int i = 0 ; i < nomeLivros.size() ; i++)
      {
    	  for(int j = 1 ; j <=3 ; j++)
    	  {
	    	  celula = row.createCell(colunaInicial);
	  		  
	  		  if(j == 1)
	  			  celula.setCellValue("W");
	  		  else if(j == 2)
	  			celula.setCellValue("E");
	  		  else if(j == 3)
	  			celula.setCellValue("P");
	  		  
	  	      celula.setCellStyle(estiloTituloTabela);
	  	      colunaInicial++;
    	  }
      }
      
      linha++;
      
      for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
      {
    	  Aseguradora aseg = i.next();
			
    	  row = planilha.createRow(linha);
    	  celula = row.createCell(0);
    	  celula.setCellValue(aseg.obterNome());
		  celula.setCellStyle(estiloTextoE);
		  r = new Region(linha, (short)0, linha, (short)1);
		  planilha.addMergedRegion(r);
		  
		  colunaInicial = 2;
		  
		  for(Iterator<String> j = nomeLivros.iterator() ; j.hasNext() ; )
		  {
			  String nomeLivro = j.next();
				
			  Livro livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
			  
			  if(livro!=null)
			  {
				  Collection arquivos = uploadedFileHome.getAllUploadedFiles(livro);
				  
				  boolean word = false;
				  boolean excel = false;
				  boolean pdf = false;
				  
				  for(Iterator k = arquivos.iterator() ; k.hasNext() ; )
				  {
					  UploadedFile arquivo = (UploadedFile) k.next();
						
					  String mimeType = arquivo.getType();
						
					  if(mimeTypesPDF.contains(mimeType))
						 pdf = true;
					  else if(mimeTypesWord.contains(mimeType))
						  word = true;
					  else if(mimeTypesExcel.contains(mimeType))
						  excel = true;
				  }
				  
				  celula = row.createCell(colunaInicial);
				  if(word)
					  celula.setCellValue("S");
				  else
					  celula.setCellValue("N");
				  celula.setCellStyle(estiloTexto);
				  
				  colunaInicial++;
				  celula = row.createCell(colunaInicial);
		    	  if(excel)
		    		  celula.setCellValue("S");
		    	  else
		    		  celula.setCellValue("N");
				  celula.setCellStyle(estiloTexto);
				  
				  colunaInicial++;
				  celula = row.createCell(colunaInicial);
		    	  if(pdf)
		    		  celula.setCellValue("S");
		    	  else
		    		  celula.setCellValue("N");
				  celula.setCellStyle(estiloTexto);
				
				  colunaInicial++;
			  }
			  else
			  {
				  celula = row.createCell(colunaInicial);
		    	  celula.setCellValue("N");
				  celula.setCellStyle(estiloTexto);
				  
				  colunaInicial++;
				  celula = row.createCell(colunaInicial);
		    	  celula.setCellValue("N");
				  celula.setCellStyle(estiloTexto);
				  
				  colunaInicial++;
				  celula = row.createCell(colunaInicial);
		    	  celula.setCellValue("N");
				  celula.setCellStyle(estiloTexto);
				  
				  colunaInicial++;
			  }
		  }
		  
		  linha++;
		  colunaInicial = 2;
	      colunaFinal = 4;
		  
	      row = planilha.createRow(linha);
	      for(Iterator<String> j = nomeLivros.iterator() ; j.hasNext() ; )
		  {
			  String nomeLivro = j.next();
			  
			  celula = row.createCell(colunaInicial);
			  Livro ultimoLivro = livroHome.obterUltimoLivro(aseg, nomeLivro);
			  
			  if(ultimoLivro!=null)
		    	  celula.setCellValue("Ultimo: " + ultimoLivro.obterMes() + "/" + ultimoLivro.obterAno());
			  else
				  celula.setCellValue("Ultimo: xx/xxxx");
			  
			  celula.setCellStyle(estiloTexto);
			  r = new Region(linha, (short)colunaInicial, linha, (short)colunaFinal);
			  planilha.addMergedRegion(r);
			  
			  colunaInicial+=3;
	  	      colunaFinal+=3;
		  }
		  
		  linha++;
      }*/
      
      linha++;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue(textoUsuario);
      celula.setCellStyle(estiloTitulo);
      r = new Region(linha, (short)0, linha, (short)13);
      planilha.addMergedRegion(r);
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}