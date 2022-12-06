# Plan for Parsing FEIP Protocols into ES

## 开发顺序
	1. FEIP3V4_CID
	3. FEIP26V1_Homepage
	4. FEIP27V1_NoticeFee
	5. FEIP16V1_Reputation
	
	6. FEIP29V2_Service
	
	7. FEIP17V3_Safe
	8. FEIP7V4_MailOnChain
	9. FEIP12V3_Contacts
	10. FEIP8V5_Declaration
	
	11. FEIP15V5_APP
	12. FEIP19V1_Group

	14. FEIP1V7_FreeProtocol
	15. FEIP28V2_Organization
	16. FEIP32V1_HAT(zh-CN)
	17. FEIP33V1_Proof(en-US)
	18. FEIP31V1_OpenKnowledge(en-US)
## 库结构
### 原则
	1. cid相关库聚合
	2. 时序库独立
	3. 流水库独立
### 索引
	1. cid_info: cid,address,homepage,fee,reputation
	2. 
### 开发包按协议簇
	1. cid_info
	2. 单个进程顺序完成所有协议
### 进度
	1. 先完成cid相关