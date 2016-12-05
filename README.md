EtherSQL
----

EtherSQL is a query layer for Ethereum blockchain, written in Java and backed by MongoDB. 

[![Build Status](https://travis-ci.org/LeonSpark/ethereumj-sql.svg?branch=master)](https://travis-ci.org/LeonSpark/ethereumj-sql)

It provides a RESTful API that is ideal for building scalable web applications or analyzing blockchain data.
EtherSQL is designed to be fully compatible with [Ethereum](https://github.com/ethereum).

For much of the core protocol logic, EtherSQL makes use of the [EthereumJ](https://github.com/ethereum/ethereumj)
library.

EtherSQL was built at [Advanced Data Analytics Lab](http://ada.suda.edu.cn), with the goal of providing
a data query infrastructure on Ethereum. It is currently in very alpha beta, and not recommended for production use until it has received sufficient testing.

## Installation
Under the root directory of the project, run the command below to install EtherSQL as a maven dependency.
`mvn install`

## Usage

* Add the maven dependency to your spring applications.

```
<dependency>
            <groupId>edu.suda.ada</groupId>
            <artifactId>ethersql</artifactId>
            <version>1.0-SNAPSHOT</version>
 </dependency>
```


* Create a bean to activate the EtherSQL

`<bean id="starter" class="edu.suda.ada.config.AppConfig"/>`

* Configuration

Add ethersql.conf file to your classpath to specify the Ethereum raw data location and MongoDB properties.
```
mongo.host=127.0.0.1
mongo.port=27017
mongo.db=test
default.db=mongo
data.location=/home/leon/data/ethereum
```

## License

EthereumSQL is licensed under [Apache License 2.0](http://www.apache.org/licenses/).
