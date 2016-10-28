package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Sinistro;

import infra.config.InfraProperties;


public class SinistrosXLS extends Excel 
{
	public SinistrosXLS(Aseguradora aseguradora, String secao, String situacao, Date dataInicio, Date dataFim, Collection<Sinistro> sinistros, String nomeAsegurado) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet planilha = wb.createSheet("Planilha");
        
        HSSFFont fonteTitulo = wb.createFont();
        fonteTitulo.setFontHeightInPoints((short)10);
        fonteTitulo.setFontName("Arial");
        fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle estiloTitulo = wb.createCellStyle();
        estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitulo.setFont(fonteTitulo);
        
        HSSFFont fonteTituloTabela = wb.createFont();
        fonteTituloTabela.setFontHeightInPoints((short)9);
        fonteTituloTabela.setFontName("Arial");
        fonteTituloTabela.setColor(HSSFColor.WHITE.index);
        
        HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
        estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTituloTabela.setFont(fonteTituloTabela);
        estiloTituloTabela.setFillForegroundColor(HSSFColor.BLACK.index);
        estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)9);
        fonteTexto.setFontName("Arial");
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoC = wb.createCellStyle();
        estiloTextoC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoC.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoD = wb.createCellStyle();
        estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTextoD.setFont(fonteTexto);
        
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
        
        Region r = new Region(1, (short)5, 1, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(2);
        celula = row.createCell(5);
        celula.setCellValue("Aseguradora:" + aseguradora.obterNome());
        celula.setCellStyle(estiloTitulo);
        r = new Region(2, (short)5, 2, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(3);
        celula = row.createCell(5);
        if(secao!=null)
        	celula.setCellValue("Sección:" + secao);
        else
        	celula.setCellValue("Sección: Todas");
        celula.setCellStyle(estiloTitulo);
        r = new Region(3, (short)5, 3, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(4);
        celula = row.createCell(5);
        if(situacao.equals("0"))
        	celula.setCellValue("Situación: Todas");
        else
        	celula.setCellValue("Situación:" + situacao);
        celula.setCellStyle(estiloTitulo);
        r = new Region(4, (short)5, 4, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(5);
        celula = row.createCell(5);
        String datas = "";
        if(dataInicio!=null)
        	datas = "Fecha Emisión: "+ new SimpleDateFormat("dd/MM/yyyy").format(dataInicio);
        if(dataFim!=null)
        	datas+= " hasta: "+ new SimpleDateFormat("dd/MM/yyyy").format(dataFim);
        celula.setCellValue(datas);
        celula.setCellStyle(estiloTitulo);
        r = new Region(5, (short)5, 5, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(6);
        celula = row.createCell(5);
       	celula.setCellValue("Nombre Asegurado:: " + nomeAsegurado);
        celula.setCellStyle(estiloTitulo);
        r = new Region(6, (short)5, 6, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(7);
        celula = row.createCell(5);
       	celula.setCellValue("Cantidade: " + sinistros.size());
        celula.setCellStyle(estiloTitulo);
        r = new Region(7, (short)5, 7, (short)17);
        planilha.addMergedRegion(r);
        
        int linha = 9;
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
       	celula.setCellValue("Póliza");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(1);
       	celula.setCellValue("Siniestro");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(2);
       	celula.setCellValue("Modalidad");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)2, linha, (short)3);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(4);
       	celula.setCellValue("Fecha Siniestro");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(5);
       	celula.setCellValue("Fecha Denuncia");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(6);
       	celula.setCellValue("Liquidador");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)6, linha, (short)7);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(8);
       	celula.setCellValue("Monto Gs");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)8, linha, (short)9);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(10);
       	celula.setCellValue("Monto ME");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)10, linha, (short)11);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(12);
       	celula.setCellValue("Situación del Siniestro");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)12, linha, (short)13);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(14);
       	celula.setCellValue("Recup. Terceiro");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)14, linha, (short)15);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(16);
       	celula.setCellValue("Fecha Recupero");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(17);
       	celula.setCellValue("Finaliz. Pago");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(18);
       	celula.setCellValue("Recup. Reaseguro");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)18, linha, (short)19);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(20);
       	celula.setCellValue("Partic. Reaseguro");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)20, linha, (short)21);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(22);
       	celula.setCellValue("Descripción");
        celula.setCellStyle(estiloTituloTabela);
        r = new Region(linha, (short)22, linha, (short)23);
        planilha.addMergedRegion(r);
        
        linha++;
        
        for(Iterator<Sinistro> i = sinistros.iterator() ; i.hasNext() ; )
        {
        	Sinistro sinistro = i.next();
        	Apolice apolice = (Apolice) sinistro.obterSuperior();
        	Plano plano = apolice.obterPlano();
        	
        	row = planilha.createRow(linha);
            celula = row.createCell(0);
            celula.setCellValue(apolice.obterNumeroApolice());
            celula.setCellStyle(estiloTextoC);
            
            celula = row.createCell(1);
            celula.setCellValue(sinistro.obterNumero());
            celula.setCellStyle(estiloTextoC);
            
            celula = row.createCell(2);
            if(plano!=null)
            	celula.setCellValue(plano.obterSecao());
            else
            	celula.setCellValue("");
            celula.setCellStyle(estiloTextoC);
            r = new Region(linha, (short)2, linha, (short)3);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(4);
            celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(sinistro.obterDataSinistro()));
            celula.setCellStyle(estiloTextoC);
            
            Date dataDenuncia = sinistro.obterDataDenuncia(); 
            celula = row.createCell(5);
            if(dataDenuncia!=null)
            	celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(dataDenuncia));
            else
            	celula.setCellValue("");
            celula.setCellStyle(estiloTextoC);
            
            celula = row.createCell(6);
            if (sinistro.obterAuxiliar() != null)
            	celula.setCellValue(sinistro.obterAuxiliar().obterInscricaoAtiva().obterInscricao() + " - " + sinistro.obterAuxiliar().obterNome());
            else
            	celula.setCellValue("");
            celula.setCellStyle(estiloTexto);
            r = new Region(linha, (short)6, linha, (short)7);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(8);
           	celula.setCellValue(format.format(sinistro.obterMontanteGs()));
            celula.setCellStyle(estiloTextoD);
            r = new Region(linha, (short)8, linha, (short)9);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(10);
           	celula.setCellValue(format.format(sinistro.obterMontanteMe()));
            celula.setCellStyle(estiloTextoD);
            r = new Region(linha, (short)10, linha, (short)11);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(12);
           	celula.setCellValue(sinistro.obterSituacao());
            celula.setCellStyle(estiloTexto);
            r = new Region(linha, (short)12, linha, (short)13);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(14);
           	celula.setCellValue(format.format(sinistro.obterValorRecuperacaoTerceiro()));
            celula.setCellStyle(estiloTextoD);
            r = new Region(linha, (short)14, linha, (short)15);
            planilha.addMergedRegion(r);
            
            Date dataRecupero = sinistro.obterDataRecuperacao(); 
            celula = row.createCell(16);
           	celula.setCellValue("Fecha Recupero");
           	if(dataRecupero!=null)
           		celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(dataRecupero));
           	else
           		celula.setCellValue("");
           	celula.setCellStyle(estiloTextoC);
           	
           	celula = row.createCell(17);
           	Date dataPagamento = sinistro.obterDataPagamento();
           	if(dataPagamento!=null)
           		celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(dataPagamento));
           	else
           		celula.setCellValue("");
            celula.setCellStyle(estiloTextoC);
            
            celula = row.createCell(18);
           	celula.setCellValue(format.format(sinistro.obterValorRecuperacao()));
            celula.setCellStyle(estiloTextoD);
            r = new Region(linha, (short)18, linha, (short)19);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(20);
            celula.setCellValue(format.format(sinistro.obterParticipacao()));
            celula.setCellStyle(estiloTextoD);
            r = new Region(linha, (short)20, linha, (short)21);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(22);
           	celula.setCellValue(sinistro.obterDescricao());
            celula.setCellStyle(estiloTexto);
            r = new Region(linha, (short)22, linha, (short)23);
            planilha.addMergedRegion(r);
            
            linha++;
        }
        
        wb.write(stream);

        stream.flush();

        stream.close();
	}
}
