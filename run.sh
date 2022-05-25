#!/bin/bash 

mvn compile package 
clear 
java --module-path jos-core/target/lib -m jos.core ir.moke.jos.core.AppRunner 
