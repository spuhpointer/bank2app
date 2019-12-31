# bank2app
Enduro/X Demonstration app

This application shows how to use Enduor/X Middleware to combine in single application code made in C, Go and Java programming languages. The program is based on multi-processing principles.

Application consits of following binaries:

- *txupload* Java, jar based program which generates transactions. To get some high TPS, several copies of the generators are started.

- *tranhsv* C program, XATMI server which calls the debit and credit side of the transaction.

- *debitsv* Java program (compiled into binary), XATMI server, performs simulation of transaction debit operation.

- *creditsv* Go program, XATMI server, simulates transaction credit side.

The application is monitored by NetXMS monitoring suite.
