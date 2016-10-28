package com.gvs.crm.component;

import infra.view.Select;

public class TipoInstrumentosSelect extends Select
{
	public TipoInstrumentosSelect(String nome, String valor)
	{
		super(nome,1);
		
		this.add("Todas", "0", false);
		this.add("Póliza Individual", "Póliza Individual", "Póliza Individual".equals(valor));
		this.add("Póliza Madre", "Póliza Madre", "Póliza Madre".equals(valor));
		this.add("Certificado de Seguro Colectivo", "Certificado de Seguro Colectivo", "Certificado de Seguro Colectivo".equals(valor));
		this.add("Certificado Provisorio", "Certificado Provisorio", "Certificado Provisorio".equals(valor));
		this.add("Nota de Cobertura de Reaseguro", "Nota de Cobertura de Reaseguro", "Nota de Cobertura de Reaseguro".equals(valor));
	}
}
