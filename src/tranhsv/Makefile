
SOURCES = tranhsv.c
TARGET  = tranhsv
OBJECTS = $(SOURCES:%.c=%.o)
CFLAGS	= -I../ubftab
LIBS = -latmisrvinteg -latmi -lubf -lnstd -lpthread -lrt -ldl -lm

default: target

target: $(TARGET)

$(TARGET): $(OBJECTS)
	$(CC) -o $@ $(LDFLAGS) $^ $(LIBS)

%.o: %.c
	$(CC) -c -o $@ $< $(CFLAGS)

clean:
	-rm *.o
	-rm $(TARGET)

