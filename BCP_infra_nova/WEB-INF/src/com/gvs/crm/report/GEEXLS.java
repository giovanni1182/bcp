package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.EntidadeHome;

import infra.config.InfraProperties;

public class GEEXLS extends Excel 
{
	private EntidadeHome home;
	private DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	private String aseguradorasMenor80;
	private HSSFCellStyle estiloTituloTabela;
	private HSSFCellStyle estiloTextoCor;
	private HSSFCellStyle estiloTextoE;
	private HSSFCellStyle estiloTextoD;
	private int linha = 0;
	
	public GEEXLS(Date data, EntidadeHome home, String aseguradorasMenor80, String textoUsuario) throws Exception
	{
		String ano = new SimpleDateFormat("yyyy").format(data);
		String mes = new SimpleDateFormat("MM").format(data);
		this.aseguradorasMenor80 = aseguradorasMenor80;
		
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(data.getTime());
		
		Date dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMaximum(Calendar.DAY_OF_MONTH) + "/" + mes + "/" + ano + " 23:59:59");
		
		c.add(Calendar.MONTH, -12);
		
		String anoInicio = new SimpleDateFormat("yyyy").format(c.getTime());
		String mesInicio = new SimpleDateFormat("MM").format(c.getTime());
		
		Date dataInicioDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(c.getActualMinimum(Calendar.DAY_OF_MONTH) + "/" + mesInicio + "/" + anoInicio + " 00:00:00");
		
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTimeInMillis(dataInicioDate.getTime());
		
		this.home = home;
		
		this.instanciarContasGEE();
		
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
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
        this.estiloTextoD = estiloTextoD;
        
        HSSFCellStyle estiloTextoE = wb.createCellStyle();
        estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
        estiloTextoE.setFont(fonteTexto);
        this.estiloTextoE = estiloTextoE;
        
        HSSFCellStyle estiloTextoN = wb.createCellStyle();
        estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoN.setFont(fonteTextoN);
        
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
        estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTituloTabela.setFont(fonteTituloTabela);
        estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
        estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        this.estiloTituloTabela = estiloTituloTabela;
        
        HSSFCellStyle estiloTextoCor = wb.createCellStyle();
        estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoCor.setFont(fonteTexto);
        estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        this.estiloTextoCor = estiloTextoCor;
        
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
        
        celula.setCellValue("SERVICIOS DE SEGUROS - " + new SimpleDateFormat("MM/yyyy").format(dataInicioDate) + " hasta " + new SimpleDateFormat("MM/yyyy").format(dataFim));
        celula.setCellStyle(estiloTitulo);
        
        Region r = new Region(6, (short)0, 6, (short)15);
        planilha.addMergedRegion(r);
        
        //PRIMAS
        linha = 8;
        Map<String, ClassificacaoContas> contas = new TreeMap<String, ClassificacaoContas>();
        contas.put("1 - Primas de seguros", c0401010000);
        contas.put("2 - (Anulación primas de seguros)", null);
        contas.put("3 - Primas de reaseguros activos",c0402000000);
        contas.put("4 - (Anulación primas de reaseguros activos)",null);
        contas.put("5 - (Primas de reaseguros pasivos)",c0501000000);
        contas.put("6 - Anulación primas de reaseguros pasivos",null);
        this.montaPlanilha(contas, "PRIMAS", planilha, dataInicioDate, dataFim);
        
        //Indemnizaciones
        //linha+=contas.size() + 2;
        linha++;
        contas.clear();
        contas.put("1 - Siniestros", c0506000000);
        contas.put("2 - Siniestros reaseguros activos", c0513000000);
        contas.put("3 - (Siniestros recuperados de reaseguros)",c0408000000);
        this.montaPlanilha(contas, "Indemnizaciones".toUpperCase(), planilha, dataInicioDate, dataFim);
        
        //Otras producciones de servicios de seguros
        //linha+=contas.size() + 2;
        linha++;
        contas.clear();
        contas.put("1 - Participación contrato reaseguros pasivos", c0410040000);
        contas.put("2 - (Participación contrato reaseguros activos)", c0514040000);
        contas.put("3 - Intereses reservas reaseguros activos",c0413010000);
        contas.put("4 - (Intereses reservas reaseguros pasivos)", c0510020000);
        contas.put("5 - Utilidades varias", c0435000000);
        contas.put("6 - (Pérdidas varias)",c0535000000);
        contas.put("7 - Recargos administrativos", c0525010000);
        contas.put("8 - Gastos liquidaciones siniestros recuperaciones de reaseguros", c0508000000);
        contas.put("9 - Comisiones reaseguros pasivos",c0410010000);
        this.montaPlanilha(contas, "Otras producciones de servicios de seguros".toUpperCase(), planilha, dataInicioDate, dataFim);
        
