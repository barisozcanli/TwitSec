package com.peace.twitsec.service.impl;

import com.peace.twitsec.app.common.pool.TwitSecThreadPool;
import com.peace.twitsec.app.util.MailSender;
import com.peace.twitsec.service.MailService;
import com.peace.twitsec.service.TwitSecService;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl extends TwitSecService implements MailService {

	@Override
	public void sendMail(final String email, final String subject, final String body) {

		TwitSecThreadPool.EMAIL_POOL.runTask(new Runnable() {

			@Override
			public void run() {
				MailSender.getInstance().sendMail(email, subject, body);
			}
		});
	}
}
