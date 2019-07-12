package com.example.batch;

import javax.batch.api.chunk.ItemProcessor;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendMailItemProcessor implements ItemProcessor<SendMailTargetData, SendMailData>{
	@Component
	@Setter
	@ConfigurationProperties("spring.mail")
	public static class DefaultMailConfig {
		private String host;
		private String port;
		private String username;
		private String password;		
	}
	private final JavaMailSender mailSender;
	private final MailProperties mailProperties;
	private final DefaultMailConfig defaultMailConfig;
	@Override
	public SendMailData process(SendMailTargetData item) throws Exception {
		((JavaMailSenderImpl) mailSender)
		.setHost(mailProperties.getProperty("host", defaultMailConfig.host));
		((JavaMailSenderImpl) mailSender)
		.setPort(mailProperties.getProperty("port", defaultMailConfig.port));
		((JavaMailSenderImpl) mailSender)
		.setUsername(mailProperties.getProperty("user", defaultMailConfig.username));
		((JavaMailSenderImpl) mailSender)
		.setPassword(mailProperties.getProperty("password", defaultMailConfig.password));
		MimeMessage message = mailSender.createMimeMessage();
		message.setHeader("Message-ID", String.format("<%s@%s>", item.getMsgId(), mailProperties.getProperty("domain")));
		
		MimeMessageHelper helper = new MimeMessageHelper(message, false);
		if (StringUtils.isEmpty(item.getPersonalName())) {
			helper.setFrom(item.getFromEmail());
		} else {
			helper.setFrom(item.getFromEmail(), item.getPersonalName());
		}
		helper.setTo(item.getToEmail());
		helper.setSubject(item.getSubject());
		helper.setText(item.getBody());
		
		SendMailData sendMailData = new SendMailData();
		sendMailData.setMimeMessage(message);
		sendMailData.setMsgId(item.getMsgId());
		sendMailData.setServerId(mailProperties.getProperty("serverId"));
		
		return sendMailData;
	}
}
