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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Livro;
import com.gvs.crm.model.LivroHome;
import com.gvs.crm.model.SampleModelManager;
import com.gvs.crm.model.UploadedFile;
import com.gvs.crm.model.UploadedFileHome;
import com.gvs.crm.model.Uteis;

import infra.config.InfraProperties;

public class LivrosPendentesXLS extends Excel 
{
	private Uteis uteis = new Uteis();
	private int linha;
	
	public LivrosPendentesXLS(Aseguradora aseguradora, String tipo, Date dataInicio, Date dataFim, Collection<Aseguradora> aseguradoras, LivroHome livroHome, UploadedFileHome uploadedFileHome
			, String textoUsuario) throws Exception
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
      fonteTituloTabela.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
      
      HSSFFont fonteTexto = wb.createFont();
      fonteTexto.setFontHeightInPoints((short)8);
      fonteTexto.setFontName("Arial");
      
      HSSFFont fonteTextoN = wb.createFont();
      fonteTextoN.setFontHeightInPoints((short)8);
      fonteTextoN.setFontName("Arial");
      fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      
      HSSFCellStyle estiloTextoN = wb.createCellStyle();
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloData = wb.createCellStyle();
      estiloData.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
      
      HSSFCellStyle estiloTitulo = wb.createCellStyle();
      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTitulo.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTituloE = wb.createCellStyle();
      estiloTituloE.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTexto = wb.createCellStyle();
      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTexto.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoJ = wb.createCellStyle();
      estiloTextoJ.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
      estiloTextoJ.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoCor = wb.createCellStyle();
      estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoCor.setFont(fonteTexto);
      estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
      estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTextoE = wb.createCellStyle();
      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
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
      
      HSSFCellStyle estiloTituloTabelaE = wb.createCellStyle();
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
      