        //Producción secundaria
        //linha+=contas.size() + 2;
        linha++;
        contas.clear();
        contas.put("1 - Siniestros recuperados de terceros y salvataje", c0407000000);
        this.montaPlanilha(contas, "Producción secundaria".toUpperCase(),  planilha, dataInicioDate, dataFim);
        
        //Comisiones
        //linha+=contas.size() + 2;
        linha++;
        contas.clear();
        contas.put("1 - Comisiones primas de seguros", c0504010000);
        contas.put("2 - (Comisiones recuperadas)", c0426050000);
        contas.put("3 - Comisiones reaseguros activos", c0514010000);
        contas.put("4 - (Anulación comisiones reaseguros activos)", null);
        this.montaPlanilha(contas, "Comisiones".toUpperCase(),  planilha, dataInicioDate, dataFim);
        
        linha++;
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue(textoUsuario);
        celula.setCellStyle(estiloTitulo);
        r = new Region(linha, (short)0, linha, (short)15);
        planilha.addMergedRegion(r);
		
		wb.write(stream);

        stream.flush();

        stream.close();
	}
	
	private void montaPlanilha(Map<String,ClassificacaoContas> contas, String titulo, HSSFSheet planilha, Date dataInicioDate, Date dataFim) throws Exception
	{
		Calendar dataInicio = Calendar.getInstance();
		dataInicio.setTime(dataInicioDate);
		
        HSSFRow row = planilha.createRow(linha);
        HSSFCell celula = row.createCell(0);
        celula.setCellValue(titulo);
        celula.setCellStyle(estiloTituloTabela);
        Region r = new Region(linha, (short)0, linha, (short)15);
        planilha.addMergedRegion(r);

        linha++;
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("");
        celula.setCellStyle(estiloTextoCor);
        r = new Region(linha, (short)0, linha, (short)1);
        planilha.addMergedRegion(r);
        
        int coluna = 2;
        while(dataInicio.getTime().compareTo(dataFim) < 0)
        {
    	   String mesAnoCabecalho = new SimpleDateFormat("MM/yyyy").format(dataInicio.getTime());
    	   
    	   celula = row.createCell(coluna);
           celula.setCellValue(mesAnoCabecalho);
           celula.setCellStyle(estiloTextoCor);
           
           dataInicio.add(Calendar.MONTH, 1);
           
           coluna++;
        }
        
        celula = row.createCell(coluna);
        celula.setCellValue("TOTAL");
        celula.setCellStyle(estiloTextoCor);
        
        linha++;
     
        for(Iterator<String> i = contas.keySet().iterator() ; i.hasNext() ; )
        {
        	String nomeConta = i.next();
        	dataInicio.setTime(dataInicioDate);
        	
        	String[] n = nomeConta.split("-");
        	
        	String nomeContaReal = n[1].trim();
        	
        	row = planilha.createRow(linha);
	        celula = row.createCell(0);
	        celula.setCellValue(nomeConta);
	        celula.setCellStyle(estiloTextoE);
	        r = new Region(linha, (short)0, linha, (short)1);
	        planilha.addMergedRegion(r);
	        
	        double total = 0;
	        coluna = 2;
	        while(dataInicio.getTime().compareTo(dataFim) < 0)
	        {
	        	if(contas.get(nomeConta)!=null)
	        	{
	        	   ClassificacaoContas cContas = contas.get(nomeConta); 
		    	   celula = row.createCell(coluna);
		    	   double valor = 0;
		    	   
		    	   if(nomeContaReal.equals("Primas de reaseguros activos"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) - c0403000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("(Primas de reaseguros pasivos)"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0502000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("Siniestros"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0507000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("Siniestros reaseguros activos"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0515000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("(Siniestros recuperados de reaseguros)"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0409000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0412000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0414000000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("Participación contrato reaseguros pasivos"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0410050000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0411040000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0411050000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("(Participación contrato reaseguros activos)"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0514050000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0516040000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0516050000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("Intereses reservas reaseguros activos"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0413020000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0415010000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0415020000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("(Intereses reservas reaseguros pasivos)"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0512020000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("Comisiones reaseguros pasivos"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0411010000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else if(nomeContaReal.equals("Comisiones reaseguros activos"))
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80) + c0516010000.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		    	   else
		    		   valor = cContas.obterTotalSaldoAtualMensal(dataInicio.getTime(), aseguradorasMenor80);
		           celula.setCellValue(formataValor.format(valor));
		           celula.setCellStyle(estiloTextoD);
		           total+=valor;
	        	}
	        	else
	        	{
		    	   celula = row.createCell(coluna);
		           celula.setCellValue("0,00");
		           celula.setCellStyle(estiloTextoD);
	        	}
	           
	           dataInicio.add(Calendar.MONTH, 1);
	           
	           coluna++;
	        }
	        
	        celula = row.createCell(coluna);
            celula.setCellValue(formataValor.format(total));
            celula.setCellStyle(estiloTextoD);
	        
	        linha++;
        }
	}
	
	private ClassificacaoContas c0401010000;
	private ClassificacaoContas c0402000000;
	private ClassificacaoContas c0403000000;
	private ClassificacaoContas c0407000000;
	private ClassificacaoContas c0408000000;
	private ClassificacaoContas c0409000000;
	private ClassificacaoContas c0410010000;
	private ClassificacaoContas c0410040000;
	private ClassificacaoContas c0410050000;
	private ClassificacaoContas c0411010000;
	private ClassificacaoContas c0411040000;
	private ClassificacaoContas c0411050000;
	private ClassificacaoContas c0412000000;
	private ClassificacaoContas c0413010000;
	private ClassificacaoContas c0413020000;
	private ClassificacaoContas c0414000000;
	private ClassificacaoContas c0415010000;
	private ClassificacaoContas c0415020000;
	private ClassificacaoContas c0426050000;
	private ClassificacaoContas c0435000000;
	private ClassificacaoContas c0501000000;
	private ClassificacaoContas c0502000000;
	private ClassificacaoContas c0504010000;
	private ClassificacaoContas c0506000000;
	private ClassificacaoContas c0507000000;
	private ClassificacaoContas c0508000000;
	private ClassificacaoContas c0510020000;
	private ClassificacaoContas c0512020000;
	private ClassificacaoContas c0513000000;
	private ClassificacaoContas c0514010000;
	private ClassificacaoContas c0514040000;
	private ClassificacaoContas c0514050000;
	private ClassificacaoContas c0515000000;
	private ClassificacaoContas c0516010000;
	private ClassificacaoContas c0516040000;
	private ClassificacaoContas c0516050000;
	private ClassificacaoContas c0525010000;
	private ClassificacaoContas c0535000000;
	
	private void instanciarContasGEE() throws Exception
	{
		this.c0401010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0401010000");
		this.c0402000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0402000000");
		this.c0403000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0403000000");
		this.c0407000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0407000000");
		this.c0408000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0408000000");
		this.c0409000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0409000000");
		this.c0410010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0410010000");
		this.c0410040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0410040000");
		this.c0411010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0411010000");
		this.c0411040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0411040000");
		this.c0411050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0411050000");
		this.c0410050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0410050000");
		this.c0412000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0412000000");
		this.c0413010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0413010000");
		this.c0413020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0413020000");
		this.c0414000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0414000000");
		this.c0415010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0415010000");
		this.c0415020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0415020000");
		this.c0426050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0426050000");
		this.c0435000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0435000000");
		this.c0501000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0501000000");
		this.c0502000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0502000000");
		this.c0504010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0504010000");
		this.c0506000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0506000000");
		this.c0507000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0507000000");
		this.c0508000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0508000000");
		this.c0510020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0510020000");
		this.c0512020000 = (ClassificacaoContas) home.obterEntidadePorApelido("0512020000");
		this.c0513000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0513000000");
		this.c0514010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0514010000");
		this.c0514040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0514040000");
		this.c0514050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0514050000");
		this.c0515000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0515000000");
		this.c0516010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0516010000");
		this.c0516040000 = (ClassificacaoContas) home.obterEntidadePorApelido("0516040000");
		this.c0516050000 = (ClassificacaoContas) home.obterEntidadePorApelido("0516050000");
		this.c0525010000 = (ClassificacaoContas) home.obterEntidadePorApelido("0525010000");
		this.c0535000000 = (ClassificacaoContas) home.obterEntidadePorApelido("0535000000");
	}
}
