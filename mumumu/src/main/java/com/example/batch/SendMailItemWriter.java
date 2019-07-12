package com.example.batch;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendMailItemWriter implements ItemWriter<SendMailData>{

	private final JavaMailSender mailSender;
	private final MailSendInfDao mailSendInfDao;
	private final MailSendRsltDao mailSendRsltDao;
	
	private final Clock clock;
	
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void write(List<? extends SendMailData> sendMailDatas)vthrows Exception {
		for(SendMailData sendMailData : sendMailDatas) {
			mailSender.send(sendMailData.getMimeMessage());
			
		}catch(Exception e) {
			throw e;
		}
		try {
			mailSendRsltDao.updateStatusSending(sendMailData.getMsgId(), 
					sendMailData.getServerId(), LocalDateTime.now(clock));
			int deleteCount = mailSendInfDao.deleteByMsgId(sendMailData.getMsgId());
		}catch(Exception e) {
			throw e;
		}
	}
	
}
