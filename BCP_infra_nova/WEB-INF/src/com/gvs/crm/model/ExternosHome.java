package com.gvs.crm.model;

import java.util.Collection;
import java.util.Date;

public interface ExternosHome 
{
	boolean possuemAgendasNoPerido(Date data) throws Exception;
	Collection obterTodasAsContas() throws Exception;
	
}
