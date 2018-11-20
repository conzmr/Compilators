import sys
def main():
    parentheses = sys.argv[1]

    stack = []
    char_num = 0
    for parenthesis in parentheses:
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
