package com.spring.fleamarket.domain.chat.service;

import java.util.Date;
import java.util.List;

import com.spring.fleamarket.domain.chat.model.MessageModel;

public interface MessageManageService {

	public void saveMessage(MessageModel msgModel);
	
	public List<MessageModel> recallChat(MessageModel msgModel);
	
	public Date checkTime(String content);
		
}
