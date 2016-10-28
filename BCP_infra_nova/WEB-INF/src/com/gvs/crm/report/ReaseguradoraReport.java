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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import infra.config.InfraProperties;

public class ReaseguradoraReport extends PDF 
{
private Date data;
	
	class CabecalhoRodape extends PdfPageEventHelper
	{
		String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        
        public void onEndPage(PdfWriter writer, Document document)
        {
        	 Rectangle page = document.getPageSize();
        	 Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 7);
        	 Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 7,Font.BOLD);
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
	 	         
	 	        String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
		        
		        Paragraph texto = new Paragraph("REGISTRO PUBLICO DE EMPRESAS REASEGURADORAS DEL EXTERIOR HABILITADAS A OPERAR EN EL PAIS, AL " + dataStr,fontTitulo);
		        
		        cell2 = new PdfPCell(texto);
		        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		        cell2.setBorder(0);
		        head.addCell(cell2);
		        
	             head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin()); 
	             head.writeSelectedRows(0, -1, document.leftMargin(),page.getHeight() - document.topMargin() + head.getTotalHeight(),writer.getDirectContent());
	 	        
	        	/* PdfPTable foot = new PdfPTable(1);  
	        	 Paragraph aa = new Paragraph("Los Agentes " + dataStr,fontTexto);
	        	 
	        	 PdfPCell cell = new PdfPCell(aa);
	        	 cell.setBorder(0);
	        	 cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        	 foot.addCell(cell);
	        	 
	        	 foot.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());  
	        	 foot.writeSelectedRows(0, -1, document.leftMargin(), document.bottomMargin(), writer.getDirectContent());*/
        	 }
        	 catch (Exception e)
        	 {
				// TODO: handle exception
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

	public ReaseguradoraReport(Collection lista, Date data, String obs, String textoUsuario,boolean ci) throws Exception
	{
		this.data = data;
		
		Document documento = new Document(PageSize.A4.rotate(),25,25,90,25);
		try 
	    {
			/*BaseFont base = BaseFont.createFont("c:/windows/fonts/BASKVILL.TTF", BaseFont.WINANSI, false);
			
			Font fontPequenoCor = new Font(base, 12,Font.BOLD,BaseColor.BLACK);
			Font fontTextoCor = new Font(base, 12,Font.BOLD,BaseColor.BLACK);
			Font fontTexto = new Font(base, 12);
	        Font fontTitulo = new Font(base, 12,Font.BOLD);
	        Font fontTituloP = new Font(base, 12,Font.BOLD);*/
			
			Font fontPequenoCor = new Font(Font.FontFamily.TIMES_ROMAN, 6,Font.BOLD,BaseColor.BLACK);
			Font fontTextoCor = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD,BaseColor.BLACK);
			Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 8);
	        Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD);
	        Font fontTituloP = new Font(Font.FontFamily.TIMES_ROMAN, 8,Font.BOLD);
	        
	        String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
	        
	        this.setCaminho(caminho);
	        
	        PdfWriter pdf = PdfWriter.getInstance(documento, new FileOutputStream(caminho));
	        pdf.setPageEvent(new CabecalhoRodape());
	        
	        documento.open();
	        
	        Paragraph espaco = new Paragraph(" ",fontTextoCor);
	        espaco.setSpacingAfter(10);
	        documento.add(espaco);
	        
	        PdfPTable table = null;
        	table = new PdfPTable(new float[] { 0.05f, 0.05f, 0.30f, 0.08f, 0.12f, 0.08f, 0.08f, 0.16f, 0.08f});
	        //, 0.16f,0.08f
	        table.setWidthPercentage(100);
	        
	        Paragraph texto10 = new Paragraph("Constancia Nº",fontTextoCor);
	        Paragraph texto11 = new Paragraph("Habilitación",fontTextoCor);
	        
	        PdfPCell cell = new PdfPCell(new Phrase(""));
	        cell.setBorder(0);
	        table.addCell(cell);
	        table.addCell(cell);
	        table.addCell(cell);
	        table.addCell(cell);
	        cell = new PdfPCell(texto10);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBorder(0);
	        table.addCell(cell);
	        cell = new PdfPCell(new Phrase(""));
	        cell.setBorder(0);
	        table.addCell(cell);
	        cell = new PdfPCell(texto11);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBorder(0);
	        table.addCell(cell);
	        cell = new PdfPCell(new Phrase(""));
	        cell.setBorder(0);
	        table.addCell(cell);
	        table.addCell(cell);
	        if(ci)
	        	table.addCell(cell);
	        
	        Paragraph texto4 = new Paragraph("Nº",fontPequenoCor);
	        Paragraph texto5 = new Paragraph("Cód. Entidad",fontPequenoCor);
	        Paragraph texto6 = new Paragraph("Compañías Reaseguradoras",fontTextoCor);
	        Paragraph texto18 = new Paragraph("País",fontTextoCor);
	        Paragraph texto7 = new Paragraph("y/o Res. N°",fontTextoCor);
	        Paragraph texto8 = new Paragraph("Fecha",fontTextoCor);
	        Paragraph texto9 = new Paragraph("Hasta",fontTextoCor);
	        
	        cell = new PdfPCell(texto4);
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        PdfPCell cell2 = new PdfPCell(texto5);
	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        PdfPCell cell3 = new PdfPCell(texto6);
	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        PdfPCell cell13 = new PdfPCell(texto18);
	        cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell13.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        PdfPCell cell4 = new PdfPCell(texto7);
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        PdfPCell cell5 = new PdfPCell(texto8);
	        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell5.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        PdfPCell cell6 = new PdfPCell(texto9);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        
	        table.addCell(cell);
	        table.addCell(cell2);
	        table.addCell(cell3);
	        table.addCell(cell13);
	        table.addCell(cell4);
	        table.addCell(cell5);
	        table.addCell(cell6);
	        
	        texto9 = new Paragraph("Ramo/Grupo",fontTextoCor);
	        cell6 = new PdfPCell(texto9);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell6);
	        
	        texto9 = new Paragraph("CESION HASTA",fontTextoCor);
	        cell6 = new PdfPCell(texto9);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell6.setBackgroundColor(BaseColor.LIGHT_GRAY);
	        table.addCell(cell6);
	        
	        int cont = 1;
	        
	        for(Iterator i = lista.iterator() ; i.hasNext() ; )
	        {
	        	String str = (String) i.next();
	        	
	        	String[] dados = str.split(";");
	        	
	        	String inscricao = dados[0];
	        	String nome = dados[1];
	        	String dataR = dados[2];
	        	String numeroR = dados[3];
	        	String dataV = dados[4];
	        	String pais = dados[5];
	        	String ramo = dados[6];
	        	String cesion = dados[7] + "%";
	        	
	        	Paragraph texto20 = new Paragraph(new Integer(cont).toString(),fontTexto);
		        PdfPCell cell7 = new PdfPCell(texto20);
		        cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph texto21 = new Paragraph(inscricao,fontTexto);
		        PdfPCell cell8 = new PdfPCell(texto21);
		        cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph texto12 = new Paragraph(nome,fontTexto);
		        PdfPCell cell9 = new PdfPCell(texto12);
		        
		        Paragraph texto19 = new Paragraph(pais,fontTexto);
		        PdfPCell cell14 = new PdfPCell(texto19);
		        cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph texto13 = new Paragraph(numeroR,fontTexto);
		        PdfPCell cell10 = new PdfPCell(texto13);
		        cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph texto14 = new Paragraph(dataR,fontTexto);
		        PdfPCell cell11 = new PdfPCell(texto14);
		        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
		        
		        Paragraph texto15 = new Paragraph(dataV,fontTexto);
		        PdfPCell cell12 = new PdfPCell(texto15);
		        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
		        
		        table.addCell(cell7);
		        table.addCell(cell8);
		        table.addCell(cell9);
		        table.addCell(cell14);
		        table.addCell(cell10);
		        table.addCell(cell11);
		        table.addCell(cell12);
		        
		        texto15 = new Paragraph(ramo,fontTexto);
		        cell12 = new PdfPCell(texto15);
		        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell12);
		        
		        texto15 = new Paragraph(cesion,fontTexto);
		        cell12 = new PdfPCell(texto15);
		        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
		        table.addCell(cell12);
		        cont++;
	        }
	        
	        table.setSpacingAfter(10);
	        
	        documento.add(table);
	        
	        Paragraph texto22 = new Paragraph("NOTA: A partir de la expiración de la habilitación, las inscripciones y renovaciones se regirán por los procedimientos establecidos por Resolución SS.SG. Nº 284/07 del 26/11/07, la que se encuentra a disposición en la página web de esta SIS.",fontTituloP);
	        texto22.setAlignment(Element.ALIGN_JUSTIFIED);
	        texto22.setSpacingAfter(10);
	        documento.add(texto22);
	        
	        PdfPTable table2 = new PdfPTable(3);
	        table2.setWidthPercentage(100);
	        
	        Paragraph texto23 = new Paragraph("Hecho por:",fontTituloP);
	        table2.addCell(texto23);
	        texto23 = new Paragraph("Vdo. Jefe DCOR",fontTituloP);
	        table2.addCell(texto23);
	        texto23 = new Paragraph("V°.B°. Intendente ICORAS",fontTituloP);
	        table2.addCell(texto23);
	        
	        documento.add(table2);
	        
	        if(obs.length() > 0)
	        {
	        	 Paragraph texto16 = new Paragraph("Observación:",fontTitulo);
	        	 texto16.setSpacingBefore(10);
	        	 documento.add(texto16);
	        	 
	        	 Paragraph texto17 = new Paragraph(obs,fontTexto);
	        	 texto17.setAlignment(Element.ALIGN_JUSTIFIED);
	        	 //texto17.setSpacingAfter(20);
	        	 documento.add(texto17);
	        	 
	        }
	        
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
