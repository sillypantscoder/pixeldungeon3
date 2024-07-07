import textui
import os
import subprocess

# 1. Get the files to compile

n_java = 0
files: "list[str]" = []
def checkfolder(folder: str):
	global n_java
	for file in os.listdir(folder):
		newname = os.path.join(folder, file)
		if os.path.isdir(newname):
			checkfolder(newname)
		else:
			if newname.endswith(".java"): n_java += 1
			files.append(newname)
checkfolder("src")

# 2. Compile the files

if textui.getbool(f"Do you want to compile all {len(files)} files ({n_java} Java files, {len(files) - n_java} resource files)?"):
	print("Compiling all files...")
	os.makedirs("compiled_output", exist_ok=True)
	cmd = ["javac", "-g", "-d", "compiled_output", "-cp", "src"]
	for filename in files:
		if filename.endswith(".java"):
			cmd.append(filename)
		else:
			subprocess.run(["mkdir", "-p", "compiled_output/" + filename[4:filename.rfind("/")]])
			subprocess.run(["cp", filename, "compiled_output/" + filename[4:]])
	subprocess.run(cmd)
else:
	toc = textui.getboollist("Which files do you want to compile?", files)
	print("Compiling selected files...")
	cmd = ["javac", "-g", "-d", "compiled_output", "-cp", "src"]
	for fileno in range(len(files)):
		if not toc[fileno]: continue
		filename = files[fileno]
		if filename.endswith(".java"):
			cmd.append(filename)
		else:
			subprocess.run(["cp", filename, "compiled_output/" + filename])
	cmd.extend([files[i] for i in range(len(files)) if toc[i]])
	subprocess.run(cmd)

# 3. Zip the files

print("Saving...")
f = open("compiled_output/manifest", "w")
f.write(f"""Manifest-Version: 1.0
Created-By: 17.0.3 (GraalVM Community)
Main-Class: com.sillypantscoder.pixeldungeon3.Main

""")
f.close()
subprocess.run(["jar", "-c", "-v", "-f", "compiled.jar", "-m", "compiled_output/manifest", "-C", "compiled_output/", "."])
subprocess.run(["rm", "-r", "compiled_output"])
print("Running...")
subprocess.run(["java", "-jar", "compiled.jar"])
