import distutils.spawn
import os
import platform
import traceback

# The following is a function for searching for an file on the current system
# Accepts String value of file_name, base directory to start search and if program is executable(True/False)
# Windows default(wbase) = 'C:\\' unix or Mac default(ubase) = '/'
# File executable default(exe) = False
# Returns path of file if found, and returns False if not found
# Example: file_present(file_name="workflow-cli.exe", wbase="C:\\Users\\", exe=True)


def file_present(file_name='not_passed', wbase='C:\\', ubase='/', exe=False):
    try:
        file_name = str(file_name)
        os_system = platform.system()
        if os_system == "Windows":
            base_search = wbase
        else:
            base_search = ubase

        # Loops through directories on system start from base search directory (C:\ for windows / for unix and Mac)
        for root, dirs, files in os.walk(base_search, topdown=False):
            for name in files:
                # If executable(exe) = True, checks if file is executable, else only checks for file
                if exe:
                    # If file name found and is an executable returns Path to File
                    if file_name == name and distutils.spawn.find_executable(file_name, path=root):
                        return root
                else:
                    # If file name found  returns Path to File
                    if file_name == name:
                        return root
        print(file_name + " not found")
        return False

    except Exception as e:
        print("Unexpected error: " + str(e))
        traceback.print_exc()
        raise Exception(e)