# MTA spec schema
This directory structure contains the [Multi-Target Application (MTA)](https://www.sap.com/documents/2016/06/e2f618e4-757c-0010-82c7-eda71af511fa.html) specification schema files.

# How to test after a change?
1. Install [kwalify](http://www.kuwata-lab.com/kwalify/):
```
gem install kwalify
```
2. Run:
```
kwalify -f <schema> <descriptor>
```
