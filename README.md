KmsEncryption4S
================
Tool to encrypt and decrypt files and strings using AWS KMS Data Keys.

Requirements
------------

- Java 8
- SBT 0.13
- [Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files](http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html)

Instal
------
Run `./install.sh`, this will compile the project and create a `crypto` command that can be used globally

Usage
-----

**Encrypting strings:**
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
