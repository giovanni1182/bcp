package com.gvs.crm.report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Usuario;
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

public class AseguradoraPlanosReport extends PDF 
{
		private Aseguradora aseguradora;
		
		class CabecalhoRodape extends PdfPageEventHelper
		{
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
		 	         
		 	        Paragraph texto2 = new Paragraph("NUESTRA VISIÓN: Desarrollar una gestión eficiente, autónoma y basada en la excelencia de sus talentos, tendiente a contribuir com la mejor calidad de vida de los habitantes de nuestro pais y lograr el reconocimiento nacional e internacional como institución confiable y creíble",fontTitulo);
		 	        texto2.setAlignment(Element.ALIGN_JUSTIFIED);
		 	         
		 	         PdfPTable head = new PdfPTable(new float[] { 0.40f, 0.60f});
		 	         
		 	         PdfPCell cell2 = new PdfPCell(img);
		 	         cell2.setBorder(0);
		 	         
		 	         head.addCell(cell2);
		 	         
		 	         cell2 = new PdfPCell(texto2);
		 	         cell2.setBorder(0);
		 	         cell2.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
		 	         
		 	         head.addCell(cell2);
		 	         
		 	         String[] nomeV = aseguradora.obterNome().split("-");
			         Paragraph texto = new Paragraph(nomeV[0].toUpperCase(),fontTitulo);
		 	         texto.setAlignment(Element.ALIGN_CENTER);
		 	         
		 	         cell2 = new PdfPCell(texto);
		 	         cell2.setBorder(0);
		 	         cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		 	         cell2.setColspan(2);
		 	         
		 	         head.addCell(cell2);
		 	         
		 	         texto = new Paragraph("PLANES DE SEGUROS INSCRIPTOS EN EL REGISTRO PÚBLICO",fontTitulo);
		 	         texto.setAlignment(Element.ALIGN_CENTER);
		 	         texto.setSpacingAfter(10);
		 	         
		 	         cell2 = new PdfPCell(texto);
		 	         cell2.setBorder(0);
		 	         cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		 	         cell2.setColspan(2);
		 	         
		 	         head.addCell(cell2);
		 	         
		             head.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin()); 
		             head.writeSelectedRows(0, -1, document.leftMargin(),page.getHeight() - document.topMargin() + head.getTotalHeight(),writer.getDirectContent());
		             
		        	 PdfPTable foot = new PdfPTable(1);  
		        	 Paragraph aa = new Paragraph("Pagina " + document.getPageNumber(),fontTexto);
		        	 
		        	 PdfPCell cell = new PdfPCell(aa);
		        	 cell.setBorder(0);
		        	 cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
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
		
		public AseguradoraPlanosReport(Aseguradora aseguradora, Usuario usuario, String textoUsuario) throws Exception
		{
			this.aseguradora = aseguradora;
			
			Document documento = new Document(PageSize.A4,50,50,100,50);
			try 
		    {
				Collection<Plano> planos = aseguradora.obterPlanosOrdenadorPorSecao();
			
				Font fontTextoCor = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL,BaseColor.WHITE);
				Font fontTextoCor2 = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.BOLD);
				Font fontTexto = new Font(Font.FontFamily.TIMES_ROMAN, 9);
		        Font fontTitulo = new Font(Font.FontFamily.TIMES_ROMAN, 10,Font.BOLD);
		        
		        //String caminho = "C:/tmp/" + new Date().getTime() + ".pdf";
		        
		        String caminho = "C:/tmp/" + aseguradora.obterNome() + "_Planes" + usuario.obterId() + ".pdf";
		        
		        this.setCaminho(caminho);
		        
		        PdfWriter pdf = PdfWriter.getInstance(documento, new FileOutputStream(caminho));
		        pdf.setPageEvent(new CabecalhoRodape());
		        
		        documento.open();
		        
