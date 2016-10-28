package com.gvs.crm.control;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.ExternosHome;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.RelTodasAsContasXLS;
import com.gvs.crm.view.RelatorioSetorEconomicoView;

import infra.control.Action;
import infra.control.Control;

public class ExternosControl extends Control 
{
	public void visualizarRelEconomico(Action action) throws Exception
	{
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		ExternosHome externosHome = (ExternosHome) mm.getHome("ExternosHome");
		EventoHome eventoHome = (EventoHome) mm.getHome("EventoHome");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		AseguradoraHome home = (AseguradoraHome) mm.getHome("AseguradoraHome");
		Date data = action.getDate("data");
		
		mm.beginTransaction();
		try 
		{
			if(action.getBoolean("view"))
				this.setResponseView(new RelatorioSetorEconomicoView(data));
			else
			{
				if(data == null)
					throw new Exception("Feche en blanco");
				
				if(!externosHome.possuemAgendasNoPerido(data))
					throw new Exception("Alguma Aseguradora não enviou os dados da Contabilidade, não é possível gerar o relatório");
				
				String aseguradorasMenor80 = "";
				int cont = 0;
				
				for(Iterator i = home.obterAseguradorasPorMenor80OrdenadoPorNome().iterator() ; i.hasNext() ; )
				{
					Aseguradora aseg = (Aseguradora) i.next();
					
					if(aseg.obterId()!=5205)
					{
						if(cont == 0)
							aseguradorasMenor80+="(seguradora=" + aseg.obterId();
						else
							aseguradorasMenor80+=" or seguradora=" + aseg.obterId();
						
						cont++;
					}
				}
				
				aseguradorasMenor80+=")";
				
				RelTodasAsContasXLS xls = new RelTodasAsContasXLS(data, externosHome.obterTodasAsContas(), aseguradorasMenor80);
				
				InputStream arquivo = xls.obterArquivo();
				String nome = "Setor Economico - " + new SimpleDateFormat("dd/MM/yyyy").format(data) + ".xls";
				String mime = "application/vnd.ms-excel";
				
				this.setResponseInputStream(arquivo);
		        this.setResponseFileName(nome);
		        this.setResponseContentType(mime);
		        this.setResponseContentSize(arquivo.available());
				
			}
			mm.commitTransaction();

		}
		catch (Exception exception)
		{
			this.setAlert(Util.translateException(exception));
			this.setResponseView(new RelatorioSetorEconomicoView(data));
			mm.rollbackTransaction();
		}
	}
}
