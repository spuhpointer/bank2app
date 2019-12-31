export GOPATH=$(shell pwd)/../

all:
	go get github.com/endurox-dev/endurox-go
	make -C ubftab
	make -C creditsv
	make -C debitsv
	make -C tranhsv
	make -C txupload
	
clean:
	make -C ubftab clean
	make -C creditsv clean
	make -C debitsv clean
	make -C tranhsv clean
	make -C txupload clean


.PHONY: all clean


