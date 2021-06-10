package com.spring.fleamarket.domain.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.fleamarket.domain.account.service.AccountFindService;
import com.spring.fleamarket.domain.chat.model.MessageModel;
import com.spring.fleamarket.domain.chat.service.MessageManageService;
import com.spring.fleamarket.domain.model.Account;
import com.spring.fleamarket.global.security.annotation.LoginedAccount;

@RestController
@CrossOrigin
public class UsersController {
	
	@Autowired
	MessageManageService service;
	
	@GetMapping("/recallChat/{sender}/{receiver}")
	public List<MessageModel> bringUpMessageRecord(MessageModel msgModel, @PathVariable int sender, @PathVariable int receiver) {
			
		msgModel.setSenderId(sender);
		
		msgModel.setReceiverId(receiver);
			
		return service.recallChat(msgModel);
		
	}
	
	
}
