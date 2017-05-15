import distutils.spawn
import os
import platform
import traceback

# The following is a function for searching for an executable program on the current system
# Accepts String value of program name and base directory to start search
# Windows default(wbase) = 'C:\\' unix or Mac default(ubase) = '/'
# Returns path of executable file if found, and returns False if not found
# Example: executable_present(program="workflow-cli", wbase="C:\\Users\\")


def executable_present(program='not_passed', wbase='C:\\', ubase='/'):
    try:
        program = str(program)
        os_system = platform.system()
        if os_system == "Windows":
            base_search = wbase
        else:
            base_search = ubase

        # Loops through directories on system start from base search directory (C:\ for windows / for unix and Mac)
        for root, dirs, files in os.walk(base_search, topdown=False):
            for name in files:
                # If program name found and is an executable returns Path to File
                if program in os.path.join(root, name) and distutils.spawn.find_executable(program, path=root):
                    return root
        print(program + " not found")
        return False

    except Exception as e:
        print("Unexpected error: " + str(e))
        traceback.print_exc()
        raise Exception(e)
