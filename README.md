# FEIP Parser

## What's FEIP Parser

FEIP Parser is a program to parse FEIP orderData on freecash blockchain to Elasticsearch.

## Install

1. Installing FEIP parser only needs copy it to your device.

2. Before running FEIP parser，you have to sure freecash full node, Elasticsearch, java, and FreeChain was installed, running, and  got all orderData ready.

3. Get the path of opreturn0.byte. It is located in the path of FreeChain.

4. Run FEIP parser and follow the instructions.

## Indices and protocols

1. `cid` : Current status of addresses according to protocols: 
	*FEIP3V4_CID(en-US)*
	*FEIP4V1_Abandon(en-US)*
	*FEIP6V6_Master(en-US)*
	*FEIP9V1_Homepage(en-US)*
	*FEIP10V1_NoticeFee(en-US)*
	*FEIP16V1_Reputation(en-US)*
2. `cid_history`: All valid records of following protocols:
	*FEIP3V4_CID(en-US)*
	*FEIP4V1_Abandon(en-US)*
	*FEIP6V6_Master(en-US)*
	*FEIP9V1_Homepage(en-US)*
	*FEIP10V1_NoticeFee(en-US)*
3. `reputation_history`: All valid records of *FEIP16V1_Reputation(en-US)*.
4. `protocol`: Current status of protocols according to *FEIP1V7_FreeProtocol(en-US)*.
5. `protocol_history`: All valid records of *FEIP1V7_FreeProtocol(en-US)*.
6. `code`: Current status of codes according to *FEIP2V1_Code(en-US)*.
7. `code_history`: All valid records of *FEIP2V1_Code(en-US)*.
8. `service`: Current status of services according to *FEIP5V7_Service(en-US)*.
9. `service_history`: All valid records of *FEIP5V7_Service(en-US)*.
10. `app`: Current status of APPs according to *FEIP15V5_App(en-US)*.
11. `app_history`: All valid records of *FEIP15V5_App(en-US)*.
12. `concern`: Current valid concerns according to *FEIP12V3_Concern(en-US)*.
13. `mail`: Current valid mails according to *FEIP7V4_Mail(en-US)*.
14. `safe`: Current valid safe items according to *FEIP17V3_Safe(en-US)*.
15. `statement`:  Current valid statements according to *FEIP8V5_Statement(en-US)*.
16. `group`: Current status of groups according to *FEIP19V1_Group(en-US)*.
17. `group_history`: All valid records of *FEIP19V1_Group(en-US)*.
18. `team`: Current Current status of teams according to  *FEIP18V2_Team(en-US)*.
19. `team_history`: All valid records of *FEIP18V2_Team(en-US)*.
20. `parse_mark`: Only for parsing.

by No1_NrC7