
package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import com.gvs.crm.component.EntidadePopup;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.control.Action;
import infra.security.User;
import infra.view.Block;
import infra.view.Button;
import infra.view.InputDate;
import infra.view.Label;
import infra.view.Space;
import infra.view.Table;
import infra.view.View;


public class PolizasView extends PortalView {
private Entidade entidade ;
private Collection polizas ;
private Aseguradora aseguradora;
private Date fecha_inicio; 
private Date fecha_fin;
private int pagina;


public PolizasView (Entidade entidade, Collection polizas, Aseguradora aseguradora, Date fecha_inicio, Date fecha_fin, int pagina) throws Exception {
	this.entidade 		= entidade ;
	this.polizas		= polizas;
	this.aseguradora	= aseguradora;
	this.fecha_inicio	= fecha_inicio;
	this.fecha_fin		= fecha_fin;
	this.pagina			= pagina;
}
	
public View getBody(User user, Locale locale, Properties properties)
			throws Exception {
	
	Table table = new Table(5) ;
	Table table2 = new Table(2);
	table.setWidth("100%");
	table2.setWidth("100%");
	
	table2.addHeader("ASEGURADORA:");
	table2.add(new EntidadePopup("aseguradora_id","aseguradora_nombre",aseguradora,"aseguradora",true));
	table2.addHeader("Fecha Inicio");
	
	Block block = new Block(Block.HORIZONTAL);
	
	block.add(new InputDate("Fecha_desde",fecha_inicio));
	block.add(new Space(3));
	
	Label label =  new Label("Hasta");
	label.setBold(true);
	
	block.add(label);
	block.add(new Space(3));
	block.add(new InputDate("Fecha_hasta",fecha_fin));
	
	table2.add(block);
	
	
		
	if (polizas.size() > 0){
		table.addSubtitle("Cantidad de Polizas:  "+ polizas.size()+ " Pag.:"+pagina);
		
		table.addHeader("Fecha de Inicio");
		table.addHeader("Fecha de Fin");
		table.addHeader("Tipo de Poliza");
		table.addHeader("Valor de la Prima");
		table.addHeader("Seccion");
	
	for (Iterator i = polizas.iterator();i.hasNext();){
		
		Apolice poliza = (Apolice) i.next();
		
		table.add(new SimpleDateFormat("dd/MM/yyyy").format(poliza.obterDataPrevistaInicio()));
		table.add(new SimpleDateFormat("dd/MM/yyyy").format(poliza.obterDataPrevistaConclusao()));
		table.add(poliza.obterTipo());
		
		table.add(new Label(poliza.obterPrimaGs()));
		table.add(poliza.obterSecao().obterNome());
	}
	}

	Button sigte = new Button("Siguiente",new Action("visualizarPaginaPrueba"));
	Button anterior = new Button("Anterior",new Action("visualizarPaginaPrueba"));
	
	if(polizas.size() > 0 ) {
		
		sigte.getAction().add("Lista",true);
		table2.addFooter(sigte);
		sigte.getAction().add("Pagina",pagina + 1);
		
		if(pagina > 2) {
			anterior.getAction().add("Pagina",pagina - 1);
			anterior.getAction().add("Lista",true);
			table2.addFooter(anterior);
		}
			
	}
	
	
	Button boton = new Button("Buscar",new Action("visualizarPaginaPrueba"));
	boton.getAction().add("Lista",true);
	
	table2.setNextColSpan(table2.getColumns());
	table2.setNextHAlign(Table.HALIGN_CENTER);
	table2.add(boton);
	
	
	table2.setNextColSpan(table2.getColumns());
	table.addStyle(Table.STYLE_ALTERNATE);
	table2.add(table);	
	
	
		return table2;
	}


	public String getSelectedGroup() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getSelectedOption()
	 */
	public String getSelectedOption() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getTitle()
	 */
	public View getTitle() throws Exception {
		return new Label("Listado de Polizas por Aseguradora");
	}

	/* (non-Javadoc)
	 * @see com.gvs.crm.view.PortalView#getOrigemMenu()
	 */
	public Entidade getOrigemMenu() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
