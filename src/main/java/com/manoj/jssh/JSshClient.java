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

/**
 * 
 * @author manojpawar
 *
 */
public interface JSshClient {

	/**
	 * To test the SSH connection with Remote server.
	 * 
	 * @param jSshProxy {@link JSshProxy<?>} instance
	 * @return It return the {@link JSshResult} object instance. The
	 *         {@link JSshResult} provide the details
	 *         {@code Code, Message, Exception}
	 */
	JSshResult testSSH(JSshProxy<?> jSshProxy);

	/**
	 * To execute the command on remote server using SSH Execute Channel.
	 * 
	 * @param jSshProxy {@link JSshProxy<String>} instance
	 * @return It return the {@link JSshResult} object instance. The
	 *         {@link JSshResult} provide the details
	 *         {@code Status, Message, Exception}
	 */
	JSshResult execute(JSshProxy<String> commandProxy);

	/**
	 * To execute the multiple commands on remote server using SSH Shell Channel.
	 * 
	 * @param jSshProxy {@link JSshProxy<ShellCommand>} instance
	 * @return It return the {@link JSshResult} object instance. The
	 *         {@link JSshResult} provide the details
	 *         {@code Status, Message, Exception}
	 */
	JSshResult shell(JSshProxy<ShellCommand> shellProxy);

	/**
	 * To execute the PUT/GET file on remote server using SSH SFTP Channel.
	 * 
	 * @param jSshProxy {@link JSshProxy<SftpCommand>} instance
	 * @return It return the {@link JSshResult} object instance. The
	 *         {@link JSshResult} provide the details
	 *         {@code Status, Message, Exception}
	 */
	JSshResult sftp(JSshProxy<SftpCommand> sftpProxy);

}
