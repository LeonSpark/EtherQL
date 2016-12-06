EtherQL
----

EtherQL is a query layer for Ethereum blockchain, written in Java and backed by MongoDB. 

[![Build Status](https://travis-ci.org/LeonSpark/ethereumj-sql.svg?branch=master)](https://travis-ci.org/LeonSpark/ethereumj-sql)

It provides a RESTful API that is ideal for building scalable web applications or analyzing blockchain data.
EtherQL is designed to be fully compatible with [Ethereum](https://github.com/ethereum).

For much of the core protocol logic, EtherQL makes use of the [EthereumJ](https://github.com/ethereum/ethereumj)
library.

EtherQL was built at [Advanced Data Analytics Lab](http://ada.suda.edu.cn), with the goal of providing
a data query infrastructure on Ethereum. It is currently in very alpha beta, and not recommended for production use until it has received sufficient testing.

##Prerequisites
1. JDK 1.8.0 or later.
    TODO
2. Maven 3.0 or later.
    TODO
3. MongoDB 3.2 or later.
Current version of this project relies on [MongoDB](https://www.mongodb.com/) as the underlying data storage.
Install MongoDB according to this [documentation](https://docs.mongodb.com/manual/installation/). 

## Installation

Note: for spring developers, you can add EtherQL as a maven dependency by installing to your local maven repo.

`mvn install`

### Usage

* Add the maven dependency to your spring applications.

```
        <dependency>
            <groupId>edu.suda.ada</groupId>
            <artifactId>ethersql</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
```

* Create a bean to activate the EtherQL

`<bean id="starter" class="edu.suda.ada.config.AppConfig"/>`

## If you want to run a standalone application that just sync data from the blockchain.

At the root directory of this project.

Configuration

Add ethersql.conf file to your classpath to specify the Ethereum raw data location and MongoDB properties.
```
mongo.host=127.0.0.1
mongo.port=27017
mongo.db=test
default.db=mongo
data.location=/home/leon/data/ethereum
```

## License

EtherQL is licensed under [Apache License 2.0](http://www.apache.org/licenses/).
