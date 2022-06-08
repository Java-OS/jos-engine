### Current Implementation 
1. Kernel (linux) 
2. init (rust)
3. jos-engine (java) 

**Features :**
* jos-shell:> 
  * exit
  * echo 
  * module (install, list, delete, enable, disable, ...)
---
### Module Demo :    
[![asciicast](https://asciinema.org/a/O5bA5vu4IMXjeBtYgYEosDG0h.svg)](https://asciinema.org/a/O5bA5vu4IMXjeBtYgYEosDG0h)
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
   
2. **Services** 
   * HttpServer [com.sun.net.httpserver.HttpServer] (Admin panel)
   * Job scheduler [java.util.TimerTask] (Cron)

3. **Log Management**
   * Log [java.util.logging.Logger] (logs) 
--- 

### Modular System 
1. ~~OSGI~~
2. JPMS
