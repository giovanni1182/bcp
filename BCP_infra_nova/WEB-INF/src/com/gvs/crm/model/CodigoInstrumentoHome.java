package com.gvs.crm.model;

import java.util.Collection;

public interface CodigoInstrumentoHome 
{
	CodigoInstrumento obterCodigoInstrumento(int codigo) throws Exception;
	Collection<CodigoInstrumento> obterCodigosInstrumento() throws Exception;
}
