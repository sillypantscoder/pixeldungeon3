from sys import stdout

class _Getch:
	"""Gets a single character from standard input.  Does not echo to the
screen."""
	def __init__(self):
		try:
			self.impl = _GetchWindows()
		except ImportError:
			self.impl = _GetchUnix()
	def __call__(self) -> str:
		stdout.flush()
		s = self.impl()
		if s == '\x03':
			# Ctrl-C
			raise KeyboardInterrupt
		return s

class _GetchUnix:
	def __init__(self):
		import tty, sys # type: ignore
	def __call__(self):
		import sys, tty, termios
		fd = sys.stdin.fileno()
		old_settings = termios.tcgetattr(fd)
		try:
			tty.setraw(sys.stdin.fileno())
			ch = sys.stdin.read(1)
		finally:
			termios.tcsetattr(fd, termios.TCSADRAIN, old_settings)
		return ch


class _GetchWindows:
	def __init__(self):
		import msvcrt # type: ignore
	def __call__(self) -> str:
		import msvcrt
		return msvcrt.getch() # type: ignore

getch = _Getch()
