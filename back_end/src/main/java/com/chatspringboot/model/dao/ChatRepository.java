package com.chatspringboot.model.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.chatspringboot.model.documento.Mensagem;

public interface ChatRepository extends MongoRepository<Mensagem, String>{

	public List<Mensagem> findFirst10ByOrderByDataDesc();
	
}
