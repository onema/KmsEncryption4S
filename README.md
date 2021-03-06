KmsEncryption4S
================
[![Build Status](https://travis-ci.org/onema/KmsEncryption4S.svg?branch=master)](https://travis-ci.org/onema/KmsEncryption4S)

Summary
--------
Tool to encrypt and decrypt files and strings using AWS KMS Data Keys.

Requirements
------------

- Java 8
- SBT 0.13
- [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)
    - Paste the contents of the `jrce_policy-8` (the `local_policy.jar` and the `US_export_policy.jar`) to the following directory: 
      - Mac OSX: `/Library/Java/JavaVirtualMachines/<jdk_version>/Contents/Home/jre/lib/security`
      - Windows: 
        - `C:\Program Files\Java\<jdk_version>\jre\lib\security`
        - `C:\Program Files (x86)\Java\<jdk_version>\lib\securityy`

Install 
-------

### Download package
Download the latest package for MAC OSX or Windows [here](https://github.com/onema/KmsEncryption4S/releases)

### Compile from project
Alternatively you can compile and install the project

**Mac OSX**
Run `./build.sh install`, this will compile the project and create a `crypto` command that can be used globally

**Windows**
Use SBT to compile and create the jar like such:

```bash
sbt compile
sbt assembly
```

You can then run the crypto command:
```bash
.\crypto.cmd --help
```

Usage
-----

**Encrypting strings:**

> **NOTE:** For windows use the provided `crypto.cmd` to run the application from PowerShell

```bash
crypto --data foo --encrypt --key alias/developer-encryption-key --profile dev
```

**Decrypting string:**
```bash
crypto --data AYAD+...= --decrypt --key alias/developer-encryption-key --profile dev
```

**Encrypting files:**
```bash
crypto --file ~/my_file.txt --encrypt --key alias/developer-encryption-key --profile dev
```

This will output a file `~/my_file.txt.enc`

**Decrypting files:**
```bash
crypto --file ~/my_file.txt.enc --decrypt --key alias/developer-encryption-key --profile dev
```

Options
-------

| Argument Name     | Required                                  | Description                |
| :---------------- | :---------------------------------------- | :------------------------- |
| `--key` `-k`      | YES                                       | KMS CMK Key ARN or Alias.  |
| `--data` `-d`     | Either `data` or `file` is required       | Data to encrypt or decrypt. This will output an encrypted or decrypted string. |
| `--file` `-f`     | Either `data` or `file` is required       | File to encrypt or decrypt. This will outpu an encrypted or decrypted file.    |
| `--profile` `-p`  | NO, defaults to `default`                 | AWS Profile to use.  |
| `--encrypt` `-E`  | Either `encrypt` or `decrypt` is required | Data or file should be encrypted.  |
| `--decrypt` `-D`  | Either `encrypt` or `decrypt` is required | Data or file should be decrypted.  |
