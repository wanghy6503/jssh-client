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

import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.github.manojpawar94.SftpCommand.SftpType;

/**
 * 
 * @author manojpawar
 *
 */
class JcraftClientTest {

	Logger logger = Logger.getLogger(JcraftClientTest.class.getName());
	JSshClient client = JcraftClient.getInstance();

	@Disabled
	@Test
	void testSshServer() {

		JSshProxy<?> jSshProxy = JSshProxy.builder().hostname("123.12.34.5").username("test").password("test@123")
				.enablePty(true).build();

		JSshResult jSshResult = client.testSSH(jSshProxy);

		Assertions.assertNotNull(jSshResult);
		Assertions.assertTrue(jSshResult.isStatus());
		logger.info(jSshResult.getValue());
	}

	@Disabled
	@Test
	void testExecuteCommands() {
		String command = "ls -lrt";
		JSshResult jSshResult = client.execute(getProxy(command));

		Assertions.assertNotNull(jSshResult);
		Assertions.assertTrue(jSshResult.isStatus());
		Assertions.assertNull(jSshResult.getError());
		logger.info(jSshResult.getValue());
	}

	@Disabled
	@Test
	void testShellCommands() {
		ShellCommand command = ShellCommand.builder().command("ls -lrt").command("pwd").command("who i am").build();
		JSshResult jSshResult = client.shell(getProxy(command));

		Assertions.assertNotNull(jSshResult);
		Assertions.assertTrue(jSshResult.isStatus());
		Assertions.assertNull(jSshResult.getError());
		logger.info(jSshResult.getValue());
	}

	@Disabled
	@Test
	void testCopyToRemote() {
		SftpCommand command = SftpCommand.builder().from("C:/Users/manojpawar/sample_file.txt")
				.to("/home/manojpawar/uploads").sftpType(SftpType.PUT).build();
		JSshResult jSshResult = client.sftp(getProxy(command));

		Assertions.assertNotNull(jSshResult);
		Assertions.assertTrue(jSshResult.isStatus());
		Assertions.assertNull(jSshResult.getError());
		logger.info(jSshResult.getValue());
	}

	@Test
	void testCopyFromRemote() {
		SftpCommand command = SftpCommand.builder().from("/home/manojpawar/uploads/sample_file.txt")
				.to("C:/Users/manojpawar/Downloads").sftpType(SftpType.GET).build();
		JSshResult jSshResult = client.testSSH(getProxy(command));

		Assertions.assertNotNull(jSshResult);
		Assertions.assertTrue(jSshResult.isStatus());
		Assertions.assertNull(jSshResult.getError());
		logger.info(jSshResult.getValue());
	}

	<T> JSshProxy<T> getProxy(T task) {
		return JSshProxy.<T>builder().hostname("123.12.34.5").username("test").password("test@123").enablePty(true)
				.task(task).build();
	}

}
