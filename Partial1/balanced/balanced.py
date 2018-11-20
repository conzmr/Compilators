import sys
import time

def main():
    file = sys.argv[1]
    stack = []
    char_num = 0
    with open(file,'r') as fileobj:
        space = False
        start_of_file = True
        for line in fileobj:
            for parenthesis in line:
                if parenthesis == '(':
                    stack.append(parenthesis)
                if parenthesis == ')':
                    if len(stack) == 0 or stack[len(stack)-1] != '(':
                        print("No balanceados.")
                    else:
                        stack.pop()
                char_num += 1
            if len(stack) > 0:
                print("No balanceados.")
            else:
                print("Balanceados.")


if __name__ == "__main__":
    main()
