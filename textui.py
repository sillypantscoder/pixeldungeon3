from getch import getch

def getbool(prompt: str, yesvalue: str = "Yes", novalue: str = "No") -> bool:
	selection = True
	while True:
		if selection: print(f"{prompt} \u001b[7m{yesvalue}\u001b[0m {novalue}")
		else: print(f"{prompt} {yesvalue} \u001b[7m{novalue}\u001b[0m")
		char = getch()
		if char == '\x1b': # Escape sequence:
			getch() # Consume the [ character
			newChar = getch()
			if newChar == 'D': # Left arrow
				selection = True
			elif newChar == 'C': # Right arrow
				selection = False
		elif char == '\r': # Enter
			return selection
		# Clear
		print("\r\u001b[1A\u001b[2K", end="")

def getboollist(prompt: str, items: list[str]) -> "list[bool]":
	selection = [True for _ in items]
	cursorPos = 0
	while True:
		print(prompt)
		# Print the items
		for i in range(len(items)):
			if i == cursorPos: print(f"\u001b[7m", end="")
			if selection[i]: print(f"\u001b[32m{items[i]}\u001b[0m")
			else: print(f"\u001b[31m{items[i]}\u001b[0m")
		# Get the input
		char = getch()
		if char == '\x1b':
			getch() # Consume the [ character
			newChar = getch()
			if newChar == 'A': # Up arrow
				cursorPos -= 1
				if cursorPos < 0: cursorPos = 0
			elif newChar == 'B': # Down arrow
				cursorPos += 1
				if cursorPos >= len(items): cursorPos = len(items) - 1
		elif char == '\r': # Enter
			return selection
		elif char == ' ': # Space
			selection[cursorPos] = not selection[cursorPos]
		# Clear
		print("\r\u001b[1A\u001b[2K", end="")
		for i in range(len(items)):
			print("\r\u001b[1A\u001b[2K", end="")

def getstr(prompt: str) -> str:
	current = ""
	cursorPos = 0
	while True:
		print(f"{prompt} {current if current != '' else ' '}", end="")
		if cursorPos != len(current): print(f"\u001b[{len(current) - cursorPos}D", end="")
		if len(current) == 0: print(f"\u001b[1D", end="")
		# Get the input
		char = getch()
		if char == '\x1b':
			getch() # Consume the [ character
			newChar = getch()
			if newChar == 'D': # Left arrow
				cursorPos -= 1
				if cursorPos < 0: cursorPos = 0
			elif newChar == 'C': # Right arrow
				cursorPos += 1
				if cursorPos > len(current): cursorPos = len(current)
		elif char == '\r': # Enter
			return current
		elif char == '\x7f': # Backspace
			if cursorPos > 0:
				current = current[:cursorPos - 1] + current[cursorPos:]
				cursorPos -= 1
		else:
			current = current[:cursorPos] + char + current[cursorPos:]
			cursorPos += 1
		# Clear
		print("\r\u001b[2K", end="")
