package com.gvs.crm.view;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.Usuario;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Link;
import infra.view.Radio;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;

public class CalcularIndicadoresView extends PortalView
{

	private Usuario e;
	private Date dataTecnico;
	private boolean mostraTela;
	private boolean geraArquivo;
	private boolean pdf;
	private boolean xls;
	
	public CalcularIndicadoresView(Usuario e, Date dataTecnico, boolean mostraTela, boolean geraArquivo, boolean pdf, boolean xls) throws Exception
	{
		this.e = e;
		this.dataTecnico = dataTecnico;
		this.mostraTela = mostraTela;
		this.geraArquivo = geraArquivo;
		this.pdf = pdf;
		this.xls = xls;
	}
	
	public View getBody(User user, Locale locale, Properties properties) throws Exception
	{
		Table table = new Table(2);
		
		table.addSubtitle("INDICADORES TECNICOS - DATOS ACUMULADOS EN 12 MESES");
		
		table.setNextColSpan(table.getColumns());
		
		table.add("(Stros. Brutos/PD, Stros. Netos. Reas/PDNR, Gtos. Operativos/PD, Gtos. de Prod./PNA, Gtos. de Explot./PNA)");
		
		table.addSubtitle("");
		
		table.addHeader("INDICADORES PATRIMONIALES y RENTABILIDAD - DATOS AL CIERRE DEL PERIODO OBSERVADO");
		table.add("");
		table.add("(Inversiones/Prov. Técnicas netas, PN / Activo Total, Retorno s/ PN, Resultado Técnico s/ PN, Margen de Ganancia s/ Primas Devengadas)");
		table.add("");
		
		table.addSubtitle("");
		
		table.addSubtitle("Opciones");
		
		Block block = new Block(Block.HORIZONTAL);
		/*block.add(new Radio("gerar", "tela", mostraTela));
		block.add(new Space(2));
		block.add(new Label("Visualizar en la pantalia"));
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		block = new Block(Block.HORIZONTAL);
		block.add(new Radio("gerar", "arquivo", geraArquivo));
		block.add(new Space(2));
		block.add(new Label("Generar archivo(.txt)"));
		table.setNextColSpan(table.getColumns());
		table.add(block);*/
		
		block = new Block(Block.HORIZONTAL);
		block.add(new Radio("gerar", "pdf", this.pdf));
		block.add(new Space(2));
		block.add(new Label("Generar PDF(.pdf)"));
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		block = new Block(Block.HORIZONTAL);
		block.add(new Radio("gerar", "xls", this.xls));
		block.add(new Space(2));
		block.add(new Label("Generar Excel(.xls)"));
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		block = new Block(Block.HORIZONTAL);
		block.add(new Radio("gerar", "xlsI", this.xls));
		block.add(new Space(2));
		block.add(new Label("Generar Excel de los calculos(.xls)"));
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		table.addSubtitle("");
		
		block = new Block(Block.HORIZONTAL);
		Label label = new Label("Fecha del último mes:");
		label.setBold(true);
		block.add(label);
		block.add(new Space(2));
		InputDate data = new InputDate("dataTecnicos", this.dataTecnico);
		data.setMesAno(true);
		block.add(data);
		
		if(this.geraArquivo)
		{
			if(this.dataTecnico!=null)
			{
				String mesAnoStr = new SimpleDateFormat("MM/yyyy").format(dataTecnico);
				
				String[] s = mesAnoStr.split("/");
				
				String mesAnoM = s[0] + s[1] + ".txt";
				
				String nomeArquivo = "C:/tmp/Ind_Tecnicos_" + this.e.obterChave() +"_"+ mesAnoM;
				File file = new File(nomeArquivo);
				
				if(file.exists())
				{
					table.addSubtitle("Archivo Generado");
					
					Block block4 = new Block(Block.HORIZONTAL);
					Link link = new Link(file.getName(),new Action("downloadArquivo"));
					link.getAction().add("arquivoDownload", file.getName());
					block4.add(link);
					
					table.setNextColSpan(table.getColumns());
					table.add(block4);
					
					table.addSubtitle("");
				}
			}
			
		}
		
		Button calcularTecnicoButton = new Button("Calcular",new Action("calcularTecnicos"));
		block.add(new Space(10));
		block.add(calcularTecnicoButton);
		
		table.setNextColSpan(table.getColumns());
		table.add(block);
		
		return table;
	}

	public Entidade getOrigemMenu() throws Exception
	{
		return e;
	}

	public String getSelectedGroup() throws Exception
	{
		return null;
	}

	public String getSelectedOption() throws Exception
	{
		return null;
	}

	public View getTitle() throws Exception
	{
		return null;
	}

}
