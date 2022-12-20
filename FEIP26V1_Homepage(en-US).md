```
FEIP26: Homepage
Version: 1
Language: en-US
Author: C_armX
Status: draft
Created date: 2022-10-02
Last modified date：2022-12-20
PID: unknown
TxID: Unpublished
```

# FEIP26V1_Homepage(en-US)

## Contents

[Introduction](#introduction)

[General rules of FEIP type protocols](#general-rules-of-feip-type-protocols)

[Rules specific to this protocol](#rules-specific-to-this-protocol)

[Register](#register)

[Unregister](#unregister)



## Introduction

```

Protocol type: FEIP
Serial number: 26
Protocol name: Homepage
Version: 1
Description : Register homepage path on the freecash blockchain for an address.
Author: C_armX
Language: en-US
Previous version PID:

```

## General consensus of FEIP type protocols

1. Write important data in OP_RETURN for public witness under FEIP type protocols.

2. The SIGHASH flag of all transaction inputs is ‘ALL’ (value 0x01).

3. The max size of OP_RETURN : 4096 bytes.

4. The format of the data in op_return: JSON.

5. Encoding : utf-8.


## Consensus of this protocol

1. An address can register one URL or more URLs as its homepag in op_return.

2. The homepage is only registered for the address of the first input.

3. When new homepage is registered, all the old ones are automatically unregistered.



## register

The OP_RETURN of which contains the data as follows:

|field number|field name|type|length|content|required|
|:----|:----|:----|:----|:----|:----|
|1|type|String|4|Fixed: "FEIP"|Y|
|2|sn|int|2|Serial number<br>Fixed: 26|Y|
|3|ver|int|1|Fixed: 1|Y|
|4|name|String|8|Fixed: "Homepage"<|N|
|5|pid|hex|32|Sha256 value of this protocol file|N|
|6|data.op|string|7|operation: "register" |Y|
|7|data.homepages|string array|512|URLs of the homepage|Y|

### Example
```

{
    "type": "FEIP",
    "sn": 26,
    "ver": 1,
    "name": "Homepage",
    "pid": "",
    "data":{
        "op": "register",
        "homepages": ["https://cid.cash/html/others/cid.html?cid=CY_vpAv","100.100.100.100:9300"]
        }
}
```
## Unregister

The OP_RETURN of which contains the data as follows:

|field number|field name|type|length|content|required|
|:----|:----|:----|:----|:----|:----|
|1|type|String|4|Fixed: "FEIP"|Y|
|2|sn|int|2|Serial number<br>Fixed: 26|Y|
|3|ver|int|1|Fixed: 1|Y|
|4|name|String|8|Fixed: "Homepage"|N|
|5|pid|hex|32|Sha256 value of this protocol file|N|
|6|data.op|string|9|operation: "unregister" |Y|

### Example
```
{
    "type": "FEIP",
    "sn": 26,
    "ver": 1,
    "name": "Homepage",
    "pid": "",
    "data":{
        "op": "unregister"
        }
}
```