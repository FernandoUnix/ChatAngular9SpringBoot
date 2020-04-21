package com.chatspringboot.model.service;

import java.util.List;

import com.chatspringboot.model.documento.Mensagem;

public interface ChatService {

	public List<Mensagem> obterUltimas10Mensagens();
	public Mensagem salvar(Mensagem mensagem);
}
