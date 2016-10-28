package com.gvs.crm.view;

import java.text.SimpleDateFormat;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.SinistroFiniquitadoCentralRisco;

import infra.security.User;
import infra.view.Label;
import infra.view.Table;

public class CentralRiscoSinistrosVigentesView extends Table 
{
	public CentralRiscoSinistrosVigentesView(User user, Collection<Aseguradora> aseguradoras, String nomeAsegurado, String documento) throws Exception
	{
		super(10);
		
		CRMModelManager mm = new CRMModelManager(user);
		AseguradoraHome aseguradoraHome = (AseguradoraHome) mm.getHome("AseguradoraHome");
		
		this.addStyle(Table.STYLE_ALTERNATE);
		this.setWidth("100%");
		
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Aseg.");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Fecha Corte");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Plan");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Cant. Siniestros");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Monto Estimado en GS");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("M/E");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Monto Estimado en M/E");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Cap. en Riesgo GS");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("M/E");
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("Cap. en Riesgo M/E");
		
		int j = 1;
		int totalQtde = 0;
		double totalMontanteGS = 0;
		double totalCapitalGS = 0;
		Collection<SinistroFiniquitadoCentralRisco> sinistros;
		String data;
		
		for(Aseguradora aseg : aseguradoras)
		{
			sinistros = aseguradoraHome.obterSinistrosVigentesCentralRisco(aseg, nomeAsegurado, documento); 
			
			for(SinistroFiniquitadoCentralRisco sinistro : sinistros)
			{
				data = new SimpleDateFormat("MM/yyyy").format(sinistro.obterDataCorte());
				
				this.setNextHAlign(HALIGN_CENTER);
				this.add(new Label(j));
				this.setNextHAlign(HALIGN_CENTER);
				this.add(data);
				this.add(sinistro.obterPlano().obterPlano());
				this.setNextHAlign(HALIGN_CENTER);
				this.add(new Label(sinistro.obterQtdeSinistros()));
				totalQtde+=sinistro.obterQtdeSinistros();
				this.setNextHAlign(HALIGN_RIGHT);
				this.add(new Label(sinistro.obterMontantePagoGs(),"#,##0.00"));
				totalMontanteGS+=sinistro.obterMontantePagoGs();
				if(sinistro.obterMoedaMontantePagoME().toLowerCase().indexOf("guara")>-1)
					this.add("");
				else
					this.add(sinistro.obterMoedaMontantePagoME());
				
				this.setNextHAlign(HALIGN_RIGHT);
				this.add(new Label(sinistro.obterMontantePagoME(),"#,##0.00"));
				
				this.setNextHAlign(HALIGN_RIGHT);
				this.add(new Label(sinistro.obterCapitalGs(),"#,##0.00"));
				totalCapitalGS+=sinistro.obterCapitalGs();
				if(sinistro.obterMoedaCapitalME().toLowerCase().indexOf("guara")>-1)
					this.add("");
				else
					this.add(sinistro.obterMoedaCapitalME());
				this.setNextHAlign(HALIGN_RIGHT);
				this.add(new Label(sinistro.obterCapitalME(),"#,##0.00"));
			}
			
			if(sinistros.size() > 0)
				j++;
			
			sinistros.clear();
		}
		
		this.setNextHAlign(HALIGN_CENTER);
		this.addHeader("TOTAL");
		this.addHeader("");
		this.addHeader("");
		Label label = new Label(totalQtde);
		label.setBold(true);
		this.setNextHAlign(HALIGN_CENTER);
		this.add(label);
		label = new Label(totalMontanteGS,"#,##0.00");
		label.setBold(true);
		this.setNextHAlign(HALIGN_RIGHT);
		this.add(label);
		this.addHeader("");
		this.addHeader("");
		label = new Label(totalCapitalGS,"#,##0.00");
		label.setBold(true);
		this.setNextHAlign(HALIGN_RIGHT);
		this.add(label);
		this.addHeader("");
		this.addHeader("");
	}
}
