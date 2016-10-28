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

import com.gvs.crm.model.ApoliceHome;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;

import infra.config.InfraProperties;

public class TotalApolicesSecaoAnualXLS extends Excel
{
	private Map totalMes = new TreeMap();
	private Map<String,Double> primaGSMesMap = new TreeMap<String, Double>();
	private Map<String,Double> primaMeMesMap = new TreeMap<String, Double>();
	
	private Map<String,Double> montanteGSMesMap = new TreeMap<String, Double>();
	private Map<String,Double> montanteMeMesMap = new TreeMap<String, Double>();
	private int linha,coluna;
	private HSSFRow row;
	private HSSFCell celula;
	private Region r;
	private HSSFSheet planilha;
	private DecimalFormat format = new DecimalFormat("#,##0.00");
	private Date dataFim, dataInicioDate;
	private Calendar dataInicio;
	private HSSFCellStyle estiloTitulo, estiloTexto, estiloTextoD, estiloTextoN, estiloTextoND, estiloTextoN_E, estiloTextoCor, estiloTextoE, estiloTextoCorE, estiloTituloTabela;
	private boolean valores, admin, somenteTotais;
	private AseguradoraHome aseguradoraHome;
	private String secao, modalidade, ramo;
	private Map<String,String> planosaAeguradoraTotal;
	
	public TotalApolicesSecaoAnualXLS(Collection aseguradoras, Date mesAnoInicio, Date mesAnoFim, ApoliceHome home, AseguradoraHome aseguradoraHome, boolean valores, String textoUsuario, String secao, String modalidade, boolean admin, String ramo, boolean somenteTotais) throws Exception
	{
		this.valores = valores;
		this.aseguradoraHome = aseguradoraHome;
		this.secao = secao;
		this.modalidade = modalidade;
		this.ramo = ramo;
		this.planosaAeguradoraTotal = new TreeMap<String,String>();
		this.somenteTotais = somenteTotais;
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(mesAnoFim.getTime());
		
		String anoFim = new SimpleDateFormat("yyyy").format(c.getTime());
		String mesFim = new SimpleDateFormat("MM").format(c.getTime());		
		
		dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mesFim + "/" + anoFim + " 23:59:59");
		
		c.setTimeInMillis(mesAnoInicio.getTime());
		String anoInicio = new SimpleDateFormat("yyyy").format(c.getTime());
		String mesInicio = new SimpleDateFormat("MM").format(c.getTime());
		
		dataInicioDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMinimum(Calendar.DAY_OF_MONTH) + "/" + mesInicio + "/" + anoInicio + " 00:00:00");
		
		dataInicio = Calendar.getInstance();
		dataInicio.setTimeInMillis(dataInicioDate.getTime());

		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		 this.setCaminho(caminho);
		
	   HSSFWorkbook wb = new HSSFWorkbook();

       planilha = wb.createSheet("Planilha");
       
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
       
       estiloTitulo = wb.createCellStyle();
       estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTitulo.setFont(fonteTitulo);
       
       estiloTexto = wb.createCellStyle();
       estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTexto.setFont(fonteTexto);
       
       estiloTextoD = wb.createCellStyle();
       estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
       estiloTextoD.setFont(fonteTexto);
       
       estiloTextoN = wb.createCellStyle();
       estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTextoN.setFont(fonteTextoN);
       
       estiloTextoND = wb.createCellStyle();
       estiloTextoND.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
       estiloTextoND.setFont(fonteTextoN);
       
       estiloTextoN_E = wb.createCellStyle();
       estiloTextoN_E.setFont(fonteTextoN);
       
       estiloTextoCor = wb.createCellStyle();
       estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTextoCor.setFont(fonteTexto);
       estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
       estiloTextoE = wb.createCellStyle();
       estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
       estiloTextoE.setFont(fonteTexto);
       
       estiloTextoCorE = wb.createCellStyle();
       estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTextoCorE.setFont(fonteTexto);
       estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
       estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
       estiloTituloTabela = wb.createCellStyle();
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
       
       row = planilha.createRow(6);
       celula = row.createCell(0);
       
       celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
       celula.setCellStyle(estiloTitulo);
       
       r = new Region(6, (short)0, 6, (short)13);
       planilha.addMergedRegion(r);
       
       row = planilha.createRow(8);
       celula = row.createCell(0);
       
       celula.setCellValue("Total de pólizas emitidas y siniestros ocurridos de: " + new SimpleDateFormat("MM/yyyy").format(dataInicio.getTime()) + " hasta " + new SimpleDateFormat("MM/yyyy").format(dataFim));
       celula.setCellStyle(estiloTitulo);
       
       r = new Region(8, (short)0, 8, (short)13);
       planilha.addMergedRegion(r);
       
       linha = 10;
       
       if(!somenteTotais)
       {
	       for(Iterator i = aseguradoras.iterator() ; i.hasNext() ; )
	       {
	    	   Aseguradora aseg = (Aseguradora) i.next();
	    	   
	    	   this.montarResultado(aseg, "Apolice");
	    	   this.montarResultado(aseg, "Sinistro");
	       }
       }
       
       this.montarResultado(null, "ApoliceT");
	   this.montarResultado(null, "SinistroT");
       
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
	
	private void montarResultado(Aseguradora aseguradora, String tipo) throws Exception
    {
		Calendar c = Calendar.getInstance();
		
		this.totalMes.clear();
		this.primaGSMesMap.clear();
		this.primaMeMesMap.clear();
 	   
		if(tipo.equals("Apolice"))
		{
			row = planilha.createRow(linha);
	        celula = row.createCell(0);
	        
	        celula.setCellValue(aseguradora.obterNome().toUpperCase());
	        celula.setCellStyle(estiloTituloTabela);
	        
	        r = new Region(linha, (short)0, linha, (short)13);
	        planilha.addMergedRegion(r);
	        
	        linha++;
		}
		else if(tipo.equals("ApoliceT"))
		{
			String titulo = "Total general de pólizas emitidas de: " + new SimpleDateFormat("MM/yyyy").format(dataInicioDate) + " hasta " + new SimpleDateFormat("MM/yyyy").format(dataFim); 
		       
			row = planilha.createRow(linha);
	        celula = row.createCell(0);
			celula.setCellValue(titulo.toUpperCase());
			celula.setCellStyle(estiloTituloTabela);
	       
			r = new Region(linha, (short)0, linha, (short)13);
			planilha.addMergedRegion(r);
	       
			linha++;
		}
		else if(tipo.equals("SinistroT"))
		{
			String titulo = "Total general de siniestros ocurridos de: " + new SimpleDateFormat("MM/yyyy").format(dataInicioDate) + " hasta " + new SimpleDateFormat("MM/yyyy").format(dataFim); 
		       
			row = planilha.createRow(linha);
	        celula = row.createCell(0);
			celula.setCellValue(titulo.toUpperCase());
			celula.setCellStyle(estiloTituloTabela);
	       
			r = new Region(linha, (short)0, linha, (short)13);
			planilha.addMergedRegion(r);
	       
			linha++;
		}
		
        if(tipo.equals("Apolice"))
        {
        	row = planilha.createRow(linha);
            celula = row.createCell(0);
            
        	if(!valores)
	     	   celula.setCellValue("Cantidad de pólizas emitidas".toUpperCase());
	        else
	     	   celula.setCellValue("Valores de pólizas emitidas".toUpperCase());
	        
	        celula.setCellStyle(estiloTextoN_E);
	        r = new Region(linha, (short)0, linha, (short)13);
	        planilha.addMergedRegion(r);
	 	   
	 	   	linha++;
        }
        else if(tipo.equals("Sinistro"))
        {
        	row = planilha.createRow(linha);
            celula = row.createCell(0);
        	if(!valores)
        		celula.setCellValue("Cantidad de siniestros ocurridos".toUpperCase());
            else
           	   celula.setCellValue("Valores de siniestros ocurridos".toUpperCase());
        	  
        	celula.setCellStyle(estiloTextoN_E);
        	r = new Region(linha, (short)0, linha, (short)13);
            planilha.addMergedRegion(r);
     	   
     	   	linha++;
        }
 	   
 	   	row = planilha.createRow(linha);
        
 	   	celula = row.createCell(0);
        celula.setCellValue("SECCIÓN");
        celula.setCellStyle(estiloTextoCor);
        
        celula = row.createCell(1);
        celula.setCellValue("MODALIDAD");
        celula.setCellStyle(estiloTextoCor);
        
        coluna = 2;
        
        dataInicio.setTimeInMillis(dataInicioDate.getTime());
        
        while(dataInicio.getTime().compareTo(dataFim) < 0)
        {
        	String mesAnoCabecalho = new SimpleDateFormat("MM/yyyy").format(dataInicio.getTime());
    	   
        	celula = row.createCell(coluna);
        	celula.setCellValue(mesAnoCabecalho);
        	celula.setCellStyle(estiloTextoCor);
        	if(valores)
        	{
        		r = new Region(linha, (short)coluna, linha, (short)++coluna);
        		planilha.addMergedRegion(r);
        	}
           
        	dataInicio.add(Calendar.MONTH, 1);
           
        	coluna++;
        }
	       
        linha++;
       
        if(valores)
        {
        	dataInicio.setTimeInMillis(dataInicioDate.getTime());
    	   
        	coluna = 2;
    	   
        	row = planilha.createRow(linha);
        	while(dataInicio.getTime().compareTo(dataFim) < 0)
        	{
        		celula = row.createCell(coluna);
        		if(tipo.startsWith("Apolice"))
        		{
	        		celula.setCellValue("Prima Gs");
	        		celula.setCellStyle(estiloTextoCor);
		           
	        		celula = row.createCell(++coluna);
	        		celula.setCellValue("Prima Me");
	        		celula.setCellStyle(estiloTextoCor);
        		}
        		else if(tipo.startsWith("Sinistro"))
        		{
        			 celula = row.createCell(coluna);
        			 celula.setCellValue("Mto pagado Gs");
        			 celula.setCellStyle(estiloTextoCor);
      	           
        			 celula = row.createCell(++coluna);
        			 celula.setCellValue("Mto pagado Me");
        			 celula.setCellStyle(estiloTextoCor);
        		}
    		   
        		dataInicio.add(Calendar.MONTH, 1);
	           
        		coluna++;
        	}
    	   
        	linha++;
        }
	       
        Map<String,String> planosaAeguradora = new TreeMap<String,String>();
        
        if(!secao.equals("") && !modalidade.equals(""))
       		planosaAeguradora.put(secao+";"+modalidade,secao+";"+modalidade);
        else
        {
	        if(tipo.equals("Apolice"))
	        {
	        	planosaAeguradora = aseguradora.obterNomePlanosPeriodo(dataInicioDate, dataFim, secao, admin, ramo);
	        	this.planosaAeguradoraTotal.putAll(planosaAeguradora);
	        }
	        else if(tipo.equals("Sinistro"))
	        {
	        	planosaAeguradora = aseguradora.obterNomePlanosSinistrosPorPeriodo(dataInicioDate, dataFim, secao, admin, ramo);
	        	this.planosaAeguradoraTotal.putAll(planosaAeguradora);
	        }
	        else if(tipo.equals("ApoliceT"))
	        {
	        	if(!somenteTotais)
	        		planosaAeguradora = this.planosaAeguradoraTotal;
	        	else
	        		planosaAeguradora = aseguradoraHome.obterNomePlanoPeriodoTODAS(dataInicioDate, dataFim, ramo, secao);
	        }
	        else if(tipo.equals("SinistroT"))
	        {
	        	if(!somenteTotais)
	        		planosaAeguradora = this.planosaAeguradoraTotal;
	        	else
	        		planosaAeguradora = aseguradoraHome.obterNomePlanosSinistrosPorPeriodoTODAS(dataInicioDate, dataFim, ramo, secao);
	        }
        }
        
        //System.out.println(planosaAeguradora.toString());
        
        for(Iterator<String> j = planosaAeguradora.values().iterator() ; j.hasNext() ; )
        {
        	String[] secaoSuja = j.next().split(";");
        	
        	String secao = secaoSuja[0];
        	String modalidade = secaoSuja[1];
        	
        	if(modalidade.equals("-"))
        		modalidade = "";
        	
        	//System.out.println(secao + " - " + modalidade);
    	   
        	row = planilha.createRow(linha);
        	celula = row.createCell(0);
        	celula.setCellValue(secao);
        	celula.setCellStyle(estiloTextoE);
        	
        	celula = row.createCell(1);
        	celula.setCellStyle(estiloTextoE);
        	if(this.secao.equals(""))
        		celula.setCellValue("Todas");
        	else
        		celula.setCellValue(modalidade);
           
        	coluna = 2;
           
        	dataInicio.setTimeInMillis(dataInicioDate.getTime());
    	   
        	while(dataInicio.getTime().compareTo(dataFim) < 0)
        	{
        		int qtde = 0;
        		double primamontoGs = 0;
        		double primaMontoMe = 0;
        		String[] dadosSujos = new String[1];
        		String dados;
        		String[] dados2 = new String[1];
        		String ano2 = new SimpleDateFormat("yyyy").format(dataInicio.getTime());
        		String mes2 = new SimpleDateFormat("MM").format(dataInicio.getTime());
        		
        		c.setTimeInMillis(dataInicio.getTime().getTime());
        		Date dataFim2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mes2 + "/" + ano2 + " 23:59:59");
    		
    			if(tipo.equals("Apolice"))
    				dadosSujos = aseguradora.obterQtdeApolicesPorPeriodoNOVO(dataInicio.getTime(), dataFim2, valores, secao, modalidade, ramo);
    			else if(tipo.equals("Sinistro"))
    				dadosSujos = aseguradora.obterQtdeSinistrosPorPeriodoNOVO(dataInicio.getTime(), dataFim2, valores,secao, modalidade, ramo);
    			else if(tipo.equals("ApoliceT"))
    				dadosSujos = aseguradoraHome.obterQtdeApolicesPorPeriodoTODASNOVO(dataInicio.getTime(), dataFim2, valores, secao, modalidade, ramo);
    			else if(tipo.equals("SinistroT"))
    				dadosSujos = aseguradoraHome.obterQtdeSinistrosPorPeriodoTODASNOVO(dataInicio.getTime(), dataFim2, valores, secao, modalidade, ramo);
    			
    			dados = dadosSujos[0];
    			dados2 = dados.split(";");
    	   
    			if(!valores)
    			{
    				qtde = Integer.valueOf(dados2[0]);
    				celula = row.createCell(coluna);
    				celula.setCellValue(qtde);
    				celula.setCellStyle(estiloTexto);
    			}
    			else
    			{
					primamontoGs = Double.valueOf(dados2[1]); 
    				celula = row.createCell(coluna);
    				celula.setCellValue(format.format(primamontoGs));
    				celula.setCellStyle(estiloTextoD);
		           
    				primaMontoMe = Double.valueOf(dados2[2]);
    				celula = row.createCell(++coluna);
    				celula.setCellValue(format.format(primaMontoMe));
    				celula.setCellStyle(estiloTextoD);
    			}
    			
	    		this.addTotalMes(mes2 + "/" + ano2, qtde);
	    		this.addTotalMesPrimaGs(mes2 + "/" + ano2, primamontoGs);
	    		this.addTotalMesPrimaMe(mes2 + "/" + ano2, primaMontoMe);
			   
	    		dataInicio.add(Calendar.MONTH, 1);
	    		coluna++;
        	}
        	
        	linha++;
       }
 	   
 	   //MOSTRAR TOTAL MES APOLICES DEPOIS DE CADA SEGURADORA
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("TOTAL");
       celula.setCellStyle(estiloTextoN_E);
	       
       coluna = 2;
       dataInicio.setTimeInMillis(dataInicioDate.getTime());
        
       while(dataInicio.getTime().compareTo(dataFim) < 0)
       {
    	   String mesAnoGeral = new SimpleDateFormat("MM/yyyy").format(dataInicio.getTime());
    	   int qtde = 0;
    	   double valorPrima = 0;
    	   double valorMe = 0;
    	   
    	   if(!valores)
    	   {
	    	   if(this.totalMes.containsKey(mesAnoGeral))
		    	   qtde = Integer.parseInt(this.totalMes.get(mesAnoGeral).toString());
	    	   
	    	   celula = row.createCell(coluna);
	           celula.setCellValue(qtde);
	           celula.setCellStyle(estiloTextoN);
    	   }
           else
           {
	    	   if(this.primaGSMesMap.containsKey(mesAnoGeral))
		    	   valorPrima = this.primaGSMesMap.get(mesAnoGeral);
	    	   
	    	   celula = row.createCell(coluna);
	           celula.setCellValue(format.format(valorPrima));
	           celula.setCellStyle(estiloTextoND);
		    	   
               if(this.primaMeMesMap.containsKey(mesAnoGeral))
		    	   valorMe = this.primaMeMesMap.get(mesAnoGeral);
               
               celula = row.createCell(++coluna);
	           celula.setCellValue(format.format(valorMe));
	           celula.setCellStyle(estiloTextoND);
           }
           
           coluna++;
           
           dataInicio.add(Calendar.MONTH, 1);
       }
   
       linha+=2;
    }
	
	private void addTotalMes(String mesAno, int qtde)
	{
		if(this.totalMes.containsKey(mesAno))
		{
			int qtdeG = Integer.parseInt(this.totalMes.get(mesAno).toString());
			
			qtdeG+=qtde;
			
			this.totalMes.remove(mesAno);
			
			this.totalMes.put(mesAno, qtdeG);
		}
		else
			this.totalMes.put(mesAno, qtde);
	}
	
	private void addTotalMesPrimaGs(String mesAno, double valor)
	{
		if(this.primaGSMesMap.containsKey(mesAno))
		{
			double valorVelho = this.primaGSMesMap.get(mesAno);
			
			valorVelho+=valor;
			
			this.primaGSMesMap.remove(mesAno);
			
			this.primaGSMesMap.put(mesAno, valorVelho);
		}
		else
			this.primaGSMesMap.put(mesAno, valor);
	}
	
	private void addTotalMesPrimaMe(String mesAno, double valor)
	{
		if(this.primaMeMesMap.containsKey(mesAno))
		{
			double valorVelho = this.primaMeMesMap.get(mesAno);
			
			valorVelho+=valor;
			
			this.primaMeMesMap.remove(mesAno);
			
			this.primaMeMesMap.put(mesAno, valorVelho);
		}
		else
			this.primaMeMesMap.put(mesAno, valor);
	}
}
