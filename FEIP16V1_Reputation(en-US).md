```
FEIP16: Reputation
Version: 1
Language: en-US
Author: C_armX, Deisler-JJ_Sboy
Status: draft
Created date: 2021-02-5
Last modified date：2022-12-20
PID: ""
TXID: 
```

# FEIP16V1_Reputation(en-US)

## Contents

[Introduction](#introduction)

[General rules of FEIP type protocols](#general-rules-of-feip-type-protocols)

[Rules specific to this protocol](#rules-specific-to-this-protocol)

[Example of positive rating](#example-of-positive-rating)

[Example of negative rating](#example-of-negative-rating)


## Introduction

```
Protocol type: FEIP
Serial number: 16
Protocol name: Reputation
Version: 1
Description : Evaluate an CID to increase/decrease its reputation.
Author: Deisler-JJ_Sboy，C_armX
Language: en-US
```

## General consensus of FEIP type protocols

1. Write important data in OP_RETURN for public witness under FEIP type protocols.

2. The SIGHASH flag of all transaction inputs is ‘ALL’ (value 0x01).

3. The max size of OP_RETURN : 4096 bytes.

4. The format of the data in op_return: JSON.

5. Encoding : utf-8.


## Consensus of this protocol

1. FEIP16 provides a way for an CID to evaluate other CIDs.
2. Rating someone on chain consumes CoinDays.
3. The rating can be Positive（“+”） or Negative("-"), and The measure unit of an rating is CoinDays. 
4. The quantity of an rating is all the CoinDays Destoryed(CDD) in the transaction.For example: "100 CoinDays Negative rating" can be marked as "-100cd", and "88 CoinDays positive rating" can be marked as "88cd" or "+88cd".
5. The rater is the first input address,and the ratee is the addrsee of the first output other than OP_RETURN output and rater.
6. Anyone can't rate itself.

## OP_RETURN

The OP_RETURN of which contains the data as follows:

|field number|field name|type|length|content|required|
|:----|:----|:----|:----|:----|:----|
|1|type|String|4|Fixed: "FEIP"|Y|
|2|sn|int|2|Serial number<br>Fixed: 16|Y|
|3|ver|int|1|Fixed: 1|Y|
|4|name|String|10|Fixed: "Reputation"|N|
|5|pid|hex|32|Sha256 value of this protocol file|N|
|6|data.sign|string|1|must be "+" or "-"|Y|


## Example of positive rating
```

First Input address: FPL44YJRwPdd2ipziFvqq6y2tw4VnVvpAv
First output address: FS2AWq1dgdhCpNTwqfBbMBBJGNNj1LSboy

OP_RETURN content:
{
    "type": "FEIP",
    "sn": 16,
    "ver": 1,
    "name": "Reputation",
    "pid": "",
    "data":{
        "sign": "+"
        }
}

```

## Example of negative rating
```

First Input address: FPL44YJRwPdd2ipziFvqq6y2tw4VnVvpAv
First output address: FS2AWq1dgdhCpNTwqfBbMBBJGNNj1LSboy

OP_RETURN content:

{
    "type": "FEIP",
    "sn": 3,
    "ver": 4,
    "name": "Reputation",
    "pid": "",
    "data":{
        "sign": "-"
        }
}

```