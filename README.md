参考spring-cloud-config的作用，可以直接引用到springboot应用中，使得应用在启动时可以访问git服务器上的配置文件，读取为运行参数，方便应用与配置的分离

# 快速使用

## (1)在`build.gradle`中加入依赖 并在`springboot`项目的`application.yml`加入配置项

==过程暂无==

使用了springboot自动配置的特性，加入该依赖后，即可自动读取`application.yml`的相关配置，在启动时即可访问指定的git库，进行配置项读取。

## (2)简单配置如下

```yaml

git-config:
  # git uri
  uri: https://github.com/hskill/git-config
  # 用户名
  username: <your@email.com>
  # 密码
  password: ~
  # 配置文件
  files: application.yml, app-for-user.yml
```

注意读取的多个文件用逗号隔开。

# SSH访问git

用户名与密码存于配置文件中并不安全，因此可以通过部署公钥访问的方式

## (1)通过ssh-keygen生成公钥与私钥

## (2)在git服务器（github或gitee）部署公钥

## (3)配置项

配置（一）

```yaml
  # git uri
  uri: git@github.com/hskill/git-config
  # 配置文件
  files: application.yml, app-for-user.yml
  # 私钥文件在本机的位置
  privateKey: /User/<user>/.ssh/id_rsa
```


配置（二）

```yaml
  # git uri
  uri: git@gitee.com/hskill/config.git
  # 配置文件
  files: application.yml, app-for-user.yml
  # 私钥文件在本机的位置
  privateKey: |
  -----BEGIN RSA PRIVATE KEY-----
  MIIEpAIBAAKCAQEAsYeIlcxirAQsRLbcDNqM+PurxXmToFCtzLia7NFMS7zraWK5
  ua9Uh0hh48Uk1lZeQ4UYtqbsBMG3ir1rSGyGq40P7QkJPpGQ8YEm6bvK7HEwJ9ql
  Nwph+d7UQtPX+vEAQA86GyE5x6a/wowv4W7JZG2VKbySEq4JIkhvu3eRdUHsX8Qs
  LfuWTRyorEKqNXgtTf8Zmcl/BNAD7wlLRo5kia5CjvSWf95lEYRkmv6VFQ7KtPAH
  tdxqwqq4NC/TYegbFMutkkOq/r0j8tqIgAVrdEJmOe3gfQv8lG2kFHIq+GlP2fVn
  JK80KfTcj0L+4324324324324vxcsfdsfdsar3fdsfds3423fdsfdsa4343fdsfd
  yfrbyHRXEGBC4M6yf6yIHHlQtRx+A7EMjaP+lNJHPMCTGpEsG1NM9vi4FsxVlDl/
  PQSh99Bf5hU+tW42yA9lrxY6+0PiLIUOZZZoF1qwcW/ZjIayZgFw/6uJKLDyXnEf
  65RXKwv9JmTITBg9Z+FsXmeuRYPlLrMHAi+b4k5ThzMvxwj0NaOT+0fo4lccY1+g
  wqDgm9GxwHgzR36Ygt3l5y6DGVBeJ7dUBcjffrvGnuoSooSsE1hQ27uoo7qBFd5A
  FRQ+FnswiAtlkhuA06+fKPiIDItGSKZcxBk3Erc3GmETc/xOTwviFvn3NnCqAKcv
  ssD8mArFAoGBAOcS9a8TW1YVxs5lXKcWXpZhQJp/+FWPIe73iG3IGOupkycHJR/o
  SqaMr5VW2iYyHe/Wdl+l62sl/c1B13Gm9FvmGHCKdn3cRAxUe7muPTkrn7mnQk7b
  +VpfFMdGvCmutWveytvuMsyO1FWyo7EwVaDABNgLlmJXPYT7zFmpWpFvAoGBAMSt
  9Hl8N+5PqngC5cwFVvqfFnwd5DKB0Vem/yHfbA3ZerP2ouR84lK1LLV21IlVn7d9
  S+C/jaEqRIwg2RmB+xesq/hcLbk623KjWo67GI0sSI9RvOaEv/cn6iZ2PNVxqO+V
  x7wDS0LF185LDUT5H8sZnGd5GHAV3H40bKj7/7LbAoGAd2czryE4fDZKgdKc2vZT
  MC/W2z8vSr7okPw18kyInBYHJQ6rH17AXGmsPWYg2cXh9FIE3w30pWx/RrY0JtVX
  xbkTfev+qLDONPTe7ibcGhOxuJR2ThubMRiauxKRHpTZyIDaoGTouQ1BKXti3KeW
  R7eRiTYLootkDbizcs1xMF8CgYBp87A01dcnmWEi/6SdsO63WoZ5XHz3PHny9zKQ
  I9UI7PSuCBqebixaE+G+7AH47dh5t5TtwSNiA+wjdPqfRCdpDIsj4zx6gZuAeUGq
  jF3gh0+bbZQOtsrXMXhxOdbxwSYZ0x1jSn77vKrSJhpC4tXeyrA4CcNBdnVuw30Z
  1wVyawKBgQDPKTC24DaVJ6F/ucfLCxoHAubRPq4ZP+cukexC+4K39ARiB6z52yYv
  vhKI+96ENKt5FLFdl0skr4GF0W0mMpHuNspv02yuIPRJRR0I1iXWXKPIpP9UltV2
  0LKqdQSw5NU4tBQus6hCJ0ag+qEeEwHs9cLcMiyjRqTg3uvV9X/mDA==
  -----END RSA PRIVATE KEY-----
  |
```

# 其它说明
本项目使用了lombok，源码运行需满足相关要求