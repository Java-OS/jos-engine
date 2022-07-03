### Current Implementation 
1. Kernel (linux) 
2. init (rust)
3. jos-engine (java) 
    * slf4j + logback as default log managements
    * System basic commands 

**Features :**
* jos-shell:>
  * help 
  * exit
  * echo 
  * module (install, list, delete, enable, disable, ...)
  * environment [JNI] (set,get,unset)
  * reboot [JNI] & shutdown
  * Networking [ip ,Route] (JNI)
  
---
### Module Demo :    
[![asciicast](https://asciinema.org/a/O5bA5vu4IMXjeBtYgYEosDG0h.svg)](https://asciinema.org/a/O5bA5vu4IMXjeBtYgYEosDG0h)    

---
### Docker container:
```shell
docker pull mah454/jos:latest 
docker run -it --name=jos-demo --rm jos:latest
```

##### Note:     
We have a problem with init program (infinite loop) , so you **can not** exit from container with ```exit``` command ,
please use another console and kill container with this command :     
```shell
docker kill jos-demo
```
---
### TODO 
1. **CLI**   
   * hostname (JNI)
   * Timezone (Java)
   * Date and time (???) 
   * save & load (YAML)
   
2. **Services** 
   * HttpServer [com.sun.net.httpserver.HttpServer] (Admin panel)
   * Job scheduler [java.util.TimerTask] (Cron)

--- 

### Modular System 
1. ~~OSGI~~
2. JPMS
