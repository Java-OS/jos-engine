#!/bin/bash 

mvn compile package 
clear 
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 --module-path jos-core/target/lib -m jos.core ir.moke.jos.core.AppRunner 