		       /* String[] nomeV = aseguradora.obterNome().split("-");
		        
		        Paragraph texto = new Paragraph(nomeV[0].toUpperCase(),fontTitulo);
	 	        texto.setAlignment(Element.ALIGN_CENTER);
	 	        
	 	        documento.add(texto);
	 	        
	 	        texto = new Paragraph("PLANES DE SEGUROS INSCRIPTOS EN EL REGISTRO PÚBLICO",fontTitulo);
	 	        texto.setAlignment(Element.ALIGN_CENTER);
	 	        texto.setSpacingAfter(10);
	 	       
	 	        documento.add(texto);*/
		        
		        PdfPTable table = new PdfPTable(new float[] { 0.10f, 0.60f, 0.20f, 0.10f});
		        table.setWidthPercentage(100);
		        Paragraph texto4 = new Paragraph("CÓDIGO",fontTextoCor);
		        Paragraph texto5 = new Paragraph("DENOMINACIÓN",fontTextoCor);
		        Paragraph texto6 = new Paragraph("RESOLUCIÓN / NOTA",fontTextoCor);
		        Paragraph texto7 = new Paragraph("FECHA",fontTextoCor);
		        
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
		        
		        table.addCell(cell);
		        table.addCell(cell2);
		        table.addCell(cell3);
		        table.addCell(cell4);
		        
		        Map secao = new TreeMap();
		        
		        for(Plano plano : planos)
		        {
		        	if(!secao.containsKey(plano.obterSecao()))
        			{
		        		Paragraph texto12 = new Paragraph("SECCIÓN: " + plano.obterSecao(),fontTextoCor2);
				        PdfPCell cell9 = new PdfPCell(texto12);
				        cell9.setHorizontalAlignment(Element.ALIGN_LEFT);
				        cell9.setColspan(4);
				        cell9.setBackgroundColor(BaseColor.LIGHT_GRAY);
				        //cell9.setBorderColor(BaseColor.WHITE);
				        
				        table.addCell(cell9);
        			}
		        	
			        Paragraph texto12 = new Paragraph(plano.obterIdentificador(),fontTexto);
			        PdfPCell cell9 = new PdfPCell(texto12);
			        cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
			        
			        Paragraph texto13 = new Paragraph(plano.obterTitulo(),fontTexto);
			        PdfPCell cell10 = new PdfPCell(texto13);
			        cell10.setHorizontalAlignment(Element.ALIGN_LEFT);
			        
			        Paragraph texto14 = new Paragraph(plano.obterResolucao(),fontTexto);
			        PdfPCell cell11 = new PdfPCell(texto14);
			        cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
			        
			        Paragraph texto15 = new Paragraph(new SimpleDateFormat("dd/MM/yyyy").format(plano.obterDataResolucao()),fontTexto);
			        PdfPCell cell12 = new PdfPCell(texto15);
			        cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
			        
			        table.addCell(cell9);
			        table.addCell(cell10);
			        table.addCell(cell11);
			        table.addCell(cell12);
			        
			        secao.put(plano.obterSecao(), plano.obterSecao());
		        }
		        
		        table.setSpacingAfter(10);
		        
		        documento.add(table);
		        
		        Paragraph texto = new Paragraph("Última actualización: " + new SimpleDateFormat("dd/MM/yyyy").format(new Date()),fontTitulo);
		        texto.setAlignment(Element.ALIGN_CENTER);
		        texto.setSpacingAfter(10);
		        
		        documento.add(texto);
		        
		        texto = new Paragraph("NUESTRA MISIÓN: Preservar y velar por la estabilidad del valor de la moneda, promover la eficacia y estabilidad del sistema financiero y servir a la " + 
		        "Sociedad con excelencia en su rol de banco de bancos y agente financiero del Estado.",fontTitulo);
		        texto.setAlignment(Element.ALIGN_JUSTIFIED);
		        
		        documento.add(texto);
		        
		       /* Paragraph p = new Paragraph("Elaborado por: IETA - Intendencia de Estudios Técnicos y Actuariales.",fontTitulo);
	       	 	p.setSpacingBefore(7);
		        documento.add(p);
		        
		        p = new Paragraph(textoUsuario,fontTitulo);
	       	 	p.setSpacingBefore(7);
		        documento.add(p);*/
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
