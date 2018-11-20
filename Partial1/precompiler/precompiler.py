import sys
import time

def main():
    file = sys.argv[1]
    with open(file,'r') as fileobj:
        print("Abriendo archivo: "+file)
        space = False
        start_of_file = True
        for line in fileobj:
            for ch in line:
                if ch and ch.strip():
                    if start_of_file:
                        start_of_file = False
                    if space:
                        space = False
                        print ' '+ch,
                    else:
                        print ch,
                else:
                    if not start_of_file:
                        space = True
                sys.stdout.flush()
                time.sleep(.1)
        print("\n<EOF>")
        print("Cerrando archivo")


if __name__ == "__main__":
    main()
