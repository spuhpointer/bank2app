# bank2app
Enduro/X Demonstration app

This application shows how to use Enduor/X Middleware to combine in single application code made in C, Go and Java programming languages. The program is based on multi-processing principles.

Application consits of following binaries:

- *txupload* Java, jar based program which generates transactions. To get some high TPS, several copies of the generators are started.
- *tranhsv* C program, XATMI server which calls the debit and credit side of the transaction.
- *debitsv* Java program (compiled into binary), XATMI server, performs simulation of transaction debit operation.
- *creditsv* Go program, XATMI server, simulates transaction credit side.

The application is monitored by NetXMS monitoring suite. Program is installed for "user1" user.


## Getting up and running

- Install Enduro/X, Enduro/X Java, Go, Java JDK and C/C++ compilers
- Install NetXMS agent
- Get the source and build demo app
- Provision the runtime and run

### Install the NetXMS agent Enduro/X support

```
$ git clone https://github.com/netxms/netxms
$ cd netxms
$ ./reconf
$ ./configure --with-agent --prefix=/opt/nxagent --with-tuxedo=/usr --disable-mqtt
$ make
$ sudo make install
$ sudo chown -R user1:user1 /opt/nxagent

```

After installing the binaries, needs to add the configuration for the agent:

```
$ sudo -s
$ cat << EOF > /etc/nxagentd.conf

# Log File
LogFile=/home/user1/projects/bank2app/log/nxagentd.log

# IP white list, can contain multiple records separated by comma.
# CIDR notation supported for subnets.
MasterServers=testhost
ServerConnection=testhost
SubAgent=tuxedo.nsm

EOF

$ sudo chmod a+r /etc/nxagentd.conf
```

add "testhost" in your /etc/hosts file to point your ip address, do not use 127.0.0.1, but some real network interface IP.


### Get the source and build demo app

App will be installed into /home/user1/projects/bank2app, so lets go:

```
$ mkdir /home/user1/projects
$ cd /home/user1/projects
$ git clone https://github.com/spuhpointer/bank2app
```

Next update the Java JDK pathes in following files (so that build can complete):

- /home/user1/projects/bank2app/src/debitsv/Makefile

Next step is to build the whole solution:

```
$ cd /home/user1/projects/bank2app
$ make
```

Once all is bulit we may start the app.

### Provision and run

Will use default settings for provisioning the runtime:

```
$ cd /home/user1/projects/bank2app
$ xadmin provision -d
```

Change log levels before start, as there is very high tps set via many clients started:

- Update: /home/user1/projects/bank2app/conf/app.ini

Have something like this set:

```
[@debug]
; * - goes for all binaries not listed bellow
*= ndrx=2 ubf=1 tp=2 file=
xadmin= ndrx=5 ubf=1 tp=5 file=${NDRX_APPHOME}/log/xadmin.log
ndrxd= ndrx=5 ubf=1 tp=5 file=${NDRX_APPHOME}/log/ndrxd.log
```

- Update environment file to include library paths to JDK install, for example (added last line) to */home/user1/projects/bank2app/conf/settest1*:

```
$ cat settest1 
#/bin/bash
#
# @(#) Load this script in environment
#
export NDRX_APPHOME=/home/user1/projects/bank2app
export NDRX_CCONFIG=$NDRX_APPHOME/conf
export PATH=$PATH:$NDRX_APPHOME/bin
export CDPATH=$CDPATH:.:$NDRX_APPHOME
export LD_LIBRARY_PATH=/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64:/usr/lib/jvm/java-8-openjdk-amd64/jre/lib/amd64/server
```

boot the app:

```
$ cd /home/user1/projects/bank2app/conf
$ source settest1
$ xadmin start -y
...
exec cpmsrv -k 0myWI5nu -i 9999 -e /home/user1/projects/bank2app/log/cpmsrv.log -r -- -k3 -i1 --  :
	process id=22731 ... Started.
Startup finished. 105 processes started.
NDRX 1> 
```

Check the status of clients:

```
$ xadmin pc
...
TXUPLD/49 - running pid 23817 (Tue Dec 31 17:38:41 2019)
TXUPLD/50 - running pid 23818 (Tue Dec 31 17:38:41 2019)
NXAGENT/- - running pid 24802 (Tue Dec 31 17:40:18 2019)

```

## NetXMS dashbord for running system

The demo application is monitored by NetXMS suite, the Data collection items can be seen here: 

- doc/NetXMS-DCI-Demo.csv

Differnet configuration are possible, but for demo purposes following dashbord is created:

![Alt text](doc/img/netxms-demo.png?raw=true "NetXMS Dashbord")