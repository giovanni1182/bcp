package com.gvs.crm.avaliador;

import java.util.Collection;
import java.util.HashMap;

public class Atributos {
	private HashMap hashMap;

	public Atributos() {
		this.hashMap = new HashMap();
	}

	public void adicionarAtributo(Atributo atributo) {
		this.hashMap.put(atributo.obterNome(), atributo);
	}

	public Atributo obterAtributo(String nome) throws Exception {
		return (Atributo) this.hashMap.get(nome);
	}

	public Collection obterAtributos() {
		return this.hashMap.values();
	}
}