package com.bigid.web.common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageResolver implements ApplicationContextAware {

	private static MessageSourceAccessor messages;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		MessageSource source = applicationContext.getBean("messageSource",
				ReloadableResourceBundleMessageSource.class);
		messages = new MessageSourceAccessor(source);
	}

	/**
	 * Retrieve the message for the given code. Uses default Locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param args
	 *            arguments for the message, or <code>null</code> if none
	 * @return the message
	 * @throws org.springframework.context.NoSuchMessageException
	 *             if not found
	 */
	public static String getMessage(String code, Object[] args) {
		return messages.getMessage(code, args);
	}

	/**
	 * Retrieve the message for the given code. Uses the default locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param args
	 *            arguments for the message, or <code>null</code> if none
	 * @param defaultMessage
	 *            String to return if the lookup fails
	 * @return the message
	 */
	public static String getMessage(String code, Object[] args,
			String defaultMessage) {
		return messages.getMessage(code, args, defaultMessage);
	}

	/**
	 * Retrieve the message for the given code. Uses the default locale.
	 * 
	 * @param code
	 *            code of the message
	 * @param defaultMessage
	 *            String to return if the lookup fails
	 * @return the message
	 */
	public static String getMessage(String code, String defaultMessage) {
		return getMessage(code, null, defaultMessage);
	}

	/**
	 * Retrieve the message for the given code. Uses the default locale and no
	 * arguments.
	 * 
	 * @param code
	 *            code of the message
	 * @return the message
	 */
	public static String getMessage(String code) {
		return getMessage(code, null, null);
	}
}