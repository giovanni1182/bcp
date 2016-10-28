package com.gvs.crm.control;

import java.util.ArrayList;
import java.util.Collection;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.model.impl.Pruebaimpl;

import infra.control.Action;
import infra.control.Control;


public class Prueba_control extends Control {
	public void visualizarPaginaPrueba(Action action) throws Exception {
		CRMModelManager mm = new CRMModelManager(this.getUser());
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");
		
		Pruebaimpl pruebaimpl = (Pruebaimpl) mm.getHome("Pruebaimpl");
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuario_actual = (Usuario) usuarioHome.obterUsuarioPorUser(this
				.getUser());
		mm.beginTransaction();
		Collection poliza = new ArrayList();
		Aseguradora aseguradora = null;
		Collection plan_ctas = new ArrayList();
		int pagina = action.getInt("Pagina");
		
		try {
		
			if(pagina == 0){
				pagina = 1;
			}
			
			
			
			if(action.getBoolean("Lista")) {
					
				if(action.getLong("aseguradora_id") == 0)
					throw new Exception("La Aseguradora no puede ser Cero");
				aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora_id"));
			//Collection aseguradoras = pruebaimpl.GetAseguradoras();
//			if(action.getLong("aseguradora_id") == 0)
//				throw new Exception("La Aseguradora no puede ser Cero");
//			
//			if(action.getDate("Fecha_desde") == null)
//				throw new Exception("Debe ingresar la Fecha Desde");
//			
//			if(action.getDate("Fecha_hasta") == null)
//				throw new Exception("Debe ingresar la Fecha Hasta");
//			
//			aseguradora = (Aseguradora) entidadeHome.obterEntidadePorId(action.getLong("aseguradora_id"));
//			
//			poliza = aseguradora.obtenerPolizas(action.getDate("Fecha_desde"),action.getDate("Fecha_hasta"),pagina );
//			
		    plan_ctas = aseguradora.obtenerPlanCtas(action.getInt("Mes"), action.getInt("Year"));
			}			
			
			String mes =new Integer(action.getInt("Mes")).toString();
			if (mes.length()== 1)
				mes = "0"+mes;
			this.setResponseView(new PlanCtas_view (aseguradora, Integer.parseInt(mes), action.getInt("Year"),plan_ctas));
			mm.commitTransaction();

		} catch (Exception exception) {
			this.setResponseView(new PlanCtas_view(aseguradora, action.getInt("Mes"), action.getInt("Year"),plan_ctas));
			this.setAlert(Util.translateException(exception));
			mm.rollbackTransaction();
		}
}
}