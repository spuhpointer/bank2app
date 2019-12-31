# bank2app
Enduro/X Demonstration app

This application shows how to use Enduor/X Middleware to combine in single application code made in C, Go and Java programming languages. The program is based on multi-processing principles.

Application consits of following binaries:

- *txupload* Java, jar based program which generates transactions. To get some high TPS, several copies of the generators are started.
- *tranhsv* C program, XATMI server which calls the debit and credit side of the transaction.
- *debitsv* Java program (compiled into binary), XATMI server, performs simulation of transaction debit operation.
- *creditsv* Go program, XATMI server, simulates transaction credit side.

The application is monitored by NetXMS monitoring suite.


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
```

After installing the binaries, needs to add the configuration for the agent:

```
$ sudo -s
$ cat << EOF > /etc/nxagentd.conf

# Log File
LogFile=/var/log/nxagentd

# IP white list, can contain multiple records separated by comma.
# CIDR notation supported for subnets.
MasterServers=testhost
ServerConnection=testhost
SubAgent=tuxedo.nsm

EOF
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


