### Current Implementation 
1. Kernel (linux) 
2. init (rust)
3. jos-engine (java) 

**Features :**
* jos-shell:> 
  * exit
  * echo 
--- 
### TODO 
1. **CLI**
   * reboot & shutdown (JNI) 
   * restart JRE (Java)  
   * hostname (JNI)
   * Networking [ip ,Route] (JNI)
   * Timezone (Java)
   * Date and time (???) 
   * save & load (YAML)
   * install & remove & disable & enable 
   
2. **Services** 
   * HttpServer [com.sun.net.httpserver.HttpServer] (Admin panel)
   * Job scheduler [java.util.TimerTask] (Cron)

3. **Log Management**
   * Log [java.util.logging.Logger] (logs) 
--- 

### Modular System 
1. ~~OSGI~~
2. JPMS
