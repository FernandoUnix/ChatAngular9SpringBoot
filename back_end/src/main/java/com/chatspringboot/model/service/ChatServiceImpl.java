package com.chatspringboot.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatspringboot.model.dao.ChatRepository;
import com.chatspringboot.model.documento.Mensagem;

@Service
public class ChatServiceImpl implements ChatService{

	@Autowired
	private ChatRepository chatRepository;
	
	@Override
	public List<Mensagem> obterUltimas10Mensagens() {
		return chatRepository.findFirst10ByOrderByDataDesc();
	}

	@Override
	public Mensagem salvar(Mensagem mensagem) {
		return chatRepository.save(mensagem);
	}

}
