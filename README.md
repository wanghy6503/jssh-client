# jssh-client

<<<<<<< HEAD
The Jssh-client is Java based library build on top of the Jsch Library to perform operation on the remote server over SSH and SFTP channel. It has the following features,
<ul>
<li>Test SSH connection with remote SSH server</li>
<li>Execute a command on remote server using Execute Channel</li>
<li>Execute commands on remote server using Shell channel</li>
<li>Upload/Download the file to/from the remote server from/to local using SFTP channel</li>
</ul>

## How to use?

### Step 1: Add Maven Dependency to POM.xml [use lastest version]

=======
The JSsh-client is a Java-based library build on top of the Jsch Library to perform an operation on the remote server over SSH and SFTP channel. It has the following features,

* Test SSH connection with a remote SSH server
* Execute a command on a remote server using Execute Channel
* Execute commands on a remote server using Shell channel
* Upload/Download the file to/from the remote server from/to local using the SFTP channel

## How to use it?

### Step 1: Add Maven Dependency to POM.xml [use lastest version]
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7
Current version: 0.0.2

```
<dependency>
<<<<<<< HEAD
	<groupId>io.github.manojpawar94</groupId>
	<artifactId>jssh-client</artifactId>
	<version>${current_verion}}</version>
=======
    <groupId>io.github.manojpawar94</groupId>
    <artifactId>jssh-client</artifactId>
    <version>${current_verion}</version>
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7
</dependency>
```

### Step 2: Get Instance of JsshClient
<<<<<<< HEAD
JsshClient is an interface. It has a singleton implementation class JscraftClient. We can get instance of class as below
=======
JsshClient is an interface. It has a singleton implementation class JscraftClient. We can get an instance of the class as below
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

```
JSshClient client = JcraftClient.getInstance();
```

### Step 3: Create the JsshProxy Object instance
<<<<<<< HEAD
It has various options to set based upon our need. It support both password based and SSH key based authentication mechnism. To set authentication mechnism we need to set respective attributes to the JSshProxy object.

```
JSshProxy<?> jSshProxy = JSshProxy.builder()
					.hostname("123.12.34.5")
					.username("test")
					.password("test@123")
					.enablePty(true)
					.build();
=======
It has various options to set based upon our needs. It supports both password-based and SSH key-based authentication mechanism. To set authentication mechanism we need to set respective attributes to the JSshProxy object.

```
java.util.Properties properties = new java.util.Properties();
JSshProxy<?> jSshProxy = JSshProxy.builder()
                    .hostname("123.12.34.5")
                    .port(22)
                    .username("test")
                    .password("test@123")
                    //.privateKey("")
                    .knownHostsFileName("")
                    .sessionTimeOut(10000)
                    .channelTimeOut(5000)
                    .properties(properties)
                    .enablePty(true)
                    //.ptyType("ANSI") used only for shell channel operation
                    .build();
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7
```

### Step 4a: Test SSH connection with Remote Server

```
JSshProxy<?> jSshProxy = JSshProxy.builder()
<<<<<<< HEAD
					.hostname("123.12.34.5")
					.username("test")
					.password("test@123")
					.enablePty(true)
					.build();
=======
                    .hostname("123.12.34.5")
                    .username("test")
                    .password("test@123")
                    .enablePty(true)
                    .build();
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

JSshResult jSshResult = client.testSSH(jSshProxy);
```

<<<<<<< HEAD
### Step 4b: Execute a command on remote server using Execute Channel
=======
### Step 4b: Execute a command on a remote server using Execute Channel
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

```
String command = "ls -lrt";
JSshProxy<String> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
<<<<<<< HEAD
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
=======
                    .task(command)
                                        .build();
JSshResult jSshResult = client.execute(jSshProxy);
```

### Step 4c: Execute a command on a remote server using Execute Channel

```
ShellCommand command = ShellCommand.builder()
                .command("ls -lrt")
                .command("pwd")
                .command("who i am")
                .build();
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

JSshProxy<ShellCommand> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
                                        .task(command)
                                        .build();
JSshResult jSshResult = client.shell(jSshProxy);
<<<<<<< HEAD
``` 

### Step 4d: Upload the file to the remote server from local using SFTP channel
=======
```

### Step 4d: Upload the file to the remote server from local using the SFTP channel
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

```
String from = "C:\\Users\\sample_file_to_upload.txt";
String to = "/home/user/upload";

SftpCommand command = SftpCommand.builder()
<<<<<<< HEAD
					.from(from)
					.to(to)
					.sftpType(SftpType.PUT)
					.build();
=======
                    .from(from)
                    .to(to)
                    .sftpType(SftpType.PUT)
                    .build();
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

JSshProxy<ShellCommand> jSshProxy = JSshProxy.builder()
                                        .hostname("123.12.34.5")
                                        .username("test")
                                        .password("test@123")
                                        .enablePty(true)
                                        .task(command)
                                        .build();
JSshResult jSshResult = client.sftp(jSshProxy);
```

<<<<<<< HEAD
### Step 4d: Download the file from the remote server to local using SFTP channel
=======
### Step 4e: Download the file from the remote server to local using the SFTP channel
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

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

<<<<<<< HEAD
### Credits

---
title: JSsh-Client Documentation
author: Manoj Pawar
date: today
---
=======
## Credits

Title: JSsh-Client Documentation<br>
Author: Manoj Pawar<br>
Date: 03 April, 2021<br>
>>>>>>> 34ff622889841c067d4098062949961d8494d2e7

