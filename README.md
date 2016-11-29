EthereumSQL
----

EthereumSQL is a query layer for Ethereum blockchain, written in Java and backed by MongoDB. 

[![Build Status](https://travis-ci.org/LeonSpark/ethereumj-sql.svg?branch=master)](https://travis-ci.org/LeonSpark/ethereumj-sql)

It provides a RESTful API that is ideal for building scalable web applications or analyzing blockchain data.
EthereumSQL is designed to be fully compatible with [Ethereum](https://github.com/ethereum).

For much of the core protocol logic, EthereumSQL makes use of the [EthereumJ](https://github.com/ethereum/ethereumj)
library.

EthereumSQL was built at [Advanced Data Analytics Lab](http://ada.suda.edu.cn), with the goal of providing
a data query infrastructure on Ethereum. It is currently in very alpha beta, and not recommended for production use until it has received sufficient testing.

## Installation

`mvn install`

## Usage

* Add the maven dependency to your spring applications.

```
<dependency>
            <groupId>edu.suda.ada</groupId>
            <artifactId>ethersql</artifactId>
            <version>1.0-SNAPSHOT</version>
            <exclusions>
                <exclusion>
                    <groupId>org.slf4j</groupId>
                    <artifactId>slf4j-log4j12</artifactId>
                </exclusion>
                <exclusion>
                    <groupId>log4j</groupId>
                    <artifactId>log4j</artifactId>
                </exclusion>
            </exclusions>
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
