# jssh-client

The Jssh-client is Java based library build on top of the Jsch Library to perform operation on the remote server over SSH and SFTP channel. It has the following features,
<ul>
<li>Test SSH connection with remote SSH server</li>
<li>Execute a command on remote server using Execute Channel</li>
<li>Execute commands on remote server using Shell channel</li>
<li>Upload/Download the file to/from the remote server from/to local using SFTP channel</li>
</ul>

## How to use?

### Step 1: Add Maven Dependency to POM.xml [use lastest version]

Current version: 0.0.2

```
<dependency>
	<groupId>io.github.manojpawar94</groupId>
	<artifactId>jssh-client</artifactId>
	<version>${current_verion}}</version>
</dependency>
```

### Step 2: Get Instance of JsshClient
JsshClient is an interface. It has a singleton implementation class JscraftClient. We can get instance of class as below

```
JSshClient client = JcraftClient.getInstance();
```

### Step 3: Create the JsshProxy Object instance
It has various options to set based upon our need. It support both password based and SSH key based authentication mechnism. To set authentication mechnism we need to set respective attributes to the JSshProxy object.

```
JSshProxy<?> jSshProxy = JSshProxy.builder()
					.hostname("123.12.34.5")
					.username("test")
					.password("test@123")
					.enablePty(true)
					.build();
```

### Step 4a: Test SSH connection with Remote Server

```
JSshProxy<?> jSshProxy = JSshProxy.builder()
					.hostname("123.12.34.5")
					.username("test")
					.password("test@123")
					.enablePty(true)
					.build();

JSshResult jSshResult = client.testSSH(jSshProxy);
```

### Step 4b: Execute a command on remote server using Execute Channel

```
String command = "ls -lrt";
JSshProxy<String> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
					.task(command)
                                        .build();
JSshResult jSshResult = client.execute(jSshProxy);
``` 

### Step 4c: Execute a command on remote server using Execute Channel

```
ShellCommand command = ShellCommand.builder()
				.command("ls -lrt")
				.command("pwd")
				.command("who i am")
				.build();

JSshProxy<ShellCommand> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
                                        .task(command)
                                        .build();
JSshResult jSshResult = client.shell(jSshProxy);
``` 

### Step 4d: Upload the file to the remote server from local using SFTP channel

```
String from = "C:\\Users\\sample_file_to_upload.txt";
String to = "/home/user/upload";

SftpCommand command = SftpCommand.builder()
					.from(from)
					.to(to)
					.sftpType(SftpType.PUT)
					.build();

JSshProxy<ShellCommand> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
                                        .task(command)
                                        .build();
JSshResult jSshResult = client.sftp(jSshProxy);
```

### Step 4d: Download the file from the remote server to local using SFTP channel

```
String from = "/home/user/sample_file_to_download.txt";
String to = "C:\\Users\\Downloads";

SftpCommand command = SftpCommand.builder()
                                        .from(from)
                                        .to(to)
                                        .sftpType(SftpType.GET)
                                        .build();

JSshProxy<ShellCommand> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
                                        .task(command)
                                        .build();
JSshResult jSshResult = client.sftp(jSshProxy);
```

### Credits

---
title: JSsh-Client Documentation
author: Manoj Pawar
date: today
---

