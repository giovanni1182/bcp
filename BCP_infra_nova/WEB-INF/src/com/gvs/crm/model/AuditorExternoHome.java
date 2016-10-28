package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface AuditorExternoHome {
	Collection obterAuditores() throws Exception;

	Collection obterAuditoresPorDataResolucao(Date dataResolucao) throws Exception;
	
	Collection obterAuditoresVigentes(Date data,boolean ci) throws Exception;
	Collection obterAuditoresNoVigentes(Date data,boolean ci) throws Exception;
}