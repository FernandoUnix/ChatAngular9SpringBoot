package com.chatspringboot.controller;

import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.chatspringboot.model.documento.Mensagem;
import com.chatspringboot.model.service.ChatService;

@Controller
public class ChatController {

	private String[] cores = {"red", "green", "blue", "pink","black", "gray"};
	
	@Autowired
	private ChatService chatService ;
	
	@Autowired
	private SimpMessagingTemplate websocket;
	
	@MessageMapping("/mensagem")
	@SendTo("/chat/mensagem")
	public Mensagem receberMensagem(Mensagem mensagem) {
		mensagem.setData(new Date().getTime());
		
		if(mensagem.getTipo().equals("Novo_Usuario")) {
			mensagem.setCor(cores[new Random().nextInt(cores.length)]);
			mensagem.setTexto("novo usuário");
		}else {
			chatService.salvar(mensagem);
		}
		
		return mensagem;
	}
	
	@MessageMapping("/escrevendo")
	@SendTo("/chat/escrevendo")
	public String estaEsrevendo(String usuario) {
		return usuario.concat(" esta escrevendo");
	}
	
	@MessageMapping("/historico")
	private void historico(String clienteId){
		websocket.convertAndSend("/chat/historico/"+clienteId,chatService.obterUltimas10Mensagens() );
	}
}
