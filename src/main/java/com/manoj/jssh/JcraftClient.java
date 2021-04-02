/*
 * Copyright 2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manoj.jssh;

import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.manoj.jssh.SftpCommand.SftpType;

/**
 * 
 * @author manojpawar
 *
 */
public class JcraftClient extends Jcraft implements JSshClient {

	private static JcraftClient instance;

	private JcraftClient() {
	}

	@Override
	public JSshResult testSSH(JSshProxy<?> jSshProxy) {
		Session session = null;
		try {
			session = configureSession(jSshProxy);
			session.connect(jSshProxy.getSessionTimeOut());
			return JSshResult.builder().status(true)
					.value("SSH Connection established successfully with host " + jSshProxy.getHostname() + ".")
					.build();
		} catch (JSchException e) {
			e.printStackTrace();
			return JSshResult.builder().status(false).value("Failed to establish SSH connection with host "
					+ jSshProxy.getHostname() + ". Error: " + e.getMessage()).error(e).build();
		} finally {
			if (Objects.nonNull(session))
				session.disconnect();
		}
	}

	@Override
	public JSshResult execute(JSshProxy<String> commandProxy) {
		Session session = null;
		ChannelExec channelExec = null;
		StringBuilder resultText = new StringBuilder();
		try {
			session = configureSession(commandProxy);
			session.connect(commandProxy.getSessionTimeOut());

			channelExec = configureChannel(ChannelExec.class, session, commandProxy);
			channelExec.setCommand(commandProxy.getTask());
			InputStream commandOutput = channelExec.getInputStream();
			channelExec.connect(commandProxy.getChannelTimeOut());

			int readByte = commandOutput.read();

			while (readByte != 0xffffffff) {
				resultText.append((char) readByte);
				readByte = commandOutput.read();
			}

			return JSshResult.builder().status(true).value(resultText.toString()).build();
		} catch (JSchException | IOException e) {
			resultText.append(e.getLocalizedMessage());
			Arrays.stream(e.getStackTrace()).forEach(resultText::append);
			return JSshResult.builder().status(false).value(resultText.toString()).error(e).build();
		} finally {
			if (Objects.nonNull(channelExec))
				channelExec.disconnect();
			if (Objects.nonNull(session))
				session.disconnect();
		}
	}

	@Override
	public JSshResult shell(JSshProxy<ShellCommand> shellProxy) {
		Session session = null;
		ChannelShell channelShell = null;
		StringBuilder resultText = new StringBuilder();
		try {
			session = configureSession(shellProxy);
			session.connect(shellProxy.getSessionTimeOut());

			channelShell = configureChannel(ChannelShell.class, session, shellProxy);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			channelShell.setOutputStream(outputStream);
			PrintStream printStream = new PrintStream(channelShell.getOutputStream());
			channelShell.connect(shellProxy.getChannelTimeOut());

			for (String command : shellProxy.getTask().getCommands()) {
				resultText.append(sendCommand(printStream, outputStream, command, shellProxy.getTask().getMaxRetry(),
						shellProxy.getTask().getWaitingTimeout()));
			}

			return JSshResult.builder().status(true).value(resultText.toString()).build();
		} catch (JSchException | IOException | InterruptedException e) {
			resultText.append(e.getLocalizedMessage());
			Arrays.stream(e.getStackTrace()).forEach(resultText::append);
			return JSshResult.builder().status(false).value(resultText.toString()).error(e).build();
		} finally {
			if (Objects.nonNull(channelShell))
				channelShell.disconnect();
			if (Objects.nonNull(session))
				session.disconnect();
		}
	}

	private String sendCommand(PrintStream stream, ByteArrayOutputStream outputStream, final String command,
			final int maxRetry, final int waitingTimeout) throws JSchException, InterruptedException, IOException {
		stream.print(command + StringUtils.LF);
		stream.flush();
		return listenToPrompt(outputStream, maxRetry, waitingTimeout);
	}

	private String listenToPrompt(ByteArrayOutputStream outputStream, final int maxRetry, final int waitingTimeout)
			throws InterruptedException, IOException {
		for (int retryIndex = 0; retryIndex < maxRetry; retryIndex++) {
			Thread.sleep(waitingTimeout);
			String tempBuffer = StringUtils.stripAccents(outputStream.toString("UTF-8").trim());
			if (StringUtils.endsWith(tempBuffer, "$") || StringUtils.endsWith(tempBuffer, "#")
					|| StringUtils.endsWith(tempBuffer, ">") || StringUtils.endsWith(tempBuffer, "%")) {
				outputStream.reset();
				return tempBuffer;
			}
		}
		return StringUtils.EMPTY;
	}

	@Override
	public JSshResult sftp(JSshProxy<SftpCommand> jSshProxy) {
		Session session = null;
		ChannelSftp channelSftp = null;
		try {
			URI from = URI.create(jSshProxy.getTask().getFrom());
			URI to = URI.create(jSshProxy.getTask().getTo());

			session = configureSession(jSshProxy);
			session.connect(jSshProxy.getSessionTimeOut());

			channelSftp = configureChannel(ChannelSftp.class, session, jSshProxy);
			channelSftp.connect(jSshProxy.getChannelTimeOut());

			if (SftpType.PUT.equals(jSshProxy.getTask().getSftpType()))
				return upload(channelSftp, from, to);
			else if (SftpType.GET.equals(jSshProxy.getTask().getSftpType()))
				return download(channelSftp, from, to);
			else
				return JSshResult.builder().status(true).value("Illegal SFTP Type")
						.error(new IllegalArgumentException("")).build();

		} catch (JSchException e) {
			return JSshResult.builder().status(false).value("").error(e).build();
		} finally {
			if (Objects.nonNull(channelSftp))
				channelSftp.disconnect();
			if (Objects.nonNull(session))
				session.disconnect();
		}

	}

	private JSshResult upload(ChannelSftp channelSftp, URI from, URI to) {
		try (FileInputStream fis = new FileInputStream(new File(from))) {
			channelSftp.cd(to.getPath());
			channelSftp.put(fis, getName(from.getPath()));
			return JSshResult.builder().status(true).value("File has uploaded").build();
		} catch (IOException | SftpException e) {
			return JSshResult.builder().status(false).value("Cannot upload the file").error(e).build();
		}
	}

	private JSshResult download(ChannelSftp channelSftp, URI from, URI to) {
		File out = new File(new File(to), getName(from.getPath()));
		try (OutputStream os = new FileOutputStream(out); BufferedOutputStream bos = new BufferedOutputStream(os)) {
			channelSftp.cd(getFullPath(from.getPath()));
			channelSftp.get(getName(from.getPath()), bos);
			return JSshResult.builder().status(true).value("File has downloaded").build();
		} catch (IOException | SftpException e) {
			return JSshResult.builder().status(false).value("Cannot download the file").error(e).build();
		}
	}

	public static JcraftClient getInstance() {
		if (Objects.isNull(instance)) {
			synchronized (instance) {
				instance = new JcraftClient();
			}
		}
		return instance;
	}

}
