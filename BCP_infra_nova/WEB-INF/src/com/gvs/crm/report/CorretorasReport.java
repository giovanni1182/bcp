package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import infra.config.InfraProperties;

public class CorretorasReport extends PDF 
{
	private Date data;
	
	class CabecalhoRodape extends PdfPageEventHelper
	{
		public String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        
        public void onEndPage(PdfWriter writer, Document document)
        {
        	 Rectangle page = document.getPageSize();
        	 Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
        	 Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 9);
        	 try
        	 {
        		 String dirImages = InfraProperties.getInstance().getProperty("report.images.url");
 	 	        
	 	         Image img = Image.getInstance(dirImages + "/bcp.jpg");
	 	         img.scalePercent(50.0f);
	 	         img.setAlignment(Element.ALIGN_CENTER);
	 	         
	 	         PdfPTable head = new PdfPTable(1);
	 	         
	 	         PdfPCell cell2 = new PdfPCell(img);
	 	         cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	 	         cell2.setBorder(0);
	 	         head.addCell(cell2);
	 	         
	 	        String ano = new SimpleDateFormat("yyyy").format(data);
		        String mes = this.getMesExtenso(data);
		        
		        Paragraph texto = new Paragraph("SUPERINTENDENCIA DE SEGUROS",fontTitulo);
		        Paragraph texto2 = new Paragraph("INTENDENCIA DE CONTROL DE OPERACIONES DE REASEGUROS Y AUXILIARES DEL SEGURO",fontTitulo);
		        Paragraph texto3 = new Paragraph("REGISTRO DE CORREDORES DE REASEGUROS HABILITADOS, AL "+mes.toUpperCase()+" - "+ano,fontTitulo);
		        Paragraph texto4 = new Paragraph("FECHA " + dataStr,fontTitulo);
		        
		        cell2 = new PdfPCell(texto);
		        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell2.setBorder(0);
		        head.addCell(cell2);
		        cell2 = new PdfPCell(texto2);
		        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell2.setBorder(0);
		        head.addCell(cell2);
		        cell2 = new PdfPCell(texto3);
		        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell2.setBorder(0);
		        head.addCell(cell2);
		        cell2 = new PdfPCell(texto4);
		        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell2.setBorder(0);
		        head.addCell(cell2);
	 	         
	             head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin()); 
	             head.writeSelectedRows(0, -1, document.leftMargin(),page.getHeight() - document.topMargin() + head.getTotalHeight(),writer.getDirectContent());
	             
