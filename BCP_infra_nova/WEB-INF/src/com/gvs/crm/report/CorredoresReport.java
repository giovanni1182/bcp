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
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import infra.config.InfraProperties;

public class CorredoresReport extends PDF 
{
	private Date data;
	
	class CabecalhoRodape extends PdfPageEventHelper
	{
		String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        
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
		        Paragraph texto3 = new Paragraph("CORREDORES DE SEGUROS CON MATRICULA VIGENTE - "+mes.toUpperCase()+" - "+ano+"",fontTitulo);
		        
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
	 	         
	             head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin()); 
	             head.writeSelectedRows(0, -1, document.leftMargin(),page.getHeight() - document.topMargin() + head.getTotalHeight(),writer.getDirectContent());
	             
	        	 PdfPTable foot = new PdfPTable(1);  
	        	 Paragraph aa = new Paragraph("Los Corredores " + dataStr,fontTexto);
	        	 
	        	 PdfPCell cell = new PdfPCell(aa);
	        	 cell.setBorder(0);
	        	 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	 foot.addCell(cell);
	        	 
	        	 foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());  
	        	 foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());
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
	
	public CorredoresReport(Collection lista, Date data, String obs, String textoUsuario,boolean ci) throws Exception
	{
		this.data = data;
		//Document documento = new Document(PageSize.A4,50,50,120,50);
		Document documento = new Document(PageSize.A4.rotate(),10,10,120,10);
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
	        
	       /* String dirImages = InfraProperties.getInstance().getProperty("report.images.url");
	        
	        Image img = Image.getInstance(dirImages + "/bcp.jpg");
	        img.scalePercent(60.0f);
	        img.setAlignment(Element.ALIGN_CENTER);
	        documento.add(img);
	        
	        String ano = new SimpleDateFormat("yyyy").format(data);
	        String mes = this.getMesExtenso(data);
	        
	        Paragraph texto = new Paragraph("SUPERINTENDENCIA DE SEGUROS",fontTitulo);
	        texto.setAlignment(Element.ALIGN_CENTER);
	        Paragraph texto2 = new Paragraph("INTENDENCIA DE CONTROL DE OPERACIONES DE REASEGUROS Y AUXILIARES DEL SEGURO",fontTitulo);
	        texto2.setAlignment(Element.ALIGN_CENTER);
	        Paragraph texto3 = new Paragraph("CORREDORES DE SEGUROS CON MATRICULA VIGENTE - "+mes.toUpperCase()+" - "+ano+"",fontTitulo);
	        texto3.setAlignment(Element.ALIGN_CENTER);
	        texto3.setSpacingAfter(10);
	        
	        documento.add(texto);
	        documento.add(texto2);
	        documento.add(texto3);*/
	        
	        PdfPTable table = null;
	        if(ci)
	        	table = new PdfPTable(new float[] { 0.05f, 0.05f, 0.31f, 0.08f, 0.08f, 0.08f, 0.18f,0.09f, 0.08f});
	        else
	        	table = new PdfPTable(new float[] { 0.05f, 0.05f, 0.31f, 0.10f, 0.10f, 0.10f, 0.20f,0.09f});
	        
	        table.setWidthPercentage(100);
	        Paragraph texto4 = new Paragraph("Orden",fontPequenoCor);
	        Paragraph texto5 = new Paragraph("Matr.",fontTextoCor);
	        Paragraph texto6 = new Paragraph("CORREDORES",fontTextoCor);
	        Paragraph texto7 = new Paragraph("Fecha de Res.",fontTextoCor);
	        Paragraph texto8 = new Paragraph("N° de Res.",fontTextoCor);
	        Paragraph texto9 = new Paragraph("Vencimiento",fontTextoCor);
	        Paragraph texto77 = new Paragraph("Dirección Comercial",fontTextoCor);
	        Paragraph texto79 = new Paragraph("Tel. Comercial",fontTextoCor);
	        
	        PdfPCell cell = new PdfPCell(texto4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell2 = new PdfPCell(texto5);
	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell2.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell2.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell3 = new PdfPCell(texto6);
	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell3.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell3.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell4 = new PdfPCell(texto7);
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell4.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell4.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell5 = new PdfPCell(texto8);
	        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell5.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell5.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell6 = new PdfPCell(texto9);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell6.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell6.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell77 = new PdfPCell(texto77);
	        cell77.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell77.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell77.setBorderColor(BaseColor.WHITE);
	        
	        PdfPCell cell79 = new PdfPCell(texto79);
	        cell79.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell79.setBackgroundColor(BaseColor.DARK_GRAY);
	        cell79.setBorderColor(BaseColor.WHITE);
	        
	        table.addCell(cell);
	        table.addCell(cell2);
	        table.addCell(cell3);
	        table.addCell(cell4);
	        table.addCell(cell5);
	        table.addCell(cell6);
	        table.addCell(cell77);
	        table.addCell(cell79);
	        if(ci)
	        {
	        	cell79 = new PdfPCell(new Paragraph("CI o RUC",fontTextoCor));
		        cell79.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell79.setBackgroundColor(BaseColor.DARK_GRAY);
		        cell79.setBorderColor(BaseColor.WHITE);
		        table.addCell(cell79);
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
	        	String endereco = dados[5];
	        	String fone = dados[6];
	        	String ruc = dados[7];
	        	
	        	Paragraph texto10 = new Paragraph(new Integer(cont).toString(),fontTexto);
		        PdfPCell cell7 = new PdfPCell(texto10);
		        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell7.setBorderColor(BaseColor.WHITE);
		        
		        Paragraph texto11 = new Paragraph(inscricao,fontTexto);
		        PdfPCell cell8 = new PdfPCell(texto11);
		        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell8.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell8.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        Paragraph texto12 = new Paragraph(nome,fontTexto);
		        PdfPCell cell9 = new PdfPCell(texto12);
		        cell9.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell9.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        Paragraph texto13 = new Paragraph(dataR,fontTexto);
		        PdfPCell cell10 = new PdfPCell(texto13);
		        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell10.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell10.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        Paragraph texto14 = new Paragraph(numeroR,fontTexto);
		        PdfPCell cell11 = new PdfPCell(texto14);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell11.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell11.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        Paragraph texto15 = new Paragraph(dataV,fontTexto);
		        PdfPCell cell12 = new PdfPCell(texto15);
		        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell12.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell12.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        texto15 = new Paragraph(endereco,fontTexto);
		        PdfPCell cell78 = new PdfPCell(texto15);
		        cell78.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell78.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        texto15 = new Paragraph(fone,fontTexto);
		        PdfPCell cell80 = new PdfPCell(texto15);
		        cell80.setBorderColor(BaseColor.WHITE);
		        if(cor)
		        	cell80.setBackgroundColor(BaseColor.LIGHT_GRAY);
		        
		        table.addCell(cell7);
		        table.addCell(cell8);
		        table.addCell(cell9);
		        table.addCell(cell10);
		        table.addCell(cell11);
		        table.addCell(cell12);
		        table.addCell(cell78);
		        table.addCell(cell80);
		        if(ci)
		        {
		        	cell79 = new PdfPCell(new Paragraph(ruc,fontTexto));
			        cell79.setHorizontalAlignment(Element.ALIGN_CENTER);
			        cell79.setBorderColor(BaseColor.WHITE);
			        if(cor)
			        	cell79.setBackgroundColor(BaseColor.LIGHT_GRAY);
			        table.addCell(cell79);
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
	        
	        Paragraph p = new Paragraph("Elaborado por: ICORAS - Intendencia de Control de Operaciones de Reaseguros y Auxiliares del Seguro.",fontTitulo);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	        
	        p = new Paragraph(textoUsuario,fontTitulo);
       	 	p.setSpacingBefore(7);
	        documento.add(p);
	        
	        /*String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
	        
	        Paragraph texto16 = new Paragraph("Los Corredores " + dataStr,fontTexto);
	        texto16.setAlignment(Element.ALIGN_CENTER);
	        
	        documento.add(texto16);*/
	        
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
