package com.spring.fleamarket.domain.chat.mapper;

import java.util.Date;
import java.util.List;

import com.spring.fleamarket.domain.chat.model.MessageModel;

public interface MessageManageMapper {

	public void saveMessage(MessageModel msgModel);
	
	public List<MessageModel> recallChat(MessageModel msgModel);
	
	public Date checkTime(int messageNo);

}
