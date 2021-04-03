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

package io.github.manojpawar94;

import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 
 * @author manojpawar
 *
 */
public abstract class Jcraft {
	private final JSch jSch = new JSch();
	private static final int TERMINAL_WIDTH = 1000;
	private static final int TERMINAL_WIDTH_IN_PIXELS = 1000;
	private static final int TERMINAL_HEIGHT = 1000;
	private static final int TERMINAL_HEIGHT_IN_PIXELS = 1000;

	protected int maxRetry = 5;
	protected int waitingTimeout = 1000;

	/**
	 * Configure the {@link Session} object instance based upon the attribute set in
	 * the {@link JSshProxy}. It doesn't initialise/connect the {@link Session}. To
	 * initialise/connect {@link Session} one must call {@code session.connect()}.
	 * 
	 * @param jSshProxy {@link JSshProxy} object instance
	 * @return It returns the Session instance. It is ready to use by calling
	 *         {@code session.connect()}
	 * @throws JSchException
	 */
	protected Session configureSession(JSshProxy<?> jSshProxy) throws JSchException {
		Session session = jSch.getSession(jSshProxy.getUsername(), jSshProxy.getHostname(), jSshProxy.getPort());

		if (StringUtils.isNotEmpty(jSshProxy.getPrivateKey())) {
			jSch.addIdentity(jSshProxy.getPrivateKey());
		} else {
			session.setPassword(jSshProxy.getPassword().toString());
		}

		if (StringUtils.isNotBlank(jSshProxy.getKnownHostsFileName())) {
			jSch.setKnownHosts(jSshProxy.getKnownHostsFileName());
		} else {
			session.setConfig("StrictHostKeyChecking", "no");
		}

		if (Objects.nonNull(jSshProxy.getProperties())) {
			session.setConfig("compression.s2c", jSshProxy.getProperties().getProperty("compression.s2c", "zlib,none"));
			session.setConfig("compression.c2s", jSshProxy.getProperties().getProperty("compression.c2s", "zlib,none"));
			jSshProxy.getProperties().remove("compression.s2c");
			jSshProxy.getProperties().remove("compression.c2s");

			Set<String> keys = jSshProxy.getProperties().stringPropertyNames();
			for (String key : keys) {
				session.setConfig(key, jSshProxy.getProperties().getProperty(key));
			}
		} else {
			session.setConfig("compression.s2c", "zlib,none");
			session.setConfig("compression.c2s", "zlib,none");
		}

		return session;
	}

	/**
	 * 
	 * @param session   {@link Session} instance {@code session.connect()} must be
	 *                  on {@link Session} object instance before passing to the
	 *                  method
	 * @param jSshProxy {@link JSshProxy} object instance
	 * @return
	 * @throws JSchException
	 */
	protected ChannelExec configureExecChannel(Session session, JSshProxy<?> jSshProxy) throws JSchException {
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setPty(jSshProxy.isEnablePty());
		return channelExec;
	}

	/**
	 * 
	 * @param session   {@link Session} instance {@code session.connect()} must be
	 *                  on {@link Session} object instance before passing to the
	 *                  method
	 * @param jSshProxy {@link JSshProxy} object instance
	 * @return
	 * @throws JSchException
	 */
	protected ChannelShell configureShellChannel(Session session, JSshProxy<?> jSshProxy) throws JSchException {
		ChannelShell channelShell = (ChannelShell) session.openChannel("shell");
		;
		channelShell.setPty(jSshProxy.isEnablePty());
		channelShell.setPtyType(jSshProxy.getPtyType(), TERMINAL_WIDTH, TERMINAL_HEIGHT, TERMINAL_WIDTH_IN_PIXELS,
				TERMINAL_HEIGHT_IN_PIXELS);
		return channelShell;
	}

	/**
	 * 
	 * @param session   {@link Session} instance {@code session.connect()} must be
	 *                  on {@link Session} object instance before passing to the
	 *                  method
	 * @param jSshProxy {@link JSshProxy} object instance
	 * @return
	 * @throws JSchException
	 */
	protected ChannelSftp configureSftpChannel(Session session, JSshProxy<?> jSshProxy) throws JSchException {
		ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
		return channelSftp;
	}

}