      HSSFRow row = planilha.createRow(0);
      HSSFCell celula = row.createCell(3);
      
      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
      celula.setCellStyle(estiloTituloE);
      Region r = new Region(0, (short)3, 0, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(1);
      celula = row.createCell(3);
   	  celula.setCellValue("Libros Electrónicos Pendientes");
      celula.setCellStyle(estiloTituloE);
      r = new Region(1, (short)3, 1, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(2);
      celula = row.createCell(3);
      
      if(aseguradora!=null)
    	  celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
      else
    	  celula.setCellValue("Aseguradora: Todas");
      celula.setCellStyle(estiloTituloE);
      r = new Region(2, (short)3, 2, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(3);
      
      if(!tipo.equals(""))
    	  celula.setCellValue("Tipo: " + tipo);
      else
    	  celula.setCellValue("Tipo: Todos");
      celula.setCellStyle(estiloTituloE);
      r = new Region(3, (short)3, 3, (short)20);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(4);
      celula = row.createCell(3);
      
   	  celula.setCellValue("Periodo: " + new SimpleDateFormat("MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTituloE);
      r = new Region(4, (short)3, 4, (short)20);
      planilha.addMergedRegion(r);
      
      linha = 6;
      
      Collection<String> mimeTypesPDF = uteis.obterMimeTypesPDF();
      Collection<String> mimeTypesWord = uteis.obterMimeTypesWord();
      Collection<String> mimeTypesExcel = uteis.obterMimeTypesExcel();
		
      Map<String,Map<String,String>> grupoLivros = uteis.obterGruposELivros();
      SampleModelManager mm2 = new SampleModelManager();
      UploadedFileHome home = (UploadedFileHome) mm2.getHome("UploadedFileHome");
		
      if(!tipo.equals(""))
      {
    	  for(String nomeGrupo : grupoLivros.keySet())
    	  {
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
      
      Calendar c =  Calendar.getInstance();
      
      SimpleDateFormat formatoData = new SimpleDateFormat("MM/yyyy");
      Livro livro,ultimoLivro; 
      Collection<UploadedFile> arquivos;
      long idPdf;
      long idWord;
      long idExcel;
      String mimeType;
      
      int contMes = 0;
      c.setTime(dataInicio);
      
      while(c.getTime().compareTo(dataFim) <=0)
      {
    	  contMes++;
    	  c.add(Calendar.MONTH, 1);
      }
      
      int tamanhoTabela = 2+(contMes*3);
		
      for(Aseguradora aseg : aseguradoras)
      {
    	  row = planilha.createRow(linha);
    	  celula = row.createCell(0);
		      
    	  celula.setCellValue(aseg.obterNome());
    	  celula.setCellStyle(estiloTituloTabelaE);
    	  r = new Region(linha, (short)0, linha, (short)tamanhoTabela);
    	  planilha.addMergedRegion(r);
    	  
    	  linha++;
    	  
    	  row = planilha.createRow(linha);
    	  celula = row.createCell(0);
		  
    	  celula.setCellValue("LIBROS ELECTRÓNICOS");
    	  celula.setCellStyle(estiloTituloTabela);
    	  
    	  celula = row.createCell(1);
    	  celula.setCellValue("FRECUENCIA");
    	  celula.setCellStyle(estiloTituloTabela);
    	  
	      c.setTime(dataInicio);
	      
	      int coluna = 1;
	      
	      while(c.getTime().compareTo(dataFim) <=0)
	      {
	    	  celula = row.createCell(++coluna);
	    	  celula.setCellValue("WORD " + formatoData.format(c.getTime()));
	    	  celula.setCellStyle(estiloTituloTabela);
	    	  
	    	  celula = row.createCell(++coluna);
	    	  celula.setCellValue("EXCEL " + formatoData.format(c.getTime()));
	    	  celula.setCellStyle(estiloTituloTabela);
	    	  
	    	  celula = row.createCell(++coluna);
	    	  celula.setCellValue("PDF " + formatoData.format(c.getTime()));
	    	  celula.setCellStyle(estiloTituloTabela);
	    	  
	    	  c.add(Calendar.MONTH, 1);
	      }
	      
	      celula = row.createCell(++coluna);
    	  celula.setCellValue("ÚLTIMO ENVÍO");
    	  celula.setCellStyle(estiloTituloTabela);
    	  
    	  for(String nomeGrupo : grupoLivros.keySet())
    	  {
    		  row = planilha.createRow(++linha);
    		  celula = row.createCell(0);
	    	  celula.setCellValue(nomeGrupo);
	    	  celula.setCellStyle(estiloTextoN);
	    	  r = new Region(linha, (short)0, linha, (short)tamanhoTabela);
	    	  planilha.addMergedRegion(r);
	    	  
	    	  Map<String,String> nomeLivros = grupoLivros.get(nomeGrupo);
	    	  
	    	  for(String nomeLivro : nomeLivros.values())
	    	  {
	    		  row = planilha.createRow(++linha);
	    		  celula = row.createCell(0);
	    		  celula.setCellValue("    "+nomeLivro);
	    		  celula.setCellStyle(estiloTextoE);
	    		  
	    		  celula = row.createCell(1);
	    		  celula.setCellValue(uteis.obterFrequenciaLivro(nomeLivro));
	    		  celula.setCellStyle(estiloTexto);
	        	  
	        	  c.setTime(dataInicio);
	        	  coluna = 1;
	        	  while(c.getTime().compareTo(dataFim) <=0)
	        	  {
	        		  int mes = Integer.valueOf(new SimpleDateFormat("MM").format(c.getTime()));
	        		  int ano = Integer.valueOf(new SimpleDateFormat("yyyy").format(c.getTime()));
						
	        		  livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
						
	        		  if(livro!=null)
	        		  {
	        			  arquivos = home.getAllUploadedFiles(livro);
							
	        			  idPdf = 0;
	        			  idWord = 0;
	        			  idExcel = 0;
							
	        			  for(UploadedFile arquivo : arquivos)
	        			  {
	        				  mimeType = arquivo.getType();
								
	        				  if(mimeTypesPDF.contains(mimeType))
	        					  idPdf = arquivo.getId();
	        				  else if(mimeTypesWord.contains(mimeType))
	        					  idWord = arquivo.getId();
	        				  else if(mimeTypesExcel.contains(mimeType))
	        					  idExcel = arquivo.getId();
	        			  }
							
	        			  celula = row.createCell(++coluna);
        		    	  celula.setCellStyle(estiloTexto);
	        			  if(idWord > 0)
	        		    	  celula.setCellValue("Sí");
	        			  else
	        				  celula.setCellValue("No");
						
	        			  celula = row.createCell(++coluna);
        		    	  celula.setCellStyle(estiloTexto);
	        			  if(idExcel > 0)
	        				  celula.setCellValue("Sí");
	        			  else
	        				  celula.setCellValue("No");
							
	        			  celula = row.createCell(++coluna);
        		    	  celula.setCellStyle(estiloTexto);
	        			  if(idPdf > 0)
	        				  celula.setCellValue("Sí");
	        			  else
	        				  celula.setCellValue("No");
	        		  }
	        		  else
	        		  {
	        			  celula = row.createCell(++coluna);
        		    	  celula.setCellStyle(estiloTexto);
        		    	  celula.setCellValue("No");
        		    	  
        		    	  celula = row.createCell(++coluna);
        		    	  celula.setCellStyle(estiloTexto);
        		    	  celula.setCellValue("No");
        		    	  
        		    	  celula = row.createCell(++coluna);
        		    	  celula.setCellStyle(estiloTexto);
        		    	  celula.setCellValue("No");
	        		  }
						
	        		  c.add(Calendar.MONTH, 1);
	        	  }
	        	  
	        	  ultimoLivro = livroHome.obterUltimoLivro(aseg, nomeLivro);
	        	  celula = row.createCell(++coluna);
		    	  celula.setCellStyle(estiloTexto);
	        	  if(ultimoLivro!=null)
	        	  {
	        		  String mesStr = ultimoLivro.obterMes()+"";
	        		  if(mesStr.length() == 1)
	        			  mesStr = "0"+mesStr;
						
	        		  celula.setCellValue(mesStr + "/" + ultimoLivro.obterAno());
	        	  }
	        	  else
	        		  celula.setCellValue("");
	    	  }
	    	  
	    	  row = planilha.createRow(++linha);
    		  celula = row.createCell(0);
    		  celula.setCellValue("");
    		  r = new Region(linha, (short)0, linha, (short)tamanhoTabela);
	    	  planilha.addMergedRegion(r);
    		  
    	  }
    	  
	      
	      linha+=2;
      }
      
      
     /* Collection<String> mimeTypesPDF = uteis.obterMimeTypesPDF();
      Collection<String> mimeTypesWord = uteis.obterMimeTypesWord();
      Collection<String> mimeTypesExcel = uteis.obterMimeTypesExcel();
      Collection<String> nomeLivros = new ArrayList<String>();
      
      if(tipo.equals(""))
    	  nomeLivros = uteis.obterNomeLivros();
      else
    	  nomeLivros.add(tipo);
      
      for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
      {
    	  Aseguradora aseg = i.next();
			
    	  row = planilha.createRow(linha);
    	  celula = row.createCell(0);
		      
    	  celula.setCellValue(aseg.obterNome());
    	  celula.setCellStyle(estiloTitulo);
    	  r = new Region(linha, (short)0, linha, (short)13);
    	  planilha.addMergedRegion(r);
    	  
    	  Calendar c = Calendar.getInstance();
    	  c.setTime(dataInicio);
			
    	  
    	  linha++;
    	  
    	  while(c.getTime().compareTo(dataFim) <=0)
    	  {
    		  int mes = Integer.parseInt(new SimpleDateFormat("MM").format(c.getTime()));
    		  int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getTime()));
    		  
    		  row = planilha.createRow(linha);
        	  celula = row.createCell(0);
    		      
        	  celula.setCellValue(new SimpleDateFormat("MM/yyyy").format(c.getTime()));
        	  celula.setCellStyle(estiloTituloE);
        	  r = new Region(linha, (short)0, linha, (short)13);
        	  planilha.addMergedRegion(r);
        	  
        	  linha++;
        	  
        	  this.montaCabecalho(planilha, nomeLivros, estiloTituloTabela);
        	  
        	  linha++;
        	  row = planilha.createRow(linha);
        	  int colunaInicial = 0;
    		  for(Iterator<String> j = nomeLivros.iterator() ; j.hasNext() ; )
    		  {
    			  String nomeLivro = j.next();
					
    			  Livro livro = livroHome.obterLivro(aseg, nomeLivro, mes, ano);
					
    			  if(livro == null)
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
    			  else
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
    		  }
    		  
    		  linha+=2;
    		  c.add(Calendar.MONTH, 1);
    	  }
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
	
	public void montaCabecalho(HSSFSheet planilha, Collection<String> nomeLivros, HSSFCellStyle estiloTituloTabela)
	{
		int colunaInicial = 0;
		int colunaFinal = 2;
		HSSFRow row = planilha.createRow(linha);
		for(Iterator<String> i = nomeLivros.iterator() ; i.hasNext() ; )
		{
			String nomeLivro = i.next();
			HSSFCell celula = row.createCell(colunaInicial);
  		  
			celula.setCellValue(uteis.obterNomeLivroAreviado(nomeLivro));
			celula.setCellStyle(estiloTituloTabela);
			Region r = new Region(linha, (short)colunaInicial, linha, (short)colunaFinal);
			planilha.addMergedRegion(r);
  	      
  	      	colunaInicial+=3;
  	      	colunaFinal+=3;
		}
      
		linha++;
      
		row = planilha.createRow(linha);
		HSSFCell celula = row.createCell(0);
      
		colunaInicial = 0;
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
	}
}
