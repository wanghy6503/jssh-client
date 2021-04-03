# jssh-client

The JSsh-client is a Java-based library build on top of the Jsch Library to perform an operation on the remote server over SSH and SFTP channel. It has the following features,

* Test SSH connection with a remote SSH server
* Execute a command on a remote server using Execute Channel
* Execute commands on a remote server using Shell channel
* Upload/Download the file to/from the remote server from/to local using the SFTP channel

## How to use it?

### Step 1: Add Maven Dependency to POM.xml [use lastest version]
Current version: 0.0.2

```
<dependency>
    <groupId>io.github.manojpawar94</groupId>
    <artifactId>jssh-client</artifactId>
    <version>${current_verion}</version>
</dependency>
```

### Step 2: Get Instance of JsshClient
JsshClient is an interface. It has a singleton implementation class JscraftClient. We can get an instance of the class as below

```
JSshClient client = JcraftClient.getInstance();
```

### Step 3: Create the JsshProxy Object instance
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

### Step 4b: Execute a command on a remote server using Execute Channel

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

### Step 4c: Execute a command on a remote server using Execute Channel

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

### Step 4d: Upload the file to the remote server from local using the SFTP channel

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

### Step 4e: Download the file from the remote server to local using the SFTP channel

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

## Credits

Title: JSsh-Client Documentation<br>
Author: Manoj Pawar<br>
Date: 03 April, 2021<br>