	        	 /*PdfPTable foot = new PdfPTable(1);  
	        	 Paragraph aa = new Paragraph("Los Corredores " + dataStr,fontTexto);
	        	 
	        	 PdfPCell cell = new PdfPCell(aa);
	        	 cell.setBorder(0);
	        	 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	 foot.addCell(cell);
	        	 
	        	 foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());  
	        	 foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());*/
        	 }
        	 catch (Exception e)
        	 {
        		 System.out.println(e.getMessage());
			}
        }
        
        public String getMesExtenso(Date data)
    	{
    		String mesStr = "";
    		int mes = Integer.parseInt(new SimpleDateFormat("MM").format(data));
    		
    		if(mes == 1)
    			mesStr = "Enero";
    		else if(mes == 2)
    			mesStr = "Febrero";
    		else if(mes == 3)
    			mesStr = "Marzo";
    		else if(mes == 4)
    			mesStr = "Abril";
    		else if(mes == 5)
    			mesStr = "Mayo";
    		else if(mes == 6)
    			mesStr = "Junio";
    		else if(mes == 7)
    			mesStr = "Julio";
    		else if(mes == 8)
    			mesStr = "Agosto";
    		else if(mes == 9)
    			mesStr = "Septiembre";
    		else if(mes == 10)
    			mesStr = "Octubre";
    		else if(mes == 11)
    			mesStr = "Noviembre";
    		else if(mes == 12)
    			mesStr = "Diciembre";
    			
    		return mesStr;
    	}
    }
	
	public CorretorasReport(Collection lista, Date data, String obs, String textoUsuario,boolean ci) throws Exception
	{
		this.data = data;
		//Document documento = new Document(PageSize.A4,50,50,120,50);
		Document documento = new Document(new Rectangle(1020, 620),25,25,120,5);
		try 
	    {
		
			Font fontPequenoCor = new Font(Font.FontFamily.TIMES_ROMAN, 7,Font.NORMAL,BaseColor.WHITE);
			Font fontTextoCor = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL,BaseColor.WHITE);
			Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 9);
	        Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
	        
	        String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
	        
	        this.setCaminho(caminho);
	        
	        PdfWriter pdf = PdfWriter.getInstance(documento, new FileOutputStream(caminho));
	        pdf.setPageEvent(new CabecalhoRodape());
	        
	        documento.open();
	        
	        PdfPTable table = null;
	        if(ci)
	        	table = new PdfPTable(new float[] { 0.05f, 0.05f, 0.38f, 0.12f, 0.10f, 0.14f, 0.08f, 0.08f});
	        else
	        	table = new PdfPTable(new float[] { 0.05f, 0.05f, 0.44f, 0.12f, 0.10f, 0.14f, 0.10f});
	        table.setWidthPercentage(100);
	        
	        Paragraph texto = new Paragraph("Nº",fontPequenoCor);
	        PdfPCell cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        texto = new Paragraph("Cod",fontTextoCor);
	        cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        texto = new Paragraph("Corredores de Reaseguros",fontTextoCor);
	        cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        texto = new Paragraph("Res/Const Nº",fontTextoCor);
	        cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        texto = new Paragraph("Fecha",fontTextoCor);
	        cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        texto = new Paragraph("Ramos",fontTextoCor);
	        cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        texto = new Paragraph("Vencimiento",fontTextoCor);
	        cell = new PdfPCell(texto);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        table.addCell(cell);
	        
	        if(ci)
	        {
		        texto = new Paragraph("CI o RUC",fontTextoCor);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBackgroundColor(BaseColor.DARK_GRAY);
		        cell.setBorderColor(BaseColor.WHITE);
		        table.addCell(cell);
	        }
	        
	        int cont = 1;
	        
	        boolean cor = false;
	        
	        for(Iterator i = lista.iterator() ; i.hasNext() ; )
	        {
	        	String str = (String) i.next();
	        	
	        	String[] dados = str.split(";");
	        	
	        	String inscricao = dados[0];
	        	String nome = dados[1];
	        	String dataR = dados[2];
	        	String numeroR = dados[3];
	        	String dataV = dados[4];
	        	String ramo = dados[5];
	        	String ruc = dados[6];
	        	
	        	texto = new Paragraph(new Integer(cont).toString(),fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        texto = new Paragraph(inscricao,fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        texto = new Paragraph(nome,fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        texto = new Paragraph(numeroR,fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        texto = new Paragraph(dataR,fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        texto = new Paragraph(ramo,fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        texto = new Paragraph(dataV,fontTexto);
		        cell = new PdfPCell(texto);
		        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        table.addCell(cell);
		        
		        if(ci)
		        {
		        	texto = new Paragraph(ruc,fontTexto);
				    cell = new PdfPCell(texto);
				    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				    cell.setBorderColor(BaseColor.WHITE);
				    if(cor)
				    	cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				    table.addCell(cell);
		        }
		        
		        
		        cor=!cor;
		        
		        cont++;
	        }
	        
	        table.setSpacingAfter(20);
	        
	        documento.add(table);
	        
	        if(obs.length() > 0)
	        {
	        	 Paragraph texto16 = new Paragraph("Observación:",fontTitulo);
	        	 documento.add(texto16);
	        	 
	        	 Paragraph texto17 = new Paragraph(obs,fontTexto);
	        	 texto17.setAlignment(Element.ALIGN_JUSTIFIED);
	        	 texto17.setSpacingAfter(20);
	        	 documento.add(texto17);
	        	 
	        }
	        
	        texto = new Paragraph("NOTA: Todas estas Corredoras de Reaseguros han sido habilitadas conforme a lo dispuesto en las Resoluciones SS.SG. Nºs 15/96 y 285/07 de fechas 24.06.96 y 26.11.07, respectivamente, que establecen procedimientos para la inscripción y renovación de las mismas en el registro de la S.I.S.",fontTexto);
	        texto.setAlignment(Element.ALIGN_JUSTIFIED);
	        documento.add(texto);
	        
	        Paragraph p = new Paragraph("Elaborado por: ICORAS - Intendencia de Control de Operaciones de Reaseguros y Auxiliares del Seguro.",fontTitulo);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	        
	        p = new Paragraph(textoUsuario,fontTitulo);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	        
	    }
		catch(DocumentException de) 
	    {
	        System.err.println(de.getMessage());
	        throw new Exception(de.getMessage());
	    }
	    catch(IOException ioe) 
	    {
	        System.err.println(ioe.getMessage());
	        throw new Exception(ioe.getMessage());
	    }
	    
	    documento.close();
	}
}
